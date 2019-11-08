package ca.mcgill.ecse429.mutant;

/**
 * An object representing a software mutant. It can be injected into a {@link SourceCode}.
 */
public class Mutant {
    private int lineNumber;
    private int id;
    private String mutantLine;

    /**
     *
     * @param lineNumber The line number in the original source where this mutant was generated.
     * @param id A special identifier for a class of mutants generated for a given line in a source.
     * @param mutantLine The mutated line of code.
     */
    Mutant(int lineNumber, int id, String mutantLine) {
        this.lineNumber = lineNumber;
        this.id = id;
        this.mutantLine = mutantLine;
    }

    /**
     * injects the mutant into the {@link SourceCode} object
     * @param sourceCode a source to inject this mutant into
     */
    void injectInto(SourceCode sourceCode) {
        String originalLine = sourceCode.getLine(lineNumber);
        //insert the mutant while preserving tabulation
        String mutatedLine = originalLine.replace(originalLine.trim(), mutantLine);
        sourceCode.replaceLine(lineNumber, mutatedLine);
    }

    /**
     * @return The line number in the original source where this mutant was generated.
     */
    int getLineNumber() {
        return lineNumber;
    }

    /**
     * @return A special identifier for a class of mutants generated for a given line in a source.
     */
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
