package com.danibuiza.for360t.numberencoding;

/**
 * Utility class for phone encodings comparisons. Provides methods that return differences between
 * phones and words combinations
 * 
 * @author Daniel Gutierrez Diez
 */
public class NumberEncodingComparator
{
    public static final int NOT_FOUND = -2;

    /**
     * @param phone
     * @param dictItem
     * @return 0, if the difference is found in the first position, the phone (cleaned) lenght -1 if
     *         the difference is found at the end, the position where the difference is found if it
     *         is in the middle of the phone, -2 if there are more or less than 1 difference
     */
    public static int equalsButOne( PhoneItem phone, DictionaryItem dictItem )
    {

        int pos = compare( phone, dictItem, 1 );
        if( pos == 1 )
        {
            pos = 0;
        }
        else if( pos == 0 )
        {
            pos = phone.getCleanedPhoneNumber().length() - 1;
        }
        else if( pos == -1 )
        {
            pos = getSingleDiffMiddle( phone, dictItem );
        }

        return pos;
    }

    /**
     * Returns the position where the difference is located, it is assumed that there is only one
     * difference
     * 
     * @param phone
     * @param dictItem
     * @return
     */
    private static int getSingleDiffMiddle( PhoneItem phone, DictionaryItem dictItem )
    {
        for( int i = 0; i < dictItem.getDecodedWord().length(); i++ )
        {
            if( dictItem.getDecodedWord().charAt( i ) != phone.getCleanedPhoneNumber().charAt( i ) )
            {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * @param phone
     * @param dictItem
     * @return the position where the difference starts, -2 if there more or less than 2 differences
     */
    public static int equalsButTwo( PhoneItem phone, DictionaryItem dictItem )
    {
        return compare( phone, dictItem, 2 );
    }

    private static int compare( PhoneItem phone, DictionaryItem dictItem, int differences )
    {
        int pos = NOT_FOUND;

        if( dictItem != null && phone != null && phone.getCleanedPhoneNumber() != null
            && dictItem.getDecodedWord() != null
            && phone.getCleanedPhoneNumber().length() == dictItem.getDecodedWord().length() + differences )
        {
            return phone.getCleanedPhoneNumber().indexOf( dictItem.getDecodedWord() );
        }
        return pos;
    }

    public static boolean completelyEquals( PhoneItem phone, DictionaryItem dictItem )
    {
        return dictItem != null && phone != null && phone.getCleanedPhoneNumber() != null
            && dictItem.getDecodedWord() != null && dictItem.getDecodedWord().equals( phone.getCleanedPhoneNumber() );
    }

    public static boolean subString( PhoneItem phone, DictionaryItem dictItem )
    {
        return dictItem != null && phone != null && phone.getCleanedPhoneNumber() != null
            && dictItem.getDecodedWord() != null && phone.getCleanedPhoneNumber().contains( dictItem.getDecodedWord() );
    }
}
