package matrix;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.IOException;

/** Provides user entered input to be parsed and interpreted.
 *  @author Christian Choi
 */
class ReaderSource {

    /** Input source. */
    private BufferedReader _input;

    ReaderSource(Reader input) {
        _input = new BufferedReader(input);
    }

    /** Reads in a line at a time from the source. */
    public String getLine(String prompt) {
        if (_input == null) {
            return null;
        }
        try {
            System.out.print(prompt);
            System.out.flush();
            String result = _input.readLine();
            if (result == null) {
                _input.close();
            }
            result = result.trim();
            if (result.length() == 0) {
                return getLine(prompt);
            } else {
                return result;
            }
        } catch (IOException excp) {
            return null;
        }
    }


}
