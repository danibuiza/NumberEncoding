package com.danibuiza.for360t.numberencoding;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DictionaryItemTest
{

    @Test
    public void testConstructors()
    {
        DictionaryItem di = new DictionaryItem( "antes", true );
        DictionaryItem noparts = new DictionaryItem( "despues", false );

        assertEquals( "antes", di.getWord() );
        assertEquals( "51403", di.getDecodedWord() );
        assertEquals( false, di.isCombined() );
        assertEquals( false, di.isPrefixed() );
        assertEquals( false, di.isSuffixed() );
        assertEquals( 1, di.getParts().size() );

        assertEquals( 0, noparts.getParts().size() );

        DictionaryItem combined = new DictionaryItem( di, noparts );
        assertEquals( 2, combined.getParts().size() );

        DictionaryItem modifiedSuf = new DictionaryItem( di, '3' );
        assertEquals( 1, modifiedSuf.getParts().size() );

        DictionaryItem modifiedPre = new DictionaryItem( '3', di );
        assertEquals( 1, modifiedPre.getParts().size() );

        combined.modifyPart( 1, '6' );

        assertEquals( "antes despues 6", combined.getWord() );

        combined.modifyPart( 4, '6' );
        // no change
        assertEquals( "antes despues 6", combined.getWord() );

        DictionaryItem empty = new DictionaryItem( "", true );
        assertEquals( "", empty.getWord() );

    }

}
