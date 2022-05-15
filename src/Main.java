import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        System.out.println("-- Primeira Gramática Regular --");
        ExpressionGrammar expressionGrammar = new ExpressionGrammar();
        expressionGrammar.addOnGrammar("S", "aS");
        expressionGrammar.addOnGrammar("S", "bS");
        expressionGrammar.addOnGrammar("S", "cS");
        expressionGrammar.addOnGrammar("S", "a");
        expressionGrammar.addOnGrammar("S", "b");
        expressionGrammar.addOnGrammar("S", "c");

        expressionGrammar.testSentence("abc");
        expressionGrammar.testSentence("aaabbca");
        expressionGrammar.testSentence("c");

        System.out.println("\n-- Segunda Gramática Regular --");
        expressionGrammar = new ExpressionGrammar();
        expressionGrammar.addOnGrammar("S", "XXXY");
        expressionGrammar.addOnGrammar("X", "a");
        expressionGrammar.addOnGrammar("X", "b");
        expressionGrammar.addOnGrammar("X", "c");
        expressionGrammar.addOnGrammar("Y", "XY");
        expressionGrammar.addOnGrammar("Y", "&");

        expressionGrammar.testSentence("abc");
        expressionGrammar.testSentence("abc&");
        expressionGrammar.testSentence("abcccaa");
        expressionGrammar.testSentence("ccc");

        System.out.println("\n-- Terceira Gramática Regular --");
        expressionGrammar = new ExpressionGrammar();
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

        expressionGrammar.testSentence("acaac&b");
        expressionGrammar.testSentence("acaa&&d");
        expressionGrammar.testSentence("a");
        expressionGrammar.testSentence("a&");

        System.out.println("\n-- Quarta Gramática Regular --");
        expressionGrammar = new ExpressionGrammar();
        expressionGrammar.addOnGrammar("S", "abc");

        expressionGrammar.testSentence("abc");

        System.out.println("\n-- Geradores automáticos --");
        expressionGrammar.generateCpf();
        expressionGrammar.generateEmail();
        expressionGrammar.generatePhoneNumber();
    }
}
