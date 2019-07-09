/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nob.dao.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import nob.model.Entity_SIM;

/**
 *
 * @author alimk
 */
public interface ISimDao extends Remote {

    String GenerateReqIzinID(Entity_SIM ES) throws RemoteException;

    String GenerateReqActID(Entity_SIM ES) throws RemoteException;

    List<Entity_SIM> getListIzinbyEmp(String IDEmp) throws RemoteException;

    List<Entity_SIM> getListActbyEmp(String IDEmp) throws RemoteException;

    List<Entity_SIM> getCbTipeIzin() throws RemoteException;

    List<Entity_SIM> getCbTipeActivity() throws RemoteException;

    String getIDIzin(Entity_SIM ES) throws RemoteException;

    String getIDAct(Entity_SIM ES) throws RemoteException;

    int saveIzin(Entity_SIM ES) throws RemoteException;

    int saveActivity(Entity_SIM ES) throws RemoteException;

    int CountIzinSuccess(Entity_SIM ES) throws RemoteException;

    int CountIzinPending(Entity_SIM ES) throws RemoteException;

    int CountIzinRejected(Entity_SIM ES) throws RemoteException;

    int CountActSuccess(Entity_SIM ES) throws RemoteException;

    int CountActPending(Entity_SIM ES) throws RemoteException;

    int CountActRejected(Entity_SIM ES) throws RemoteException;

    int CountIzinSuccessAll() throws RemoteException;

    int CountIzinPendingAll() throws RemoteException;

    int CountIzinRejectedAll() throws RemoteException;

    int CountActSuccessAll() throws RemoteException;

    int CountActPendingAll() throws RemoteException;

    int CountActRejectedAll() throws RemoteException;
    
    List<Entity_SIM> getListIzinAll() throws RemoteException;
    
    List<Entity_SIM> getListActAll() throws RemoteException;
    
    int updateApproveIzin(Entity_SIM ES) throws RemoteException;
    
    int updateRejectIzin(Entity_SIM ES) throws RemoteException;
    
    int updateApproveAct(Entity_SIM ES) throws RemoteException;
    
    int updateRejectAct(Entity_SIM ES) throws RemoteException;
}
