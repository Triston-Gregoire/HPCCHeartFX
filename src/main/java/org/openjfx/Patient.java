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
    StringProperty disease;



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
    public String toCSV(){
        return String.join(", ", toList());
    }
}
