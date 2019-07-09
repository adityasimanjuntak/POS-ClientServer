/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nob.ui.bo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import static java.awt.image.ImageObserver.WIDTH;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import nob.config.configuration;
import nob.dao.api.IDepartmentDao;
import nob.dao.api.IDistributorDao;
import nob.dao.api.ILoginDao;
import nob.dao.api.IReportDao;
import nob.dao.api.ISimulationDao;
import nob.dao.api.ITaskDao;
import nob.dao.api.ITransactionDao;
import nob.dao.api.IUserDao;
import nob.model.Entity_Bank;
import nob.model.Entity_Department;
import nob.model.Entity_Distributor;
import nob.model.Entity_ErrorReport;
import nob.model.Entity_Item;
import nob.model.Entity_Piutang;
import nob.model.Entity_Signin;
import nob.model.Entity_Signup;
import nob.model.Entity_Simulation;
import nob.model.Entity_Task;
import nob.model.Entity_Transaction;

/**
 *
 * @author alimk
 */
public class HomePageClientCashier extends javax.swing.JFrame {

    private IUserDao userDao = null;
    private ILoginDao loginDao = null;
    private IDistributorDao distributorDao = null;
    private IDepartmentDao departmentDao = null;
    private ITaskDao taskDao = null;
    private IReportDao errorDao = null;
    private ITransactionDao transactionDao = null;
    private ISimulationDao simulationDao = null;

    public String kodeSistem = "CAS";
    String hasilgetStatusServer = null;
    String hasilgetStatusCas = null;
    final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

    int last_session = 0;
    String State_run_Cas = null;

    String ip = null;
    String modesu = null;
    String whoisonline = null;
    String dataTerpilih = null;
    String dataTerpilihTabelTugas = null;
    String stockstatus = null;

    int mode = 0;

    String namaDistributor = null;
    String emailDistributor = null;
    String phoneDistributor = null;

    String namaCustomer = null;
    String emailCustomer = null;
    String phoneCustomer = null;

    String kodeVoucher = "NOVOUCHER";
    int cektanggalexpiredVoucher = 0;
    int cekstokVoucher = 0;
    int discountVoucher = 0;
    int availableVoucher = 0;
    Date tanggalVoucher = null;

    int PriceNTA = 0;
    int PricePub = 0;
    int PriceSingle = 0;
    int JenisHarga = 0;
    int qty = 0;
    int NTA = 0;
    int subTotalPublish = 0;
    int subTotalNTA = 0;
    int pricePublish = 0;
    int pricepblh = 0;
    int pricePublishmin = 0;
    double discount;
    double grandTotal;
    int jumlahBarang = 0;
    int NTAminus = 0;
    int qtyMinus = 0;

    String NamaItem = null;
    int availabelItem = 0;

    String idBank = null;
    String namaBank = null;

    String idPiutang = null;
    String namaPiutang = null;

    String paymentVia = null;
    String paymentType = null;
    String Nominal = null;
    String Bank = null;
    String piutangBy = null;
    String customerType = null;
    String admin = null;
    String paymentDesc = null;

    int hasilsaveTrans, hasilsaveTransDetail, hasilsavePiutang, hasilupdateDisLastTrans;

    int hargaSimProcAMD = 0;
    int hargaSimProcIntel = 0;
    int hargaSimMotherboard = 0;
    int hargaSimSSD = 0;
    int hargaSimHDD = 0;
    int hargaSimRAM = 0;
    int hargaSimVGA = 0;
    int hargaSimCasing = 0;
    int hargaSimPSU = 0;
    int hargaSimLCD = 0;
    int hargaSimOptical = 0;
    int hargaSimKeyboard = 0;
    int hargaSimSpeaker = 0;
    int hargaSimHeadset = 0;
    int hargaSimCpuCooler = 0;
    int hargaSimCoolerFan = 0;
    int hargaSimNetworking = 0;
    int grandtotalSim = 0;
    String modeSimulation = null;
    int hasilsaveSim = 0;
    int hasilsaveSimDetail = 0;

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

    String dataTerpilihTabelTrans = null;

    //Inisiasi Tabel Karyawan pada Update Data
    private final Object[] DaftarKywcolumNames = {"ID Karyawan", "No Regis", "Department", "Nama", "POB", "Dob", "Gender", "Phone", "Mail"};
    private final DefaultTableModel tableModelKaryawan = new DefaultTableModel();
    private List<Entity_Signup> recordKaryawan = new ArrayList<Entity_Signup>();

    private final Object[] DaftarDepartmentcolumNames = {"ID Department", "Category", "Description", "Status"};
    private final DefaultTableModel tableModelDepartment = new DefaultTableModel();
    private List<Entity_Department> recordDepartment = new ArrayList<Entity_Department>();

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

    private final Object[] DaftarItemSimulationcolumNames = {"Item", "Price"};
    private final DefaultTableModel tableModelItemSimulation = new DefaultTableModel();

    private List<Entity_Transaction> recordProcessorAMD = new ArrayList<Entity_Transaction>();
    private List<Entity_Transaction> recordProcessorIntel = new ArrayList<Entity_Transaction>();
    private List<Entity_Transaction> recordMotherboard = new ArrayList<Entity_Transaction>();
    private List<Entity_Transaction> recordSSD = new ArrayList<Entity_Transaction>();
    private List<Entity_Transaction> recordHDD = new ArrayList<Entity_Transaction>();
    private List<Entity_Transaction> recordRAM = new ArrayList<Entity_Transaction>();
    private List<Entity_Transaction> recordVGA = new ArrayList<Entity_Transaction>();
    private List<Entity_Transaction> recordCasing = new ArrayList<Entity_Transaction>();
    private List<Entity_Transaction> recordPSU = new ArrayList<Entity_Transaction>();
    private List<Entity_Transaction> recordLCD = new ArrayList<Entity_Transaction>();
    private List<Entity_Transaction> recordOptical = new ArrayList<Entity_Transaction>();
    private List<Entity_Transaction> recordKeyboard = new ArrayList<Entity_Transaction>();
    private List<Entity_Transaction> recordSpeaker = new ArrayList<Entity_Transaction>();
    private List<Entity_Transaction> recordHeadset = new ArrayList<Entity_Transaction>();
    private List<Entity_Transaction> recordCPUCooler = new ArrayList<Entity_Transaction>();
    private List<Entity_Transaction> recordCoolerFan = new ArrayList<Entity_Transaction>();
    private List<Entity_Transaction> recordNetworking = new ArrayList<Entity_Transaction>();

