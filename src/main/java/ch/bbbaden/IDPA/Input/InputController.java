/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbbaden.IDPA.Input;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Alexander
 */
public class InputController implements ChangeListener, Initializable {

    private Stage stage;
    private int anschaff;
    private int prozentzahl;
    private int restbetragzahl;
    private int nutzungsjahre;
    private Model model = Model.getInstance();

    @FXML
    private Spinner<Integer> spinnerjahre;
    @FXML
    private JFXRadioButton radiodegressiv;
    @FXML
    private JFXRadioButton radiolinear;
    @FXML
    private Label lblprozentrestbetrag;
    @FXML
    private JFXTextField prozent;
    @FXML
    private JFXRadioButton direkt;
    @FXML
    private JFXRadioButton indirekt;
    @FXML
    private JFXTextField restbetrag;
    @FXML
    private ToggleGroup radioabschreibungsart;
    @FXML
    private ToggleGroup radiobuchungsart;
    @FXML
    private JFXButton butanzeigen;
    @FXML
    private JFXTextField lblacquisitionValue;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.spinnerjahre.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100));
        radiodegressiv.setSelected(true);
        direkt.setSelected(true);
        radiobuttongroup();
    }

    /**
     *
     * @param event neue Stage
     * @throws IOException Stage loader Exception
     */
    @FXML
    private void abschreibunganzeigen(ActionEvent event) throws IOException {
        if (datenuebertragung() == false) {

            JOptionPane.showMessageDialog(null, "Achten Sie auf folgende Fehlereingaben:\n"
                    + "\n1. Buchstaben oder andere Zeichen wurden eingegeben"
                    + "\n2. Negative Zahlen oder Null wurden eingegeben"
                    + "\n3. Prozentzahl darf nicht grösser oder gleich 100 sein"
                    + "\n4. Restbetrag ist grösser als Anschaffungswert", "Warnung", JOptionPane.ERROR_MESSAGE);


        } else {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/Output.fxml"));
            loader.load();

            Parent root = loader.getRoot();
            stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ergebnisse");
            stage.setResizable(false);
            stage.show();
        }

    }

    /**
     *
     * @return ob Datenübertragung funktioniert hat
     */
    private boolean datenuebertragung() {
        if (fehlerEingabeRestbetrag() == true && fehlerEingabeAnschaffungswert() == true) {
            char lod = 0;
            char doid = 0;
            doid = this.indirekt.isSelected() ? 'i' : 'd';
            lod = this.radiolinear.isSelected() ? 'l' : 'd';

            model.setAnschaff(anschaff);
            model.setJahre(nutzungsjahre);
            model.setLoid(doid);
            model.setLod(lod);
            model.setProzentzahl(prozentzahl);
            model.setRestbetragzahl(restbetragzahl);
            return true;
        } else {
            return false;
        }

    }

    /**
     *
     * @return ob Eingabe des Restbetrags gültig ist
     */
    private boolean fehlerEingabeRestbetrag() {
        if (radiolinear.isSelected()) {
            try {
                anschaff = Integer.parseInt(this.lblacquisitionValue.getText());
                restbetragzahl = Integer.parseInt(this.restbetrag.getText());
                if (restbetragzahl < 0 || restbetragzahl >= anschaff) {
                    return false;
                }
                nutzungsjahre = spinnerjahre.getValue();
                return true;
            } catch (NumberFormatException e) {
                lblacquisitionValue.clear();
                prozent.clear();
                restbetrag.clear();

                return false;
            }
        } else {
            return fehlerEingabeProzent();
        }
    }

    /**
     *
     * @return ob Eingabe des Prozentwerts gültig ist
     */
    private boolean fehlerEingabeProzent() {
        try {
            prozentzahl = Integer.parseInt(this.prozent.getText());
            if (prozentzahl < 0 || prozentzahl >= 100) {
                return false;
            }
            nutzungsjahre = spinnerjahre.getValue();
            return true;
        } catch (NumberFormatException e) {
            lblacquisitionValue.clear();
            prozent.clear();
            restbetrag.clear();
            return false;
        }

    }

    /**
     *
     * @return ob Eingabe des Anschaffungswerts gültig ist
     */
    private boolean fehlerEingabeAnschaffungswert() {
        int bet;
        try {
            bet = Integer.parseInt(this.lblacquisitionValue.getText());
            anschaff = bet;

            return bet == (int) bet && bet >= 1;

        } catch (NumberFormatException e) {
            lblacquisitionValue.clear();
            prozent.clear();
            restbetrag.clear();
            return false;
        }
    }

    private void radiobuttongroup() {
        radioabschreibungsart.selectedToggleProperty().addListener((obserableValue, old_toggle, new_toggle) -> {
            if (radioabschreibungsart.getSelectedToggle() != null) {
                String rechnung;
                if (radiodegressiv.isSelected()) {
                    lblprozentrestbetrag.setText("Prozent: ");
                    restbetrag.setVisible(false);
                    prozent.setVisible(true);
                    rechnung = radiodegressiv.getText();

                } else {
                    lblprozentrestbetrag.setText("Restbetrag: ");
                    prozent.setVisible(false);
                    restbetrag.setVisible(true);
                    rechnung = radiolinear.getText();
                }
            }
        });

    }


    @Override
    public void stateChanged(ChangeEvent e) {
        
    }

}
