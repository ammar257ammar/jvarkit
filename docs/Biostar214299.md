# Biostar214299

![Last commit](https://img.shields.io/github/last-commit/lindenb/jvarkit.png)

Extract allele specific reads from bamfiles


## Usage

```
Usage: java -jar dist/biostar214299.jar  [options] Files
Usage: biostar214299 [options] Files
  Options:
    --bamcompression
      Compression Level. 0: no compression. 9: max compression;
      Default: 5
    -h, --help
      print help and exit
    --helpFormat
      What kind of help. One of [usage,markdown,xml].
    -o, --out
      Output file. Optional . Default: stdout
  * -p, --positions
      Position file. A Tab delimited file containing the following 4 column: 
      (1)chrom (2)position (3) allele A/T/G/C (4) sample name.
    -R, --reference
      Indexed fasta Reference file. This file must be indexed with samtools 
      faidx and with picard CreateSequenceDictionary
    --regions
      Limit analysis to this interval. A source of intervals. The following 
      suffixes are recognized: vcf, vcf.gz bed, bed.gz, gtf, gff, gff.gz, 
      gtf.gz.Otherwise it could be an empty string (no interval) or a list of 
      plain interval separated by '[ \t\n;,]'
    --samoutputformat
      Sam output format.
      Default: SAM
      Possible Values: [BAM, SAM, CRAM]
    --validation-stringency
      SAM Reader Validation Stringency
      Default: LENIENT
      Possible Values: [STRICT, LENIENT, SILENT]
    --version
      print version and exit

```


## Keywords

 * sam
 * bam
 * variant
 * snp



## See also in Biostars

 * [https://www.biostars.org/p/214299](https://www.biostars.org/p/214299)


## Compilation

### Requirements / Dependencies

* java [compiler SDK 11](https://jdk.java.net/11/). Please check that this java is in the `${PATH}`. Setting JAVA_HOME is not enough : (e.g: https://github.com/lindenb/jvarkit/issues/23 )


### Download and Compile

```bash
$ git clone "https://github.com/lindenb/jvarkit.git"
$ cd jvarkit
$ ./gradlew biostar214299
```

The java jar file will be installed in the `dist` directory.


## Creation Date

20160930

## Source code 

[https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/biostar/Biostar214299.java](https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/biostar/Biostar214299.java)

### Unit Tests

[https://github.com/lindenb/jvarkit/tree/master/src/test/java/com/github/lindenb/jvarkit/tools/biostar/Biostar214299Test.java](https://github.com/lindenb/jvarkit/tree/master/src/test/java/com/github/lindenb/jvarkit/tools/biostar/Biostar214299Test.java)


## Contribute

- Issue Tracker: [http://github.com/lindenb/jvarkit/issues](http://github.com/lindenb/jvarkit/issues)
- Source Code: [http://github.com/lindenb/jvarkit](http://github.com/lindenb/jvarkit)

## License

The project is licensed under the MIT license.

## Citing

Should you cite **biostar214299** ? [https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md](https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md)

The current reference is:

[http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)

> Lindenbaum, Pierre (2015): JVarkit: java-based utilities for Bioinformatics. figshare.
> [http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)


The program removes all the existing read group and create some new one from the 'position file'.
For now, only simple alleles are supported.
Reads group are affected if a specific variant is found in the 'position file'.
If two samples share the same group, the read group is AMBIGOUS.
If the read is unmapped, the read group is UNMAPPED.
If no sample is affected to a read, the read group will be UNAFFECTED;

## see also:

* [https://www.biostars.org/p/283969](https://www.biostars.org/p/283969)  " How to extract reads with a known variant form a bam file"


## Example

the positions file

```
$ cat positions.tsv
rotavirus       267     C       SAMPLE1
rotavirus       267     G       SAMPLE2
```

processing :

```
$ java -jar dist/biostar214299.jar -p positions.tsv input.bam

@HD     VN:1.5  SO:coordinate
@SQ     SN:rotavirus    LN:1074
@RG     ID:UNAFFECTED   SM:UNAFFECTED   LB:UNAFFECTED
@RG     ID:UNMAPPED     SM:UNMAPPED     LB:UNMAPPED
@RG     ID:SAMPLE1      SM:SAMPLE1      LB:SAMPLE1
@RG     ID:SAMPLE2      SM:SAMPLE2      LB:SAMPLE2
@RG     ID:AMBIGOUS     SM:AMBIGOUS     LB:AMBIGOUS
(...)
rotavirus_237_744_6:0:0_3:0:0_29c       163     rotavirus       237     60      70M     =       675     508     ATCCGGCGTTAAATGGAAAGTTTCGGTGATCTATTAGAAATAGAAATTGGATGACTGATTCAAAAACGGT  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++      MD:Z:3A19A1C1C1G31T8    RG:Z:SAMPLE1    NM:i:6  AS:i:41 XS:i:0
rotavirus_234_692_6:0:1_4:0:0_3ac       163     rotavirus       237     60      6S30M5I1M5D28M  =       623     456     TTGGTAATCAGGCGTTAAATGGAAAGTTTAGCTCAGGACAACGAAATAGAAATTGGATGACTGATTCTAA  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++      MD:Z:31^TATTA28 RG:Z:SAMPLE2    NM:i:10 AS:i:37 XS:i:0
rotavirus_237_777_6:0:0_7:0:0_216       99      rotavirus       237     60      70M     =       708     541     ATCAGGGGTTAAATTGAAAGTTTAGCTCAGCTCTTAGACATAGAAATTGGATGACTGATTGTACAACGGT  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++      MD:Z:6C7G17A5A21C2A6    RG:Z:SAMPLE1    NM:i:6  AS:i:40 XS:i:0
rotavirus_237_699_3:0:0_8:0:0_22f       163     rotavirus       237     60      70M     =       650     463     ATGAGGCGTTAAATGGAAAGTTTATCTCAGCTATTAGAAATAGCAATTGGATGACTGATTCTAAAACGGT  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++      MD:Z:2C21G18A26 RG:Z:SAMPLE1    NM:i:3  AS:i:57 XS:i:0
(...)
rotavirus_311_846_10:0:0_11:0:0_3d7     141     *       0       0       *       *       0       0       AACTTAGATGAAGACGATCAAAACCTTAGAATGACTTTATGTTCTAAATGGCTCGACCCAAAGATGAGAG  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++      RG:Z:UNMAPPED   AS:i:0  XS:i:0
rotavirus_85_600_7:0:0_9:0:0_3e0        77      *       0       0       *       *       0       0       AGCTGCAGTTGTTTCTGCTCCTTCAACATTAGAATTACTGGGTATTGAATATGATTCCAATGAAGTCTAT  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++      RG:Z:UNMAPPED   AS:i:0  XS:i:0
rotavirus_85_600_7:0:0_9:0:0_3e0        141     *       0       0       *       *       0       0       TATTTCTCCTTAAGCCTGTGTTTTATTGCATCAAATCTTTTTTCAAACTGCTCATAACGAGATTTCCACT  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++      RG:Z:UNMAPPED   AS:i:0  XS:i:0
```

## Cited In

 * Anatomy, transcription dynamics and evolution of wheat ribosomal RNA loci deciphered by a multi-omics approach.  https://doi.org/10.1101/2020.08.29.273623
 * Reciprocal allopolyploid grasses (Festuca × Lolium) display stable patterns of genome dominance . Marek Glombik & al. 2021. The plant journal.  doi:10.1111/tpj.15375
 * Fine structure and transcription dynamics of bread wheat ribosomal DNA loci deciphered by a multi-omics approach. Z Tulpova & al.  2022.  The Plante Genome. https://doi.org/10.1002/tpg2.20191
 * Watson CM, Jackson L, Crinnion LA, et al. Long-read sequencing to resolve the parent of origin of a de novo pathogenic UBE3A variant. Journal of Medical Genetics Published Online First: 12 April 2022. doi: 10.1136/jmedgenet-2021-108314


