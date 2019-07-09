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
public class Entity_Item implements Serializable {

    private String item_id, item_name, item_categories, item_supplier, item_guarantee, item_stock;
    private int item_price_NTA, item_price_Pub, item_price_Single;

    /**
     * @return the item_id
     */
    public String getItem_id() {
        return item_id;
    }

    /**
     * @param item_id the item_id to set
     */
    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    /**
     * @return the item_name
     */
    public String getItem_name() {
        return item_name;
    }

    /**
     * @param item_name the item_name to set
     */
    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    /**
     * @return the item_categories
     */
    public String getItem_categories() {
        return item_categories;
    }

    /**
     * @param item_categories the item_categories to set
     */
    public void setItem_categories(String item_categories) {
        this.item_categories = item_categories;
    }

    /**
     * @return the item_supplier
     */
    public String getItem_supplier() {
        return item_supplier;
    }

    /**
     * @param item_supplier the item_supplier to set
     */
    public void setItem_supplier(String item_supplier) {
        this.item_supplier = item_supplier;
    }

    /**
     * @return the item_guarantee
     */
    public String getItem_guarantee() {
        return item_guarantee;
    }

    /**
     * @param item_guarantee the item_guarantee to set
     */
    public void setItem_guarantee(String item_guarantee) {
        this.item_guarantee = item_guarantee;
    }

    /**
     * @return the item_stock
     */
    public String getItem_stock() {
        return item_stock;
    }

    /**
     * @param item_stock the item_stock to set
     */
    public void setItem_stock(String item_stock) {
        this.item_stock = item_stock;
    }

    /**
     * @return the item_price_NTA
     */
    public int getItem_price_NTA() {
        return item_price_NTA;
    }

    /**
     * @param item_price_NTA the item_price_NTA to set
     */
    public void setItem_price_NTA(int item_price_NTA) {
        this.item_price_NTA = item_price_NTA;
    }

    /**
     * @return the item_price_Pub
     */
    public int getItem_price_Pub() {
        return item_price_Pub;
    }

    /**
     * @param item_price_Pub the item_price_Pub to set
     */
    public void setItem_price_Pub(int item_price_Pub) {
        this.item_price_Pub = item_price_Pub;
    }

    /**
     * @return the item_price_Single
     */
    public int getItem_price_Single() {
        return item_price_Single;
    }

    /**
     * @param item_price_Single the item_price_Single to set
     */
    public void setItem_price_Single(int item_price_Single) {
        this.item_price_Single = item_price_Single;
    }

}
