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
public class Entity_ErrorReport implements Serializable {

    private String error_id, error_title, error_desc, error_pic, error_status;
    private Date error_date;
    private String error_added_by;

    /**
     * @return the error_id
     */
    public String getError_id() {
        return error_id;
    }

    /**
     * @param error_id the error_id to set
     */
    public void setError_id(String error_id) {
        this.error_id = error_id;
    }

    /**
     * @return the error_title
     */
    public String getError_title() {
        return error_title;
    }

    /**
     * @param error_title the error_title to set
     */
    public void setError_title(String error_title) {
        this.error_title = error_title;
    }

    /**
     * @return the error_desc
     */
    public String getError_desc() {
        return error_desc;
    }

    /**
     * @param error_desc the error_desc to set
     */
    public void setError_desc(String error_desc) {
        this.error_desc = error_desc;
    }

    /**
     * @return the error_pic
     */
    public String getError_pic() {
        return error_pic;
    }

    /**
     * @param error_pic the error_pic to set
     */
    public void setError_pic(String error_pic) {
        this.error_pic = error_pic;
    }

    /**
     * @return the error_status
     */
    public String getError_status() {
        return error_status;
    }

    /**
     * @param error_status the error_status to set
     */
    public void setError_status(String error_status) {
        this.error_status = error_status;
    }

    /**
     * @return the error_date
     */
    public Date getError_date() {
        return error_date;
    }

    /**
     * @param error_date the error_date to set
     */
    public void setError_date(Date error_date) {
        this.error_date = error_date;
    }

    /**
     * @return the error_added_by
     */
    public String getError_added_by() {
        return error_added_by;
    }

    /**
     * @param error_added_by the error_added_by to set
     */
    public void setError_added_by(String error_added_by) {
        this.error_added_by = error_added_by;
    }

}
