package matrix;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static matrix.Command.Type.*;
import static matrix.MatrixException.error;

/** Calculator component of Matrix Calculator
 *  @author Christian Choi
 */

class Calculator {

    /** Input source. */
    private final ReaderSource _input;

    /** Reading initially from baseSource. */
    Calculator(ReaderSource baseSource) {
        _input = baseSource;
    }

    /** Run a session of Matrix Calculator. */
    void process() {
        while (true) {
            doCommand();
        }
    }

    /** Perform the next command from our input source. */
    void doCommand() {
        try {
            Command cmnd =
                Command.parseCommand(_input.getLine(">>> "));
            Command.Type t = cmnd.commandType();
            String[] op = cmnd.operands();
            _commands.get(t).accept(op);
        } catch (MatrixException excp) {
            System.out.println(excp.getMessage());
        }
    }

    /** Prints String representation of matrix. */
    void doPrint(MatrixObj m) {
        System.out.println(m.toString());
        System.out.flush();
    }

    /** Prints all saved matrices. */
    void doDisplay(String[] operands) {
        for (Map.Entry<String, MatrixObj> e : _matrices.entrySet()) {
            System.out.println(e.getKey() + ":");
            System.out.println(e.getValue().toString());
            System.out.println("");
        }
        System.out.flush();
    }

    /** Perform a 'help' command. */
    void doHelp(String[] unused) {
        InputStream help =
            Calculator.class.getClassLoader().getResourceAsStream(
                "matrix/help.txt");
        if (help == null) {
            System.out.println("No help available.");
        } else {
            try {
                BufferedReader r
                    = new BufferedReader(new InputStreamReader(help));
                while (true) {
                    String line = r.readLine();
                    if (line == null) {
                        break;
                    }
                    System.out.println(line);
                }
                r.close();
            } catch (IOException e) {
                System.out.println("error");
            }
        }
    }

    /** Multiplies two matrices or does scalar multiplication. */
    void doMultiply(String[] operands) {
        if (_matrices.get(operands[0]) == null
            && _matrices.get(operands[2]) == null) {
            throw error("matrices do not exist");
        } else if (_matrices.get(operands[0]) != null
            && _matrices.get(operands[2]) == null) {
            double constant = Double.parseDouble(operands[2]);
            MatrixObj matrix = _matrices.get(operands[0]);
            ArrayList<Double> newElems = new ArrayList<Double>();
            for (double i : matrix.elems()) {
                newElems.add(i * constant);
            }
            MatrixObj m = new MatrixObj(matrix.rows(), matrix.cols(),
                newElems);
            doPrint(m);
            if (operands.length > 3) {
                _matrices.put(operands[operands.length - 1], m);
            }
        } else if (_matrices.get(operands[0]) == null
            && _matrices.get(operands[2]) != null) {
            String temp = operands[0];
            operands[0] = operands[2];
            operands[2] = temp;
            doMultiply(operands);
        } else {
            MatrixObj a = _matrices.get(operands[0]);
            MatrixObj b = _matrices.get(operands[2]);
            if (a.cols() != b.rows()) {
                throw error("bad dimensions for multiplication");
            }
            ArrayList<Double> products = new ArrayList<Double>();
            for (List<Double> lst : a.matrix()) {
                int count = 0;
                for (int i = 0; i < b.cols(); i += 1) {
                    ArrayList<Double> cols = new ArrayList<Double>();
                    for (int j = 0; j < b.rows(); j += 1) {
                        cols.add(b.matrix().get(j).get(i));
                    }
                    double total = 0;
                    while (count < lst.size()) {
                        total += lst.get(count) * cols.get(count);
                        count += 1;
                    }
                    products.add(total);
                    count = 0;
                }
            }
            MatrixObj m = new MatrixObj(a.rows(), b.cols(), products);
            doPrint(m);
            if (operands.length > 3) {
                _matrices.put(operands[operands.length - 1], m);
            }
        }
    }


