import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        ExpressionGrammar expressionGrammar = new ExpressionGrammar();

//        expressionGrammar.addOnGrammar("S", "aS");
//        expressionGrammar.addOnGrammar("S", "bS");
//        expressionGrammar.addOnGrammar("S", "cS");
//        expressionGrammar.addOnGrammar("S", "&");
//        expressionGrammar.addOnGrammar("S", "a");
//        expressionGrammar.addOnGrammar("S", "b");
//        expressionGrammar.addOnGrammar("S", "c");

        expressionGrammar.addOnGrammar("S", "XYX");
        expressionGrammar.addOnGrammar("S", "XYd");
        expressionGrammar.addOnGrammar("S", "XY");
        expressionGrammar.addOnGrammar("X", "a");
        expressionGrammar.addOnGrammar("X", "b");
        expressionGrammar.addOnGrammar("X", "c");
        expressionGrammar.addOnGrammar("Y", "XZY");
        expressionGrammar.addOnGrammar("Y", "XKY");
        expressionGrammar.addOnGrammar("Y", "&");
        expressionGrammar.addOnGrammar("Z", "aac");
        expressionGrammar.addOnGrammar("K", "aaY");

        System.out.println(expressionGrammar.testSentence("abaa&&a"));
    }
}
