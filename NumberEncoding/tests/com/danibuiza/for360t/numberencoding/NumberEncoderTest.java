package com.danibuiza.for360t.numberencoding;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class NumberEncoderTest
{
    @Test
    public void testDecode()
    {
        assertNull( NumberEncoder.decode( null ) );

        assertEquals( "", NumberEncoder.decode( "" ) );

        assertEquals( "", NumberEncoder.decode( "#?=}}}" ) );

        assertEquals( "51403", NumberEncoder.decode( "antes" ) );

        assertEquals( "51403", NumberEncoder.decode( "an\" te s" ) );

        assertEquals( "12345651234561403123456", NumberEncoder.decode( "123456a1###2#????3456n\" te s123456" ) );
    }

    @Test
    public void testFilterWords()
    {
        assertEquals( new ArrayList<DictionaryItem>(), NumberEncoder.filterWords( null, null ) );

        List<DictionaryItem> words = populateWords();
        PhoneItem phone = new PhoneItem( "51482" );
        List<DictionaryItem> filtered = NumberEncoder.filterWords( words, phone );

        assertNotNull( filtered );
        assertEquals( 2, filtered.size() );
        assertEquals( "an", filtered.get( 0 ).getWord() );
        assertEquals( "tor", filtered.get( 1 ).getWord() );
    }

    private List<DictionaryItem> populateWords()
    {
        List<DictionaryItem> words = new ArrayList<DictionaryItem>();
        words.add( new DictionaryItem( "an", true ) );
        words.add( new DictionaryItem( "aalfer", true ) );
        words.add( new DictionaryItem( "tor", true ) );
        words.add( new DictionaryItem( "maria", true ) );
        words.add( new DictionaryItem( "pep", true ) );
        words.add( new DictionaryItem( "juan", true ) );
        words.add( new DictionaryItem( "adolfo", true ) );
        words.add( new DictionaryItem( "dani", true ) );
        words.add( new DictionaryItem( "antonio", true ) );
        words.add( new DictionaryItem( "rodrigo", true ) );
        words.add( new DictionaryItem( "romualdo", true ) );
        words.add( new DictionaryItem( "daniel", true ) );
        words.add( new DictionaryItem( "david", true ) );
        words.add( new DictionaryItem( "pepe", true ) );
        words.add( new DictionaryItem( "eva", true ) );
        words.add( new DictionaryItem( "Torf", true ) );
        words.add( new DictionaryItem( "fort", true ) );

        return words;
    }

    @Test
    public void testMatch()
    {
        Map<String, String> results = null;
        NumberEncoder.match( null, null, results, null, null );

        assertNull( results );

        List<DictionaryItem> words = populateWords();

        DictionaryItem dictItem = new DictionaryItem( "Torf", true );
        PhoneItem phoneItem = new PhoneItem( "4824" );
        Map<String, String> conflicts = null;
        results = new HashMap<String, String>();
        NumberEncoder.match( dictItem, phoneItem, results, conflicts, words );

        assertNotNull( results );
        assertEquals( 1, results.keySet().size() );
        
        phoneItem = new PhoneItem( "04824" );
        NumberEncoder.match( dictItem, phoneItem, results, conflicts, words );

        assertNotNull( results );
        assertEquals( 1, results.keySet().size() );
        
        phoneItem = new PhoneItem( "048241" );
        NumberEncoder.match( dictItem, phoneItem, results, conflicts, words );

        assertNotNull( results );
        assertEquals( 1, results.keySet().size() );

    }

    @Test
    public void testResolveConflict()
    {
        assertTrue( NumberEncoder.resolveConflict( null, null, null ) );

        Map<String, String> conflicts = new HashMap<String, String>();
        Map<String, String> results = new HashMap<String, String>();;

        conflicts.put( "Torf", "4824" );
        conflicts.put( "fort", "4824" );

        String conflict = "4 Ort";
        assertFalse( NumberEncoder.resolveConflict( conflict, conflicts, results ) );

        conflict = "Tor 4";
        assertTrue( NumberEncoder.resolveConflict( conflict, conflicts, results ) );

        conflict = "4 Ort";
        conflicts = new HashMap<String, String>();
        results.put( "Torf", "4824" );
        results.put( "fort", "4824" );
        assertFalse( NumberEncoder.resolveConflict( conflict, conflicts, results ) );

        conflict = "Tor 4";
        assertTrue( NumberEncoder.resolveConflict( conflict, conflicts, results ) );
    }
}
