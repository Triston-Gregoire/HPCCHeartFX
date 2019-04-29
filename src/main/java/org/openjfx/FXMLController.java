package org.openjfx;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import com.jfoenix.controls.*;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import org.hpccsystems.ws.client.HPCCFileSprayClient;
import org.hpccsystems.ws.client.HPCCWsClient;
import org.hpccsystems.ws.client.platform.Platform;
import org.hpccsystems.ws.client.platform.WorkunitInfo;
import org.hpccsystems.ws.client.utils.DelimitedDataOptions;

import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

import javax.swing.filechooser.FileSystemView;

/*
The controller class for the UI and backend to interact with each other
 */
public class FXMLController implements Initializable {
    private String fileName = "predictme.csv";
    private String filePath;
    private double[] precision;
    private List<String> observations = new ArrayList<>();


    //series for chart data
    //ScatterPlot series - age vs st depression
    private XYChart.Series series0;
    private XYChart.Series series1;

    //ScatterPlot series - age vs heart rate
    private XYChart.Series ageHeartSeries0;
    private XYChart.Series ageHeartSeries1;
    private XYChart.Series ageRateAllSeries;

    //BarChart series - disease distribution
    private XYChart.Series barCleanSeries;
    private XYChart.Series barDiseasedSeries;

    //PieChart series - gender pie
    private PieChart.Data maleSlice = new PieChart.Data("Male", 0);
    private PieChart.Data femaleSlice = new PieChart.Data("Female", 0);

    //PieChart series - accuracy pie
    private PieChart.Data correctSlice = new PieChart.Data("Correct", 0);
    private PieChart.Data incorrectSlice = new PieChart.Data("Incorrect", 0);

    //PieChart series - Recall pie
    private PieChart.Data truePositiveSlice = new PieChart.Data("True Positives", 0);
    private PieChart.Data falsePositiveSlice = new PieChart.Data("False Positives", 0);
    private PieChart.Data trueNegativeSlice = new PieChart.Data("True Negatives", 0);
    private PieChart.Data falseNegativeSlice = new PieChart.Data("False Negatives", 0);

    //Series for age vs disease frequency chart
    //not currenty used
    private XYChart.Series ageFrequencySeries0;
    private XYChart.Series ageFrequencySeries1;

    //PieChart definitions
    private PieChart pieChart;
    private PieChart accuracyPie;
    private PieChart precisionPie;

    //indicates if program is on first pass through
    boolean flag = true;

    @FXML
    private JFXTextField idField;

    @FXML
    private JFXTextField cholesterolField;

    @FXML
    private JFXTextField bloodSugarField;

    @FXML
    private JFXComboBox<String> ecgCombo;

    @FXML
    private JFXTextField heartRateField;

    @FXML
    private JFXComboBox<String> exerciseCombo;

    @FXML
    private JFXTextField oldPeakField;

    @FXML
    private JFXComboBox<String> slopeBox;

    @FXML
    private ChoiceBox<String> diseaseChoice;

    @FXML
    private JFXButton addButton;

    @FXML
    private JFXTextField fNameField;

    @FXML
    private JFXTextField lNameField;

    @FXML
    private JFXTextField ageField;

    @FXML
    private JFXComboBox<String> sexCombo;

    @FXML
    private JFXComboBox<String> chestPainBox;

    @FXML
    private JFXTextField bloodPressureField;

    @FXML
    private JFXComboBox<String> veinsBox;

    @FXML
    private JFXComboBox<String> thalBox;

    @FXML
    ComboBox<Object> patientBox;

    @FXML
    JFXTreeTableView<Patient> patientTable;

    @FXML
    AnchorPane tab2Pane;

    @FXML
    GridPane chartGrid;

    @FXML
    JFXButton submitPatientsButton;

    @FXML
    JFXButton updatePatientButton;

    @FXML
    JFXButton removePatientButton;

    @FXML
    JFXButton queryButton;


    private void setSexes(){
        ObservableList<String> options = FXCollections.observableArrayList("Female", "Male");
        sexCombo.setValue("Female");
        sexCombo.setItems(options);
    }

    private void setChestPainChoice(){
        ObservableList<String> options = FXCollections.observableArrayList("Typical Angina", "Atypical Angina", "Non-Anginal", "Asymptomatic");
        chestPainBox.setItems(options);
    }

    private void setEcgChoice(){
        ObservableList<String> options = FXCollections.observableArrayList("Normal", "Abnormality", "Probable/Definite Ventricular Hypertrophy");
        ecgCombo.setItems(options);
    }

    private void setExerciseChoice(){
        ObservableList<String> options = FXCollections.observableArrayList("Yes", "No");
        exerciseCombo.setItems(options);
    }

    private void setSlopeChoice() {
        ObservableList<String> options = FXCollections.observableArrayList("Upsloping", "Flat", "Downsloping");
        slopeBox.setItems(options);
    }

