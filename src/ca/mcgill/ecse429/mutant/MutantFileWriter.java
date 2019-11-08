package ca.mcgill.ecse429.mutant;

import java.io.FileOutputStream;
import java.nio.file.FileSystems;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MutantFileWriter implements Runnable {
    private Mutant mutant;
    private SourceCode originalSourceCode;
    private String folderPath;
    private String mutantFileBaseName;
    private Logger logger = Logger.getLogger(MutantFileWriter.class.getName());

    MutantFileWriter(Mutant mutant, SourceCode originalSourceCode, String folderPath, String mutantFileBaseName) {
        this.mutant = mutant;
        this.originalSourceCode = originalSourceCode;
        this.folderPath = folderPath;
        this.mutantFileBaseName = mutantFileBaseName;
    }

    @Override
    public void run() {
        SourceCode infectedSource = originalSourceCode.copy();
        this.mutant.injectInto(infectedSource);
        String infectedSourceStr = infectedSource.toString();

        String outputFile = folderPath + FileSystems.getDefault().getSeparator() + "Line-" + mutant.getLineNumber() +
                "-Mutant-" + mutant.getId() + "-" + mutantFileBaseName;
        try (FileOutputStream output = new FileOutputStream(outputFile)) {
            output.write(infectedSourceStr.getBytes());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not create mutant file for " + mutant);
        }
    }
}
