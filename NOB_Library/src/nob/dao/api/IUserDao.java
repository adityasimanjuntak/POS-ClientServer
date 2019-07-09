/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nob.dao.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import nob.model.Entity_Signin;
import nob.model.Entity_Signup;

/**
 *
 * @author alimk
 */
public interface IUserDao extends Remote {

    int saveDataAkun(Entity_Signup ESU) throws RemoteException;

    int saveDataLogin(Entity_Signin ESI) throws RemoteException;

    String GenerateRegistNumber(Entity_Signup ESU) throws RemoteException;

    String GenerateIDManajemen(Entity_Signup ESU) throws RemoteException;

    String GenerateIDKeu(Entity_Signup ESU) throws RemoteException;

    String GenerateIDHRD(Entity_Signup ESU) throws RemoteException;

    String GenerateIDCashier(Entity_Signup ESU) throws RemoteException;

    String GenerateIDWarehouse(Entity_Signup ESU) throws RemoteException;

    int updateUser(Entity_Signup ESU) throws RemoteException;

    int updateUserLogin(Entity_Signin ESI) throws RemoteException;

    int cariNoRegister(Entity_Signup ESU) throws RemoteException;

    List<Entity_Signup> getAll() throws RemoteException;

    List<Entity_Signup> getByName(String namaKyw) throws RemoteException;

    List<Entity_Signup> getByDept(String deptKyw) throws RemoteException;

    List<Entity_Signup> getByNoregKyw(String noregKyw) throws RemoteException;

    List<Entity_Signup> getByIdKyw(String idKyw) throws RemoteException;

    int deleteUser(Entity_Signup ESU) throws RemoteException;

    int deleteUserLogin(Entity_Signup ESU) throws RemoteException;

    String GenerateIDManajemen2(Entity_Signup ESU) throws RemoteException;

    String GenerateIDKeu2(Entity_Signup ESU) throws RemoteException;

    String GenerateIDHRD2(Entity_Signup ESU) throws RemoteException;

    String GenerateIDCashier2(Entity_Signup ESU) throws RemoteException;

    String GenerateIDWarehouse2(Entity_Signup ESU) throws RemoteException;

    String CountStaff() throws RemoteException;
   
}
