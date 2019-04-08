package org.openjfx;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.net.URL;
import java.util.*;
import java.util.List;

import com.jfoenix.controls.*;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import edu.emory.mathcs.backport.java.util.Arrays;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.hpccsystems.ws.client.HPCCFileSprayClient;
import org.hpccsystems.ws.client.HPCCWsClient;
import org.hpccsystems.ws.client.platform.Platform;
import org.hpccsystems.ws.client.platform.WorkunitInfo;
import org.hpccsystems.ws.client.utils.DelimitedDataOptions;

public class FXMLController implements Initializable {
    String fileName = "predictme.csv";
    String filePath;

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






    public void setSexes(){
        ObservableList<String> options = FXCollections.observableArrayList("Female", "Male");
        sexCombo.setValue("Female");
        sexCombo.setItems(options);
    }

    public void setChestPainChoice(){
        ObservableList<String> options = FXCollections.observableArrayList("Typical Angina", "Atypical Angina", "Non-Anginal", "Asymptomatic");
        chestPainBox.setItems(options);
    }

    public void setEcgChoice(){
        ObservableList<String> options = FXCollections.observableArrayList("Normal", "Abnormality", "Probable/Definite Ventricular Hypertrophy");
        ecgCombo.setItems(options);
    }

    public void setExerciseChoice(){
        ObservableList<String> options = FXCollections.observableArrayList("Yes", "No");
        exerciseCombo.setItems(options);
    }

    public void setSlopeChoice() {
        ObservableList<String> options = FXCollections.observableArrayList("Upsloping", "Flat", "Downsloping");
        slopeBox.setItems(options);
    }

    public void setFlourosopyChoice(){
        ObservableList<String> options = FXCollections.observableArrayList("Zero","One", "Two", "Three", "Four");
        veinsBox.setItems(options);
    }

    public void setThalChoice(){
        ObservableList<String> options = FXCollections.observableArrayList("NA", "Normal", "Previously Fixed Defect", "Reversible Defect");
        thalBox.setItems(options);
    }

    public void setDiseaseChoice(){
        ObservableList<String> options = FXCollections.observableArrayList("Narrowing < 50%", "Narrowing > %0%");
        diseaseChoice.setItems(options);
    }

    public void handleAddPress(ActionEvent action){
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
        String excerciseInduced = exerciseCombo.getValue();
        String peak = oldPeakField.getText();
        String slope = slopeBox.getValue();
        String flourosopy = veinsBox.getValue();
        String thal = thalBox.getValue();
        Patient newPatient = new Patient(id, fname, lname, age, sex, chestPain, restingBP, cholesterol, bloodSugar, ecg, maxBPM, excerciseInduced, peak, slope, flourosopy, thal);

        List<String> patientAttribuesToTargetHeartDisease = new ArrayList<String>();
        patientAttribuesToTargetHeartDisease.add(id);
        patientAttribuesToTargetHeartDisease.add(fname);
        patientAttribuesToTargetHeartDisease.add(lname);
        patientAttribuesToTargetHeartDisease.add(age);
        patientAttribuesToTargetHeartDisease.add(sex);
        patientAttribuesToTargetHeartDisease.add(chestPain);
        patientAttribuesToTargetHeartDisease.add(restingBP);
        patientAttribuesToTargetHeartDisease.add(cholesterol);
        patientAttribuesToTargetHeartDisease.add(bloodSugar);
        patientAttribuesToTargetHeartDisease.add(ecg);
        patientAttribuesToTargetHeartDisease.add(maxBPM);
        patientAttribuesToTargetHeartDisease.add(excerciseInduced);
        patientAttribuesToTargetHeartDisease.add(peak);
        patientAttribuesToTargetHeartDisease.add(slope);
        patientAttribuesToTargetHeartDisease.add(flourosopy);
        patientAttribuesToTargetHeartDisease.add(thal);

        ObservableList<Patient> patients = FXCollections.observableArrayList();
        //patients.add(new Patient("Holy", "Shit"));
        //TreeItem<Patient> root = new RecursiveTreeItem<>(patients, RecursiveTreeObject::getChildren);
        //patientTable.setRoot(root);
        //patientTable.setShowRoot(false);
        ObservableList<TreeItem<Patient>> currentList = patientTable.getRoot().getChildren();
        currentList.add(new TreeItem<Patient>(newPatient));


        String patient = String.join(", ", patientAttribuesToTargetHeartDisease);

        ObservableList patientList = patientBox.getItems();
        patientList.add(patient);
        //uploadAndDownload();
    }

