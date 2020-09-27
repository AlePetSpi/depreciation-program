/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbbaden.IDPA.Output;

import ch.bbbaden.IDPA.Input.Model;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Alexander
 */
public class OutputController implements Initializable {

    private Model model = Model.getInstance();
    private Stage stage;
    private String depreciationType = "";
    private String text = "";
    private String bookingType = "";

    @FXML
    private Label lblacquisitionValue;
    @FXML
    private Label lblpercent;
    @FXML
    private Label lblusefulLife;
    @FXML
    private Label lbldepreciationType;
    @FXML
    private Label lblbookingType;
    @FXML
    private Label lblremainingAmount;
    @FXML
    private JFXButton butagain;
    @FXML
    private JFXButton butclose;
    @FXML
    private JFXListView<String> lvbookingRates;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        init();
        // TODO
    }

    /**
     * Zeigt Werte an
     */
    private void init() {
        depreciationType = model.getLinareOrDegressive() == 'l' ? "Linear" : "Degressiv";
        text = model.getLinareOrDegressive() == 'l' ? model.getRemainingValue()+"" : model.getRound('d')+"";
        bookingType = model.getDirectlyOrIndirectly()== 'i' ? "Indrekt" : "Direkt";
        lblacquisitionValue.setText("" + model.getAcquisition());
        lblpercent.setText("" + model.getPercentNumber());
        lblusefulLife.setText("" + model.getYears());
        lbldepreciationType.setText(depreciationType);
        lblbookingType.setText(bookingType);
        lblremainingAmount.setText("" + text);
        for (int i = 0; i < model.getYears(); i++) {
            lvbookingRates.getItems().add(model.getBuchungssaetze(i));
        }

    }

    @FXML
    private void again(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Input.fxml"));
        loader.load();

        Parent root = loader.getRoot();
        stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Input");
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void close(ActionEvent event) {
        System.exit(0);
    }
}
