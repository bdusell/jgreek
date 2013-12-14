package greek.devel;

import greek.code.PhonoCode;
import greek.code.Unicode;
import greek.phonology.PitchedPhoneme;
import greek.spelling.Grapheme;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class PhonoCodeTest {

	public static void main(String [] args) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line;
		while(true) {
			try {
				line = reader.readLine();
			}
			catch(IOException e) {
				break;
			}
			if(line == null || line.isEmpty()) break;
			List<PitchedPhoneme> phonemes = PhonoCode.toPhonemes(line);
			List<Grapheme> graphemes;
			for(PitchedPhoneme p : phonemes) {
				graphemes = p.toGraphemes();
				for(Grapheme g : graphemes) {
					System.out.print(Unicode.toPrecombinedCharacter(g));
				}
				System.out.println();
			}
		}

	}

}
