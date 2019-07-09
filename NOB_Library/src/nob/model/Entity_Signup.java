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
public class Entity_Signup implements Serializable {

    private String name, gender, phone, email, department, subdepartment, id, pob, noregister, password;
    private Date dob;
    private byte[] foto;
    private byte[] qr;
    private int jlhstaff;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the pob
     */
    public String getPob() {
        return pob;
    }

    /**
     * @param pob the pob to set
     */
    public void setPob(String pob) {
        this.pob = pob;
    }

    /**
     * @return the noregister
     */
    public String getNoregister() {
        return noregister;
    }

    /**
     * @param noregister the noregister to set
     */
    public void setNoregister(String noregister) {
        this.noregister = noregister;
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
     * @return the dob
     */
    public Date getDob() {
        return dob;
    }

    /**
     * @param dob the dob to set
     */
    public void setDob(Date dob) {
        this.dob = dob;
    }

    /**
     * @return the foto
     */
    public byte[] getFoto() {
        return foto;
    }

    /**
     * @param foto the foto to set
     */
    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    /**
     * @return the qr
     */
    public byte[] getQr() {
        return qr;
    }

    /**
     * @param qr the qr to set
     */
    public void setQr(byte[] qr) {
        this.qr = qr;
    }

    /**
     * @return the subdepartment
     */
    public String getSubdepartment() {
        return subdepartment;
    }

    /**
     * @param subdepartment the subdepartment to set
     */
    public void setSubdepartment(String subdepartment) {
        this.subdepartment = subdepartment;
    }

    /**
     * @return the jlhstaff
     */
    public int getJlhstaff() {
        return jlhstaff;
    }

    /**
     * @param jlhstaff the jlhstaff to set
     */
    public void setJlhstaff(int jlhstaff) {
        this.jlhstaff = jlhstaff;
    }

}
