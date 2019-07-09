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
import javax.swing.JOptionPane;
import nob.dao.api.IUserDao;
import nob.model.Entity_Signup;
import nob.model.Entity_Signin;

/**
 *
 * @author alimk
 */
public class UserDao extends UnicastRemoteObject implements IUserDao {

    private Connection conn = null;
    private String strSql = "";

    public UserDao(Connection conn) throws RemoteException {
        this.conn = conn;
    }

    @Override
    public int saveDataAkun(Entity_Signup ESU) throws RemoteException {
        int result = 0;
        System.out.println("Method Create Akun Karyawan diakses secara remote");
        strSql = "INSERT INTO nob_emp(emp_id, emp_noregister, emp_department, emp_name, emp_pob, emp_dob, emp_gend, emp_phone, "
                + "emp_mail, emp_photo, emp_qr) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, ESU.getId());
            ps.setString(2, ESU.getNoregister());
            ps.setString(3, ESU.getDepartment());
            ps.setString(4, ESU.getName());
            ps.setString(5, ESU.getPob());
            ps.setDate(6, new Date(ESU.getDob().getTime()));
            ps.setString(7, ESU.getGender());
            ps.setString(8, ESU.getPhone());
            ps.setString(9, ESU.getEmail());
            ps.setBytes(10, ESU.getFoto());
            ps.setBytes(11, ESU.getQr());
            // jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int saveDataLogin(Entity_Signin ESI) throws RemoteException {
        int result = 0;
        System.out.println("Method Save Data Karyawan diakses secara remote");
        strSql = "INSERT INTO nob_emp_login (emp_noregister, emp_signin_id, emp_signin_password, emp_status) VALUES (?, ?, ?, 'Active')";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, ESI.getRegister());
            ps.setString(2, ESI.getUsername());
            ps.setString(3, ESI.getPassword());
            // jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String GenerateRegistNumber(Entity_Signup ESU) throws RemoteException {
        String result = null;
        String x = "REG-";
        System.out.println("Method Generate Register Number diakses secara remote");
        strSql = "SELECT * FROM nob_emp ORDER BY emp_noregister DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String noreg = rs.getString("emp_noregister").substring(4);
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
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String GenerateIDManajemen(Entity_Signup ESU) throws RemoteException {
        String result = null;
        String x = "MGM-1515";
        System.out.println("Method Generate Register Number diakses secara remote");
        strSql = "SELECT * FROM nob_emp ORDER BY emp_id DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {

                String noreg = rs.getString("emp_id").substring(8);
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
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String GenerateIDKeu(Entity_Signup ESU) throws RemoteException {
        String result = null;
        String x = "KEU-1616";
        System.out.println("Method Generate Register Number diakses secara remote");
        strSql = "SELECT * FROM nob_emp ORDER BY emp_id DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {

                String noreg = rs.getString("emp_id").substring(8);
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
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String GenerateIDHRD(Entity_Signup ESU) throws RemoteException {
        String result = null;
        String x = "HRD-1717";
        System.out.println("Method Generate Register Number diakses secara remote");
        strSql = "SELECT * FROM nob_emp ORDER BY emp_id DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {

                String noreg = rs.getString("emp_id").substring(8);
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
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String GenerateIDCashier(Entity_Signup ESU) throws RemoteException {
        String result = null;
        String x = "CAS-1818";
        System.out.println("Method Generate Register Number diakses secara remote");
        strSql = "SELECT * FROM nob_emp ORDER BY emp_id DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {

                String noreg = rs.getString("emp_id").substring(8);
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
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String GenerateIDWarehouse(Entity_Signup ESU) throws RemoteException {
        String result = null;
        String x = "WRH-1919";
        System.out.println("Method Generate Register Number diakses secara remote");
        strSql = "SELECT * FROM nob_emp ORDER BY emp_id DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {

                String noreg = rs.getString("emp_id").substring(8);
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
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int updateUser(Entity_Signup ESU) throws RemoteException {
        int result = 0;
        System.out.println("Method update Ruangan diakses secara remote");
        strSql = "UPDATE nob_emp SET emp_id=?, emp_department=?, emp_name=?, emp_pob=?, emp_dob=?, emp_gend=?, "
                + "emp_phone=?, emp_mail=?, emp_photo=?, emp_qr=?"
                + "WHERE emp_noregister=?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, ESU.getId());
            ps.setString(2, ESU.getDepartment());
            ps.setString(3, ESU.getName());
            ps.setString(4, ESU.getPob());
            ps.setDate(5, new Date(ESU.getDob().getTime()));
            ps.setString(6, ESU.getGender());
            ps.setString(7, ESU.getPhone());
            ps.setString(8, ESU.getEmail());
            ps.setBytes(9, ESU.getFoto());
            ps.setBytes(10, ESU.getQr());
            ps.setString(11, ESU.getNoregister());
            // jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int updateUserLogin(Entity_Signin ESI) throws RemoteException {
        int result = 1;
        String SQL = "UPDATE nob_emp_login set emp_signin_id=?, emp_signin_password=?, emp_status = ? where emp_noregister=?";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, ESI.getUsername());
            ps.setString(2, ESI.getPassword());
            ps.setString(3, "Active");
            ps.setString(4, ESI.getRegister());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int cariNoRegister(Entity_Signup ESU) throws RemoteException {
        int result = 0;
        String Noreg;
        String strSql = "Select * from nob_emp where emp_noregister like '" + ESU.getNoregister() + "'";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Noreg = rs.getString("emp_noregister");
                if (ESU.getNoregister().equals(Noreg)) {
                    JOptionPane.showMessageDialog(null, "DATA ADA BOS!!", "Sign-In Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "CARIK APA ? GADA", "Sign-In Error", JOptionPane.ERROR_MESSAGE);
                }
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Signup> getAll() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Signup> daftarKaryawan = new ArrayList<Entity_Signup>();
        System.out.println("Method getAll Daftar Karyawan Event Organizer diakses secara remote");
        strSql = "SELECT emp_id, emp_noregister, emp_department, emp_name, emp_pob, emp_dob, emp_gend, emp_phone, emp_mail FROM nob_emp ORDER BY emp_noregister";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Signup ESU = new Entity_Signup();
                ESU.setId(rs.getString("emp_id"));
                ESU.setNoregister(rs.getString("emp_noregister"));
                ESU.setDepartment(rs.getString("emp_department"));
                ESU.setName(rs.getString("emp_name"));
                ESU.setPob(rs.getString("emp_pob"));
                ESU.setDob(rs.getDate("emp_dob"));
                ESU.setGender(rs.getString("emp_gend"));
                ESU.setPhone(rs.getString("emp_phone"));
                ESU.setEmail(rs.getString("emp_mail"));
                // simpan objek barang ke dalam objek class List
                daftarKaryawan.add(ESU);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarKaryawan;
    }

    @Override
    public List<Entity_Signup> getByName(String namaKyw) throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Signup> daftarKyw = new ArrayList<Entity_Signup>();
        System.out.println("Method getByNameKyw diakses secara remote");
        strSql = "SELECT emp_id, emp_noregister, emp_department, emp_name, emp_pob, emp_dob, emp_gend, emp_phone, emp_mail FROM nob_emp WHERE emp_name LIKE ? ORDER BY emp_noregister";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, "%" + namaKyw + "%");
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Signup ESU = new Entity_Signup();
                ESU.setId(rs.getString("emp_id"));
                ESU.setNoregister(rs.getString("emp_noregister"));
                ESU.setDepartment(rs.getString("emp_department"));
                ESU.setName(rs.getString("emp_name"));
                ESU.setPob(rs.getString("emp_pob"));
                ESU.setDob(rs.getDate("emp_dob"));
                ESU.setGender(rs.getString("emp_gend"));
                ESU.setPhone(rs.getString("emp_phone"));
                ESU.setEmail(rs.getString("emp_mail"));
                // simpan objek barang ke dalam objek class List
                daftarKyw.add(ESU);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarKyw;
    }

    @Override
    public List<Entity_Signup> getByDept(String deptKyw) throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Signup> daftarKyw = new ArrayList<Entity_Signup>();
        System.out.println("Method getByDivKyw diakses secara remote");
        strSql = "Select E.emp_id, E.emp_noregister, E.emp_department, E.emp_name, E.emp_pob, E.emp_dob, E.emp_gend, E.emp_phone, E.emp_mail, D.dep_description FROM nob_emp E \n"
                + "JOIN nob_department D ON D.dep_id = E.emp_department\n"
                + "WHERE E.emp_department LIKE ? OR D.dep_description LIKE ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, "%" + deptKyw + "%");
            ps.setString(2, "%" + deptKyw + "%");
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Signup ESU = new Entity_Signup();
                ESU.setId(rs.getString("emp_id"));
                ESU.setNoregister(rs.getString("emp_noregister"));
                ESU.setDepartment(rs.getString("emp_department"));
                ESU.setName(rs.getString("emp_name"));
                ESU.setPob(rs.getString("emp_pob"));
                ESU.setDob(rs.getDate("emp_dob"));
                ESU.setGender(rs.getString("emp_gend"));
                ESU.setPhone(rs.getString("emp_phone"));
                ESU.setEmail(rs.getString("emp_mail"));
                // simpan objek barang ke dalam objek class List
                daftarKyw.add(ESU);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarKyw;
    }

    @Override
    public List<Entity_Signup> getByNoregKyw(String noregKyw) throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Signup> daftarKyw = new ArrayList<Entity_Signup>();
        System.out.println("Method getByNoregKyw diakses secara remote");
        strSql = "SELECT emp_id, emp_noregister, emp_department, emp_name, emp_pob, emp_dob, emp_gend, emp_phone, emp_mail FROM nob_emp WHERE emp_noregister LIKE ? ORDER BY emp_noregister";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, "%" + noregKyw + "%");
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Signup ESU = new Entity_Signup();
                ESU.setId(rs.getString("emp_id"));
                ESU.setNoregister(rs.getString("emp_noregister"));
                ESU.setDepartment(rs.getString("emp_department"));
                ESU.setName(rs.getString("emp_name"));
                ESU.setPob(rs.getString("emp_pob"));
                ESU.setDob(rs.getDate("emp_dob"));
                ESU.setGender(rs.getString("emp_gend"));
                ESU.setPhone(rs.getString("emp_phone"));
                ESU.setEmail(rs.getString("emp_mail"));
                // simpan objek barang ke dalam objek class List
                daftarKyw.add(ESU);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarKyw;
    }

    @Override
    public List<Entity_Signup> getByIdKyw(String idKyw) throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Signup> daftarKyw = new ArrayList<Entity_Signup>();
        System.out.println("Method getByIdKyw diakses secara remote");
        strSql = "SELECT emp_id, emp_noregister, emp_department, emp_name, emp_pob, emp_dob, emp_gend, emp_phone, emp_mail FROM nob_emp WHERE emp_id LIKE ? ORDER BY emp_noregister";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, "%" + idKyw + "%");
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Signup ESU = new Entity_Signup();
                ESU.setId(rs.getString("emp_id"));
                ESU.setNoregister(rs.getString("emp_noregister"));
                ESU.setDepartment(rs.getString("emp_department"));
                ESU.setName(rs.getString("emp_name"));
                ESU.setPob(rs.getString("emp_pob"));
                ESU.setDob(rs.getDate("emp_dob"));
                ESU.setGender(rs.getString("emp_gend"));
                ESU.setPhone(rs.getString("emp_phone"));
                ESU.setEmail(rs.getString("emp_mail"));
                // simpan objek barang ke dalam objek class List
                daftarKyw.add(ESU);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarKyw;
    }

    @Override
    public int deleteUser(Entity_Signup ESU) throws RemoteException {
        int result = 0;
        System.out.println("Method delete Data User diakses secara remote");
        strSql = "DELETE FROM nob_emp WHERE emp_noregister = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, ESU.getNoregister());
            // jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int deleteUserLogin(Entity_Signup ESU) throws RemoteException {
        int result = 0;
        System.out.println("Method delete Data User diakses secara remote");
        strSql = "DELETE FROM nob_emp_login WHERE emp_noregister = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, ESU.getNoregister());
            // jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String GenerateIDManajemen2(Entity_Signup ESU) throws RemoteException {
        String result = null;
        String x = "MGM-1515";
        System.out.println("Method Generate Register Number diakses secara remote");
        strSql = "SELECT * FROM nob_emp ORDER BY emp_id DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {

                String noreg = rs.getString("emp_id").substring(8);
                String AN = "" + (Integer.parseInt(noreg));
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
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String GenerateIDKeu2(Entity_Signup ESU) throws RemoteException {
        String result = null;
        String x = "KEU-1616";
        System.out.println("Method Generate Register Number diakses secara remote");
        strSql = "SELECT * FROM nob_emp ORDER BY emp_id DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {

                String noreg = rs.getString("emp_id").substring(8);
                String AN = "" + (Integer.parseInt(noreg));
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
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String GenerateIDHRD2(Entity_Signup ESU) throws RemoteException {
        String result = null;
        String x = "HRD-1717";
        System.out.println("Method Generate Register Number diakses secara remote");
        strSql = "SELECT * FROM nob_emp ORDER BY emp_id DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {

                String noreg = rs.getString("emp_id").substring(8);
                String AN = "" + (Integer.parseInt(noreg));
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
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String GenerateIDCashier2(Entity_Signup ESU) throws RemoteException {
        String result = null;
        String x = "CAS-1818";
        System.out.println("Method Generate Register Number diakses secara remote");
        strSql = "SELECT * FROM nob_emp ORDER BY emp_id DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {

                String noreg = rs.getString("emp_id").substring(8);
                String AN = "" + (Integer.parseInt(noreg));
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
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String GenerateIDWarehouse2(Entity_Signup ESU) throws RemoteException {
        String result = null;
        String x = "WRH-1919";
        System.out.println("Method Generate Register Number diakses secara remote");
        strSql = "SELECT * FROM nob_emp ORDER BY emp_id DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {

                String noreg = rs.getString("emp_id").substring(8);
                String AN = "" + (Integer.parseInt(noreg));
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
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String CountStaff() throws RemoteException {
        String result = null;
        System.out.println("Method Hitung Staff diakses secara remote");
        strSql = "SELECT COUNT(*) From nob_emp";
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
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
