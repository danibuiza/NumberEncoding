package com.danibuiza.for360t.numberencoding;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that abstracts an item in the word list and provides functionalities to handle the encoding
 * process easily. All constructors do not accept nulls as parameter and throw exceptions
 * (assumption)
 * 
 * @author Daniel Gutierrez Diez
 */
public class DictionaryItem
{

    private String               word;

    private String               decodedWord;

    private boolean              suffixed;

    private boolean              prefixed;

    private boolean              combined;

    private List<DictionaryItem> parts;

    /**
     * private basic constructor, decodes the word passed as parameter
     * 
     * @param word
     */
    private DictionaryItem( String word )
    {
        this.word = word;
        this.decodedWord = NumberEncoder.decode( this.word );
        this.suffixed = false;
        this.prefixed = false;
        this.combined = false;

        if( this.parts == null )
        {
            this.parts = new ArrayList<DictionaryItem>();
        }

    }

    /**
     * Creates a {@link DictionaryItem} and uses it as a part of itself
     * 
     * @param word
     * @param addAsPart
     */
    public DictionaryItem( String word, boolean addAsPart )
    {
        this( word );
        if( addAsPart )
        {
            this.addPart( this );
        }
    }

    /**
     * Creates a {@link DictionaryItem}, do not uses itself as part and adds a character at the end
     * 
     * @param dictItem
     * @param additionalChar
     */
    public DictionaryItem( DictionaryItem dictItem, char additionalChar )
    {
        this( dictItem.getWord() + " " + additionalChar, false );
        this.prefixed = dictItem.isPrefixed();
        this.suffixed = true;
        this.combined = dictItem.isCombined();
        this.addPart( dictItem );
    }

    /**
     * Creates a {@link DictionaryItem}, do not uses itself as part and adds a character at the
     * beggining
     * 
     * @param additionalChar
     * @param dictItem
     */
    public DictionaryItem( char additionalChar, DictionaryItem dictItem )
    {
        this( additionalChar + " " + dictItem.getWord(), false );
        this.suffixed = dictItem.isSuffixed();
        this.prefixed = true;
        this.combined = dictItem.isCombined();
        this.addPart( dictItem );

    }

    /**
     * Creates a {@link DictionaryItem} using two {@link DictionaryItem}, combines them with an
     * space in between and uses both as parts of it. For example if the first one contains the word
     * "antes" and the second one the word "despues", the result would be a new
     * {@link DictionaryItem} with the word "antes despues"
     * 
     * @param dictItem
     * @param otherDictitem
     */
    public DictionaryItem( DictionaryItem dictItem, DictionaryItem otherDictitem )
    {
        this( dictItem.getWord() + " " + otherDictitem.getWord(), false );
        this.suffixed = dictItem.isSuffixed() || otherDictitem.isSuffixed();
        this.prefixed = dictItem.isPrefixed() || otherDictitem.isPrefixed();
        this.combined = true;
        this.addPart( dictItem );
        this.addPart( otherDictitem );

    }

    /**
     * Modifies a part by adding a character at the end and recalculates the whole
     * {@link DictionaryItem}
     * 
     * @param counterParts
     * @param charAt
     */
    public void modifyPart( int counterParts, char charAt )
    {
        if( getParts() != null && getParts().size() > counterParts )
        {
            getParts().set( counterParts, new DictionaryItem( getParts().get( counterParts ), charAt ) );
            recalculate();
        }
    }

    public boolean isSuffixed()
    {
        return suffixed;
    }

    public boolean isPrefixed()
    {
        return prefixed;
    }

    public boolean isCombined()
    {
        return this.combined;
    }

    public String getWord()
    {
        return word;
    }

    public String getDecodedWord()
    {
        return decodedWord;
    }

    public void addPart( DictionaryItem item )
    {

        // in case there is only one
        if( item.getParts() == null || item.getParts().size() == 0 )
        {
            this.parts.add( item );
        }
        // in case there are more than one
        else
        {
            this.parts.addAll( item.getParts() );
        }
    }

    public List<DictionaryItem> getParts()
    {
        return parts;
    }

    /**
     * recalculates the {@link DictionaryItem} completely: sets the word to the combination of the
     * parts separated by spaces, decodes it afterwards, sets combined to true and prefixed and
     * suffixed to the combination of the parts prefixed and suffixed flags
     */
    private void recalculate()
    {
        this.word = "";
        this.decodedWord = "";
        for( DictionaryItem part : getParts() )
        {
            if( this.word.isEmpty() )
            {
                this.word = part.getWord();
            }
            else
            {
                this.word += " " + part.getWord();
                this.combined = true;
            }
            this.suffixed = part.isSuffixed() || this.suffixed;
            this.prefixed = part.isPrefixed() || this.prefixed;
        }
        this.decodedWord = NumberEncoder.decode( this.word );

    }
}
