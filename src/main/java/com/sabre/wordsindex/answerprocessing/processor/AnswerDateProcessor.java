package com.sabre.wordsindex.answerprocessing.processor;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AnswerDateProcessor implements AnswerProcessor {

    private final String DATE_PATTERN = "(?:^|\\s{1})(?=\\d{2}([-.\\/])\\d{2}\\1\\d{4})(?:0[1-9]|1\\d|[2][0-8]|" +
            "29(?!.02.(?!(?!(?:[02468][1-35-79]|[13579][0-13-57-9])00)\\d{2}(?:[02468][048]|[13579][26])))|30(?!.02)|" +
            "31(?=.(?:0[13578]|10|12))).(?:0[1-9]|1[012]).\\d{4}(?:\\s{1}|$)";
    private final String DATE_MULTI_CHARACTERS_PATTERN = "[`~!@#$%^&*\\()_+={\\[}\\]\\|'\":;?><,\t]+";
    private final String DATE_START_ENDS_CHAR_PATTERN = "(?:(^[/\\-.]+)|(\\s+[/\\-.]+)|([/\\-.]+\\s+)|([/\\-.]+$))";
    private final String DATE_SEPARATOR = "(?:(/)|(-)|(\\.))";

    @Override
    public Set<String> processAnswer(final String answer) {
        String clearedAnswer = clearAnswer(answer);
        Set<String> datesCollection = collectToStringDates(clearedAnswer);
        Set<String> localDatesCollection = dateSort(datesCollection);
        return localDatesCollection;
    }

    private Set<String> dateSort(final Set<String> datesCollection) {
        return datesCollection.stream()
                    .map(String::trim)
                    .map(date -> date.split(DATE_SEPARATOR))
                    .peek(datesList-> Collections.reverse(Arrays.asList(datesList)))
                    .map(dateArray -> String.join(MINUS, dateArray))
                    .map(LocalDate::parse)
                    .collect(Collectors.toCollection(TreeSet::new))
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Set<String> collectToStringDates(final String recreatedAnswer) {
        Matcher matcher = Pattern.compile(DATE_PATTERN)
                .matcher(recreatedAnswer);

        Set<String> datesCollection = new HashSet<>();
        while (matcher.find()) {
            datesCollection.add(matcher.group());
        }
        return datesCollection;
    }

    private String clearAnswer(final String answer) {
        return answer
                    .replaceAll(LETTER_PATTERN, SPACE)
                    .replaceAll(DATE_MULTI_CHARACTERS_PATTERN, SPACE)
                    .replaceAll(DATE_START_ENDS_CHAR_PATTERN, SPACE)
                    .replaceAll(SPACE, DOUBLE_SPACE);
    }
}
