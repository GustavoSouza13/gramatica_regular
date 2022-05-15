import java.util.*;
import java.util.stream.Collectors;

public class ExpressionGrammar {

    private Map<String, Set<String>> grammar = new HashMap<>();

    public void addOnGrammar(String variable, String value) {
        Set<String> sequences = grammar.get(variable) != null ? grammar.get(variable) : new HashSet<>();
        sequences.add(value);

        grammar.put(variable, sequences);
    }

    public void testSentence(String sentence) {
        Map<String, Set<String>> initials = grammar.entrySet().stream()
                .filter(entry -> entry.getKey().equals("S"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        boolean accept = testSentence(initials, sentence, sentence.length() == 1) == sentence.length();
        System.out.println(accept ? String.format("%s é aceita.", sentence) : String.format("%s não é aceita.", sentence));
    }

    public void generateCpf() {
        generateCpf("XXXPXXXPXXXTXX");
    }

    public void generateEmail() {
        generateEmail("XXXXXXPXXNNADCB");
    }

    public void generatePhoneNumber() {
        generatePhoneNumber("XXNXXXXTXXXX");
    }

    public void generateCpf(String sentence) {
        this.grammar = new HashMap<>();
        addOnGrammar("X", "0");
        addOnGrammar("X", "1");
        addOnGrammar("X", "2");
        addOnGrammar("X", "3");
        addOnGrammar("X", "4");
        addOnGrammar("X", "5");
        addOnGrammar("X", "6");
        addOnGrammar("X", "7");
        addOnGrammar("X", "8");
        addOnGrammar("X", "9");
        addOnGrammar("P", ".");
        addOnGrammar("T", "-");

        System.out.printf("CPF: %s\n", generate(sentence));
    }

    public void generateEmail(String sentence) {
        this.grammar = new HashMap<>();
        addOnGrammar("X", "a");
        addOnGrammar("X", "b");
        addOnGrammar("X", "c");
        addOnGrammar("X", "d");
        addOnGrammar("X", "e");
        addOnGrammar("X", "f");
        addOnGrammar("X", "g");
        addOnGrammar("X", "h");
        addOnGrammar("X", "i");
        addOnGrammar("X", "j");
        addOnGrammar("X", "k");
        addOnGrammar("X", "i");
        addOnGrammar("X", "j");
        addOnGrammar("X", "k");
        addOnGrammar("X", "l");
        addOnGrammar("X", "m");
        addOnGrammar("X", "n");
        addOnGrammar("X", "o");
        addOnGrammar("X", "p");
        addOnGrammar("X", "q");
        addOnGrammar("X", "r");
        addOnGrammar("X", "s");
        addOnGrammar("X", "t");
        addOnGrammar("X", "u");
        addOnGrammar("X", "v");
        addOnGrammar("X", "w");
        addOnGrammar("X", "x");
        addOnGrammar("X", "y");
        addOnGrammar("X", "z");
        addOnGrammar("N", "1");
        addOnGrammar("N", "2");
        addOnGrammar("N", "3");
        addOnGrammar("N", "4");
        addOnGrammar("N", "5");
        addOnGrammar("N", "6");
        addOnGrammar("N", "7");
        addOnGrammar("N", "8");
        addOnGrammar("N", "9");
        addOnGrammar("P", ".");
        addOnGrammar("A", "@");
        addOnGrammar("D", "gmail");
        addOnGrammar("D", "hotmail");
        addOnGrammar("D", "outlook");
        addOnGrammar("C", "Pcom");
        addOnGrammar("C", "Pcom");
        addOnGrammar("B", "Pbr");
        addOnGrammar("B", "&");

        System.out.printf("E-mail: %s\n", generate(sentence));
    }

    public void generatePhoneNumber(String sentence) {
        this.grammar = new HashMap<>();
        addOnGrammar("X", "0");
        addOnGrammar("X", "1");
        addOnGrammar("X", "2");
        addOnGrammar("X", "3");
        addOnGrammar("X", "4");
        addOnGrammar("X", "5");
        addOnGrammar("X", "6");
        addOnGrammar("X", "7");
        addOnGrammar("X", "8");
        addOnGrammar("X", "9");
        addOnGrammar("N", "9");
        addOnGrammar("T", "-");

        System.out.printf("Telefone: %s\n", generate(sentence));
    }

    private int testSentence(Map<String, Set<String>> grammar, String sentence, boolean canBeTerminal) {
        String localSentence = sentence; // Somente para salvar qual era a sentence original, antes das mudanças que acontecem nos fors.
        int quantityFindedPositions = 0; // 0 -> Não aceita, >0 -> Aceita total (chamada original) ou parte (chamada recursiva).
        for (Set<String> grammarSentences : grammar.values()) { // TODO Acho que não precisaria desse for, já que ele executa a mesma coisa na linha 137
            for (int i = 1; i <= sentence.length(); i++) {
                String sentencePart = sentence.substring(0, i);

                if (grammarSentences.stream().anyMatch(grammarSentence -> testSentence(grammarSentence, sentencePart))) {
                    if (grammarSentences.stream().anyMatch(grammarSentence -> grammarSentence.equals(sentencePart)) && (canBeTerminal || i == sentence.length())) {
                        return i; // Encontrou um terminal para o sentencePart e retorna quantos loops foram precisos (sentencePart.length()).
                    } else {
                        for (String grammarSentence : grammarSentences.stream().filter(grammarSentence -> testSentence(grammarSentence, sentencePart)).collect(Collectors.toSet())) { // Filtra as grammarSentences que possuem o sentencePart atual ou apenas variáveis
                            for (String grammarVariableSentence : grammarSentence.split("")) {
                                if (grammarVariableSentence.matches("[a-z]+") || grammarVariableSentence.equals("&")) { // Valida se o grammarVariableSentence é equivalente a um terminal entre as variáveis, por exemplo, XXtY, ou se é epsilon (&). Se for, verificar se ele é igual a sentence buscada, se não, não aceita.
                                    if (grammarVariableSentence.equals(sentence) || grammarVariableSentence.equals(sentencePart)) {
                                        sentence = sentence.substring(i);
                                        quantityFindedPositions += i;
                                    } else {
                                        quantityFindedPositions = 0; // quantityFindedPositions é resetado e vai para a próxima grammerSentence possível.
                                        break; // Pula para a próxima grammarSentence pq não com a atual aceita.
                                    }
                                } else {
                                    Map<String, Set<String>> waysFromVariable = this.grammar.entrySet().stream()
                                            .filter(entry -> grammarVariableSentence.equals(entry.getKey()))
                                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)); // Pega os caminhos possíveis a partir de uma variável, ou seja, sempre terá somente uma entry.

                                    if (sentence.isEmpty()) {
                                        if (!new ArrayList<>(waysFromVariable.values()).stream().findFirst().orElse(new HashSet<>()).contains("&")) { // Aceita caso o waysFromVariable possuir & (epsilon), caso contrário, não aceita.
                                            quantityFindedPositions = 0;
                                        }
                                    } else {
                                        boolean isVariable = grammarSentence.replaceAll("[a-z]*", "").length() > 1; /*grammarVariableSentence.matches("[A-Z]+") && grammarVariableSentence.length() > 1*/;

                                        int findedPosition = testSentence(waysFromVariable, sentence, canBeTerminal || isVariable || sentence.length() == 1); // Chama o testSentence recursivamente para validar com o waysFromVariable, nextSentence e se pode ser terminal
                                        if (findedPosition == 0) {
                                            quantityFindedPositions = 0; // É resetado e vai para a próxima grammerSentence possível.
                                            break; // Pula para a próxima grammarSentence pq não com a atual aceita.
                                        } else {
                                            sentence = sentence.substring(findedPosition); // Avança a sentence para a próxima parte a ser validada.
                                            quantityFindedPositions += findedPosition; // Soma a quantidade de posições que foram validadas e aceitas na recursividade
                                        }
                                    }
                                }
                            }

                            if (quantityFindedPositions == localSentence.length() || quantityFindedPositions > 0 && canBeTerminal) { // Retorna caso aceite.
                                return quantityFindedPositions; // Aceita.
                            } else {
                                sentence = localSentence; // Volta para a sentence original (como estava no início do método atual).
                                quantityFindedPositions = 0; // É resetado pois não aceita.
                            }
                        }
                    }
                }
            }
        }

        return quantityFindedPositions;
    }

    private boolean testSentence(String grammarValue, String sentence) {
        String grammarValueSentence = grammarValue.replaceAll("[A-Z]*", "");
        return grammarValueSentence.equals(sentence) || grammarValue.replaceAll("[a-z]*", "").matches("[A-Z]+"); // Valida se o grammarValue é igual a sentence ou se possui somente letras maiúsculas (variáveis).
    }

    private String generate(String sentence) {
        String result = "";
        for (int i = 0; i < sentence.length(); i++) {
            String sentencePart = sentence.substring(i, i + 1);

            Set<String> grammarSentences = grammar.get(sentencePart);
            if (grammarSentences == null || grammarSentences.isEmpty()) {
                return null;
            } else {
                String grammarSentence = new ArrayList<>(grammarSentences).get((int) Math.floor(Math.random() * grammarSentences.size()));
                for (String grammarSentencePart : grammarSentence.split("")) {
                    result += grammarSentencePart.matches("[A-Z]+") ? generate(grammarSentencePart) : grammarSentencePart;
                }
            }
        }

        return result.replace("&", "");
    }
}
