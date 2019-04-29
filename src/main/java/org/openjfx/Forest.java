package org.openjfx;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.*;
import weka.core.converters.ArffLoader;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class Forest {
    private static String modelName = "heart_disease.model";
    private RandomForest forest;
    private Instances trainingDataSet;
    private File testFile;

    private Instances getDataSet(URL fileName) throws IOException{
        int classID = 13;
        ArffLoader arffLoader = new ArffLoader();
        arffLoader.setSource(fileName);
        Instances data = arffLoader.getDataSet();
        data.setClassIndex(classID);
        return data;
    }
    private void createUnlabeledFile(){
        String header = "@relation 'heart-weka.filters.unsupervised.attribute.NumericToNominal-R2,3,6,7,9,11,13,14'\n" +
                "\n" +
                "@attribute \uFEFFage numeric\n" +
                "@attribute sex {0,1}\n" +
                "@attribute cp {0,1,2,3}\n" +
                "@attribute trestbps numeric\n" +
                "@attribute chol numeric\n" +
                "@attribute fbs {0,1}\n" +
                "@attribute restecg {0,1,2}\n" +
                "@attribute thalach numeric\n" +
                "@attribute exang {0,1}\n" +
                "@attribute oldpeak numeric\n" +
                "@attribute slope {0,1,2}\n" +
                "@attribute ca numeric\n" +
                "@attribute thal {0,1,2,3}\n" +
                "@attribute target {0,1}\n\n" +
                "@data\n";
        String desktop = FileSystemView.getFileSystemView().getHomeDirectory().getPath();
        System.out.println("desktop: " + desktop);
        testFile = new File(desktop.concat("/").concat("prediction_Data.arff"));
        try {
//            if (Files.exists(testFile.toPath()))
//                return;
            Files.deleteIfExists(testFile.toPath());
            if(testFile.createNewFile()) {
                System.out.println("File Created");
                BufferedWriter writer = Files.newBufferedWriter(testFile.toPath());
                writer.write(header);
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addObservations(List<String> observations) throws IOException {
        BufferedWriter writer;
        writer = new BufferedWriter(new FileWriter(testFile.getPath(), true));
        for (String patient  : observations) {
            String output = patient.concat(",?");
            writer.write(output);
            writer.newLine();
        }
        writer.close();
    }
    private void addObservationArray(List<Object[]> observations) throws IOException{
        BufferedWriter writer;
        writer = new BufferedWriter(new FileWriter(testFile.getPath(), true));
        String str;
        List<String> stringList;
        for (Object[] observation : observations) {
            String[] largerArray = Arrays.copyOf((String[]) observation, observation.length + 1);
            largerArray[largerArray.length-1] = "?";
            String[] temp = Arrays.copyOfRange(largerArray, 3, largerArray.length  );
            stringList = Arrays.asList(temp);
            str = String.join(",", stringList);
            writer.write(str);
            writer.newLine();
        }
        writer.close();
    }
    Instances classify(RandomForest model, List<Object[]> observations) throws IOException {
        forest = model;
        createUnlabeledFile();
        addObservationArray(observations);
        FileReader fileReader = new FileReader(testFile);
        BufferedReader reader = new BufferedReader(fileReader);
        Instances unlabeled = new Instances(reader);
        unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
        Instances labeled = new Instances(unlabeled);
        double[] predictions = null;
        for (int i = 0; i < unlabeled.numInstances(); i++) {
            predictions = new double[unlabeled.numInstances()];
            double clsLabel = 0;
            try {
                clsLabel = forest.classifyInstance(unlabeled.instance(i));
                observations.get(i)[15] = String.valueOf(clsLabel);
            } catch (Exception e) {
                e.printStackTrace();
            }
            labeled.instance(i).setClassValue(clsLabel);
        }
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(FileSystemView.getFileSystemView().getHomeDirectory().getPath().concat("/prediction_result.arff")));
            writer.write(labeled.toString());
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String header = "";
            Path path = Paths.get(FileSystemView.getFileSystemView().getHomeDirectory().getPath().concat("/PredictionResults.txt"));
            //Files.deleteIfExists(path);
            if (!Files.exists(path)) {
                header = "---Predictions---\n\nID - First Name - Last Name : Prediction\n";
            }
            writer = new BufferedWriter(new FileWriter(FileSystemView.getFileSystemView().getHomeDirectory().getPath().concat("/PredictionResults.txt"), true));
            writer.write(header);

            for ( Object[] observation : observations) {
                String[] stringObservation = (String[]) observation;
                String result = stringObservation[0].concat(" - ").concat(stringObservation[1]).concat(" - ").concat(stringObservation[2].concat(" : "));
                if (stringObservation[15].equalsIgnoreCase("0.0"))
                    result = result.concat("Negative").concat("\n");
                else
                    result = result.concat("Positive").concat("\n");
                writer.write(result);
            }
            //writer.newLine();
            writer.flush();
            writer.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        return labeled;
    }

    Classifier getModel() throws Exception {
        String absPath = new File("").getAbsolutePath().concat("/src/main/resources/").concat(modelName);
        return (Classifier) weka.core.SerializationHelper.read(absPath);
    }

    double[] process(URL path)throws Exception{
        RandomForest forest = new RandomForest();
        trainingDataSet = getDataSet(path);
        forest.buildClassifier(trainingDataSet);
        String directory = new File("").getAbsolutePath();
        directory = directory.concat("/src/main/resources/");
        System.out.println(directory);
        weka.core.SerializationHelper.write(directory + modelName, forest);
        Evaluation evaluation = new Evaluation(trainingDataSet);
        evaluation.crossValidateModel(forest, trainingDataSet, 10, new Random(1));
        //evaluation.evaluateModel(forest, trainingDataSet);
        outputTextFile(evaluation);

        System.out.println("** Decision Tress Evaluation with Datasets **");
        System.out.println(evaluation.toSummaryString());
        System.out.print(" the expression for the input data as per algorithm is ");
        System.out.println(forest);
        System.out.println(evaluation.toMatrixString());
        System.out.println(evaluation.toClassDetailsString());
        System.out.println(Arrays.deepToString(evaluation.confusionMatrix()));
        double[] performance = new double[6];
        performance[0] = evaluation.pctCorrect();
        performance[1] = evaluation.pctIncorrect();
        performance[2] = evaluation.numTruePositives(1);
        performance[3] = evaluation.numFalsePositives(1);
        performance[4] = evaluation.numTrueNegatives(1);
        performance[5] = evaluation.numFalseNegatives(1);

        return performance;
    }

    private void outputTextFile(Evaluation evaluation){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FileSystemView.getFileSystemView().getHomeDirectory().getPath().concat("/PredictionPerformance.txt")));
            writer.write(evaluation.toMatrixString("Heart Disease Prediction--"));
            writer.newLine();
            writer.newLine();
            writer.write(evaluation.toClassDetailsString("Class Details--"));
            writer.newLine();
            writer.newLine();
            writer.write(evaluation.toSummaryString("Summary--!", true));
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}