package com.test.interfaces;

import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Alex on 16.02.2016.
 */
public interface FileReplacer {
    public void replaceTags(ArrayList<String> header, CSVRecord record, File resultFile);
    public void replaceTagsWithCoef(ArrayList<String> header, int headerIndex, double coefficient, CSVRecord record, File resultFile);
}
