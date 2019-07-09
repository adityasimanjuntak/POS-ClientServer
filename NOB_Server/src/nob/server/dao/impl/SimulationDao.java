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
import java.util.logging.Level;
import java.util.logging.Logger;
import nob.dao.api.ISimulationDao;
import nob.model.Entity_Simulation;

/**
 *
 * @author alimk
 */
public class SimulationDao extends UnicastRemoteObject implements ISimulationDao {

    private Connection conn = null;
    private String strSql = "";

    public SimulationDao(Connection conn) throws RemoteException {
        this.conn = conn;
    }

    @Override
    public String GenerateSimID(Entity_Simulation ES) throws RemoteException {
        String result = null;
        String x = "SIM-";
        System.out.println("Method Generate ID Simulation diakses secara remote");
        strSql = "SELECT * FROM nob_simulation ORDER BY sim_id DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String noreg = rs.getString("sim_id").substring(4);
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
            Logger.getLogger(SimulationDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int saveSimulasi(Entity_Simulation ES) throws RemoteException {
        int result = 0;
        System.out.println("Method saveSimulation diakses secara remote");
        strSql = "INSERT INTO nob_simulation(sim_id, sim_date, sim_added_by, sim_grand_total) VALUES (?, ?, ?, ?)";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, ES.getSim_id());
            ps.setDate(2, new Date(ES.getSim_date().getTime()));
            ps.setString(3, ES.getSim_added_by());
            ps.setInt(4, ES.getSim_grand_total());
            // jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SimulationDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int saveSimulationDetail(Entity_Simulation ES) throws RemoteException {
        int result = 0;
        System.out.println("Method saveSimulationDetail diakses secara remote");
        strSql = "INSERT INTO nob_simulation_detail(sim_code, sim_item, sim_item_price) VALUES (?, ?, ?)";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, ES.getSim_id());
            ps.setString(2, ES.getSim_item_name());
            ps.setInt(3, ES.getSim_item_price());
            // jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SimulationDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
