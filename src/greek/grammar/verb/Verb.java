package greek.grammar.verb;

import greek.grammar.Mood;
import greek.grammar.Person;
import greek.grammar.Tense;
import greek.grammar.Voice;
import greek.grammar.Number;
import greek.morphology.Morpheme;

/**
 * A verb form (as opposed to a lexical verb).
 * @author Brian DuSell <bdusell@gmail.com>
 */
public class Verb {

	Morpheme _morpheme;
	greek.lexeme.Verb _lexeme;
	Tense  _tense;
	Voice  _voice;
	Mood   _mood;
	Person _person;
	Number _number;

	/**
	 * Initialize the verb form with its morpheme, the lexeme from which it
	 * is derived, and its tense, voice, mood, person, and number.
	 * @param morpheme
	 * @param lexeme
	 * @param t
	 * @param v
	 * @param m
	 * @param p
	 * @param n 
	 */
	public Verb(Morpheme morpheme, greek.lexeme.Verb lexeme,
		Tense t, Voice v, Mood m, Person p, Number n) {
		_morpheme = morpheme;
		_lexeme = lexeme;
		_tense  = t;
		_voice  = v;
		_mood   = m;
		_person = p;
		_number = n;
	}

	/**
	 * Get the tense of this form.
	 * @return 
	 */
	public Tense getTense() {
		return _tense;
	}

	/**
	 * Get the voice of this form.
	 * @return 
	 */
	public Voice getVoice() {
		return _voice;
	}

	/**
	 * Get the mood of this form.
	 * @return 
	 */
	public Mood getMood() {
		return _mood;
	}

	/**
	 * Get the person of this form.
	 * @return 
	 */
	public Person getPerson() {
		return _person;
	}

	/**
	 * Get the number of this form.
	 * @return 
	 */
	public Number getNumber() {
		return _number;
	}

	/**
	 * Get the morpheme of this form.
	 * @return 
	 */
	public Morpheme getMorpheme() {
		return _morpheme;
	}

	/**
	 * Get the lexeme from which this form is derived.
	 * @return 
	 */
	public greek.lexeme.Verb getLexeme() {
		return _lexeme;
	}

	/**
	 * Set the lexeme from which this form is derived.
	 * @param lexeme 
	 */
	public void setLexeme(greek.lexeme.Verb lexeme) {
		_lexeme = lexeme;
	}

}
