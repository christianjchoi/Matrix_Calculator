package matrix;

import java.io.InputStreamReader;

/** The main program for Matrix Calculator
 *  @author Christian Choi */

public class Main {

    public static void main(String[] args) {
        Calculator calculator;
        InputStreamReader reader = new InputStreamReader(System.in);
        calculator = new Calculator(new ReaderSource(reader));
        calculator.process();
    }
}