    /** Adds two matrices. */
    void doAdd(String[] operands) {
        checkExists(operands[0]);
        checkExists(operands[2]);
        MatrixObj a = _matrices.get(operands[0]);
        MatrixObj b = _matrices.get(operands[2]);
        if (!a.dim().equals(b.dim())) {
            throw error("can't add matrices of differing dimensions");
        }
        ArrayList<Double> combined = new ArrayList<Double>();
        for (int i = 0; i < a.elems().size(); i += 1) {
            double aElem = a.elems().get(i);
            double bElem = b.elems().get(i);
            if (operands[1].equals("+")) {
                combined.add(aElem + bElem);
            } else {
                combined.add(aElem - bElem);
            }
        }
        MatrixObj m = new MatrixObj(a.rows(), a.cols(), combined);
        doPrint(m);
        if (operands.length > 3) {
            _matrices.put(operands[operands.length - 1], m);
        }

    }

    /** Saves matrix input by user. */
    void doSave(String[] operands) {
        double r = Double.parseDouble(operands[1]);
        double c = Double.parseDouble(operands[3]);
        if (operands.length - (r * c) != 6) {
            throw error("bad specification");
        }
        String name = operands[0];
        ArrayList<Double> nums = new ArrayList<Double>();
        for (int i = 5; i < operands.length - 1; i += 1) {
            nums.add(Double.parseDouble(operands[i]));
        }
        _matrices.put(name, new MatrixObj((int) r, (int) c, nums));
    }

    /** Exits calculator. */
    void doQuit(String[] operands) {
        System.exit(0);
    }

    /** Error for invalid commands. */
    void doError(String[] unused) {
        throw error("Command not understood");
    }

    /** Checks if a matrix was ever saved. */
    void checkExists(String name) {
        if (_matrices.get(name) == null) {
            throw error("matrix does not exist");
        }
    }

    /** Renames a matrix assuming that it exists. */
    void doRename(String[] operands) {
        String name = operands[1];
        String newName = operands[2];
        checkExists(name);
        MatrixObj temp = _matrices.get(name);
        _matrices.remove(name);
        _matrices.put(newName, temp);
    }

    /** Displays a matrix assuming that it exists. */
    void doShow(String[] operands) {
        String name = operands[0];
        checkExists(name);
        doPrint(_matrices.get(name));
    }

    /** Removes a saved matrix assuming that it exists. */
    void doRemove(String[] operands) {
        String name = operands[1];
        checkExists(name);
        _matrices.remove(name);
    }

    /** Clears all saved matrices. */
    void doReset(String[] operands) {
        _matrices.clear();
    }

    /** A zero matrix. */
    void doZero(String[] operands) {
        double r = Double.parseDouble(operands[1]);
        double c = Double.parseDouble(operands[3]);
        String name = operands[0];
        ArrayList<Double> nums = new ArrayList<Double>();
        for (int i = 0; i < r * c; i += 1) {
            nums.add((double) 0);
        }
        _matrices.put(name, new MatrixObj((int) r, (int) c, nums));
    }

    /** Identity matrix command. */
    void doIdentity(String[] operands) {
        int dims = Integer.parseInt(operands[1]);
        String name = operands[0];
        MatrixObj id = makeIdentity(dims);
        _matrices.put(name, id);
    }

    /** Creates an identity matrix of specified dimensions. */
    MatrixObj makeIdentity(int dims) {
        ArrayList<Double> nums = new ArrayList<Double>();
        int capacity = dims * dims;
        while (capacity > 0) {
            nums.add((double) 0);
            capacity -= 1;
        }
        for (int i = 0; i < nums.size(); i += dims + 1) {
            nums.set(i, (double) 1);
        }
        return new MatrixObj(dims, dims, nums);
    }

    /** Transposes a matrix. */
    void doTranspose(String[] operands) {
        checkExists(operands[0]);
        ArrayList<Double> transposed = new ArrayList<Double>();
        MatrixObj matrix = _matrices.get(operands[0]);
        for (int i = 0; i < matrix.cols(); i += 1) {
            for (int j = 0; j < matrix.rows(); j += 1) {
                transposed.add(matrix.matrix().get(j).get(i));
            }
        }
        MatrixObj m = new MatrixObj(matrix.cols(), matrix.rows(),
            transposed);
        doPrint(m);
        if (operands.length > 3) {
            String name = operands[operands.length - 1];
            _matrices.put(name, m);
        }
    }

