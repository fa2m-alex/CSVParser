package com.test.controllers;

import com.test.App;
import com.test.impl.FileReplacerDoc;
import com.test.impl.FileReplacerTxt;
import com.test.interfaces.FileReplacer;
import com.test.services.CSVReader;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.hwpf.usermodel.Table;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Alex on 26.02.2016.
 */
public class MainController {

    @FXML
    private TableView<Integer> headerTable = new TableView<>();;

    @FXML
    private Label txtLabel = new Label();

    @FXML
    private Button selectAllBut;

    @FXML
    private TextField searchField;

    // Reference to the main application
    private App mainApp;

    private CSVReader csvReader;
    private File csvFile;
    private File templateFile;

    private List currentRecords;

    public void setMainApp(App mainApp) {
        this.mainApp = mainApp;
    }


    @FXML
    private void setTable(List records) throws ClassNotFoundException {
        headerTable.setEditable(true);
        headerTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        for (int i = 0; i < records.size(); i++) {
            headerTable.getItems().add(i);
        }

        for(int i=0; i<csvReader.getHeader().size(); i++){
            TableColumn<Integer, String> column = new TableColumn<>(csvReader.getHeader().get(i));
            column.setSortable(false);
            List list = csvReader.getVerticalRecords(i, records);
            column.setCellValueFactory(cellData -> {
                Integer rowIndex = cellData.getValue();
                return new ReadOnlyStringWrapper((String) list.get(rowIndex));
            });

            headerTable.getColumns().add(column);
        }

        if(selectAllBut.isDisable())
            selectAllBut.setDisable(false);
    }

    @FXML
    private void importCsv() throws ClassNotFoundException {
        FileChooser fileChooser = new FileChooser();
        // Set extension filter
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV files", "*.csv"));

        File temp = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if(temp!=null){
            csvFile = temp;
            csvReader = new CSVReader(csvFile);
            clearTable();
            setTable(csvReader.getRecords());
            currentRecords = csvReader.getRecords();
        }
    }

    @FXML
    private void importTemplate(){
        FileChooser fileChooser = new FileChooser();
        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt), (*.doc)", "*.txt", "*.doc");
        fileChooser.getExtensionFilters().add(extFilter);

        File temp = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if(temp!=null){
            templateFile = temp;
            txtLabel.setText("Imported file: " + templateFile.getName());
        }
    }


    @FXML
    private void saveFile(){
        if(templateFile != null && csvFile != null){

            FileReplacer fileReplacer = null;

            if(templateFile.getName().contains(".txt")) {
                fileReplacer = new FileReplacerTxt(templateFile);
            }
            else if(templateFile.getName().contains(".doc")){
                fileReplacer = new FileReplacerDoc(templateFile);
            }

            int tableIndex = headerTable.getSelectionModel().getSelectedIndex();


            if(tableIndex >= 0){

                DirectoryChooser directoryChooser = new DirectoryChooser();
                File directory = directoryChooser.showDialog(mainApp.getPrimaryStage());;

                for(int i=0; i<headerTable.getSelectionModel().getSelectedIndices().size(); i++){
                    int temp = headerTable.getSelectionModel().getSelectedIndices().get(i);
                    CSVRecord record = (CSVRecord) currentRecords.get(temp);

                    File file = null;

                    if(directory != null) {
                        file = new File(directory.getAbsolutePath() + "/" + i + "-" + templateFile.getName());
                    }

                    if (file != null) {
                        fileReplacer.replaceTags(csvReader.getHeader(), record, file);
                    }
                }

                if(directory != null)
                    showMessage("Done", "Done", "Done");
            }
            else{
                showAlert("No Selection", "No Row Selected", "Please select a row in the table.");
            }
        }
        else{
            showAlert("No files", "No files", "Please import files.");
        }
    }

    @FXML
    private void searchRecords(){
        if(csvFile != null){
            clearTable();
            try {
                setTable(csvReader.searchRecords(searchField.getText()));
                currentRecords = csvReader.searchRecords(searchField.getText());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            showMessage("", "", "");
        }
    }


    private void showAlert(String title, String headerText, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(content);

        alert.showAndWait();
    }

    private void showMessage(String title, String headerText, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(content);

        alert.showAndWait();
    }

    private void clearTable(){
        headerTable.getColumns().remove(0, headerTable.getColumns().size());
        headerTable.getItems().remove(0, headerTable.getItems().size());
    }

    @FXML
    private void selectAll(){
        headerTable.getSelectionModel().selectAll();
    }

}
