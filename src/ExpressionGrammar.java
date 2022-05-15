import java.util.*;
import java.util.stream.Collectors;

public class ExpressionGrammar {

    private Map<String, Set<String>> grammar = new HashMap<>();

    public void addOnGrammar(String variable, String value) {
        Set<String> sequences = grammar.get(variable) != null ? grammar.get(variable) : new HashSet<>();
        sequences.add(value);

        grammar.put(variable, sequences);
    }

    public boolean testSentence(String sentence) {
        Map<String, Set<String>> initials = grammar.entrySet().stream()
                .filter(entry -> entry.getKey().equals("S"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return testSentence(initials, sentence, false) == sentence.length();
    }

    private int testSentence(Map<String, Set<String>> grammar, String sentence, boolean canBeTerminal) {
        String localSentence = sentence; // Somente para salvar qual era a sentence original, antes das mudanças que acontecem nos fors.
        int quantityFindedPositions = 0; // -1 -> Não aceita, > 0 -> Aceita total (chamada original) ou parte (chamada recursiva).
        for (Set<String> grammarSentences : grammar.values()) {
            for (int i = 1; i <= sentence.length(); i++) {
                String sentencePart = sentence.substring(0, i);

                if (grammarSentences.stream().anyMatch(grammarSentence -> testSentence(grammarSentence, sentencePart))) {
                    if (grammarSentences.stream().anyMatch(grammarSentence -> grammarSentence.equals(sentencePart)) && canBeTerminal) {
                        return i; // Encontrou um terminal para o sentencePart e retorna quantos loops foram precisos (sentencePart.length())
                    } else {
                        // Filtra as grammarSentences que possuem o sentencePart atual ou apenas variáveis
                        for (String grammarSentence : grammarSentences.stream().filter(grammarSentence -> testSentence(grammarSentence, sentencePart)).collect(Collectors.toSet())) {
//                        // Volta para a sentence original, quantityFindedPositions é resetado e vai para a próxima
//                        // grammerSentence possível.
//                            sentence = localSentence;
//                            quantityFindedPositions = 0;
                            if (quantityFindedPositions == 0) {
                                for (String grammarVariableSentence : grammarSentence.split("")) {
                                    // Valida se o grammarVariableSentence é equivalente a um terminal entre as variáveis,
                                    // por exemplo, XXtY. Se for, verificar se ele é igual a sentence buscada, se não,
                                    // não aceita.
                                    if (grammarVariableSentence.matches("[a-z]+")) {
                                        if (grammarVariableSentence.equals(sentence) || grammarVariableSentence.equals(sentencePart)) {
                                            sentence = sentence.substring(i);
                                            quantityFindedPositions += i;
                                        } else {
                                            // Volta para a sentence original, quantityFindedPositions é resetado e vai para a próxima
                                            // grammerSentence possível.
                                            sentence = localSentence;
                                            quantityFindedPositions = 0;
                                            break; // Pula para a próxima grammarSentence pq não com a atual aceita.
                                        }
                                    } else {
                                        // Pega os caminhos possíveis a partir de uma variável, ou seja, sempre terá somente uma entry.
                                        Map<String, Set<String>> waysFromVariable = this.grammar.entrySet().stream()
                                                .filter(entry -> grammarVariableSentence.equals(entry.getKey()))
                                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                                        boolean isVariable = grammarVariableSentence.matches("[A-Z]+");
                                        // Se a grammarSentence for igual a grammarValueVariables (só tiver variáveis),
                                        // ele validará a sentence atual, caso não, avança na sentence.
                                        String nextSentence = isVariable ? sentence : sentence.substring(Math.min(i + 1, sentence.length())); // FIXME Pode ser que o sentence.substring(i + 1) de IndexOutOfBoundsException, se der, só colocar sentence.substring(Math.min(i + 1, sentence.length())) deve resolver.
                                        if (nextSentence.isEmpty()) {
                                            // Aceita caso o waysFromVariable possuir {e} (epsilon), caso contrário, não aceita
                                            if (new ArrayList<>(waysFromVariable.values()).stream().findFirst().orElse(new HashSet<>()).contains("&")) {
                                                quantityFindedPositions++;
                                            } else {
                                                quantityFindedPositions = 0;
                                            }
                                        } else {
                                            // Chama o testSentence recursivamente para validar com o waysFromVariable, nextSentence e se pode ser terminal
                                            int findedPosition = testSentence(waysFromVariable, nextSentence, isVariable || nextSentence.length() == 1);
                                            if (findedPosition == 1 && findedPosition == nextSentence.length()) {
                                                quantityFindedPositions++; // Aceita total.
                                            } else if (findedPosition == 0) {
                                                // Volta para a sentence original, quantityFindedPositions é resetado e vai para a próxima
                                                // grammerSentence possível.
                                                sentence = localSentence;
                                                quantityFindedPositions = 0;
                                                break; // Pula para a próxima grammarSentence pq não com a atual aceita.
                                            } else {
                                                // Avança a sentence para a próxima parte a ser validada.
                                                sentence = sentence.substring(findedPosition);
                                                quantityFindedPositions += findedPosition;
                                            }
                                        }
                                    }
                                }
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
        // Valida se o grammarValue é igual a sentence ou se possui somente letras maiúsculas (variáveis)
        return grammarValueSentence.equals(sentence) || grammarValue.replaceAll("[a-z]*", "").matches("[A-Z]+");
    }
}
