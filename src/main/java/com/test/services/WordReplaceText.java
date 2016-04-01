package com.test.services;

import com.independentsoft.office.word.Run;
import com.independentsoft.office.word.WordDocument;
import com.test.impl.FileReplacerTxt;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Alex on 10.03.2016.
 */
public class WordReplaceText {
    public static final String SOURCE_FILE = "C:\\Users\\Alex\\Desktop\\Doc1sdg.docx";
    public static final String OUTPUT_FILE = "new_test.doc";

    public static void main(String[] args) throws Exception {
       /* WordReplaceText instance = new WordReplaceText();
        HWPFDocument doc = instance.openDocument(SOURCE_FILE);
        if (doc != null) {
            doc = instance.replaceText(doc, "<Title>", "Br");
            instance.saveDocument(doc, OUTPUT_FILE);
        }*/


        WordDocument doc = new WordDocument(SOURCE_FILE);
        doc.replace("<name>", "Олексій");
        doc.save("output.docx", true);


    }

    public HWPFDocument replaceText(HWPFDocument doc, String findText, String replaceText) {
        Range r = doc.getRange();
        for (int i = 0; i < r.numSections(); ++i) {
            Section s = r.getSection(i);
            for (int j = 0; j < s.numParagraphs(); j++) {
                Paragraph p = s.getParagraph(j);
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


    public XWPFDocument replaceText(XWPFDocument doc, String findText, String replaceText){
        for (int i = 0; i < doc.getParagraphs().size(); ++i) {
            XWPFParagraph paragraph = doc.getParagraphs().get(i);
            //paragraph.getText().replaceAll(findText, replaceText);
            for(int j=0; j<paragraph.getRuns().size(); j++){
                XWPFRun run = paragraph.getRuns().get(j);
                String str = run.text();
                //String beforeStr = paragraph.getRuns().get(j-1).text();
                //String afterStr = paragraph.getRuns().get(j+1).text();
                System.out.println(str);

                /*if(str.contains(findText)){
                    str = str.replaceAll(findText, replaceText);
                    run.setText(str, 0);
                }*/
                if((str).contains(findText)){
                    paragraph.removeRun(j-1);
                    paragraph.removeRun(j++);
                    str = str.replaceAll(findText, replaceText);
                    run.setText(str, 0);
                }

            }
        }
        return doc;


    }

    public void saveDocument(HWPFDocument doc, String file)  {
        try (FileOutputStream out = new FileOutputStream(file)) {
            doc.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
