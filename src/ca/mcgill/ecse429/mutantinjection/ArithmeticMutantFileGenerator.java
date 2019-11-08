package ca.mcgill.ecse429.mutantinjection;

import ca.mcgill.ecse429.mutantgeneration.ArithmeticMutantFaultListGenerator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArithmeticMutantFileGenerator {

    public static void main(String[] args) throws IOException, InterruptedException {
        String sourceCodePath = args[0];//the actual source code file that mutants are generated from
        String sourceCodeFileName = FileSystems.getDefault().getPath(sourceCodePath).getFileName().toString();
        String mutantFaultListPath = args[1];
        String mutantOutputDirectoryPath = args[2];

        //create the output directory for the mutants if it doesn't already exist
        Path mutantOutputDirectory = FileSystems.getDefault().getPath(mutantOutputDirectoryPath);
        if (!mutantOutputDirectory.toFile().exists()) {
            Files.createDirectory(mutantOutputDirectory);
        }

        SourceCode originalSource = importSourceCode(sourceCodePath);
        List<Mutant> mutants = importMutants(mutantFaultListPath);

        //Writing many mutated files can be done in parallel. Which is good since there can be a lot of mutants.
        List<Thread> mutantFileWriters = new LinkedList<>();
        for (Mutant mutant : mutants) {
            MutantFileWriter mutantFileWriter = new MutantFileWriter(
                    mutant, originalSource, mutantOutputDirectoryPath, sourceCodeFileName);

            Thread mutantFileWriterThread = new Thread(mutantFileWriter);
            mutantFileWriters.add(mutantFileWriterThread);
            mutantFileWriterThread.start();
        }
        for (Thread mutantFileWriter : mutantFileWriters) {
            mutantFileWriter.join();
        }
    }

    /**
     *
     * @param sourceCodePath a path to a source code file.
     * @return a {@link SourceCode} object representing the source code.
     * @throws IOException if the file cannot be read.
     */
    private static SourceCode importSourceCode(String sourceCodePath) throws IOException {
        SourceCode src = new SourceCode();
        try (BufferedReader reader = new BufferedReader(new FileReader(sourceCodePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                src.append(line);
            }
        }
        return src;
    }

    /**
     *
     * @param pathToMutantFaultList a path to a Mutant Fault List in the format
     *                              output by {@link ArithmeticMutantFaultListGenerator}.
     * @return a {@link List} of {@link Mutant} objects.
     * @throws IOException if the Mutant Fault List cannot be read.
     */
    private static List<Mutant> importMutants(String pathToMutantFaultList) throws IOException{
        //regex patterns for parsing out mutant data
        Pattern mutantLinePat = Pattern.compile("Line (\\d+): .+;");
        Pattern mutantPat = Pattern.compile(" {4}.+;");
        List<Mutant> mutants = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(pathToMutantFaultList))) {
            int mutantLineNumber = 0;//the line number in the original source for which a mutant can be inserted
            int mutantId = 0;//id := the Nth mutant generated for a given mutantLineNumber

            String line;
            while ((line = reader.readLine()) != null) {
                //lineMatcher pattern-matches when line that generates mutants is read
                Matcher lineMatcher = mutantLinePat.matcher(line);
                //mutantMatcher pattern-matches when a mutant is read
                Matcher mutantMatcher = mutantPat.matcher(line);
                if (lineMatcher.matches()) {
                    mutantLineNumber = Integer.parseInt(lineMatcher.group(1));
                    mutantId = 0;
                } else if (mutantMatcher.matches()) {
                    Mutant mutant = new Mutant(
                            mutantLineNumber, mutantId++, mutantMatcher.group().trim());
                    mutants.add(mutant);
                }
            }
        }
        return mutants;
    }
}
