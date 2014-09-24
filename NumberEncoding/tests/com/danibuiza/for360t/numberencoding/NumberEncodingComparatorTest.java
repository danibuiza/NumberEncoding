package com.danibuiza.for360t.numberencoding;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NumberEncodingComparatorTest
{

    @Test
    public void testEqualsButOne()
    {
        PhoneItem phone = null;
        DictionaryItem dictItem = null;
        assertEquals( NumberEncodingComparator.NOT_FOUND, NumberEncodingComparator.equalsButOne( phone, dictItem ) );

        phone = new PhoneItem( "514031" );
        dictItem = new DictionaryItem( "antes", true );
        assertEquals( 5, NumberEncodingComparator.equalsButOne( phone, dictItem ) );

        phone = new PhoneItem( "051403" );
        dictItem = new DictionaryItem( "antes", true );
        assertEquals( 0, NumberEncodingComparator.equalsButOne( phone, dictItem ) );

        phone = new PhoneItem( "00251403" );
        dictItem = new DictionaryItem( "antes", true );
        assertEquals( NumberEncodingComparator.NOT_FOUND, NumberEncodingComparator.equalsButTwo( phone, dictItem ) );
    }

    @Test
    public void testEqualsButTwo()
    {
        PhoneItem phone = null;
        DictionaryItem dictItem = null;
        assertEquals( NumberEncodingComparator.NOT_FOUND, NumberEncodingComparator.equalsButTwo( phone, dictItem ) );

        phone = new PhoneItem( "5140315" );
        dictItem = new DictionaryItem( "antes", true );
        assertEquals( 0, NumberEncodingComparator.equalsButTwo( phone, dictItem ) );

        phone = new PhoneItem( "0251403" );
        dictItem = new DictionaryItem( "antes", true );
        assertEquals( 2, NumberEncodingComparator.equalsButTwo( phone, dictItem ) );

        phone = new PhoneItem( "00251403" );
        dictItem = new DictionaryItem( "antes", true );
        assertEquals( NumberEncodingComparator.NOT_FOUND, NumberEncodingComparator.equalsButTwo( phone, dictItem ) );
    }

    @Test
    public void testSubStringMoreTwo()
    {
        PhoneItem phone = null;
        DictionaryItem dictItem = null;
        assertFalse( NumberEncodingComparator.subStringMoreTwo( phone, dictItem ) );

        phone = new PhoneItem( "51403255" );
        dictItem = new DictionaryItem( "antes", true );
        assertTrue( NumberEncodingComparator.subStringMoreTwo( phone, dictItem ) );

        phone = new PhoneItem( "02514032" );
        dictItem = new DictionaryItem( "antes", true );
        assertTrue( NumberEncodingComparator.subStringMoreTwo( phone, dictItem ) );

        phone = new PhoneItem( "0251254032" );
        dictItem = new DictionaryItem( "antes", true );
        assertFalse( NumberEncodingComparator.subStringMoreTwo( phone, dictItem ) );
    }

    @Test
    public void testCompletelyEquals()
    {
        PhoneItem phone = null;
        DictionaryItem dictItem = null;
        assertFalse( NumberEncodingComparator.completelyEquals( phone, dictItem ) );

        phone = new PhoneItem( "123456789" );
        dictItem = new DictionaryItem( "antes", true );
        assertFalse( NumberEncodingComparator.completelyEquals( phone, dictItem ) );

        phone = new PhoneItem( "51403" );
        dictItem = new DictionaryItem( "antes", true );
        assertTrue( NumberEncodingComparator.completelyEquals( phone, dictItem ) );

        phone = new PhoneItem( "051403" );
        dictItem = new DictionaryItem( "antes", true );
        assertFalse( NumberEncodingComparator.completelyEquals( phone, dictItem ) );
    }

}
