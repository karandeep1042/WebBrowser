package com.example.browser2;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private TabPane tabPane;

    @FXML
    private WebView webView;

    @FXML
    private TextField textField;

    @FXML
    private WebEngine engine;

    @FXML
    private WebHistory history;

    @FXML
    private Tab initialTab;

    private String homePage;

    private String initialTabName;

    @FXML
    private ProgressBar progress;

    public void checkLastTab(){
        if(tabPane.getTabs().size()==2){
            Platform.exit();
        }
    }

    //Code For Every New tab Added
    public void addNewTab(){
        Tab tab2 = new Tab();
        AnchorPane pane = new AnchorPane();
        Button backbtn = new Button("Back");
        AnchorPane.setLeftAnchor(backbtn,6.0);
        pane.getChildren().add(backbtn);

        Button forwadbtn = new Button("Forwad");
        AnchorPane.setLeftAnchor(forwadbtn,55.0);
        pane.getChildren().add(forwadbtn);

        Button reloadbtn = new Button("Reload");
        AnchorPane.setLeftAnchor(reloadbtn,117.0);
        pane.getChildren().add(reloadbtn);

        TextField searchBar = new TextField();
        searchBar.setPrefWidth(337);
        searchBar.setPrefHeight(26);
        AnchorPane.setLeftAnchor(searchBar,182.0);
        AnchorPane.setRightAnchor(searchBar,81.0);
        pane.getChildren().add(searchBar);

        Button optionsbtn = new Button("Options");
        AnchorPane.setRightAnchor(optionsbtn,14.0);
        pane.getChildren().add(optionsbtn);

        WebView page = new WebView();
        page.setPrefWidth(600);
        page.setPrefHeight(344);
        AnchorPane.setTopAnchor(page,29.0);
        AnchorPane.setLeftAnchor(page,0.0);
        AnchorPane.setRightAnchor(page,0.0);
        AnchorPane.setBottomAnchor(page,0.0);
        pane.getChildren().add(page);

        tab2.setContent(pane);
        tabPane.getTabs().add(tabPane.getTabs().size()-1,tab2);
        tabPane.getSelectionModel().select(tabPane.getTabs().size()-2);
        System.out.println(tabPane.getTabs().size());
        tab2.setOnCloseRequest(event->{
            if(tabPane.getTabs().size()==2){
                Platform.exit();
            }
        });

        WebEngine newTabEngine = page.getEngine();
        newTabEngine.load("https://www.google.com");
        System.out.println("Initial title "+newTabEngine.getTitle());
        WebHistory webHistory = newTabEngine.getHistory();
        System.out.println("Current url = "+newTabEngine.getLocation());

        searchBar.setOnAction(e->{
            if(searchBar.getText().startsWith("https")){
                newTabEngine.load(searchBar.getText());
            }
            else if (searchBar.getText().startsWith("www")){
                newTabEngine.load("https://"+searchBar.getText());
            }
            else{
                newTabEngine.load("https://www.google.com/search?q="+searchBar.getText());
            }
        });

        newTabEngine.locationProperty().addListener((observable,oldValue,newValue)->{
            searchBar.setText(newTabEngine.getLocation());
        });

        newTabEngine.titleProperty().addListener((observable,oldValue,newValue)->{
            tab2.setText(newTabEngine.getTitle());
            System.out.println("Current title "+newTabEngine.getTitle());
        });

        backbtn.setOnAction(e->{
            ObservableList<WebHistory.Entry> entriees = webHistory.getEntries();
            webHistory.go(-1);
            if(webHistory.getCurrentIndex()==0){
                tab2.setText("Google");
                searchBar.setText("https://www.google.com");
            }
            else {
                tab2.setText(entriees.get(webHistory.getCurrentIndex()).getTitle());
                searchBar.setText(entriees.get(webHistory.getCurrentIndex()).getUrl());
            }
        });

        forwadbtn.setOnAction(e->{
            ObservableList<WebHistory.Entry> entriees = webHistory.getEntries();
            webHistory.go(1);
            tab2.setText(entriees.get(webHistory.getCurrentIndex()).getTitle());
            searchBar.setText(entriees.get(webHistory.getCurrentIndex()).getUrl());
        });

        reloadbtn.setOnAction(e->{
            newTabEngine.reload();
        });
    }

    //Code For The initial tab
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        engine = webView.getEngine();
        homePage = "www.google.com";
        textField.setText(homePage);
        loadPage();
    }

    public void loadPage(){
        if(textField.getText().startsWith("https")){
            engine.load(textField.getText());
        }
        else if (textField.getText().startsWith("www")){
            engine.load("https://"+textField.getText());
        }
        else{
            engine.load("https://www.google.com/search?q="+textField.getText());
        }
        progress.progressProperty().bind(engine.getLoadWorker().progressProperty());
        engine.locationProperty().addListener((observable,oldValue,newValue)->{
            textField.setText(engine.getLocation());
        });
        engine.titleProperty().addListener((observable,oldValue,newValue)->{
            initialTab.setText(engine.getTitle());
        });
    }

    public void reloadPage(){
        engine.reload();
    }

    public void backPage(){
        history = webView.getEngine().getHistory();
        ObservableList<WebHistory.Entry> entries = history.getEntries();
        history.go(-1);
        if(history.getCurrentIndex()==0){
            initialTab.setText("Google");
            textField.setText("https://www.google.com");
        }
        else {
            initialTab.setText(entries.get(history.getCurrentIndex()).getTitle());
            textField.setText(entries.get(history.getCurrentIndex()).getUrl());
        }
    }

    public void nextPage(){
        history = webView.getEngine().getHistory();
        ObservableList<WebHistory.Entry> entries = history.getEntries();
        history.go(1);
        initialTab.setText(entries.get(history.getCurrentIndex()).getTitle());
        textField.setText(entries.get(history.getCurrentIndex()).getUrl());
    }
}