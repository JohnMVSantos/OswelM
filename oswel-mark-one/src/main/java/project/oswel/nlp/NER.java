package project.oswel.nlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.nd4j.common.io.ClassPathResource;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.Span;


public class NER {

    private TokenNameFinderModel locationNERModel = null;
    private POSModel taggerNERModel = null;

    public NER(String locationNERModelFileName, String taggerNERModelFileName) {
        locationNERModel = loadLocationNERModel(locationNERModelFileName); 
        taggerNERModel = loadTaggerNERModel(taggerNERModelFileName);
    }

    private TokenNameFinderModel loadLocationNERModel(
                                            String locationNERModelFileName) {
        try {
            String modelPath = new ClassPathResource(
                        locationNERModelFileName).getFile().getPath();
            InputStream is = new FileInputStream(modelPath);
            locationNERModel = new TokenNameFinderModel(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return locationNERModel;
    }

    private POSModel loadTaggerNERModel(String taggerNERModelFileName) {
        try {
            String modelPath = new ClassPathResource(
                        taggerNERModelFileName).getFile().getPath();
            InputStream is = new FileInputStream(modelPath);
            try {
                taggerNERModel = new POSModel(is);
            }
            catch (IOException e) {
                e.printStackTrace();
              }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return taggerNERModel;
    }

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

    public String[] tagSentence(String sentence) {
        POSTaggerME tagger = new POSTaggerME(taggerNERModel);
        String sent[] = sentence.split(" ");
        String tags[] = tagger.tag(sent);
        return tags;
    }
}
