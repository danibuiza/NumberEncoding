package com.danibuiza.for360t.numberencoding;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PhoneItemTest
{

    // covers everything
    @Test
    public void testClean()
    {

        PhoneItem pi = new PhoneItem( null );
        assertNull( pi.getPhoneNumber() );
        assertNull( pi.getCleanedPhoneNumber() );

        PhoneItem pi2 = new PhoneItem( "123456789" );
        assertEquals( "123456789", pi2.getPhoneNumber() );
        assertEquals( "123456789", pi2.getCleanedPhoneNumber() );

        PhoneItem pi3 = new PhoneItem( "-1234-5/6-7-8/9-" );
        assertEquals( "-1234-5/6-7-8/9-", pi3.getPhoneNumber() );
        assertEquals( "123456789", pi3.getCleanedPhoneNumber() );

    }
}
