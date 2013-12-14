package greek.phonology;

import greek.code.BetaCode;
import greek.spelling.Grapheme;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Definitions of the phonemes in the ancient Greek language, as appropriate for
 * the purposes of this project. Note that these definitions include diphthongs,
 * some consonant clusters, and other concepts which may not correlate exactly
 * to phonemes in the linguistic sense.
 * @author Brian DuSell <bdusell@gmail.com>
 */
public class Phoneme {

	private static final ArrayList<Phoneme> _values = new ArrayList<>();
	private static final Map<Phoneme, Map<Modifier, Phoneme>> _modifierMap = new HashMap<>();
	private static final Map<Phoneme, Map<Phoneme, Phoneme>> _comboMap = new HashMap<>();

	/* Note that many of the methods in this class are sensitive to the
	 ordering of these definitions. */
	
	public static final Phoneme A = new Phoneme("a");
	public static final Phoneme E = new Phoneme("e");
	public static final Phoneme I = new Phoneme("i");
	public static final Phoneme O = new Phoneme("o");
	public static final Phoneme U = new Phoneme("u");

	public static final Phoneme AA = new Phoneme("a", A, Modifier.LONG);
	public static final Phoneme EE = new Phoneme("h", E, Modifier.LONG);
	public static final Phoneme II = new Phoneme("i", I, Modifier.LONG);
	public static final Phoneme OO = new Phoneme("w", O, Modifier.LONG);
	public static final Phoneme UU = new Phoneme("u", U, Modifier.LONG);

	public static final Phoneme AJ  = new Phoneme("ai", A, Modifier.J);
	public static final Phoneme AI  = new Phoneme("ai", A, I);
	public static final Phoneme AAI = new Phoneme("a|", AA, I);
	public static final Phoneme AU  = new Phoneme("au", A, U);
	public static final Phoneme EI  = new Phoneme("ei", E, I);
	public static final Phoneme EEI = new Phoneme("h|", EE, I);
	public static final Phoneme EU  = new Phoneme("eu", E, U);
	public static final Phoneme EEU = new Phoneme("hu", EE, U);
	public static final Phoneme OJ  = new Phoneme("oi", O, Modifier.J);
	public static final Phoneme OI  = new Phoneme("oi", O, I);
	public static final Phoneme OOI = new Phoneme("w|", OO, I);
	public static final Phoneme OU  = new Phoneme("ou", O, U);
	public static final Phoneme UI  = new Phoneme("ui", U, I);
	public static final Phoneme UUI = new Phoneme("ui", UU, I);

	public static final Phoneme L = new Phoneme("l");
	public static final Phoneme M = new Phoneme("m");
	public static final Phoneme N = new Phoneme("n");
	public static final Phoneme R = new Phoneme("r");
	public static final Phoneme S = new Phoneme("s");
	public static final Phoneme H = new Phoneme();

	public static final Phoneme B = new Phoneme("b");
	public static final Phoneme G = new Phoneme("g");
	public static final Phoneme D = new Phoneme("d");
	public static final Phoneme P = new Phoneme("p");
	public static final Phoneme K = new Phoneme("k");
	public static final Phoneme T = new Phoneme("t");

	public static final Phoneme PH = new Phoneme("f", P, H);
	public static final Phoneme KH = new Phoneme("x", K, H);
	public static final Phoneme TH = new Phoneme("q", T, H);
	
	public static final Phoneme ZD = new Phoneme("z", S, D);
	public static final Phoneme KS = new Phoneme("c", K, S);
	public static final Phoneme PS = new Phoneme("y", P, S);

	public static final Phoneme MOVABLE_NU = new Phoneme("n", N, Modifier.MOVABLE);
	public static final Phoneme NG = new Phoneme("g", N, G);

	/**
	 * Miscellaneous phoneme modifiers.
	 */
	public static enum Modifier {

		/**
		 * Vowel lengthening.
		 */
		LONG,

		/**
		 * Semivowel glide j (the 'y' sound). Used to specify short
		 * diphthongs ai and oi.
		 */
		J,

		/**
		 * Movability, as with terminal nu.
		 */
		MOVABLE;
	}

	private int      _ordinal;
	private Phoneme  _first;
	private Phoneme  _second;
	private Modifier _modifier;
	private List<Grapheme> _graphemes;

	private Phoneme() {
		_first = null;
		_second = null;
		_modifier = null;
		_graphemes = null;
		addConstant();
	}

	private Phoneme(String betaCode) {
		this();
		_graphemes = BetaCode.betaCodeToLetters(betaCode);
	}

	private Phoneme(String betaCode, Phoneme base, Modifier modifier) {
		this(betaCode);
		_first = base;
		_modifier = modifier;
		addBaseModifierPair(base, modifier);
	}

