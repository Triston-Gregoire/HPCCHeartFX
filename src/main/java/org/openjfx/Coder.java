package org.openjfx;

import edu.emory.mathcs.backport.java.util.Arrays;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Coder {
    public static List<Object[]> encode(List<Object> input){
        List<Object[]> cleanList = new ArrayList<>();
        Object[] encodedAttributes = new String[20];
        for (Object o : input) {
            String[] attributes = ((String) o).split(", ");
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

    private static String mapSex(String sex){
        String result = null;
        assert(sex != null);
        if ("Male".equals(sex))
            result = "1";
        else if ("Female".equals(sex))
            result = "0";
        return result;
    }

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

    private static String mapBloodSugar(String sugah){
        String result = null;
        int num = Integer.parseInt(sugah);
        if (num > 120)
            result = "1";
        else
            result = "0";
        return result;
    }
    private static String mapECG(String ecg){
        assert(ecg != null);
        String result = null;
        if ("Normal".equals(ecg))
            result = "0";
        else if ("Abnormality".equals(ecg))
            result = "1";
        else if ("Probable/Definite Ventricular Hypertrophy".equals(ecg))
            result = "2";
        return result;
    }

    private static String mapExcercised(String exercise){
        assert(exercise != null);
        String result = null;
        if ("No".equals(exercise))
            result = "0";
        else if ("Yes".equals(exercise))
            result = "1";
        return result;
    }

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
