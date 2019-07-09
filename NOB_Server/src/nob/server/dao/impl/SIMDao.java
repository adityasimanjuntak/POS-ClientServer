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
import nob.dao.api.ISimDao;
import nob.model.Entity_SIM;

/**
 *
 * @author alimk
 */
public class SIMDao extends UnicastRemoteObject implements ISimDao {

    private Connection conn = null;
    private String strSql = "";

    public SIMDao(Connection conn) throws RemoteException {
        this.conn = conn;
    }

    @Override
    public String GenerateReqIzinID(Entity_SIM ES) throws RemoteException {
        String result = null;
        String x = "REQIZI-";
        System.out.println("Method Generate Request Izin ID diakses secara remote");
        strSql = "SELECT * FROM nob_req_izin ORDER BY req_izin_id DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String noreg = rs.getString("req_izin_id").substring(7);
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
            Logger.getLogger(SIMDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String GenerateReqActID(Entity_SIM ES) throws RemoteException {
        String result = null;
        String x = "REQACT-";
        System.out.println("Method Generate Request Act ID diakses secara remote");
        strSql = "SELECT * FROM nob_req_act ORDER BY req_act_id DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String noreg = rs.getString("req_act_id").substring(7);
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
            Logger.getLogger(SIMDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_SIM> getListIzinbyEmp(String IDEmp) throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_SIM> daftarIzin = new ArrayList<Entity_SIM>();
        System.out.println("Method getListIzinbyEmp diakses secara remote");
        strSql = "Select RI.req_izin_id, E.emp_name, CI.izin_name, RI.req_izin_date_start, RI.req_izin_date_end, RI.req_izin_desc, "
                + "RI.req_izin_created_date, RI.req_izin_status_approve\n"
                + "FROM nob_req_izin RI \n"
                + "JOIN nob_category_izin CI ON CI.izin_cat = RI.req_izin_cat\n"
                + "JOIN nob_emp E  ON E.emp_id = RI.req_izin_id_emp \n"
                + "WHERE RI.req_izin_id_emp = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, IDEmp);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_SIM ES = new Entity_SIM();
                ES.setReq_izin_id(rs.getString("req_izin_id"));
                ES.setReq_izin_id_emp(rs.getString("emp_name"));
                ES.setReq_izin_cat(rs.getString("izin_name"));
                ES.setReq_izin_date_start(rs.getDate("req_izin_date_start"));
                ES.setReq_izin_date_end(rs.getDate("req_izin_date_end"));
                ES.setReq_izin_desc(rs.getString("req_izin_desc"));
                ES.setReq_izin_created_date(rs.getDate("req_izin_created_date"));
                ES.setReq_izin_status_approve(rs.getString("req_izin_status_approve"));

                // simpan objek barang ke dalam objek class List
                daftarIzin.add(ES);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SIMDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarIzin;
    }

    @Override
    public List<Entity_SIM> getListActbyEmp(String IDEmp) throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_SIM> daftarAct = new ArrayList<Entity_SIM>();
        System.out.println("Method getListActbyEmp diakses secara remote");
        strSql = "Select RA.req_act_id, E.emp_name, CA.act_name, RA.req_act_date_start, RA.req_act_date_end, RA.req_act_desc, "
                + "RA.req_act_created_date, RA.req_act_status_approve\n"
                + "FROM nob_req_act RA \n"
                + "JOIN nob_category_activity CA ON CA.act_cat = RA.req_act_cat\n"
                + "JOIN nob_emp E ON E.emp_id = RA.req_act_id_emp \n"
                + "WHERE RA.req_act_id_emp = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, IDEmp);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_SIM ES = new Entity_SIM();
                ES.setReq_act_id(rs.getString("req_act_id"));
                ES.setReq_act_id_emp(rs.getString("emp_name"));
                ES.setReq_act_cat(rs.getString("act_name"));
                ES.setReq_act_date_start(rs.getDate("req_act_date_start"));
                ES.setReq_act_date_end(rs.getDate("req_act_date_end"));
                ES.setReq_act_desc(rs.getString("req_act_desc"));
                ES.setReq_act_created_date(rs.getDate("req_act_created_date"));
                ES.setReq_act_status_approve(rs.getString("req_act_status_approve"));

                // simpan objek barang ke dalam objek class List
                daftarAct.add(ES);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SIMDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarAct;
    }

    @Override
    public List<Entity_SIM> getCbTipeIzin() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_SIM> daftarTipeIzin = new ArrayList<Entity_SIM>();
        System.out.println("Method getCbTipeIzin diakses secara remote");
        strSql = "SELECT izin_cat, izin_name from nob_category_izin";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_SIM ES = new Entity_SIM();
                ES.setIzin_name(rs.getString("izin_name"));
                // simpan objek barang ke dalam objek class List
                daftarTipeIzin.add(ES);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SIMDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarTipeIzin;
    }

    @Override
    public List<Entity_SIM> getCbTipeActivity() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_SIM> daftarTipeAct = new ArrayList<Entity_SIM>();
        System.out.println("Method getCbTipeActivity diakses secara remote");
        strSql = "SELECT act_cat, act_name from nob_category_activity";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_SIM ES = new Entity_SIM();
                ES.setAct_name(rs.getString("act_name"));
                // simpan objek barang ke dalam objek class List
                daftarTipeAct.add(ES);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SIMDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarTipeAct;
    }

