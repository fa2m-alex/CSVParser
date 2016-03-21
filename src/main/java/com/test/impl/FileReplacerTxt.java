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

            //File file = new File(path);
            //FileWriter fw = new FileWriter(resultFile.getAbsoluteFile());
            Writer bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resultFile.getAbsoluteFile()), "UTF-8"));

            while ((sCurrentLine = br.readLine()) != null) {
                for(int i=0; i<header.size(); i++){
                    sCurrentLine = sCurrentLine.replaceAll("<" + header.get(i) + ">", record.get(i));
                }
                //System.out.println(sCurrentLine);
                sCurrentLine += System.getProperty("line.separator");
                bw.write(sCurrentLine);
            }

            System.out.println("Done");
            bw.close();
            //fw.close();

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
