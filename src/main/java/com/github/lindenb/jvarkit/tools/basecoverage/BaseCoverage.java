/*
The MIT License (MIT)

Copyright (c) 2022 Pierre Lindenbaum

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

*/
package com.github.lindenb.jvarkit.tools.basecoverage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;
import com.github.lindenb.jvarkit.bed.BedLineReader;
import com.github.lindenb.jvarkit.io.IOUtils;
import com.github.lindenb.jvarkit.lang.StringUtils;
import com.github.lindenb.jvarkit.samtools.SAMRecordDefaultFilter;
import com.github.lindenb.jvarkit.util.JVarkitVersion;
import com.github.lindenb.jvarkit.util.bio.SequenceDictionaryUtils;
import com.github.lindenb.jvarkit.util.bio.fasta.ContigNameConverter;
import com.github.lindenb.jvarkit.util.iterator.EqualRangeIterator;
import com.github.lindenb.jvarkit.util.jcommander.Launcher;
import com.github.lindenb.jvarkit.util.jcommander.Program;
import com.github.lindenb.jvarkit.util.log.Logger;
import com.github.lindenb.jvarkit.util.picard.AbstractDataCodec;
import com.github.lindenb.jvarkit.variant.variantcontext.writer.WritingVariantsDelegate;

import htsjdk.samtools.AlignmentBlock;
import htsjdk.samtools.QueryInterval;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SAMSequenceDictionary;
import htsjdk.samtools.SamReader;
import htsjdk.samtools.SamReaderFactory;
import htsjdk.samtools.reference.ReferenceSequenceFile;
import htsjdk.samtools.reference.ReferenceSequenceFileFactory;
import htsjdk.samtools.util.CloseableIterator;
import htsjdk.samtools.util.IOUtil;
import htsjdk.samtools.util.SortingCollection;
import htsjdk.samtools.util.StopWatch;
import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.Genotype;
import htsjdk.variant.variantcontext.GenotypeBuilder;
import htsjdk.variant.variantcontext.VariantContextBuilder;
import htsjdk.variant.variantcontext.writer.VariantContextWriter;
import htsjdk.variant.vcf.VCFConstants;
import htsjdk.variant.vcf.VCFHeader;
import htsjdk.variant.vcf.VCFHeaderLine;
import htsjdk.variant.vcf.VCFHeaderLineType;
import htsjdk.variant.vcf.VCFInfoHeaderLine;
import htsjdk.variant.vcf.VCFStandardHeaderLines;

/**
BEGIN_DOC

## Input

input is a set of bam files or one file with the '.list' suffix containing the path to the bams

## Example:

```
$ java -jar dist/basecoverage.jar --bed jeter.bed -R src/test/resources/rotavirus_rf.fa src/test/resources/S*.bam 

lindenb@asimov:~/src/jvarkit$ java -jar dist/basecoverage.jar --bed jeter.bed -R src/test/resources/rotavirus_rf.fa src/test/resources/S*.bam 

##fileformat=VCFv4.2
##FORMAT=<ID=DP,Number=1,Type=Integer,Description="Approximate read depth (reads with MQ=255 or with bad mates are filtered)">
##INFO=<ID=DP,Number=1,Type=Integer,Description="Approximate read depth; some reads may have been filtered">
##basecoverage.meta=compilation:20220420110509 githash:afbe74ab2 htsjdk:2.24.1 date:20220420110554 cmd:--bed jeter.bed -R src/test/resources/rotavirus_rf.fa src/test/resources/S1.bam src/test/resources/S2.bam src/test/resources/S3.bam src/test/resources/S4.bam src/test/resources/S5.bam
##contig=<ID=RF01,length=3302>
##contig=<ID=RF02,length=2687>
##contig=<ID=RF03,length=2592>
##contig=<ID=RF04,length=2362>
##contig=<ID=RF05,length=1579>
##contig=<ID=RF06,length=1356>
##contig=<ID=RF07,length=1074>
##contig=<ID=RF08,length=1059>
##contig=<ID=RF09,length=1062>
##contig=<ID=RF10,length=751>
##contig=<ID=RF11,length=666>
#CHROM	POS	ID	REF	ALT	QUAL	FILTER	INFO	FORMAT	S1	S2	S3	S4	S5
RF03	491	.	N	.	.	.	DP=28	DP	3	6	6	8	5
RF03	492	.	N	.	.	.	DP=27	DP	3	5	5	8	6
RF03	493	.	N	.	.	.	DP=27	DP	3	5	5	8	6
RF03	494	.	N	.	.	.	DP=24	DP	3	4	4	8	5
RF03	495	.	N	.	.	.	DP=24	DP	3	4	4	8	5
RF03	496	.	N	.	.	.	DP=24	DP	3	4	4	8	5
RF03	497	.	N	.	.	.	DP=24	DP	3	4	4	8	5
RF03	498	.	N	.	.	.	DP=24	DP	3	4	4	8	5
RF03	499	.	N	.	.	.	DP=25	DP	3	4	4	9	5
RF03	500	.	N	.	.	.	DP=23	DP	3	3	3	9	5


```

END_DOC
 */
