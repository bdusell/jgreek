package greek.spelling;

/**
 * A single letter in polytonic Greek script. Consists of a base character plus
 * any modifiers which it may take.
 * @author Brian DuSell <bdusell@gmail.com>
 */
public class Grapheme implements Cloneable {

	private Glyph _letter;
	private Glyph _accent;
	private Glyph _breathing;
	private boolean _isCapital;
	private boolean _hasIotaSubscript;
	private boolean _hasMacron;
	private boolean _hasBreve;
	private boolean _hasDiaeresis;
	private boolean _isTerminal;

	/**
	 * Create an empty grapheme.
	 */
	public Grapheme() {
		clear();
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Grapheme)) return false;
		Grapheme that = (Grapheme) o;
		return _letter == that._letter &&
			_accent == that._accent &&
			_breathing == that._breathing &&
			_isCapital == that._isCapital &&
			_hasIotaSubscript == that._hasIotaSubscript &&
			_hasMacron == that._hasMacron &&
			_hasBreve == that._hasBreve &&
			_hasDiaeresis == that._hasDiaeresis &&
			_isTerminal == that._isTerminal;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 83 * hash + (this._letter != null ? this._letter.hashCode() : 0);
		hash = 83 * hash + (this._accent != null ? this._accent.hashCode() : 0);
		hash = 83 * hash + (this._breathing != null ? this._breathing.hashCode() : 0);
		hash = 83 * hash + (this._isCapital ? 1 : 0);
		hash = 83 * hash + (this._hasIotaSubscript ? 1 : 0);
		hash = 83 * hash + (this._hasMacron ? 1 : 0);
		hash = 83 * hash + (this._hasBreve ? 1 : 0);
		hash = 83 * hash + (this._hasDiaeresis ? 1 : 0);
		hash = 83 * hash + (this._isTerminal ? 1 : 0);
		return hash;
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
	 * Get the base letter of this grapheme.
	 * @return The base letter, or null if there is none.
	 */
	public Glyph getLetter() {
		return _letter;
	}

	/**
	 * Tell whether this grapheme has a base letter.
	 * @return 
	 */
	public boolean hasLetter() {
		return _letter != null;
	}

	/**
	 * Get the accent mark of this grapheme.
	 * @return The accent mark, or null if there is none.
	 */
	public Glyph getAccent() {
		return _accent;
	}

	/**
	 * Tell whether this grapheme has an accent mark.
	 * @return 
	 */
	public boolean hasAccent() {
		return _accent != null;
	}

	/**
	 * Tell whether this grapheme has an acute accent.
	 * @return 
	 */
	public boolean hasAcute() {
		return _accent == Glyph.ACUTE;
	}

	/**
	 * Tell whether this grapheme has a grave accent.
	 * @return 
	 */
	public boolean hasGrave() {
		return _accent == Glyph.GRAVE;
	}

	/**
	 * Tell whether this grapheme has a circumflex accent.
	 * @return 
	 */
	public boolean hasCircumflex() {
		return _accent == Glyph.CIRCUMFLEX;
	}

	/**
	 * Get the breathing mark on this grapheme.
	 * @return The breathing mark, or null if there is none.
	 */
	public Glyph getBreathing() {
		return _breathing;
	}

	/**
	 * Tell whether this grapheme has a breathing mark.
	 * @return 
	 */
	public boolean hasBreathing() {
		return _breathing != null;
	}

	/**
	 * Tell whether this grapheme has a smooth breathing.
	 * @return 
	 */
	public boolean hasSmoothBreathing() {
		return _breathing == Glyph.SMOOTH_BREATHING;
	}

	/**
	 * Tell whether this grapheme has a rough breathing.
	 * @return 
	 */
	public boolean hasRoughBreathing() {
		return _breathing == Glyph.ROUGH_BREATHING;
	}

	/**
	 * Tell whether this grapheme is capitalized.
	 * @return 
	 */
	public boolean isCapital() {
		return _isCapital;
	}

	/**
	 * Tell whether this grapheme has an iota subscript.
	 * @return 
	 */
	public boolean hasIotaSubscript() {
		return _hasIotaSubscript;
	}

	/**
	 * Tell whether this grapheme has a length sign.
	 * @return 
	 */
	public boolean hasLengthSign() {
		return hasMacron() || hasBreve();
	}

	/**
	 * Tell whether this grapheme has a macron.
	 * @return 
	 */
	public boolean hasMacron() {
		return _hasMacron;
	}

	/**
	 * Tell whether this grapheme has a breve.
	 * @return 
	 */
	public boolean hasBreve() {
		return _hasBreve;
	}

	/**
	 * Tell whether this grapheme has a diaeresis.
	 * @return 
	 */
	public boolean hasDiaeresis() {
		return _hasDiaeresis;
	}

	/**
	 * Tell whether this grapheme is in terminating form (for lowercase
	 * sigma).
	 * @return 
	 */
	public boolean isTerminal() {
		return _isTerminal;
	}

	/**
	 * Attempt to add a glyph to this grapheme.
	 * @param c Any glyph, or null, which is a harmless no-op.
	 * @return Whether the glyph was added.
	 */
	public boolean tryAddGlyph(Glyph c) {
		if(c == null) return false;
		else if(c.isLetter()) return tryAddLetter(c);
		else if(c.isAccent()) return tryAddAccent(c);
		else if(c.isBreathing()) return tryAddBreathing(c);
		else if(c == Glyph.CAPITALIZATION) return tryCapitalize();
		else if(c == Glyph.IOTA_SUBSCRIPT) return tryAddIotaSubscript();
		else if(c == Glyph.MACRON) return tryAddMacron();
		else if(c == Glyph.BREVE) return tryAddBreve();
		else if(c == Glyph.DIAERESIS) return tryAddDiaeresis();
		else if(c == Glyph.TERMINATION) return tryMakeTerminal();
		else return false;
	}

	/**
	 * Tell whether a certain glyph can be added to this grapheme.
	 * @param c Any glyph, or null.
	 * @return 
	 */
	public boolean canHaveGlyph(Glyph c) {
		if(c == null) return false;
		else if(c.isLetter()) return canHaveLetter(c);
		else if(c.isAccent()) return canHaveAccent(c);
		else if(c.isBreathing()) return canHaveBreathing(c);
		else if(c == Glyph.CAPITALIZATION) return canBeCapital();
		else if(c == Glyph.IOTA_SUBSCRIPT) return canHaveIotaSubscript();
		else if(c == Glyph.MACRON) return canHaveMacron();
		else if(c == Glyph.BREVE) return canHaveBreve();
		else if(c == Glyph.DIAERESIS) return canHaveDiaeresis();
		else if(c == Glyph.TERMINATION) return canBeTerminal();
		else return false;
	}

	/**
	 * Attempt to add a glyph to this grapheme as a base letter.
	 * @param c Any glyph, or null.
	 * @return Whether the glyph was added.
	 */
	public boolean tryAddLetter(Glyph c) {
		if(canHaveLetter(c)) {
			_letter = c;
			return true;
		}
		else return false;
	}

	/**
	 * Tell whether the given glyph can be added to this grapheme as a base
	 * letter.
	 * @param c Any glyph, or null.
	 * @return
	 */
	public boolean canHaveLetter(Glyph c) {
		return !hasLetter() && c != null && c.isLetter() &&
			(!hasAccent() || c.canHaveAccent(getAccent())) &&
			(!hasBreathing() || c.canHaveBreathing()) &&
			(!hasIotaSubscript() || c.canHaveIotaSubscript()) &&
			(!hasLengthSign() || c.canHaveLengthSign()) &&
			(!hasDiaeresis() || c.canHaveDiaeresis());
	}

	/**
	 * Attempt to add a glyph to this grapheme as an accent.
	 * @param c Any glyph, or null.
	 * @return Whether the glyph was added.
	 */
	public boolean tryAddAccent(Glyph c) {
		if(canHaveAccent(c)) {
			_accent = c;
			return true;
		}
		else return false;
	}

	/**
	 * Tell whether the given glyph can be added to this grapheme as a pitch
	 * accent mark.
	 * @param c Any glyph, or null.
	 * @return 
	 */
	public boolean canHaveAccent(Glyph c) {
		return !hasAccent() && c != null && c.isAccent() && (!hasLetter() || getLetter().canHaveAccent(c));
	}

	/**
	 * Attempt to add a glyph to this grapheme as a breathing mark.
	 * @param c Any glyph, or null.
	 * @return Whether the glyph was added.
	 */
	public boolean tryAddBreathing(Glyph c) {
		if(canHaveBreathing(c)) {
			_breathing = c;
			return true;
		}
		else return false;
	}

	/**
	 * Tell whether the given glyph can be added to this grapheme as a
	 * breathing mark.
	 * @param c
	 * @return 
	 */
	public boolean canHaveBreathing(Glyph c) {
		return !hasBreathing() && !hasDiaeresis() && c != null && c.isBreathing() &&
			(!hasLetter() || getLetter().canHaveBreathing());
	}

	/**
	 * Attempt to capitalize this grapheme. Note that capitalized letters
	 * cannot be capitalized again.
	 * @return Whether the grapheme was capitalized.
	 */
	public boolean tryCapitalize() {
		if(canBeCapital()) {
			_isCapital = true;
			return true;
		}
		else return false;
	}

	/**
	 * Tell whether this grapheme can be capitalized. Not that capitalized
	 * letters cannot be capitalized again.
	 * @return 
	 */
	public boolean canBeCapital() {
		return !isCapital();
	}

	/**
	 * Attempt to add an iota subscript to this grapheme.
	 * @return Whether an iota subscript was added.
	 */
	public boolean tryAddIotaSubscript() {
		if(canHaveIotaSubscript()) {
			_hasIotaSubscript = true;
			return true;
		}
		else return false;
	}

	/**
	 * Tell whether an iota subscript can be added to this grapheme.
	 * @return 
	 */
	public boolean canHaveIotaSubscript() {
		return !hasIotaSubscript() && (!hasLetter() || getLetter().canHaveIotaSubscript());
	}

	/**
	 * Attempt to add a macron to this grapheme.
	 * @return Whether a macron was added.
	 */
	public boolean tryAddMacron() {
		if(canHaveMacron()) {
			_hasMacron = true;
			return true;
		}
		else return false;
	}

	/**
	 * Tell whether a macron can be added to this grapheme.
	 * @return 
	 */
	public boolean canHaveMacron() {
		return !hasLengthSign() && (!hasLetter() || getLetter().canHaveLengthSign());
	}

	/**
	 * Attempt to add a breve (short sign, or vrachy) to this grapheme.
	 * @return Whether a breve was added.
	 */
	public boolean tryAddBreve() {
		if(canHaveBreve()) {
			_hasBreve = true;
			return true;
		}
		else return false;
	}

	/**
	 * Tell whether a breve can be added to this grapheme.
	 * @return 
	 */
	public boolean canHaveBreve() {
		return !hasLengthSign() && (!hasLetter() || getLetter().canHaveLengthSign());
	}

	/**
	 * Attempt to add a diaeresis to this grapheme.
	 * @return Whether a diaeresis was added.
	 */
	public boolean tryAddDiaeresis() {
		if(canHaveDiaeresis()) {
			_hasDiaeresis = true;
			return true;
		}
		else return false;
	}

	/**
	 * Tell whether a diaeresis can be added to this grapheme.
	 * @return 
	 */
	public boolean canHaveDiaeresis() {
		return !hasDiaeresis() && !hasBreathing() && (!hasLetter() || getLetter().canHaveDiaeresis());
	}

	/**
	 * Attempt to mark this grapheme as a terminating character.
	 * @return Whether the grapheme was marked terminal.
	 */
	public boolean tryMakeTerminal() {
		if(canBeTerminal()) {
			_isTerminal = true;
			return true;
		}
		else return false;
	}

	/**
	 * Tell whether this grapheme can be marked terminal. Note that the only
	 * condition required is that it is not already marked terminal.
	 * @return 
	 */
	public boolean canBeTerminal() {
		return !isTerminal();
	}

	/**
	 * Clear all glyphs from this grapheme, as if it were created anew.
	 */
	public final void clear() {
		_letter           = null;
		_accent           = null;
		_breathing        = null;
		_isCapital        = false;
		_hasIotaSubscript = false;
		_hasMacron        = false;
		_hasBreve         = false;
		_hasDiaeresis     = false;
		_isTerminal       = false;
	}

}
