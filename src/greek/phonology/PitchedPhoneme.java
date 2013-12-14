package greek.phonology;

import greek.morphology.verb.VowelContraction;
import greek.spelling.Glyph;
import greek.spelling.Grapheme;
import java.util.List;

/**
 * A phoneme plus optional pitch accent.
 * @author Brian DuSell <bdusell@gmail.com>
 */
public class PitchedPhoneme implements Cloneable {

	private Phoneme _phoneme = null;
	private Pitch   _pitch   = null;

	/**
	 * The pitch accents.
	 */
	public enum Pitch {
		ACUTE, GRAVE, CIRCUMFLEX;
	}

	/**
	 * The breathing marks.
	 */
	public enum Breathing {
		SMOOTH, ROUGH;
	}

	/**
	 * Initialize a pitched phoneme with no pitch.
	 * @param phoneme 
	 */
	public PitchedPhoneme(Phoneme phoneme) {
		_phoneme = phoneme;
	}

	/**
	 * Initialize a pitched phoneme with a phoneme and pitch accent.
	 * @param phoneme
	 * @param pitch A pitch accent or null for no accent.
	 */
	public PitchedPhoneme(Phoneme phoneme, Pitch pitch) {
		_phoneme = phoneme;
		_pitch = pitch;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		}
		catch(CloneNotSupportedException e) {
			return null;
		}
	}

	/**
	 * Tell whether the pitched phoneme contains a phoneme.
	 * @return 
	 */
	public boolean hasPhoneme() {
		return _phoneme != null;
	}

	/**
	 * Get the unpitched phoneme.
	 * @return 
	 */
	public Phoneme getPhoneme() {
		return _phoneme;
	}

	/**
	 * Get the pitch accent.
	 * @return The pitch accent or null if there is none.
	 */
	public Pitch getPitch() {
		return _pitch;
	}

	/**
	 * Tell whether the phoneme has a pitch accent.
	 * @return 
	 */
	public boolean hasPitch() {
		return _pitch != null;
	}

	/**
	 * Set the pitch accent.
	 * @param p 
	 */
	public void setPitch(Pitch p) {
		_pitch = p;
	}

	/**
	 * If this phoneme has an acute accent, change it to a grave accent.
	 */
	public void setGrave() {
		if(getPitch() == Pitch.ACUTE) {
			setPitch(Pitch.GRAVE);
		}
	}

	/**
	 * Tell whether the phoneme is a vowel with a single mora and an acute
	 * accent.
	 * @return 
	 */
	private boolean isPitchedShortVowel() {
		return getPitch() == Pitch.ACUTE && getPhoneme().isShort();
	}
	
	/**
	 * Attempt to add a pitch accent, phoneme, or modifier to this phoneme.
	 * @param that
	 * @return Whether the item was added.
	 */
	public boolean tryAddObject(Object that) {
		if(that instanceof Pitch) {
			if(((Pitch) that) == Pitch.ACUTE) {
				return tryAddPitch();
			}
			else return false;
		}
		else {
			boolean wasShort = isPitchedShortVowel();
			Phoneme result = getPhoneme().plusObject(that);
			if(result == null) return false;
			else {
				_phoneme = result;
				if(wasShort && result.isLong()) setPitch(Pitch.CIRCUMFLEX);
				return true;
			}
		}
	}

	/**
	 * Attempt to add another pitched phoneme to this one.
	 * @param that
	 * @return Whether the phoneme was added.
	 */
	public boolean tryAddPitchedPhoneme(PitchedPhoneme that) {
		if(that == null) return false;
		if(that.hasPhoneme() && !(hasPitch() && that.hasPitch())) {
			boolean wasShort = isPitchedShortVowel();
			Phoneme result = _phoneme.plusPhoneme(that.getPhoneme());
			if(result == null) return false;
			else {
				_phoneme = result;
				if(wasShort && result.isLong()) setPitch(Pitch.CIRCUMFLEX);
				else if(that.hasPitch()) setPitch(that.getPitch());
				return true;
			}
		}
		else return false;
	}

	private boolean tryAddPitch() {
		if(!hasPitch()) {
			setPitch(Pitch.ACUTE);
			return true;
		}
		else return false;
	}

	private Glyph pitchToGlyph(Pitch p) {
		if(p == null) return null;
		else if(p == Pitch.ACUTE) return Glyph.ACUTE;
		else if(p == Pitch.GRAVE) return Glyph.GRAVE;
		else if(p == Pitch.CIRCUMFLEX) return Glyph.CIRCUMFLEX;
		else return null;
	}

	/**
	 * Convert this pitched phoneme to a corresponding list of graphemes.
	 * @return The corresponding graphemes or null if the conversion failed.
	 */
	public List<Grapheme> toGraphemes() {
		List<Grapheme> result = _phoneme.toGraphemes();
		if(result == null) return null;
		if(!result.isEmpty()) {
			Grapheme temp = result.get(result.size() - 1);
			temp.tryAddAccent(pitchToGlyph(getPitch()));
		}
		return result;
	}

	/**
	 * Convert this pitched phoneme to a corresponding list of graphemes,
	 * with the first grapheme marked with a given kind of breathing.
	 * @param breathing
	 * @return The corresponding graphemes or null if the conversion failed.
	 */
	public List<Grapheme> toBreathingGraphemes(Breathing breathing) {
		List<Grapheme> result = toGraphemes();
		if(result == null) return null;
		else {
			if(result.isEmpty() || !result.get(result.size() - 1).tryAddBreathing(breathingToGlyph(breathing))) {
				return null;
			}
		}
		return result;
	}

	private static Glyph breathingToGlyph(Breathing breathing) {
		switch(breathing) {
			case SMOOTH:
				return Glyph.SMOOTH_BREATHING;
			case ROUGH:
				return Glyph.ROUGH_BREATHING;
			default:
				return null;
		}
	}

	/**
	 * Attempt contract another pitched phoneme into this one.
	 * @param that
	 * @return Whether a contraction took place.
	 */
	public boolean tryContractWith(PitchedPhoneme that) {
		if(that != null && that.hasPhoneme() && this.hasPhoneme()) {
			Phoneme contraction = VowelContraction.contractedVowel(this.getPhoneme(), that.getPhoneme());
			if(contraction == null) return false;
			else {
				boolean wasShort = isPitchedShortVowel();
				setPhoneme(contraction);
				if(wasShort && getPhoneme().isLong()) {
					setPitch(Pitch.CIRCUMFLEX);
				}
				return true;
			}
		}
		else return false;
	}

	private void setPhoneme(Phoneme p) {
		_phoneme = p;
	}

}
