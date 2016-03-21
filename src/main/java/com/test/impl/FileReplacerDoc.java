package com.test.impl;

import com.test.interfaces.FileReplacer;
import com.test.services.WordReplaceText;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.hwpf.HWPFDocument;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Alex on 10.03.2016.
 */
public class FileReplacerDoc implements FileReplacer {

    private File rootFile;

    public FileReplacerDoc(File rootFile){
        this.rootFile = rootFile;
    }

    public void replaceTags(ArrayList<String> header, CSVRecord record, File resultFile) {

        WordReplaceText instance = new WordReplaceText();
        HWPFDocument doc = null;
        try {
            doc = instance.openDocument(rootFile.getAbsolutePath());

            for(int i=0; i<header.size(); i++){
                //String temp_s = "$" + header.get(i) + "$";

                doc = instance.replaceText(doc, "<" + header.get(i) + ">", record.get(i));
            }

            instance.saveDocument(doc, resultFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
