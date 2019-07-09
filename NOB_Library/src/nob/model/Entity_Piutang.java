/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nob.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author alimk
 */
public class Entity_Piutang implements Serializable {

    private String piutang_id;
    private String piutang_by;
    private String piutang_by_status;
    private String piutang_transaction;
    private Date piutang_date;
    private double piutang_total;

    /**
     * @return the piutang_id
     */
    public String getPiutang_id() {
        return piutang_id;
    }

    /**
     * @param piutang_id the piutang_id to set
     */
    public void setPiutang_id(String piutang_id) {
        this.piutang_id = piutang_id;
    }

    /**
     * @return the piutang_by
     */
    public String getPiutang_by() {
        return piutang_by;
    }

    /**
     * @param piutang_by the piutang_by to set
     */
    public void setPiutang_by(String piutang_by) {
        this.piutang_by = piutang_by;
    }

    /**
     * @return the piutang_by_status
     */
    public String getPiutang_by_status() {
        return piutang_by_status;
    }

    /**
     * @param piutang_by_status the piutang_by_status to set
     */
    public void setPiutang_by_status(String piutang_by_status) {
        this.piutang_by_status = piutang_by_status;
    }

    /**
     * @return the piutang_transaction
     */
    public String getPiutang_transaction() {
        return piutang_transaction;
    }

    /**
     * @param piutang_transaction the piutang_transaction to set
     */
    public void setPiutang_transaction(String piutang_transaction) {
        this.piutang_transaction = piutang_transaction;
    }

    /**
     * @return the piutang_date
     */
    public Date getPiutang_date() {
        return piutang_date;
    }

    /**
     * @param piutang_date the piutang_date to set
     */
    public void setPiutang_date(Date piutang_date) {
        this.piutang_date = piutang_date;
    }

    /**
     * @return the piutang_total
     */
    public double getPiutang_total() {
        return piutang_total;
    }

    /**
     * @param piutang_total the piutang_total to set
     */
    public void setPiutang_total(double piutang_total) {
        this.piutang_total = piutang_total;
    }

}
