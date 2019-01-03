/*
The MIT License (MIT)

Copyright (c) 2019 Pierre Lindenbaum

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
package com.github.lindenb.jvarkit.util.bio;

import java.util.function.ToIntBiFunction;

/**
 * Grantham Score 
 * generated from 
 * clear && curl -sL "https://gist.githubusercontent.com/arq5x/5408712/raw/d6e7ebde0bc5293ccf975ec46de9eeb92e40d743/grantham.matrix.txt" | awk 'BEGIN {printf("switch(aa1) {\n");} {if(NR==1) { N=split($0,header);} else {printf("case \"%s\": case \"%s\" : switch(aa2) { ",$1,tolower($1)); for(i=2;i<=N;i++) {printf("case \"%s\": case \"%s\": return %s ; " ,header[i],tolower(header[i]),$i); } printf(" default: break;}\n");}} END {printf("}\n");}' | tr '"' "'"


	Citation:   http://www.ncbi.nlm.nih.gov/pubmed/4843792
	Provenance: https://gist.github.com/arq5x/5408712
	
 */

public class GranthamScore implements ToIntBiFunction<Character, Character>{
		private static final int DEFAULT_SCORE=255;
		
		public static int score(final char aa1,final char aa2) {
			switch(aa1) {
			case 'A': case 'a' : switch(aa2) { case 'A': case 'a': return 0 ; case 'R': case 'r': return 112 ; case 'N': case 'n': return 111 ; case 'D': case 'd': return 126 ; case 'C': case 'c': return 195 ; case 'Q': case 'q': return 91 ; case 'E': case 'e': return 107 ; case 'G': case 'g': return 60 ; case 'H': case 'h': return 86 ; case 'I': case 'i': return 94 ; case 'L': case 'l': return 96 ; case 'K': case 'k': return 106 ; case 'M': case 'm': return 84 ; case 'F': case 'f': return 113 ; case 'P': case 'p': return 27 ; case 'S': case 's': return 99 ; case 'T': case 't': return 58 ; case 'W': case 'w': return 148 ; case 'Y': case 'y': return 112 ; case 'V': case 'v': return 64 ;  default: break;}
			case 'R': case 'r' : switch(aa2) { case 'A': case 'a': return 112 ; case 'R': case 'r': return 0 ; case 'N': case 'n': return 86 ; case 'D': case 'd': return 96 ; case 'C': case 'c': return 180 ; case 'Q': case 'q': return 43 ; case 'E': case 'e': return 54 ; case 'G': case 'g': return 125 ; case 'H': case 'h': return 29 ; case 'I': case 'i': return 97 ; case 'L': case 'l': return 102 ; case 'K': case 'k': return 26 ; case 'M': case 'm': return 91 ; case 'F': case 'f': return 97 ; case 'P': case 'p': return 103 ; case 'S': case 's': return 110 ; case 'T': case 't': return 71 ; case 'W': case 'w': return 101 ; case 'Y': case 'y': return 77 ; case 'V': case 'v': return 96 ;  default: break;}
			case 'N': case 'n' : switch(aa2) { case 'A': case 'a': return 111 ; case 'R': case 'r': return 86 ; case 'N': case 'n': return 0 ; case 'D': case 'd': return 23 ; case 'C': case 'c': return 139 ; case 'Q': case 'q': return 46 ; case 'E': case 'e': return 42 ; case 'G': case 'g': return 80 ; case 'H': case 'h': return 68 ; case 'I': case 'i': return 149 ; case 'L': case 'l': return 153 ; case 'K': case 'k': return 94 ; case 'M': case 'm': return 142 ; case 'F': case 'f': return 158 ; case 'P': case 'p': return 91 ; case 'S': case 's': return 46 ; case 'T': case 't': return 65 ; case 'W': case 'w': return 174 ; case 'Y': case 'y': return 143 ; case 'V': case 'v': return 133 ;  default: break;}
			case 'D': case 'd' : switch(aa2) { case 'A': case 'a': return 126 ; case 'R': case 'r': return 96 ; case 'N': case 'n': return 23 ; case 'D': case 'd': return 0 ; case 'C': case 'c': return 154 ; case 'Q': case 'q': return 61 ; case 'E': case 'e': return 45 ; case 'G': case 'g': return 94 ; case 'H': case 'h': return 81 ; case 'I': case 'i': return 168 ; case 'L': case 'l': return 172 ; case 'K': case 'k': return 101 ; case 'M': case 'm': return 160 ; case 'F': case 'f': return 177 ; case 'P': case 'p': return 108 ; case 'S': case 's': return 65 ; case 'T': case 't': return 85 ; case 'W': case 'w': return 181 ; case 'Y': case 'y': return 160 ; case 'V': case 'v': return 152 ;  default: break;}
			case 'C': case 'c' : switch(aa2) { case 'A': case 'a': return 195 ; case 'R': case 'r': return 180 ; case 'N': case 'n': return 139 ; case 'D': case 'd': return 154 ; case 'C': case 'c': return 0 ; case 'Q': case 'q': return 154 ; case 'E': case 'e': return 170 ; case 'G': case 'g': return 159 ; case 'H': case 'h': return 174 ; case 'I': case 'i': return 198 ; case 'L': case 'l': return 198 ; case 'K': case 'k': return 202 ; case 'M': case 'm': return 196 ; case 'F': case 'f': return 205 ; case 'P': case 'p': return 169 ; case 'S': case 's': return 112 ; case 'T': case 't': return 149 ; case 'W': case 'w': return 215 ; case 'Y': case 'y': return 194 ; case 'V': case 'v': return 192 ;  default: break;}
			case 'Q': case 'q' : switch(aa2) { case 'A': case 'a': return 91 ; case 'R': case 'r': return 43 ; case 'N': case 'n': return 46 ; case 'D': case 'd': return 61 ; case 'C': case 'c': return 154 ; case 'Q': case 'q': return 0 ; case 'E': case 'e': return 29 ; case 'G': case 'g': return 87 ; case 'H': case 'h': return 24 ; case 'I': case 'i': return 109 ; case 'L': case 'l': return 113 ; case 'K': case 'k': return 53 ; case 'M': case 'm': return 101 ; case 'F': case 'f': return 116 ; case 'P': case 'p': return 76 ; case 'S': case 's': return 68 ; case 'T': case 't': return 42 ; case 'W': case 'w': return 130 ; case 'Y': case 'y': return 99 ; case 'V': case 'v': return 96 ;  default: break;}
			case 'E': case 'e' : switch(aa2) { case 'A': case 'a': return 107 ; case 'R': case 'r': return 54 ; case 'N': case 'n': return 42 ; case 'D': case 'd': return 45 ; case 'C': case 'c': return 170 ; case 'Q': case 'q': return 29 ; case 'E': case 'e': return 0 ; case 'G': case 'g': return 98 ; case 'H': case 'h': return 40 ; case 'I': case 'i': return 134 ; case 'L': case 'l': return 138 ; case 'K': case 'k': return 56 ; case 'M': case 'm': return 126 ; case 'F': case 'f': return 140 ; case 'P': case 'p': return 93 ; case 'S': case 's': return 80 ; case 'T': case 't': return 65 ; case 'W': case 'w': return 152 ; case 'Y': case 'y': return 122 ; case 'V': case 'v': return 121 ;  default: break;}
			case 'G': case 'g' : switch(aa2) { case 'A': case 'a': return 60 ; case 'R': case 'r': return 125 ; case 'N': case 'n': return 80 ; case 'D': case 'd': return 94 ; case 'C': case 'c': return 159 ; case 'Q': case 'q': return 87 ; case 'E': case 'e': return 98 ; case 'G': case 'g': return 0 ; case 'H': case 'h': return 98 ; case 'I': case 'i': return 135 ; case 'L': case 'l': return 138 ; case 'K': case 'k': return 127 ; case 'M': case 'm': return 127 ; case 'F': case 'f': return 153 ; case 'P': case 'p': return 42 ; case 'S': case 's': return 56 ; case 'T': case 't': return 59 ; case 'W': case 'w': return 184 ; case 'Y': case 'y': return 147 ; case 'V': case 'v': return 109 ;  default: break;}
			case 'H': case 'h' : switch(aa2) { case 'A': case 'a': return 86 ; case 'R': case 'r': return 29 ; case 'N': case 'n': return 68 ; case 'D': case 'd': return 81 ; case 'C': case 'c': return 174 ; case 'Q': case 'q': return 24 ; case 'E': case 'e': return 40 ; case 'G': case 'g': return 98 ; case 'H': case 'h': return 0 ; case 'I': case 'i': return 94 ; case 'L': case 'l': return 99 ; case 'K': case 'k': return 32 ; case 'M': case 'm': return 87 ; case 'F': case 'f': return 100 ; case 'P': case 'p': return 77 ; case 'S': case 's': return 89 ; case 'T': case 't': return 47 ; case 'W': case 'w': return 115 ; case 'Y': case 'y': return 83 ; case 'V': case 'v': return 84 ;  default: break;}
			case 'I': case 'i' : switch(aa2) { case 'A': case 'a': return 94 ; case 'R': case 'r': return 97 ; case 'N': case 'n': return 149 ; case 'D': case 'd': return 168 ; case 'C': case 'c': return 198 ; case 'Q': case 'q': return 109 ; case 'E': case 'e': return 134 ; case 'G': case 'g': return 135 ; case 'H': case 'h': return 94 ; case 'I': case 'i': return 0 ; case 'L': case 'l': return 5 ; case 'K': case 'k': return 102 ; case 'M': case 'm': return 10 ; case 'F': case 'f': return 21 ; case 'P': case 'p': return 95 ; case 'S': case 's': return 142 ; case 'T': case 't': return 89 ; case 'W': case 'w': return 61 ; case 'Y': case 'y': return 33 ; case 'V': case 'v': return 29 ;  default: break;}
			case 'L': case 'l' : switch(aa2) { case 'A': case 'a': return 96 ; case 'R': case 'r': return 102 ; case 'N': case 'n': return 153 ; case 'D': case 'd': return 172 ; case 'C': case 'c': return 198 ; case 'Q': case 'q': return 113 ; case 'E': case 'e': return 138 ; case 'G': case 'g': return 138 ; case 'H': case 'h': return 99 ; case 'I': case 'i': return 5 ; case 'L': case 'l': return 0 ; case 'K': case 'k': return 107 ; case 'M': case 'm': return 15 ; case 'F': case 'f': return 22 ; case 'P': case 'p': return 98 ; case 'S': case 's': return 145 ; case 'T': case 't': return 92 ; case 'W': case 'w': return 61 ; case 'Y': case 'y': return 36 ; case 'V': case 'v': return 32 ;  default: break;}
			case 'K': case 'k' : switch(aa2) { case 'A': case 'a': return 106 ; case 'R': case 'r': return 26 ; case 'N': case 'n': return 94 ; case 'D': case 'd': return 101 ; case 'C': case 'c': return 202 ; case 'Q': case 'q': return 53 ; case 'E': case 'e': return 56 ; case 'G': case 'g': return 127 ; case 'H': case 'h': return 32 ; case 'I': case 'i': return 102 ; case 'L': case 'l': return 107 ; case 'K': case 'k': return 0 ; case 'M': case 'm': return 95 ; case 'F': case 'f': return 102 ; case 'P': case 'p': return 103 ; case 'S': case 's': return 121 ; case 'T': case 't': return 78 ; case 'W': case 'w': return 110 ; case 'Y': case 'y': return 85 ; case 'V': case 'v': return 97 ;  default: break;}
			case 'M': case 'm' : switch(aa2) { case 'A': case 'a': return 84 ; case 'R': case 'r': return 91 ; case 'N': case 'n': return 142 ; case 'D': case 'd': return 160 ; case 'C': case 'c': return 196 ; case 'Q': case 'q': return 101 ; case 'E': case 'e': return 126 ; case 'G': case 'g': return 127 ; case 'H': case 'h': return 87 ; case 'I': case 'i': return 10 ; case 'L': case 'l': return 15 ; case 'K': case 'k': return 95 ; case 'M': case 'm': return 0 ; case 'F': case 'f': return 28 ; case 'P': case 'p': return 87 ; case 'S': case 's': return 135 ; case 'T': case 't': return 81 ; case 'W': case 'w': return 67 ; case 'Y': case 'y': return 36 ; case 'V': case 'v': return 21 ;  default: break;}
			case 'F': case 'f' : switch(aa2) { case 'A': case 'a': return 113 ; case 'R': case 'r': return 97 ; case 'N': case 'n': return 158 ; case 'D': case 'd': return 177 ; case 'C': case 'c': return 205 ; case 'Q': case 'q': return 116 ; case 'E': case 'e': return 140 ; case 'G': case 'g': return 153 ; case 'H': case 'h': return 100 ; case 'I': case 'i': return 21 ; case 'L': case 'l': return 22 ; case 'K': case 'k': return 102 ; case 'M': case 'm': return 28 ; case 'F': case 'f': return 0 ; case 'P': case 'p': return 114 ; case 'S': case 's': return 155 ; case 'T': case 't': return 103 ; case 'W': case 'w': return 40 ; case 'Y': case 'y': return 22 ; case 'V': case 'v': return 50 ;  default: break;}
			case 'P': case 'p' : switch(aa2) { case 'A': case 'a': return 27 ; case 'R': case 'r': return 103 ; case 'N': case 'n': return 91 ; case 'D': case 'd': return 108 ; case 'C': case 'c': return 169 ; case 'Q': case 'q': return 76 ; case 'E': case 'e': return 93 ; case 'G': case 'g': return 42 ; case 'H': case 'h': return 77 ; case 'I': case 'i': return 95 ; case 'L': case 'l': return 98 ; case 'K': case 'k': return 103 ; case 'M': case 'm': return 87 ; case 'F': case 'f': return 114 ; case 'P': case 'p': return 0 ; case 'S': case 's': return 74 ; case 'T': case 't': return 38 ; case 'W': case 'w': return 147 ; case 'Y': case 'y': return 110 ; case 'V': case 'v': return 68 ;  default: break;}
			case 'S': case 's' : switch(aa2) { case 'A': case 'a': return 99 ; case 'R': case 'r': return 110 ; case 'N': case 'n': return 46 ; case 'D': case 'd': return 65 ; case 'C': case 'c': return 112 ; case 'Q': case 'q': return 68 ; case 'E': case 'e': return 80 ; case 'G': case 'g': return 56 ; case 'H': case 'h': return 89 ; case 'I': case 'i': return 142 ; case 'L': case 'l': return 145 ; case 'K': case 'k': return 121 ; case 'M': case 'm': return 135 ; case 'F': case 'f': return 155 ; case 'P': case 'p': return 74 ; case 'S': case 's': return 0 ; case 'T': case 't': return 58 ; case 'W': case 'w': return 177 ; case 'Y': case 'y': return 144 ; case 'V': case 'v': return 124 ;  default: break;}
			case 'T': case 't' : switch(aa2) { case 'A': case 'a': return 58 ; case 'R': case 'r': return 71 ; case 'N': case 'n': return 65 ; case 'D': case 'd': return 85 ; case 'C': case 'c': return 149 ; case 'Q': case 'q': return 42 ; case 'E': case 'e': return 65 ; case 'G': case 'g': return 59 ; case 'H': case 'h': return 47 ; case 'I': case 'i': return 89 ; case 'L': case 'l': return 92 ; case 'K': case 'k': return 78 ; case 'M': case 'm': return 81 ; case 'F': case 'f': return 103 ; case 'P': case 'p': return 38 ; case 'S': case 's': return 58 ; case 'T': case 't': return 0 ; case 'W': case 'w': return 128 ; case 'Y': case 'y': return 92 ; case 'V': case 'v': return 69 ;  default: break;}
			case 'W': case 'w' : switch(aa2) { case 'A': case 'a': return 148 ; case 'R': case 'r': return 101 ; case 'N': case 'n': return 174 ; case 'D': case 'd': return 181 ; case 'C': case 'c': return 215 ; case 'Q': case 'q': return 130 ; case 'E': case 'e': return 152 ; case 'G': case 'g': return 184 ; case 'H': case 'h': return 115 ; case 'I': case 'i': return 61 ; case 'L': case 'l': return 61 ; case 'K': case 'k': return 110 ; case 'M': case 'm': return 67 ; case 'F': case 'f': return 40 ; case 'P': case 'p': return 147 ; case 'S': case 's': return 177 ; case 'T': case 't': return 128 ; case 'W': case 'w': return 0 ; case 'Y': case 'y': return 37 ; case 'V': case 'v': return 88 ;  default: break;}
			case 'Y': case 'y' : switch(aa2) { case 'A': case 'a': return 112 ; case 'R': case 'r': return 77 ; case 'N': case 'n': return 143 ; case 'D': case 'd': return 160 ; case 'C': case 'c': return 194 ; case 'Q': case 'q': return 99 ; case 'E': case 'e': return 122 ; case 'G': case 'g': return 147 ; case 'H': case 'h': return 83 ; case 'I': case 'i': return 33 ; case 'L': case 'l': return 36 ; case 'K': case 'k': return 85 ; case 'M': case 'm': return 36 ; case 'F': case 'f': return 22 ; case 'P': case 'p': return 110 ; case 'S': case 's': return 144 ; case 'T': case 't': return 92 ; case 'W': case 'w': return 37 ; case 'Y': case 'y': return 0 ; case 'V': case 'v': return 55 ;  default: break;}
			case 'V': case 'v' : switch(aa2) { case 'A': case 'a': return 64 ; case 'R': case 'r': return 96 ; case 'N': case 'n': return 133 ; case 'D': case 'd': return 152 ; case 'C': case 'c': return 192 ; case 'Q': case 'q': return 96 ; case 'E': case 'e': return 121 ; case 'G': case 'g': return 109 ; case 'H': case 'h': return 84 ; case 'I': case 'i': return 29 ; case 'L': case 'l': return 32 ; case 'K': case 'k': return 97 ; case 'M': case 'm': return 21 ; case 'F': case 'f': return 50 ; case 'P': case 'p': return 68 ; case 'S': case 's': return 124 ; case 'T': case 't': return 69 ; case 'W': case 'w': return 88 ; case 'Y': case 'y': return 55 ; case 'V': case 'v': return 0 ;  default: break;}
			default: break;
			}
		return DEFAULT_SCORE;
		}
	
	@Override
	public int applyAsInt(final Character aa1, final Character aa2) {
		return aa1==null || aa2==null ? DEFAULT_SCORE : GranthamScore.score(aa1, aa2);
		}
}
