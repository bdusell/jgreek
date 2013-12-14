package greek.morphology;

import greek.phonology.Phoneme;
import java.util.List;

/**
 * A syllable (TODO).
 * @author Brian DuSell <bdusell@gmail.com>
 */
public class Syllable {

	private List<Phoneme> _onset;
	private List<Phoneme> _nucleus;
	private List<Phoneme> _coda;

	private Syllable(List<Phoneme> onset, List<Phoneme> nucleus,
		List<Phoneme> coda) {
		_onset = onset;
		_nucleus = nucleus;
		_coda = coda;
	}
	
	public static Syllable toSyllable(List<Phoneme> syllable) {
		// TODO
		// some stuff
		return null;
	}

	public int numMorae() {
		// TODO
		return 0;
	}

}
