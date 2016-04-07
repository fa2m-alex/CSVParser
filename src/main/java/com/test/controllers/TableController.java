package com.test.controllers;

import com.independentsoft.office.word.Paragraph;
import com.independentsoft.office.word.Run;
import com.independentsoft.office.word.StandardBorderStyle;
import com.independentsoft.office.word.tables.*;
import com.independentsoft.office.word.tables.Cell;
import com.test.App;
import com.test.services.CSVReader;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 05.04.2016.
 */
public class TableController {

    @FXML
    private TableView<Integer> table;

    List header;
    List records;
    private CSVReader csvReader;

    private App mainApp;

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setHeaderRecords(List header, List records, CSVReader csvReader){
        this.header = header;
        this.records = records;
        this.csvReader = csvReader;
        setTable(header, records);
    }

    public void setMainApp(App mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize(){

    }

    private void setTable(List<String> header, List records){
        table.setEditable(true);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getSelectionModel().setCellSelectionEnabled(true);

        for (int i = 0; i < records.size(); i++) {
            table.getItems().add(i);
        }

        for(int i=0; i<header.size(); i++){
            TableColumn<Integer, String> column = new TableColumn<>(header.get(i));
            column.setSortable(false);
            List list = csvReader.getVerticalRecords(i, records);
            column.setCellValueFactory(cellData -> {
                Integer rowIndex = cellData.getValue();
                return new ReadOnlyStringWrapper((String) list.get(rowIndex));
            });

            table.getColumns().add(column);
        }

    }

    @FXML
    public Table getTable(){
        //if(table.getSelectionModel().getSelectedCells().size() > 0){
            ArrayList<Paragraph> headerItems = new ArrayList<Paragraph>();
            ObservableList<TablePosition> selectedCells = table.getSelectionModel().getSelectedCells();

            for(int i=0; i<selectedCells.size(); i++){
                if(!headerItems.contains(header.get(selectedCells.get(i).getColumn()))){
                    Paragraph temp = new Paragraph();
                    String text = header.get(selectedCells.get(i).getColumn()).toString();
                    Run run = new Run(text);
                    temp.add(run);
                    headerItems.add(temp);
                }
            }

            ArrayList<Cell> headerCells = new ArrayList<Cell>();

            for(int i=0; i<headerItems.size(); i++){
                Cell cell = new Cell();
                cell.add(headerItems.get(i));
                headerCells.add(cell);
            }

            Row headerRow = new Row();

            for(int i=0; i<headerCells.size(); i++){
                headerRow.add(headerCells.get(i));
            }

            Table table1 = new Table(StandardBorderStyle.SINGLE_LINE);
            table1.setWidth(new Width(TableWidthUnit.PERCENT, 100));

            table1.add(headerRow);

            dialogStage.close();

            if(headerItems.size() > 0)
                return table1;
            else
                return null;

    }

    @FXML
    private void handleCancel(){
        dialogStage.close();
    }



}
