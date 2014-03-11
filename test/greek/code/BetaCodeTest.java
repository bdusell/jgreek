/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.greek.code;

import org.junit.Test;
import static org.junit.Assert.*;
import greek.code.BetaCode;
import greek.spelling.Glyph;
import greek.spelling.Grapheme;
import java.util.List;

public class BetaCodeTest {

	@Test
	public void testSomeMethod() {

		assertTrue(BetaCode.ALPHA.getCharacter() == 'a');

		assertTrue(BetaCode.betaCodeToComponent('a') == Glyph.ALPHA);
		assertTrue(BetaCode.betaCodeToComponent('k') == Glyph.KAPPA);
		assertTrue(BetaCode.betaCodeToComponent('c') == Glyph.XI);

		assertTrue(BetaCode.componentToBetaCode(Glyph.KAPPA) == 'k');
		assertTrue(BetaCode.componentToBetaCode(Glyph.BETA) == 'b');
		assertTrue(BetaCode.componentToBetaCode(Glyph.PERIOD) == '.');

		Grapheme a = new Grapheme();
		assertTrue(a.tryAddGlyph(Glyph.ALPHA));
		assertTrue(a.tryAddGlyph(Glyph.SMOOTH_BREATHING));
		assertTrue(a.tryAddGlyph(Glyph.ACUTE));
		assertTrue(a.tryAddGlyph(Glyph.IOTA_SUBSCRIPT));

		assertEquals("a)/|", BetaCode.letterToBetaCode(a));

		List<Grapheme> result = BetaCode.betaCodeToLetters(")a/|");
		
		assertTrue(result.size() == 1);
		assertTrue(result.get(0).equals(a));

		result = BetaCode.betaCodeToLetters("a(|/");
		assertTrue(result.size() == 1);
		assertFalse(result.get(0).equals(a));

		result = BetaCode.betaCodeToLetters("");
		assertTrue(result.isEmpty());

		result = BetaCode.betaCodeToLetters("a)lhqw=s");

		for(Grapheme l : result) {
			System.out.println(BetaCode.letterToBetaCode(l));
		}

		assertEquals(6, result.size());

		result = BetaCode.betaCodeToLetters("q09v,pwv4tpaAWA{WVL");
		assertTrue(result == null);

	}

}