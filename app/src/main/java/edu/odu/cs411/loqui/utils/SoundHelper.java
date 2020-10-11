<<<<<<< HEAD
package edu.odu.cs411.loqui.utils;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.Builder;
import android.os.Build;

import androidx.annotation.RequiresApi;

import edu.odu.cs411.loqui.R;

/**
 * Created by Richard Atwell on 4/11/2020
 * Old Dominion University
 * ratwe002@odu.edu
 */
public class SoundHelper {

    private final static String TAG = "SoundHelper";

    private MediaPlayer myPlayer;
    private SoundPool mySoundPool;
    private int successSoundID0, successSoundID1, incorrectSoundID0, incorrectSoundID1, homeSound,
                startListeningSound, endListeningSound;
    private boolean isLoaded;
    private float myVolume;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public SoundHelper(Activity activity){

        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        float actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        myVolume = actVolume / maxVolume;

        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        mySoundPool = new Builder().setAudioAttributes(attributes).setMaxStreams(1).build();

        mySoundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> isLoaded = true);

        successSoundID0 = mySoundPool.load(activity, R.raw.success0,1);
        successSoundID1 = mySoundPool.load(activity, R.raw.success1,1);
        incorrectSoundID0 = mySoundPool.load(activity, R.raw.wrong0, 1);
        incorrectSoundID1 = mySoundPool.load(activity, R.raw.wrong1, 1);
        homeSound = mySoundPool.load(activity,R.raw.home, 1);
        startListeningSound = mySoundPool.load(activity, R.raw.startlistening, 1);
        endListeningSound = mySoundPool.load(activity, R.raw.endlistening, 1);
    }

    public void playCorrectSound() {

        int randomSound = (int)(Math.random()*(2));

        if (randomSound == 0) {
            mySoundPool.play(successSoundID0, myVolume, myVolume, 1, 0, 1f);
        }

        else if (randomSound == 1) {
            mySoundPool.play(successSoundID1, myVolume, myVolume, 1, 0, 1f);
        }

    }

    public void playIncorrectSound() {

        int randomSound = (int)(Math.random()*(2));

        if (randomSound == 0) {
            mySoundPool.play(incorrectSoundID0, myVolume, myVolume, 1, 0, 1f);
        }

        else if (randomSound == 1) {
            mySoundPool.play(incorrectSoundID1, myVolume, myVolume, 1, 0, 1f);
        }

    }

    public void playHomeSound() {
        mySoundPool.play(homeSound, myVolume, myVolume, 1, 0, 1f);
    }

    public void playStartListeningSound() {
        mySoundPool.play(startListeningSound, myVolume, myVolume,
                1, 0, 1f);
    }

    public void playEndListeningSound() {
        mySoundPool.play(endListeningSound, myVolume, myVolume,
                1, 0, 1f);
    }

}

=======
version https://git-lfs.github.com/spec/v1
oid sha256:10c26c282d90422de47578b42ab3d8fa3cab8f67a1774642cdc800c2f9ef3f16
size 3278
>>>>>>> 69b172438de57648718503ac42d98caad0ea5cf8
