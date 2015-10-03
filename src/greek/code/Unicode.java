package greek.code;

import greek.spelling.Grapheme;
import greek.spelling.Glyph;
import java.util.List;

/**
 * Utilities for dealing with the Unicode representation of polytonic Greek
 * script.
 * @author Brian DuSell <bdusell@gmail.com>
 */
public class Unicode {

	/**
	 * Tell whether a character is one used in polytonic Greek script.
	 * @param u
	 * @return 
	 */
	public static boolean isGreekCharacter(char u) {
		return u == 0x037A || // iota subscript
			u == 0x037E || // question mark
			u == 0x0387 || // colon
			u >= 0x0391 && u <= 0x03A1 || // Upper case letters before sigma
			u >= 0x03A3 && u <= 0x03AB || // Upper case letters including and after sigma, capitals with tremas
			u >= 0x03B1 && u <= 0x03CB || // Lower case letters, lower cases with tremas
			u >= 0x03D0 && u <= 0x03D6 || // Alternate glyphs
			u >= 0x03DC && u <= 0x03DD || // digamma
			u >= 0x03F0 && u <= 0x03F2 || // Alternate glyphs
			u >= 0x1F00 && u <= 0x1F15 || // Vowels with breathings
			u >= 0x1F18 && u <= 0x1F1D ||
			u >= 0x1F20 && u <= 0x1F45 ||
			u >= 0x1F48 && u <= 0x1F4D ||
			u >= 0x1F50 && u <= 0x1F57 ||
			u >= 0x1F59 && u <= 0x1F5F && u % 2 != 0 || // Capital upsilon with breathings
			u >= 0x1F60 && u <= 0x1F7D || // Vowels with breathings, lower case vowels with accents
			u >= 0x1F80 && u <= 0x1FB4 || // Vowels with breathings and subscripts, miscellaneous
			u >= 0x1FB6 && u <= 0x1FBC || // miscellaneous
			u >= 0x1FBF && u <= 0x1FC4 ||
			u >= 0x1FC6 && u <= 0x1FD3 ||
			u >= 0x1FD6 && u <= 0x1FDB ||
			u >= 0x1FDD && u <= 0x1FEF ||
			u >= 0x1FF2 && u <= 0x1FF4 ||
			u >= 0x1FF6 && u <= 0x1FFE ||
			u == 0x00AF || // macron
			u == 0x00A8 || // trema
			u == 0x02D8 || // breve
			u == 0x2019; // apostrophe
	}

