import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/*
Aim of the project : To find the top N frequency words using Max Fibonacci Heap and Hash table
Input : File containing the keywords
Output : Top N frequency Keywords
 */
public class keywordcounter {



    private Map<String, Node> keywordToReferenceMapping = new HashMap<>();
    private MaxFibonacciHeap maxFibonacciHeap = new MaxFibonacciHeap();

    /*
   Read the queries and keywords from file and save that in a list for later processing
    */

    public static List<String> readKeywordsAndQueriesFromFile(String fileName) {

        List<String> keywordsAndQueriesList = Collections.emptyList();
        try {
            keywordsAndQueriesList = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        return keywordsAndQueriesList;
    }

    /*
    Used for testing. Prints the results saved in the list
     */

    public static void printResults (List<String> resultList) {
        for (String output : resultList) {
            System.out.println(output);
        }
    }

    /*
    This function is used to check the correctness of the code. It matches my output with the correct output, and computes the test cases on which my code is failing,
     */



    /*
    //Uncomment this to run the test program
    public void test(Map<String, Node> keywordToReferenceMapping, List<Node> removedMaxNodeList, int testCaseNumber, int topNKeywordsCount, PrintWriter out ) {
        TestClass testClass = new TestClass();
        testClass.testTheCorrectness(removedMaxNodeList, keywordToReferenceMapping, testCaseNumber, topNKeywordsCount, out);
    }

    */




    /*
    This function is used to execute the queries in the following way
        1) IF the query start a '$' symbol that means this key have to be inserted/increased in fibonacci heap
        2) IF the entry starts with a integer, then  write down the most popular keywords to the output file in
           descending order.
        3) When “stop” (without $sign) appears in the input stream, program should end.
     */

    public void executeQueries (List<String> input, PrintWriter out) {

        int testCaseNumber = 0;

        for (String inp : input) {

            // Terminates the program when input is stop
            if (inp.equalsIgnoreCase("stop")) {
                break;
            }

            /*
                Insert the entry into fib heap if already not present or increase the freq of the key.

                    1) This is achieved through a hashMap. Map maps the keyword to the corresponding node in the Fibonacci heap.
                    2) IF the keyword is already present in the map, then just increase the key frequency in the fibonacci heap.
                    3) If the keyword is not present in the map, that means that keyword has not been inserted in the Fib heap till now so insert a new node in the fib heap.
             */

            if (inp.charAt(0) == '$') {
                int index = 0;
                for (int i = 1; i < inp.length(); i++) {
                    if (Character.isWhitespace(inp.charAt(i))) {
                        index = i;
                        break;
                    }
                }

                String keyword = inp.substring(1,index);
                int frequency = Integer.valueOf(inp.substring(index+1));

                if (keywordToReferenceMapping.get(keyword) != null) {
                    maxFibonacciHeap.increaseKey(keywordToReferenceMapping.get(keyword), frequency);
                } else {
                    Node newNode = new Node(0,null,frequency,null,false,null,null,keyword);
                    newNode = maxFibonacciHeap.insertIntoFibHeap(newNode);
                    keywordToReferenceMapping.put(keyword,newNode);
                }
            } else {

                /*
                    This handled the case when Query will be an integer number (n) without $ sign in the beginning
                    Here we just extract the top n keywords from the Fib heap, print the output and insert them back into fib heap.
                 */
                testCaseNumber++;

                int topNKeywordsCount = Integer.valueOf(inp);
                List<Node> removedMaxNodeList = new ArrayList<>();


                for (int i = 0; i < topNKeywordsCount; i++) {
                    removedMaxNodeList.add(maxFibonacciHeap.deleteMaxNodeFromFibHeap(testCaseNumber));
                }

                for (int i = 0; i < topNKeywordsCount-1; i++) {
                    out.print(removedMaxNodeList.get(i).getKeyword()+",");
                }

                out.println(removedMaxNodeList.get(topNKeywordsCount-1).getKeyword());
              //  test(keywordToReferenceMapping,removedMaxNodeList, testCaseNumber, topNKeywordsCount, out);

                for (Node node : removedMaxNodeList) {
                    maxFibonacciHeap.insertIntoFibHeap(node);
                }
            }
        }
    }


    public static void main (String [] args) {
        List<String> input = keywordcounter.readKeywordsAndQueriesFromFile(args[0]);
      //  keywordcounter.printResults(input);
        keywordcounter keywordFrequencyCounter = new keywordcounter();
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter("output_file.txt", false), true);
            keywordFrequencyCounter.executeQueries(input,out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }

    }
}


