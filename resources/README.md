Update the language model to support the following languages in addition to existing English:
+ German: Deutscher Audiosupport
+ Arabic: دعم الصوت العربي
+ Mandarin Chinese: 中文音频支持
+ Turkish: Türkçe ses desteği

###  Text to speech code:

    :::java
    // 1- initiate the remote speech model
    RemoteSpeechModel model = new RemoteSpeechModel(apiKey, SpeechModels.google);
    
    // 2- call generateEnglishText with any text
    Text2SpeechInput input = new Text2SpeechInput.Builder("Hi, I am Intelligent Java.").build();
    byte[] decodedAudio1 = model.generateEnglishText(input);
    byte[] decodedAudio2 = model.generateMandarinText(input);
    byte[] decodedAudio3 = model.generateGermanText(input);


### ChatGPT notes
If you have access to GPT4, you can use the library to access it as follow:

    :::java
    // 1- initiate the chat model.
    Chatbot bot = new Chatbot(apiKey, SupportedChatModels.openai);
    
    // 2- prepare the chat history by calling addMessage.
    String mode = "You are a helpful astronomy assistant.";
    String ask = "generate LinkedIn post for new AI programming course.";
    ChatModelInput input = new ChatGPTInput.Builder(mode).setModel("gpt-4").addUserMessage(ask).build();
    
    // 3- call chat!
    List<String> resValues =  bot.chat(input);




### Integration
Maven:

    :::xml
    <dependency>
        <groupId>io.github.barqawiz</groupId>
        <artifactId>intellijava.core</artifactId>
        <version>0.8.0</version>
    </dependency>

Gradle:

    implementation 'io.github.barqawiz:intellijava.core:0.8.0'

