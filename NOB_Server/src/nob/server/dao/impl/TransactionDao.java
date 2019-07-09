/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nob.server.dao.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nob.dao.api.ITransactionDao;
import nob.model.Entity_Bank;
import nob.model.Entity_Distributor;
import nob.model.Entity_Item;
import nob.model.Entity_Piutang;
import nob.model.Entity_Signup;
import nob.model.Entity_Transaction;

/**
 *
 * @author alimk
 */
public class TransactionDao extends UnicastRemoteObject implements ITransactionDao {

    private Connection conn = null;
    private String strSql = "";

    public TransactionDao(Connection conn) throws RemoteException {
        this.conn = conn;
    }

    @Override
    public String GenerateTransID(Entity_Transaction ETR) throws RemoteException {
        String result = null;
        String x = "NOBTSC-";
        System.out.println("Method Generate ID Transaksi diakses secara remote");
        strSql = "SELECT * FROM nob_transaction ORDER BY trans_id DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String noreg = rs.getString("trans_id").substring(7);
                String AN = "" + (Integer.parseInt(noreg) + 1);
                String nol = "";
                switch (AN.length()) {
                    case 1:
                        nol = "00";
                        break;
                    case 2:
                        nol = "0";
                        break;
                    case 3:
                        nol = "";
                        break;
                    default:
                        break;
                }
                result = x + nol + AN;
            } else {
                result = x + "00" + "1";
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String CekNamaItem(Entity_Transaction ETR) throws RemoteException {
        String result = null;
        System.out.println("Method Cek Nama Item diakses secara remote");
        strSql = "Select li_iditem, li_name FROM nob_list_item where li_iditem = ?";
        String ItemID, NamaItem;
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getItem_id());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                ItemID = rs.getString("li_iditem");
                NamaItem = rs.getString("li_name");
                if (ETR.getItem_id().equals(ItemID)) {
                    result = NamaItem;
                }
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int CekItemAvailable(Entity_Item EI) throws RemoteException {
        int result = 0;
        String itemCode = null;
        System.out.println("Method Cek Item Available diakses secara remote");
        strSql = "Select li_iditem FROM nob_list_item where li_iditem = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, EI.getItem_id());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                itemCode = rs.getString("li_iditem");
                if (EI.getItem_id().equals(itemCode)) {
                    result = 1;
                } else {
                    result = 0;
                }
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int CekPriceNTAItem(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        System.out.println("Method Cek Nama Item diakses secara remote");
        strSql = "Select li_iditem, li_price_nta FROM nob_list_item where li_iditem = ?";
        String ItemID;
        int NTAItem;
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getItem_id());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                ItemID = rs.getString("li_iditem");
                NTAItem = rs.getInt("li_price_NTA");
                if (ETR.getItem_id().equals(ItemID)) {
                    result = NTAItem;
                }
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int CekPricePubItem(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        System.out.println("Method Cek Nama Item diakses secara remote");
        strSql = "Select li_iditem, li_price_pub FROM nob_list_item where li_iditem = ?";
        String ItemID;
        int PubItem;
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getItem_id());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                ItemID = rs.getString("li_iditem");
                PubItem = rs.getInt("li_price_pub");
                if (ETR.getItem_id().equals(ItemID)) {
                    result = PubItem;
                }
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int CekPriceSingleItem(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        System.out.println("Method Cek Nama Item diakses secara remote");
        strSql = "Select li_iditem, li_price_single FROM nob_list_item where li_iditem = ?";
        String ItemID;
        int SingleItem;
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getItem_id());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                ItemID = rs.getString("li_iditem");
                SingleItem = rs.getInt("li_price_single");
                if (ETR.getItem_id().equals(ItemID)) {
                    result = SingleItem;
                }
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int CekStok(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        int Stok = 0;
        System.out.println("Method Cek Stok diakses secara remote");
        strSql = "Select li_stock FROM nob_list_item where li_iditem = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getItem_id());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                Stok = rs.getInt("li_stock");
                if (ETR.getQuantity() < Stok) {
                    result = 1;
                } else {
                    result = 0;
                }
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Item> getAll() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Item> daftarItem = new ArrayList<Entity_Item>();
        System.out.println("Method getAll Daftar Item diakses secara remote");
        strSql = "SELECT li_price_nta, li_price_pub, li_price_single FROM nob_list_item";
        try {
            // buat objek PreparedStatement
            Entity_Item EI = new Entity_Item();
            PreparedStatement ps = conn.prepareStatement(strSql);
//            ps.setString(1, EI.getItem_id());
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // mapping objek ResultSet ke object barang
                EI.setItem_price_NTA(rs.getInt("li_price_nta"));
                EI.setItem_price_Pub(rs.getInt("li_price_pub"));
                EI.setItem_price_Single(rs.getInt("li_price_single"));
                // simpan objek barang ke dalam objek class List
                daftarItem.add(EI);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarItem;
    }

    @Override
    public int CekDiscountVoucher(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        int Discount = 0;
        System.out.println("Method Cek Voucher diakses secara remote");
        strSql = "Select vou_disc FROM nob_voucher where vou_id = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getVou_id());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                Discount = rs.getInt("vou_disc");
                result = Discount;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int CekExpiredVoucher(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        LocalDate localDate = LocalDate.now();
        java.sql.Date DateNow = java.sql.Date.valueOf(localDate);
        java.sql.Date DateVoucher;

        System.out.println("Method Cek Expired diakses secara remote");
        strSql = "Select vou_date_exp FROM nob_voucher where vou_id = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getVou_id());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                DateVoucher = java.sql.Date.valueOf(rs.getString("vou_date_exp"));
                if (DateNow.after(DateVoucher)) {
                    result = 1;
                    //Tanggal Hari ini sesudah Tanggal Voucher
                    //Expired
                } else if (DateNow.before(DateVoucher)) {
                    result = 2;
                    //Tanggal Hari ini sebelum Tanggal Voucher
                    //Tidak Expired
                } else if (DateNow.equals(DateVoucher)) {
                    result = 3;
                    //Tanggal Hari ini masih sama dengan Tanggal Voucher
                    //Tidak Expired
                }
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public Date CekTanggalVoucher(Entity_Transaction ETR) throws RemoteException {
        Date result = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.now();
        java.sql.Date DateNow = java.sql.Date.valueOf(localDate);
        java.sql.Date DateVoucher;

        System.out.println("Method Cek Expired diakses secara remote");
        strSql = "Select vou_date_exp FROM nob_voucher where vou_id = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getVou_id());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                DateVoucher = java.sql.Date.valueOf(rs.getString("vou_date_exp"));
                result = DateVoucher;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int CekStokVoucher(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        int Stok = 0;
        System.out.println("Method Cek Stok Voucher diakses secara remote");
        strSql = "Select vou_stock FROM nob_voucher where vou_id = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getVou_id());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                Stok = rs.getInt("vou_stock");
                if (Stok >= 1) {
                    result = 1;
                } else {
                    result = 0;
                }
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int CekVoucherAvailable(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        String VoucherCode = null;
        System.out.println("Method Cek Voucher Available diakses secara remote");
        strSql = "Select vou_id FROM nob_voucher where vou_id = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getVou_id());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                VoucherCode = rs.getString("vou_id");
                if (ETR.getVou_id().equals(VoucherCode)) {
                    result = 1;
                } else {
                    result = 0;
                }
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String CekNamaDistributor(Entity_Distributor ED) throws RemoteException {
        String result = null;
        String nama_distributor = null;
        System.out.println("Method Cek Nama Distributor diakses secara remote");
        strSql = "Select dis_name FROM nob_distributor where dis_id = ? and dis_status = 'Active'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ED.getDis_id());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                nama_distributor = rs.getString("dis_name");
                result = nama_distributor;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String CekEmailDistributor(Entity_Distributor ED) throws RemoteException {
        String result = null;
        String email_distributor = null;
        System.out.println("Method Cek Email Distributor diakses secara remote");
        strSql = "Select dis_email FROM nob_distributor where dis_id = ? and dis_status = 'Active'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ED.getDis_id());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                email_distributor = rs.getString("dis_email");
                result = email_distributor;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String CekPhoneDistributor(Entity_Distributor ED) throws RemoteException {
        String result = null;
        String phone_distributor = null;
        System.out.println("Method Cek Phone Distributor diakses secara remote");
        strSql = "Select dis_phone FROM nob_distributor where dis_id = ? and dis_status = 'Active'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ED.getDis_id());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                phone_distributor = rs.getString("dis_phone");
                result = phone_distributor;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int CekDistributorAvailable(Entity_Distributor ED) throws RemoteException {
        int result = 0;
        String distributorCode = null;
        System.out.println("Method Cek Distributor Available diakses secara remote");
        strSql = "Select dis_id FROM nob_distributor where dis_id = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ED.getDis_id());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                distributorCode = rs.getString("dis_id");
                if (ED.getDis_id().equals(distributorCode)) {
                    result = 1;
                } else {
                    result = 0;
                }
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int CekDistributorActive(Entity_Distributor ED) throws RemoteException {
        int result = 0;
        String distributorCode = null;
        System.out.println("Method Cek Distributor Active diakses secara remote");
        strSql = "Select dis_id FROM nob_distributor where dis_id = ? and dis_status = 'Active'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ED.getDis_id());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                distributorCode = rs.getString("dis_id");
                if (ED.getDis_id().equals(distributorCode)) {
                    result = 1;
                } else {
                    result = 0;
                }
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Bank> getCbBank() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Bank> daftarCbBank = new ArrayList<Entity_Bank>();
        System.out.println("Method getCbBank diakses secara remote");
        strSql = "SELECT bank_name FROM nob_bank where bank_status = 'Active'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Bank EB = new Entity_Bank();
                EB.setBank_name(rs.getString("bank_name"));
                // simpan objek barang ke dalam objek class List
                daftarCbBank.add(EB);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarCbBank;
    }

    @Override
    public List<Entity_Piutang> getCbPiutang() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Piutang> daftarCbPiutang = new ArrayList<Entity_Piutang>();
        System.out.println("Method getCbPiutang diakses secara remote");
        strSql = "Select P.piutang_by, E.emp_name, P.piutang_by_status  FROM nob_emp E \n"
                + "JOIN nob_piutang P ON P.Piutang_by = E.emp_id\n"
                + "WHERE P.piutang_by_status = 'Active'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Piutang EP = new Entity_Piutang();
                EP.setPiutang_by(rs.getString("emp_name"));
                // simpan objek barang ke dalam objek class List
                daftarCbPiutang.add(EP);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarCbPiutang;
    }

    @Override
    public String getIDBank(Entity_Bank EB) throws RemoteException {
        String result = null;
        String id_bank = null;
        System.out.println("Method getIDBank diakses secara remote");
        strSql = "SELECT bank_id FROM nob_bank where bank_status = 'Active' and bank_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, EB.getBank_name());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                id_bank = rs.getString("bank_id");
                result = id_bank;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String getIDPiutang(Entity_Piutang EP) throws RemoteException {
        String result = null;
        String id_piutang = null;
        System.out.println("Method getIDPiutang diakses secara remote");
        strSql = "Select P.piutang_id, E.emp_name  FROM nob_emp E \n"
                + "JOIN nob_piutang P ON P.Piutang_by = E.emp_id\n"
                + "WHERE P.piutang_by_status = 'Active' and E.emp_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, EP.getPiutang_by());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                id_piutang = rs.getString("piutang_id");
                result = id_piutang;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int saveTransaksi(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        System.out.println("Method saveTransaksi diakses secara remote");
        strSql = "INSERT INTO nob_transaction(trans_id, trans_date, trans_processby, trans_updateby, trans_id_cust, "
                + "trans_paymentmethod, trans_total_NTA, trans_total, trans_voucher, trans_discount, trans_payment, "
                + "trans_bank, trans_payment_desc, trans_status, trans_qr) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, ETR.getTrans_id());
            ps.setDate(2, new Date(ETR.getTrans_date().getTime()));
            ps.setString(3, ETR.getTrans_processBy());
            ps.setString(4, ETR.getTrans_updateBy());
            ps.setString(5, ETR.getTrans_cust_id());
            ps.setString(6, ETR.getTrans_paymentVia());
            ps.setDouble(7, ETR.getTrans_NTA());
            ps.setDouble(8, ETR.getTrans_totalPayment());
            ps.setString(9, ETR.getVou_id());
            ps.setDouble(10, ETR.getTrans_discount());
            ps.setString(11, ETR.getTrans_paymentType());
            ps.setString(12, ETR.getBank_id());
            ps.setString(13, ETR.getTrans_paymentDesc());
            ps.setString(14, ETR.getTrans_status());
            ps.setBytes(15, ETR.getTrans_qr());
            // jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int saveTransaksi_Detail(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        System.out.println("Method saveTransaksi_Detail diakses secara remote");
        strSql = "INSERT INTO nob_transaction_detail (id_trans, id_item, item_name, item_quantity, "
                + "item_price, item_price_subtotal) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, ETR.getTrans_id());
            ps.setString(2, ETR.getItem_id());
            ps.setString(3, ETR.getItem_name());
            ps.setString(4, ETR.getItem_qty_string());
            ps.setString(5, ETR.getItem_price_string());
            ps.setString(6, ETR.getItem_subtotal_string());
            // jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int savePiutang(Entity_Piutang EP) throws RemoteException {
        int result = 0;
        System.out.println("Method savePiutang diakses secara remote");
        strSql = "INSERT INTO nob_piutang_detail(piutang_id_detail,piutang_date, piutang_transaction,"
                + " piutang_total, piutang_status) VALUES (?, ?, ?, ?, ?)";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, EP.getPiutang_id());
            ps.setDate(2, new Date(EP.getPiutang_date().getTime()));
            ps.setString(3, EP.getPiutang_transaction());
            ps.setDouble(4, EP.getPiutang_total());
            ps.setString(5, EP.getPiutang_by_status());
            // jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int getNTA(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        int NTA = 0;
        System.out.println("Method getIDPiutang diakses secara remote");
        strSql = "Select li_price_nta from nob_list_item where li_iditem = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getItem_id());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                NTA = rs.getInt("li_price_nta");
                result = NTA;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Transaction> getListTransbyID(String idTrans) throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarTrans = new ArrayList<Entity_Transaction>();
        System.out.println("Method getListTransbyID diakses secara remote");
        strSql = "Select TD.id_trans, TD.id_item, T.trans_date, T.trans_processby, T.trans_id_cust, TD.item_name, "
                + "TD.item_quantity, TD.item_price, TD.item_price_subtotal, T.trans_total, T.trans_status "
                + "FROM nob_transaction_detail TD\n"
                + "JOIN  nob_Transaction T ON T.trans_id = TD.id_trans \n"
                + "Where TD.id_trans = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, idTrans);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setTrans_id(rs.getString("id_trans"));
                ETR.setItem_id(rs.getString("id_item"));
                ETR.setTrans_date(rs.getDate("trans_date"));
                ETR.setTrans_processBy(rs.getString("trans_processby"));
                ETR.setDis_id(rs.getString("trans_id_cust"));
                ETR.setItem_name(rs.getString("item_name"));
                ETR.setQuantity(rs.getInt("item_quantity"));
                ETR.setItem_price(rs.getInt("item_price"));
                ETR.setItem_price_subtotal(rs.getInt("item_price_subtotal"));
                ETR.setTrans_totalPayment(rs.getInt("trans_total"));
                ETR.setTrans_status(rs.getString("trans_status"));
                // simpan objek barang ke dalam objek class List
                daftarTrans.add(ETR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarTrans;
    }

    @Override
    public List<Entity_Transaction> getListTransbyIDCust(String idCust) throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarTrans = new ArrayList<Entity_Transaction>();
        System.out.println("Method getListTransbyID diakses secara remote");
        strSql = "Select TD.id_trans, TD.id_item, T.trans_date, T.trans_processby, T.trans_id_cust, TD.item_name, "
                + "TD.item_quantity, TD.item_price, TD.item_price_subtotal, T.trans_total, T.trans_status "
                + "FROM nob_transaction_detail TD\n"
                + "JOIN  nob_Transaction T ON T.trans_id = TD.id_trans \n"
                + "Where T.trans_id_cust = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, idCust);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setTrans_id(rs.getString("id_trans"));
                ETR.setItem_id(rs.getString("id_item"));
                ETR.setTrans_date(rs.getDate("trans_date"));
                ETR.setTrans_processBy(rs.getString("trans_processby"));
                ETR.setDis_id(rs.getString("trans_id_cust"));
                ETR.setItem_name(rs.getString("item_name"));
                ETR.setQuantity(rs.getInt("item_quantity"));
                ETR.setItem_price(rs.getInt("item_price"));
                ETR.setItem_price_subtotal(rs.getInt("item_price_subtotal"));
                ETR.setTrans_totalPayment(rs.getInt("trans_total"));
                ETR.setTrans_status(rs.getString("trans_status"));
                // simpan objek barang ke dalam objek class List
                daftarTrans.add(ETR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarTrans;
    }

    @Override
    public List<Entity_Transaction> getListTransbyDate(String transDate) throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarTrans = new ArrayList<Entity_Transaction>();
        System.out.println("Method getListTransbyID diakses secara remote");
        strSql = "Select TD.id_trans, TD.id_item, T.trans_date, T.trans_processby, T.trans_id_cust, TD.item_name, "
                + "TD.item_quantity, TD.item_price, TD.item_price_subtotal, T.trans_total, T.trans_status "
                + "FROM nob_transaction_detail TD\n"
                + "JOIN  nob_Transaction T ON T.trans_id = TD.id_trans \n"
                + "Where T.trans_date = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, transDate);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setTrans_id(rs.getString("id_trans"));
                ETR.setItem_id(rs.getString("id_item"));
                ETR.setTrans_date(rs.getDate("trans_date"));
                ETR.setTrans_processBy(rs.getString("trans_processby"));
                ETR.setDis_id(rs.getString("trans_id_cust"));
                ETR.setItem_name(rs.getString("item_name"));
                ETR.setQuantity(rs.getInt("item_quantity"));
                ETR.setItem_price(rs.getInt("item_price"));
                ETR.setItem_price_subtotal(rs.getInt("item_price_subtotal"));
                ETR.setTrans_totalPayment(rs.getInt("trans_total"));
                ETR.setTrans_status(rs.getString("trans_status"));
                // simpan objek barang ke dalam objek class List
                daftarTrans.add(ETR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarTrans;
    }

    @Override
    public List<Entity_Transaction> getListTransbyAdmin(String transProcessby) throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarTrans = new ArrayList<Entity_Transaction>();
        System.out.println("Method getListTransbyID diakses secara remote");
        strSql = "Select TD.id_trans, TD.id_item, T.trans_date, T.trans_processby, T.trans_id_cust, TD.item_name, "
                + "TD.item_quantity, TD.item_price, TD.item_price_subtotal, T.trans_total, T.trans_status "
                + "FROM nob_transaction_detail TD\n"
                + "JOIN  nob_Transaction T ON T.trans_id = TD.id_trans \n"
                + "Where T.trans_processby = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, transProcessby);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setTrans_id(rs.getString("id_trans"));
                ETR.setItem_id(rs.getString("id_item"));
                ETR.setTrans_date(rs.getDate("trans_date"));
                ETR.setTrans_processBy(rs.getString("trans_processby"));
                ETR.setDis_id(rs.getString("trans_id_cust"));
                ETR.setItem_name(rs.getString("item_name"));
                ETR.setQuantity(rs.getInt("item_quantity"));
                ETR.setItem_price(rs.getInt("item_price"));
                ETR.setItem_price_subtotal(rs.getInt("item_price_subtotal"));
                ETR.setTrans_totalPayment(rs.getInt("trans_total"));
                ETR.setTrans_status(rs.getString("trans_status"));
                // simpan objek barang ke dalam objek class List
                daftarTrans.add(ETR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarTrans;
    }

    @Override
    public List<Entity_Transaction> getListItembyName(String itemName) throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarItem = new ArrayList<Entity_Transaction>();
        System.out.println("Method getListItembyName diakses secara remote");
        strSql = "Select LI.li_iditem, LI.li_name, LI.li_categories, LI.li_brand, LI.Li_guarantee, LI.Li_stock, LI.Li_info, LI.Li_price_pub, LI.Li_price_single FROM nob_list_item LI\n"
                + "JOIN nob_item_categories C ON C.cat_id = LI.li_categories\n"
                + "JOIN nob_item_brand B ON B.brand_id = LI.li_brand\n"
                + "Where LI.li_name LIKE ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, "%" + itemName + "%");
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_id(rs.getString("li_iditem"));
                ETR.setItem_name(rs.getString("li_name"));
                ETR.setItem_category(rs.getString("li_categories"));
                ETR.setItem_brand(rs.getString("li_brand"));
                ETR.setItem_guarantee(rs.getString("li_guarantee"));
                ETR.setStock(rs.getInt("li_stock"));
                ETR.setItem_info(rs.getString("li_info"));
                ETR.setItem_price_Pub(rs.getInt("li_price_pub"));
                ETR.setItem_price_Single(rs.getInt("li_price_single"));
                // simpan objek barang ke dalam objek class List
                daftarItem.add(ETR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarItem;
    }

    @Override
    public List<Entity_Transaction> getListItembyID(String itemID) throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarItem = new ArrayList<Entity_Transaction>();
        System.out.println("Method getListItembyName diakses secara remote");
        strSql = "Select LI.li_iditem, LI.li_name, LI.li_categories, LI.li_brand, LI.Li_guarantee, LI.Li_stock, LI.Li_info, LI.Li_price_pub, LI.Li_price_single FROM nob_list_item LI\n"
                + "JOIN nob_item_categories C ON C.cat_id = LI.li_categories\n"
                + "JOIN nob_item_brand B ON B.brand_id = LI.li_brand\n"
                + "Where LI.li_iditem = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, itemID);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_id(rs.getString("li_iditem"));
                ETR.setItem_name(rs.getString("li_name"));
                ETR.setItem_category(rs.getString("li_categories"));
                ETR.setItem_brand(rs.getString("li_brand"));
                ETR.setItem_guarantee(rs.getString("li_guarantee"));
                ETR.setStock(rs.getInt("li_stock"));
                ETR.setItem_info(rs.getString("li_info"));
                ETR.setItem_price_Pub(rs.getInt("li_price_pub"));
                ETR.setItem_price_Single(rs.getInt("li_price_single"));
                // simpan objek barang ke dalam objek class List
                daftarItem.add(ETR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarItem;
    }

    @Override
    public List<Entity_Transaction> getListItembyCategory(String catName) throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarItem = new ArrayList<Entity_Transaction>();
        System.out.println("Method getListItembyCategory diakses secara remote");
        strSql = "Select LI.li_iditem, LI.li_name, LI.li_categories, LI.li_brand, LI.Li_guarantee, LI.Li_stock, LI.Li_info, LI.Li_price_pub, LI.Li_price_single FROM nob_list_item LI\n"
                + "JOIN nob_item_categories C ON C.cat_id = LI.li_categories\n"
                + "JOIN nob_item_brand B ON B.brand_id = LI.li_brand\n"
                + "Where C.cat_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, catName);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_id(rs.getString("li_iditem"));
                ETR.setItem_name(rs.getString("li_name"));
                ETR.setItem_category(rs.getString("li_categories"));
                ETR.setItem_brand(rs.getString("li_brand"));
                ETR.setItem_guarantee(rs.getString("li_guarantee"));
                ETR.setStock(rs.getInt("li_stock"));
                ETR.setItem_info(rs.getString("li_info"));
                ETR.setItem_price_Pub(rs.getInt("li_price_pub"));
                ETR.setItem_price_Single(rs.getInt("li_price_single"));
                // simpan objek barang ke dalam objek class List
                daftarItem.add(ETR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarItem;
    }

    @Override
    public List<Entity_Transaction> getListItembyBrand(String brandName) throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarItem = new ArrayList<Entity_Transaction>();
        System.out.println("Method getListItembybrand diakses secara remote");
        strSql = "Select LI.li_iditem, LI.li_name, LI.li_categories, LI.li_brand, LI.Li_guarantee, LI.Li_stock, LI.Li_info, LI.Li_price_pub, LI.Li_price_single FROM nob_list_item LI\n"
                + "JOIN nob_item_categories C ON C.cat_id = LI.li_categories\n"
                + "JOIN nob_item_brand B ON B.brand_id = LI.li_brand\n"
                + "Where B.brand_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, brandName);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_id(rs.getString("li_iditem"));
                ETR.setItem_name(rs.getString("li_name"));
                ETR.setItem_category(rs.getString("li_categories"));
                ETR.setItem_brand(rs.getString("li_brand"));
                ETR.setItem_guarantee(rs.getString("li_guarantee"));
                ETR.setStock(rs.getInt("li_stock"));
                ETR.setItem_info(rs.getString("li_info"));
                ETR.setItem_price_Pub(rs.getInt("li_price_pub"));
                ETR.setItem_price_Single(rs.getInt("li_price_single"));
                // simpan objek barang ke dalam objek class List
                daftarItem.add(ETR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarItem;
    }

    @Override
    public List<Entity_Transaction> getCbProcessorAMD() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarProcessorAMD = new ArrayList<Entity_Transaction>();
        System.out.println("Method getCbProcessorAMD diakses secara remote");
        strSql = "SELECT LI.li_name from nob_list_item LI\n"
                + "JOIN nob_item_categories C ON C.cat_id = LI.li_categories\n"
                + "JOIN nob_item_brand B ON B.brand_id = LI.li_brand\n"
                + "Where B.brand_name = 'AMD' AND C.cat_name = 'Processor'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name(rs.getString("li_name"));
                // simpan objek barang ke dalam objek class List
                daftarProcessorAMD.add(ETR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarProcessorAMD;
    }

    @Override
    public List<Entity_Transaction> getCbProcessorIntel() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarProcessorIntel = new ArrayList<Entity_Transaction>();
        System.out.println("Method getCbProcessorAMD diakses secara remote");
        strSql = "SELECT LI.li_name from nob_list_item LI\n"
                + "JOIN nob_item_categories C ON C.cat_id = LI.li_categories\n"
                + "JOIN nob_item_brand B ON B.brand_id = LI.li_brand\n"
                + "Where B.brand_name = 'Intel' AND C.cat_name = 'Processor'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name(rs.getString("li_name"));
                // simpan objek barang ke dalam objek class List
                daftarProcessorIntel.add(ETR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarProcessorIntel;
    }

    @Override
    public int getHargaProcAMD(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        int hargaProcAMD = 0;
        System.out.println("Method getHargaProcAMD diakses secara remote");
        strSql = "SELECT li_price_single FROM nob_list_item where li_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getItem_name());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                hargaProcAMD = rs.getInt("li_price_single");
                result = hargaProcAMD;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int getHargaProcIntel(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        int hargaProcIntel = 0;
        System.out.println("Method getHargaProcAMD diakses secara remote");
        strSql = "SELECT li_price_single FROM nob_list_item where li_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getItem_name());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                hargaProcIntel = rs.getInt("li_price_single");
                result = hargaProcIntel;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Transaction> getCbMotherboard() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarMotherboard = new ArrayList<Entity_Transaction>();
        System.out.println("Method getCbProcessorAMD diakses secara remote");
        strSql = "SELECT LI.li_name from nob_list_item LI\n"
                + "JOIN nob_item_categories C ON C.cat_id = LI.li_categories\n"
                + "JOIN nob_item_brand B ON B.brand_id = LI.li_brand\n"
                + "Where C.cat_name = 'Motherboard'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name(rs.getString("li_name"));
                // simpan objek barang ke dalam objek class List
                daftarMotherboard.add(ETR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarMotherboard;
    }

    @Override
    public int getHargaMotherboard(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        int hargaMotherboard = 0;
        System.out.println("Method getHargaMotherboard diakses secara remote");
        strSql = "SELECT li_price_single FROM nob_list_item where li_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getItem_name());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                hargaMotherboard = rs.getInt("li_price_single");
                result = hargaMotherboard;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Transaction> getCbSSD() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarSSD = new ArrayList<Entity_Transaction>();
        System.out.println("Method getCbSSD diakses secara remote");
        strSql = "SELECT LI.li_name from nob_list_item LI\n"
                + "JOIN nob_item_categories C ON C.cat_id = LI.li_categories\n"
                + "JOIN nob_item_brand B ON B.brand_id = LI.li_brand\n"
                + "Where C.cat_name = 'Solid State Drive'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name(rs.getString("li_name"));
                // simpan objek barang ke dalam objek class List
                daftarSSD.add(ETR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarSSD;
    }

    @Override
    public int getHargaSSD(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        int hargaSSD = 0;
        System.out.println("Method getHargaSSD diakses secara remote");
        strSql = "SELECT li_price_single FROM nob_list_item where li_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getItem_name());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                hargaSSD = rs.getInt("li_price_single");
                result = hargaSSD;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Transaction> getCbHDD() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarHDD = new ArrayList<Entity_Transaction>();
        System.out.println("Method getCbHDD diakses secara remote");
        strSql = "SELECT LI.li_name from nob_list_item LI\n"
                + "JOIN nob_item_categories C ON C.cat_id = LI.li_categories\n"
                + "JOIN nob_item_brand B ON B.brand_id = LI.li_brand\n"
                + "Where C.cat_name = 'Harddisk'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name(rs.getString("li_name"));
                // simpan objek barang ke dalam objek class List
                daftarHDD.add(ETR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarHDD;
    }

    @Override
    public int getHargaHDD(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        int hargaHDD = 0;
        System.out.println("Method getHargaHDD diakses secara remote");
        strSql = "SELECT li_price_single FROM nob_list_item where li_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getItem_name());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                hargaHDD = rs.getInt("li_price_single");
                result = hargaHDD;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Transaction> getCbRAM() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarRAM = new ArrayList<Entity_Transaction>();
        System.out.println("Method getCbRAM diakses secara remote");
        strSql = "SELECT LI.li_name from nob_list_item LI\n"
                + "JOIN nob_item_categories C ON C.cat_id = LI.li_categories\n"
                + "JOIN nob_item_brand B ON B.brand_id = LI.li_brand\n"
                + "Where C.cat_name = 'Memory RAM'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name(rs.getString("li_name"));
                // simpan objek barang ke dalam objek class List
                daftarRAM.add(ETR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarRAM;
    }

    @Override
    public int getHargaRAM(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        int hargaRAM = 0;
        System.out.println("Method getHargaRAM diakses secara remote");
        strSql = "SELECT li_price_single FROM nob_list_item where li_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getItem_name());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                hargaRAM = rs.getInt("li_price_single");
                result = hargaRAM;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Transaction> getCbVGA() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarVGA = new ArrayList<Entity_Transaction>();
        System.out.println("Method getCbVGA diakses secara remote");
        strSql = "SELECT LI.li_name from nob_list_item LI\n"
                + "JOIN nob_item_categories C ON C.cat_id = LI.li_categories\n"
                + "JOIN nob_item_brand B ON B.brand_id = LI.li_brand\n"
                + "Where C.cat_name = 'VGA'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name(rs.getString("li_name"));
                // simpan objek barang ke dalam objek class List
                daftarVGA.add(ETR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarVGA;
    }

    @Override
    public int getHargaVGA(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        int hargaVGA = 0;
        System.out.println("Method getHargaVGA diakses secara remote");
        strSql = "SELECT li_price_single FROM nob_list_item where li_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getItem_name());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                hargaVGA = rs.getInt("li_price_single");
                result = hargaVGA;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Transaction> getCbCasing() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarCasing = new ArrayList<Entity_Transaction>();
        System.out.println("Method getCbCasing diakses secara remote");
        strSql = "SELECT LI.li_name from nob_list_item LI\n"
                + "JOIN nob_item_categories C ON C.cat_id = LI.li_categories\n"
                + "JOIN nob_item_brand B ON B.brand_id = LI.li_brand\n"
                + "Where C.cat_name = 'Casing'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name(rs.getString("li_name"));
                // simpan objek barang ke dalam objek class List
                daftarCasing.add(ETR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarCasing;
    }

    @Override
    public int getHargaCasing(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        int hargaCasing = 0;
        System.out.println("Method getHargaCasing diakses secara remote");
        strSql = "SELECT li_price_single FROM nob_list_item where li_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getItem_name());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                hargaCasing = rs.getInt("li_price_single");
                result = hargaCasing;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Transaction> getCbPSU() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarPSU = new ArrayList<Entity_Transaction>();
        System.out.println("Method getCbPSU diakses secara remote");
        strSql = "SELECT LI.li_name from nob_list_item LI\n"
                + "JOIN nob_item_categories C ON C.cat_id = LI.li_categories\n"
                + "JOIN nob_item_brand B ON B.brand_id = LI.li_brand\n"
                + "Where C.cat_name = 'PSU'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name(rs.getString("li_name"));
                // simpan objek barang ke dalam objek class List
                daftarPSU.add(ETR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarPSU;
    }

    @Override
    public int getHargaPSU(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        int hargaPSU = 0;
        System.out.println("Method getHargaPSU diakses secara remote");
        strSql = "SELECT li_price_single FROM nob_list_item where li_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getItem_name());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                hargaPSU = rs.getInt("li_price_single");
                result = hargaPSU;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Transaction> getCbLCD() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarLCD = new ArrayList<Entity_Transaction>();
        System.out.println("Method getCbLCD diakses secara remote");
        strSql = "SELECT LI.li_name from nob_list_item LI\n"
                + "JOIN nob_item_categories C ON C.cat_id = LI.li_categories\n"
                + "JOIN nob_item_brand B ON B.brand_id = LI.li_brand\n"
                + "Where C.cat_name = 'LCD'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name(rs.getString("li_name"));
                // simpan objek barang ke dalam objek class List
                daftarLCD.add(ETR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarLCD;
    }

    @Override
    public int getHargaLCD(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        int hargaLCD = 0;
        System.out.println("Method getHargaLCD diakses secara remote");
        strSql = "SELECT li_price_single FROM nob_list_item where li_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getItem_name());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                hargaLCD = rs.getInt("li_price_single");
                result = hargaLCD;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Transaction> getCbOptical() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarOptical = new ArrayList<Entity_Transaction>();
        System.out.println("Method getCbLCD diakses secara remote");
        strSql = "SELECT LI.li_name from nob_list_item LI\n"
                + "JOIN nob_item_categories C ON C.cat_id = LI.li_categories\n"
                + "JOIN nob_item_brand B ON B.brand_id = LI.li_brand\n"
                + "Where C.cat_name = 'Optical'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name(rs.getString("li_name"));
                // simpan objek barang ke dalam objek class List
                daftarOptical.add(ETR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarOptical;
    }

    @Override
    public int getHargaOptical(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        int hargaOptical = 0;
        System.out.println("Method getHargaOptical diakses secara remote");
        strSql = "SELECT li_price_single FROM nob_list_item where li_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getItem_name());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                hargaOptical = rs.getInt("li_price_single");
                result = hargaOptical;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Transaction> getCbKeyboard() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarKeyboard = new ArrayList<Entity_Transaction>();
        System.out.println("Method getCbKeyboard diakses secara remote");
        strSql = "SELECT LI.li_name from nob_list_item LI\n"
                + "JOIN nob_item_categories C ON C.cat_id = LI.li_categories\n"
                + "JOIN nob_item_brand B ON B.brand_id = LI.li_brand\n"
                + "Where C.cat_name = 'Keyboard'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name(rs.getString("li_name"));
                // simpan objek barang ke dalam objek class List
                daftarKeyboard.add(ETR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarKeyboard;
    }

    @Override
    public int getHargaKeyboard(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        int hargaKeyboard = 0;
        System.out.println("Method getHargaKeyboard diakses secara remote");
        strSql = "SELECT li_price_single FROM nob_list_item where li_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getItem_name());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                hargaKeyboard = rs.getInt("li_price_single");
                result = hargaKeyboard;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Transaction> getCbSpeaker() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarSpeaker = new ArrayList<Entity_Transaction>();
        System.out.println("Method getCbSpeaker diakses secara remote");
        strSql = "SELECT LI.li_name from nob_list_item LI\n"
                + "JOIN nob_item_categories C ON C.cat_id = LI.li_categories\n"
                + "JOIN nob_item_brand B ON B.brand_id = LI.li_brand\n"
                + "Where C.cat_name = 'Speaker'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name(rs.getString("li_name"));
                // simpan objek barang ke dalam objek class List
                daftarSpeaker.add(ETR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarSpeaker;
    }

    @Override
    public int getHargaSpeaker(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        int hargaSpeaker = 0;
        System.out.println("Method getHargaSpeaker diakses secara remote");
        strSql = "SELECT li_price_single FROM nob_list_item where li_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getItem_name());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                hargaSpeaker = rs.getInt("li_price_single");
                result = hargaSpeaker;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Transaction> getCbHeadset() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarHeadset = new ArrayList<Entity_Transaction>();
        System.out.println("Method getCbSpeaker diakses secara remote");
        strSql = "SELECT LI.li_name from nob_list_item LI\n"
                + "JOIN nob_item_categories C ON C.cat_id = LI.li_categories\n"
                + "JOIN nob_item_brand B ON B.brand_id = LI.li_brand\n"
                + "Where C.cat_name = 'Headset'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name(rs.getString("li_name"));
                // simpan objek barang ke dalam objek class List
                daftarHeadset.add(ETR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarHeadset;
    }

    @Override
    public int getHargaHeadset(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        int hargaHeadset = 0;
        System.out.println("Method getHargaHeadset diakses secara remote");
        strSql = "SELECT li_price_single FROM nob_list_item where li_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getItem_name());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                hargaHeadset = rs.getInt("li_price_single");
                result = hargaHeadset;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Transaction> getCbCPUCooler() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarCPUCooler = new ArrayList<Entity_Transaction>();
        System.out.println("Method getCbCPUCooler diakses secara remote");
        strSql = "SELECT LI.li_name from nob_list_item LI\n"
                + "JOIN nob_item_categories C ON C.cat_id = LI.li_categories\n"
                + "JOIN nob_item_brand B ON B.brand_id = LI.li_brand\n"
                + "Where C.cat_name = 'CPU Cooler'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name(rs.getString("li_name"));
                // simpan objek barang ke dalam objek class List
                daftarCPUCooler.add(ETR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarCPUCooler;
    }

    @Override
    public int getHargaCPUCooler(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        int hargaCPUCooler = 0;
        System.out.println("Method getCPUCooler diakses secara remote");
        strSql = "SELECT li_price_single FROM nob_list_item where li_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getItem_name());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                hargaCPUCooler = rs.getInt("li_price_single");
                result = hargaCPUCooler;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Transaction> getCbCoolerFan() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarCoolerFan = new ArrayList<Entity_Transaction>();
        System.out.println("Method getCbCoolerFan diakses secara remote");
        strSql = "SELECT LI.li_name from nob_list_item LI\n"
                + "JOIN nob_item_categories C ON C.cat_id = LI.li_categories\n"
                + "JOIN nob_item_brand B ON B.brand_id = LI.li_brand\n"
                + "Where C.cat_name = 'Cooler Fan'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name(rs.getString("li_name"));
                // simpan objek barang ke dalam objek class List
                daftarCoolerFan.add(ETR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarCoolerFan;
    }

    @Override
    public int getHargaCoolerFan(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        int hargaCoolerFan = 0;
        System.out.println("Method getCoolerFan diakses secara remote");
        strSql = "SELECT li_price_single FROM nob_list_item where li_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getItem_name());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                hargaCoolerFan = rs.getInt("li_price_single");
                result = hargaCoolerFan;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Transaction> getCbNetworking() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarNetworking = new ArrayList<Entity_Transaction>();
        System.out.println("Method getCbNetworking diakses secara remote");
        strSql = "SELECT LI.li_name from nob_list_item LI\n"
                + "JOIN nob_item_categories C ON C.cat_id = LI.li_categories\n"
                + "JOIN nob_item_brand B ON B.brand_id = LI.li_brand\n"
                + "Where C.cat_name = 'Networking'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name(rs.getString("li_name"));
                // simpan objek barang ke dalam objek class List
                daftarNetworking.add(ETR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarNetworking;
    }

    @Override
    public int getHargaNetworking(Entity_Transaction ETR) throws RemoteException {
        int result = 0;
        int hargaNetworking = 0;
        System.out.println("Method getNetworking diakses secara remote");
        strSql = "SELECT li_price_single FROM nob_list_item where li_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, ETR.getItem_name());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                hargaNetworking = rs.getInt("li_price_single");
                result = hargaNetworking;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String CountTransaction() throws RemoteException {
        String result = null;
        System.out.println("Method Count Transaction diakses secara remote");
        strSql = "SELECT COUNT(*) From nob_transaction where trans_status = 'Success'";
        String rowcount;
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            while (rs.next()) {
                result = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String CountTransactionPending() throws RemoteException {
        String result = null;
        System.out.println("Method Count Transaction diakses secara remote");
        strSql = "SELECT COUNT(*) From nob_transaction where trans_status = 'Pending'";
        String rowcount;
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            while (rs.next()) {
                result = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String CountTaskCas() throws RemoteException {
        String result = null;
        System.out.println("Method Hitung Tugas Kasir diakses secara remote");
        strSql = "SELECT COUNT(*) From nob_task_list where task_dep = 'Cas'";
        String rowcount;
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            while (rs.next()) {
                result = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int updateDistributorLastTrans(Entity_Distributor ED) throws RemoteException {
        int result = 0;
        String SQL = "UPDATE nob_distributor set dis_last_tran = ? where dis_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setDate(1, new Date(ED.getDis_last_trans().getTime()));
            ps.setString(2, ED.getDis_id());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
