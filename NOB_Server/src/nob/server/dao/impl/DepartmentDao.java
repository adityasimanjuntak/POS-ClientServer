/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nob.server.dao.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nob.dao.api.IDepartmentDao;
import nob.model.Entity_Department;
import nob.model.Entity_Signup;

/**
 *
 * @author alimk
 */
public class DepartmentDao extends UnicastRemoteObject implements IDepartmentDao {

    private Connection conn = null;
    private String strSql = "";

    public DepartmentDao(Connection conn) throws RemoteException {
        this.conn = conn;
    }

    @Override
    public int saveDataDepartment(Entity_Department ED) throws RemoteException {
        int result = 0;
        System.out.println("Method Tambah Department diakses secara remote");
        strSql = "INSERT INTO nob_department(dep_id, dep_cat, dep_description, dep_status, dep_sys_status) VALUES (?, ?, ?, ?, 'Offline')";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, ED.getDep_id());
            ps.setString(2, ED.getDep_cat());
            ps.setString(3, ED.getDep_desc());
            ps.setString(4, ED.getDep_status());
            // jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int updateStatusDepIn(Entity_Department ED) throws RemoteException {
        int result = 1;
        String SQL = "UPDATE nob_department set dep_status = 'Inactive' where dep_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, ED.getDep_id());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int updateStatusDepAc(Entity_Department ED) throws RemoteException {
        int result = 1;
        String SQL = "UPDATE nob_department set dep_status = 'Active' where dep_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, ED.getDep_id());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int updateStatusLoginInactive(Entity_Department ED) throws RemoteException {
        int result = 1;
        String SQL = "UPDATE nob_emp_login set emp_status='Inactive' Where LEFT(emp_signin_id,3) = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, ED.getDep_status());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int updateStatusLoginActive(Entity_Department ED) throws RemoteException {
        int result = 1;
        String SQL = "UPDATE nob_emp_login set emp_status='Active' Where LEFT(emp_signin_id,3) = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, ED.getDep_status());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Department> getAll() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Department> daftarDepartment = new ArrayList<Entity_Department>();
        System.out.println("Method getAll Daftar Department diakses secara remote");
        strSql = "SELECT dep_id, dep_cat, dep_description, dep_status FROM nob_department ORDER BY dep_cat";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Department ED = new Entity_Department();
                ED.setDep_id(rs.getString("dep_id"));
                ED.setDep_cat(rs.getString("dep_cat"));
                ED.setDep_desc(rs.getString("dep_description"));
                ED.setDep_status(rs.getString("dep_status"));
                // simpan objek barang ke dalam objek class List
                daftarDepartment.add(ED);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarDepartment;
    }

    @Override
    public List<Entity_Department> getCbDepartment() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Department> daftarCbDep = new ArrayList<Entity_Department>();
        System.out.println("Method getCbDepartment diakses secara remote");
        strSql = "SELECT dep_id FROM nob_department ORDER BY dep_id";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Department ED = new Entity_Department();
                ED.setDep_id(rs.getString("dep_id"));
                // simpan objek barang ke dalam objek class List
                daftarCbDep.add(ED);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarCbDep;
    }
}
