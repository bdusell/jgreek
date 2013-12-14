package greek.code;

import greek.spelling.Glyph;
import java.util.ArrayList;
import greek.spelling.Grapheme;
import java.util.List;

/**
 * A converter and interpreter of beta code. Extensions of this class can adjust
 * the method of conversion to suit different needs.
 * @author Brian DuSell <bdusell@gmail.com>
 */
public class BetaCodeConverter {

	/**
	 * Convert a string of beta code to a list of graphemes.
	 * @param betaCode
	 * @return The corresponding list of graphemes or null if the beta code
	 * conversion was not possible.
	 */
	public final List<Grapheme> toLetters(String betaCode) {
		List<ConvertedCharacter> converted = toConvertedCharacters(betaCode);
		if(converted == null) return null;
		ArrayList<Grapheme> result = new ArrayList<>();
		for(ConvertedCharacter c : converted) {
			result.add(c.getGreekLetter());
		}
		return result;
	}

	/**
	 * Convert a string of beta code into a string of pre-combined Unicode
	 * characters.
	 * @param betaCode
	 * @return The corresponding string of Unicode characters or null if the
	 * beta code conversion was not possible.
	 */
	public final String toPrecombinedUnicode(String betaCode) {
		StringBuilder buffer = new StringBuilder();
		List<ConvertedCharacter> letters = toConvertedCharacters(betaCode);
		if(letters == null) return null;
		Character tempChar;
		for(ConvertedCharacter c : letters) {
			tempChar = c.toCharacter();
			if(tempChar == null) {
				String placeholder = onNonexistantCharacter(c);
				if(placeholder == null) return null;
				buffer.append(placeholder);
			}
			else buffer.append(tempChar);
		}
		return buffer.toString();
	}

	/* Convert a string of beta code into a workable representation. This is
	 the heart of the conversion algorithm. */
	private List<ConvertedCharacter> toConvertedCharacters(String betaCode) {
		List<ConvertedCharacter> result = new ArrayList<>();
		Grapheme temp; // A temporary grapheme object
		Integer i = 0, n = betaCode.length();
		/* Repeatedly attempt to add successive characters to an empty
		 grapheme until they are no longer applicable. Respond to the
		 overridable methods to adjust error recovery policy. */
		while(i < n) {
			temp = new Grapheme();
			if(temp.tryAddGlyph(BetaCode.betaCodeToGlyph(betaCode.charAt(i)))) {
				++i;
				while(i < n && temp.tryAddGlyph(BetaCode.betaCodeToGlyph(betaCode.charAt(i)))) ++i;
				result.add(new ConvertedCharacter(temp));
			}
			else {
				int inew = onNonLetterChar(result, betaCode, i);
				if(inew < 0) return null;
				else i = inew;
			}
		}

		markTerminals(result);

		return result;
	}

	/**
	 * An overridable method which determines the behavior of the converter
	 * upon encountering a non-beta code character. The default action is
	 * for the conversion to fail.
	 * @param result The list of characters converted so far.
	 * @param betaCode The input string begin converted.
	 * @param i The index of the current character being processed, which is
	 * not a beta code character.
	 * @return Either the index of the next character at which the converter
	 * should resume normal processing, or a negative value if the converter
	 * should fail altogether.
	 */
	protected int onNonLetterChar(List<ConvertedCharacter> result, String betaCode, int i) {
		return -1;
	}

	/**
	 * An overridable method which determines how the Unicode conversion
	 * algorithm deals with characters which have valid glyph combinations
	 * but no code-point. The default action is for the conversion to fail.
	 * @param c The character which has no Unicode representation.
	 * @return A string which should be used in its place, or null if the
	 * converter should fail altogether.
	 */
	protected String onNonexistantCharacter(ConvertedCharacter c) {
		return null;
	}

	/**
	 * An overridable method which determines how the conversion algorithm
	 * decides to mark a letter as being in a terminating position. The
	 * default action is to mark no letters as terminal.
	 * @param prev The letter preceding the letter under consideration.
	 * @param curr The letter being considered.
	 * @param next The letter following the letter under consideration.
	 * @return Whether or not the letter should be marked terminal.
	 */
	protected boolean markTerminal(ConvertedCharacter prev, Grapheme curr,
		ConvertedCharacter next) {
		return false;
	}

	/* Responsible for marking terminating sigmas. */
	private void markTerminals(List<ConvertedCharacter> result) {
		ConvertedCharacter curr;
		for(int i = 0, n = result.size(); i < n; ++i) {
			curr = result.get(i);
			if(curr.hasGreekLetter() && curr.getGreekLetter() != null) {
				Grapheme currLetter = curr.getGreekLetter();
				if(markTerminal(
					i - 1 >= 0 ? result.get(i - 1) : null,
					currLetter,
					i + 1 < n ? result.get(i + 1) : null)
				) {
					currLetter.tryMakeTerminal();
				}
			}
		}
	}

	/**
	 * A character which has undergone conversion. It is a variant container
	 * containing either a complete grapheme, a character, or a single
	 * glyph.
	 */
	protected static class ConvertedCharacter {

		private Grapheme _grapheme;
		private Character _arbitrary;
		private Glyph _glyph;

		/**
		 * Create a converted character out of a grapheme.
		 * @param g 
		 */
		public ConvertedCharacter(Grapheme g) {
			_grapheme = g;
			_arbitrary = null;
			_glyph = null;
		}

		/**
		 * Create a converted character out of an arbitrary character.
		 * @param c 
		 */
		public ConvertedCharacter(Character c) {
			_grapheme = null;
			_arbitrary = c;
			_glyph = null;
		}

		/**
		 * Create a converted character out of an arbitrary character
		 * and its corresponding glyph.
		 * @param c
		 * @param g 
		 */
		public ConvertedCharacter(Character c, Glyph g) {
			_grapheme = null;
			_arbitrary = c;
			_glyph = g;
		}

		/**
		 * Get the character representation of the converted letter.
		 * @return The converted character or null if it cannot be
		 * converted.
		 */
		public Character toCharacter() {
			if(_arbitrary != null) return _arbitrary;
			else if(_grapheme != null) return Unicode.toPrecombinedCharacter(_grapheme);
			else return null;
		}

		/**
		 * Tell if the letter is a regular Greek grapheme and not an
		 * arbitrary character.
		 * @return 
		 */
		public boolean hasGreekLetter() {
			return _arbitrary == null;
		}

		/**
		 * Get the Greek grapheme underlying the converted character, if
		 * it has one.
		 * @return The grapheme or null if the character does not have
		 * one.
		 */
		public Grapheme getGreekLetter() {
			return hasGreekLetter() ? _grapheme : null;
		}

		/**
		 * Tell if the character has a corresponding glyph.
		 * @return 
		 */
		public boolean hasGlyph() {
			return _glyph != null;
		}

		/**
		 * Get the component glyph, if there is one.
		 * @return The glyph, or null if there is none.
		 */
		public Glyph getGlyph() {
			return _glyph;
		}

		/**
		 * Get a string representation of the converted character that
		 * is useful for debugging.
		 * @return 
		 */
		@Override
		public String toString() {
			return "<" + (_grapheme == null ? "null" : BetaCode.letterToBetaCode(_grapheme)) +
				", " + (_arbitrary == null ? "null" : _arbitrary) +
				", " + (_glyph == null ? "null" : _glyph) + ">";
		}

	}

}
