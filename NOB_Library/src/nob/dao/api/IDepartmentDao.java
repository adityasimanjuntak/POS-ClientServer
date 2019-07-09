/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nob.dao.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import nob.model.Entity_Department;

/**
 *
 * @author alimk
 */
public interface IDepartmentDao extends Remote {

    int saveDataDepartment(Entity_Department ED) throws RemoteException;

    int updateStatusDepIn(Entity_Department ED) throws RemoteException;

    int updateStatusDepAc(Entity_Department ED) throws RemoteException;

    int updateStatusLoginInactive(Entity_Department ED) throws RemoteException;

    int updateStatusLoginActive(Entity_Department ED) throws RemoteException;

    List<Entity_Department> getAll() throws RemoteException;

    List<Entity_Department> getCbDepartment() throws RemoteException;
}
