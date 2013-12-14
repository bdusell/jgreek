package greek.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import greek.spelling.Glyph;
import greek.spelling.Grapheme;
import java.util.Map;

/**
 * The basic definitions underlying the so-called "Beta Code" method of encoding
 * polytonic Greek script using ASCII characters.
 * @author Brian DuSell <bdusell@gmail.com>
 */
public enum BetaCode {

	/* NOTE: The current implementation requires that the ordering of the
	letters here match that of the Glyph class. */

	/* The 24 canonical letters of the alphabet. */
	ALPHA('a', Glyph.ALPHA),
	BETA('b', Glyph.BETA),
	GAMMA('g', Glyph.GAMMA),
	DELTA('d', Glyph.DELTA),
	EPSILON('e', Glyph.EPSILON),
	ZETA('z', Glyph.ZETA),
	ETA('h', Glyph.ETA),
	THETA('q', Glyph.THETA),
	IOTA('i', Glyph.IOTA),
	KAPPA('k', Glyph.KAPPA),
	LAMBDA('l', Glyph.LAMBDA),
	MU('m', Glyph.MU),
	NU('n', Glyph.NU),
	XI('c', Glyph.XI),
	OMICRON('o', Glyph.OMICRON),
	PI('p', Glyph.PI),
	RHO('r', Glyph.RHO),
	SIGMA('s', Glyph.SIGMA),
	TAU('t', Glyph.TAU),
	UPSILON('u', Glyph.UPSILON),
	PHI('f', Glyph.PHI),
	CHI('x', Glyph.CHI),
	PSI('y', Glyph.PSI),
	OMEGA('w', Glyph.OMEGA),

	/* Extra letters. */
	DIGAMMA('v', Glyph.DIGAMMA),

	/* Diacritics. */
	ACUTE('/', Glyph.ACUTE),
	GRAVE('\\', Glyph.GRAVE),
	CIRCUMFLEX('=', Glyph.CIRCUMFLEX),
	SMOOTH_BREATHING(')', Glyph.SMOOTH_BREATHING),
	ROUGH_BREATHING('(', Glyph.ROUGH_BREATHING),
	IOTA_SUBSCRIPT('|', Glyph.IOTA_SUBSCRIPT),
	MACRON(':', Glyph.MACRON),
	BREVE('#', Glyph.BREVE),
	DIAERESIS('+', Glyph.DIAERESIS),

	/* Meta-markers. */
	CAPITALIZATION('*', Glyph.CAPITALIZATION),
	TERMINATION('$', Glyph.TERMINATION),

	/* Punctuation. */
	PERIOD('.', Glyph.PERIOD),
	COMMA(',', Glyph.COMMA),
	QUESTION_MARK('?', Glyph.QUESTION_MARK),
	SEMICOLON(';', Glyph.SEMICOLON),
	APOSTROPHE('\'', Glyph.APOSTROPHE),
	LEFT_PARENTHESIS('[', Glyph.LEFT_PARENTHESIS),
	RIGHT_PARENTHESIS(']', Glyph.RIGHT_PARENTHESIS),
	HYPHEN('-', Glyph.HYPHEN),
	DASH('_', Glyph.DASH),
	LEFT_SINGLE_QUOTE('<', Glyph.LEFT_SINGLE_QUOTE),
	RIGHT_SINGLE_QUOTE('>', Glyph.RIGHT_SINGLE_QUOTE),

	/* Spacing. */
	SPACE(' ', Glyph.SPACE),
	NEWLINE('\n', Glyph.NEWLINE);

	/* Compile the mapping from characters to glyphs. */
	private static final Map<Character, Glyph> _charactersToGlyphs =
		new HashMap<>();
	static {
		for(BetaCode c : BetaCode.values()) {
			_charactersToGlyphs.put(c.getCharacter(), c.getGlyph());
		}
	}
	
	/* Compile the reverse mapping. */
	private static final Map<Glyph, Character> _glyphsToCharacters =
		new HashMap<>();
	static {
		for(BetaCode c : BetaCode.values()) {
			_glyphsToCharacters.put(c.getGlyph(), c.getCharacter());
		}
	}
	
