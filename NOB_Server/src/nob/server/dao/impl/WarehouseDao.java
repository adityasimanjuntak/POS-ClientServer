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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nob.dao.api.IWarehouseDao;
import nob.model.Entity_Bank;
import nob.model.Entity_Brand;
import nob.model.Entity_Category;
import nob.model.Entity_Transaction;
import nob.model.Entity_Voucher;
import nob.model.Entity_Warehouse;

/**
 *
 * @author alimk
 */
public class WarehouseDao extends UnicastRemoteObject implements IWarehouseDao {

    private Connection conn = null;
    private String strSql = "";

    public WarehouseDao(Connection conn) throws RemoteException {
        this.conn = conn;
    }

    @Override
    public int CekItem(Entity_Warehouse EW) throws RemoteException {
        int result = 0;
        System.out.println("Method Cek Item diakses secara remote");
        strSql = "Select li_iditem FROM nob_list_item where li_iditem = ?";
        String itemID;
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, EW.getItem_id());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                itemID = rs.getString("li_iditem");
                if (EW.getItem_id().equals(itemID)) {
                    result = 1;
                }
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int CekStock(Entity_Warehouse EW) throws RemoteException {
        int result = 0;
        System.out.println("Method Cek Stock diakses secara remote");
        strSql = "Select li_stock FROM nob_list_item where li_iditem = ?";
        int Stock = 0;
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, EW.getItem_id());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                Stock = rs.getInt("li_stock");
                result = Stock;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int saveItem(Entity_Warehouse EW) throws RemoteException {
        int result = 0;
        System.out.println("Method saveItem diakses secara remote");
        strSql = "INSERT INTO nob_list_item(li_iditem, li_name, li_categories, li_brand, li_guarantee, li_info, li_supplier, li_stock, li_price_nta, li_price_pub, li_price_single)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, EW.getItem_id());
            ps.setString(2, EW.getItem_name());
            ps.setString(3, EW.getItem_category());
            ps.setString(4, EW.getItem_brand());
            ps.setString(5, EW.getItem_guarantee());
            ps.setString(6, EW.getItem_info());
            ps.setString(7, EW.getItem_supplier());
            ps.setInt(8, EW.getItem_stock());
            ps.setInt(9, EW.getItem_price_nta());
            ps.setInt(10, EW.getItem_price_pub());
            ps.setInt(11, EW.getItem_price_single());
            // jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Warehouse> getCbCategory() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Warehouse> daftarCategory = new ArrayList<Entity_Warehouse>();
        System.out.println("Method getCbCategory diakses secara remote");
        strSql = "select cat_name from nob_item_categories";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Warehouse EW = new Entity_Warehouse();
                EW.setItem_category(rs.getString("cat_name"));
                // simpan objek barang ke dalam objek class List
                daftarCategory.add(EW);
            }
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarCategory;
    }

    @Override
    public List<Entity_Warehouse> getCbBrand() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Warehouse> daftarBrand = new ArrayList<Entity_Warehouse>();
        System.out.println("Method getCbBrand diakses secara remote");
        strSql = "select brand_name from nob_item_brand";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Warehouse EW = new Entity_Warehouse();
                EW.setItem_brand(rs.getString("brand_name"));
                // simpan objek barang ke dalam objek class List
                daftarBrand.add(EW);
            }
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarBrand;
    }

    @Override
    public List<Entity_Warehouse> getCbSupplier() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Warehouse> daftarSupplier = new ArrayList<Entity_Warehouse>();
        System.out.println("Method getCBSupplier diakses secara remote");
        strSql = "select sup_name from nob_supplier";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                Entity_Warehouse EW = new Entity_Warehouse();
                EW.setItem_supplier(rs.getString("sup_name"));
                // simpan objek barang ke dalam objek class List
                daftarSupplier.add(EW);
            }
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarSupplier;
    }

    @Override
    public int updateStock(Entity_Warehouse EW) throws RemoteException {
        int result = 0;
        String SQL = "UPDATE nob_list_item set li_stock = ? where li_iditem = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setInt(1, EW.getItem_stock());
            ps.setString(2, EW.getItem_id());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String getIDCategory(Entity_Warehouse EW) throws RemoteException {
        String result = null;
        String id_cat = null;
        System.out.println("Method getIDCategory diakses secara remote");
        strSql = "SELECT cat_id FROM nob_item_categories where cat_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, EW.getItem_category());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                id_cat = rs.getString("cat_id");
                result = id_cat;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String getIDBrand(Entity_Warehouse EW) throws RemoteException {
        String result = null;
        String id_brand = null;
        System.out.println("Method getIDBrand diakses secara remote");
        strSql = "SELECT brand_id FROM nob_item_brand where brand_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, EW.getItem_brand());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                id_brand = rs.getString("brand_id");
                result = id_brand;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String getIDSupplier(Entity_Warehouse EW) throws RemoteException {
        String result = null;
        String id_supplier = null;
        System.out.println("Method getIDSupplier diakses secara remote");
        strSql = "SELECT sup_id FROM nob_supplier where sup_name = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ps.setString(1, EW.getItem_supplier());
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            if (rs.next()) {
                id_supplier = rs.getString("sup_id");
                result = id_supplier;
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Category> getAllCategory() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Category> daftarCategory = new ArrayList<Entity_Category>();
        System.out.println("Method getAll Daftar Category diakses secara remote");
        strSql = "SELECT cat_id, cat_name FROM nob_item_categories";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
//            ps.setString(1, EI.getItem_id());
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Entity_Category EC = new Entity_Category();
                // mapping objek ResultSet ke object barang
                EC.setCat_id(rs.getString("cat_id"));
                EC.setCat_name(rs.getString("cat_name"));
                // simpan objek barang ke dalam objek class List
                daftarCategory.add(EC);
            }
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarCategory;
    }

    @Override
    public String GenerateCatID(Entity_Category EC) throws RemoteException {
        String result = null;
        String x = "IC-";
        System.out.println("Method Generate Cat ID diakses secara remote");
        strSql = "SELECT * FROM nob_item_categories ORDER BY cat_id DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String noreg = rs.getString("cat_id").substring(3);
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
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int saveCategory(Entity_Category EC) throws RemoteException {
        int result = 0;
        System.out.println("Method saveCategory diakses secara remote");
        strSql = "INSERT INTO nob_item_categories(cat_id, cat_name) VALUES (?, ?)";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, EC.getCat_id());
            ps.setString(2, EC.getCat_name());
            // jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int updateCategory(Entity_Category EC) throws RemoteException {
        int result = 0;
        String SQL = "UPDATE nob_item_categories set cat_name = ? where cat_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, EC.getCat_name());
            ps.setString(2, EC.getCat_id());
            ps.executeUpdate();

            //jalankanperinah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int deleteCategory(Entity_Category EC) throws RemoteException {
        int result = 0;
        String SQL = "DELETE FROM nob_item_categories where cat_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, EC.getCat_id());

            //jalankanperinah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int deleteItem(Entity_Warehouse EW) throws RemoteException {
        int result = 0;
        String SQL = "DELETE FROM nob_list_item where li_iditem = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, EW.getItem_id());

            //jalankanperinah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public String GenerateBrandID(Entity_Brand EB) throws RemoteException {
        String result = null;
        String x = "IB-";
        System.out.println("Method Generate Cat ID diakses secara remote");
        strSql = "SELECT * FROM nob_item_brand ORDER BY brand_id DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String noreg = rs.getString("brand_id").substring(3);
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
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Brand> getAllBrand() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Brand> daftarBrand = new ArrayList<Entity_Brand>();
        System.out.println("Method getAllBrand Daftar Category diakses secara remote");
        strSql = "SELECT brand_id, brand_name FROM nob_item_brand";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Entity_Brand EB = new Entity_Brand();
                // mapping objek ResultSet ke object barang
                EB.setBrand_id(rs.getString("brand_id"));
                EB.setBrand_name(rs.getString("brand_name"));
                // simpan objek barang ke dalam objek class List
                daftarBrand.add(EB);
            }
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarBrand;
    }

    @Override
    public int saveBrand(Entity_Brand EB) throws RemoteException {
        int result = 0;
        System.out.println("Method saveBrand diakses secara remote");
        strSql = "INSERT INTO nob_item_brand(brand_id, brand_name) VALUES (?, ?)";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, EB.getBrand_id());
            ps.setString(2, EB.getBrand_name());
            // jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int updateBrand(Entity_Brand EB) throws RemoteException {
        int result = 0;
        String SQL = "UPDATE nob_item_brand set brand_name = ? where brand_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, EB.getBrand_name());
            ps.setString(2, EB.getBrand_id());
            ps.executeUpdate();

            //jalankanperinah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int deleteBrand(Entity_Brand EB) throws RemoteException {
        int result = 0;
        String SQL = "DELETE FROM nob_item_brand where brand_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, EB.getBrand_id());

            //jalankanperinah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int CountTransactionPending() throws RemoteException {
        int result = 0;
        System.out.println("Method Hitung Transaksi Pending diakses secara remote");
        strSql = "SELECT COUNT(*) From nob_transaction where trans_status = 'Pending'";
        String rowcount;
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            while (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Transaction> getAllTransactionPending() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarTransaksiPending = new ArrayList<Entity_Transaction>();
        System.out.println("Method getAllTransactionPending diakses secara remote");
        strSql = "SELECT trans_id, trans_date, trans_id_cust, trans_processby, trans_total, trans_status FROM nob_transaction where trans_status = 'Pending'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
//            ps.setString(1, EI.getItem_id());
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Entity_Transaction ET = new Entity_Transaction();
                // mapping objek ResultSet ke object barang
                ET.setTrans_id(rs.getString("trans_id"));
                ET.setTrans_date(rs.getDate("trans_date"));
                ET.setTrans_cust_id(rs.getString("trans_id_cust"));
                ET.setTrans_processBy(rs.getString("trans_processby"));
                ET.setTrans_totalPayment(rs.getInt("trans_total"));
                ET.setTrans_status(rs.getString("trans_status"));
                // simpan objek barang ke dalam objek class List
                daftarTransaksiPending.add(ET);
            }
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarTransaksiPending;
    }

    @Override
    public List<Entity_Transaction> getAllListItemTransPending(String idTrans) throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Transaction> daftarItemPending = new ArrayList<Entity_Transaction>();
        System.out.println("Method getAllTransactionPending diakses secara remote");
        strSql = "SELECT id_item, item_name, item_price, item_quantity, item_price_subtotal FROM nob_transaction_detail where id_trans = ?";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            Entity_Transaction ET = new Entity_Transaction();
            ps.setString(1, idTrans);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // mapping objek ResultSet ke object barang
                ET.setItem_id(rs.getString("id_item"));
                ET.setItem_name(rs.getString("item_name"));
                ET.setItem_price(rs.getInt("item_price"));
                ET.setQuantity(rs.getInt("item_quantity"));
                ET.setItem_price_subtotal(rs.getInt("item_price_subtotal"));

                // simpan objek barang ke dalam objek class List
                daftarItemPending.add(ET);
            }
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarItemPending;
    }

    @Override
    public int updateTrans_Success(Entity_Transaction ET) throws RemoteException {
        int result = 0;
        String SQL = "UPDATE nob_transaction set trans_status = 'Success', trans_updateby = ? where trans_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, ET.getTrans_updateBy());
            ps.setString(2, ET.getTrans_id());
            ps.executeUpdate();

            //jalankanperinah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int updateTrans_Failed(Entity_Transaction ET) throws RemoteException {
        int result = 1;
        String SQL = "UPDATE nob_transaction set trans_status = 'Failed', trans_updateby = ? where trans_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, ET.getTrans_updateBy());
            ps.setString(2, ET.getTrans_id());
            ps.executeUpdate();

            //jalankanperinah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Bank> getAllBank() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Bank> daftarBank = new ArrayList<Entity_Bank>();
        System.out.println("Method getAllBank Daftar Category diakses secara remote");
        strSql = "SELECT bank_id, bank_name, bank_description, bank_status FROM nob_bank";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Entity_Bank EB = new Entity_Bank();
                // mapping objek ResultSet ke object barang
                EB.setBank_id(rs.getString("bank_id"));
                EB.setBank_name(rs.getString("bank_name"));
                EB.setBank_description(rs.getString("bank_description"));
                EB.setBank_status(rs.getString("bank_status"));
                // simpan objek barang ke dalam objek class List
                daftarBank.add(EB);
            }
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarBank;
    }

    @Override
    public int saveBank(Entity_Bank EB) throws RemoteException {
        int result = 0;
        System.out.println("Method saveBank diakses secara remote");
        strSql = "INSERT INTO nob_bank(bank_id, bank_name, bank_description, bank_status) VALUES (?, ?, ?, ?)";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, EB.getBank_id());
            ps.setString(2, EB.getBank_name());
            ps.setString(3, EB.getBank_description());
            ps.setString(4, EB.getBank_status());
            // jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int updateBank(Entity_Bank EB) throws RemoteException {
        int result = 0;
        String SQL = "UPDATE nob_bank set bank_status = ? where bank_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, EB.getBank_status());
            ps.setString(2, EB.getBank_id());
            ps.executeUpdate();

            //jalankanperinah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int deleteBank(Entity_Bank EB) throws RemoteException {
        int result = 0;
        String SQL = "DELETE FROM nob_bank where bank_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, EB.getBank_id());

            //jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public List<Entity_Voucher> getAllVoucher() throws RemoteException {
        // buat objek List (class Java Collection)
        List<Entity_Voucher> daftarVoucher = new ArrayList<Entity_Voucher>();
        System.out.println("Method getAllVoucher diakses secara remote");
        strSql = "SELECT vou_id, vou_date_exp, vou_stock, vou_disc, vou_desc FROM nob_voucher";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // buat objek ResultSet utk menampung hasil SELECT
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Entity_Voucher EV = new Entity_Voucher();
                // mapping objek ResultSet ke object barang
                EV.setVoucher_id(rs.getString("vou_id"));
                EV.setVoucher_date(rs.getDate("vou_date_exp"));
                EV.setVoucher_stock(rs.getInt("vou_stock"));
                EV.setVoucher_discount(rs.getInt("vou_disc"));
                EV.setVoucher_desc(rs.getString("vou_desc"));

                // simpan objek barang ke dalam objek class List
                daftarVoucher.add(EV);
            }
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return daftarVoucher;
    }

    @Override
    public int saveVoucher(Entity_Voucher EV) throws RemoteException {
        int result = 0;
        System.out.println("Method saveVoucher diakses secara remote");
        strSql = "INSERT INTO nob_voucher(vou_id, vou_date_exp, vou_stock, vou_disc, vou_desc) VALUES (?, ?, ?, ?, ?)";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            // set nilai parameter yg akan dikirim ke sql
            ps.setString(1, EV.getVoucher_id());
            ps.setDate(2, new Date(EV.getVoucher_date().getTime()));
            ps.setInt(3, EV.getVoucher_stock());
            ps.setInt(4, EV.getVoucher_discount());
            ps.setString(5, EV.getVoucher_desc());
            // jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int updateVoucher(Entity_Voucher EV) throws RemoteException {
        int result = 0;
        String SQL = "UPDATE nob_voucher set vou_date_exp = ?, vou_stock = ?, vou_disc = ?, vou_desc = ? where vou_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setDate(1, new Date(EV.getVoucher_date().getTime()));
            ps.setInt(2, EV.getVoucher_stock());
            ps.setInt(3, EV.getVoucher_discount());
            ps.setString(4, EV.getVoucher_desc());
            ps.setString(5, EV.getVoucher_id());
            ps.executeUpdate();

            //jalankanperinah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int deleteVoucher(Entity_Voucher EV) throws RemoteException {
        int result = 0;
        String SQL = "DELETE FROM nob_voucher where vou_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, EV.getVoucher_id());

            //jalankan perintah sql
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int CountTransactionSuccess() throws RemoteException {
        int result = 0;
        System.out.println("Method Count Transaction diakses secara remote");
        strSql = "SELECT COUNT(*) From nob_transaction where trans_status = 'Success'";
        try {
            // buat objek PreparedStatement
            PreparedStatement ps = conn.prepareStatement(strSql);
            ResultSet rs = ps.executeQuery();
            // set nilai parameter yg akan dikirim ke sql
            while (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(WarehouseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
