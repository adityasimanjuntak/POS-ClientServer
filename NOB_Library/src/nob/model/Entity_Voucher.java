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
public class Entity_Voucher implements Serializable {

    private String voucher_id, voucher_desc;
    private Date voucher_date;
    private int voucher_discount, voucher_stock;

    /**
     * @return the voucher_id
     */
    public String getVoucher_id() {
        return voucher_id;
    }

    /**
     * @param voucher_id the voucher_id to set
     */
    public void setVoucher_id(String voucher_id) {
        this.voucher_id = voucher_id;
    }

    /**
     * @return the voucher_desc
     */
    public String getVoucher_desc() {
        return voucher_desc;
    }

    /**
     * @param voucher_desc the voucher_desc to set
     */
    public void setVoucher_desc(String voucher_desc) {
        this.voucher_desc = voucher_desc;
    }

    /**
     * @return the voucher_date
     */
    public Date getVoucher_date() {
        return voucher_date;
    }

    /**
     * @param voucher_date the voucher_date to set
     */
    public void setVoucher_date(Date voucher_date) {
        this.voucher_date = voucher_date;
    }

    /**
     * @return the voucher_discount
     */
    public int getVoucher_discount() {
        return voucher_discount;
    }

    /**
     * @param voucher_discount the voucher_discount to set
     */
    public void setVoucher_discount(int voucher_discount) {
        this.voucher_discount = voucher_discount;
    }

    /**
     * @return the voucher_stock
     */
    public int getVoucher_stock() {
        return voucher_stock;
    }

    /**
     * @param voucher_stock the voucher_stock to set
     */
    public void setVoucher_stock(int voucher_stock) {
        this.voucher_stock = voucher_stock;
    }
}
