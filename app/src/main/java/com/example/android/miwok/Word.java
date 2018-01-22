package com.example.android.miwok;

/**
 * Created by test-pc on 29-Dec-17.
 */

public class Word {
    /** Default translation for the word */
    private String englishTranslation;

    /** Miwok translation for the word */
    private String miwokTranslation;

    /** Constant value that represents no image was provided for this word */
    private Integer imageResourceID = NO_IMAGE_PROVIDED;

    /** Image resource ID for the word */
    private static final Integer NO_IMAGE_PROVIDED = -1;

    /** Audio resource ID for the word */
    private int soundResourceID;

    /**
     * Create a new Word object.
     *
     * @param englishTranslation the word in a language that the user is already familiar with
     *                           (such as English)
     * @param miwokTranslation is the word in the Miwok language
     * @param soundResourceID is the resource ID for the audio file associated with this word
     */
    public Word(String englishTranslation, String miwokTranslation, int soundResourceID)
        {
            this.englishTranslation = englishTranslation;
            this.miwokTranslation = miwokTranslation;
            this.soundResourceID = soundResourceID;
        }

    /**
     * Create a new Word object.
     *
     * @param englishTranslation the word in a language that the user is already familiar with
     *                           (such as English)
     * @param miwokTranslation is the word in the Miwok language
     * @param imageResourceID is the drawable resource ID for the image associated with the word
     * @param soundResourceID is the resource ID for the audio file associated with this word
     */
    public Word(String englishTranslation, String miwokTranslation, Integer imageResourceID, int soundResourceID)
        {
            this.englishTranslation = englishTranslation;
            this.miwokTranslation = miwokTranslation;
            this.imageResourceID = imageResourceID;
            this.soundResourceID = soundResourceID;
        }

    /**
     * Get the default translation of the word.
     */
    public String getEnglishTranslation()
        {
            return this.englishTranslation;
        }

    /**
     * Get the Miwok translation of the word.
     */
    public String getMiwokTranslation()
        {
            return this.miwokTranslation;
        }

    /**
     * Return the image resource ID of the word.
     */
    public Integer getImageResourceID() {
        return imageResourceID;
    }

    /**
     * Returns whether or not there is an image for this word.
     */
    public boolean hasImage()
        {
            return imageResourceID != NO_IMAGE_PROVIDED;
        }

    /**
     * Return the audio resource ID of the word.
     */
    public int getSoundResourceID(){ return soundResourceID; }
}
