package greek.morphology.verb;

import greek.code.PhonoCode;
import greek.grammar.Person;
import greek.grammar.Number;
import greek.morphology.Morpheme;

/**
 * The present indicative active verb endings.
 * @author Brian DuSell <bdusell@gmail.com>
 * @deprecated 
 */
public enum PresIndActEnding {
	FIRST_SING("o:"), SECOND_SING("eis"), THIRD_SING("ei"),
	FIRST_PLUR("omen"), SECOND_PLUR("ete"), THIRD_PLUR("ousin?");

	private final Morpheme _morpheme;

	private PresIndActEnding(String phonoCode) {
		_morpheme = PhonoCode.toMorpheme(phonoCode);
	}

	public Morpheme getMorpheme() {
		return (Morpheme) _morpheme.clone();
	}

	public static PresIndActEnding getEnding(Person p, Number n) {
		return PresIndActEnding.values()[Person.values().length * n.ordinal() + p.ordinal()];
	}

	public Person getPerson() {
		return Person.values()[this.ordinal() % Person.values().length];
	}

	public Number getNumber() {
		return Number.values()[this.ordinal() / Person.values().length];
	}

}
