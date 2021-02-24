import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            List<String> results = getResults(scanner.nextLine());
            if (results.size() == 0)
                continue;
            for (String line : results) {
                System.out.println(line);
            }
        }
    }

    private static List<String> getResults(String line) {
        List<String> expressionList = getExpressions(line);
        List<String> resultList = new ArrayList<>();
        for (String expression : expressionList) {
            if (Math.abs(computeExpression(expression) - 100) < 1.0e-5) {
                resultList.add(expression + " = 100");
            }
        }

        return resultList;
    }

    private static double computeExpression(String expression) {
        Stack<String> stack = new Stack<>();
        String[] postfixRecord = getPostfixRecord(expression).split("\s");
        for (String token : postfixRecord) {
            if (token.matches("[0-9]+"))
                stack.push(token);
            else {
                double a = Double.parseDouble(stack.pop());
                double b = Double.parseDouble(stack.pop());
                double c = calculate(b, a, token.charAt(0));
                stack.push(Double.toString(c));
            }
        }

        return Double.parseDouble(stack.pop());
    }

    private static String getPostfixRecord(String expression) {
        StringBuilder postfixRecord = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        for (var i = 0; i < expression.length(); i++) {
            Character token = expression.charAt(i);
            if (Character.isDigit(token))
                postfixRecord.append(token);
            else {
                while (!stack.isEmpty() && getPriority(stack.peek()) >= getPriority(token))
                    postfixRecord.append(" ").append(stack.pop());
                stack.push(token);
                postfixRecord.append(" ");
            }

        }
        while (!stack.isEmpty())
            postfixRecord.append(" ").append(stack.pop());

        return postfixRecord.toString();
    }

    private static int getPriority(char ch) {
        if (ch == '*' || ch == '/')
            return 1;
        return 0;
    }

    private static List<String> getExpressions(String line) {
        String formatString = "%0" + (line.length() - 1) + "d";
        char[] charList = line.toCharArray();
        int count = (int) Math.pow(5, line.length() - 1);
        List<String> expressionList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String code = Integer.toString(i, 5);
            code = String.format(formatString, Integer.parseInt(code));
            StringBuilder stringBuilder = new StringBuilder();
            for (int k = 0; k < line.length() - 1; k++) {
                stringBuilder.append(charList[k]);
                if (code.charAt(k) < '5' && code.charAt(k) > '0')
                    stringBuilder.append(getOperationById(code.charAt(k)));
            }
            stringBuilder.append(charList[charList.length - 1]);
            expressionList.add(stringBuilder.toString());
        }

        return expressionList;
    }

    private static char getOperationById(char ch) {
        if (ch == '1')
            return '+';
        else if (ch == '2')
            return '-';
        else if (ch == '3')
            return '*';
        else if (ch == '4')
            return '/';

        return '\0';
    }

    private static double calculate(double x, double y, char operator) {
        if (operator == '+')
            return x + y;
        else if (operator == '-')
            return x - y;
        else if (operator == '*')
            return x * y;
        else if (operator == '/' && y != 0)
            return x / y;

        return 0;
    }

}