@Program(name="basecoverage",
	description="'Depth of Coverage' per base.",
	keywords={"depth","bam","sam","coverage","vcf"},
	creationDate="20220420",
	modificationDate="20220420"
	)
public class BaseCoverage extends Launcher
	{
	private static Logger LOG=Logger.build(BaseCoverage.class).make();

	
	@Parameter(names={"-R","--reference"},description=INDEXED_FASTA_REFERENCE_DESCRIPTION,required = true)
	private Path faidx = null;
	@Parameter(names={"-B","--bed"},description="optional bed containing regions to be SCANNED)")
	private Path bedInput = null;
	@Parameter(names={"--mapq"},description=" min mapping quality.")
	private int mapping_quality=1;
	@Parameter(names={"-o","--out"},description=OPT_OUPUT_FILE_OR_STDOUT)
	private Path outputFile=null;
	@ParametersDelegate
	private WritingVariantsDelegate writingVariantsDelegate = new WritingVariantsDelegate();
	@ParametersDelegate
	private WritingSortingCollection writingSortingCollection = new WritingSortingCollection();
	
	
	private static class Base {
		int tid;
		int pos;
		int sample_idx;
		int depth;
		int compare2(final Base o) {
			int i = Integer.compare(this.tid, o.tid);
			if(i!=0) return i;
			i = Integer.compare(this.pos, o.pos);
			if(i!=0) return i;
			return 0;
		}
		int compare1(final Base o) {
			int i = compare2(o);
			if(i!=0) return i;
			i = Integer.compare(this.sample_idx, o.sample_idx);
			return i;
		}
	}
	
	private static class BaseCodec extends AbstractDataCodec<Base> {
		@Override
		public Base decode(DataInputStream dis) throws IOException {
			final Base b = new Base();
			try {
				b.tid =dis.readInt();
				}
			catch(final EOFException err) {
				return null;
				}
			b.pos = dis.readInt();
			b.sample_idx = dis.readInt();
			b.depth = dis.readInt();
			return b;
			}
		@Override
		public void encode(final DataOutputStream o, final Base b) throws IOException {
			o.writeInt(b.tid);
			o.writeInt(b.pos);
			o.writeInt(b.sample_idx);
			o.writeInt(b.depth);
			}
		@Override
		public BaseCodec clone() {
			return new BaseCodec();
			}
		}

	
	
	@Override
	public int doWork(final List<String> args)
		{
		SortingCollection<Base>	sorting=null;
		try
			{
			final List<Path> bamsIn = IOUtils.unrollPaths(args);
			if(bamsIn.isEmpty()) {
				LOG.error("input is empty.");
				return -1;
				}
			
			final SAMSequenceDictionary dict = SequenceDictionaryUtils.extractRequired(this.faidx);
			final QueryInterval[] intervalList;

			if(this.bedInput!=null) {
				try(BedLineReader blr = new BedLineReader(this.bedInput)) {
					blr.setContigNameConverter(ContigNameConverter.fromOneDictionary(dict));
					intervalList = blr.optimizeIntervals(dict);
				}
			} else //add whole genome
			{
				intervalList = null;
			}
			
			final SamReaderFactory srf = super.createSamReaderFactory().
					referenceSequence(this.faidx);
			
			final List<String> samples = new ArrayList<>(bamsIn.size());
			final Map<String, Integer> sample2idx = new HashMap<>();
			
			sorting = SortingCollection.newInstance(
					Base.class,
					new BaseCodec(),
					(A,B)->A.compare1(B),
					this.writingSortingCollection.getMaxRecordsInRam(),
					this.writingSortingCollection.getTmpPaths()
					);
			sorting.setDestructiveIteration(true);
			
			long millisec_per_samples= 0L;
			int sam_idx=0;
			for(final Path bamPath: bamsIn) {
				final StopWatch stopWatch = new StopWatch();
				stopWatch.start();
				IOUtil.assertFileIsReadable(bamPath);
				LOG.info("scanning "+bamPath+" "+(++sam_idx)+"/" + bamsIn.size());
				try(SamReader sr = srf.open(bamPath)) {
					if(!sr.hasIndex()) {
						throw new IllegalArgumentException("bam "+bamPath+" is not indexed");
						}
					final SAMFileHeader header = sr.getFileHeader();
					final String sn = header.getReadGroups().stream().
							map(S->S.getSample()).
							filter(S->!StringUtils.isBlank(S)).
							findFirst().
							orElse(IOUtils.getFilenameWithoutCommonSuffixes(bamPath));
					if(sample2idx.containsKey(sn)) {
						LOG.error("duplicate sample "+sn+" in bamPath");
						return -1;
					}
					final int sample_idx = samples.size();
					sample2idx.put(sn, sample_idx);
					samples.add(sn);
					
					int prev_tid = -1;
					int prev_pos = 0;
					final TreeMap<Integer, Integer> pos2depth = new TreeMap<>();
					try(CloseableIterator<SAMRecord> it= intervalList==null?sr.iterator():sr.query(intervalList, false)) {
						for(;;) {
							final SAMRecord rec = it.hasNext()?it.next():null;
							if(rec!=null && !SAMRecordDefaultFilter.accept(rec, this.mapping_quality)) continue;
							
							if(rec==null || prev_tid!=rec.getReferenceIndex()) {
								for(final Integer pos:pos2depth.keySet()) {
				            		final Base b = new Base();
				            		b.sample_idx = sample_idx;
				            		b.tid = prev_tid;
				            		b.pos = pos;
				            		b.depth = pos2depth.get(b.pos);
				            		sorting.add(b);
									}
								if(rec==null) break;
								pos2depth.clear();
								}
							prev_tid = rec.getReferenceIndex();
							prev_pos = rec.getAlignmentStart();
							for(Iterator<Integer> rpos = pos2depth.keySet().iterator();rpos.hasNext();) {
								final int pos = rpos.next();
								if(pos>=prev_pos) break;
			            		final Base b = new Base();
			            		b.sample_idx = sample_idx;
			            		b.tid = prev_tid;
			            		b.pos = pos;
			            		b.depth = pos2depth.get(b.pos);
			            		sorting.add(b);
			            		rpos.remove();
								}
							for(AlignmentBlock ab:rec.getAlignmentBlocks()) {
								for(int n=0;n<ab.getLength();n++) {
									final int ref= ab.getReferenceStart() + n;
									pos2depth.put(ref, 1 + pos2depth.getOrDefault(ref, 0));
									}
								}
							}
						}
					} // end SAMReader
				stopWatch.stop();
				millisec_per_samples+= stopWatch.getElapsedTime();
				LOG.info("That took "+StringUtils.niceDuration(stopWatch.getElapsedTime())+
						". Elapsed:"+
						StringUtils.niceDuration(millisec_per_samples)+" Remains:"+
						StringUtils.niceDuration((long)((millisec_per_samples/(double)sam_idx))*(bamsIn.size()-sam_idx)));
				}//end for each bam
			sorting.doneAdding();
			
			try(VariantContextWriter w = this.writingVariantsDelegate.dictionary(dict).open(this.outputFile);
				ReferenceSequenceFile fasta = ReferenceSequenceFileFactory.getReferenceSequenceFile(this.faidx)) {
			final Set<VCFHeaderLine> metaData = new HashSet<>();
			final VCFInfoHeaderLine infoMeanDP = new VCFInfoHeaderLine("AVG_DP",1,VCFHeaderLineType.Float,"average DP");
			final VCFInfoHeaderLine infoMinDP = new VCFInfoHeaderLine("MIN_DP",1,VCFHeaderLineType.Integer,"min DP");
			final VCFInfoHeaderLine infoMaxDP = new VCFInfoHeaderLine("MAX_DP",1,VCFHeaderLineType.Integer,"max DP");
			metaData.add(infoMeanDP);
			metaData.add(infoMinDP);
			metaData.add(infoMaxDP);
			VCFStandardHeaderLines.addStandardFormatLines(metaData, true, VCFConstants.DEPTH_KEY);
			VCFStandardHeaderLines.addStandardInfoLines(metaData, true, VCFConstants.DEPTH_KEY);
			final VCFHeader header = new VCFHeader(metaData,samples);
			header.setSequenceDictionary(dict);
			JVarkitVersion.getInstance().addMetaData(this, header);
			w.writeHeader(header);
			try(CloseableIterator<Base> iter0= sorting.iterator()) {
				final EqualRangeIterator<Base> iter1 = new EqualRangeIterator<>(iter0, (A,B)->A.compare2(B));
				while(iter1.hasNext()) {
					final List<Base> array = iter1.next();
					final Base first = array.get(0);
					final Map<String, Integer> sample2depth = new HashMap<>(samples.size());
					array.stream().forEach(B->sample2depth.put(samples.get(B.sample_idx), B.depth));
					
					final List<Genotype> genotypes = new ArrayList<>(samples.size());
					for(final String sn:samples) {
						final GenotypeBuilder gb=new GenotypeBuilder(sn);
						gb.DP(sample2depth.getOrDefault(sn, 0));
						genotypes.add(gb.make());
					}
					final String contig = dict.getSequence(first.tid).getContig();
					final Allele ref_allele = Allele.create(fasta.getSubsequenceAt(contig, first.pos, first.pos).getBases(), true);
					final VariantContextBuilder vcb = new VariantContextBuilder(
							null,
							contig,
							first.pos,
							first.pos,
							Collections.singletonList(ref_allele)
							);
					vcb.genotypes(genotypes);
					
					vcb.attribute(VCFConstants.DEPTH_KEY, samples.stream().mapToInt(S->sample2depth.getOrDefault(S, 0)).sum());
					vcb.attribute(infoMeanDP.getID(), samples.stream().mapToInt(S->sample2depth.getOrDefault(S, 0)).average().orElse(0.0));
					vcb.attribute(infoMinDP.getID(), samples.stream().mapToInt(S->sample2depth.getOrDefault(S, 0)).min().orElse(0));
					vcb.attribute(infoMaxDP.getID(), samples.stream().mapToInt(S->sample2depth.getOrDefault(S, 0)).max().orElse(0));
					w.add(vcb.make());
					}
				iter1.close();
				}//end iter0
			}
			
			return 0;
			}
		catch(final Throwable err)
			{
			LOG.error(err);
			return -1;
			}
		finally
			{
			}
		}

	public static void main(final String[] args)
		{
		new BaseCoverage().instanceMainWithExit(args);
		}		

}
