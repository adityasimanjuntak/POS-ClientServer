/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nob.model;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.Date;

/**
 *
 * @author alimk
 */
public class Entity_Simulation implements Serializable {

    private String sim_id, sim_added_by, sim_item_name;
    private int sim_item_price, sim_grand_total;
    private Date sim_date;

    /**
     * @return the sim_id
     */
    public String getSim_id() {
        return sim_id;
    }

    /**
     * @param sim_id the sim_id to set
     */
    public void setSim_id(String sim_id) {
        this.sim_id = sim_id;
    }

    /**
     * @return the sim_added_by
     */
    public String getSim_added_by() {
        return sim_added_by;
    }

    /**
     * @param sim_added_by the sim_added_by to set
     */
    public void setSim_added_by(String sim_added_by) {
        this.sim_added_by = sim_added_by;
    }

    /**
     * @return the sim_item_name
     */
    public String getSim_item_name() {
        return sim_item_name;
    }

    /**
     * @param sim_item_name the sim_item_name to set
     */
    public void setSim_item_name(String sim_item_name) {
        this.sim_item_name = sim_item_name;
    }

    /**
     * @return the sim_item_price
     */
    public int getSim_item_price() {
        return sim_item_price;
    }

    /**
     * @param sim_item_price the sim_item_price to set
     */
    public void setSim_item_price(int sim_item_price) {
        this.sim_item_price = sim_item_price;
    }

    /**
     * @return the sim_grand_total
     */
    public int getSim_grand_total() {
        return sim_grand_total;
    }

    /**
     * @param sim_grand_total the sim_grand_total to set
     */
    public void setSim_grand_total(int sim_grand_total) {
        this.sim_grand_total = sim_grand_total;
    }

    /**
     * @return the sim_date
     */
    public Date getSim_date() {
        return sim_date;
    }

    /**
     * @param sim_date the sim_date to set
     */
    public void setSim_date(Date sim_date) {
        this.sim_date = sim_date;
    }

}
