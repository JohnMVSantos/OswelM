package project.oswel.speech.player;

import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import javax.sound.sampled.UnsupportedAudioFileException;
import project.oswel.speech.exceptions.TtsException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import java.io.IOException;
import java.io.File;

/**
 * @author zh-hq
 * @date 2023/3/30
 * 
 * Modified by John Santos
 * @date 2023/8/17
 */
public class Mp3Player extends PcmPlayer {

    public static int recordedTimeInSec;
    @Override
    public void play(String path) throws IOException, UnsupportedAudioFileException {
        File file = new File(path);
        if (!file.exists() || !path.toLowerCase().endsWith(".mp3")) {
            throw TtsException.of(
                "The file oswelAudio.mp3 does not exist.");
        }
        
        AudioInputStream stream = null;
        //使用 mp3spi 解码 mp3 音频文件
        MpegAudioFileReader mp = new MpegAudioFileReader();
        stream = mp.getAudioInputStream(file);
        AudioFormat baseFormat = stream.getFormat();

        // For diagnostic purposes, this is the length of time of the audio file.
        recordedTimeInSec = (int) Math.abs(file.length() / 
                (baseFormat.getFrameSize() * baseFormat.getFrameRate()))*10;
        
        //设定输出格式为pcm格式的音频文件
        AudioFormat format = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED, 
            baseFormat.getSampleRate(), 
            16, 
            baseFormat.getChannels(), 
            baseFormat.getChannels() * 2, 
            baseFormat.getSampleRate(), 
            false
        );

        // 输出到音频
        stream = AudioSystem.getAudioInputStream(format, stream);
        playPcm(stream);

        try { 
            Thread.sleep(4000);
        } catch(InterruptedException e) {
            file.delete();
            throw TtsException.of(
                "Speech was interrupted.");
        }
        //This is added to delete the saved files. 
        file.delete();
    }
}
