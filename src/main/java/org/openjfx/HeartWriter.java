package org.openjfx;

import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class HeartWriter {
    String destination;
    String file;
    List<Object> listToWrite;
    List<Object[]> encodedList;
    public HeartWriter(String diskLocaiton,String fileName, List<Object> input){
        destination = diskLocaiton;
        file = fileName;
        listToWrite = new ArrayList<>(input);
    }

    public HeartWriter(String diskLocation, String fileName){
        destination = diskLocation;
        file = fileName;
    }

    public void writeEncoded(List<Object[]> input) throws IOException {
//        input.removeIf(Objects::isNull);
//        input.removeAll(Collections.singletonList(null));
//        input.removeAll(Arrays.asList("", null));
        List<String> headerList = new ArrayList<>();
        headerList.add("PatientID");
        headerList.add("FirstName");
        headerList.add("LastName");
        headerList.add("Age");
        headerList.add("Sex");
        headerList.add("ChestPainType");
        headerList.add("RestingBloodPressure");
        headerList.add("Cholesterol");
        headerList.add("FastingBloodSugar");
        headerList.add("EcgResult");
        headerList.add("MaxHeartRate");
        headerList.add("ExerciseInduced");
        headerList.add("OldPeak");
        headerList.add("Slope");
        headerList.add("FlourosopyColoredVeins");
        headerList.add("Thal");

        StringBuilder sb = new StringBuilder(destination);
        sb.append("\\");
        sb.append(file);
        String outputPath = sb.toString();
        System.out.println(outputPath);
        FileWriter fileWriter = new FileWriter(outputPath);
        CsvWriter writer = new CsvWriter(fileWriter, new CsvWriterSettings());
        writer.writeHeaders(headerList);
        writer.writeRowsAndClose(input);
    }

    public void writeCSV() throws IOException {
        Collection <Object[]> rows = new ArrayList<>();
        for (Object o : listToWrite) {
            String instance = (String) o;
            Object[] objects = instance.split(", ");
            rows.add(objects);
        }


        List<String> headerList = new ArrayList<>();
        headerList.add("PatientID");
        headerList.add("FirstName");
        headerList.add("LastName");
        headerList.add("Age");
        headerList.add("Sex");
        headerList.add("ChestPainType");
        headerList.add("RestingBloodPressure");
        headerList.add("Cholesterol");
        headerList.add("EcgResult");
        headerList.add("MaxHeartRate");
        headerList.add("ExerciseInduced");
        headerList.add("OldPeak");
        headerList.add("Slope");
        headerList.add("FlourosopyColoredVeins");
        headerList.add("Thal");
        //headerList.add("HeartDisease");

        StringBuilder sb = new StringBuilder(destination);
        sb.append("\\");
        sb.append(file);
        String outputPath = sb.toString();
        System.out.println(outputPath);
        FileWriter fileWriter = new FileWriter(outputPath);
        CsvWriter writer = new CsvWriter(fileWriter, new CsvWriterSettings());
        writer.writeHeaders(headerList);
        writer.writeRowsAndClose(rows);
    }

}
