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
    private int acquisition;
    private int percentNumber;
    private int remainingValue;
    private int usefulLife;
    private Model model = Model.getInstance();

    @FXML
    private JFXTextField lblAcquisitionValue;
    @FXML
    private JFXRadioButton radDegressive;
    @FXML
    private ToggleGroup radDepreciationType;
    @FXML
    private JFXRadioButton radLinear;
    @FXML
    private JFXTextField lblPercent;
    @FXML
    private JFXRadioButton radDirectly;
    @FXML
    private ToggleGroup radBookingType;
    @FXML
    private JFXRadioButton radIndirectly;
    @FXML
    private JFXButton butshow;
    @FXML
    private JFXTextField lblRemainingAmount;
    @FXML
    private Spinner<Integer> spinyears;
    @FXML
    private Label lblPercentAndRemainingAmount;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.spinyears.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100));
        radDegressive.setSelected(true);
        radDirectly.setSelected(true);
        radiobuttongroup();
    }

    /**
     *
     * @param event neue Stage
     * @throws IOException Stage loader Exception
     */
    @FXML
    private void showDepreciation(ActionEvent event) throws IOException {
        if (isDataTransmission() == false) {
            JOptionPane.showMessageDialog(null, "Requirements for a successful entry:\n"
                    + "\n1. It cannot contain any letters or words"
                    + "\n2. Negative numbers or zero are not allowed"
                    + "\n3. Percent must not be greater than or equal to 100"
                    + "\n4. The remaining amount must not be greater than the purchase price", "warning", JOptionPane.ERROR_MESSAGE);
        }else {            
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/Output.fxml"));
            loader.load();

            Parent root = loader.getRoot();
            stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Output");
            stage.setResizable(false);
            stage.show();
        }

    }

    /**
     *
     * @return ob Datenübertragung funktioniert hat
     */
    private boolean isDataTransmission() {
        if (isErrorInputbyRemainingAmount() == true && isErrorInputbyAcquisitionValue() == true) {
        char linareOrDegressive;
        char directlyOrIndirectly;
        directlyOrIndirectly = this.radIndirectly.isSelected() ? 'i' : 'd';
        linareOrDegressive = this.radLinear.isSelected() ? 'l' : 'd';

        model.setAcquisition(acquisition);
        model.setYears(usefulLife);
        model.setDirectlyOrIndirectly(directlyOrIndirectly);
        model.setLinareOrDegressive(linareOrDegressive);
        model.setPercentNumber(percentNumber);
        model.setRemainingValue(remainingValue);
        return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @return ob Eingabe des Restbetrags gültig ist
     */
    private boolean isErrorInputbyRemainingAmount() {
        if (radLinear.isSelected()) {
            try {
                acquisition = Integer.parseInt(this.lblAcquisitionValue.getText());
                remainingValue = Integer.parseInt(this.lblRemainingAmount.getText());
                if (remainingValue < 0 || remainingValue >= acquisition) {
                    return false;
                }
                usefulLife = spinyears.getValue();
                return true;
            } catch (NumberFormatException e) {
                lblAcquisitionValue.clear();
                lblPercent.clear();
                lblRemainingAmount.clear();

                return false;
            }
        } else {
            return isErrorInputbyPrecent();
        }
    }

    /**
     *
     * @return ob Eingabe des lblPercentwerts gültig ist
     */
    private boolean isErrorInputbyPrecent() {
        try {
            percentNumber = Integer.parseInt(this.lblPercent.getText());
            if (percentNumber < 0 || percentNumber >= 100) {
                return false;
            }
            usefulLife = spinyears.getValue();
            return true;
        } catch (NumberFormatException e) {
            lblAcquisitionValue.clear();
            lblPercent.clear();
            lblRemainingAmount.clear();
            return false;
        }

    }

    /**
     *
     * @return ob Eingabe des Anschaffungswerts gültig ist
     */
    private boolean isErrorInputbyAcquisitionValue() {
        int bet;
        try {
            bet = Integer.parseInt(this.lblAcquisitionValue.getText());
            acquisition = bet;

            return bet == (int) bet && bet >= 1;

        } catch (NumberFormatException e) {
            lblAcquisitionValue.clear();
            lblPercent.clear();
            lblRemainingAmount.clear();
            return false;
        }
    }

    private void radiobuttongroup() {
        radDepreciationType.selectedToggleProperty().addListener((obserableValue, old_toggle, new_toggle) -> {
            if (radDepreciationType.getSelectedToggle() != null) {
                String rechnung;
                if (radDegressive.isSelected()) {
                    lblPercentAndRemainingAmount.setText("Percent *: ");
                    lblRemainingAmount.setVisible(false);
                    lblPercent.setVisible(true);
                    rechnung = radDegressive.getText();

                } else {
                    lblPercentAndRemainingAmount.setText("Remaining amount *: ");
                    lblPercent.setVisible(false);
                    lblRemainingAmount.setVisible(true);
                    rechnung = radLinear.getText();
                }
            }
        });

    }

    @Override
    public void stateChanged(ChangeEvent e) {

    }

}
