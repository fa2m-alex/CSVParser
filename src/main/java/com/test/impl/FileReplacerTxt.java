package com.test.impl;

import com.test.interfaces.FileReplacer;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Alex on 16.02.2016.
 */
public class FileReplacerTxt implements FileReplacer {
    private File rootFile;

    public FileReplacerTxt(File rootFile){
        this.rootFile = rootFile;
    }

    public void replaceTags(ArrayList<String> header, CSVRecord record, File resultFile){
        BufferedReader br = null;

        try {

            String sCurrentLine;
            br = new BufferedReader(new FileReader(rootFile));

            Writer bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resultFile.getAbsoluteFile()), "UTF-8"));

            while ((sCurrentLine = br.readLine()) != null) {
                for(int i=0; i<header.size(); i++){
                    sCurrentLine = sCurrentLine.replaceAll("<" + header.get(i) + ">", record.get(i));
                }
                sCurrentLine += System.getProperty("line.separator");
                bw.write(sCurrentLine);
            }

            System.out.println("Done");
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void replaceTagsWithCoef(ArrayList<String> header, int headerIndex, double coefficient, CSVRecord record, File resultFile) {
        BufferedReader br = null;

        try {

            String sCurrentLine;
            br = new BufferedReader(new FileReader(rootFile));

            Writer bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resultFile.getAbsoluteFile()), "UTF-8"));

            while ((sCurrentLine = br.readLine()) != null) {
                for(int i=0; i<header.size(); i++){
                    if(i == headerIndex) {
                        double replacer = Double.parseDouble(record.get(i)) * coefficient;
                        sCurrentLine = sCurrentLine.replaceAll("<" + header.get(i) + ">", String.valueOf(replacer));
                    }
                    else{
                        sCurrentLine = sCurrentLine.replaceAll("<" + header.get(i) + ">", record.get(i));
                    }
                }
                sCurrentLine += System.getProperty("line.separator");
                bw.write(sCurrentLine);
            }

            System.out.println("Done");
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
