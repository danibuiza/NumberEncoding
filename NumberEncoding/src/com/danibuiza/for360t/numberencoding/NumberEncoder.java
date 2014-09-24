package com.danibuiza.for360t.numberencoding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility class with encoding and decoding methods
 * 
 * @author Daniel Gutierrez Diez
 */
public class NumberEncoder
{

    private static final char JOKER = '#';

    /**
     * Filters the list of words passed as parameter with the ones that can be used for encoding the
     * given phone number, if no one applies, an empty list is returned
     * 
     * @param words
     * @param phone
     * @return a list of {@link DictionaryItem}
     */
    public static List<DictionaryItem> filterWords( List<DictionaryItem> words, PhoneItem phone )
    {
        List<DictionaryItem> filteredWords = new ArrayList<DictionaryItem>();
        if( phone != null && phone.getCleanedPhoneNumber() != null )
        {
            words.stream().filter( dictItem -> dictItem.getDecodedWord() != null )
                    .filter( dictItem -> phone.getCleanedPhoneNumber().contains( dictItem.getDecodedWord() ) )
                    .forEach( dictItem -> filteredWords.add( dictItem ) );
        }
        return filteredWords;
    }

    /**
     * For a given conflict, decides if it is a valid result or not
     * 
     * @param conflict, the conflict to check
     * @param conflicts, map with all the potential conflicts
     * @param results, map with all the valid results
     * @return true if it is a valid result, false otherwise
     */
    public static boolean resolveConflict( String conflict, Map<String, String> conflicts, Map<String, String> results )
    {
        boolean valid = true;
        for( int i = 0; conflict != null && i < conflict.length(); ++i )
        {
            // checks every numeric value present in the conflict
            if( String.valueOf( conflict.charAt( i ) ).matches( "[0-9]" ) )
            {
                valid = compareAgainstMap( conflict, conflicts, i );
                if( valid )
                {
                    valid = compareAgainstMap( conflict, results, i );
                }
            }
        }
        return valid;
    }

