package com.test.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.maven.internal.commons.io.input.BOMInputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alex on 15.02.2016.
 */
public class CSVReader {
    private ArrayList<String> header;
    private List records;
    private File csvFile;

    public ArrayList<String> getHeader() {
        return header;
    }

    public List getRecords() {
        return records;
    }

    public CSVReader(File csvFile){
        this.csvFile = csvFile;
        this.header = generateHeader();
        this.records = receiveRecords();
    }

    public List receiveRecords(){
        CSVFormat csvFileFormat = CSVFormat.EXCEL.withDelimiter(getDelimiter(csvFile));
        List result = new LinkedList<>();

        try {
            InputStream inputStream = new FileInputStream(csvFile);
            Reader fileReader = new InputStreamReader(inputStream, "UTF-8");
            CSVParser csvFileParser = new CSVParser(fileReader, csvFileFormat);

            List temp = csvFileParser.getRecords();
            for(int i=1; i<temp.size(); i++){
                result.add(temp.get(i));
            }

            fileReader.close();
            inputStream.close();
            csvFileParser.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public List searchRecords(String word){
        List result = new LinkedList<>();
        for (int i=0; i<records.size(); i++){
            CSVRecord record = (CSVRecord) records.get(i);
            for(int j=0; j<record.size(); j++){
                if(record.get(j).contains(word) && !result.contains(record)){
                    result.add(record);
                }
            }
        }
        return result;
    }

    public List getVerticalRecords(int index, List recs){
        List result = new LinkedList<>();
        for(int i=0; i<recs.size(); i++){
            CSVRecord record = (CSVRecord) recs.get(i);
            result.add(record.get(index));
        }
        return result;
    }



    private char getDelimiter(File file){
        BufferedReader br = null;

        try {

            String sCurrentLine;
            br = new BufferedReader(new FileReader(file));

            while ((sCurrentLine = br.readLine()) != null) {
                    if(sCurrentLine.contains(";")){
                        return ';';
                    }
                    else if(sCurrentLine.contains(",")){
                        return ',';
                    }

            }

            System.out.println("Done");


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return 'a';
    }

    private ArrayList<String> generateHeader(){

        CSVFormat csvFileFormat = CSVFormat.EXCEL.withDelimiter(getDelimiter(csvFile));
        CSVRecord record = null;
        ArrayList<String> result = null;

        try {
            FileReader fileReader = new FileReader(csvFile);
            CSVParser csvFileParser = new CSVParser(fileReader, csvFileFormat);

            List csvRecords = csvFileParser.getRecords();
            record = (CSVRecord) csvRecords.get(0);

            result = new ArrayList<String>();

            for(int i=0; i<record.size(); i++){
                result.add(record.get(i));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
