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

        try {
            BufferedReader reader = new BufferedReader(new FileReader(programPath));
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(mutantLibraryPath)));

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

            reader.close();
            writer.close();
        } catch (IOException e) {
            System.out.println("Could not generate mutant file");
            e.printStackTrace();
            System.exit(-2);
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
    private static Boolean writeMutants(String line, int lineNumber, PrintWriter writer, Map<String, Integer> operatorCount) {
        //check if the line contains arithmetic operators
        if (!line.matches(".*[+\\-*/].*")) {
            return false;
        }

        writer.println("Line "+lineNumber+": "+line);

        //iterate through the string, every time we find an arithmetic operator we create mutants.
        char[] charStr = line.toCharArray();
        final char[] ops = new char[] {'+', '-', '*', '/'};

        for (int i = 0; i < charStr.length; i++) {
            //check for + - * / operators
            for (char op : ops) {
                if (op == charStr[i]) {
                    //handle the inc/decrement: "variable++" or "++variable" kind
                    //of operator found in many languages, so that a mutant is created only ONCE for it.
                    // (+ appear twice in ++, creating the mutant "var+-" makes no sense)
//                    if (i > 0 && op == charStr[i-1]) {
//                        //ignore the second + in ++ or second - in --
//                        break;
//                    }

                    //this handles the first + in ++ or first - in --
                    if (charStr[i+1] == charStr[i]) {
                        switch (charStr[i]) {
                            case '+': {
                                // increment the mutant count
                                String key = "-";
                                operatorCount.replace(key, operatorCount.get(key) + 1);
                                charStr[i] = '-';
                                charStr[i + 1] = '-';
                                break;
                            }
                            case '-': {
                                // increment the mutant count
                                String key = "+";
                                operatorCount.replace(key, operatorCount.get(key) + 1);
                                charStr[i] = '+';
                                charStr[i + 1] = '+';
                                break;
                            }
                        }

                        writer.print("    ");
                        writer.print(charStr);
                        writer.println();

                        //restore changed operator
                        charStr[i] = op;
                        charStr[i + 1] = op;

                        //skip over the next character since it's part of the operator we just handled
                        i++;

                        break;
                    }

                    for (char mutantOp : ops) {
                        //only print if changing the operator produces a mutant
                        if (mutantOp != op) {
                            // increment the mutant count
                            String key = String.valueOf(mutantOp);
                            operatorCount.replace(key, operatorCount.get(key) + 1);

                            charStr[i] = mutantOp;

                            writer.print("    ");
                            writer.print(charStr);
                            writer.println();
                        }
                    }
                    //restore the changed operator
                    charStr[i] = op;
                    //operator found, no need to check for other operators at the same location
                    break;
                }
            }
        }
        return true;
    }

    private static void initOperatorCount() {
        operatorCount.clear();
        operatorCount.put("+", 0);
        operatorCount.put("-", 0);
        operatorCount.put("*", 0);
        operatorCount.put("/", 0);
    }
}
