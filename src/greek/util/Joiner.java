package greek.util;

/**
 * A string joiner. Upon being fed a series of string values, it produces a
 * string consisting of the given values joined by a separator.
 * @author Brian DuSell <bdusell@gmail.com>
 */
public class Joiner {

	/**
	 * Initialize the joiner with a separator.
	 * @param sep 
	 */
	public Joiner(String sep) {
		_sep = sep;
		_buffer = new StringBuilder();
	}

	/**
	 * Feed a string to the joiner.
	 * @param s 
	 */
	public void add(String s) {
		if(_buffer.length() != 0) _buffer.append(_sep);
		_buffer.append(s);
	}

	/**
	 * Reset the separator.
	 * @param sep 
	 */
	public void setSeparator(String sep) {
		_sep = sep;
	}

	/**
	 * Get the joined string.
	 * @return 
	 */
	public String result() {
		return _buffer.toString();
	}

	private String _sep;
	private StringBuilder _buffer;

}
