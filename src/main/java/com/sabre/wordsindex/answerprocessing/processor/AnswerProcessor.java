package com.sabre.wordsindex.answerprocessing.processor;

import java.text.Collator;
import java.util.*;
import java.util.stream.Stream;

public interface AnswerProcessor {

    String SPACE = " ";
    String EMPTY_STRING = "";
    String MULTI_SPACE_PATTERN = "\\s+";
    String SLASH = "/";
    String MINUS = "-";
    String DOT = ".";
    String COLON = ":";
    String DOUBLE_SPACE = "  ";
    String LETTER_PATTERN = "\\p{L}";
    String TAB_PATTERN = "\\t";
    String NEW_LINE_PATTERN = "\\n";
    String BACK_PATTERN = "\\r";
    String BACKSLASH_PATTERN = "\\";

    default Map<String, Set<String>> buildIndexedWordsCollection(final String text) {
        final Collator collator = Collator.getInstance(Locale.getDefault());
        Map<String, Set<String>> indexWithWords = new TreeMap<>(collator);
        Set<String> wordsCollection = collectWordsFrom(text);
        wordsCollection.forEach(word -> {
            String[] splitWord = word.split(EMPTY_STRING);
            Stream.of(splitWord)
                    .forEach(singleChar -> {
                        if (singleChar.equals(SLASH) || singleChar.equals(MINUS) || singleChar.equals(DOT) || singleChar.equals(COLON)) {
                            return;
                        }

                        if (indexWithWords.containsKey(singleChar)) {
                            indexWithWords.get(singleChar).add(word);
                            return;
                        }

                        final SortedSet<String> wordsByIndex = new TreeSet<String>(collator) {{
                            add(word);
                        }};

                        indexWithWords.put(singleChar, wordsByIndex);
                    });
        });

        return indexWithWords;
    }

    default Set<String> collectWordsFrom(final String text) {
        return new TreeSet<>(processAnswer(text));
    }

    Set<String> processAnswer(final String answer);
}
