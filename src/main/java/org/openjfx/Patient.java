package org.openjfx;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
Patient stores all patient attributes entered into the system as well as print their attributes into convenient forms

 */
public class Patient extends RecursiveTreeObject<Patient> {
    StringProperty id;
    StringProperty fname;
    StringProperty lname;
    StringProperty age;
    StringProperty sex;
    StringProperty chestPain;
    StringProperty bloodPressure;
    StringProperty cholesterol;
    StringProperty bloodSugar;
    StringProperty ecg;
    StringProperty heartRate;
    StringProperty exerciseInduced;
    StringProperty oldPeak;
    StringProperty slope;
    StringProperty flourosopy;
    StringProperty thal;
    StringProperty disease;

    /*
    Constructor

    @param String id - patient identification number
    @param String fname - patient First Name
    @param String lname - patient Last Name
    @param String age - patient's age
    @param String sex - patient's gender
    @param String chestPain - patient's type of chestpain
    @param String bloodPressure - patient's blood pressure
    @param String cholesterol - patient's cholesterol
    @param String bloodSugar - patient's bloodSugar
    @param String ecg - patient's ECG result type
    @param String heartRate - patient's heart rate
    @param String exerciseInduced - whether or not the patient had exercise induced pain during testing
    @param String oldPeak - the ST depression type during testing
    @param String slope - the slope of the patient's pressure during testing
    @param String flourosopy - the amount of veins colored bu flourosopy
    @param String thal - the patient's thal amount
     */
    Patient(String id, String fname, String lname, String age, String sex, String chestPain, String bloodPressure, String cholesterol,
            String bloodSugar, String ecg, String heartRate, String exerciseInduced, String oldPeak, String slope,
            String flourosopy, String thal){
        this.id = new SimpleStringProperty(id);
        this.fname = new SimpleStringProperty(fname);
        this.lname = new SimpleStringProperty(lname);
        this.age = new SimpleStringProperty(age);
        this.sex = new SimpleStringProperty(sex);
        this.chestPain = new SimpleStringProperty(chestPain);
        this.bloodPressure =  new SimpleStringProperty(bloodPressure);
        this.cholesterol = new SimpleStringProperty(cholesterol);
        this.bloodSugar = new SimpleStringProperty(bloodSugar);
        this.ecg = new SimpleStringProperty(ecg);
        this.heartRate = new SimpleStringProperty(heartRate);
        this.exerciseInduced = new SimpleStringProperty(exerciseInduced);
        this.oldPeak = new SimpleStringProperty(oldPeak);
        this.slope = new SimpleStringProperty(slope);
        this.flourosopy = new SimpleStringProperty(flourosopy);
        this.thal = new SimpleStringProperty(thal);
        this.disease = new SimpleStringProperty("?");
    }

    /*
    Outputs a patient's attributes in the form of a List<String> where each string in the list is a separate attribute

    return List<String> attributes - list of String attributes
     */
    public List<String> toList(){
        return new ArrayList<String>(Arrays.asList(
                id.get(),
                fname.get(),
                lname.get(),
                age.get(),
                sex.get(),
                chestPain.get(),
                bloodPressure.get(),
                cholesterol.get(),
                bloodSugar.get(),
                ecg.get(),
                heartRate.get(),
                exerciseInduced.get(),
                oldPeak.get(),
                slope.get(),
                flourosopy.get(),
                thal.get(),
                disease.get()
        ));
    }

    /*
    outputs the patient's attributes as a comma separated string

    return String attributes - CSV form of a patient's attributes
     */
    public String toCSV(){
        return String.join(", ", toList());
    }
}
