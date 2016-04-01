package com.test.controllers;

import com.test.App;
import com.test.impl.FileReplacerDoc;
import com.test.impl.FileReplacerDocx;
import com.test.impl.FileReplacerTxt;
import com.test.interfaces.FileReplacer;
import com.test.services.CSVReader;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.hslf.record.Record;

import java.io.File;
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
    @FXML
    private TextField coefficientField;
    @FXML
    private CheckBox activateCoef;
    @FXML
    private ChoiceBox<String> headerFields;

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
        //headerTable.getSelectionModel().setCellSelectionEnabled(true);

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
            initializeChoiceBox();
        }
    }

    @FXML
    private void importTemplate(){
        FileChooser fileChooser = new FileChooser();
        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.docx)", "*.docx");
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
            if(!activateCoef.isSelected()){
                replace();
            }
            else{
                if(isNumber(coefficientField.getText())){
                    replace();
                }
            }
        }
        else{
            showAlert("No files", "No files", "Please import files.");
        }
    }

    private void replace(){
        FileReplacer fileReplacer = null;

        if(templateFile.getName().contains(".txt")) {
            fileReplacer = new FileReplacerTxt(templateFile);
        }
        else if(templateFile.getName().contains(".docx")){
            fileReplacer = new FileReplacerDocx(templateFile);
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
                    if(activateCoef.isSelected() && isNumber(coefficientField.getText()))
                        fileReplacer.replaceTagsWithCoef(csvReader.getHeader(), headerFields.getSelectionModel().getSelectedIndex(), Double.parseDouble(coefficientField.getText()), record, file);
                    else
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

    @FXML
    private void activateCoefficient(){
        if(activateCoef.isSelected()){
            coefficientField.setDisable(false);
            headerFields.setDisable(false);
        }
        else{
            coefficientField.setDisable(true);
            headerFields.setDisable(true);
        }

    }

    private void initializeChoiceBox(){
        ObservableList<String> headerList = FXCollections.observableArrayList();

        List list = csvReader.getHeader();

        for(int i=0; i<list.size(); i++){
            headerList.add((String) list.get(i));
        }
        headerFields.setItems(headerList);
        headerFields.getSelectionModel().selectFirst();
    }

    private boolean isNumber(String string) {
        String errorMessage = "";

        if (string == null || string.length() == 0) {
            errorMessage += "No valid coefficient!\n";
        } else {
            // try to parse the postal code into an int.
            try {
                Double.parseDouble(string);
            } catch (NumberFormatException e) {
                errorMessage += "No valid coefficient (must be an integer)!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            showAlert("", "", errorMessage);

            return false;
        }
    }

}
