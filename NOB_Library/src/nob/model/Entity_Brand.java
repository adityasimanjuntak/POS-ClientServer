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
public class Entity_Brand implements Serializable {

    private String brand_id;
    private String brand_name;

    /**
     * @return the brand_id
     */
    public String getBrand_id() {
        return brand_id;
    }

    /**
     * @param brand_id the brand_id to set
     */
    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    /**
     * @return the brand_name
     */
    public String getBrand_name() {
        return brand_name;
    }

    /**
     * @param brand_name the brand_name to set
     */
    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

}
