package com.sabre.wordsindex.answerprocessing;

import java.text.Collator;
import java.util.*;
import java.util.stream.Stream;

public interface AnswerProcessor {

    String SPACE = " ";
    String EMPTY_STRING = "";
    String MULTI_SPACE_PATTERN = "\\s+";
    String MULTI_CHARACTERS_PATTERN = "(?:([`~!@#$%^&*()_+={\\[}\\]\\|'\":;?/>.<,\t\\\\-])|(\\d))";

    default Map<String, Set<String>> buildIndexedWordsCollection(String text) {
        final Collator collator = Collator.getInstance(Locale.getDefault());
        Map<String, Set<String>> indexWithWords = new TreeMap<>(collator);
        Set<String> wordsCollection = collectWordsFrom(text);
        wordsCollection.forEach(word -> {
            String[] splitWord = word.split(EMPTY_STRING);
            Stream.of(splitWord)
                    .forEach(singleChar -> {
                        if (indexWithWords.containsKey(singleChar)) {
                            indexWithWords.get(singleChar).add(word);
                            return;
                        }
                        final SortedSet<String> wordsByIndex = new TreeSet<String>(collator){{add(word);}};
                        indexWithWords.put(singleChar, wordsByIndex);
                    });
        });

        return indexWithWords;
    }

    default Set<String> collectWordsFrom(String text) {
        return new TreeSet<>(processAnswer(text));
    }

    Collection<String> processAnswer(String answer);
}
