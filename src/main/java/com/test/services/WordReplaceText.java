package com.test.services;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Alex on 10.03.2016.
 */
public class WordReplaceText {
    public static final String SOURCE_FILE = "C:\\Users\\Alex\\Desktop\\Doc1.doc";
    public static final String OUTPUT_FILE = "new_test.doc";

    public static void main(String[] args) throws Exception {
        WordReplaceText instance = new WordReplaceText();
        HWPFDocument doc = instance.openDocument(SOURCE_FILE);
        if (doc != null) {
            doc = instance.replaceText(doc, "<Title>", "Br");
            instance.saveDocument(doc, OUTPUT_FILE);
        }
    }

    public HWPFDocument replaceText(HWPFDocument doc, String findText, String replaceText) {
        Range r = doc.getRange();
        for (int i = 0; i < r.numSections(); ++i) {
            Section s = r.getSection(i);
            for (int j = 0; j < s.numParagraphs(); j++) {
                Paragraph p = s.getParagraph(j);
                /*for (int k = 0; k < p.numCharacterRuns(); k++) {
                    CharacterRun run = p.getCharacterRun(k);
                    //String text = run.text();
                    //if (text.contains(findText)) {
                        run.replaceText(findText, replaceText);

                    //}
                }*/
                p.replaceText(findText, replaceText);
            }
        }
        return doc;
    }

    public HWPFDocument openDocument(String file) throws Exception {
        HWPFDocument document = null;
        document = new HWPFDocument(new POIFSFileSystem(new File(file)));
        return document;
    }

    public void saveDocument(HWPFDocument doc, String file) {
        try (FileOutputStream out = new FileOutputStream(file)) {
            doc.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
