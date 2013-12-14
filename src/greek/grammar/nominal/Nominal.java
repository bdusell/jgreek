package greek.grammar.nominal;

import greek.grammar.Case;
import greek.grammar.Gender;
import greek.grammar.Number;
import greek.morphology.Morpheme;

/**
 * A grammatical form (as opposed to a lexeme) which is part of the nominal
 * system (nouns, adjectives, articles).
 * @author Brian DuSell <bdusell@gmail.com>
 */
public class Nominal {

	Morpheme _morpheme;
	greek.lexeme.Nominal _lexeme;
	Case _case;
	Gender _gender;
	Number _number;

	/**
	 * Initialize the word form with a morpheme, the lexeme from which it is
	 * derived, its case, its gender, and its number.
	 * @param morpheme
	 * @param lexeme
	 * @param case_
	 * @param gender
	 * @param number 
	 */
	public Nominal(Morpheme morpheme, greek.lexeme.Nominal lexeme,
		Case case_, Gender gender, Number number) {
		_morpheme = morpheme;
		_lexeme = lexeme;
		_case = case_;
		_gender = gender;
		_number = number;
	}

	/**
	 * Get the form's morpheme.
	 * @return 
	 */
	public Morpheme getMorpheme() {
		return _morpheme;
	}

	/**
	 * Get the lexeme from which this form is derived.
	 * @return 
	 */
	public greek.lexeme.Nominal getLexeme() {
		return _lexeme;
	}

	/**
	 * Get the case of this form.
	 * @return 
	 */
	public Case getCase() {
		return _case;
	}

	/**
	 * Get the gender of this form.
	 * @return 
	 */
	public Gender getGender() {
		return _gender;
	}

	/**
	 * Get the number of this form.
	 * @return 
	 */
	public Number getNumber() {
		return _number;
	}

}
