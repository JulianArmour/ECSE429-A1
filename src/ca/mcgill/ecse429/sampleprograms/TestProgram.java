package ca.mcgill.ecse429.sampleprograms;

public class TestProgram {
    public static void main(String[] args) {
        int sum = 0;
        for (String num : args) {
            sum += Integer.parseInt(num);
        }
        System.out.println(sum / args.length);
    }
}
