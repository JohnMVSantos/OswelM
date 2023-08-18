package project.oswel.nlp;

import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.Annotation;
import org.nd4j.common.io.ClassPathResource;
import org.nd4j.linalg.api.ndarray.INDArray;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;
import org.nd4j.linalg.factory.Nd4j;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;
import java.io.FileReader;
import java.util.Random;
import java.util.List;

/**
 * This class loads the NLP Keras (H5) model trained in Python 
 * to be deployed in Java.
 * @author John Santos
 */
public class ChatKeras {

    protected StanfordCoreNLP pipeline;
    private MultiLayerNetwork model;
    private ArrayList<String> words;
    private ArrayList<String> classes;
    private JSONArray intents;

    /**
     * Creates a new ChatKeras object given the model resources used/generated
     * during the training process.
     * @param modelFileName This is the name of the H5 model located under
     *                      the resources folder.
     * @param wordsFileName This is the name of the file containing the words
     *                      used to train the model. By default this file is
     *                      called words.txt.
     * @param classesFileName This is the name of the file containing all the
     *                        different tags used to categorize the words.
     *                        By default this file is called classes.txt.
     * @param intentsFileName This is the name of the file used to train the
     *                        model. By default this is the intents.json.
     */
    public ChatKeras(
        String modelFileName, 
        String wordsFileName, 
        String classesFileName, 
        String intentsFileName
    ) {
        this.loadNLPPipeline();
        this.loadModel(modelFileName);
        this.readIntents(intentsFileName);
        this.words = readText(wordsFileName);
        this.classes = readText(classesFileName);
    }

    /**
     * Loads the Stanford NLP pipeline that is used to lemmatize sentences.
     */
    private void loadNLPPipeline() {
        // Create StanfordCoreNLP object properties, with POS tagging
        // (required for lemmatization), and lemmatization.
        Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");

        // StanfordCoreNLP loads a lot of models, so you probably
        // only want to do this once per execution.
        this.pipeline = new StanfordCoreNLP(props);
    }

    /**
     * Loads the Keras NLP model given the name of the model.
     * @param modelFileName This is the name of the H5 model primarily 
     *                      stored inside the resources directory.
     */
    private void loadModel(String modelFileName) {
        try {
            String fullModel = new ClassPathResource(modelFileName)
                                    .getFile()
                                    .getPath();
            this.model = KerasModelImport
                            .importKerasSequentialModelAndWeights(
                                    fullModel, 
                                    false
                            );
        } catch (IOException | InvalidKerasConfigurationException | 
                UnsupportedKerasConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * This reads the intents file and stores the contents as a JSONArray.
     * @param intentsFileName This is the name of the intents file. By 
     *                        default it is called intents.json.
     */
    private void readIntents(String intentsFileName) { 
        try {
            String intentsPath = new ClassPathResource(intentsFileName)
                                        .getFile()
                                        .getPath();
            InputStream is = new FileInputStream(intentsPath);
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            JSONTokener tokener = new JSONTokener(in);
            JSONObject jsonObject = new JSONObject(tokener);
            this.intents = jsonObject.getJSONArray("intents");			
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    /**
     * This reads text files line by line given the filename and stores
     * the contents inside an ArrayList.
     * @param fileName This is the name of the text file to be read. 
     *                 This file should be stored inside the resources 
     *                 directory.
     * @return The contents of the text file (ArrayList).
     */
    private static ArrayList<String> readText(String fileName) {
        ArrayList<String> contents = new ArrayList<String>();
        try {
            String filePath = new ClassPathResource(fileName)
                                    .getFile()
                                    .getPath();
            try (BufferedReader br = new BufferedReader(
                                        new FileReader(filePath))) {
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

    /**
     * Lemmatizes the input sentence.
     * @param documentText The input sentence as a string to be lemmatized.
     * @return Contains all the lemmatized tokens making up the sentence
     *         (LinkedList).
     */
    private LinkedList<String> lemmatize(String documentText) {
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

    /**
     * Prior to being lemmatized, the sentence needs to be tokenized into
     * its different parts. However, after this process, this method calls
     * the lemmatizer to return the lemmatized tokens.
     * @param sentence The sentence to be lemmatized and tokenized. 
     * @return The lemmatized tokens (LinkedList).
     */
    private LinkedList<String> cleanUpSentence(String sentence) {
        StringTokenizer tokenizer = new StringTokenizer(sentence);
        String documentText = "";
        while (tokenizer.hasMoreElements()) {
            documentText += tokenizer.nextToken() + " ";
        }
        LinkedList<String> lemmatizedTokens = this.lemmatize(documentText);        
        return lemmatizedTokens;
    }

    /**
     * Creates a bag of words which is a binary array where the length 
     * is the number of words, each element corresponding to the word in the
     * array. If there is a match in the word, this is denoted as a 1. 
     * Otherwise non matches are denoted as zeros.
     * @param sentence This is the sentence to process: tokenized, lemmatized,
     *                 and translated into a bag of words.
     * @return An binary array representing the words that have been matched
     *         (INDArray).
     */
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

    /**
     * This returns the scores per category or tag.
     * @param sentence This is the sentence to be processed and have the model
     *                 make predictions as a classifier.
     * @return All the scores per category. Primarily the highest score is 
     *         denoted as the match (INDArray).
     */
    private INDArray predictScores(String sentence) {
        INDArray bow = this.bagOfWords(sentence);
        INDArray scores = model.output(bow);
        return scores;
    } 

    /**
     * Gets the category based on the maximum index which is reflected upon
     * as the index of the highest score.
     * @param maxIndex The index pointing to the maximum score the model 
     *                 outputs.
     * @return The category upon which the passed sentence belongs (String).
     */
    private String getClass(int maxIndex) {
        return classes.get(maxIndex);
    }

    /**
     * Performs all the processes: tokenizes, lemmatizes, transforms the
     * sentence into a bag of words for the loaded model to make predictions.
     * Gets the scores the model generated per category, and utilizes the 
     * maximum score to be the intention to return any random response from
     * the intended category.
     * @param sentence The sentence to classify belonging to a specific 
     *                 category.
     * @return A container consisting of the category, the score, and
     *         the random generated response from the category (JSONObject).
     */
    public JSONObject getRandomResponse(String sentence) {
        JSONObject response = new JSONObject();

        INDArray scores = this.predictScores(sentence);
        int maxIndex = Nd4j.argMax(scores,1).getInt(0);
        double score = scores.getDouble(maxIndex);
        String category = "confirmation";

        if (score >= 0.50)  {
            category = this.getClass(maxIndex);
        } 

        response.put("category", category);
        response.put("score", score);

        Random rand = new Random();
        for (int i=0; i<intents.length(); i++) {
            JSONObject intent = intents.getJSONObject(i);
            if (category.equalsIgnoreCase((String) intent.get("tag"))) {
                JSONArray responses = intent.getJSONArray("responses");
                int index = rand.nextInt(responses.length());
                response.put("response", responses.getString(index));
                return response;
            }
        }
        response.put("response", "Could not generate a response.");
        return response;
    }
}
