package project.oswel.nlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.nd4j.common.io.ClassPathResource;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

public class NER {

    private static TokenNameFinderModel dateNERModel;

    public NER(String dateNERModelFileName) {
        dateNERModel = loadDateNERModel(dateNERModelFileName);
    }

    private static TokenNameFinderModel loadDateNERModel(
                                                String dateNERModelFileName) {
        try {
            String modelPath = new ClassPathResource(
                        dateNERModelFileName).getFile().getPath();
            InputStream is = new FileInputStream(modelPath);
            dateNERModel = new TokenNameFinderModel(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dateNERModel;
    }

    public static String[] findDate(String sentence) {
        NameFinderME finder = new NameFinderME(dateNERModel);
        String[] words = sentence.split(" ");
        Span[] spans = finder.find(words);
        String[] dates = new String[spans.length];
        
        for (int i=0; i<spans.length; i++) {
            Span span = spans[i];
            int index = span.getStart();
            dates[i] = words[index];
        }
        return dates;
    }
}