	/**
	 * Convert a grapheme to its Unicode representation.
	 * @param g Any grapheme.
	 * @return The corresponding Unicode character or null if there is no
	 * corresponding code point.
	 */
	public static Character toPrecombinedCharacter(Grapheme g) {
		if(!g.hasLetter()) { // no letter
			if(g.hasLengthSign()) { // length signs
				if(!(g.hasAccent() || g.hasBreathing() || g.hasIotaSubscript() ||
					(g.hasMacron() && g.hasBreve()) || g.hasDiaeresis())) {
					if(g.hasMacron()) return 0x00AF; // macron
					else return 0x02D8; // breve
				}
			}
			else if(g.hasIotaSubscript()) { // iota subscripts
				if(!(g.hasAccent() || g.hasBreathing() || g.hasDiaeresis())) {
					return 0x037A; // lone iota subscript
				}
			}
			else if(g.hasAccent() && !(g.hasCircumflex() && g.hasDiaeresis()) &&
				(g.hasBreathing() || g.hasDiaeresis())) {
				return (char) (0x1FCD + 0x10 * breathingDiaeresisNumber(g) +
					(accentNumber(g) - 1));
			}
			else if(!g.hasBreathing()) {
				if(g.hasCircumflex()) return (char) (0x1FC0 + (g.hasDiaeresis() ? 1 : 0));
				else if(g.hasAccent() && !g.hasDiaeresis()) {
					if(g.hasGrave()) return 0x1FEF;
					else return 0x1FFD;
				}
				else if(g.hasDiaeresis()) return 0x00A8;
			}
			else if(!g.hasAccent()) {
				if(g.hasDiaeresis()) return null;
				else if(g.hasSmoothBreathing()) return 0x1FBF;
				else return 0x1FFE;
			}
		}
		else if(g.hasLetter()) { // letters of the alphabet
			if(g.hasLengthSign()) { // any vowel with a length sign
				if(g.hasMacron() && g.hasBreve()) return null; // unicode lacks letters with both
				else if(!(g.hasAccent() || g.hasBreathing() || g.hasIotaSubscript() || g.hasDiaeresis())) {
					return (char) (0x1FB0 + 0x10 * lengthSignVowelNumber(g) +
						0x08 * capitalNumber(g) +
						lengthSignNumber(g));
				}
			}
			else if(g.hasBreathing()) { // any letter with a breathing and no length sign
				if(g.getLetter() == Glyph.RHO) { // rho with a breathing
					// unicode lacks capital rho with smooth breathing
					if(g.isCapital() && g.hasSmoothBreathing()) return null;
					else return (char) (0x1FE4 + breathingDiaeresisNumber(g) + 0x07 * capitalNumber(g));
				}
				// vowel with breathing
				// unicode lacks capital upsilon with smooth breathing
				else if(g.getLetter() == Glyph.UPSILON && g.isCapital() && g.hasSmoothBreathing()) return null;
				else return breathingBlockChar(g);
			}
			else if((g.hasAcute() || g.hasGrave()) && !g.isCapital() && !g.hasIotaSubscript()) { // no breathing or length sign
				if(g.hasDiaeresis()) {
					return (char) (0x1FD2 + 0x10 * (circumflexVowelNumber(g) - 2) +
						(accentNumber(g) - 1));
				}
				else {
					return (char) (0x1F70 + 0x02 * vowelNumber(g) + (accentNumber(g) - 1));
				}
			}
			else if((g.getLetter() == Glyph.EPSILON || g.getLetter() == Glyph.OMICRON) &&
				g.hasAccent() && g.isCapital()) {
				return (char) (0x1FC8 + (g.getLetter() == Glyph.EPSILON ? 0 : 0x30) +
					(accentNumber(g) - 1));
			}
			else if(g.hasCircumflex() && !g.isCapital()) {
				return (char) (0x1FB6 + 0x10 * circumflexVowelNumber(g) +
					(g.hasIotaSubscript() || g.hasDiaeresis() ? 1 : 0));
			}
			else if((g.hasAcute() || g.hasGrave()) && g.hasIotaSubscript() && !g.isCapital()) {
				return (char) (0x1FB2 + 0x10 * circumflexVowelNumber(g) +
					(g.hasAcute() ? 2 : 0));
			}
			else if(!(g.getLetter() == Glyph.EPSILON || g.getLetter() == Glyph.OMICRON) &&
				(g.hasAcute() || g.hasGrave()) && g.isCapital() && !g.hasIotaSubscript() && !g.hasDiaeresis()) {
				return (char) (0x1FBA + 0x10 * circumflexVowelNumber(g) + (g.hasAcute() ? 1 : 0));
			}
			else if(!g.hasAccent() && g.hasIotaSubscript()) {
				return (char) (0x1FB3 + 0x10 * circumflexVowelNumber(g) +
					(g.isCapital() ? 0x09 : 0));
			}
			else if(g.getLetter() == Glyph.DIGAMMA) {
				return (char) (0x03DD - capitalNumber(g));
			}
			else if(!g.hasAccent() && !g.hasIotaSubscript()) { // no diacritics or diaeresis only
				return basicBlockChar(g);
			}
		}
		return null; // does not exist
	}

	/**
	 * Convert a glyph representing a punctuation mark to its corresponding
	 * Unicode code-point.
	 * @param g
	 * @return The corresponding code-point, or null if the glyph is not a
	 * punctuation mark.
	 */
	public static Character punctuationToUnicode(Glyph g) {
		switch(g) {
			case PERIOD:             return '.';
			case COMMA:              return ',';
			case QUESTION_MARK:      return 0x037E;
			case SEMICOLON:          return 0x0387;
			case APOSTROPHE:         return 0x2019;
			case LEFT_PARENTHESIS:   return '(';
			case RIGHT_PARENTHESIS:  return ')';
			case HYPHEN:             return '-';
			case DASH:               return 0x2014;
			case LEFT_SINGLE_QUOTE:  return 0x2018;
			case RIGHT_SINGLE_QUOTE: return 0x2019;
			case SPACE:              return ' ';
			case NEWLINE:            return '\n';
			default: return null;
		}
	}

	/**
	 * Convert a character to a its corresponding grapheme.
	 * @param u
	 * @return The grapheme, or null if the character is not used in Greek
	 * script.
	 */
	public static Grapheme toGrapheme(char u) {
		if(!isGreekCharacter(u)) return null;
		Grapheme result = new Grapheme();
		result.tryAddLetter(baseLetterOf(u));
		result.tryAddAccent(accentOf(u));
		result.tryAddBreathing(breathingOf(u));
		if(isCapital(u)) result.tryCapitalize();
		if(hasIotaSubscript(u)) result.tryAddIotaSubscript();
		if(hasMacron(u)) result.tryAddMacron();
		if(hasBreve(u)) result.tryAddBreve();
		if(hasDiaeresis(u)) result.tryAddDiaeresis();
		if(u == 0x03C2) result.tryMakeTerminal();
		return result;
	}

