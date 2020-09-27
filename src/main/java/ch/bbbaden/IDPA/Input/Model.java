/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbbaden.IDPA.Input;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alexander
 */
public class Model {

    private final String[] accounts = {"investment account", "bank", "depreciation", "allowance account investments"};
    private String bookingRatesLinear = "";
    private String bookingRatesDegressive = "";
    private List<Double> depreciationAmountDegressive = new ArrayList<>();
    private int acquisition;
    private int percentNumber;
    private int remainingValue;
    private double outputlinear;
    private double degressiveRemainingValue = 0.0;
    private String bookingRates = "";
    private int years;
    private char directlyOrIndirectly;
    private char linareOrDegressive;

    // A (hidden) class variable of the type of its own class
    // static variable single_instance of the singleton type
    private static Model single_instance = null;

    // private constructor restricted to this class itself 
    private Model() {

    }

    // static method to create instance of Singleton class 
    public static Model getInstance() {
        if (single_instance == null) {
            single_instance = new Model();
        }

        return single_instance;
    }

    public String getBookingRatesLinear() {
        return bookingRatesLinear;
    }

    public String getBookingRatesDegressive() {
        return bookingRatesDegressive;
    }

    public List<Double> getDepreciationAmountDegressive() {
        return depreciationAmountDegressive;
    }

    public int getAcquisition() {
        return acquisition;
    }

    public void setAcquisition(int acquisition) {
        this.acquisition = acquisition;
    }

    public int getPercentNumber() {
        return percentNumber;
    }

    public void setPercentNumber(int percentNumber) {
        this.percentNumber = percentNumber;
    }

    public int getRemainingValue() {
        return remainingValue;
    }

    public void setRemainingValue(int remainingValue) {
        this.remainingValue = remainingValue;
    }

    public String getBookingRates() {
        return bookingRates;
    }

    public void setBookingRates(String bookingRates) {
        this.bookingRates = bookingRates;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public char getDirectlyOrIndirectly() {
        return directlyOrIndirectly;
    }

    public void setDirectlyOrIndirectly(char directlyOrIndirectly) {
        this.directlyOrIndirectly = directlyOrIndirectly;
    }

    public char getLinareOrDegressive() {
        return linareOrDegressive;
    }

    public void setLinareOrDegressive(char linareOrDegressive) {
        this.linareOrDegressive = linareOrDegressive;
    }

    public static Model getSingle_instance() {
        return single_instance;
    }

    public static void setSingle_instance(Model single_instance) {
        Model.single_instance = single_instance;
    }

    /**
     *
     * @return result of linear accounting
     */
    private double getOutputlinear() {
        outputlinear = (acquisition - remainingValue) / years;
        return outputlinear;
    }

    /**
     *
     * @return degressive Remaining Amount
     */
    private double getDegressiveRemainingValue() {
        BigDecimal bd = null;
        double temp = acquisition;
        double tempbetrag;
        int count = 1;
        do {
            tempbetrag = temp;
            temp = temp - ((temp / 100) * percentNumber);
            bd = BigDecimal.valueOf(tempbetrag - temp);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            depreciationAmountDegressive.add(count - 1, bd.doubleValue());
            if (count >= years) {
                degressiveRemainingValue = temp;
                break;
            }
            count++;
        } while (true);

        System.out.println("Prozent-D:" + degressiveRemainingValue);
        return degressiveRemainingValue;
    }

    /**
     *
     * @param i number of booking records
     * @return booking records
     */
    public String getBuchungssaetze(int i) {
        switch (linareOrDegressive) {
            case 'l':
                bookingRates = getBookingRatesLinear(i);
                break;
            case 'd':
                bookingRates = getBookingRatesDegressive(i);
                break;
            default:
                System.out.println("Error to the booking rates");
        }
        return bookingRates;

    }

    /**
     *
     * @param i Number of booking rates
     * @return Booking rates degressive
     */
    private String getBookingRatesDegressive(int i) {
        if (directlyOrIndirectly == 'd') {
            bookingRatesDegressive = i + 1 + ". " + accounts[2] + " / " + accounts[0] + " " + depreciationAmountDegressive.get(i);
        } else {
            bookingRatesDegressive = i + 1 + ". " + accounts[2] + " / " + accounts[3] + " " + depreciationAmountDegressive.get(i);
        }

        return bookingRatesDegressive;
    }

    /**
     *
     * @param i Anzahl Buchungssätze
     * @return Buchungssatz linear
     */
    private String getBookingRatesLinear(int i) {
        if (directlyOrIndirectly == 'd') {
            bookingRatesLinear = i + 1 + ". " + accounts[2] + " / " + accounts[0] + " " + getRound('l');
        } else {
            bookingRatesLinear = i + 1 + ". " + accounts[2] + " / " + accounts[3] + " " + getRound('l');
        }

        return bookingRatesLinear;
    }

    /**
     *
     * @param lod linear or degressive
     * @return rounded result
     */
    public double getRound(char lod) {
        BigDecimal bd = null;
        if (2 < 0) {
            throw new IllegalArgumentException();
        }

        switch (lod) {
            case 'l':
                bd = BigDecimal.valueOf(getOutputlinear());
                break;
            case 'd':
                bd = BigDecimal.valueOf(getDegressiveRemainingValue());
                break;
            default:
                break;
        }
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
