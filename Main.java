    /**Cam Lischke
     * Program Assignment 2
     * CSC 221-Fall 2019
     * 9/11/2019
     *
     *  This program reads a series of infix expressions from an external file called "expressions.txt".
     *  For each expression, it will output the original infix expression, convert the expression to postfix and output the postfix
     *  expression, evaluate the postfix expression and output the answer as an integer, and convert the postfix expression back to
     *  infix and output the infix expression.
     */

    package com.company;

    import java.io.FileInputStream;
    import java.io.FileNotFoundException;
    import java.util.Scanner;
    import java.util.Stack;
    import java.lang.String;

    public class Main {

        public static void main(String[] args) throws FileNotFoundException {
            FileInputStream input = new FileInputStream("expressions.txt");
            Scanner in = new Scanner(input);
            Stack<String> stack = new Stack<String>();


            //This code will produce a postfix expression from the inputted infix expression

            while (in.hasNextLine()) {
                String result = "";
                String nextLine = in.nextLine();
                System.out.println("Original Infix Expression: " + nextLine);
                Scanner line = new Scanner(nextLine);
                while (line.hasNext()) {
                    String next = line.next();
                    char type = next.charAt(0);
                    if (Character.isLetterOrDigit(type) || next.length() != 1)            //operand
                        result += next + " ";
                    else if (next.equals("("))
                        stack.push(next);
                    else if (next.equals(")")) {
                        while (!stack.empty() && !stack.peek().equals("("))
                            result += stack.pop() + " ";
                    } else {                                            //operator
                        while (!stack.empty() && precedence(next) <= precedence(stack.peek()))
                            result += stack.pop() + " ";
                        stack.push(next);
                    }
                }
                while (!stack.empty()) {
                    if (!stack.peek().equals("("))
                        result += stack.pop() + " ";
                    else
                        stack.pop();
                }



                //THIS SECTION OF CODE TRANSFORMS THE POSTFIX EXPRESSION TO ITS EVALUATION, AS A MATHEMATICAL ANSWER

                System.out.println("Postfix Expression: " + result);
                Scanner postfix = new Scanner(result);
                double answer = 0.0;
                while (postfix.hasNext()) {
                    String character = postfix.next();

                    if (precedence(character) > 0) {                 //character is an operator
                        if (character.equals("+")) {
                            answer = Double.parseDouble(stack.pop()) + Double.parseDouble(stack.pop());
                        } else if (character.equals("-")) {
                            double second = Double.parseDouble(stack.pop());        //because the top number of stack is actually the second number in exp
                            answer = Double.parseDouble(stack.pop()) - second;
                        } else if (character.equals("/")) {
                            double divisor = Double.parseDouble(stack.pop());       //because the top number of stack is actually the second number in exp
                            answer = Double.parseDouble(stack.pop()) / divisor;
                        } else if (character.equals("*")) {
                            answer = Double.parseDouble(stack.pop()) * Double.parseDouble(stack.pop());
                        } else if (character.equals("^")) {
                            double exponent = Double.parseDouble(stack.pop());
                            answer = Math.pow(Double.parseDouble(stack.pop()), exponent);
                        }
                        stack.push(Double.toString(answer));
                    }
                    else
                        stack.push(character);
                }

                System.out.println("This expression amounts to: " + answer);
                while(!stack.empty())                           //clears the stack for the next part
                    stack.pop();



                //THIS CODE TRANSFORMS POSTFIX BACK TO INFIX

                Scanner translator = new Scanner(result);
                String infix = "";
                while(translator.hasNext()){
                    String symbol = translator.next();

                    if (precedence(symbol) > 0){                    //operator code
                        String rightOperand = stack.pop();
                        String leftOperand = stack.pop();
                        infix = "( " + leftOperand + " " + symbol + " " + rightOperand + " ) ";
                        stack.push(infix);
                    }
                    else{                                           //operand code
                        stack.push(symbol);
                    }

                }
                System.out.println("Translated Infix Expression: " + stack.pop() + "\n");
            }


        }



    /** This function determines the precedence of each operator. Returns an integer so that I can compare the priority to other operators
     *
     * @param str
     * @return int
     */
    public static int precedence(String str){
        if (str.equals("+") || str.equals("-")){
            return 1;
        }
        if (str.equals("/") || str.equals("*"))
            return 2;
        if (str.equals("^"))
            return 3;
        return -1;          //if all else fails
    }
}
