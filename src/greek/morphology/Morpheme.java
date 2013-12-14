package greek.morphology;

import greek.phonology.Phoneme;
import greek.phonology.PitchedPhoneme;
import greek.phonology.PitchedPhoneme.Pitch;
import greek.spelling.Grapheme;
import java.util.ArrayList;
import java.util.List;

/**
 * A sequence of phonemes, including pitch accents.
 * @author Brian DuSell <bdusell@gmail.com>
 */
public class Morpheme implements Cloneable {

	/**
	 * A clitic type, either enclitic (throwing pitch backwards) or
	 * proclitic (throwing pitch forwards).
	 */
	public enum CliticType { ENCLITIC, PROCLITIC };

	private List<PitchedPhoneme> _phonemes;
	private List<PitchedPhoneme> _vowels;
	CliticType _cliticType;

	private Morpheme() {
		_phonemes = new ArrayList<>();
		_vowels = null;
		_cliticType = null;
	}

	/**
	 * Initialize the morpheme as a copy of another.
	 * @param copy 
	 */
	public Morpheme(Morpheme copy) {
		this(copy.getPhonemes());
	}

	/**
	 * Initialize the morpheme from a sequence of phonemes.
	 * @param phonemes 
	 */
	public Morpheme(Iterable<PitchedPhoneme> phonemes) {
		this();
		for(PitchedPhoneme p : phonemes) {
			addPhonemeClone(p);
		}
	}

	/**
	 * Initialize the morpheme from a sequence of phonemes and mark its
	 * clitic type.
	 * @param phonemes
	 * @param cliticType A clitic type or null for not a clitic.
	 */
	public Morpheme(Iterable<PitchedPhoneme> phonemes, CliticType cliticType) {
		this(phonemes);
		_cliticType = cliticType;
	}

	private void findVowels() {
		if(_vowels == null) {
			_vowels = new ArrayList<>();
			for(PitchedPhoneme p : getPhonemes()) {
				if(p.hasPhoneme() && p.getPhoneme().isVowel()) {
					_vowels.add(p);
				}
			}
		}
	}

	/**
	 * Throw a pitch accent as far back as possible on the morpheme, as on
	 * a verb. If the accent falls as far back as the first syllable and
	 * the first syllable can have a circumflex, then give it one.
	 */
	public void throwBackPitch() {
		findVowels();
		for(int i = 0, n = _vowels.size(); i < n; ++i) {
			if(i == 0 && canHaveCircumflex(i)) {
				_vowels.get(i).setPitch(Pitch.CIRCUMFLEX);
				break;
			}
			else if(canHaveAcute(i)) {
				_vowels.get(i).setPitch(Pitch.ACUTE);
				break;
			}
		}
	}

	/**
	 * Tell if the morpheme has a pitch accent.
	 * @return 
	 */
	public boolean hasPitch() {
		for(PitchedPhoneme p : this.getPhonemes()) {
			if(p.hasPitch()) return true;
		}
		return false;
	}

	/**
	 * Attempt to set the clitic type of the morpheme. The clitic type can
	 * only be set if the morpheme has no pitch accent and is currently
	 * unset.
	 * @param t
	 * @return Whether the clitic type was changed.
	 */
	public boolean trySetCliticType(CliticType t) {
		if(hasPitch() || t != null) return false;
		else {
			this._cliticType = t;
			return true;
		}
	}
	
	private boolean canHaveCircumflex(int i) {
		findVowels();
		PitchedPhoneme vowel = _vowels.get(i);
		switch(_vowels.size() - i) {
			case 1:
				return vowel.getPhoneme().isLong();
			case 2:
				return vowel.getPhoneme().isLong() &&
					!_vowels.get(_vowels.size() - 1).getPhoneme().isLong();
			default:
				return false;
		}
	}

	private boolean canHaveAcute(int i) {
		findVowels();
		switch(_vowels.size() - i) {
			case 1:
			case 2:
				return true;
			case 3:
				return !_vowels.get(_vowels.size() - 1).getPhoneme().isLong();
			default:
				return false;
		}
	}

	/**
	 * Return the concatenation of this morpheme and another.
	 * @param that
	 * @return 
	 */
	public Morpheme plus(Morpheme that) {
		Morpheme result = new Morpheme(this);
		result.addMorpheme(that);
		return result;
	}

	/**
	 * Concatenate a morpheme to this one.
	 * @param that 
	 */
	public void addMorpheme(Morpheme that) {
		for(PitchedPhoneme p : that.getPhonemes()) {
			addPhonemeClone(p);
		}
	}

