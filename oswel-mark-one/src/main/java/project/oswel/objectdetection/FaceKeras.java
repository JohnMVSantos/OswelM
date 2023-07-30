package project.oswel.objectdetection;

import java.io.IOException;

import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.common.io.ClassPathResource;

public class FaceKeras {
    
    private ComputationGraph model;

    public FaceKeras (String modelFileName) {
        this.loadModel(modelFileName);
    }

    private void loadModel(String modelFileName) {
        try {
            String fullModel = new ClassPathResource(modelFileName)
                                    .getFile()
                                    .getPath();
            this.model = KerasModelImport
                            .importKerasModelAndWeights(
                                    fullModel, 
                                    false
                            );
        } catch (IOException | InvalidKerasConfigurationException | 
                UnsupportedKerasConfigurationException e) {
            e.printStackTrace();
        }
    }
}
