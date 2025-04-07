package com.github.kleesup.kleegdx.client.sound;

import com.badlogic.gdx.audio.Sound;
import com.github.kleesup.heiopeibattlegame.HeiopeiBattleGame;

/**
 * Implementation of {@link Sound} that connects to {@link AudioEngine}.
 * This class uses {@link #play()}, {@link #play(float, float)}, {@link #play(float)} and
 * {@link #play(float volume, float pitch, float pan)} to call the similar methods in {@link AudioEngine} to
 * achieve spatial sounds.
 */
public class ESound implements Sound {

    private final Sound sound;
    ESound(Sound sound){
        this.sound = sound;
    }

    private AudioEngine engine(){
        return HeiopeiBattleGame.getInstance().getAudio();
    }

    public long play(float x, float y){
        return engine().play(sound, x,y);
    }

    @Override
    public long play() {
        return engine().play(sound);
    }

    @Override
    public long play(float volume) {
        return engine().play(sound, volume);
    }

    @Override
    public long play(float volume, float pitch, float pan) {
        return sound.play(volume, pitch, pan); //in this case, engine has no function and is ignored.
    }

    @Override
    public long loop() {
        return sound.loop();
    }

    @Override
    public long loop(float volume) {
        return sound.loop(volume);
    }

    @Override
    public long loop(float volume, float pitch, float pan) {
        return sound.loop(volume, pitch, pan);
    }

    @Override
    public void stop() {
        sound.stop();
    }

    @Override
    public void pause() {
        sound.pause();
    }

    @Override
    public void resume() {
        sound.resume();
    }

    @Override
    public void dispose() {
        sound.dispose();
    }

    @Override
    public void stop(long soundId) {
        sound.stop(soundId);
    }

    @Override
    public void pause(long soundId) {
        sound.pause(soundId);
    }

    @Override
    public void resume(long soundId) {
        sound.resume(soundId);
    }

    @Override
    public void setLooping(long soundId, boolean looping) {
        sound.setLooping(soundId, looping);
    }

    @Override
    public void setPitch(long soundId, float pitch) {
        sound.setPitch(soundId, pitch);
    }

    @Override
    public void setVolume(long soundId, float volume) {
        sound.setVolume(soundId, volume);
    }

    @Override
    public void setPan(long soundId, float pan, float volume) {
        sound.setPan(soundId,pan,volume);
    }
}
