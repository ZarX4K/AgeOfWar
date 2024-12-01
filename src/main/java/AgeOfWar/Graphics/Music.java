package AgeOfWar.Graphics;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class Music implements LineListener {//
    private Clip musicClip;
    private boolean isPlaybackCompleted;

    @Override
    public void update(LineEvent event) {
        if (LineEvent.Type.START == event.getType()) {
        } else if (LineEvent.Type.STOP == event.getType()) {
            isPlaybackCompleted = true;
        }
    }

    // Method to load a music file from the resources
    public void loadMusic(String audioFilePath) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(audioFilePath)) {
            if (inputStream == null) {
                throw new IOException("Audio file not found: " + audioFilePath);
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(inputStream);
            AudioFormat audioFormat = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);

            musicClip = (Clip) AudioSystem.getLine(info);
            musicClip.addLineListener(this);
            musicClip.open(audioStream);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    // Method to play music in a loop
    public void playMusic() {
        if (musicClip != null) {
            isPlaybackCompleted = false;
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            musicClip.start();
        }
    }

    // Method to stop the music
    public void stopMusic() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
        }
    }



}