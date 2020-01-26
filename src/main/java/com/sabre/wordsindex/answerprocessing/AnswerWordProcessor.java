package com.sabre.wordsindex.answerprocessing;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnswerWordProcessor implements AnswerProcessor {

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
