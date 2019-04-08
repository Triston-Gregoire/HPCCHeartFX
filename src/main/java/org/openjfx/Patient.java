package org.openjfx;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    }

    public List<String> toList(){
        List<String> result = new ArrayList<String>(Arrays.asList(
                id.toString(),
                fname.toString(),
                lname.toString(),
                age.toString(),
                sex.toString(),
                chestPain.toString(),
                bloodPressure.toString(),
                cholesterol.toString(),
                bloodSugar.toString(),
                ecg.toString(),
                heartRate.toString(),
                exerciseInduced.toString(),
                oldPeak.toString(),
                slope.toString(),
                flourosopy.toString(),
                thal.toString()
        ));
        return result;
    }
}
