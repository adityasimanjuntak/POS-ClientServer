/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nob.dao.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import nob.model.Entity_Simulation;

/**
 *
 * @author alimk
 */
public interface ISimulationDao extends Remote {

    String GenerateSimID(Entity_Simulation ES) throws RemoteException;

    int saveSimulasi(Entity_Simulation ES) throws RemoteException;

    int saveSimulationDetail(Entity_Simulation ES) throws RemoteException;
}
