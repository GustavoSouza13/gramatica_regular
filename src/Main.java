import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        ExpressionGrammar expressionGrammar = new ExpressionGrammar();

//        expressionGrammar.addOnGrammar("S", "aS");
//        expressionGrammar.addOnGrammar("S", "bS");
//        expressionGrammar.addOnGrammar("S", "cS");
//        expressionGrammar.addOnGrammar("S", "a");
//        expressionGrammar.addOnGrammar("S", "b");
//        expressionGrammar.addOnGrammar("S", "c");

        expressionGrammar.addOnGrammar("S", "XXXY");
        expressionGrammar.addOnGrammar("X", "a");
        expressionGrammar.addOnGrammar("X", "b");
        expressionGrammar.addOnGrammar("X", "c");
        expressionGrammar.addOnGrammar("Y", "XY");
        expressionGrammar.addOnGrammar("Y", "{e}");

        System.out.println(expressionGrammar.testSentence("bacacabcbacbacbabcbbcbcbcbccbaaaa"));
    }
}
