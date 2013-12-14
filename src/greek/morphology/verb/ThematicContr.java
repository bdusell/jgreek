package greek.morphology.verb;

import greek.grammar.Mood;
import greek.grammar.Person;
import greek.grammar.Number;
import greek.grammar.Tense;
import greek.grammar.Voice;
import greek.morphology.ContractingMorpheme;
import greek.morphology.Morpheme;

/**
 * A thematic verb with a contracting (vowel) stem.
 * @author Brian DuSell <bdusell@gmail.com>
 */
public class ThematicContr extends ThematicUncontr {

	/**
	 * Initialize with a stem.
	 * @param stem 
	 */
	public ThematicContr(Morpheme stem) {
		super(stem);
	}

	@Override
	protected Morpheme getMorpheme(Tense t, Voice v, Mood m, Person p, Number n) {
		Ending ending = Ending.getEnding(t, v, m, p, n);
		if(ending == null) return null;
		ContractingMorpheme result = new ContractingMorpheme(getStem(), ending.getMorpheme());
		result.throwBackPitch();
		result.contract();
		return result.getMorpheme();
	}

}
