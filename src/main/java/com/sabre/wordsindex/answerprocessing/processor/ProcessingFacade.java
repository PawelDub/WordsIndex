package com.sabre.wordsindex.answerprocessing.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProcessingFacade {

    private ProcessorFactory processorFactory;

    @Autowired
    public ProcessingFacade(final ProcessorFactory processorFactory) {
        this.processorFactory = processorFactory;
    }

    public Map<String, Set<String>> processAnswerFrom(final String text, final List<ProcessingType> processingTypes) {
        Map<String, Set<String>> indexedSelectedProcesses = new LinkedHashMap<>();
        processingTypes.forEach(processor -> {
            Map<String, Set<String>> indexedCollection = generateIndexingCollection(text, processor);
            mergeCollections(indexedSelectedProcesses, indexedCollection);
        });
        return indexedSelectedProcesses;
    }

    private void mergeCollections(final Map<String, Set<String>> indexedSelectedProcesses, final Map<String, Set<String>> indexedCollection) {
        indexedCollection.forEach((index, collection) ->
                indexedSelectedProcesses.merge(index, collection, (updatingCollection, collectionToAdd) -> {
                    Set<String> mergedCollection = buildMergedCollection(updatingCollection, collectionToAdd);
                    return mergedCollection;
                }));
    }

    private Set<String> buildMergedCollection(final Set<String> updatingCollection, final Set<String> collectionToAdd) {
        Set<String> mergedCollection = new LinkedHashSet<>(updatingCollection);
        mergedCollection.addAll(collectionToAdd);
        return mergedCollection;
    }

    private Map<String, Set<String>> generateIndexingCollection(final String text, final ProcessingType processor) {
        AnswerProcessor answerProcessor = processorFactory.createProcessor(processor);
        return answerProcessor.buildIndexedWordsCollection(text);
    }
}
