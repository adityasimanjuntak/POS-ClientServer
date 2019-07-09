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
public class Entity_Task implements Serializable {

    private String task_id, task_title, task_desc, task_pic, task_dep, task_status;
    private Date task_date, task_date_deadline, task_date_open;

    /**
     * @return the task_id
     */
    public String getTask_id() {
        return task_id;
    }

    /**
     * @param task_id the task_id to set
     */
    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    /**
     * @return the task_title
     */
    public String getTask_title() {
        return task_title;
    }

    /**
     * @param task_title the task_title to set
     */
    public void setTask_title(String task_title) {
        this.task_title = task_title;
    }

    /**
     * @return the task_desc
     */
    public String getTask_desc() {
        return task_desc;
    }

    /**
     * @param task_desc the task_desc to set
     */
    public void setTask_desc(String task_desc) {
        this.task_desc = task_desc;
    }

    /**
     * @return the task_pic
     */
    public String getTask_pic() {
        return task_pic;
    }

    /**
     * @param task_pic the task_pic to set
     */
    public void setTask_pic(String task_pic) {
        this.task_pic = task_pic;
    }

    /**
     * @return the task_dep
     */
    public String getTask_dep() {
        return task_dep;
    }

    /**
     * @param task_dep the task_dep to set
     */
    public void setTask_dep(String task_dep) {
        this.task_dep = task_dep;
    }

    /**
     * @return the task_status
     */
    public String getTask_status() {
        return task_status;
    }

    /**
     * @param task_status the task_status to set
     */
    public void setTask_status(String task_status) {
        this.task_status = task_status;
    }

    /**
     * @return the task_date
     */
    public Date getTask_date() {
        return task_date;
    }

    /**
     * @param task_date the task_date to set
     */
    public void setTask_date(Date task_date) {
        this.task_date = task_date;
    }

    /**
     * @return the task_date_deadline
     */
    public Date getTask_date_deadline() {
        return task_date_deadline;
    }

    /**
     * @param task_date_deadline the task_date_deadline to set
     */
    public void setTask_date_deadline(Date task_date_deadline) {
        this.task_date_deadline = task_date_deadline;
    }

    /**
     * @return the task_date_open
     */
    public Date getTask_date_open() {
        return task_date_open;
    }

    /**
     * @param task_date_open the task_date_open to set
     */
    public void setTask_date_open(Date task_date_open) {
        this.task_date_open = task_date_open;
    }

}
