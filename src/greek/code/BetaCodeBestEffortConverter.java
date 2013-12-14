package greek.code;

import greek.spelling.Glyph;
import greek.spelling.Grapheme;
import java.util.List;

/**
 * A beta code converter which tries to make sense of just about any conceivable
 * input.
 * @author Brian DuSell <bdusell@gmail.com>
 */
public class BetaCodeBestEffortConverter extends BetaCodeConverter {

	/**
	 * Upon encountering a non-beta code character, 
	 * @param result
	 * @param betaCode
	 * @param i
	 * @return 
	 */
	@Override
	protected int onNonLetterChar(List<ConvertedCharacter> result, String betaCode, int i) {

		// The current input character.
		char currentCharacter = betaCode.charAt(i);		
		// The input character in lower case.
		char lowerCharacter = Character.toLowerCase(currentCharacter);
		// The grapheme to be added to the result.
		Grapheme currentGrapheme;
		// The glyph corresponding to the input character in lower case.
		Glyph letterGlyph = BetaCode.betaCodeToGlyph(lowerCharacter);

		/* If the input character is just a capitalized beta code
		 letter, then add a letter and capitalization glyph to the
		 result. If the result ends in a converted character which
		 contains a grapheme, then attempt to add the letter glyph to
		 it. This will succeed in the case that the grapheme consists
		 only of diacritics which can fall on the given letter. If this
		 fails, then the last grapheme already had a letter, and the new
		 glyphs should be added to a brand new one. In either case,
		 finish adding as many diacritics/modifiers as possible to the
		 grapheme used.
		 
		 If the input character is not a capitalized beta code letter,
		 then attempt to treat it as punctuation. Get the Unicode
		 representation of the punctuation mark and pass it along as the
		 converted character.
		 
		 Failing all else, pass the input character along as-is. */
		if(letterGlyph != null && Character.isUpperCase(currentCharacter)) {

			ConvertedCharacter prevConverted =
				result.isEmpty() ?
				null : result.get(result.size() - 1);
			Grapheme prevGrapheme =
				prevConverted != null && prevConverted.hasGreekLetter() ?
				prevConverted.getGreekLetter() : null;

			if(prevGrapheme != null && prevGrapheme.tryAddLetter(letterGlyph)) {
				currentGrapheme = prevGrapheme;
			}
			else {
				currentGrapheme = new Grapheme();
				currentGrapheme.tryAddLetter(letterGlyph);
			}

			currentGrapheme.tryCapitalize();
			++i;
			while(i < betaCode.length() && currentGrapheme.tryAddGlyph(BetaCode.betaCodeToGlyph(betaCode.charAt(i)))) ++i;
			if(prevGrapheme != currentGrapheme) result.add(new BetaCodeConverter.ConvertedCharacter(currentGrapheme));
		}
		else {
			letterGlyph = BetaCode.betaCodeToGlyph(currentCharacter);
			if(letterGlyph != null) {
				// The character is a punctuation mark.
				Character tempChar = Unicode.punctuationToUnicode(letterGlyph);
				if(tempChar != null) result.add(new BetaCodeConverter.ConvertedCharacter(tempChar, letterGlyph));
				else result.add(new BetaCodeConverter.ConvertedCharacter(currentCharacter));
			}
			else {
				// The character is not recognized and will be
				// passed as-is.
				result.add(new BetaCodeConverter.ConvertedCharacter(currentCharacter));
			}
			++i;
		}

		return i;
	}

	/**
	 * When a Unicode representation if unavailable, use beta code or simply
	 * omit the character.
	 * @param c
	 * @return 
	 */
	@Override
	protected String onNonexistantCharacter(ConvertedCharacter c) {
		if(c.hasGreekLetter()) return BetaCode.letterToBetaCode(c.getGreekLetter());
		else return "";
	}

	/**
	 * Mark a letter terminal when it is preceded by a Greek letter and is
	 * not followed by a Greek letter, apostrophe, or hyphen.
	 * @param prev
	 * @param curr
	 * @param next
	 * @return 
	 */
	@Override
	protected boolean markTerminal(ConvertedCharacter prev, Grapheme curr,
		ConvertedCharacter next) {

		return (prev != null && prev.hasGreekLetter()) &&
			(next == null ||
				(!next.hasGreekLetter() && !(next.hasGlyph() &&
				(next.getGlyph() == Glyph.APOSTROPHE ||
				next.getGlyph() == Glyph.HYPHEN))));

	}

}
