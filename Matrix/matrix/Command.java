package matrix;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/** All things to do with parsing commands.
 *  @author Christian Choi
 *  Credits for class design to Paul Hilfinger
 */

class Command {

    static enum Type {
        ADD("\\w+\\s+[+|-]\\s+\\w+(\\s+[-][>]\\s+\\w+)?"),
        MULTIPLY("[-]?\\w+([.]\\d+)?\\s+[*]\\s+[-]?\\w+([.]\\d+)?(\\s+?[-][>]\\s+\\w+)?"),
        TRANSPOSE("\\w+\\s+\\^\\s+T(\\s+?[-][>]\\s+?\\w+)?"),
        INVERSE("inverse\\s+\\w+(\\s+?[-][>]\\s+?\\w+)?"),
        DOTPRODUCT("\\w+\\s+dot\\s+\\w+"),
        CROSSPRODUCT("\\w+\\s+x\\s+\\w+(\\s+?[-][>]\\s+?\\w+)?"),
        COFACTOR("cofactor\\s+\\w+(\\s+?[-][>]\\s+?\\w+)?"),
        SAVE("\\w+\\s+\\d+\\s+[x]\\s+\\d+\\s+[{]\\s+([-]?\\d+([.]\\d+)?\\s+)+[}]"),
        ZERO("\\w+\\s+\\d+\\s+[x]\\s+\\d+\\s+zero"),
        DETERMINANT("det\\s+\\w+"),
        RREF("rref\\s+\\w+(\\s+?[-][>]\\s+?\\w+)?"),
        RENAME("rename\\s+\\w+\\s+\\w+"),
        SHOW("\\w+\\s+?[?]"),
        IDENTITY("\\w+\\s+\\d+\\s+identity"),
        REMOVE("remove\\s+\\w+"),
        QUIT("quit"),
        RESET("reset"),
        HELP("help"),
        DISPLAY("display"),
        LOAD("load\\s+((.)+)"),
        ERROR(".*"),
        EOF;

        /** PATTERN is a regular expression string giving the syntax of
         *  a command of the given type.  It matches the entire command,
         *  assuming no leading or trailing whitespace.  The groups in
         *  the pattern capture the operands (if any). */
        Type(String pattern) {
            _pattern = Pattern.compile(pattern.toLowerCase() + "$");
        }

        /** A Type whose pattern is the lower-case version of its name
         *  (with no arguments). */
        Type() {
            _pattern = Pattern.compile(this.toString().toLowerCase() + "$");
        }

        /** The Pattern describing syntactically correct versions of this
         *  type of command. */
        private final Pattern _pattern;

    }

    /** A new Command of type TYPE with OPERANDS as its operands. */
    Command(Type type, String... operands) {
        _type = type;
        _operands = operands;
    }

    /** Return the type of this Command. */
    Type commandType() {
        return _type;
    }

    /** Returns this Command's operands. */
    String[] operands() {
        return _operands;
    }

    /** Parse COMMAND, returning the command and its operands.
     *  COMMAND is assumed to be trimmed of all leading and
     *  trailing whitespace.  */
    static Command parseCommand(String command) {
        if (command == null || command.equals("<EOF>")) {
            return new Command(Type.EOF);
        }
        command = command.toLowerCase();
        command = command.trim();
        for (Type type : Type.values()) {
            Matcher mat = type._pattern.matcher(command);
            if (mat.matches()) {
                String[] operands = command.split("\\s+");
                return new Command(type, operands);
            }
        }
        throw new
            Error("Error command did not match.");
    }

    /** The command name. */
    private final Type _type;
    /** Command arguments. */
    private final String[] _operands;
}
