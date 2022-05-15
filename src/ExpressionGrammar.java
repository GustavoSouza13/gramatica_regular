import java.util.*;
import java.util.stream.Collectors;

public class ExpressionGrammar {

    private Map<String, Set<String>> grammar = new HashMap<>();

    public boolean testSentence(String sentence) {
        Map<String, Set<String>> copyGrammar = new HashMap<>(grammar);
        copyGrammar.entrySet().forEach(entry -> entry.setValue(new HashSet<>(entry.getValue())));

        Map<String, Set<String>> grammerInitials = copyGrammar.entrySet().stream()
                .filter(entry -> entry.getKey().equals("S"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return testSentence(grammerInitials, sentence, false) >= 0;
    }

    private int testSentence(Map<String, Set<String>> grammar, String sentence, boolean canBeTerminal) {
        for (int i = 0; i < sentence.length(); i++) {
            String sentencePart = sentence.substring(0, i + 1);

            // Filtra as variáveis que possuem alguma grammarSentence com o sentencePart atual ou algum grammarSentence apenas com variáveis
            Map<String, Set<String>> ways = grammar.entrySet().stream()
                    .filter(entry -> entry.getValue().stream().anyMatch(grammarSentence -> testSentence(grammarSentence, sentencePart)))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            if (!ways.isEmpty()) {
                if (ways.values().stream().anyMatch(grammarSentences -> grammarSentences.contains(sentencePart)) && canBeTerminal) {
                    // Encontrou um terminal para o sentencePart e retorna quantos loops foram precisos (sentencePart.length())
                    return i + 1;
                } else {
                    for (Set<String> grammarSentences : ways.values()) {
                        // Filtra as grammarSentences que possuem o sentencePart atual ou apenas variáveis
                        for (String grammarSentence : grammarSentences.stream().filter(grammarSentence -> testSentence(grammarSentence, sentencePart)).collect(Collectors.toSet())) {
                            String grammarVariablesSentence = grammarSentence.replaceAll("[a-z]*", "");
                            if (!grammarVariablesSentence.isEmpty()) {
                                int positionFinded = 1;
                                for (String grammarVariableSentence : grammarVariablesSentence.split("")) {
                                    if (--positionFinded == 0) {
                                        // Pega os caminhos possíveis a partir de uma variável, ou seja, sempre terá somente uma entry.
                                        Map<String, Set<String>> waysFromVariable = this.grammar.entrySet().stream()
                                                .filter(entry -> grammarVariableSentence.equals(entry.getKey()))
                                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                                        boolean onlyVariables = grammarVariablesSentence.equals(grammarSentence);
                                        // Se a grammarSentence for igual a grammarValueVariables (só tiver variáveis),
                                        // ele validará a sentence atual, caso não, avança na sentence.
                                        String nextSentence = onlyVariables ? sentence : sentence.substring(i + 1); // FIXME Pode ser que o sentence.substring(i + 1) de IndexOutOfBoundsException, se der, só colocar sentence.substring(Math.min(i + 1, sentence.length())) deve resolver.
                                        if (nextSentence.isEmpty()) {
                                            // Aceita caso o waysFromVariable possuir {e} (epsilon), caso contrário, não aceita
                                            return new ArrayList<>(waysFromVariable.values()).get(0).contains("{e}") ? 0 : -1;
                                        } else {
                                            // Chama o testSentence recursivamente para validar com o waysFromVariable, nextSentence e se pode ser terminal
                                            positionFinded = testSentence(waysFromVariable, nextSentence, onlyVariables || nextSentence.length() == 1);
                                            if (positionFinded < 0 || positionFinded != 1 && positionFinded == nextSentence.length()) {
                                                // posFinded != 1 ? Retorna se aceita a parte ou não : Aceita a parte
                                                return positionFinded;
                                            } else {
                                                // Avança a sentence para a posição da próxima
                                                sentence = sentence.substring(positionFinded);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // Aceita
                    return 0;
                }
            } else if (sentence.length() == i + 1) {
                // Percorreu toda a sentence e não aceita
                return -1;
            }
        }
        // Aceita
        return sentence.length();
    }

//    private boolean testSentence(Map<String, Set<String>> grammar, String sentence, boolean initial, boolean mustBeTerminal) {
//        if (sentence.isEmpty()) {
//            return false;
//        } else {
//            for (int i = 0; i < sentence.length(); i++) {
//                String sentencePart = sentence.substring(0, i + 1);
//
//                Map<String, Set<String>> sentencesPossibles = grammar.entrySet().stream()
//                        .filter(entry -> !initial || entry.getKey().equals("S"))
//                        .filter(entry -> {
//                            entry.setValue(entry.getValue().stream()
//                                    .filter(grammarSentence -> testSentence(grammarSentence, sentencePart))
//                                    .collect(Collectors.toSet()));
//
//                            return !entry.getValue().isEmpty();
//                        })
//                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//
//                if (!sentencesPossibles.isEmpty()) {
//                    if (mustBeTerminal && sentencesPossibles.values().stream().anyMatch(value -> value.contains(sentencePart))) {
//                        return true;
//                    } else {
//                        for (Set<String> grammarValues : sentencesPossibles.values()) {
//                            for (String grammarValue : grammarValues) {
//                                String grammarValueVariables = grammarValue.replaceAll("[a-z]*", "");
//                                Map<String, Set<String>> grammarVariable = this.grammar.entrySet().stream()
//                                        .filter(entry -> grammarValueVariables.contains(entry.getKey()))
//                                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//
//                                if (grammarValue.equals(grammarValueVariables) && grammarValueVariables.length() != sentence.length()) {
//                                    continue;
//                                } else {
//                                    String nextSentence = grammarValue.equals(grammarValueVariables) ? sentence : sentence.substring(1);
//                                    if (testSentence(grammarVariable, nextSentence, false, nextSentence.length() == 1)) {
//                                        return true;
//                                    }
//                                }
//                            }
//                        }
//                    }
////                    String variable = grammarValueVariables.substring(0, i + 1);
////
////                    Map<String, List<String>> grammarPossibles = grammar.entrySet().stream()
////                            .filter(entry -> entry.getKey().equals(variable))
////                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
////
////                    return testSentence(grammarPossibles, sentence.substring(1));
////                }
////                return testSentence(variablesPossibles, sentence.substring(i + 1), true);
//                }
//            }
//
//            return false;
//        }
//    }

    private boolean testSentence(String grammarValue, String sentence) {
        String grammarValueSentence = grammarValue.replaceAll("[A-Z]*", "");
//        if (grammarValueSentence.equals(sentence)) {
            String grammarValueVariables = grammarValue.replaceAll("[a-z]*", "");
//
//            if (grammarValueSentence.length() > 1 || grammarHasTerminalTo(grammarValueVariables)) {
//                for (int i = 0; i < grammarValueVariables.length(); i++) {
//                    String variable = grammarValueVariables.substring(0, i + 1);
//
//                    Map<String, List<String>> grammarPossibles = grammar.entrySet().stream()
//                            .filter(entry -> entry.getKey().equals(variable))
//                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//
//                    return testSentence(grammarPossibles, sentence.substring(1));
//                }
//            }
//        }

//        return false;

        return grammarValueSentence.equals(sentence) || grammarValue.matches("[A-Z]*") /*|| variablesHaveTerminalTo(grammarValueVariables, sentence)&& (!mustHaveTerminal || grammarValue.equals(sentence) || variablesHaveTerminalTo(grammarValueVariables, sentence))*/;
    }

    private boolean variablesHaveTerminalTo(String variables, String sentence) {
        for (String variable : variables.split("")) {
            boolean terminalInclude = grammar.entrySet().stream()
                    .filter(entry -> entry.getKey().equals(variable))
                    .anyMatch(entry -> entry.getValue().stream()
                            .anyMatch(grammarValue -> {
                                String grammarValueSentence = grammarValue.replaceAll("[A-Z]*", "");
                                return grammarValue.equals(grammarValueSentence) && grammarValueSentence.equals(sentence);
                            }));

            if (terminalInclude) {
                return true;
            }
        }

        return false;
    }

    public void addOnGrammar(String variable, String value) {
        Set<String> sequences = grammar.get(variable) != null ? grammar.get(variable) : new HashSet<>();
        sequences.add(value);

        grammar.put(variable, sequences);
    }
}