	/**
	 * Convert a list of graphemes to a string of Unicode characters.
	 * @param graphemes
	 * @return The converted string or null if the conversion failed.
	 */
	public static String toPrecombinedUnicode(List<Grapheme> graphemes) {
		StringBuilder result = new StringBuilder(graphemes.size());
		Character c;
		for(Grapheme g : graphemes) {
			c = Unicode.toPrecombinedCharacter(g);
			if(c == null) return null;
			result.append(c);
		}
		return result.toString();
	}

	/* The order of a vowel in the alphabet.
	 0. alpha; 1. epsilon; 2. eta; 3. iota; 4. omicron; 5. upsilon;
	 6. omega */
	private static int vowelNumber(Grapheme a) {
		switch(a.getLetter()) {
			case ALPHA:   return 0;
			case EPSILON: return 1;
			case ETA:     return 2;
			case IOTA:    return 3;
			case OMICRON: return 4;
			case UPSILON: return 5;
			case OMEGA:   return 6;
			default:      return 0;
		}
	}

	/* The order of a circumflex-capable vowel in the alphabet.
	 0. alpha; 1. eta; 2. iota; 3. upsilon; 4. omega */
	private static int circumflexVowelNumber(Grapheme a) {
		switch(a.getLetter()) {
			case ALPHA:   return 0;
			case ETA:     return 1;
			case IOTA:    return 2;
			case UPSILON: return 3;
			case OMEGA:   return 4;
			default:      return 5;
		}
	}

	/* 0. smooth breathing; 1. rough breathing; 2. diaeresis */
	private static int breathingDiaeresisNumber(Grapheme a) {
		if(a.hasSmoothBreathing()) return 0;
		else if(a.hasRoughBreathing()) return 1;
		else if(a.hasDiaeresis()) return 2;
		else return 0; // Undefined
	}

	/* 0. none; 1. grave; 2. acute; 3. circumflex */
	private static int accentNumber(Grapheme a) {
		if(!a.hasAccent()) return 0;
		else if(a.hasGrave()) return 1;
		else if(a.hasAcute()) return 2;
		else if(a.hasCircumflex()) return 3;
		else return 0;
	}

	/* 0. a; 2. i; 3. u */
	private static int lengthSignVowelNumber(Grapheme a) {
		if(a.getLetter() == Glyph.ALPHA) return 0;
		else if(a.getLetter() == Glyph.IOTA) return 2;
		else if(a.getLetter() == Glyph.UPSILON) return 3;
		else return 0;
	}

	/* 0. not capital; 1. is capital */
	private static int capitalNumber(Grapheme a) {
		return a.isCapital() ? 1 : 0;
	}

	/* 0. breve; 1. macron */
	private static int lengthSignNumber(Grapheme a) {
		if(a.hasBreve()) return 0;
		else if(a.hasMacron()) return 1;
		else return 0;
	}

	/* Computes the Unicode character when the character has at least some
	 breathing mark. */
	private static char breathingBlockChar(Grapheme a) {
		return (char) (breathingNoIotaChar(a) + (a.hasIotaSubscript() ? 0x80 + 0x10 * breathingToIotaShiftNumber(a) : 0));
	}

	/* Returns the factor of 0x10 by which to add to breathingNoIotaChar(a)
	 to get the equivalent character with an iota subscript. */
	private static int breathingToIotaShiftNumber(Grapheme a) {
		switch(a.getLetter()) {
			case ALPHA: return 0;
			case ETA:   return -1;
			case OMEGA: return -4;
			default:    return 0;
		}
	}

	/* Computes the position of the letter in the first block of plain
	 characters. Iota and upsilon with diaeresis are tacked onto the end.
	 The offset because of two lower case sigmas is taken into account. */
	private static int alphabetBlockNumber(Grapheme a) {
		if(a.hasDiaeresis()) {
			return 25 + (a.getLetter() == Glyph.IOTA ? 0 : 1);
		}
		else {
			int n = alphabetNumber(a);
			return n + sigmaOffset(a, n);
		}
	}

	/* Computes the position of the letter in the Greek alphabet. */
	private static int alphabetNumber(Grapheme a) {
		return a.getLetter().ordinal() - Glyph.ALPHA.ordinal();
	}