    public void handleRemovePressed(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Deleting a patient can't be undone!");

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
        //heartRateSpinner.getValueFactory().setValue(Integer.parseInt(attributes[15]));


    }

    public void checkComboSelection(ActionEvent event){
        boolean isNothingSelected = patientBox.getSelectionModel().isEmpty();
        if(isNothingSelected){  return; }
        removePatientButton.setDisable(false);
        updatePatientButton.setDisable(false);
        //submitPatientsButton.setDisable(false);
    }

    public void checkPatientListOnSubmit(ActionEvent event){
        if (patientBox.getItems().isEmpty()){
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


        //System.out.println(filePath);
        HeartWriter writer = new HeartWriter(filePath, fileName);
        try {
            writer.writeEncoded(Coder.encode(patients));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void editSpinner(MouseEvent event){
    }

    public void handleAgeSlider(ActionEvent event){
        double value = Double.parseDouble(ageField.getText());
        System.out.println(value);
        //ageValueLabel.setText(String.valueOf(value));
    }
    
//    @FXML
//    private void handleButtonAction(ActionEvent event) {
//        System.out.println("You clicked me!");
//        label.setText("Hello World!");
//    }

    public void uploadAndDownload() {
        Platform platform = Platform.get("http", "192.168.56.101", 8010, "hpccdemo", "hpccdemo");
        try {
            NumberAxis xaxis = new NumberAxis(30, 80, 10);
            NumberAxis yaxis = new NumberAxis(0, 7, 1);
            ScatterChart<Number, Number> sc = new
                    ScatterChart<Number, Number>(xaxis, yaxis);
            xaxis.setLabel("Age");
            yaxis.setLabel("depression");
            sc.setTitle("please work");
            XYChart.Series series0 = new XYChart.Series();
            XYChart.Series series1 = new XYChart.Series();
            series0.setName("No disease");
            series1.setName("Heart Disease");
            sc.getData().addAll(series0, series1);

            NumberAxis lineXAsis = new NumberAxis(30, 80, 10);
            NumberAxis lineYAxis = new NumberAxis(75, 200, 25);
            lineXAsis.setLabel("Age (Years)");
            lineYAxis.setLabel("Max Heart Rate");
            LineChart<Number, Number> lineChart = new LineChart<Number, Number>(lineXAsis, lineYAxis);
            lineChart.setTitle("Age vs Heart Rate");
            XYChart.Series ageHeartSeries0 = new XYChart.Series();
            XYChart.Series ageHeartSeries1 = new XYChart.Series();
            XYChart.Series ageRateAllSeries = new XYChart.Series();
            lineChart.getData().addAll(ageRateAllSeries);

            CategoryAxis barSexAxis = new CategoryAxis();
            NumberAxis barCountAxis = new NumberAxis(0, 120, 20);
            BarChart<String, Number> barChart = new BarChart<String, Number>(barSexAxis, barCountAxis);
            barSexAxis.setLabel("Gender");
            barCountAxis.setLabel("Count");
            barChart.setTitle("# of Male and Female with and without heart disease");
            XYChart.Series barCleanSeries = new XYChart.Series();
            XYChart.Series barDiseasedSeries = new XYChart.Series();
            barCleanSeries.setName("Negative");
            barDiseasedSeries.setName("Positive");
            barChart.getData().addAll(barCleanSeries, barDiseasedSeries);


            chartGrid.add(sc, 0, 0);
            chartGrid.add(lineChart, 0, 1);
            chartGrid.add(barChart, 1, 1);
            //tab2Pane.getChildren().get(0)


            CsvParserSettings parserSettings = new CsvParserSettings();
            CsvParser parser = new CsvParser(parserSettings);
            FileReader fileReader = new FileReader("C:\\Users\\h3hmm\\Desktop\\heart-disease-uci\\heart.csv");
            int i = 0;
            List<String[]> document = new ArrayList<String[]>();
            for (String[] row : parser.iterate(fileReader)) {
                if (i == 0) {
                    i++;
                    continue;
                }
                String[] newRow = new String[row.length];
                for (int j = 0; j < row.length; j++) {
                    newRow[j] = String.valueOf(row[j]);
                }
                //String[] newRow = (String[]) Arrays.copyOf(row, row.length-1);
                document.add(newRow);
                i++;
            }

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
                    "heart:= sample(input[2..], 5, 1);\n" +
                    "ok := output(heart,{age,oldpeak,target}, named('ok'));\n" +
                    "rateByDepression := output(heart,{max_heart_rate, oldpeak});\n" +
                    "ok;\n";


            HPCCWsClient connector = platform.getWsClient();
            DelimitedDataOptions options = new DelimitedDataOptions();
            boolean isuploaded = connector.httpUploadFileToFirstHPCCLandingZone("C:\\Users\\h3hmm\\Desktop\\predictme.csv");//THIS COULD BE THE KEY
//            connector.uploadFileToHPCC()
            //Thread.sleep(10000);
            connector.sprayVariableHPCCFile("predictme.csv", "~online::tcg::predictme.csv", "hthor__myeclagent", options, true, HPCCFileSprayClient.SprayVariableFormat.DFUff_csv);
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
            int groupIndicator = -1;
            for (List<Object> result : results) {
                groupIndicator = Integer.parseInt((String) result.get(2));
                if (groupIndicator == 0) {
                    seriesZero.add(result);
                } else if (groupIndicator == 1) {
                    seriesOne.add(result);
                }
            }


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


            lineXAsis.setLabel("Age (Years)");
            lineYAxis.setLabel("Max Heart Rate");
            lineChart.setTitle("Age vs Heart Rate");

            ageHeartSeries0.setName("No Disease");
            ageHeartSeries1.setName("Heart Disease");
            ageRateAllSeries.setName("All Patients");

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
                    "heart:= sample(input[2..], 5, 1);\n" +
                    "ok := output(heart,{age,max_heart_rate,target}, named('ok'));\n" +
                    "ok;\n";

            WorkunitInfo ageRateWU = new WorkunitInfo();
            ageRateWU.setECL(ageHeartQuery);
            ageRateWU.setCluster("hthor");
            List<List<Object>> ageHeartResults = connector.submitECLandGetResultsList(ageRateWU);
            List<List<Object>> ageHeartSeriesZero = new ArrayList<>();
            List<List<Object>> ageHeartSeriesOne = new ArrayList<>();
            List<List<Object>> ageRateAllList = new ArrayList<>();
            groupIndicator = -1;
            for (List<Object> result : ageHeartResults) {
                groupIndicator = Integer.parseInt((String) result.get(2));
                if (groupIndicator == 0) {
                    ageHeartSeriesZero.add(result);
                } else if (groupIndicator == 1) {
                    ageHeartSeriesOne.add(result);
                }
                ageRateAllSeries.getData().add(new XYChart.Data<>(Integer.parseInt((String) result.get(0)), Integer.parseInt((String) result.get(1))));
            }

            //System.out.printlm();
            int ageHeartSeriesZeroX = -1;
            int ageHeartSeriesZeroY = -1;
            for (List<Object> objects : ageHeartSeriesZero) {
                ageHeartSeriesZeroX = Integer.parseInt((String) objects.get(0));
                ageHeartSeriesZeroY = Integer.parseInt((String) objects.get(1));
                ageHeartSeries0.getData().add(new XYChart.Data(ageHeartSeriesZeroX, ageHeartSeriesZeroY));
            }

            int ageHeartSeriesOneX = -1;
            int ageHeartSeriesOneY = -1;
            for (List<Object> objects : ageHeartSeriesOne) {
                ageHeartSeriesOneX = Integer.parseInt((String) objects.get(0));
                ageHeartSeriesOneY = Integer.parseInt((String) objects.get(1));
                ageHeartSeries1.getData().add(new XYChart.Data<>(ageHeartSeriesOneX, ageHeartSeriesOneY));
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
            List<List<Object>> noDiseaseList = new ArrayList<>();
            List<List<Object>> diseasedList = new ArrayList<>();

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setTestingDefaults(){
        thalBox.setValue(thalBox.getItems().get(1));
        chestPainBox.setValue(chestPainBox.getItems().get(1));
        veinsBox.setValue(veinsBox.getItems().get(1));
        slopeBox.setValue(slopeBox.getItems().get(1));
        exerciseCombo.setValue(exerciseCombo.getItems().get(1));
        ecgCombo.setValue(ecgCombo.getItems().get(1));
        bloodPressureField.setText("180");
        cholesterolField.setText("260");
        bloodSugarField.setText("351");
        heartRateField.setText("195");
        oldPeakField.setText("199");




    }

    public void setPatientTable(){
        //id
        JFXTreeTableColumn<Patient, String> idColumn = new JFXTreeTableColumn<>("Patient ID");
        idColumn.setPrefWidth(75);
        idColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().id);
        //first name
        JFXTreeTableColumn<Patient, String> fnameColumn = new JFXTreeTableColumn<>("FirstName");
        fnameColumn.setPrefWidth(125);
        fnameColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().fname);
        //last name
        JFXTreeTableColumn<Patient, String> lnameColumn = new JFXTreeTableColumn<>("LastName");
        lnameColumn.setPrefWidth(125);
        lnameColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().lname);

        JFXTreeTableColumn<Patient, String> ageColumn = new JFXTreeTableColumn<>("Age");
        ageColumn.setPrefWidth(50);
        ageColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().age);

        JFXTreeTableColumn<Patient, String> sexColumn = new JFXTreeTableColumn<>("Sex");
        sexColumn.setPrefWidth(50);
        sexColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().sex);

        //chest pain
        JFXTreeTableColumn<Patient, String>  chestColumn = new JFXTreeTableColumn<>("Chest Pain");
        chestColumn.setPrefWidth(125);
        chestColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().chestPain);

        JFXTreeTableColumn<Patient, String> bloodPressureColumn = new JFXTreeTableColumn<>("Blood Pressure");
        bloodPressureColumn.setPrefWidth(125);
        bloodPressureColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().bloodPressure);

