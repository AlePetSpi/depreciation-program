/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbbaden.IDPA.Eingabe;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alexander
 */
public class Model {

    private final String[] Konto = {"Anlagekonto", "Bank", "Abschreibung", "WB Anlagen"};
    private String Buchungsatzl;
    private String Buchungsatzd;
    private String Buchungssaetzel = "";
    private String Buchungssaetzed = "";
    private double abschreibungsbetragl;
    private List<Double> abschreibungsbetragd = new ArrayList<>();
    private int anschaff;
    private int prozentzahl;
    private int restbetragzahl;
    private double ergebnislinear;
    private double degressiver_restbetrag = 0.0;
    private String Buchungssaetze = "";
    private int jahre;
    private char loid;
    private char lod;

    // Eine (versteckte) Klassenvariable vom Typ der eigenen Klasse
    // static variable single_instance of type Singleton 
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

    public char getLoid() {
        return loid;
    }

    public void setLoid(char loid) {
        this.loid = loid;
    }

    public char getLod() {
        return lod;
    }

    public void setLod(char lod) {
        this.lod = lod;
    }

    public int getAnschaff() {
        return anschaff;
    }

    public void setAnschaff(int anschaff) {
        this.anschaff = anschaff;
    }

    public int getProzentzahl() {
        return prozentzahl;
    }

    public void setProzentzahl(int prozentzahl) {
        this.prozentzahl = prozentzahl;
    }

    public int getRestbetragzahl() {
        return restbetragzahl;
    }

    public void setRestbetragzahl(int restbetragzahl) {
        this.restbetragzahl = restbetragzahl;
    }

    public int getJahre() {
        return jahre;
    }

    public void setJahre(int jahre) {
        this.jahre = jahre;
    }

    /**
     *
     * @param i Anzahl Buchungssätze
     * @return Buchungsätze
     */
    public String getBuchungssaetze(int i) {
        switch (lod) {
            case 'l':
                Buchungssaetze = getBuchungssaetzel(i);
                break;
            case 'd':
                Buchungssaetze = getBuchungssaetzed(i);
                break;
            default:
                System.out.println("Fehler bei Buchungssaetze");
        }
        return Buchungssaetze;

    }

    /**
     *
     * @param i Anzahl Buchungssätze
     * @return Buchungssatz degressiv
     */
    private String getBuchungssaetzed(int i) {
        if (loid == 'd') {
            Buchungssaetzed = i + 1 + ". " + Konto[2] + " / " + Konto[0] + " " + abschreibungsbetragd.get(i);
        } else {
//            Buchungssaetzed = i+1 + ". " + Konto[0] + "||" + Konto[1] + " " + anschaff;
            Buchungssaetzed = i + 1 + ". " + Konto[2] + " / " + Konto[3] + " " + abschreibungsbetragd.get(i);
        }

        return Buchungssaetzed;
    }

    /**
     *
     * @param i Anzahl Buchungssätze
     * @return Buchungssatz linear
     */
    private String getBuchungssaetzel(int i) {
        if (loid == 'd') {
            Buchungssaetzel = i + 1 + ". " + Konto[2] + " / " + Konto[0] + " " + round('l');
        } else {
//            Buchungssaetzel = i+1 + ". " + Konto[0] + "||" + Konto[1] + " " + anschaff;
            Buchungssaetzel = i + 1 + ". " + Konto[2] + " / " + Konto[3] + " " + round('l');
        }

        return Buchungssaetzel;
    }

    /**
     *
     * @return degressiver Restbetrag
     */
    private double getDegressiver_restbetrag() {
        BigDecimal bd = null;
        double temp = anschaff;
        double tempbetrag;
        int count = 1;
        do {
            tempbetrag = temp;
            temp = temp - ((temp / 100) * prozentzahl);
            bd = BigDecimal.valueOf(tempbetrag - temp);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            abschreibungsbetragd.add(count - 1, bd.doubleValue());
            if (count >= jahre) {
                degressiver_restbetrag = temp;
                break;
            }
            count++;
        } while (true);

        System.out.println("Prozent-D:" + degressiver_restbetrag);
        return degressiver_restbetrag;
    }

    /**
     *
     * @return Ergebnis linearer Buchführung
     */
    private double Linear() {
        ergebnislinear = (anschaff - restbetragzahl) / jahre;
        return ergebnislinear;
    }

    /**
     *
     * @param lod linear oder degressiv
     * @return gerundetes Ergebniss
     */
    public double round(char lod) {
        BigDecimal bd = null;
        if (2 < 0) {
            throw new IllegalArgumentException();
        }

        switch (lod) {
            case 'l':
                bd = BigDecimal.valueOf(Linear());
                break;
            case 'd':
                bd = BigDecimal.valueOf(getDegressiver_restbetrag());
                break;
            default:
                break;
        }
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
