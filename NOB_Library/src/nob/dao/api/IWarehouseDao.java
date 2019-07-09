package nob.dao.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import nob.model.Entity_Bank;
import nob.model.Entity_Brand;
import nob.model.Entity_Category;
import nob.model.Entity_Distributor;
import nob.model.Entity_Transaction;
import nob.model.Entity_Voucher;
import nob.model.Entity_Warehouse;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author alimk
 */
public interface IWarehouseDao extends Remote {

    int CekItem(Entity_Warehouse EW) throws RemoteException;

    int CekStock(Entity_Warehouse EW) throws RemoteException;

    int saveItem(Entity_Warehouse EW) throws RemoteException;

    List<Entity_Warehouse> getCbCategory() throws RemoteException;

    List<Entity_Warehouse> getCbBrand() throws RemoteException;

    List<Entity_Warehouse> getCbSupplier() throws RemoteException;

    int updateStock(Entity_Warehouse EW) throws RemoteException;

    String getIDCategory(Entity_Warehouse EW) throws RemoteException;

    String getIDBrand(Entity_Warehouse EW) throws RemoteException;

    String getIDSupplier(Entity_Warehouse EW) throws RemoteException;

    List<Entity_Category> getAllCategory() throws RemoteException;

    String GenerateCatID(Entity_Category EC) throws RemoteException;

    int saveCategory(Entity_Category EC) throws RemoteException;

    int updateCategory(Entity_Category EC) throws RemoteException;

    int deleteCategory(Entity_Category EC) throws RemoteException;

    int deleteItem(Entity_Warehouse EW) throws RemoteException;

    String GenerateBrandID(Entity_Brand EB) throws RemoteException;

    List<Entity_Brand> getAllBrand() throws RemoteException;

    int saveBrand(Entity_Brand EB) throws RemoteException;

    int updateBrand(Entity_Brand EB) throws RemoteException;

    int deleteBrand(Entity_Brand EB) throws RemoteException;

    int CountTransactionPending() throws RemoteException;

    List<Entity_Transaction> getAllTransactionPending() throws RemoteException;

    List<Entity_Transaction> getAllListItemTransPending(String idTrans) throws RemoteException;

    int updateTrans_Success(Entity_Transaction ET) throws RemoteException;

    int updateTrans_Failed(Entity_Transaction ET) throws RemoteException;
    
    List<Entity_Bank> getAllBank() throws RemoteException;
    
    int saveBank(Entity_Bank EB) throws RemoteException;
    
    int updateBank(Entity_Bank EB) throws RemoteException;
    
    int deleteBank(Entity_Bank EB) throws RemoteException;
    
    List<Entity_Voucher> getAllVoucher() throws RemoteException;
    
    int saveVoucher(Entity_Voucher EV) throws RemoteException;
    
    int updateVoucher(Entity_Voucher EV) throws RemoteException;
    
    int deleteVoucher(Entity_Voucher EV) throws RemoteException;
    
    int CountTransactionSuccess() throws RemoteException;
    

}
