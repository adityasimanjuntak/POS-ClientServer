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
public class Entity_Category implements Serializable {

    private String cat_id;
    private String cat_name;

    /**
     * @return the cat_id
     */
    public String getCat_id() {
        return cat_id;
    }

    /**
     * @param cat_id the cat_id to set
     */
    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    /**
     * @return the cat_name
     */
    public String getCat_name() {
        return cat_name;
    }

    /**
     * @param cat_name the cat_name to set
     */
    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

}
