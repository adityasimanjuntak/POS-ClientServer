/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nob.dao.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import nob.model.Entity_Task;

/**
 *
 * @author alimk
 */
public interface ITaskDao extends Remote {

    int saveTugas(Entity_Task ET) throws RemoteException;

    String GenerateTaskID(Entity_Task ET) throws RemoteException;

    List<Entity_Task> getAll() throws RemoteException;

    int updateStatusTask_Done(Entity_Task ET) throws RemoteException;

    int updateStatusTask_UnDone(Entity_Task ET) throws RemoteException;

    int updateStatusTask_OnProgress(Entity_Task ET) throws RemoteException;

    int deleteTask(Entity_Task ET) throws RemoteException;

    String CountTask() throws RemoteException;

    String CountTaskWrh() throws RemoteException;
}
