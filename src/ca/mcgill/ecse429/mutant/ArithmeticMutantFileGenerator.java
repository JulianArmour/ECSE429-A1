package ca.mcgill.ecse429.mutant;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArithmeticMutantFileGenerator {

    public static void main(String[] args) throws IOException {
        System.out.println(importMutants("C:\\Users\\Julian\\Desktop\\mutant-list.txt"));
    }

    public static SourceCode importSourceCode(String pathToSource) throws IOException {
        SourceCode src = new SourceCode();
        try (BufferedReader reader = new BufferedReader(new FileReader(pathToSource))) {
            String line;
            while ((line = reader.readLine()) != null) {
                src.appendLine(line);
            }
        }
        return src;
    }

    private static List<Mutant> importMutants(String pathToMutantFaultList) throws IOException{
        Pattern mutantLinePat = Pattern.compile("Line (\\d+): (.+;)");
        Pattern mutantPat = Pattern.compile(" {4}.+;");
        List<Mutant> mutants = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(pathToMutantFaultList))) {
            int originalCodeLineNumber = 0;//the line on which the mutant resides in the original source
            String originalCodeLine = "";//the line of code at mutantLineNumber in the original source
            int mutantId = 0;//id := the Nth mutant generated for a given mutantLine

            String line;
            while ((line = reader.readLine()) != null) {
                //lineMatcher pattern-matches when line that generates mutants is read
                Matcher lineMatcher = mutantLinePat.matcher(line);
                //mutantMatcher pattern-matches when a mutant is read
                Matcher mutantMatcher = mutantPat.matcher(line);
                if (lineMatcher.matches()) {
                    originalCodeLineNumber = Integer.parseInt(lineMatcher.group(1));
                    originalCodeLine = lineMatcher.group(2);
                } else if (mutantMatcher.matches()) {
                    Mutant mutant = new Mutant(
                            originalCodeLineNumber, mutantId, originalCodeLine, mutantMatcher.group().trim());
                    mutants.add(mutant);
                }
            }
        }
        return mutants;
    }
}
