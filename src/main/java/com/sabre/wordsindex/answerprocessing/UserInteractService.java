package com.sabre.wordsindex.answerprocessing;

import com.sabre.wordsindex.answerprocessing.processor.ProcessingFacade;
import com.sabre.wordsindex.answerprocessing.processor.ProcessingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.exit;

@Service
class UserInteractService implements UserInteract {
    private final static String YES_OR_NOT = "yes/not: ";
    private final static String Y = "y";
    private final static String YES = "yes";
    private final static String PLEASE_ENTER_TEXT = "Please enter your text.";

    private ProcessingFacade processingFacade;

    @Autowired
    UserInteractService(final ProcessingFacade processingFacade) {
        this.processingFacade = processingFacade;
    }

    @Override
    public void userInteractProcess() {
        try (Scanner scanner = new Scanner(System.in)) {
            performUserInteract(scanner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printEnterText() {
        System.out.println(PLEASE_ENTER_TEXT);
    }

    private void performUserInteract(final Scanner scanner) {
            printSelectedTextIndexing();
            final List<ProcessingType> processingTypes = collectProcessors(scanner);
            printEnterText();
            final String userText = scanner.nextLine();
            final Map<String, Set<String>> processedAnswer = processingFacade.processAnswerFrom(userText, processingTypes);
            printChosenProcessingTypes(processingTypes);
            printCollectionOf(processedAnswer);
            printContinueQuestion();
            final String continueAnswer = scanner.nextLine();
            manageContinueAnswer(continueAnswer, scanner);
    }

    private void printChosenProcessingTypes(final List<ProcessingType> processingTypes) {
        List<String> processes = processingTypes.stream().map(Objects::toString).collect(Collectors.toList());
        System.out.println(String.format("\nYou have chosen indexing by %s", String.join(", ", processes)));
    }

    private void printSelectedTextIndexing() {
        System.out.println("Please, tell me what would you like to indexing.");
    }

    private List<ProcessingType> collectProcessors(final Scanner scanner) {
        return Stream.of(ProcessingType.values())
                .filter(processor -> {
                    printAskingOfIndexingType(processor);
                    final String answerWords = scanner.nextLine().toLowerCase();
                    return (answerWords.equals(Y) || answerWords.equals(YES));
                })
                .collect(Collectors.toList());
    }

    private void printAskingOfIndexingType(final ProcessingType processor) {
        System.out.println(String.format("Would you like indexing: %s %s", processor, processor.getMessage()));
        System.out.print(YES_OR_NOT);
    }


    private void printCollectionOf(final Map<String, Set<String>> answer) {
        System.out.println("\nIndexed collections: ");
        answer.forEach((key, value) -> System.out.println(key + ": " + String.join(", ", value)));
        System.out.println("\n--------------------------------");
    }

    private void printContinueQuestion() {
        System.out.println("\nWould you like to continue?");
        System.out.print(YES_OR_NOT);
    }

    private void manageContinueAnswer(final String continueAnswer, final Scanner scanner) {
        final String lowerAnswer = continueAnswer.toLowerCase();
        if (lowerAnswer.equals(Y) || lowerAnswer.equals(YES)) {
            System.out.println(PLEASE_ENTER_TEXT);
            performUserInteract(scanner);
        }
        System.out.println("\nThank you for cooperate. \nGood Bye, see you next time.");
        exit(0);
    }
}
