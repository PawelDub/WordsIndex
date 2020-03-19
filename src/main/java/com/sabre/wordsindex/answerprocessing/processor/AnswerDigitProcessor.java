package com.sabre.wordsindex.answerprocessing.processor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnswerDigitProcessor implements AnswerProcessor {

    private final String NO_DIGITS_PATTERN = "\\D+";

    @Override
    public Map<String, Set<String>> buildIndexedWordsCollection(final String text) {
        Set<String> digitsCollection = collectWordsFrom(text);

        Map<String, Set<String>> indexWithDigits = new HashMap<>();
        buildIndexWithDigits(indexWithDigits, digitsCollection);

        Map<String, Set<String>> indexWithSortedDigits = buildSortedIndexedDigits(indexWithDigits);

        return indexWithSortedDigits;
    }

    private void buildIndexWithDigits(final Map<String, Set<String>> indexWithDigits, final Set<String> wordsCollection) {
        wordsCollection.forEach(word -> {
            String[] splitWord = word.split(EMPTY_STRING);
            Stream.of(splitWord)
                    .forEach(singleChar -> {
                        if (indexWithDigits.containsKey(singleChar)) {
                            indexWithDigits.get(singleChar).add(word);
                            return;
                        }
                        final Set<String> wordsByIndex = new HashSet<String>() {{
                            add(word);
                        }};
                        indexWithDigits.put(singleChar, wordsByIndex);
                    });
        });
    }

    private Map<String, Set<String>> buildSortedIndexedDigits(final Map<String, Set<String>> indexWithDigits) {
        Map<String, Set<String>> indexWithSortedDigits = new TreeMap<>();

        indexWithDigits.forEach((index, digits) -> {
            Set<String> sortedDigits = digits
                    .stream()
                    .filter(this::digitFitToLongRangeValue)
                    .sorted(Comparator.comparingLong(Long::parseLong))
                    .collect(Collectors.toCollection(TreeSet::new));
            indexWithSortedDigits.put(index, sortedDigits);
        });
        return indexWithSortedDigits;
    }

    private boolean digitFitToLongRangeValue(final String digit) {
        try {
            Long.parseLong(digit);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Set<String> processAnswer(final String answer) {
        return Stream.of(answer
                .replaceAll(NO_DIGITS_PATTERN, SPACE)
                .replaceAll(MULTI_SPACE_PATTERN, SPACE)
                .trim()
                .split(SPACE))
                .collect(Collectors.toSet());
    }
}
