package ca.mcgill.ecse429.mutant;

import java.util.ArrayList;
import java.util.List;

public class SourceCode {
    private List<String> lines;

    public SourceCode() {
        this.lines = new ArrayList<>();
    }

    public int getLineCount() {
        return lines.size();
    }

    public void appendLine(String lineOfCode) {
        this.lines.add(lineOfCode);
    }

    public void deleteLine(int lineNumber) {
        this.lines.remove(lineNumber);
    }
}
