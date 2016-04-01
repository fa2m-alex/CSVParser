package com.test.impl;

import com.independentsoft.office.word.WordDocument;
import com.test.interfaces.FileReplacer;
import org.apache.commons.csv.CSVRecord;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Alex on 31.03.2016.
 */
public class FileReplacerDocx implements FileReplacer {

    private File rootFile;

    public FileReplacerDocx(File rootFile){
        this.rootFile = rootFile;
    }

    @Override
    public void replaceTags(ArrayList<String> header, CSVRecord record, File resultFile) {
        try {
            WordDocument doc = new WordDocument(rootFile.getAbsolutePath());
            for(int i=0; i<header.size(); i++){
                doc.replace("<" + header.get(i) + ">", record.get(i));
            }
            doc.save(resultFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void replaceTagsWithCoef(ArrayList<String> header, int headerIndex, double coefficient, CSVRecord record, File resultFile) {
        try {
            WordDocument doc = new WordDocument(rootFile.getAbsolutePath());
            for(int i=0; i<header.size(); i++){
                if(i == headerIndex){
                    double replacer = Double.parseDouble(record.get(i)) * coefficient;
                    doc.replace("<" + header.get(i) + ">", String.valueOf(replacer));
                }
                else{
                    doc.replace("<" + header.get(i) + ">", record.get(i));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
