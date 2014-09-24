package com.danibuiza.for360t.numberencoding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class reads a list of phone numbers and tries to encode them using combinations from a given
 * list of words
 * 
 * @author Daniel Gutierrez Diez
 */
public class NumberEncodingMain
{

    // This file can be infinite and cannot be stored in memory
    private static String                     DEFAULT_PHONES_FILE_NAME     = "docs/input.txt";

    // This file has a maximum of 7500 words and can be stored in memory.
    private static String                     DEFAULT_DICTIONARY_FILE_NAME = "docs/dictionary.txt";

    // List of words to be stored in memory
    private static final List<DictionaryItem> words                        = new ArrayList<DictionaryItem>();

    // Number of maximum threads to use in the pool
    private static final int                  NUMBER_THREADS               = 10;

    /**
     * main program
     * 
     * @param args
     * @throws InterruptedException
     * @throws IOException
     */
    public static void main( String[] params ) throws InterruptedException, IOException
    {

        long start = System.currentTimeMillis();
        boolean printDuration = true;
        if( params != null && params.length == 3 )
        {
            DEFAULT_PHONES_FILE_NAME = params[0];
            DEFAULT_DICTIONARY_FILE_NAME = params[1];
            printDuration = Boolean.valueOf( params[2] );
        }

        // stores in memory the list of words with related decodings
        createDictionary();

        // Phones are handle directly from the file and not stored in memory
        InputStream isPhones = new FileInputStream( new File( DEFAULT_PHONES_FILE_NAME ) );
        Scanner scannerPhones = new Scanner( isPhones, "UTF-8" );

        // init the thread pool manager
        ExecutorService executor = Executors.newFixedThreadPool( NUMBER_THREADS );

        // iterates through each phone and find matches
        while( scannerPhones.hasNextLine() )
        {
            // using lambdas execute in different threads the encoding for each phone
            String nextLine = scannerPhones.nextLine();
            if( nextLine != null && nextLine.length() > 0 && nextLine.length() < 50 )
            {
                PhoneItem phone = new PhoneItem( nextLine );
                executor.execute( ( ) -> findMatches( phone ) );
            }

        }

        // shutdown
        executor.shutdown();

        // wait for all threads to finish
        while( !executor.isTerminated() )
        {}

        // closes scanner and input stream
        if( scannerPhones != null )
        {
            scannerPhones.close();
        }
        if( isPhones != null )
        {
            isPhones.close();
        }
        long end = System.currentTimeMillis();

        if( printDuration )
        {
            System.out.println( "Time: " + ( end - start ) + " ms." );
        }

    }

    /**
     * Copies all the words in a list that can be reused
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void createDictionary() throws FileNotFoundException, IOException
    {
        // words can be stored in memory
        InputStream isDict = new FileInputStream( new File( DEFAULT_DICTIONARY_FILE_NAME ) );
        BufferedReader brDict = new BufferedReader( new InputStreamReader( isDict ) );
        brDict.lines().forEach( word -> words.add( new DictionaryItem( word, true ) ) );
        brDict.close();
    }

    /**
     * Finds all encodings for the given phone using words present in the dictionary
     * 
     * @param phone
     */
    private static void findMatches( PhoneItem phone )
    {
        // encodings for a given phone
        Map<String, String> results = new HashMap<String, String>();

        // potential encoding that have to be verified for a given phone
        Map<String, String> conflicts = new HashMap<String, String>();

        // filtered list of words containing only potential phone encoding parts
        List<DictionaryItem> filteredWords = NumberEncoder.filterWords( words, phone );

        // for each potential part we try to find the encodings for a phone
        filteredWords.forEach( dictItem -> NumberEncoder.match( dictItem, phone, results, conflicts, filteredWords ) );

        // before printing results is needed to resolve the conflicts arised
        resolveConflicts( conflicts, results );

        printResults( results );

    }

    /**
     * Resolves potential conflicts given and stores the successful ones in the results passed as
     * parameter
     * 
     * @param conflicts
     * @param results
     */
    private static void resolveConflicts( Map<String, String> conflicts, Map<String, String> results )
    {
        for( String conflict : conflicts.keySet() )
        {
            if( NumberEncoder.resolveConflict( conflict, conflicts, results ) )
                results.put( conflict, conflicts.get( conflict ) );
        }
    }

    /**
     * Prints the results
     * 
     * @param results
     */
    private static void printResults( Map<String, String> results )
    {

        for( String result : results.keySet() )
        {
            System.out.println( results.get( result ) + ": " + result );
        }

    }

}
