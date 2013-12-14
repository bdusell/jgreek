package greek.code;

import greek.spelling.Grapheme;

/**
 * A beta code converter which marks the last character terminal.
 * @author Brian DuSell <bdusell@gmail.com>
 */
public class BetaCodeTerminatingConverter extends BetaCodeConverter {

	/**
	 * Mark the letter terminal if there is no letter following it. Note
	 * that waiting until the end of a loop over the entire input string is
	 * a rather inefficient way of accomplishing the task...
	 * @param prev
	 * @param curr
	 * @param next
	 * @return 
	 */
	@Override
	protected boolean markTerminal(ConvertedCharacter prev, Grapheme curr,
		ConvertedCharacter next) {
		return next == null;
	}

}
