package org.openjfx;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Parser {

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
            //String[] newRow = (String[]) Arrays.copyOf(row, row.length-1);
            document.add(newRow);
            i++;
        }
        return document;
    }
}
