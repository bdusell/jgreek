package greek.code;

import greek.morphology.Morpheme;
import greek.phonology.Phoneme;
import greek.phonology.Phoneme.Modifier;
import greek.phonology.PitchedPhoneme;
import greek.phonology.PitchedPhoneme.Pitch;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The basic definitions underlying the phonetic code used in this project.
 * @author Brian DuSell <bdusell@gmail.com>
 */
public enum PhonoCode {

	/* Vowels. */
	A('a', Phoneme.A),
	E('e', Phoneme.E),
	I('i', Phoneme.I),
	O('o', Phoneme.O),
	U('u', Phoneme.U),

	/* Voiced stops. */
	B('b', Phoneme.B),
	G('g', Phoneme.G),
	D('d', Phoneme.D),

	/* Unvoiced stops. */
	P('p', Phoneme.P),
	K('k', Phoneme.K),
	T('t', Phoneme.T),

	/* Liquids. */
	L('l', Phoneme.L),
	M('m', Phoneme.M),
	N('n', Phoneme.N),
	R('r', Phoneme.R),

	/* The sibilant s. */
	S('s', Phoneme.S),
	
	/* The rough breathing or aspiration. */
	H('h', Phoneme.H),

	/* Modifiers. */
	
	/* Vowel lengthening. */
	LONG(':', Modifier.LONG),
	
	/* Semivowel glide j. */
	J('j', Modifier.J),
	
	/* Rising pitch. */
	ACUTE('/', Pitch.ACUTE),
	
	/* Grave pitch. */
	GRAVE('\\', Pitch.GRAVE),
	
	/* Movability, as with final nu. */
	MOVABLE('?', Modifier.MOVABLE);

	private char _character;
	private Phoneme _phoneme;
	private Modifier _modifier;
	private Pitch _pitch;

	private static final HashMap<Character, PhonoCode> _charsToCode = new HashMap<>();
	static {
		for(PhonoCode c : PhonoCode.values()) {
			_charsToCode.put(c.getCharacter(), c);
		}
	}

	private PhonoCode(char c, Phoneme phoneme) {
		_character = c;
		_phoneme = phoneme;
		_modifier = null;
		_pitch = null;
	}

	private PhonoCode(char c, Modifier modifier) {
		_character = c;
		_phoneme = null;
		_modifier = modifier;
		_pitch = null;
	}

	private PhonoCode(char c, Pitch pitch) {
		_character = c;
		_phoneme = null;
		_modifier = null;
		_pitch = pitch;
	}

	/**
	 * Convert a string of phonetic code to a morpheme.
	 * @param phonoCode A string of phonetic code.
	 * @return The morpheme or null if the conversion failed.
	 */
	public static Morpheme toMorpheme(String phonoCode) {
		List<PitchedPhoneme> phonemes = toPhonemes(phonoCode);
		if(phonemes == null) return null;
		return new Morpheme(phonemes);
	}

	/**
	 * Convert a string of phonetic code to a list of pitched phonemes.
	 * @param phonoCode
	 * @return The list of pitched phonemes or null if the conversion
	 * failed.
	 */
	public static List<PitchedPhoneme> toPhonemes(String phonoCode) {
		List<PitchedPhoneme> r1 = toUncombinedPhonemes(phonoCode);
		if(r1 == null) return null;
		else return toCombinedPhonemes(r1);
	}

	/**
	 * Convert a string of phonetic code to a list of uncombined pitched
	 * phonemes.
	 * @param phonoCode
	 * @return The phonemes or null if the conversion failed.
	 */
	private static List<PitchedPhoneme> toUncombinedPhonemes(String phonoCode) {
		List<PitchedPhoneme> result = new ArrayList<>();
		PitchedPhoneme p = null;
		PhonoCode pc;
		for(int i = 0, n = phonoCode.length(); i < n; ++i) {
			pc = toPhonoCode(phonoCode.charAt(i));
			if(pc == null) return null;
			else if(p == null || pc.isPhoneme()) {
				if(p != null) result.add(p);
				p = new PitchedPhoneme(pc.getPhoneme());
			}
			else if(!p.tryAddObject(pc.getObject())) {
				return null;
			}
		}
		if(p != null) result.add(p);
		return result;
	}

	/**
	 * Convert a list of uncombined pitched phonemes into a list of combined
	 * phonemes.
	 * @param phonemes A list of uncombined phonemes.
	 * @return The combined phonemes.
	 */
	private static ArrayList<PitchedPhoneme> toCombinedPhonemes(List<PitchedPhoneme> phonemes) {
		ArrayList<PitchedPhoneme> result = new ArrayList<>();
		PitchedPhoneme p;
		int i = 0, n = phonemes.size();
		while(i < n) {
			p = phonemes.get(i++);
			while(i < n) {
				if(p.tryAddPitchedPhoneme(phonemes.get(i))) {
					++i;
				}
				else break;
			}
			result.add(p);
		}

		return result;
	}

	/**
	 * Convert a character to its corresponding phonetic code definition.
	 * @param c
	 * @return The phonetic code definition or null if there is none.
	 */
	public static PhonoCode toPhonoCode(Character c) {
		return _charsToCode.get(c);
	}

	/**
	 * Get the object contained by this definition.
	 * @return A phoneme, modifier, or pitch accent.
	 */
	public Object getObject() {
		if(getPhoneme() != null) return getPhoneme();
		else if(getModifier() != null) return getModifier();
		else if(getPitch() != null) return getPitch();
		else return null;
	}

	/**
	 * Get the character which encodes this element of phonetic code.
	 * @return 
	 */
	public char getCharacter() {
		return _character;
	}

	/**
	 * Get the phoneme corresponding to this definition.
	 * @return The phoneme or null if this definition corresponds to
	 * something else.
	 */
	public Phoneme getPhoneme() {
		return _phoneme;
	}

	/**
	 * Get the modifier corresponding to this definition.
	 * @return The modifier or null if this definition corresponds to
	 * something else.
	 */
	public Modifier getModifier() {
		return _modifier;
	}

	/**
	 * Get the pitch accent corresponding to this definition.
	 * @return The pitch accent or null if this definition corresponds to
	 * something else.
	 */
	public Pitch getPitch() {
		return _pitch;
	}

	/**
	 * Tell whether this definition corresponds to a phoneme.
	 * @return 
	 */
	public boolean isPhoneme() {
		return _phoneme != null;
	}

	/**
	 * Tell whether this definition corresponds to a modifier.
	 * @return 
	 */
	public boolean isModifier() {
		return _modifier != null;
	}

	/**
	 * Tell whether this definition corresponds to a pitch accent.
	 * @return 
	 */
	public boolean isPitch() {
		return _pitch != null;
	}

}