        JFXTreeTableColumn<Patient, String> cholesterolColumn = new JFXTreeTableColumn<>("Cholesterol");
        cholesterolColumn.setPrefWidth(75);
        cholesterolColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().cholesterol);

        JFXTreeTableColumn<Patient, String> bloodSugarColumn = new JFXTreeTableColumn<>("Blood Sugar");
        bloodSugarColumn.setPrefWidth(125);
        bloodSugarColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().bloodSugar);

        JFXTreeTableColumn<Patient, String> ecgColumn = new JFXTreeTableColumn<>("ECG Result");
        ecgColumn.setPrefWidth(125);
        ecgColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().ecg);

        JFXTreeTableColumn<Patient, String> heartRateColumn = new JFXTreeTableColumn<>("Max Heart Rate");
        heartRateColumn.setPrefWidth(125);
        heartRateColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().heartRate);

        JFXTreeTableColumn<Patient, String> exerciseColumn = new JFXTreeTableColumn<>("Exercise Induced");
        exerciseColumn.setPrefWidth(125);
        exerciseColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().exerciseInduced);

        JFXTreeTableColumn<Patient, String> peakColumn = new JFXTreeTableColumn<>("ST Depression");
        peakColumn.setPrefWidth(125);
        peakColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().oldPeak);

        JFXTreeTableColumn<Patient, String> slopeColumn = new JFXTreeTableColumn<>("Slope");
        slopeColumn.setPrefWidth(125);
        slopeColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().slope);

        JFXTreeTableColumn<Patient, String> flourosopyColumn = new JFXTreeTableColumn<>("Flourosopy");
        flourosopyColumn.setPrefWidth(75);
        flourosopyColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().flourosopy);

        JFXTreeTableColumn<Patient, String> thalColumn = new JFXTreeTableColumn<>("Thal");
        thalColumn.setPrefWidth(75);
        thalColumn.setCellValueFactory(patientStringCellDataFeatures -> patientStringCellDataFeatures.getValue().getValue().thal);



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
//        patients.add(new Patient("Triston", "Gregoire"));
//        patients.add(new Patient("Anastasia", "Romanova"));

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
                thalColumn

                );
        patientTable.setRoot(root);
        patientTable.setShowRoot(false);
        //patients.get(1).fname = new SimpleStringProperty( "Natalia");
        //patientTable.getCh


    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setSexes();
        setChestPainChoice();
        setEcgChoice();
        setExerciseChoice();
        setSlopeChoice();
        setFlourosopyChoice();
        setThalChoice();
        setDiseaseChoice();
        //comment out when running for real
        setTestingDefaults();
        setPatientTable();
    }

}
