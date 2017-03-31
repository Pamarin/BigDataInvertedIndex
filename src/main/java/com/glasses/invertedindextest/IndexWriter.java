package com.glasses.invertedindextest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jean-Luc Burot
 * @since 2017-03-31
 */
public class IndexWriter implements Runnable {
    private HashMap<String, HashSet<Integer>> indexMap;
    private String address;
    private int currentProgress;
    private boolean started;
    
    public int getTotalProgress() {
        return indexMap.size();
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public boolean isStarted() {
        return started;
    }
    
    public IndexWriter(String address, HashMap<String, HashSet<Integer>> indexMap) {
        this.address = address;
        this.indexMap = indexMap;
        this.currentProgress = 0;
        this.started = false;
    }

    @Override
    public void run() {
        // Sort all words.
        SortedSet<String> words = new TreeSet<>(this.indexMap.keySet());
        
        // Write to file.
        try {
            // Open file access.
            OutputStream output = new FileOutputStream(this.address);
            
            // Stepwise file writing.
            Iterator iterator = words.iterator();
            this.started = true;
            while(iterator.hasNext()) {
                // Get current word.
                String currentWord = (String)iterator.next();
                
                // Create current line.
                String currentLine = currentWord + " " + this.indexMap.get(currentWord) + System.lineSeparator();
                
                try {
                    // Write line to file.
                    output.write(currentLine.getBytes());
                } catch (IOException ex) {
                    System.err.println("Error: " + ex.getMessage());
                }
                
                // Keep track of the progress.
                this.currentProgress++;
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        
        /*
        // Text to be written into a file.
        String resultContent = "";
        
        // Concatenate file content.
        Iterator iterator = words.iterator();
        this.started = true;
        while(iterator.hasNext()) {
            // Get current word.
            String currentWord = (String)iterator.next();
            
            // Add current word plus indexes.
            resultContent = resultContent.concat(currentWord + " " + this.indexMap.get(currentWord) + System.lineSeparator());
            
            // Keep track of the progress.
            this.currentProgress++;
        }
        
        //Write new file.
        File targetFile = new File(this.address);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile))) {
            writer.write(resultContent);
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        */
    }
}
