package greek.morphology.verb;

import greek.grammar.Mood;
import greek.grammar.Person;
import greek.grammar.Number;
import greek.grammar.Tense;
import greek.grammar.Voice;
import greek.morphology.Morpheme;

/**
 * A thematic verb with a non-contracting stem.
 * @author Brian DuSell <bdusell@gmail.com>
 */
public class ThematicUncontr extends greek.lexeme.Verb {

	private Morpheme _stem;

	public ThematicUncontr(Morpheme stem) {
		_stem = stem;
	}

	@Override
	protected Morpheme getMorpheme(Tense t, Voice v, Mood m, Person p, Number n) {
		Ending ending = Ending.getEnding(t, v, m, p, n);
		if(ending == null) return null;
		Morpheme result = getStem().plus(Ending.getEnding(t, v, m, p, n).getMorpheme());
		result.throwBackPitch();
		return result;
	}

	public Morpheme getStem() {
		return _stem;
	}

}
