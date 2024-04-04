package edu.emu.netmonitoring.controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardController implements Initializable {

    @FXML
    private Button btnExist;
    @FXML
    private Button menuBars;
    @FXML
    private Button menuBarsClose;
    @FXML
    private Pane sideMenu;
    @FXML
    private StackPane panelsContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnExist.setOnMouseClicked(event -> {
            System.exit(0);
        });

        sideMenu.setTranslateX(-230);
        menuBars.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(sideMenu);

            slide.setToX(0);
            slide.play();

            sideMenu.setTranslateX(-230);

            slide.setOnFinished((ActionEvent actionEvent) -> {
                menuBars.setVisible(false);
                menuBarsClose.setVisible(true);
            });
        });
        menuBarsClose.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(sideMenu);

            slide.setToX(-230);
            slide.play();

            sideMenu.setTranslateX(0);

            slide.setOnFinished((ActionEvent actionEvent) -> {
                menuBars.setVisible(true);
                menuBarsClose.setVisible(false);
            });
        });

        try {
//            Parent fxml = FXMLLoader.load(getClass().getResource("/edu/emu/netmonitoring/fxml/home.fxml"));
            Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/emu/netmonitoring/fxml/home.fxml")));
            this.panelsContainer.getChildren().removeAll();
            this.panelsContainer.getChildren().setAll(fxml);


        } catch (IOException ex) {
            Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void homePanel(ActionEvent actionEvent) throws IOException{
        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/emu/netmonitoring/fxml/home.fxml")));
        panelsContainer.getChildren().removeAll();
        panelsContainer.getChildren().setAll(fxml);
    }
    public void page2Panel(ActionEvent actionEvent) throws IOException{
        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/emu/netmonitoring/fxml/Panel2.fxml")));
        panelsContainer.getChildren().removeAll();
        panelsContainer.getChildren().setAll(fxml);
    }
    public void page3Panel(ActionEvent actionEvent) throws IOException{
        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/emu/netmonitoring/fxml/Panel3.fxml")));
        panelsContainer.getChildren().removeAll();
        panelsContainer.getChildren().setAll(fxml);
    }
    public void ExportPanel(ActionEvent actionEvent) throws IOException{
        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/emu/netmonitoring/fxml/Export.fxml")));
        panelsContainer.getChildren().removeAll();
        panelsContainer.getChildren().setAll(fxml);
    }
}
