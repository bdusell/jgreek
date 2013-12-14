package greek.morphology;

/**
 * Two morphemes contracting together.
 * @author Brian DuSell <bdusell@gmail.com>
 */
public class ContractingMorpheme {

	Morpheme _morpheme;
	int _sep;

	/**
	 * Initialize the contracting morpheme with its two parts. Initially,
	 * the combined morpheme is simply their concatenation.
	 * @param stem
	 * @param ending 
	 */
	public ContractingMorpheme(Morpheme stem, Morpheme ending) {
		_morpheme = stem.plus(ending);
		_sep = stem.size() - 1;
	}

	/**
	 * Throw a pitch accent as far back as it will go on the combined
	 * morpheme, as on a verb form.
	 */
	public void throwBackPitch() {
		_morpheme.throwBackPitch();
	}

	/**
	 * Contract the two parts of the morpheme together. If there is a pitch
	 * accent on the first part of a contracted vowel, make it a circumflex.
	 * @return Whether a contraction took place.
	 */
	public boolean contract() {
		if(_sep >= 0 && _sep + 1 < _morpheme.size()) {
			if(_morpheme.getPhoneme(_sep).tryContractWith(_morpheme.getPhoneme(_sep + 1))) {
				_morpheme.removePhoneme(_sep + 1);
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the combined morpheme in its current state.
	 * @return 
	 */
	public Morpheme getMorpheme() {
		return _morpheme;
	}

}
