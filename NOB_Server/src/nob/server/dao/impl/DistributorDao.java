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
import nob.dao.api.IDistributorDao;
import nob.model.Entity_Distributor;
import nob.model.Entity_Signup;

/**
 *
 * @author alimk
 */
public class DistributorDao extends UnicastRemoteObject implements IDistributorDao {

    private Connection conn = null;
    private String strSql = "";

    public DistributorDao(Connection conn) throws RemoteException {
        this.conn = conn;
    }

    @Override
    public String CountDisActive() throws RemoteException {
        String result = null;
        System.out.println("Method Hitung Distributor diakses secara remote");
        strSql = "SELECT COUNT(*) From nob_distributor where dis_status = 'Active'";
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
            Logger.getLogger(DistributorDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int saveDistributor(Entity_Distributor ED) throws RemoteException {
        int result = 0;
        System.out.println("Method Add Distributor diakses secara remote");
        strSql = "INSERT INTO nob_distributor(dis_id, dis_name, dis_address, dis_phone, dis_email, dis_last_tran, "
                + "dis_status, dis_desc, dis_add_by) VALUES (?, ?, ?, ?, ?, '', ?, ?, ?)";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, ED.getDis_id());
            ps.setString(2, ED.getDis_name());
            ps.setString(3, ED.getDis_address());
            ps.setString(4, ED.getDis_phone());
            ps.setString(5, ED.getDis_email());
            ps.setString(6, ED.getDis_status());
            ps.setString(7, ED.getDis_desc());
            ps.setString(8, ED.getDis_added_by());
            // jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DistributorDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String GenerateDistributorID(Entity_Distributor ED) throws RemoteException {
        String result = null;
        String x = "DIS-";
        System.out.println("Method Generate Distributor ID diakses secara remote");
        strSql = "SELECT * FROM nob_distributor ORDER BY dis_id DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String nodis = rs.getString("dis_id").substring(4);
                String AN = "" + (Integer.parseInt(nodis) + 1);
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
            Logger.getLogger(DistributorDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Distributor> getAll() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Distributor> daftarDistributor = new ArrayList<Entity_Distributor>();
        System.out.println("Method getAll Daftar Distributor diakses secara remote");
        strSql = "SELECT dis_id, dis_name, dis_phone, dis_email, dis_status, dis_add_by FROM nob_distributor ORDER BY dis_id";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Distributor ED = new Entity_Distributor();
                ED.setDis_id(rs.getString("dis_id"));
                ED.setDis_name(rs.getString("dis_name"));
                ED.setDis_phone(rs.getString("dis_phone"));
                ED.setDis_email(rs.getString("dis_email"));
                ED.setDis_status(rs.getString("dis_status"));
                ED.setDis_added_by(rs.getString("dis_add_by"));
                // simpan objek barang ke dalam objek class List
                daftarDistributor.add(ED);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DistributorDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarDistributor;
    }

    @Override
    public String getIDAdmin(Entity_Signup ESU) throws RemoteException {
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

    @Override
    public int updateStatusDistributor_Active(Entity_Distributor ED) throws RemoteException {
        int result = 1;
        String SQL = "UPDATE nob_distributor set dis_status = 'Active' where dis_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, ED.getDis_id());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DistributorDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int updateStatusDistributor_inActive(Entity_Distributor ED) throws RemoteException {
        int result = 1;
        String SQL = "UPDATE nob_distributor set dis_status = 'Inactive' where dis_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, ED.getDis_id());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DistributorDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int deleteDistributor(Entity_Distributor ED) throws RemoteException {
        int result = 0;
        System.out.println("Method delete Distributor diakses secara remote");
        strSql = "DELETE FROM nob_distributor WHERE dis_id = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, ED.getDis_id());
            // jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DistributorDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
