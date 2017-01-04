package matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;


/** Represents a matrix.
 *  @author Christian Choi
 */
class MatrixObj {

    /** Number of rows and cols. */
    private final int _rows, _cols;

    /** Array of all elements of matrix. */
    private ArrayList<Double> _elems;

    /** Nested list representation of matrix. */
    private ArrayList<List<Double>> _matrix;

    /** Multi-dimensional array representation for matrix rows. */
    private double[][] allRows;

    /** Multi-dimensional array representation for matrix cols. */
    private double[][] allCols;

    MatrixObj(int rows, int cols, ArrayList<Double> elems) {
        _rows = rows;
        _cols = cols;
        _elems = elems;
        _matrix = new ArrayList<List<Double>>();
        int capacity = _rows;
        while (capacity > 0) {
            _matrix.add(null);
            capacity -= 1;
        }
        int start = 0;
        int end = _cols;
        for (int i = 0; i < _rows; i += 1) {
            List<Double> each = _elems.subList(start, end);
            _matrix.set(i, each);
            start = end;
            end += _cols;
        }
        allRows = new double[_rows][_cols];
        allCols = new double[_cols][_rows];
        for (int i = 0; i < _rows; i += 1) {
            for (int j = 0; j < _cols; j += 1) {
                allRows[i][j] = _matrix.get(i).get(j);
            }
        }
        for (int i = 0; i < _cols; i += 1) {
            for (int j = 0; j < _rows; j += 1) {
                allCols[i][j] = _matrix.get(j).get(i);
            }
        }
    }

    /** Getter method for number of rows. */
    public int rows() {
        return _rows;
    }

    /** Getter method for number of cols. */
    public int cols() {
        return _cols;
    }

    /** Getter method for list of elements. */
    public ArrayList<Double> elems() {
        return _elems;
    }

    /** Getter method for list representation of matrix. */
    public ArrayList<List<Double>> matrix() {
        return _matrix;
    }

    /** The dimensions of this matrix. */
    public String dim() {
        return Integer.toString(rows())
            + " x "
            + Integer.toString(cols());
    }

    /** Getter method for rows of this matrix. */
    public double[][] allRows() {
        return allRows;
    }

    /** Getter method for cols of this matrix. */
    public double[][] allCols() {
        return allCols;
    }

    /** String representation of this matrix. */
    public String toString() {
        String matrix = "";
        for (int i = 0; i < rows(); i += 1) {
            String row = "|\t";
            for (double j : _matrix.get(i)) {
                j = Math.round(j * 100.0) / 100.0;
                String d = Double.toString(j);
                if (d.charAt(d.length() - 1) == '0') {
                    row += Integer.toString((int) j) + "\t";
                } else {
                    row += d + "\t";
                }
            }
            row += "|";
            if (i == rows() - 1) {
                matrix += row;
            } else {
                matrix += row + "\n";
            }
        }
        return matrix;
    }

    /** Whether or not this matrix is a square matrix. */
    public boolean isSquare() {
        return rows() == cols();
    }

    /** Returns an array of indices of repeated rows. */
    public ArrayList<Integer> repeats() {
        ArrayList<Integer> reps = new ArrayList<>();
        for (int i = 0; i < rows(); i += 1) {
            for (int j = 0; j < rows(); j += 1) {
                if (i != j && Arrays.equals(allRows()[i],
                    allRows()[j])) {
                    if (!reps.contains(i)) {
                        reps.add(i);
                    }
                    if (!reps.contains(j)) {
                        reps.add(j);
                    }
                }
            }
        }
        return reps;
    }

