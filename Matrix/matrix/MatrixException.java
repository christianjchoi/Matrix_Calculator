package matrix;

/** A general error report exception for Matrix package.
 *  @author Christian Choi
 */
class MatrixException extends RuntimeException {

    MatrixException(String msg) {
        super(msg);
    }

    /** Returns a new exception with a message
     *  errMsg */
    static MatrixException error(String errMsg) {
        return new MatrixException(errMsg);
    }

}