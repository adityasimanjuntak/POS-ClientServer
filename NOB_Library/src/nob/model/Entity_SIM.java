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
public class Entity_SIM implements Serializable {

    private String izin_cat, izin_name, act_cat, act_name;
    private String req_izin_id, req_izin_id_emp, req_izin_cat, req_izin_desc, req_izin_status_approve;
    private Date req_izin_date_start, req_izin_date_end, req_izin_created_date;
    private String req_act_id, req_act_id_emp, req_act_cat, req_act_desc, req_act_status_approve;
    private Date req_act_date_start, req_act_date_end, req_act_created_date;

    /**
     * @return the izin_cat
     */
    public String getIzin_cat() {
        return izin_cat;
    }

    /**
     * @param izin_cat the izin_cat to set
     */
    public void setIzin_cat(String izin_cat) {
        this.izin_cat = izin_cat;
    }

    /**
     * @return the izin_name
     */
    public String getIzin_name() {
        return izin_name;
    }

    /**
     * @param izin_name the izin_name to set
     */
    public void setIzin_name(String izin_name) {
        this.izin_name = izin_name;
    }

    /**
     * @return the act_cat
     */
    public String getAct_cat() {
        return act_cat;
    }

    /**
     * @param act_cat the act_cat to set
     */
    public void setAct_cat(String act_cat) {
        this.act_cat = act_cat;
    }

    /**
     * @return the act_name
     */
    public String getAct_name() {
        return act_name;
    }

    /**
     * @param act_name the act_name to set
     */
    public void setAct_name(String act_name) {
        this.act_name = act_name;
    }

    /**
     * @return the req_izin_id
     */
    public String getReq_izin_id() {
        return req_izin_id;
    }

    /**
     * @param req_izin_id the req_izin_id to set
     */
    public void setReq_izin_id(String req_izin_id) {
        this.req_izin_id = req_izin_id;
    }

    /**
     * @return the req_izin_id_emp
     */
    public String getReq_izin_id_emp() {
        return req_izin_id_emp;
    }

    /**
     * @param req_izin_id_emp the req_izin_id_emp to set
     */
    public void setReq_izin_id_emp(String req_izin_id_emp) {
        this.req_izin_id_emp = req_izin_id_emp;
    }

    /**
     * @return the req_izin_cat
     */
    public String getReq_izin_cat() {
        return req_izin_cat;
    }

    /**
     * @param req_izin_cat the req_izin_cat to set
     */
    public void setReq_izin_cat(String req_izin_cat) {
        this.req_izin_cat = req_izin_cat;
    }

    /**
     * @return the req_izin_desc
     */
    public String getReq_izin_desc() {
        return req_izin_desc;
    }

    /**
     * @param req_izin_desc the req_izin_desc to set
     */
    public void setReq_izin_desc(String req_izin_desc) {
        this.req_izin_desc = req_izin_desc;
    }

    /**
     * @return the req_izin_status_approve
     */
    public String getReq_izin_status_approve() {
        return req_izin_status_approve;
    }

    /**
     * @param req_izin_status_approve the req_izin_status_approve to set
     */
    public void setReq_izin_status_approve(String req_izin_status_approve) {
        this.req_izin_status_approve = req_izin_status_approve;
    }

    /**
     * @return the req_izin_date_start
     */
    public Date getReq_izin_date_start() {
        return req_izin_date_start;
    }

    /**
     * @param req_izin_date_start the req_izin_date_start to set
     */
    public void setReq_izin_date_start(Date req_izin_date_start) {
        this.req_izin_date_start = req_izin_date_start;
    }

    /**
     * @return the req_izin_date_end
     */
    public Date getReq_izin_date_end() {
        return req_izin_date_end;
    }

    /**
     * @param req_izin_date_end the req_izin_date_end to set
     */
    public void setReq_izin_date_end(Date req_izin_date_end) {
        this.req_izin_date_end = req_izin_date_end;
    }

    /**
     * @return the req_izin_created_date
     */
    public Date getReq_izin_created_date() {
        return req_izin_created_date;
    }

    /**
     * @param req_izin_created_date the req_izin_created_date to set
     */
    public void setReq_izin_created_date(Date req_izin_created_date) {
        this.req_izin_created_date = req_izin_created_date;
    }

    /**
     * @return the req_act_id
     */
    public String getReq_act_id() {
        return req_act_id;
    }

    /**
     * @param req_act_id the req_act_id to set
     */
    public void setReq_act_id(String req_act_id) {
        this.req_act_id = req_act_id;
    }

    /**
     * @return the req_act_id_emp
     */
    public String getReq_act_id_emp() {
        return req_act_id_emp;
    }

    /**
     * @param req_act_id_emp the req_act_id_emp to set
     */
    public void setReq_act_id_emp(String req_act_id_emp) {
        this.req_act_id_emp = req_act_id_emp;
    }

    /**
     * @return the req_act_cat
     */
    public String getReq_act_cat() {
        return req_act_cat;
    }

    /**
     * @param req_act_cat the req_act_cat to set
     */
    public void setReq_act_cat(String req_act_cat) {
        this.req_act_cat = req_act_cat;
    }

    /**
     * @return the req_act_desc
     */
    public String getReq_act_desc() {
        return req_act_desc;
    }

    /**
     * @param req_act_desc the req_act_desc to set
     */
    public void setReq_act_desc(String req_act_desc) {
        this.req_act_desc = req_act_desc;
    }

    /**
     * @return the req_act_status_approve
     */
    public String getReq_act_status_approve() {
        return req_act_status_approve;
    }

    /**
     * @param req_act_status_approve the req_act_status_approve to set
     */
    public void setReq_act_status_approve(String req_act_status_approve) {
        this.req_act_status_approve = req_act_status_approve;
    }

    /**
     * @return the req_act_date_start
     */
    public Date getReq_act_date_start() {
        return req_act_date_start;
    }

    /**
     * @param req_act_date_start the req_act_date_start to set
     */
    public void setReq_act_date_start(Date req_act_date_start) {
        this.req_act_date_start = req_act_date_start;
    }

    /**
     * @return the req_act_date_end
     */
    public Date getReq_act_date_end() {
        return req_act_date_end;
    }

    /**
     * @param req_act_date_end the req_act_date_end to set
     */
    public void setReq_act_date_end(Date req_act_date_end) {
        this.req_act_date_end = req_act_date_end;
    }

    /**
     * @return the req_act_created_date
     */
    public Date getReq_act_created_date() {
        return req_act_created_date;
    }

    /**
     * @param req_act_created_date the req_act_created_date to set
     */
    public void setReq_act_created_date(Date req_act_created_date) {
        this.req_act_created_date = req_act_created_date;
    }

}
