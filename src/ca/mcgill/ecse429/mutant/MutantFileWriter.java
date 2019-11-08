package ca.mcgill.ecse429.mutant;

public class MutantFileWriter implements Runnable {
    private Mutant mutant;
    private SourceCode originalSourceCode;
    private String folderPath;
    private String mutantFileBaseName;

    public MutantFileWriter(Mutant mutant, SourceCode originalSourceCode, String folderPath, String mutantFileBaseName) {
        this.mutant = mutant;
        this.originalSourceCode = originalSourceCode;
        this.folderPath = folderPath;
        this.mutantFileBaseName = mutantFileBaseName;
    }

    @Override
    public void run() {
        SourceCode infectedSource = originalSourceCode.copy();

    }
}
