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
import nob.dao.api.IReportDao;
import nob.model.Entity_ErrorReport;
import nob.model.Entity_Signup;
import nob.model.Entity_Task;

/**
 *
 * @author alimk
 */
public class ErrorReportDao extends UnicastRemoteObject implements IReportDao {

    private Connection conn = null;
    private String strSql = "";

    public ErrorReportDao(Connection conn) throws RemoteException {
        this.conn = conn;
    }

    @Override
    public int saveError(Entity_ErrorReport EER) throws RemoteException {
        int result = 0;
        System.out.println("Method SaveError diakses secara remote");
        strSql = "INSERT INTO nob_error_report(error_id, error_title, error_description, error_date, error_status, error_added_by) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, EER.getError_id());
            ps.setString(2, EER.getError_title());
            ps.setString(3, EER.getError_desc());
            ps.setDate(4, new Date(EER.getError_date().getTime()));
            ps.setString(5, EER.getError_status());
            ps.setString(6, EER.getError_added_by());
            // jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ErrorReportDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String GenerateErrorID(Entity_ErrorReport EER) throws RemoteException {
        String result = null;
        String x = "ERROR-";
        System.out.println("Method Generate Report ID diakses secara remote");
        strSql = "SELECT * FROM nob_error_report ORDER BY error_id DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String noreg = rs.getString("error_id").substring(6);
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
            Logger.getLogger(ErrorReportDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_ErrorReport> getAll() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_ErrorReport> daftarError = new ArrayList<Entity_ErrorReport>();
        System.out.println("Method getAll Daftar Error diakses secara remote");
        strSql = "SELECT error_id, error_title, error_description, error_date, error_pic, error_status FROM nob_error_report ORDER BY error_id";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_ErrorReport EER = new Entity_ErrorReport();
                EER.setError_id(rs.getString("error_id"));
                EER.setError_title(rs.getString("error_title"));
                EER.setError_desc(rs.getString("error_description"));
                EER.setError_date(rs.getDate("error_date"));
                EER.setError_pic(rs.getString("error_pic"));
                EER.setError_status(rs.getString("error_status"));
                // simpan objek barang ke dalam objek class List
                daftarError.add(EER);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ErrorReportDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarError;
    }

    @Override
    public int deleteError(Entity_ErrorReport EER) throws RemoteException {
        int result = 0;
        System.out.println("Method delete Error diakses secara remote");
        strSql = "DELETE FROM nob_error_report WHERE error_id = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, EER.getError_id());
            // jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TaskDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String getIDAdminError(Entity_Signup ESU) throws RemoteException {
        String result = null;
        String idAdmin = null;
        System.out.println("Method getIDAdmin diakses secara remote");
        strSql = "Select emp_id FROM nob_emp where emp_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ESU.getName());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                idAdmin = rs.getString("emp_id");
                result = idAdmin;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DistributorDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
