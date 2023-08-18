package project.oswel.speech.constant;

/**
 * @author zh-hq
 * @date 2023/3/30
 */
public enum VoiceEnum {

    /*
     edge voice list
      https://speech.platform.bing.com/consumer/speech/synthesize/readaloud/voices/list?trustedclienttoken=6A5AA1D4EAFF4E9FB37E23D68491D6F4
     */
    /**
     * 晓晓 活泼、温暖的声音，具有多种场景风格和情感
     */
    zh_CN_XiaoxiaoNeural("zh-CN-XiaoxiaoNeural", "女", "zh-CN"),
    /**
     * 英语
     */
    en_US_AnaNeural("en-US-AnaNeural", "女", "en-US"),
    en_US_GuyNeural("en-US-GuyNeural", "男", "en-US"),
    en_GB_RyanNeural("en-GB-RyanNeural", "Male", "en-GB"),
    en_PH_JamesNeural("en-PH-JamesNeural", "Male", "en-PH"),

    /*
    AZURE 语音库补充
    https://learn.microsoft.com/zh-cn/azure/cognitive-services/speech-service/language-support?tabs=tts
    中文（普通话，简体）
    */
    /**
     * 晓辰 休闲、放松的语音，用于自发性对话和会议听录
     */
    zh_CN_XiaochenNeural("zh-CN-XiaochenNeural", "女", "zh-CN"),
    ;

    private final String shortName;
    private final String gender;
    private final String locale;

    VoiceEnum(String shortName, String gender, String locale) {
        this.shortName = shortName;
        this.gender = gender;
        this.locale = locale;
    }

    public String getShortName() {
        return shortName;
    }

    public String getGender() {
        return gender;
    }

    public String getLocale() {
        return locale;
    }
}
