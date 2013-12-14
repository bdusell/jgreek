package greek.cli;

import greek.code.BetaCodeBestEffortConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A command-line tool for converting beta code to pre-combined Unicode
 * characters.
 * @author Brian DuSell <bdusell@gmail.com>
 */
public class BetaCodeConverter {

	/**
	 * The command which the program identifies itself as.
	 */
	public static final String PROGRAM_NAME = "betacode";
	
	/**
	 * The main function.
	 * @param args 
	 */
	public static void main(String [] args) {

		if(args.length > 1) {
			System.out.println(
"Usage: " + PROGRAM_NAME + " [beta code]\n" +
"    If beta code is provided as an argument, it is converted and printed to\n" +
"    stdout. Otherwise, stdin is read, converted, and printed to stdout.");
			System.exit(1);
		}

		// Use the fault-tolerant best-effort conversion algorithm.
		BetaCodeBestEffortConverter converter = new BetaCodeBestEffortConverter();

		if(args.length == 1) {
			System.out.println(converter.toPrecombinedUnicode(args[0]));
		}
		else {
			// Convert stdin line-by-line
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String line;
			while(true) {
				try {
					line = reader.readLine();
				}
				catch(IOException e) {
					break;
				}
				if(line == null) break;
				System.out.println(converter.toPrecombinedUnicode(line));
			}
		}
		
	}

}
