package greek.devel;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RegexTest {

	public static void main(String [] args) {

		String s = "alpha beta gamma";

		Pattern p = Pattern.compile("(b|th|z)eta");

		Matcher m = p.matcher(s);

		try {
			if(m.find()) {
				System.out.println("Result: " + m.group(1));
			}
			else {
				System.out.println("No match");
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}

	}

}
