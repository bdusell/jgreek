package greek.lexeme;

import greek.grammar.Case;
import greek.grammar.Number;
import greek.grammar.Gender;
import greek.morphology.Morpheme;

/**
 * A lexeme which is part of the nominal system (nouns, adjectives, articles).
 * @author Brian DuSell <bdusell@gmail.com>
 */
public abstract class Nominal {

	/**
	 * Get a particular form derived from this lexeme.
	 * @param c
	 * @param n
	 * @param g
	 * @return The requested word form or null if no such form exists.
	 */
	public greek.grammar.nominal.Nominal getNominalForm(Case c, Number n, Gender g) {
		Morpheme morpheme = getMorpheme(c, n, g);
		if(morpheme == null) return null;
		else return new greek.grammar.nominal.Nominal(morpheme, this, c, g, n);
	}

	/**
	 * Get a particular form derived from this lexeme as a morpheme.
	 * @param c
	 * @param n
	 * @param g
	 * @return The requested form or null if there is no such form.
	 */
	abstract protected Morpheme getMorpheme(Case c, Number n, Gender g);

}
