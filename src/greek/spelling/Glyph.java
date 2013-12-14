package greek.spelling;

import java.util.HashSet;
import java.util.Set;

/**
 * Definitions of the component shapes of characters in polytonic Greek script.
 * @author Brian DuSell <bdusell@gmail.com>
 */
public enum Glyph {

	/* NOTE: Many of the methods below are sensitive to the ordering of
	 these definitions. */
	
	/* The 24 canonical letters of the alphabet. */
	ALPHA,
	BETA,
	GAMMA,
	DELTA,
	EPSILON,
	ZETA,
	ETA,
	THETA,
	IOTA,
	KAPPA,
	LAMBDA,
	MU,
	NU,
	XI,
	OMICRON,
	PI,
	RHO,
	SIGMA,
	TAU,
	UPSILON,
	PHI,
	CHI,
	PSI,
	OMEGA,

	/* Extra letters. */
	DIGAMMA,

	/* Diacritics. */
	ACUTE,
	GRAVE,
	CIRCUMFLEX,
	SMOOTH_BREATHING,
	ROUGH_BREATHING,
	IOTA_SUBSCRIPT,
	MACRON,
	BREVE,
	DIAERESIS,

	/* Modifiers. */
	CAPITALIZATION,
	TERMINATION,

	/* Punctuation. */
	PERIOD,
	COMMA,
	QUESTION_MARK,
	SEMICOLON,
	APOSTROPHE,
	LEFT_PARENTHESIS,
	RIGHT_PARENTHESIS,
	HYPHEN,
	DASH,
	LEFT_SINGLE_QUOTE,
	RIGHT_SINGLE_QUOTE,
	
	/* White space. */
	SPACE,
	NEWLINE;

	private static final Set<Glyph> _vowelSet = new HashSet<Glyph>();
	static {
		_vowelSet.add(ALPHA);
		_vowelSet.add(EPSILON);
		_vowelSet.add(ETA);
		_vowelSet.add(IOTA);
		_vowelSet.add(OMICRON);
		_vowelSet.add(UPSILON);
		_vowelSet.add(OMEGA);
	}
	
	/**
	 * Tell whether a glyph is one of the letters of the alphabet.
	 * @return 
	 */
	public boolean isLetter() {
		return ALPHA.ordinal() <= this.ordinal() &&
			this.ordinal() <= DIGAMMA.ordinal();
	}

	/**
	 * Tell whether a glyph is a vowel.
	 * @return 
	 */
	public boolean isVowel() {
		return _vowelSet.contains(this);
	}

	/**
	 * Tell whether a glyph is a short vowel.
	 * @return 
	 */
	public boolean isShortVowel() {
		return this == EPSILON || this == OMICRON;
	}

	/**
	 * Tell whether a glyph is a long vowel.
	 * @return 
	 */
	public boolean isLongVowel() {
		return this == ETA || this == OMEGA;
	}

	/**
	 * Tell whether a glyph is a consonant.
	 * @return 
	 */
	public boolean isConsonant() {
		return isLetter() && !isVowel();
	}

	/**
	 * Tell whether a glyph is a diacritic.
	 * @return 
	 */
	public boolean isDiacritic() {
		return ACUTE.ordinal() <= this.ordinal() &&
			this.ordinal() <= DIAERESIS.ordinal();
	}

	/**
	 * Tell whether a glyph is an accent mark.
	 * @return 
	 */
	public boolean isAccent() {
		return ACUTE.ordinal() <= this.ordinal() &&
			this.ordinal() <= CIRCUMFLEX.ordinal();
	}

	/**
	 * Tell whether a glyph is a breathing mark.
	 * @return 
	 */
	public boolean isBreathing() {
		return SMOOTH_BREATHING.ordinal() <= this.ordinal() &&
			this.ordinal() <= ROUGH_BREATHING.ordinal();
	}

	/**
	 * Tell whether a glyph is a length (value) sign.
	 * @return 
	 */
	public boolean isLengthSign() {
		return MACRON.ordinal() <= this.ordinal() &&
			this.ordinal() <= BREVE.ordinal();
	}

	/**
	 * Tell whether a glyph is a form of punctuation.
	 * @return 
	 */
	public boolean isPunctuation() {
		return PERIOD.ordinal() <= this.ordinal() &&
			this.ordinal() <= NEWLINE.ordinal();
	}

	/**
	 * Tell whether a glyph is a vowel and can receive the given accent
	 * mark.
	 * @param c c.isAccent() must be true.
	 * @return 
	 */
	public boolean canHaveAccent(Glyph c) {
		if(c == ACUTE || c == GRAVE) return canHaveAcuteOrGrave();
		else if(c == CIRCUMFLEX) return canHaveCircumflex();
		else return false;
	}

	/**
	 * Tell whether a glyph is a vowel and can receive an acute or grave.
	 * @return 
	 */
	public boolean canHaveAcuteOrGrave() {
		return isVowel();
	}

	/**
	 * Tell whether a glyph is a vowel and can receive a circumflex.
	 * @return 
	 */
	public boolean canHaveCircumflex() {
		return isVowel() && !isShortVowel();
	}

	/**
	 * Tell whether a glyph can receive a breathing mark.
	 * @return 
	 */
	public boolean canHaveBreathing() {
		return isVowel() || this == RHO;
	}

	/**
	 * Tell whether a glyph can receive an iota subscript.
	 * @return 
	 */
	public boolean canHaveIotaSubscript() {
		return this == ALPHA || this == ETA || this == OMEGA; // Only three vowels can have iota subscripts
	}

	/**
	 * Tell whether a glyph can receive a length marker.
	 * @return 
	 */
	public boolean canHaveLengthSign() {
		return this == ALPHA || this == IOTA || this == UPSILON;
	}

	/**
	 * Tell whether a glyph can receive a diaeresis (dots).
	 * @return 
	 */
	public boolean canHaveDiaeresis() {
		return this == IOTA || this == UPSILON;
	}

}