    /** Returns this matrix with any repeated rows removed. */
    public MatrixObj removeRepeats() {
        if (repeats().size() <= 1) {
            return this;
        }
        ArrayList<List<Double>> newMat =
            new ArrayList<List<Double>>(matrix());
        for (int i = 1; i < repeats().size(); i += 1) {
            int repIndex = repeats().get(i);
            List<Double> row = newMat.get(repIndex);
            for (int j = 0; j < row.size(); j += 1) {
                row.set(j, (double) 0);
            }
            newMat.set(repIndex, row);
        }
        ArrayList<Double> rmElems = new ArrayList<>();
        for (List<Double> lst : newMat) {
            rmElems.addAll(lst);
        }
        return new MatrixObj(rows(), cols(), rmElems);
    }

    /** Helper method for GCD of two numbers. */
    private double gcd(double a, double b) {
        if (b == 0) {
            return Math.abs(a);
        }
        return gcd(b, a % b);
    }

    /** Helper method for GCD of a list of numbers. */
    private double gcd(List<Double> lst) {
        double init = lst.get(0);
        for (double d : lst) {
            init = gcd(init, d);
        }
        return init;
    }

    /** Reduces this matrix. */
    public MatrixObj reduced() {
        ArrayList<Double> newRows = new ArrayList<>();
        for (List<Double> row : matrix()) {
            if (Collections.max(row) <= 0 || row.get(0) < 0) {
                for (int i = 0; i < row.size(); i += 1) {
                    row.set(i, -row.get(i));
                }
            }
            if (nonZeroIndex(row) != -1) {
                row.set(nonZeroIndex(row), (double) 1);
            }
            for (int i = 0; i < row.size(); i += 1) {
                double addThis;
                if (row.get(i) != 0) {
                    if (!decimalCheck(row)) {
                        addThis = row.get(i) / gcd(row);
                    } else {
                        addThis = row.get(i);
                    }
                } else if (Math.abs(row.get(i)) < 1E-15
                    || row.get(i) == 0) {
                    addThis = 0;
                } else {
                    addThis = 0;
                }
                newRows.add(addThis);
            }
        }
        return new MatrixObj(rows(), cols(), newRows);
    }

    /** Helper method for finding index of leading coefficient
     * for a row of this matrix. */
    public static int leadIndex(List<Double> row) {
        for (int i = 0; i < row.size(); i += 1) {
            if (row.get(i) != 0) {
                return i;
            }
        }
        return Integer.MAX_VALUE;
    }

    /** Helper method for finding index of only non-zero
     * entry of a row, if the row has only one non-zero entry. */
    public int nonZeroIndex(List<Double> row) {
        int zeroCount = 0;
        for (int i = 0; i < row.size(); i += 1) {
            if (row.get(i) == 0) {
                zeroCount += 1;
            }
        }
        if (zeroCount == row.size() - 1) {
            double max = Collections.max(row);
            return row.indexOf(max);
        }
        return -1;
    }

    /** Checks whether the row contains any decimals. */
    public boolean decimalCheck(List<Double> lst) {
        for (double i : lst) {
            String d = Double.toString(i);
            if (d.charAt(d.length() - 1) != '0') {
                return true;
            }
        }
        return false;
    }

    /** Reorders matrix rows to agree with RREF. */
    public MatrixObj reorder() {
        Stack<List<Double>> sorted = new Stack<>();
        ArrayList<List<Double>> copy = new ArrayList<>();
        for (List<Double> lst : matrix()) {
            copy.add(new ArrayList<Double>(lst));
        }
        while (copy.size() != 0) {
            int max = Integer.MIN_VALUE;
            int index = 0;
            for (int i = 0; i < copy.size(); i += 1) {
                List<Double> lst = copy.get(i);
                if (leadIndex(lst) > max) {
                    max = leadIndex(lst);
                    index = i;
                }
            }
            sorted.push(copy.get(index));
            copy.remove(index);
        }
        ArrayList<Double> sortedMat = new ArrayList<>();
        while (!sorted.isEmpty()) {
            sortedMat.addAll(sorted.pop());
        }
        return new MatrixObj(rows(), cols(), sortedMat);
    }

}