	/**
	 * Return the concatenation of this morpheme and another, with the
	 * adjoining morphemes contracted if such a contraction can be made.
	 * @param that
	 * @return 
	 */
	public Morpheme plusContract(Morpheme that) {
		Morpheme result = new Morpheme();
		PitchedPhoneme first = this.isEmpty() ? null : this.getLastPhoneme();
		PitchedPhoneme second = that.isEmpty() ? null : that.getFirstPhoneme();
		for(int i = 0, n = this.size() - 1; i < n; ++i) {
			result.addPhonemeClone(this.getPhoneme(i));
		}
		PitchedPhoneme contraction = null;
		if(first != null && second != null && first.hasPhoneme() && second.hasPhoneme()) {
			contraction = (PitchedPhoneme) first.clone();
			if(!contraction.tryContractWith(second)) contraction = null;
		}
		if(contraction == null) {
			if(first != null) result.addPhonemeClone(first);
			if(second != null) result.addPhonemeClone(second);
		}
		else {
			result.addPhoneme(contraction);
		}
		for(int i = 1, n = that.size(); i < n; ++i) {
			result.addPhonemeClone(that.getPhoneme(i));
		}
		return result;
	}

	/**
	 * Get the sequence of graphemes corresponding to this grapheme. Mark
	 * the last grapheme as terminal.
	 * @return 
	 */
	public List<Grapheme> getGraphemes() {
		return getGraphemes(true);
	}

	/**
	 * Get the sequence of graphemes corresponding to this grapheme.
	 * @param terminate Whether or not to mark the last grapheme as
	 * terminal.
	 * @return 
	 */
	public List<Grapheme> getGraphemes(boolean terminate) {
		List<Grapheme> result = new ArrayList<>();
		boolean roughBreathing = false;
		PitchedPhoneme curr;
		List<Grapheme> graphemes;
		for(int i = 0, n = _phonemes.size(); i < n; ++i) {
			curr = this.getPhoneme(i);
			if(i == 0 && curr.getPhoneme() == Phoneme.H) {
				roughBreathing = true;
			}
			else {
				if((i == 0 || roughBreathing) && curr.getPhoneme().canHaveBreathing()) {
					graphemes = curr.toBreathingGraphemes(
						roughBreathing ?
							PitchedPhoneme.Breathing.ROUGH :
							PitchedPhoneme.Breathing.SMOOTH);
					roughBreathing = false;
				}
				else {
					if(roughBreathing) return null;
					graphemes = curr.toGraphemes();
				}
				if(graphemes == null) return null;
				for(Grapheme g : graphemes) {
					result.add(g);
				}
			}
		}
		if(terminate && !result.isEmpty()) result.get(result.size() - 1).tryMakeTerminal();
		return result;
	}

	private List<PitchedPhoneme> getPhonemes() {
		return _phonemes;
	}

	private void addPhoneme(PitchedPhoneme p) {
		_phonemes.add(p);
	}

	private PitchedPhoneme addPhonemeClone(PitchedPhoneme p) {
		PitchedPhoneme result = (PitchedPhoneme) p.clone();
		addPhoneme(result);
		return result;
	}

	/**
	 * Remove the phoneme at position i from this morpheme.
	 * @param i 
	 */
	public void removePhoneme(int i) {
		_phonemes.remove(i);
	}

	/**
	 * Tell whether this morpheme is empty.
	 * @return 
	 */
	public boolean isEmpty() {
		return _phonemes.isEmpty();
	}

	/**
	 * Get the number of phonemes in this morpheme.
	 * @return 
	 */
	public int size() {
		return _phonemes.size();
	}

	/**
	 * Get the phoneme at position i.
	 * @param i
	 * @return 
	 */
	public PitchedPhoneme getPhoneme(int i) {
		return _phonemes.get(i);
	}

	/**
	 * Get the first phoneme in this morpheme.
	 * @return 
	 */
	public PitchedPhoneme getFirstPhoneme() {
		return _phonemes.isEmpty() ? null : _phonemes.get(0);
	}

	/**
	 * Get the last phoneme in this morpheme.
	 * @return 
	 */
	public PitchedPhoneme getLastPhoneme() {
		return _phonemes.isEmpty() ? null : _phonemes.get(_phonemes.size() - 1);
	}

	/**
	 * Tell whether this morpheme is an enclitic.
	 * @return 
	 */
	public boolean isEnclitic() {
		return _cliticType == CliticType.ENCLITIC;
	}

	/**
	 * Tell whether this morpheme is a proclitic.
	 * @return 
	 */
	public boolean isProclitic() {
		return _cliticType == CliticType.PROCLITIC;
	}

	/**
	 * Tell whether this morpheme is not a clitic.
	 * @return 
	 */
	public boolean isNonClitic() {
		return _cliticType == null;
	}

	@Override
	public Object clone() {
		return new Morpheme(this);
	}

}
