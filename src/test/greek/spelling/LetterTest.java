package test.greek.spelling;

import greek.spelling.Glyph;
import org.junit.Test;
import static org.junit.Assert.*;
import greek.spelling.Grapheme;

public class LetterTest {

	@Test
	public void test1() {

		Grapheme x = new Grapheme();

		// Test initial state
		assertFalse(x.hasLetter());
		assertFalse(x.hasAccent());
		assertFalse(x.hasBreathing());
		assertFalse(x.hasIotaSubscript());
		assertFalse(x.isCapital());

		// Test adding a letter
		assertTrue(x.tryAddLetter(Glyph.ALPHA));
		assertTrue(x.hasLetter());
		assertFalse(x.hasAccent());

		// Test failing to add a second letter
		assertFalse(x.tryAddLetter(Glyph.BETA));
		assertTrue(x.hasLetter());
		assertTrue(x.getLetter() == Glyph.ALPHA);

		// Test adding an invalid breathing
		assertFalse(x.tryAddBreathing(Glyph.ACUTE));
		assertFalse(x.hasAccent());
		assertFalse(x.hasBreathing());

		// Test adding a valid accent
		assertTrue(x.tryAddAccent(Glyph.CIRCUMFLEX));
		assertTrue(x.hasAccent());
		assertFalse(x.hasBreathing());

		// Test failing to add another accent
		assertFalse(x.tryAddAccent(Glyph.ACUTE));

		// Test adding a subscript
		assertTrue(x.tryAddIotaSubscript());
		assertTrue(x.hasIotaSubscript());

		x = new Grapheme();

		// Test failing to add two length marks
		assertTrue(x.tryAddMacron());
		assertTrue(x.hasLengthSign());
		assertTrue(x.hasMacron());
		assertFalse(x.hasBreve());
		assertFalse(x.tryAddBreve());
		assertTrue(x.hasMacron());

		x = new Grapheme();

		// Test adding capitalization before anything else
		assertFalse(x.isCapital());
		assertTrue(x.tryCapitalize());
		assertTrue(x.isCapital());
		
		// Test adding other things before adding a letter
		assertTrue(x.tryAddMacron());
		assertTrue(x.hasMacron());

	}

	@Test
	public void letterTest() {

		Grapheme x = new Grapheme();
		assertFalse(x.hasLetter());
		assertTrue(x.tryAddLetter(Glyph.ZETA));
		assertTrue(x.hasLetter());
		assertTrue(x.getLetter() == Glyph.ZETA);
		assertFalse(x.tryAddLetter(Glyph.MU));
		assertTrue(x.getLetter() == Glyph.ZETA);

	}

	@Test
	public void accentTest() {

		Grapheme x = new Grapheme();
		assertFalse(x.hasAccent());
		assertTrue(x.tryAddAccent(Glyph.ACUTE));
		assertTrue(x.hasAccent());
		assertTrue(x.getAccent() == Glyph.ACUTE);
		assertFalse(x.tryAddAccent(Glyph.CIRCUMFLEX));
		assertFalse(x.getAccent() == Glyph.CIRCUMFLEX);

		x = new Grapheme();
		assertFalse(x.tryAddAccent(Glyph.MU));
		assertFalse(x.hasAccent());

		x = new Grapheme();
		assertTrue(x.tryAddLetter(Glyph.OMICRON));
		assertFalse(x.tryAddAccent(Glyph.CIRCUMFLEX));
		assertTrue(x.tryAddAccent(Glyph.ACUTE));

	}

	@Test
	public void breathingTest() {

		Grapheme x = new Grapheme();
		assertFalse(x.hasBreathing());
		assertTrue(x.tryAddBreathing(Glyph.ROUGH_BREATHING));
		assertTrue(x.hasBreathing());
		assertTrue(x.getBreathing() == Glyph.ROUGH_BREATHING);

		x = new Grapheme();
		assertFalse(x.tryAddBreathing(Glyph.MU));

		x = new Grapheme();
		assertTrue(x.tryAddLetter(Glyph.IOTA));
		assertTrue(x.tryAddBreathing(Glyph.SMOOTH_BREATHING));

	}

	@Test
	public void capitalTest() {

		Grapheme x = new Grapheme();
		assertFalse(x.isCapital());
		assertTrue(x.canBeCapital());
		assertTrue(x.tryCapitalize());
		assertTrue(x.isCapital());
		assertFalse(x.tryCapitalize());

		x = new Grapheme();
		assertTrue(x.tryAddLetter(Glyph.SIGMA));
		assertTrue(x.tryCapitalize());

	}

