/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nob.dao.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import nob.model.Entity_ErrorReport;
import nob.model.Entity_Signup;

/**
 *
 * @author alimk
 */
public interface IReportDao extends Remote {

    String GenerateErrorID(Entity_ErrorReport EER) throws RemoteException;

    List<Entity_ErrorReport> getAll() throws RemoteException;

    int saveError(Entity_ErrorReport EER) throws RemoteException;
    
    int deleteError(Entity_ErrorReport EER) throws RemoteException;
    
    String getIDAdminError(Entity_Signup ESU) throws RemoteException;
}
