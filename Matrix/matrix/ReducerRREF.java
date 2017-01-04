package matrix;

import java.util.ArrayList;
import java.util.List;

/** Class for RREF computation utilities
 * @author Christian Choi */

class ReducerRREF {

    /** Contains string representation of matrix at each step. */
    private static ArrayList<String> rrefSteps
        = new ArrayList<>();

    /** Contains labels of calculations for each step. */
    private static ArrayList<String> labels
        = new ArrayList<>();

    /** Prints all steps involved in reaching RREF. */
    static MatrixObj printRREF(MatrixObj m) {
        clearMsg();
        MatrixObj mat = rref(m);
        for (int i = 0; i < rrefSteps.size(); i += 1) {
            System.out.println(labels.get(i));
            System.out.println(rrefSteps.get(i));
            System.out.println("");
        }
        clearMsg();
        return mat;
    }

    /** Algorithm for computing RREF. */
    static MatrixObj rref(MatrixObj m) {
        rrefSteps.add(m.toString());
        labels.add("Initial");
        MatrixObj temp = m;
        m = m.reduced().removeRepeats();
        if (!m.toString().equals(temp.toString())) {
            labels.add("Reduced");
            rrefSteps.add(m.toString());
        }
        ArrayList<List<Double>> rrefMatrix
            = new ArrayList<>(m.matrix());
        for (int i = 0; i < m.rows(); i += 1) {
            List<Double> row = rrefMatrix.get(i);
            int leadIndex = MatrixObj.leadIndex(row);
            if (leadIndex != Integer.MAX_VALUE) {
                double leadNum = row.get(leadIndex);
                for (int j = 0; j < m.rows(); j += 1) {
                    List<Double> compRow = rrefMatrix.get(j);
                    int compLeadIndex = MatrixObj.leadIndex(compRow);
                    if (i != j && compLeadIndex <= leadIndex) {
                        double compLeadNum = compRow.get(leadIndex);
                        double scale = compLeadNum / leadNum;
                        for (int k = 0; k < row.size(); k += 1) {
                            double addThis = row.get(k) * scale;
                            if (Math.abs(compRow.get(k) - addThis) < 1E-10) {
                                compRow.set(k, (double) 0);
                            } else {
                                compRow.set(k, compRow.get(k) - addThis);
                            }
                        }
                        String instruction = rrefStepStr(scale, " - ", j, i);
                        rrefMatrix.set(j, compRow);
                        MatrixObj mRREF = stepMatrix(rrefMatrix);
                        String str = mRREF.toString();
                        mRREF = mRREF.reduced().removeRepeats();
                        String rrStr = mRREF.toString();
                        if (scale != 0) {
                            rrefSteps.add(str);
                            labels.add(instruction);
                        }
                        if (!str.equals(rrStr)) {
                            rrefSteps.add(rrStr);
                            labels.add("Reduced");
                        }
                        rrefMatrix = mRREF.matrix();
                    }
                }
            }
        }
        return fixUpRREF(rrefMatrix);
    }

    /** Helper method for generating label for each calculation
     * at each step. */
    private static String rrefStepStr(double fact,
        String op, int j, int i) {
        if (fact < 0) {
            op = " + ";
            fact = -fact;
        }
        String changeRow = "R" + Integer.toString(j + 1);
        String multRow = "R" + Integer.toString(i + 1);
        String mult = "";
        String scaleStr = Double.toString(fact);
        if (scaleStr.charAt(scaleStr.length() - 1) != '0') {
            double rounded = Math.round(fact * 100.0) / 100.0;
            mult = Double.toString(rounded);
        } else {
            mult = Integer.toString((int) fact);
        }
        String instruction = changeRow
            + " -> " + changeRow + op + multRow + " * " + mult;
        return instruction;
    }

    /** Helper method for final fixup before completion of
      * RREF algorithm. */
    private static MatrixObj fixUpRREF(ArrayList<List<Double>> arr) {
        MatrixObj mRREF = stepMatrix(arr);
        String lastStr = mRREF.toString();
        ArrayList<List<Double>> finalMat = mRREF.matrix();
        for (int i = 0; i < finalMat.size(); i += 1) {
            List<Double> lst = finalMat.get(i);
            int lead = MatrixObj.leadIndex(lst);
            if (lead != Integer.MAX_VALUE) {
                double num = lst.get(lead);
                for (int j = 0; j < lst.size(); j += 1) {
                    if (lst.get(j) != 0) {
                        lst.set(j, lst.get(j) / num);
                    }
                }
            }
            finalMat.set(i, lst);
        }
        MatrixObj mat = stepMatrix(finalMat);
        if (!mat.toString().equals(lastStr)) {
            rrefSteps.add(mat.toString());
            labels.add("Reduced");
        }
        MatrixObj rMat = mat.reorder();
        if (!mat.toString().equals(rMat.toString())) {
            rrefSteps.add(rMat.toString());
            labels.add("Reordered");
        }
        return rMat;
    }

    /** Helper method for determining if two numbers have
     * the same sign. */
    private static boolean sameSign(double x, double y) {
        return (x >= 0 && y >= 0)
            || (x < 0 && y < 0);
    }

    /** Helper method for generating a matrix at each
     * step along the RREF algorithm. */
    private static MatrixObj stepMatrix(ArrayList<List<Double>> arr) {
        ArrayList<Double> rrefElems = new ArrayList<>();
        for (List<Double> lst : arr) {
            rrefElems.addAll(lst);
        }
        int r = arr.size();
        int c = arr.get(0).size();
        return new MatrixObj(r, c, rrefElems);
    }

    /** Clears arraylists containing matrix string representations
     * and labels of calculations for each step. */
    private static void clearMsg() {
        rrefSteps.clear();
        labels.clear();
    }

}

