package greek.lexeme;

import greek.grammar.Case;
import greek.grammar.Gender;
import greek.grammar.Number;
import greek.morphology.Morpheme;

/**
 * The definite article.
 * @author Brian DuSell <bdusell@gmail.com>
 */
public class DefiniteArticle extends Nominal {

	@Override
	public greek.grammar.nominal.Nominal getNominalForm(Case c, Number n, Gender g) {
		return greek.grammar.nominal.DefiniteArticle.getDefiniteArticle(c, g, n);
	}

	@Override
	protected Morpheme getMorpheme(Case c, Number n, Gender g) {
		//return greek.grammar.nominal.DefiniteArticle.getDefiniteArticle(c, g, n).getMorpheme();
		return null;
	}

}
