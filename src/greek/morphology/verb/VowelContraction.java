package greek.morphology.verb;

import greek.code.PhonoCode;
import greek.phonology.Phoneme;
import greek.phonology.PitchedPhoneme;
import java.util.HashMap;
import java.util.List;

/**
 * Definitions of possible vowel contractions.
 * @author Brian DuSell <bdusell@gmail.com>
 */
public enum VowelContraction {

	AA("a", "a", "a:"), AE("a", "e", "a:"), AEI("a", "ei", "a:i"),
	AI("a", "i", "ai"), AEE("a", "e:", "a:"), AEEI("a", "e:i", "a:i"),
	AO("a", "o", "o:"), AOU("a", "ou", "o:"), AOI("a", "oi", "o:i"),
	AOO("a", "o:", "o:"), AOOI("a", "o:i", "o:i"),

	EA("e", "a", "e:"), EE("e", "e", "ei"), EEI("e", "ei", "ei"),
	EI("e", "i", "ei"), EEE("e", "e:", "e:"), EEEI("e", "e:i", "e:i"),
	EO("e", "o", "ou"), EOU("e", "ou", "ou"), EOI("e", "oi", "oi"),
	EOO("e", "o:", "o:"), EOOI("e", "o:i", "o:i"),

	OA("o", "a", "o:"), OE("o", "e", "ou"), OEI("o", "ei", "oi"),
	OI("o", "i", "oi"), OEE("o", "e:", "o:"), OEEI("o", "e:i", "oi"),
	OO("o", "o", "ou"), OOU("o", "ou", "ou"), OOI("o", "oi", "oi"),
	OOO("o", "o:", "o:"), OOOI("o", "o:i", "o:i");

	private static final HashMap<Phoneme, HashMap<Phoneme, Phoneme>> _contractionMap =
		new HashMap<>();
	static {
		for(VowelContraction v : VowelContraction.values()) {
			HashMap<Phoneme, Phoneme> map;
			if(_contractionMap.containsKey(v.getFirst())) {
				map = _contractionMap.get(v.getFirst());
			}
			else {
				map = new HashMap<>();
				_contractionMap.put(v.getFirst(), map);
			}
			map.put(v.getSecond(), v.getContraction());
		}
	}

	private Phoneme _first;
	private Phoneme _second;
	private Phoneme _out;

	private VowelContraction(String first, String second, String out) {
		_first = stringToPhoneme(first);
		_second = stringToPhoneme(second);
		_out = stringToPhoneme(out);
	}

	private static Phoneme stringToPhoneme(String phonoCode) {
		List<PitchedPhoneme> result = PhonoCode.toPhonemes(phonoCode);
		assert result != null && result.size() == 1 && result.get(0).hasPhoneme();
		return result.get(0).getPhoneme();
	}

	/**
	 * Get the first vowel being contracted.
	 * @return 
	 */
	public Phoneme getFirst() {
		return _first;
	}

	/**
	 * Get the second vowel being contracted.
	 * @return 
	 */
	public Phoneme getSecond() {
		return _second;
	}

	/**
	 * Get the contracted form of the two vowels.
	 * @return 
	 */
	public Phoneme getContraction() {
		return _out;
	}

	/**
	 * Get the contracted form of two given vowels, if they contract.
	 * @param first
	 * @param second
	 * @return The single phoneme (which may be diphthong) into which the
	 * two given phonemes contract, or null if they do not contract.
	 */
	public static Phoneme contractedVowel(Phoneme first, Phoneme second) {
		if(_contractionMap.containsKey(first)) {
			HashMap<Phoneme, Phoneme> map = _contractionMap.get(first);
			if(map.containsKey(second)) return map.get(second);
			else return null;
		}
		else return null;
	}

}
