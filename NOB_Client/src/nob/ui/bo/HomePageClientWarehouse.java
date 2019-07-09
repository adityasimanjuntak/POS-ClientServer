/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nob.ui.bo;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.Thread.sleep;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import nob.config.configuration;
import nob.dao.api.IDepartmentDao;
import nob.dao.api.IDistributorDao;
import nob.dao.api.ILoginDao;
import nob.dao.api.IReportDao;
import nob.dao.api.ISimulationDao;
import nob.dao.api.ITaskDao;
import nob.dao.api.ITransactionDao;
import nob.dao.api.IUserDao;
import nob.dao.api.IWarehouseDao;
import nob.model.Entity_Bank;
import nob.model.Entity_Brand;
import nob.model.Entity_Category;
import nob.model.Entity_Department;
import nob.model.Entity_Distributor;
import nob.model.Entity_ErrorReport;
import nob.model.Entity_Piutang;
import nob.model.Entity_Signin;
import nob.model.Entity_Signup;
import nob.model.Entity_Task;
import nob.model.Entity_Transaction;
import nob.model.Entity_Voucher;
import nob.model.Entity_Warehouse;
import sun.audio.*;

/**
 *
 * @author alimk
 */
public class HomePageClientWarehouse extends javax.swing.JFrame {

    private IUserDao userDao = null;
    private ILoginDao loginDao = null;
    private IDistributorDao distributorDao = null;
    private IDepartmentDao departmentDao = null;
    private ITaskDao taskDao = null;
    private IReportDao errorDao = null;
    private ITransactionDao transactionDao = null;
    private ISimulationDao simulationDao = null;
    private IWarehouseDao warehouseDao = null;

    public String kodeSistem = "WRH";
    String hasilgetStatusServer = null;
    String hasilgetStatusWrh = null;
    final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
    int last_session = 0;
    String State_run_Wrh = null;

    String ip = null;
    String modesu = null;
    String whoisonline = null;
    String dataTerpilih = null;
    String dataTerpilihTabelTugas = null;
    String stockstatus = null;
    String modeAddItem = null;
    String modeAddCategory = null;
    String modeAddBrand = null;
    String modeAddBank = null;
    String modeAddVoucher = null;

    String idAdminWrh = null;

    int mode = 0;

    String kodeVoucher = "NOVOUCHER";
    int cektanggalexpiredVoucher = 0;
    int cekstokVoucher = 0;
    int discountVoucher = 0;
    int availableVoucher = 0;
    Date tanggalVoucher = null;

    String NamaItem = null;
    int availabelItem = 0;

    String id_cat = null;
    String id_brand = null;
    String id_supplier = null;
    int stockUpdate = 0;
    int checkitem = 0;

    String namaDistributor = null;
    String emailDistributor = null;
    String phoneDistributor = null;

    int kolom = 0;
    int baris = 0;

    private Image image;
    String filename = null;
    byte[] person_image = null;
    byte[] qr_image = null;
    private DefaultTableModel tabmode;
    int udm = 0, resol = 100, rot = 0;
    float mi = 0.000f, md = 0.000f, ms = 0.000f, min = 0.000f, tam = 5.00f;
    FileOutputStream fout;
    ByteArrayOutputStream out;

    //Inisiasi Tabel Karyawan pada Update Data
    private final Object[] DaftarTugascolumNames = {"ID Tugas", "Title", "Description", "Date", "Deadline", "Opened", "PIC", "Department", "Status"};
    private final DefaultTableModel tableModelTugas = new DefaultTableModel();
    private List<Entity_Task> recordTask = new ArrayList<Entity_Task>();

    private final Object[] DaftarErrorcolumNames = {"Error ID", "Title", "Description", "Date", "PIC", "Status"};
    private final DefaultTableModel tableModelError = new DefaultTableModel();
    private List<Entity_ErrorReport> recordError = new ArrayList<Entity_ErrorReport>();

    private List<Entity_Department> recordCbDepartment = new ArrayList<Entity_Department>();
    private List<Entity_Bank> recordCbBank = new ArrayList<Entity_Bank>();
    private List<Entity_Piutang> recordCbPiutang = new ArrayList<Entity_Piutang>();

    private final Object[] DaftarTransactioncolumNames = {"ID Trans", "ID Item", "Date", "Process By", "ID Cust", "Item Name", "Qty", "Price", "SubTotal", "Total Payment", "Status"};
    private final DefaultTableModel tableModelTransaction = new DefaultTableModel();
    private List<Entity_Transaction> recordTransaction = new ArrayList<Entity_Transaction>();

    private final Object[] DaftarDistributorcolumNames = {"Dis ID", "Name", "Phone", "Email", "Status"};
    private final DefaultTableModel tableModelDistributor = new DefaultTableModel();
    private List<Entity_Distributor> recordDistributor = new ArrayList<Entity_Distributor>();

    private final Object[] DaftarItemcolumNames = {"ID Item", "Item Name", "Category", "Brand", "Guarantee", "Info", "Stock", "Price Publish", "Price Single"};
    private final DefaultTableModel tableModelItem = new DefaultTableModel();
    private List<Entity_Transaction> recordItem = new ArrayList<Entity_Transaction>();

    private final Object[] DaftarCategorycolumNames = {"ID Category", "Category"};
    private final DefaultTableModel tableModelCategory = new DefaultTableModel();
    private List<Entity_Category> recordCategories = new ArrayList<Entity_Category>();

    private final Object[] DaftarBrandcolumNames = {"ID Brand", "Brand"};
    private final DefaultTableModel tableModelBrand = new DefaultTableModel();
    private List<Entity_Brand> recordBrands = new ArrayList<Entity_Brand>();

    private final Object[] DaftarTransPendingcolumNames = {"Transaction ID", "Date", "Customer ID", "Process By", "Total Price", "Status"};
    private final DefaultTableModel tableModelTransPending = new DefaultTableModel();
    private List<Entity_Transaction> recordTransPending = new ArrayList<Entity_Transaction>();

    private final Object[] DaftarItemPendingcolumNames = {"ID Item", "Item Name", "Item Price", "Qty", "Sub Total"};
    private final DefaultTableModel tableModelItemPending = new DefaultTableModel();
    private List<Entity_Transaction> recordItemPending = new ArrayList<Entity_Transaction>();

    private List<Entity_Warehouse> recordCategory = new ArrayList<Entity_Warehouse>();
    private List<Entity_Warehouse> recordBrand = new ArrayList<Entity_Warehouse>();
    private List<Entity_Warehouse> recordSupplier = new ArrayList<Entity_Warehouse>();

    private final Object[] DaftarBankcolumNames = {"ID Bank", "Bank", "Bank Description", "Bank Status"};
    private final DefaultTableModel tableModelBank = new DefaultTableModel();
    private List<Entity_Bank> recordBank = new ArrayList<Entity_Bank>();

    private final Object[] DaftarVouchercolumNames = {"ID Voucher", "Voucher Expired Date", "Voucher Stock", "Voucher Discount", "Voucher Description"};
    private final DefaultTableModel tableModelVoucher = new DefaultTableModel();
    private List<Entity_Voucher> recordVoucher = new ArrayList<Entity_Voucher>();

