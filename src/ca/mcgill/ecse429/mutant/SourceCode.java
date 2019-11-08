package ca.mcgill.ecse429.mutant;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides an in-memory representation of a source file.
 */
public class SourceCode {
    private List<String> lines;

    /**
     * Creates a blank SourceCode object.
     */
    SourceCode() {
        this.lines = new ArrayList<>();
    }

    private SourceCode(List<String> lines) {
        this.lines = lines;
    }

    /**
     * @return a copy of this SourceCode object. Mutating this new object will have no effect on the one it was copied from.
     */
    SourceCode copy() {
        return new SourceCode(new ArrayList<>(this.lines));
    }

    /**
     * @return This object as one String.
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : lines) {
            stringBuilder.append(line).append("\n");
        }
        return stringBuilder.toString();
    }

    void append(String lineOfCode) {
        this.lines.add(lineOfCode);
    }

    /**
     * Replaces an existing line of code with a new one. Note the first line of code is at lineNumber = 1.
     *
     * @param lineNumber the line in the source code to replace the existing line with newLine.
     * @param newLine    the new line of code to replace the existing one.
     */
    void replaceLine(int lineNumber, String newLine) {
        this.lines.set(lineNumber - 1, newLine);
    }

    /**
     * @param lineNumber the line number for which to return the line of code.
     * @return the line of code at lineNumber. Note that the first line of code is at lineNumber = 1.
     */
    String getLine(int lineNumber) {
        return lines.get(lineNumber - 1);
    }
}
