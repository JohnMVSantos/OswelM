package project.oswel.nlp;

import opennlp.tools.namefind.TokenNameFinderModel;
import org.nd4j.common.io.ClassPathResource;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.postag.POSModel;
import java.io.FileInputStream;
import opennlp.tools.util.Span;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class provides methods that can parse a given sentence to find
 * the location or tags each words as nouns, pronouns, adjectives, etc.
 * @author John Santos
 */
public class NER {

    private TokenNameFinderModel locationNERModel;
    private POSModel taggerNERModel;

    /**
     * Creates an NER object given the names of the location and tagger models.
     * @param locationNERModelFileName The name of the location NER model
     *                                 stored inside the resources folder.
     * @param taggerNERModelFileName The name of the tagger NER model stored
     *                               inside the resources folder.
     */
    public NER(String locationNERModelFileName, String taggerNERModelFileName) {
        this.loadLocationNERModel(locationNERModelFileName); 
        this.loadTaggerNERModel(taggerNERModelFileName);
    }

    /**
     * Loads the location NER model provided with the name of the model.
     * @param locationNERModelFileName The name of the NER location model
     *                                 stored inside the resources folder.
     */
    private void loadLocationNERModel(String locationNERModelFileName) {
        try {
            String modelPath = new ClassPathResource(locationNERModelFileName)
                                        .getFile()
                                        .getPath();
            InputStream is = new FileInputStream(modelPath);
            this.locationNERModel = new TokenNameFinderModel(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the tagger NER model provided with the name of the model.
     * @param taggerNERModelFileName The name of the NER tagger model 
     *                               stored inside the resources folder.
     */
    private void loadTaggerNERModel(String taggerNERModelFileName) {
        try {
            String modelPath = new ClassPathResource(taggerNERModelFileName)
                                        .getFile()
                                        .getPath();
            InputStream is = new FileInputStream(modelPath);
            try {
                this.taggerNERModel = new POSModel(is);
            }
            catch (IOException e) {
                e.printStackTrace();
              }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds the locations in a given sentence. 
     * @param sentence The string sentence to parse the locations if any.
     * @return The locations parsed (String[]).
     */
    public String[] findLocation(String sentence) {
        NameFinderME finder = new NameFinderME(locationNERModel);
        String[] words = sentence.split(" ");
        Span[] spans = finder.find(words);
        String[] locations = new String[spans.length];
        
        for (int i=0; i<spans.length; i++) {
            Span span = spans[i];
            int start = span.getStart();
            int end = span.getEnd() - 1;
            if (start == end) {
                locations[i] = words[start];
            } else {
                locations[i] = words[start] + " " + words[end];
            }
        }
        return locations;
    }

    /**
     * Returns the tags describing each word in a sentence.
     * @param sentence The string sentence to tag each word.
     * @return The tags denoting each word (String[]).
     */
    public String[] tagSentence(String sentence) {
        POSTaggerME tagger = new POSTaggerME(taggerNERModel);
        String sent[] = sentence.split(" ");
        String tags[] = tagger.tag(sent);
        return tags;
    }
}
