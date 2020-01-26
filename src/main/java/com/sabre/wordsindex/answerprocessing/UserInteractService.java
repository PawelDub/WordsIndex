package com.sabre.wordsindex.answerprocessing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import static java.lang.System.exit;

@Service
class UserInteractService implements UserInteract {

    private ProcessorFactory processorFactory;

    @Autowired
    UserInteractService(final ProcessorFactory processorFactory) {
        this.processorFactory = processorFactory;
    }

    @Override
    public void userInteractProcess() {
        try (Scanner scanner = new Scanner(System.in)) {
            printEnterText();
            userInteract(scanner);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void userInteract(final Scanner scanner) {
        while (scanner.hasNext()) {
            final String userText = scanner.nextLine();
            final Map<String, Set<String>> processedAnswer = processAnswerFrom(userText);
            printCollectionOf(processedAnswer);
            printContinueQuestion();
            final String continueAnswer = scanner.nextLine();
            manageContinueAnswer(continueAnswer);
        }
    }

    private Map<String, Set<String>> processAnswerFrom(final String text) {
        AnswerProcessor answerProcessor = processorFactory.createProcessor(ProcessorFactory.WordProcessing.WORD);
        return answerProcessor.buildIndexedWordsCollection(text);
    }

    private void printEnterText() {
        System.out.println("Please enter your text.");
    }

    private void printCollectionOf(final Map<String, Set<String>> answer) {
        System.out.println("\nIndexed collections: ");
        answer.forEach((key, value) -> System.out.println(key + ": " + String.join(", ", value)));
        System.out.println("\n--------------------------------");
    }

    private void printContinueQuestion() {
        System.out.println("\nWould you like to continue?");
        System.out.print("yes/not: ");
    }

    private void manageContinueAnswer(final String continueAnswer) {
        final String lowerAnswer = continueAnswer.toLowerCase();
        if (lowerAnswer.equals("y") || lowerAnswer.equals("yes")) {
            System.out.println("Please enter new text.");
            return;
        }
        System.out.println("\nThank you for cooperate. \nGood Bye, see you next time.");
        exit(0);
    }
}
