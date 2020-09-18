/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbbaden.IDPA.Ausgabe;

import ch.bbbaden.IDPA.Eingabe.Model;
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
public class AusgabeController implements Initializable {

    private Model model = Model.getInstance();
    private Stage stage;
    private String abschreibungsart = "";
    private String text = "";
    private String buchungsart = "";

    @FXML
    private Label anschaffungswert;
    @FXML
    private Label prozent;
    @FXML
    private Label Nutzungsjahr;
    @FXML
    private Label Abschreibungsart;
    @FXML
    private Label Buchungsart;
    @FXML
    private Label Restbetrag;
    @FXML
    private JFXButton butanzeigen;
    @FXML
    private JFXButton butanzeigen1;
    @FXML
    private JFXListView<String> Buchungssatz;

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
        abschreibungsart = model.getLod() == 'l' ? "Linear" : "Degressiv";
        text = model.getLod() == 'l' ? model.getRestbetragzahl()+"" : model.round('d')+"";
        buchungsart = model.getLoid() == 'i' ? "Indrekt" : "Direkt";
        anschaffungswert.setText("" + model.getAnschaff());
        prozent.setText("" + model.getProzentzahl());
        Nutzungsjahr.setText("" + model.getJahre());
        Abschreibungsart.setText(abschreibungsart);
        Buchungsart.setText(buchungsart);
        Restbetrag.setText("" + text);
        for (int i = 0; i < model.getJahre(); i++) {
            Buchungssatz.getItems().add(model.getBuchungssaetze(i));
        }

    }

    @FXML
    private void nochmal(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ch/bbbaden/IDPA/Eingabe/Eingabe.fxml"));
        loader.load();

        Parent root = loader.getRoot();
        stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Ergebnisse");
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void schliessen(ActionEvent event) {
        System.exit(0);
    }
}