	private Phoneme(String betaCode, Phoneme first, Phoneme second) {
		this(betaCode);
		_first = first;
		_second = second;
		addPhonemeComboPair(first, second);
	}

	/**
	 * Get the combination of this phoneme with another, if one exists.
	 * @param that
	 * @return The combination of the two phonemes, or null if they do not
	 * combine.
	 */
	public Phoneme plusPhoneme(Phoneme that) {
		if(_comboMap.containsKey(this)) {
			Map<Phoneme, Phoneme> map = _comboMap.get(this);
			if(map.containsKey(that)) {
				return map.get(that);
			}
			else return null;
		}
		else return null;
	}

	/**
	 * Get the combination of this phoneme with a modifier, if one exists.
	 * @param m
	 * @return The combination of this phoneme with the modifier, or null if
	 * they do not combine.
	 */
	public Phoneme plusModifier(Modifier m) {
		if(_modifierMap.containsKey(this)) {
			Map<Modifier, Phoneme> map = _modifierMap.get(this);
			if(map.containsKey(m)) {
				return map.get(m);
			}
			else return null;
		}
		else return null;
	}

	/**
	 * Get the combination of this phoneme with another phoneme or modifier,
	 * if one exists.
	 * @param that
	 * @return The combination of this phoneme with the given phoneme or
	 * modifier, or null if they do no combine.
	 */
	public Phoneme plusObject(Object that) {
		if(that instanceof Phoneme) return plusPhoneme((Phoneme) that);
		else if(that instanceof Modifier) return plusModifier((Modifier) that);
		else return null;
	}

	private void addConstant() {
		_ordinal = _values.size();
		_values.add(this);
	}

	private void addBaseModifierPair(Phoneme base, Modifier modifier) {
		Map<Modifier, Phoneme> map;
		if(_modifierMap.containsKey(base)) {
			map = _modifierMap.get(base);
		}
		else {
			map = new HashMap<>();
			_modifierMap.put(base, map);
		}
		map.put(modifier, this);
	}

	private void addPhonemeComboPair(Phoneme first, Phoneme second) {
		Map<Phoneme, Phoneme> map;
		if(_comboMap.containsKey(first)) {
			map = _comboMap.get(first);
		}
		else {
			map = new HashMap<>();
			_comboMap.put(first, map);
		}
		map.put(second, this);
	}

	/**
	 * Tell whether the phoneme is a vowel.
	 * @return 
	 */
	public boolean isVowel() {
		return A.getOrdinal() <= this.getOrdinal() &&
			this.getOrdinal() <= UUI.getOrdinal();
	}

	/**
	 * Tell whether a phoneme is a vowel and a monophthong.
	 * @return 
	 */
	public boolean isMonophthong() {
		return A.getOrdinal() <= this.getOrdinal() &&
			this.getOrdinal() <= UU.getOrdinal();
	}

	/**
	 * Tell whether a phoneme is a vowel and a diphthong.
	 * @return 
	 */
	public boolean isDiphthong() {
		return AJ.getOrdinal() <= this.getOrdinal() &&
			this.getOrdinal() <= UUI.getOrdinal();
	}

	/**
	 * Tell whether the phoneme is a consonant.
	 * @return 
	 */
	public boolean isConsonant() {
		return L.getOrdinal() <= this.getOrdinal() &&
			this.getOrdinal() <= NG.getOrdinal();
	}

	/**
	 * Tell whether the phoneme is a short vowel.
	 * @return 
	 */
	public boolean isShort() {
		return isVowel() && !isLong();
	}

	/**
	 * Tell whether the phoneme is a long vowel.
	 * @return 
	 */
	public boolean isLong() {
		return AA.getOrdinal() <= this.getOrdinal() &&
			this.getOrdinal() <= UUI.getOrdinal() &&
			this.getModifier() != Modifier.J;
	}

	private int getOrdinal() {
		return _ordinal;
	}

	private Modifier getModifier() {
		return _modifier;
	}

	/**
	 * Convert this phoneme to a list of representative graphemes.
	 * @return The corresponding graphemes or null if not applicable.
	 */
	public List<Grapheme> toGraphemes() {
		if(_graphemes == null) return null;
		List<Grapheme> result = new ArrayList<>(_graphemes.size());
		for(Grapheme g : _graphemes) {
			result.add((Grapheme) g.clone());
		}
		return result;
	}

	/**
	 * Tell whether this phoneme can have a breathing sign.
	 * @return 
	 */
	public boolean canHaveBreathing() {
		return isVowel() || this == Phoneme.R;
	}

}
