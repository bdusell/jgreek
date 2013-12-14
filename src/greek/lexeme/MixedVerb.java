package greek.lexeme;

import greek.grammar.Mood;
import greek.grammar.Number;
import greek.grammar.Person;
import greek.grammar.Tense;
import greek.grammar.Voice;
import greek.morphology.Morpheme;
import java.util.ArrayList;
import java.util.List;

/**
 * A lexical verb composed of multiple types of lexical verb layered on top of
 * each other, where each provides a set of forms which the ones before cannot.
 * This allows more complex lexical verb types to be built out of simpler
 * pieces.
 * @author Brian DuSell <bdusell@gmail.com>
 */
public class MixedVerb extends Verb {

	private List<Verb> _forms;

	public MixedVerb() {
		_forms = new ArrayList<>();
	}

	/**
	 * Add a lexical verb type. A verb type provides a verb form if it
	 * returns a non-null morpheme for the requested form. Verb types added
	 * earlier mask those added later for the word forms they provide.
	 * @param form 
	 */
	public void addForm(Verb form) {
		_forms.add(form);
	}

	@Override
	protected Morpheme getMorpheme(Tense t, Voice v, Mood m, Person p, Number n) {
		Morpheme result;
		for(Verb verb : _forms) {
			result = verb.getMorpheme(t, v, m, p, n);
			if(result != null) return result;
		}
		return null;
	}

}
