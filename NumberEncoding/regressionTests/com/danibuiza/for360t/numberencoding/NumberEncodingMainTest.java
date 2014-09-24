package com.danibuiza.for360t.numberencoding;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * This class executes the main program using as parameters two small test files for the phones and
 * the words input, redirects the standard output to a file, resets the standards output to the
 * console afterwards, and checks if the expected results are the ones produced
 * 
 * @author dgutierrez-diez
 */
public class NumberEncodingMainTest
{
    List<String> predefinedList;

    @Before
    public void setUp()
    {
        predefinedList = new ArrayList<String>();

        predefinedList.add( "5624-82: Mix Tor" );
        predefinedList.add( "5624-82: mir Tor" );
        predefinedList.add( "4824: Torf" );
        predefinedList.add( "4824: Tor 4" );
        predefinedList.add( "4824: fort" );
        predefinedList.add( "381482: so 1 Tor" );
        predefinedList.add( "04824: 0 fort" );
        predefinedList.add( "04824: 0 Tor 4" );
        predefinedList.add( "04824: 0 Torf" );
        predefinedList.add( "10/783--5: je bo\"s 5" );
        predefinedList.add( "10/783--5: neu o\"d 5" );
        predefinedList.add( "10/783--5: je Bo\" da" );

    }

    private void redirectStandardOutputToFile() throws FileNotFoundException
    {
        File file = new File( "regressionTests/output_test.txt" );
        FileOutputStream fos = new FileOutputStream( file );
        PrintStream ps = new PrintStream( fos );
        System.setOut( ps );
    }

    @Test
    public void testMain() throws InterruptedException, IOException
    {

        redirectStandardOutputToFile();

        // call main
        String[] params = { "docs/input_small.txt", "docs/dictionary_small.txt", "false" };
        NumberEncodingMain.main( params );

        InputStream isDict = new FileInputStream( new File( "regressionTests/output_test.txt" ) );
        BufferedReader brDict = new BufferedReader( new InputStreamReader( isDict ) );
        List<String> words = new ArrayList<String>();
        brDict.lines().forEach( encoding -> words.add( encoding ) );
        brDict.close();

        // actual test
        words.forEach( word -> assertTrue( predefinedList.contains( word ) ) );
        // words.forEach(System.out::println);
        // predefinedList.forEach(System.out::println);

    }

}
