/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nob.model;

import java.io.Serializable;

/**
 *
 * @author alimk
 */
public class Entity_Customer implements Serializable {

    private String cust_id;
    private String cust_name, cust_address, cust_phone;

    /**
     * @return the cust_id
     */
    public String getCust_id() {
        return cust_id;
    }

    /**
     * @param cust_id the cust_id to set
     */
    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    /**
     * @return the cust_name
     */
    public String getCust_name() {
        return cust_name;
    }

    /**
     * @param cust_name the cust_name to set
     */
    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    /**
     * @return the cust_address
     */
    public String getCust_address() {
        return cust_address;
    }

    /**
     * @param cust_address the cust_address to set
     */
    public void setCust_address(String cust_address) {
        this.cust_address = cust_address;
    }

    /**
     * @return the cust_phone
     */
    public String getCust_phone() {
        return cust_phone;
    }

    /**
     * @param cust_phone the cust_phone to set
     */
    public void setCust_phone(String cust_phone) {
        this.cust_phone = cust_phone;
    }

}
