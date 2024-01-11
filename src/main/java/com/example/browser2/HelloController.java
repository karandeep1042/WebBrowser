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
    private WebHistory history,newTabHistory;

    @FXML
    private Tab initialTab;

    private String homePage;

    private String initialTabName;

    @FXML
    private ProgressBar progress;

    @FXML
    private Tab tab2;

    @FXML
    private AnchorPane pane;

    @FXML
    private Button backbtn;

    @FXML
    private Button forwadbtn;

    @FXML
    private Button reloadbtn;

    @FXML
    private TextField searchBar;

    @FXML
    private Button optionsbtn;

    @FXML
    private WebView page;

    private WebEngine newTabEngine;

    public void checkLastTab(){
        if(tabPane.getTabs().size()==2){
            Platform.exit();
        }
    }

    //Code For Every New tab Added
    public void addNewTab(){
        tab2 = new Tab("");
        pane = new AnchorPane();
        backbtn = new Button("Back");
        AnchorPane.setLeftAnchor(backbtn,6.0);
        pane.getChildren().add(backbtn);
        backbtn.setOnAction(e->{
            backPageForNewtab();
        });

        forwadbtn = new Button("Forwad");
        AnchorPane.setLeftAnchor(forwadbtn,55.0);
        pane.getChildren().add(forwadbtn);
        forwadbtn.setOnAction(e->{
            forwadPageForNewtab();
        });

        reloadbtn = new Button("Reload");
        AnchorPane.setLeftAnchor(reloadbtn,117.0);
        pane.getChildren().add(reloadbtn);
        forwadbtn.setOnAction(e->{
            reloadPageForNewtab();
        });

        searchBar = new TextField();
        searchBar.setPrefWidth(337);
        searchBar.setPrefHeight(26);
        AnchorPane.setLeftAnchor(searchBar,182.0);
        AnchorPane.setRightAnchor(searchBar,81.0);
        pane.getChildren().add(searchBar);
        searchBar.setOnAction(e->{
            loadNewTabPage();
        });

        optionsbtn = new Button("Options");
        AnchorPane.setRightAnchor(optionsbtn,14.0);
        pane.getChildren().add(optionsbtn);
        //not defined yet
        optionsbtn.setOnAction(e->{
            optionsbtnForNewtab();
        });

        page = new WebView();
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
        initializeNewTab();
    }

    private void initializeNewTab() {
//        newTabEngine = new WebEngine();
        newTabEngine = page.getEngine();
        String newTabHomePage = "www.google.com";
        String newTabTitle = "Google";
        searchBar.setText(newTabHomePage);
        tab2.setText(newTabTitle);
        loadNewTabPage();
    }

    private void loadNewTabPage() {
        if(searchBar.getText().startsWith("https")){
            newTabEngine.load(searchBar.getText());
        }
        else if (searchBar.getText().startsWith("www")){
            newTabEngine.load("https://"+searchBar.getText());
        }
        else{
            newTabEngine.load("https://www.google.com/search?q="+searchBar.getText());
        }
//        progress.progressProperty().bind(newTabEngine.getLoadWorker().progressProperty());
    }

    private void optionsbtnForNewtab() {

    }

    private void reloadPageForNewtab() {
        newTabEngine.reload();
    }

    private void forwadPageForNewtab() {
        newTabHistory = page.getEngine().getHistory();
        ObservableList<WebHistory.Entry> entries = newTabHistory.getEntries();
        newTabHistory.go(1);
        System.out.println("Entries for new tab is : "+entries);
        tab2.setText(entries.get(newTabHistory.getCurrentIndex()).getTitle());
        searchBar.setText(entries.get(newTabHistory.getCurrentIndex()).getUrl());
    }

    private void backPageForNewtab() {
        newTabHistory = page.getEngine().getHistory();
        ObservableList<WebHistory.Entry> entries = newTabHistory.getEntries();
        System.out.println("Entries for new tab is : "+entries);
        newTabHistory.go(-1);
        if(newTabHistory.getCurrentIndex()==0){
            tab2.setText("Google");
            searchBar.setText("https://www.google.com");
        }
        else {
            tab2.setText(entries.get(newTabHistory.getCurrentIndex()).getTitle());
            searchBar.setText(entries.get(newTabHistory.getCurrentIndex()).getUrl());
        }
    }

    //Code For The initial tab
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        engine = webView.getEngine();
        homePage = "www.google.com";
        initialTabName = "Google";
        textField.setText(homePage);
        initialTab.setText(initialTabName);
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