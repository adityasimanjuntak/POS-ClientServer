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
import java.util.logging.Level;
import java.util.logging.Logger;
import nob.dao.api.ILoginDao;
import nob.model.Entity_Department;
import nob.model.Entity_Signin;

/**
 *
 * @author alimk
 */
public class LoginDao extends UnicastRemoteObject implements ILoginDao {

    private Connection conn = null;
    private String strSql = "";

    public LoginDao(Connection conn) throws RemoteException {
        this.conn = conn;
    }

    @Override
    public int Login(Entity_Signin ESI) throws RemoteException {
        int result = 0;
        System.out.println("Method Login Aplikasi diakses secara remote");
        strSql = "Select EL.emp_signin_id, EL.emp_signin_password, EL.emp_status, E.emp_department FROM nob_emp_login EL, nob_emp E "
                + "Where EL.emp_noregister = E.emp_noregister AND EL.emp_signin_id = '" + ESI.getUsername() + "'";
        String Username, Password, DeptLetterCode, StatusAkun;
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                Username = rs.getString("emp_signin_id");
                Password = rs.getString("emp_signin_password");
                DeptLetterCode = rs.getString("emp_department");
                StatusAkun = rs.getString("emp_status");
                if (ESI.getUsername().equals(Username) && ESI.getPassword().equals(Password) && ESI.getStatus().equals(StatusAkun)) {
                    result = 1;
                }
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String CekNamaOnline(Entity_Signin ESI) throws RemoteException {
        String result = null;
        System.out.println("Method Cek Nama diakses secara remote");
        strSql = "Select emp_id, emp_name FROM nob_emp where emp_id = ?";
        String Username, Nama;
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ESI.getUsername());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                Username = rs.getString("emp_id");
                Nama = rs.getString("emp_name");
                if (ESI.getUsername().equals(Username)) {
                    result = Nama;
                }
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String CekClientStatus(String kodeSistem) throws RemoteException {
        String result = null;
        System.out.println("Method Cek Client Status diakses secara remote");
        strSql = "Select dep_sys_status from nob_department where dep_id = ?";
        String Status_Sistem = null;
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, kodeSistem);
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                Status_Sistem = rs.getString("dep_sys_status");
                result = Status_Sistem;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int UpdateStatusOnline(String kodeSistem) throws RemoteException {
        int result = 0;
        System.out.println("Method UpdateStatusOnline diakses secara remote");
        strSql = "Update nob_department Set dep_sys_status = 'Online' from nob_department where dep_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, kodeSistem);
            ps.executeUpdate();
            //jalankanperinah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SIMDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int UpdateStatusInvisible(String kodeSistem) throws RemoteException {
        int result = 0;
        System.out.println("Method UpdateStatusInvisible diakses secara remote");
        strSql = "Update nob_department Set dep_sys_status = 'Invisible' from nob_department where dep_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, kodeSistem);
            ps.executeUpdate();
            //jalankanperinah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SIMDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int UpdateStatusIdle(String kodeSistem) throws RemoteException {
        int result = 0;
        System.out.println("Method UpdateStatusOffline diakses secara remote");
        strSql = "Update nob_department Set dep_sys_status = 'Idle' from nob_department where dep_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, kodeSistem);
            ps.executeUpdate();
            //jalankanperinah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SIMDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int UpdateStatusOffline(String kodeSistem) throws RemoteException {
        int result = 0;
        System.out.println("Method UpdateStatusOffline diakses secara remote");
        strSql = "Update nob_department Set dep_sys_status = 'Offline' from nob_department where dep_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, kodeSistem);
            ps.executeUpdate();
            //jalankanperinah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SIMDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int UpdateStatusDisconnect(String kodeSistem) throws RemoteException {
        int result = 0;
        System.out.println("Method UpdateStatusOffline diakses secara remote");
        strSql = "Update nob_department Set dep_sys_status = 'Disconnect' from nob_department where dep_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, kodeSistem);
            ps.executeUpdate();
            //jalankanperinah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SIMDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int UpdateStatusServerOffline(String kodeSistem) throws RemoteException {
        int result = 0;
        System.out.println("Method UpdateStatusOffline diakses secara remote");
        strSql = "Update nob_department Set dep_sys_status = 'Server Offline' from nob_department where dep_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, kodeSistem);
            ps.executeUpdate();
            //jalankanperinah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SIMDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int UpdateStatusKilledbyServer(String kodeSistem) throws RemoteException {
        int result = 0;
        System.out.println("Method UpdateStatusOffline diakses secara remote");
        strSql = "Update nob_department Set dep_sys_status = 'Killed by Server' from nob_department where dep_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, kodeSistem);
            ps.executeUpdate();
            //jalankanperinah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SIMDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int UpdateIP(Entity_Signin ES, String kodeSistem) throws RemoteException {
        int result = 0;
        System.out.println("Method Set IP diakses secara remote");
        strSql = "Update nob_department Set dep_sys_ip = ? from nob_department where dep_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ES.getIp());
            ps.setString(2, kodeSistem);
            ps.executeUpdate();
            //jalankanperinah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SIMDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String CekIPServer() throws RemoteException {
        String result = null;
        System.out.println("Method Cek Client Status diakses secara remote");
        strSql = "Select dep_sys_ip from nob_department where dep_id = 'SERVER'";
        String IPServer = null;
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                IPServer = rs.getString("dep_sys_ip");
                result = IPServer;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
