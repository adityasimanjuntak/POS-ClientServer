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
public class Entity_Department implements Serializable {

    private String dep_id;
    private String dep_cat;
    private String dep_desc;
    private String dep_status;

    /**
     * @return the dep_id
     */
    public String getDep_id() {
        return dep_id;
    }

    /**
     * @param dep_id the dep_id to set
     */
    public void setDep_id(String dep_id) {
        this.dep_id = dep_id;
    }

    /**
     * @return the dep_cat
     */
    public String getDep_cat() {
        return dep_cat;
    }

    /**
     * @param dep_cat the dep_cat to set
     */
    public void setDep_cat(String dep_cat) {
        this.dep_cat = dep_cat;
    }

    /**
     * @return the dep_desc
     */
    public String getDep_desc() {
        return dep_desc;
    }

    /**
     * @param dep_desc the dep_desc to set
     */
    public void setDep_desc(String dep_desc) {
        this.dep_desc = dep_desc;
    }

    /**
     * @return the dep_status
     */
    public String getDep_status() {
        return dep_status;
    }

    /**
     * @param dep_status the dep_status to set
     */
    public void setDep_status(String dep_status) {
        this.dep_status = dep_status;
    }

}
