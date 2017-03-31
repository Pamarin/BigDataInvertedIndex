package com.glasses.invertedindextest;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Jean-Luc Burot
 * @since 2017-03-30
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Welcome to InvertedIndexTest!");
        String sourceAddress = "/home/jean/Schreibtisch/Big Data/documents_small.txt";
        String targetAddress = "/home/jean/Schreibtisch/Big Data/documents_small.index";
        
        WordAggregator wa = aggregate(sourceAddress);
        save(targetAddress, wa.getWords());
        
        System.out.println("Done!");
    }
    
    private static WordAggregator aggregate(String sourceAddress) {
        // Setup tasks.
        WordAggregator wa = new WordAggregator(Paths.get(sourceAddress));
        Thread t1 = new Thread(wa);
        
        // Start thread.
        t1.start();
        
        // Check thread.
        int documentsCurrent = 0;
        int documentsTotal = 0;
        boolean continueLoop = true;
        
        do {
            // Remember the current status.
            documentsCurrent = wa.getDocumentsCurrent();
            documentsTotal = wa.getDocumentsTotal();
            
            // Decide on loop continuation.
            continueLoop = (!wa.isStarted() || documentsCurrent < documentsTotal);
            
            // Output current progress.
            if(documentsTotal > 0) {
                System.out.println("Aggregating " + documentsCurrent + " of " + documentsTotal + ". Found words: " + wa.getWords().size() + ".");
            }
            
            // Sleep for some time.
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                System.err.println("Error: " + ex.getMessage());
            }
        } while(continueLoop);
        
        // Wait for thread.
        try {
            t1.join();
        } catch (InterruptedException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        
        // Return word aggreator.
        return wa;
    }
    
    private static void save(String targetAddress, HashMap<String, HashSet<Integer>> words) {
        
        // Setup tasks.
        IndexWriter iw = new IndexWriter(targetAddress, words);
        Thread t2 = new Thread(iw);
        
        // Start thread.
        t2.start();
        
        // Check thread.
        int linesCurrent = 0;
        int linesTotal = 0;
        boolean continueLoop = true;
        do {
            // Remember the current status.
            linesCurrent = iw.getCurrentProgress();
            linesTotal = iw.getTotalProgress();
            
            // Decide on loop continuation.
            continueLoop = (!iw.isStarted() || linesCurrent < linesTotal);
            
            // Output current progress.
            if(linesTotal > 0) {
                System.out.println("Concatenating " + linesCurrent + " of " + linesTotal + ".");
            }
            
            // Sleep for some time.
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                System.err.println("Error: " + ex.getMessage());
            }
        } while(continueLoop);
        
        // Wait for thread.
        try {
            t2.join();
        } catch (InterruptedException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }
}
