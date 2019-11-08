package ca.mcgill.ecse429.mutant;

import java.io.FileOutputStream;
import java.nio.file.FileSystems;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides a means to mutate a {@link SourceCode} object and write it to a file. Can be ran in parallel with other
 * {@link MutantFileWriter}s via the run() method.
 */
public class MutantFileWriter implements Runnable {
    private Mutant mutant;
    private SourceCode originalSourceCode;
    private String folderPath;
    private String mutantFileBaseName;
    private Logger logger = Logger.getLogger(MutantFileWriter.class.getName());

    /**
     *
     * @param mutant the mutant to inject into the original source code.
     * @param originalSourceCode the source code for which to inject the mutant into.
     * @param folderPath a directory to write the mutated file to.
     * @param mutantFileBaseName a string which appears in every mutant file's filename (usually it's the original
     *                           source code's filename).
     */
    MutantFileWriter(Mutant mutant, SourceCode originalSourceCode, String folderPath, String mutantFileBaseName) {
        this.mutant = mutant;
        this.originalSourceCode = originalSourceCode;
        this.folderPath = folderPath;
        this.mutantFileBaseName = mutantFileBaseName;
    }

    /**
     * The driver method. Invoking this method injects the mutant into a copy of the source code and writes it to the
     * output directory.
     */
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
