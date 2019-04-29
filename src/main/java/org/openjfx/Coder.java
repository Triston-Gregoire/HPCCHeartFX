package org.openjfx;

import edu.emory.mathcs.backport.java.util.Arrays;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/*
    Coder

    April 29th 2019

    Triston Gregorie


    Class Coder deals with mapping user input into numerical and nominal format that is readable for HPCC
 */
class Coder {
    /*
    This function encodes list of comma separated string into numerical representations appropriate for machine learning algorithms

    @param List<String> input - List of strings which should contain comma separated attributes
    @return List<Object[]> cleanList - List of mapped attributes. Each object[] in the list represents a patient
     */
    static List<Object[]> encode(List<String> input){
        List<Object[]> cleanList = new ArrayList<>();
        Object[] encodedAttributes = new String[20];
        for (String o : input) {
            String[] attributes = o.split(", ");
            encodedAttributes[0] = attributes[0];
            encodedAttributes[1] = attributes[1];
            encodedAttributes[2] = attributes[2];
            encodedAttributes[3] = attributes[3];
            encodedAttributes[4] = mapSex(attributes[4]);
            encodedAttributes[5] = mapChestPain(attributes[5]);
            encodedAttributes[6] = attributes[6];
            encodedAttributes[7] = attributes[7];
            encodedAttributes[8] = mapBloodSugar(attributes[8]);
            encodedAttributes[9] = mapECG(attributes[9]);
            encodedAttributes[10] = attributes[10];
            encodedAttributes[11] = mapExcercised(attributes[11]);
            encodedAttributes[12] = attributes[12];
            encodedAttributes[13] = mapSlope(attributes[13]);
            encodedAttributes[14] = mapflourosopy(attributes[14]);
            encodedAttributes[15] = mapThal(attributes[15]);
            cleanList.add(Arrays.copyOf(encodedAttributes,16));
        }
        return cleanList;
    }
    /*
    Maps the gender of the patient to a binary value. The function is case sensitive

    @param String sex - Gender being either Male or Female
    @returns result - numeric representation of gender
     */
    private static String mapSex(String sex){
        String result = null;
        assert(sex != null);
        if ("Male".equals(sex))
            result = "1";//Male = 1
        else if ("Female".equals(sex))
            result = "0";//Male = 0
        return result;
    }

    /*
    Maps various types of chest pain to numeric representation

    @param String cp - type of chest pain
    @returns chestPain - numeric representation of cp
     */
    private static String mapChestPain(String cp){
        assert(cp != null);
        String chestPain = null;
        switch (cp){
            case "Typical Angina":
                chestPain = "0";
                break;
            case "Atypical Angina":
                chestPain = "1";
                break;
            case "Non-Anginal":
                chestPain = "2";
                break;
            case "Asymptomatic":
                chestPain = "3";
                break;
        }
        return chestPain;
    }

    /*
    Maps received blood sugar to numeric representation
    blood sugar is mapped according to whether or not they are above 120

    @param String sugah - blood sugar expecting only numbers in parameter
    @returns String result - numeric representation of mapped blood sugar
     */
    private static String mapBloodSugar(String sugah){
        String result = null;
        int num = Integer.parseInt(sugah);
        if (num > 120)
            result = "1";
        else
            result = "0";
        return result;
    }

    /*
    Maps ECG to numeric value suitable for machine learning

    @param String ecg - Type of ecg
    @returns String result - numeric representation of ecg
     */
    private static String mapECG(String ecg){
        assert(ecg != null);
        String result = null;
        switch (ecg) {
            case "Normal":
                result = "0";
                break;
            case "Abnormality":
                result = "1";
                break;
            case "Probable/Definite Ventricular Hypertrophy":
                result = "2";
                break;
        }
        return result;
    }

        /*
    Maps whether a patient had exercise induced pain to numeric value suitable for machine learning

    @param String exercise - Yes or No
    @returns String result - numeric representation of whether or not patient had exercise induced pain
     */

    private static String mapExcercised(String exercise){
        assert(exercise != null);
        String result = null;
        if ("No".equals(exercise))
            result = "0";
        else if ("Yes".equals(exercise))
            result = "1";
        return result;
    }

    /*
    Maps slope to numeric value suitable for machine learning

    @param String slope - Type of slope
    @returns String result - numeric representation of slope
     */
    private static String mapSlope(String slope){
        assert(slope != null);
        String result = null;
        if ("Upsloping".equals(slope))
            result = "0";
        else if ("Flat".equals(slope))
            result = "1";
        else if ("Downsloping".equals(slope))
            result = "2";
        return result;
    }

    /*
    Maps flourosopy to numeric value suitable for machine learning

    @param String flour - amount in word form of the veins colored during flourosopy
    @returns String result - numeric representation of ecg
     */
    private static String mapflourosopy(String flour){
        assert (flour != null);
        String result = null;
        switch (flour) {
            case "Zero":
                result = "0";
                break;
            case "One":
                result = "1";
                break;
            case "Two":
                result = "2";
                break;
            case "Three":
                result = "3";
                break;
            case "Four":
                result = "4";
                break;
        }
        return result;
    }

    /*
    Maps Thal to numeric value suitable for machine learning

    @param String Thal - Type of Thal received from UI
    @returns String result - numeric representation of Thal
     */
    private static String mapThal(String thal){
        assert(thal != null);
        String result = null;
        switch (thal) {
            case "Normal":
                result = "1";
                break;
            case "Previously Fixed Defect":
                result = "2";
                break;
            case "Reversible Defect":
                result = "3";
                break;
            case "???":
                result = "0";
                break;
        }
        return result;
    }
}