	@Test
	public void subscriptTest() {

		Grapheme x = new Grapheme();
		assertFalse(x.hasIotaSubscript());
		assertTrue(x.tryAddIotaSubscript());
		assertTrue(x.hasIotaSubscript());
		assertFalse(x.tryAddIotaSubscript());
		assertFalse(x.tryAddLetter(Glyph.MU));
		assertFalse(x.tryAddLetter(Glyph.EPSILON));
		assertTrue(x.tryAddLetter(Glyph.ALPHA));

	}

	@Test
	public void lengthSignTest() {

		Grapheme x = new Grapheme();
		assertFalse(x.hasLengthSign());
		assertFalse(x.hasMacron());
		assertTrue(x.tryAddMacron());
		assertFalse(x.tryAddLetter(Glyph.RHO));
		assertFalse(x.tryAddLetter(Glyph.OMICRON));
		assertFalse(x.tryAddLetter(Glyph.ETA));
		assertTrue(x.tryAddLetter(Glyph.UPSILON));
		assertTrue(x.hasMacron());
		assertTrue(x.getLetter() == Glyph.UPSILON);
		assertFalse(x.tryAddBreve());

	}

	@Test
	public void diaeresisTest() {

		Grapheme x = new Grapheme();
		assertFalse(x.hasDiaeresis());
		assertTrue(x.tryAddDiaeresis());
		assertTrue(x.hasDiaeresis());
		assertFalse(x.tryAddDiaeresis());
		assertTrue(x.tryAddLetter(Glyph.UPSILON));

		x = new Grapheme();
		assertTrue(x.tryAddLetter(Glyph.IOTA));
		assertTrue(x.tryAddAccent(Glyph.ACUTE));
		assertTrue(x.tryAddDiaeresis());

	}

	@Test
	public void terminalTest() {

		Grapheme x = new Grapheme();
		assertFalse(x.isTerminal());
		assertTrue(x.tryMakeTerminal());
		assertTrue(x.isTerminal());
		assertTrue(x.tryAddLetter(Glyph.SIGMA));

		x = new Grapheme();
		assertTrue(x.tryAddLetter(Glyph.SIGMA));
		assertTrue(x.tryMakeTerminal());

		x = new Grapheme();
		assertTrue(x.tryAddLetter(Glyph.ALPHA));
		assertTrue(x.tryMakeTerminal());
		assertFalse(x.tryMakeTerminal());

	}

	@Test
	public void componentTest() {

		// A)/|
		Grapheme x = new Grapheme();
		assertTrue(x.tryAddGlyph(Glyph.ALPHA));
		assertTrue(x.tryAddGlyph(Glyph.CAPITALIZATION));
		assertTrue(x.tryAddGlyph(Glyph.ACUTE));
		assertTrue(x.tryAddGlyph(Glyph.IOTA_SUBSCRIPT));

		// |=(w
		x = new Grapheme();
		assertTrue(x.tryAddGlyph(Glyph.IOTA_SUBSCRIPT));
		assertTrue(x.tryAddGlyph(Glyph.CIRCUMFLEX));
		assertTrue(x.tryAddGlyph(Glyph.ROUGH_BREATHING));
		assertTrue(x.tryAddGlyph(Glyph.OMEGA));

	}

	@Test
	public void equalsTest() {

		Grapheme x, y;

		// Empty letters are equal
		x = new Grapheme();
		y = new Grapheme();
		assertTrue(x.equals(y));

		// a)/ == a)/
		assertTrue(x.tryAddGlyph(Glyph.ALPHA));
		assertTrue(x.tryAddGlyph(Glyph.SMOOTH_BREATHING));
		assertTrue(x.tryAddGlyph(Glyph.ACUTE));
		assertTrue(y.tryAddGlyph(Glyph.ALPHA));
		assertTrue(y.tryAddGlyph(Glyph.SMOOTH_BREATHING));
		assertTrue(y.tryAddGlyph(Glyph.ACUTE));
		assertTrue(x.equals(y));

		// a)/ != I+
		y = new Grapheme();
		assertTrue(y.tryAddGlyph(Glyph.IOTA));
		assertTrue(y.tryAddGlyph(Glyph.CAPITALIZATION));
		assertTrue(y.tryAddGlyph(Glyph.DIAERESIS));
		assertFalse(x.equals(y));

		// Other types of object
		Integer i = new Integer(6);
		assertFalse(x.equals(i));

		// Null
		assertFalse(x.equals(null));

	}

}