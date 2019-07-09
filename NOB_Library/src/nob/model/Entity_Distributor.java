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
public class Entity_Distributor implements Serializable {

    private String dis_id;
    private String dis_name, dis_address, dis_phone, dis_email, dis_status;
    private Date dis_last_trans;
    private String dis_desc;
    private String dis_added_by;

    /**
     * @return the dis_id
     */
    public String getDis_id() {
        return dis_id;
    }

    /**
     * @param dis_id the dis_id to set
     */
    public void setDis_id(String dis_id) {
        this.dis_id = dis_id;
    }

    /**
     * @return the dis_name
     */
    public String getDis_name() {
        return dis_name;
    }

    /**
     * @param dis_name the dis_name to set
     */
    public void setDis_name(String dis_name) {
        this.dis_name = dis_name;
    }

    /**
     * @return the dis_address
     */
    public String getDis_address() {
        return dis_address;
    }

    /**
     * @param dis_address the dis_address to set
     */
    public void setDis_address(String dis_address) {
        this.dis_address = dis_address;
    }

    /**
     * @return the dis_phone
     */
    public String getDis_phone() {
        return dis_phone;
    }

    /**
     * @param dis_phone the dis_phone to set
     */
    public void setDis_phone(String dis_phone) {
        this.dis_phone = dis_phone;
    }

    /**
     * @return the dis_email
     */
    public String getDis_email() {
        return dis_email;
    }

    /**
     * @param dis_email the dis_email to set
     */
    public void setDis_email(String dis_email) {
        this.dis_email = dis_email;
    }

    /**
     * @return the dis_status
     */
    public String getDis_status() {
        return dis_status;
    }

    /**
     * @param dis_status the dis_status to set
     */
    public void setDis_status(String dis_status) {
        this.dis_status = dis_status;
    }

    /**
     * @return the dis_last_trans
     */
    public Date getDis_last_trans() {
        return dis_last_trans;
    }

    /**
     * @param dis_last_trans the dis_last_trans to set
     */
    public void setDis_last_trans(Date dis_last_trans) {
        this.dis_last_trans = dis_last_trans;
    }

    /**
     * @return the dis_desc
     */
    public String getDis_desc() {
        return dis_desc;
    }

    /**
     * @param dis_desc the dis_desc to set
     */
    public void setDis_desc(String dis_desc) {
        this.dis_desc = dis_desc;
    }

    /**
     * @return the dis_added_by
     */
    public String getDis_added_by() {
        return dis_added_by;
    }

    /**
     * @param dis_added_by the dis_added_by to set
     */
    public void setDis_added_by(String dis_added_by) {
        this.dis_added_by = dis_added_by;
    }

}
