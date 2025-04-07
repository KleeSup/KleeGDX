package com.github.kleesup.kleegdx.client.sound;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.github.kleesup.kleegdx.core.util.Verify;
import lombok.Getter;
import lombok.Setter;

/**
 * A very simple and lightweight audio engine that plays sounds in a spatial matter and manages general volume.
 * <p>
 * This works for 2d only and auto implementation can be done by loading sound or music files with {@link ESound} or
 * {@link EMusic}. Furthermore, instances of {@link Music} need to be loaded with {@link #loadMusic(Music)} or otherwise
 * those are not affected by volume changes.
 * </p>
 * For further information to spatial sound see {@link #play(Sound, float, float, float)}.
 * <p>This class is not thread-safe!</p>
 */
public class AudioEngine {

    @Getter
    private static AudioEngine instance = new AudioEngine();
    public static void setInstance(AudioEngine instance) {
        Verify.nonNullArg(instance, "AudioEngine instance cannot be null!");
        AudioEngine.instance = instance;
    }

    /** List of all loaded music instances. */
    private final Array<Music> loadedMusics = new Array<>();
    /** Current position used for spatial sound calculations */
    private final Vector2 position = new Vector2();
    /** 1f / soundRadius */
    private float inv_R;
    @Setter
    private float soundVolume;
    @Getter
    private float musicVolume;
    public AudioEngine(float soundVolume, float musicVolume){
        setSoundRadius(8f);
        this.soundVolume = soundVolume;
        this.musicVolume = musicVolume;
    }
    public AudioEngine(){
        this(1,1);
    }

    /**
     * Necessary to be called when music volume management is wanted. This method is already automatically called when
     * loading music via {@link EMusic}.
     * @param music The music to load.
     */
    public void loadMusic(Music music){
        loadedMusics.add(music);
        music.setVolume(musicVolume);
    }

    /**
     * Sets the general volume for all playing music instances.
     * @param volume The volume to apply.
     */
    public void setMusicVolume(float volume){
        this.musicVolume = volume;
        for(Music music : loadedMusics){
            music.setVolume(volume);
        }
    }

    /**
     * Sets the radius which is used to calculate volume drop-off based of the current {@link #position}.
     * @param soundRadius The radius used to do calculations.
     */
    public void setSoundRadius(float soundRadius) {
        this.inv_R = 1f / soundRadius;
    }

    /**
     * Sets the current position which is used to calculate sound effects in spatial environment.
     * @param x The x-value of the position.
     * @param y The y-value of the position.
     */
    public void setPosition(float x, float y){
        this.position.set(x,y);
    }

    /**
     * Plays a sound with the current set {@link #soundVolume}.
     * @param sound The sound to play.
     * @return The id of the sound instance if successful, or -1 on failure.
     */
    public long play(Sound sound){
        return sound.play(soundVolume);
    }

    /**
     * Plays a sound with a given volume.
     * @param sound The sound to play.
     * @param volume The volume used to play the sound.
     * @return The id of the sound instance if successful, or -1 on failure.
     */
    public long play(Sound sound, float volume){
        return sound.play(volume);
    }

    /**
     * Plays a sound in the spatial environment through a given position.
     * @param sound The sound to play.
     * @param x The x-value of the sound coordinate.
     * @param y The y-value of the sound coordinate.
     * @return The id of the sound instance if successful, or -1 on failure.
     */
    public long play(Sound sound, float x, float y){
        return play(sound, x, y, soundVolume);
    }

    /**
     * Plays a sound in a spatial environment.
     * @param sound The sound to play.
     * @param x The x-value of the sound coordinate.
     * @param y The y-value of the sound coordinate.
     * @param volume The base volume to play the sound with.
     * @return The new sound ID. For more reference see {@link Sound#play(float, float, float)}.
     */
    public long play(Sound sound, float x, float y, float volume){
        return play(sound,x,y,volume,1.0f);
    }

    /**
     * Plays a sound in a spatial environment.
     * @param sound The sound to play.
     * @param x The x-value of the sound coordinate.
     * @param y The y-value of the sound coordinate.
     * @param volume The base volume to play the sound with.
     * @param pitch The pitch which is used for playing.
     * @return The new sound ID. For more reference see {@link Sound#play(float, float, float)}.
     */
    public long play(Sound sound, float x, float y, float volume, float pitch){
        //distance based volume
        float distance = this.position.dst(x,y);
        float totalVol = volume - Math.max(0, 1 - (distance * inv_R));
        //panning
        float dx = x - this.position.x;
        float dy = y - this.position.y;
        float angle = (float) Math.atan2(dy, dx);
        float panning = (float) MathUtils.clamp(Math.sin(angle), -1, 1);
        return sound.play(totalVol, pitch, panning);
    }

}
