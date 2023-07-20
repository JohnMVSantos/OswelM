package project.oswel.nlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.nd4j.common.io.ClassPathResource;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

public class NER {

    private static TokenNameFinderModel locationNERModel;

    public NER(String locationNERModelFileName) {
        locationNERModel = loadLocationNERModel(locationNERModelFileName); 
    }

    private static TokenNameFinderModel loadLocationNERModel(
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
}
