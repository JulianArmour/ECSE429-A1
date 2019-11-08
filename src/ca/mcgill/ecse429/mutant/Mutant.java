package ca.mcgill.ecse429.mutant;

public class Mutant {
    private int lineNumber;
    private int id;
    private String mutantLine;

    Mutant(int lineNumber, int id, String mutantLine) {
        this.lineNumber = lineNumber;
        this.id = id;
        this.mutantLine = mutantLine;
    }

    void injectInto(SourceCode sourceCode) {
        String originalLine = sourceCode.getLine(lineNumber);
        //insert the mutant while preserving tabulation
        String mutatedLine = originalLine.replace(originalLine.trim(), mutantLine);
        sourceCode.replaceLine(lineNumber, mutatedLine);
    }

    int getLineNumber() {
        return lineNumber;
    }

    int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Mutant{" +
                "lineNumber=" + lineNumber +
                ", id=" + id +
                ", mutantLine='" + mutantLine + '\'' +
                '}';
    }
}
