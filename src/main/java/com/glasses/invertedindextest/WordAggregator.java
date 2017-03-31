package com.glasses.invertedindextest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Jean-Luc Burot
 * @since 2017-03-31
 */
public class WordAggregator implements Runnable {
    private boolean started;
    private int documentsTotal;
    private int documentsCurrent;
    private Path path;
    private HashMap<String, HashSet<Integer>> words;

    public boolean isStarted() {
        return started;
    }

    public int getDocumentsTotal() {
        return documentsTotal;
    }

    public int getDocumentsCurrent() {
        return documentsCurrent;
    }

    public HashMap<String, HashSet<Integer>> getWords() {
        return words;
    }
    
    public WordAggregator(Path path) {
        this.started = false;
        this.path = path;
        this.words = new HashMap<>();
        documentsTotal = 0;
        documentsCurrent = 0;
    }

    @Override
    public void run() {
        try {
            // Get file content.
            List<String> file = Files.readAllLines(this.path);
            this.documentsTotal = file.size();
            
            // Get the words in every line.
            // Keep track of processing event.
            this.started = true;
            for(int i=0; i<this.documentsTotal; i++) {
                
                // Filter line text.
                String filteredDocument = filterText(file.get(i));
                
                // Normalize text.
                filteredDocument = filteredDocument.toLowerCase();
                
                // Split line text.
                splitText(filteredDocument, i);
                
                // Keep track of the progress.
                this.documentsCurrent = i + 1;
            }
            
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }
    
    private void splitText(String text, int documentIdentifier) {
        if(!"".equals(text)) {
            // Split text with blank char.
            String[] wordsInText = text.split(" ");

            // Put all words in map.
            for(int i=0; i<wordsInText.length; i++) {
                if(this.words.containsKey(wordsInText[i])) {
                    // Word already exists, hence add document identifier.

                    // Get index.
                    HashSet<Integer> indexList = this.words.get(wordsInText[i]);

                    // Add current index.
                    indexList.add(documentIdentifier);

                } else {
                    // Word does not exists, hence add word and document identifier.

                    // Create and set identifier.
                    HashSet<Integer> indexList = new HashSet<>();
                    indexList.add(documentIdentifier);

                    // Add new word with index to map.
                    this.words.put(wordsInText[i], indexList);
                }
            }
        }
    }
    
    private String filterText(String text) {
        String filteredText = text;
        
        // Remove the following characters.
        filteredText = filteredText.replace("\n", "");
        filteredText = filteredText.replace("\r", "");
        filteredText = filteredText.replace(".", "");
        filteredText = filteredText.replace(",", "");
        filteredText = filteredText.replace(";", "");
        filteredText = filteredText.replace(":", "");
        filteredText = filteredText.replace("(", "");
        filteredText = filteredText.replace(")", "");
        filteredText = filteredText.replace("`", "");
        filteredText = filteredText.replace("$", "");
        filteredText = filteredText.replace("!", "");
        filteredText = filteredText.replace("?", "");
        filteredText = filteredText.replace("%", "");
        filteredText = filteredText.replace("&", "");
        filteredText = filteredText.replace("0", "");
        filteredText = filteredText.replace("1", "");
        filteredText = filteredText.replace("2", "");
        filteredText = filteredText.replace("3", "");
        filteredText = filteredText.replace("4", "");
        filteredText = filteredText.replace("5", "");
        filteredText = filteredText.replace("6", "");
        filteredText = filteredText.replace("7", "");
        filteredText = filteredText.replace("8", "");
        filteredText = filteredText.replace("9", "");
        filteredText = filteredText.replace("*", "");
        filteredText = filteredText.replace("/", "");
        filteredText = filteredText.replace("+", "");
        filteredText = filteredText.replace("\\", "");
        filteredText = filteredText.replace("\"", "");
        filteredText = filteredText.replace("'", "");
        filteredText = filteredText.replace("|", "");
        filteredText = filteredText.replace("#", "");
        filteredText = filteredText.replace("=", "");
        filteredText = filteredText.replace("{", "");
        filteredText = filteredText.replace("}", "");
        filteredText = filteredText.replace("@", "");
        
        // Replace the following characters.
        filteredText = filteredText.trim().replaceAll("-+", " ");
        filteredText = filteredText.trim().replaceAll(" +", " ");
        
        return filteredText;
    }

    @Override
    public String toString() {
        return "WordAggregator{" + "documentsTotal=" + documentsTotal + ", documentsCurrent=" + documentsCurrent + ", count(words)=" + words.size() + '}';
    }
}
