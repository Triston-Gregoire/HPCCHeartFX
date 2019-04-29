package org.openjfx;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
/*
Parser handles reading from the csv files for their use in the application
*/
public class Parser {

    /*
    reads the file pointed to by parameter path into memory

    @param String path - location of csv file to read
    @return List<Object[]> document - content of csv file red into memory
     */
    public static List<Object[]> read(String path){
        CsvParserSettings parserSettings = new CsvParserSettings();
        CsvParser parser = new CsvParser(parserSettings);
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int i = 0;
        List<Object[]> document = new ArrayList<>();
        for (String[] row : parser.iterate(fileReader)) {
            if (i == 0) {
                i++;
                continue;
            }
            String[] newRow = new String[row.length];
            for (int j = 0; j < row.length; j++) {
                newRow[j] = String.valueOf(row[j]);
            }
            document.add(newRow);
            i++;
        }
        return document;
    }
}
