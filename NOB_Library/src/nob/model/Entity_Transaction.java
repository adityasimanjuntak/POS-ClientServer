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
public class Entity_Transaction implements Serializable {

    private String buyer_id;
    private String trans_id, item_name;
    private int quantity;
    private int price;
    private double trans_totalPayment;
    private byte[] trans_qr;
    private String item_id;
    private Date trans_date;
    private int stock;
    private int item_price_NTA, item_price_Pub, item_price_Single;
    private int item_price;
    private int item_price_subtotal;
    private String vou_id, vou_desc;
    private int vou_disc, vou_stock;
    private Date vou_date_exp;
    private String trans_processBy;
    private String trans_updateBy;
    private String trans_paymentVia;
    private double trans_discount;
    private String trans_paymentType;
    private String bank_id;
    private String trans_paymentDesc;
    private String trans_status;
    private String dis_id;
    private String trans_cust_id;
    private String item_qty_string;
    private String item_price_string;
    private String item_subtotal_string;
    private String item_category;
    private String item_brand;
    private String item_guarantee;
    private String item_info;
    private double trans_NTA;

    /**
     * @return the trans_id
     */
    public String getTrans_id() {
        return trans_id;
    }

    /**
     * @param trans_id the trans_id to set
     */
    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
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
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the trans_qr
     */
    public byte[] getTrans_qr() {
        return trans_qr;
    }

    /**
     * @param trans_qr the trans_qr to set
     */
    public void setTrans_qr(byte[] trans_qr) {
        this.trans_qr = trans_qr;
    }

    /**
     * @return the buyer_id
     */
    public String getBuyer_id() {
        return buyer_id;
    }

    /**
     * @param buyer_id the buyer_id to set
     */
    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    /**
     * @return the price
     */
    public int getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(int price) {
        this.price = price;
    }

    public int Subtotal() {
        return (quantity * price);
    }

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
     * @return the trans_date
     */
    public Date getTrans_date() {
        return trans_date;
    }

