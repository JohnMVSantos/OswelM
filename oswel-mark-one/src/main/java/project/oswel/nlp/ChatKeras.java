package project.oswel.nlp;

import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.json.simple.parser.ParseException;
import edu.stanford.nlp.pipeline.Annotation;
import org.nd4j.common.io.ClassPathResource;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.json.simple.parser.JSONParser;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;
import org.nd4j.linalg.factory.Nd4j;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.util.LinkedList;
import java.util.Properties;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileReader;
import java.util.Random;
import java.util.List;

public class ChatKeras {

    protected StanfordCoreNLP pipeline;
    private static MultiLayerNetwork model;
    private static ArrayList<String> words;
    private static ArrayList<String> classes;
    private static JSONArray intents;

    public ChatKeras(
        String modelFileName, 
        String wordsFileName, 
        String classesFileName, 
        String intentsFileName
    ) {
        // Create StanfordCoreNLP object properties, with POS tagging
        // (required for lemmatization), and lemmatization.
        Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");

        // StanfordCoreNLP loads a lot of models, so you probably
        // only want to do this once per execution.
        this.pipeline = new StanfordCoreNLP(props);

        model = loadModel(modelFileName);
        intents = readIntents(intentsFileName);
        words = readText(wordsFileName);
        classes = readText(classesFileName);
    }

    private static MultiLayerNetwork loadModel(String modelFileName) {
        try {
            String fullModel = new ClassPathResource(
                    modelFileName).getFile().getPath();
            model = KerasModelImport.importKerasSequentialModelAndWeights(
                fullModel, false);
        } catch (IOException | InvalidKerasConfigurationException | 
                                UnsupportedKerasConfigurationException e) {
            e.printStackTrace();
        }
        return model;
    }

    private static JSONArray readIntents(String intentsFileName) { 
        JSONParser parser = new JSONParser();
        JSONArray intents = new JSONArray();
        try {
            String intentsPath = new ClassPathResource(
                    intentsFileName).getFile().getPath();
            JSONObject jsonObject = (JSONObject) parser.parse(
                    new FileReader(intentsPath));
            intents = (JSONArray) jsonObject.get("intents");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } 
        return intents;
    }

    private static ArrayList<String> readText(String fileName) {
        ArrayList<String> contents = new ArrayList<String>();
        try {
            String filePath = new ClassPathResource(
                    fileName).getFile().getPath();
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    contents.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contents;
    }

    private LinkedList<String> lemmatize(String documentText)
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
                // Retrieve and add the lemma for each word into the 
                // list of lemmas.
                lemmas.add(token.get(LemmaAnnotation.class));
            }
        }
        return lemmas;
    }

    private LinkedList<String> cleanUpSentence(String sentence) {
        StringTokenizer tokenizer = new StringTokenizer(sentence);
        String documentText = "";
        while (tokenizer.hasMoreElements()) {
            documentText += tokenizer.nextToken() + " ";
        }
        LinkedList<String> lemmatizedTokens = this.lemmatize(documentText);        
        return lemmatizedTokens;
    }

    private INDArray bagOfWords(String sentence) { 
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

    public String predict_class(String sentence) {
        INDArray bow = this.bagOfWords(sentence);
        INDArray scores = model.output(bow);
        int maxIndex = Nd4j.argMax(scores,1).getInt(0);
        return classes.get(maxIndex);
    }

    public String[] getRandomResponse(String sentence) {
        String[] response = new String[2];
        String cls = this.predict_class(sentence);
        response[0] = cls;
        Random rand = new Random();
        for (int i=0; i<intents.size(); i++) {
            JSONObject intent = (JSONObject) intents.get(i);
            if (cls.equals(intent.get("tag"))) {
                JSONArray responses = (JSONArray) intent.get("responses");
                int index = rand.nextInt(responses.size());
                response[1] = (String) responses.get(index);
                return response;
            }
        }
        response[1] = "Could not generate a response."; 
        return response;
    }
}
