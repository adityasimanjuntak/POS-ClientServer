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
public class Entity_Warehouse implements Serializable {

    private String item_id, item_name, item_category, item_brand, item_guarantee, item_info, item_supplier;
    private int item_stock, item_price_nta, item_price_pub, item_price_single;

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
     * @return the item_category
     */
    public String getItem_category() {
        return item_category;
    }

    /**
     * @param item_category the item_category to set
     */
    public void setItem_category(String item_category) {
        this.item_category = item_category;
    }

    /**
     * @return the item_brand
     */
    public String getItem_brand() {
        return item_brand;
    }

    /**
     * @param item_brand the item_brand to set
     */
    public void setItem_brand(String item_brand) {
        this.item_brand = item_brand;
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
     * @return the item_info
     */
    public String getItem_info() {
        return item_info;
    }

    /**
     * @param item_info the item_info to set
     */
    public void setItem_info(String item_info) {
        this.item_info = item_info;
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
     * @return the item_stock
     */
    public int getItem_stock() {
        return item_stock;
    }

    /**
     * @param item_stock the item_stock to set
     */
    public void setItem_stock(int item_stock) {
        this.item_stock = item_stock;
    }

    /**
     * @return the item_price_nta
     */
    public int getItem_price_nta() {
        return item_price_nta;
    }

    /**
     * @param item_price_nta the item_price_nta to set
     */
    public void setItem_price_nta(int item_price_nta) {
        this.item_price_nta = item_price_nta;
    }

    /**
     * @return the item_price_pub
     */
    public int getItem_price_pub() {
        return item_price_pub;
    }

    /**
     * @param item_price_pub the item_price_pub to set
     */
    public void setItem_price_pub(int item_price_pub) {
        this.item_price_pub = item_price_pub;
    }

    /**
     * @return the item_price_single
     */
    public int getItem_price_single() {
        return item_price_single;
    }

    /**
     * @param item_price_single the item_price_single to set
     */
    public void setItem_price_single(int item_price_single) {
        this.item_price_single = item_price_single;
    }

}
