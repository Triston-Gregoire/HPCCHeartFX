package org.openjfx;

import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import weka.core.Instance;
import weka.core.Instances;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/*
    HeartWriter

    April 29th 2019

    Triston Gregoire

    HeartWriter handles the writing out to intermediate flat files before their contents are sent to HPCC cluster

 */
public class HeartWriter {
    private String destination;
    private String file;
    private List<Object> listToWrite;

    /*
    Constructor

    @params String diskLocation - the disk location for the file to be created
    @params String fileName - the name of the file to create at diskLocation
    @params List<Object> input - the initial content to write to the file
     */
    public HeartWriter(String diskLocaiton,String fileName, List<Object> input){
        destination = diskLocaiton;
        file = fileName;
        listToWrite = new ArrayList<>(input);
    }


    /*
    Constructor

    @params String diskLocation - the disk location for the file to be created
    @params String fileName - the name of the file to create at diskLocation
    */
    HeartWriter(String diskLocation, String fileName){
        destination = diskLocation;
        file = fileName;
    }


    /*
    Writes the instances to the location pointed to by class variables diskLocation and file

    @params Instances instances - Collection of patient instances to be written to disk
     */
    void writeEncoded(Instances instances) throws IOException {
        List<Object[]> instanceStrings = new ArrayList<>();
        for (Instance instance : instances)
            instanceStrings.add(instance.toString().split(","));


        List<Object[]> oldFile = Parser.read(getClass().getResource("/heart.csv").getPath());
        oldFile.addAll(instanceStrings);
        List<String> headerList = new ArrayList<>();
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
        headerList.add("Target");

        StringBuilder sb = new StringBuilder(destination);
        sb.append("\\");
        sb.append(file);
        String outputPath = sb.toString();
        System.out.println(outputPath);
        FileWriter fileWriter = new FileWriter(outputPath);
        CsvWriter writer = new CsvWriter(fileWriter, new CsvWriterSettings());
        writer.writeHeaders(headerList);
        writer.writeRowsAndClose(oldFile);
        writer.close();
        fileWriter.close();

    }
}