    /** Computes cross product of two 3-dim vectors. */
    void doCrossProduct(String[] operands) {
        checkExists(operands[0]);
        checkExists(operands[2]);
        MatrixObj a = _matrices.get(operands[0]);
        MatrixObj b = _matrices.get(operands[2]);
        if ((a.rows() == 1 || a.cols() == 1)
            && (b.rows() == 1 || b.cols() == 1)) {
            if (a.elems().size() != b.elems().size()) {
                throw error("vectors must be same size for dot product");
            }
            if (a.elems().size() != 3) {
                throw error("cross product only for 3-dimensional vectors");
            }
            double i = a.elems().get(1) * b.elems().get(2)
                - a.elems().get(2) * b.elems().get(1);
            double j = a.elems().get(0) * b.elems().get(2)
                - a.elems().get(2) * b.elems().get(0);
            double k = a.elems().get(0) * b.elems().get(1)
                - a.elems().get(1) * b.elems().get(0);
            ArrayList<Double> vec = new ArrayList<>();
            vec.add(i); vec.add(-j); vec.add(k);
            MatrixObj m = new MatrixObj(3, 1, vec);
            doPrint(m);
            if (operands.length > 3) {
                String name = operands[operands.length - 1];
                _matrices.put(name, m);
            }
        } else {
            throw error("matrices must be vectors for cross product");
        }
    }

    /** Computes dot product of two vectors. */
    void doDotProduct(String[] operands) {
        checkExists(operands[0]);
        checkExists(operands[2]);
        MatrixObj a = _matrices.get(operands[0]);
        MatrixObj b = _matrices.get(operands[2]);
        if ((a.rows() == 1 || a.cols() == 1)
            && (b.rows() == 1 || b.cols() == 1)) {
            if (a.elems().size() != b.elems().size()) {
                throw error("vectors must be same size for dot product");
            }
            double total = 0;
            for (int i = 0; i < a.elems().size(); i += 1) {
                total += a.elems().get(i) * b.elems().get(i);
            }
            String d = Double.toString(total);
            int index = d.indexOf(".");
            if (d.charAt(index + 1) == '0') {
                System.out.println((int) total);
            } else {
                System.out.println(total);
            }
        } else {
            throw error("matrices must be vectors for dot product");
        }
    }

    /** Command for computing determinant of matrix. */
    void doDeterminant(String[] operands) {
        checkExists(operands[1]);
        MatrixObj m = _matrices.get(operands[1]);
        if (!m.isSquare()) {
            throw error("determinant only for square matrices");
        }
        double total = determinant(m);
        String d = Double.toString(total);
        int index = d.indexOf(".");
        if (d.charAt(index + 1) == '0') {
            System.out.println((int) total);
        } else {
            System.out.println(total);
        }
    }

    /** Calculation of a determinant. */
    double determinant(MatrixObj m) {
        if (m.rows() == 1) {
            return m.elems().get(0);
        } else if (m.rows() == 2) {
            return m.allRows()[0][0] * m.allRows()[1][1]
                - m.allRows()[0][1] * m.allRows()[1][0];
        }
        double total = 0;
        for (int i = 0; i < m.rows(); i += 1) {
            ArrayList<Double> a = new ArrayList<>();
            for (int j = 0; j < m.rows(); j += 1) {
                for (int k = 0; k < m.rows(); k += 1) {
                    if (j != 0 && k != i) {
                        a.add(m.allRows()[j][k]);
                    }
                }
            }
            total += Math.pow(-1, i) * m.allRows()[0][i]
                * determinant(new MatrixObj(m.rows() - 1,
                    m.rows() - 1, a));
        }
        return total;
    }

    /** Cofactor expansion matrix. */
    MatrixObj cofactor(MatrixObj m) {
        ArrayList<Double> dets = new ArrayList<>();
        for (int i = 0; i < m.rows(); i += 1) {
            for (int j = 0; j < m.rows(); j += 1) {
                ArrayList<Double> entries = new ArrayList<>();
                for (int k = 0; k < m.rows(); k += 1) {
                    if (i != k) {
                        for (int l = 0; l < m.rows(); l += 1) {
                            if (j != l) {
                                entries.add(m.allRows()[k][l]);
                            }
                        }
                    }
                }
                MatrixObj detMat = new MatrixObj(m.rows() - 1,
                    m.rows() - 1, entries);
                dets.add(Math.pow(-1, i + j) * determinant(detMat));
            }
        }
        return new MatrixObj(m.rows(), m.cols(), dets);
    }

