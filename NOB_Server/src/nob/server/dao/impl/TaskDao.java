/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nob.server.dao.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nob.dao.api.ITaskDao;
import nob.model.Entity_Signup;
import nob.model.Entity_Task;

/**
 *
 * @author alimk
 */
public class TaskDao extends UnicastRemoteObject implements ITaskDao {

    private Connection conn = null;
    private String strSql = "";

    public TaskDao(Connection conn) throws RemoteException {
        this.conn = conn;
    }

    @Override
    public int saveTugas(Entity_Task ET) throws RemoteException {
        int result = 0;
        System.out.println("Method Add Task diakses secara remote");
        strSql = "INSERT INTO nob_task_list(task_id, task_title, task_description, task_date, task_date_deadline, task_date_opened, task_pic, task_dep, task_status) VALUES (?, ?, ?, ?, ?, '', 'null', ?, ?)";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, ET.getTask_id());
            ps.setString(2, ET.getTask_title());
            ps.setString(3, ET.getTask_desc());
            ps.setDate(4, new Date(ET.getTask_date().getTime()));
            ps.setDate(5, new Date(ET.getTask_date_deadline().getTime()));
            ps.setString(6, ET.getTask_dep());
            ps.setString(7, ET.getTask_status());
            // jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TaskDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String GenerateTaskID(Entity_Task ET) throws RemoteException {
        String result = null;
        String x = "TASK-";
        System.out.println("Method Generate Task ID diakses secara remote");
        strSql = "SELECT * FROM nob_task_list ORDER BY task_id DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String noreg = rs.getString("task_id").substring(5);
                String AN = "" + (Integer.parseInt(noreg) + 1);
                String nol = "";
                switch (AN.length()) {
                    case 1:
                        nol = "00";
                        break;
                    case 2:
                        nol = "0";
                        break;
                    case 3:
                        nol = "";
                        break;
                    default:
                        break;
                }
                result = x + nol + AN;
            } else {
                result = x + "00" + "1";
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(TaskDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Task> getAll() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Task> daftarTugas = new ArrayList<Entity_Task>();
        System.out.println("Method getAll Daftar Tugas diakses secara remote");
        strSql = "SELECT task_id, task_title, task_description, task_date, task_date_deadline, task_date_opened, task_pic, task_dep, task_status FROM nob_task_list ORDER BY task_id";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Task ET = new Entity_Task();
                ET.setTask_id(rs.getString("task_id"));
                ET.setTask_title(rs.getString("task_title"));
                ET.setTask_desc(rs.getString("task_description"));
                ET.setTask_date(rs.getDate("task_date"));
                ET.setTask_date_deadline(rs.getDate("task_date_deadline"));
                ET.setTask_date_open(rs.getDate("task_date_opened"));
                ET.setTask_pic(rs.getString("task_pic"));
                ET.setTask_dep(rs.getString("task_dep"));
                ET.setTask_status(rs.getString("task_status"));
                // simpan objek barang ke dalam objek class List
                daftarTugas.add(ET);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TaskDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarTugas;
    }

    @Override
    public int updateStatusTask_Done(Entity_Task ET) throws RemoteException {
        int result = 1;
        String SQL = "UPDATE nob_task_list set task_status = 'Done' where task_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, ET.getTask_id());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TaskDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int updateStatusTask_UnDone(Entity_Task ET) throws RemoteException {
        int result = 1;
        String SQL = "UPDATE nob_task_list set task_status = 'Currently Added' where task_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, ET.getTask_id());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TaskDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int updateStatusTask_OnProgress(Entity_Task ET) throws RemoteException {
        int result = 1;
        String SQL = "UPDATE nob_task_list set task_status = 'On Progress' where task_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, ET.getTask_id());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TaskDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int deleteTask(Entity_Task ET) throws RemoteException {
        int result = 0;
        System.out.println("Method delete Task diakses secara remote");
        strSql = "DELETE FROM nob_task_list WHERE task_id = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, ET.getTask_id());
            // jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TaskDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String CountTask() throws RemoteException {
        String result = null;
        System.out.println("Method Hitung Tugas diakses secara remote");
        strSql = "SELECT COUNT(*) From nob_task_list where task_status = 'Currently Added'";
        String rowcount;
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            while (rs.next()) {
                result = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TaskDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String CountTaskWrh() throws RemoteException {
        String result = null;
        System.out.println("Method Hitung Tugas Warehouse diakses secara remote");
        strSql = "SELECT COUNT(*) From nob_task_list where task_status = 'Currently Added' and task_dep ='WRH'";
        String rowcount;
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            while (rs.next()) {
                result = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TaskDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