    /**
     * Creates new form HomePageClient_Administrator
     */
    public HomePageClientWarehouse() {
        initComponents();
        Mode12();
        get_ip_address();
        ShowIPModeWrh();
        set_TimeServer();

        init_User_Dao();
        init_LoginDao();
        init_DistributorDao();
        init_DepartmentDao();
        init_TaskDao();
        init_ErrorDao();
        init_TransactionDao();
        init_SimulationDao();
        init_WarehouseDao();

        txt_count_trans_pending.setVisible(false);
        HitungJumlahTransaksiPending();
        HitungJumlahTransactionSuccess();
        HitungJumlahTugasWrh();

        State_run_Wrh = "Running";

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Object[] options = {"Yes", "Cancel"};
                int reply = JOptionPane.showOptionDialog(null, "Are you sure to close the system ?", "Information", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                switch (reply) {
                    case 0:
                        try {
                            if (State_run_Wrh.equals("Running")) {
                                SetWrhStatusDisconnect();
                                e.getWindow().dispose();
                                System.exit(0);
                            } else if (hasilgetStatusServer.equals("Server Offline")) {
                                SetWrhStatusDisconnect();
                                e.getWindow().dispose();
                            } else {
                                int hasilSetWrhStatus = loginDao.UpdateStatusOffline("WRH");
                                if (hasilSetWrhStatus == 1) {
                                    e.getWindow().dispose();
                                    System.exit(0);
                                }
                            }
                        } catch (RemoteException ex) {
                            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    case 1:
                        break;
                    default:
                        break;
                }
            }
        }
        );
    }

    public void SetWrhStatusOnline() {
        try {
            int hasilSetServerStatus = loginDao.UpdateStatusOnline(kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SetWrhStatusOffline() {
        try {
            int hasilSetServerStatus = loginDao.UpdateStatusOffline(kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SetWrhStatusDisconnect() {
        try {
            int hasilSetWrhStatus = loginDao.UpdateStatusDisconnect(kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getStatusWrh() {
        try {
            hasilgetStatusWrh = loginDao.CekClientStatus(kodeSistem);
            txt_stateWrh.setText(hasilgetStatusWrh);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getStatusServer() {
        try {
            hasilgetStatusServer = loginDao.CekClientStatus("SERVER");
            if (hasilgetStatusWrh.equals("Killed by Server")) {
                Mode16();
                SetWrhStatusKilledbyServer();
            } else if (hasilgetStatusServer.equals("Server Offline")) {
                Mode16();
                SetWrhStatusDisconnect();
            } else if (hasilgetStatusWrh.equals("Online")) {
                if (last_session == 3) {
                    Mode3();
                } else if (last_session == 4) {
                    Mode4();
                } else if (last_session == 5) {
                    Mode5();
                } else if (last_session == 6) {
                    Mode6();
                } else if (last_session == 7) {
                    Mode7();
                } else if (last_session == 8) {
                    Mode8();
                } else if (last_session == 9) {
                    Mode9();
                } else if (last_session == 10) {
                    Mode10();
                } else if (last_session == 11) {
                    Mode11();
                }
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SetWrhStatusKilledbyServer() {
        try {
            int hasilSetWrhStatus = loginDao.UpdateStatusKilledbyServer(kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setIPWrh() {
        Entity_Signin ES = new Entity_Signin();
        ES.setIp(ip);
        try {
            int hasilsetIPWrh = loginDao.UpdateIP(ES, kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void CekStatus5Sec() {
        ses.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                // TODO add your handling code here:
                getStatusWrh();
                getStatusServer();
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    public void Mode1() {
        mode = 1;
        //Mode 1 = Menampilkan Panel Daftar Department Front Office - Warehouse - Landing Page
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);

        panelWrhLandingPage.setVisible(true);
        panelWrhLogin.setVisible(false);
        panelWrhDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode2() {
        mode = 2;
        //Mode 2 = Menampilkan Panel Daftar Department Front Office - Warehouse - Login
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelWrhLandingPage.setVisible(false);
        panelWrhLogin.setVisible(true);
        panelWrhDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode3() {
        mode = 3;
        //Mode 3 = Menampilkan Panel Daftar Department Front Office - Warehouse - Dashboard
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelWrhLandingPage.setVisible(false);
        panelWrhLogin.setVisible(false);
        panelWrhDashboard.setVisible(true);
        subpanelWrhDashboard.setVisible(true);
        ScrollPaneAddItem.setVisible(false);
        subpanelWrhAddItem.setVisible(false);
        subpanelWrhAddCategory.setVisible(false);
        subpanelWrhAddBrand.setVisible(false);
        subpanelWrhTransactionPending.setVisible(false);
        ScrollPaneWrhTransactionPendingProcessSP.setVisible(false);
        subpanelWrhTransactionPendingProcess.setVisible(false);
        subpanelWrhAddBank.setVisible(false);
        subpanelWrhAddVoucher.setVisible(false);
        subpanelWrhErrorReport.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode4() {
        mode = 4;
        //Mode 4 = Menampilkan Panel Daftar Department Front Office - Warehouse - Add Item
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelWrhLandingPage.setVisible(false);
        panelWrhLogin.setVisible(false);
        panelWrhDashboard.setVisible(true);
        subpanelWrhDashboard.setVisible(false);
        ScrollPaneAddItem.setVisible(true);
        subpanelWrhAddItem.setVisible(true);
        subpanelWrhAddCategory.setVisible(false);
        subpanelWrhAddBrand.setVisible(false);
        subpanelWrhTransactionPending.setVisible(false);
        ScrollPaneWrhTransactionPendingProcessSP.setVisible(false);
        subpanelWrhTransactionPendingProcess.setVisible(false);
        subpanelWrhAddBank.setVisible(false);
        subpanelWrhAddVoucher.setVisible(false);
        subpanelWrhErrorReport.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode5() {
        mode = 5;
        //Mode 5 = Menampilkan Panel Daftar Department Front Office - Warehouse - Add Category
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelWrhLandingPage.setVisible(false);
        panelWrhLogin.setVisible(false);
        panelWrhDashboard.setVisible(true);
        subpanelWrhDashboard.setVisible(false);
        ScrollPaneAddItem.setVisible(false);
        subpanelWrhAddItem.setVisible(false);
        subpanelWrhAddCategory.setVisible(true);
        subpanelWrhAddBrand.setVisible(false);
        subpanelWrhTransactionPending.setVisible(false);
        ScrollPaneWrhTransactionPendingProcessSP.setVisible(false);
        subpanelWrhTransactionPendingProcess.setVisible(false);
        subpanelWrhAddBank.setVisible(false);
        subpanelWrhAddVoucher.setVisible(false);
        subpanelWrhErrorReport.setVisible(false);
    }

    public void Mode6() {
        mode = 6;
        //Mode 6 = Menampilkan Panel Daftar Department Front Office - Warehouse - Add Brand
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelWrhLandingPage.setVisible(false);
        panelWrhLogin.setVisible(false);
        panelWrhDashboard.setVisible(true);
        subpanelWrhDashboard.setVisible(false);
        ScrollPaneAddItem.setVisible(false);
        subpanelWrhAddItem.setVisible(false);
        subpanelWrhAddCategory.setVisible(false);
        subpanelWrhAddBrand.setVisible(true);
        subpanelWrhTransactionPending.setVisible(false);
        ScrollPaneWrhTransactionPendingProcessSP.setVisible(false);
        subpanelWrhTransactionPendingProcess.setVisible(false);
        subpanelWrhAddBank.setVisible(false);
        subpanelWrhAddVoucher.setVisible(false);
        subpanelWrhErrorReport.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode7() {
        mode = 7;
        //Mode 7 = Menampilkan Panel Daftar Department Front Office - Warehouse - Data Transaction Pending
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelWrhLandingPage.setVisible(false);
        panelWrhLogin.setVisible(false);
        panelWrhDashboard.setVisible(true);
        subpanelWrhDashboard.setVisible(false);
        ScrollPaneAddItem.setVisible(false);
        subpanelWrhAddItem.setVisible(false);
        subpanelWrhAddCategory.setVisible(false);
        subpanelWrhAddBrand.setVisible(false);
        subpanelWrhTransactionPending.setVisible(true);
        ScrollPaneWrhTransactionPendingProcessSP.setVisible(false);
        subpanelWrhTransactionPendingProcess.setVisible(false);
        subpanelWrhAddBank.setVisible(false);
        subpanelWrhAddVoucher.setVisible(false);
        subpanelWrhErrorReport.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode8() {
        mode = 8;
        //Mode 8 = Menampilkan Panel Daftar Department Front Office - Warehouse - Data Transaction Pending Process
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelWrhLandingPage.setVisible(false);
        panelWrhLogin.setVisible(false);
        panelWrhDashboard.setVisible(true);
        subpanelWrhDashboard.setVisible(false);
        ScrollPaneAddItem.setVisible(false);
        subpanelWrhAddItem.setVisible(false);
        subpanelWrhAddCategory.setVisible(false);
        subpanelWrhAddBrand.setVisible(false);
        subpanelWrhTransactionPending.setVisible(false);
        ScrollPaneWrhTransactionPendingProcessSP.setVisible(true);
        subpanelWrhTransactionPendingProcess.setVisible(true);
        subpanelWrhAddBank.setVisible(false);
        subpanelWrhAddVoucher.setVisible(false);
        subpanelWrhErrorReport.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode9() {
        mode = 9;
        //Mode 9 = Menampilkan Panel Daftar Department Front Office - Warehouse - Add Bank
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelWrhLandingPage.setVisible(false);
        panelWrhLogin.setVisible(false);
        panelWrhDashboard.setVisible(true);
        subpanelWrhDashboard.setVisible(false);
        ScrollPaneAddItem.setVisible(false);
        subpanelWrhAddItem.setVisible(false);
        subpanelWrhAddCategory.setVisible(false);
        subpanelWrhAddBrand.setVisible(false);
        subpanelWrhTransactionPending.setVisible(false);
        ScrollPaneWrhTransactionPendingProcessSP.setVisible(false);
        subpanelWrhTransactionPendingProcess.setVisible(false);
        subpanelWrhAddBank.setVisible(true);
        subpanelWrhAddVoucher.setVisible(false);
        subpanelWrhErrorReport.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode10() {
        mode = 10;
        //Mode 10 = Menampilkan Panel Daftar Department Front Office - Warehouse - Add Voucher
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelWrhLandingPage.setVisible(false);
        panelWrhLogin.setVisible(false);
        panelWrhDashboard.setVisible(true);
        subpanelWrhDashboard.setVisible(false);
        ScrollPaneAddItem.setVisible(false);
        subpanelWrhAddItem.setVisible(false);
        subpanelWrhAddCategory.setVisible(false);
        subpanelWrhAddBrand.setVisible(false);
        subpanelWrhTransactionPending.setVisible(false);
        ScrollPaneWrhTransactionPendingProcessSP.setVisible(false);
        subpanelWrhTransactionPendingProcess.setVisible(false);
        subpanelWrhAddBank.setVisible(false);
        subpanelWrhAddVoucher.setVisible(true);
        subpanelWrhErrorReport.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode11() {
        mode = 11;
        //Mode 11 = Menampilkan Panel Daftar Department Front Office - Warehouse - Error Report
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelWrhLandingPage.setVisible(false);
        panelWrhLogin.setVisible(false);
        panelWrhDashboard.setVisible(true);
        subpanelWrhDashboard.setVisible(false);
        ScrollPaneAddItem.setVisible(false);
        subpanelWrhAddItem.setVisible(false);
        subpanelWrhAddCategory.setVisible(false);
        subpanelWrhAddBrand.setVisible(false);
        subpanelWrhTransactionPending.setVisible(false);
        ScrollPaneWrhTransactionPendingProcessSP.setVisible(false);
        subpanelWrhTransactionPendingProcess.setVisible(false);
        subpanelWrhAddBank.setVisible(false);
        subpanelWrhAddVoucher.setVisible(false);
        subpanelWrhErrorReport.setVisible(true);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode12() {
        //sebagai mode awal
        mode = 12;
        panelLandingPage.setVisible(true);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelWrhLandingPage.setVisible(false);
        panelWrhLogin.setVisible(false);
        panelWrhDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode13() {
        mode = 13;
        //Mode 13 = Menampilkan Panel Daftar Department
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(true);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelWrhLandingPage.setVisible(false);
        panelWrhLogin.setVisible(false);
        panelWrhDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode14() {
        mode = 14;
        //Mode 14 = Menampilkan Panel Daftar Department Back Office
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(true);
        panelFrontOffice.setVisible(false);
        panelWrhLandingPage.setVisible(false);
        panelWrhLogin.setVisible(false);
        panelWrhDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode15() {
        mode = 15;
        //Mode 15 = Menampilkan Panel Daftar Department Front Office
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(true);
        panelWrhLandingPage.setVisible(false);
        panelWrhLogin.setVisible(false);
        panelWrhDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode16() {
        mode = 16;
        //Mode 16 = Menampilkan Panel Killed by Server
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelWrhLandingPage.setVisible(false);
        panelWrhLogin.setVisible(false);
        panelWrhDashboard.setVisible(false);
        panelKilledbyServer.setVisible(true);
    }

    public void set_TimeServer() {

        Thread clock = new Thread() {
            public void run() {
                for (;;) {

                    Calendar cal = new GregorianCalendar();
                    int month = cal.get(Calendar.MONTH);
                    int year = cal.get(Calendar.YEAR);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    int second = cal.get(Calendar.SECOND);
                    int minute = cal.get(Calendar.MINUTE);
                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                    txtServerTimeWrh.setText("Date  :  " + day + "/" + (month + 1) + "/" + year + "   Time : " + hour + ":" + (minute) + ":" + second);

                    try {
                        sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        };

        clock.start();
    }

    public void get_ip_address() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            ip = address.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void HideIPModeWrh() {
        btnShowIPWrh.setVisible(false);
        btnHideIPWrh.setVisible(true);
    }

    public void ShowIPModeWrh() {
        btnShowIPWrh.setVisible(true);
        btnHideIPWrh.setVisible(false);
    }

    private void init_User_Dao() {
        String url = "rmi://" + configuration.ip_server + ":1099/userDao";
        try {
            //ambil referensi dari remote objek yang ada di server
            userDao = (IUserDao) Naming.lookup(url);
        } catch (NotBoundException | MalformedURLException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            //Logger.getLogger(FrmSupplier.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Koneksi ke Service" + url + "[GAGAL], cek kembali app server", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void init_LoginDao() {
        String url = "rmi://" + configuration.ip_server + ":1099/loginDao";
        try {
            //ambil referensi dari remote objek yang ada di server
            loginDao = (ILoginDao) Naming.lookup(url);
        } catch (NotBoundException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            //Logger.getLogger(FrmSupplier.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Koneksi ke Service" + url + "[GAGAL], cek kembali app server", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void init_DistributorDao() {
        String url = "rmi://" + configuration.ip_server + ":1099/distributorDao";
        try {
            //ambil referensi dari remote objek yang ada di server
            distributorDao = (IDistributorDao) Naming.lookup(url);
        } catch (NotBoundException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            //Logger.getLogger(FrmSupplier.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Koneksi ke Service" + url + "[GAGAL], cek kembali app server", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void init_DepartmentDao() {
        String url = "rmi://" + configuration.ip_server + ":1099/departmentDao";
        try {
            //ambil referensi dari remote objek yang ada di server
            departmentDao = (IDepartmentDao) Naming.lookup(url);
        } catch (NotBoundException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            //Logger.getLogger(FrmSupplier.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Koneksi ke Service" + url + "[GAGAL], cek kembali app server", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void init_TaskDao() {
        String url = "rmi://" + configuration.ip_server + ":1099/taskDao";
        try {
            //ambil referensi dari remote objek yang ada di server
            taskDao = (ITaskDao) Naming.lookup(url);
        } catch (NotBoundException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            //Logger.getLogger(FrmSupplier.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Koneksi ke Service" + url + "[GAGAL], cek kembali app server", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void init_ErrorDao() {
        String url = "rmi://" + configuration.ip_server + ":1099/errorDao";
        try {
            //ambil referensi dari remote objek yang ada di server
            errorDao = (IReportDao) Naming.lookup(url);
        } catch (NotBoundException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            //Logger.getLogger(FrmSupplier.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Koneksi ke Service" + url + "[GAGAL], cek kembali app server", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void init_TransactionDao() {
        String url = "rmi://" + configuration.ip_server + ":1099/transactionDao";
        try {
            //ambil referensi dari remote objek yang ada di server
            transactionDao = (ITransactionDao) Naming.lookup(url);
        } catch (NotBoundException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            //Logger.getLogger(FrmSupplier.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Koneksi ke Service" + url + "[GAGAL], cek kembali app server", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void init_SimulationDao() {
        String url = "rmi://" + configuration.ip_server + ":1099/simulationDao";
        try {
            //ambil referensi dari remote objek yang ada di server
            simulationDao = (ISimulationDao) Naming.lookup(url);
        } catch (NotBoundException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            //Logger.getLogger(FrmSupplier.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Koneksi ke Service" + url + "[GAGAL], cek kembali app server", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void init_WarehouseDao() {
        String url = "rmi://" + configuration.ip_server + ":1099/warehouseDao";
        try {
            //ambil referensi dari remote objek yang ada di server
            warehouseDao = (IWarehouseDao) Naming.lookup(url);
        } catch (NotBoundException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            //Logger.getLogger(FrmSupplier.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Koneksi ke Service" + url + "[GAGAL], cek kembali app server", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void initTabelDaftarItem() {
        //set header table
        tableModelItem.setColumnIdentifiers(DaftarItemcolumNames);
        tabelItemList.setModel(tableModelItem);
        tabelItemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelItemList.setAutoResizeMode(tabelItemList.AUTO_RESIZE_OFF);
        customTabelDaftarItem();
    }

    private void customTabelDaftarItem() {
        tabelItemList.getColumnModel().getColumn(0).setPreferredWidth(120);
        tabelItemList.getColumnModel().getColumn(1).setPreferredWidth(400);
        tabelItemList.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabelItemList.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabelItemList.getColumnModel().getColumn(4).setPreferredWidth(100);
        tabelItemList.getColumnModel().getColumn(5).setPreferredWidth(100);
        tabelItemList.getColumnModel().getColumn(6).setPreferredWidth(100);
        tabelItemList.getColumnModel().getColumn(7).setPreferredWidth(200);
        tabelItemList.getColumnModel().getColumn(8).setPreferredWidth(200);
    }

    private void loadDaftarItembyID() {
        tabelItemList.setAutoResizeMode(tabelItemList.AUTO_RESIZE_OFF);
        DecimalFormat df = new DecimalFormat("IDR ,###.00");
        customTabelDaftarItem();
        try {
            // reset data di tabel
            // mengambil data barang dari server
            tableModelItem.setRowCount(0);
            // kemudian menyimpannya ke objek list
            recordItem = transactionDao.getListItembyID(txt_trans_searchitemByID.getText());
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Transaction ETR : recordItem) {
                // ambil nomor urut terakhir
                Object[] objects = new Object[DaftarItemcolumNames.length];
                objects[0] = ETR.getItem_id();
                objects[1] = ETR.getItem_name();
                objects[2] = ETR.getItem_category();
                objects[3] = ETR.getItem_brand();
                objects[4] = ETR.getItem_guarantee();
                objects[5] = ETR.getStock();
                objects[6] = ETR.getItem_info();
                objects[7] = df.format(ETR.getItem_price_Pub());
                objects[8] = df.format(ETR.getItem_price_Single());
                // tambahkan data barang ke dalam tabel
                tableModelItem.addRow(objects);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadDaftarItembyName() {
        tabelItemList.setAutoResizeMode(tabelItemList.AUTO_RESIZE_OFF);
        DecimalFormat df = new DecimalFormat("IDR ,###.00");
        customTabelDaftarItem();
        try {
            // reset data di tabel
            // mengambil data barang dari server
            tableModelItem.setRowCount(0);
            // kemudian menyimpannya ke objek list
            recordItem = transactionDao.getListItembyName(txt_trans_searchitemByName.getText());
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Transaction ETR : recordItem) {
                // ambil nomor urut terakhir
                Object[] objects = new Object[DaftarItemcolumNames.length];
                objects[0] = ETR.getItem_id();
                objects[1] = ETR.getItem_name();
                objects[2] = ETR.getItem_category();
                objects[3] = ETR.getItem_brand();
                objects[4] = ETR.getItem_guarantee();
                objects[5] = ETR.getStock();
                objects[6] = ETR.getItem_info();
                objects[7] = df.format(ETR.getItem_price_Pub());
                objects[8] = df.format(ETR.getItem_price_Single());
                // tambahkan data barang ke dalam tabel
                tableModelItem.addRow(objects);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void AddItemEnabled() {
        txt_item_id.setEnabled(true);
        txt_item_name.setEnabled(true);
        cb_item_category.setEnabled(true);
        cb_item_brand.setEnabled(true);
        txt_item_info.setEnabled(true);
        txt_item_guarantee.setEnabled(true);
        cb_item_supplier.setEnabled(true);
        txt_item_stock_new.setEnabled(true);
        txt_item_price_nta.setEnabled(true);
        txt_item_price_publish.setEnabled(true);
        txt_item_price_single.setEnabled(true);
        btn_check_item.setEnabled(false);
    }

    public void AddItemDisabled() {
        txt_item_id.setEnabled(false);
        txt_item_name.setEnabled(false);
        cb_item_category.setEnabled(false);
        cb_item_brand.setEnabled(false);
        txt_item_guarantee.setEnabled(false);
        txt_item_info.setEnabled(false);
        cb_item_supplier.setEnabled(false);
        txt_item_stock_new.setEnabled(false);
        txt_item_price_nta.setEnabled(false);
        txt_item_price_publish.setEnabled(false);
        txt_item_price_single.setEnabled(false);
        txt_item_id_update.setEnabled(false);
        txt_item_stock_last.setEnabled(false);
        txt_item_stock.setEnabled(false);
        btn_check_item.setEnabled(false);
    }

    public void UpdateItemDisabled() {
        txt_item_id_update.setEnabled(false);
        txt_item_stock_last.setEnabled(false);
        txt_item_stock.setEnabled(false);
    }

    public void UpdateItemEnabled() {
        txt_item_stock.setEnabled(true);
    }

    private void loadComboBoxCategory() {
        try {
            // mengambil data department dari server
            // kemudian menyimpannya ke objek list
            recordCategory = warehouseDao.getCbCategory();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Warehouse EW : recordCategory) {
                // ambil nomor urut terakhir
                cb_item_category.addItem(EW.getItem_category());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadComboBoxBrand() {
        try {
            // mengambil data department dari server
            // kemudian menyimpannya ke objek list
            recordBrand = warehouseDao.getCbBrand();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Warehouse EW : recordBrand) {
                // ambil nomor urut terakhir
                cb_item_brand.addItem(EW.getItem_brand());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadComboBoxSupplier() {
        try {
            // mengambil data department dari server
            // kemudian menyimpannya ke objek list
            recordSupplier = warehouseDao.getCbSupplier();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Warehouse EW : recordSupplier) {
                // ambil nomor urut terakhir
                cb_item_supplier.addItem(EW.getItem_supplier());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clearFormItem() {
        txt_item_id.setText("");
        txt_item_name.setText("");
        cb_item_category.setSelectedIndex(0);
        cb_item_brand.setSelectedIndex(0);
        txt_item_guarantee.setText("");
        txt_item_info.setText("");
        cb_item_supplier.setSelectedIndex(0);
        txt_item_stock_new.setText("");
        txt_item_price_nta.setText("Contoh : 1140000");
        txt_item_price_publish.setText("Contoh : 1140000");
        txt_item_price_single.setText("Contoh : 1140000");
        txt_item_id_update.setText("");
        txt_item_stock_last.setText("");
        txt_item_stock.setText("");
    }

    public void initTabelCategory() {
        //set header table
        tableModelCategory.setColumnIdentifiers(DaftarCategorycolumNames);
        tabelDaftarCategory.setModel(tableModelCategory);
        tabelDaftarCategory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelDaftarCategory.setAutoResizeMode(tabelDaftarCategory.AUTO_RESIZE_OFF);
        customTabelCategory();
    }

    public void customTabelCategory() {
        tabelDaftarCategory.getColumnModel().getColumn(0).setPreferredWidth(300);
        tabelDaftarCategory.getColumnModel().getColumn(1).setPreferredWidth(300);
    }

    public void loadDaftarTabelCategory() {
        tabelDaftarCategory.setAutoResizeMode(tabelDaftarCategory.AUTO_RESIZE_OFF);
        customTabelCategory();
        try {
            // reset data di tabel
            // mengambil data barang dari server
            tableModelCategory.setRowCount(0);
            // kemudian menyimpannya ke objek list
            recordCategories = warehouseDao.getAllCategory();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Category EC : recordCategories) {
                // ambil nomor urut terakhir
                Object[] objects = new Object[DaftarCategorycolumNames.length];
                objects[0] = EC.getCat_id();
                objects[1] = EC.getCat_name();

                // tambahkan data barang ke dalam tabel
                tableModelCategory.addRow(objects);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init_Cat_ID() {
        Entity_Category EC = new Entity_Category();
        try {
            String hasil = warehouseDao.GenerateCatID(EC);
            if (hasil == null) {
                //do something
            } else {
                txt_cat_id.setText(hasil);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init_Brand_ID() {
        Entity_Brand EB = new Entity_Brand();
        try {
            String hasil = warehouseDao.GenerateBrandID(EB);
            if (hasil == null) {
                //do something
            } else {
                txt_brand_id.setText(hasil);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initTabelBrand() {
        //set header table
        tableModelBrand.setColumnIdentifiers(DaftarBrandcolumNames);
        tabelDaftarBrand.setModel(tableModelBrand);
        tabelDaftarBrand.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelDaftarBrand.setAutoResizeMode(tabelDaftarBrand.AUTO_RESIZE_OFF);
        customTabelBrand();
    }

    public void customTabelBrand() {
        tabelDaftarBrand.getColumnModel().getColumn(0).setPreferredWidth(300);
        tabelDaftarBrand.getColumnModel().getColumn(1).setPreferredWidth(300);
    }

    public void loadDaftarTabelBrand() {
        tabelDaftarBrand.setAutoResizeMode(tabelDaftarBrand.AUTO_RESIZE_OFF);
        customTabelBrand();
        try {
            // reset data di tabel
            // mengambil data barang dari server
            tableModelBrand.setRowCount(0);
            // kemudian menyimpannya ke objek list
            recordBrands = warehouseDao.getAllBrand();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Brand EB : recordBrands) {
                // ambil nomor urut terakhir
                Object[] objects = new Object[DaftarBrandcolumNames.length];
                objects[0] = EB.getBrand_id();
                objects[1] = EB.getBrand_name();

                // tambahkan data barang ke dalam tabel
                tableModelBrand.addRow(objects);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sfx_transaction_pending() throws FileNotFoundException, IOException {

        // open the sound file as a Java input stream
//        String gongFile = "C:\\Users\\alimk\\Desktop\\Skripsi\\NOB Technology\\Sound FX\\sfx_transaction_pending.wav";
        String gongFile = "src\\nob\\soundfx\\sfx_transaction_pending.wav";
        InputStream in = new FileInputStream(gongFile);

        // create an audiostream from the inputstream
        AudioStream audioStream = new AudioStream(in);

        // play the audio clip with the audioplayer class
        AudioPlayer.player.start(audioStream);

    }

    public int HitungJumlahTransaksiPending() {
        int hasiltranspending = 0;
        try {
            hasiltranspending = warehouseDao.CountTransactionPending();
            txt_count_trans_pending.setText(String.valueOf(hasiltranspending));
            txt_trans_pending.setText(String.valueOf(hasiltranspending));
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasiltranspending;
    }

    public void HitungJumlahTransactionSuccess() {
        try {
            int hasil = warehouseDao.CountTransactionSuccess();
            txt_trans_success.setText(String.valueOf(hasil));
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void HitungJumlahTugasWrh() {
        try {
            String hasil = taskDao.CountTaskWrh();
            txt_jumlahtugas_wrh.setText(hasil);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void CekTransaksiPending5Sec() {
        final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                // TODO add your handling code here:
                if (HitungJumlahTransaksiPending() == 0) {
                    txt_count_trans_pending.setVisible(false);
                } else {
                    txt_count_trans_pending.setVisible(true);
                    try {
                        sfx_transaction_pending();
                    } catch (IOException ex) {
                        Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    public void initTabelTransaksiPending() {
        //set header table
        tableModelTransPending.setColumnIdentifiers(DaftarTransPendingcolumNames);
        tabelDaftarTransactionPending.setModel(tableModelTransPending);
        tabelDaftarTransactionPending.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelDaftarTransactionPending.setAutoResizeMode(tabelDaftarTransactionPending.AUTO_RESIZE_OFF);
        customTabelTransPending();
    }

    public void customTabelTransPending() {
        tabelDaftarTransactionPending.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabelDaftarTransactionPending.getColumnModel().getColumn(1).setPreferredWidth(125);
        tabelDaftarTransactionPending.getColumnModel().getColumn(2).setPreferredWidth(225);
        tabelDaftarTransactionPending.getColumnModel().getColumn(3).setPreferredWidth(150);
        tabelDaftarTransactionPending.getColumnModel().getColumn(4).setPreferredWidth(150);
        tabelDaftarTransactionPending.getColumnModel().getColumn(5).setPreferredWidth(150);
    }

    public void loadDaftarTabelTransPending() {
        tabelDaftarTransactionPending.setAutoResizeMode(tabelDaftarTransactionPending.AUTO_RESIZE_OFF);
        customTabelTransPending();
        DecimalFormat df = new DecimalFormat(",###.00");
        try {
            // reset data di tabel
            // mengambil data barang dari server
            tableModelTransPending.setRowCount(0);
            // kemudian menyimpannya ke objek list
            recordTransPending = warehouseDao.getAllTransactionPending();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Transaction ET : recordTransPending) {
                // ambil nomor urut terakhir
                Object[] objects = new Object[DaftarTransPendingcolumNames.length];
                objects[0] = ET.getTrans_id();
                objects[1] = ET.getTrans_date();
                objects[2] = ET.getTrans_cust_id();
                objects[3] = ET.getTrans_processBy();
                objects[4] = df.format(ET.getTrans_totalPayment());
                objects[5] = ET.getTrans_status();

                // tambahkan data barang ke dalam tabel
                tableModelTransPending.addRow(objects);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        }
        txt_trans_total_data.setText(String.valueOf(tableModelTransPending.getRowCount()));
    }

    public void initTabelItemPending() {
        //set header table
        tableModelItemPending.setColumnIdentifiers(DaftarItemPendingcolumNames);
        tabelShoppingCartPending.setModel(tableModelItemPending);
        tabelShoppingCartPending.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelShoppingCartPending.setAutoResizeMode(tabelShoppingCartPending.AUTO_RESIZE_OFF);
        customTabelItemPending();
    }

    public void customTabelItemPending() {
        tabelShoppingCartPending.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabelShoppingCartPending.getColumnModel().getColumn(1).setPreferredWidth(300);
        tabelShoppingCartPending.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabelShoppingCartPending.getColumnModel().getColumn(3).setPreferredWidth(150);
        tabelShoppingCartPending.getColumnModel().getColumn(4).setPreferredWidth(150);
    }

    public void loadDaftarTabelItemPending() {
        tabelShoppingCartPending.setAutoResizeMode(tabelShoppingCartPending.AUTO_RESIZE_OFF);
        customTabelItemPending();
        DecimalFormat df = new DecimalFormat(",###.00");
        try {
            // reset data di tabel
            // mengambil data barang dari server
            tableModelItemPending.setRowCount(0);
            // kemudian menyimpannya ke objek list
            recordItemPending = warehouseDao.getAllListItemTransPending(txt_trans_invoiceno.getText());
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Transaction ET : recordItemPending) {
                // ambil nomor urut terakhir
                Object[] objects = new Object[DaftarItemPendingcolumNames.length];
                objects[0] = ET.getItem_id();
                objects[1] = ET.getItem_name();
                objects[2] = df.format(ET.getItem_price());
                objects[3] = ET.getQuantity();
                objects[4] = df.format(ET.getItem_price_subtotal());
                // tambahkan data barang ke dalam tabel
                tableModelItemPending.addRow(objects);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initTabelBank() {
        //set header table
        tableModelBank.setColumnIdentifiers(DaftarBankcolumNames);
        tabelDaftarBank.setModel(tableModelBank);
        tabelDaftarBank.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelDaftarBank.setAutoResizeMode(tabelDaftarBank.AUTO_RESIZE_OFF);
        customTabelBank();
    }

    public void customTabelBank() {
        tabelDaftarBank.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabelDaftarBank.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabelDaftarBank.getColumnModel().getColumn(2).setPreferredWidth(400);
        tabelDaftarBank.getColumnModel().getColumn(3).setPreferredWidth(100);
    }

    public void loadDaftarTabelBank() {
        tabelDaftarBank.setAutoResizeMode(tabelDaftarBank.AUTO_RESIZE_OFF);
        customTabelBank();
        DecimalFormat df = new DecimalFormat(",###.00");
        try {
            // reset data di tabel
            // mengambil data barang dari server
            tableModelBank.setRowCount(0);
            // kemudian menyimpannya ke objek list
            recordBank = warehouseDao.getAllBank();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Bank EB : recordBank) {
                // ambil nomor urut terakhir
                Object[] objects = new Object[DaftarBankcolumNames.length];
                objects[0] = EB.getBank_id();
                objects[1] = EB.getBank_name();
                objects[2] = EB.getBank_description();
                objects[3] = EB.getBank_status();

                // tambahkan data barang ke dalam tabel
                tableModelBank.addRow(objects);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ClearFormAddBank() {
        txt_bank_id.setText("");
        txt_bank_name.setText("");
        txt_bank_desc.setText("");
        cb_bank_status.setSelectedIndex(0);
    }

    public void initTabelVoucher() {
        //set header table
        tableModelVoucher.setColumnIdentifiers(DaftarVouchercolumNames);
        tabelDaftarVoucher.setModel(tableModelVoucher);
        tabelDaftarVoucher.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelDaftarVoucher.setAutoResizeMode(tabelDaftarVoucher.AUTO_RESIZE_OFF);
        customTabelVoucher();
    }

    public void customTabelVoucher() {
        tabelDaftarVoucher.getColumnModel().getColumn(0).setPreferredWidth(170);
        tabelDaftarVoucher.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabelDaftarVoucher.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabelDaftarVoucher.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabelDaftarVoucher.getColumnModel().getColumn(4).setPreferredWidth(200);
    }

    public void loadDaftarTabelVoucher() {
        tabelDaftarVoucher.setAutoResizeMode(tabelDaftarVoucher.AUTO_RESIZE_OFF);
        customTabelVoucher();
        DecimalFormat df = new DecimalFormat(",###.00");
        try {
            // reset data di tabel
            // mengambil data barang dari server
            tableModelVoucher.setRowCount(0);
            // kemudian menyimpannya ke objek list
            recordVoucher = warehouseDao.getAllVoucher();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Voucher EV : recordVoucher) {
                // ambil nomor urut terakhir
                Object[] objects = new Object[DaftarVouchercolumNames.length];
                objects[0] = EV.getVoucher_id();
                objects[1] = EV.getVoucher_date();
                objects[2] = EV.getVoucher_stock();
                objects[3] = df.format(EV.getVoucher_discount());
                objects[4] = EV.getVoucher_desc();

                // tambahkan data barang ke dalam tabel
                tableModelVoucher.addRow(objects);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ClearFormAddVoucher() {
        txt_voucher_id.setText("");
        txt_voucher_desc.setText("");
        txt_voucher_discount.setText("");
        txt_voucher_stock.setText("");
        dt_voucher_expired.setDate(null);
    }

    public void ClearFormError() {
        txt_error_title_wrh.setText("");
        txt_error_desc_wrh.setText("");
        dtError_Date_wrh.setDate(null);
        txt_error_title_wrh.requestFocus();
        cb_error_status_wrh.setSelectedIndex(0);
    }

    private void init_Error_ID() {
        Entity_ErrorReport EER = new Entity_ErrorReport();
        try {
            String hasil = errorDao.GenerateErrorID(EER);
            if (hasil == null) {
                //do something
            } else {
                txt_error_id_wrh.setText(hasil);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initTableDaftarError() {
        //set header table
        tableModelError.setColumnIdentifiers(DaftarErrorcolumNames);
        tabelDaftarError_Wrh.setModel(tableModelError);
        tabelDaftarError_Wrh.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelDaftarError_Wrh.setAutoResizeMode(tabelDaftarVoucher.AUTO_RESIZE_OFF);
        customTabelError();
    }

    private void customTabelError() {
        tabelDaftarError_Wrh.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabelDaftarError_Wrh.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabelDaftarError_Wrh.getColumnModel().getColumn(2).setPreferredWidth(400);
        tabelDaftarError_Wrh.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabelDaftarError_Wrh.getColumnModel().getColumn(4).setPreferredWidth(100);
    }

    private void loadDaftarError() {
        tabelDaftarError_Wrh.setAutoResizeMode(tabelDaftarVoucher.AUTO_RESIZE_OFF);
        customTabelError();
        try {
            // reset data di tabel
            tableModelError.setRowCount(0);
            // mengambil data barang dari server
            // kemudian menyimpannya ke objek list
            recordError = errorDao.getAll();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_ErrorReport EER : recordError) {
                // ambil nomor urut terakhir
                Object[] objects = new Object[DaftarErrorcolumNames.length];
                objects[0] = EER.getError_id();
                objects[1] = EER.getError_title();
                objects[2] = EER.getError_desc();
                objects[3] = EER.getError_date();
                objects[4] = EER.getError_pic();
                objects[5] = EER.getError_status();
                // tambahkan data barang ke dalam tabel
                tableModelError.addRow(objects);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void clearFormLogin() {
        txtUsernameWrh.setText("");
        txtPasswordWrh.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttongroup_Gender = new javax.swing.ButtonGroup();
        inframeQRCode = new javax.swing.JInternalFrame();
        panelQRCode_trans = new javax.swing.JPanel();
        txt_trans_invoiceno_confirm = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        btn_trans_process1 = new javax.swing.JButton();
        btn_trans_clearall1 = new javax.swing.JButton();
        lblTransInvoice_Confirm = new javax.swing.JLabel();
        panelWrhDashboard = new javax.swing.JPanel();
        btnNotifWrh = new javax.swing.JLabel();
        btnMsgWrh = new javax.swing.JLabel();
        btnLogoutWrh = new javax.swing.JLabel();
        subpanelWrhDashboard = new javax.swing.JPanel();
        txt_jumlahtugas_wrh = new javax.swing.JLabel();
        txt_trans_pending = new javax.swing.JLabel();
        txt_trans_success = new javax.swing.JLabel();
        lblDashboardTitle2 = new javax.swing.JLabel();
        lblDashboardTitle3 = new javax.swing.JLabel();
        jSeparator19 = new javax.swing.JSeparator();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        subpanelWrhErrorReport = new javax.swing.JPanel();
        btnSaveError_wrh = new javax.swing.JLabel();
        btnClearError_wrh = new javax.swing.JLabel();
        lbl_list_error_wrh = new javax.swing.JLabel();
        cb_error_status_wrh = new javax.swing.JComboBox<>();
        lbl_error_date_wrh = new javax.swing.JLabel();
        dtError_Date_wrh = new com.toedter.calendar.JDateChooser();
        lbl_error_status_wrh = new javax.swing.JLabel();
        txt_error_desc_wrh = new javax.swing.JTextArea();
        lbl_error_desc_wrh = new javax.swing.JLabel();
        txt_error_title_wrh = new javax.swing.JTextField();
        lbl_error_title_wrh = new javax.swing.JLabel();
        txt_error_id_wrh = new javax.swing.JTextField();
        lbl_error_id_wrh = new javax.swing.JLabel();
        lblErrorReportTitleWrh = new javax.swing.JLabel();
        lblErrorReportWrhSubTitle = new javax.swing.JLabel();
        jSeparator21 = new javax.swing.JSeparator();
        tabelDaftarReportSP = new javax.swing.JScrollPane();
        tabelDaftarError_Wrh = new javax.swing.JTable();
        lblErrorReportWrhSubTitle1 = new javax.swing.JLabel();
        btn_delete_error_wrh = new javax.swing.JLabel();
        subpanelWrhAddVoucher = new javax.swing.JPanel();
        lblWrhAddVoucher = new javax.swing.JLabel();
        lblWrh5 = new javax.swing.JLabel();
        jSeparator20 = new javax.swing.JSeparator();
        lblAddVoucherSubTitle = new javax.swing.JLabel();
        lbl_voucher_id = new javax.swing.JLabel();
        txt_voucher_id = new javax.swing.JTextField();
        lbl_voucher_expired = new javax.swing.JLabel();
        dt_voucher_expired = new com.toedter.calendar.JDateChooser();
        lbl_voucher_stock = new javax.swing.JLabel();
        txt_voucher_stock = new javax.swing.JTextField();
        lbl_voucher_discount = new javax.swing.JLabel();
        txt_voucher_discount = new javax.swing.JTextField();
        lbl_bank_desc1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txt_voucher_desc = new javax.swing.JTextPane();
        lbl_list_voucher = new javax.swing.JLabel();
        tabelDaftarBankSP1 = new javax.swing.JScrollPane();
        tabelDaftarVoucher = new javax.swing.JTable();
        btnSave_voucher = new javax.swing.JLabel();
        btnClear_voucher = new javax.swing.JLabel();
        btnDelete_voucher = new javax.swing.JLabel();
        subpanelWrhAddBank = new javax.swing.JPanel();
        lblWrhAddBank = new javax.swing.JLabel();
        lblWrh4 = new javax.swing.JLabel();
        jSeparator17 = new javax.swing.JSeparator();
        lblAddBankSubTitle = new javax.swing.JLabel();
        lbl_bank_id = new javax.swing.JLabel();
        txt_bank_id = new javax.swing.JTextField();
        lbl_bank_name = new javax.swing.JLabel();
        txt_bank_name = new javax.swing.JTextField();
        lbl_bank_desc = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt_bank_desc = new javax.swing.JTextPane();
        lbl_bank_status = new javax.swing.JLabel();
        cb_bank_status = new javax.swing.JComboBox<>();
        lbl_list_bank = new javax.swing.JLabel();
        tabelDaftarBankSP = new javax.swing.JScrollPane();
        tabelDaftarBank = new javax.swing.JTable();
        btnSave_Bank = new javax.swing.JLabel();
        btnClear_Bank = new javax.swing.JLabel();
        btnDelete_bank = new javax.swing.JLabel();
        ScrollPaneWrhTransactionPendingProcessSP = new javax.swing.JScrollPane();
        subpanelWrhTransactionPendingProcess = new javax.swing.JPanel();
        lblTransactionPendingTitle = new javax.swing.JLabel();
        lblTransactionPendingTitleSubTitle = new javax.swing.JLabel();
        jSeparator27 = new javax.swing.JSeparator();
        panel_Trans_Invoice = new javax.swing.JPanel();
        jSeparator28 = new javax.swing.JSeparator();
        lblTransInvoiceNo = new javax.swing.JLabel();
        lblTransTotalNTA = new javax.swing.JLabel();
        jSeparator29 = new javax.swing.JSeparator();
        lblRp1 = new javax.swing.JLabel();
        lblTransCreateDate = new javax.swing.JLabel();
        jSeparator30 = new javax.swing.JSeparator();
        txt_trans_invoiceno = new javax.swing.JLabel();
        txt_trans_TotalNTA = new javax.swing.JLabel();
        btn_trans_show_nta = new javax.swing.JButton();
        btn_trans_hide_nta = new javax.swing.JButton();
        txt_trans_date = new javax.swing.JLabel();
        panel_Trans_Contact = new javax.swing.JPanel();
        lblTransCustName = new javax.swing.JLabel();
        jSeparator41 = new javax.swing.JSeparator();
        txt_trans_cust_id = new javax.swing.JLabel();
        jSeparator42 = new javax.swing.JSeparator();
        lblTransCustEmail = new javax.swing.JLabel();
        txt_trans_custmail = new javax.swing.JLabel();
        jSeparator43 = new javax.swing.JSeparator();
        lblTransCustPhone = new javax.swing.JLabel();
        jSeparator56 = new javax.swing.JSeparator();
        lblTransCustCode = new javax.swing.JLabel();
        txt_trans_custname = new javax.swing.JLabel();
        txt_trans_custphone = new javax.swing.JLabel();
        panel_Trans_CartList = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabelShoppingCartPending = new javax.swing.JTable();
        panel_Trans_Navigasi = new javax.swing.JPanel();
        btn_pending_success = new javax.swing.JButton();
        btn_pending_failed = new javax.swing.JButton();
        panel_Trans_PriceInfo = new javax.swing.JPanel();
        lblTransGrandTotal = new javax.swing.JLabel();
        lblIDR8 = new javax.swing.JLabel();
        txt_trans_grand_total = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        subpanelWrhTransactionPending = new javax.swing.JPanel();
        lblWrhTransPending = new javax.swing.JLabel();
        txt_trans_total_data = new javax.swing.JLabel();
        lblTransTotalDat = new javax.swing.JLabel();
        jSeparator16 = new javax.swing.JSeparator();
        tabelDaftarTransactionPendingSP = new javax.swing.JScrollPane();
        tabelDaftarTransactionPending = new javax.swing.JTable();
        lblWrh3 = new javax.swing.JLabel();
        btn_open_trans = new javax.swing.JLabel();
        lblListTransactionPending = new javax.swing.JLabel();
        subpanelWrhAddBrand = new javax.swing.JPanel();
        btnSave_Brand = new javax.swing.JLabel();
        btnClear_Brand = new javax.swing.JLabel();
        lbl_list_brand = new javax.swing.JLabel();
        txt_brand_name = new javax.swing.JTextField();
        lbl_brand_name = new javax.swing.JLabel();
        txt_brand_id = new javax.swing.JTextField();
        lbl_brand_id = new javax.swing.JLabel();
        lblWrhAddBrand = new javax.swing.JLabel();
        lblAddBrandSubTitle = new javax.swing.JLabel();
        jSeparator15 = new javax.swing.JSeparator();
        tabelDaftarBrandSP = new javax.swing.JScrollPane();
        tabelDaftarBrand = new javax.swing.JTable();
        lblWrh2 = new javax.swing.JLabel();
        btn_delete_brand = new javax.swing.JLabel();
        subpanelWrhAddCategory = new javax.swing.JPanel();
        btnSave_Cat = new javax.swing.JLabel();
        btnClear_Cat = new javax.swing.JLabel();
        lbl_list_category = new javax.swing.JLabel();
        txt_cat_name = new javax.swing.JTextField();
        lbl_category_name = new javax.swing.JLabel();
        txt_cat_id = new javax.swing.JTextField();
        lbl_category_id = new javax.swing.JLabel();
        lblWrhAddCategory = new javax.swing.JLabel();
        lblAddCategorySubTitle = new javax.swing.JLabel();
        jSeparator14 = new javax.swing.JSeparator();
        tabelDaftarCategorySP = new javax.swing.JScrollPane();
        tabelDaftarCategory = new javax.swing.JTable();
        lblWrh = new javax.swing.JLabel();
        btn_delete_category = new javax.swing.JLabel();
        ScrollPaneAddItem = new javax.swing.JScrollPane();
        subpanelWrhAddItem = new javax.swing.JPanel();
        lbl_searchItemName = new javax.swing.JLabel();
        lbl_searchItemID = new javax.swing.JLabel();
        panelItemSearchbyID = new javax.swing.JPanel();
        txt_trans_searchitemByID = new javax.swing.JTextField();
        panelItemSearchbyItem = new javax.swing.JPanel();
        txt_trans_searchitemByName = new javax.swing.JTextField();
        panelItemDetail = new javax.swing.JPanel();
        lbl_item_id = new javax.swing.JLabel();
        txt_item_id = new javax.swing.JTextField();
        lbl_item_name = new javax.swing.JLabel();
        txt_item_name = new javax.swing.JTextField();
        lbl_item_category = new javax.swing.JLabel();
        cb_item_category = new javax.swing.JComboBox<>();
        lbl_item_brand = new javax.swing.JLabel();
        cb_item_brand = new javax.swing.JComboBox<>();
        lbl_item_guarantee = new javax.swing.JLabel();
        txt_item_guarantee = new javax.swing.JTextField();
        lbl_item_info = new javax.swing.JLabel();
        txt_item_info = new javax.swing.JTextField();
        lbl_item_supplier = new javax.swing.JLabel();
        cb_item_supplier = new javax.swing.JComboBox<>();
        btn_check_item = new javax.swing.JButton();
        panelItemPriceDetail = new javax.swing.JPanel();
        lbl_item_new_stock = new javax.swing.JLabel();
        txt_item_stock = new javax.swing.JTextField();
        lbl_item_last_stock = new javax.swing.JLabel();
        txt_item_stock_last = new javax.swing.JTextField();
        txt_item_id_update = new javax.swing.JTextField();
        panelItemPriceDetail1 = new javax.swing.JPanel();
        lbl_item_stock_new = new javax.swing.JLabel();
        txt_item_stock_new = new javax.swing.JTextField();
        lbl_item_price_NTA1 = new javax.swing.JLabel();
        txt_item_price_nta = new javax.swing.JTextField();
        lbl_item_price_publish1 = new javax.swing.JLabel();
        txt_item_price_publish = new javax.swing.JTextField();
        lbl_item_price_single1 = new javax.swing.JLabel();
        txt_item_price_single = new javax.swing.JTextField();
        lblWarehouseAddItem = new javax.swing.JLabel();
        lbl_warehouse = new javax.swing.JLabel();
        jSeparator18 = new javax.swing.JSeparator();
        tabelItemListSP = new javax.swing.JScrollPane();
        tabelItemList = new javax.swing.JTable();
        lbl_daftarkyw2 = new javax.swing.JLabel();
        lbl_item_update_stock = new javax.swing.JLabel();
        lbl_item_detail = new javax.swing.JLabel();
        btn_new_item = new javax.swing.JButton();
        btn_clear_item = new javax.swing.JButton();
        btn_process_item = new javax.swing.JButton();
        lbl_item_price_detail = new javax.swing.JLabel();
        btn_add_item = new javax.swing.JButton();
        btn_delete_item = new javax.swing.JLabel();
        jSeparator104 = new javax.swing.JSeparator();
        menuWrhErrorReport = new javax.swing.JPanel();
        iconWrhErrorReport = new javax.swing.JLabel();
        jSeparator96 = new javax.swing.JSeparator();
        menuWrhDataTransactionPending = new javax.swing.JPanel();
        iconWrhDataTransactionPending = new javax.swing.JLabel();
        txt_count_trans_pending = new javax.swing.JTextField();
        jSeparator95 = new javax.swing.JSeparator();
        menuWrhAddVoucher = new javax.swing.JPanel();
        iconWrhAddVoucher = new javax.swing.JLabel();
        jSeparator97 = new javax.swing.JSeparator();
        menuWrhAddBank = new javax.swing.JPanel();
        iconWrhAddBank = new javax.swing.JLabel();
        jSeparator98 = new javax.swing.JSeparator();
        menuWrhAddBrand = new javax.swing.JPanel();
        iconWrhAddBrand = new javax.swing.JLabel();
        jSeparator99 = new javax.swing.JSeparator();
        menuWrhAddCategory = new javax.swing.JPanel();
        iconWrhAddCategory = new javax.swing.JLabel();
        jSeparator100 = new javax.swing.JSeparator();
        menuWrhAddItem = new javax.swing.JPanel();
        iconWrhAddItem = new javax.swing.JLabel();
        jSeparator101 = new javax.swing.JSeparator();
        menuWrhDashboard = new javax.swing.JPanel();
        iconWrhDashboard = new javax.swing.JLabel();
        jSeparator102 = new javax.swing.JSeparator();
        btnChangeStateWrh = new javax.swing.JButton();
        btnHideIPWrh = new javax.swing.JButton();
        btnShowIPWrh = new javax.swing.JButton();
        txtServerTimeWrh = new javax.swing.JLabel();
        txt_stateBigWrh = new javax.swing.JLabel();
        lblStateBigWrh = new javax.swing.JLabel();
        lblStateWrh = new javax.swing.JLabel();
        txt_stateWrh = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        txtWrhNama = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        iconAdminWrh = new javax.swing.JLabel();
        bgWrh = new javax.swing.JLabel();
        panelKilledbyServer = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        panelWrhLogin = new javax.swing.JPanel();
        txtUsernameWrh = new javax.swing.JTextField();
        txtPasswordWrh = new javax.swing.JPasswordField();
        btnLoginWrh = new javax.swing.JButton();
        btnResetWrh = new javax.swing.JButton();
        panelLoginWrh = new javax.swing.JLabel();
        bgClientCashierWrh = new javax.swing.JLabel();
        panelWrhLandingPage = new javax.swing.JPanel();
        iconHome1 = new javax.swing.JLabel();
        bgWrhLogo = new javax.swing.JLabel();
        bgClientWarehouse = new javax.swing.JLabel();
        panelBackOffice = new javax.swing.JPanel();
        iconSIM = new javax.swing.JLabel();
        iconHRD = new javax.swing.JLabel();
        iconWarehouse = new javax.swing.JLabel();
        iconKeuangan = new javax.swing.JLabel();
        iconManagement = new javax.swing.JLabel();
        iconBOBig = new javax.swing.JLabel();
        bgBackOffice = new javax.swing.JLabel();
        panelFrontOffice = new javax.swing.JPanel();
        iconSIMFO = new javax.swing.JLabel();
        iconCashier = new javax.swing.JLabel();
        iconFOBig = new javax.swing.JLabel();
        bgFrontOffice = new javax.swing.JLabel();
        panelDepartment = new javax.swing.JPanel();
        iconFO = new javax.swing.JLabel();
        iconBO = new javax.swing.JLabel();
        bgDepartment = new javax.swing.JLabel();
        panelLandingPage = new javax.swing.JPanel();
        bgLogoNOB = new javax.swing.JLabel();
        bgLandingPage = new javax.swing.JLabel();

        inframeQRCode.setTitle("QRCode Transaction");
        inframeQRCode.setPreferredSize(new java.awt.Dimension(600, 300));
        inframeQRCode.setVisible(true);
        inframeQRCode.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelQRCode_trans.setBackground(new java.awt.Color(255, 255, 255));
        panelQRCode_trans.setPreferredSize(new java.awt.Dimension(140, 170));

        javax.swing.GroupLayout panelQRCode_transLayout = new javax.swing.GroupLayout(panelQRCode_trans);
        panelQRCode_trans.setLayout(panelQRCode_transLayout);
        panelQRCode_transLayout.setHorizontalGroup(
            panelQRCode_transLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );
        panelQRCode_transLayout.setVerticalGroup(
            panelQRCode_transLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 170, Short.MAX_VALUE)
        );

        inframeQRCode.getContentPane().add(panelQRCode_trans, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, -1));

        txt_trans_invoiceno_confirm.setFont(new java.awt.Font("Gotham Medium", 0, 18)); // NOI18N
        txt_trans_invoiceno_confirm.setText("#NO INVOICE");
        inframeQRCode.getContentPane().add(txt_trans_invoiceno_confirm, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 110, -1, -1));

        jLabel22.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        jLabel22.setText("Save your transaction ?");
        inframeQRCode.getContentPane().add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 180, -1, -1));

        jLabel23.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        jLabel23.setText("CONFIRMATION");
        inframeQRCode.getContentPane().add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        btn_trans_process1.setBackground(new java.awt.Color(153, 0, 0));
        btn_trans_process1.setForeground(new java.awt.Color(255, 255, 255));
        btn_trans_process1.setText("PROCESS");
        btn_trans_process1.setToolTipText(null);
        btn_trans_process1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_trans_process1MouseClicked(evt);
            }
        });
        inframeQRCode.getContentPane().add(btn_trans_process1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 210, 180, 30));

        btn_trans_clearall1.setBackground(new java.awt.Color(0, 102, 153));
        btn_trans_clearall1.setForeground(new java.awt.Color(255, 255, 255));
        btn_trans_clearall1.setText("CLEAR");
        btn_trans_clearall1.setToolTipText(null);
        btn_trans_clearall1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_trans_clearall1MouseClicked(evt);
            }
        });
        inframeQRCode.getContentPane().add(btn_trans_clearall1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 210, 90, 30));

        lblTransInvoice_Confirm.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lblTransInvoice_Confirm.setText("TRANSACTION INVOICE");
        inframeQRCode.getContentPane().add(lblTransInvoice_Confirm, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, -1, -1));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("NOB Tech - Client - Warehouse");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelWrhDashboard.setAutoscrolls(true);
        panelWrhDashboard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnNotifWrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconNotif.png"))); // NOI18N
        btnNotifWrh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelWrhDashboard.add(btnNotifWrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(1039, 97, -1, -1));

        btnMsgWrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconMassage.png"))); // NOI18N
        btnMsgWrh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelWrhDashboard.add(btnMsgWrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 97, -1, -1));

        btnLogoutWrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconLogout.png"))); // NOI18N
        btnLogoutWrh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogoutWrh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogoutWrhMouseClicked(evt);
            }
        });
        panelWrhDashboard.add(btnLogoutWrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 97, -1, -1));

        subpanelWrhDashboard.setBackground(new java.awt.Color(255, 255, 255));
        subpanelWrhDashboard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_jumlahtugas_wrh.setFont(new java.awt.Font("Gotham Ultra", 1, 48)); // NOI18N
        txt_jumlahtugas_wrh.setForeground(new java.awt.Color(255, 255, 255));
        txt_jumlahtugas_wrh.setText("0");
        subpanelWrhDashboard.add(txt_jumlahtugas_wrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 150, -1, -1));

        txt_trans_pending.setFont(new java.awt.Font("Gotham Ultra", 1, 48)); // NOI18N
        txt_trans_pending.setForeground(new java.awt.Color(255, 255, 255));
        txt_trans_pending.setText("0");
        subpanelWrhDashboard.add(txt_trans_pending, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 150, -1, -1));

        txt_trans_success.setFont(new java.awt.Font("Gotham Ultra", 1, 48)); // NOI18N
        txt_trans_success.setForeground(new java.awt.Color(255, 255, 255));
        txt_trans_success.setText("0");
        subpanelWrhDashboard.add(txt_trans_success, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 150, -1, -1));

        lblDashboardTitle2.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblDashboardTitle2.setForeground(java.awt.Color.gray);
        lblDashboardTitle2.setText("Warehouse");
        subpanelWrhDashboard.add(lblDashboardTitle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 53, -1, -1));

        lblDashboardTitle3.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblDashboardTitle3.setForeground(java.awt.Color.gray);
        lblDashboardTitle3.setText("DASHBOARD");
        subpanelWrhDashboard.add(lblDashboardTitle3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        jSeparator19.setForeground(new java.awt.Color(241, 241, 241));
        subpanelWrhDashboard.add(jSeparator19, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 930, 20));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconTaskQueue.png"))); // NOI18N
        subpanelWrhDashboard.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 110, -1, -1));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconDashboardTransactionPending.png"))); // NOI18N
        subpanelWrhDashboard.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 110, -1, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconDashboardTransactionSuccess.png"))); // NOI18N
        subpanelWrhDashboard.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        panelWrhDashboard.add(subpanelWrhDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        subpanelWrhErrorReport.setBackground(new java.awt.Color(255, 255, 255));
        subpanelWrhErrorReport.setPreferredSize(new java.awt.Dimension(1010, 538));
        subpanelWrhErrorReport.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSaveError_wrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconSave.png"))); // NOI18N
        btnSaveError_wrh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSaveError_wrh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveError_wrhMouseClicked(evt);
            }
        });
        subpanelWrhErrorReport.add(btnSaveError_wrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 410, -1, -1));

        btnClearError_wrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconClear.png"))); // NOI18N
        btnClearError_wrh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClearError_wrh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClearError_wrhMouseClicked(evt);
            }
        });
        subpanelWrhErrorReport.add(btnClearError_wrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 410, -1, -1));

        lbl_list_error_wrh.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_list_error_wrh.setForeground(java.awt.Color.gray);
        lbl_list_error_wrh.setText("Report List");
        subpanelWrhErrorReport.add(lbl_list_error_wrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 100, -1, -1));

        cb_error_status_wrh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Status -", "Currently Added", "On Progress", "Done" }));
        subpanelWrhErrorReport.add(cb_error_status_wrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 410, 180, -1));

        lbl_error_date_wrh.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_date_wrh.setText("Date");
        subpanelWrhErrorReport.add(lbl_error_date_wrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, -1, -1));
        subpanelWrhErrorReport.add(dtError_Date_wrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 360, 180, -1));

        lbl_error_status_wrh.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_status_wrh.setText("Status");
        subpanelWrhErrorReport.add(lbl_error_status_wrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 410, -1, -1));

        txt_error_desc_wrh.setBackground(new java.awt.Color(240, 240, 240));
        txt_error_desc_wrh.setColumns(20);
        txt_error_desc_wrh.setRows(5);
        subpanelWrhErrorReport.add(txt_error_desc_wrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 240, 180, -1));

        lbl_error_desc_wrh.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_desc_wrh.setText("Description");
        subpanelWrhErrorReport.add(lbl_error_desc_wrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, -1, -1));
        subpanelWrhErrorReport.add(txt_error_title_wrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 188, 180, -1));

        lbl_error_title_wrh.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_title_wrh.setText("Title");
        subpanelWrhErrorReport.add(lbl_error_title_wrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, -1));

        txt_error_id_wrh.setEnabled(false);
        subpanelWrhErrorReport.add(txt_error_id_wrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 180, -1));

        lbl_error_id_wrh.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_id_wrh.setText("Report ID");
        subpanelWrhErrorReport.add(lbl_error_id_wrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));

        lblErrorReportTitleWrh.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblErrorReportTitleWrh.setForeground(java.awt.Color.gray);
        lblErrorReportTitleWrh.setText("ERROR REPORT");
        subpanelWrhErrorReport.add(lblErrorReportTitleWrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        lblErrorReportWrhSubTitle.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblErrorReportWrhSubTitle.setForeground(java.awt.Color.gray);
        lblErrorReportWrhSubTitle.setText("Add Report");
        subpanelWrhErrorReport.add(lblErrorReportWrhSubTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        jSeparator21.setForeground(new java.awt.Color(241, 241, 241));
        subpanelWrhErrorReport.add(jSeparator21, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 950, 20));

        tabelDaftarReportSP.setBackground(new java.awt.Color(241, 241, 241));
        tabelDaftarReportSP.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        tabelDaftarReportSP.setAutoscrolls(true);
        tabelDaftarReportSP.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tabelDaftarError_Wrh.setBackground(new java.awt.Color(240, 240, 240));
        tabelDaftarError_Wrh.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Error ID", "Title", "Description", "Date", "Status"
            }
        ));
        tabelDaftarError_Wrh.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabelDaftarError_Wrh.setAutoscrolls(false);
        tabelDaftarError_Wrh.setGridColor(new java.awt.Color(241, 241, 241));
        tabelDaftarError_Wrh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelDaftarError_WrhMouseClicked(evt);
            }
        });
        tabelDaftarReportSP.setViewportView(tabelDaftarError_Wrh);
        if (tabelDaftarError_Wrh.getColumnModel().getColumnCount() > 0) {
            tabelDaftarError_Wrh.getColumnModel().getColumn(0).setPreferredWidth(150);
            tabelDaftarError_Wrh.getColumnModel().getColumn(1).setPreferredWidth(150);
            tabelDaftarError_Wrh.getColumnModel().getColumn(2).setPreferredWidth(400);
            tabelDaftarError_Wrh.getColumnModel().getColumn(3).setPreferredWidth(100);
            tabelDaftarError_Wrh.getColumnModel().getColumn(4).setPreferredWidth(100);
        }

        subpanelWrhErrorReport.add(tabelDaftarReportSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 130, 580, 260));

        lblErrorReportWrhSubTitle1.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblErrorReportWrhSubTitle1.setForeground(java.awt.Color.gray);
        lblErrorReportWrhSubTitle1.setText("Warehouse");
        subpanelWrhErrorReport.add(lblErrorReportWrhSubTitle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(335, 53, -1, -1));

        btn_delete_error_wrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/btnDelete.png"))); // NOI18N
        btn_delete_error_wrh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_delete_error_wrh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_delete_error_wrhMouseClicked(evt);
            }
        });
        subpanelWrhErrorReport.add(btn_delete_error_wrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 87, -1, -1));

        panelWrhDashboard.add(subpanelWrhErrorReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        subpanelWrhAddVoucher.setBackground(new java.awt.Color(255, 255, 255));
        subpanelWrhAddVoucher.setPreferredSize(new java.awt.Dimension(1010, 538));
        subpanelWrhAddVoucher.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblWrhAddVoucher.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblWrhAddVoucher.setForeground(java.awt.Color.gray);
        lblWrhAddVoucher.setText("ADD VOUCHER");
        subpanelWrhAddVoucher.add(lblWrhAddVoucher, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        lblWrh5.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblWrh5.setForeground(java.awt.Color.gray);
        lblWrh5.setText("Warehouse");
        subpanelWrhAddVoucher.add(lblWrh5, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 53, -1, -1));

        jSeparator20.setForeground(new java.awt.Color(241, 241, 241));
        subpanelWrhAddVoucher.add(jSeparator20, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 950, 20));

        lblAddVoucherSubTitle.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblAddVoucherSubTitle.setForeground(java.awt.Color.gray);
        lblAddVoucherSubTitle.setText("Add Voucher");
        subpanelWrhAddVoucher.add(lblAddVoucherSubTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        lbl_voucher_id.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_voucher_id.setText("Voucher ID");
        subpanelWrhAddVoucher.add(lbl_voucher_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, -1, -1));
        subpanelWrhAddVoucher.add(txt_voucher_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 140, 180, 30));

        lbl_voucher_expired.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_voucher_expired.setText("Voucher Expired");
        subpanelWrhAddVoucher.add(lbl_voucher_expired, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, -1, -1));
        subpanelWrhAddVoucher.add(dt_voucher_expired, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 190, 180, 30));

        lbl_voucher_stock.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_voucher_stock.setText("Voucher Stock");
        subpanelWrhAddVoucher.add(lbl_voucher_stock, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, -1, -1));

        txt_voucher_stock.setForeground(new java.awt.Color(153, 153, 153));
        txt_voucher_stock.setText("Contoh : 100");
        txt_voucher_stock.setToolTipText("");
        txt_voucher_stock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_voucher_stockMouseClicked(evt);
            }
        });
        subpanelWrhAddVoucher.add(txt_voucher_stock, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 240, 180, 30));

        lbl_voucher_discount.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_voucher_discount.setText("Voucher Discount");
        subpanelWrhAddVoucher.add(lbl_voucher_discount, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, -1, -1));

        txt_voucher_discount.setForeground(new java.awt.Color(153, 153, 153));
        txt_voucher_discount.setText("Contoh : 1140000");
        txt_voucher_discount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_voucher_discountMouseClicked(evt);
            }
        });
        subpanelWrhAddVoucher.add(txt_voucher_discount, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 290, 180, 30));

        lbl_bank_desc1.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_bank_desc1.setText("Voucher Description");
        subpanelWrhAddVoucher.add(lbl_bank_desc1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, -1, -1));

        jScrollPane2.setViewportView(txt_voucher_desc);

        subpanelWrhAddVoucher.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 340, 180, 90));

        lbl_list_voucher.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_list_voucher.setForeground(java.awt.Color.gray);
        lbl_list_voucher.setText("List Voucher");
        subpanelWrhAddVoucher.add(lbl_list_voucher, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 100, -1, -1));

        tabelDaftarBankSP1.setBackground(new java.awt.Color(241, 241, 241));
        tabelDaftarBankSP1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        tabelDaftarBankSP1.setAutoscrolls(true);
        tabelDaftarBankSP1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tabelDaftarVoucher.setBackground(new java.awt.Color(240, 240, 240));
        tabelDaftarVoucher.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Voucher ID", "Voucher Expired Date", "Voucher Stock", "Voucher Discount", "Voucher Description"
            }
        ));
        tabelDaftarVoucher.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabelDaftarVoucher.setAutoscrolls(false);
        tabelDaftarVoucher.setGridColor(new java.awt.Color(241, 241, 241));
        tabelDaftarVoucher.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelDaftarVoucherMouseClicked(evt);
            }
        });
        tabelDaftarBankSP1.setViewportView(tabelDaftarVoucher);
        if (tabelDaftarVoucher.getColumnModel().getColumnCount() > 0) {
            tabelDaftarVoucher.getColumnModel().getColumn(0).setPreferredWidth(170);
            tabelDaftarVoucher.getColumnModel().getColumn(1).setPreferredWidth(150);
            tabelDaftarVoucher.getColumnModel().getColumn(2).setPreferredWidth(100);
            tabelDaftarVoucher.getColumnModel().getColumn(3).setPreferredWidth(100);
            tabelDaftarVoucher.getColumnModel().getColumn(4).setPreferredWidth(200);
        }

        subpanelWrhAddVoucher.add(tabelDaftarBankSP1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 130, 580, 260));

        btnSave_voucher.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconSave.png"))); // NOI18N
        btnSave_voucher.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSave_voucher.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSave_voucherMouseClicked(evt);
            }
        });
        subpanelWrhAddVoucher.add(btnSave_voucher, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 410, -1, -1));

        btnClear_voucher.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconClear.png"))); // NOI18N
        btnClear_voucher.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClear_voucher.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClear_voucherMouseClicked(evt);
            }
        });
        subpanelWrhAddVoucher.add(btnClear_voucher, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 410, -1, -1));

        btnDelete_voucher.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/btnDelete.png"))); // NOI18N
        btnDelete_voucher.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDelete_voucher.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDelete_voucherMouseClicked(evt);
            }
        });
        subpanelWrhAddVoucher.add(btnDelete_voucher, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 87, -1, -1));

        panelWrhDashboard.add(subpanelWrhAddVoucher, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        subpanelWrhAddBank.setBackground(new java.awt.Color(255, 255, 255));
        subpanelWrhAddBank.setPreferredSize(new java.awt.Dimension(1010, 538));
        subpanelWrhAddBank.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblWrhAddBank.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblWrhAddBank.setForeground(java.awt.Color.gray);
        lblWrhAddBank.setText("ADD BANK");
        subpanelWrhAddBank.add(lblWrhAddBank, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        lblWrh4.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblWrh4.setForeground(java.awt.Color.gray);
        lblWrh4.setText("Warehouse");
        subpanelWrhAddBank.add(lblWrh4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 53, -1, -1));

        jSeparator17.setForeground(new java.awt.Color(241, 241, 241));
        subpanelWrhAddBank.add(jSeparator17, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 950, 20));

        lblAddBankSubTitle.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblAddBankSubTitle.setForeground(java.awt.Color.gray);
        lblAddBankSubTitle.setText("Add Bank");
        subpanelWrhAddBank.add(lblAddBankSubTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        lbl_bank_id.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_bank_id.setText("Bank ID");
        subpanelWrhAddBank.add(lbl_bank_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, -1, -1));
        subpanelWrhAddBank.add(txt_bank_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 180, 30));

        lbl_bank_name.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_bank_name.setText("Bank Name");
        subpanelWrhAddBank.add(lbl_bank_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, -1, -1));
        subpanelWrhAddBank.add(txt_bank_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 190, 180, 30));

        lbl_bank_desc.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_bank_desc.setText("Bank Description");
        subpanelWrhAddBank.add(lbl_bank_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, -1, -1));

        jScrollPane1.setViewportView(txt_bank_desc);

        subpanelWrhAddBank.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 250, 180, 90));

        lbl_bank_status.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_bank_status.setText("Bank Status");
        subpanelWrhAddBank.add(lbl_bank_status, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 370, -1, -1));

        cb_bank_status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Status -", "ACTIVE", "BLOCKED" }));
        subpanelWrhAddBank.add(cb_bank_status, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 360, 180, 30));

        lbl_list_bank.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_list_bank.setForeground(java.awt.Color.gray);
        lbl_list_bank.setText("List Bank");
        subpanelWrhAddBank.add(lbl_list_bank, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 100, -1, -1));

        tabelDaftarBankSP.setBackground(new java.awt.Color(241, 241, 241));
        tabelDaftarBankSP.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        tabelDaftarBankSP.setAutoscrolls(true);
        tabelDaftarBankSP.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tabelDaftarBank.setBackground(new java.awt.Color(240, 240, 240));
        tabelDaftarBank.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID Bank", "Bank", "Bank Description", "Bank Status"
            }
        ));
        tabelDaftarBank.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabelDaftarBank.setAutoscrolls(false);
        tabelDaftarBank.setGridColor(new java.awt.Color(241, 241, 241));
        tabelDaftarBank.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelDaftarBankMouseClicked(evt);
            }
        });
        tabelDaftarBankSP.setViewportView(tabelDaftarBank);
        if (tabelDaftarBank.getColumnModel().getColumnCount() > 0) {
            tabelDaftarBank.getColumnModel().getColumn(0).setPreferredWidth(170);
            tabelDaftarBank.getColumnModel().getColumn(1).setPreferredWidth(150);
            tabelDaftarBank.getColumnModel().getColumn(2).setPreferredWidth(100);
            tabelDaftarBank.getColumnModel().getColumn(3).setPreferredWidth(100);
        }

        subpanelWrhAddBank.add(tabelDaftarBankSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 130, 580, 260));

        btnSave_Bank.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconSave.png"))); // NOI18N
        btnSave_Bank.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSave_Bank.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSave_BankMouseClicked(evt);
            }
        });
        subpanelWrhAddBank.add(btnSave_Bank, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 410, -1, -1));

        btnClear_Bank.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconClear.png"))); // NOI18N
        btnClear_Bank.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClear_Bank.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClear_BankMouseClicked(evt);
            }
        });
        subpanelWrhAddBank.add(btnClear_Bank, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 410, -1, -1));

        btnDelete_bank.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/btnDelete.png"))); // NOI18N
        btnDelete_bank.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDelete_bank.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDelete_bankMouseClicked(evt);
            }
        });
        subpanelWrhAddBank.add(btnDelete_bank, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 87, -1, -1));

        panelWrhDashboard.add(subpanelWrhAddBank, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        ScrollPaneWrhTransactionPendingProcessSP.setBorder(null);
        ScrollPaneWrhTransactionPendingProcessSP.setForeground(new java.awt.Color(204, 204, 204));
        ScrollPaneWrhTransactionPendingProcessSP.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        ScrollPaneWrhTransactionPendingProcessSP.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        ScrollPaneWrhTransactionPendingProcessSP.setAutoscrolls(true);
        ScrollPaneWrhTransactionPendingProcessSP.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                ScrollPaneWrhTransactionPendingProcessSPMouseWheelMoved(evt);
            }
        });
        ScrollPaneWrhTransactionPendingProcessSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ScrollPaneWrhTransactionPendingProcessSPMouseEntered(evt);
            }
        });

        subpanelWrhTransactionPendingProcess.setBackground(new java.awt.Color(255, 255, 255));
        subpanelWrhTransactionPendingProcess.setPreferredSize(new java.awt.Dimension(1010, 950));
        subpanelWrhTransactionPendingProcess.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTransactionPendingTitle.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblTransactionPendingTitle.setForeground(new java.awt.Color(102, 102, 102));
        lblTransactionPendingTitle.setText("TRANSACTION PENDING");
        subpanelWrhTransactionPendingProcess.add(lblTransactionPendingTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        lblTransactionPendingTitleSubTitle.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransactionPendingTitleSubTitle.setForeground(new java.awt.Color(153, 153, 153));
        lblTransactionPendingTitleSubTitle.setText("Warehouse");
        subpanelWrhTransactionPendingProcess.add(lblTransactionPendingTitleSubTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 53, -1, -1));

        jSeparator27.setBackground(new java.awt.Color(51, 51, 51));
        jSeparator27.setForeground(new java.awt.Color(241, 241, 241));
        subpanelWrhTransactionPendingProcess.add(jSeparator27, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 940, 20));

        panel_Trans_Invoice.setBackground(new java.awt.Color(243, 243, 243));
        panel_Trans_Invoice.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSeparator28.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator28.setForeground(new java.awt.Color(241, 241, 241));
        jSeparator28.setToolTipText(null);
        panel_Trans_Invoice.add(jSeparator28, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 400, 10));

        lblTransInvoiceNo.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransInvoiceNo.setForeground(new java.awt.Color(128, 128, 128));
        lblTransInvoiceNo.setText("Invoice No");
        lblTransInvoiceNo.setToolTipText(null);
        panel_Trans_Invoice.add(lblTransInvoiceNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, -1));

        lblTransTotalNTA.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransTotalNTA.setForeground(new java.awt.Color(128, 128, 128));
        lblTransTotalNTA.setText("Total NTA");
        lblTransTotalNTA.setToolTipText(null);
        panel_Trans_Invoice.add(lblTransTotalNTA, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        jSeparator29.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator29.setForeground(new java.awt.Color(241, 241, 241));
        jSeparator29.setToolTipText(null);
        panel_Trans_Invoice.add(jSeparator29, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 400, 10));

        lblRp1.setFont(new java.awt.Font("Gotham Light", 1, 18)); // NOI18N
        lblRp1.setForeground(new java.awt.Color(51, 102, 0));
        lblRp1.setText("IDR");
        lblRp1.setToolTipText(null);
        panel_Trans_Invoice.add(lblRp1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 70, -1, -1));

        lblTransCreateDate.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransCreateDate.setForeground(new java.awt.Color(128, 128, 128));
        lblTransCreateDate.setText("Created Date");
        lblTransCreateDate.setToolTipText(null);
        panel_Trans_Invoice.add(lblTransCreateDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        jSeparator30.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator30.setForeground(new java.awt.Color(241, 241, 241));
        jSeparator30.setToolTipText(null);
        panel_Trans_Invoice.add(jSeparator30, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 95, 400, 10));

        txt_trans_invoiceno.setFont(new java.awt.Font("Gotham Light", 1, 18)); // NOI18N
        txt_trans_invoiceno.setForeground(new java.awt.Color(255, 51, 51));
        txt_trans_invoiceno.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txt_trans_invoiceno.setText("#InvoiceNo");
        txt_trans_invoiceno.setToolTipText(null);
        txt_trans_invoiceno.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panel_Trans_Invoice.add(txt_trans_invoiceno, new org.netbeans.lib.awtextra.AbsoluteConstraints(234, 30, 170, -1));

        txt_trans_TotalNTA.setFont(new java.awt.Font("Gotham Light", 1, 18)); // NOI18N
        txt_trans_TotalNTA.setForeground(new java.awt.Color(51, 102, 0));
        txt_trans_TotalNTA.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txt_trans_TotalNTA.setText("Not Show");
        txt_trans_TotalNTA.setToolTipText(null);
        txt_trans_TotalNTA.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panel_Trans_Invoice.add(txt_trans_TotalNTA, new org.netbeans.lib.awtextra.AbsoluteConstraints(237, 70, 170, -1));

        btn_trans_show_nta.setBackground(new java.awt.Color(153, 0, 0));
        btn_trans_show_nta.setForeground(new java.awt.Color(255, 255, 255));
        btn_trans_show_nta.setText("SHOW NTA");
        btn_trans_show_nta.setToolTipText(null);
        btn_trans_show_nta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_trans_show_nta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_trans_show_ntaMouseClicked(evt);
            }
        });
        panel_Trans_Invoice.add(btn_trans_show_nta, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 160, 120, 30));

        btn_trans_hide_nta.setBackground(new java.awt.Color(0, 102, 153));
        btn_trans_hide_nta.setForeground(new java.awt.Color(255, 255, 255));
        btn_trans_hide_nta.setText("HIDE NTA");
        btn_trans_hide_nta.setToolTipText(null);
        btn_trans_hide_nta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_trans_hide_nta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_trans_hide_ntaMouseClicked(evt);
            }
        });
        panel_Trans_Invoice.add(btn_trans_hide_nta, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 160, 120, 30));

        txt_trans_date.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        txt_trans_date.setForeground(new java.awt.Color(51, 51, 51));
        txt_trans_date.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txt_trans_date.setText("#CreatedDate");
        txt_trans_date.setToolTipText(null);
        txt_trans_date.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panel_Trans_Invoice.add(txt_trans_date, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 110, 230, -1));

        subpanelWrhTransactionPendingProcess.add(panel_Trans_Invoice, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, 420, 210));

        panel_Trans_Contact.setBackground(new java.awt.Color(243, 243, 243));
        panel_Trans_Contact.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTransCustName.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransCustName.setForeground(new java.awt.Color(128, 128, 128));
        lblTransCustName.setText("Name");
        lblTransCustName.setToolTipText(null);
        panel_Trans_Contact.add(lblTransCustName, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        jSeparator41.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator41.setToolTipText(null);
        panel_Trans_Contact.add(jSeparator41, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 400, 10));

        txt_trans_cust_id.setFont(new java.awt.Font("Gotham Light", 1, 18)); // NOI18N
        txt_trans_cust_id.setForeground(new java.awt.Color(0, 102, 204));
        txt_trans_cust_id.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txt_trans_cust_id.setText("#CustomerCode");
        txt_trans_cust_id.setToolTipText(null);
        txt_trans_cust_id.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panel_Trans_Contact.add(txt_trans_cust_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, 240, -1));

        jSeparator42.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator42.setToolTipText(null);
        panel_Trans_Contact.add(jSeparator42, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 400, 10));

        lblTransCustEmail.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransCustEmail.setForeground(new java.awt.Color(128, 128, 128));
        lblTransCustEmail.setText("Email");
        lblTransCustEmail.setToolTipText(null);
        panel_Trans_Contact.add(lblTransCustEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));

        txt_trans_custmail.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        txt_trans_custmail.setForeground(new java.awt.Color(51, 51, 51));
        txt_trans_custmail.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txt_trans_custmail.setText("@youremail");
        txt_trans_custmail.setToolTipText(null);
        txt_trans_custmail.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panel_Trans_Contact.add(txt_trans_custmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 120, 230, -1));

        jSeparator43.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator43.setToolTipText(null);
        panel_Trans_Contact.add(jSeparator43, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 400, 10));

        lblTransCustPhone.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransCustPhone.setForeground(new java.awt.Color(128, 128, 128));
        lblTransCustPhone.setText("Phone");
        lblTransCustPhone.setToolTipText(null);
        panel_Trans_Contact.add(lblTransCustPhone, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));

        jSeparator56.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator56.setToolTipText(null);
        panel_Trans_Contact.add(jSeparator56, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 400, 10));

        lblTransCustCode.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransCustCode.setForeground(new java.awt.Color(128, 128, 128));
        lblTransCustCode.setText("Customer Code");
        lblTransCustCode.setToolTipText(null);
        panel_Trans_Contact.add(lblTransCustCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        txt_trans_custname.setFont(new java.awt.Font("Gotham Light", 1, 18)); // NOI18N
        txt_trans_custname.setForeground(new java.awt.Color(0, 102, 204));
        txt_trans_custname.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txt_trans_custname.setText("#YourName");
        txt_trans_custname.setToolTipText(null);
        txt_trans_custname.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panel_Trans_Contact.add(txt_trans_custname, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 240, -1));

        txt_trans_custphone.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        txt_trans_custphone.setForeground(new java.awt.Color(51, 51, 51));
        txt_trans_custphone.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txt_trans_custphone.setText("#PhoneNumber");
        txt_trans_custphone.setToolTipText(null);
        txt_trans_custphone.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panel_Trans_Contact.add(txt_trans_custphone, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 160, 230, -1));

        subpanelWrhTransactionPendingProcess.add(panel_Trans_Contact, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 140, 420, 210));

        panel_Trans_CartList.setBackground(new java.awt.Color(243, 243, 243));
        panel_Trans_CartList.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabelShoppingCartPending.setAutoCreateRowSorter(true);
        tabelShoppingCartPending.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Item", "Item Name", "Price", "Qty", "Sub Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tabelShoppingCartPending.setToolTipText(null);
        tabelShoppingCartPending.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabelShoppingCartPending.setGridColor(new java.awt.Color(153, 153, 153));
        tabelShoppingCartPending.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelShoppingCartPendingMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tabelShoppingCartPending);
        if (tabelShoppingCartPending.getColumnModel().getColumnCount() > 0) {
            tabelShoppingCartPending.getColumnModel().getColumn(0).setPreferredWidth(150);
            tabelShoppingCartPending.getColumnModel().getColumn(1).setPreferredWidth(300);
            tabelShoppingCartPending.getColumnModel().getColumn(2).setPreferredWidth(150);
            tabelShoppingCartPending.getColumnModel().getColumn(3).setPreferredWidth(150);
            tabelShoppingCartPending.getColumnModel().getColumn(4).setPreferredWidth(150);
        }

        panel_Trans_CartList.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 890, 360));

        subpanelWrhTransactionPendingProcess.add(panel_Trans_CartList, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, 930, 400));

        panel_Trans_Navigasi.setBackground(new java.awt.Color(243, 243, 243));
        panel_Trans_Navigasi.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_pending_success.setBackground(new java.awt.Color(153, 0, 0));
        btn_pending_success.setForeground(new java.awt.Color(255, 255, 255));
        btn_pending_success.setText("UPDATE SUCCESS");
        btn_pending_success.setToolTipText(null);
        btn_pending_success.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_pending_successMouseClicked(evt);
            }
        });
        panel_Trans_Navigasi.add(btn_pending_success, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 15, 150, 30));

        btn_pending_failed.setBackground(new java.awt.Color(0, 102, 153));
        btn_pending_failed.setForeground(new java.awt.Color(255, 255, 255));
        btn_pending_failed.setText("UPDATE FAILED");
        btn_pending_failed.setToolTipText(null);
        btn_pending_failed.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_pending_failedMouseClicked(evt);
            }
        });
        panel_Trans_Navigasi.add(btn_pending_failed, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 15, 140, 30));

        subpanelWrhTransactionPendingProcess.add(panel_Trans_Navigasi, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 860, 370, 60));

        panel_Trans_PriceInfo.setBackground(new java.awt.Color(243, 243, 243));
        panel_Trans_PriceInfo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTransGrandTotal.setFont(new java.awt.Font("Gotham Light", 1, 18)); // NOI18N
        lblTransGrandTotal.setForeground(java.awt.Color.gray);
        lblTransGrandTotal.setText("Grand Total");
        lblTransGrandTotal.setToolTipText(null);
        panel_Trans_PriceInfo.add(lblTransGrandTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        lblIDR8.setFont(new java.awt.Font("Gotham Light", 1, 18)); // NOI18N
        lblIDR8.setForeground(java.awt.Color.gray);
        lblIDR8.setText("IDR");
        lblIDR8.setToolTipText(null);
        panel_Trans_PriceInfo.add(lblIDR8, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, -1, -1));

        txt_trans_grand_total.setFont(new java.awt.Font("Gotham Medium", 1, 18)); // NOI18N
        txt_trans_grand_total.setForeground(java.awt.Color.gray);
        txt_trans_grand_total.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txt_trans_grand_total.setText("0");
        txt_trans_grand_total.setToolTipText(null);
        txt_trans_grand_total.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panel_Trans_PriceInfo.add(txt_trans_grand_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 180, -1));

        subpanelWrhTransactionPendingProcess.add(panel_Trans_PriceInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 860, 490, 60));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconDataKeranjangItem_1.png"))); // NOI18N
        subpanelWrhTransactionPendingProcess.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 370, -1, -1));

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconDataTotalHarga_1.png"))); // NOI18N
        subpanelWrhTransactionPendingProcess.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 830, -1, -1));

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconDetailInvoice.png"))); // NOI18N
        subpanelWrhTransactionPendingProcess.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, -1, -1));

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconInformasiKontak_1.png"))); // NOI18N
        subpanelWrhTransactionPendingProcess.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 110, -1, -1));

        ScrollPaneWrhTransactionPendingProcessSP.setViewportView(subpanelWrhTransactionPendingProcess);

        panelWrhDashboard.add(ScrollPaneWrhTransactionPendingProcessSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        subpanelWrhTransactionPending.setBackground(new java.awt.Color(255, 255, 255));
        subpanelWrhTransactionPending.setPreferredSize(new java.awt.Dimension(1010, 538));
        subpanelWrhTransactionPending.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblWrhTransPending.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblWrhTransPending.setForeground(java.awt.Color.gray);
        lblWrhTransPending.setText("TRANSACTION PENDING");
        subpanelWrhTransactionPending.add(lblWrhTransPending, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        txt_trans_total_data.setFont(new java.awt.Font("Gotham Light", 1, 12)); // NOI18N
        txt_trans_total_data.setForeground(java.awt.Color.gray);
        txt_trans_total_data.setText("0");
        subpanelWrhTransactionPending.add(txt_trans_total_data, new org.netbeans.lib.awtextra.AbsoluteConstraints(105, 490, -1, -1));

        lblTransTotalDat.setFont(new java.awt.Font("Gotham Light", 0, 12)); // NOI18N
        lblTransTotalDat.setForeground(java.awt.Color.gray);
        lblTransTotalDat.setText("Total Data :");
        subpanelWrhTransactionPending.add(lblTransTotalDat, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 490, -1, -1));

        jSeparator16.setForeground(new java.awt.Color(241, 241, 241));
        subpanelWrhTransactionPending.add(jSeparator16, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 950, 20));

        tabelDaftarTransactionPendingSP.setBackground(new java.awt.Color(241, 241, 241));
        tabelDaftarTransactionPendingSP.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        tabelDaftarTransactionPendingSP.setAutoscrolls(true);
        tabelDaftarTransactionPendingSP.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tabelDaftarTransactionPending.setBackground(new java.awt.Color(240, 240, 240));
        tabelDaftarTransactionPending.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Transaction ID", "Date", "Customer ID", "Process By", "Total Price", "Status"
            }
        ));
        tabelDaftarTransactionPending.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabelDaftarTransactionPending.setAutoscrolls(false);
        tabelDaftarTransactionPending.setGridColor(new java.awt.Color(241, 241, 241));
        tabelDaftarTransactionPending.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelDaftarTransactionPendingMouseClicked(evt);
            }
        });
        tabelDaftarTransactionPendingSP.setViewportView(tabelDaftarTransactionPending);
        if (tabelDaftarTransactionPending.getColumnModel().getColumnCount() > 0) {
            tabelDaftarTransactionPending.getColumnModel().getColumn(0).setPreferredWidth(150);
            tabelDaftarTransactionPending.getColumnModel().getColumn(1).setPreferredWidth(125);
            tabelDaftarTransactionPending.getColumnModel().getColumn(2).setPreferredWidth(225);
            tabelDaftarTransactionPending.getColumnModel().getColumn(3).setPreferredWidth(150);
            tabelDaftarTransactionPending.getColumnModel().getColumn(4).setPreferredWidth(150);
            tabelDaftarTransactionPending.getColumnModel().getColumn(5).setPreferredWidth(150);
        }

        subpanelWrhTransactionPending.add(tabelDaftarTransactionPendingSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 950, 320));

        lblWrh3.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblWrh3.setForeground(java.awt.Color.gray);
        lblWrh3.setText("Warehouse");
        subpanelWrhTransactionPending.add(lblWrh3, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 53, -1, -1));

        btn_open_trans.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/warehouse/iconOpenTrans.png"))); // NOI18N
        btn_open_trans.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_open_trans.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_open_transMouseClicked(evt);
            }
        });
        subpanelWrhTransactionPending.add(btn_open_trans, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 100, -1, -1));

        lblListTransactionPending.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblListTransactionPending.setForeground(java.awt.Color.gray);
        lblListTransactionPending.setText("List Transaction Pending");
        subpanelWrhTransactionPending.add(lblListTransactionPending, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, -1));

        panelWrhDashboard.add(subpanelWrhTransactionPending, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        subpanelWrhAddBrand.setBackground(new java.awt.Color(255, 255, 255));
        subpanelWrhAddBrand.setPreferredSize(new java.awt.Dimension(1010, 538));
        subpanelWrhAddBrand.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSave_Brand.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconSave.png"))); // NOI18N
        btnSave_Brand.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSave_Brand.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSave_BrandMouseClicked(evt);
            }
        });
        subpanelWrhAddBrand.add(btnSave_Brand, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 410, -1, -1));

        btnClear_Brand.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconClear.png"))); // NOI18N
        btnClear_Brand.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClear_Brand.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClear_BrandMouseClicked(evt);
            }
        });
        subpanelWrhAddBrand.add(btnClear_Brand, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 410, -1, -1));

        lbl_list_brand.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_list_brand.setForeground(java.awt.Color.gray);
        lbl_list_brand.setText("List Brand");
        subpanelWrhAddBrand.add(lbl_list_brand, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 100, -1, -1));
        subpanelWrhAddBrand.add(txt_brand_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 190, 180, 30));

        lbl_brand_name.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_brand_name.setText("Brand Name");
        subpanelWrhAddBrand.add(lbl_brand_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, -1, -1));

        txt_brand_id.setEnabled(false);
        subpanelWrhAddBrand.add(txt_brand_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 180, 30));

        lbl_brand_id.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_brand_id.setText("Brand ID");
        subpanelWrhAddBrand.add(lbl_brand_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, -1, -1));

        lblWrhAddBrand.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblWrhAddBrand.setForeground(java.awt.Color.gray);
        lblWrhAddBrand.setText("ADD BRAND");
        subpanelWrhAddBrand.add(lblWrhAddBrand, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        lblAddBrandSubTitle.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblAddBrandSubTitle.setForeground(java.awt.Color.gray);
        lblAddBrandSubTitle.setText("Add Brand");
        subpanelWrhAddBrand.add(lblAddBrandSubTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        jSeparator15.setForeground(new java.awt.Color(241, 241, 241));
        subpanelWrhAddBrand.add(jSeparator15, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 950, 20));

        tabelDaftarBrandSP.setBackground(new java.awt.Color(241, 241, 241));
        tabelDaftarBrandSP.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        tabelDaftarBrandSP.setAutoscrolls(true);
        tabelDaftarBrandSP.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tabelDaftarBrand.setBackground(new java.awt.Color(240, 240, 240));
        tabelDaftarBrand.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ID Brand", "Brand"
            }
        ));
        tabelDaftarBrand.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabelDaftarBrand.setAutoscrolls(false);
        tabelDaftarBrand.setGridColor(new java.awt.Color(241, 241, 241));
        tabelDaftarBrand.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelDaftarBrandMouseClicked(evt);
            }
        });
        tabelDaftarBrandSP.setViewportView(tabelDaftarBrand);
        if (tabelDaftarBrand.getColumnModel().getColumnCount() > 0) {
            tabelDaftarBrand.getColumnModel().getColumn(0).setPreferredWidth(170);
            tabelDaftarBrand.getColumnModel().getColumn(1).setPreferredWidth(150);
        }

        subpanelWrhAddBrand.add(tabelDaftarBrandSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 130, 580, 260));

        lblWrh2.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblWrh2.setForeground(java.awt.Color.gray);
        lblWrh2.setText("Warehouse");
        subpanelWrhAddBrand.add(lblWrh2, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 53, -1, -1));

        btn_delete_brand.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/btnDelete.png"))); // NOI18N
        btn_delete_brand.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_delete_brand.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_delete_brandMouseClicked(evt);
            }
        });
        subpanelWrhAddBrand.add(btn_delete_brand, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 87, -1, -1));

        panelWrhDashboard.add(subpanelWrhAddBrand, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        subpanelWrhAddCategory.setBackground(new java.awt.Color(255, 255, 255));
        subpanelWrhAddCategory.setPreferredSize(new java.awt.Dimension(1010, 538));
        subpanelWrhAddCategory.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSave_Cat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconSave.png"))); // NOI18N
        btnSave_Cat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSave_Cat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSave_CatMouseClicked(evt);
            }
        });
        subpanelWrhAddCategory.add(btnSave_Cat, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 410, -1, -1));

        btnClear_Cat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconClear.png"))); // NOI18N
        btnClear_Cat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClear_Cat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClear_CatMouseClicked(evt);
            }
        });
        subpanelWrhAddCategory.add(btnClear_Cat, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 410, -1, -1));

        lbl_list_category.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_list_category.setForeground(java.awt.Color.gray);
        lbl_list_category.setText("List Category");
        subpanelWrhAddCategory.add(lbl_list_category, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 100, -1, -1));
        subpanelWrhAddCategory.add(txt_cat_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 190, 180, 30));

        lbl_category_name.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_category_name.setText("Category Name");
        subpanelWrhAddCategory.add(lbl_category_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, -1, -1));

        txt_cat_id.setEnabled(false);
        subpanelWrhAddCategory.add(txt_cat_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 180, 30));

        lbl_category_id.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_category_id.setText("Category ID");
        subpanelWrhAddCategory.add(lbl_category_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, -1, -1));

        lblWrhAddCategory.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblWrhAddCategory.setForeground(java.awt.Color.gray);
        lblWrhAddCategory.setText("ADD CATEGORY");
        subpanelWrhAddCategory.add(lblWrhAddCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        lblAddCategorySubTitle.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblAddCategorySubTitle.setForeground(java.awt.Color.gray);
        lblAddCategorySubTitle.setText("Add Category");
        subpanelWrhAddCategory.add(lblAddCategorySubTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        jSeparator14.setForeground(new java.awt.Color(241, 241, 241));
        subpanelWrhAddCategory.add(jSeparator14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 950, 20));

        tabelDaftarCategorySP.setBackground(new java.awt.Color(241, 241, 241));
        tabelDaftarCategorySP.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        tabelDaftarCategorySP.setAutoscrolls(true);
        tabelDaftarCategorySP.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tabelDaftarCategory.setBackground(new java.awt.Color(240, 240, 240));
        tabelDaftarCategory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ID Category", "Category"
            }
        ));
        tabelDaftarCategory.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabelDaftarCategory.setAutoscrolls(false);
        tabelDaftarCategory.setGridColor(new java.awt.Color(241, 241, 241));
        tabelDaftarCategory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelDaftarCategoryMouseClicked(evt);
            }
        });
        tabelDaftarCategorySP.setViewportView(tabelDaftarCategory);
        if (tabelDaftarCategory.getColumnModel().getColumnCount() > 0) {
            tabelDaftarCategory.getColumnModel().getColumn(0).setPreferredWidth(300);
            tabelDaftarCategory.getColumnModel().getColumn(1).setPreferredWidth(300);
        }

        subpanelWrhAddCategory.add(tabelDaftarCategorySP, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 130, 580, 260));

        lblWrh.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblWrh.setForeground(java.awt.Color.gray);
        lblWrh.setText("Warehouse");
        subpanelWrhAddCategory.add(lblWrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 53, -1, -1));

        btn_delete_category.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/btnDelete.png"))); // NOI18N
        btn_delete_category.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_delete_category.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_delete_categoryMouseClicked(evt);
            }
        });
        subpanelWrhAddCategory.add(btn_delete_category, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 87, -1, -1));

        panelWrhDashboard.add(subpanelWrhAddCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        ScrollPaneAddItem.setBorder(null);
        ScrollPaneAddItem.setForeground(new java.awt.Color(204, 204, 204));
        ScrollPaneAddItem.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        ScrollPaneAddItem.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        ScrollPaneAddItem.setAutoscrolls(true);
        ScrollPaneAddItem.setPreferredSize(new java.awt.Dimension(1029, 536));
        ScrollPaneAddItem.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                ScrollPaneAddItemMouseWheelMoved(evt);
            }
        });
        ScrollPaneAddItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ScrollPaneAddItemMouseEntered(evt);
            }
        });

        subpanelWrhAddItem.setBackground(new java.awt.Color(255, 255, 255));
        subpanelWrhAddItem.setPreferredSize(new java.awt.Dimension(1010, 1000));
        subpanelWrhAddItem.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_searchItemName.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_searchItemName.setText("Search Item");
        subpanelWrhAddItem.add(lbl_searchItemName, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 90, -1, -1));

        lbl_searchItemID.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_searchItemID.setText("Search ID Item");
        subpanelWrhAddItem.add(lbl_searchItemID, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));

        panelItemSearchbyID.setBackground(new java.awt.Color(241, 241, 241));
        panelItemSearchbyID.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_trans_searchitemByID.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        txt_trans_searchitemByID.setForeground(new java.awt.Color(204, 204, 204));
        txt_trans_searchitemByID.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_trans_searchitemByID.setText("Press enter to search ...");
        txt_trans_searchitemByID.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt_trans_searchitemByID.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_trans_searchitemByIDMouseClicked(evt);
            }
        });
        txt_trans_searchitemByID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_trans_searchitemByIDKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_trans_searchitemByIDKeyTyped(evt);
            }
        });
        panelItemSearchbyID.add(txt_trans_searchitemByID, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 10, 430, 40));

        subpanelWrhAddItem.add(panelItemSearchbyID, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 460, 60));

        panelItemSearchbyItem.setBackground(new java.awt.Color(241, 241, 241));
        panelItemSearchbyItem.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_trans_searchitemByName.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        txt_trans_searchitemByName.setForeground(new java.awt.Color(204, 204, 204));
        txt_trans_searchitemByName.setText("Press enter to search ...");
        txt_trans_searchitemByName.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt_trans_searchitemByName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_trans_searchitemByNameMouseClicked(evt);
            }
        });
        txt_trans_searchitemByName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_trans_searchitemByNameKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_trans_searchitemByNameKeyTyped(evt);
            }
        });
        panelItemSearchbyItem.add(txt_trans_searchitemByName, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 10, 430, 40));

        subpanelWrhAddItem.add(panelItemSearchbyItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 110, 460, 60));

        panelItemDetail.setBackground(new java.awt.Color(241, 241, 241));
        panelItemDetail.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_item_id.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_item_id.setText("ID Item");
        panelItemDetail.add(lbl_item_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        txt_item_id.setEnabled(false);
        panelItemDetail.add(txt_item_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 300, 30));

        lbl_item_name.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_item_name.setText("Item Name");
        panelItemDetail.add(lbl_item_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        txt_item_name.setEnabled(false);
        panelItemDetail.add(txt_item_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 300, 30));

        lbl_item_category.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_item_category.setText("Item Category");
        panelItemDetail.add(lbl_item_category, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, -1, -1));

        cb_item_category.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Item Category -" }));
        cb_item_category.setEnabled(false);
        cb_item_category.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_item_categoryItemStateChanged(evt);
            }
        });
        panelItemDetail.add(cb_item_category, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 220, 30));

        lbl_item_brand.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_item_brand.setText("Item Brand");
        panelItemDetail.add(lbl_item_brand, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, -1, -1));

        cb_item_brand.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Item Brand -" }));
        cb_item_brand.setEnabled(false);
        cb_item_brand.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_item_brandItemStateChanged(evt);
            }
        });
        panelItemDetail.add(cb_item_brand, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 220, 30));

        lbl_item_guarantee.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_item_guarantee.setText("Guarantee");
        panelItemDetail.add(lbl_item_guarantee, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, -1, -1));

        txt_item_guarantee.setEnabled(false);
        panelItemDetail.add(txt_item_guarantee, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, 300, 30));

        lbl_item_info.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_item_info.setText("Item Info");
        panelItemDetail.add(lbl_item_info, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, -1, -1));

        txt_item_info.setEnabled(false);
        panelItemDetail.add(txt_item_info, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 300, 30));

        lbl_item_supplier.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_item_supplier.setText("Supplier");
        panelItemDetail.add(lbl_item_supplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, -1, -1));

        cb_item_supplier.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Supplier -" }));
        cb_item_supplier.setEnabled(false);
        cb_item_supplier.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_item_supplierItemStateChanged(evt);
            }
        });
        panelItemDetail.add(cb_item_supplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 420, 220, 30));

        btn_check_item.setBackground(new java.awt.Color(153, 0, 0));
        btn_check_item.setForeground(new java.awt.Color(255, 255, 255));
        btn_check_item.setText("CHECK ITEM");
        btn_check_item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_check_itemMouseClicked(evt);
            }
        });
        panelItemDetail.add(btn_check_item, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 50, -1, 30));

        subpanelWrhAddItem.add(panelItemDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 470, 460, 480));

        panelItemPriceDetail.setBackground(new java.awt.Color(241, 241, 241));
        panelItemPriceDetail.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_item_new_stock.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_item_new_stock.setText("New Stock");
        panelItemPriceDetail.add(lbl_item_new_stock, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, -1, -1));

        txt_item_stock.setEnabled(false);
        panelItemPriceDetail.add(txt_item_stock, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, 110, 30));

        lbl_item_last_stock.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_item_last_stock.setText("Last Stock");
        panelItemPriceDetail.add(lbl_item_last_stock, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        txt_item_stock_last.setEnabled(false);
        panelItemPriceDetail.add(txt_item_stock_last, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 110, 30));

        txt_item_id_update.setEnabled(false);
        panelItemPriceDetail.add(txt_item_id_update, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 240, 30));

        subpanelWrhAddItem.add(panelItemPriceDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 820, 460, 130));

        panelItemPriceDetail1.setBackground(new java.awt.Color(241, 241, 241));
        panelItemPriceDetail1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_item_stock_new.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_item_stock_new.setText("Stock");
        panelItemPriceDetail1.add(lbl_item_stock_new, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        txt_item_stock_new.setEnabled(false);
        panelItemPriceDetail1.add(txt_item_stock_new, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 110, 30));

        lbl_item_price_NTA1.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_item_price_NTA1.setText("Price NTA");
        panelItemPriceDetail1.add(lbl_item_price_NTA1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        txt_item_price_nta.setForeground(new java.awt.Color(204, 204, 204));
        txt_item_price_nta.setText("Contoh : 1140000");
        txt_item_price_nta.setEnabled(false);
        txt_item_price_nta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_item_price_ntaMouseClicked(evt);
            }
        });
        panelItemPriceDetail1.add(txt_item_price_nta, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 300, 30));

        lbl_item_price_publish1.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_item_price_publish1.setText("Price Publish");
        panelItemPriceDetail1.add(lbl_item_price_publish1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        txt_item_price_publish.setForeground(new java.awt.Color(204, 204, 204));
        txt_item_price_publish.setText("Contoh : 1140000");
        txt_item_price_publish.setEnabled(false);
        txt_item_price_publish.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_item_price_publishMouseClicked(evt);
            }
        });
        panelItemPriceDetail1.add(txt_item_price_publish, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 300, 30));

        lbl_item_price_single1.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_item_price_single1.setText("Price Single");
        panelItemPriceDetail1.add(lbl_item_price_single1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, -1, -1));

        txt_item_price_single.setForeground(new java.awt.Color(204, 204, 204));
        txt_item_price_single.setText("Contoh : 1140000");
        txt_item_price_single.setEnabled(false);
        txt_item_price_single.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_item_price_singleMouseClicked(evt);
            }
        });
        panelItemPriceDetail1.add(txt_item_price_single, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 300, 30));

        subpanelWrhAddItem.add(panelItemPriceDetail1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 470, 460, 310));

        lblWarehouseAddItem.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblWarehouseAddItem.setForeground(java.awt.Color.gray);
        lblWarehouseAddItem.setText("ADD ITEM");
        subpanelWrhAddItem.add(lblWarehouseAddItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        lbl_warehouse.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_warehouse.setForeground(java.awt.Color.gray);
        lbl_warehouse.setText("Warehouse");
        subpanelWrhAddItem.add(lbl_warehouse, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 53, -1, -1));

        jSeparator18.setForeground(new java.awt.Color(241, 241, 241));
        subpanelWrhAddItem.add(jSeparator18, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 950, 20));

        tabelItemListSP.setBackground(new java.awt.Color(241, 241, 241));

        tabelItemList.setBackground(new java.awt.Color(240, 240, 240));
        tabelItemList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Item", "Item Name", "Category", "Brand", "Guarantee", "Stock", "Info", "Price Publish", "Price Single"
            }
        ));
        tabelItemList.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabelItemList.setGridColor(new java.awt.Color(241, 241, 241));
        tabelItemList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelItemListMouseClicked(evt);
            }
        });
        tabelItemListSP.setViewportView(tabelItemList);
        if (tabelItemList.getColumnModel().getColumnCount() > 0) {
            tabelItemList.getColumnModel().getColumn(0).setPreferredWidth(120);
            tabelItemList.getColumnModel().getColumn(1).setPreferredWidth(400);
            tabelItemList.getColumnModel().getColumn(2).setPreferredWidth(100);
            tabelItemList.getColumnModel().getColumn(3).setPreferredWidth(100);
            tabelItemList.getColumnModel().getColumn(4).setPreferredWidth(100);
            tabelItemList.getColumnModel().getColumn(5).setPreferredWidth(100);
            tabelItemList.getColumnModel().getColumn(6).setPreferredWidth(100);
            tabelItemList.getColumnModel().getColumn(7).setPreferredWidth(200);
            tabelItemList.getColumnModel().getColumn(8).setPreferredWidth(200);
        }

        subpanelWrhAddItem.add(tabelItemListSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 950, 210));

        lbl_daftarkyw2.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_daftarkyw2.setForeground(java.awt.Color.gray);
        lbl_daftarkyw2.setText("List Item ");
        subpanelWrhAddItem.add(lbl_daftarkyw2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, -1));

        lbl_item_update_stock.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_item_update_stock.setForeground(java.awt.Color.gray);
        lbl_item_update_stock.setText("+ Update Stock Item");
        subpanelWrhAddItem.add(lbl_item_update_stock, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 800, -1, -1));

        lbl_item_detail.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_item_detail.setForeground(java.awt.Color.gray);
        lbl_item_detail.setText("+ Item Detail");
        subpanelWrhAddItem.add(lbl_item_detail, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 450, -1, -1));

        btn_new_item.setBackground(new java.awt.Color(0, 102, 102));
        btn_new_item.setForeground(new java.awt.Color(255, 255, 255));
        btn_new_item.setText("NEW ITEM");
        btn_new_item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_new_itemMouseClicked(evt);
            }
        });
        subpanelWrhAddItem.add(btn_new_item, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 960, 140, 30));

        btn_clear_item.setBackground(new java.awt.Color(0, 102, 153));
        btn_clear_item.setForeground(new java.awt.Color(255, 255, 255));
        btn_clear_item.setText("CLEAR");
        btn_clear_item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_clear_itemMouseClicked(evt);
            }
        });
        subpanelWrhAddItem.add(btn_clear_item, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 960, 100, 30));

        btn_process_item.setBackground(new java.awt.Color(153, 0, 0));
        btn_process_item.setForeground(new java.awt.Color(255, 255, 255));
        btn_process_item.setText("PROCESS");
        btn_process_item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_process_itemMouseClicked(evt);
            }
        });
        btn_process_item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_process_itemActionPerformed(evt);
            }
        });
        subpanelWrhAddItem.add(btn_process_item, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 960, 200, 30));

        lbl_item_price_detail.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_item_price_detail.setForeground(java.awt.Color.gray);
        lbl_item_price_detail.setText("+ Item Price Detail");
        subpanelWrhAddItem.add(lbl_item_price_detail, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 450, -1, -1));

        btn_add_item.setBackground(new java.awt.Color(0, 102, 153));
        btn_add_item.setForeground(new java.awt.Color(255, 255, 255));
        btn_add_item.setText("+ Add Item");
        btn_add_item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_add_itemMouseClicked(evt);
            }
        });
        subpanelWrhAddItem.add(btn_add_item, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 440, -1, -1));

        btn_delete_item.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/btnDelete.png"))); // NOI18N
        btn_delete_item.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_delete_item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_delete_itemMouseClicked(evt);
            }
        });
        subpanelWrhAddItem.add(btn_delete_item, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 177, -1, -1));

        ScrollPaneAddItem.setViewportView(subpanelWrhAddItem);

        panelWrhDashboard.add(ScrollPaneAddItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));
        panelWrhDashboard.add(jSeparator104, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 590, 170, 10));

        menuWrhErrorReport.setBackground(new java.awt.Color(241, 241, 241));
        menuWrhErrorReport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuWrhErrorReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuWrhErrorReportMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuWrhErrorReportMouseExited(evt);
            }
        });
        menuWrhErrorReport.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconWrhErrorReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/IconErrorReport.png"))); // NOI18N
        iconWrhErrorReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconWrhErrorReportMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconWrhErrorReportMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconWrhErrorReportMouseExited(evt);
            }
        });
        menuWrhErrorReport.add(iconWrhErrorReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        panelWrhDashboard.add(menuWrhErrorReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 555, 170, 30));
        panelWrhDashboard.add(jSeparator96, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 550, 170, 10));

        menuWrhDataTransactionPending.setBackground(new java.awt.Color(241, 241, 241));
        menuWrhDataTransactionPending.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuWrhDataTransactionPending.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuWrhDataTransactionPendingMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuWrhDataTransactionPendingMouseExited(evt);
            }
        });
        menuWrhDataTransactionPending.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconWrhDataTransactionPending.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconDataTransaction.png"))); // NOI18N
        iconWrhDataTransactionPending.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconWrhDataTransactionPendingMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconWrhDataTransactionPendingMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconWrhDataTransactionPendingMouseExited(evt);
            }
        });
        menuWrhDataTransactionPending.add(iconWrhDataTransactionPending, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 7, -1, -1));

        txt_count_trans_pending.setEditable(false);
        txt_count_trans_pending.setBackground(new java.awt.Color(32, 193, 237));
        txt_count_trans_pending.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txt_count_trans_pending.setForeground(new java.awt.Color(255, 255, 255));
        txt_count_trans_pending.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_count_trans_pending.setText("0");
        txt_count_trans_pending.setBorder(null);
        menuWrhDataTransactionPending.add(txt_count_trans_pending, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 5, 20, 20));

        panelWrhDashboard.add(menuWrhDataTransactionPending, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 515, 170, 30));
        panelWrhDashboard.add(jSeparator95, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 510, 170, 10));

        menuWrhAddVoucher.setBackground(new java.awt.Color(241, 241, 241));
        menuWrhAddVoucher.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuWrhAddVoucher.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuWrhAddVoucherMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuWrhAddVoucherMouseExited(evt);
            }
        });
        menuWrhAddVoucher.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconWrhAddVoucher.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/warehouse/iconAddVoucher.png"))); // NOI18N
        iconWrhAddVoucher.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconWrhAddVoucherMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconWrhAddVoucherMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconWrhAddVoucherMouseExited(evt);
            }
        });
        menuWrhAddVoucher.add(iconWrhAddVoucher, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 7, -1, -1));

        panelWrhDashboard.add(menuWrhAddVoucher, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 475, 170, 30));
        panelWrhDashboard.add(jSeparator97, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 470, 170, 10));

        menuWrhAddBank.setBackground(new java.awt.Color(241, 241, 241));
        menuWrhAddBank.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuWrhAddBank.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuWrhAddBankMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuWrhAddBankMouseExited(evt);
            }
        });
        menuWrhAddBank.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconWrhAddBank.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/warehouse/iconAddBank.png"))); // NOI18N
        iconWrhAddBank.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconWrhAddBankMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconWrhAddBankMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconWrhAddBankMouseExited(evt);
            }
        });
        menuWrhAddBank.add(iconWrhAddBank, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 7, -1, -1));

        panelWrhDashboard.add(menuWrhAddBank, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 435, 170, 30));
        panelWrhDashboard.add(jSeparator98, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 170, 10));

        menuWrhAddBrand.setBackground(new java.awt.Color(241, 241, 241));
        menuWrhAddBrand.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuWrhAddBrand.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuWrhAddBrandMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuWrhAddBrandMouseExited(evt);
            }
        });
        menuWrhAddBrand.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconWrhAddBrand.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/warehouse/iconAddBrand.png"))); // NOI18N
        iconWrhAddBrand.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconWrhAddBrandMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconWrhAddBrandMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconWrhAddBrandMouseExited(evt);
            }
        });
        menuWrhAddBrand.add(iconWrhAddBrand, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        panelWrhDashboard.add(menuWrhAddBrand, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 395, 170, 30));
        panelWrhDashboard.add(jSeparator99, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 170, 10));

        menuWrhAddCategory.setBackground(new java.awt.Color(241, 241, 241));
        menuWrhAddCategory.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuWrhAddCategory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuWrhAddCategoryMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuWrhAddCategoryMouseExited(evt);
            }
        });
        menuWrhAddCategory.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconWrhAddCategory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/warehouse/iconAddCategory.png"))); // NOI18N
        iconWrhAddCategory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconWrhAddCategoryMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconWrhAddCategoryMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconWrhAddCategoryMouseExited(evt);
            }
        });
        menuWrhAddCategory.add(iconWrhAddCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        panelWrhDashboard.add(menuWrhAddCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 355, 170, 30));
        panelWrhDashboard.add(jSeparator100, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 170, 10));

        menuWrhAddItem.setBackground(new java.awt.Color(241, 241, 241));
        menuWrhAddItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuWrhAddItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuWrhAddItemMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuWrhAddItemMouseExited(evt);
            }
        });
        menuWrhAddItem.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconWrhAddItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/warehouse/iconAddItem.png"))); // NOI18N
        iconWrhAddItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconWrhAddItemMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconWrhAddItemMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconWrhAddItemMouseExited(evt);
            }
        });
        menuWrhAddItem.add(iconWrhAddItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 9, -1, -1));

        panelWrhDashboard.add(menuWrhAddItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 315, 170, 30));
        panelWrhDashboard.add(jSeparator101, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 170, 10));

        menuWrhDashboard.setBackground(new java.awt.Color(241, 241, 241));
        menuWrhDashboard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuWrhDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuWrhDashboardMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuWrhDashboardMouseExited(evt);
            }
        });
        menuWrhDashboard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconWrhDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/IconDashboard.png"))); // NOI18N
        iconWrhDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconWrhDashboardMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconWrhDashboardMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconWrhDashboardMouseExited(evt);
            }
        });
        menuWrhDashboard.add(iconWrhDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 7, -1, -1));

        panelWrhDashboard.add(menuWrhDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 275, 170, 30));
        panelWrhDashboard.add(jSeparator102, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 170, 10));

        btnChangeStateWrh.setText("Change State");
        btnChangeStateWrh.setToolTipText("Click to Change State");
        btnChangeStateWrh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnChangeStateWrh.setMaximumSize(new java.awt.Dimension(73, 23));
        btnChangeStateWrh.setMinimumSize(new java.awt.Dimension(74, 23));
        btnChangeStateWrh.setPreferredSize(new java.awt.Dimension(78, 23));
        btnChangeStateWrh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnChangeStateWrhMouseClicked(evt);
            }
        });
        panelWrhDashboard.add(btnChangeStateWrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(83, 230, 100, -1));

        btnHideIPWrh.setText("Hide IP");
        btnHideIPWrh.setToolTipText("Click to Hide IP");
        btnHideIPWrh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHideIPWrh.setMaximumSize(new java.awt.Dimension(73, 23));
        btnHideIPWrh.setMinimumSize(new java.awt.Dimension(74, 23));
        btnHideIPWrh.setPreferredSize(new java.awt.Dimension(73, 23));
        btnHideIPWrh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHideIPWrhMouseClicked(evt);
            }
        });
        panelWrhDashboard.add(btnHideIPWrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 230, -1, -1));

        btnShowIPWrh.setText("Show IP");
        btnShowIPWrh.setToolTipText("Click to show IP");
        btnShowIPWrh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnShowIPWrh.setMaximumSize(new java.awt.Dimension(73, 23));
        btnShowIPWrh.setMinimumSize(new java.awt.Dimension(73, 23));
        btnShowIPWrh.setPreferredSize(new java.awt.Dimension(73, 23));
        btnShowIPWrh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnShowIPWrhMouseClicked(evt);
            }
        });
        panelWrhDashboard.add(btnShowIPWrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 230, -1, -1));

        txtServerTimeWrh.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        txtServerTimeWrh.setText("SERVER TIME");
        panelWrhDashboard.add(txtServerTimeWrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 693, -1, -1));

        txt_stateBigWrh.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        txt_stateBigWrh.setText("SERVER STATE");
        panelWrhDashboard.add(txt_stateBigWrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 693, -1, -1));

        lblStateBigWrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OnlineState.png"))); // NOI18N
        panelWrhDashboard.add(lblStateBigWrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 693, -1, -1));

        lblStateWrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OnlineState.png"))); // NOI18N
        panelWrhDashboard.add(lblStateWrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(78, 208, -1, -1));

        txt_stateWrh.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        txt_stateWrh.setText("Online");
        panelWrhDashboard.add(txt_stateWrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 210, -1, -1));

        jLabel32.setFont(new java.awt.Font("Gotham Medium", 1, 11)); // NOI18N
        jLabel32.setText("Keyko");
        panelWrhDashboard.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(595, 695, -1, -1));

        jLabel33.setFont(new java.awt.Font("Gotham Light", 2, 11)); // NOI18N
        jLabel33.setText("powered by");
        panelWrhDashboard.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 695, -1, -1));

        jLabel34.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        jLabel34.setText("NOB Tech - ");
        panelWrhDashboard.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(465, 695, -1, -1));

        txtWrhNama.setFont(new java.awt.Font("Gotham Medium", 1, 12)); // NOI18N
        txtWrhNama.setText("Warehouse");
        panelWrhDashboard.add(txtWrhNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(79, 190, -1, -1));

        jLabel35.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        jLabel35.setText("Hello, ");
        panelWrhDashboard.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 170, -1, -1));

        iconAdminWrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/AdminLogo.png"))); // NOI18N
        panelWrhDashboard.add(iconAdminWrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        bgWrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/warehouse/BgPanelWarehouse2.png"))); // NOI18N
        panelWrhDashboard.add(bgWrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelWrhDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 721));

        panelKilledbyServer.setBackground(new java.awt.Color(255, 255, 255));
        panelKilledbyServer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/warehouse/WrhServerDisconnect.png"))); // NOI18N
        panelKilledbyServer.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelKilledbyServer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 720));

        panelWrhLogin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelWrhLogin.add(txtUsernameWrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 380, 230, 40));
        panelWrhLogin.add(txtPasswordWrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 470, 230, 40));

        btnLoginWrh.setText("LOGIN");
        btnLoginWrh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLoginWrhMouseClicked(evt);
            }
        });
        panelWrhLogin.add(btnLoginWrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 550, -1, -1));

        btnResetWrh.setText("CANCEL");
        btnResetWrh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnResetWrhMouseClicked(evt);
            }
        });
        panelWrhLogin.add(btnResetWrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 550, -1, -1));

        panelLoginWrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/warehouse/panelLoginWrh.png"))); // NOI18N
        panelWrhLogin.add(panelLoginWrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 120, -1, -1));

        bgClientCashierWrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/warehouse/BgLoginWarehouse.png"))); // NOI18N
        panelWrhLogin.add(bgClientCashierWrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelWrhLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 721));

        panelWrhLandingPage.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconHome1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconHomeBlue.png"))); // NOI18N
        iconHome1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconHome1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconHome1MouseClicked(evt);
            }
        });
        panelWrhLandingPage.add(iconHome1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        bgWrhLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/warehouse/LogoWarehouse.png"))); // NOI18N
        bgWrhLogo.setToolTipText("");
        bgWrhLogo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bgWrhLogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bgWrhLogoMouseClicked(evt);
            }
        });
        panelWrhLandingPage.add(bgWrhLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 200, -1, -1));

        bgClientWarehouse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/warehouse/BgLandingPageWarehouse.png"))); // NOI18N
        panelWrhLandingPage.add(bgClientWarehouse, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelWrhLandingPage, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 720));

        panelBackOffice.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconSIM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/btnSIM.png"))); // NOI18N
        iconSIM.setToolTipText("");
        iconSIM.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconSIM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconSIMMouseClicked(evt);
            }
        });
        panelBackOffice.add(iconSIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 120, -1, -1));

        iconHRD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/landingpage/iconBackOfficeHRD.png"))); // NOI18N
        iconHRD.setToolTipText("");
        iconHRD.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconHRD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconHRDMouseClicked(evt);
            }
        });
        panelBackOffice.add(iconHRD, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 430, -1, -1));

        iconWarehouse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconBackOfficeWarehouse.png"))); // NOI18N
        iconWarehouse.setToolTipText("");
        iconWarehouse.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconWarehouse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconWarehouseMouseClicked(evt);
            }
        });
        panelBackOffice.add(iconWarehouse, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 430, -1, -1));

        iconKeuangan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/landingpage/iconBackOfficeKeuangan.png"))); // NOI18N
        iconKeuangan.setToolTipText("");
        iconKeuangan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconKeuangan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconKeuanganMouseClicked(evt);
            }
        });
        panelBackOffice.add(iconKeuangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 430, -1, -1));

        iconManagement.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/landingpage/iconBackOfficeManagement.png"))); // NOI18N
        iconManagement.setToolTipText("");
        iconManagement.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconManagement.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconManagementMouseClicked(evt);
            }
        });
        panelBackOffice.add(iconManagement, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 430, -1, -1));

        iconBOBig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/landingpage/iconBackOfficeBig.png"))); // NOI18N
        iconBOBig.setToolTipText("");
        iconBOBig.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconBOBig.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconBOBigMouseClicked(evt);
            }
        });
        panelBackOffice.add(iconBOBig, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 170, -1, -1));

        bgBackOffice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/landingpage/bgDepartment.png"))); // NOI18N
        panelBackOffice.add(bgBackOffice, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelBackOffice, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 720));

        panelFrontOffice.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconSIMFO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/btnSIM.png"))); // NOI18N
        iconSIMFO.setToolTipText("");
        iconSIMFO.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconSIMFO.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconSIMFOMouseClicked(evt);
            }
        });
        panelFrontOffice.add(iconSIMFO, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 120, -1, -1));

        iconCashier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconFrontOfficeCashier.png"))); // NOI18N
        iconCashier.setToolTipText("");
        iconCashier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconCashier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconCashierMouseClicked(evt);
            }
        });
        panelFrontOffice.add(iconCashier, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 430, -1, -1));

        iconFOBig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/landingpage/iconFrontOffice.png"))); // NOI18N
        iconFOBig.setToolTipText("");
        iconFOBig.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconFOBig.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconFOBigMouseClicked(evt);
            }
        });
        panelFrontOffice.add(iconFOBig, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 170, -1, -1));

        bgFrontOffice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/landingpage/bgDepartment.png"))); // NOI18N
        panelFrontOffice.add(bgFrontOffice, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelFrontOffice, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 720));

        panelDepartment.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconFO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/landingpage/iconFrontOffice.png"))); // NOI18N
        iconFO.setToolTipText("");
        iconFO.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconFO.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconFOMouseClicked(evt);
            }
        });
        panelDepartment.add(iconFO, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 250, -1, -1));

        iconBO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/landingpage/iconBackOffice.png"))); // NOI18N
        iconBO.setToolTipText("");
        iconBO.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconBO.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconBOMouseClicked(evt);
            }
        });
        panelDepartment.add(iconBO, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 250, -1, -1));

        bgDepartment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/landingpage/bgDepartment.png"))); // NOI18N
        panelDepartment.add(bgDepartment, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelDepartment, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 720));

        panelLandingPage.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bgLogoNOB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/Client.png"))); // NOI18N
        bgLogoNOB.setToolTipText("");
        bgLogoNOB.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bgLogoNOB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bgLogoNOBMouseClicked(evt);
            }
        });
        panelLandingPage.add(bgLogoNOB, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 200, -1, -1));

        bgLandingPage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/landingpage/bgLandingPage.png"))); // NOI18N
        panelLandingPage.add(bgLandingPage, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelLandingPage, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 720));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void bgLogoNOBMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bgLogoNOBMouseClicked
        // TODO add your handling code here:
        Mode13();
    }//GEN-LAST:event_bgLogoNOBMouseClicked

    private void iconBOMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconBOMouseClicked
        // TODO add your handling code here:
        Mode14();
    }//GEN-LAST:event_iconBOMouseClicked

    private void iconFOMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconFOMouseClicked
        // TODO add your handling code here:
        Mode15();
    }//GEN-LAST:event_iconFOMouseClicked

    private void iconManagementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconManagementMouseClicked
        // TODO add your handling code here:
        HomePageClientManagement HPCM = new HomePageClientManagement();
        HPCM.setVisible(true);
        HPCM.Mode5();
        dispose();
    }//GEN-LAST:event_iconManagementMouseClicked

    private void iconCashierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconCashierMouseClicked
        // TODO add your handling code here:
        HomePageClientCashier HPCC = new HomePageClientCashier();
        HPCC.setVisible(true);
        HPCC.Mode13();
        dispose();
    }//GEN-LAST:event_iconCashierMouseClicked

    private void btn_trans_process1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_trans_process1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_trans_process1MouseClicked

    private void btn_trans_clearall1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_trans_clearall1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_trans_clearall1MouseClicked

    private void bgWrhLogoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bgWrhLogoMouseClicked
        // TODO add your handling code here:
        Mode2();
    }//GEN-LAST:event_bgWrhLogoMouseClicked

    private void btnLoginWrhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLoginWrhMouseClicked
        // TODO add your handling code here:
        String lettercode = txtUsernameWrh.getText().substring(0, 3);
        System.out.println(lettercode);
        if (txtUsernameWrh.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please Input Your Username", "Sign In Error", JOptionPane.ERROR_MESSAGE);
            txtUsernameWrh.requestFocus();
        } else if (txtPasswordWrh.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please Input Your Password", "Sign In Error", JOptionPane.ERROR_MESSAGE);
            txtPasswordWrh.requestFocus();
        } else if (txtUsernameWrh.getText().substring(0, 3).equals("WRH")) {
            Entity_Signin ESI = new Entity_Signin();
            ESI.setUsername(txtUsernameWrh.getText());
            ESI.setPassword(txtPasswordWrh.getText());
            ESI.setStatus("Active");
            ESI.setDlc("WRH");
            try {
                int hasil = loginDao.Login(ESI);
                if (hasil == 1) {
                    JOptionPane.showMessageDialog(null, "Sign In Success", "Sign-In Success", JOptionPane.INFORMATION_MESSAGE);
                    Mode3();
                    CekTransaksiPending5Sec();
                    idAdminWrh = txtUsernameWrh.getText();
                    SetWrhStatusOnline();
                    CekStatus5Sec();
                    last_session = 3;
                    setIPWrh();
                } else {
                    JOptionPane.showMessageDialog(null, "Password or Username Does Not Match", "Sign-In Error", JOptionPane.ERROR_MESSAGE);
                    txtUsernameWrh.setText("");
                    txtPasswordWrh.setText("");
                }
            } catch (RemoteException ex) {
                Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Sorry, You Have No Access to Warehouse Area", "Sign In Failed", JOptionPane.ERROR_MESSAGE);
            txtPasswordWrh.requestFocus();
            txtUsernameWrh.setText("");
            txtPasswordWrh.setText("");
        }

        whoisonline = txtUsernameWrh.getText();
        Entity_Signin ESI = new Entity_Signin();
        ESI.setUsername(whoisonline);
        try {
            String hasil = loginDao.CekNamaOnline(ESI);
            txtWrhNama.setText(hasil);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnLoginWrhMouseClicked

    private void btnResetWrhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetWrhMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnResetWrhMouseClicked

    private void btnLogoutWrhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutWrhMouseClicked
        // TODO add your handling code here:
        Object[] options = {"LogOut", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Are you sure to Logout ?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                Mode2();
                clearFormLogin();
                SetWrhStatusOffline();
                ses.shutdown();
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btnLogoutWrhMouseClicked

    private void iconWrhErrorReportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWrhErrorReportMouseClicked
        // TODO add your handling code here:
        Mode11();
        init_Error_ID();
        initTableDaftarError();
        loadDaftarError();
        last_session = 11;
    }//GEN-LAST:event_iconWrhErrorReportMouseClicked

    private void iconWrhErrorReportMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWrhErrorReportMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuWrhErrorReport.setBackground(skyBlue);
    }//GEN-LAST:event_iconWrhErrorReportMouseEntered

    private void iconWrhErrorReportMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWrhErrorReportMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuWrhErrorReport.setBackground(lightGray);
    }//GEN-LAST:event_iconWrhErrorReportMouseExited

    private void menuWrhErrorReportMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuWrhErrorReportMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuWrhErrorReport.setBackground(skyBlue);
    }//GEN-LAST:event_menuWrhErrorReportMouseEntered

    private void menuWrhErrorReportMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuWrhErrorReportMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuWrhErrorReport.setBackground(lightGray);
    }//GEN-LAST:event_menuWrhErrorReportMouseExited

    private void iconWrhAddBankMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWrhAddBankMouseClicked
        // TODO add your handling code here:
        Mode9();
        initTabelBank();
        loadDaftarTabelBank();
        modeAddBank = "Insert";
        last_session = 9;
    }//GEN-LAST:event_iconWrhAddBankMouseClicked

    private void iconWrhAddBankMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWrhAddBankMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuWrhAddBank.setBackground(skyBlue);
    }//GEN-LAST:event_iconWrhAddBankMouseEntered

    private void iconWrhAddBankMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWrhAddBankMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuWrhAddBank.setBackground(lightGray);
    }//GEN-LAST:event_iconWrhAddBankMouseExited

    private void menuWrhAddBankMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuWrhAddBankMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuWrhAddBank.setBackground(skyBlue);
    }//GEN-LAST:event_menuWrhAddBankMouseEntered

    private void menuWrhAddBankMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuWrhAddBankMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuWrhAddBank.setBackground(lightGray);
    }//GEN-LAST:event_menuWrhAddBankMouseExited

    private void iconWrhDataTransactionPendingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWrhDataTransactionPendingMouseClicked
        // TODO add your handling code here:
        Mode7();
        initTabelTransaksiPending();
        loadDaftarTabelTransPending();
        last_session = 7;
    }//GEN-LAST:event_iconWrhDataTransactionPendingMouseClicked

    private void iconWrhDataTransactionPendingMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWrhDataTransactionPendingMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuWrhDataTransactionPending.setBackground(skyBlue);
    }//GEN-LAST:event_iconWrhDataTransactionPendingMouseEntered

    private void iconWrhDataTransactionPendingMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWrhDataTransactionPendingMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuWrhDataTransactionPending.setBackground(lightGray);
    }//GEN-LAST:event_iconWrhDataTransactionPendingMouseExited

    private void menuWrhDataTransactionPendingMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuWrhDataTransactionPendingMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuWrhDataTransactionPending.setBackground(skyBlue);
    }//GEN-LAST:event_menuWrhDataTransactionPendingMouseEntered

    private void menuWrhDataTransactionPendingMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuWrhDataTransactionPendingMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuWrhDataTransactionPending.setBackground(lightGray);
    }//GEN-LAST:event_menuWrhDataTransactionPendingMouseExited

    private void iconWrhAddBrandMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWrhAddBrandMouseClicked
        // TODO add your handling code here:
        Mode6();
        init_Brand_ID();
        initTabelBrand();
        loadDaftarTabelBrand();
        modeAddBrand = "Insert";
        last_session = 6;
    }//GEN-LAST:event_iconWrhAddBrandMouseClicked

    private void iconWrhAddBrandMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWrhAddBrandMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuWrhAddBrand.setBackground(skyBlue);
    }//GEN-LAST:event_iconWrhAddBrandMouseEntered

    private void iconWrhAddBrandMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWrhAddBrandMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuWrhAddBrand.setBackground(lightGray);
    }//GEN-LAST:event_iconWrhAddBrandMouseExited

    private void menuWrhAddBrandMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuWrhAddBrandMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuWrhAddBrand.setBackground(skyBlue);
    }//GEN-LAST:event_menuWrhAddBrandMouseEntered

    private void menuWrhAddBrandMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuWrhAddBrandMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuWrhAddBrand.setBackground(lightGray);
    }//GEN-LAST:event_menuWrhAddBrandMouseExited

    private void iconWrhAddCategoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWrhAddCategoryMouseClicked
        // TODO add your handling code here:
        Mode5();
        initTabelCategory();
        loadDaftarTabelCategory();
        init_Cat_ID();
        modeAddCategory = "Insert";
        last_session = 5;
    }//GEN-LAST:event_iconWrhAddCategoryMouseClicked

    private void iconWrhAddCategoryMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWrhAddCategoryMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuWrhAddCategory.setBackground(skyBlue);
    }//GEN-LAST:event_iconWrhAddCategoryMouseEntered

    private void iconWrhAddCategoryMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWrhAddCategoryMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuWrhAddCategory.setBackground(lightGray);
    }//GEN-LAST:event_iconWrhAddCategoryMouseExited

    private void menuWrhAddCategoryMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuWrhAddCategoryMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuWrhAddCategory.setBackground(skyBlue);
    }//GEN-LAST:event_menuWrhAddCategoryMouseEntered

    private void menuWrhAddCategoryMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuWrhAddCategoryMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuWrhAddCategory.setBackground(lightGray);
    }//GEN-LAST:event_menuWrhAddCategoryMouseExited

    private void iconWrhAddItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWrhAddItemMouseClicked
        // TODO add your handling code here:
        Mode4();
        initTabelDaftarItem();
        loadComboBoxCategory();
        loadComboBoxBrand();
        loadComboBoxSupplier();
        last_session = 4;
    }//GEN-LAST:event_iconWrhAddItemMouseClicked

    private void iconWrhAddItemMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWrhAddItemMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuWrhAddItem.setBackground(skyBlue);
    }//GEN-LAST:event_iconWrhAddItemMouseEntered

    private void iconWrhAddItemMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWrhAddItemMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuWrhAddItem.setBackground(lightGray);
    }//GEN-LAST:event_iconWrhAddItemMouseExited

    private void menuWrhAddItemMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuWrhAddItemMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuWrhAddItem.setBackground(skyBlue);
    }//GEN-LAST:event_menuWrhAddItemMouseEntered

    private void menuWrhAddItemMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuWrhAddItemMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuWrhAddItem.setBackground(lightGray);
    }//GEN-LAST:event_menuWrhAddItemMouseExited

    private void iconWrhDashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWrhDashboardMouseClicked
        // TODO add your handling code here:
        Mode3();
        last_session = 3;
    }//GEN-LAST:event_iconWrhDashboardMouseClicked

    private void iconWrhDashboardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWrhDashboardMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuWrhDashboard.setBackground(skyBlue);
    }//GEN-LAST:event_iconWrhDashboardMouseEntered

    private void iconWrhDashboardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWrhDashboardMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuWrhDashboard.setBackground(lightGray);
    }//GEN-LAST:event_iconWrhDashboardMouseExited

    private void menuWrhDashboardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuWrhDashboardMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuWrhDashboard.setBackground(skyBlue);
    }//GEN-LAST:event_menuWrhDashboardMouseEntered

    private void menuWrhDashboardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuWrhDashboardMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuWrhDashboard.setBackground(lightGray);
    }//GEN-LAST:event_menuWrhDashboardMouseExited

    private void btnChangeStateWrhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChangeStateWrhMouseClicked
        // TODO add your handling code here:
        int hasilUpdateStatusWrh = 0;
        Object[] options = {"Online", "Idle", "Invisible", "Offline"};
        int reply = JOptionPane.showOptionDialog(null, "Choose your Client [Cashier] state : ", "State Chooser", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                if (txt_stateWrh.getText().equals("Online")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Online", "Warning Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int hasilSetWrhStatus = loginDao.UpdateStatusOnline(kodeSistem);
                        if (hasilSetWrhStatus == 1) {
                            txt_stateBigWrh.setText("Server is Well-Connected");
                            lblStateWrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OnlineState.png")));
                            lblStateBigWrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OnlineState.png")));
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case 1:
                if (txt_stateWrh.getText().equals("Idle")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Idle", "Warning Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int hasilSetWrhStatus = loginDao.UpdateStatusIdle(kodeSistem);
                        if (hasilSetWrhStatus == 1) {
                            txt_stateBigWrh.setText("Server is Well-Connected");
                            lblStateWrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/IdleState.png")));
                            lblStateBigWrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/IdleState.png")));
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case 2:
                if (txt_stateWrh.getText().equals("Invisible")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Invisible", "Warning Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int hasilSetWrhStatus = loginDao.UpdateStatusInvisible(kodeSistem);
                        if (hasilSetWrhStatus == 1) {
                            txt_stateBigWrh.setText("Server is Well-Connected");
                            lblStateWrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/InvisState.png")));
                            lblStateBigWrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/InvisState.png")));
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case 3:
                if (txt_stateWrh.getText().equals("Offline")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Offline", "Warning Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int hasilSetWrhStatus = loginDao.UpdateStatusDisconnect(kodeSistem);
                        if (hasilSetWrhStatus == 1) {

                            txt_stateBigWrh.setText("Server is Disconnected");
                            lblStateWrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OffState.png")));
                            lblStateBigWrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OffState.png")));
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            //donothing
            default:
                break;
        }
    }//GEN-LAST:event_btnChangeStateWrhMouseClicked

    private void btnHideIPWrhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHideIPWrhMouseClicked
        // TODO add your handling code here:
        whoisonline = txtUsernameWrh.getText();
//        System.out.println(whoisonline);
        Entity_Signin ESI = new Entity_Signin();
        ESI.setUsername(whoisonline);
        try {
            String hasil = loginDao.CekNamaOnline(ESI);
            txtWrhNama.setText(hasil);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        ShowIPModeWrh();
    }//GEN-LAST:event_btnHideIPWrhMouseClicked

    private void btnShowIPWrhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnShowIPWrhMouseClicked
        // TODO add your handling code here:
        txtWrhNama.setText(ip);
        HideIPModeWrh();
    }//GEN-LAST:event_btnShowIPWrhMouseClicked

    private void iconWarehouseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWarehouseMouseClicked
        // TODO add your handling code here:
        Mode1();
    }//GEN-LAST:event_iconWarehouseMouseClicked

    private void txt_trans_searchitemByNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_trans_searchitemByNameMouseClicked
        // TODO add your handling code here:
        txt_trans_searchitemByID.setText("Please enter to search ...");
        txt_trans_searchitemByName.setText("");
    }//GEN-LAST:event_txt_trans_searchitemByNameMouseClicked

    private void txt_trans_searchitemByNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_trans_searchitemByNameKeyPressed
        // TODO add your handling code here:
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            initTabelDaftarItem();
            loadDaftarItembyName();
        }
    }//GEN-LAST:event_txt_trans_searchitemByNameKeyPressed

    private void txt_trans_searchitemByNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_trans_searchitemByNameKeyTyped
        // TODO add your handling code here:
        initTabelDaftarItem();
        loadDaftarItembyName();
    }//GEN-LAST:event_txt_trans_searchitemByNameKeyTyped

    private void txt_trans_searchitemByIDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_trans_searchitemByIDMouseClicked
        // TODO add your handling code here:
        txt_trans_searchitemByID.setText("");
        txt_trans_searchitemByName.setText("Please enter to search ...");
    }//GEN-LAST:event_txt_trans_searchitemByIDMouseClicked

    private void txt_trans_searchitemByIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_trans_searchitemByIDKeyPressed
        // TODO add your handling code here:
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            initTabelDaftarItem();
            loadDaftarItembyID();
        }
    }//GEN-LAST:event_txt_trans_searchitemByIDKeyPressed

    private void txt_trans_searchitemByIDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_trans_searchitemByIDKeyTyped
        // TODO add your handling code here:
        initTabelDaftarItem();
        loadDaftarItembyID();
    }//GEN-LAST:event_txt_trans_searchitemByIDKeyTyped

    private void tabelItemListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelItemListMouseClicked
        // TODO add your handling code here:
        baris = tabelItemList.getSelectedRow();
        kolom = tabelItemList.getSelectedColumn();
        dataTerpilih = tabelItemList.getValueAt(baris, 0).toString();
    }//GEN-LAST:event_tabelItemListMouseClicked

    private void ScrollPaneAddItemMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_ScrollPaneAddItemMouseWheelMoved
        // TODO add your handling code here:
        subpanelWrhAddItem.setAutoscrolls(true);
        ScrollPaneAddItem.setAutoscrolls(true);
        ScrollPaneAddItem.getVerticalScrollBar().setUnitIncrement(40);
    }//GEN-LAST:event_ScrollPaneAddItemMouseWheelMoved

    private void ScrollPaneAddItemMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ScrollPaneAddItemMouseEntered
        // TODO add your handling code here:
        subpanelWrhAddItem.setAutoscrolls(true);
        ScrollPaneAddItem.setAutoscrolls(true);
        ScrollPaneAddItem.getVerticalScrollBar().setUnitIncrement(40);
    }//GEN-LAST:event_ScrollPaneAddItemMouseEntered

    private void btn_process_itemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_process_itemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_process_itemActionPerformed

    private void btn_add_itemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_add_itemMouseClicked
        // TODO add your handling code here:
        AddItemDisabled();
        txt_item_id.setEnabled(true);
    }//GEN-LAST:event_btn_add_itemMouseClicked

    private void btn_check_itemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_check_itemMouseClicked
        // TODO add your handling code here:
        Entity_Warehouse EW = new Entity_Warehouse();
        EW.setItem_id(txt_item_id.getText());

        int hasil;
        int Stock;
        try {
            hasil = warehouseDao.CekItem(EW);
            if (hasil == 1) {
                JOptionPane.showMessageDialog(null, "Data Found, Just update item stock", "Item Found", JOptionPane.WARNING_MESSAGE);
                modeAddItem = "Update";
                txt_item_id_update.setText(txt_item_id.getText());
                UpdateItemEnabled();
                txt_item_stock.requestFocus();

                Stock = warehouseDao.CekStock(EW);
                txt_item_stock_last.setText(String.valueOf(Stock));
            } else {
                JOptionPane.showMessageDialog(null, "Data Not Found, insert New Item", "Item Not Found", JOptionPane.INFORMATION_MESSAGE);
                modeAddItem = "Insert";
                AddItemEnabled();
                UpdateItemDisabled();
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_check_itemMouseClicked

    private void btn_process_itemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_process_itemMouseClicked
        // TODO add your handling code here:
        if (modeAddItem.equals("Insert")) {
            if (txt_item_id.getText().equals("")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Item ID", "Item Save Warning", JOptionPane.WARNING_MESSAGE);
                txt_item_id.requestFocus();
            } else if (txt_item_name.getText().equals("")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Item Name", "Item Save Warning", JOptionPane.WARNING_MESSAGE);
                txt_item_name.requestFocus();
            } else if (cb_item_category.getSelectedIndex() == 0) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Choose Category", "Item Save Warning", JOptionPane.WARNING_MESSAGE);
                cb_item_category.requestFocus();
            } else if (cb_item_brand.getSelectedIndex() == 0) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Choose Brand", "Item Save Warning", JOptionPane.WARNING_MESSAGE);
                cb_item_brand.requestFocus();
            } else if (txt_item_guarantee.getText().equals("")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Item Guarantee", "Item Save Warning", JOptionPane.WARNING_MESSAGE);
                txt_item_guarantee.requestFocus();
            } else if (txt_item_info.getText().equals("")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Item Info", "Item Save Warning", JOptionPane.WARNING_MESSAGE);
                txt_item_info.requestFocus();
            } else if (txt_item_stock_new.getText().equals("")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Item Stock", "Item Save Warning", JOptionPane.WARNING_MESSAGE);
                txt_item_stock_new.requestFocus();
            } else if (txt_item_stock_new.getText().equals("")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Item Stock", "Item Save Warning", JOptionPane.WARNING_MESSAGE);
                txt_item_stock_new.requestFocus();
            } else if (txt_item_price_nta.getText().equals("Contoh : 1140000")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Item Price NTA", "Item Save Warning", JOptionPane.WARNING_MESSAGE);
                txt_item_price_nta.requestFocus();
            } else if (txt_item_price_publish.getText().equals("Contoh : 1140000")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Item Price Publish", "Item Save Warning", JOptionPane.WARNING_MESSAGE);
                txt_item_price_publish.requestFocus();
            } else if (txt_item_price_single.getText().equals("Contoh : 1140000")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Item Price Single", "Item Save Warning", JOptionPane.WARNING_MESSAGE);
                txt_item_price_single.requestFocus();
            } else {
                Entity_Warehouse EW = new Entity_Warehouse();
                EW.setItem_id(txt_item_id.getText());
                EW.setItem_name(txt_item_name.getText());
                EW.setItem_category(id_cat);
                EW.setItem_brand(id_brand);
                EW.setItem_guarantee(txt_item_guarantee.getText());
                EW.setItem_info(txt_item_info.getText());
                EW.setItem_supplier(id_supplier);
                EW.setItem_stock(Integer.valueOf(txt_item_stock_new.getText()));
                EW.setItem_price_nta(Integer.valueOf(txt_item_price_nta.getText()));
                EW.setItem_price_pub(Integer.valueOf(txt_item_price_publish.getText()));
                EW.setItem_price_single(Integer.valueOf(txt_item_price_single.getText()));

                try {
                    int hasil = warehouseDao.saveItem(EW);
                    if (hasil == 1) {
                        JOptionPane.showMessageDialog(null, "Save Item Success", "Item Save Info", JOptionPane.INFORMATION_MESSAGE);
                        clearFormItem();
                        AddItemDisabled();
                        modeAddItem = "Insert";
                    } else {
                        JOptionPane.showMessageDialog(null, "Save Item Failed", "Item Save Info", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (modeAddItem.equals("Update")) {
            stockUpdate = Integer.parseInt(txt_item_stock_last.getText()) + Integer.parseInt(txt_item_stock.getText());

            Entity_Warehouse EW = new Entity_Warehouse();
            EW.setItem_stock(Integer.valueOf(stockUpdate));
            EW.setItem_id(txt_item_id_update.getText());

            try {
                int hasilupdate = warehouseDao.updateStock(EW);
                if (hasilupdate == 1) {
                    JOptionPane.showMessageDialog(null, "Update Item Success", "Item Update Info", JOptionPane.INFORMATION_MESSAGE);
                    clearFormItem();
                    AddItemDisabled();
                    modeAddItem = "Insert";
                } else {
                    JOptionPane.showMessageDialog(null, "Update Item Failed", "Item Update Info", JOptionPane.ERROR_MESSAGE);
                }
            } catch (RemoteException ex) {
                Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btn_process_itemMouseClicked

    private void cb_item_categoryItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_item_categoryItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == evt.SELECTED) {

            Entity_Warehouse EW = new Entity_Warehouse();
            EW.setItem_category((String) cb_item_category.getSelectedItem());
            try {
                id_cat = warehouseDao.getIDCategory(EW);
            } catch (RemoteException ex) {
                Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_cb_item_categoryItemStateChanged

    private void cb_item_brandItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_item_brandItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == evt.SELECTED) {

            Entity_Warehouse EW = new Entity_Warehouse();
            EW.setItem_brand((String) cb_item_brand.getSelectedItem());
            try {
                id_brand = warehouseDao.getIDBrand(EW);
            } catch (RemoteException ex) {
                Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_cb_item_brandItemStateChanged

    private void cb_item_supplierItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_item_supplierItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == evt.SELECTED) {

            Entity_Warehouse EW = new Entity_Warehouse();
            EW.setItem_supplier((String) cb_item_supplier.getSelectedItem());
            try {
                id_supplier = warehouseDao.getIDSupplier(EW);
            } catch (RemoteException ex) {
                Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_cb_item_supplierItemStateChanged

    private void txt_item_price_ntaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_item_price_ntaMouseClicked
        // TODO add your handling code here:
        if (modeAddItem == "Insert") {
            txt_item_price_nta.setText("");
        } else {
            //donothing
        }
    }//GEN-LAST:event_txt_item_price_ntaMouseClicked

    private void txt_item_price_publishMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_item_price_publishMouseClicked
        // TODO add your handling code here:
        if (modeAddItem == "Insert") {
            txt_item_price_publish.setText("");
        } else {
            //donothing
        }
    }//GEN-LAST:event_txt_item_price_publishMouseClicked

    private void txt_item_price_singleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_item_price_singleMouseClicked
        // TODO add your handling code here:
        if (modeAddItem == "Insert") {
            txt_item_price_single.setText("");
        } else {
            //donothing
        }
    }//GEN-LAST:event_txt_item_price_singleMouseClicked

    private void btn_clear_itemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_clear_itemMouseClicked
        // TODO add your handling code here:       
        Object[] options = {"Yes", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Clear Add Item Form, Are you sure?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                clearFormItem();
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btn_clear_itemMouseClicked

    private void btn_new_itemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_new_itemMouseClicked
        // TODO add your handling code here:
        Object[] options = {"Yes", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "The last change will be not recorded, Are you sure?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                clearFormItem();
                AddItemDisabled();
                UpdateItemDisabled();
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btn_new_itemMouseClicked

    private void btnSave_CatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSave_CatMouseClicked
        // TODO add your handling code here:
        if (modeAddCategory.equals("Insert")) {
            if (txt_cat_name.getText().equals("")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Category Name", "Warning", JOptionPane.WARNING_MESSAGE);
                txt_cat_name.requestFocus();
            } else {
                Entity_Category EC = new Entity_Category();
                EC.setCat_id(txt_cat_id.getText());
                EC.setCat_name(txt_cat_name.getText());
                try {
                    int hasilAddCategory = warehouseDao.saveCategory(EC);
                    if (hasilAddCategory == 1) {
                        JOptionPane.showMessageDialog(null, "Add Category Success", "Info", JOptionPane.INFORMATION_MESSAGE);
                        initTabelCategory();
                        loadDaftarTabelCategory();
                        init_Cat_ID();
                        txt_cat_name.setText("");
                    } else {
                        evt.consume();
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Add Category Failed", "Info", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (modeAddCategory.equals("Update")) {
            if (txt_cat_name.getText().equals("")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Category Name", "Warning", JOptionPane.WARNING_MESSAGE);
                txt_cat_name.requestFocus();
            } else {
                Entity_Category EC = new Entity_Category();
                EC.setCat_id(txt_cat_id.getText());
                EC.setCat_name(txt_cat_name.getText());
                try {
                    int hasilUpdateCategory = warehouseDao.updateCategory(EC);
                    if (hasilUpdateCategory == 1) {
                        JOptionPane.showMessageDialog(null, "Update Category Success", "Info", JOptionPane.INFORMATION_MESSAGE);
                        initTabelCategory();
                        loadDaftarTabelCategory();
                        init_Cat_ID();
                        txt_cat_name.setText("");
                    } else {
                        evt.consume();
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Update Category Failed", "Info", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_btnSave_CatMouseClicked

    private void btnClear_CatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClear_CatMouseClicked
        // TODO add your handling code here:
        init_Cat_ID();
        txt_cat_name.setText("");
    }//GEN-LAST:event_btnClear_CatMouseClicked

    private void tabelDaftarCategoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelDaftarCategoryMouseClicked
        // TODO add your handling code here:

        baris = tabelDaftarCategory.getSelectedRow();
        kolom = tabelDaftarCategory.getSelectedColumn();
        dataTerpilih = tabelDaftarCategory.getValueAt(baris, 0).toString();

        Object[] options = {"Yes", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Update Category ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                DefaultTableModel model = (DefaultTableModel) tabelDaftarCategory.getModel();
                int selectedRowIndex = tabelDaftarCategory.getSelectedRow();
                txt_cat_id.setText(model.getValueAt(selectedRowIndex, 0).toString());
                txt_cat_name.setText(model.getValueAt(selectedRowIndex, 1).toString());
                modeAddCategory = "Update";
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_tabelDaftarCategoryMouseClicked

    private void btn_delete_categoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_delete_categoryMouseClicked
        // TODO add your handling code here:
        if (dataTerpilih == null) {
            JOptionPane.showMessageDialog(null, "Choose Category From Table to Delete", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            Object[] options = {"Yes", "Cancel"};
            int reply = JOptionPane.showOptionDialog(null, "Are you sure to delete this Category ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            switch (reply) {
                case 0:
                    Entity_Category EC = new Entity_Category();
                    EC.setCat_id(dataTerpilih);
                    try {
                        int hasil = warehouseDao.deleteCategory(EC);
                        if (hasil == 1) {
                            JOptionPane.showMessageDialog(null, "Delete Category Success", "Info", JOptionPane.INFORMATION_MESSAGE);
                            initTabelCategory();
                            loadDaftarTabelCategory();
                            init_Cat_ID();
                            txt_cat_name.setText("");
                        } else {
                            evt.consume();
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(null, "Delete Category Failed", "Info", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 1:
                    break;
                default:
                    break;
            }
        }
    }//GEN-LAST:event_btn_delete_categoryMouseClicked

    private void btn_delete_itemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_delete_itemMouseClicked
        // TODO add your handling code here:
        if (dataTerpilih == null) {
            JOptionPane.showMessageDialog(null, "Choose Item From Table to Delete", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            Object[] options = {"Yes", "Cancel"};
            int reply = JOptionPane.showOptionDialog(null, "Are you sure to delete this Item ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            switch (reply) {
                case 0:
                    Entity_Warehouse EW = new Entity_Warehouse();
                    EW.setItem_id(dataTerpilih);
                    try {
                        int hasil = warehouseDao.deleteItem(EW);
                        if (hasil == 1) {
                            JOptionPane.showMessageDialog(null, "Delete Item Success", "Info", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            evt.consume();
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(null, "Delete Item Failed", "Info", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 1:
                    break;
                default:
                    break;
            }
        }
    }//GEN-LAST:event_btn_delete_itemMouseClicked

    private void btnSave_BrandMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSave_BrandMouseClicked
        // TODO add your handling code here:
        if (modeAddBrand.equals("Insert")) {
            if (txt_brand_name.getText().equals("")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Brand Name", "Warning", JOptionPane.WARNING_MESSAGE);
                txt_brand_name.requestFocus();
            } else {
                Entity_Brand EB = new Entity_Brand();
                EB.setBrand_id(txt_brand_id.getText());
                EB.setBrand_name(txt_brand_name.getText());
                try {
                    int hasilAddBrand = warehouseDao.saveBrand(EB);
                    if (hasilAddBrand == 1) {
                        JOptionPane.showMessageDialog(null, "Add Brand Success", "Info", JOptionPane.INFORMATION_MESSAGE);
                        initTabelBrand();
                        loadDaftarTabelBrand();
                        init_Brand_ID();
                        txt_brand_name.setText("");
                    } else {
                        evt.consume();
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Add Brand Failed", "Info", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (modeAddBrand.equals("Update")) {
            if (txt_brand_name.getText().equals("")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Brand Name", "Warning", JOptionPane.WARNING_MESSAGE);
                txt_brand_name.requestFocus();
            } else {
                Entity_Brand EB = new Entity_Brand();
                EB.setBrand_id(txt_brand_id.getText());
                EB.setBrand_name(txt_brand_name.getText());
                try {
                    int hasilUpdateBrand = warehouseDao.updateBrand(EB);
                    if (hasilUpdateBrand == 1) {
                        JOptionPane.showMessageDialog(null, "Update Brand Success", "Info", JOptionPane.INFORMATION_MESSAGE);
                        initTabelBrand();
                        loadDaftarTabelBrand();
                        init_Brand_ID();
                        txt_brand_name.setText("");
                    } else {
                        evt.consume();
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Update Brand Failed", "Info", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_btnSave_BrandMouseClicked

    private void btnClear_BrandMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClear_BrandMouseClicked
        // TODO add your handling code here:
        init_Brand_ID();
        txt_brand_name.setText("");
    }//GEN-LAST:event_btnClear_BrandMouseClicked

    private void tabelDaftarBrandMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelDaftarBrandMouseClicked
        // TODO add your handling code here:
        baris = tabelDaftarBrand.getSelectedRow();
        kolom = tabelDaftarBrand.getSelectedColumn();
        dataTerpilih = tabelDaftarBrand.getValueAt(baris, 0).toString();

        Object[] options = {"Yes", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Update Brand ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                DefaultTableModel model = (DefaultTableModel) tabelDaftarBrand.getModel();
                int selectedRowIndex = tabelDaftarBrand.getSelectedRow();
                txt_brand_id.setText(model.getValueAt(selectedRowIndex, 0).toString());
                txt_brand_name.setText(model.getValueAt(selectedRowIndex, 1).toString());
                modeAddBrand = "Update";
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_tabelDaftarBrandMouseClicked

    private void btn_delete_brandMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_delete_brandMouseClicked
        // TODO add your handling code here:
        if (dataTerpilih == null) {
            JOptionPane.showMessageDialog(null, "Choose Brand From Table to Delete", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            Object[] options = {"Yes", "Cancel"};
            int reply = JOptionPane.showOptionDialog(null, "Are you sure to delete this Brand ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            switch (reply) {
                case 0:
                    Entity_Brand EB = new Entity_Brand();
                    EB.setBrand_id(dataTerpilih);
                    try {
                        int hasil = warehouseDao.deleteBrand(EB);
                        if (hasil == 1) {
                            JOptionPane.showMessageDialog(null, "Delete Brand Success", "Info", JOptionPane.INFORMATION_MESSAGE);
                            initTabelBrand();
                            loadDaftarTabelBrand();
                            init_Brand_ID();
                            txt_brand_name.setText("");
                        } else {
                            evt.consume();
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(null, "Delete Brand Failed", "Info", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 1:
                    break;
                default:
                    break;
            }
        }
    }//GEN-LAST:event_btn_delete_brandMouseClicked

    private void tabelDaftarTransactionPendingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelDaftarTransactionPendingMouseClicked
        // TODO add your handling code here:
        baris = tabelDaftarTransactionPending.getSelectedRow();
        kolom = tabelDaftarTransactionPending.getSelectedColumn();
        dataTerpilih = tabelDaftarTransactionPending.getValueAt(baris, 0).toString();
    }//GEN-LAST:event_tabelDaftarTransactionPendingMouseClicked

    private void btn_open_transMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_open_transMouseClicked
        // TODO add your handling code here:
        Object[] options = {"Yes", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Open Transaction ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                Mode8();
                last_session = 8;
                DefaultTableModel model = (DefaultTableModel) tabelDaftarTransactionPending.getModel();
                int selectedRowIndex = tabelDaftarTransactionPending.getSelectedRow();
                txt_trans_invoiceno.setText(model.getValueAt(selectedRowIndex, 0).toString());
                txt_trans_date.setText(model.getValueAt(selectedRowIndex, 1).toString());
                txt_trans_cust_id.setText(model.getValueAt(selectedRowIndex, 2).toString());
                txt_trans_grand_total.setText(model.getValueAt(selectedRowIndex, 4).toString());
                Entity_Distributor ED = new Entity_Distributor();
                ED.setDis_id(txt_trans_cust_id.getText());
                try {
                    namaDistributor = transactionDao.CekNamaDistributor(ED);
                    emailDistributor = transactionDao.CekEmailDistributor(ED);
                    phoneDistributor = transactionDao.CekPhoneDistributor(ED);
                    txt_trans_custname.setText(namaDistributor);
                    txt_trans_custmail.setText(emailDistributor);
                    txt_trans_custphone.setText(phoneDistributor);
                    initTabelItemPending();
                    loadDaftarTabelItemPending();
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btn_open_transMouseClicked

    private void btn_trans_show_ntaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_trans_show_ntaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_trans_show_ntaMouseClicked

    private void btn_trans_hide_ntaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_trans_hide_ntaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_trans_hide_ntaMouseClicked

    private void tabelShoppingCartPendingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelShoppingCartPendingMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabelShoppingCartPendingMouseClicked

    private void btn_pending_successMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_pending_successMouseClicked
        // TODO add your handling code here:
        Object[] options = {"Yes", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Update Transaction Status Success ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                Entity_Transaction ET = new Entity_Transaction();
                ET.setTrans_updateBy(idAdminWrh);
                ET.setTrans_id(txt_trans_invoiceno.getText());
                try {
                    int hasil = warehouseDao.updateTrans_Success(ET);
                    if (hasil == 1) {
                        JOptionPane.showMessageDialog(null, "Transaction Success - " + txt_trans_invoiceno.getText() + "\nUpdate Transaction Success", "Info", JOptionPane.INFORMATION_MESSAGE);
                        Mode7();
                        initTabelTransaksiPending();
                        loadDaftarTabelTransPending();
                    } else {
                        evt.consume();
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Update Transaction Failed", "Info", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btn_pending_successMouseClicked

    private void btn_pending_failedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_pending_failedMouseClicked
        // TODO add your handling code here:
        Object[] options = {"Yes", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Update Transaction Status Success ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                Entity_Transaction ET = new Entity_Transaction();
                ET.setTrans_updateBy(idAdminWrh);
                ET.setTrans_id(txt_trans_invoiceno.getText());
                try {
                    int hasil = warehouseDao.updateTrans_Failed(ET);
                    if (hasil == 1) {
                        JOptionPane.showMessageDialog(null, "Transaction Failed - " + txt_trans_invoiceno.getText() + "\nUpdate Transaction Success", "Info", JOptionPane.INFORMATION_MESSAGE);
                        Mode7();
                        initTabelTransaksiPending();
                        loadDaftarTabelTransPending();
                    } else {
                        evt.consume();
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Update Transaction Failed", "Info", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btn_pending_failedMouseClicked

    private void ScrollPaneWrhTransactionPendingProcessSPMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_ScrollPaneWrhTransactionPendingProcessSPMouseWheelMoved
        // TODO add your handling code here:
        subpanelWrhTransactionPendingProcess.setAutoscrolls(true);
        ScrollPaneWrhTransactionPendingProcessSP.setAutoscrolls(true);
        ScrollPaneWrhTransactionPendingProcessSP.getVerticalScrollBar().setUnitIncrement(40);
    }//GEN-LAST:event_ScrollPaneWrhTransactionPendingProcessSPMouseWheelMoved

    private void ScrollPaneWrhTransactionPendingProcessSPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ScrollPaneWrhTransactionPendingProcessSPMouseEntered
        // TODO add your handling code here:
        subpanelWrhTransactionPendingProcess.setAutoscrolls(true);
        ScrollPaneWrhTransactionPendingProcessSP.setAutoscrolls(true);
        ScrollPaneWrhTransactionPendingProcessSP.getVerticalScrollBar().setUnitIncrement(40);
    }//GEN-LAST:event_ScrollPaneWrhTransactionPendingProcessSPMouseEntered

    private void iconWrhAddVoucherMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWrhAddVoucherMouseClicked
        // TODO add your handling code here:
        Mode10();
        initTabelVoucher();
        loadDaftarTabelVoucher();
        modeAddVoucher = "Insert";
        last_session = 10;
    }//GEN-LAST:event_iconWrhAddVoucherMouseClicked

    private void iconWrhAddVoucherMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWrhAddVoucherMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuWrhAddVoucher.setBackground(skyBlue);
    }//GEN-LAST:event_iconWrhAddVoucherMouseEntered

    private void iconWrhAddVoucherMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWrhAddVoucherMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuWrhAddVoucher.setBackground(lightGray);
    }//GEN-LAST:event_iconWrhAddVoucherMouseExited

    private void menuWrhAddVoucherMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuWrhAddVoucherMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuWrhAddVoucher.setBackground(skyBlue);
    }//GEN-LAST:event_menuWrhAddVoucherMouseEntered

    private void menuWrhAddVoucherMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuWrhAddVoucherMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuWrhAddVoucher.setBackground(lightGray);
    }//GEN-LAST:event_menuWrhAddVoucherMouseExited

    private void btnSave_BankMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSave_BankMouseClicked
        // TODO add your handling code here:
        if (modeAddBank.equals("Insert")) {
            if (txt_bank_id.getText().equals("")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Bank ID", "Warning", JOptionPane.WARNING_MESSAGE);
                txt_bank_id.requestFocus();
            } else if (txt_bank_name.getText().equals("")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Bank Name", "Warning", JOptionPane.WARNING_MESSAGE);
                txt_bank_name.requestFocus();
            } else if (txt_bank_desc.getText().equals("")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Bank Description", "Warning", JOptionPane.WARNING_MESSAGE);
                txt_bank_desc.requestFocus();
            } else if (cb_bank_status.getSelectedIndex() == 0) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Choose Bank Status", "Warning", JOptionPane.WARNING_MESSAGE);
                cb_bank_status.requestFocus();
            } else {
                Entity_Bank EB = new Entity_Bank();
                EB.setBank_id(txt_bank_id.getText());
                EB.setBank_name(txt_bank_name.getText());
                EB.setBank_description(txt_bank_desc.getText());
                EB.setBank_status(String.valueOf(cb_bank_status.getSelectedItem()));

                try {
                    int hasilAddBank = warehouseDao.saveBank(EB);
                    if (hasilAddBank == 1) {
                        JOptionPane.showMessageDialog(null, "Add Bank Success", "Info", JOptionPane.INFORMATION_MESSAGE);
                        initTabelBank();
                        loadDaftarTabelBank();
                        ClearFormAddBank();
                        txt_bank_id.setEnabled(true);
                        txt_bank_name.setEnabled(true);
                        txt_bank_desc.setEnabled(true);
                    } else {
                        evt.consume();
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Add Brand Failed", "Info", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (modeAddBank.equals("Update")) {
            if (txt_bank_id.getText().equals("")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Bank ID", "Warning", JOptionPane.WARNING_MESSAGE);
                txt_bank_id.requestFocus();
            } else if (txt_bank_name.getText().equals("")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Bank Name", "Warning", JOptionPane.WARNING_MESSAGE);
                txt_bank_name.requestFocus();
            } else if (txt_bank_desc.getText().equals("")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Bank Description", "Warning", JOptionPane.WARNING_MESSAGE);
                txt_bank_desc.requestFocus();
            } else if (cb_bank_status.getSelectedIndex() == 0) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Choose Bank Status", "Warning", JOptionPane.WARNING_MESSAGE);
                cb_bank_status.requestFocus();
            } else {
                Entity_Bank EB = new Entity_Bank();
                EB.setBank_status(String.valueOf(cb_bank_status.getSelectedItem()));
                EB.setBank_id(txt_bank_id.getText());
                try {
                    int hasilUpdateBank = warehouseDao.updateBank(EB);
                    System.out.println(hasilUpdateBank);
                    if (hasilUpdateBank == 1) {
                        JOptionPane.showMessageDialog(null, "Update Bank Success", "Info", JOptionPane.INFORMATION_MESSAGE);
                        initTabelBank();
                        loadDaftarTabelBank();
                        ClearFormAddBank();
                    } else {
                        evt.consume();
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Update Bank Failed", "Info", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_btnSave_BankMouseClicked

    private void btnClear_BankMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClear_BankMouseClicked
        // TODO add your handling code here:
        ClearFormAddBank();
    }//GEN-LAST:event_btnClear_BankMouseClicked

    private void tabelDaftarBankMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelDaftarBankMouseClicked
        // TODO add your handling code here:
        baris = tabelDaftarBank.getSelectedRow();
        kolom = tabelDaftarBank.getSelectedColumn();
        dataTerpilih = tabelDaftarBank.getValueAt(baris, 0).toString();

        Object[] options = {"Yes", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Update Bank ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                DefaultTableModel model = (DefaultTableModel) tabelDaftarBank.getModel();
                int selectedRowIndex = tabelDaftarBank.getSelectedRow();
                txt_bank_id.setEnabled(false);
                txt_bank_name.setEnabled(false);
                txt_bank_desc.setEnabled(false);

                txt_bank_id.setText(model.getValueAt(selectedRowIndex, 0).toString());
                txt_bank_name.setText(model.getValueAt(selectedRowIndex, 1).toString());
                txt_bank_desc.setText(model.getValueAt(selectedRowIndex, 2).toString());
                modeAddBank = "Update";
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_tabelDaftarBankMouseClicked

    private void btnDelete_bankMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelete_bankMouseClicked
        // TODO add your handling code here:
        if (dataTerpilih == null) {
            JOptionPane.showMessageDialog(null, "Choose Bank From Table to Delete", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            Object[] options = {"Yes", "Cancel"};
            int reply = JOptionPane.showOptionDialog(null, "Are you sure to delete this Bank ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            switch (reply) {
                case 0:
                    Entity_Bank EB = new Entity_Bank();
                    EB.setBank_id(dataTerpilih);
                    try {
                        int hasildeletebank = warehouseDao.deleteBank(EB);
                        System.out.println(hasildeletebank);
                        if (hasildeletebank == 1) {
                            JOptionPane.showMessageDialog(null, "Delete Bank Success", "Info", JOptionPane.INFORMATION_MESSAGE);
                            initTabelBank();
                            loadDaftarTabelBank();
                            ClearFormAddBank();
                        } else {
                            evt.consume();
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(null, "Delete Bank Failed", "Info", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 1:
                    break;
                default:
                    break;
            }
        }
    }//GEN-LAST:event_btnDelete_bankMouseClicked

    private void tabelDaftarVoucherMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelDaftarVoucherMouseClicked
        // TODO add your handling code here:
        baris = tabelDaftarVoucher.getSelectedRow();
        kolom = tabelDaftarVoucher.getSelectedColumn();
        dataTerpilih = tabelDaftarVoucher.getValueAt(baris, 0).toString();

        Object[] options = {"Yes", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Update Voucher ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                DefaultTableModel model = (DefaultTableModel) tabelDaftarVoucher.getModel();
                int selectedRowIndex = tabelDaftarVoucher.getSelectedRow();
                txt_voucher_id.setEnabled(false);
                txt_voucher_id.setText(model.getValueAt(selectedRowIndex, 0).toString());
                modeAddVoucher = "Update";
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_tabelDaftarVoucherMouseClicked

    private void btnSave_voucherMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSave_voucherMouseClicked
        // TODO add your handling code here:
        if (modeAddVoucher.equals("Insert")) {
            if (txt_voucher_id.getText().equals("")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Voucher ID", "Warning", JOptionPane.WARNING_MESSAGE);
                txt_voucher_id.requestFocus();
            } else if (dt_voucher_expired.getDate().equals(null)) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Voucher Expired Date", "Warning", JOptionPane.WARNING_MESSAGE);
                dt_voucher_expired.requestFocus();
            } else if (txt_voucher_stock.getText().equals("")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Voucher Stock", "Warning", JOptionPane.WARNING_MESSAGE);
                txt_voucher_stock.requestFocus();
            } else if (txt_voucher_discount.getText().equals("")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Voucher Discount", "Warning", JOptionPane.WARNING_MESSAGE);
                txt_voucher_discount.requestFocus();
            } else if (txt_voucher_desc.getText().equals("")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Voucher Description", "Warning", JOptionPane.WARNING_MESSAGE);
                txt_voucher_desc.requestFocus();
            } else {
                Entity_Voucher EV = new Entity_Voucher();
                EV.setVoucher_id(txt_voucher_id.getText());
                EV.setVoucher_date(dt_voucher_expired.getDate());
                EV.setVoucher_stock(Integer.valueOf(txt_voucher_stock.getText()));
                EV.setVoucher_discount(Integer.valueOf(txt_voucher_discount.getText()));
                EV.setVoucher_desc(txt_voucher_desc.getText());

                try {
                    int hasilAddVoucher = warehouseDao.saveVoucher(EV);
                    if (hasilAddVoucher == 1) {
                        JOptionPane.showMessageDialog(null, "Add Voucher Success", "Info", JOptionPane.INFORMATION_MESSAGE);
                        initTabelVoucher();
                        loadDaftarTabelVoucher();
                        ClearFormAddVoucher();
                    } else {
                        evt.consume();
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Add Voucher Failed", "Info", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (modeAddVoucher.equals("Update")) {
            if (txt_voucher_id.getText().equals("")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Voucher ID", "Warning", JOptionPane.WARNING_MESSAGE);
                txt_voucher_id.requestFocus();
            } else if (dt_voucher_expired.getDate().equals(null)) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Voucher Expired Date", "Warning", JOptionPane.WARNING_MESSAGE);
                dt_voucher_expired.requestFocus();
            } else if (txt_voucher_stock.getText().equals("")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Voucher Stock", "Warning", JOptionPane.WARNING_MESSAGE);
                txt_voucher_stock.requestFocus();
            } else if (txt_voucher_discount.getText().equals("")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Voucher Discount", "Warning", JOptionPane.WARNING_MESSAGE);
                txt_voucher_discount.requestFocus();
            } else if (txt_voucher_desc.getText().equals("")) {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Fill Voucher Description", "Warning", JOptionPane.WARNING_MESSAGE);
                txt_voucher_desc.requestFocus();
            } else {
                Entity_Voucher EV = new Entity_Voucher();
                EV.setVoucher_date(dt_voucher_expired.getDate());
                EV.setVoucher_stock(Integer.valueOf(txt_voucher_stock.getText()));
                EV.setVoucher_discount(Integer.valueOf(txt_voucher_discount.getText()));
                EV.setVoucher_desc(txt_voucher_desc.getText());
                EV.setVoucher_id(txt_voucher_id.getText());
                try {
                    int hasilUpdateVoucher = warehouseDao.updateVoucher(EV);
                    if (hasilUpdateVoucher == 1) {
                        JOptionPane.showMessageDialog(null, "Update Voucher Success", "Info", JOptionPane.INFORMATION_MESSAGE);
                        initTabelVoucher();
                        loadDaftarTabelVoucher();
                        ClearFormAddVoucher();
                        txt_voucher_id.setEnabled(true);
                    } else {
                        evt.consume();
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Update Voucher Failed", "Info", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_btnSave_voucherMouseClicked

    private void btnClear_voucherMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClear_voucherMouseClicked
        // TODO add your handling code here:
        ClearFormAddVoucher();
    }//GEN-LAST:event_btnClear_voucherMouseClicked

    private void btnDelete_voucherMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelete_voucherMouseClicked
        // TODO add your handling code here:
        if (dataTerpilih == null) {
            JOptionPane.showMessageDialog(null, "Choose Voucher From Table to Delete", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            Object[] options = {"Yes", "Cancel"};
            int reply = JOptionPane.showOptionDialog(null, "Are you sure to delete this Voucher ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            switch (reply) {
                case 0:
                    Entity_Voucher EV = new Entity_Voucher();
                    EV.setVoucher_id(dataTerpilih);
                    try {
                        int hasildeleteVoucher = warehouseDao.deleteVoucher(EV);
                        if (hasildeleteVoucher == 1) {
                            JOptionPane.showMessageDialog(null, "Delete Voucher Success", "Info", JOptionPane.INFORMATION_MESSAGE);
                            initTabelVoucher();
                            loadDaftarTabelVoucher();
                            ClearFormAddVoucher();
                        } else {
                            evt.consume();
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(null, "Delete Voucher Failed", "Info", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientWarehouse.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 1:
                    break;
                default:
                    break;
            }
        }
    }//GEN-LAST:event_btnDelete_voucherMouseClicked

    private void txt_voucher_stockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_voucher_stockMouseClicked
        // TODO add your handling code here:
        txt_voucher_stock.setText("");
    }//GEN-LAST:event_txt_voucher_stockMouseClicked

    private void txt_voucher_discountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_voucher_discountMouseClicked
        // TODO add your handling code here:
        txt_voucher_discount.setText("");
    }//GEN-LAST:event_txt_voucher_discountMouseClicked

    private void btnSaveError_wrhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveError_wrhMouseClicked
        // TODO add your handling code here:
        String error_added_by = null;

        Entity_Signup ESU = new Entity_Signup();
        ESU.setName(txtWrhNama.getText());
        try {
            error_added_by = errorDao.getIDAdminError(ESU);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }

        Entity_ErrorReport EER = new Entity_ErrorReport();
        EER.setError_id(txt_error_id_wrh.getText());
        EER.setError_title(txt_error_title_wrh.getText());
        EER.setError_desc(txt_error_desc_wrh.getText());
        EER.setError_date(dtError_Date_wrh.getDate());
        EER.setError_status((String) cb_error_status_wrh.getSelectedItem());
        EER.setError_added_by(error_added_by);
        try {
            int hasil = errorDao.saveError(EER);
            if (hasil == 1) {
                JOptionPane.showMessageDialog(null, "Save Error Success", "Save Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Save Error Failed", "Save Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        ClearFormError();
        init_Error_ID();
        loadDaftarError();
    }//GEN-LAST:event_btnSaveError_wrhMouseClicked

    private void btnClearError_wrhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClearError_wrhMouseClicked
        // TODO add your handling code here:
        ClearFormError();
    }//GEN-LAST:event_btnClearError_wrhMouseClicked

    private void tabelDaftarError_WrhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelDaftarError_WrhMouseClicked
        // TODO add your handling code here:
        baris = tabelDaftarError_Wrh.getSelectedRow();
        kolom = tabelDaftarError_Wrh.getSelectedColumn();
        dataTerpilih = tabelDaftarError_Wrh.getValueAt(baris, 0).toString();
    }//GEN-LAST:event_tabelDaftarError_WrhMouseClicked

    private void btn_delete_error_wrhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_delete_error_wrhMouseClicked
        // TODO add your handling code here:
        Object[] options = {"Yes", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Are you sure to delete this Error Report ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                Entity_ErrorReport EER = new Entity_ErrorReport();
                EER.setError_id(dataTerpilih);
                try {
                    int hasil = errorDao.deleteError(EER);
                    if (hasil == 1) {
                        JOptionPane.showMessageDialog(null, "Delete Report Success", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        evt.consume();
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Delete Report Failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                }
                loadDaftarError();
                init_Error_ID();
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btn_delete_error_wrhMouseClicked

    private void iconFOBigMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconFOBigMouseClicked
        // TODO add your handling code here:
        Mode14();
    }//GEN-LAST:event_iconFOBigMouseClicked

    private void iconBOBigMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconBOBigMouseClicked
        // TODO add your handling code here:
        Mode14();
    }//GEN-LAST:event_iconBOBigMouseClicked

    private void iconSIMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconSIMMouseClicked
        // TODO add your handling code here:
        HomePageClientSIM HPCSIM = new HomePageClientSIM();
        HPCSIM.setVisible(true);
        HPCSIM.Mode1();
    }//GEN-LAST:event_iconSIMMouseClicked

    private void iconSIMFOMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconSIMFOMouseClicked
        // TODO add your handling code here:
        HomePageClientSIM HPCSIM = new HomePageClientSIM();
        HPCSIM.setVisible(true);
        HPCSIM.Mode1();
    }//GEN-LAST:event_iconSIMFOMouseClicked

    private void iconHome1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconHome1MouseClicked
        // TODO add your handling code here:
        Mode13();
    }//GEN-LAST:event_iconHome1MouseClicked

    private void iconHRDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconHRDMouseClicked
        // TODO add your handling code here:
        HomePageClientHRD HPCHRD = new HomePageClientHRD();
        HPCHRD.setVisible(true);
        HPCHRD.Mode1();
        dispose();
    }//GEN-LAST:event_iconHRDMouseClicked

    private void iconKeuanganMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconKeuanganMouseClicked
        // TODO add your handling code here:
        HomePageClientKeuangan HPCKEU = new HomePageClientKeuangan();
        HPCKEU.setVisible(true);
        HPCKEU.Mode2();
        dispose();
    }//GEN-LAST:event_iconKeuanganMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HomePageClientWarehouse.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomePageClientWarehouse.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomePageClientWarehouse.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomePageClientWarehouse.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomePageClientWarehouse().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane ScrollPaneAddItem;
    private javax.swing.JScrollPane ScrollPaneWrhTransactionPendingProcessSP;
    private javax.swing.JLabel bgBackOffice;
    private javax.swing.JLabel bgClientCashierWrh;
    private javax.swing.JLabel bgClientWarehouse;
    private javax.swing.JLabel bgDepartment;
    private javax.swing.JLabel bgFrontOffice;
    private javax.swing.JLabel bgLandingPage;
    private javax.swing.JLabel bgLogoNOB;
    private javax.swing.JLabel bgWrh;
    private javax.swing.JLabel bgWrhLogo;
    private javax.swing.JButton btnChangeStateWrh;
    private javax.swing.JLabel btnClearError_wrh;
    private javax.swing.JLabel btnClear_Bank;
    private javax.swing.JLabel btnClear_Brand;
    private javax.swing.JLabel btnClear_Cat;
    private javax.swing.JLabel btnClear_voucher;
    private javax.swing.JLabel btnDelete_bank;
    private javax.swing.JLabel btnDelete_voucher;
    private javax.swing.JButton btnHideIPWrh;
    private javax.swing.JButton btnLoginWrh;
    private javax.swing.JLabel btnLogoutWrh;
    private javax.swing.JLabel btnMsgWrh;
    private javax.swing.JLabel btnNotifWrh;
    private javax.swing.JButton btnResetWrh;
    private javax.swing.JLabel btnSaveError_wrh;
    private javax.swing.JLabel btnSave_Bank;
    private javax.swing.JLabel btnSave_Brand;
    private javax.swing.JLabel btnSave_Cat;
    private javax.swing.JLabel btnSave_voucher;
    private javax.swing.JButton btnShowIPWrh;
    private javax.swing.JButton btn_add_item;
    private javax.swing.JButton btn_check_item;
    private javax.swing.JButton btn_clear_item;
    private javax.swing.JLabel btn_delete_brand;
    private javax.swing.JLabel btn_delete_category;
    private javax.swing.JLabel btn_delete_error_wrh;
    private javax.swing.JLabel btn_delete_item;
    private javax.swing.JButton btn_new_item;
    private javax.swing.JLabel btn_open_trans;
    private javax.swing.JButton btn_pending_failed;
    private javax.swing.JButton btn_pending_success;
    private javax.swing.JButton btn_process_item;
    private javax.swing.JButton btn_trans_clearall1;
    private javax.swing.JButton btn_trans_hide_nta;
    private javax.swing.JButton btn_trans_process1;
    private javax.swing.JButton btn_trans_show_nta;
    private javax.swing.ButtonGroup buttongroup_Gender;
    private javax.swing.JComboBox<String> cb_bank_status;
    private javax.swing.JComboBox<String> cb_error_status_wrh;
    private javax.swing.JComboBox<String> cb_item_brand;
    private javax.swing.JComboBox<String> cb_item_category;
    private javax.swing.JComboBox<String> cb_item_supplier;
    private com.toedter.calendar.JDateChooser dtError_Date_wrh;
    private com.toedter.calendar.JDateChooser dt_voucher_expired;
    private javax.swing.JLabel iconAdminWrh;
    private javax.swing.JLabel iconBO;
    private javax.swing.JLabel iconBOBig;
    private javax.swing.JLabel iconCashier;
    private javax.swing.JLabel iconFO;
    private javax.swing.JLabel iconFOBig;
    private javax.swing.JLabel iconHRD;
    private javax.swing.JLabel iconHome1;
    private javax.swing.JLabel iconKeuangan;
    private javax.swing.JLabel iconManagement;
    private javax.swing.JLabel iconSIM;
    private javax.swing.JLabel iconSIMFO;
    private javax.swing.JLabel iconWarehouse;
    private javax.swing.JLabel iconWrhAddBank;
    private javax.swing.JLabel iconWrhAddBrand;
    private javax.swing.JLabel iconWrhAddCategory;
    private javax.swing.JLabel iconWrhAddItem;
    private javax.swing.JLabel iconWrhAddVoucher;
    private javax.swing.JLabel iconWrhDashboard;
    private javax.swing.JLabel iconWrhDataTransactionPending;
    private javax.swing.JLabel iconWrhErrorReport;
    private javax.swing.JInternalFrame inframeQRCode;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator100;
    private javax.swing.JSeparator jSeparator101;
    private javax.swing.JSeparator jSeparator102;
    private javax.swing.JSeparator jSeparator104;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JSeparator jSeparator21;
    private javax.swing.JSeparator jSeparator27;
    private javax.swing.JSeparator jSeparator28;
    private javax.swing.JSeparator jSeparator29;
    private javax.swing.JSeparator jSeparator30;
    private javax.swing.JSeparator jSeparator41;
    private javax.swing.JSeparator jSeparator42;
    private javax.swing.JSeparator jSeparator43;
    private javax.swing.JSeparator jSeparator56;
    private javax.swing.JSeparator jSeparator95;
    private javax.swing.JSeparator jSeparator96;
    private javax.swing.JSeparator jSeparator97;
    private javax.swing.JSeparator jSeparator98;
    private javax.swing.JSeparator jSeparator99;
    private javax.swing.JLabel lblAddBankSubTitle;
    private javax.swing.JLabel lblAddBrandSubTitle;
    private javax.swing.JLabel lblAddCategorySubTitle;
    private javax.swing.JLabel lblAddVoucherSubTitle;
    private javax.swing.JLabel lblDashboardTitle2;
    private javax.swing.JLabel lblDashboardTitle3;
    private javax.swing.JLabel lblErrorReportTitleWrh;
    private javax.swing.JLabel lblErrorReportWrhSubTitle;
    private javax.swing.JLabel lblErrorReportWrhSubTitle1;
    private javax.swing.JLabel lblIDR8;
    private javax.swing.JLabel lblListTransactionPending;
    private javax.swing.JLabel lblRp1;
    private javax.swing.JLabel lblStateBigWrh;
    private javax.swing.JLabel lblStateWrh;
    private javax.swing.JLabel lblTransCreateDate;
    private javax.swing.JLabel lblTransCustCode;
    private javax.swing.JLabel lblTransCustEmail;
    private javax.swing.JLabel lblTransCustName;
    private javax.swing.JLabel lblTransCustPhone;
    private javax.swing.JLabel lblTransGrandTotal;
    private javax.swing.JLabel lblTransInvoiceNo;
    private javax.swing.JLabel lblTransInvoice_Confirm;
    private javax.swing.JLabel lblTransTotalDat;
    private javax.swing.JLabel lblTransTotalNTA;
    private javax.swing.JLabel lblTransactionPendingTitle;
    private javax.swing.JLabel lblTransactionPendingTitleSubTitle;
    private javax.swing.JLabel lblWarehouseAddItem;
    private javax.swing.JLabel lblWrh;
    private javax.swing.JLabel lblWrh2;
    private javax.swing.JLabel lblWrh3;
    private javax.swing.JLabel lblWrh4;
    private javax.swing.JLabel lblWrh5;
    private javax.swing.JLabel lblWrhAddBank;
    private javax.swing.JLabel lblWrhAddBrand;
    private javax.swing.JLabel lblWrhAddCategory;
    private javax.swing.JLabel lblWrhAddVoucher;
    private javax.swing.JLabel lblWrhTransPending;
    private javax.swing.JLabel lbl_bank_desc;
    private javax.swing.JLabel lbl_bank_desc1;
    private javax.swing.JLabel lbl_bank_id;
    private javax.swing.JLabel lbl_bank_name;
    private javax.swing.JLabel lbl_bank_status;
    private javax.swing.JLabel lbl_brand_id;
    private javax.swing.JLabel lbl_brand_name;
    private javax.swing.JLabel lbl_category_id;
    private javax.swing.JLabel lbl_category_name;
    private javax.swing.JLabel lbl_daftarkyw2;
    private javax.swing.JLabel lbl_error_date_wrh;
    private javax.swing.JLabel lbl_error_desc_wrh;
    private javax.swing.JLabel lbl_error_id_wrh;
    private javax.swing.JLabel lbl_error_status_wrh;
    private javax.swing.JLabel lbl_error_title_wrh;
    private javax.swing.JLabel lbl_item_brand;
    private javax.swing.JLabel lbl_item_category;
    private javax.swing.JLabel lbl_item_detail;
    private javax.swing.JLabel lbl_item_guarantee;
    private javax.swing.JLabel lbl_item_id;
    private javax.swing.JLabel lbl_item_info;
    private javax.swing.JLabel lbl_item_last_stock;
    private javax.swing.JLabel lbl_item_name;
    private javax.swing.JLabel lbl_item_new_stock;
    private javax.swing.JLabel lbl_item_price_NTA1;
    private javax.swing.JLabel lbl_item_price_detail;
    private javax.swing.JLabel lbl_item_price_publish1;
    private javax.swing.JLabel lbl_item_price_single1;
    private javax.swing.JLabel lbl_item_stock_new;
    private javax.swing.JLabel lbl_item_supplier;
    private javax.swing.JLabel lbl_item_update_stock;
    private javax.swing.JLabel lbl_list_bank;
    private javax.swing.JLabel lbl_list_brand;
    private javax.swing.JLabel lbl_list_category;
    private javax.swing.JLabel lbl_list_error_wrh;
    private javax.swing.JLabel lbl_list_voucher;
    private javax.swing.JLabel lbl_searchItemID;
    private javax.swing.JLabel lbl_searchItemName;
    private javax.swing.JLabel lbl_voucher_discount;
    private javax.swing.JLabel lbl_voucher_expired;
    private javax.swing.JLabel lbl_voucher_id;
    private javax.swing.JLabel lbl_voucher_stock;
    private javax.swing.JLabel lbl_warehouse;
    private javax.swing.JPanel menuWrhAddBank;
    private javax.swing.JPanel menuWrhAddBrand;
    private javax.swing.JPanel menuWrhAddCategory;
    private javax.swing.JPanel menuWrhAddItem;
    private javax.swing.JPanel menuWrhAddVoucher;
    private javax.swing.JPanel menuWrhDashboard;
    private javax.swing.JPanel menuWrhDataTransactionPending;
    private javax.swing.JPanel menuWrhErrorReport;
    private javax.swing.JPanel panelBackOffice;
    private javax.swing.JPanel panelDepartment;
    private javax.swing.JPanel panelFrontOffice;
    private javax.swing.JPanel panelItemDetail;
    private javax.swing.JPanel panelItemPriceDetail;
    private javax.swing.JPanel panelItemPriceDetail1;
    private javax.swing.JPanel panelItemSearchbyID;
    private javax.swing.JPanel panelItemSearchbyItem;
    private javax.swing.JPanel panelKilledbyServer;
    private javax.swing.JPanel panelLandingPage;
    private javax.swing.JLabel panelLoginWrh;
    private javax.swing.JPanel panelQRCode_trans;
    private javax.swing.JPanel panelWrhDashboard;
    private javax.swing.JPanel panelWrhLandingPage;
    private javax.swing.JPanel panelWrhLogin;
    private javax.swing.JPanel panel_Trans_CartList;
    private javax.swing.JPanel panel_Trans_Contact;
    private javax.swing.JPanel panel_Trans_Invoice;
    private javax.swing.JPanel panel_Trans_Navigasi;
    private javax.swing.JPanel panel_Trans_PriceInfo;
    private javax.swing.JPanel subpanelWrhAddBank;
    private javax.swing.JPanel subpanelWrhAddBrand;
    private javax.swing.JPanel subpanelWrhAddCategory;
    private javax.swing.JPanel subpanelWrhAddItem;
    private javax.swing.JPanel subpanelWrhAddVoucher;
    private javax.swing.JPanel subpanelWrhDashboard;
    private javax.swing.JPanel subpanelWrhErrorReport;
    private javax.swing.JPanel subpanelWrhTransactionPending;
    private javax.swing.JPanel subpanelWrhTransactionPendingProcess;
    private javax.swing.JTable tabelDaftarBank;
    private javax.swing.JScrollPane tabelDaftarBankSP;
    private javax.swing.JScrollPane tabelDaftarBankSP1;
    private javax.swing.JTable tabelDaftarBrand;
    private javax.swing.JScrollPane tabelDaftarBrandSP;
    private javax.swing.JTable tabelDaftarCategory;
    private javax.swing.JScrollPane tabelDaftarCategorySP;
    private javax.swing.JTable tabelDaftarError_Wrh;
    private javax.swing.JScrollPane tabelDaftarReportSP;
    private javax.swing.JTable tabelDaftarTransactionPending;
    private javax.swing.JScrollPane tabelDaftarTransactionPendingSP;
    private javax.swing.JTable tabelDaftarVoucher;
    private javax.swing.JTable tabelItemList;
    private javax.swing.JScrollPane tabelItemListSP;
    private javax.swing.JTable tabelShoppingCartPending;
    private javax.swing.JPasswordField txtPasswordWrh;
    private javax.swing.JLabel txtServerTimeWrh;
    private javax.swing.JTextField txtUsernameWrh;
    private javax.swing.JLabel txtWrhNama;
    private javax.swing.JTextPane txt_bank_desc;
    private javax.swing.JTextField txt_bank_id;
    private javax.swing.JTextField txt_bank_name;
    private javax.swing.JTextField txt_brand_id;
    private javax.swing.JTextField txt_brand_name;
    private javax.swing.JTextField txt_cat_id;
    private javax.swing.JTextField txt_cat_name;
    private javax.swing.JTextField txt_count_trans_pending;
    private javax.swing.JTextArea txt_error_desc_wrh;
    private javax.swing.JTextField txt_error_id_wrh;
    private javax.swing.JTextField txt_error_title_wrh;
    private javax.swing.JTextField txt_item_guarantee;
    private javax.swing.JTextField txt_item_id;
    private javax.swing.JTextField txt_item_id_update;
    private javax.swing.JTextField txt_item_info;
    private javax.swing.JTextField txt_item_name;
    private javax.swing.JTextField txt_item_price_nta;
    private javax.swing.JTextField txt_item_price_publish;
    private javax.swing.JTextField txt_item_price_single;
    private javax.swing.JTextField txt_item_stock;
    private javax.swing.JTextField txt_item_stock_last;
    private javax.swing.JTextField txt_item_stock_new;
    private javax.swing.JLabel txt_jumlahtugas_wrh;
    private javax.swing.JLabel txt_stateBigWrh;
    private javax.swing.JLabel txt_stateWrh;
    private javax.swing.JLabel txt_trans_TotalNTA;
    private javax.swing.JLabel txt_trans_cust_id;
    private javax.swing.JLabel txt_trans_custmail;
    private javax.swing.JLabel txt_trans_custname;
    private javax.swing.JLabel txt_trans_custphone;
    private javax.swing.JLabel txt_trans_date;
    private javax.swing.JLabel txt_trans_grand_total;
    private javax.swing.JLabel txt_trans_invoiceno;
    private javax.swing.JLabel txt_trans_invoiceno_confirm;
    private javax.swing.JLabel txt_trans_pending;
    private javax.swing.JTextField txt_trans_searchitemByID;
    private javax.swing.JTextField txt_trans_searchitemByName;
    private javax.swing.JLabel txt_trans_success;
    private javax.swing.JLabel txt_trans_total_data;
    private javax.swing.JTextPane txt_voucher_desc;
    private javax.swing.JTextField txt_voucher_discount;
    private javax.swing.JTextField txt_voucher_id;
    private javax.swing.JTextField txt_voucher_stock;
    // End of variables declaration//GEN-END:variables
}