    /** Command for computing cofactor expansion matrix. */
    void doCofactor(String[] operands) {
        checkExists(operands[1]);
        MatrixObj m = _matrices.get(operands[1]);
        if (!m.isSquare()) {
            throw error("cofactor expansion only for square matrices");
        }
        m = cofactor(m);
        doPrint(m);
        if (operands.length > 2) {
            String name = operands[operands.length - 1];
            _matrices.put(name, m);
        }
    }

    /** Command for computing RREF of matrix with steps displayed. */
    void doRREF(String[] operands) {
        checkExists(operands[1]);
        MatrixObj m
            = ReducerRREF.printRREF(_matrices.get(operands[1]));
        if (operands.length > 2) {
            String name = operands[3];
            _matrices.put(name, m);
        }
    }

    /** Computes inverse matrix. */
    void doInverse(String[] operands) {
        checkExists(operands[1]);
        MatrixObj m = _matrices.get(operands[1]);
        if (!m.isSquare()) {
            throw error("only square matrices have inverses");
        } else if (determinant(m) == 0) {
            throw error("determinant of 0 means no inverse");
        }
        MatrixObj id = makeIdentity(m.rows());
        ArrayList<Double> invArr = new ArrayList<>();
        for (int i = 0; i < m.rows(); i += 1) {
            invArr.addAll(m.matrix().get(i));
            invArr.addAll(id.matrix().get(i));
        }
        MatrixObj inverse = new MatrixObj(m.rows(), m.rows() * 2,
            invArr);
        inverse = ReducerRREF.rref(inverse);
        ArrayList<Double> rightHalf = new ArrayList<>();
        for (List<Double> lst : inverse.matrix()) {
            for (int i = lst.size() / 2; i < lst.size(); i += 1) {
                rightHalf.add(lst.get(i));
            }
        }
        inverse = new MatrixObj(m.rows(), m.rows(), rightHalf);
        doPrint(inverse);
        if (operands.length > 2) {
            String name = operands[operands.length - 1];
            _matrices.put(name, inverse);
        }
    }

    /** Loads commands from a file. */
    void doLoad(String[] operands) {
        try {
            Scanner reader = new Scanner(new File(operands[1]));
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line == null) {
                    break;
                }
                if (line.equals("")) {
                    continue;
                }
                try {
                    Command cmnd = Command.parseCommand(line);
                    _commands.get(cmnd.commandType()).accept(cmnd.operands());
                } catch (MatrixException excp) {
                    System.out.println(excp.getMessage());
                }
            }
        } catch (IOException e) {
            throw error("Cannot open file this file");
        }
    }

    /** All existing commands. */
    private final HashMap<Command.Type, Consumer<String[]>> _commands
        = new HashMap<>();
    {
        _commands.put(ADD, this::doAdd);
        _commands.put(SAVE, this::doSave);
        _commands.put(DISPLAY, this::doDisplay);
        _commands.put(INVERSE, this::doInverse);
        _commands.put(HELP, this::doHelp);
        _commands.put(COFACTOR, this::doCofactor);
        _commands.put(DOTPRODUCT, this::doDotProduct);
        _commands.put(CROSSPRODUCT, this::doCrossProduct);
        _commands.put(QUIT, this::doQuit);
        _commands.put(ERROR, this::doError);
        _commands.put(EOF, this::doQuit);
        _commands.put(RENAME, this::doRename);
        _commands.put(SHOW, this::doShow);
        _commands.put(REMOVE, this::doRemove);
        _commands.put(RESET, this::doReset);
        _commands.put(ZERO, this::doZero);
        _commands.put(IDENTITY, this::doIdentity);
        _commands.put(MULTIPLY, this::doMultiply);
        _commands.put(TRANSPOSE, this::doTranspose);
        _commands.put(DETERMINANT, this::doDeterminant);
        _commands.put(RREF, this::doRREF);
        _commands.put(LOAD, this::doLoad);
    }

    /** HashMap of saved matrices. */
    private HashMap<String, MatrixObj> _matrices =
        new HashMap<>();

}