    private void setFlourosopyChoice(){
        ObservableList<String> options = FXCollections.observableArrayList("Zero","One", "Two", "Three", "Four");
        veinsBox.setItems(options);
    }

    private void setThalChoice(){
        ObservableList<String> options = FXCollections.observableArrayList("NA", "Normal", "Previously Fixed Defect", "Reversible Defect");
        thalBox.setItems(options);
    }

    private void setDiseaseChoice(){
        ObservableList<String> options = FXCollections.observableArrayList("Narrowing < 50%", "Narrowing > %0%");
        diseaseChoice.setItems(options);
    }

    /*
    Event handler for the user pressing the add button which adds a new patient to the TreeTableView

    @param ActionEvent action - button click event
     */
    public void handleAddPress(ActionEvent action){
        //taking attributes from other fields in the UI
        String id = idField.getText();
        String fname = fNameField.getText();
        String lname = lNameField.getText();
        String age =  String.valueOf(ageField.getText());
        String sex = sexCombo.getValue();
        String chestPain = chestPainBox.getValue();
        String restingBP = String.valueOf(bloodPressureField.getText());
        String cholesterol = String.valueOf(cholesterolField.getText());
        String bloodSugar = String.valueOf(bloodSugarField.getText());
        String ecg = ecgCombo.getValue();
        String maxBPM = String.valueOf(heartRateField.getText());
        String exerciseInduced = exerciseCombo.getValue();
        String peak = oldPeakField.getText();
        String slope = slopeBox.getValue();
        String flourosopy = veinsBox.getValue();
        String thal = thalBox.getValue();
        Patient newPatient = new Patient(id, fname, lname, age, sex, chestPain, restingBP, cholesterol, bloodSugar, ecg, maxBPM, exerciseInduced, peak, slope, flourosopy, thal);

        //Insert attributes from UI to list
        //ToDo: replace with Arrays.ToList(...)
        List<String> patientAttributesToTargetHeartDisease = new ArrayList<String>();
        patientAttributesToTargetHeartDisease.add(id);
        patientAttributesToTargetHeartDisease.add(fname);
        patientAttributesToTargetHeartDisease.add(lname);
        patientAttributesToTargetHeartDisease.add(age);
        patientAttributesToTargetHeartDisease.add(sex);
        patientAttributesToTargetHeartDisease.add(chestPain);
        patientAttributesToTargetHeartDisease.add(restingBP);
        patientAttributesToTargetHeartDisease.add(cholesterol);
        patientAttributesToTargetHeartDisease.add(bloodSugar);
        patientAttributesToTargetHeartDisease.add(ecg);
        patientAttributesToTargetHeartDisease.add(maxBPM);
        patientAttributesToTargetHeartDisease.add(exerciseInduced);
        patientAttributesToTargetHeartDisease.add(peak);
        patientAttributesToTargetHeartDisease.add(slope);
        patientAttributesToTargetHeartDisease.add(flourosopy);
        patientAttributesToTargetHeartDisease.add(thal);

        //add Patient to UI's table view
        ObservableList<TreeItem<Patient>> currentList = patientTable.getRoot().getChildren();
        currentList.add(new TreeItem<>(newPatient));
        String str = String.join(", ", patientAttributesToTargetHeartDisease);
        observations.add(str);
        ObservableList patientList = patientBox.getItems();
        patientList.add(str);
    }

