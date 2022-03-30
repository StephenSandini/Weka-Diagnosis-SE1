package Controller.Diagnosis;

import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.util.ArrayList;

public class Predict2 {

    private static String trainLocation;
    private static String testLocation;
    private ArrayList<String> predictions;

    public Predict2(String train, String test) throws Exception {
        trainLocation = train;
        testLocation = test;
        predictions = new ArrayList<String>();
    }

    public void Run() throws Exception {
        // load data
        DataSource s = new DataSource(trainLocation);
        Instances train = s.getDataSet();
        train.setClassIndex(train.numAttributes() - 1);

        J48 model = new J48();
        model.setUnpruned(true);

        DataSource t = new DataSource(testLocation);
        Instances test = t.getDataSet();
        test.setClassIndex(test.numAttributes() - 1);

        FilteredClassifier fc = new FilteredClassifier();
        fc.setClassifier(model);
        fc.buildClassifier(train);
        for (int i = 0; i < test.numInstances(); i++) {
            double pred = fc.classifyInstance(test.instance(i));
            System.out.print("actual: " + test.classAttribute().value((int) test.instance(i).classValue()));
            System.out.println(", predicted: " + test.classAttribute().value((int) pred));
            predictions.add(test.classAttribute().value((int) pred));
        }
        //System.out.println(fc.toString());
    }
    public ArrayList<String> getPredictions() {
        return predictions;
    }
}