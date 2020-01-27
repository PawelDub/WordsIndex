package com.sabre.wordsindex.answerprocessing.processor;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnswerWordProcessor implements AnswerProcessor {

    private final String MULTI_CHARACTERS_PATTERN = "(?:([`~!@#$%^&*()_+={\\[}\\]\\|'\":;?/>.<,\t\\\\-])|(\\d))";

    @Override
    public Set<String> processAnswer(String answer) {
        return Stream.of(answer
                .toLowerCase()
                .replaceAll(MULTI_CHARACTERS_PATTERN, SPACE)
                .replaceAll(MULTI_SPACE_PATTERN, SPACE)
                .trim()
                .split(SPACE))
                .collect(Collectors.toSet());
    }
}
