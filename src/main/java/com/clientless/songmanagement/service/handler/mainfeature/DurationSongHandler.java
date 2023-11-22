package com.clientless.songmanagement.service.handler.mainfeature;


import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class DurationSongHandler {

    public String getAudioDuration(File audioFile) throws IOException, CannotReadException, TagException, InvalidAudioFrameException, ReadOnlyFileException {
        AudioFile file = AudioFileIO.read(audioFile);
        int durationInSeconds = file.getAudioHeader().getTrackLength();
        int hours = durationInSeconds / 3600;
        int minutes = (durationInSeconds % 3600) / 60;
        int seconds = durationInSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    public String uploadAudio(MultipartFile audioFile) {
        if (!audioFile.isEmpty()) {
            try {
                File tempFile = File.createTempFile("temp", ".mp3");
                audioFile.transferTo(tempFile);

                //tempFile.delete(); // Hapus file sementara setelah selesai

                return getAudioDuration(tempFile);
            } catch (IOException | CannotReadException | InvalidAudioFrameException e) {
                e.printStackTrace();
                return "Failed to process audio file.";
            } catch (TagException | ReadOnlyFileException e) {
                throw new RuntimeException(e);
            }
        } else {
            return "No audio file found.";
        }
    }
}