    @Override
    public String getIDIzin(Entity_SIM ES) throws RemoteException {
        String result = null;
        String id_izin = null;
        System.out.println("Method getIDCat diakses secara remote");
        strSql = "SELECT izin_cat FROM nob_category_izin where izin_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ES.getIzin_name());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                id_izin = rs.getString("izin_cat");
                result = id_izin;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(SIMDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String getIDAct(Entity_SIM ES) throws RemoteException {
        String result = null;
        String id_act = null;
        System.out.println("Method getIDAct diakses secara remote");
        strSql = "SELECT act_cat FROM nob_category_activity where act_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ES.getAct_name());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                id_act = rs.getString("act_cat");
                result = id_act;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(SIMDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int saveIzin(Entity_SIM ES) throws RemoteException {
        int result = 0;
        System.out.println("Method saveIzin diakses secara remote");
        strSql = "INSERT INTO nob_req_izin VALUES (?, ?, ?, ?, ?, ?, ?, 'Pending', 'Not-Read')";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, ES.getReq_izin_id());
            ps.setString(2, ES.getReq_izin_id_emp());
            ps.setString(3, ES.getReq_izin_cat());
            ps.setDate(4, new Date(ES.getReq_izin_date_start().getTime()));
            ps.setDate(5, new Date(ES.getReq_izin_date_end().getTime()));
            ps.setString(6, ES.getReq_izin_desc());
            ps.setDate(7, new Date(ES.getReq_izin_created_date().getTime()));
            // jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SIMDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int saveActivity(Entity_SIM ES) throws RemoteException {
        int result = 0;
        System.out.println("Method saveAct diakses secara remote");
        strSql = "INSERT INTO nob_req_act VALUES (?, ?, ?, ?, ?, ?, ?, 'Pending', 'Not-Read')";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, ES.getReq_act_id());
            ps.setString(2, ES.getReq_act_id_emp());
            ps.setString(3, ES.getReq_act_cat());
            ps.setDate(4, new Date(ES.getReq_act_date_start().getTime()));
            ps.setDate(5, new Date(ES.getReq_act_date_end().getTime()));
            ps.setString(6, ES.getReq_act_desc());
            ps.setDate(7, new Date(ES.getReq_act_created_date().getTime()));
            // jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SIMDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int CountIzinSuccess(Entity_SIM ES) throws RemoteException {
        int result = 0;
        System.out.println("Method Count Izin Sukses diakses secara remote");
        strSql = "SELECT COUNT(*) From nob_req_izin where req_izin_status_approve = 'Approved' and req_izin_id_emp = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ES.getReq_izin_id_emp());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            while (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int CountIzinPending(Entity_SIM ES) throws RemoteException {
        int result = 0;
        System.out.println("Method Count Izin Pending diakses secara remote");
        strSql = "SELECT COUNT(*) From nob_req_izin where req_izin_status_approve = 'Pending' and req_izin_id_emp = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ES.getReq_izin_id_emp());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            while (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int CountIzinRejected(Entity_SIM ES) throws RemoteException {
        int result = 0;
        System.out.println("Method Count Izin Rejected diakses secara remote");
        strSql = "SELECT COUNT(*) From nob_req_izin where req_izin_status_approve = 'Rejected' and req_izin_id_emp = ? ";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ES.getReq_izin_id_emp());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            while (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int CountActSuccess(Entity_SIM ES) throws RemoteException {
        int result = 0;
        System.out.println("Method Count Act Sukses diakses secara remote");
        strSql = "SELECT COUNT(*) From nob_req_act where req_act_status_approve = 'Approved' and req_act_id_emp = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ES.getReq_act_id_emp());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            while (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int CountActPending(Entity_SIM ES) throws RemoteException {
        int result = 0;
        System.out.println("Method Count Act Pending diakses secara remote");
        strSql = "SELECT COUNT(*) From nob_req_act where req_act_status_approve = 'Pending' and req_act_id_emp = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ES.getReq_act_id_emp());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            while (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int CountActRejected(Entity_SIM ES) throws RemoteException {
        int result = 0;
        System.out.println("Method Count Act Rejected diakses secara remote");
        strSql = "SELECT COUNT(*) From nob_req_act where req_act_status_approve = 'Rejected' and req_act_id_emp = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ES.getReq_act_id_emp());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            while (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public int CountIzinSuccessAll() throws RemoteException {
        int result = 0;
        System.out.println("Method Count Izin Sukses diakses secara remote");
        strSql = "SELECT COUNT(*) From nob_req_izin where req_izin_status_approve = 'Approved'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            while (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int CountIzinPendingAll() throws RemoteException {
        int result = 0;
        System.out.println("Method Count Izin Pending diakses secara remote");
        strSql = "SELECT COUNT(*) From nob_req_izin where req_izin_status_approve = 'Pending'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            while (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int CountIzinRejectedAll() throws RemoteException {
        int result = 0;
        System.out.println("Method Count Izin Rejected diakses secara remote");
        strSql = "SELECT COUNT(*) From nob_req_izin where req_izin_status_approve = 'Rejected'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            while (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int CountActSuccessAll() throws RemoteException {
        int result = 0;
        System.out.println("Method Count Act Sukses diakses secara remote");
        strSql = "SELECT COUNT(*) From nob_req_act where req_act_status_approve = 'Approved'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            while (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int CountActPendingAll() throws RemoteException {
        int result = 0;
        System.out.println("Method Count Act Pending diakses secara remote");
        strSql = "SELECT COUNT(*) From nob_req_act where req_act_status_approve = 'Pending'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            while (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int CountActRejectedAll() throws RemoteException {
        int result = 0;
        System.out.println("Method Count Act Rejected diakses secara remote");
        strSql = "SELECT COUNT(*) From nob_req_act where req_act_status_approve = 'Rejected'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            while (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_SIM> getListIzinAll() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_SIM> daftarIzin = new ArrayList<Entity_SIM>();
        System.out.println("Method getListIzinbyEmp diakses secara remote");
        strSql = "Select RI.req_izin_id, E.emp_name, CI.izin_name, RI.req_izin_date_start, RI.req_izin_date_end, RI.req_izin_desc, "
                + "RI.req_izin_created_date, RI.req_izin_status_approve\n"
                + "FROM nob_req_izin RI \n"
                + "JOIN nob_category_izin CI ON CI.izin_cat = RI.req_izin_cat\n"
                + "JOIN nob_emp E  ON E.emp_id = RI.req_izin_id_emp";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_SIM ES = new Entity_SIM();
                ES.setReq_izin_id(rs.getString("req_izin_id"));
                ES.setReq_izin_id_emp(rs.getString("emp_name"));
                ES.setReq_izin_cat(rs.getString("izin_name"));
                ES.setReq_izin_date_start(rs.getDate("req_izin_date_start"));
                ES.setReq_izin_date_end(rs.getDate("req_izin_date_end"));
                ES.setReq_izin_desc(rs.getString("req_izin_desc"));
                ES.setReq_izin_created_date(rs.getDate("req_izin_created_date"));
                ES.setReq_izin_status_approve(rs.getString("req_izin_status_approve"));

                // simpan objek barang ke dalam objek class List
                daftarIzin.add(ES);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SIMDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarIzin;
    }

    @Override
    public List<Entity_SIM> getListActAll() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_SIM> daftarAct = new ArrayList<Entity_SIM>();
        System.out.println("Method getListActbyEmp diakses secara remote");
        strSql = "Select RA.req_act_id, E.emp_name, CA.act_name, RA.req_act_date_start, RA.req_act_date_end, RA.req_act_desc, "
                + "RA.req_act_created_date, RA.req_act_status_approve\n"
                + "FROM nob_req_act RA \n"
                + "JOIN nob_category_activity CA ON CA.act_cat = RA.req_act_cat\n"
                + "JOIN nob_emp E ON E.emp_id = RA.req_act_id_emp";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_SIM ES = new Entity_SIM();
                ES.setReq_act_id(rs.getString("req_act_id"));
                ES.setReq_act_id_emp(rs.getString("emp_name"));
                ES.setReq_act_cat(rs.getString("act_name"));
                ES.setReq_act_date_start(rs.getDate("req_act_date_start"));
                ES.setReq_act_date_end(rs.getDate("req_act_date_end"));
                ES.setReq_act_desc(rs.getString("req_act_desc"));
                ES.setReq_act_created_date(rs.getDate("req_act_created_date"));
                ES.setReq_act_status_approve(rs.getString("req_act_status_approve"));

                // simpan objek barang ke dalam objek class List
                daftarAct.add(ES);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SIMDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarAct;
    }

    @Override
    public int updateApproveIzin(Entity_SIM ES) throws RemoteException {
        int result = 0;
        String SQL = "UPDATE nob_req_izin set req_izin_status_approve = 'Approved' where req_izin_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, ES.getReq_izin_id());
            ps.executeUpdate();
            //jalankanperinah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SIMDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int updateRejectIzin(Entity_SIM ES) throws RemoteException {
        int result = 0;
        String SQL = "UPDATE nob_req_izin set req_izin_status_approve = 'Rejected' where req_izin_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, ES.getReq_izin_id());
            ps.executeUpdate();
            //jalankanperinah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SIMDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int updateApproveAct(Entity_SIM ES) throws RemoteException {
        int result = 0;
        String SQL = "UPDATE nob_req_act set req_act_status_approve = 'Approved' where req_act_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, ES.getReq_act_id());
            ps.executeUpdate();
            //jalankanperinah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SIMDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int updateRejectAct(Entity_SIM ES) throws RemoteException {
        int result = 0;
        String SQL = "UPDATE nob_req_act set req_act_status_approve = 'Rejected' where req_act_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, ES.getReq_act_id());
            ps.executeUpdate();
            //jalankanperinah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SIMDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
