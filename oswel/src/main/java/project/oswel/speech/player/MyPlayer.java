package project.oswel.speech.player;

import javax.sound.sampled.UnsupportedAudioFileException;
import project.oswel.speech.exceptions.TtsException;
import java.io.IOException;

/**
 * @author zh-hq
 * @date 2023/3/30
 */
public interface MyPlayer {
    static MyPlayer getInstance(String path) {
        String suffix = path.substring(path.lastIndexOf("."));
        switch (suffix) {
            case ".mp3":
                return new Mp3Player();
            case ".pcm":
                return new PcmPlayer();
            default:
                throw TtsException.of("不支持的音频文件：" + suffix);
        }
    }

    /**
     * 播放音频
     *
     * @param path
     * @throws IOException
     * @throws UnsupportedAudioFileException
     */
    void play(String path) throws IOException, UnsupportedAudioFileException;
}
