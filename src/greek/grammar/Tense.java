package greek.grammar;

/**
 * Grammatical tense.
 * @author Brian DuSell <bdusell@gmail.com>
 */
public enum Tense {
	PRESENT(Aspect.IMPERFECTIVE),
	FUTURE(Aspect.IMPERFECTIVE),
	IMPERFECT(Aspect.IMPERFECTIVE),
	AORIST(Aspect.AORIST),
	PERFECT(Aspect.PERFECTIVE),
	PLUPERFECT(Aspect.PERFECTIVE),
	FUTURE_PERFECT(Aspect.PERFECTIVE);

	private Aspect _aspect;

	private Tense(Aspect aspect) {
		_aspect = aspect;
	}

	public Aspect getAspect() {
		return _aspect;
	}

}
