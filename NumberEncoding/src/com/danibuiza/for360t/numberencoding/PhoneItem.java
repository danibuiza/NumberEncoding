package com.danibuiza.for360t.numberencoding;

/**
 * This class abstract phone numbers and provides functionalities to handle the encoding process
 * easily
 * 
 * @author Daniel Gutierrez Diez
 */
public class PhoneItem
{
    String phoneNumber;

    String cleanedPhoneNumber;

    public PhoneItem( String phoneNumber )
    {
        this.phoneNumber = phoneNumber;
        this.cleanedPhoneNumber = clean( this.phoneNumber );
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public String getCleanedPhoneNumber()
    {
        return cleanedPhoneNumber;
    }

    /**
     * Cleans a phone number by deleting "/" and "-"
     * 
     * @param phoneNumber
     * @return
     */
    private String clean( String phoneNumber )
    {
        String cleanedPhone = null;
        if( phoneNumber != null )
        {
            cleanedPhone = phoneNumber.replaceAll( "/", "" );
            cleanedPhone = cleanedPhone.replaceAll( "-", "" );
        }

        return cleanedPhone;
    }

}
