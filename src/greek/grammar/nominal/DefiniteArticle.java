package greek.grammar.nominal;

import greek.code.PhonoCode;
import greek.grammar.Case;
import greek.grammar.Gender;
import greek.grammar.Number;
import greek.morphology.Morpheme;
import greek.morphology.Morpheme.CliticType;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The definite article in its various forms.
 * @author Brian DuSell <bdusell@gmail.com>
 */
public class DefiniteArticle extends Nominal {

	private static HashMap<ArrayList<Object>, DefiniteArticle> _defArtMapping =
		new HashMap<>();

	/* Masculine */
	public static final DefiniteArticle NOM_SING_MASC = new DefiniteArticle("ho");
	public static final DefiniteArticle ACC_SING_MASC = new DefiniteArticle("to/n");
	public static final DefiniteArticle GEN_SING_MASC = new DefiniteArticle("to/u");
	public static final DefiniteArticle DAT_SING_MASC = new DefiniteArticle("to/:i");

	public static final DefiniteArticle NOM_PLUR_MASC = new DefiniteArticle("hoi");
	public static final DefiniteArticle ACC_PLUR_MASC = new DefiniteArticle("tou/s");
	public static final DefiniteArticle GEN_PLUR_MASC = new DefiniteArticle("to/:n");
	public static final DefiniteArticle DAT_PLUR_MASC = new DefiniteArticle("to/is");

	/* Feminine */
	public static final DefiniteArticle NOM_SING_FEM = new DefiniteArticle("he:");
	public static final DefiniteArticle ACC_SING_FEM = new DefiniteArticle("te:/n");
	public static final DefiniteArticle GEN_SING_FEM = new DefiniteArticle("te/:s");
	public static final DefiniteArticle DAT_SING_FEM = new DefiniteArticle("te/:i");

	public static final DefiniteArticle NOM_PLUR_FEM = new DefiniteArticle("hai");
	public static final DefiniteArticle ACC_PLUR_FEM = new DefiniteArticle("ta:/s");
	public static final DefiniteArticle GEN_PLUR_FEM = new DefiniteArticle(GEN_PLUR_MASC);
	public static final DefiniteArticle DAT_PLUR_FEM = new DefiniteArticle("ta/is");

	/* Neuter */
	public static final DefiniteArticle NOM_SING_NEUT = new DefiniteArticle("to/");
	public static final DefiniteArticle ACC_SING_NEUT = new DefiniteArticle(NOM_SING_NEUT);
	public static final DefiniteArticle GEN_SING_NEUT = new DefiniteArticle(GEN_SING_MASC);
	public static final DefiniteArticle DAT_SING_NEUT = new DefiniteArticle(DAT_SING_MASC);

	public static final DefiniteArticle NOM_PLUR_NEUT = new DefiniteArticle("ta/");
	public static final DefiniteArticle ACC_PLUR_NEUT = new DefiniteArticle(NOM_PLUR_NEUT);
	public static final DefiniteArticle GEN_PLUR_NEUT = new DefiniteArticle(GEN_PLUR_MASC);
	public static final DefiniteArticle DAT_PLUR_NEUT = new DefiniteArticle(DAT_PLUR_MASC);

	private int _ordinal;

	private DefiniteArticle(String phonoCode) {
		super(PhonoCode.toMorpheme(phonoCode),
			new greek.lexeme.DefiniteArticle(),
			ctorCase(), ctorGender(), ctorNumber());
		super.getMorpheme().trySetCliticType(CliticType.PROCLITIC);
		registerConstant();
	}

	private DefiniteArticle(DefiniteArticle other) {
		super(other._morpheme,
			new greek.lexeme.DefiniteArticle(),
			ctorCase(), ctorGender(), ctorNumber());
		registerConstant();
	}

	private void registerConstant() {
		_ordinal = _defArtMapping.size();
		ArrayList<Object> key = makeKey(getCase(), getGender(), getNumber());
		_defArtMapping.put(key, this);
	}

	private static ArrayList<Object> makeKey(Case c, Gender g, Number n) {
		ArrayList<Object> result = new ArrayList<>();
		result.add(c);
		result.add(g);
		result.add(n);
		return result;
	}

	private static int ctorOrdinal() {
		return _defArtMapping.size();
	}

	private static Case ctorCase() {
		return Case.values()[ctorOrdinal() % 4];
	}

	private static Gender ctorGender() {
		return Gender.values()[ctorOrdinal() / 8];
	}

	private static Number ctorNumber() {
		return Number.values()[ctorOrdinal() / 4 % 2];
	}

	/**
	 * Get the form of the definite article corresponding to the given case,
	 * gender, and number.
	 * @param c
	 * @param g
	 * @param n
	 * @return 
	 */
	public static DefiniteArticle getDefiniteArticle(Case c, Gender g, Number n) {
		ArrayList<Object> key = makeKey(c, g, n);
		return _defArtMapping.containsKey(key) ? _defArtMapping.get(key) : null;
	}

	/**
	 * Get the morpheme associated with this form of the definite article.
	 * @return 
	 */
	@Override
	public Morpheme getMorpheme() {
		return (Morpheme) super.getMorpheme().clone();
	}

}
