package ca.mcgill.ecse429;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ArithmeticMutantGenerator {
    // to keep track of the + - * / operators
    private static Map<String, Integer> operatorCount = new HashMap<>();

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Invalid number of arguments");
            System.exit(-1);
        }

        //initialize operatorCount
        initOperatorCount();

        String programPath = args[0];
        String mutantLibraryPath = args[1];

        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader(programPath));
            writer = new PrintWriter(new BufferedWriter(new FileWriter(mutantLibraryPath)));

            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if(writeMutants(line.trim(), lineNumber, writer, operatorCount)) {
                    writer.println();
                }
            }

            // write the count of each operator mutation
            writer.println("----------Total Mutants----------");
            for (String op : new String[] {"+", "-", "*", "/"}) {
                writer.println(op + " mutant count: " + operatorCount.get(op));
            }


        } catch (IOException e) {
            System.out.println("Could not generate mutant file");
            System.exit(-2);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //ignore
                }
            }
            if (writer != null) writer.close();
        }
    }

    /**
     * Writes mutants to <i>writer</i> for each arithmetic operator found in <i>line</i>.
     *
     * @param line a String containing the line of code
     * @param lineNumber the line number of the line of code
     * @param writer a PrintWriter that has the mutant library file opened
     * @param operatorCount a Map containing the count of mutants created of each arithmetic operator
     * @return true if mutants were created, false otherwise
     */
    private static boolean writeMutants(String line, int lineNumber, PrintWriter writer, Map<String, Integer> operatorCount) {
        //check if the line contains arithmetic operators
        if (!line.matches(".*[+\\-*/].*")) {
            return false;
        }

        writer.println("Line "+lineNumber+": "+line);

        //iterate through the string, every time we find an arithmetic operator we create mutants.
        char[] lineCharArray = line.toCharArray();
        final char[] arithmeticOperators = new char[] {'+', '-', '*', '/'};

        for (int i = 0; i < lineCharArray.length; i++) {
            //check for + - * / operators
            for (char op : arithmeticOperators) {
                if (op == lineCharArray[i]) {
                    //this handles the first + in ++ or first - in --
                    if (lineCharArray[i] == lineCharArray[i+1]) {
                        writeIncDecMutant(writer, operatorCount, lineCharArray, i, op);
                    } else if (i == 0 || lineCharArray[i] != lineCharArray[i - 1]) {
                        writeArithmeticOperatorMutants(writer, operatorCount, lineCharArray, arithmeticOperators, i, op);
                    }
                    //operator found, no need to check for other operators at the same location
                    break;
                }
            }
        }
        return true;
    }

    /**
     * Writes several arithmetic mutants, at a given index, for a line of source code.
     *
     * @param writer a PrintWriter to write the mutant to
     * @param operatorCount a Map containing the count of mutants created of each arithmetic operator
     * @param lineCharArray the line of code to create mutants for
     * @param arithmeticOps an array of binary arithmetic operators
     * @param index the index in lineCharArray to insert the mutants
     * @param op the original operator at index
     */
    private static void writeArithmeticOperatorMutants(PrintWriter writer, Map<String, Integer> operatorCount,
                                                       char[] lineCharArray, char[] arithmeticOps, int index, char op) {
        for (char mutantOp : arithmeticOps) {
            //only write to file if changing the operator produces a mutant
            if (mutantOp != op) {
                // increment the mutant count
                String key = String.valueOf(mutantOp);
                operatorCount.replace(key, operatorCount.get(key) + 1);

                lineCharArray[index] = mutantOp;

                printMutant(writer, lineCharArray);
            }
        }
        //restore the changed operator
        lineCharArray[index] = op;
    }

    /**
     *
     * @param writer a PrintWriter to write the mutant to
     * @param operatorCount a Map containing the count of mutants created of each arithmetic operator
     * @param lineCharArray the line of code for which to insert the mutant
     * @param index the index where to insert the mutant
     * @param originalOperator the original operator before mutants were inserted
     */
    private static void writeIncDecMutant(PrintWriter writer, Map<String, Integer> operatorCount,
                                          char[] lineCharArray, int index, char originalOperator) {
        switch (lineCharArray[index]) {
            case '+':
                createIncDecOperatorMutant(operatorCount, lineCharArray, index, '-');
                break;
            case '-':
                createIncDecOperatorMutant(operatorCount, lineCharArray, index, '+');
                break;
            default:
                break;
        }

        printMutant(writer, lineCharArray);

        //restore changed operator
        lineCharArray[index] = originalOperator;
        lineCharArray[index + 1] = originalOperator;
    }

    /**
     * Writes the mutant (indented) to a file, appending a new line.
     *
     * @param writer a PrintWriter to write to
     * @param lineCharArray the line of code containing a mutant
     */
    private static void printMutant(PrintWriter writer, char[] lineCharArray) {
        writer.print("    ");
        writer.print(lineCharArray);
        writer.println();
    }

    /**
     * Inserts a inc/decrement operator mutant at a given index in a line of code.
     *
     * @param operatorCount a Map containing the count of mutants created of each arithmetic operator
     * @param lineCharArray the line of code where we want to insert the mutant
     * @param index the location in lineCharArray to insert the mutant
     * @param mutant the type of mutant to insert (e.g. '+' will replace the operator with '++')
     */
    private static void createIncDecOperatorMutant(Map<String, Integer> operatorCount, char[] lineCharArray,
                                                   int index, char mutant) {
        // increment the mutant count
        String key = String.valueOf(mutant);
        operatorCount.replace(key, operatorCount.get(key) + 1);
        //replace operator with mutant
        lineCharArray[index] = mutant;
        lineCharArray[index + 1] = mutant;
    }

    private static void initOperatorCount() {
        operatorCount.clear();
        operatorCount.put("+", 0);
        operatorCount.put("-", 0);
        operatorCount.put("*", 0);
        operatorCount.put("/", 0);
    }
}
