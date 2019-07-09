/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nob.dao.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import nob.model.Entity_Bank;
import nob.model.Entity_Distributor;
import nob.model.Entity_Item;
import nob.model.Entity_Piutang;
import nob.model.Entity_Transaction;

/**
 *
 * @author alimk
 */
public interface ITransactionDao extends Remote {

    String GenerateTransID(Entity_Transaction ETR) throws RemoteException;

    String CekNamaItem(Entity_Transaction ETR) throws RemoteException;

    int CekItemAvailable(Entity_Item EI) throws RemoteException;

    int CekStok(Entity_Transaction ETR) throws RemoteException;

    int CekPriceNTAItem(Entity_Transaction ETR) throws RemoteException;

    int CekPricePubItem(Entity_Transaction ETR) throws RemoteException;

    int CekPriceSingleItem(Entity_Transaction ETR) throws RemoteException;

    List<Entity_Item> getAll() throws RemoteException;

    int CekDiscountVoucher(Entity_Transaction ETR) throws RemoteException;

    int CekExpiredVoucher(Entity_Transaction ETR) throws RemoteException;

    Date CekTanggalVoucher(Entity_Transaction ETR) throws RemoteException;

    int CekStokVoucher(Entity_Transaction ETR) throws RemoteException;

    int CekVoucherAvailable(Entity_Transaction ETR) throws RemoteException;

    String CekNamaDistributor(Entity_Distributor ED) throws RemoteException;

    String CekEmailDistributor(Entity_Distributor ED) throws RemoteException;

    String CekPhoneDistributor(Entity_Distributor ED) throws RemoteException;

    int CekDistributorAvailable(Entity_Distributor ED) throws RemoteException;

    int CekDistributorActive(Entity_Distributor ED) throws RemoteException;

    List<Entity_Bank> getCbBank() throws RemoteException;

    List<Entity_Piutang> getCbPiutang() throws RemoteException;

    String getIDBank(Entity_Bank EB) throws RemoteException;

    String getIDPiutang(Entity_Piutang EP) throws RemoteException;

    int saveTransaksi(Entity_Transaction ETR) throws RemoteException;

    int saveTransaksi_Detail(Entity_Transaction ETR) throws RemoteException;

    int savePiutang(Entity_Piutang EP) throws RemoteException;

    int getNTA(Entity_Transaction ETR) throws RemoteException;

    List<Entity_Transaction> getListTransbyID(String idTrans) throws RemoteException;

    List<Entity_Transaction> getListTransbyIDCust(String idCust) throws RemoteException;

    List<Entity_Transaction> getListTransbyDate(String transDate) throws RemoteException;

    List<Entity_Transaction> getListTransbyAdmin(String transProcessby) throws RemoteException;

    List<Entity_Transaction> getListItembyName(String itemName) throws RemoteException;

    List<Entity_Transaction> getListItembyID(String itemID) throws RemoteException;

    List<Entity_Transaction> getListItembyCategory(String catName) throws RemoteException;

    List<Entity_Transaction> getListItembyBrand(String brandName) throws RemoteException;

    List<Entity_Transaction> getCbProcessorAMD() throws RemoteException;

    List<Entity_Transaction> getCbProcessorIntel() throws RemoteException;

    int getHargaProcAMD(Entity_Transaction ETR) throws RemoteException;

    int getHargaProcIntel(Entity_Transaction ETR) throws RemoteException;

    List<Entity_Transaction> getCbMotherboard() throws RemoteException;

    int getHargaMotherboard(Entity_Transaction ETR) throws RemoteException;

    List<Entity_Transaction> getCbSSD() throws RemoteException;

    int getHargaSSD(Entity_Transaction ETR) throws RemoteException;

    List<Entity_Transaction> getCbHDD() throws RemoteException;

    int getHargaHDD(Entity_Transaction ETR) throws RemoteException;

    List<Entity_Transaction> getCbRAM() throws RemoteException;

    int getHargaRAM(Entity_Transaction ETR) throws RemoteException;

    List<Entity_Transaction> getCbVGA() throws RemoteException;

    int getHargaVGA(Entity_Transaction ETR) throws RemoteException;

    List<Entity_Transaction> getCbCasing() throws RemoteException;

    int getHargaCasing(Entity_Transaction ETR) throws RemoteException;

    List<Entity_Transaction> getCbPSU() throws RemoteException;

    int getHargaPSU(Entity_Transaction ETR) throws RemoteException;

    List<Entity_Transaction> getCbLCD() throws RemoteException;

    int getHargaLCD(Entity_Transaction ETR) throws RemoteException;

    List<Entity_Transaction> getCbOptical() throws RemoteException;

    int getHargaOptical(Entity_Transaction ETR) throws RemoteException;

    List<Entity_Transaction> getCbKeyboard() throws RemoteException;

    int getHargaKeyboard(Entity_Transaction ETR) throws RemoteException;

    List<Entity_Transaction> getCbSpeaker() throws RemoteException;

    int getHargaSpeaker(Entity_Transaction ETR) throws RemoteException;

    List<Entity_Transaction> getCbHeadset() throws RemoteException;

    int getHargaHeadset(Entity_Transaction ETR) throws RemoteException;

    List<Entity_Transaction> getCbCPUCooler() throws RemoteException;

    int getHargaCPUCooler(Entity_Transaction ETR) throws RemoteException;

    List<Entity_Transaction> getCbCoolerFan() throws RemoteException;

    int getHargaCoolerFan(Entity_Transaction ETR) throws RemoteException;

    List<Entity_Transaction> getCbNetworking() throws RemoteException;

    int getHargaNetworking(Entity_Transaction ETR) throws RemoteException;

    String CountTransaction() throws RemoteException;

    String CountTransactionPending() throws RemoteException;

    String CountTaskCas() throws RemoteException;

    int updateDistributorLastTrans(Entity_Distributor ED) throws RemoteException;
}
