package com.sabre.wordsindex.answerprocessing.processor;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnswerWordProcessor implements AnswerProcessor {

    private final String MULTI_CHARACTERS_PATTERN = "(?:([`~!@#$%^&*()_+={\\[}\\]|'\":;?/>.<,-])|(\\d))";

    @Override
    public Set<String> processAnswer(String answer) {

        return Stream.of(answer
                .toLowerCase()
                .replaceAll(MULTI_CHARACTERS_PATTERN, DOUBLE_SPACE)
                .replace(TAB_PATTERN, DOUBLE_SPACE)
                .replace(NEW_LINE_PATTERN, DOUBLE_SPACE)
                .replace(BACK_PATTERN, DOUBLE_SPACE)
                .replace(BACKSLASH_PATTERN, DOUBLE_SPACE)
                .replaceAll(MULTI_SPACE_PATTERN, SPACE)
                .trim()
                .split(SPACE))
                .collect(Collectors.toSet());
    }
}
