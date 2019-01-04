import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/*
This class is used to test the correctness of the code
 */
public class TestClass implements Comparator<String> {

    Map<String, Node> map;

    public TestClass() {
    }

    public TestClass(Map<String, Node> map) {
        this.map = map;
    }

    public int compare(String keyA, String keyB) {
        Node valueA = (Node) map.get(keyA);
        Node valueB = (Node) map.get(keyB);

        if (valueB.getData() - valueA.getData() >= 0) {
            return 1;
        } else {
            return -1;
        }

    }

    public  Map<String, Node> sortByValue(Map<String, Node> unsortedMap) {
        Map<String, Node> sortedMap = new TreeMap(new TestClass(unsortedMap));
        sortedMap.putAll(unsortedMap);
        return sortedMap;
    }

    public void testTheCorrectness (List<Node> outputByProgram, Map<String, Node> keywordToReferenceMapping, int testCaseNumber, int topNKeywordsCount, PrintWriter out) {

        // This map contains keyword to Fib heap node mapping, sorted by the frequency of the keyword
        Map<String, Node> sortedMap = sortByValue(keywordToReferenceMapping);

        System.out.println(keywordToReferenceMapping.size());
        System.out.println(sortedMap.size());
        System.out.println();
        for (Map.Entry<String, Node> entry : sortedMap.entrySet()) {
            out.println(entry.getValue().getKeyword() + " " + entry.getValue().getData());
        }

        out.println();

        // OutputByProgram list contains output of my program, i.e those keywords that my program thinks have maximum frequency
        for (Node node : outputByProgram) {
            out.println(node.getKeyword() + " " + node.getData());
        }

        out.println();

        out.println("Test case number is " + testCaseNumber + " number of entries to be extracted " + topNKeywordsCount);

        int count = 0;

        // This for loop compares frequencies of the keywords my program extracted and actual answer frequencies. If they are same then my code runs fine otherwise there is a bug
        for (Map.Entry<String, Node> entry : sortedMap.entrySet()) {

            if (count == topNKeywordsCount) {
                break;
            }

            if (entry.getValue().getData() != outputByProgram.get(count).getData()) {
                out.println( outputByProgram.get(count).getKeyword() + " " + outputByProgram.get(count).getData() + " is an error ");
               // out.println("Expected " + entry.getValue().getData() + " " + entry.getValue().getKeyword() + " " + entry.getValue().getParent().getKeyword());
            }

            count++;
        }

        out.println();
    }
}