    /**
     * Compares a given conflict against a Map with potential results using the position i
     * 
     * @param conflict
     * @param conflicts
     * @param position
     * @return true if there is no other possibility than adding the number at this position
     */
    private static boolean compareAgainstMap( String conflict, Map<String, String> potentialResults, int position )
    {
        if( potentialResults != null )
        {
            for( String otherConflict : potentialResults.keySet() )
            {
                if( !conflict.equals( otherConflict ) && otherConflict.length() > position + 1 )
                {
                    if( !String.valueOf( otherConflict.charAt( position ) ).matches( "[0-9]" )
                        && !String.valueOf( otherConflict.charAt( position + 1 ) ).matches( "[0-9]" ) )
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void match( DictionaryItem dictItem, PhoneItem phone, Map<String, String> results,
            Map<String, String> conflicts, List<DictionaryItem> filteredWords )
    {
        // potential encoding, store it as a valid result if no numeric character is used, to the
        // potential ones otherwise
        if( NumberEncodingComparator.completelyEquals( phone, dictItem ) )
        {
            addPotentialResult( dictItem, phone, results, conflicts );
        }
        else if( NumberEncodingComparator.subString( phone, dictItem ) )
        {
            matchAllCleanCombinations( dictItem, phone, results, conflicts, filteredWords );
            if( NumberEncodingComparator.equalsButOne( phone, dictItem ) != NumberEncodingComparator.NOT_FOUND )
            {
                // potential use of parts of the phone
                match1diffs( dictItem, phone, results, conflicts, filteredWords );
            }
            else if( NumberEncodingComparator.equalsButTwo( phone, dictItem ) != NumberEncodingComparator.NOT_FOUND )
            {
                // potential use of parts of the phone
                match2diffs( dictItem, phone, results, conflicts, filteredWords );
            }
        }
    }

    /**
     * Adds an encoding to the results or to the conflicts passed
     * 
     * @param dictItem
     * @param phone
     * @param results
     * @param conflicts
     */
    private static void addPotentialResult( DictionaryItem dictItem, PhoneItem phone, Map<String, String> results,
            Map<String, String> conflicts )
    {
        if( dictItem.isSuffixed() || dictItem.isPrefixed() )
        {
            if( conflicts != null )
            {
                conflicts.put( dictItem.getWord(), phone.getPhoneNumber() );
            }
        }
        else
        {
            if( results != null )
            {
                results.put( dictItem.getWord(), phone.getPhoneNumber() );
            }
        }
    }

    /**
     * Recursively calls the match method with all possible combinations using the passed
     * {@link DictionaryItem} without adding any parts of the phone
     * 
     * @param dictItem
     * @param phone
     * @param results
     * @param conflicts
     * @param filteredWords
     */
    private static void matchAllCleanCombinations( DictionaryItem dictItem, PhoneItem phone,
            Map<String, String> results, Map<String, String> conflicts, List<DictionaryItem> filteredWords )
    {
        for( DictionaryItem otherDictitem : filteredWords )
        {
            match( new DictionaryItem( dictItem, otherDictitem ), phone, results, conflicts, filteredWords );
        }
    }

    /**
     * Tries to match the passed {@link DictionaryItem} using all possible combinations and adding
     * parts of the phone to the beggining and to the end if needed
     * 
     * @param dictItem
     * @param phone
     * @param results
     * @param conflicts
     * @param filteredWords
     */
    private static void match2diffs( DictionaryItem dictItem, PhoneItem phone, Map<String, String> results,
            Map<String, String> conflicts, List<DictionaryItem> filteredWords )
    {
        DictionaryItem dictItemPrefixed = null;
        if( !dictItem.isPrefixed() )
        {
            dictItemPrefixed = new DictionaryItem( phone.getCleanedPhoneNumber().charAt( 0 ), dictItem );
            DictionaryItem dictItemSuffixed = null;
            if( !dictItem.isSuffixed() )
            {
                dictItemSuffixed = new DictionaryItem( dictItemPrefixed, phone.getCleanedPhoneNumber().charAt( phone
                        .getCleanedPhoneNumber().length() - 1 ) );
                match( dictItemSuffixed, phone, results, conflicts, filteredWords );

            }
        }
    }

    /**
     * Ties to match the passed {@link DictionaryItem} using all possible combinations and adding
     * parts of the phone to the beggining or to the end if needed
     * 
     * @param dictItem
     * @param phone
     * @param results
     * @param conflicts
     * @param filteredWords
     */
    private static void match1diffs( DictionaryItem dictItem, PhoneItem phone, Map<String, String> results,
            Map<String, String> conflicts, List<DictionaryItem> filteredWords )
    {
        int position = NumberEncodingComparator.equalsButOne( phone, dictItem );

        if( position == 0 )
        {
            DictionaryItem dictItemPrefixed = null;
            if( !dictItem.isPrefixed() )
            {
                dictItemPrefixed = new DictionaryItem( phone.getCleanedPhoneNumber().charAt( position ), dictItem );
                match( dictItemPrefixed, phone, results, conflicts, filteredWords );
            }

        }
        else if( position == phone.getCleanedPhoneNumber().length() - 1 )
        {
            DictionaryItem dictItemSuffixed = null;
            if( !dictItem.isSuffixed() )
            {
                dictItemSuffixed = new DictionaryItem( dictItem, phone.getCleanedPhoneNumber().charAt( position ) );
                match( dictItemSuffixed, phone, results, conflicts, filteredWords );

            }
        }
        else if( position > 0 )
        {
            int counter = 0;
            int counterParts = 0;
            for( DictionaryItem part : dictItem.getParts() )
            {
                counter += part.getDecodedWord().length();
                if( counter == position )
                {
                    dictItem.modifyPart( counterParts, phone.getCleanedPhoneNumber().charAt( position ) );
                    match( dictItem, phone, results, conflicts, filteredWords );
                }
                else if( counter > position )
                {
                    break;

                }
                counterParts++;
            }
        }
    }

    /**
     * Decodes a given word: it cleans it by deleting all " and spaces; and returns an string with
     * each character replaced by its decoding
     * 
     * @param word
     * @return a decoded word
     */
    public static String decode( String word )
    {
        String decodedWord = null;
        if( word != null )
        {
            String cleanedWord = word.replaceAll( "\"", "" );
            cleanedWord = cleanedWord.replaceAll( " ", "" );

            decodedWord = "";
            for( int i = 0; i < cleanedWord.length(); i++ )
            {
                char decodedChar = decodeChar( cleanedWord.charAt( i ) );
                if( JOKER != decodedChar )
                {
                    decodedWord += decodedChar;
                }
            }
        }
        return decodedWord;
    }

    private static char decodeChar( char item )
    {
        switch( Character.toLowerCase( item ) )
        {
            case 'a':
                return '5';
            case 'b':
                return '7';
            case 'c':
                return '6';
            case 'd':
                return '3';
            case 'e':
                return '0';
            case 'f':
                return '4';
            case 'g':
                return '9';
            case 'h':
                return '9';
            case 'i':
                return '6';
            case 'j':
                return '1';
            case 'k':
                return '7';
            case 'l':
                return '8';
            case 'm':
                return '5';
            case 'n':
                return '1';
            case 'o':
                return '8';
            case 'p':
                return '8';
            case 'q':
                return '1';
            case 'r':
                return '2';
            case 's':
                return '3';
            case 't':
                return '4';
            case 'u':
                return '7';
            case 'v':
                return '6';
            case 'w':
                return '2';
            case 'x':
                return '2';
            case 'y':
                return '3';
            case 'z':
                return '9';
            case '1':
                return '1';
            case '2':
                return '2';
            case '3':
                return '3';
            case '4':
                return '4';
            case '5':
                return '5';
            case '6':
                return '6';
            case '7':
                return '7';
            case '8':
                return '8';
            case '9':
                return '9';
            case '0':
                return '0';
            default:
                return JOKER;

        }
    }

}
