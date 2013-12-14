package greek.morphology.verb;

import greek.code.PhonoCode;
import greek.grammar.Mood;
import greek.grammar.Person;
import greek.grammar.Number;
import greek.grammar.Tense;
import greek.grammar.Voice;
import greek.morphology.Morpheme;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Verb endings.
 * @author Brian DuSell <bdusell@gmail.com>
 */
public enum Ending {

	FIRST_SING ("o:",     Tense.PRESENT, Voice.ACTIVE, Mood.INDICATIVE, Person.FIRST,  Number.SINGULAR),
	SECOND_SING("eis",    Tense.PRESENT, Voice.ACTIVE, Mood.INDICATIVE, Person.SECOND, Number.SINGULAR),
	THIRD_SING ("ei",     Tense.PRESENT, Voice.ACTIVE, Mood.INDICATIVE, Person.THIRD,  Number.SINGULAR),
	FIRST_PLUR ("omen",   Tense.PRESENT, Voice.ACTIVE, Mood.INDICATIVE, Person.FIRST,  Number.PLURAL),
	SECOND_PLUR("ete",    Tense.PRESENT, Voice.ACTIVE, Mood.INDICATIVE, Person.SECOND, Number.PLURAL),
	THIRD_PLUR ("ousin?", Tense.PRESENT, Voice.ACTIVE, Mood.INDICATIVE, Person.THIRD,  Number.PLURAL),

	IMPV_SING  ("e",      Tense.PRESENT, Voice.ACTIVE, Mood.IMPERATIVE, Person.SECOND, Number.SINGULAR),
	IMPV_PLUR  ("ete",    Tense.PRESENT, Voice.ACTIVE, Mood.IMPERATIVE, Person.SECOND, Number.PLURAL);

	private static final HashMap<ArrayList<Object>, Ending> _endingMap = new HashMap<>();
	static {
		for(Ending e : Ending.values()) {
			_endingMap.put(makeKey(e.getTense(), e.getVoice(), e.getMood(), e.getPerson(), e.getNumber()), e);
		}
	}

	private static ArrayList<Object> makeKey(Tense t, Voice v, Mood m, Person p, Number n) {
		ArrayList<Object> result = new ArrayList<>();
		result.add(t);
		result.add(v);
		result.add(m);
		result.add(p);
		result.add(n);
		return result;
	}

	private final Morpheme _morpheme;
	private Tense _tense;
	private Voice _voice;
	private Mood _mood;
	private Person _person;
	private Number _number;

	private Ending(String phonoCode) {
		_morpheme = PhonoCode.toMorpheme(phonoCode);
	}

	private Ending(String phonoCode, Tense t, Voice v, Mood m, Person p, Number n) {
		this(phonoCode);
		_tense = t;
		_voice = v;
		_mood = m;
		_person = p;
		_number = n;
	}

	/**
	 * Get this ending as a morpheme.
	 * @return 
	 */
	public Morpheme getMorpheme() {
		return (Morpheme) _morpheme.clone();
	}

	/**
	 * Look up an ending by its tense, voice, mood, person, and number.
	 * @param t
	 * @param v
	 * @param m
	 * @param p
	 * @param n
	 * @return The requested ending or null if no such ending exists.
	 */
	public static Ending getEnding(Tense t, Voice v, Mood m, Person p, Number n) {
		ArrayList<Object> key = makeKey(t, v, m, p, n);
		return _endingMap.get(key);
	}

	/**
	 * Get the ending's tense.
	 * @return 
	 */
	public Tense getTense() {
		return _tense;
	}

	/**
	 * Get the ending's voice.
	 * @return 
	 */
	public Voice getVoice() {
		return _voice;
	}

	/**
	 * Get the ending's mood.
	 * @return 
	 */
	public Mood getMood() {
		return _mood;
	}

	/**
	 * Get the ending's person.
	 * @return 
	 */
	public Person getPerson() {
		return _person;
	}

	/**
	 * Get the ending's number.
	 * @return 
	 */
	public Number getNumber() {
		return _number;
	}

}