	/* Computes the offset due to the presence of two kinds of sigma in the
	 basic alphabet block. */
	private static int sigmaOffset(Grapheme a, int n) {
		// Sigma offset is +1 if the letter comes after rho (16) and is not lower case terminal sigma (17)
		if(n > 16 && !(n == 17 && a.isTerminal() && !a.isCapital())) return 1;
		else return 0;
	}

	/* Computes the Unicode character when the letter falls within the first
	 block of plain characters. The root is plain lower case alpha. Adding
	 the alphabetBlockNumber gets the correct letter of the alphabet.
	 Subtracting 0x20 gets the capitalized version. */
	private static char basicBlockChar(Grapheme a) {
		return (char) (0x03B1 + alphabetBlockNumber(a) - 0x20 * capitalNumber(a));
	}

	/* Computes the Unicode character when the the letter falls within the
	 large block of characters which have at least some breathing mark. */
	private static char breathingNoIotaChar(Grapheme a) {
		return (char) (0x1F00 + // base offset
			0x10 * vowelNumber(a) + // vowel offset
			0x08 * capitalNumber(a) + // capitalization offset
			0x02 * accentNumber(a) + // accent mark offset
			breathingDiaeresisNumber(a));
	}

	private static Glyph baseLetterOf(char u) {
		if(u <= 0x0391 && u <= 0x03A9 || u >= 0x03B1 && u <= 0x03C9) {
			int p = u - 0x0391 - (u >= 0x03B1 ? 0x20 : 0);
			if(p > 17) --p;
			return Glyph.values()[p];
		}
		else if(u >= 0x03AA && u <= 0x03AB || u >= 0x03CA && u <= 0x03CB) {
			return u % 2 == 0 ? Glyph.IOTA : Glyph.UPSILON;
		}
		else if(u == 0x03D0) return Glyph.BETA;
		else if(u == 0x03D1) return Glyph.THETA;
		else if(u >= 0x03D2 & u <= 0x03D4) return Glyph.UPSILON;
		else if(u == 0x03D5) return Glyph.PHI;
		else if(u == 0x03D6) return Glyph.PI;
		else if(u >= 0x03DC && u <= 0x03DD) return Glyph.DIGAMMA;
		else if(u == 0x03F0) return Glyph.KAPPA;
		else if(u == 0x03F1) return Glyph.RHO;
		else if(u == 0x03F2) return Glyph.SIGMA;
		else if(u >= 0x1F00 && u <= 0x1F6F) return vowelNumberToBaseLetter((u - 0x1F00) / 0x10);
		else if(u >= 0x1F70 && u <= 0x1F7D) return vowelNumberToBaseLetter((u - 0x1F70) / 2);
		else if(u >= 0x1F80 && u <= 0x1FAF) return iotaVowelNumberToBaseLetter((u - 0x1F80) / 0x10);
		else if(u >= 0x1FB0 && u <= 0x1FB4 || u >= 0x1FB6 && u <= 0x1FBC) return Glyph.ALPHA;
		else if(u >= 0x1FC8 && u <= 0x1FC9) return Glyph.EPSILON;
		else if(u >= 0x1FC2 && u <= 0x1FC4 || u >= 0x1FC6 && u <= 0x1FCC) return Glyph.ETA;
		else if(u >= 0x1FD0 && u <= 0x1FD3 || u >= 0x1FD6 && u <= 0x1FDB) return Glyph.IOTA;
		else if(u >= 0x1FE4 && u <= 0x1FE5 || u == 0x1FEC) return Glyph.RHO;
		else if(u >= 0x1FE0 && u <= 0x1FEB) return Glyph.UPSILON;
		else if(u >= 0x1FF8 && u <= 0x1FF9) return Glyph.OMICRON;
		else if(u >= 0x1FF2 && u <= 0x1FF4 || u >= 0x1FF6 && u <= 0x1FFC) return Glyph.OMEGA;
		//else if(u == 0x2019) return '\'';
		else return null;
	}

	private static Glyph vowelNumberToBaseLetter(int i) {
		Glyph [] vowels = {Glyph.ALPHA, Glyph.EPSILON,
			Glyph.ETA, Glyph.IOTA, Glyph.OMICRON,
			Glyph.UPSILON, Glyph.OMEGA};
		return vowels[i];
	}

	private static Glyph iotaVowelNumberToBaseLetter(int i) {
		Glyph [] iotaVowels = {Glyph.ALPHA, Glyph.ETA,
			Glyph.OMEGA};
		return iotaVowels[i];
	}