	private final char _character;
	private final Glyph _glyph;
	
	private BetaCode(char character, Glyph glyph) {
		_character = character;
		_glyph = glyph;
	}

	/**
	 * Get the ASCII character associated with this glyph.
	 * @return 
	 */
	public char getCharacter() {
		return _character;
	}
	
	/**
	 * Get the glyph object for this definition.
	 * @return 
	 */
	public Glyph getGlyph() {
		return _glyph;
	}

	/**
	 * Tell whether a character is a beta code character.
	 * @param c
	 * @return 
	 */
	public static boolean isBetaCode(char c) {
		return _charactersToGlyphs.containsKey(c);
	}

	/**
	 * Convert a beta code character to a glyph.
	 * @param c The letter to be converted.
	 * @return The corresponding glyph, or null if c is not a beta code letter.
	 */
	public static Glyph betaCodeToGlyph(char c) {
		return _charactersToGlyphs.get(c);
	}

	/**
	 * Convert a glyph to a beta code character.
	 * @param c The glyph to be converted.
	 * @return The corresponding beta code character.
	 */
	public static Character glyphToBetaCode(Glyph c) {
		return _glyphsToCharacters.get(c);
	}

	/**
	 * Convert a grapheme (combination of glyphs) to a string of beta code.
	 * @param l
	 * @return 
	 */
	public static String letterToBetaCode(Grapheme l) {
		StringBuilder buffer = new StringBuilder();
		/* Capitalize the base letter if the grapheme contains
		 capitalization. If the grapheme has no letter but has
		 capitalization, display the meta-character. */ 
		if(l.hasLetter()) {
			Character base = glyphToBetaCode(l.getLetter());
			if(l.isCapital()) base = Character.toUpperCase(base);
			buffer.append(base);
		}
		else if(l.isCapital()) buffer.append(glyphToBetaCode(Glyph.CAPITALIZATION));
		if(l.hasBreathing()) buffer.append(glyphToBetaCode(l.getBreathing()));
		if(l.hasAccent()) buffer.append(glyphToBetaCode(l.getAccent()));
		if(l.hasDiaeresis()) buffer.append(glyphToBetaCode(Glyph.DIAERESIS));
		if(l.hasMacron()) buffer.append(glyphToBetaCode(Glyph.MACRON));
		if(l.hasBreve()) buffer.append(glyphToBetaCode(Glyph.BREVE));
		if(l.hasIotaSubscript()) buffer.append(glyphToBetaCode(Glyph.IOTA_SUBSCRIPT));
		if(l.isTerminal()) buffer.append(glyphToBetaCode(Glyph.TERMINATION));
		return buffer.toString();
	}

	// Returns null if conversion not possible
	public static ArrayList<Grapheme> betaCodeToLetters(String betaCode) {
		ArrayList<Grapheme> result = new ArrayList<Grapheme>();
		Grapheme temp;
		int i = 0, n = betaCode.length();
		while(i < n) {
			temp = new Grapheme();
			if(temp.tryAddGlyph(betaCodeToGlyph(betaCode.charAt(i)))) {
				++i;
				while(i < n && temp.tryAddGlyph(betaCodeToGlyph(betaCode.charAt(i)))) ++i;
				result.add(temp);
			}
			else {
				return null;
			}
		}
		return result;
	}

	public static List<Grapheme> terminateLetters(List<Grapheme> letters) {
		if(letters == null || letters.isEmpty()) return null;
		if(!letters.get(letters.size() - 1).tryMakeTerminal()) return null;
		return letters;
	}

	public static String betaCodeToPrecombinedUnicode(String betaCode) {
		String result = "";
		ArrayList<Grapheme> letters = betaCodeToLetters(betaCode);
		if(letters == null) return null;
		Character tempChar;
		for(Grapheme c : terminateLetters(betaCodeToLetters(betaCode))) {
			tempChar = Unicode.toPrecombinedCharacter(c);
			if(tempChar == null) return null;
			result += tempChar;
		}
		return result;
	}

	public static Grapheme makeLetter(String betaCode) {
		List<Grapheme> result = betaCodeToLetters(betaCode);
		if(result.size() == 1) return result.get(0);
		else return null;
	}

}
