package com.example.android.miwok;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {

    /** Handles playback of all the sound files */
    private MediaPlayer mediaPlayer;
    private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer)
                {
                    releaseMediaPlayer();
                }
        };

    /** Handles audio focus when playing a sound file */
    private AudioManager audioManager;

    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener()
        {
            @Override
            public void onAudioFocusChange(int i)
                {
                    if(i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)
                        {
                            // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                            // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                            // our app is allowed to continue playing sound but at a lower volume. We'll treat
                            // both cases the same way because our app is playing short sound files.
                            // Pause playback and reset player to the start of the file. That way, we can
                            // play the word from the beginning when we resume playback.
                            mediaPlayer.pause();
                            mediaPlayer.seekTo(0);
                        }
                    else if(i == AudioManager.AUDIOFOCUS_GAIN)
                        {
                            // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                            mediaPlayer.start();
                        }
                    else if(i == AudioManager.AUDIOFOCUS_LOSS)
                        {
                            // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                            // Stop playback and clean up resources
                            releaseMediaPlayer();
                        }
                }
        };

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer()
        {
            // If the media player is not null, then it may be currently playing a sound.
            if (mediaPlayer != null)
                {
                    // Regardless of the current state of the media player, release its resources
                    // because we no longer need it.
                    mediaPlayer.release();

                    // Set the media player back to null. For our code, we've decided that
                    // setting the media player to null is an easy way to tell that the media player
                    // is not configured to play an audio file at the moment.
                    mediaPlayer = null;

                    //Regardless of whether or not we are granted audio focus, abandon it. This also
                    //unregisters the AudioFocusChangeListener so we dont get anymore callbacks
                    audioManager.abandonAudioFocus(onAudioFocusChangeListener);
                }
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // Create a list of words
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("Red", "Wetetti", R.drawable.color_red, R.raw.color_red));
        words.add(new Word("Green", "Chokokki", R.drawable.color_green, R.raw.color_green));
        words.add(new Word("Brown", "Takaakka", R.drawable.color_brown, R.raw.color_brown));
        words.add((new Word("Gray", "Topoppi", R.drawable.color_gray, R.raw.color_gray)));
        words.add(new Word("Black", "Kululli", R.drawable.color_black, R.raw.color_black));
        words.add(new Word("White", "Kelelli", R.drawable.color_white, R.raw.color_white));
        words.add((new Word("Dusty Yellow", "Topiisa", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow)));
        words.add(new Word("Mustard Yellow", "Chiwiita", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));

        // Create an {@link ArrayAdapter}, whose data source is a list of Strings. The
        // adapter knows how to create layouts for each item in the list, using the
        // simple_list_item_1.xml layout resource defined in the Android framework.
        // This list item layout contains a single {@link TextView}, which the adapter will set to
        // display a single word.
        WordAdapter adapter = new WordAdapter(this, words, R.color.category_colors);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // activity_numbers.xml layout file.
        ListView listView = (ListView) findViewById(R.id.list);

        // Make the {@link ListView} use the {@link ArrayAdapter} we created above, so that the
        // {@link ListView} will display list items for each word in the list of words.
        // Do this by calling the setAdapter method on the {@link ListView} object and pass in
        // 1 argument, which is the {@link ArrayAdapter} with the variable name itemsAdapter.
        listView.setAdapter(adapter);

        // Set a click listener to play the audio when the list item is clicked on
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                    {
                        // Get the {@link Word} object at the given position the user clicked on
                        Word word = words.get(i);

                        //Release the media player if it currently exists because we are about to play
                        //a different sound file
                        releaseMediaPlayer();

                        int result = audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                        if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
                            {
                                //We have audio focus now

                                // Create and setup the {@link MediaPlayer} for the audio resource associated
                                // with the current word
                                mediaPlayer = MediaPlayer.create(ColorsActivity.this, word.getSoundResourceID());

                                // Start the audio file
                                mediaPlayer.start();

                                //Setup a listener on the media player, so that we can stop and release the
                                //mediaplayer once the sound has finished.
                                mediaPlayer.setOnCompletionListener(completionListener);
                            }
                    }
            }
        );
    }

    @Override
    protected void onStop()
        {
            super.onStop();
            //When the activity is stopped, release the media player resource because we won't
            //be playing anymore sound
            releaseMediaPlayer();
        }
}