    /**
     * @param trans_date the trans_date to set
     */
    public void setTrans_date(Date trans_date) {
        this.trans_date = trans_date;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
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

    /**
     * @return the item_price
     */
    public int getItem_price() {
        return item_price;
    }

    /**
     * @param item_price the item_price to set
     */
    public void setItem_price(int item_price) {
        this.item_price = item_price;
    }

    /**
     * @return the item_price_subtotal
     */
    public int getItem_price_subtotal() {
        return item_price_subtotal;
    }

    /**
     * @param item_price_subtotal the item_price_subtotal to set
     */
    public void setItem_price_subtotal(int item_price_subtotal) {
        this.item_price_subtotal = item_price_subtotal;
    }

    /**
     * @return the vou_id
     */
    public String getVou_id() {
        return vou_id;
    }

    /**
     * @param vou_id the vou_id to set
     */
    public void setVou_id(String vou_id) {
        this.vou_id = vou_id;
    }

    /**
     * @return the vou_desc
     */
    public String getVou_desc() {
        return vou_desc;
    }

    /**
     * @param vou_desc the vou_desc to set
     */
    public void setVou_desc(String vou_desc) {
        this.vou_desc = vou_desc;
    }

    /**
     * @return the vou_disc
     */
    public int getVou_disc() {
        return vou_disc;
    }

    /**
     * @param vou_disc the vou_disc to set
     */
    public void setVou_disc(int vou_disc) {
        this.vou_disc = vou_disc;
    }

    /**
     * @return the vou_stock
     */
    public int getVou_stock() {
        return vou_stock;
    }

    /**
     * @param vou_stock the vou_stock to set
     */
    public void setVou_stock(int vou_stock) {
        this.vou_stock = vou_stock;
    }

    /**
     * @return the vou_date_exp
     */
    public Date getVou_date_exp() {
        return vou_date_exp;
    }

    /**
     * @param vou_date_exp the vou_date_exp to set
     */
    public void setVou_date_exp(Date vou_date_exp) {
        this.vou_date_exp = vou_date_exp;
    }

    /**
     * @return the trans_processBy
     */
    public String getTrans_processBy() {
        return trans_processBy;
    }

    /**
     * @param trans_processBy the trans_processBy to set
     */
    public void setTrans_processBy(String trans_processBy) {
        this.trans_processBy = trans_processBy;
    }

    /**
     * @return the trans_paymentVia
     */
    public String getTrans_paymentVia() {
        return trans_paymentVia;
    }

    /**
     * @param trans_paymentVia the trans_paymentVia to set
     */
    public void setTrans_paymentVia(String trans_paymentVia) {
        this.trans_paymentVia = trans_paymentVia;
    }

    /**
     * @return the trans_paymentType
     */
    public String getTrans_paymentType() {
        return trans_paymentType;
    }

    /**
     * @param trans_paymentType the trans_paymentType to set
     */
    public void setTrans_paymentType(String trans_paymentType) {
        this.trans_paymentType = trans_paymentType;
    }

    /**
     * @return the bank_id
     */
    public String getBank_id() {
        return bank_id;
    }

    /**
     * @param bank_id the bank_id to set
     */
    public void setBank_id(String bank_id) {
        this.bank_id = bank_id;
    }

    /**
     * @return the trans_paymentDesc
     */
    public String getTrans_paymentDesc() {
        return trans_paymentDesc;
    }

    /**
     * @param trans_paymentDesc the trans_paymentDesc to set
     */
    public void setTrans_paymentDesc(String trans_paymentDesc) {
        this.trans_paymentDesc = trans_paymentDesc;
    }

    /**
     * @return the trans_status
     */
    public String getTrans_status() {
        return trans_status;
    }

    /**
     * @param trans_status the trans_status to set
     */
    public void setTrans_status(String trans_status) {
        this.trans_status = trans_status;
    }

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
     * @return the trans_totalPayment
     */
    public double getTrans_totalPayment() {
        return trans_totalPayment;
    }

    /**
     * @param trans_totalPayment the trans_totalPayment to set
     */
    public void setTrans_totalPayment(double trans_totalPayment) {
        this.trans_totalPayment = trans_totalPayment;
    }

    /**
     * @param trans_discount the trans_discount to set
     */
    public void setTrans_discount(double trans_discount) {
        this.trans_discount = trans_discount;
    }

    /**
     * @return the trans_discount
     */
    public double getTrans_discount() {
        return trans_discount;
    }

    /**
     * @return the trans_cust_id
     */
    public String getTrans_cust_id() {
        return trans_cust_id;
    }

    /**
     * @param trans_cust_id the trans_cust_id to set
     */
    public void setTrans_cust_id(String trans_cust_id) {
        this.trans_cust_id = trans_cust_id;
    }

    /**
     * @return the item_qty_string
     */
    public String getItem_qty_string() {
        return item_qty_string;
    }

    /**
     * @param item_qty_string the item_qty_string to set
     */
    public void setItem_qty_string(String item_qty_string) {
        this.item_qty_string = item_qty_string;
    }

    /**
     * @return the item_subtotal_string
     */
    public String getItem_subtotal_string() {
        return item_subtotal_string;
    }

    /**
     * @param item_subtotal_string the item_subtotal_string to set
     */
    public void setItem_subtotal_string(String item_subtotal_string) {
        this.item_subtotal_string = item_subtotal_string;
    }

    /**
     * @return the item_price_string
     */
    public String getItem_price_string() {
        return item_price_string;
    }

    /**
     * @param item_price_string the item_price_string to set
     */
    public void setItem_price_string(String item_price_string) {
        this.item_price_string = item_price_string;
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
     * @return the trans_NTA
     */
    public double getTrans_NTA() {
        return trans_NTA;
    }

    /**
     * @param trans_NTA the trans_NTA to set
     */
    public void setTrans_NTA(double trans_NTA) {
        this.trans_NTA = trans_NTA;
    }

    /**
     * @return the trans_updateBy
     */
    public String getTrans_updateBy() {
        return trans_updateBy;
    }

    /**
     * @param trans_updateBy the trans_updateBy to set
     */
    public void setTrans_updateBy(String trans_updateBy) {
        this.trans_updateBy = trans_updateBy;
    }



}
