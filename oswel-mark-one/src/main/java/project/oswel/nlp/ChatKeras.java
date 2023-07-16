package project.oswel.nlp;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Properties;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import org.json.simple.parser.ParseException;

import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.nd4j.common.io.ClassPathResource;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
// import org.nd4j.linalg.io.ClassPathResource;

public class ChatKeras {


    protected StanfordCoreNLP pipeline;
    private static MultiLayerNetwork model;

    private static ArrayList<String> words = new ArrayList<String>(Arrays.asList("'s", "could", "do", "have", "how", "i", "is", "show", "tell", "the", "what", "at", "cloudy", "cold", "condition", "current", "date", "degree", "forecast", "got", "have", "hot", "in", "is", "it", "know", "like", "many", "me", "or", "outside", "please", "raining", "snowing", "sunny", "tell", "temperature", "the", "time", "to", "today", "want", "warm", "weather", "what", "you"));

    private static String[] classes = new String[]{ "date", "time", "weather" };

    public ChatKeras() {
        // Create StanfordCoreNLP object properties, with POS tagging
        // (required for lemmatization), and lemmatization
        Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");

        // StanfordCoreNLP loads a lot of models, so you probably
        // only want to do this once per execution
        this.pipeline = new StanfordCoreNLP(props);

        try {
            
           
            
            // String simpleMlp = new ClassPathResource(
            //     "oswel_nlp.h5").getFile().getPath();

            String fullModel = new ClassPathResource("oswel.h5").getFile().getPath();
         
            model = KerasModelImport.
                        importKerasSequentialModelAndWeights(fullModel, false);
                       
        } catch (IOException | InvalidKerasConfigurationException | UnsupportedKerasConfigurationException e) {
            e.printStackTrace();
        }

    
    }

    public JSONArray readIntents() { 
        JSONParser parser = new JSONParser();
        JSONArray intents = new JSONArray();
        try {
            String intentsPath = new ClassPathResource("intents.json").getFile().getPath();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(intentsPath));
            intents = (JSONArray) jsonObject.get("intents");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } 
        return intents;
    }

    public void readText() { }

    public LinkedList<String> lemmatize(String documentText)
    {
        LinkedList<String> lemmas = new LinkedList<String>();

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(documentText);

        // run all Annotators on this text
        this.pipeline.annotate(document);

        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            // Iterate over all tokens in a sentence
            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                // Retrieve and add the lemma for each word into the list of lemmas
                lemmas.add(token.get(LemmaAnnotation.class));
            }
        }

        return lemmas;
    }


    public LinkedList<String> cleanUpSentence(String sentence) {
        StringTokenizer tokenizer = new StringTokenizer(sentence);
        String documentText = "";
        while (tokenizer.hasMoreElements()) {
            documentText += tokenizer.nextToken() + " ";
        }
        LinkedList<String> lemmatizedTokens = this.lemmatize(documentText);        
        return lemmatizedTokens;
    }

    public INDArray bagOfWords(String sentence) { 
        
        LinkedList<String> lemmatizedTokens = this.cleanUpSentence(sentence);
        INDArray bag = Nd4j.zeros(1, words.size());
        for (int i=0; i<lemmatizedTokens.size(); i++) {
            String token = lemmatizedTokens.get(i).toLowerCase();
            if (words.contains(token)) {                
                bag.putScalar(words.indexOf(token), 1.0);
            }
        }
        return bag;
    }

    public void predict_class(String sentence) {

        INDArray bow = this.bagOfWords(sentence);
        System.out.println("" + model.output(bow));

    }

    


    
}
