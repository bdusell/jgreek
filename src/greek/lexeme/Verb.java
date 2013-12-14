package greek.lexeme;

import greek.grammar.Mood;
import greek.grammar.Person;
import greek.grammar.Tense;
import greek.grammar.Voice;
import greek.grammar.Number;
import greek.morphology.Morpheme;

/**
 * A lexical verb.
 * @author Brian DuSell <bdusell@gmail.com>
 */
public abstract class Verb {

	/**
	 * Get a particular form of this verb.
	 * @param t
	 * @param v
	 * @param m
	 * @param p
	 * @param n
	 * @return The requested form or null if there is no such form.
	 */
	public final greek.grammar.verb.Verb getVerbForm(Tense t, Voice v, Mood m, Person p, Number n) {
		Morpheme morpheme = getMorpheme(t, v, m, p, n);
		if(morpheme == null) return null;
		else return new greek.grammar.verb.Verb(morpheme, this, t, v, m, p, n);
	}

	/**
	 * Get a particular form of this verb as a morpheme.
	 * @param t
	 * @param v
	 * @param m
	 * @param p
	 * @param n
	 * @return The requested form or null if there is no such form.
	 */
	abstract protected Morpheme getMorpheme(Tense t, Voice v, Mood m, Person p, Number n);

}