    /**
     * Creates new form HomePageClient_Administrator
     */
    public HomePageClientCashier() {
        initComponents();
        Mode1();
        get_ip_address();
        ShowIPModeCas();
        set_TimeServer();

        init_User_Dao();
        init_LoginDao();
        init_DistributorDao();
        init_DepartmentDao();
        init_TaskDao();
        init_ErrorDao();
        init_TransactionDao();
        init_SimulationDao();

        init_Transaction_ID();

        HitungJumlahTugasCas();
        HitungJumlahTransactionPending();
        HitungJumlahTransaction();

        last_session = 15;
        State_run_Cas = "Running";

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Object[] options = {"Yes", "Cancel"};
                int reply = JOptionPane.showOptionDialog(null, "Are you sure to close the system ?", "Information", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                switch (reply) {
                    case 0:
                        try {
                            if (State_run_Cas.equals("Running")) {
                                SetCasStatusDisconnect();
                                e.getWindow().dispose();
                                System.exit(0);
                            } else if (hasilgetStatusServer.equals("Server Offline")) {
                                SetCasStatusDisconnect();
                                e.getWindow().dispose();
                            } else {
                                int hasilSetCasStatus = loginDao.UpdateStatusOffline("CAS");
                                if (hasilSetCasStatus == 1) {
                                    e.getWindow().dispose();
                                    System.exit(0);
                                }
                            }
                        } catch (RemoteException ex) {
                            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    default:
                        break;
                }
            }
        }
        );
    }

    public void SetCasStatusOnline() {
        try {
            int hasilSetServerStatus = loginDao.UpdateStatusOnline(kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SetCasStatusOffline() {
        try {
            int hasilSetServerStatus = loginDao.UpdateStatusOffline(kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SetCasStatusDisconnect() {
        try {
            int hasilSetCasStatus = loginDao.UpdateStatusDisconnect(kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getStatusCashier() {
        try {
            hasilgetStatusCas = loginDao.CekClientStatus(kodeSistem);
            txt_stateCas.setText(hasilgetStatusCas);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SetCasStatusKilledbyServer() {
        try {
            int hasilSetCasStatus = loginDao.UpdateStatusKilledbyServer(kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getStatusServer() {
        try {
            hasilgetStatusServer = loginDao.CekClientStatus("SERVER");
            if (hasilgetStatusCas.equals("Killed by Server")) {
                Mode24();
                SetCasStatusKilledbyServer();
            } else if (hasilgetStatusServer.equals("Server Offline")) {
                Mode24();
                SetCasStatusDisconnect();
            } else if (hasilgetStatusCas.equals("Online")) {
                if (last_session == 15) {
                    Mode15();
                } else if (last_session == 16) {
                    Mode16();
                } else if (last_session == 17) {
                    Mode17();
                } else if (last_session == 18) {
                    Mode18();
                } else if (last_session == 19) {
                    Mode19();
                } else if (last_session == 20) {
                    Mode20();
                } else if (last_session == 21) {
                    Mode21();
                } else if (last_session == 22) {
                    Mode16();
                    ShowPanelPayment();
                }
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setIPCas() {
        Entity_Signin ES = new Entity_Signin();
        ES.setIp(ip);
        try {
            int hasilsetIPServer = loginDao.UpdateIP(ES, kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void CekStatus5Sec() {
        ses.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                // TODO add your handling code here:
                getStatusCashier();
                getStatusServer();

            }
        }, 0, 10, TimeUnit.SECONDS
        );
    }

    public void Mode1() {
        //sebagai mode awal
        mode = 1;
        panelLandingPage.setVisible(true);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelCasLandingPage.setVisible(false);
        panelCasLogin.setVisible(false);
        panelCasDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode2() {
        mode = 2;
        //Mode 2 = Menampilkan Panel Daftar Department
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(true);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelCasLandingPage.setVisible(false);
        panelCasLogin.setVisible(false);
        panelCasDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode3() {
        mode = 3;
        //Mode 3 = Menampilkan Panel Daftar Department Back Office
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(true);
        panelFrontOffice.setVisible(false);
        panelCasLandingPage.setVisible(false);
        panelCasLogin.setVisible(false);
        panelCasDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode4() {
        mode = 4;
        //Mode 4 = Menampilkan Panel Daftar Department Front Office
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(true);
        panelCasLandingPage.setVisible(false);
        panelCasLogin.setVisible(false);
        panelCasDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode13() {
        mode = 13;
        //Mode 13 = Menampilkan Panel Daftar Department Front Office - Cashier - LandingPage
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelCasLandingPage.setVisible(true);
        panelCasLogin.setVisible(false);
        panelCasDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode14() {
        mode = 14;
        //Mode 14 = Menampilkan Panel Daftar Department Front Office - Cashier - LoginPage
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelCasLandingPage.setVisible(false);
        panelCasLogin.setVisible(true);
        panelCasDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode15() {
        mode = 15;
        //Mode 15 = Menampilkan Panel Daftar Department Front Office - Cashier - Dashboard
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelCasLandingPage.setVisible(false);
        panelCasLogin.setVisible(false);
        panelCasDashboard.setVisible(true);
        ScrollPaneAddTrans.setVisible(false);
        subpanelCasDashboard.setVisible(true);
        subpanelCasAddTransaction.setVisible(false);
        subpanelCasAddDistributor.setVisible(false);
        subpanelCasListItem.setVisible(false);
        subpanelCasDataTransaction.setVisible(false);
        subpanelCasErrorReport.setVisible(false);

        ScrollPaneSimulasi.setVisible(false);
        subpanelCasSimulasi.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode16() {
        mode = 16;
        //Mode 16 = Menampilkan Panel Daftar Department Front Office - Cashier - Add Transaction
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelCasLandingPage.setVisible(false);
        panelCasLogin.setVisible(false);
        panelCasDashboard.setVisible(true);
        subpanelCasDashboard.setVisible(false);
        ScrollPaneAddTrans.setVisible(true);
        subpanelCasAddTransaction.setVisible(true);
        subpanelCasAddDistributor.setVisible(false);
        subpanelCasListItem.setVisible(false);
        subpanelCasDataTransaction.setVisible(false);
        subpanelCasErrorReport.setVisible(false);
        ScrollPaneSimulasi.setVisible(false);
        subpanelCasSimulasi.setVisible(false);
        panelKilledbyServer.setVisible(false);
        init_Transaction_ID();
        HidePanelPayment();
    }

    public void Mode17() {
        mode = 17;
        //Mode 17 = Menampilkan Panel Daftar Department Front Office - Cashier - Add Customer
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelCasLandingPage.setVisible(false);
        panelCasLogin.setVisible(false);
        panelCasDashboard.setVisible(true);
        subpanelCasDashboard.setVisible(false);
        ScrollPaneAddTrans.setVisible(false);
        subpanelCasAddTransaction.setVisible(false);
        subpanelCasAddDistributor.setVisible(true);
        subpanelCasListItem.setVisible(false);
        subpanelCasDataTransaction.setVisible(false);
        subpanelCasErrorReport.setVisible(false);
        ScrollPaneSimulasi.setVisible(false);
        subpanelCasSimulasi.setVisible(false);
        panelKilledbyServer.setVisible(false);
        init_Distributor_ID();
    }

    public void Mode18() {
        mode = 18;
        //Mode 18 = Menampilkan Panel Daftar Department Front Office - Cashier - List Item
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelCasLandingPage.setVisible(false);
        panelCasLogin.setVisible(false);
        panelCasDashboard.setVisible(true);
        subpanelCasDashboard.setVisible(false);
        ScrollPaneAddTrans.setVisible(false);
        subpanelCasAddTransaction.setVisible(false);
        subpanelCasAddDistributor.setVisible(false);
        subpanelCasListItem.setVisible(true);
        subpanelCasDataTransaction.setVisible(false);
        subpanelCasErrorReport.setVisible(false);
        ScrollPaneSimulasi.setVisible(false);
        subpanelCasSimulasi.setVisible(false);
        panelKilledbyServer.setVisible(false);
        init_Distributor_ID();
    }

    public void Mode19() {
        mode = 19;
        //Mode 19 = Menampilkan Panel Daftar Department Front Office - Cashier - Data Transaction
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelCasLandingPage.setVisible(false);
        panelCasLogin.setVisible(false);
        panelCasDashboard.setVisible(true);
        subpanelCasDashboard.setVisible(false);
        ScrollPaneAddTrans.setVisible(false);
        subpanelCasAddTransaction.setVisible(false);
        subpanelCasAddDistributor.setVisible(false);
        subpanelCasListItem.setVisible(false);
        subpanelCasDataTransaction.setVisible(true);
        subpanelCasErrorReport.setVisible(false);
        ScrollPaneSimulasi.setVisible(false);
        subpanelCasSimulasi.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode20() {
        mode = 20;
        //Mode 20 = Menampilkan Panel Daftar Department Front Office - Cashier - Error Report
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelCasLandingPage.setVisible(false);
        panelCasLogin.setVisible(false);
        panelCasDashboard.setVisible(true);
        subpanelCasDashboard.setVisible(false);
        ScrollPaneAddTrans.setVisible(false);
        subpanelCasAddTransaction.setVisible(false);
        subpanelCasAddDistributor.setVisible(false);
        subpanelCasListItem.setVisible(false);
        subpanelCasDataTransaction.setVisible(false);
        subpanelCasErrorReport.setVisible(true);
        ScrollPaneSimulasi.setVisible(false);
        subpanelCasSimulasi.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode21() {
        mode = 21;
        //Mode 21 = Menampilkan Panel Daftar Department Front Office - Cashier - Simulasi
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelCasLandingPage.setVisible(false);
        panelCasLogin.setVisible(false);
        panelCasDashboard.setVisible(true);
        subpanelCasDashboard.setVisible(false);
        ScrollPaneAddTrans.setVisible(false);
        subpanelCasAddTransaction.setVisible(false);
        subpanelCasAddDistributor.setVisible(false);
        subpanelCasListItem.setVisible(false);
        subpanelCasDataTransaction.setVisible(false);
        subpanelCasErrorReport.setVisible(false);
        ScrollPaneSimulasi.setVisible(true);
        subpanelCasSimulasi.setVisible(true);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode24() {
        mode = 22;
        //Mode 2 = Menampilkan Panel Killed by Server
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelCasLandingPage.setVisible(false);
        panelCasLogin.setVisible(false);
        panelCasDashboard.setVisible(false);
        subpanelCasDashboard.setVisible(false);
        ScrollPaneAddTrans.setVisible(false);
        subpanelCasAddTransaction.setVisible(false);
        subpanelCasAddDistributor.setVisible(false);
        subpanelCasListItem.setVisible(false);
        subpanelCasDataTransaction.setVisible(false);
        subpanelCasErrorReport.setVisible(false);
        ScrollPaneSimulasi.setVisible(false);
        subpanelCasSimulasi.setVisible(false);
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
                    txtServerTimeCas.setText("Date  :  " + day + "/" + (month + 1) + "/" + year + "   Time : " + hour + ":" + (minute) + ":" + second);

                    try {
                        sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
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

    public void HideIPModeCas() {
        btnShowIPCas.setVisible(false);
        btnHideIPCas.setVisible(true);
    }

    public void ShowIPModeCas() {
        btnShowIPCas.setVisible(true);
        btnHideIPCas.setVisible(false);
    }

    public void GetQrCode_Trans() {
        String t = txt_trans_invoiceno.getText();
        out = QRCode.from(t).withSize(141, 168).to(ImageType.PNG).stream();
        try {
            fout = new FileOutputStream(new File("temp.png"));
            fout.write(out.toByteArray());
            fout.flush();
            fout.close();

            BufferedImage miQr = ImageIO.read(new File("temp.png"));
            JLabel label = new JLabel(new ImageIcon(miQr));

            Graphics g = lbl_qrcode_trans.getGraphics();
            g.drawImage(miQr, WIDTH, WIDTH, label);
            qr_image = out.toByteArray();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void init_User_Dao() {
        String url = "rmi://" + configuration.ip_server + ":1099/userDao";
        try {
            //ambil referensi dari remote objek yang ada di server
            userDao = (IUserDao) Naming.lookup(url);
        } catch (NotBoundException | MalformedURLException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            //Logger.getLogger(FrmSupplier.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Koneksi ke Service" + url + "[GAGAL], cek kembali app server", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void init_Error_ID() {
        Entity_ErrorReport EER = new Entity_ErrorReport();
        try {
            String hasil = errorDao.GenerateErrorID(EER);
            if (hasil == null) {
                //do something
            } else {
                txt_error_id_cas.setText(hasil);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init_Transaction_ID() {
        Entity_Transaction ETR = new Entity_Transaction();
        try {
            String hasil = transactionDao.GenerateTransID(ETR);
            if (hasil == null) {
                //do something
            } else {
                txt_trans_invoiceno.setHorizontalAlignment(SwingConstants.RIGHT);
                txt_trans_invoiceno.setText(hasil);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init_Distributor_ID() {
        Entity_Distributor ED = new Entity_Distributor();
        try {
            String hasil = distributorDao.GenerateDistributorID(ED);
            if (hasil == null) {
                //do something
            } else {
                txt_distributor_id.setText(hasil);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init_Simulation_ID() {
        Entity_Simulation ES = new Entity_Simulation();
        try {
            String hasil = simulationDao.GenerateSimID(ES);
            if (hasil == null) {
                //do something
            } else {
                txt_trans_simulation_id.setText(hasil);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadDaftarKaryawan() {
        try {
            // reset data di tabel
            tableModelKaryawan.setRowCount(0);
            // mengambil data barang dari server
            // kemudian menyimpannya ke objek list
            recordKaryawan = userDao.getAll();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Signup ESU : recordKaryawan) {
                // ambil nomor urut terakhir
                Object[] objects = new Object[DaftarKywcolumNames.length];
                objects[0] = ESU.getId();
                objects[1] = ESU.getNoregister();
                objects[2] = ESU.getDepartment();
                objects[3] = ESU.getName();
                objects[4] = ESU.getPob();
                objects[5] = ESU.getDob();
                objects[6] = ESU.getGender();
                objects[7] = ESU.getPhone();
                objects[8] = ESU.getEmail();
                // tambahkan data barang ke dalam tabel
                tableModelKaryawan.addRow(objects);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initTableDaftarError() {
        //set header table
        tableModelError.setColumnIdentifiers(DaftarErrorcolumNames);
        tabelDaftarError_Cas.setModel(tableModelError);
        tabelDaftarError_Cas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void loadDaftarError() {
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
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadComboBoxBank() {
        try {
            // mengambil data department dari server
            // kemudian menyimpannya ke objek list
            recordCbBank = transactionDao.getCbBank();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Bank EB : recordCbBank) {
                // ambil nomor urut terakhir
                cb_trans_bank.addItem(EB.getBank_name());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadComboBoxPiutang() {
        try {
            // mengambil data department dari server
            // kemudian menyimpannya ke objek list
            recordCbPiutang = transactionDao.getCbPiutang();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Piutang EP : recordCbPiutang) {
                // ambil nomor urut terakhir
                cb_trans_piutang.addItem(EP.getPiutang_by());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initTableDaftarDistributor() {
        //set header table
        tableModelDistributor.setColumnIdentifiers(DaftarDistributorcolumNames);
        tabelDataDistributor.setModel(tableModelDistributor);
        tabelDataDistributor.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void loadDaftarDistributor() {
        try {
            // reset data di tabel
            tableModelDistributor.setRowCount(0);
            // mengambil data barang dari server
            // kemudian menyimpannya ke objek list
            recordDistributor = distributorDao.getAll();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Distributor ED : recordDistributor) {
                // ambil nomor urut terakhir
                Object[] objects = new Object[DaftarDistributorcolumNames.length];
                objects[0] = ED.getDis_id();
                objects[1] = ED.getDis_name();
                objects[2] = ED.getDis_phone();
                objects[3] = ED.getDis_email();
                objects[4] = ED.getDis_status();
                // tambahkan data barang ke dalam tabel
                tableModelDistributor.addRow(objects);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void customTabelTransList() {
        tabelTransList.getColumnModel().getColumn(0).setPreferredWidth(100);
        tabelTransList.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabelTransList.getColumnModel().getColumn(2).setPreferredWidth(80);
        tabelTransList.getColumnModel().getColumn(3).setPreferredWidth(110);
        tabelTransList.getColumnModel().getColumn(4).setPreferredWidth(80);
        tabelTransList.getColumnModel().getColumn(5).setPreferredWidth(300);
        tabelTransList.getColumnModel().getColumn(6).setPreferredWidth(50);
        tabelTransList.getColumnModel().getColumn(7).setPreferredWidth(80);
        tabelTransList.getColumnModel().getColumn(8).setPreferredWidth(80);
        tabelTransList.getColumnModel().getColumn(9).setPreferredWidth(80);
        tabelTransList.getColumnModel().getColumn(10).setPreferredWidth(80);
    }

    private void customTabelListItem() {
        tabelShoppingCart.getColumnModel().getColumn(0).setPreferredWidth(120);
        tabelShoppingCart.getColumnModel().getColumn(1).setPreferredWidth(400);
    }

    private void initTabelTransList() {
        //set header table
        tableModelTransaction.setColumnIdentifiers(DaftarTransactioncolumNames);
        tabelTransList.setModel(tableModelTransaction);
        tabelTransList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelTransList.setAutoResizeMode(tabelTransList.AUTO_RESIZE_OFF);
        customTabelTransList();
    }

    private void loadDaftarTransactionbyIDTrans() {
        tabelTransList.setAutoResizeMode(tabelTransList.AUTO_RESIZE_OFF);
        customTabelTransList();
        try {
            // reset data di tabel
            // mengambil data barang dari server
            tableModelTransaction.setRowCount(0);
            // kemudian menyimpannya ke objek list
            recordTransaction = transactionDao.getListTransbyID(txt_trans_searchByID.getText());
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Transaction ETR : recordTransaction) {
                // ambil nomor urut terakhir
                Object[] objects = new Object[DaftarTransactioncolumNames.length];
                objects[0] = ETR.getTrans_id();
                objects[1] = ETR.getItem_id();
                objects[2] = ETR.getTrans_date();
                objects[3] = ETR.getTrans_processBy();
                objects[4] = ETR.getDis_id();
                objects[5] = ETR.getItem_name();
                objects[6] = ETR.getQuantity();
                objects[7] = ETR.getItem_price();
                objects[8] = ETR.getItem_price_subtotal();
                objects[9] = ETR.getTrans_totalPayment();
                objects[10] = ETR.getTrans_status();
                // tambahkan data barang ke dalam tabel
                tableModelTransaction.addRow(objects);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadDaftarTransactionbyIDCust() {
        tabelTransList.setAutoResizeMode(tabelTransList.AUTO_RESIZE_OFF);
        customTabelTransList();
        try {
            // reset data di tabel
            // mengambil data barang dari server
            tableModelTransaction.setRowCount(0);
            // kemudian menyimpannya ke objek list
            recordTransaction = transactionDao.getListTransbyIDCust(txt_trans_searchByDis.getText());
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Transaction ETR : recordTransaction) {
                // ambil nomor urut terakhir
                Object[] objects = new Object[DaftarTransactioncolumNames.length];
                objects[0] = ETR.getTrans_id();
                objects[1] = ETR.getItem_id();
                objects[2] = ETR.getTrans_date();
                objects[3] = ETR.getTrans_processBy();
                objects[4] = ETR.getDis_id();
                objects[5] = ETR.getItem_name();
                objects[6] = ETR.getQuantity();
                objects[7] = ETR.getItem_price();
                objects[8] = ETR.getItem_price_subtotal();
                objects[9] = ETR.getTrans_totalPayment();
                objects[10] = ETR.getTrans_status();
                // tambahkan data barang ke dalam tabel
                tableModelTransaction.addRow(objects);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void loadDaftarTransactionbyTransDate() {
        tabelTransList.setAutoResizeMode(tabelTransList.AUTO_RESIZE_OFF);
        customTabelTransList();
        try {
            // reset data di tabel
            // mengambil data barang dari server
            tableModelTransaction.setRowCount(0);
            // kemudian menyimpannya ke objek list
            recordTransaction = transactionDao.getListTransbyDate(txt_trans_searchByDate.getText());
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Transaction ETR : recordTransaction) {
                // ambil nomor urut terakhir
                Object[] objects = new Object[DaftarTransactioncolumNames.length];
                objects[0] = ETR.getTrans_id();
                objects[1] = ETR.getItem_id();
                objects[2] = ETR.getTrans_date();
                objects[3] = ETR.getTrans_processBy();
                objects[4] = ETR.getDis_id();
                objects[5] = ETR.getItem_name();
                objects[6] = ETR.getQuantity();
                objects[7] = ETR.getItem_price();
                objects[8] = ETR.getItem_price_subtotal();
                objects[9] = ETR.getTrans_totalPayment();
                objects[10] = ETR.getTrans_status();
                // tambahkan data barang ke dalam tabel
                tableModelTransaction.addRow(objects);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadDaftarTransactionbyAdmin() {
        tabelTransList.setAutoResizeMode(tabelTransList.AUTO_RESIZE_OFF);
        customTabelTransList();
        try {
            // reset data di tabel
            // mengambil data barang dari server
            tableModelTransaction.setRowCount(0);
            // kemudian menyimpannya ke objek list
            recordTransaction = transactionDao.getListTransbyAdmin(txt_trans_searchByAdmin.getText());
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Transaction ETR : recordTransaction) {
                // ambil nomor urut terakhir
                Object[] objects = new Object[DaftarTransactioncolumNames.length];
                objects[0] = ETR.getTrans_id();
                objects[1] = ETR.getItem_id();
                objects[2] = ETR.getTrans_date();
                objects[3] = ETR.getTrans_processBy();
                objects[4] = ETR.getDis_id();
                objects[5] = ETR.getItem_name();
                objects[6] = ETR.getQuantity();
                objects[7] = ETR.getItem_price();
                objects[8] = ETR.getItem_price_subtotal();
                objects[9] = ETR.getTrans_totalPayment();
                objects[10] = ETR.getTrans_status();
                // tambahkan data barang ke dalam tabel
                tableModelTransaction.addRow(objects);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadDaftarItembyCategory() {
        tabelItemList.setAutoResizeMode(tabelItemList.AUTO_RESIZE_OFF);
        DecimalFormat df = new DecimalFormat("IDR ,###.00");
        customTabelDaftarItem();
        try {
            // reset data di tabel
            // mengambil data barang dari server
            tableModelItem.setRowCount(0);
            // kemudian menyimpannya ke objek list
            recordItem = transactionDao.getListItembyCategory(txt_trans_searchitemByCategory.getText());
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
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadDaftarItembyBrand() {
        tabelItemList.setAutoResizeMode(tabelItemList.AUTO_RESIZE_OFF);
        DecimalFormat df = new DecimalFormat("IDR ,###.00");
        customTabelDaftarItem();
        try {
            // reset data di tabel
            // mengambil data barang dari server
            tableModelItem.setRowCount(0);
            // kemudian menyimpannya ke objek list
            recordItem = transactionDao.getListItembyBrand(txt_trans_searchitemByBrand.getText());
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
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadComboBoxProcessorAMD() {
        try {
            // mengambil data department dari server
            // kemudian menyimpannya ke objek list
            recordProcessorAMD = transactionDao.getCbProcessorAMD();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Transaction ETR : recordProcessorAMD) {
                // ambil nomor urut terakhir
                cb_trans_sim_processor_amd.addItem(ETR.getItem_name());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadComboBoxProcessorIntel() {
        try {
            // mengambil data department dari server
            // kemudian menyimpannya ke objek list
            recordProcessorIntel = transactionDao.getCbProcessorIntel();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Transaction ETR : recordProcessorIntel) {
                // ambil nomor urut terakhir
                cb_trans_sim_processor_intel.addItem(ETR.getItem_name());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadComboBoxMotherboard() {
        try {
            // mengambil data department dari server
            // kemudian menyimpannya ke objek list
            recordMotherboard = transactionDao.getCbMotherboard();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Transaction ETR : recordMotherboard) {
                // ambil nomor urut terakhir
                cb_trans_sim_motherboard.addItem(ETR.getItem_name());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadComboBoxSSD() {
        try {
            // mengambil data department dari server
            // kemudian menyimpannya ke objek list
            recordSSD = transactionDao.getCbSSD();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Transaction ETR : recordSSD) {
                // ambil nomor urut terakhir
                cb_trans_sim_ssd.addItem(ETR.getItem_name());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadComboBoxHDD() {
        try {
            // mengambil data department dari server
            // kemudian menyimpannya ke objek list
            recordHDD = transactionDao.getCbHDD();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Transaction ETR : recordHDD) {
                // ambil nomor urut terakhir
                cb_trans_sim_hdd.addItem(ETR.getItem_name());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadComboBoxRAM() {
        try {
            // mengambil data department dari server
            // kemudian menyimpannya ke objek list
            recordRAM = transactionDao.getCbRAM();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Transaction ETR : recordRAM) {
                // ambil nomor urut terakhir
                cb_trans_sim_ram.addItem(ETR.getItem_name());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadComboBoxVGA() {
        try {
            // mengambil data department dari server
            // kemudian menyimpannya ke objek list
            recordVGA = transactionDao.getCbVGA();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Transaction ETR : recordVGA) {
                // ambil nomor urut terakhir
                cb_trans_sim_vga.addItem(ETR.getItem_name());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadComboBoxCasing() {
        try {
            // mengambil data department dari server
            // kemudian menyimpannya ke objek list
            recordCasing = transactionDao.getCbCasing();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Transaction ETR : recordCasing) {
                // ambil nomor urut terakhir
                cb_trans_sim_casing.addItem(ETR.getItem_name());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadComboBoxPSU() {
        try {
            // mengambil data department dari server
            // kemudian menyimpannya ke objek list
            recordPSU = transactionDao.getCbPSU();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Transaction ETR : recordPSU) {
                // ambil nomor urut terakhir
                cb_trans_sim_psu.addItem(ETR.getItem_name());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadComboBoxLCD() {
        try {
            // mengambil data department dari server
            // kemudian menyimpannya ke objek list
            recordLCD = transactionDao.getCbLCD();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Transaction ETR : recordLCD) {
                // ambil nomor urut terakhir
                cb_trans_sim_lcd.addItem(ETR.getItem_name());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadComboBoxOptical() {
        try {
            // mengambil data department dari server
            // kemudian menyimpannya ke objek list
            recordOptical = transactionDao.getCbOptical();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Transaction ETR : recordOptical) {
                // ambil nomor urut terakhir
                cb_trans_sim_optical.addItem(ETR.getItem_name());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadComboBoxKeyboard() {
        try {
            // mengambil data department dari server
            // kemudian menyimpannya ke objek list
            recordKeyboard = transactionDao.getCbKeyboard();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Transaction ETR : recordKeyboard) {
                // ambil nomor urut terakhir
                cb_trans_sim_keyboard.addItem(ETR.getItem_name());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadComboBoxSpeaker() {
        try {
            // mengambil data department dari server
            // kemudian menyimpannya ke objek list
            recordSpeaker = transactionDao.getCbSpeaker();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Transaction ETR : recordSpeaker) {
                // ambil nomor urut terakhir
                cb_trans_sim_speaker.addItem(ETR.getItem_name());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadComboBoxHeadset() {
        try {
            // mengambil data department dari server
            // kemudian menyimpannya ke objek list
            recordHeadset = transactionDao.getCbHeadset();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Transaction ETR : recordHeadset) {
                // ambil nomor urut terakhir
                cb_trans_sim_headset.addItem(ETR.getItem_name());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadComboBoxCPUCooler() {
        try {
            // mengambil data department dari server
            // kemudian menyimpannya ke objek list
            recordCPUCooler = transactionDao.getCbCPUCooler();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Transaction ETR : recordCPUCooler) {
                // ambil nomor urut terakhir
                cb_trans_sim_cpucooler.addItem(ETR.getItem_name());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadComboBoxCoolerFan() {
        try {
            // mengambil data department dari server
            // kemudian menyimpannya ke objek list
            recordCoolerFan = transactionDao.getCbCoolerFan();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Transaction ETR : recordCoolerFan) {
                // ambil nomor urut terakhir
                cb_trans_sim_coolerfan.addItem(ETR.getItem_name());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadComboBoxNetworking() {
        try {
            // mengambil data department dari server
            // kemudian menyimpannya ke objek list
            recordNetworking = transactionDao.getCbNetworking();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Transaction ETR : recordNetworking) {
                // ambil nomor urut terakhir
                cb_trans_sim_networking.addItem(ETR.getItem_name());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void customTabelDaftarItemSimulation() {
        tabel_item_simulation.getColumnModel().getColumn(0).setPreferredWidth(800);
        tabel_item_simulation.getColumnModel().getColumn(1).setPreferredWidth(220);
    }

    private void initTabelTransItemSimulation() {
        //set header table
        tableModelItemSimulation.setColumnIdentifiers(DaftarItemSimulationcolumNames);
        tabel_item_simulation.setModel(tableModelItemSimulation);
        tabel_item_simulation.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabel_item_simulation.setAutoResizeMode(tabel_item_simulation.AUTO_RESIZE_OFF);
        customTabelDaftarItemSimulation();
    }

    public void ClearFormError() {
        txt_error_title_cas.setText("");
        txt_error_desc_cas.setText("");
        dtError_Date_cas.setDate(null);
        txt_error_title_cas.requestFocus();
        cb_error_status_cas.setSelectedIndex(0);
    }

    public void ClearFormItem() {
        txt_trans_itemcode.setText("");
        txt_trans_itemname.setText("");
        txt_trans_NTAFare.setText("Not Show");
        txt_trans_PubFare.setText("");
        txt_trans_SingleFare.setText("");
        txt_trans_qty.setText("");
    }

    public void ClearFormTrans() {
        txt_trans_itemcode.setText("Please Enter to get Item Detail ...");
        txt_trans_itemname.setText("");
        txt_trans_NTAFare.setText("Not Show");
        txt_trans_PubFare.setText("");
        txt_trans_SingleFare.setText("");
        txt_trans_qty.setText("");
        cb_spt.setSelectedIndex(0);
        txt_trans_TotalNTA.setText("Not Show");
        dtTrans_date.setDate(null);
        txt_trans_Vouchercode.setText("#VOucherCode");
        txt_trans_voucherdiscount.setText("0");
        cb_CustType.setSelectedIndex(0);
        txt_trans_customerid.setText("Press Enter to Get Customer Data ...");
        txt_trans_custname.setText("#YourName");
        txt_trans_custmail.setText("@youremail");
        txt_trans_custphone.setText("#PhoneNumber");
        txt_trans_vouchercode.setText("");
        cb_trans_paymentvia.setSelectedIndex(0);
        cb_trans_paymenttype.setSelectedIndex(0);
        txt_trans_nominal.setText("Contoh : 1190000");
        cb_trans_bank.setSelectedIndex(0);
        cb_trans_piutang.setSelectedIndex(0);
        txt_trans_description.setText("");
        txt_trans_total_harga_publish.setText("0");
        txt_price_disc.setText("0");
        txt_trans_voucher.setText("0");
        txt_trans_grand_total.setText("0");
    }

    public void ClearFormAddDistributor() {
        txt_distributor_id.setText("");
        txt_distributor_address.setText("");
        txt_distributor_phone.setText("");
        txt_distributor_email.setText("");
        cb_distributor_description.setSelectedIndex(0);
        cb_distributor_status.setSelectedIndex(0);
    }

    public void ClearFormSimulation() {
        cb_trans_sim_brandprocessor.setSelectedIndex(0);
        cb_trans_sim_processor_amd.setSelectedIndex(0);
        cb_trans_sim_motherboard.setSelectedIndex(0);
        cb_trans_sim_ssd.setSelectedIndex(0);
        cb_trans_sim_hdd.setSelectedIndex(0);
        cb_trans_sim_ram.setSelectedIndex(0);
        cb_trans_sim_vga.setSelectedIndex(0);
        cb_trans_sim_casing.setSelectedIndex(0);
        cb_trans_sim_psu.setSelectedIndex(0);
        cb_trans_sim_lcd.setSelectedIndex(0);
        cb_trans_sim_optical.setSelectedIndex(0);
        cb_trans_sim_keyboard.setSelectedIndex(0);
        cb_trans_sim_speaker.setSelectedIndex(0);
        cb_trans_sim_headset.setSelectedIndex(0);
        cb_trans_sim_cpucooler.setSelectedIndex(0);
        cb_trans_sim_coolerfan.setSelectedIndex(0);
        cb_trans_sim_networking.setSelectedIndex(0);

        txt_trans_sim_price_processor.setText("");
        txt_trans_sim_price_motherboard.setText("");
        txt_trans_sim_price_ssd.setText("");
        txt_trans_sim_price_hdd.setText("");
        txt_trans_sim_price_ram.setText("");
        txt_trans_sim_price_vga.setText("");
        txt_trans_sim_price_casing.setText("");
        txt_trans_sim_price_psu.setText("");
        txt_trans_sim_price_lcd.setText("");
        txt_trans_sim_price_optical.setText("");
        txt_trans_sim_price_keyboard.setText("");
        txt_trans_sim_price_speaker.setText("");
        txt_trans_sim_price_headset.setText("");
        txt_trans_sim_price_cpucooler.setText("");
        txt_trans_sim_price_coolerfan.setText("");
        txt_trans_sim_price_networking.setText("");

        dt_simulation.setDate(null);
        txt_trans_simulation_grandtotal.setText("#GrandTotalSimulation");
    }

    public void HitungJumlahTransaction() {
        try {
            String hasil = transactionDao.CountTransaction();
            txt_trans_success.setText(hasil);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void HitungJumlahTransactionPending() {
        try {
            String hasil = transactionDao.CountTransaction();
            txt_trans_pending.setText(hasil);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void HitungJumlahTugasCas() {
        try {
            String hasil = transactionDao.CountTaskCas();
            txt_jumlahtugas_cas.setText(hasil);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ShowPanelPayment() {
        lbl_trans_paymentinfo.setVisible(true);
        panel_Trans_Payment.setVisible(true);
        loadComboBoxBank();
        loadComboBoxPiutang();
    }

    public void HidePanelPayment() {
        lbl_trans_paymentinfo.setVisible(false);
        panel_Trans_Payment.setVisible(false);
    }

    public void HideTransactionElement() {
        btn_trans_process.setVisible(false);
        cb_trans_bank.setEnabled(false);
        cb_trans_piutang.setEnabled(false);
    }

    private void clearFormLogin() {
        txtUsernameCas.setText("");
        txtPasswordCas.setText("");
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
        panelCasDashboard = new javax.swing.JPanel();
        btnNotifCas = new javax.swing.JLabel();
        btnMsgCas = new javax.swing.JLabel();
        btnLogoutCas = new javax.swing.JLabel();
        subpanelCasDashboard = new javax.swing.JPanel();
        txt_jumlahtugas_cas = new javax.swing.JLabel();
        txt_trans_pending = new javax.swing.JLabel();
        txt_trans_success = new javax.swing.JLabel();
        lblDashboardTitle2 = new javax.swing.JLabel();
        lblDashboardTitle3 = new javax.swing.JLabel();
        jSeparator19 = new javax.swing.JSeparator();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        subpanelCasDataTransaction = new javax.swing.JPanel();
        lbl_searchnama1 = new javax.swing.JLabel();
        lbl_searchdep1 = new javax.swing.JLabel();
        lbl_searchidkyw1 = new javax.swing.JLabel();
        lbl_searchnoreg1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        txt_trans_searchByDis = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        txt_trans_searchByAdmin = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        txt_trans_searchByID = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        txt_trans_searchByDate = new javax.swing.JTextField();
        lblDataKaryawanTitel1 = new javax.swing.JLabel();
        lbl_management1 = new javax.swing.JLabel();
        jSeparator17 = new javax.swing.JSeparator();
        tabelDataKaryawanSP1 = new javax.swing.JScrollPane();
        tabelTransList = new javax.swing.JTable();
        lbl_daftarkyw1 = new javax.swing.JLabel();
        btn_printTrans = new javax.swing.JButton();
        ScrollPaneAddTrans = new javax.swing.JScrollPane();
        subpanelCasAddTransaction = new javax.swing.JPanel();
        panel_Trans_Invoice = new javax.swing.JPanel();
        jSeparator28 = new javax.swing.JSeparator();
        lblTransInvoiceNo = new javax.swing.JLabel();
        lblTransTotalNTA = new javax.swing.JLabel();
        jSeparator29 = new javax.swing.JSeparator();
        dtTrans_date = new com.toedter.calendar.JDateChooser();
        lblRp1 = new javax.swing.JLabel();
        lblTransCreateDate = new javax.swing.JLabel();
        jSeparator30 = new javax.swing.JSeparator();
        txt_trans_invoiceno = new javax.swing.JLabel();
        txt_trans_TotalNTA = new javax.swing.JLabel();
        btn_trans_show_nta = new javax.swing.JButton();
        btn_trans_hide_nta = new javax.swing.JButton();
        panel_Trans_Payment = new javax.swing.JPanel();
        jSeparator31 = new javax.swing.JSeparator();
        lblTransVoucher = new javax.swing.JLabel();
        lblTransAdmin = new javax.swing.JLabel();
        jSeparator32 = new javax.swing.JSeparator();
        txt_Trans_Admin = new javax.swing.JLabel();
        jSeparator35 = new javax.swing.JSeparator();
        lblTransPaymentVia = new javax.swing.JLabel();
        jSeparator39 = new javax.swing.JSeparator();
        lblTranPiutang = new javax.swing.JLabel();
        txt_trans_nominal = new javax.swing.JTextField();
        btn_Trans_getVoucher = new javax.swing.JButton();
        btn_Trans_rmvVoucher = new javax.swing.JButton();
        cb_trans_piutang = new javax.swing.JComboBox<>();
        cb_trans_paymentvia = new javax.swing.JComboBox<>();
        jSeparator44 = new javax.swing.JSeparator();
        lblTransBank = new javax.swing.JLabel();
        lblTransNominal = new javax.swing.JLabel();
        jSeparator45 = new javax.swing.JSeparator();
        txt_trans_vouchercode = new javax.swing.JTextField();
        lblTransPaymentType = new javax.swing.JLabel();
        jSeparator46 = new javax.swing.JSeparator();
        cb_trans_paymenttype = new javax.swing.JComboBox<>();
        cb_trans_bank = new javax.swing.JComboBox<>();
        jSeparator51 = new javax.swing.JSeparator();
        lblTransDescription = new javax.swing.JLabel();
        btn_trans_lock_payment = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        txt_trans_description = new javax.swing.JTextPane();
        panel_Trans_Voucher = new javax.swing.JPanel();
        jSeparator33 = new javax.swing.JSeparator();
        lblTransVoucherCode = new javax.swing.JLabel();
        lblTransDiscount = new javax.swing.JLabel();
        jSeparator34 = new javax.swing.JSeparator();
        lblRp2 = new javax.swing.JLabel();
        txt_trans_Vouchercode = new javax.swing.JLabel();
        txt_trans_voucherdiscount = new javax.swing.JLabel();
        panel_Trans_AddItem = new javax.swing.JPanel();
        lblTransItemQty = new javax.swing.JLabel();
        jSeparator36 = new javax.swing.JSeparator();
        jSeparator37 = new javax.swing.JSeparator();
        lblTransItemCode = new javax.swing.JLabel();
        btn_trans_add_item = new javax.swing.JButton();
        jSeparator38 = new javax.swing.JSeparator();
        lblTransItemName = new javax.swing.JLabel();
        txt_trans_itemname = new javax.swing.JTextField();
        btn_trans_check_stock = new javax.swing.JButton();
        txt_trans_itemcode = new javax.swing.JTextField();
        txt_trans_qty = new javax.swing.JTextField();
        jSeparator52 = new javax.swing.JSeparator();
        lblTransSellPriceType = new javax.swing.JLabel();
        lblTransNTAItem = new javax.swing.JLabel();
        jSeparator53 = new javax.swing.JSeparator();
        txt_trans_NTAFare = new javax.swing.JTextField();
        jSeparator54 = new javax.swing.JSeparator();
        lblTransItemQty2 = new javax.swing.JLabel();
        txt_trans_PubFare = new javax.swing.JTextField();
        jSeparator55 = new javax.swing.JSeparator();
        lblTransItemQty3 = new javax.swing.JLabel();
        txt_trans_SingleFare = new javax.swing.JTextField();
        cb_spt = new javax.swing.JComboBox<>();
        btn_trans_show_NTAItem = new javax.swing.JButton();
        btn_trans_hide_NTAItem = new javax.swing.JButton();
        jSeparator27 = new javax.swing.JSeparator();
        lblErrorReportTitle2 = new javax.swing.JLabel();
        lblDepartmentSubTitle10 = new javax.swing.JLabel();
        panel_Trans_CartList = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabelShoppingCart = new javax.swing.JTable();
        btn_trans_delitem = new javax.swing.JLabel();
        panel_Trans_Contact = new javax.swing.JPanel();
        jSeparator40 = new javax.swing.JSeparator();
        lblTransCustType = new javax.swing.JLabel();
        lblTransCustName = new javax.swing.JLabel();
        jSeparator41 = new javax.swing.JSeparator();
        txt_trans_custname = new javax.swing.JLabel();
        jSeparator42 = new javax.swing.JSeparator();
        lblTransCustEmail = new javax.swing.JLabel();
        txt_trans_custmail = new javax.swing.JLabel();
        jSeparator43 = new javax.swing.JSeparator();
        lblTransCustPhone = new javax.swing.JLabel();
        txt_trans_custphone = new javax.swing.JLabel();
        txt_trans_customerid = new javax.swing.JTextField();
        jSeparator56 = new javax.swing.JSeparator();
        lblTransCustCode = new javax.swing.JLabel();
        cb_CustType = new javax.swing.JComboBox<>();
        panel_Trans_Navigator = new javax.swing.JPanel();
        jSeparator58 = new javax.swing.JSeparator();
        lblTransTotalPricePub = new javax.swing.JLabel();
        lblIDR6 = new javax.swing.JLabel();
        txt_trans_total_harga_publish = new javax.swing.JLabel();
        jSeparator59 = new javax.swing.JSeparator();
        lblTransPriceDiscount = new javax.swing.JLabel();
        lblIDR9 = new javax.swing.JLabel();
        txt_price_disc = new javax.swing.JLabel();
        jSeparator57 = new javax.swing.JSeparator();
        lblTransPriceVoucher = new javax.swing.JLabel();
        lblIDR10 = new javax.swing.JLabel();
        txt_trans_voucher = new javax.swing.JLabel();
        jSeparator60 = new javax.swing.JSeparator();
        lblTransGrandTotal = new javax.swing.JLabel();
        lblIDR8 = new javax.swing.JLabel();
        txt_trans_grand_total = new javax.swing.JLabel();
        btn_trans_getQr = new javax.swing.JButton();
        panelqrcodetrans = new javax.swing.JPanel();
        lbl_qrcode_trans = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        lbl_trans_paymentinfo = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        btn_trans_new = new javax.swing.JButton();
        btn_trans_process = new javax.swing.JButton();
        btn_trans_clearall = new javax.swing.JButton();
        ScrollPaneSimulasi = new javax.swing.JScrollPane();
        subpanelCasSimulasi = new javax.swing.JPanel();
        panel_Trans_List_Simulation = new javax.swing.JPanel();
        lblTransSimulationCreatedDate1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabel_item_simulation = new javax.swing.JTable();
        panel_Trans_Add_GrandTotal = new javax.swing.JPanel();
        txt_trans_simulation_grandtotal = new javax.swing.JLabel();
        lblTransSimulationGrandTotal = new javax.swing.JLabel();
        btn_trans_lock_simulation = new javax.swing.JButton();
        panel_Trans_Add_Simulation_Detail = new javax.swing.JPanel();
        lblTransSimulationCreatedDate = new javax.swing.JLabel();
        txt_trans_simulation_id = new javax.swing.JLabel();
        txt_Trans_Simulation_Admin = new javax.swing.JLabel();
        lblTransSimulationID = new javax.swing.JLabel();
        lblTransSimulationAdmin = new javax.swing.JLabel();
        dt_simulation = new com.toedter.calendar.JDateChooser();
        panel_Trans_Add_Simulation = new javax.swing.JPanel();
        lblTrans_Sim_BrandProcessor = new javax.swing.JLabel();
        cb_trans_sim_brandprocessor = new javax.swing.JComboBox<>();
        lblTrans_Sim_Processor = new javax.swing.JLabel();
        cb_trans_sim_processor_amd = new javax.swing.JComboBox<>();
        cb_trans_sim_processor_intel = new javax.swing.JComboBox<>();
        txt_trans_sim_price_networking = new javax.swing.JTextField();
        lblTrans_Sim_Motherboard = new javax.swing.JLabel();
        cb_trans_sim_motherboard = new javax.swing.JComboBox<>();
        lblTrans_Sim_SSD = new javax.swing.JLabel();
        cb_trans_sim_ssd = new javax.swing.JComboBox<>();
        cb_trans_sim_hdd = new javax.swing.JComboBox<>();
        lblTrans_Sim_HDD = new javax.swing.JLabel();
        lblTrans_Sim_RAM = new javax.swing.JLabel();
        cb_trans_sim_ram = new javax.swing.JComboBox<>();
        lblTrans_Sim_VGA = new javax.swing.JLabel();
        cb_trans_sim_vga = new javax.swing.JComboBox<>();
        cb_trans_sim_casing = new javax.swing.JComboBox<>();
        lblTrans_Sim_Casing = new javax.swing.JLabel();
        cb_trans_sim_psu = new javax.swing.JComboBox<>();
        lblTrans_Sim_PSU = new javax.swing.JLabel();
        lblTrans_Sim_LCD = new javax.swing.JLabel();
        cb_trans_sim_lcd = new javax.swing.JComboBox<>();
        lblTrans_Sim_Optical = new javax.swing.JLabel();
        cb_trans_sim_optical = new javax.swing.JComboBox<>();
        cb_trans_sim_keyboard = new javax.swing.JComboBox<>();
        lblTrans_Sim_Keyboard = new javax.swing.JLabel();
        cb_trans_sim_speaker = new javax.swing.JComboBox<>();
        lblTrans_Sim_Speaker = new javax.swing.JLabel();
        lblTrans_Sim_Headset = new javax.swing.JLabel();
        cb_trans_sim_headset = new javax.swing.JComboBox<>();
        lblTrans_Sim_CPUCooler = new javax.swing.JLabel();
        cb_trans_sim_cpucooler = new javax.swing.JComboBox<>();
        cb_trans_sim_coolerfan = new javax.swing.JComboBox<>();
        lblTrans_Sim_CoolerFan = new javax.swing.JLabel();
        txt_trans_sim_price_processor = new javax.swing.JTextField();
        txt_trans_sim_price_motherboard = new javax.swing.JTextField();
        txt_trans_sim_price_ssd = new javax.swing.JTextField();
        txt_trans_sim_price_hdd = new javax.swing.JTextField();
        txt_trans_sim_price_ram = new javax.swing.JTextField();
        txt_trans_sim_price_vga = new javax.swing.JTextField();
        txt_trans_sim_price_casing = new javax.swing.JTextField();
        txt_trans_sim_price_psu = new javax.swing.JTextField();
        txt_trans_sim_price_lcd = new javax.swing.JTextField();
        txt_trans_sim_price_optical = new javax.swing.JTextField();
        txt_trans_sim_price_keyboard = new javax.swing.JTextField();
        txt_trans_sim_price_speaker = new javax.swing.JTextField();
        txt_trans_sim_price_headset = new javax.swing.JTextField();
        txt_trans_sim_price_cpucooler = new javax.swing.JTextField();
        lblTrans_Sim_Networking = new javax.swing.JLabel();
        cb_trans_sim_networking = new javax.swing.JComboBox<>();
        txt_trans_sim_price_coolerfan = new javax.swing.JTextField();
        jSeparator79 = new javax.swing.JSeparator();
        lblTransSimulationTitle = new javax.swing.JLabel();
        lblTransSimulationSubTitle = new javax.swing.JLabel();
        lblTransSimulationInputDataItem = new javax.swing.JLabel();
        btn_trans_new_sim = new javax.swing.JButton();
        btn_trans_clearall_sim = new javax.swing.JButton();
        btn_trans_process_sim = new javax.swing.JButton();
        subpanelCasListItem = new javax.swing.JPanel();
        lbl_searchItemName = new javax.swing.JLabel();
        lbl_searchItemBrand = new javax.swing.JLabel();
        lbl_searchItemCategory = new javax.swing.JLabel();
        lbl_searchItemID = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        txt_trans_searchitemByName = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        txt_trans_searchitemByBrand = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        txt_trans_searchitemByID = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        txt_trans_searchitemByCategory = new javax.swing.JTextField();
        lblDataKaryawanTitel2 = new javax.swing.JLabel();
        lbl_management2 = new javax.swing.JLabel();
        jSeparator18 = new javax.swing.JSeparator();
        tabelDataKaryawanSP2 = new javax.swing.JScrollPane();
        tabelItemList = new javax.swing.JTable();
        lbl_daftarkyw2 = new javax.swing.JLabel();
        subpanelCasErrorReport = new javax.swing.JPanel();
        btnSaveError_cas = new javax.swing.JLabel();
        btnClearError_cas = new javax.swing.JLabel();
        lbl_list_error_cas = new javax.swing.JLabel();
        cb_error_status_cas = new javax.swing.JComboBox<>();
        lbl_error_date_cas = new javax.swing.JLabel();
        dtError_Date_cas = new com.toedter.calendar.JDateChooser();
        lbl_error_status_cas = new javax.swing.JLabel();
        txt_error_desc_cas = new javax.swing.JTextArea();
        lbl_error_desc_cas = new javax.swing.JLabel();
        txt_error_title_cas = new javax.swing.JTextField();
        lbl_error_title_cas = new javax.swing.JLabel();
        txt_error_id_cas = new javax.swing.JTextField();
        lbl_error_id_cas = new javax.swing.JLabel();
        lblErrorReportTitleCas = new javax.swing.JLabel();
        lblErrorReportCasSubTitle1 = new javax.swing.JLabel();
        jSeparator14 = new javax.swing.JSeparator();
        tabelDaftarReportSP1 = new javax.swing.JScrollPane();
        tabelDaftarError_Cas = new javax.swing.JTable();
        lblErrorReportCasSubTitle = new javax.swing.JLabel();
        btn_delete_error_cas = new javax.swing.JLabel();
        subpanelCasAddDistributor = new javax.swing.JPanel();
        lblDistributor = new javax.swing.JLabel();
        lblDistributortSubTitle = new javax.swing.JLabel();
        jSeparator16 = new javax.swing.JSeparator();
        lblAddDistributorSubTitle = new javax.swing.JLabel();
        lbl_id_distributor = new javax.swing.JLabel();
        txt_distributor_id = new javax.swing.JTextField();
        lbl_distributor_name = new javax.swing.JLabel();
        txt_distributor_name = new javax.swing.JTextField();
        lbl_distributor_address = new javax.swing.JLabel();
        txt_distributor_address = new javax.swing.JTextArea();
        lbl_distributor_phone = new javax.swing.JLabel();
        txt_distributor_phone = new javax.swing.JTextField();
        lbl_distributor_mail = new javax.swing.JLabel();
        txt_distributor_email = new javax.swing.JTextField();
        lbl_distributor_desc = new javax.swing.JLabel();
        cb_distributor_description = new javax.swing.JComboBox<>();
        lbl_distributor_status = new javax.swing.JLabel();
        cb_distributor_status = new javax.swing.JComboBox<>();
        lblListDistributor = new javax.swing.JLabel();
        tabelDataDepartmentSP1 = new javax.swing.JScrollPane();
        tabelDataDistributor = new javax.swing.JTable();
        btn_active_distributor = new javax.swing.JLabel();
        btn_deactive_distributor = new javax.swing.JLabel();
        btnSaveDistributor = new javax.swing.JLabel();
        btnClearDistributor = new javax.swing.JLabel();
        jSeparator48 = new javax.swing.JSeparator();
        menuCasErrorReport = new javax.swing.JPanel();
        iconCasErrorReport = new javax.swing.JLabel();
        jSeparator21 = new javax.swing.JSeparator();
        menuCasSimulation = new javax.swing.JPanel();
        iconCasSimulation = new javax.swing.JLabel();
        jSeparator20 = new javax.swing.JSeparator();
        menuCasDataTransaction = new javax.swing.JPanel();
        iconCasDataTransaction = new javax.swing.JLabel();
        jSeparator22 = new javax.swing.JSeparator();
        menuCasListItem = new javax.swing.JPanel();
        iconCasListItem = new javax.swing.JLabel();
        jSeparator23 = new javax.swing.JSeparator();
        menuCasAddCustomer = new javax.swing.JPanel();
        iconCasAddCustomer = new javax.swing.JLabel();
        jSeparator24 = new javax.swing.JSeparator();
        menuCasAddTransaction = new javax.swing.JPanel();
        iconCasAddTrans = new javax.swing.JLabel();
        jSeparator25 = new javax.swing.JSeparator();
        menuCasDashboard = new javax.swing.JPanel();
        iconCasDashboard = new javax.swing.JLabel();
        jSeparator26 = new javax.swing.JSeparator();
        btnChangeStateCas = new javax.swing.JButton();
        btnHideIPCas = new javax.swing.JButton();
        btnShowIPCas = new javax.swing.JButton();
        txtServerTimeCas = new javax.swing.JLabel();
        txt_stateBigCas = new javax.swing.JLabel();
        lblStateBigCas = new javax.swing.JLabel();
        lblStateCas = new javax.swing.JLabel();
        txt_stateCas = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtCasNama = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        iconAdminCas = new javax.swing.JLabel();
        bgCashier = new javax.swing.JLabel();
        jSeparator47 = new javax.swing.JSeparator();
        panelKilledbyServer = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        panelCasLogin = new javax.swing.JPanel();
        txtUsernameCas = new javax.swing.JTextField();
        txtPasswordCas = new javax.swing.JPasswordField();
        btnLoginCas = new javax.swing.JButton();
        btnResetCas = new javax.swing.JButton();
        panelLoginCas = new javax.swing.JLabel();
        bgClientCashierLogin = new javax.swing.JLabel();
        panelCasLandingPage = new javax.swing.JPanel();
        iconHome2 = new javax.swing.JLabel();
        bgLogoCashier = new javax.swing.JLabel();
        bgClientCashier = new javax.swing.JLabel();
        panelBackOffice = new javax.swing.JPanel();
        iconSIM = new javax.swing.JLabel();
        iconHRD = new javax.swing.JLabel();
        iconWarehouse = new javax.swing.JLabel();
        iconKeuangan = new javax.swing.JLabel();
        iconManagement = new javax.swing.JLabel();
        iconBOBig = new javax.swing.JLabel();
        bgBackOffice = new javax.swing.JLabel();
        panelFrontOffice2 = new javax.swing.JPanel();
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
        setTitle("NOB Tech - Client - Cashier");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelCasDashboard.setAutoscrolls(true);
        panelCasDashboard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnNotifCas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconNotif.png"))); // NOI18N
        btnNotifCas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelCasDashboard.add(btnNotifCas, new org.netbeans.lib.awtextra.AbsoluteConstraints(1039, 97, -1, -1));

        btnMsgCas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconMassage.png"))); // NOI18N
        btnMsgCas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelCasDashboard.add(btnMsgCas, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 97, -1, -1));

        btnLogoutCas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconLogout.png"))); // NOI18N
        btnLogoutCas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogoutCas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogoutCasMouseClicked(evt);
            }
        });
        panelCasDashboard.add(btnLogoutCas, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 97, -1, -1));

        subpanelCasDashboard.setBackground(new java.awt.Color(255, 255, 255));
        subpanelCasDashboard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_jumlahtugas_cas.setFont(new java.awt.Font("Gotham Ultra", 1, 48)); // NOI18N
        txt_jumlahtugas_cas.setForeground(new java.awt.Color(255, 255, 255));
        txt_jumlahtugas_cas.setText("0");
        subpanelCasDashboard.add(txt_jumlahtugas_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 150, -1, -1));

        txt_trans_pending.setFont(new java.awt.Font("Gotham Ultra", 1, 48)); // NOI18N
        txt_trans_pending.setForeground(new java.awt.Color(255, 255, 255));
        txt_trans_pending.setText("0");
        subpanelCasDashboard.add(txt_trans_pending, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 150, -1, -1));

        txt_trans_success.setFont(new java.awt.Font("Gotham Ultra", 1, 48)); // NOI18N
        txt_trans_success.setForeground(new java.awt.Color(255, 255, 255));
        txt_trans_success.setText("0");
        subpanelCasDashboard.add(txt_trans_success, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 150, -1, -1));

        lblDashboardTitle2.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblDashboardTitle2.setForeground(java.awt.Color.gray);
        lblDashboardTitle2.setText("Cashier");
        subpanelCasDashboard.add(lblDashboardTitle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 53, -1, -1));

        lblDashboardTitle3.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblDashboardTitle3.setForeground(java.awt.Color.gray);
        lblDashboardTitle3.setText("DASHBOARD");
        subpanelCasDashboard.add(lblDashboardTitle3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        jSeparator19.setForeground(new java.awt.Color(241, 241, 241));
        subpanelCasDashboard.add(jSeparator19, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 930, 20));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconTaskQueue.png"))); // NOI18N
        subpanelCasDashboard.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 110, -1, -1));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconDashboardTransactionPending.png"))); // NOI18N
        subpanelCasDashboard.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 110, -1, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconDashboardTransactionSuccess.png"))); // NOI18N
        subpanelCasDashboard.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        panelCasDashboard.add(subpanelCasDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        subpanelCasDataTransaction.setBackground(new java.awt.Color(255, 255, 255));
        subpanelCasDataTransaction.setPreferredSize(new java.awt.Dimension(1010, 538));
        subpanelCasDataTransaction.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_searchnama1.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_searchnama1.setText("Search Distributor");
        subpanelCasDataTransaction.add(lbl_searchnama1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, -1));

        lbl_searchdep1.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_searchdep1.setText("Search Transaction by Admin");
        subpanelCasDataTransaction.add(lbl_searchdep1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 190, -1, -1));

        lbl_searchidkyw1.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_searchidkyw1.setText("Search Transaction Date");
        subpanelCasDataTransaction.add(lbl_searchidkyw1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 100, -1, -1));

        lbl_searchnoreg1.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_searchnoreg1.setText("Search ID Transaction");
        subpanelCasDataTransaction.add(lbl_searchnoreg1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        jPanel5.setBackground(new java.awt.Color(241, 241, 241));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_trans_searchByDis.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        txt_trans_searchByDis.setForeground(new java.awt.Color(204, 204, 204));
        txt_trans_searchByDis.setText("Press enter to search ...");
        txt_trans_searchByDis.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt_trans_searchByDis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_trans_searchByDisMouseClicked(evt);
            }
        });
        txt_trans_searchByDis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_trans_searchByDisKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_trans_searchByDisKeyTyped(evt);
            }
        });
        jPanel5.add(txt_trans_searchByDis, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 10, 430, 40));

        subpanelCasDataTransaction.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 460, 60));

        jPanel6.setBackground(new java.awt.Color(241, 241, 241));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_trans_searchByAdmin.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        txt_trans_searchByAdmin.setForeground(new java.awt.Color(204, 204, 204));
        txt_trans_searchByAdmin.setText("Press enter to search ...");
        txt_trans_searchByAdmin.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt_trans_searchByAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_trans_searchByAdminMouseClicked(evt);
            }
        });
        txt_trans_searchByAdmin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_trans_searchByAdminKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_trans_searchByAdminKeyTyped(evt);
            }
        });
        jPanel6.add(txt_trans_searchByAdmin, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 10, 430, 40));

        subpanelCasDataTransaction.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 210, 460, 60));

        jPanel7.setBackground(new java.awt.Color(241, 241, 241));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_trans_searchByID.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        txt_trans_searchByID.setForeground(new java.awt.Color(204, 204, 204));
        txt_trans_searchByID.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_trans_searchByID.setText("Press enter to search ...");
        txt_trans_searchByID.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt_trans_searchByID.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_trans_searchByIDMouseClicked(evt);
            }
        });
        txt_trans_searchByID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_trans_searchByIDKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_trans_searchByIDKeyTyped(evt);
            }
        });
        jPanel7.add(txt_trans_searchByID, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 10, 430, 40));

        subpanelCasDataTransaction.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 460, 60));

        jPanel8.setBackground(new java.awt.Color(241, 241, 241));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_trans_searchByDate.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        txt_trans_searchByDate.setForeground(new java.awt.Color(204, 204, 204));
        txt_trans_searchByDate.setText("Press enter to search ...");
        txt_trans_searchByDate.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt_trans_searchByDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_trans_searchByDateMouseClicked(evt);
            }
        });
        txt_trans_searchByDate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_trans_searchByDateKeyPressed(evt);
            }
        });
        jPanel8.add(txt_trans_searchByDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 10, 430, 40));

        subpanelCasDataTransaction.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 120, 460, 60));

        lblDataKaryawanTitel1.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblDataKaryawanTitel1.setForeground(java.awt.Color.gray);
        lblDataKaryawanTitel1.setText("DATA TRANSACTION");
        subpanelCasDataTransaction.add(lblDataKaryawanTitel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        lbl_management1.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_management1.setForeground(java.awt.Color.gray);
        lbl_management1.setText("Cashier");
        subpanelCasDataTransaction.add(lbl_management1, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 53, -1, -1));

        jSeparator17.setForeground(new java.awt.Color(241, 241, 241));
        subpanelCasDataTransaction.add(jSeparator17, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 950, 20));

        tabelDataKaryawanSP1.setBackground(new java.awt.Color(241, 241, 241));

        tabelTransList.setBackground(new java.awt.Color(240, 240, 240));
        tabelTransList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Trans", "ID Item", "Date", "Process By", "ID Cust", "Item Name", "Qty", "Price", "SubTotal", "Total Payment", "Status"
            }
        ));
        tabelTransList.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabelTransList.setGridColor(new java.awt.Color(241, 241, 241));
        tabelTransList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelTransListMouseClicked(evt);
            }
        });
        tabelDataKaryawanSP1.setViewportView(tabelTransList);
        if (tabelTransList.getColumnModel().getColumnCount() > 0) {
            tabelTransList.getColumnModel().getColumn(0).setPreferredWidth(120);
            tabelTransList.getColumnModel().getColumn(1).setPreferredWidth(400);
            tabelTransList.getColumnModel().getColumn(2).setPreferredWidth(100);
            tabelTransList.getColumnModel().getColumn(3).setPreferredWidth(100);
            tabelTransList.getColumnModel().getColumn(4).setPreferredWidth(100);
            tabelTransList.getColumnModel().getColumn(5).setPreferredWidth(200);
            tabelTransList.getColumnModel().getColumn(6).setPreferredWidth(200);
            tabelTransList.getColumnModel().getColumn(7).setMinWidth(110);
            tabelTransList.getColumnModel().getColumn(7).setPreferredWidth(110);
            tabelTransList.getColumnModel().getColumn(7).setMaxWidth(110);
            tabelTransList.getColumnModel().getColumn(7).setHeaderValue("Price");
            tabelTransList.getColumnModel().getColumn(8).setPreferredWidth(100);
            tabelTransList.getColumnModel().getColumn(9).setMinWidth(110);
            tabelTransList.getColumnModel().getColumn(9).setPreferredWidth(110);
            tabelTransList.getColumnModel().getColumn(9).setMaxWidth(110);
            tabelTransList.getColumnModel().getColumn(9).setHeaderValue("Total Payment");
            tabelTransList.getColumnModel().getColumn(10).setMinWidth(110);
            tabelTransList.getColumnModel().getColumn(10).setPreferredWidth(110);
            tabelTransList.getColumnModel().getColumn(10).setMaxWidth(110);
            tabelTransList.getColumnModel().getColumn(10).setHeaderValue("Status");
        }

        subpanelCasDataTransaction.add(tabelDataKaryawanSP1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 330, 950, 190));

        lbl_daftarkyw1.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_daftarkyw1.setForeground(java.awt.Color.gray);
        lbl_daftarkyw1.setText("List Transaction");
        subpanelCasDataTransaction.add(lbl_daftarkyw1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, -1, -1));

        btn_printTrans.setBackground(new java.awt.Color(153, 0, 0));
        btn_printTrans.setForeground(new java.awt.Color(255, 255, 255));
        btn_printTrans.setText("PRINT");
        btn_printTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_printTransActionPerformed(evt);
            }
        });
        subpanelCasDataTransaction.add(btn_printTrans, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 290, 90, 30));

        panelCasDashboard.add(subpanelCasDataTransaction, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        ScrollPaneAddTrans.setBorder(null);
        ScrollPaneAddTrans.setForeground(new java.awt.Color(204, 204, 204));
        ScrollPaneAddTrans.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        ScrollPaneAddTrans.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        ScrollPaneAddTrans.setAutoscrolls(true);
        ScrollPaneAddTrans.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                ScrollPaneAddTransMouseWheelMoved(evt);
            }
        });
        ScrollPaneAddTrans.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ScrollPaneAddTransMouseEntered(evt);
            }
        });

        subpanelCasAddTransaction.setBackground(new java.awt.Color(255, 255, 255));
        subpanelCasAddTransaction.setPreferredSize(new java.awt.Dimension(1010, 1450));
        subpanelCasAddTransaction.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        dtTrans_date.setToolTipText(null);
        panel_Trans_Invoice.add(dtTrans_date, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 110, 170, 30));

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
        panel_Trans_Invoice.add(btn_trans_show_nta, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 170, 120, 30));

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
        panel_Trans_Invoice.add(btn_trans_hide_nta, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 170, 120, 30));

        subpanelCasAddTransaction.add(panel_Trans_Invoice, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 140, 420, 210));

        panel_Trans_Payment.setBackground(new java.awt.Color(243, 243, 243));
        panel_Trans_Payment.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSeparator31.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator31.setToolTipText(null);
        panel_Trans_Payment.add(jSeparator31, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 400, 10));

        lblTransVoucher.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransVoucher.setForeground(new java.awt.Color(128, 128, 128));
        lblTransVoucher.setText("Voucher");
        lblTransVoucher.setToolTipText(null);
        panel_Trans_Payment.add(lblTransVoucher, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 37, -1, -1));

        lblTransAdmin.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransAdmin.setForeground(new java.awt.Color(128, 128, 128));
        lblTransAdmin.setText("By Admin");
        lblTransAdmin.setToolTipText(null);
        panel_Trans_Payment.add(lblTransAdmin, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 83, -1, -1));

        jSeparator32.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator32.setToolTipText(null);
        panel_Trans_Payment.add(jSeparator32, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 400, 10));

        txt_Trans_Admin.setFont(new java.awt.Font("Gotham Light", 1, 18)); // NOI18N
        txt_Trans_Admin.setForeground(new java.awt.Color(204, 0, 51));
        txt_Trans_Admin.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txt_Trans_Admin.setText("CAS-1818002");
        txt_Trans_Admin.setToolTipText(null);
        txt_Trans_Admin.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panel_Trans_Payment.add(txt_Trans_Admin, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 83, 240, -1));

        jSeparator35.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator35.setToolTipText(null);
        panel_Trans_Payment.add(jSeparator35, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 400, 10));

        lblTransPaymentVia.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransPaymentVia.setForeground(new java.awt.Color(128, 128, 128));
        lblTransPaymentVia.setText("Payment Via");
        lblTransPaymentVia.setToolTipText(null);
        panel_Trans_Payment.add(lblTransPaymentVia, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 123, -1, -1));

        jSeparator39.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator39.setToolTipText(null);
        panel_Trans_Payment.add(jSeparator39, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 400, 10));

        lblTranPiutang.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTranPiutang.setForeground(new java.awt.Color(128, 128, 128));
        lblTranPiutang.setText("Piutang By ");
        lblTranPiutang.setToolTipText(null);
        panel_Trans_Payment.add(lblTranPiutang, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, -1, -1));

        txt_trans_nominal.setForeground(new java.awt.Color(153, 153, 153));
        txt_trans_nominal.setText("Contoh : 1190000");
        txt_trans_nominal.setToolTipText(null);
        txt_trans_nominal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_trans_nominalMouseClicked(evt);
            }
        });
        txt_trans_nominal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_trans_nominalKeyTyped(evt);
            }
        });
        panel_Trans_Payment.add(txt_trans_nominal, new org.netbeans.lib.awtextra.AbsoluteConstraints(205, 200, 200, 30));

        btn_Trans_getVoucher.setBackground(new java.awt.Color(153, 0, 0));
        btn_Trans_getVoucher.setForeground(new java.awt.Color(255, 255, 255));
        btn_Trans_getVoucher.setText("GET VOUCHER");
        btn_Trans_getVoucher.setToolTipText(null);
        btn_Trans_getVoucher.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_Trans_getVoucher.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_Trans_getVoucherMouseClicked(evt);
            }
        });
        panel_Trans_Payment.add(btn_Trans_getVoucher, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 30, 120, 30));

        btn_Trans_rmvVoucher.setBackground(new java.awt.Color(0, 102, 153));
        btn_Trans_rmvVoucher.setForeground(new java.awt.Color(255, 255, 255));
        btn_Trans_rmvVoucher.setText("REMOVE VOUCHER");
        btn_Trans_rmvVoucher.setToolTipText(null);
        btn_Trans_rmvVoucher.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_Trans_rmvVoucher.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_Trans_rmvVoucherMouseClicked(evt);
            }
        });
        panel_Trans_Payment.add(btn_Trans_rmvVoucher, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 30, 120, 30));

        cb_trans_piutang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Piutang By -" }));
        cb_trans_piutang.setToolTipText(null);
        cb_trans_piutang.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_trans_piutangItemStateChanged(evt);
            }
        });
        panel_Trans_Payment.add(cb_trans_piutang, new org.netbeans.lib.awtextra.AbsoluteConstraints(206, 290, 200, -1));

        cb_trans_paymentvia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Payment Via -", "Tamu Datang", "SMS Center", "Call Center", "Online" }));
        cb_trans_paymentvia.setToolTipText(null);
        cb_trans_paymentvia.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_trans_paymentviaItemStateChanged(evt);
            }
        });
        panel_Trans_Payment.add(cb_trans_paymentvia, new org.netbeans.lib.awtextra.AbsoluteConstraints(206, 120, 200, -1));

        jSeparator44.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator44.setToolTipText(null);
        panel_Trans_Payment.add(jSeparator44, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 400, 10));

        lblTransBank.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransBank.setForeground(new java.awt.Color(128, 128, 128));
        lblTransBank.setText("Bank");
        lblTransBank.setToolTipText(null);
        panel_Trans_Payment.add(lblTransBank, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 253, -1, -1));

        lblTransNominal.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransNominal.setForeground(new java.awt.Color(128, 128, 128));
        lblTransNominal.setText("Nominal (*)");
        lblTransNominal.setToolTipText(null);
        panel_Trans_Payment.add(lblTransNominal, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 205, -1, -1));

        jSeparator45.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator45.setToolTipText(null);
        panel_Trans_Payment.add(jSeparator45, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 400, 10));

        txt_trans_vouchercode.setToolTipText(null);
        txt_trans_vouchercode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_trans_vouchercodeKeyPressed(evt);
            }
        });
        panel_Trans_Payment.add(txt_trans_vouchercode, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 30, 110, 30));

        lblTransPaymentType.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransPaymentType.setForeground(new java.awt.Color(128, 128, 128));
        lblTransPaymentType.setText("Payment Type");
        lblTransPaymentType.setToolTipText(null);
        panel_Trans_Payment.add(lblTransPaymentType, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 163, -1, -1));

        jSeparator46.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator46.setToolTipText(null);
        panel_Trans_Payment.add(jSeparator46, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 400, 10));

        cb_trans_paymenttype.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Payment Type -", "Tunai", "Transfer", "Debit", "Piutang" }));
        cb_trans_paymenttype.setToolTipText(null);
        cb_trans_paymenttype.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_trans_paymenttypeItemStateChanged(evt);
            }
        });
        panel_Trans_Payment.add(cb_trans_paymenttype, new org.netbeans.lib.awtextra.AbsoluteConstraints(206, 160, 200, -1));

        cb_trans_bank.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Bank -" }));
        cb_trans_bank.setToolTipText(null);
        cb_trans_bank.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_trans_bankItemStateChanged(evt);
            }
        });
        panel_Trans_Payment.add(cb_trans_bank, new org.netbeans.lib.awtextra.AbsoluteConstraints(206, 250, 200, -1));

        jSeparator51.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator51.setToolTipText(null
        );
        panel_Trans_Payment.add(jSeparator51, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 400, 10));

        lblTransDescription.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransDescription.setForeground(new java.awt.Color(128, 128, 128));
        lblTransDescription.setText("Description");
        lblTransDescription.setToolTipText(null
        );
        panel_Trans_Payment.add(lblTransDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, -1, -1));

        btn_trans_lock_payment.setBackground(new java.awt.Color(153, 0, 0));
        btn_trans_lock_payment.setForeground(new java.awt.Color(255, 255, 255));
        btn_trans_lock_payment.setText("LOCK");
        btn_trans_lock_payment.setToolTipText(null
        );
        btn_trans_lock_payment.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_trans_lock_paymentMouseClicked(evt);
            }
        });
        panel_Trans_Payment.add(btn_trans_lock_payment, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, -1, 30));

        jScrollPane5.setToolTipText("<none>");

        txt_trans_description.setToolTipText(null);
        txt_trans_description.setEnabled(false);
        jScrollPane5.setViewportView(txt_trans_description);

        panel_Trans_Payment.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 330, 280, 110));

        subpanelCasAddTransaction.add(panel_Trans_Payment, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 920, 420, 450));

        panel_Trans_Voucher.setBackground(new java.awt.Color(243, 243, 243));
        panel_Trans_Voucher.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSeparator33.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator33.setForeground(new java.awt.Color(241, 241, 241));
        jSeparator33.setToolTipText(null);
        panel_Trans_Voucher.add(jSeparator33, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 400, 10));

        lblTransVoucherCode.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransVoucherCode.setForeground(new java.awt.Color(128, 128, 128));
        lblTransVoucherCode.setText("Voucher Code");
        lblTransVoucherCode.setToolTipText(null);
        panel_Trans_Voucher.add(lblTransVoucherCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, -1));

        lblTransDiscount.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransDiscount.setForeground(new java.awt.Color(128, 128, 128));
        lblTransDiscount.setText("Discount");
        lblTransDiscount.setToolTipText(null);
        panel_Trans_Voucher.add(lblTransDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 65, -1, -1));

        jSeparator34.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator34.setForeground(new java.awt.Color(241, 241, 241));
        jSeparator34.setToolTipText(null);
        panel_Trans_Voucher.add(jSeparator34, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 55, 400, 10));

        lblRp2.setFont(new java.awt.Font("Gotham Light", 1, 18)); // NOI18N
        lblRp2.setForeground(new java.awt.Color(51, 102, 0));
        lblRp2.setText("IDR");
        lblRp2.setToolTipText(null);
        panel_Trans_Voucher.add(lblRp2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 65, -1, -1));

        txt_trans_Vouchercode.setFont(new java.awt.Font("Gotham Light", 1, 18)); // NOI18N
        txt_trans_Vouchercode.setForeground(new java.awt.Color(255, 51, 51));
        txt_trans_Vouchercode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txt_trans_Vouchercode.setText("#VoucherCode");
        txt_trans_Vouchercode.setToolTipText(null);
        txt_trans_Vouchercode.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panel_Trans_Voucher.add(txt_trans_Vouchercode, new org.netbeans.lib.awtextra.AbsoluteConstraints(241, 30, 170, -1));

        txt_trans_voucherdiscount.setFont(new java.awt.Font("Gotham Light", 1, 18)); // NOI18N
        txt_trans_voucherdiscount.setForeground(new java.awt.Color(51, 102, 0));
        txt_trans_voucherdiscount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txt_trans_voucherdiscount.setText("0");
        txt_trans_voucherdiscount.setToolTipText(null);
        txt_trans_voucherdiscount.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panel_Trans_Voucher.add(txt_trans_voucherdiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 65, 130, -1));

        subpanelCasAddTransaction.add(panel_Trans_Voucher, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 400, 420, 140));

        panel_Trans_AddItem.setBackground(new java.awt.Color(243, 243, 243));
        panel_Trans_AddItem.setForeground(new java.awt.Color(255, 255, 255));
        panel_Trans_AddItem.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTransItemQty.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransItemQty.setForeground(java.awt.Color.gray);
        lblTransItemQty.setText("Qty (s)");
        lblTransItemQty.setToolTipText(null);
        panel_Trans_AddItem.add(lblTransItemQty, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, -1, -1));

        jSeparator36.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator36.setForeground(new java.awt.Color(241, 241, 241));
        jSeparator36.setToolTipText(null);
        panel_Trans_AddItem.add(jSeparator36, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 460, 10));

        jSeparator37.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator37.setForeground(new java.awt.Color(241, 241, 241));
        jSeparator37.setToolTipText(null);
        panel_Trans_AddItem.add(jSeparator37, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 460, 10));

        lblTransItemCode.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransItemCode.setForeground(java.awt.Color.gray);
        lblTransItemCode.setText("Item Code");
        lblTransItemCode.setToolTipText(null);
        panel_Trans_AddItem.add(lblTransItemCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        btn_trans_add_item.setBackground(new java.awt.Color(0, 102, 204));
        btn_trans_add_item.setForeground(new java.awt.Color(255, 255, 255));
        btn_trans_add_item.setText("+ Tambah Item");
        btn_trans_add_item.setToolTipText(null);
        btn_trans_add_item.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_trans_add_item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_trans_add_itemMouseClicked(evt);
            }
        });
        panel_Trans_AddItem.add(btn_trans_add_item, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 370, 130, 30));

        jSeparator38.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator38.setForeground(new java.awt.Color(241, 241, 241));
        jSeparator38.setToolTipText(null);
        panel_Trans_AddItem.add(jSeparator38, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 460, 10));

        lblTransItemName.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransItemName.setForeground(java.awt.Color.gray);
        lblTransItemName.setText("Item");
        lblTransItemName.setToolTipText(null);
        panel_Trans_AddItem.add(lblTransItemName, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 87, -1, -1));

        txt_trans_itemname.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_trans_itemname.setToolTipText(null);
        txt_trans_itemname.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txt_trans_itemname.setEnabled(false);
        panel_Trans_AddItem.add(txt_trans_itemname, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, 290, 30));

        btn_trans_check_stock.setText("CHECK STOCK");
        btn_trans_check_stock.setToolTipText(null);
        btn_trans_check_stock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_trans_check_stockMouseClicked(evt);
            }
        });
        panel_Trans_AddItem.add(btn_trans_check_stock, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 270, 120, 30));

        txt_trans_itemcode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_trans_itemcode.setForeground(new java.awt.Color(153, 153, 153));
        txt_trans_itemcode.setText("Please Enter to get Item Detail ...");
        txt_trans_itemcode.setToolTipText(null);
        txt_trans_itemcode.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txt_trans_itemcode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_trans_itemcodeMouseClicked(evt);
            }
        });
        txt_trans_itemcode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_trans_itemcodeKeyPressed(evt);
            }
        });
        panel_Trans_AddItem.add(txt_trans_itemcode, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 290, 30));

        txt_trans_qty.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_trans_qty.setToolTipText(null);
        txt_trans_qty.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        panel_Trans_AddItem.add(txt_trans_qty, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 270, 70, 30));

        jSeparator52.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator52.setForeground(new java.awt.Color(241, 241, 241));
        jSeparator52.setToolTipText(null);
        panel_Trans_AddItem.add(jSeparator52, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 460, 10));

        lblTransSellPriceType.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransSellPriceType.setForeground(java.awt.Color.gray);
        lblTransSellPriceType.setText("Sell Price Type");
        lblTransSellPriceType.setToolTipText(null);
        panel_Trans_AddItem.add(lblTransSellPriceType, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, -1, -1));

        lblTransNTAItem.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransNTAItem.setForeground(java.awt.Color.gray);
        lblTransNTAItem.setText("NTA Fare");
        lblTransNTAItem.setToolTipText(null);
        panel_Trans_AddItem.add(lblTransNTAItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));

        jSeparator53.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator53.setForeground(new java.awt.Color(241, 241, 241));
        jSeparator53.setToolTipText(null);
        panel_Trans_AddItem.add(jSeparator53, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 460, 10));

        txt_trans_NTAFare.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_trans_NTAFare.setForeground(new java.awt.Color(153, 153, 153));
        txt_trans_NTAFare.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_trans_NTAFare.setText("Not Show");
        txt_trans_NTAFare.setToolTipText(null);
        txt_trans_NTAFare.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txt_trans_NTAFare.setEnabled(false);
        panel_Trans_AddItem.add(txt_trans_NTAFare, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 130, 150, 30));

        jSeparator54.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator54.setForeground(new java.awt.Color(241, 241, 241));
        jSeparator54.setToolTipText(null);
        panel_Trans_AddItem.add(jSeparator54, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 460, 10));

        lblTransItemQty2.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransItemQty2.setForeground(java.awt.Color.gray);
        lblTransItemQty2.setText("Publish Fare");
        lblTransItemQty2.setToolTipText(null);
        panel_Trans_AddItem.add(lblTransItemQty2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 183, -1, -1));

        txt_trans_PubFare.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_trans_PubFare.setForeground(new java.awt.Color(153, 153, 153));
        txt_trans_PubFare.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_trans_PubFare.setToolTipText(null);
        txt_trans_PubFare.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txt_trans_PubFare.setEnabled(false);
        panel_Trans_AddItem.add(txt_trans_PubFare, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 175, 150, 30));

        jSeparator55.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator55.setForeground(new java.awt.Color(241, 241, 241));
        jSeparator55.setToolTipText(null);
        panel_Trans_AddItem.add(jSeparator55, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 460, 10));

        lblTransItemQty3.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransItemQty3.setForeground(java.awt.Color.gray);
        lblTransItemQty3.setText("Single Fare");
        lblTransItemQty3.setToolTipText(null);
        panel_Trans_AddItem.add(lblTransItemQty3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, -1, -1));

        txt_trans_SingleFare.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_trans_SingleFare.setForeground(new java.awt.Color(153, 153, 153));
        txt_trans_SingleFare.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_trans_SingleFare.setToolTipText(null);
        txt_trans_SingleFare.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txt_trans_SingleFare.setEnabled(false);
        panel_Trans_AddItem.add(txt_trans_SingleFare, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 220, 150, 30));

        cb_spt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Sell Price Type -", "Sell Price Type 1", "Sell Price Type 2" }));
        cb_spt.setToolTipText(null);
        cb_spt.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_sptItemStateChanged(evt);
            }
        });
        panel_Trans_AddItem.add(cb_spt, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 330, 290, -1));

        btn_trans_show_NTAItem.setBackground(new java.awt.Color(153, 0, 0));
        btn_trans_show_NTAItem.setForeground(new java.awt.Color(255, 255, 255));
        btn_trans_show_NTAItem.setText("SHOW NTA");
        btn_trans_show_NTAItem.setToolTipText(null);
        btn_trans_show_NTAItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_trans_show_NTAItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_trans_show_NTAItemMouseClicked(evt);
            }
        });
        panel_Trans_AddItem.add(btn_trans_show_NTAItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 130, 120, 30));

        btn_trans_hide_NTAItem.setBackground(new java.awt.Color(0, 102, 153));
        btn_trans_hide_NTAItem.setForeground(new java.awt.Color(255, 255, 255));
        btn_trans_hide_NTAItem.setText("HIDE NTA");
        btn_trans_hide_NTAItem.setToolTipText(null);
        btn_trans_hide_NTAItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_trans_hide_NTAItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_trans_hide_NTAItemMouseClicked(evt);
            }
        });
        panel_Trans_AddItem.add(btn_trans_hide_NTAItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 130, 120, 30));

        subpanelCasAddTransaction.add(panel_Trans_AddItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 500, 410));

        jSeparator27.setBackground(new java.awt.Color(51, 51, 51));
        jSeparator27.setForeground(new java.awt.Color(241, 241, 241));
        subpanelCasAddTransaction.add(jSeparator27, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 940, 20));

        lblErrorReportTitle2.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblErrorReportTitle2.setForeground(new java.awt.Color(102, 102, 102));
        lblErrorReportTitle2.setText("ADD TRANSACTION");
        subpanelCasAddTransaction.add(lblErrorReportTitle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        lblDepartmentSubTitle10.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblDepartmentSubTitle10.setForeground(new java.awt.Color(153, 153, 153));
        lblDepartmentSubTitle10.setText("Cashier");
        subpanelCasAddTransaction.add(lblDepartmentSubTitle10, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 53, -1, -1));

        panel_Trans_CartList.setBackground(new java.awt.Color(243, 243, 243));
        panel_Trans_CartList.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabelShoppingCart.setAutoCreateRowSorter(true);
        tabelShoppingCart.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelShoppingCart.setToolTipText(null);
        tabelShoppingCart.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabelShoppingCart.setGridColor(new java.awt.Color(153, 153, 153));
        tabelShoppingCart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelShoppingCartMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tabelShoppingCart);
        if (tabelShoppingCart.getColumnModel().getColumnCount() > 0) {
            tabelShoppingCart.getColumnModel().getColumn(0).setPreferredWidth(120);
            tabelShoppingCart.getColumnModel().getColumn(1).setPreferredWidth(400);
        }

        panel_Trans_CartList.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 480, 180));

        btn_trans_delitem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/btnDelete.png"))); // NOI18N
        btn_trans_delitem.setToolTipText(null);
        btn_trans_delitem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_trans_delitemMouseClicked(evt);
            }
        });
        panel_Trans_CartList.add(btn_trans_delitem, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, -1, -1));

        subpanelCasAddTransaction.add(panel_Trans_CartList, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 600, 500, 260));

        panel_Trans_Contact.setBackground(new java.awt.Color(243, 243, 243));
        panel_Trans_Contact.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSeparator40.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator40.setToolTipText(null);
        panel_Trans_Contact.add(jSeparator40, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 400, 10));

        lblTransCustType.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransCustType.setForeground(new java.awt.Color(128, 128, 128));
        lblTransCustType.setText("Customer Type");
        lblTransCustType.setToolTipText(null);
        panel_Trans_Contact.add(lblTransCustType, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, -1));

        lblTransCustName.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransCustName.setForeground(new java.awt.Color(128, 128, 128));
        lblTransCustName.setText("Name");
        lblTransCustName.setToolTipText(null);
        panel_Trans_Contact.add(lblTransCustName, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));

        jSeparator41.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator41.setToolTipText(null);
        panel_Trans_Contact.add(jSeparator41, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 400, 10));

        txt_trans_custname.setFont(new java.awt.Font("Gotham Light", 1, 18)); // NOI18N
        txt_trans_custname.setForeground(new java.awt.Color(0, 102, 204));
        txt_trans_custname.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txt_trans_custname.setText("#YourName");
        txt_trans_custname.setToolTipText(null);
        txt_trans_custname.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panel_Trans_Contact.add(txt_trans_custname, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 120, 240, -1));

        jSeparator42.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator42.setToolTipText(null);
        panel_Trans_Contact.add(jSeparator42, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 400, 10));

        lblTransCustEmail.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransCustEmail.setForeground(new java.awt.Color(128, 128, 128));
        lblTransCustEmail.setText("Email");
        lblTransCustEmail.setToolTipText(null);
        panel_Trans_Contact.add(lblTransCustEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));

        txt_trans_custmail.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        txt_trans_custmail.setForeground(new java.awt.Color(51, 51, 51));
        txt_trans_custmail.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txt_trans_custmail.setText("@youremail");
        txt_trans_custmail.setToolTipText(null);
        txt_trans_custmail.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panel_Trans_Contact.add(txt_trans_custmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 160, 230, -1));

        jSeparator43.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator43.setToolTipText(null);
        panel_Trans_Contact.add(jSeparator43, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 400, 10));

        lblTransCustPhone.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransCustPhone.setForeground(new java.awt.Color(128, 128, 128));
        lblTransCustPhone.setText("Phone");
        lblTransCustPhone.setToolTipText(null);
        panel_Trans_Contact.add(lblTransCustPhone, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, -1, -1));

        txt_trans_custphone.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        txt_trans_custphone.setForeground(new java.awt.Color(51, 51, 51));
        txt_trans_custphone.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txt_trans_custphone.setText("#PhoneNumber");
        txt_trans_custphone.setToolTipText(null);
        txt_trans_custphone.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panel_Trans_Contact.add(txt_trans_custphone, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 200, 230, -1));

        txt_trans_customerid.setForeground(new java.awt.Color(153, 153, 153));
        txt_trans_customerid.setText("Press Enter to Get Customer Data ...");
        txt_trans_customerid.setToolTipText(null);
        txt_trans_customerid.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_trans_customeridMouseClicked(evt);
            }
        });
        txt_trans_customerid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_trans_customeridKeyPressed(evt);
            }
        });
        panel_Trans_Contact.add(txt_trans_customerid, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 70, 210, 30));

        jSeparator56.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator56.setToolTipText(null);
        panel_Trans_Contact.add(jSeparator56, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 400, 10));

        lblTransCustCode.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransCustCode.setForeground(new java.awt.Color(128, 128, 128));
        lblTransCustCode.setText("Customer Code");
        lblTransCustCode.setToolTipText(null);
        panel_Trans_Contact.add(lblTransCustCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 77, -1, -1));

        cb_CustType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Customer Type -", "Customer", "Distributor" }));
        cb_CustType.setToolTipText(null);
        cb_CustType.setEnabled(false);
        panel_Trans_Contact.add(cb_CustType, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 210, -1));

        subpanelCasAddTransaction.add(panel_Trans_Contact, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 600, 420, 260));

        panel_Trans_Navigator.setBackground(new java.awt.Color(243, 243, 243));
        panel_Trans_Navigator.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSeparator58.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator58.setToolTipText(null);
        panel_Trans_Navigator.add(jSeparator58, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 460, 10));

        lblTransTotalPricePub.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransTotalPricePub.setForeground(java.awt.Color.gray);
        lblTransTotalPricePub.setText("Total Harga Publish");
        lblTransTotalPricePub.setToolTipText(null);
        panel_Trans_Navigator.add(lblTransTotalPricePub, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        lblIDR6.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblIDR6.setForeground(java.awt.Color.gray);
        lblIDR6.setText("IDR");
        lblIDR6.setToolTipText(null);
        panel_Trans_Navigator.add(lblIDR6, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 40, -1, -1));

        txt_trans_total_harga_publish.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        txt_trans_total_harga_publish.setForeground(java.awt.Color.gray);
        txt_trans_total_harga_publish.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txt_trans_total_harga_publish.setText("0");
        txt_trans_total_harga_publish.setToolTipText(null);
        txt_trans_total_harga_publish.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panel_Trans_Navigator.add(txt_trans_total_harga_publish, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 40, 180, -1));

        jSeparator59.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator59.setToolTipText(null);
        panel_Trans_Navigator.add(jSeparator59, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 460, 10));

        lblTransPriceDiscount.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransPriceDiscount.setForeground(java.awt.Color.gray);
        lblTransPriceDiscount.setText("Discount");
        lblTransPriceDiscount.setToolTipText(null);
        panel_Trans_Navigator.add(lblTransPriceDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 87, -1, -1));

        lblIDR9.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblIDR9.setForeground(java.awt.Color.gray);
        lblIDR9.setText("IDR");
        lblIDR9.setToolTipText(null);
        panel_Trans_Navigator.add(lblIDR9, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 87, -1, -1));

        txt_price_disc.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        txt_price_disc.setForeground(java.awt.Color.gray);
        txt_price_disc.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txt_price_disc.setText("0");
        txt_price_disc.setToolTipText(null);
        txt_price_disc.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panel_Trans_Navigator.add(txt_price_disc, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 90, 180, -1));

        jSeparator57.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator57.setToolTipText(null);
        panel_Trans_Navigator.add(jSeparator57, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 460, 10));

        lblTransPriceVoucher.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransPriceVoucher.setForeground(java.awt.Color.gray);
        lblTransPriceVoucher.setText("Voucher");
        lblTransPriceVoucher.setToolTipText(null);
        panel_Trans_Navigator.add(lblTransPriceVoucher, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 135, -1, -1));

        lblIDR10.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblIDR10.setForeground(java.awt.Color.gray);
        lblIDR10.setText("IDR");
        lblIDR10.setToolTipText(null);
        panel_Trans_Navigator.add(lblIDR10, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 135, -1, -1));

        txt_trans_voucher.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        txt_trans_voucher.setForeground(java.awt.Color.gray);
        txt_trans_voucher.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txt_trans_voucher.setText("0");
        txt_trans_voucher.setToolTipText(null);
        txt_trans_voucher.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panel_Trans_Navigator.add(txt_trans_voucher, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 135, 180, -1));

        jSeparator60.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator60.setToolTipText(null);
        panel_Trans_Navigator.add(jSeparator60, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 165, 460, 10));

        lblTransGrandTotal.setFont(new java.awt.Font("Gotham Light", 1, 18)); // NOI18N
        lblTransGrandTotal.setForeground(java.awt.Color.gray);
        lblTransGrandTotal.setText("Grand Total");
        lblTransGrandTotal.setToolTipText(null);
        panel_Trans_Navigator.add(lblTransGrandTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, -1, -1));

        lblIDR8.setFont(new java.awt.Font("Gotham Light", 1, 18)); // NOI18N
        lblIDR8.setForeground(java.awt.Color.gray);
        lblIDR8.setText("IDR");
        lblIDR8.setToolTipText(null);
        panel_Trans_Navigator.add(lblIDR8, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 180, -1, -1));

        txt_trans_grand_total.setFont(new java.awt.Font("Gotham Medium", 1, 18)); // NOI18N
        txt_trans_grand_total.setForeground(java.awt.Color.gray);
        txt_trans_grand_total.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txt_trans_grand_total.setText("0");
        txt_trans_grand_total.setToolTipText(null);
        txt_trans_grand_total.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panel_Trans_Navigator.add(txt_trans_grand_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 180, 180, -1));

        btn_trans_getQr.setBackground(new java.awt.Color(0, 102, 153));
        btn_trans_getQr.setForeground(new java.awt.Color(255, 255, 255));
        btn_trans_getQr.setText("GET QR");
        btn_trans_getQr.setToolTipText(null);
        btn_trans_getQr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_trans_getQrMouseClicked(evt);
            }
        });
        panel_Trans_Navigator.add(btn_trans_getQr, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 140, 30));

        panelqrcodetrans.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_qrcode_trans.setBackground(new java.awt.Color(255, 255, 255));
        lbl_qrcode_trans.setForeground(new java.awt.Color(255, 51, 204));
        panelqrcodetrans.add(lbl_qrcode_trans, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 141, 168));

        panel_Trans_Navigator.add(panelqrcodetrans, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 240, 160, 190));

        subpanelCasAddTransaction.add(panel_Trans_Navigator, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 920, 500, 450));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconDataKeranjangItem_1.png"))); // NOI18N
        subpanelCasAddTransaction.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 570, -1, -1));

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconDataTotalHarga_1.png"))); // NOI18N
        subpanelCasAddTransaction.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 890, -1, -1));

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconDataVoucher_1.png"))); // NOI18N
        subpanelCasAddTransaction.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 370, -1, -1));

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconDetailInvoice.png"))); // NOI18N
        subpanelCasAddTransaction.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 110, -1, -1));

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconInputDataItem_1.png"))); // NOI18N
        subpanelCasAddTransaction.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconInformasiKontak_1.png"))); // NOI18N
        subpanelCasAddTransaction.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 570, -1, -1));

        lbl_trans_paymentinfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconDataPembayaran_1.png"))); // NOI18N
        subpanelCasAddTransaction.add(lbl_trans_paymentinfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 890, -1, -1));

        jButton3.setBackground(new java.awt.Color(0, 102, 204));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("+ Add Payment");
        jButton3.setToolTipText(null);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });
        subpanelCasAddTransaction.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 880, 200, 30));

        btn_trans_new.setBackground(new java.awt.Color(0, 153, 102));
        btn_trans_new.setForeground(new java.awt.Color(255, 255, 255));
        btn_trans_new.setText("NEW TRANSACTION");
        btn_trans_new.setToolTipText(null);
        btn_trans_new.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_trans_newMouseClicked(evt);
            }
        });
        subpanelCasAddTransaction.add(btn_trans_new, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 1400, 180, 30));

        btn_trans_process.setBackground(new java.awt.Color(153, 0, 0));
        btn_trans_process.setForeground(new java.awt.Color(255, 255, 255));
        btn_trans_process.setText("PROCESS");
        btn_trans_process.setToolTipText(null);
        btn_trans_process.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_trans_processMouseClicked(evt);
            }
        });
        subpanelCasAddTransaction.add(btn_trans_process, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 1400, 150, 30));

        btn_trans_clearall.setBackground(new java.awt.Color(0, 102, 153));
        btn_trans_clearall.setForeground(new java.awt.Color(255, 255, 255));
        btn_trans_clearall.setText("CLEAR");
        btn_trans_clearall.setToolTipText(null);
        btn_trans_clearall.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_trans_clearallMouseClicked(evt);
            }
        });
        subpanelCasAddTransaction.add(btn_trans_clearall, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 1400, 70, 30));

        ScrollPaneAddTrans.setViewportView(subpanelCasAddTransaction);

        panelCasDashboard.add(ScrollPaneAddTrans, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        ScrollPaneSimulasi.setBorder(null);
        ScrollPaneSimulasi.setForeground(new java.awt.Color(204, 204, 204));
        ScrollPaneSimulasi.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        ScrollPaneSimulasi.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        ScrollPaneSimulasi.setAutoscrolls(true);
        ScrollPaneSimulasi.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                ScrollPaneSimulasiMouseWheelMoved(evt);
            }
        });
        ScrollPaneSimulasi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ScrollPaneSimulasiMouseEntered(evt);
            }
        });

        subpanelCasSimulasi.setBackground(new java.awt.Color(255, 255, 255));
        subpanelCasSimulasi.setPreferredSize(new java.awt.Dimension(1010, 2100));
        subpanelCasSimulasi.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panel_Trans_List_Simulation.setBackground(new java.awt.Color(243, 243, 243));
        panel_Trans_List_Simulation.setForeground(new java.awt.Color(255, 255, 255));
        panel_Trans_List_Simulation.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTransSimulationCreatedDate1.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransSimulationCreatedDate1.setForeground(new java.awt.Color(128, 128, 128));
        lblTransSimulationCreatedDate1.setText("List Simulation");
        lblTransSimulationCreatedDate1.setToolTipText(null);
        panel_Trans_List_Simulation.add(lblTransSimulationCreatedDate1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        tabel_item_simulation.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item", "Harga"
            }
        ));
        tabel_item_simulation.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane2.setViewportView(tabel_item_simulation);
        if (tabel_item_simulation.getColumnModel().getColumnCount() > 0) {
            tabel_item_simulation.getColumnModel().getColumn(0).setPreferredWidth(400);
            tabel_item_simulation.getColumnModel().getColumn(1).setPreferredWidth(120);
        }

        panel_Trans_List_Simulation.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 420, 330));

        subpanelCasSimulasi.add(panel_Trans_List_Simulation, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 1590, 460, 410));

        panel_Trans_Add_GrandTotal.setBackground(new java.awt.Color(243, 243, 243));
        panel_Trans_Add_GrandTotal.setForeground(new java.awt.Color(255, 255, 255));
        panel_Trans_Add_GrandTotal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_trans_simulation_grandtotal.setFont(new java.awt.Font("Gotham Light", 1, 18)); // NOI18N
        txt_trans_simulation_grandtotal.setForeground(new java.awt.Color(255, 51, 51));
        txt_trans_simulation_grandtotal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txt_trans_simulation_grandtotal.setText("#GrandTotalSimulation");
        txt_trans_simulation_grandtotal.setToolTipText(null);
        txt_trans_simulation_grandtotal.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panel_Trans_Add_GrandTotal.add(txt_trans_simulation_grandtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 260, -1));

        lblTransSimulationGrandTotal.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransSimulationGrandTotal.setForeground(new java.awt.Color(128, 128, 128));
        lblTransSimulationGrandTotal.setText("Grand Total");
        lblTransSimulationGrandTotal.setToolTipText(null);
        panel_Trans_Add_GrandTotal.add(lblTransSimulationGrandTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        btn_trans_lock_simulation.setBackground(new java.awt.Color(153, 0, 0));
        btn_trans_lock_simulation.setForeground(new java.awt.Color(255, 255, 255));
        btn_trans_lock_simulation.setText("LOCK SIMULATION");
        btn_trans_lock_simulation.setToolTipText(null);
        btn_trans_lock_simulation.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_trans_lock_simulationMouseClicked(evt);
            }
        });
        panel_Trans_Add_GrandTotal.add(btn_trans_lock_simulation, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 150, 30));

        subpanelCasSimulasi.add(panel_Trans_Add_GrandTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 1810, 460, 190));

        panel_Trans_Add_Simulation_Detail.setBackground(new java.awt.Color(243, 243, 243));
        panel_Trans_Add_Simulation_Detail.setForeground(new java.awt.Color(255, 255, 255));
        panel_Trans_Add_Simulation_Detail.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTransSimulationCreatedDate.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransSimulationCreatedDate.setForeground(new java.awt.Color(128, 128, 128));
        lblTransSimulationCreatedDate.setText("Created Date");
        lblTransSimulationCreatedDate.setToolTipText(null);
        panel_Trans_Add_Simulation_Detail.add(lblTransSimulationCreatedDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));

        txt_trans_simulation_id.setFont(new java.awt.Font("Gotham Light", 1, 18)); // NOI18N
        txt_trans_simulation_id.setForeground(new java.awt.Color(255, 51, 51));
        txt_trans_simulation_id.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txt_trans_simulation_id.setText("#SimulationID");
        txt_trans_simulation_id.setToolTipText(null);
        txt_trans_simulation_id.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panel_Trans_Add_Simulation_Detail.add(txt_trans_simulation_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 30, 170, -1));

        txt_Trans_Simulation_Admin.setFont(new java.awt.Font("Gotham Light", 1, 18)); // NOI18N
        txt_Trans_Simulation_Admin.setForeground(new java.awt.Color(204, 0, 51));
        txt_Trans_Simulation_Admin.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txt_Trans_Simulation_Admin.setText("CAS-1818002");
        txt_Trans_Simulation_Admin.setToolTipText(null);
        txt_Trans_Simulation_Admin.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        panel_Trans_Add_Simulation_Detail.add(txt_Trans_Simulation_Admin, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 80, 170, -1));

        lblTransSimulationID.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransSimulationID.setForeground(new java.awt.Color(128, 128, 128));
        lblTransSimulationID.setText("Simulation ID");
        lblTransSimulationID.setToolTipText(null);
        panel_Trans_Add_Simulation_Detail.add(lblTransSimulationID, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        lblTransSimulationAdmin.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransSimulationAdmin.setForeground(new java.awt.Color(128, 128, 128));
        lblTransSimulationAdmin.setText("By Admin");
        lblTransSimulationAdmin.setToolTipText(null);
        panel_Trans_Add_Simulation_Detail.add(lblTransSimulationAdmin, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));
        panel_Trans_Add_Simulation_Detail.add(dt_simulation, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 120, 130, 30));

        subpanelCasSimulasi.add(panel_Trans_Add_Simulation_Detail, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 1590, 460, 190));

        panel_Trans_Add_Simulation.setBackground(new java.awt.Color(243, 243, 243));
        panel_Trans_Add_Simulation.setForeground(new java.awt.Color(255, 255, 255));
        panel_Trans_Add_Simulation.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTrans_Sim_BrandProcessor.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTrans_Sim_BrandProcessor.setForeground(java.awt.Color.gray);
        lblTrans_Sim_BrandProcessor.setText("Brand Processor");
        lblTrans_Sim_BrandProcessor.setToolTipText(null);
        panel_Trans_Add_Simulation.add(lblTrans_Sim_BrandProcessor, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        cb_trans_sim_brandprocessor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Brand Processor -", "AMD", "Intel" }));
        cb_trans_sim_brandprocessor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_trans_sim_brandprocessorItemStateChanged(evt);
            }
        });
        panel_Trans_Add_Simulation.add(cb_trans_sim_brandprocessor, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 600, 30));

        lblTrans_Sim_Processor.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTrans_Sim_Processor.setForeground(java.awt.Color.gray);
        lblTrans_Sim_Processor.setText("Processor");
        lblTrans_Sim_Processor.setToolTipText(null);
        panel_Trans_Add_Simulation.add(lblTrans_Sim_Processor, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        cb_trans_sim_processor_amd.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Processor -" }));
        cb_trans_sim_processor_amd.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_trans_sim_processor_amdItemStateChanged(evt);
            }
        });
        panel_Trans_Add_Simulation.add(cb_trans_sim_processor_amd, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 600, 30));

        cb_trans_sim_processor_intel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Processor -" }));
        cb_trans_sim_processor_intel.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_trans_sim_processor_intelItemStateChanged(evt);
            }
        });
        panel_Trans_Add_Simulation.add(cb_trans_sim_processor_intel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 600, 30));

        txt_trans_sim_price_networking.setEnabled(false);
        panel_Trans_Add_Simulation.add(txt_trans_sim_price_networking, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 1330, 190, 30));

        lblTrans_Sim_Motherboard.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTrans_Sim_Motherboard.setForeground(java.awt.Color.gray);
        lblTrans_Sim_Motherboard.setText("Motherboard");
        lblTrans_Sim_Motherboard.setToolTipText(null);
        panel_Trans_Add_Simulation.add(lblTrans_Sim_Motherboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, -1, -1));

        cb_trans_sim_motherboard.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Motherboard -" }));
        cb_trans_sim_motherboard.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_trans_sim_motherboardItemStateChanged(evt);
            }
        });
        panel_Trans_Add_Simulation.add(cb_trans_sim_motherboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 600, 30));

        lblTrans_Sim_SSD.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTrans_Sim_SSD.setForeground(java.awt.Color.gray);
        lblTrans_Sim_SSD.setText("Solid State Drive");
        lblTrans_Sim_SSD.setToolTipText(null);
        panel_Trans_Add_Simulation.add(lblTrans_Sim_SSD, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, -1, -1));

        cb_trans_sim_ssd.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose SSD -" }));
        cb_trans_sim_ssd.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_trans_sim_ssdItemStateChanged(evt);
            }
        });
        panel_Trans_Add_Simulation.add(cb_trans_sim_ssd, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 600, 30));

        cb_trans_sim_hdd.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose HardDisk -" }));
        cb_trans_sim_hdd.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_trans_sim_hddItemStateChanged(evt);
            }
        });
        panel_Trans_Add_Simulation.add(cb_trans_sim_hdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 600, 30));

        lblTrans_Sim_HDD.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTrans_Sim_HDD.setForeground(java.awt.Color.gray);
        lblTrans_Sim_HDD.setText("HardDisk");
        lblTrans_Sim_HDD.setToolTipText(null);
        panel_Trans_Add_Simulation.add(lblTrans_Sim_HDD, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, -1, -1));

        lblTrans_Sim_RAM.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTrans_Sim_RAM.setForeground(java.awt.Color.gray);
        lblTrans_Sim_RAM.setText("Memory RAM");
        lblTrans_Sim_RAM.setToolTipText(null);
        panel_Trans_Add_Simulation.add(lblTrans_Sim_RAM, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 420, -1, -1));

        cb_trans_sim_ram.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose RAM -" }));
        cb_trans_sim_ram.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_trans_sim_ramItemStateChanged(evt);
            }
        });
        panel_Trans_Add_Simulation.add(cb_trans_sim_ram, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 450, 600, 30));

        lblTrans_Sim_VGA.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTrans_Sim_VGA.setForeground(java.awt.Color.gray);
        lblTrans_Sim_VGA.setText("VGA");
        lblTrans_Sim_VGA.setToolTipText(null);
        panel_Trans_Add_Simulation.add(lblTrans_Sim_VGA, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 500, -1, -1));

        cb_trans_sim_vga.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose VGA -" }));
        cb_trans_sim_vga.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_trans_sim_vgaItemStateChanged(evt);
            }
        });
        panel_Trans_Add_Simulation.add(cb_trans_sim_vga, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 530, 600, 30));

        cb_trans_sim_casing.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Casing -" }));
        cb_trans_sim_casing.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_trans_sim_casingItemStateChanged(evt);
            }
        });
        panel_Trans_Add_Simulation.add(cb_trans_sim_casing, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 610, 600, 30));

        lblTrans_Sim_Casing.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTrans_Sim_Casing.setForeground(java.awt.Color.gray);
        lblTrans_Sim_Casing.setText("Casing");
        lblTrans_Sim_Casing.setToolTipText(null);
        panel_Trans_Add_Simulation.add(lblTrans_Sim_Casing, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 580, -1, -1));

        cb_trans_sim_psu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose PSU -" }));
        cb_trans_sim_psu.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_trans_sim_psuItemStateChanged(evt);
            }
        });
        panel_Trans_Add_Simulation.add(cb_trans_sim_psu, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 690, 600, 30));

        lblTrans_Sim_PSU.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTrans_Sim_PSU.setForeground(java.awt.Color.gray);
        lblTrans_Sim_PSU.setText("PSU");
        lblTrans_Sim_PSU.setToolTipText(null);
        panel_Trans_Add_Simulation.add(lblTrans_Sim_PSU, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 660, -1, -1));

        lblTrans_Sim_LCD.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTrans_Sim_LCD.setForeground(java.awt.Color.gray);
        lblTrans_Sim_LCD.setText("LCD");
        lblTrans_Sim_LCD.setToolTipText(null);
        panel_Trans_Add_Simulation.add(lblTrans_Sim_LCD, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 740, -1, -1));

        cb_trans_sim_lcd.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose LCD -" }));
        cb_trans_sim_lcd.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_trans_sim_lcdItemStateChanged(evt);
            }
        });
        panel_Trans_Add_Simulation.add(cb_trans_sim_lcd, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 770, 600, 30));

        lblTrans_Sim_Optical.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTrans_Sim_Optical.setForeground(java.awt.Color.gray);
        lblTrans_Sim_Optical.setText("Optical");
        lblTrans_Sim_Optical.setToolTipText(null);
        panel_Trans_Add_Simulation.add(lblTrans_Sim_Optical, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 820, -1, -1));

        cb_trans_sim_optical.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Optical -" }));
        cb_trans_sim_optical.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_trans_sim_opticalItemStateChanged(evt);
            }
        });
        panel_Trans_Add_Simulation.add(cb_trans_sim_optical, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 850, 600, 30));

        cb_trans_sim_keyboard.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Keyboard -" }));
        cb_trans_sim_keyboard.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_trans_sim_keyboardItemStateChanged(evt);
            }
        });
        panel_Trans_Add_Simulation.add(cb_trans_sim_keyboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 930, 600, 30));

        lblTrans_Sim_Keyboard.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTrans_Sim_Keyboard.setForeground(java.awt.Color.gray);
        lblTrans_Sim_Keyboard.setText("Keyboard");
        lblTrans_Sim_Keyboard.setToolTipText(null);
        panel_Trans_Add_Simulation.add(lblTrans_Sim_Keyboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 900, -1, -1));

        cb_trans_sim_speaker.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Speaker -" }));
        cb_trans_sim_speaker.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_trans_sim_speakerItemStateChanged(evt);
            }
        });
        panel_Trans_Add_Simulation.add(cb_trans_sim_speaker, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1010, 600, 30));

        lblTrans_Sim_Speaker.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTrans_Sim_Speaker.setForeground(java.awt.Color.gray);
        lblTrans_Sim_Speaker.setText("Speaker");
        lblTrans_Sim_Speaker.setToolTipText(null);
        panel_Trans_Add_Simulation.add(lblTrans_Sim_Speaker, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 980, -1, -1));

        lblTrans_Sim_Headset.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTrans_Sim_Headset.setForeground(java.awt.Color.gray);
        lblTrans_Sim_Headset.setText("Headset");
        lblTrans_Sim_Headset.setToolTipText(null);
        panel_Trans_Add_Simulation.add(lblTrans_Sim_Headset, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1060, -1, -1));

        cb_trans_sim_headset.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Headset -" }));
        cb_trans_sim_headset.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_trans_sim_headsetItemStateChanged(evt);
            }
        });
        panel_Trans_Add_Simulation.add(cb_trans_sim_headset, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1090, 600, 30));

        lblTrans_Sim_CPUCooler.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTrans_Sim_CPUCooler.setForeground(java.awt.Color.gray);
        lblTrans_Sim_CPUCooler.setText("CPU Cooler");
        lblTrans_Sim_CPUCooler.setToolTipText(null);
        panel_Trans_Add_Simulation.add(lblTrans_Sim_CPUCooler, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1140, -1, -1));

        cb_trans_sim_cpucooler.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose CPU Cooler -" }));
        cb_trans_sim_cpucooler.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_trans_sim_cpucoolerItemStateChanged(evt);
            }
        });
        panel_Trans_Add_Simulation.add(cb_trans_sim_cpucooler, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1170, 600, 30));

        cb_trans_sim_coolerfan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Cooler Fan -" }));
        cb_trans_sim_coolerfan.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_trans_sim_coolerfanItemStateChanged(evt);
            }
        });
        panel_Trans_Add_Simulation.add(cb_trans_sim_coolerfan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1250, 600, 30));

        lblTrans_Sim_CoolerFan.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTrans_Sim_CoolerFan.setForeground(java.awt.Color.gray);
        lblTrans_Sim_CoolerFan.setText("Cooler Fan");
        lblTrans_Sim_CoolerFan.setToolTipText(null);
        panel_Trans_Add_Simulation.add(lblTrans_Sim_CoolerFan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1220, -1, -1));

        txt_trans_sim_price_processor.setEnabled(false);
        panel_Trans_Add_Simulation.add(txt_trans_sim_price_processor, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 130, 190, 30));

        txt_trans_sim_price_motherboard.setEnabled(false);
        panel_Trans_Add_Simulation.add(txt_trans_sim_price_motherboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 210, 190, 30));

        txt_trans_sim_price_ssd.setEnabled(false);
        panel_Trans_Add_Simulation.add(txt_trans_sim_price_ssd, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 290, 190, 30));

        txt_trans_sim_price_hdd.setEnabled(false);
        panel_Trans_Add_Simulation.add(txt_trans_sim_price_hdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 370, 190, 30));

        txt_trans_sim_price_ram.setEnabled(false);
        panel_Trans_Add_Simulation.add(txt_trans_sim_price_ram, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 450, 190, 30));

        txt_trans_sim_price_vga.setEnabled(false);
        panel_Trans_Add_Simulation.add(txt_trans_sim_price_vga, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 530, 190, 30));

        txt_trans_sim_price_casing.setEnabled(false);
        panel_Trans_Add_Simulation.add(txt_trans_sim_price_casing, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 610, 190, 30));

        txt_trans_sim_price_psu.setEnabled(false);
        panel_Trans_Add_Simulation.add(txt_trans_sim_price_psu, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 690, 190, 30));

        txt_trans_sim_price_lcd.setEnabled(false);
        panel_Trans_Add_Simulation.add(txt_trans_sim_price_lcd, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 770, 190, 30));

        txt_trans_sim_price_optical.setEnabled(false);
        panel_Trans_Add_Simulation.add(txt_trans_sim_price_optical, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 850, 190, 30));

        txt_trans_sim_price_keyboard.setEnabled(false);
        panel_Trans_Add_Simulation.add(txt_trans_sim_price_keyboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 930, 190, 30));

        txt_trans_sim_price_speaker.setEnabled(false);
        panel_Trans_Add_Simulation.add(txt_trans_sim_price_speaker, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 1010, 190, 30));

        txt_trans_sim_price_headset.setEnabled(false);
        panel_Trans_Add_Simulation.add(txt_trans_sim_price_headset, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 1090, 190, 30));

        txt_trans_sim_price_cpucooler.setEnabled(false);
        panel_Trans_Add_Simulation.add(txt_trans_sim_price_cpucooler, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 1170, 190, 30));

        lblTrans_Sim_Networking.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTrans_Sim_Networking.setForeground(java.awt.Color.gray);
        lblTrans_Sim_Networking.setText("Networking");
        lblTrans_Sim_Networking.setToolTipText(null);
        panel_Trans_Add_Simulation.add(lblTrans_Sim_Networking, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1300, -1, -1));

        cb_trans_sim_networking.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Networking -" }));
        cb_trans_sim_networking.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_trans_sim_networkingItemStateChanged(evt);
            }
        });
        panel_Trans_Add_Simulation.add(cb_trans_sim_networking, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1330, 600, 30));

        txt_trans_sim_price_coolerfan.setEnabled(false);
        panel_Trans_Add_Simulation.add(txt_trans_sim_price_coolerfan, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 1250, 190, 30));

        subpanelCasSimulasi.add(panel_Trans_Add_Simulation, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 950, 1410));

        jSeparator79.setBackground(new java.awt.Color(241, 241, 241));
        jSeparator79.setForeground(new java.awt.Color(241, 241, 241));
        subpanelCasSimulasi.add(jSeparator79, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 940, 20));

        lblTransSimulationTitle.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblTransSimulationTitle.setForeground(new java.awt.Color(102, 102, 102));
        lblTransSimulationTitle.setText("SIMULATION");
        subpanelCasSimulasi.add(lblTransSimulationTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        lblTransSimulationSubTitle.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblTransSimulationSubTitle.setForeground(new java.awt.Color(153, 153, 153));
        lblTransSimulationSubTitle.setText("Cashier");
        subpanelCasSimulasi.add(lblTransSimulationSubTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 53, -1, -1));

        lblTransSimulationInputDataItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconInputDataItem_1.png"))); // NOI18N
        subpanelCasSimulasi.add(lblTransSimulationInputDataItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        btn_trans_new_sim.setBackground(new java.awt.Color(0, 153, 102));
        btn_trans_new_sim.setForeground(new java.awt.Color(255, 255, 255));
        btn_trans_new_sim.setText("NEW SIMULATION");
        btn_trans_new_sim.setToolTipText(null);
        btn_trans_new_sim.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_trans_new_simMouseClicked(evt);
            }
        });
        subpanelCasSimulasi.add(btn_trans_new_sim, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 2030, 160, 30));

        btn_trans_clearall_sim.setBackground(new java.awt.Color(0, 102, 153));
        btn_trans_clearall_sim.setForeground(new java.awt.Color(255, 255, 255));
        btn_trans_clearall_sim.setText("CLEAR");
        btn_trans_clearall_sim.setToolTipText(null);
        btn_trans_clearall_sim.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_trans_clearall_simMouseClicked(evt);
            }
        });
        subpanelCasSimulasi.add(btn_trans_clearall_sim, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 2030, 130, 30));

        btn_trans_process_sim.setBackground(new java.awt.Color(153, 0, 0));
        btn_trans_process_sim.setForeground(new java.awt.Color(255, 255, 255));
        btn_trans_process_sim.setText("PROCESS");
        btn_trans_process_sim.setToolTipText(null);
        btn_trans_process_sim.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_trans_process_simMouseClicked(evt);
            }
        });
        subpanelCasSimulasi.add(btn_trans_process_sim, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 2030, 150, 30));

        ScrollPaneSimulasi.setViewportView(subpanelCasSimulasi);

        panelCasDashboard.add(ScrollPaneSimulasi, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        subpanelCasListItem.setBackground(new java.awt.Color(255, 255, 255));
        subpanelCasListItem.setPreferredSize(new java.awt.Dimension(1010, 538));
        subpanelCasListItem.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_searchItemName.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_searchItemName.setText("Search Item");
        subpanelCasListItem.add(lbl_searchItemName, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, -1));

        lbl_searchItemBrand.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_searchItemBrand.setText("Search Item Brand");
        subpanelCasListItem.add(lbl_searchItemBrand, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 190, -1, -1));

        lbl_searchItemCategory.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_searchItemCategory.setText("Search Item Category");
        subpanelCasListItem.add(lbl_searchItemCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 100, -1, -1));

        lbl_searchItemID.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_searchItemID.setText("Search ID Item");
        subpanelCasListItem.add(lbl_searchItemID, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        jPanel9.setBackground(new java.awt.Color(241, 241, 241));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        jPanel9.add(txt_trans_searchitemByName, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 10, 430, 40));

        subpanelCasListItem.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 460, 60));

        jPanel10.setBackground(new java.awt.Color(241, 241, 241));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_trans_searchitemByBrand.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        txt_trans_searchitemByBrand.setForeground(new java.awt.Color(204, 204, 204));
        txt_trans_searchitemByBrand.setText("Press enter to search ...");
        txt_trans_searchitemByBrand.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt_trans_searchitemByBrand.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_trans_searchitemByBrandMouseClicked(evt);
            }
        });
        txt_trans_searchitemByBrand.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_trans_searchitemByBrandKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_trans_searchitemByBrandKeyTyped(evt);
            }
        });
        jPanel10.add(txt_trans_searchitemByBrand, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 10, 430, 40));

        subpanelCasListItem.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 210, 460, 60));

        jPanel11.setBackground(new java.awt.Color(241, 241, 241));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        jPanel11.add(txt_trans_searchitemByID, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 10, 430, 40));

        subpanelCasListItem.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 460, 60));

        jPanel12.setBackground(new java.awt.Color(241, 241, 241));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_trans_searchitemByCategory.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        txt_trans_searchitemByCategory.setForeground(new java.awt.Color(204, 204, 204));
        txt_trans_searchitemByCategory.setText("Press enter to search ...");
        txt_trans_searchitemByCategory.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt_trans_searchitemByCategory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_trans_searchitemByCategoryMouseClicked(evt);
            }
        });
        txt_trans_searchitemByCategory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_trans_searchitemByCategoryKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_trans_searchitemByCategoryKeyTyped(evt);
            }
        });
        jPanel12.add(txt_trans_searchitemByCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 10, 430, 40));

        subpanelCasListItem.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 120, 460, 60));

        lblDataKaryawanTitel2.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblDataKaryawanTitel2.setForeground(java.awt.Color.gray);
        lblDataKaryawanTitel2.setText("LIST ITEM");
        subpanelCasListItem.add(lblDataKaryawanTitel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        lbl_management2.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_management2.setForeground(java.awt.Color.gray);
        lbl_management2.setText("Cashier");
        subpanelCasListItem.add(lbl_management2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 53, -1, -1));

        jSeparator18.setForeground(new java.awt.Color(241, 241, 241));
        subpanelCasListItem.add(jSeparator18, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 950, 20));

        tabelDataKaryawanSP2.setBackground(new java.awt.Color(241, 241, 241));

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
        tabelDataKaryawanSP2.setViewportView(tabelItemList);
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

        subpanelCasListItem.add(tabelDataKaryawanSP2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, 950, 210));

        lbl_daftarkyw2.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_daftarkyw2.setForeground(java.awt.Color.gray);
        lbl_daftarkyw2.setText("List Item ");
        subpanelCasListItem.add(lbl_daftarkyw2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 285, -1, -1));

        panelCasDashboard.add(subpanelCasListItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        subpanelCasErrorReport.setBackground(new java.awt.Color(255, 255, 255));
        subpanelCasErrorReport.setPreferredSize(new java.awt.Dimension(1010, 538));
        subpanelCasErrorReport.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSaveError_cas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconSave.png"))); // NOI18N
        btnSaveError_cas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSaveError_cas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveError_casMouseClicked(evt);
            }
        });
        subpanelCasErrorReport.add(btnSaveError_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 410, -1, -1));

        btnClearError_cas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconClear.png"))); // NOI18N
        btnClearError_cas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClearError_cas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClearError_casMouseClicked(evt);
            }
        });
        subpanelCasErrorReport.add(btnClearError_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 410, -1, -1));

        lbl_list_error_cas.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_list_error_cas.setForeground(java.awt.Color.gray);
        lbl_list_error_cas.setText("Report List");
        subpanelCasErrorReport.add(lbl_list_error_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 100, -1, -1));

        cb_error_status_cas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Status -", "Currently Added", "On Progress", "Done" }));
        subpanelCasErrorReport.add(cb_error_status_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 410, 180, -1));

        lbl_error_date_cas.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_date_cas.setText("Date");
        subpanelCasErrorReport.add(lbl_error_date_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, -1, -1));
        subpanelCasErrorReport.add(dtError_Date_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 360, 180, -1));

        lbl_error_status_cas.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_status_cas.setText("Status");
        subpanelCasErrorReport.add(lbl_error_status_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 410, -1, -1));

        txt_error_desc_cas.setBackground(new java.awt.Color(240, 240, 240));
        txt_error_desc_cas.setColumns(20);
        txt_error_desc_cas.setRows(5);
        subpanelCasErrorReport.add(txt_error_desc_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 240, 180, -1));

        lbl_error_desc_cas.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_desc_cas.setText("Description");
        subpanelCasErrorReport.add(lbl_error_desc_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, -1, -1));
        subpanelCasErrorReport.add(txt_error_title_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 188, 180, -1));

        lbl_error_title_cas.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_title_cas.setText("Title");
        subpanelCasErrorReport.add(lbl_error_title_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, -1));

        txt_error_id_cas.setEnabled(false);
        subpanelCasErrorReport.add(txt_error_id_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 180, -1));

        lbl_error_id_cas.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_id_cas.setText("Report ID");
        subpanelCasErrorReport.add(lbl_error_id_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));

        lblErrorReportTitleCas.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblErrorReportTitleCas.setForeground(java.awt.Color.gray);
        lblErrorReportTitleCas.setText("ERROR REPORT");
        subpanelCasErrorReport.add(lblErrorReportTitleCas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        lblErrorReportCasSubTitle1.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblErrorReportCasSubTitle1.setForeground(java.awt.Color.gray);
        lblErrorReportCasSubTitle1.setText("Add Report");
        subpanelCasErrorReport.add(lblErrorReportCasSubTitle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        jSeparator14.setForeground(new java.awt.Color(241, 241, 241));
        subpanelCasErrorReport.add(jSeparator14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 950, 20));

        tabelDaftarReportSP1.setBackground(new java.awt.Color(241, 241, 241));
        tabelDaftarReportSP1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        tabelDaftarReportSP1.setAutoscrolls(true);
        tabelDaftarReportSP1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tabelDaftarError_Cas.setBackground(new java.awt.Color(240, 240, 240));
        tabelDaftarError_Cas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabelDaftarError_Cas.setAutoscrolls(false);
        tabelDaftarError_Cas.setGridColor(new java.awt.Color(241, 241, 241));
        tabelDaftarError_Cas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelDaftarError_CasMouseClicked(evt);
            }
        });
        tabelDaftarReportSP1.setViewportView(tabelDaftarError_Cas);

        subpanelCasErrorReport.add(tabelDaftarReportSP1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 130, 580, 260));

        lblErrorReportCasSubTitle.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblErrorReportCasSubTitle.setForeground(java.awt.Color.gray);
        lblErrorReportCasSubTitle.setText("Cashier");
        subpanelCasErrorReport.add(lblErrorReportCasSubTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(335, 53, -1, -1));

        btn_delete_error_cas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/btnDelete.png"))); // NOI18N
        btn_delete_error_cas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_delete_error_cas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_delete_error_casMouseClicked(evt);
            }
        });
        subpanelCasErrorReport.add(btn_delete_error_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 87, -1, -1));

        panelCasDashboard.add(subpanelCasErrorReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        subpanelCasAddDistributor.setBackground(new java.awt.Color(255, 255, 255));
        subpanelCasAddDistributor.setPreferredSize(new java.awt.Dimension(1010, 538));
        subpanelCasAddDistributor.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblDistributor.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblDistributor.setForeground(java.awt.Color.gray);
        lblDistributor.setText("ADD DISTRIBUTOR");
        subpanelCasAddDistributor.add(lblDistributor, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        lblDistributortSubTitle.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblDistributortSubTitle.setForeground(java.awt.Color.gray);
        lblDistributortSubTitle.setText("Cashier");
        subpanelCasAddDistributor.add(lblDistributortSubTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 53, -1, -1));

        jSeparator16.setForeground(new java.awt.Color(241, 241, 241));
        subpanelCasAddDistributor.add(jSeparator16, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 950, 20));

        lblAddDistributorSubTitle.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblAddDistributorSubTitle.setForeground(java.awt.Color.gray);
        lblAddDistributorSubTitle.setText("Add Distributor");
        subpanelCasAddDistributor.add(lblAddDistributorSubTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        lbl_id_distributor.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_id_distributor.setText("ID Distributor");
        subpanelCasAddDistributor.add(lbl_id_distributor, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));

        txt_distributor_id.setEnabled(false);
        subpanelCasAddDistributor.add(txt_distributor_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 130, 180, 30));

        lbl_distributor_name.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_distributor_name.setText("Name");
        subpanelCasAddDistributor.add(lbl_distributor_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, -1));
        subpanelCasAddDistributor.add(txt_distributor_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 180, 180, 30));

        lbl_distributor_address.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_distributor_address.setText("Address");
        subpanelCasAddDistributor.add(lbl_distributor_address, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, -1, -1));

        txt_distributor_address.setBackground(new java.awt.Color(240, 240, 240));
        txt_distributor_address.setColumns(20);
        txt_distributor_address.setRows(5);
        subpanelCasAddDistributor.add(txt_distributor_address, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 230, 180, -1));

        lbl_distributor_phone.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_distributor_phone.setText("Phone Number");
        subpanelCasAddDistributor.add(lbl_distributor_phone, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, -1, -1));
        subpanelCasAddDistributor.add(txt_distributor_phone, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 340, 180, 30));

        lbl_distributor_mail.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_distributor_mail.setText("E-Mail");
        subpanelCasAddDistributor.add(lbl_distributor_mail, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 400, -1, -1));
        subpanelCasAddDistributor.add(txt_distributor_email, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 390, 180, 30));

        lbl_distributor_desc.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_distributor_desc.setText("Description");
        subpanelCasAddDistributor.add(lbl_distributor_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 440, -1, -1));

        cb_distributor_description.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- ChooseDescription -", "Official Distributor", "Customer" }));
        subpanelCasAddDistributor.add(cb_distributor_description, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 440, 180, -1));

        lbl_distributor_status.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_distributor_status.setText("Status");
        subpanelCasAddDistributor.add(lbl_distributor_status, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 480, -1, -1));

        cb_distributor_status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Status -", "Active", "Inactive" }));
        subpanelCasAddDistributor.add(cb_distributor_status, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 480, 180, -1));

        lblListDistributor.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblListDistributor.setForeground(java.awt.Color.gray);
        lblListDistributor.setText("List Distributor");
        subpanelCasAddDistributor.add(lblListDistributor, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 100, -1, -1));

        tabelDataDepartmentSP1.setBackground(new java.awt.Color(241, 241, 241));

        tabelDataDistributor.setBackground(new java.awt.Color(240, 240, 240));
        tabelDataDistributor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabelDataDistributor.setGridColor(new java.awt.Color(241, 241, 241));
        tabelDataDistributor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelDataDistributorMouseClicked(evt);
            }
        });
        tabelDataDepartmentSP1.setViewportView(tabelDataDistributor);

        subpanelCasAddDistributor.add(tabelDataDepartmentSP1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 130, 580, 260));

        btn_active_distributor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/btnActive.png"))); // NOI18N
        btn_active_distributor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_active_distributor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_active_distributorMouseClicked(evt);
            }
        });
        subpanelCasAddDistributor.add(btn_active_distributor, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 87, -1, -1));

        btn_deactive_distributor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/btnDeactive3.png"))); // NOI18N
        btn_deactive_distributor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_deactive_distributor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_deactive_distributorMouseClicked(evt);
            }
        });
        subpanelCasAddDistributor.add(btn_deactive_distributor, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 87, -1, -1));

        btnSaveDistributor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconSave.png"))); // NOI18N
        btnSaveDistributor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSaveDistributor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveDistributorMouseClicked(evt);
            }
        });
        subpanelCasAddDistributor.add(btnSaveDistributor, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 410, -1, -1));

        btnClearDistributor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconClear.png"))); // NOI18N
        btnClearDistributor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClearDistributor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClearDistributorMouseClicked(evt);
            }
        });
        subpanelCasAddDistributor.add(btnClearDistributor, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 410, -1, -1));

        panelCasDashboard.add(subpanelCasAddDistributor, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));
        panelCasDashboard.add(jSeparator48, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 510, 170, 10));

        menuCasErrorReport.setBackground(new java.awt.Color(241, 241, 241));
        menuCasErrorReport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuCasErrorReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuCasErrorReportMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuCasErrorReportMouseExited(evt);
            }
        });
        menuCasErrorReport.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconCasErrorReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/IconErrorReport.png"))); // NOI18N
        iconCasErrorReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconCasErrorReportMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconCasErrorReportMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconCasErrorReportMouseExited(evt);
            }
        });
        menuCasErrorReport.add(iconCasErrorReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        panelCasDashboard.add(menuCasErrorReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 515, 170, 30));
        panelCasDashboard.add(jSeparator21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 470, 170, 10));

        menuCasSimulation.setBackground(new java.awt.Color(241, 241, 241));
        menuCasSimulation.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuCasSimulation.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuCasSimulationMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuCasSimulationMouseExited(evt);
            }
        });
        menuCasSimulation.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconCasSimulation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconSimulation.png"))); // NOI18N
        iconCasSimulation.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconCasSimulationMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconCasSimulationMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconCasSimulationMouseExited(evt);
            }
        });
        menuCasSimulation.add(iconCasSimulation, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        panelCasDashboard.add(menuCasSimulation, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 475, 170, 30));
        panelCasDashboard.add(jSeparator20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 550, 170, 10));

        menuCasDataTransaction.setBackground(new java.awt.Color(241, 241, 241));
        menuCasDataTransaction.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuCasDataTransaction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuCasDataTransactionMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuCasDataTransactionMouseExited(evt);
            }
        });
        menuCasDataTransaction.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconCasDataTransaction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconDataTransaction.png"))); // NOI18N
        iconCasDataTransaction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconCasDataTransactionMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconCasDataTransactionMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconCasDataTransactionMouseExited(evt);
            }
        });
        menuCasDataTransaction.add(iconCasDataTransaction, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 7, -1, -1));

        panelCasDashboard.add(menuCasDataTransaction, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 435, 170, 30));
        panelCasDashboard.add(jSeparator22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 170, 10));

        menuCasListItem.setBackground(new java.awt.Color(241, 241, 241));
        menuCasListItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuCasListItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuCasListItemMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuCasListItemMouseExited(evt);
            }
        });
        menuCasListItem.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconCasListItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconListItem.png"))); // NOI18N
        iconCasListItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconCasListItemMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconCasListItemMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconCasListItemMouseExited(evt);
            }
        });
        menuCasListItem.add(iconCasListItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        panelCasDashboard.add(menuCasListItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 395, 170, 30));
        panelCasDashboard.add(jSeparator23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 170, 10));

        menuCasAddCustomer.setBackground(new java.awt.Color(241, 241, 241));
        menuCasAddCustomer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuCasAddCustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuCasAddCustomerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuCasAddCustomerMouseExited(evt);
            }
        });
        menuCasAddCustomer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconCasAddCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconAddCustomer.png"))); // NOI18N
        iconCasAddCustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconCasAddCustomerMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconCasAddCustomerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconCasAddCustomerMouseExited(evt);
            }
        });
        menuCasAddCustomer.add(iconCasAddCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 7, -1, -1));

        panelCasDashboard.add(menuCasAddCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 355, 170, 30));
        panelCasDashboard.add(jSeparator24, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 170, 10));

        menuCasAddTransaction.setBackground(new java.awt.Color(241, 241, 241));
        menuCasAddTransaction.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuCasAddTransaction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuCasAddTransactionMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuCasAddTransactionMouseExited(evt);
            }
        });
        menuCasAddTransaction.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconCasAddTrans.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconAddTransaction.png"))); // NOI18N
        iconCasAddTrans.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconCasAddTransMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconCasAddTransMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconCasAddTransMouseExited(evt);
            }
        });
        menuCasAddTransaction.add(iconCasAddTrans, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 9, -1, -1));

        panelCasDashboard.add(menuCasAddTransaction, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 315, 170, 30));
        panelCasDashboard.add(jSeparator25, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 170, 10));

        menuCasDashboard.setBackground(new java.awt.Color(241, 241, 241));
        menuCasDashboard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuCasDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuCasDashboardMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuCasDashboardMouseExited(evt);
            }
        });
        menuCasDashboard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconCasDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/IconDashboard.png"))); // NOI18N
        iconCasDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconCasDashboardMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconCasDashboardMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconCasDashboardMouseExited(evt);
            }
        });
        menuCasDashboard.add(iconCasDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 7, -1, -1));

        panelCasDashboard.add(menuCasDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 275, 170, 30));
        panelCasDashboard.add(jSeparator26, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 170, 10));

        btnChangeStateCas.setText("Change State");
        btnChangeStateCas.setToolTipText("Click to Change State");
        btnChangeStateCas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnChangeStateCas.setMaximumSize(new java.awt.Dimension(73, 23));
        btnChangeStateCas.setMinimumSize(new java.awt.Dimension(74, 23));
        btnChangeStateCas.setPreferredSize(new java.awt.Dimension(78, 23));
        btnChangeStateCas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnChangeStateCasMouseClicked(evt);
            }
        });
        panelCasDashboard.add(btnChangeStateCas, new org.netbeans.lib.awtextra.AbsoluteConstraints(83, 230, 100, -1));

        btnHideIPCas.setText("Hide IP");
        btnHideIPCas.setToolTipText("Click to Hide IP");
        btnHideIPCas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHideIPCas.setMaximumSize(new java.awt.Dimension(73, 23));
        btnHideIPCas.setMinimumSize(new java.awt.Dimension(74, 23));
        btnHideIPCas.setPreferredSize(new java.awt.Dimension(73, 23));
        btnHideIPCas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHideIPCasMouseClicked(evt);
            }
        });
        panelCasDashboard.add(btnHideIPCas, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 230, -1, -1));

        btnShowIPCas.setText("Show IP");
        btnShowIPCas.setToolTipText("Click to show IP");
        btnShowIPCas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnShowIPCas.setMaximumSize(new java.awt.Dimension(73, 23));
        btnShowIPCas.setMinimumSize(new java.awt.Dimension(73, 23));
        btnShowIPCas.setPreferredSize(new java.awt.Dimension(73, 23));
        btnShowIPCas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnShowIPCasMouseClicked(evt);
            }
        });
        panelCasDashboard.add(btnShowIPCas, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 230, -1, -1));

        txtServerTimeCas.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        txtServerTimeCas.setText("SERVER TIME");
        panelCasDashboard.add(txtServerTimeCas, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 693, -1, -1));

        txt_stateBigCas.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        txt_stateBigCas.setText("SERVER STATE");
        panelCasDashboard.add(txt_stateBigCas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 693, -1, -1));

        lblStateBigCas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OnlineState.png"))); // NOI18N
        panelCasDashboard.add(lblStateBigCas, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 693, -1, -1));

        lblStateCas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OnlineState.png"))); // NOI18N
        panelCasDashboard.add(lblStateCas, new org.netbeans.lib.awtextra.AbsoluteConstraints(78, 208, -1, -1));

        txt_stateCas.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        txt_stateCas.setText("Online");
        panelCasDashboard.add(txt_stateCas, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 210, -1, -1));

        jLabel12.setFont(new java.awt.Font("Gotham Medium", 1, 11)); // NOI18N
        jLabel12.setText("Keyko");
        panelCasDashboard.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(595, 695, -1, -1));

        jLabel13.setFont(new java.awt.Font("Gotham Light", 2, 11)); // NOI18N
        jLabel13.setText("powered by");
        panelCasDashboard.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 695, -1, -1));

        jLabel14.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        jLabel14.setText("NOB Tech - ");
        panelCasDashboard.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(465, 695, -1, -1));

        txtCasNama.setFont(new java.awt.Font("Gotham Medium", 1, 12)); // NOI18N
        txtCasNama.setText("Cashier");
        panelCasDashboard.add(txtCasNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(79, 190, -1, -1));

        jLabel4.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        jLabel4.setText("Hello, ");
        panelCasDashboard.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 170, -1, -1));

        iconAdminCas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/AdminLogo.png"))); // NOI18N
        panelCasDashboard.add(iconAdminCas, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        bgCashier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/cashier/BgPanelCashier.png"))); // NOI18N
        panelCasDashboard.add(bgCashier, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
        panelCasDashboard.add(jSeparator47, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 510, 170, 10));

        getContentPane().add(panelCasDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 721));

        panelKilledbyServer.setBackground(new java.awt.Color(255, 255, 255));
        panelKilledbyServer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/cashier/CasServerDisconnect.png"))); // NOI18N
        panelKilledbyServer.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelKilledbyServer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 720));

        panelCasLogin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelCasLogin.add(txtUsernameCas, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 380, 230, 40));
        panelCasLogin.add(txtPasswordCas, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 470, 230, 40));

        btnLoginCas.setText("LOGIN");
        btnLoginCas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLoginCasMouseClicked(evt);
            }
        });
        panelCasLogin.add(btnLoginCas, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 550, -1, -1));

        btnResetCas.setText("CANCEL");
        btnResetCas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnResetCasMouseClicked(evt);
            }
        });
        panelCasLogin.add(btnResetCas, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 550, -1, -1));

        panelLoginCas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/cashier/paenelLoginCas.png"))); // NOI18N
        panelCasLogin.add(panelLoginCas, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 120, -1, -1));

        bgClientCashierLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/cashier/BgLoginCashier.png"))); // NOI18N
        panelCasLogin.add(bgClientCashierLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelCasLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 721));

        panelCasLandingPage.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconHome2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconHomeGreen.png"))); // NOI18N
        iconHome2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconHome2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconHome2MouseClicked(evt);
            }
        });
        panelCasLandingPage.add(iconHome2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        bgLogoCashier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/cashier/LogoCashier_1.png"))); // NOI18N
        bgLogoCashier.setToolTipText("");
        bgLogoCashier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bgLogoCashier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bgLogoCashierMouseClicked(evt);
            }
        });
        panelCasLandingPage.add(bgLogoCashier, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 200, -1, -1));

        bgClientCashier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/cashier/BgLandingPageCashier.png"))); // NOI18N
        panelCasLandingPage.add(bgClientCashier, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelCasLandingPage, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 720));

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

        panelFrontOffice2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconSIMFO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/btnSIM.png"))); // NOI18N
        iconSIMFO.setToolTipText("");
        iconSIMFO.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconSIMFO.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconSIMFOMouseClicked(evt);
            }
        });
        panelFrontOffice2.add(iconSIMFO, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 120, -1, -1));

        iconCashier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconFrontOfficeCashier.png"))); // NOI18N
        iconCashier.setToolTipText("");
        iconCashier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconCashier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconCashierMouseClicked(evt);
            }
        });
        panelFrontOffice2.add(iconCashier, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 430, -1, -1));

        iconFOBig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/landingpage/iconFrontOffice.png"))); // NOI18N
        iconFOBig.setToolTipText("");
        iconFOBig.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconFOBig.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconFOBigMouseClicked(evt);
            }
        });
        panelFrontOffice2.add(iconFOBig, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 170, -1, -1));

        bgFrontOffice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/landingpage/bgDepartment.png"))); // NOI18N
        panelFrontOffice2.add(bgFrontOffice, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelFrontOffice2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 720));

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
        Mode2();
    }//GEN-LAST:event_bgLogoNOBMouseClicked

    private void iconBOMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconBOMouseClicked
        // TODO add your handling code here:
        Mode3();
    }//GEN-LAST:event_iconBOMouseClicked

    private void iconFOMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconFOMouseClicked
        // TODO add your handling code here:
        Mode4();
    }//GEN-LAST:event_iconFOMouseClicked

    private void bgLogoCashierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bgLogoCashierMouseClicked
        // TODO add your handling code here:
        Mode14();
    }//GEN-LAST:event_bgLogoCashierMouseClicked

    private void btnLoginCasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLoginCasMouseClicked
        // TODO add your handling code here:
        String lettercode = txtUsernameCas.getText().substring(0, 3);
        System.out.println(lettercode);
        if (txtUsernameCas.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please Input Your Username", "Sign In Error", JOptionPane.ERROR_MESSAGE);
            txtUsernameCas.requestFocus();
        } else if (txtPasswordCas.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please Input Your Password", "Sign In Error", JOptionPane.ERROR_MESSAGE);
            txtPasswordCas.requestFocus();
        } else if (txtUsernameCas.getText().substring(0, 3).equals("CAS")) {
            Entity_Signin ESI = new Entity_Signin();
            ESI.setUsername(txtUsernameCas.getText());
            ESI.setPassword(txtPasswordCas.getText());
            ESI.setStatus("Active");
            ESI.setDlc("CAS");
            try {
                int hasil = loginDao.Login(ESI);
                if (hasil == 1) {
                    JOptionPane.showMessageDialog(null, "Sign In Success", "Sign-In Success", JOptionPane.INFORMATION_MESSAGE);
                    Mode15();
                    txt_Trans_Admin.setText(txtUsernameCas.getText());
                    txt_Trans_Simulation_Admin.setText(txtUsernameCas.getText());
                    SetCasStatusOnline();
                    ses.scheduleWithFixedDelay(new Runnable() {
                        @Override
                        public void run() {
                            // TODO add your handling code here:
                            getStatusCashier();
                            getStatusServer();
                        }
                    }, 0, 10, TimeUnit.SECONDS);
                    CekStatus5Sec();
                    setIPCas();
                } else {
                    JOptionPane.showMessageDialog(null, "Password or Username Does Not Match", "Sign-In Error", JOptionPane.ERROR_MESSAGE);
                    txtUsernameCas.setText("");
                    txtPasswordCas.setText("");
                }
            } catch (RemoteException ex) {
                Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Sorry, You Have No Access to Cashier Area", "Sign In Error", JOptionPane.ERROR_MESSAGE);
            txtPasswordCas.requestFocus();
            txtUsernameCas.setText("");
            txtPasswordCas.setText("");
        }

        whoisonline = txtUsernameCas.getText();
        Entity_Signin ESI = new Entity_Signin();
        ESI.setUsername(whoisonline);
        try {
            String hasil = loginDao.CekNamaOnline(ESI);
            txtCasNama.setText(hasil);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnLoginCasMouseClicked

    private void btnResetCasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetCasMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnResetCasMouseClicked

    private void btnLogoutCasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutCasMouseClicked
        // TODO add your handling code here:
        Object[] options = {"LogOut", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Are you sure to Logout ?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                Mode14();
                clearFormLogin();
                SetCasStatusOffline();
                ses.shutdownNow();
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btnLogoutCasMouseClicked

    private void btnSaveError_casMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveError_casMouseClicked
        // TODO add your handling code here:
        String error_added_by = null;

        Entity_Signup ESU = new Entity_Signup();
        ESU.setName(txtCasNama.getText());
        try {
            error_added_by = errorDao.getIDAdminError(ESU);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }

        Entity_ErrorReport EER = new Entity_ErrorReport();
        EER.setError_id(txt_error_id_cas.getText());
        EER.setError_title(txt_error_title_cas.getText());
        EER.setError_desc(txt_error_desc_cas.getText());
        EER.setError_date(dtError_Date_cas.getDate());
        EER.setError_status((String) cb_error_status_cas.getSelectedItem());
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
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
        ClearFormError();
        init_Error_ID();
        loadDaftarError();
    }//GEN-LAST:event_btnSaveError_casMouseClicked

    private void btnClearError_casMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClearError_casMouseClicked
        // TODO add your handling code here:
        ClearFormError();
    }//GEN-LAST:event_btnClearError_casMouseClicked

    private void tabelDaftarError_CasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelDaftarError_CasMouseClicked
        // TODO add your handling code here:
        baris = tabelDaftarError_Cas.getSelectedRow();
        kolom = tabelDaftarError_Cas.getSelectedColumn();
        dataTerpilih = tabelDaftarError_Cas.getValueAt(baris, 0).toString();
    }//GEN-LAST:event_tabelDaftarError_CasMouseClicked

    private void btn_delete_error_casMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_delete_error_casMouseClicked
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
                    Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
                }
                loadDaftarError();
                init_Error_ID();
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btn_delete_error_casMouseClicked

    private void btnSaveDistributorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveDistributorMouseClicked
        // TODO add your handling code here:
        String hasilIDadmin = null;
        if (txt_distributor_name.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please Enter Distributor Name", "Add Distributor Warning", JOptionPane.WARNING_MESSAGE);
            txt_distributor_name.requestFocus();
        } else if (txt_distributor_address.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please Enter Distributor Address", "Add Distributor Warning", JOptionPane.WARNING_MESSAGE);
            txt_distributor_address.requestFocus();
        } else if (txt_distributor_phone.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please Enter Distributor Phone", "Add Distributor Warning", JOptionPane.WARNING_MESSAGE);
            txt_distributor_phone.requestFocus();
        } else if (txt_distributor_email.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please Enter Distributor Email", "Add Distributor Warning", JOptionPane.WARNING_MESSAGE);
            txt_distributor_email.requestFocus();
        } else if (cb_distributor_description.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Please Choose Distributor Description", "Add Distributor Warning", JOptionPane.WARNING_MESSAGE);
            cb_distributor_description.requestFocus();
        } else if (cb_distributor_status.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Please Choose Distributor Status", "Add Distributor Warning", JOptionPane.WARNING_MESSAGE);
            cb_distributor_status.requestFocus();
        } else {

            Entity_Signup ESU = new Entity_Signup();
            ESU.setName(txtCasNama.getText());
            try {
                hasilIDadmin = distributorDao.getIDAdmin(ESU);
                Entity_Distributor ED = new Entity_Distributor();
                ED.setDis_id(txt_distributor_id.getText());
                ED.setDis_name(txt_distributor_name.getText());
                ED.setDis_address(txt_distributor_address.getText());
                ED.setDis_phone(txt_distributor_phone.getText());
                ED.setDis_email(txt_distributor_email.getText());
                ED.setDis_desc((String) cb_distributor_description.getSelectedItem());
                ED.setDis_status((String) cb_distributor_status.getSelectedItem());
                ED.setDis_added_by(hasilIDadmin);
                try {
                    int hasil = distributorDao.saveDistributor(ED);
                    if (hasil == 1) {
                        JOptionPane.showMessageDialog(null, "Distributor Added", "Add Distributor Success", JOptionPane.INFORMATION_MESSAGE);
                        init_Distributor_ID();
                        ClearFormAddDistributor();
                        initTableDaftarDistributor();
                        loadDaftarDistributor();
                    } else {
                        JOptionPane.showMessageDialog(null, "Distributor Add Failed", "Add Distributor Failed", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (RemoteException ex) {
                Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnSaveDistributorMouseClicked

    private void btnClearDistributorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClearDistributorMouseClicked
        // TODO add your handling code here:
        ClearFormAddDistributor();
    }//GEN-LAST:event_btnClearDistributorMouseClicked

    private void tabelDataDistributorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelDataDistributorMouseClicked
        // TODO add your handling code here:
        baris = tabelDataDistributor.getSelectedRow();
        kolom = tabelDataDistributor.getSelectedColumn();
        dataTerpilih = tabelDataDistributor.getValueAt(baris, 0).toString();
        Object[] options = {"Yes", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Are you sure to delete this Distributor ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                Entity_Distributor ED = new Entity_Distributor();
                ED.setDis_id(dataTerpilih);
                try {
                    int hasil = distributorDao.deleteDistributor(ED);
                    if (hasil == 1) {
                        JOptionPane.showMessageDialog(null, "Distributor Deleted", "Delete Distributor Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        evt.consume();
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Delete Delete Failed", "Delete Distributor Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
                }
                loadDaftarDistributor();
                init_Distributor_ID();
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_tabelDataDistributorMouseClicked

    private void btn_active_distributorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_active_distributorMouseClicked
        // TODO add your handling code here:
        Object[] options = {"Yes", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Are you sure to make this Distributor Active ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                if (tabelDataDistributor.getValueAt(baris, 4).toString().equals("Active")) {
                    JOptionPane.showMessageDialog(null, "Distributor Already Active", "Activation Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    Entity_Distributor ED = new Entity_Distributor();
                    ED.setDis_id(dataTerpilih);
                    try {
                        int hasil = distributorDao.updateStatusDistributor_Active(ED);
                        if (hasil == 1) {
                            JOptionPane.showMessageDialog(null, "Activation Department Success", "Activation Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            evt.consume();
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(null, "Activation Department Failed", "Activation Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    loadDaftarDistributor();
                }
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btn_active_distributorMouseClicked

    private void btn_deactive_distributorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_deactive_distributorMouseClicked
        // TODO add your handling code here:        
        Object[] options = {"Yes", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Are you sure to make this Distributor Inactive ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                if (tabelDataDistributor.getValueAt(baris, 4).toString().equals("Inactive")) {
                    JOptionPane.showMessageDialog(null, "Distributor Already Inactive", "Activation Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    Entity_Distributor ED = new Entity_Distributor();
                    ED.setDis_id(dataTerpilih);
                    try {
                        int hasil = distributorDao.updateStatusDistributor_inActive(ED);
                        if (hasil == 1) {
                            JOptionPane.showMessageDialog(null, "DeActivation Department Success", "Activation Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            evt.consume();
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(null, "DeActivation Department Failed", "Activation Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    loadDaftarDistributor();
                }
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btn_deactive_distributorMouseClicked

    private void txt_trans_searchByIDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_trans_searchByIDMouseClicked
        // TODO add your handling code here:
        txt_trans_searchByID.setText("");
        txt_trans_searchByDis.setText("Please enter to search ...");
        txt_trans_searchByDate.setText("Please enter to search ...");
        txt_trans_searchByAdmin.setText("Please enter to search ...");
    }//GEN-LAST:event_txt_trans_searchByIDMouseClicked

    private void txt_trans_searchByIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_trans_searchByIDKeyPressed
        // TODO add your handling code here:
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            initTabelTransList();
            loadDaftarTransactionbyIDTrans();
        }
    }//GEN-LAST:event_txt_trans_searchByIDKeyPressed

    private void txt_trans_searchByDateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_trans_searchByDateMouseClicked
        // TODO add your handling code here:
        txt_trans_searchByDate.setText("");
        txt_trans_searchByDis.setText("Please enter to search ...");
        txt_trans_searchByID.setText("Please enter to search ...");
        txt_trans_searchByAdmin.setText("Please enter to search ...");
    }//GEN-LAST:event_txt_trans_searchByDateMouseClicked

    private void txt_trans_searchByDateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_trans_searchByDateKeyPressed
        // TODO add your handling code here:
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            initTabelTransList();
            loadDaftarTransactionbyTransDate();
        }
    }//GEN-LAST:event_txt_trans_searchByDateKeyPressed

    private void tabelTransListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelTransListMouseClicked
        // TODO add your handling code here:
        baris = tabelTransList.getSelectedRow();
        kolom = tabelTransList.getSelectedColumn();
        dataTerpilihTabelTrans = tabelTransList.getValueAt(baris, 0).toString();
    }//GEN-LAST:event_tabelTransListMouseClicked

    private void iconCasErrorReportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconCasErrorReportMouseClicked
        // TODO add your handling code here:
        Mode20();
        init_Error_ID();
        initTableDaftarError();
        loadDaftarError();
        last_session = 20;
    }//GEN-LAST:event_iconCasErrorReportMouseClicked

    private void iconCasErrorReportMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconCasErrorReportMouseEntered
        // TODO add your handling code here:
        Color darkGreen = new Color(24, 165, 93);
        menuCasErrorReport.setBackground(darkGreen);
    }//GEN-LAST:event_iconCasErrorReportMouseEntered

    private void iconCasErrorReportMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconCasErrorReportMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuCasErrorReport.setBackground(lightGray);
    }//GEN-LAST:event_iconCasErrorReportMouseExited

    private void menuCasErrorReportMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuCasErrorReportMouseEntered
        // TODO add your handling code here:
        Color darkGreen = new Color(24, 165, 93);
        menuCasErrorReport.setBackground(darkGreen);
    }//GEN-LAST:event_menuCasErrorReportMouseEntered

    private void menuCasErrorReportMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuCasErrorReportMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuCasErrorReport.setBackground(lightGray);
    }//GEN-LAST:event_menuCasErrorReportMouseExited

    private void iconCasDataTransactionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconCasDataTransactionMouseClicked
        // TODO add your handling code here:
        Mode19();
        txt_trans_searchByDate.setText("Please enter to search ...");
        txt_trans_searchByDis.setText("Please enter to search ...");
        txt_trans_searchByID.setText("Please enter to search ...");
        txt_trans_searchByAdmin.setText("Please enter to search ...");
        last_session = 19;
    }//GEN-LAST:event_iconCasDataTransactionMouseClicked

    private void iconCasDataTransactionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconCasDataTransactionMouseEntered
        // TODO add your handling code here:
        Color darkGreen = new Color(24, 165, 93);
        menuCasDataTransaction.setBackground(darkGreen);
    }//GEN-LAST:event_iconCasDataTransactionMouseEntered

    private void iconCasDataTransactionMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconCasDataTransactionMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuCasDataTransaction.setBackground(lightGray);
    }//GEN-LAST:event_iconCasDataTransactionMouseExited

    private void menuCasDataTransactionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuCasDataTransactionMouseEntered
        // TODO add your handling code here:
        Color darkGreen = new Color(24, 165, 93);
        menuCasDataTransaction.setBackground(darkGreen);
    }//GEN-LAST:event_menuCasDataTransactionMouseEntered

    private void menuCasDataTransactionMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuCasDataTransactionMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuCasDataTransaction.setBackground(lightGray);
    }//GEN-LAST:event_menuCasDataTransactionMouseExited

    private void iconCasListItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconCasListItemMouseClicked
        // TODO add your handling code here:
        Mode18();
        customTabelDaftarItem();
        txt_trans_searchitemByID.setText("Please enter to search ...");
        txt_trans_searchitemByName.setText("Please enter to search ...");
        txt_trans_searchitemByCategory.setText("Please enter to search ...");
        txt_trans_searchitemByBrand.setText("Please enter to search ...");
        last_session = 18;
    }//GEN-LAST:event_iconCasListItemMouseClicked

    private void iconCasListItemMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconCasListItemMouseEntered
        // TODO add your handling code here:
        Color darkGreen = new Color(24, 165, 93);
        menuCasListItem.setBackground(darkGreen);
    }//GEN-LAST:event_iconCasListItemMouseEntered

    private void iconCasListItemMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconCasListItemMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuCasListItem.setBackground(lightGray);
    }//GEN-LAST:event_iconCasListItemMouseExited

    private void menuCasListItemMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuCasListItemMouseEntered
        // TODO add your handling code here:
        Color darkGreen = new Color(24, 165, 93);
        menuCasListItem.setBackground(darkGreen);
    }//GEN-LAST:event_menuCasListItemMouseEntered

    private void menuCasListItemMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuCasListItemMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuCasListItem.setBackground(lightGray);
    }//GEN-LAST:event_menuCasListItemMouseExited

    private void iconCasAddCustomerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconCasAddCustomerMouseClicked
        // TODO add your handling code here:
        Mode17();
        initTableDaftarDistributor();
        loadDaftarDistributor();
        last_session = 17;
    }//GEN-LAST:event_iconCasAddCustomerMouseClicked

    private void iconCasAddCustomerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconCasAddCustomerMouseEntered
        // TODO add your handling code here:
        Color darkGreen = new Color(24, 165, 93);
        menuCasAddCustomer.setBackground(darkGreen);
    }//GEN-LAST:event_iconCasAddCustomerMouseEntered

    private void iconCasAddCustomerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconCasAddCustomerMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuCasAddCustomer.setBackground(lightGray);
    }//GEN-LAST:event_iconCasAddCustomerMouseExited

    private void menuCasAddCustomerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuCasAddCustomerMouseEntered
        // TODO add your handling code here:
        Color darkGreen = new Color(24, 165, 93);
        menuCasAddCustomer.setBackground(darkGreen);
    }//GEN-LAST:event_menuCasAddCustomerMouseEntered

    private void menuCasAddCustomerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuCasAddCustomerMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuCasAddCustomer.setBackground(lightGray);
    }//GEN-LAST:event_menuCasAddCustomerMouseExited

    private void iconCasAddTransMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconCasAddTransMouseClicked
        // TODO add your handling code here:
        Mode16();
        HideTransactionElement();
        customTabelDaftarItem();
        last_session = 16;
    }//GEN-LAST:event_iconCasAddTransMouseClicked

    private void iconCasAddTransMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconCasAddTransMouseEntered
        // TODO add your handling code here:
        Color darkGreen = new Color(24, 165, 93);
        menuCasAddTransaction.setBackground(darkGreen);
    }//GEN-LAST:event_iconCasAddTransMouseEntered

    private void iconCasAddTransMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconCasAddTransMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuCasAddTransaction.setBackground(lightGray);
    }//GEN-LAST:event_iconCasAddTransMouseExited

    private void menuCasAddTransactionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuCasAddTransactionMouseEntered
        // TODO add your handling code here:
        Color darkGreen = new Color(24, 165, 93);
        menuCasAddTransaction.setBackground(darkGreen);
    }//GEN-LAST:event_menuCasAddTransactionMouseEntered

    private void menuCasAddTransactionMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuCasAddTransactionMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuCasAddTransaction.setBackground(lightGray);
    }//GEN-LAST:event_menuCasAddTransactionMouseExited

    private void iconCasDashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconCasDashboardMouseClicked
        // TODO add your handling code here:
        Mode15();
        ScrollPaneAddTrans.getVerticalScrollBar().setUnitIncrement(60);
        last_session = 15;
    }//GEN-LAST:event_iconCasDashboardMouseClicked

    private void iconCasDashboardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconCasDashboardMouseEntered
        // TODO add your handling code here:
        Color darkGreen = new Color(24, 165, 93);
        menuCasDashboard.setBackground(darkGreen);
    }//GEN-LAST:event_iconCasDashboardMouseEntered

    private void iconCasDashboardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconCasDashboardMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuCasDashboard.setBackground(lightGray);
    }//GEN-LAST:event_iconCasDashboardMouseExited

    private void menuCasDashboardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuCasDashboardMouseEntered
        // TODO add your handling code here:
        Color darkGreen = new Color(24, 165, 93);
        menuCasDashboard.setBackground(darkGreen);
    }//GEN-LAST:event_menuCasDashboardMouseEntered

    private void menuCasDashboardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuCasDashboardMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuCasDashboard.setBackground(lightGray);
    }//GEN-LAST:event_menuCasDashboardMouseExited

    private void btnChangeStateCasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChangeStateCasMouseClicked
        // TODO add your handling code here:
        int hasilUpdateStatusCas = 0;
        Object[] options = {"Online", "Idle", "Invisible", "Offline"};
        int reply = JOptionPane.showOptionDialog(null, "Choose your Client [Cashier] state : ", "State Chooser", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                if (txt_stateCas.getText().equals("Online")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Online", "Warning Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int hasilSetCasStatus = loginDao.UpdateStatusOnline(kodeSistem);
                        if (hasilSetCasStatus == 1) {
                            txt_stateBigCas.setText("Server is Well-Connected");
                            lblStateCas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OnlineState.png")));
                            lblStateBigCas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OnlineState.png")));
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case 1:
                if (txt_stateCas.getText().equals("Idle")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Idle", "Warning Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int hasilSetCasStatus = loginDao.UpdateStatusIdle(kodeSistem);
                        if (hasilSetCasStatus == 1) {
                            txt_stateBigCas.setText("Server is Well-Connected");
                            lblStateCas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/IdleState.png")));
                            lblStateBigCas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/IdleState.png")));
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case 2:
                if (txt_stateCas.getText().equals("Invisible")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Invisible", "Warning Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int hasilSetCasStatus = loginDao.UpdateStatusInvisible(kodeSistem);
                        if (hasilSetCasStatus == 1) {
                            txt_stateBigCas.setText("Server is Well-Connected");
                            lblStateCas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/InvisState.png")));
                            lblStateBigCas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/InvisState.png")));
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case 3:
                if (txt_stateCas.getText().equals("Offline")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Offline", "Warning Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int hasilSetCasStatus = loginDao.UpdateStatusDisconnect(kodeSistem);
                        if (hasilSetCasStatus == 1) {
                            txt_stateBigCas.setText("Server is Disconnected");
                            lblStateCas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OffState.png")));
                            lblStateBigCas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OffState.png")));
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
    }//GEN-LAST:event_btnChangeStateCasMouseClicked

    private void btnHideIPCasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHideIPCasMouseClicked
        // TODO add your handling code here:
        whoisonline = txtUsernameCas.getText();
//        System.out.println(whoisonline);
        Entity_Signin ESI = new Entity_Signin();
        ESI.setUsername(whoisonline);
        try {
            String hasil = loginDao.CekNamaOnline(ESI);
            txtCasNama.setText(hasil);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
        ShowIPModeCas();
    }//GEN-LAST:event_btnHideIPCasMouseClicked

    private void btnShowIPCasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnShowIPCasMouseClicked
        // TODO add your handling code here:
        txtCasNama.setText(ip);
        HideIPModeCas();
    }//GEN-LAST:event_btnShowIPCasMouseClicked

    private void btn_trans_getQrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_trans_getQrMouseClicked
        // TODO add your handling code here:
        GetQrCode_Trans();
    }//GEN-LAST:event_btn_trans_getQrMouseClicked

    private void btn_trans_check_stockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_trans_check_stockMouseClicked
        // TODO add your handling code here:
        if (txt_trans_itemcode.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Get Item First", "Stock Info", JOptionPane.WARNING_MESSAGE);
            txt_trans_itemcode.requestFocus();
            txt_trans_qty.setText("");
        } else if (txt_trans_itemname.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Get Item First", "Stock Info", JOptionPane.WARNING_MESSAGE);
            txt_trans_itemcode.requestFocus();
            txt_trans_qty.setText("");
        } else if (txt_trans_qty.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Input Quantity", "Stock Info", JOptionPane.WARNING_MESSAGE);
            txt_trans_qty.requestFocus();
        } else {
            Entity_Transaction ETR = new Entity_Transaction();
            ETR.setItem_id(txt_trans_itemcode.getText());
            ETR.setQuantity(Integer.parseInt(txt_trans_qty.getText()));
            System.out.println(ETR.getQuantity());
            try {
                int hasil = transactionDao.CekStok(ETR);
                System.out.println(hasil);
                if (hasil == 1) {
                    JOptionPane.showMessageDialog(null, "Stock Available", "Stock Info", JOptionPane.INFORMATION_MESSAGE);
                    stockstatus = "A";
                } else {
                    JOptionPane.showMessageDialog(null, "Stock Not Available", "Stock Info", JOptionPane.INFORMATION_MESSAGE);
                    stockstatus = "NA";
                }
            } catch (RemoteException ex) {
                Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btn_trans_check_stockMouseClicked

    private void ScrollPaneAddTransMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_ScrollPaneAddTransMouseWheelMoved
        // TODO add your handling code here:
        subpanelCasAddTransaction.setAutoscrolls(true);
        ScrollPaneAddTrans.setAutoscrolls(true);
        ScrollPaneAddTrans.getVerticalScrollBar().setUnitIncrement(40);
    }//GEN-LAST:event_ScrollPaneAddTransMouseWheelMoved

    private void ScrollPaneAddTransMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ScrollPaneAddTransMouseEntered
        // TODO add your handling code here:
        subpanelCasAddTransaction.setAutoscrolls(true);
        ScrollPaneAddTrans.setAutoscrolls(true);
        ScrollPaneAddTrans.getVerticalScrollBar().setUnitIncrement(40);
    }//GEN-LAST:event_ScrollPaneAddTransMouseEntered

    private void txt_trans_nominalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_trans_nominalKeyTyped
        // TODO add your handling code here:
        txt_trans_nominal.setForeground(Color.black);
    }//GEN-LAST:event_txt_trans_nominalKeyTyped

    private void txt_trans_itemcodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_trans_itemcodeKeyPressed
        // TODO add your handling code here:
        DecimalFormat df = new DecimalFormat("IDR ,###.00");
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            Entity_Transaction ETR = new Entity_Transaction();
            Entity_Item EI = new Entity_Item();
            ETR.setItem_id(txt_trans_itemcode.getText());
            EI.setItem_id(txt_trans_itemcode.getText());
            try {
                availabelItem = transactionDao.CekItemAvailable(EI);
                if (availabelItem == 0) {
                    JOptionPane.showMessageDialog(null, "Item Does Not Exist", "Item Info", JOptionPane.WARNING_MESSAGE);
                } else {
                    NamaItem = transactionDao.CekNamaItem(ETR);
                    PriceNTA = transactionDao.CekPriceNTAItem(ETR);
                    PricePub = transactionDao.CekPricePubItem(ETR);
                    PriceSingle = transactionDao.CekPriceSingleItem(ETR);

                    txt_trans_itemname.setText(NamaItem);
                    txt_trans_NTAFare.setText("Not Show");
                    txt_trans_PubFare.setText(String.valueOf(df.format(PricePub)));
                    txt_trans_SingleFare.setText(String.valueOf(df.format(PriceSingle)));
                    txt_trans_qty.requestFocus();
                }
            } catch (RemoteException ex) {
                Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_txt_trans_itemcodeKeyPressed

    private void txt_trans_itemcodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_trans_itemcodeMouseClicked
        // TODO add your handling code here:
        txt_trans_itemcode.setText("");
    }//GEN-LAST:event_txt_trans_itemcodeMouseClicked

    private void btn_trans_add_itemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_trans_add_itemMouseClicked
        // TODO add your handling code here:
        TableModel tabelModel;
        tabelModel = tabelShoppingCart.getModel();
        DefaultTableModel model = (DefaultTableModel) tabelShoppingCart.getModel();
        DecimalFormat df = new DecimalFormat(",###.00");
        if (cb_spt.getSelectedIndex() == 1) {
            cb_CustType.setSelectedIndex(2);
            subTotalPublish = PricePub * Integer.parseInt(txt_trans_qty.getText());
            JenisHarga = PricePub;
            model.addRow(new Object[]{txt_trans_itemcode.getText(), txt_trans_itemname.getText(), JenisHarga, txt_trans_qty.getText(), subTotalPublish});
            customTabelListItem();
            int barisnya = tabelShoppingCart.getRowCount();
            for (int i = 0; i < barisnya; i++) {
                pricepblh = Integer.parseInt(model.getValueAt(i, 4).toString());
            }
            pricePublish = pricePublish + pricepblh;
            txt_trans_total_harga_publish.setText(String.valueOf(df.format(pricePublish)));
            subTotalNTA = PriceNTA * Integer.valueOf(txt_trans_qty.getText());
            NTA = NTA + subTotalNTA;
            discount = 0.01 * pricePublish;
            txt_price_disc.setText(String.valueOf(df.format(discount)));
            grandTotal = pricePublish - discount - discountVoucher;
            txt_trans_grand_total.setText(String.valueOf(df.format(grandTotal)));
            ClearFormItem();
        } else if (cb_spt.getSelectedIndex() == 2) {
            cb_CustType.setSelectedIndex(1);
            subTotalPublish = PriceSingle * Integer.parseInt(txt_trans_qty.getText());
            JenisHarga = PriceSingle;
            model.addRow(new Object[]{txt_trans_itemcode.getText(), txt_trans_itemname.getText(), JenisHarga, txt_trans_qty.getText(), subTotalPublish});
            customTabelListItem();
            int barisnya = tabelShoppingCart.getRowCount();
            for (int i = 0; i < barisnya; i++) {
                pricepblh = Integer.parseInt(model.getValueAt(i, 4).toString());
            }
            pricePublish = pricePublish + pricepblh;
            txt_trans_total_harga_publish.setText(String.valueOf(df.format(pricePublish)));
            subTotalNTA = PriceNTA * Integer.valueOf(txt_trans_qty.getText());
            NTA = NTA + subTotalNTA;
            discount = 0.01 * pricePublish;
            txt_price_disc.setText(String.valueOf(df.format(discount)));
            grandTotal = pricePublish - discount - discountVoucher;
            txt_trans_grand_total.setText(String.valueOf(df.format(grandTotal)));
            ClearFormItem();
        } else {
        }
    }//GEN-LAST:event_btn_trans_add_itemMouseClicked

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        // TODO add your handling code here:
        ShowPanelPayment();
        last_session = 22;
    }//GEN-LAST:event_jButton3MouseClicked

    private void txt_trans_vouchercodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_trans_vouchercodeKeyPressed
        // TODO add your handling code here:
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            if (txt_trans_vouchercode.getText().equals("")) {
                kodeVoucher = "NOVOUCHER";
                discountVoucher = 0;
            } else {
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setVou_id(txt_trans_vouchercode.getText());
                try {
                    cektanggalexpiredVoucher = transactionDao.CekExpiredVoucher(ETR);
                    tanggalVoucher = transactionDao.CekTanggalVoucher(ETR);
                    cekstokVoucher = transactionDao.CekStokVoucher(ETR);
                    availableVoucher = transactionDao.CekVoucherAvailable(ETR);
                    System.out.println(availableVoucher);

                    if (availableVoucher == 0) {
                        evt.consume();
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Voucher Does Not Exist", "Voucher Info", JOptionPane.WARNING_MESSAGE);
                    } else {
                        if (cektanggalexpiredVoucher == 1) {
                            SimpleDateFormat sm = new SimpleDateFormat("dd/MM/yyyy");
                            String strDate = sm.format(tanggalVoucher);
                            evt.consume();
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(null, "Voucher Expired" + "\nTanggal Berlaku Voucher hingga " + strDate, "Voucher Info", JOptionPane.WARNING_MESSAGE);
                        } else if (cekstokVoucher == 0) {
                            evt.consume();
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(null, "Voucher Out of Stock", "Voucher Info", JOptionPane.WARNING_MESSAGE);
                        } else if (cektanggalexpiredVoucher == 2 || cektanggalexpiredVoucher == 3) {
                            if (cekstokVoucher == 1) {
                                kodeVoucher.equals(txt_trans_Vouchercode.getText());
                                discountVoucher = transactionDao.CekDiscountVoucher(ETR);
                                txt_trans_Vouchercode.setText(txt_trans_vouchercode.getText());
                                DecimalFormat df = new DecimalFormat(",###.00");
                                txt_trans_voucherdiscount.setText(String.valueOf(df.format(discountVoucher)));
                                txt_trans_voucher.setText(String.valueOf(df.format(discountVoucher)));
                                grandTotal = pricePublish - discount - discountVoucher;
                                txt_trans_grand_total.setText(String.valueOf(df.format(grandTotal)));
                            } else {
                                JOptionPane.showMessageDialog(null, "Voucher Out Of Stock ", "Voucher Info", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
                }
                btn_Trans_rmvVoucher.setVisible(true);
                btn_Trans_getVoucher.setVisible(false);
            }
        }
    }//GEN-LAST:event_txt_trans_vouchercodeKeyPressed

    private void btn_Trans_getVoucherMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Trans_getVoucherMouseClicked
        // TODO add your handling code here:
        if (txt_trans_vouchercode.getText().equals("")) {
            kodeVoucher = "NOVOUCHER";
            discountVoucher = 0;
        } else {
            Entity_Transaction ETR = new Entity_Transaction();
            ETR.setVou_id(txt_trans_vouchercode.getText());
            try {
                cektanggalexpiredVoucher = transactionDao.CekExpiredVoucher(ETR);
                tanggalVoucher = transactionDao.CekTanggalVoucher(ETR);
                cekstokVoucher = transactionDao.CekStokVoucher(ETR);
                availableVoucher = transactionDao.CekVoucherAvailable(ETR);
                System.out.println(availableVoucher);

                if (availableVoucher == 0) {
                    evt.consume();
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Voucher Does Not Exist", "Voucher Info", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (cektanggalexpiredVoucher == 1) {
                        SimpleDateFormat sm = new SimpleDateFormat("dd/MM/yyyy");
                        String strDate = sm.format(tanggalVoucher);
                        evt.consume();
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Voucher Expired" + "\nTanggal Berlaku Voucher hingga " + strDate, "Voucher Info",
                                JOptionPane.WARNING_MESSAGE);
                    } else if (cekstokVoucher == 0) {
                        evt.consume();
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Voucher Out of Stock", "Voucher Info", JOptionPane.WARNING_MESSAGE);
                    } else if (cektanggalexpiredVoucher == 2 || cektanggalexpiredVoucher == 3) {
                        if (cekstokVoucher == 1) {
                            kodeVoucher.equals(txt_trans_Vouchercode.getText());
                            discountVoucher = transactionDao.CekDiscountVoucher(ETR);
                            txt_trans_Vouchercode.setText(txt_trans_vouchercode.getText());
                            DecimalFormat df = new DecimalFormat(",###.00");
                            txt_trans_voucherdiscount.setText(String.valueOf(df.format(discountVoucher)));
                            txt_trans_voucher.setText(String.valueOf(df.format(discountVoucher)));
                            grandTotal = pricePublish - discount - discountVoucher;
                            txt_trans_grand_total.setText(String.valueOf(df.format(grandTotal)));
                        } else {
                            JOptionPane.showMessageDialog(null, "Voucher Out Of Stock ", "Voucher Info", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            } catch (RemoteException ex) {
                Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
            }
            btn_Trans_rmvVoucher.setVisible(true);
            btn_Trans_getVoucher.setVisible(false);
        }
    }//GEN-LAST:event_btn_Trans_getVoucherMouseClicked

    private void txt_trans_customeridKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_trans_customeridKeyPressed
        // TODO add your handling code here:       
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            Entity_Distributor ED = new Entity_Distributor();
            ED.setDis_id(txt_trans_customerid.getText());
            try {
                int availableDistributor = transactionDao.CekDistributorAvailable(ED);
                int activeDistributor = transactionDao.CekDistributorActive(ED);
                if (availableDistributor == 0) {
                    JOptionPane.showMessageDialog(null, "Customer / Distributor Not Exist", "Customer Info", JOptionPane.WARNING_MESSAGE);
                } else if (activeDistributor == 0) {
                    JOptionPane.showMessageDialog(null, "Customer / Distributor Not Active", "Customer Info", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (cb_CustType.getSelectedIndex() == 1) {
                        namaDistributor = transactionDao.CekNamaDistributor(ED);
                        emailDistributor = transactionDao.CekEmailDistributor(ED);
                        phoneDistributor = transactionDao.CekPhoneDistributor(ED);
                        if (namaDistributor.equals(null) || emailDistributor.equals(null) || phoneDistributor.equals(null)) {
                            JOptionPane.showMessageDialog(null, "ID Customer Does Not Exist", "Customer Info", JOptionPane.WARNING_MESSAGE);
                        } else {
                            txt_trans_custname.setText(namaDistributor);
                            txt_trans_custmail.setText(emailDistributor);
                            txt_trans_custphone.setText(phoneDistributor);
                        }
                    } else if (cb_CustType.getSelectedIndex() == 2) {

                        namaDistributor = transactionDao.CekNamaDistributor(ED);
                        emailDistributor = transactionDao.CekEmailDistributor(ED);
                        phoneDistributor = transactionDao.CekPhoneDistributor(ED);
                        if (namaDistributor.equals(null) || emailDistributor.equals(null) || phoneDistributor.equals(null)) {
                            JOptionPane.showMessageDialog(null, "ID Customer Does Not Exist", "Customer Info", JOptionPane.WARNING_MESSAGE);
                        } else {
                            txt_trans_custname.setText(namaDistributor);
                            txt_trans_custmail.setText(emailDistributor);
                            txt_trans_custphone.setText(phoneDistributor);
                        }
                    } else {
                        evt.consume();
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Choose Customer Type", "Customer Info", JOptionPane.WARNING_MESSAGE);
                        cb_CustType.requestFocus();
                    }
                }
            } catch (RemoteException ex) {
                Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_txt_trans_customeridKeyPressed

    private void txt_trans_customeridMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_trans_customeridMouseClicked
        // TODO add your handling code here:
        txt_trans_customerid.setText("");
    }//GEN-LAST:event_txt_trans_customeridMouseClicked

    private void txt_trans_nominalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_trans_nominalMouseClicked
        // TODO add your handling code here:
        txt_trans_nominal.setText("");
    }//GEN-LAST:event_txt_trans_nominalMouseClicked

    private void cb_trans_bankItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_trans_bankItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == evt.SELECTED) {
            Entity_Bank EB = new Entity_Bank();
            EB.setBank_name((String) cb_trans_bank.getSelectedItem());
            try {
                idBank = transactionDao.getIDBank(EB);
                System.out.println(idBank);

            } catch (RemoteException ex) {
                Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_cb_trans_bankItemStateChanged

    private void cb_trans_piutangItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_trans_piutangItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == evt.SELECTED) {
            Entity_Piutang EP = new Entity_Piutang();
            EP.setPiutang_by((String) cb_trans_piutang.getSelectedItem());
            try {
                idPiutang = transactionDao.getIDPiutang(EP);
                System.out.println(idPiutang);

            } catch (RemoteException ex) {
                Logger.getLogger(HomePageClientCashier.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_cb_trans_piutangItemStateChanged

    private void cb_sptItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_sptItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == evt.SELECTED) {
            if (cb_spt.getSelectedIndex() == 1) {
                cb_CustType.setSelectedIndex(2);
            } else if (cb_spt.getSelectedIndex() == 2) {
                cb_CustType.setSelectedIndex(1);
            }
        }
    }//GEN-LAST:event_cb_sptItemStateChanged

    private void btn_trans_show_ntaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_trans_show_ntaMouseClicked
        // TODO add your handling code here:
        DecimalFormat df = new DecimalFormat(",###.00");
        txt_trans_TotalNTA.setText(String.valueOf(df.format(NTA)));
        btn_trans_show_nta.setVisible(false);
        btn_trans_hide_nta.setVisible(true);
    }//GEN-LAST:event_btn_trans_show_ntaMouseClicked

    private void btn_trans_show_NTAItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_trans_show_NTAItemMouseClicked
        // TODO add your handling code here:
        DecimalFormat df = new DecimalFormat("IDR ,###.00");
        txt_trans_NTAFare.setText(String.valueOf(df.format(PriceNTA)));
        btn_trans_show_NTAItem.setVisible(false);
        btn_trans_hide_NTAItem.setVisible(true);
    }//GEN-LAST:event_btn_trans_show_NTAItemMouseClicked

    private void btn_trans_hide_ntaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_trans_hide_ntaMouseClicked
        // TODO add your handling code here:
        txt_trans_TotalNTA.setText("Not Show");
        btn_trans_show_nta.setVisible(true);
        btn_trans_hide_nta.setVisible(false);
    }//GEN-LAST:event_btn_trans_hide_ntaMouseClicked

    private void btn_trans_hide_NTAItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_trans_hide_NTAItemMouseClicked
        // TODO add your handling code here:
        txt_trans_NTAFare.setText("Not Show");
        btn_trans_show_NTAItem.setVisible(true);
        btn_trans_hide_NTAItem.setVisible(false);
    }//GEN-LAST:event_btn_trans_hide_NTAItemMouseClicked

    private void btn_trans_lock_paymentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_trans_lock_paymentMouseClicked
        // TODO add your handling code here:

        DecimalFormat df = new DecimalFormat(",###.00");
        if (cb_CustType.getSelectedIndex() == 0) {
            evt.consume();
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Choose Customer Type", "Payment Info", JOptionPane.WARNING_MESSAGE);
            cb_CustType.requestFocus();
        } else if (cb_trans_paymentvia.getSelectedIndex() == 0) {
            evt.consume();
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Choose Payment Via", "Payment Info", JOptionPane.WARNING_MESSAGE);
            cb_trans_paymentvia.requestFocus();
        } else if (cb_trans_paymenttype.getSelectedIndex() == 0) {
            evt.consume();
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Choose Payment Type", "Payment Info", JOptionPane.WARNING_MESSAGE);
            cb_trans_paymenttype.requestFocus();
        } else if (txt_trans_nominal.getText().equals("")) {
            evt.consume();
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Nominal is not same with Grand Total", "Payment Info", JOptionPane.WARNING_MESSAGE);
            cb_trans_paymenttype.requestFocus();
        } else if (Double.parseDouble(txt_trans_nominal.getText()) != grandTotal) {
            evt.consume();
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Nominal is not same with Grand Total", "Payment Info", JOptionPane.WARNING_MESSAGE);
            txt_trans_nominal.requestFocus();
        } else {
            if (cb_CustType.getSelectedIndex() == 1) {
                customerType = "Customer";
            } else if (cb_CustType.getSelectedIndex() == 2) {
                customerType = "Distributor";
            }

            if (cb_trans_bank.getSelectedIndex() == 0) {
                idBank = "NOT-TRANSFER";
                Bank = "NOT-TRANSFER";
            } else {
                Bank = String.valueOf(cb_trans_bank.getSelectedItem());
            }

            if (cb_trans_piutang.getSelectedIndex() == 0) {
                idPiutang = "NOT-PIUTANG";
                piutangBy = "NOT-PIUTANG";
            } else {
                piutangBy = String.valueOf(cb_trans_piutang.getSelectedItem());
            }

            paymentVia = String.valueOf(cb_trans_paymentvia.getSelectedItem());
            paymentType = String.valueOf(cb_trans_paymenttype.getSelectedItem());
            Nominal = df.format(Integer.parseInt(txt_trans_nominal.getText()));
            customerType = String.valueOf(cb_CustType.getSelectedItem());
            admin = txt_Trans_Admin.getText();
            paymentDesc = "Payment Detail" + "\nTransfer Via " + paymentVia + "\nPayment Type : " + paymentType + "\nNominal : IDR " + Nominal + "\nBank : " + Bank + "\nPiutang By :" + piutangBy + "\nCustomer Type : " + customerType + "\nDone By : " + admin;
            txt_trans_description.setText(paymentDesc);
            btn_trans_process.setVisible(true);
        }
    }//GEN-LAST:event_btn_trans_lock_paymentMouseClicked

    private void btn_Trans_rmvVoucherMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_Trans_rmvVoucherMouseClicked
        // TODO add your handling code here:

        DecimalFormat df = new DecimalFormat(",###.00");

        txt_trans_vouchercode.setText("");
        txt_trans_Vouchercode.setText("");
        txt_trans_voucherdiscount.setText("0");
        txt_trans_voucher.setText("0");
        btn_Trans_rmvVoucher.setVisible(false);
        btn_Trans_getVoucher.setVisible(true);

        discountVoucher = 0;
        grandTotal = pricePublish - discount - discountVoucher;
        txt_trans_grand_total.setText(String.valueOf(df.format(grandTotal)));
    }//GEN-LAST:event_btn_Trans_rmvVoucherMouseClicked

    private void cb_trans_paymenttypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_trans_paymenttypeItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == evt.SELECTED) {
            if (cb_trans_paymenttype.getSelectedIndex() == 1) {
                cb_trans_bank.setEnabled(false);
                cb_trans_piutang.setEnabled(false);
            } else if (cb_trans_paymenttype.getSelectedIndex() == 2 || cb_trans_paymenttype.getSelectedIndex() == 3) {
                cb_trans_bank.setEnabled(true);
                cb_trans_piutang.setEnabled(false);
            } else if (cb_trans_paymenttype.getSelectedIndex() == 4) {
                cb_trans_bank.setEnabled(false);
                cb_trans_piutang.setEnabled(true);
            }
        }
    }//GEN-LAST:event_cb_trans_paymenttypeItemStateChanged

    private void btn_trans_processMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_trans_processMouseClicked
        // TODO add your handling code here:       
        if (lbl_qrcode_trans.getGraphics() == null) {
            JOptionPane.showMessageDialog(null, "Get QR Code Transaction", "Save Warning", JOptionPane.WARNING_MESSAGE);
            btn_trans_getQr.requestFocus();
        } else if (dtTrans_date.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Select Transaction Date", "Save Warning", JOptionPane.WARNING_MESSAGE);
            dtTrans_date.requestFocus();
        } else if (txt_trans_custname.getText().equals("#YourName") || txt_trans_custmail.getText().equals("@youremail") || txt_trans_custphone.getText().equals("#PhoneNumber")) {
            JOptionPane.showMessageDialog(null, "Add Customer Data", "Save Warning", JOptionPane.WARNING_MESSAGE);
            txt_trans_customerid.requestFocus();
        } else if (cb_trans_paymentvia.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Select Payment Via", "Save Warning", JOptionPane.WARNING_MESSAGE);
            cb_trans_paymentvia.requestFocus();
        } else if (cb_trans_paymenttype.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Select Payment Type", "Save Warning", JOptionPane.WARNING_MESSAGE);
            cb_trans_paymenttype.requestFocus();
        } else if (cb_trans_paymenttype.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Select Payment Type", "Save Warning", JOptionPane.WARNING_MESSAGE);
            cb_trans_paymenttype.requestFocus();
        } else {
            TableModel tabelModel;
            tabelModel = tabelShoppingCart.getModel();
            DefaultTableModel model = (DefaultTableModel) tabelShoppingCart.getModel();
            baris = model.getRowCount();

            Entity_Transaction ETR = new Entity_Transaction();
            ETR.setTrans_id(txt_trans_invoiceno.getText());
            ETR.setTrans_date(dtTrans_date.getDate());
            ETR.setTrans_processBy(txt_Trans_Admin.getText());
            ETR.setTrans_cust_id(txt_trans_customerid.getText());
            ETR.setTrans_paymentVia((String) cb_trans_paymentvia.getSelectedItem());
            ETR.setTrans_NTA(NTA);
            ETR.setTrans_totalPayment(grandTotal);
            ETR.setVou_id(kodeVoucher);
            ETR.setTrans_discount(discount);
            ETR.setTrans_paymentType(paymentType);
            ETR.setBank_id(idBank);
            ETR.setTrans_paymentDesc(paymentDesc);
            ETR.setTrans_status("Pending");
            GetQrCode_Trans();
            ETR.setTrans_qr(qr_image);
            try {
                hasilsaveTrans = transactionDao.saveTransaksi(ETR);

            } catch (RemoteException ex) {
                Logger.getLogger(HomePageClientCashier.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

            for (int i = 0; i < baris; i++) {
                ETR.setTrans_id(txt_trans_invoiceno.getText());
                ETR.setItem_id(model.getValueAt(i, 0).toString());
                ETR.setItem_name(model.getValueAt(i, 1).toString());
                ETR.setItem_qty_string(model.getValueAt(i, 3).toString());
                ETR.setItem_price_string(model.getValueAt(i, 2).toString());
                ETR.setItem_subtotal_string(model.getValueAt(i, 4).toString());
                try {
                    hasilsaveTransDetail = transactionDao.saveTransaksi_Detail(ETR);

                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientCashier.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }

            Entity_Piutang EP = new Entity_Piutang();
            EP.setPiutang_id(idPiutang);
            EP.setPiutang_date(dtTrans_date.getDate());
            EP.setPiutang_transaction(txt_trans_invoiceno.getText());
            EP.setPiutang_total(grandTotal);
            EP.setPiutang_by_status("Not Paid");
            try {
                hasilsavePiutang = transactionDao.savePiutang(EP);

            } catch (RemoteException ex) {
                Logger.getLogger(HomePageClientCashier.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

            Entity_Distributor ED = new Entity_Distributor();
            ED.setDis_last_trans(dtTrans_date.getDate());
            ED.setDis_id(txt_trans_customerid.getText());
            try {
                hasilupdateDisLastTrans = transactionDao.updateDistributorLastTrans(ED);
            } catch (RemoteException ex) {
                Logger.getLogger(HomePageClientCashier.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

            if (hasilsaveTrans == 1 && hasilsaveTransDetail == 1 && hasilsavePiutang == 1) {
                JOptionPane.showMessageDialog(null, "Transaction Success. Now wait 4 hours until all item picked up by Warehouse. Thank You", "Save Success", JOptionPane.INFORMATION_MESSAGE);
                init_Transaction_ID();
                ClearFormTrans();
                PriceNTA = 0;
                PricePub = 0;
                PriceSingle = 0;
                JenisHarga = 0;
                qty = 0;
                NTA = 0;
                subTotalPublish = 0;
                subTotalNTA = 0;
                pricePublish = 0;
                pricepblh = 0;
                pricePublishmin = 0;
                discount = 0;
                grandTotal = 0;
                jumlahBarang = 0;
                NTAminus = 0;
                qtyMinus = 0;
                while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }
                Color softGray = new Color(153, 153, 153);
                txt_trans_nominal.setForeground(softGray);
            } else {
                JOptionPane.showMessageDialog(null, "Transaction Failed to Save", "Save Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btn_trans_processMouseClicked

    private void btn_trans_clearallMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_trans_clearallMouseClicked
        // TODO add your handling code here:
        ClearFormTrans();
        DefaultTableModel model = (DefaultTableModel) tabelShoppingCart.getModel();
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        Color softGray = new Color(153, 153, 153);
        txt_trans_nominal.setForeground(softGray);
    }//GEN-LAST:event_btn_trans_clearallMouseClicked

    private void cb_trans_paymentviaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_trans_paymentviaItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_trans_paymentviaItemStateChanged

    private void btn_trans_delitemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_trans_delitemMouseClicked
        // TODO add your handling code here:
        TableModel tabelModel;
        tabelModel = tabelShoppingCart.getModel();
        DefaultTableModel model = (DefaultTableModel) tabelShoppingCart.getModel();
        DecimalFormat df = new DecimalFormat(",###.00");
        pricePublish = 0;
        subTotalNTA = 0;
        PriceNTA = 0;
        qty = 0;
        discount = 0;
        grandTotal = 0;
        discountVoucher = 0;
        pricepblh = 0;
        int SelectedRowIndex = tabelShoppingCart.getSelectedRow();
        model.removeRow(SelectedRowIndex);
        int barisnya = model.getRowCount();
        for (int i = 0; i < barisnya; i++) {
            pricepblh = Integer.parseInt(model.getValueAt(i, 4).toString());
            pricePublish = pricePublish + pricepblh;
        }
        if (txt_trans_qty.getText().equals("")) {
            qty = 0;
        } else {
            qty = Integer.valueOf(txt_trans_qty.getText());
        }

        txt_trans_total_harga_publish.setText(String.valueOf(df.format(pricePublish)));
        NTA = NTA - NTAminus;
        txt_trans_TotalNTA.setText(String.valueOf(df.format(NTA)));
        discount = 0.01 * pricePublish;
        txt_price_disc.setText(String.valueOf(df.format(discount)));
        grandTotal = pricePublish - discount - discountVoucher;
        txt_trans_grand_total.setText(String.valueOf(df.format(grandTotal)));


    }//GEN-LAST:event_btn_trans_delitemMouseClicked

    private void tabelShoppingCartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelShoppingCartMouseClicked
        // TODO add your handling code here:
        int row;
        int hasilNTA = 0;
        row = tabelShoppingCart.rowAtPoint(evt.getPoint());

        String iditem = tabelShoppingCart.getValueAt(row, 0).toString();
//        System.out.println(iditem);
        qtyMinus = Integer.valueOf(tabelShoppingCart.getValueAt(row, 3).toString());
//        System.out.println(qtyMinus);

        Entity_Transaction ETR = new Entity_Transaction();
        ETR.setItem_id(iditem);
        try {
            hasilNTA = transactionDao.getNTA(ETR);

        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        NTAminus = hasilNTA * qtyMinus;
        System.out.println(NTAminus);
    }//GEN-LAST:event_tabelShoppingCartMouseClicked

    private void btn_trans_newMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_trans_newMouseClicked
        // TODO add your handling code here:.
        Object[] options = {"Yes", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "The last record will be not recorder, Are you sure?", "Info",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                init_Transaction_ID();
                ClearFormTrans();
                DefaultTableModel model = (DefaultTableModel) tabelShoppingCart.getModel();
                while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }
                Color softGray = new Color(153, 153, 153);
                txt_trans_nominal.setForeground(softGray);
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btn_trans_newMouseClicked

    private void btn_trans_process1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_trans_process1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_trans_process1MouseClicked

    private void btn_trans_clearall1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_trans_clearall1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_trans_clearall1MouseClicked

    private void txt_trans_searchitemByNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_trans_searchitemByNameMouseClicked
        // TODO add your handling code here:
        txt_trans_searchitemByID.setText("Please enter to search ...");
        txt_trans_searchitemByName.setText("");
        txt_trans_searchitemByCategory.setText("Please enter to search ...");
        txt_trans_searchitemByBrand.setText("Please enter to search ...");
    }//GEN-LAST:event_txt_trans_searchitemByNameMouseClicked

    private void txt_trans_searchitemByNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_trans_searchitemByNameKeyPressed
        // TODO add your handling code here:
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            initTabelDaftarItem();
            loadDaftarItembyName();
        }
    }//GEN-LAST:event_txt_trans_searchitemByNameKeyPressed

    private void txt_trans_searchitemByBrandMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_trans_searchitemByBrandMouseClicked
        // TODO add your handling code here:     
        txt_trans_searchitemByID.setText("Please enter to search ...");
        txt_trans_searchitemByName.setText("Please enter to search ...");
        txt_trans_searchitemByCategory.setText("Please enter to search ...");
        txt_trans_searchitemByBrand.setText("");
    }//GEN-LAST:event_txt_trans_searchitemByBrandMouseClicked

    private void txt_trans_searchitemByBrandKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_trans_searchitemByBrandKeyPressed
        // TODO add your handling code here:
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            initTabelDaftarItem();
            loadDaftarItembyBrand();
        }
    }//GEN-LAST:event_txt_trans_searchitemByBrandKeyPressed

    private void txt_trans_searchitemByIDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_trans_searchitemByIDMouseClicked
        // TODO add your handling code here:
        txt_trans_searchitemByID.setText("");
        txt_trans_searchitemByName.setText("Please enter to search ...");
        txt_trans_searchitemByCategory.setText("Please enter to search ...");
        txt_trans_searchitemByBrand.setText("Please enter to search ...");
    }//GEN-LAST:event_txt_trans_searchitemByIDMouseClicked

    private void txt_trans_searchitemByIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_trans_searchitemByIDKeyPressed
        // TODO add your handling code here:
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            initTabelDaftarItem();
            loadDaftarItembyID();
        }
    }//GEN-LAST:event_txt_trans_searchitemByIDKeyPressed

    private void txt_trans_searchitemByCategoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_trans_searchitemByCategoryMouseClicked
        // TODO add your handling code here:
        txt_trans_searchitemByID.setText("Please enter to search ...");
        txt_trans_searchitemByName.setText("Please enter to search ...");
        txt_trans_searchitemByCategory.setText("");
        txt_trans_searchitemByBrand.setText("Please enter to search ...");
    }//GEN-LAST:event_txt_trans_searchitemByCategoryMouseClicked

    private void txt_trans_searchitemByCategoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_trans_searchitemByCategoryKeyPressed
        // TODO add your handling code here:
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            initTabelDaftarItem();
            loadDaftarItembyCategory();
        }
    }//GEN-LAST:event_txt_trans_searchitemByCategoryKeyPressed

    private void tabelItemListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelItemListMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabelItemListMouseClicked

    private void txt_trans_searchByAdminKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_trans_searchByAdminKeyPressed
        // TODO add your handling code here:
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            initTabelTransList();
            loadDaftarTransactionbyAdmin();
        }
    }//GEN-LAST:event_txt_trans_searchByAdminKeyPressed

    private void txt_trans_searchByAdminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_trans_searchByAdminMouseClicked
        // TODO add your handling code here:
        txt_trans_searchByAdmin.setText("");
        txt_trans_searchByDis.setText("Please enter to search ...");
        txt_trans_searchByID.setText("Please enter to search ...");
        txt_trans_searchByDate.setText("Please enter to search ...");
    }//GEN-LAST:event_txt_trans_searchByAdminMouseClicked

    private void txt_trans_searchByDisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_trans_searchByDisKeyPressed
        // TODO add your handling code here:
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            initTabelTransList();
            loadDaftarTransactionbyIDCust();
        }
    }//GEN-LAST:event_txt_trans_searchByDisKeyPressed

    private void txt_trans_searchByDisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_trans_searchByDisMouseClicked
        // TODO add your handling code here:
        txt_trans_searchByDis.setText("");
        txt_trans_searchByID.setText("Please enter to search ...");
        txt_trans_searchByDate.setText("Please enter to search ...");
        txt_trans_searchByAdmin.setText("Please enter to search ...");
    }//GEN-LAST:event_txt_trans_searchByDisMouseClicked

    private void txt_trans_searchitemByNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_trans_searchitemByNameKeyTyped
        // TODO add your handling code here:
        initTabelDaftarItem();
        loadDaftarItembyName();
    }//GEN-LAST:event_txt_trans_searchitemByNameKeyTyped

    private void txt_trans_searchitemByIDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_trans_searchitemByIDKeyTyped
        // TODO add your handling code here:
        initTabelDaftarItem();
        loadDaftarItembyID();
    }//GEN-LAST:event_txt_trans_searchitemByIDKeyTyped

    private void txt_trans_searchitemByCategoryKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_trans_searchitemByCategoryKeyTyped
        // TODO add your handling code here:
        initTabelDaftarItem();
        loadDaftarItembyCategory();
    }//GEN-LAST:event_txt_trans_searchitemByCategoryKeyTyped

    private void txt_trans_searchitemByBrandKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_trans_searchitemByBrandKeyTyped
        // TODO add your handling code here:
        initTabelDaftarItem();
        loadDaftarItembyBrand();
    }//GEN-LAST:event_txt_trans_searchitemByBrandKeyTyped

    private void txt_trans_searchByIDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_trans_searchByIDKeyTyped
        // TODO add your handling code here:
        initTabelTransList();
        loadDaftarTransactionbyIDTrans();
    }//GEN-LAST:event_txt_trans_searchByIDKeyTyped

    private void txt_trans_searchByDisKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_trans_searchByDisKeyTyped
        // TODO add your handling code here:
        initTabelTransList();
        loadDaftarTransactionbyIDCust();
    }//GEN-LAST:event_txt_trans_searchByDisKeyTyped

    private void txt_trans_searchByAdminKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_trans_searchByAdminKeyTyped
        // TODO add your handling code here:
        initTabelTransList();
        loadDaftarTransactionbyAdmin();
    }//GEN-LAST:event_txt_trans_searchByAdminKeyTyped

    private void iconHome2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconHome2MouseClicked
        // TODO add your handling code here:
        Mode2();
    }//GEN-LAST:event_iconHome2MouseClicked

    private void menuCasSimulationMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuCasSimulationMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuCasSimulation.setBackground(lightGray);
    }//GEN-LAST:event_menuCasSimulationMouseExited

    private void menuCasSimulationMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuCasSimulationMouseEntered
        // TODO add your handling code here:
        Color darkGreen = new Color(24, 165, 93);
        menuCasSimulation.setBackground(darkGreen);
    }//GEN-LAST:event_menuCasSimulationMouseEntered

    private void iconCasSimulationMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconCasSimulationMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuCasSimulation.setBackground(lightGray);
    }//GEN-LAST:event_iconCasSimulationMouseExited

    private void iconCasSimulationMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconCasSimulationMouseEntered
        // TODO add your handling code here:
        Color darkGreen = new Color(24, 165, 93);
        menuCasSimulation.setBackground(darkGreen);
    }//GEN-LAST:event_iconCasSimulationMouseEntered

    private void iconCasSimulationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconCasSimulationMouseClicked
        // TODO add your handling code here:
        Mode21();
        loadComboBoxMotherboard();
        loadComboBoxSSD();
        loadComboBoxHDD();
        loadComboBoxRAM();
        loadComboBoxVGA();
        loadComboBoxCasing();
        loadComboBoxPSU();
        loadComboBoxLCD();
        loadComboBoxOptical();
        loadComboBoxKeyboard();
        loadComboBoxSpeaker();
        loadComboBoxHeadset();
        loadComboBoxCPUCooler();
        loadComboBoxCoolerFan();
        loadComboBoxNetworking();
        init_Simulation_ID();
        last_session = 21;
    }//GEN-LAST:event_iconCasSimulationMouseClicked

    private void ScrollPaneSimulasiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ScrollPaneSimulasiMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_ScrollPaneSimulasiMouseEntered

    private void ScrollPaneSimulasiMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_ScrollPaneSimulasiMouseWheelMoved
        // TODO add your handling code here:
        subpanelCasSimulasi.setAutoscrolls(true);
        ScrollPaneSimulasi.setAutoscrolls(true);
        ScrollPaneSimulasi.getVerticalScrollBar().setUnitIncrement(40);
    }//GEN-LAST:event_ScrollPaneSimulasiMouseWheelMoved

    private void btn_trans_process_simMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_trans_process_simMouseClicked
        // TODO add your handling code here:
        if (dt_simulation.getDate().equals("")) {
            JOptionPane.showMessageDialog(null, "Choose Simulation Date", "Simulation Info", JOptionPane.WARNING_MESSAGE);
        } else {
            TableModel tabelModel;
            tabelModel = tabel_item_simulation.getModel();
            DefaultTableModel model = (DefaultTableModel) tabel_item_simulation.getModel();
            baris = model.getRowCount();

            Entity_Simulation ES = new Entity_Simulation();
            ES.setSim_id(txt_trans_simulation_id.getText());
            ES.setSim_date(dt_simulation.getDate());
            ES.setSim_added_by(txt_Trans_Simulation_Admin.getText());
            ES.setSim_grand_total(grandtotalSim);

            try {
                hasilsaveSim = simulationDao.saveSimulasi(ES);
            } catch (RemoteException ex) {
                Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
            }

            for (int i = 0; i < baris; i++) {
                ES.setSim_id(txt_trans_simulation_id.getText());
                ES.setSim_item_name(model.getValueAt(i, 0).toString());
                ES.setSim_item_price(Integer.valueOf(model.getValueAt(i, 1).toString()));
                try {
                    hasilsaveSimDetail = simulationDao.saveSimulationDetail(ES);
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (hasilsaveSim == 1 && hasilsaveSimDetail == 1) {
                JOptionPane.showMessageDialog(null, "Simulation Success. Thank You", "Save Success", JOptionPane.INFORMATION_MESSAGE);
                ClearFormSimulation();
                init_Simulation_ID();
                while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Transaction Failed to Save", "Save Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btn_trans_process_simMouseClicked

    private void btn_trans_clearall_simMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_trans_clearall_simMouseClicked
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tabel_item_simulation.getModel();
        ClearFormSimulation();
        init_Simulation_ID();
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
    }//GEN-LAST:event_btn_trans_clearall_simMouseClicked

    private void btn_trans_new_simMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_trans_new_simMouseClicked
        // TODO add your handling code here:
        Object[] options = {"Yes", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "The last record will be not recorder, Are you sure?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                DefaultTableModel model = (DefaultTableModel) tabel_item_simulation.getModel();
                ClearFormSimulation();
                init_Simulation_ID();
                while (model.getRowCount() > 0) {
                    model.removeRow(0);
                }
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btn_trans_new_simMouseClicked

    private void cb_trans_sim_networkingItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_trans_sim_networkingItemStateChanged
        // TODO add your handling code here:
        DecimalFormat df = new DecimalFormat("IDR ,###.00");
        if (evt.getStateChange() == evt.SELECTED) {
            if (cb_trans_sim_networking.getSelectedIndex() == 0) {
                hargaSimNetworking = 0;
            } else {
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name((String) cb_trans_sim_networking.getSelectedItem());
                try {
                    hargaSimNetworking = transactionDao.getHargaNetworking(ETR);
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
                }
                txt_trans_sim_price_networking.setText(df.format(hargaSimNetworking));
            }
        }
    }//GEN-LAST:event_cb_trans_sim_networkingItemStateChanged

    private void cb_trans_sim_coolerfanItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_trans_sim_coolerfanItemStateChanged
        // TODO add your handling code here:
        DecimalFormat df = new DecimalFormat("IDR ,###.00");
        if (evt.getStateChange() == evt.SELECTED) {
            if (cb_trans_sim_coolerfan.getSelectedIndex() == 0) {
                hargaSimCoolerFan = 0;
            } else {
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name((String) cb_trans_sim_coolerfan.getSelectedItem());
                try {
                    hargaSimCoolerFan = transactionDao.getHargaCoolerFan(ETR);
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
                }
                txt_trans_sim_price_coolerfan.setText(df.format(hargaSimCoolerFan));
            }
        }
    }//GEN-LAST:event_cb_trans_sim_coolerfanItemStateChanged

    private void cb_trans_sim_cpucoolerItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_trans_sim_cpucoolerItemStateChanged
        // TODO add your handling code here:
        DecimalFormat df = new DecimalFormat("IDR ,###.00");
        if (evt.getStateChange() == evt.SELECTED) {
            if (cb_trans_sim_cpucooler.getSelectedIndex() == 0) {
                hargaSimCpuCooler = 0;
            } else {
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name((String) cb_trans_sim_cpucooler.getSelectedItem());
                try {
                    hargaSimCpuCooler = transactionDao.getHargaCPUCooler(ETR);
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
                }
                txt_trans_sim_price_cpucooler.setText(df.format(hargaSimCpuCooler));
            }
        }
    }//GEN-LAST:event_cb_trans_sim_cpucoolerItemStateChanged

    private void cb_trans_sim_headsetItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_trans_sim_headsetItemStateChanged
        // TODO add your handling code here:
        DecimalFormat df = new DecimalFormat("IDR ,###.00");
        if (evt.getStateChange() == evt.SELECTED) {
            if (cb_trans_sim_headset.getSelectedIndex() == 0) {
                hargaSimHeadset = 0;
            } else {
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name((String) cb_trans_sim_headset.getSelectedItem());
                try {
                    hargaSimHeadset = transactionDao.getHargaHeadset(ETR);
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
                }
                txt_trans_sim_price_headset.setText(df.format(hargaSimHeadset));
            }
        }
    }//GEN-LAST:event_cb_trans_sim_headsetItemStateChanged

    private void cb_trans_sim_speakerItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_trans_sim_speakerItemStateChanged
        // TODO add your handling code here:
        DecimalFormat df = new DecimalFormat("IDR ,###.00");
        if (evt.getStateChange() == evt.SELECTED) {
            if (cb_trans_sim_speaker.getSelectedIndex() == 0) {
                hargaSimSpeaker = 0;
            } else {
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name((String) cb_trans_sim_speaker.getSelectedItem());
                try {
                    hargaSimSpeaker = transactionDao.getHargaSpeaker(ETR);
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
                }
                txt_trans_sim_price_speaker.setText(df.format(hargaSimSpeaker));
            }
        }
    }//GEN-LAST:event_cb_trans_sim_speakerItemStateChanged

    private void cb_trans_sim_keyboardItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_trans_sim_keyboardItemStateChanged
        // TODO add your handling code here:
        DecimalFormat df = new DecimalFormat("IDR ,###.00");
        if (evt.getStateChange() == evt.SELECTED) {
            if (cb_trans_sim_keyboard.getSelectedIndex() == 0) {
                hargaSimKeyboard = 0;
            } else {
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name((String) cb_trans_sim_keyboard.getSelectedItem());
                try {
                    hargaSimKeyboard = transactionDao.getHargaKeyboard(ETR);
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
                }
                txt_trans_sim_price_keyboard.setText(df.format(hargaSimKeyboard));
            }
        }
    }//GEN-LAST:event_cb_trans_sim_keyboardItemStateChanged

    private void cb_trans_sim_opticalItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_trans_sim_opticalItemStateChanged
        // TODO add your handling code here:
        DecimalFormat df = new DecimalFormat("IDR ,###.00");
        if (evt.getStateChange() == evt.SELECTED) {
            if (cb_trans_sim_optical.getSelectedIndex() == 0) {
                hargaSimOptical = 0;
            } else {
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name((String) cb_trans_sim_optical.getSelectedItem());
                try {
                    hargaSimOptical = transactionDao.getHargaOptical(ETR);
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
                }
                txt_trans_sim_price_optical.setText(df.format(hargaSimOptical));
            }
        }
    }//GEN-LAST:event_cb_trans_sim_opticalItemStateChanged

    private void cb_trans_sim_lcdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_trans_sim_lcdItemStateChanged
        // TODO add your handling code here:
        DecimalFormat df = new DecimalFormat("IDR ,###.00");
        if (evt.getStateChange() == evt.SELECTED) {
            if (cb_trans_sim_lcd.getSelectedIndex() == 0) {
                hargaSimLCD = 0;
            } else {
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name((String) cb_trans_sim_lcd.getSelectedItem());
                try {
                    hargaSimLCD = transactionDao.getHargaLCD(ETR);
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
                }
                txt_trans_sim_price_lcd.setText(df.format(hargaSimLCD));
            }
        }
    }//GEN-LAST:event_cb_trans_sim_lcdItemStateChanged

    private void cb_trans_sim_psuItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_trans_sim_psuItemStateChanged
        // TODO add your handling code here:
        DecimalFormat df = new DecimalFormat("IDR ,###.00");
        if (evt.getStateChange() == evt.SELECTED) {
            if (cb_trans_sim_psu.getSelectedIndex() == 0) {
                hargaSimPSU = 0;
            } else {
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name((String) cb_trans_sim_psu.getSelectedItem());
                try {
                    hargaSimPSU = transactionDao.getHargaPSU(ETR);
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
                }
                txt_trans_sim_price_psu.setText(df.format(hargaSimPSU));
            }
        }
    }//GEN-LAST:event_cb_trans_sim_psuItemStateChanged

    private void cb_trans_sim_casingItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_trans_sim_casingItemStateChanged
        // TODO add your handling code here:
        DecimalFormat df = new DecimalFormat("IDR ,###.00");
        if (evt.getStateChange() == evt.SELECTED) {
            if (cb_trans_sim_casing.getSelectedIndex() == 0) {
                hargaSimCasing = 0;
            } else {
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name((String) cb_trans_sim_casing.getSelectedItem());
                try {
                    hargaSimCasing = transactionDao.getHargaCasing(ETR);
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
                }
                txt_trans_sim_price_casing.setText(df.format(hargaSimCasing));
            }
        }
    }//GEN-LAST:event_cb_trans_sim_casingItemStateChanged

    private void cb_trans_sim_vgaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_trans_sim_vgaItemStateChanged
        // TODO add your handling code here:
        DecimalFormat df = new DecimalFormat("IDR ,###.00");
        if (evt.getStateChange() == evt.SELECTED) {
            if (cb_trans_sim_vga.getSelectedIndex() == 0) {
                hargaSimVGA = 0;
            } else {
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name((String) cb_trans_sim_vga.getSelectedItem());
                try {
                    hargaSimVGA = transactionDao.getHargaVGA(ETR);
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
                }
                txt_trans_sim_price_vga.setText(df.format(hargaSimVGA));
            }
        }
    }//GEN-LAST:event_cb_trans_sim_vgaItemStateChanged

    private void cb_trans_sim_ramItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_trans_sim_ramItemStateChanged
        // TODO add your handling code here:
        DecimalFormat df = new DecimalFormat("IDR ,###.00");
        if (evt.getStateChange() == evt.SELECTED) {
            if (cb_trans_sim_ram.getSelectedIndex() == 0) {
                hargaSimRAM = 0;
            } else {
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name((String) cb_trans_sim_ram.getSelectedItem());
                try {
                    hargaSimRAM = transactionDao.getHargaRAM(ETR);
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
                }
                txt_trans_sim_price_ram.setText(df.format(hargaSimRAM));
            }
        }
    }//GEN-LAST:event_cb_trans_sim_ramItemStateChanged

    private void cb_trans_sim_hddItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_trans_sim_hddItemStateChanged
        // TODO add your handling code here:
        DecimalFormat df = new DecimalFormat("IDR ,###.00");
        if (evt.getStateChange() == evt.SELECTED) {
            if (cb_trans_sim_hdd.getSelectedIndex() == 0) {
                hargaSimHDD = 0;
            } else {
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name((String) cb_trans_sim_hdd.getSelectedItem());
                try {
                    hargaSimHDD = transactionDao.getHargaHDD(ETR);
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
                }
                txt_trans_sim_price_hdd.setText(df.format(hargaSimHDD));
            }
        }
    }//GEN-LAST:event_cb_trans_sim_hddItemStateChanged

    private void cb_trans_sim_ssdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_trans_sim_ssdItemStateChanged
        // TODO add your handling code here:
        DecimalFormat df = new DecimalFormat("IDR ,###.00");
        if (evt.getStateChange() == evt.SELECTED) {
            if (cb_trans_sim_ssd.getSelectedIndex() == 0) {
                hargaSimSSD = 0;
            } else {
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name((String) cb_trans_sim_ssd.getSelectedItem());
                try {
                    hargaSimSSD = transactionDao.getHargaSSD(ETR);
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
                }
                txt_trans_sim_price_ssd.setText(df.format(hargaSimSSD));
            }
        }
    }//GEN-LAST:event_cb_trans_sim_ssdItemStateChanged

    private void cb_trans_sim_motherboardItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_trans_sim_motherboardItemStateChanged
        // TODO add your handling code here:
        DecimalFormat df = new DecimalFormat("IDR ,###.00");
        if (evt.getStateChange() == evt.SELECTED) {
            if (cb_trans_sim_motherboard.getSelectedIndex() == 0) {
                hargaSimMotherboard = 0;
            } else {
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name((String) cb_trans_sim_motherboard.getSelectedItem());
                try {
                    hargaSimMotherboard = transactionDao.getHargaMotherboard(ETR);
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
                }
                txt_trans_sim_price_motherboard.setText(df.format(hargaSimMotherboard));
            }
        }
    }//GEN-LAST:event_cb_trans_sim_motherboardItemStateChanged

    private void cb_trans_sim_processor_intelItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_trans_sim_processor_intelItemStateChanged
        // TODO add your handling code here:
        DecimalFormat df = new DecimalFormat("IDR ,###.00");
        if (evt.getStateChange() == evt.SELECTED) {
            if (cb_trans_sim_processor_intel.getSelectedIndex() == 0) {
                hargaSimProcIntel = 0;
            } else {
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name((String) cb_trans_sim_processor_intel.getSelectedItem());
                try {
                    hargaSimProcIntel = transactionDao.getHargaProcIntel(ETR);
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
                }
                txt_trans_sim_price_processor.setText(df.format(hargaSimProcIntel));
            }
        }
    }//GEN-LAST:event_cb_trans_sim_processor_intelItemStateChanged

    private void cb_trans_sim_processor_amdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_trans_sim_processor_amdItemStateChanged
        // TODO add your handling code here:
        DecimalFormat df = new DecimalFormat("IDR ,###.00");
        if (evt.getStateChange() == evt.SELECTED) {
            if (cb_trans_sim_processor_amd.getSelectedIndex() == 0) {
                hargaSimProcAMD = 0;
            } else {
                Entity_Transaction ETR = new Entity_Transaction();
                ETR.setItem_name((String) cb_trans_sim_processor_amd.getSelectedItem());
                try {
                    hargaSimProcAMD = transactionDao.getHargaProcAMD(ETR);
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
                }
                txt_trans_sim_price_processor.setText(df.format(hargaSimProcAMD));
            }
        }
    }//GEN-LAST:event_cb_trans_sim_processor_amdItemStateChanged

    private void cb_trans_sim_brandprocessorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_trans_sim_brandprocessorItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == evt.SELECTED) {
            if (cb_trans_sim_brandprocessor.getSelectedIndex() == 1) {
                cb_trans_sim_processor_amd.setVisible(true);
                cb_trans_sim_processor_intel.setVisible(false);
                modeSimulation = "AMD";
                loadComboBoxProcessorAMD();
            } else if (cb_trans_sim_brandprocessor.getSelectedIndex() == 2) {
                cb_trans_sim_processor_amd.setVisible(false);
                cb_trans_sim_processor_intel.setVisible(true);
                modeSimulation = "Intel";
                loadComboBoxProcessorIntel();
            }
        }
    }//GEN-LAST:event_cb_trans_sim_brandprocessorItemStateChanged

    private void btn_trans_lock_simulationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_trans_lock_simulationMouseClicked
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tabel_item_simulation.getModel();
        model.setRowCount(0);
        DecimalFormat df = new DecimalFormat("IDR ,###.00");
        grandtotalSim = hargaSimProcAMD + hargaSimProcIntel + hargaSimMotherboard + hargaSimSSD + hargaSimHDD + hargaSimRAM + hargaSimVGA + hargaSimCasing + hargaSimPSU
                + hargaSimLCD + hargaSimOptical + hargaSimKeyboard + hargaSimSpeaker + hargaSimHeadset + hargaSimCpuCooler + hargaSimCoolerFan + hargaSimNetworking;

        txt_trans_simulation_grandtotal.setText(df.format(grandtotalSim));
        if (modeSimulation == "AMD") {
            model.addRow(new Object[]{cb_trans_sim_processor_amd.getSelectedItem(), hargaSimProcAMD});
        } else if (modeSimulation == "Intel") {
            model.addRow(new Object[]{cb_trans_sim_processor_intel.getSelectedItem(), hargaSimProcIntel});
        }

        if (cb_trans_sim_motherboard.getSelectedIndex() == 0) {
            //donothing
        } else {
            model.addRow(new Object[]{cb_trans_sim_motherboard.getSelectedItem(), hargaSimMotherboard});
        }

        if (cb_trans_sim_ssd.getSelectedIndex() == 0) {
            //donothing
        } else {
            model.addRow(new Object[]{cb_trans_sim_ssd.getSelectedItem(), hargaSimSSD});
        }

        if (cb_trans_sim_hdd.getSelectedIndex() == 0) {
            //donothing
        } else {
            model.addRow(new Object[]{cb_trans_sim_hdd.getSelectedItem(), hargaSimHDD});
        }

        if (cb_trans_sim_ram.getSelectedIndex() == 0) {
            //donothing
        } else {
            model.addRow(new Object[]{cb_trans_sim_ram.getSelectedItem(), hargaSimRAM});
        }

        if (cb_trans_sim_vga.getSelectedIndex() == 0) {
            //donothing
        } else {
            model.addRow(new Object[]{cb_trans_sim_vga.getSelectedItem(), hargaSimVGA});
        }

        if (cb_trans_sim_casing.getSelectedIndex() == 0) {
            //donothing
        } else {
            model.addRow(new Object[]{cb_trans_sim_casing.getSelectedItem(), hargaSimCasing});
        }

        if (cb_trans_sim_psu.getSelectedIndex() == 0) {
            //donothing
        } else {
            model.addRow(new Object[]{cb_trans_sim_psu.getSelectedItem(), hargaSimPSU});
        }

        if (cb_trans_sim_lcd.getSelectedIndex() == 0) {
            //donothing
        } else {
            model.addRow(new Object[]{cb_trans_sim_lcd.getSelectedItem(), hargaSimLCD});
        }
        if (cb_trans_sim_optical.getSelectedIndex() == 0) {
            //donothing
        } else {
            model.addRow(new Object[]{cb_trans_sim_optical.getSelectedItem(), hargaSimOptical});
        }
        if (cb_trans_sim_keyboard.getSelectedIndex() == 0) {
            //donothing
        } else {
            model.addRow(new Object[]{cb_trans_sim_keyboard.getSelectedItem(), hargaSimKeyboard});
        }

        if (cb_trans_sim_speaker.getSelectedIndex() == 0) {
            //donothing
        } else {
            model.addRow(new Object[]{cb_trans_sim_speaker.getSelectedItem(), hargaSimSpeaker});
        }

        if (cb_trans_sim_headset.getSelectedIndex() == 0) {
            //donothing
        } else {
            model.addRow(new Object[]{cb_trans_sim_headset.getSelectedItem(), hargaSimHeadset});
        }

        if (cb_trans_sim_cpucooler.getSelectedIndex() == 0) {
            //donothing
        } else {
            model.addRow(new Object[]{cb_trans_sim_cpucooler.getSelectedItem(), hargaSimCpuCooler});
        }

        if (cb_trans_sim_coolerfan.getSelectedIndex() == 0) {
            //donothing
        } else {
            model.addRow(new Object[]{cb_trans_sim_coolerfan.getSelectedItem(), hargaSimCoolerFan});
        }

        if (cb_trans_sim_networking.getSelectedIndex() == 0) {
            //donothing
        } else {
            model.addRow(new Object[]{cb_trans_sim_networking.getSelectedItem(), hargaSimNetworking});
        }
    }//GEN-LAST:event_btn_trans_lock_simulationMouseClicked

    private void btn_printTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_printTransActionPerformed
        // TODO add your handling code here:
        if (dataTerpilihTabelTrans.equals(null)) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Choose Transaction From Table", "Info", JOptionPane.WARNING_MESSAGE);
        } else {
            Object[] options = {"Yes", "Cancel"};
            int reply = JOptionPane.showOptionDialog(null, "Print Transaction ID " + dataTerpilihTabelTrans, "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            switch (reply) {
                case 0:
                    try {
                        JasperDesign jd = JRXmlLoader.load("src/nob/files/InvoiceTransaction.jrxml");
                        JasperReport jr = JasperCompileManager.compileReport(jd);
                        HashMap parameter = new HashMap();
                        parameter.put("NoInvoice", dataTerpilihTabelTrans);

                        JasperPrint jp = JasperFillManager.fillReport(jr, parameter, DriverManager.getConnection("jdbc:sqlserver://"+configuration.ip_server+":1433;DatabaseName=NOBTech", "sa", "aditya12345"));
                        JasperViewer.viewReport(jp, false);

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                    }
                    break;
                default:
                    break;
            }
        }
    }//GEN-LAST:event_btn_printTransActionPerformed

    private void iconBOBigMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconBOBigMouseClicked
        // TODO add your handling code here:
        Mode2();
    }//GEN-LAST:event_iconBOBigMouseClicked

    private void iconManagementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconManagementMouseClicked
        // TODO add your handling code here:
        HomePageClientManagement HPC = new HomePageClientManagement();
        HPC.setVisible(true);
        HPC.Mode5();
        dispose();
    }//GEN-LAST:event_iconManagementMouseClicked

    private void iconKeuanganMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconKeuanganMouseClicked
        // TODO add your handling code here:
        HomePageClientKeuangan HPCKEU = new HomePageClientKeuangan();
        HPCKEU.setVisible(true);
        HPCKEU.Mode2();
        dispose();
    }//GEN-LAST:event_iconKeuanganMouseClicked

    private void iconWarehouseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWarehouseMouseClicked
        // TODO add your handling code here:
        HomePageClientWarehouse HPCW = new HomePageClientWarehouse();
        HPCW.setVisible(true);
        HPCW.Mode1();
        dispose();
    }//GEN-LAST:event_iconWarehouseMouseClicked

    private void iconHRDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconHRDMouseClicked
        // TODO add your handling code here:
        HomePageClientHRD HPCHRD = new HomePageClientHRD();
        HPCHRD.setVisible(true);
        HPCHRD.Mode1();
        dispose();
    }//GEN-LAST:event_iconHRDMouseClicked

    private void iconSIMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconSIMMouseClicked
        // TODO add your handling code here:
        HomePageClientSIM HPCSIM = new HomePageClientSIM();
        HPCSIM.setVisible(true);
        HPCSIM.Mode1();
        dispose();
    }//GEN-LAST:event_iconSIMMouseClicked

    private void iconFOBigMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconFOBigMouseClicked
        // TODO add your handling code here:
        Mode2();
    }//GEN-LAST:event_iconFOBigMouseClicked

    private void iconCashierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconCashierMouseClicked
        // TODO add your handling code here:
        Mode13();
    }//GEN-LAST:event_iconCashierMouseClicked

    private void iconSIMFOMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconSIMFOMouseClicked
        // TODO add your handling code here:
        HomePageClientSIM HPC3 = new HomePageClientSIM();
        HPC3.setVisible(true);
        HPC3.Mode1();
        dispose();
    }//GEN-LAST:event_iconSIMFOMouseClicked

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
            java.util.logging.Logger.getLogger(HomePageClientCashier.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomePageClientCashier.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomePageClientCashier.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomePageClientCashier.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                new HomePageClientCashier().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane ScrollPaneAddTrans;
    private javax.swing.JScrollPane ScrollPaneSimulasi;
    private javax.swing.JLabel bgBackOffice;
    private javax.swing.JLabel bgCashier;
    private javax.swing.JLabel bgClientCashier;
    private javax.swing.JLabel bgClientCashierLogin;
    private javax.swing.JLabel bgDepartment;
    private javax.swing.JLabel bgFrontOffice;
    private javax.swing.JLabel bgLandingPage;
    private javax.swing.JLabel bgLogoCashier;
    private javax.swing.JLabel bgLogoNOB;
    private javax.swing.JButton btnChangeStateCas;
    private javax.swing.JLabel btnClearDistributor;
    private javax.swing.JLabel btnClearError_cas;
    private javax.swing.JButton btnHideIPCas;
    private javax.swing.JButton btnLoginCas;
    private javax.swing.JLabel btnLogoutCas;
    private javax.swing.JLabel btnMsgCas;
    private javax.swing.JLabel btnNotifCas;
    private javax.swing.JButton btnResetCas;
    private javax.swing.JLabel btnSaveDistributor;
    private javax.swing.JLabel btnSaveError_cas;
    private javax.swing.JButton btnShowIPCas;
    private javax.swing.JButton btn_Trans_getVoucher;
    private javax.swing.JButton btn_Trans_rmvVoucher;
    private javax.swing.JLabel btn_active_distributor;
    private javax.swing.JLabel btn_deactive_distributor;
    private javax.swing.JLabel btn_delete_error_cas;
    private javax.swing.JButton btn_printTrans;
    private javax.swing.JButton btn_trans_add_item;
    private javax.swing.JButton btn_trans_check_stock;
    private javax.swing.JButton btn_trans_clearall;
    private javax.swing.JButton btn_trans_clearall1;
    private javax.swing.JButton btn_trans_clearall_sim;
    private javax.swing.JLabel btn_trans_delitem;
    private javax.swing.JButton btn_trans_getQr;
    private javax.swing.JButton btn_trans_hide_NTAItem;
    private javax.swing.JButton btn_trans_hide_nta;
    private javax.swing.JButton btn_trans_lock_payment;
    private javax.swing.JButton btn_trans_lock_simulation;
    private javax.swing.JButton btn_trans_new;
    private javax.swing.JButton btn_trans_new_sim;
    private javax.swing.JButton btn_trans_process;
    private javax.swing.JButton btn_trans_process1;
    private javax.swing.JButton btn_trans_process_sim;
    private javax.swing.JButton btn_trans_show_NTAItem;
    private javax.swing.JButton btn_trans_show_nta;
    private javax.swing.ButtonGroup buttongroup_Gender;
    private javax.swing.JComboBox<String> cb_CustType;
    private javax.swing.JComboBox<String> cb_distributor_description;
    private javax.swing.JComboBox<String> cb_distributor_status;
    private javax.swing.JComboBox<String> cb_error_status_cas;
    private javax.swing.JComboBox<String> cb_spt;
    private javax.swing.JComboBox<String> cb_trans_bank;
    private javax.swing.JComboBox<String> cb_trans_paymenttype;
    private javax.swing.JComboBox<String> cb_trans_paymentvia;
    private javax.swing.JComboBox<String> cb_trans_piutang;
    private javax.swing.JComboBox<String> cb_trans_sim_brandprocessor;
    private javax.swing.JComboBox<String> cb_trans_sim_casing;
    private javax.swing.JComboBox<String> cb_trans_sim_coolerfan;
    private javax.swing.JComboBox<String> cb_trans_sim_cpucooler;
    private javax.swing.JComboBox<String> cb_trans_sim_hdd;
    private javax.swing.JComboBox<String> cb_trans_sim_headset;
    private javax.swing.JComboBox<String> cb_trans_sim_keyboard;
    private javax.swing.JComboBox<String> cb_trans_sim_lcd;
    private javax.swing.JComboBox<String> cb_trans_sim_motherboard;
    private javax.swing.JComboBox<String> cb_trans_sim_networking;
    private javax.swing.JComboBox<String> cb_trans_sim_optical;
    private javax.swing.JComboBox<String> cb_trans_sim_processor_amd;
    private javax.swing.JComboBox<String> cb_trans_sim_processor_intel;
    private javax.swing.JComboBox<String> cb_trans_sim_psu;
    private javax.swing.JComboBox<String> cb_trans_sim_ram;
    private javax.swing.JComboBox<String> cb_trans_sim_speaker;
    private javax.swing.JComboBox<String> cb_trans_sim_ssd;
    private javax.swing.JComboBox<String> cb_trans_sim_vga;
    private com.toedter.calendar.JDateChooser dtError_Date_cas;
    private com.toedter.calendar.JDateChooser dtTrans_date;
    private com.toedter.calendar.JDateChooser dt_simulation;
    private javax.swing.JLabel iconAdminCas;
    private javax.swing.JLabel iconBO;
    private javax.swing.JLabel iconBOBig;
    private javax.swing.JLabel iconCSR;
    private javax.swing.JLabel iconCSR1;
    private javax.swing.JLabel iconCasAddCustomer;
    private javax.swing.JLabel iconCasAddTrans;
    private javax.swing.JLabel iconCasDashboard;
    private javax.swing.JLabel iconCasDataTransaction;
    private javax.swing.JLabel iconCasErrorReport;
    private javax.swing.JLabel iconCasListItem;
    private javax.swing.JLabel iconCasSimulation;
    private javax.swing.JLabel iconCashier;
    private javax.swing.JLabel iconFO;
    private javax.swing.JLabel iconFOBig;
    private javax.swing.JLabel iconHRD;
    private javax.swing.JLabel iconHome2;
    private javax.swing.JLabel iconKeuangan;
    private javax.swing.JLabel iconManagement;
    private javax.swing.JLabel iconSIM;
    private javax.swing.JLabel iconSIMFO;
    private javax.swing.JLabel iconWarehouse;
    private javax.swing.JInternalFrame inframeQRCode;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JSeparator jSeparator21;
    private javax.swing.JSeparator jSeparator22;
    private javax.swing.JSeparator jSeparator23;
    private javax.swing.JSeparator jSeparator24;
    private javax.swing.JSeparator jSeparator25;
    private javax.swing.JSeparator jSeparator26;
    private javax.swing.JSeparator jSeparator27;
    private javax.swing.JSeparator jSeparator28;
    private javax.swing.JSeparator jSeparator29;
    private javax.swing.JSeparator jSeparator30;
    private javax.swing.JSeparator jSeparator31;
    private javax.swing.JSeparator jSeparator32;
    private javax.swing.JSeparator jSeparator33;
    private javax.swing.JSeparator jSeparator34;
    private javax.swing.JSeparator jSeparator35;
    private javax.swing.JSeparator jSeparator36;
    private javax.swing.JSeparator jSeparator37;
    private javax.swing.JSeparator jSeparator38;
    private javax.swing.JSeparator jSeparator39;
    private javax.swing.JSeparator jSeparator40;
    private javax.swing.JSeparator jSeparator41;
    private javax.swing.JSeparator jSeparator42;
    private javax.swing.JSeparator jSeparator43;
    private javax.swing.JSeparator jSeparator44;
    private javax.swing.JSeparator jSeparator45;
    private javax.swing.JSeparator jSeparator46;
    private javax.swing.JSeparator jSeparator47;
    private javax.swing.JSeparator jSeparator48;
    private javax.swing.JSeparator jSeparator51;
    private javax.swing.JSeparator jSeparator52;
    private javax.swing.JSeparator jSeparator53;
    private javax.swing.JSeparator jSeparator54;
    private javax.swing.JSeparator jSeparator55;
    private javax.swing.JSeparator jSeparator56;
    private javax.swing.JSeparator jSeparator57;
    private javax.swing.JSeparator jSeparator58;
    private javax.swing.JSeparator jSeparator59;
    private javax.swing.JSeparator jSeparator60;
    private javax.swing.JSeparator jSeparator79;
    private javax.swing.JLabel lblAddDistributorSubTitle;
    private javax.swing.JLabel lblDashboardTitle2;
    private javax.swing.JLabel lblDashboardTitle3;
    private javax.swing.JLabel lblDataKaryawanTitel1;
    private javax.swing.JLabel lblDataKaryawanTitel2;
    private javax.swing.JLabel lblDepartmentSubTitle10;
    private javax.swing.JLabel lblDistributor;
    private javax.swing.JLabel lblDistributortSubTitle;
    private javax.swing.JLabel lblErrorReportCasSubTitle;
    private javax.swing.JLabel lblErrorReportCasSubTitle1;
    private javax.swing.JLabel lblErrorReportTitle2;
    private javax.swing.JLabel lblErrorReportTitleCas;
    private javax.swing.JLabel lblIDR10;
    private javax.swing.JLabel lblIDR6;
    private javax.swing.JLabel lblIDR8;
    private javax.swing.JLabel lblIDR9;
    private javax.swing.JLabel lblListDistributor;
    private javax.swing.JLabel lblRp1;
    private javax.swing.JLabel lblRp2;
    private javax.swing.JLabel lblStateBigCas;
    private javax.swing.JLabel lblStateCas;
    private javax.swing.JLabel lblTranPiutang;
    private javax.swing.JLabel lblTransAdmin;
    private javax.swing.JLabel lblTransBank;
    private javax.swing.JLabel lblTransCreateDate;
    private javax.swing.JLabel lblTransCustCode;
    private javax.swing.JLabel lblTransCustEmail;
    private javax.swing.JLabel lblTransCustName;
    private javax.swing.JLabel lblTransCustPhone;
    private javax.swing.JLabel lblTransCustType;
    private javax.swing.JLabel lblTransDescription;
    private javax.swing.JLabel lblTransDiscount;
    private javax.swing.JLabel lblTransGrandTotal;
    private javax.swing.JLabel lblTransInvoiceNo;
    private javax.swing.JLabel lblTransInvoice_Confirm;
    private javax.swing.JLabel lblTransItemCode;
    private javax.swing.JLabel lblTransItemName;
    private javax.swing.JLabel lblTransItemQty;
    private javax.swing.JLabel lblTransItemQty2;
    private javax.swing.JLabel lblTransItemQty3;
    private javax.swing.JLabel lblTransNTAItem;
    private javax.swing.JLabel lblTransNominal;
    private javax.swing.JLabel lblTransPaymentType;
    private javax.swing.JLabel lblTransPaymentVia;
    private javax.swing.JLabel lblTransPriceDiscount;
    private javax.swing.JLabel lblTransPriceVoucher;
    private javax.swing.JLabel lblTransSellPriceType;
    private javax.swing.JLabel lblTransSimulationAdmin;
    private javax.swing.JLabel lblTransSimulationCreatedDate;
    private javax.swing.JLabel lblTransSimulationCreatedDate1;
    private javax.swing.JLabel lblTransSimulationGrandTotal;
    private javax.swing.JLabel lblTransSimulationID;
    private javax.swing.JLabel lblTransSimulationInputDataItem;
    private javax.swing.JLabel lblTransSimulationSubTitle;
    private javax.swing.JLabel lblTransSimulationTitle;
    private javax.swing.JLabel lblTransTotalNTA;
    private javax.swing.JLabel lblTransTotalPricePub;
    private javax.swing.JLabel lblTransVoucher;
    private javax.swing.JLabel lblTransVoucherCode;
    private javax.swing.JLabel lblTrans_Sim_BrandProcessor;
    private javax.swing.JLabel lblTrans_Sim_CPUCooler;
    private javax.swing.JLabel lblTrans_Sim_Casing;
    private javax.swing.JLabel lblTrans_Sim_CoolerFan;
    private javax.swing.JLabel lblTrans_Sim_HDD;
    private javax.swing.JLabel lblTrans_Sim_Headset;
    private javax.swing.JLabel lblTrans_Sim_Keyboard;
    private javax.swing.JLabel lblTrans_Sim_LCD;
    private javax.swing.JLabel lblTrans_Sim_Motherboard;
    private javax.swing.JLabel lblTrans_Sim_Networking;
    private javax.swing.JLabel lblTrans_Sim_Optical;
    private javax.swing.JLabel lblTrans_Sim_PSU;
    private javax.swing.JLabel lblTrans_Sim_Processor;
    private javax.swing.JLabel lblTrans_Sim_RAM;
    private javax.swing.JLabel lblTrans_Sim_SSD;
    private javax.swing.JLabel lblTrans_Sim_Speaker;
    private javax.swing.JLabel lblTrans_Sim_VGA;
    private javax.swing.JLabel lbl_daftarkyw1;
    private javax.swing.JLabel lbl_daftarkyw2;
    private javax.swing.JLabel lbl_distributor_address;
    private javax.swing.JLabel lbl_distributor_desc;
    private javax.swing.JLabel lbl_distributor_mail;
    private javax.swing.JLabel lbl_distributor_name;
    private javax.swing.JLabel lbl_distributor_phone;
    private javax.swing.JLabel lbl_distributor_status;
    private javax.swing.JLabel lbl_error_date_cas;
    private javax.swing.JLabel lbl_error_desc_cas;
    private javax.swing.JLabel lbl_error_id_cas;
    private javax.swing.JLabel lbl_error_status_cas;
    private javax.swing.JLabel lbl_error_title_cas;
    private javax.swing.JLabel lbl_id_distributor;
    private javax.swing.JLabel lbl_list_error_cas;
    private javax.swing.JLabel lbl_management1;
    private javax.swing.JLabel lbl_management2;
    private javax.swing.JLabel lbl_qrcode_trans;
    private javax.swing.JLabel lbl_searchItemBrand;
    private javax.swing.JLabel lbl_searchItemCategory;
    private javax.swing.JLabel lbl_searchItemID;
    private javax.swing.JLabel lbl_searchItemName;
    private javax.swing.JLabel lbl_searchdep1;
    private javax.swing.JLabel lbl_searchidkyw1;
    private javax.swing.JLabel lbl_searchnama1;
    private javax.swing.JLabel lbl_searchnoreg1;
    private javax.swing.JLabel lbl_trans_paymentinfo;
    private javax.swing.JPanel menuCasAddCustomer;
    private javax.swing.JPanel menuCasAddTransaction;
    private javax.swing.JPanel menuCasDashboard;
    private javax.swing.JPanel menuCasDataTransaction;
    private javax.swing.JPanel menuCasErrorReport;
    private javax.swing.JPanel menuCasListItem;
    private javax.swing.JPanel menuCasSimulation;
    private javax.swing.JPanel panelBackOffice;
    private javax.swing.JPanel panelCasDashboard;
    private javax.swing.JPanel panelCasLandingPage;
    private javax.swing.JPanel panelCasLogin;
    private javax.swing.JPanel panelDepartment;
    private javax.swing.JPanel panelFrontOffice;
    private javax.swing.JPanel panelFrontOffice1;
    private javax.swing.JPanel panelFrontOffice2;
    private javax.swing.JPanel panelKilledbyServer;
    private javax.swing.JPanel panelLandingPage;
    private javax.swing.JLabel panelLoginCas;
    private javax.swing.JPanel panelQRCode_trans;
    private javax.swing.JPanel panel_Trans_AddItem;
    private javax.swing.JPanel panel_Trans_Add_GrandTotal;
    private javax.swing.JPanel panel_Trans_Add_Simulation;
    private javax.swing.JPanel panel_Trans_Add_Simulation_Detail;
    private javax.swing.JPanel panel_Trans_CartList;
    private javax.swing.JPanel panel_Trans_Contact;
    private javax.swing.JPanel panel_Trans_Invoice;
    private javax.swing.JPanel panel_Trans_List_Simulation;
    private javax.swing.JPanel panel_Trans_Navigator;
    private javax.swing.JPanel panel_Trans_Payment;
    private javax.swing.JPanel panel_Trans_Voucher;
    private javax.swing.JPanel panelqrcodetrans;
    private javax.swing.JPanel subpanelCasAddDistributor;
    private javax.swing.JPanel subpanelCasAddTransaction;
    private javax.swing.JPanel subpanelCasDashboard;
    private javax.swing.JPanel subpanelCasDataTransaction;
    private javax.swing.JPanel subpanelCasErrorReport;
    private javax.swing.JPanel subpanelCasListItem;
    private javax.swing.JPanel subpanelCasSimulasi;
    private javax.swing.JTable tabelDaftarError_Cas;
    private javax.swing.JScrollPane tabelDaftarReportSP1;
    private javax.swing.JScrollPane tabelDataDepartmentSP1;
    private javax.swing.JTable tabelDataDistributor;
    private javax.swing.JScrollPane tabelDataKaryawanSP1;
    private javax.swing.JScrollPane tabelDataKaryawanSP2;
    private javax.swing.JTable tabelItemList;
    private javax.swing.JTable tabelShoppingCart;
    private javax.swing.JTable tabelTransList;
    private javax.swing.JTable tabel_item_simulation;
    private javax.swing.JLabel txtCasNama;
    private javax.swing.JPasswordField txtPasswordCas;
    private javax.swing.JLabel txtServerTimeCas;
    private javax.swing.JTextField txtUsernameCas;
    private javax.swing.JLabel txt_Trans_Admin;
    private javax.swing.JLabel txt_Trans_Simulation_Admin;
    private javax.swing.JTextArea txt_distributor_address;
    private javax.swing.JTextField txt_distributor_email;
    private javax.swing.JTextField txt_distributor_id;
    private javax.swing.JTextField txt_distributor_name;
    private javax.swing.JTextField txt_distributor_phone;
    private javax.swing.JTextArea txt_error_desc_cas;
    private javax.swing.JTextField txt_error_id_cas;
    private javax.swing.JTextField txt_error_title_cas;
    private javax.swing.JLabel txt_jumlahtugas_cas;
    private javax.swing.JLabel txt_price_disc;
    private javax.swing.JLabel txt_stateBigCas;
    private javax.swing.JLabel txt_stateCas;
    private javax.swing.JTextField txt_trans_NTAFare;
    private javax.swing.JTextField txt_trans_PubFare;
    private javax.swing.JTextField txt_trans_SingleFare;
    private javax.swing.JLabel txt_trans_TotalNTA;
    private javax.swing.JLabel txt_trans_Vouchercode;
    private javax.swing.JLabel txt_trans_custmail;
    private javax.swing.JLabel txt_trans_custname;
    private javax.swing.JTextField txt_trans_customerid;
    private javax.swing.JLabel txt_trans_custphone;
    private javax.swing.JTextPane txt_trans_description;
    private javax.swing.JLabel txt_trans_grand_total;
    private javax.swing.JLabel txt_trans_invoiceno;
    private javax.swing.JLabel txt_trans_invoiceno_confirm;
    private javax.swing.JTextField txt_trans_itemcode;
    private javax.swing.JTextField txt_trans_itemname;
    private javax.swing.JTextField txt_trans_nominal;
    private javax.swing.JLabel txt_trans_pending;
    private javax.swing.JTextField txt_trans_qty;
    private javax.swing.JTextField txt_trans_searchByAdmin;
    private javax.swing.JTextField txt_trans_searchByDate;
    private javax.swing.JTextField txt_trans_searchByDis;
    private javax.swing.JTextField txt_trans_searchByID;
    private javax.swing.JTextField txt_trans_searchitemByBrand;
    private javax.swing.JTextField txt_trans_searchitemByCategory;
    private javax.swing.JTextField txt_trans_searchitemByID;
    private javax.swing.JTextField txt_trans_searchitemByName;
    private javax.swing.JTextField txt_trans_sim_price_casing;
    private javax.swing.JTextField txt_trans_sim_price_coolerfan;
    private javax.swing.JTextField txt_trans_sim_price_cpucooler;
    private javax.swing.JTextField txt_trans_sim_price_hdd;
    private javax.swing.JTextField txt_trans_sim_price_headset;
    private javax.swing.JTextField txt_trans_sim_price_keyboard;
    private javax.swing.JTextField txt_trans_sim_price_lcd;
    private javax.swing.JTextField txt_trans_sim_price_motherboard;
    private javax.swing.JTextField txt_trans_sim_price_networking;
    private javax.swing.JTextField txt_trans_sim_price_optical;
    private javax.swing.JTextField txt_trans_sim_price_processor;
    private javax.swing.JTextField txt_trans_sim_price_psu;
    private javax.swing.JTextField txt_trans_sim_price_ram;
    private javax.swing.JTextField txt_trans_sim_price_speaker;
    private javax.swing.JTextField txt_trans_sim_price_ssd;
    private javax.swing.JTextField txt_trans_sim_price_vga;
    private javax.swing.JLabel txt_trans_simulation_grandtotal;
    private javax.swing.JLabel txt_trans_simulation_id;
    private javax.swing.JLabel txt_trans_success;
    private javax.swing.JLabel txt_trans_total_harga_publish;
    private javax.swing.JLabel txt_trans_voucher;
    private javax.swing.JTextField txt_trans_vouchercode;
    private javax.swing.JLabel txt_trans_voucherdiscount;
    // End of variables declaration//GEN-END:variables
}
