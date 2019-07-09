/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nob.dao.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import nob.model.Entity_Signin;

/**
 *
 * @author alimk
 */
public interface ILoginDao extends Remote {

    int Login(Entity_Signin ESI) throws RemoteException;

    String CekNamaOnline(Entity_Signin ESI) throws RemoteException;

    String CekClientStatus(String kodeSistem) throws RemoteException;

    int UpdateStatusOnline(String kodeSistem) throws RemoteException;

    int UpdateStatusInvisible(String kodeSistem) throws RemoteException;

    int UpdateStatusIdle(String kodeSistem) throws RemoteException;

    int UpdateStatusOffline(String kodeSistem) throws RemoteException;

    int UpdateStatusDisconnect(String kodeSistem) throws RemoteException;

    int UpdateStatusServerOffline(String kodeSistem) throws RemoteException;

    int UpdateStatusKilledbyServer(String kodeSistem) throws RemoteException;

    int UpdateIP(Entity_Signin ES, String kodeSistem) throws RemoteException;
    
    String CekIPServer() throws RemoteException;
}
