/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nob.dao.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import nob.model.Entity_Distributor;
import nob.model.Entity_Signup;

/**
 *
 * @author alimk
 */
public interface IDistributorDao extends Remote {

    String CountDisActive() throws RemoteException;

    int saveDistributor(Entity_Distributor ED) throws RemoteException;

    String GenerateDistributorID(Entity_Distributor ED) throws RemoteException;

    List<Entity_Distributor> getAll() throws RemoteException;

    String getIDAdmin(Entity_Signup ESU) throws RemoteException;

    int updateStatusDistributor_Active(Entity_Distributor ED) throws RemoteException;

    int updateStatusDistributor_inActive(Entity_Distributor ED) throws RemoteException;
    
    int deleteDistributor(Entity_Distributor ED) throws RemoteException;
}
