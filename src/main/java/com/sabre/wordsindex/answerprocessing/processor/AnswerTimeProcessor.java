package com.sabre.wordsindex.answerprocessing.processor;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AnswerTimeProcessor implements AnswerProcessor {
    private final String TIME_PATTERN = "(?:\\s|^)([0-1][0-9]|[2][0-3]):([0-5][0-9])(:[0-5][0-9])?(?:$|\\s)";
    private final String TIME_MULTI_CHARACTERS_PATTERN = "(?:([`~!@#$%^&*()_+={\\[}\\]\\|'\";?/>.<,\t\\\\-]))";
    private final String TIME_START_ENDS_CHAR_PATTERN = "(?:(^:)|(\\s:+)|(:+\\s))";

    @Override
    public Set<String> processAnswer(final String answer) {
        final String clearedTimeAnswer = clearTimeAnswer(answer);
        final Set<String> timesCollection = collectTimes(clearedTimeAnswer);
        Set<String> sortedTimesCollection =  timeSort(timesCollection);
        return sortedTimesCollection;
    }

    private String clearTimeAnswer(final String answer) {
        return answer
                    .replaceAll(LETTER_PATTERN, SPACE)
                    .replaceAll(TIME_MULTI_CHARACTERS_PATTERN, SPACE)
                    .replaceAll(TIME_START_ENDS_CHAR_PATTERN,SPACE)
                    .replaceAll(SPACE, DOUBLE_SPACE);
    }

    private Set<String> collectTimes(final String recreatedTimeAnswer) {
        Matcher matcher = Pattern.compile(TIME_PATTERN)
                .matcher(recreatedTimeAnswer);

        Set<String> timesCollection = new HashSet<>();
        while (matcher.find()) {
            timesCollection.add(matcher.group());
        }
        return timesCollection;
    }

    private Set<String> timeSort(final Set<String> datesCollection) {
        return datesCollection.stream()
                .map(String::trim)
                .map(LocalTime::parse)
                .collect(Collectors.toCollection(TreeSet::new))
                .stream()
                .map(String::valueOf)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
