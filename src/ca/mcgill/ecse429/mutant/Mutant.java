package ca.mcgill.ecse429.mutant;

public class Mutant {
    private int lineNumber;
    private int id;
    private String originalLine;
    private String mutantLine;

    public Mutant(int lineNumber, int id, String originalLine, String mutantLine) {
        this.lineNumber = lineNumber;
        this.id = id;
        this.originalLine = originalLine;
        this.mutantLine = mutantLine;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getId() {
        return id;
    }

    public String getOriginalLine() {
        return originalLine;
    }

    public String getMutantLine() {
        return mutantLine;
    }

    @Override
    public String toString() {
        return "Mutant{" +
                "lineNumber=" + lineNumber +
                ", id=" + id +
                ", originalLine='" + originalLine + '\'' +
                ", mutantLine='" + mutantLine + '\'' +
                '}';
    }
}
