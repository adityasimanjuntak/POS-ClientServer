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
public class Entity_Signin implements Serializable {

    private String username, password;
    private String cb;
    private String register;
    private String status;
    private String dlc;
    private String nama;
    private String ip;
    /**
     * @return the cb
     */
    public String getCb() {
        return cb;
    }

    /**
     * @param cb the cb to set
     */
    public void setCb(String cb) {
        this.cb = cb;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the register
     */
    public String getRegister() {
        return register;
    }

    /**
     * @param register the register to set
     */
    public void setRegister(String register) {
        this.register = register;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the dlc
     */
    public String getDlc() {
        return dlc;
    }

    /**
     * @param dlc the dlc to set
     */
    public void setDlc(String dlc) {
        this.dlc = dlc;
    }

    /**
     * @return the nama
     */
    public String getNama() {
        return nama;
    }

    /**
     * @param nama the nama to set
     */
    public void setNama(String nama) {
        this.nama = nama;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }
}