    /*
    Event handler for the remove button
    @param ActionEvent action - button click event
     */
    public void handleRemovePressed(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Deleting a patient can't be undone!");

        //Confirm the use wants to remove the patient
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
            int indexOfChosen = patientBox.getSelectionModel().getSelectedIndex();
            if (indexOfChosen >= 0)
                patientBox.getItems().remove(indexOfChosen);
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    /*
    event handler fot the updateButton

    @param ActionEvent action - button click event
     */
    public void handleUpdatePressed(ActionEvent event){
        String item = (String) patientBox.getSelectionModel().getSelectedItem();
        String[] attributes = item.split(", ");
        idField.setText(attributes[0]);
        fNameField.setText(attributes[1]);
        lNameField.setText(attributes[2]);
        ageField.setText(attributes[3]);
        sexCombo.setValue(attributes[4]);
        chestPainBox.setValue(attributes[5]);
        bloodPressureField.setText(attributes[6]);
        cholesterolField.setText(attributes[7]);
        ecgCombo.setValue(attributes[8]);
        heartRateField.setText(attributes[9]);
        exerciseCombo.setValue(attributes[10]);
        oldPeakField.setText(attributes[11]);
        slopeBox.setValue(attributes[12]);
        veinsBox.setValue(attributes[13]);
        thalBox.setValue(attributes[14]);


    }

    //for testing
    public void handleQuery(ActionEvent event){
        Runnable task = this::uploadAndDownload;
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    /*
    launches query to HPCC cluster as well as the update of GUI charts
     */
    public void launchQuery(){
        Runnable task = this::uploadAndDownload;
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    /*
    for testing
     */
    public void checkComboSelection(ActionEvent event){
        boolean isNothingSelected = patientBox.getSelectionModel().isEmpty();
        if(isNothingSelected){  return; }
        removePatientButton.setDisable(false);
        updatePatientButton.setDisable(false);
        //submitPatientsButton.setDisable(false);
    }

    /*
    subits Table of patients to the HPCC cluster for analysis

    @param ActionEvent action - button click event
     */
    public void checkPatientListOnSubmit(ActionEvent event){
        Instances instances = null;
        //if there are no patients, display error messages
        if ( patientTable.getRoot().getChildren().isEmpty() && patientBox.getItems().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Illegal Submission");
            alert.setContentText("List must have at least one patient for submission!");
            alert.showAndWait();
            return;
        }
        List<Object> patients = patientBox.getItems();
        DirectoryChooser chooser = new DirectoryChooser();
        File file = chooser.showDialog(null);
        filePath = file.getPath();

        Forest forest = new Forest();
        try {
            System.out.println(getClass().getResource("/" + "heart.arff"));
            RandomForest model = (RandomForest) forest.getModel();
            instances = forest.classify(model, Coder.encode(observations));
            precision = forest.process(getClass().getResource("/" + "heart.arff"));

            File predictionPerformanceFile = new File(FileSystemView.getFileSystemView().getHomeDirectory().getPath().concat("/PredictionPerformance.txt"));
            File predictionResultsFile = new File(FileSystemView.getFileSystemView().getHomeDirectory().getPath().concat("/PredictionResults.txt"));
            new Thread(() -> {
                try {
                    Thread.sleep(10000);
                    Desktop.getDesktop().edit(predictionPerformanceFile);
                    Desktop.getDesktop().edit(predictionResultsFile);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        HeartWriter writer = new HeartWriter(filePath, fileName);
        try {
            writer.writeEncoded(instances);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path  = filePath.concat("\\" + fileName);
        boolean didWork = HPCC.spray(path);
        launchQuery();
    }

    /*
    creates and adds charts to the GUI
     */
    private void createCharts() {
        NumberAxis xaxis = new NumberAxis(30, 80, 10);
        NumberAxis yaxis = new NumberAxis(0, 7, 1);
        ScatterChart<Number, Number> sc = new
                ScatterChart<Number, Number>(xaxis, yaxis);
        xaxis.setLabel("Age");
        yaxis.setLabel("ST Depression");
        sc.setTitle("Age vs ST Depression");
        series0 = new XYChart.Series();
        series1 = new XYChart.Series();
        series0.setName("No disease");
        series1.setName("Heart Disease");
        sc.getData().addAll(series0, series1);

        NumberAxis lineXAxis = new NumberAxis(20, 80, 10);
        NumberAxis lineYAxis = new NumberAxis(80, 220, 25);
        ScatterChart<Number, Number> lineChart = new ScatterChart<>(lineXAxis, lineYAxis);
        lineXAxis.setLabel("Age (Years)");
        lineYAxis.setLabel("Max Heart Rate (BPM)");
        lineChart.setTitle("Age vs Heart Rate");
        ageHeartSeries0 = new XYChart.Series();
        ageHeartSeries1 = new XYChart.Series();
        ageRateAllSeries = new XYChart.Series();
        //lineChart.getData().addAll(ageRateAllSeries);
        lineChart.getData().addAll(ageHeartSeries0, ageHeartSeries1);

        CategoryAxis barSexAxis = new CategoryAxis();
        NumberAxis barCountAxis = new NumberAxis(0, 140, 20);
        BarChart<String, Number> barChart = new BarChart<String, Number>(barSexAxis, barCountAxis);
        barSexAxis.setLabel("Gender");
        barCountAxis.setLabel("Count");
        barChart.setTitle("Disease Distribution Per Gender");
        barCleanSeries   = new XYChart.Series();
        barDiseasedSeries = new XYChart.Series();
        barCleanSeries.setName("Negative");
        barDiseasedSeries.setName("Positive");
        barChart.getData().addAll(barCleanSeries, barDiseasedSeries);

        pieChart = new PieChart();
        pieChart.getData().add(maleSlice);
        pieChart.getData().add(femaleSlice);
        pieChart.setTitle("Gender Distribution");
        pieChart.getData().forEach(data -> {
            data.nameProperty().bind(
                    Bindings.concat(
                            data.getName(), " ", data.pieValueProperty()
                    )
            );
        });

        accuracyPie = new PieChart();
        accuracyPie.getData().add(correctSlice);
        accuracyPie.getData().add(incorrectSlice);
        accuracyPie.setTitle("Prediction Precision");
        accuracyPie.getData().forEach(data -> {
            data.nameProperty().bind(
                    Bindings.concat(
                            data.getName(), " ", data.pieValueProperty(), "%"
                    )
            );
        });

        precisionPie = new PieChart();
        precisionPie.getData().add(truePositiveSlice);
        precisionPie.getData().add(falsePositiveSlice);
        precisionPie.getData().add(trueNegativeSlice);
        precisionPie.getData().add(falseNegativeSlice);
        precisionPie.setTitle("Model Recall");
        precisionPie.getData().forEach(data -> {
            data.nameProperty().bind(
                    Bindings.concat(
                            data.getName(), " ", data.pieValueProperty()
                    )
            );
        });


        CategoryAxis barAgeAxis = new CategoryAxis();
        NumberAxis barFrequencyAxis = new NumberAxis(0, 15, 3);
        BarChart<String, Number> ageFrequencyChart = new BarChart<>(barAgeAxis, barFrequencyAxis);
        barAgeAxis.setLabel("Age");
        barFrequencyAxis.setLabel("Frequency of Disease");
        ageFrequencyChart.setTitle("Heart Disease Frequency by Age");
        ageFrequencySeries0 = new XYChart.Series();
        ageFrequencySeries1 = new XYChart.Series();
        ageFrequencySeries0.setName("Positive");
        ageFrequencySeries1.setName("Negative");
        ageFrequencyChart.getData().addAll(ageFrequencySeries0, ageFrequencySeries1);


        chartGrid.add(sc, 0, 0);
        chartGrid.add(lineChart, 0, 1);
        chartGrid.add(pieChart, 1, 0);
        chartGrid.add(barChart, 1, 1);
        chartGrid.add(accuracyPie, 0, 2);
        chartGrid.add(precisionPie, 1, 2);
    }

    /*
    Builds and submits queries to the HPCC cluster.
    inserts data into the GUI's charts
     */
    private void uploadAndDownload() {
        try {

            HPCCWsClient connector = HPCC.getClient();
            DelimitedDataOptions options = HPCC.getOptions();
            String code = "layout := record\n" +
                    "\tinteger1 age;\n" +
                    "\tinteger1 sex;\n" +
                    "\tinteger1 chest_pain;\n" +
                    "\tunsigned1 blood_pressure;\n" +
                    "\tunsigned2 cholestoral;\n" +
                    "\tinteger1 glucose_greater_than_120;\n" +
                    "\tinteger1 ekg_result;\n" +
                    "\tunsigned2 max_heart_rate;\n" +
                    "\tinteger1 excercised_induced;\n" +
                    "\treal8 oldpeak;\n" +
                    "\tinteger1 slope;\n" +
                    "\tinteger1 flourosopy;\n" +
                    "\tinteger1 thal;\n" +
                    "\tinteger1 target;\n" +
                    "end;\n" +
                    "\n" +
                    "input := dataset('~online::tcg::heart.csv', layout, csv(separator(',')));\n" +
                    "heart:= sample(input[2..], 4, 1);\n" +
                    "ok := output(heart,{age,oldpeak,target}, named('ok'));\n" +
                    "rateByDepression := output(heart,{max_heart_rate, oldpeak});\n" +
                    "ok;\n";



            if(flag) {//if application is initializing
                connector.httpUploadFileToFirstHPCCLandingZone(getClass().getResource("/heart.csv").getPath());
                connector.sprayVariableHPCCFile("heart.csv", "~online::tcg::heart.csv", "hthor__myeclagent", options, true, HPCCFileSprayClient.SprayVariableFormat.DFUff_csv);
            }
            WorkunitInfo workunitInfo = new WorkunitInfo();
            workunitInfo.setECL(code);
            workunitInfo.setCluster("hthor");
            String[] clusterGroups = connector.getAvailableClusterGroups();
            for (String clusterGroup : clusterGroups) {
                System.out.println(clusterGroup);
            }

            List<List<Object>> results = connector.submitECLandGetResultsList(workunitInfo);
            List<List<Object>> seriesZero = new ArrayList<>();
            List<List<Object>> seriesOne = new ArrayList<>();
            int groupIndicator;
            for (List<Object> result : results) {
                groupIndicator = Integer.parseInt((String) result.get(2));
                if (groupIndicator == 0) {
                    seriesZero.add(result);
                } else if (groupIndicator == 1) {
                    seriesOne.add(result);
                }
            }


            String ageHeartQuery = "layout := record\n" +
                    "\tinteger1 age;\n" +
                    "\tinteger1 sex;\n" +
                    "\tinteger1 chest_pain;\n" +
                    "\tunsigned1 blood_pressure;\n" +
                    "\tunsigned2 cholestoral;\n" +
                    "\tinteger1 glucose_greater_than_120;\n" +
                    "\tinteger1 ekg_result;\n" +
                    "\tunsigned2 max_heart_rate;\n" +
                    "\tinteger1 excercised_induced;\n" +
                    "\treal8 oldpeak;\n" +
                    "\tinteger1 slope;\n" +
                    "\tinteger1 flourosopy;\n" +
                    "\tinteger1 thal;\n" +
                    "\tinteger1 target;\n" +
                    "end;\n" +
                    "\n" +
                    "input := dataset('~online::tcg::heart.csv', layout, csv(separator(',')));\n" +
                    "heart:= sample(input[2..], 4, 1);\n" +
                    "ok := output(heart,{age,max_heart_rate,target}, named('ok'));\n" +
                    "ok;\n";

            WorkunitInfo ageRateWU = new WorkunitInfo();
            ageRateWU.setECL(ageHeartQuery);
            ageRateWU.setCluster("hthor");
            List<List<Object>> ageHeartResults = connector.submitECLandGetResultsList(ageRateWU);
            List<List<Object>> ageHeartSeriesZero = new ArrayList<>();
            List<List<Object>> ageHeartSeriesOne = new ArrayList<>();

            String genderDistribution = "layout := record\n" +
                    "\tinteger1 age;\n" +
                    "\tinteger1 sex;\n" +
                    "\tinteger1 chest_pain;\n" +
                    "\tunsigned1 blood_pressure;\n" +
                    "\tunsigned2 cholestoral;\n" +
                    "\tinteger1 glucose_greater_than_120;\n" +
                    "\tinteger1 ekg_result;\n" +
                    "\tunsigned2 max_heart_rate;\n" +
                    "\tinteger1 excercised_induced;\n" +
                    "\treal8 oldpeak;\n" +
                    "\tinteger1 slope;\n" +
                    "\tinteger1 flourosopy;\n" +
                    "\tinteger1 thal;\n" +
                    "\tinteger1 target;\n" +
                    "end;\n" +
                    "\n" +
                    "input := dataset('~online::tcg::heart.csv', layout, csv(separator(',')));\n" +
                    "heart:= input[2..];\n" +
                    "ok := output(heart,{age,sex}, named('ok'));\n" +
                    "rateByDepression := output(heart,{max_heart_rate, oldpeak});\n" +
                    "\n" +
                    "genderDistro := record\n" +
                    "\theart.sex;\n" +
                    //"\theart.target;\n" +
                    "\tcategoryCount := count(group);\n" +
                    "\tend;\n" +
                    "\t\n" +
                    "\tslopeDepressionSummary := table(heart, genderDistro, sex);\n" +
                    "\tslopeDepressionSummary;\n";
                    WorkunitInfo genderWU = new WorkunitInfo();
                    genderWU.setECL(genderDistribution);
                    genderWU.setCluster("hthor");
                    List<List<Object>> genderQueryResult = connector.submitECLandGetResultsList(genderWU);


            for (List<Object> objects : genderQueryResult) {
                for (Object object : objects) {
                    System.out.println(object);
                }
            }


            String sexDiseaseQuery = "layout := record\n" +
                    "\tinteger1 age;\n" +
                    "\tinteger1 sex;\n" +
                    "\tinteger1 chest_pain;\n" +
                    "\tunsigned1 blood_pressure;\n" +
                    "\tunsigned2 cholestoral;\n" +
                    "\tinteger1 glucose_greater_than_120;\n" +
                    "\tinteger1 ekg_result;\n" +
                    "\tunsigned2 max_heart_rate;\n" +
                    "\tinteger1 excercised_induced;\n" +
                    "\treal8 oldpeak;\n" +
                    "\tinteger1 slope;\n" +
                    "\tinteger1 flourosopy;\n" +
                    "\tinteger1 thal;\n" +
                    "\tinteger1 target;\n" +
                    "end;\n" +
                    "\n" +
                    "input := dataset('~online::tcg::heart.csv', layout, csv(separator(',')));\n" +
                    "heart:= input[2..];\n" +
                    "ok := output(heart,{age,sex}, named('ok'));\n" +
                    "rateByDepression := output(heart,{max_heart_rate, oldpeak});\n" +
                    "\n" +
                    "slopeDepressionCategory := record\n" +
                    "\theart.sex;\n" +
                    "\theart.target;\n" +
                    "\tcategoryCount := count(group);\n" +
                    "\tend;\n" +
                    "\t\n" +
                    "\tslopeDepressionSummary := table(heart, slopeDepressionCategory, sex, target);\n" +
                    "\tslopeDepressionSummary;\n";

            WorkunitInfo barWU = new WorkunitInfo();
            barWU.setECL(sexDiseaseQuery);
            barWU.setCluster("hthor");
            List<List<Object>> barQueryResult = connector.submitECLandGetResultsList(barWU);

            String ageFrequencyQuery = "layout := record\n" +
                    "\tinteger1 age;\n" +
                    "\tinteger1 sex;\n" +
                    "\tinteger1 chest_pain;\n" +
                    "\tunsigned1 blood_pressure;\n" +
                    "\tunsigned2 cholestoral;\n" +
                    "\tinteger1 glucose_greater_than_120;\n" +
                    "\tinteger1 ekg_result;\n" +
                    "\tunsigned2 max_heart_rate;\n" +
                    "\tinteger1 excercised_induced;\n" +
                    "\treal8 oldpeak;\n" +
                    "\tinteger1 slope;\n" +
                    "\tinteger1 flourosopy;\n" +
                    "\tinteger1 thal;\n" +
                    "\tinteger1 target;\n" +
                    "end;\n" +
                    "\n" +
                    "input := dataset('~online::tcg::heart.csv', layout, csv(separator(',')));\n" +
                    "heart:= input[2..];\n" +
                    "ok := output(heart,{age,sex}, named('ok'));\n" +
                    "rateByDepression := output(heart,{max_heart_rate, oldpeak});\n" +
                    "\n" +
                    "slopeDepressionCategory := record\n" +
                    "\theart.age;\n" +
                    "\theart.target;\n" +
                    "\tcategoryCount := count(group);\n" +
                    "\tend;\n" +
                    "\t\n" +
                    "\tslopeDepressionSummary := table(heart, slopeDepressionCategory, age, target);\n" +
                    "\tslopeDepressionSummary;\n";
            WorkunitInfo ageFrequencyWU = new WorkunitInfo();
            ageFrequencyWU.setECL(ageFrequencyQuery);
            ageFrequencyWU.setCluster("hthor");
            List<List<Object>> ageFrequencyResult = connector.submitECLandGetResultsList(ageFrequencyWU);

            //update charts when the main thread is idle
            javafx.application.Platform.runLater(() -> {
                if (!flag){
                    DecimalFormat df = new DecimalFormat("##.##");
                    df.setRoundingMode(RoundingMode.CEILING);
                    accuracyPie.getData().get(0).setPieValue(Double.valueOf(df.format(precision[0])));
                    accuracyPie.getData().get(1).setPieValue(Double.valueOf(df.format(precision[1])));
                    precisionPie.getData().get(0).setPieValue(precision[2]);
                    precisionPie.getData().get(1).setPieValue(precision[3]);
                    precisionPie.getData().get(2).setPieValue(precision[4]);
                    precisionPie.getData().get(3).setPieValue(precision[5]);
                }
                ageHeartSeries0.setName("No Disease");
                ageHeartSeries1.setName("Heart Disease");
                ageRateAllSeries.setName("All Patients");

                int seriesZeroX = -1;
                Double seriesZeroY = -1.0;
                for (List<Object> objects : seriesZero) {
                    seriesZeroX = Integer.parseInt((String) objects.get(0));
                    seriesZeroY = Double.valueOf((String) objects.get(1));
                    series0.getData().add(new XYChart.Data(seriesZeroX, seriesZeroY));
                }

                int seriesOneX = -1;
                Double seriesOneY = -1.0;
                for (List<Object> objects : seriesOne) {
                    seriesOneX = Integer.parseInt((String) objects.get(0));
                    seriesOneY = Double.valueOf((String) objects.get(1));
                    series1.getData().add(new XYChart.Data<>(seriesOneX, seriesOneY));
                }

                for (List<Object> result : ageHeartResults) {
                    int indicator = Integer.parseInt((String) result.get(2));
                    if (indicator == 0) {
                        ageHeartSeries0.getData().add(new XYChart.Data<>(Integer.parseInt((String) result.get(0)), Integer.parseInt((String) result.get(1))));
                    } else if (indicator == 1) {
                        ageHeartSeries1.getData().add(new XYChart.Data<>(Integer.parseInt((String) result.get(0)), Integer.parseInt((String) result.get(1))));
                    }
                    ageRateAllSeries.getData().add(new XYChart.Data<>(Integer.parseInt((String) result.get(0)), Integer.parseInt((String) result.get(1))));
                }

                for (List<Object> objectList : genderQueryResult) {
                    int groupIndicator1 = Integer.parseInt( (String) objectList.get(0));
                    if (groupIndicator1 == 0){
                        pieChart.getData().get(1).setPieValue(Integer.parseInt((String) objectList.get(1)));
                    }
                    else if (groupIndicator1 == 1){
                        pieChart.getData().get(0).setPieValue(Integer.parseInt((String) objectList.get(1)));
                    }
                }

                int sex = -1;
                int diseased = -1;
                for (List<Object> row : barQueryResult) {
                    sex = Integer.parseInt((String) row.get(0));
                    diseased = Integer.parseInt((String) row.get(1));
                    if (diseased == 0) {
                        if (sex == 1)
                            barCleanSeries.getData().add(new XYChart.Data<>("Male", Integer.parseInt((String) row.get(2))));
                        else
                            barCleanSeries.getData().add(new XYChart.Data<>("Female", Integer.parseInt((String) row.get(2))));
                    } else if (diseased == 1) {
                        if (sex == 1)
                            barDiseasedSeries.getData().add(new XYChart.Data<>("Male", Integer.parseInt((String) row.get(2))));
                        else
                            barDiseasedSeries.getData().add(new XYChart.Data<>("Female", Integer.parseInt((String) row.get(2))));
                    }
                }

                int age = 0;
                int hasDisease = 0;
                for (List<Object> row : ageFrequencyResult) {
                    age = Integer.parseInt((String) row.get(0));
                    hasDisease = Integer.parseInt((String) row.get(1));
                    if (diseased == 0){
                        if (age < 25)
                            ageFrequencySeries0.getData().add(new XYChart.Data<>("18-24", Integer.parseInt((String) row.get(2))));
                    }
                }
                flag = false;
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
    testing
     */
    private void setTestingDefaults(){
//        thalBox.setValue(thalBox.getItems().get(1));
//        chestPainBox.setValue(chestPainBox.getItems().get(1));
//        veinsBox.setValue(veinsBox.getItems().get(1));
//        slopeBox.setValue(slopeBox.getItems().get(1));
//        exerciseCombo.setValue(exerciseCombo.getItems().get(1));
//        ecgCombo.setValue(ecgCombo.getItems().get(1));
//        bloodPressureField.setText("180");
//        cholesterolField.setText("260");
//        bloodSugarField.setText("351");
//        heartRateField.setText("195");
//        oldPeakField.setText("199");
    }

    private void setPatientTable(){
        //id
        JFXTreeTableColumn<Patient, String> idColumn = new JFXTreeTableColumn<>("ID");
        idColumn.setPrefWidth(50);
        idColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().id);
        //first name
        JFXTreeTableColumn<Patient, String> fnameColumn = new JFXTreeTableColumn<>("FirstName");
        fnameColumn.setPrefWidth(125);
        fnameColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().fname);
        //last name
        JFXTreeTableColumn<Patient, String> lnameColumn = new JFXTreeTableColumn<>("LastName");
        lnameColumn.setPrefWidth(125);
        lnameColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().lname);
        //Age
        JFXTreeTableColumn<Patient, String> ageColumn = new JFXTreeTableColumn<>("Age");
        ageColumn.setPrefWidth(50);
        ageColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().age);
        //Gender
        JFXTreeTableColumn<Patient, String> sexColumn = new JFXTreeTableColumn<>("Sex");
        sexColumn.setPrefWidth(50);
        sexColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().sex);

        //chest pain
        JFXTreeTableColumn<Patient, String>  chestColumn = new JFXTreeTableColumn<>("Chest Pain");
        chestColumn.setPrefWidth(125);
        chestColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().chestPain);
        //Blood Pressure
        JFXTreeTableColumn<Patient, String> bloodPressureColumn = new JFXTreeTableColumn<>("Blood Pressure");
        bloodPressureColumn.setPrefWidth(175);
        bloodPressureColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().bloodPressure);
        //Cholesterol
        JFXTreeTableColumn<Patient, String> cholesterolColumn = new JFXTreeTableColumn<>("Cholesterol");
        cholesterolColumn.setPrefWidth(125);
        cholesterolColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().cholesterol);
        //BloodSugar
        JFXTreeTableColumn<Patient, String> bloodSugarColumn = new JFXTreeTableColumn<>("Blood Sugar");
        bloodSugarColumn.setPrefWidth(175);
        bloodSugarColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().bloodSugar);
        //ECG Result
        JFXTreeTableColumn<Patient, String> ecgColumn = new JFXTreeTableColumn<>("ECG Result");
        ecgColumn.setPrefWidth(125);
        ecgColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().ecg);
        //BPM
        JFXTreeTableColumn<Patient, String> heartRateColumn = new JFXTreeTableColumn<>("BPM");
        heartRateColumn.setPrefWidth(75);
        heartRateColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().heartRate);
        //Exercise Induced
        JFXTreeTableColumn<Patient, String> exerciseColumn = new JFXTreeTableColumn<>("Exercise Induced");
        exerciseColumn.setPrefWidth(125);
        exerciseColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().exerciseInduced);
        //ST Depression
        JFXTreeTableColumn<Patient, String> peakColumn = new JFXTreeTableColumn<>("ST Depression");
        peakColumn.setPrefWidth(125);
        peakColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().oldPeak);
        //Slope
        JFXTreeTableColumn<Patient, String> slopeColumn = new JFXTreeTableColumn<>("Slope");
        slopeColumn.setPrefWidth(125);
        slopeColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().slope);
        //Flourosopy
        JFXTreeTableColumn<Patient, String> flourosopyColumn = new JFXTreeTableColumn<>("Flourosopy");
        flourosopyColumn.setPrefWidth(75);
        flourosopyColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().flourosopy);
        //Thal
        JFXTreeTableColumn<Patient, String> thalColumn = new JFXTreeTableColumn<>("Thal");
        thalColumn.setPrefWidth(75);
        thalColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().thal);
        //Heart Disease
        JFXTreeTableColumn<Patient, String> heartDisease = new JFXTreeTableColumn<>("Heart Disease");
        heartDisease.setPrefWidth(125);
        heartDisease.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().disease);

        //
        fnameColumn.setCellFactory((TreeTableColumn<Patient, String> param) -> new GenericEditableTreeTableCell<>(
                new TextFieldEditorBuilder()));
        fnameColumn.setOnEditCommit((TreeTableColumn.CellEditEvent<Patient, String> t) -> t.getTreeTableView()
        .getTreeItem(t.getTreeTablePosition().getRow())
        .getValue().fname.set(t.getNewValue()));

        lnameColumn.setCellFactory((TreeTableColumn<Patient, String> param) -> new GenericEditableTreeTableCell<>(
                new TextFieldEditorBuilder()));
        lnameColumn.setOnEditCommit((TreeTableColumn.CellEditEvent<Patient, String> t) -> t.getTreeTableView()
                .getTreeItem(t.getTreeTablePosition().getRow())
                .getValue().lname.set(t.getNewValue()));

        ObservableList<Patient> patients = FXCollections.observableArrayList();

        //adds columns to tableview
        TreeItem<Patient> root = new RecursiveTreeItem<>(patients, RecursiveTreeObject::getChildren);
        patientTable.getColumns().setAll(
                idColumn,
                fnameColumn,
                lnameColumn,
                ageColumn,
                sexColumn,
                chestColumn,
                bloodPressureColumn,
                cholesterolColumn,
                bloodSugarColumn,
                ecgColumn,
                heartRateColumn,
                exerciseColumn,
                peakColumn,
                slopeColumn,
                flourosopyColumn,
                thalColumn,
                heartDisease
                );
        patientTable.setRoot(root);
        patientTable.setShowRoot(false);

        //Adds pateints to tableview for testing and demo
        Patient patient1 = new Patient("304", "Matthew", "Lusk", "42", "Male", "Typical Angina", "140", "226", "100", "Abnormality", "178", "No", "0", "Downsloping", "Zero", "Previously Fixed Defect");
        Patient patient2 = new Patient("305", "Willie", "Thompson", "55", "Male", "Atypical Angina", "130", "262", "115", "Abnormality", "155", "No", "0", "Downsloping", "Zero", "Previously Fixed Defect");
        Patient patient3 = new Patient("306", "Jennifer", "Landis", "43", "Female", "Non-Anginal", "122", "213", "112", "Abnormality", "165", "No", "0.2", "Flat", "Zero", "Previously Fixed Defect");
        Patient patient4 = new Patient("307", "Benjamin", "McKinley", "62", "Male", "Atypical Angina", "120", "281", "86", "Normal", "103", "No","1.4", "Flat", "One", "Reversible Defect");
        Patient patient5 = new Patient("308", "Henry", "Thurman", "57", "Male", "Atypical Angina", "154", "232", "119", "Normal", "164", "No", "0", "Downsloping", "One", "Previously Fixed Defect");
        Patient patient6 = new Patient("309", "Priscilla", "Alvarez", "57", "Female", "Atypical Angina", "130", "236", "98", "Normal", "174", "No", "0", "Flat", "One", "Previously Fixed Defect");
        ObservableList<TreeItem<Patient>> defaultPatients = patientTable.getRoot().getChildren();
        observations.add(patient1.toCSV());
        observations.add(patient2.toCSV());
        observations.add(patient3.toCSV());
        observations.add(patient4.toCSV());
        observations.add(patient5.toCSV());
        observations.add(patient6.toCSV());
        defaultPatients.add(new TreeItem<>(patient1));
        defaultPatients.add(new TreeItem<>(patient2));
        defaultPatients.add(new TreeItem<>(patient3));
        defaultPatients.add(new TreeItem<>(patient4));
        defaultPatients.add(new TreeItem<>(patient5));
        defaultPatients.add(new TreeItem<>(patient6));
    }

    /*
    Initialized connection to HPCC cluster

     */
    private void initHPCC(){
        Platform platform = Platform.get("http", "192.168.56.101", 8010, "hpccdemo", "hpccdemo");
        HPCCWsClient connector = null;
        try {
            connector = platform.getWsClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DelimitedDataOptions options = new DelimitedDataOptions();
        HPCC.setPlatform(platform);
        HPCC.setClient(connector);
        HPCC.setOptions(options);
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        patientBox.setVisible(false);
        //set default selected choices for some GUI elements
        setSexes();
        setChestPainChoice();
        setEcgChoice();
        setExerciseChoice();
        setSlopeChoice();
        setFlourosopyChoice();
        setThalChoice();
        setDiseaseChoice();
        createCharts();
        //comment out when running for real
        setTestingDefaults();
        setPatientTable();

        //initialize HPCC connection and query file on HPCC
        initHPCC();
        Runnable task = this::uploadAndDownload;
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }
}