	private static Glyph accentOf(char u) {
		if(u == 0x03D3) return Glyph.ACUTE;
		else if(u >= 0x1F00 && u <= 0x1F6F || u >= 0x1F80 && u <= 0x1FAF) return accentNumberToAccent((u % 0x08) / 2);
		else if(u >= 0x1F70 && u <= 0x1F7D) return u % 2 == 0 ? Glyph.GRAVE : Glyph.ACUTE;
		else if(u >= 0x1FB0 && u <= 0x1FFF) {

			int mod16 = u % 0x10;
			int mod8 = u % 0x08;
			int n8 = (u - 0x1FB0) / 8;
			int n16 = n8 / 2;

			if(mod16 >= 6 && mod16 <= 7) return Glyph.CIRCUMFLEX;
			else if(mod8 == 2) return Glyph.GRAVE;
			else if(u == 0x1FEF) return Glyph.GRAVE;
			else if(n8 >= 3 && n8 <= 7 && mod16 >= 12) return accentNumberToAccent(mod16 - 4);
			else if(u == 0x1FFD) return Glyph.ACUTE;
			else if(mod16 == 4 && !(n16 >= 2 && n16 <= 3)) return Glyph.ACUTE;
			else if(mod8 == 3 && !(n8 == 0 || n8 == 2 || n8 == 8)) return Glyph.ACUTE;
			else if(n8 == 2 && mod8 < 2) return Glyph.CIRCUMFLEX;
			else if(mod8 < 2 && (n8 == 3 || n8 == 9)) return accentNumberToAccent(mod8 + 1);
			else return null;
		}
		else return null;
	}

	private static Glyph accentNumberToAccent(int i) {
		Glyph [] accents = {null, Glyph.GRAVE, Glyph.ACUTE,
			Glyph.CIRCUMFLEX};
		return accents[i];
	}

	private static Glyph breathingOf(char u) {
		if(u >= 0x1F00 && u <= 0x1F6F || u >= 0x1F80 && u <= 0x1FAF) {
			return u % 2 == 0 ? Glyph.SMOOTH_BREATHING : Glyph.ROUGH_BREATHING;
		}
		else if(u == 0x1FBF || u >= 0x1FCD && u <= 0x1FCF || u == 0x1FE4) {
			return Glyph.SMOOTH_BREATHING;
		}
		else if(u >= 0x1FDD && u <= 0x1FDF || u == 0x1FE5 || u == 0x1FEC || u == 0x1FFE) {
			return Glyph.ROUGH_BREATHING;
		}
		else return null;
	}

	private static boolean isCapital(char u) {
		return u >= 0x0391 && u <= 0x03AB ||
			u >= 0x03D2 && u <= 0x03D4 ||
			u == 0x03DC ||
			((u >= 0x1F00 && u <= 0x1F6F || u >= 0x1F80 && u <= 0x1FAF) && (u % 0x10 / 8 == 1)) ||
			(u >= 0x1FB0 && u <= 0x1FFF && u % 0x10 / 0x08 == 1 && u % 0x08 <= 4 && u != 0x1FDC);
	}

	private static boolean hasIotaSubscript(char u) {
		if(u == 0x037A || u >= 0x1F80 && u <= 0x1FAF) return true;
		else if(u >= 0x1FB0 && u <= 0x1FFF) {
			int mod16 = u % 0x10;
			int n16 = u / 0x10;
			return !(n16 >= 2 && n16 <= 3) &&
				(mod16 >= 2 && mod16 <= 4 || mod16 == 7 || mod16 == 12);
		}
		else return false;
	}
	
	private static boolean hasMacron(char u) {
		return inMacronBlock(u) || u == 0x00AF;
	}
	
	private static boolean inMacronBlock(char u) {
		return u == 0x1FB1 || u == 0x1FB9 || u == 0x1FD1 || u == 0x1FD9;
	}

	private static boolean hasBreve(char u) {
		return inMacronBlock((char) (u + 1)) || u == 0x02D8;
	}

	private static boolean hasDiaeresis(char u) {
		return u >= 0x03AA && u <= 0x03AB ||
			u >= 0x03CA && u <= 0x03CB ||
			u == 0x03D4 ||
			u == 0x1FC1 ||
			u >= 0x1FD2 && u <= 0x1FD3 ||
			u == 0x1FD7 ||
			u >= 0x1FE2 && u <= 0x1FE3 ||
			u == 0x1FE7 ||
			u >= 0x1FED && u <= 0x1FEE ||
			u == 0x00A8;
	}

}
