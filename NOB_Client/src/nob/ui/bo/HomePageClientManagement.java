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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
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
import nob.model.Entity_Piutang;
import nob.model.Entity_Signin;
import nob.model.Entity_Signup;
import nob.model.Entity_Task;
import nob.model.Entity_Transaction;

/**
 *
 * @author alimk
 */
public class HomePageClientManagement extends javax.swing.JFrame {

    public String kodeSistem = "MGM";
    String hasilgetStatusServer = null;
    String hasilgetStatusMgm = null;
    public int last_session = 0;
    public String State_run_Mgm = null;

    String dataTerpilihTabelKaryawan = null;

    private IUserDao userDao = null;
    private ILoginDao loginDao = null;
    private IDistributorDao distributorDao = null;
    private IDepartmentDao departmentDao = null;
    private ITaskDao taskDao = null;
    private IReportDao errorDao = null;
    private ITransactionDao transactionDao = null;
    private ISimulationDao simulationDao = null;

    String ip = null;
    String modesu = null;
    String whoisonline = null;
    String dataTerpilih = null;
    String dataTerpilihTabelTugas = null;
    String stockstatus = null;

    int mode = 0;

    int PriceNTA = 0;
    int PricePub = 0;
    int PriceSingle = 0;
    int JenisHarga = 0;

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

    int hasilsaveTrans, hasilsaveTransDetail, hasilsavePiutang;

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

    final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

    /**
     * Creates new form HomePageClient_Administrator
     */
    public HomePageClientManagement() {
        initComponents();
        Mode1();
        get_ip_address();
        ShowIPMode();
        set_TimeServer();

        init_User_Dao();
        init_LoginDao();
        init_DistributorDao();
        init_DepartmentDao();
        init_TaskDao();
        init_ErrorDao();
        init_TransactionDao();
        init_SimulationDao();

        init_Register_Number();

        HitungJumlahStaff();
        HitungJumlahDistributor();
        HitungJumlahTugas();

        State_run_Mgm = "Running";
        last_session = 7;

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Object[] options = {"Yes", "Cancel"};
                int reply = JOptionPane.showOptionDialog(null, "Are you sure to close the system ?", "Information", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                switch (reply) {
                    case 0:
                        try {
                            if (State_run_Mgm.equals("Running")) {
                                SetMgmStatusDisconnect();
                                e.getWindow().dispose();
                                System.exit(0);
                            } else if (hasilgetStatusServer.equals("Server Offline")) {
                                SetMgmStatusDisconnect();
                                e.getWindow().dispose();
                            } else {
                                int hasilSetMgmStatus = loginDao.UpdateStatusOffline("MGM");
                                if (hasilSetMgmStatus == 1) {
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

    public void SetMgmStatusOnline() {
        try {
            int hasilSetMgmStatus = loginDao.UpdateStatusOnline(kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SetMgmStatusOffline() {
        try {
            int hasilSetMgmStatus = loginDao.UpdateStatusOffline(kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SetMgmStatusDisconnect() {
        try {
            int hasilSetMgmStatus = loginDao.UpdateStatusDisconnect(kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SetMgmStatusKilledbyServer() {
        try {
            int hasilSetMgmStatus = loginDao.UpdateStatusKilledbyServer(kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getStatusManagement() {
        try {
            hasilgetStatusMgm = loginDao.CekClientStatus(kodeSistem);
            txt_state.setText(hasilgetStatusMgm);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getStatusServer() {
        try {
            hasilgetStatusServer = loginDao.CekClientStatus("SERVER");
            if (hasilgetStatusMgm.equals("Killed by Server")) {
                Mode13();
                SetMgmStatusKilledbyServer();
            } else if (hasilgetStatusServer.equals("Server Offline")) {
                Mode13();
                SetMgmStatusDisconnect();
            } else if (hasilgetStatusServer.equals("Online")) {
                if (last_session == 7) {
                    Mode7();
                } else if (last_session == 8) {
                    Mode8();
                } else if (last_session == 9) {
                    Mode9();
                } else if (last_session == 10) {
                    Mode10();
                } else if (last_session == 11) {
                    Mode11();
                } else if (last_session == 12) {
                    Mode12();
                }
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setIPMgm() {
        Entity_Signin ES = new Entity_Signin();
        ES.setIp(ip);
        try {
            int hasilsetIPMgm = loginDao.UpdateIP(ES, kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void CekStatus5Sec() {
        ses.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                // TODO add your handling code here:
                getStatusManagement();
                getStatusServer();
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    public void Mode1() {
        //sebagai mode awal
        mode = 1;
        panelLandingPage.setVisible(true);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelMgmLandingPage.setVisible(false);
        panelMgmLogin.setVisible(false);
        panelMgmDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode2() {
        mode = 2;
        //Mode 2 = Menampilkan Panel Daftar Department
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(true);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelMgmLandingPage.setVisible(false);
        panelMgmLogin.setVisible(false);
        panelMgmDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode3() {
        mode = 3;
        //Mode 3 = Menampilkan Panel Daftar Department Back Office
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(true);
        panelFrontOffice.setVisible(false);
        panelMgmLandingPage.setVisible(false);
        panelMgmLogin.setVisible(false);
        panelMgmDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode4() {
        mode = 4;
        //Mode 4 = Menampilkan Panel Daftar Department Front Office
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(true);
        panelMgmLandingPage.setVisible(false);
        panelMgmLogin.setVisible(false);
        panelMgmDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode5() {
        mode = 5;
        //Mode 5 = Menampilkan Panel Daftar Department Front Office - Management
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelMgmLandingPage.setVisible(true);
        panelMgmLogin.setVisible(false);
        panelMgmDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode6() {
        mode = 6;
        //Mode 6 = Menampilkan Panel Daftar Department Front Office - Management - Login
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelMgmLandingPage.setVisible(false);
        panelMgmLogin.setVisible(true);
        panelMgmDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode7() {
        mode = 7;
        //Mode 7 = Menampilkan Panel Daftar Department Front Office - Management - Dashboard
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelMgmLandingPage.setVisible(false);
        panelMgmLogin.setVisible(false);
        panelMgmDashboard.setVisible(true);
        subpanelCreateAccount.setVisible(false);
        subpanelDataKaryawan.setVisible(false);
        subpanelDataDepartment.setVisible(false);
        subpanelDashboard.setVisible(true);
        subpanelDaftarTugas.setVisible(false);
        subpanelErrorReport.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode8() {
        mode = 8;
        modesu = "save";
        //Mode 8 = Menampilkan Panel Daftar Department Front Office - Management - Create Account - Save
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelMgmLandingPage.setVisible(false);
        panelMgmLogin.setVisible(false);
        panelMgmDashboard.setVisible(true);
        subpanelCreateAccount.setVisible(true);
        subpanelDataKaryawan.setVisible(false);
        subpanelDataDepartment.setVisible(false);
        subpanelDashboard.setVisible(false);
        subpanelDaftarTugas.setVisible(false);
        subpanelErrorReport.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode8_1() {
        //Sub Mode untuk save data karyawan
        modesu = "save";
        init_Register_Number();
        txtNoRegis.setEnabled(false);
        btnSearchRegNum.setVisible(false);
        txtActiveMode.setText("Save");
        cbAction.setSelectedIndex(0);
        btnSave.setVisible(true);
        btnUpdate.setVisible(false);
    }

    public void Mode8_2() {
        //Sub Mode untuk update data karyawan
        modesu = "update";
        btnSearchRegNum.setVisible(true);
        btnSave.setVisible(false);
        btnUpdate.setVisible(true);
        txtNoRegis.setText("");
        txtActiveMode.setText("Update");
        txtNoRegis.setEnabled(true);
    }

    public void Mode9() {
        mode = 9;
        //Mode 9 = Menampilkan Panel Daftar Department Front Office - Management - Data Karyawan
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelMgmLandingPage.setVisible(false);
        panelMgmLogin.setVisible(false);
        panelMgmDashboard.setVisible(true);
        subpanelCreateAccount.setVisible(false);
        subpanelDataKaryawan.setVisible(true);
        subpanelDataDepartment.setVisible(false);
        subpanelDashboard.setVisible(false);
        subpanelDaftarTugas.setVisible(false);
        subpanelErrorReport.setVisible(false);

        panelKilledbyServer.setVisible(false);
    }

    public void Mode10() {
        mode = 10;
        //Mode 10 = Menampilkan Panel Daftar Department Front Office - Management - Data Department
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelMgmLandingPage.setVisible(false);
        panelMgmLogin.setVisible(false);
        panelMgmDashboard.setVisible(true);
        subpanelCreateAccount.setVisible(false);
        subpanelDataKaryawan.setVisible(false);
        subpanelDataDepartment.setVisible(true);
        subpanelDashboard.setVisible(false);
        subpanelDaftarTugas.setVisible(false);
        subpanelErrorReport.setVisible(false);
        initTableDaftarDepartment();
        loadDaftarDepartment();
        panelKilledbyServer.setVisible(false);
    }

    public void Mode11() {
        mode = 11;
        //Mode 11 = Menampilkan Panel Daftar Department Front Office - Management - Daftar Tugas
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelMgmLandingPage.setVisible(false);
        panelMgmLogin.setVisible(false);
        panelMgmDashboard.setVisible(true);
        subpanelCreateAccount.setVisible(false);
        subpanelDataKaryawan.setVisible(false);
        subpanelDataDepartment.setVisible(false);
        subpanelDashboard.setVisible(false);
        subpanelDaftarTugas.setVisible(true);
        subpanelErrorReport.setVisible(false);
        initTableDaftarTugas();
        loadDaftarTugas();
        panelKilledbyServer.setVisible(false);
    }

    public void Mode12() {
        mode = 12;
        //Mode 12 = Menampilkan Panel Daftar Department Front Office - Management - Daftar Error
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelMgmLandingPage.setVisible(false);
        panelMgmLogin.setVisible(false);
        panelMgmDashboard.setVisible(true);
        subpanelCreateAccount.setVisible(false);
        subpanelDataKaryawan.setVisible(false);
        subpanelDataDepartment.setVisible(false);
        subpanelDashboard.setVisible(false);
        subpanelDaftarTugas.setVisible(false);
        subpanelErrorReport.setVisible(true);
        initTableDaftarError();
        loadDaftarError();
        panelKilledbyServer.setVisible(false);
    }

    public void Mode13() {
        mode = 13;
        //Mode 13 = Menampilkan Panel Killed by Server
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelMgmLandingPage.setVisible(false);
        panelMgmLogin.setVisible(false);
        panelMgmDashboard.setVisible(false);
        subpanelCreateAccount.setVisible(false);
        subpanelDataKaryawan.setVisible(false);
        subpanelDataDepartment.setVisible(false);
        subpanelDashboard.setVisible(false);
        subpanelDaftarTugas.setVisible(false);
        subpanelErrorReport.setVisible(false);
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
                    txtServerTime.setText("Date  :  " + day + "/" + (month + 1) + "/" + year + "   Time : " + hour + ":" + (minute) + ":" + second);

                    try {
                        sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
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

    public void HideIPMode() {
        btnShowIP.setVisible(false);
        btnHideIP.setVisible(true);
    }

    public void ShowIPMode() {
        btnShowIP.setVisible(true);
        btnHideIP.setVisible(false);
    }

    public void GetQrCode() {
        if (txt_username_div.getText().length() == 0) {
            return;
        }

        String t = txt_username_div.getText();
        out = QRCode.from(t).withSize(141, 168).to(ImageType.PNG).stream();
        try {
            fout = new FileOutputStream(new File("temp.png"));
            fout.write(out.toByteArray());
            fout.flush();
            fout.close();

            BufferedImage miQr = ImageIO.read(new File("temp.png"));
            JLabel label = new JLabel(new ImageIcon(miQr));

            Graphics g = lbl_qrcode.getGraphics();
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
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            //Logger.getLogger(FrmSupplier.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Koneksi ke Service" + url + "[GAGAL], cek kembali app server", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void init_Register_Number() {
        Entity_Signup ESU = new Entity_Signup();
        try {
            String hasil = userDao.GenerateRegistNumber(ESU);
            if (hasil == null) {
                //do something
            } else {
                txtNoRegis.setText(hasil);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init_Mgm_ID() {
        Entity_Signup ESU = new Entity_Signup();
        try {
            String hasil = userDao.GenerateIDManajemen(ESU);
            if (hasil == null) {
                //do something
            } else {
                txt_username_div.setText(hasil);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init_Mgm_ID2() {
        Entity_Signup ESU = new Entity_Signup();
        try {
            String hasil = userDao.GenerateIDManajemen2(ESU);
            if (hasil == null) {
                //do something
            } else {
                txt_username_div.setText(hasil);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init_Keu_ID() {
        Entity_Signup ESU = new Entity_Signup();
        try {
            String hasil = userDao.GenerateIDKeu(ESU);
            if (hasil == null) {
                //do something
            } else {
                txt_username_div.setText(hasil);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init_Keu_ID2() {
        Entity_Signup ESU = new Entity_Signup();
        try {
            String hasil = userDao.GenerateIDKeu2(ESU);
            if (hasil == null) {
                //do something
            } else {
                txt_username_div.setText(hasil);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init_HRD_ID() {
        Entity_Signup ESU = new Entity_Signup();
        try {
            String hasil = userDao.GenerateIDHRD(ESU);
            if (hasil == null) {
                //do something
            } else {
                txt_username_div.setText(hasil);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init_HRD_ID2() {
        Entity_Signup ESU = new Entity_Signup();
        try {
            String hasil = userDao.GenerateIDHRD2(ESU);
            if (hasil == null) {
                //do something
            } else {
                txt_username_div.setText(hasil);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init_Cashier_ID() {
        Entity_Signup ESU = new Entity_Signup();
        try {
            String hasil = userDao.GenerateIDCashier(ESU);
            if (hasil == null) {
                //do something
            } else {
                txt_username_div.setText(hasil);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init_Cashier_ID2() {
        Entity_Signup ESU = new Entity_Signup();
        try {
            String hasil = userDao.GenerateIDCashier2(ESU);
            if (hasil == null) {
                //do something
            } else {
                txt_username_div.setText(hasil);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init_Warehouse_ID() {
        Entity_Signup ESU = new Entity_Signup();
        try {
            String hasil = userDao.GenerateIDWarehouse(ESU);
            if (hasil == null) {
                //do something
            } else {
                txt_username_div.setText(hasil);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init_Warehouse_ID2() {
        Entity_Signup ESU = new Entity_Signup();
        try {
            String hasil = userDao.GenerateIDWarehouse2(ESU);
            if (hasil == null) {
                //do something
            } else {
                txt_username_div.setText(hasil);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init_Task_ID() {
        Entity_Task ET = new Entity_Task();
        try {
            String hasil = taskDao.GenerateTaskID(ET);
            if (hasil == null) {
                //do something
            } else {
                txt_task_id.setText(hasil);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init_Error_ID() {
        Entity_ErrorReport EER = new Entity_ErrorReport();
        try {
            String hasil = errorDao.GenerateErrorID(EER);
            if (hasil == null) {
                //do something
            } else {
                txt_error_id.setText(hasil);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initTableDaftarKaryawan() {
        //set header table
        tableModelKaryawan.setColumnIdentifiers(DaftarKywcolumNames);
        tabelDataKaryawan.setModel(tableModelKaryawan);
        tabelDataKaryawan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customTabelDaftarKaryawan();
    }

    private void customTabelDaftarKaryawan() {
        tabelDataKaryawan.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabelDataKaryawan.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabelDataKaryawan.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabelDataKaryawan.getColumnModel().getColumn(3).setPreferredWidth(250);
        tabelDataKaryawan.getColumnModel().getColumn(4).setPreferredWidth(150);
        tabelDataKaryawan.getColumnModel().getColumn(5).setPreferredWidth(150);
        tabelDataKaryawan.getColumnModel().getColumn(6).setPreferredWidth(150);
        tabelDataKaryawan.getColumnModel().getColumn(7).setPreferredWidth(150);
        tabelDataKaryawan.getColumnModel().getColumn(8).setPreferredWidth(150);
    }

    private void loadDaftarKaryawan() {
        customTabelDaftarKaryawan();
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
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadDaftarKaryawanbyName() {
        try {
            // reset data di tabel
            tableModelKaryawan.setRowCount(0);
            // mengambil data barang dari server
            // kemudian menyimpannya ke objek list
            recordKaryawan = userDao.getByName(txt_searchkyw_byname.getText());
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
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadDaftarKaryawanbyNoreg() {
        try {
            // reset data di tabel
            tableModelKaryawan.setRowCount(0);
            // mengambil data barang dari server
            // kemudian menyimpannya ke objek list
            recordKaryawan = userDao.getByNoregKyw(txt_searchkyw_bynoreg.getText());
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
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadDaftarKaryawanbyDept() {
        try {
            // reset data di tabel
            tableModelKaryawan.setRowCount(0);
            // mengambil data barang dari server
            // kemudian menyimpannya ke objek list
            recordKaryawan = userDao.getByDept(txt_searchkyw_byDep.getText());
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
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadDaftarKaryawanbyId() {
        try {
            // reset data di tabel
            tableModelKaryawan.setRowCount(0);
            // mengambil data barang dari server
            // kemudian menyimpannya ke objek list
            recordKaryawan = userDao.getByIdKyw(txt_searchkyw_byId.getText());
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
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initTableDaftarDepartment() {
        //set header table
        tableModelDepartment.setColumnIdentifiers(DaftarDepartmentcolumNames);
        tabelDataDepartment.setModel(tableModelDepartment);
        tabelDataDepartment.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customTabelDepartment();
    }

    private void customTabelDepartment() {
        tabelDataDepartment.getColumnModel().getColumn(0).setPreferredWidth(200);
        tabelDataDepartment.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabelDataDepartment.getColumnModel().getColumn(2).setPreferredWidth(200);
        tabelDataDepartment.getColumnModel().getColumn(3).setPreferredWidth(150);
    }

    private void loadDaftarDepartment() {
        customTabelDepartment();
        try {
            // reset data di tabel
            tableModelDepartment.setRowCount(0);
            // mengambil data barang dari server
            // kemudian menyimpannya ke objek list
            recordDepartment = departmentDao.getAll();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Department ED : recordDepartment) {
                // ambil nomor urut terakhir
                Object[] objects = new Object[DaftarDepartmentcolumNames.length];
                objects[0] = ED.getDep_id();
                objects[1] = ED.getDep_cat();
                objects[2] = ED.getDep_desc();
                objects[3] = ED.getDep_status();
                // tambahkan data barang ke dalam tabel
                tableModelDepartment.addRow(objects);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initTableDaftarTugas() {
        //set header table
        tableModelTugas.setColumnIdentifiers(DaftarTugascolumNames);
        tabelDaftarTugas.setModel(tableModelTugas);
        tabelDaftarTugas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customeTabelDaftarTugas();
    }

    private void customeTabelDaftarTugas() {
        tabelDaftarTugas.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabelDaftarTugas.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabelDaftarTugas.getColumnModel().getColumn(2).setPreferredWidth(300);
        tabelDaftarTugas.getColumnModel().getColumn(3).setPreferredWidth(150);
        tabelDaftarTugas.getColumnModel().getColumn(4).setPreferredWidth(150);
        tabelDaftarTugas.getColumnModel().getColumn(5).setPreferredWidth(150);
        tabelDaftarTugas.getColumnModel().getColumn(6).setPreferredWidth(150);
        tabelDaftarTugas.getColumnModel().getColumn(7).setPreferredWidth(200);
        tabelDaftarTugas.getColumnModel().getColumn(8).setPreferredWidth(150);
    }

    private void loadDaftarTugas() {
        customeTabelDaftarTugas();
        try {
            // reset data di tabel
            tableModelTugas.setRowCount(0);
            // mengambil data barang dari server
            // kemudian menyimpannya ke objek list
            recordTask = taskDao.getAll();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Task ET : recordTask) {
                // ambil nomor urut terakhir
                Object[] objects = new Object[DaftarTugascolumNames.length];
                objects[0] = ET.getTask_id();
                objects[1] = ET.getTask_title();
                objects[2] = ET.getTask_desc();
                objects[3] = ET.getTask_date();
                objects[4] = ET.getTask_date_deadline();
                objects[5] = ET.getTask_date_open();
                objects[6] = ET.getTask_pic();
                objects[7] = ET.getTask_dep();
                objects[8] = ET.getTask_status();
                // tambahkan data barang ke dalam tabel
                tableModelTugas.addRow(objects);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initTableDaftarError() {
        //set header table
        tableModelError.setColumnIdentifiers(DaftarErrorcolumNames);
        tabelDaftarError.setModel(tableModelError);
        tabelDaftarError.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customTabelError();
    }

    private void customTabelError() {
        tabelDaftarError.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabelDaftarError.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabelDaftarError.getColumnModel().getColumn(2).setPreferredWidth(300);
        tabelDaftarError.getColumnModel().getColumn(3).setPreferredWidth(150);
        tabelDaftarError.getColumnModel().getColumn(4).setPreferredWidth(150);
        tabelDaftarError.getColumnModel().getColumn(5).setPreferredWidth(150);
    }

    private void loadDaftarError() {
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

    private void loadComboBoxDepartment() {
        try {
            // mengambil data department dari server
            // kemudian menyimpannya ke objek list
            recordCbDepartment = departmentDao.getCbDepartment();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_Department ED : recordCbDepartment) {
                // ambil nomor urut terakhir
                cb_division.addItem(ED.getDep_id());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ClearFormCreateAccount() {
        txtNama.setText("");
        txtPob.setText("");
        txtDob.setDate(null);
        su_rbpria.setSelected(false);
        su_rbwanita.setSelected(false);
        txtPhone.setText("");
        txtMail.setText("");
        cb_division.setSelectedIndex(0);
        txt_username_div.setText("");
        txt_password.setText(null);
        txt_password.setEnabled(false);
        su_fototext.setText(null);
        lbl_foto.setIcon(null);
        txtNama.requestFocus();
        btnGetID.setEnabled(true);
        lbl_qrcode.setIcon(null);
        buttongroup_Gender.clearSelection();
        cbAction.setSelectedIndex(0);
    }

    public void ClearFormAddDepartment() {
        txt_dep_id.setText("");
        txt_dep_desc.setText("");
        cb_dep_cat.setSelectedIndex(0);
        cb_dep_status.setSelectedIndex(0);
        txt_dep_id.requestFocus();
    }

    public void ClearFormTask() {
        txt_task_title.setText("");
        txt_task_desc.setText("");
        dt_task_date.setDate(null);
        dt_task_deadline.setDate(null);
        txt_task_title.requestFocus();
        cb_task_dep.setSelectedIndex(0);
        cb_task_status.setSelectedIndex(0);
    }

    public void ClearFormError() {
        txt_error_title.setText("");
        txt_error_desc.setText("");
        dtError_Date.setDate(null);
        txt_error_title.requestFocus();
        cb_error_status.setSelectedIndex(0);
    }

    private void clearFormLogin() {
        txtUsernameMgm.setText("");
        txtPasswordMgm.setText("");
    }

    public void HitungJumlahStaff() {
        try {
            String hasil = userDao.CountStaff();
            txt_jumlahstaff.setText(hasil);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void HitungJumlahDistributor() {
        try {
            String hasil = distributorDao.CountDisActive();
            txt_jumlahdistributoraktif.setText(hasil);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void HitungJumlahTugas() {
        try {
            String hasil = taskDao.CountTask();
            txt_jumlahtugas.setText(hasil);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        panelMgmDashboard = new javax.swing.JPanel();
        btnNotifMgm = new javax.swing.JLabel();
        btnMsgMgm = new javax.swing.JLabel();
        btnLogoutMgm = new javax.swing.JLabel();
        subpanelDashboard = new javax.swing.JPanel();
        txt_jumlahtugas = new javax.swing.JLabel();
        txt_jumlahdistributoraktif = new javax.swing.JLabel();
        txt_jumlahstaff = new javax.swing.JLabel();
        lblDashboardTitle1 = new javax.swing.JLabel();
        lblDashboardTitle = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        subpanelCreateAccount = new javax.swing.JPanel();
        btnSearchRegNum = new javax.swing.JButton();
        cbAction = new javax.swing.JComboBox<>();
        txtActiveMode = new javax.swing.JLabel();
        lblMode = new javax.swing.JLabel();
        lblAction = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        txtDob = new com.toedter.calendar.JDateChooser();
        panelfoto = new javax.swing.JPanel();
        lbl_foto = new javax.swing.JLabel();
        su_fototext = new javax.swing.JTextField();
        su_attachgbr = new javax.swing.JButton();
        qrsave = new javax.swing.JButton();
        panelqrcode = new javax.swing.JPanel();
        lbl_qrcode = new javax.swing.JLabel();
        txt_password = new javax.swing.JPasswordField();
        txt_username_div = new javax.swing.JTextField();
        btnGetID = new javax.swing.JButton();
        su_rbpria = new javax.swing.JRadioButton();
        su_rbwanita = new javax.swing.JRadioButton();
        cb_division = new javax.swing.JComboBox<>();
        txtMail = new javax.swing.JTextField();
        txtPhone = new javax.swing.JTextField();
        txtPob = new javax.swing.JTextField();
        txtNama = new javax.swing.JTextField();
        txtNoRegis = new javax.swing.JTextField();
        btnClear = new javax.swing.JLabel();
        btnSave = new javax.swing.JLabel();
        btnUpdate = new javax.swing.JLabel();
        bgSubPanelCreateAcc = new javax.swing.JLabel();
        subpanelErrorReport = new javax.swing.JPanel();
        btnSaveError = new javax.swing.JLabel();
        btnClearError = new javax.swing.JLabel();
        lbl_list_error = new javax.swing.JLabel();
        cb_error_status = new javax.swing.JComboBox<>();
        lbl_error_date = new javax.swing.JLabel();
        dtError_Date = new com.toedter.calendar.JDateChooser();
        lbl_error_status = new javax.swing.JLabel();
        txt_error_desc = new javax.swing.JTextArea();
        lbl_error_desc = new javax.swing.JLabel();
        txt_error_title = new javax.swing.JTextField();
        lbl_error_title = new javax.swing.JLabel();
        txt_error_id = new javax.swing.JTextField();
        lbl_error_id = new javax.swing.JLabel();
        lblErrorReportTitle = new javax.swing.JLabel();
        lblErrorReportSubTitle = new javax.swing.JLabel();
        jSeparator13 = new javax.swing.JSeparator();
        tabelDaftarReportSP = new javax.swing.JScrollPane();
        tabelDaftarError = new javax.swing.JTable();
        lblDepartmentSubTitle5 = new javax.swing.JLabel();
        btn_delete_error = new javax.swing.JLabel();
        subpanelDaftarTugas = new javax.swing.JPanel();
        btnSaveTask = new javax.swing.JLabel();
        btnClearTask = new javax.swing.JLabel();
        lbl_list_task = new javax.swing.JLabel();
        cb_task_dep = new javax.swing.JComboBox<>();
        lbl_task_dep = new javax.swing.JLabel();
        cb_task_status = new javax.swing.JComboBox<>();
        lbl_task_status = new javax.swing.JLabel();
        dt_task_deadline = new com.toedter.calendar.JDateChooser();
        lbl_task_deadline = new javax.swing.JLabel();
        dt_task_date = new com.toedter.calendar.JDateChooser();
        lbl_task_date = new javax.swing.JLabel();
        txt_task_desc = new javax.swing.JTextArea();
        lbl_task_desc = new javax.swing.JLabel();
        txt_task_title = new javax.swing.JTextField();
        lbl_task_title = new javax.swing.JLabel();
        txt_task_id = new javax.swing.JTextField();
        lbl_id_task = new javax.swing.JLabel();
        lblDepartmentTitle1 = new javax.swing.JLabel();
        lblDepartmentSubTitle2 = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();
        tabelDaftarTugasSP = new javax.swing.JScrollPane();
        tabelDaftarTugas = new javax.swing.JTable();
        lblDepartmentSubTitle3 = new javax.swing.JLabel();
        btn_upstate_task = new javax.swing.JLabel();
        btn_delete_task = new javax.swing.JLabel();
        btn_upstate_task_undone = new javax.swing.JLabel();
        subpanelDataDepartment = new javax.swing.JPanel();
        btnSaveDepartment = new javax.swing.JLabel();
        btnClearDepartment = new javax.swing.JLabel();
        lbl_list_dep = new javax.swing.JLabel();
        cb_dep_status = new javax.swing.JComboBox<>();
        lbl_dep_status = new javax.swing.JLabel();
        txt_dep_desc = new javax.swing.JTextArea();
        lbl_dep_desc = new javax.swing.JLabel();
        cb_dep_cat = new javax.swing.JComboBox<>();
        lbl_dep_cat = new javax.swing.JLabel();
        txt_dep_id = new javax.swing.JTextField();
        lbl_id_dep = new javax.swing.JLabel();
        lblDepartmentTitle = new javax.swing.JLabel();
        lblDepartmentSubTitle = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        tabelDataDepartmentSP = new javax.swing.JScrollPane();
        tabelDataDepartment = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        lblDepartmentSubTitle1 = new javax.swing.JLabel();
        btn_active_dep = new javax.swing.JLabel();
        btn_deactive_dep = new javax.swing.JLabel();
        subpanelDataKaryawan = new javax.swing.JPanel();
        lbl_searchnama = new javax.swing.JLabel();
        lbl_searchdep = new javax.swing.JLabel();
        lbl_searchidkyw = new javax.swing.JLabel();
        lbl_searchnoreg = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txt_searchkyw_byname = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        txt_searchkyw_byDep = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        txt_searchkyw_bynoreg = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        txt_searchkyw_byId = new javax.swing.JTextField();
        lblDataKaryawanTitel = new javax.swing.JLabel();
        lbl_management = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        tabelDataKaryawanSP = new javax.swing.JScrollPane();
        tabelDataKaryawan = new javax.swing.JTable();
        lbl_daftarkyw = new javax.swing.JLabel();
        btn_printTrans = new javax.swing.JButton();
        jSeparator12 = new javax.swing.JSeparator();
        menuErrorManagement = new javax.swing.JPanel();
        iconMgmErrorReport = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        menuDaftarTugas = new javax.swing.JPanel();
        iconMgmDaftarTugas = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        menuDataDepartment = new javax.swing.JPanel();
        iconMgmDataDepart = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        menuDataKaryawan = new javax.swing.JPanel();
        iconMgmDataKyw = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        menuCreateAccount = new javax.swing.JPanel();
        iconMgmCreateAcc = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        menuDashboard = new javax.swing.JPanel();
        iconMgmDashboard = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnChangeState = new javax.swing.JButton();
        btnHideIP = new javax.swing.JButton();
        btnShowIP = new javax.swing.JButton();
        txtServerTime = new javax.swing.JLabel();
        txt_stateBig = new javax.swing.JLabel();
        lblStateBig = new javax.swing.JLabel();
        lblState = new javax.swing.JLabel();
        txt_state = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txTMgmNama = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        iconAdmin = new javax.swing.JLabel();
        bgManagement = new javax.swing.JLabel();
        panelKilledbyServer = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        panelMgmLogin = new javax.swing.JPanel();
        txtUsernameMgm = new javax.swing.JTextField();
        txtPasswordMgm = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        panelLoginMgm = new javax.swing.JLabel();
        bgClientManagementLogin = new javax.swing.JLabel();
        panelMgmLandingPage = new javax.swing.JPanel();
        iconHome1 = new javax.swing.JLabel();
        bgLogo = new javax.swing.JLabel();
        bgClientManagement = new javax.swing.JLabel();
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
        setTitle("NOB Tech - Client - Management");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelMgmDashboard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnNotifMgm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconNotif.png"))); // NOI18N
        btnNotifMgm.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelMgmDashboard.add(btnNotifMgm, new org.netbeans.lib.awtextra.AbsoluteConstraints(1039, 97, -1, -1));

        btnMsgMgm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconMassage.png"))); // NOI18N
        btnMsgMgm.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelMgmDashboard.add(btnMsgMgm, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 97, -1, -1));

        btnLogoutMgm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconLogout.png"))); // NOI18N
        btnLogoutMgm.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogoutMgm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogoutMgmMouseClicked(evt);
            }
        });
        panelMgmDashboard.add(btnLogoutMgm, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 97, -1, -1));

        subpanelDashboard.setBackground(new java.awt.Color(255, 255, 255));
        subpanelDashboard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_jumlahtugas.setFont(new java.awt.Font("Gotham Ultra", 1, 48)); // NOI18N
        txt_jumlahtugas.setForeground(new java.awt.Color(255, 255, 255));
        txt_jumlahtugas.setText("1");
        subpanelDashboard.add(txt_jumlahtugas, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 150, -1, -1));

        txt_jumlahdistributoraktif.setFont(new java.awt.Font("Gotham Ultra", 1, 48)); // NOI18N
        txt_jumlahdistributoraktif.setForeground(new java.awt.Color(255, 255, 255));
        txt_jumlahdistributoraktif.setText("1");
        subpanelDashboard.add(txt_jumlahdistributoraktif, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 150, -1, -1));

        txt_jumlahstaff.setFont(new java.awt.Font("Gotham Ultra", 1, 48)); // NOI18N
        txt_jumlahstaff.setForeground(new java.awt.Color(255, 255, 255));
        txt_jumlahstaff.setText("1");
        subpanelDashboard.add(txt_jumlahstaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 150, -1, -1));

        lblDashboardTitle1.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblDashboardTitle1.setForeground(java.awt.Color.gray);
        lblDashboardTitle1.setText("Management");
        subpanelDashboard.add(lblDashboardTitle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 50, -1, -1));

        lblDashboardTitle.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblDashboardTitle.setForeground(java.awt.Color.gray);
        lblDashboardTitle.setText("DASHBOARD");
        subpanelDashboard.add(lblDashboardTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        jSeparator7.setForeground(new java.awt.Color(241, 241, 241));
        subpanelDashboard.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 930, 20));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconTaskQueue.png"))); // NOI18N
        subpanelDashboard.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 110, -1, -1));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconDistributorActive.png"))); // NOI18N
        subpanelDashboard.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 110, -1, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconStaff.png"))); // NOI18N
        subpanelDashboard.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        panelMgmDashboard.add(subpanelDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 538));

        subpanelCreateAccount.setPreferredSize(new java.awt.Dimension(1010, 538));
        subpanelCreateAccount.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSearchRegNum.setText("SEARCH");
        btnSearchRegNum.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSearchRegNumMouseClicked(evt);
            }
        });
        subpanelCreateAccount.add(btnSearchRegNum, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 105, -1, -1));

        cbAction.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Choose Action", "Save", "Update" }));
        cbAction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbActionActionPerformed(evt);
            }
        });
        subpanelCreateAccount.add(cbAction, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 360, 110, 30));
        subpanelCreateAccount.add(txtActiveMode, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 370, -1, -1));

        lblMode.setText("Active Mode :");
        subpanelCreateAccount.add(lblMode, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 370, -1, -1));

        lblAction.setText("Action : ");
        subpanelCreateAccount.add(lblAction, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 370, -1, -1));
        subpanelCreateAccount.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 352, 310, 20));
        subpanelCreateAccount.add(txtDob, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 227, 140, 30));

        lbl_foto.setBackground(new java.awt.Color(255, 255, 255));
        lbl_foto.setForeground(new java.awt.Color(255, 51, 204));

        javax.swing.GroupLayout panelfotoLayout = new javax.swing.GroupLayout(panelfoto);
        panelfoto.setLayout(panelfotoLayout);
        panelfotoLayout.setHorizontalGroup(
            panelfotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelfotoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_foto, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelfotoLayout.setVerticalGroup(
            panelfotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelfotoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_foto, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addContainerGap())
        );

        subpanelCreateAccount.add(panelfoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 110, 160, 190));

        su_fototext.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        su_fototext.setEnabled(false);
        subpanelCreateAccount.add(su_fototext, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 310, 160, 30));

        su_attachgbr.setText("ATTACH");
        su_attachgbr.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        su_attachgbr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                su_attachgbrActionPerformed(evt);
            }
        });
        subpanelCreateAccount.add(su_attachgbr, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 300, 100, 40));

        qrsave.setText("SAVE QR CODE");
        qrsave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        qrsave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qrsaveActionPerformed(evt);
            }
        });
        subpanelCreateAccount.add(qrsave, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 440, 140, 40));

        panelqrcode.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_qrcode.setBackground(new java.awt.Color(255, 255, 255));
        lbl_qrcode.setForeground(new java.awt.Color(255, 51, 204));
        panelqrcode.add(lbl_qrcode, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 141, 168));

        subpanelCreateAccount.add(panelqrcode, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 290, 160, 190));

        txt_password.setFont(new java.awt.Font("Gotham Light", 0, 12)); // NOI18N
        txt_password.setEnabled(false);
        subpanelCreateAccount.add(txt_password, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 190, 180, 30));

        txt_username_div.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        txt_username_div.setEnabled(false);
        subpanelCreateAccount.add(txt_username_div, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 105, 180, 30));

        btnGetID.setText("GET ID");
        btnGetID.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGetID.setEnabled(false);
        btnGetID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGetIDActionPerformed(evt);
            }
        });
        subpanelCreateAccount.add(btnGetID, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 100, 110, 40));

        buttongroup_Gender.add(su_rbpria);
        su_rbpria.setText("Pria");
        subpanelCreateAccount.add(su_rbpria, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 290, -1, -1));

        buttongroup_Gender.add(su_rbwanita);
        su_rbwanita.setText("Wanita");
        subpanelCreateAccount.add(su_rbwanita, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 290, -1, -1));

        cb_division.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        cb_division.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_divisionActionPerformed(evt);
            }
        });
        subpanelCreateAccount.add(cb_division, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 480, 270, 30));

        txtMail.setFont(new java.awt.Font("Gotham Book", 1, 14)); // NOI18N
        subpanelCreateAccount.add(txtMail, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 413, 190, 30));

        txtPhone.setFont(new java.awt.Font("Gotham Book", 1, 14)); // NOI18N
        subpanelCreateAccount.add(txtPhone, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 347, 190, 30));

        txtPob.setFont(new java.awt.Font("Gotham Book", 1, 14)); // NOI18N
        subpanelCreateAccount.add(txtPob, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 227, 120, 30));

        txtNama.setFont(new java.awt.Font("Gotham Book", 1, 14)); // NOI18N
        subpanelCreateAccount.add(txtNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 165, 270, 30));

        txtNoRegis.setFont(new java.awt.Font("Gotham Book", 1, 14)); // NOI18N
        txtNoRegis.setEnabled(false);
        subpanelCreateAccount.add(txtNoRegis, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 105, 190, 30));

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconClear.png"))); // NOI18N
        btnClear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClearMouseClicked(evt);
            }
        });
        subpanelCreateAccount.add(btnClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 420, -1, -1));

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconSave.png"))); // NOI18N
        btnSave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveMouseClicked(evt);
            }
        });
        subpanelCreateAccount.add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 420, -1, -1));

        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconUpdate.png"))); // NOI18N
        btnUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUpdateMouseClicked(evt);
            }
        });
        subpanelCreateAccount.add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 420, -1, -1));

        bgSubPanelCreateAcc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/BgPanelCreateAccount.png"))); // NOI18N
        bgSubPanelCreateAcc.setPreferredSize(new java.awt.Dimension(1010, 537));
        subpanelCreateAccount.add(bgSubPanelCreateAcc, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelMgmDashboard.add(subpanelCreateAccount, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 538));

        subpanelErrorReport.setBackground(new java.awt.Color(255, 255, 255));
        subpanelErrorReport.setPreferredSize(new java.awt.Dimension(1010, 538));
        subpanelErrorReport.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSaveError.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconSave.png"))); // NOI18N
        btnSaveError.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSaveError.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveErrorMouseClicked(evt);
            }
        });
        subpanelErrorReport.add(btnSaveError, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 410, -1, -1));

        btnClearError.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconClear.png"))); // NOI18N
        btnClearError.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClearError.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClearErrorMouseClicked(evt);
            }
        });
        subpanelErrorReport.add(btnClearError, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 410, -1, -1));

        lbl_list_error.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_list_error.setForeground(java.awt.Color.gray);
        lbl_list_error.setText("Report List");
        subpanelErrorReport.add(lbl_list_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 100, -1, -1));

        cb_error_status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Status -", "Currently Added", "On Progress", "Done" }));
        subpanelErrorReport.add(cb_error_status, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 410, 180, -1));

        lbl_error_date.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_date.setText("Date");
        subpanelErrorReport.add(lbl_error_date, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, -1, -1));
        subpanelErrorReport.add(dtError_Date, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 360, 180, -1));

        lbl_error_status.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_status.setText("Status");
        subpanelErrorReport.add(lbl_error_status, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 410, -1, -1));

        txt_error_desc.setBackground(new java.awt.Color(240, 240, 240));
        txt_error_desc.setColumns(20);
        txt_error_desc.setRows(5);
        subpanelErrorReport.add(txt_error_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 240, 180, -1));

        lbl_error_desc.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_desc.setText("Description");
        subpanelErrorReport.add(lbl_error_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, -1, -1));
        subpanelErrorReport.add(txt_error_title, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 188, 180, -1));

        lbl_error_title.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_title.setText("Title");
        subpanelErrorReport.add(lbl_error_title, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, -1));

        txt_error_id.setEnabled(false);
        subpanelErrorReport.add(txt_error_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 180, -1));

        lbl_error_id.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_id.setText("Task ID");
        subpanelErrorReport.add(lbl_error_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));

        lblErrorReportTitle.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblErrorReportTitle.setForeground(java.awt.Color.gray);
        lblErrorReportTitle.setText("ERROR REPORT");
        subpanelErrorReport.add(lblErrorReportTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        lblErrorReportSubTitle.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblErrorReportSubTitle.setForeground(java.awt.Color.gray);
        lblErrorReportSubTitle.setText("Add Report");
        subpanelErrorReport.add(lblErrorReportSubTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        jSeparator13.setForeground(new java.awt.Color(241, 241, 241));
        subpanelErrorReport.add(jSeparator13, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 950, 20));

        tabelDaftarReportSP.setBackground(new java.awt.Color(241, 241, 241));
        tabelDaftarReportSP.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        tabelDaftarReportSP.setAutoscrolls(true);
        tabelDaftarReportSP.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tabelDaftarError.setBackground(new java.awt.Color(240, 240, 240));
        tabelDaftarError.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Error ID", "Title", "Description", "Date", "PIC", "PIC Status"
            }
        ));
        tabelDaftarError.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabelDaftarError.setAutoscrolls(false);
        tabelDaftarError.setGridColor(new java.awt.Color(241, 241, 241));
        tabelDaftarError.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelDaftarErrorMouseClicked(evt);
            }
        });
        tabelDaftarReportSP.setViewportView(tabelDaftarError);
        if (tabelDaftarError.getColumnModel().getColumnCount() > 0) {
            tabelDaftarError.getColumnModel().getColumn(0).setPreferredWidth(150);
            tabelDaftarError.getColumnModel().getColumn(1).setPreferredWidth(150);
            tabelDaftarError.getColumnModel().getColumn(2).setPreferredWidth(300);
            tabelDaftarError.getColumnModel().getColumn(3).setPreferredWidth(150);
            tabelDaftarError.getColumnModel().getColumn(4).setPreferredWidth(150);
            tabelDaftarError.getColumnModel().getColumn(5).setPreferredWidth(150);
        }

        subpanelErrorReport.add(tabelDaftarReportSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 130, 580, 260));

        lblDepartmentSubTitle5.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblDepartmentSubTitle5.setForeground(java.awt.Color.gray);
        lblDepartmentSubTitle5.setText("Management");
        subpanelErrorReport.add(lblDepartmentSubTitle5, new org.netbeans.lib.awtextra.AbsoluteConstraints(335, 53, -1, -1));

        btn_delete_error.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/btnDelete.png"))); // NOI18N
        btn_delete_error.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_delete_error.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_delete_errorMouseClicked(evt);
            }
        });
        subpanelErrorReport.add(btn_delete_error, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 87, -1, -1));

        panelMgmDashboard.add(subpanelErrorReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 538));

        subpanelDaftarTugas.setBackground(new java.awt.Color(255, 255, 255));
        subpanelDaftarTugas.setPreferredSize(new java.awt.Dimension(1010, 538));
        subpanelDaftarTugas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSaveTask.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconSave.png"))); // NOI18N
        btnSaveTask.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSaveTask.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveTaskMouseClicked(evt);
            }
        });
        subpanelDaftarTugas.add(btnSaveTask, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 410, -1, -1));

        btnClearTask.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconClear.png"))); // NOI18N
        btnClearTask.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClearTask.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClearTaskMouseClicked(evt);
            }
        });
        subpanelDaftarTugas.add(btnClearTask, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 410, -1, -1));

        lbl_list_task.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_list_task.setForeground(java.awt.Color.gray);
        lbl_list_task.setText("Task List");
        subpanelDaftarTugas.add(lbl_list_task, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 100, -1, -1));

        cb_task_dep.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Department -", "MGM", "KEU", "HRD", "CAS", "WRH", "IT" }));
        subpanelDaftarTugas.add(cb_task_dep, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 440, 180, -1));

        lbl_task_dep.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_task_dep.setText("Department");
        subpanelDaftarTugas.add(lbl_task_dep, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 440, -1, -1));

        cb_task_status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Status -", "Currently Added", "On Progress", "Done" }));
        subpanelDaftarTugas.add(cb_task_status, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 480, 180, -1));

        lbl_task_status.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_task_status.setText("Status");
        subpanelDaftarTugas.add(lbl_task_status, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 480, -1, -1));
        subpanelDaftarTugas.add(dt_task_deadline, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 400, 180, -1));

        lbl_task_deadline.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_task_deadline.setText("Deadline");
        subpanelDaftarTugas.add(lbl_task_deadline, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 400, -1, -1));
        subpanelDaftarTugas.add(dt_task_date, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 360, 180, -1));

        lbl_task_date.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_task_date.setText("Task Date");
        subpanelDaftarTugas.add(lbl_task_date, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, -1, -1));

        txt_task_desc.setBackground(new java.awt.Color(240, 240, 240));
        txt_task_desc.setColumns(20);
        txt_task_desc.setRows(5);
        subpanelDaftarTugas.add(txt_task_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 240, 180, -1));

        lbl_task_desc.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_task_desc.setText("Description");
        subpanelDaftarTugas.add(lbl_task_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, -1, -1));
        subpanelDaftarTugas.add(txt_task_title, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 188, 180, -1));

        lbl_task_title.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_task_title.setText("Title");
        subpanelDaftarTugas.add(lbl_task_title, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, -1));

        txt_task_id.setEnabled(false);
        subpanelDaftarTugas.add(txt_task_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 180, -1));

        lbl_id_task.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_id_task.setText("Task ID");
        subpanelDaftarTugas.add(lbl_id_task, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));

        lblDepartmentTitle1.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblDepartmentTitle1.setForeground(java.awt.Color.gray);
        lblDepartmentTitle1.setText("DAFTAR TUGAS");
        subpanelDaftarTugas.add(lblDepartmentTitle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        lblDepartmentSubTitle2.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblDepartmentSubTitle2.setForeground(java.awt.Color.gray);
        lblDepartmentSubTitle2.setText("Add Task");
        subpanelDaftarTugas.add(lblDepartmentSubTitle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        jSeparator11.setForeground(new java.awt.Color(241, 241, 241));
        subpanelDaftarTugas.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 950, 20));

        tabelDaftarTugasSP.setBackground(new java.awt.Color(241, 241, 241));
        tabelDaftarTugasSP.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        tabelDaftarTugasSP.setAutoscrolls(true);
        tabelDaftarTugasSP.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tabelDaftarTugas.setBackground(new java.awt.Color(240, 240, 240));
        tabelDaftarTugas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Tugas", "Title", "Description", "Date", "Deadline", "Opened", "PIC", "Department", "Status"
            }
        ));
        tabelDaftarTugas.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabelDaftarTugas.setAutoscrolls(false);
        tabelDaftarTugas.setGridColor(new java.awt.Color(241, 241, 241));
        tabelDaftarTugas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelDaftarTugasMouseClicked(evt);
            }
        });
        tabelDaftarTugasSP.setViewportView(tabelDaftarTugas);
        if (tabelDaftarTugas.getColumnModel().getColumnCount() > 0) {
            tabelDaftarTugas.getColumnModel().getColumn(0).setPreferredWidth(150);
            tabelDaftarTugas.getColumnModel().getColumn(1).setPreferredWidth(150);
            tabelDaftarTugas.getColumnModel().getColumn(2).setPreferredWidth(300);
            tabelDaftarTugas.getColumnModel().getColumn(3).setPreferredWidth(150);
            tabelDaftarTugas.getColumnModel().getColumn(4).setPreferredWidth(150);
            tabelDaftarTugas.getColumnModel().getColumn(5).setPreferredWidth(150);
            tabelDaftarTugas.getColumnModel().getColumn(6).setPreferredWidth(150);
            tabelDaftarTugas.getColumnModel().getColumn(7).setPreferredWidth(200);
            tabelDaftarTugas.getColumnModel().getColumn(8).setPreferredWidth(150);
        }

        subpanelDaftarTugas.add(tabelDaftarTugasSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 130, 580, 260));

        lblDepartmentSubTitle3.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblDepartmentSubTitle3.setForeground(java.awt.Color.gray);
        lblDepartmentSubTitle3.setText("Management");
        subpanelDaftarTugas.add(lblDepartmentSubTitle3, new org.netbeans.lib.awtextra.AbsoluteConstraints(335, 53, -1, -1));

        btn_upstate_task.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/btnActive.png"))); // NOI18N
        btn_upstate_task.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_upstate_task.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_upstate_taskMouseClicked(evt);
            }
        });
        subpanelDaftarTugas.add(btn_upstate_task, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 87, -1, -1));

        btn_delete_task.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/btnDelete.png"))); // NOI18N
        btn_delete_task.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_delete_task.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_delete_taskMouseClicked(evt);
            }
        });
        subpanelDaftarTugas.add(btn_delete_task, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 87, -1, -1));

        btn_upstate_task_undone.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/btnDeactive3.png"))); // NOI18N
        btn_upstate_task_undone.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_upstate_task_undone.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_upstate_task_undoneMouseClicked(evt);
            }
        });
        subpanelDaftarTugas.add(btn_upstate_task_undone, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 87, -1, -1));

        panelMgmDashboard.add(subpanelDaftarTugas, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 538));

        subpanelDataDepartment.setBackground(new java.awt.Color(255, 255, 255));
        subpanelDataDepartment.setPreferredSize(new java.awt.Dimension(1010, 538));
        subpanelDataDepartment.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSaveDepartment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconSave.png"))); // NOI18N
        btnSaveDepartment.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSaveDepartment.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveDepartmentMouseClicked(evt);
            }
        });
        subpanelDataDepartment.add(btnSaveDepartment, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 420, -1, -1));

        btnClearDepartment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconClear.png"))); // NOI18N
        btnClearDepartment.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClearDepartment.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClearDepartmentMouseClicked(evt);
            }
        });
        subpanelDataDepartment.add(btnClearDepartment, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 420, -1, -1));

        lbl_list_dep.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_list_dep.setForeground(java.awt.Color.gray);
        lbl_list_dep.setText("List Department");
        subpanelDataDepartment.add(lbl_list_dep, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 100, -1, -1));

        cb_dep_status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Status -", "Active", "Inactive" }));
        subpanelDataDepartment.add(cb_dep_status, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 360, 180, -1));

        lbl_dep_status.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_dep_status.setText("Status");
        subpanelDataDepartment.add(lbl_dep_status, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, -1, -1));

        txt_dep_desc.setBackground(new java.awt.Color(240, 240, 240));
        txt_dep_desc.setColumns(20);
        txt_dep_desc.setRows(5);
        subpanelDataDepartment.add(txt_dep_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 240, 180, -1));

        lbl_dep_desc.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_dep_desc.setText("Description");
        subpanelDataDepartment.add(lbl_dep_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, -1, -1));

        cb_dep_cat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Department Category-", "Back-Office", "Front-Office" }));
        subpanelDataDepartment.add(cb_dep_cat, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 190, 180, -1));

        lbl_dep_cat.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_dep_cat.setText("Category");
        subpanelDataDepartment.add(lbl_dep_cat, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, -1));
        subpanelDataDepartment.add(txt_dep_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 180, -1));

        lbl_id_dep.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_id_dep.setText("ID Department");
        subpanelDataDepartment.add(lbl_id_dep, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));

        lblDepartmentTitle.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblDepartmentTitle.setForeground(java.awt.Color.gray);
        lblDepartmentTitle.setText("DATA DEPARTMENT");
        subpanelDataDepartment.add(lblDepartmentTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        lblDepartmentSubTitle.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblDepartmentSubTitle.setForeground(java.awt.Color.gray);
        lblDepartmentSubTitle.setText("Add Department");
        subpanelDataDepartment.add(lblDepartmentSubTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        jSeparator9.setForeground(new java.awt.Color(241, 241, 241));
        subpanelDataDepartment.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 950, 20));

        tabelDataDepartmentSP.setBackground(new java.awt.Color(241, 241, 241));

        tabelDataDepartment.setBackground(new java.awt.Color(240, 240, 240));
        tabelDataDepartment.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID Department", "Category", "Description", "Status"
            }
        ));
        tabelDataDepartment.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabelDataDepartment.setGridColor(new java.awt.Color(241, 241, 241));
        tabelDataDepartment.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelDataDepartmentMouseClicked(evt);
            }
        });
        tabelDataDepartmentSP.setViewportView(tabelDataDepartment);
        if (tabelDataDepartment.getColumnModel().getColumnCount() > 0) {
            tabelDataDepartment.getColumnModel().getColumn(0).setPreferredWidth(200);
            tabelDataDepartment.getColumnModel().getColumn(1).setPreferredWidth(150);
            tabelDataDepartment.getColumnModel().getColumn(2).setPreferredWidth(200);
            tabelDataDepartment.getColumnModel().getColumn(3).setPreferredWidth(150);
        }

        subpanelDataDepartment.add(tabelDataDepartmentSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 130, 580, 260));
        subpanelDataDepartment.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 190, -1, -1));

        lblDepartmentSubTitle1.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblDepartmentSubTitle1.setForeground(java.awt.Color.gray);
        lblDepartmentSubTitle1.setText("Management");
        subpanelDataDepartment.add(lblDepartmentSubTitle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 53, -1, -1));

        btn_active_dep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/btnActive.png"))); // NOI18N
        btn_active_dep.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_active_dep.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_active_depMouseClicked(evt);
            }
        });
        subpanelDataDepartment.add(btn_active_dep, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 87, -1, -1));

        btn_deactive_dep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/btnDeactive3.png"))); // NOI18N
        btn_deactive_dep.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_deactive_dep.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_deactive_depMouseClicked(evt);
            }
        });
        subpanelDataDepartment.add(btn_deactive_dep, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 87, -1, -1));

        panelMgmDashboard.add(subpanelDataDepartment, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 538));

        subpanelDataKaryawan.setBackground(new java.awt.Color(255, 255, 255));
        subpanelDataKaryawan.setPreferredSize(new java.awt.Dimension(1010, 538));
        subpanelDataKaryawan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_searchnama.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_searchnama.setText("Search Nama Karyawan");
        subpanelDataKaryawan.add(lbl_searchnama, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, -1));

        lbl_searchdep.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_searchdep.setText("Search Department Karyawan");
        subpanelDataKaryawan.add(lbl_searchdep, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 190, -1, -1));

        lbl_searchidkyw.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_searchidkyw.setText("Search ID Karyawan");
        subpanelDataKaryawan.add(lbl_searchidkyw, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 100, -1, -1));

        lbl_searchnoreg.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_searchnoreg.setText("Search Nomor Registrasi ");
        subpanelDataKaryawan.add(lbl_searchnoreg, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        jPanel3.setBackground(new java.awt.Color(241, 241, 241));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_searchkyw_byname.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        txt_searchkyw_byname.setForeground(new java.awt.Color(204, 204, 204));
        txt_searchkyw_byname.setText("Press enter to search ...");
        txt_searchkyw_byname.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt_searchkyw_byname.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_searchkyw_bynameMouseClicked(evt);
            }
        });
        txt_searchkyw_byname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_searchkyw_bynameKeyPressed(evt);
            }
        });
        jPanel3.add(txt_searchkyw_byname, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 10, 430, 40));

        subpanelDataKaryawan.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 460, 60));

        jPanel4.setBackground(new java.awt.Color(241, 241, 241));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_searchkyw_byDep.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        txt_searchkyw_byDep.setForeground(new java.awt.Color(204, 204, 204));
        txt_searchkyw_byDep.setText("Press enter to search ...");
        txt_searchkyw_byDep.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt_searchkyw_byDep.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_searchkyw_byDepMouseClicked(evt);
            }
        });
        txt_searchkyw_byDep.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_searchkyw_byDepKeyPressed(evt);
            }
        });
        jPanel4.add(txt_searchkyw_byDep, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 10, 430, 40));

        subpanelDataKaryawan.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 210, 460, 60));

        jPanel2.setBackground(new java.awt.Color(241, 241, 241));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_searchkyw_bynoreg.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        txt_searchkyw_bynoreg.setForeground(new java.awt.Color(204, 204, 204));
        txt_searchkyw_bynoreg.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_searchkyw_bynoreg.setText("Press enter to search ...");
        txt_searchkyw_bynoreg.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt_searchkyw_bynoreg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_searchkyw_bynoregMouseClicked(evt);
            }
        });
        txt_searchkyw_bynoreg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_searchkyw_bynoregKeyPressed(evt);
            }
        });
        jPanel2.add(txt_searchkyw_bynoreg, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 10, 430, 40));

        subpanelDataKaryawan.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 460, 60));

        jPanel1.setBackground(new java.awt.Color(241, 241, 241));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_searchkyw_byId.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        txt_searchkyw_byId.setForeground(new java.awt.Color(204, 204, 204));
        txt_searchkyw_byId.setText("Press enter to search ...");
        txt_searchkyw_byId.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt_searchkyw_byId.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_searchkyw_byIdMouseClicked(evt);
            }
        });
        txt_searchkyw_byId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_searchkyw_byIdKeyPressed(evt);
            }
        });
        jPanel1.add(txt_searchkyw_byId, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 10, 430, 40));

        subpanelDataKaryawan.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 120, 460, 60));

        lblDataKaryawanTitel.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblDataKaryawanTitel.setForeground(java.awt.Color.gray);
        lblDataKaryawanTitel.setText("DATA KARYAWAN");
        subpanelDataKaryawan.add(lblDataKaryawanTitel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        lbl_management.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_management.setForeground(java.awt.Color.gray);
        lbl_management.setText("Management");
        subpanelDataKaryawan.add(lbl_management, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 53, -1, -1));

        jSeparator8.setForeground(new java.awt.Color(241, 241, 241));
        subpanelDataKaryawan.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 950, 20));

        tabelDataKaryawanSP.setBackground(new java.awt.Color(241, 241, 241));

        tabelDataKaryawan.setBackground(new java.awt.Color(240, 240, 240));
        tabelDataKaryawan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Karyawan", "No Regis", "Department", "Nama", "POB", "DOB", "Gender", "Phone", "Mail"
            }
        ));
        tabelDataKaryawan.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabelDataKaryawan.setGridColor(new java.awt.Color(241, 241, 241));
        tabelDataKaryawan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelDataKaryawanMouseClicked(evt);
            }
        });
        tabelDataKaryawanSP.setViewportView(tabelDataKaryawan);
        if (tabelDataKaryawan.getColumnModel().getColumnCount() > 0) {
            tabelDataKaryawan.getColumnModel().getColumn(0).setPreferredWidth(150);
            tabelDataKaryawan.getColumnModel().getColumn(1).setPreferredWidth(150);
            tabelDataKaryawan.getColumnModel().getColumn(2).setPreferredWidth(150);
            tabelDataKaryawan.getColumnModel().getColumn(3).setPreferredWidth(250);
            tabelDataKaryawan.getColumnModel().getColumn(4).setPreferredWidth(150);
            tabelDataKaryawan.getColumnModel().getColumn(5).setPreferredWidth(150);
            tabelDataKaryawan.getColumnModel().getColumn(6).setPreferredWidth(150);
            tabelDataKaryawan.getColumnModel().getColumn(7).setPreferredWidth(150);
            tabelDataKaryawan.getColumnModel().getColumn(8).setPreferredWidth(150);
        }

        subpanelDataKaryawan.add(tabelDataKaryawanSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 950, 200));

        lbl_daftarkyw.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_daftarkyw.setForeground(java.awt.Color.gray);
        lbl_daftarkyw.setText("Daftar Karyawan");
        subpanelDataKaryawan.add(lbl_daftarkyw, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 290, -1, -1));

        btn_printTrans.setBackground(new java.awt.Color(153, 0, 0));
        btn_printTrans.setForeground(new java.awt.Color(255, 255, 255));
        btn_printTrans.setText("PRINT COCARD");
        btn_printTrans.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_printTransMouseClicked(evt);
            }
        });
        btn_printTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_printTransActionPerformed(evt);
            }
        });
        subpanelDataKaryawan.add(btn_printTrans, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 280, 140, 30));

        panelMgmDashboard.add(subpanelDataKaryawan, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 538));
        panelMgmDashboard.add(jSeparator12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, 170, 10));

        menuErrorManagement.setBackground(new java.awt.Color(241, 241, 241));
        menuErrorManagement.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuErrorManagement.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuErrorManagementMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuErrorManagementMouseExited(evt);
            }
        });
        menuErrorManagement.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconMgmErrorReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/IconErrorReport.png"))); // NOI18N
        iconMgmErrorReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconMgmErrorReportMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconMgmErrorReportMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconMgmErrorReportMouseExited(evt);
            }
        });
        menuErrorManagement.add(iconMgmErrorReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        panelMgmDashboard.add(menuErrorManagement, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 490, 170, 30));
        panelMgmDashboard.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 485, 170, 10));

        menuDaftarTugas.setBackground(new java.awt.Color(241, 241, 241));
        menuDaftarTugas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuDaftarTugas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuDaftarTugasMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuDaftarTugasMouseExited(evt);
            }
        });
        menuDaftarTugas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconMgmDaftarTugas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/IconDaftarTugas.png"))); // NOI18N
        iconMgmDaftarTugas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconMgmDaftarTugasMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconMgmDaftarTugasMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconMgmDaftarTugasMouseExited(evt);
            }
        });
        menuDaftarTugas.add(iconMgmDaftarTugas, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 7, -1, -1));

        panelMgmDashboard.add(menuDaftarTugas, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 447, 170, 30));
        panelMgmDashboard.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 437, 170, 10));

        menuDataDepartment.setBackground(new java.awt.Color(241, 241, 241));
        menuDataDepartment.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuDataDepartment.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuDataDepartmentMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuDataDepartmentMouseExited(evt);
            }
        });
        menuDataDepartment.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconMgmDataDepart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/IconDataDepartment.png"))); // NOI18N
        iconMgmDataDepart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconMgmDataDepartMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconMgmDataDepartMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconMgmDataDepartMouseExited(evt);
            }
        });
        menuDataDepartment.add(iconMgmDataDepart, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        panelMgmDashboard.add(menuDataDepartment, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 170, 30));
        panelMgmDashboard.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 395, 170, 10));

        menuDataKaryawan.setBackground(new java.awt.Color(241, 241, 241));
        menuDataKaryawan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuDataKaryawan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuDataKaryawanMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuDataKaryawanMouseExited(evt);
            }
        });
        menuDataKaryawan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconMgmDataKyw.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/IconDataKaryawan.png"))); // NOI18N
        iconMgmDataKyw.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconMgmDataKywMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconMgmDataKywMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconMgmDataKywMouseExited(evt);
            }
        });
        menuDataKaryawan.add(iconMgmDataKyw, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 7, -1, -1));

        panelMgmDashboard.add(menuDataKaryawan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 170, 30));
        panelMgmDashboard.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 353, 170, 10));

        menuCreateAccount.setBackground(new java.awt.Color(241, 241, 241));
        menuCreateAccount.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuCreateAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuCreateAccountMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuCreateAccountMouseExited(evt);
            }
        });
        menuCreateAccount.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconMgmCreateAcc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/IconCreateAccount.png"))); // NOI18N
        iconMgmCreateAcc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconMgmCreateAccMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconMgmCreateAccMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconMgmCreateAccMouseExited(evt);
            }
        });
        menuCreateAccount.add(iconMgmCreateAcc, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 9, -1, -1));

        panelMgmDashboard.add(menuCreateAccount, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 317, 170, 30));
        panelMgmDashboard.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 170, 10));

        menuDashboard.setBackground(new java.awt.Color(241, 241, 241));
        menuDashboard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuDashboardMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuDashboardMouseExited(evt);
            }
        });
        menuDashboard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconMgmDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/IconDashboard.png"))); // NOI18N
        iconMgmDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconMgmDashboardMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconMgmDashboardMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconMgmDashboardMouseExited(evt);
            }
        });
        menuDashboard.add(iconMgmDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 7, -1, -1));

        panelMgmDashboard.add(menuDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 275, 170, 30));
        panelMgmDashboard.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 170, 10));

        btnChangeState.setText("Change State");
        btnChangeState.setToolTipText("Click to Change State");
        btnChangeState.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnChangeState.setMaximumSize(new java.awt.Dimension(73, 23));
        btnChangeState.setMinimumSize(new java.awt.Dimension(74, 23));
        btnChangeState.setPreferredSize(new java.awt.Dimension(78, 23));
        btnChangeState.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnChangeStateMouseClicked(evt);
            }
        });
        panelMgmDashboard.add(btnChangeState, new org.netbeans.lib.awtextra.AbsoluteConstraints(83, 230, 100, -1));

        btnHideIP.setText("Hide IP");
        btnHideIP.setToolTipText("Click to Hide IP");
        btnHideIP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHideIP.setMaximumSize(new java.awt.Dimension(73, 23));
        btnHideIP.setMinimumSize(new java.awt.Dimension(74, 23));
        btnHideIP.setPreferredSize(new java.awt.Dimension(73, 23));
        btnHideIP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHideIPMouseClicked(evt);
            }
        });
        panelMgmDashboard.add(btnHideIP, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 230, 70, -1));

        btnShowIP.setText("Show IP");
        btnShowIP.setToolTipText("Click to show IP");
        btnShowIP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnShowIP.setMaximumSize(new java.awt.Dimension(73, 23));
        btnShowIP.setMinimumSize(new java.awt.Dimension(73, 23));
        btnShowIP.setPreferredSize(new java.awt.Dimension(73, 23));
        btnShowIP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnShowIPMouseClicked(evt);
            }
        });
        panelMgmDashboard.add(btnShowIP, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 230, -1, -1));

        txtServerTime.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        txtServerTime.setText("SERVER TIME");
        panelMgmDashboard.add(txtServerTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 693, -1, -1));

        txt_stateBig.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        txt_stateBig.setText("SERVER STATE");
        panelMgmDashboard.add(txt_stateBig, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 693, -1, -1));

        lblStateBig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OnlineState.png"))); // NOI18N
        panelMgmDashboard.add(lblStateBig, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 693, -1, -1));

        lblState.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OnlineState.png"))); // NOI18N
        panelMgmDashboard.add(lblState, new org.netbeans.lib.awtextra.AbsoluteConstraints(78, 208, -1, -1));

        txt_state.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        txt_state.setText("Online");
        panelMgmDashboard.add(txt_state, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 210, -1, -1));

        jLabel7.setFont(new java.awt.Font("Gotham Medium", 1, 11)); // NOI18N
        jLabel7.setText("Keyko");
        panelMgmDashboard.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(595, 695, -1, -1));

        jLabel6.setFont(new java.awt.Font("Gotham Light", 2, 11)); // NOI18N
        jLabel6.setText("powered by");
        panelMgmDashboard.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 695, -1, -1));

        jLabel5.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        jLabel5.setText("NOB Tech - ");
        panelMgmDashboard.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(465, 695, -1, -1));

        txTMgmNama.setFont(new java.awt.Font("Gotham Medium", 1, 12)); // NOI18N
        txTMgmNama.setText("Management");
        panelMgmDashboard.add(txTMgmNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(79, 190, -1, -1));

        jLabel3.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        jLabel3.setText("Hello, ");
        panelMgmDashboard.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 170, -1, -1));

        iconAdmin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/AdminLogo.png"))); // NOI18N
        panelMgmDashboard.add(iconAdmin, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        bgManagement.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/BgClient - Management Dashboard.png"))); // NOI18N
        panelMgmDashboard.add(bgManagement, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelMgmDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 721));

        panelKilledbyServer.setBackground(new java.awt.Color(255, 255, 255));
        panelKilledbyServer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/MgmServerDisconnect.png"))); // NOI18N
        panelKilledbyServer.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelKilledbyServer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 720));

        panelMgmLogin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelMgmLogin.add(txtUsernameMgm, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 380, 230, 40));
        panelMgmLogin.add(txtPasswordMgm, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 470, 230, 40));

        btnLogin.setText("LOGIN");
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLoginMouseClicked(evt);
            }
        });
        panelMgmLogin.add(btnLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 550, -1, -1));

        btnReset.setText("CANCEL");
        btnReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnResetMouseClicked(evt);
            }
        });
        panelMgmLogin.add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 550, -1, -1));

        panelLoginMgm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/NOB - ManagementLogin.png"))); // NOI18N
        panelMgmLogin.add(panelLoginMgm, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 120, -1, -1));

        bgClientManagementLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/BgClient - Management Login.png"))); // NOI18N
        panelMgmLogin.add(bgClientManagementLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelMgmLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 721));

        panelMgmLandingPage.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconHome1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconHomeYellow.png"))); // NOI18N
        iconHome1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconHome1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconHome1MouseClicked(evt);
            }
        });
        panelMgmLandingPage.add(iconHome1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        bgLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/NOBLogoV1 - Management.png"))); // NOI18N
        bgLogo.setToolTipText("");
        bgLogo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bgLogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bgLogoMouseClicked(evt);
            }
        });
        panelMgmLandingPage.add(bgLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 200, -1, -1));

        bgClientManagement.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/BgClient - Management.png"))); // NOI18N
        panelMgmLandingPage.add(bgClientManagement, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelMgmLandingPage, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 720));

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
        iconHRD.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                iconHRDAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
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

    private void btnLoginMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLoginMouseClicked
        // TODO add your handling code here:
        String lettercode = txtUsernameMgm.getText().substring(0, 3);
        System.out.println(lettercode);
        if (txtUsernameMgm.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please Input Your Username", "Sign In Error", JOptionPane.ERROR_MESSAGE);
            txtUsernameMgm.requestFocus();
        } else if (txtPasswordMgm.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please Input Your Password", "Sign In Error", JOptionPane.ERROR_MESSAGE);
            txtPasswordMgm.requestFocus();
        } else if (txtUsernameMgm.getText().substring(0, 3).equals("MGM")) {
            Entity_Signin ESI = new Entity_Signin();
            ESI.setUsername(txtUsernameMgm.getText());
            ESI.setPassword(txtPasswordMgm.getText());
            ESI.setStatus("Active");
            ESI.setDlc("MGM");
            try {
                int hasil = loginDao.Login(ESI);
                if (hasil == 1) {
                    JOptionPane.showMessageDialog(null, "Sign In Success", "Sign-In Success", JOptionPane.INFORMATION_MESSAGE);
                    Mode7();
                    SetMgmStatusOnline();
                    CekStatus5Sec();
                    setIPMgm();
                } else {
                    JOptionPane.showMessageDialog(null, "Password or Username Does Not Match", "Sign-In Error", JOptionPane.ERROR_MESSAGE);
                    txtUsernameMgm.setText("");
                    txtPasswordMgm.setText("");
                }
            } catch (RemoteException ex) {
                Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Sorry, You Have No Access to Management Area", "Sign In Error", JOptionPane.ERROR_MESSAGE);
            txtPasswordMgm.requestFocus();
            txtUsernameMgm.setText("");
            txtPasswordMgm.setText("");
        }

        whoisonline = txtUsernameMgm.getText();
//        System.out.println(whoisonline);
        Entity_Signin ESI = new Entity_Signin();
        ESI.setUsername(whoisonline);
        try {
            String hasil = loginDao.CekNamaOnline(ESI);
            txTMgmNama.setText(hasil);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnLoginMouseClicked

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnResetMouseClicked

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

    private void iconManagementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconManagementMouseClicked
        // TODO add your handling code here:
        Mode5();
    }//GEN-LAST:event_iconManagementMouseClicked

    private void bgLogoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bgLogoMouseClicked
        // TODO add your handling code here:
        Mode6();
    }//GEN-LAST:event_bgLogoMouseClicked

    private void menuCreateAccountMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuCreateAccountMouseEntered
        // TODO add your handling code here:
        Color lightYellow = new Color(255, 200, 71);
        menuCreateAccount.setBackground(lightYellow);
    }//GEN-LAST:event_menuCreateAccountMouseEntered

    private void menuCreateAccountMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuCreateAccountMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuCreateAccount.setBackground(lightGray);
    }//GEN-LAST:event_menuCreateAccountMouseExited

    private void menuDashboardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuDashboardMouseEntered
        // TODO add your handling code here:
        Color lightYellow = new Color(255, 200, 71);
        menuDashboard.setBackground(lightYellow);
    }//GEN-LAST:event_menuDashboardMouseEntered

    private void menuDashboardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuDashboardMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuDashboard.setBackground(lightGray);
    }//GEN-LAST:event_menuDashboardMouseExited

    private void btnChangeStateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChangeStateMouseClicked
        // TODO add your handling code here:
        int hasilUpdateStatusMgm = 0;
        Object[] options = {"Online", "Idle", "Invisible", "Offline"};
        int reply = JOptionPane.showOptionDialog(null, "Choose your server state : ", "State Chooser", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                if (txt_state.getText().equals("Online")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Online", "Warning Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int hasilSetMgmStatus = loginDao.UpdateStatusOnline(kodeSistem);
                        if (hasilSetMgmStatus == 1) {
                            txt_stateBig.setText("Server is Well-Connected");
                            lblState.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OnlineState.png")));
                            lblStateBig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OnlineState.png")));
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case 1:
                if (txt_state.getText().equals("Idle")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Idle", "Warning Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int hasilSetMgmStatus = loginDao.UpdateStatusIdle(kodeSistem);
                        if (hasilSetMgmStatus == 1) {
                            txt_stateBig.setText("Server is Well-Connected");
                            lblState.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/IdleState.png")));
                            lblStateBig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/IdleState.png")));
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case 2:
                if (txt_state.getText().equals("Invisible")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Invisible", "Warning Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int hasilSetMgmStatus = loginDao.UpdateStatusInvisible(kodeSistem);
                        if (hasilSetMgmStatus == 1) {
                            txt_stateBig.setText("Server is Well-Connected");
                            lblState.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/InvisState.png")));
                            lblStateBig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/InvisState.png")));
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case 3:
                if (txt_state.getText().equals("Offline")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Offline", "Warning Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int hasilSetMgmStatus = loginDao.UpdateStatusDisconnect(kodeSistem);
                        if (hasilSetMgmStatus == 1) {
                            txt_stateBig.setText("Server is Disconnected");
                            lblState.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OffState.png")));
                            lblStateBig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OffState.png")));
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
    }//GEN-LAST:event_btnChangeStateMouseClicked

    private void btnHideIPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHideIPMouseClicked
        // TODO add your handling code here:
        whoisonline = txtUsernameMgm.getText();
//        System.out.println(whoisonline);
        Entity_Signin ESI = new Entity_Signin();
        ESI.setUsername(whoisonline);
        try {
            String hasil = loginDao.CekNamaOnline(ESI);
            txTMgmNama.setText(hasil);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        ShowIPMode();
    }//GEN-LAST:event_btnHideIPMouseClicked

    private void btnShowIPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnShowIPMouseClicked
        // TODO add your handling code here:
        txTMgmNama.setText(ip);
        HideIPMode();
    }//GEN-LAST:event_btnShowIPMouseClicked

    private void cb_divisionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_divisionActionPerformed
        // TODO add your handling code here:
        btnGetID.setEnabled(true);
    }//GEN-LAST:event_cb_divisionActionPerformed

    private void btnGetIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGetIDActionPerformed
        // TODO add your handling code here:
        if (cb_division.getSelectedItem().equals("MGM")) {
            if (modesu.equals("save")) {
                init_Mgm_ID();
                GetQrCode();
                txt_password.setEnabled(true);
            } else {
                init_Mgm_ID2();
                GetQrCode();
                txt_password.setEnabled(true);
            }
        } else if (cb_division.getSelectedItem().equals("KEU")) {
            if (modesu.equals("save")) {
                init_Keu_ID();
                GetQrCode();
                txt_password.setEnabled(true);
            } else {
                init_Keu_ID2();
                GetQrCode();
                txt_password.setEnabled(true);
            }
        } else if (cb_division.getSelectedItem().equals("HRD")) {
            if (modesu.equals("save")) {
                init_HRD_ID();
                GetQrCode();
                txt_password.setEnabled(true);
            } else {
                init_HRD_ID2();
                GetQrCode();
                txt_password.setEnabled(true);
            }
        } else if (cb_division.getSelectedItem().equals("CAS")) {
            if (modesu.equals("save")) {
                init_Cashier_ID();
                GetQrCode();
                txt_password.setEnabled(true);
            } else {
                init_Cashier_ID2();
                GetQrCode();
                txt_password.setEnabled(true);
            }
        } else if (cb_division.getSelectedItem().equals("WRH")) {
            if (modesu.equals("save")) {
                init_Warehouse_ID();
                GetQrCode();
                txt_password.setEnabled(true);
            } else {
                init_Warehouse_ID2();
                GetQrCode();
                txt_password.setEnabled(true);
            }
        }
    }//GEN-LAST:event_btnGetIDActionPerformed

    private void qrsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qrsaveActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser();
        int rv = fc.showDialog(lbl_qrcode, null);
        if (rv == JFileChooser.APPROVE_OPTION) {
            try {
                String ruta = fc.getSelectedFile().getAbsolutePath() + ".png";
                fout = new FileOutputStream(new File(ruta));
                fout.write(out.toByteArray());
                fout.flush();
                fout.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
    }//GEN-LAST:event_qrsaveActionPerformed

    private void su_attachgbrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_su_attachgbrActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(new FileNameExtensionFilter("jpg|png|bmp", "jpg", "png", "bmp"));
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();

        filename = f.getAbsolutePath();
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(filename).getImage().getScaledInstance(lbl_foto.getWidth(), lbl_foto.getHeight(), Image.SCALE_DEFAULT));
        su_fototext.setText(filename);
        lbl_foto.setIcon(imageIcon);
        try {
            File images = new File(filename);
            FileInputStream fis = new FileInputStream(images);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[10000];

            for (int readNum; (readNum = fis.read(buf)) != -1;) {

                bos.write(buf, 0, readNum);
            }
            person_image = bos.toByteArray();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_su_attachgbrActionPerformed

    private void iconMgmDashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconMgmDashboardMouseClicked
        // TODO add your handling code here:
        Mode7();
        HitungJumlahStaff();
        HitungJumlahDistributor();
        HitungJumlahTugas();
        last_session = 7;
    }//GEN-LAST:event_iconMgmDashboardMouseClicked

    private void iconMgmCreateAccMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconMgmCreateAccMouseClicked
        // TODO add your handling code here:
        Mode8();
        Mode8_1();
        loadComboBoxDepartment();
        last_session = 8;
    }//GEN-LAST:event_iconMgmCreateAccMouseClicked

    private void iconMgmDashboardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconMgmDashboardMouseEntered
        // TODO add your handling code here:
        Color lightYellow = new Color(255, 200, 71);
        menuDashboard.setBackground(lightYellow);
    }//GEN-LAST:event_iconMgmDashboardMouseEntered

    private void iconMgmDashboardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconMgmDashboardMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuDashboard.setBackground(lightGray);
    }//GEN-LAST:event_iconMgmDashboardMouseExited

    private void iconMgmCreateAccMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconMgmCreateAccMouseEntered
        // TODO add your handling code here:
        Color lightYellow = new Color(255, 200, 71);
        menuCreateAccount.setBackground(lightYellow);
    }//GEN-LAST:event_iconMgmCreateAccMouseEntered

    private void iconMgmCreateAccMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconMgmCreateAccMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuCreateAccount.setBackground(lightGray);
    }//GEN-LAST:event_iconMgmCreateAccMouseExited

    private void iconMgmDataKywMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconMgmDataKywMouseClicked
        // TODO add your handling code here:
        Mode9();
        txt_searchkyw_bynoreg.setText("Please enter to search ...");
        txt_searchkyw_byname.setText("Please enter to search ...");
        txt_searchkyw_byId.setText("Please enter to search ...");
        txt_searchkyw_byDep.setText("Please enter to search ...");
        last_session = 9;
        initTableDaftarKaryawan();
        loadDaftarKaryawan();
    }//GEN-LAST:event_iconMgmDataKywMouseClicked

    private void iconMgmDataKywMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconMgmDataKywMouseEntered
        // TODO add your handling code here:
        Color lightYellow = new Color(255, 200, 71);
        menuDataKaryawan.setBackground(lightYellow);
    }//GEN-LAST:event_iconMgmDataKywMouseEntered

    private void iconMgmDataKywMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconMgmDataKywMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuDataKaryawan.setBackground(lightGray);
    }//GEN-LAST:event_iconMgmDataKywMouseExited

    private void menuDataKaryawanMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuDataKaryawanMouseEntered
        // TODO add your handling code here:
        Color lightYellow = new Color(255, 200, 71);
        menuDataKaryawan.setBackground(lightYellow);
    }//GEN-LAST:event_menuDataKaryawanMouseEntered

    private void menuDataKaryawanMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuDataKaryawanMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuDataKaryawan.setBackground(lightGray);
    }//GEN-LAST:event_menuDataKaryawanMouseExited

    private void btnSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveMouseClicked
        // TODO add your handling code here:
        String Gender;
        String SubDepart = null;
        Entity_Signup ESU = new Entity_Signup();
        ESU.setId(txt_username_div.getText());
        ESU.setNoregister(txtNoRegis.getText());
        String depcode = (String) cb_division.getSelectedItem();
        ESU.setDepartment(depcode);
        ESU.setName(txtNama.getText());
        ESU.setPob(txtPob.getText());
        ESU.setDob(txtDob.getDate());
        if (su_rbpria.isSelected()) {
            Gender = "Pria";
        } else {
            Gender = "Wanita";
        }
        ESU.setGender(Gender);
        ESU.setPhone(txtPhone.getText());
        ESU.setEmail(txtMail.getText());
        try {
            File images = new File(filename);
            FileInputStream fis = new FileInputStream(images);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];

            for (int readNum; (readNum = fis.read(buf)) != -1;) {

                bos.write(buf, 0, readNum);
            }
            person_image = bos.toByteArray();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        ESU.setFoto(person_image);
        GetQrCode();
        ESU.setQr(qr_image);

        Entity_Signin ESI = new Entity_Signin();
        ESI.setRegister(txtNoRegis.getText());
        ESI.setUsername(txt_username_div.getText());
        ESI.setPassword(txt_password.getText());
        try {
            int hasil = userDao.saveDataAkun(ESU);
            int hasil2 = userDao.saveDataLogin(ESI);

            if (hasil == 1 && hasil2 == 1) {
                JOptionPane.showMessageDialog(null, "Save Data Success", "Save Success", JOptionPane.INFORMATION_MESSAGE);
                init_Register_Number();
            } else {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Save Data Failed", "Save Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        ClearFormCreateAccount();
    }//GEN-LAST:event_btnSaveMouseClicked

    private void btnUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpdateMouseClicked
        // TODO add your handling code here:
        String Gender;
        String SubDepart = null;
        Entity_Signup ESU = new Entity_Signup();
        ESU.setId(txt_username_div.getText());
        ESU.setNoregister(txtNoRegis.getText());
        String department = (String) cb_division.getSelectedItem();
        String depcode = null;
        if (department.equals("Management")) {
            depcode = "MGM";
        } else if (department.equals("Keuangan")) {
            depcode = "KEU";
        } else if (department.equals("Human R and D")) {
            depcode = "HRD";
        } else if (department.equals("Front Cashier")) {
            depcode = "CAS";
        } else if (department.equals("Warehouse")) {
            depcode = "WRH";
        }
        ESU.setDepartment(depcode);
        ESU.setName(txtNama.getText());
        ESU.setPob(txtPob.getText());
        ESU.setDob(txtDob.getDate());
        if (su_rbpria.isSelected()) {
            Gender = "Pria";
        } else {
            Gender = "Wanita";
        }
        ESU.setGender(Gender);
        ESU.setPhone(txtPhone.getText());
        ESU.setEmail(txtMail.getText());
        try {
            File images = new File(filename);
            FileInputStream fis = new FileInputStream(images);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];

            for (int readNum; (readNum = fis.read(buf)) != -1;) {

                bos.write(buf, 0, readNum);
            }
            person_image = bos.toByteArray();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        ESU.setFoto(person_image);
        GetQrCode();
        ESU.setQr(qr_image);

        Entity_Signin ESI = new Entity_Signin();
        ESI.setRegister(txtNoRegis.getText());
        ESI.setUsername(txt_username_div.getText());
        ESI.setPassword(txt_password.getText());
        try {
            int hasil = userDao.updateUser(ESU);
            int hasil2 = userDao.updateUserLogin(ESI);

            if (hasil == 1 && hasil2 == 1) {
                JOptionPane.showMessageDialog(null, "Update Data Success", "Update Success", JOptionPane.INFORMATION_MESSAGE);
                init_Register_Number();
            } else {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Update Data Failed", "Update Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        ClearFormCreateAccount();
        init_Register_Number();
    }//GEN-LAST:event_btnUpdateMouseClicked

    private void btnClearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClearMouseClicked
        // TODO add your handling code here:
        ClearFormCreateAccount();
    }//GEN-LAST:event_btnClearMouseClicked

    private void iconMgmDataDepartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconMgmDataDepartMouseClicked
        // TODO add your handling code here:
        Mode10();
        last_session = 10;
    }//GEN-LAST:event_iconMgmDataDepartMouseClicked

    private void iconMgmDataDepartMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconMgmDataDepartMouseEntered
        // TODO add your handling code here:
        Color lightYellow = new Color(255, 200, 71);
        menuDataDepartment.setBackground(lightYellow);
    }//GEN-LAST:event_iconMgmDataDepartMouseEntered

    private void iconMgmDataDepartMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconMgmDataDepartMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuDataDepartment.setBackground(lightGray);
    }//GEN-LAST:event_iconMgmDataDepartMouseExited

    private void menuDataDepartmentMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuDataDepartmentMouseEntered
        // TODO add your handling code here:
        Color lightYellow = new Color(255, 200, 71);
        menuDataDepartment.setBackground(lightYellow);
    }//GEN-LAST:event_menuDataDepartmentMouseEntered

    private void menuDataDepartmentMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuDataDepartmentMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuDataDepartment.setBackground(lightGray);
    }//GEN-LAST:event_menuDataDepartmentMouseExited

    private void cbActionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbActionActionPerformed
        // TODO add your handling code here:
        String cbAct = (String) cbAction.getSelectedItem();
        if (cbAct.equals("Save") || cbAct.equals("Choose Action")) {
            Mode8();
            Mode8_1();
        } else {
            Mode8_2();
        }
    }//GEN-LAST:event_cbActionActionPerformed

    private void btnSearchRegNumMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchRegNumMouseClicked
        Mode9();
    }//GEN-LAST:event_btnSearchRegNumMouseClicked

    private void tabelDataKaryawanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelDataKaryawanMouseClicked
        // TODO add your handling code here:
        int row;
        Object[] options = {"Update", "Delete", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "What You Want to Do ? (Update/Delete)", "Guide", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                row = tabelDataKaryawan.rowAtPoint(evt.getPoint());

                String id = tabelDataKaryawan.getValueAt(row, 0).toString();
                txt_username_div.setText(String.valueOf(id));

                String noregis = tabelDataKaryawan.getValueAt(row, 1).toString();
                txtNoRegis.setText(String.valueOf(noregis));

                Mode8();
                break;
            case 1:
                row = tabelDataKaryawan.rowAtPoint(evt.getPoint());
                String noreg = tabelDataKaryawan.getValueAt(row, 1).toString();
                Entity_Signup ESU = new Entity_Signup();
                ESU.setNoregister(noreg);
                Entity_Signin ESI = new Entity_Signin();
                ESI.setRegister(noreg);
                System.out.println(noreg);
                try {
                    int hasil2 = userDao.deleteUserLogin(ESU);
                    int hasil = userDao.deleteUser(ESU);
                    if (hasil == 1 && hasil2 == 1) {
                        JOptionPane.showMessageDialog(null, "Data Deleted", "Delete Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        evt.consume();
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Delete Data Failed", "Delete Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                }
//                Entity.Entity_Buyer EB = new Entity_Buyer();
//                row = tbl_buyer.rowAtPoint(evt.getPoint());
//
//                String iddel = tbl_buyer.getValueAt(row, 0).toString();
//                EB.setBuyer_id(iddel);
//                System.out.println(iddel);
//                Koneksi.getBuyer().delete_buyer(EB);
//                mode2();
                break;
            case 2:
                int baristabelkyw = tabelDataKaryawan.getSelectedRow();
                int kolomtabelkyw = tabelDataKaryawan.getSelectedColumn();
                dataTerpilihTabelKaryawan = tabelDataKaryawan.getValueAt(baristabelkyw, 0).toString();
                break;
            default:
                break;
        }
    }//GEN-LAST:event_tabelDataKaryawanMouseClicked

    private void btn_deactive_depMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_deactive_depMouseClicked
        // TODO add your handling code here:

        Object[] options = {"Yes", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Are you sure to make this Department Inactive ?", "Info",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                if (tabelDataDepartment.getValueAt(baris, 3).toString().equals("Active")) {
                    Entity_Department ED = new Entity_Department();
                    ED.setDep_id(dataTerpilih);
                    ED.setDep_status(dataTerpilih);
                    try {
                        int hasil = departmentDao.updateStatusDepIn(ED);
                        int hasil2 = departmentDao.updateStatusLoginInactive(ED);
                        if (hasil == 1 && hasil2 == 1) {
                            JOptionPane.showMessageDialog(null, "Deactivation Department Success", "Deactivation Success",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            evt.consume();
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(null, "Deactivation Department Failed", "Deactivation Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    loadDaftarDepartment();
                } else {
                    JOptionPane.showMessageDialog(null, "Department already deactivated", "Deactivation Info",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btn_deactive_depMouseClicked

    private void txt_searchkyw_bynameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchkyw_bynameKeyPressed
        // TODO add your handling code here:
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            initTableDaftarKaryawan();
            loadDaftarKaryawanbyName();
        }
    }//GEN-LAST:event_txt_searchkyw_bynameKeyPressed

    private void txt_searchkyw_bynameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_searchkyw_bynameMouseClicked
        // TODO add your handling code here:
        txt_searchkyw_bynoreg.setText("Please enter to search ...");
        txt_searchkyw_byname.setText("");
        txt_searchkyw_byId.setText("Please enter to search ...");
        txt_searchkyw_byDep.setText("Please enter to search ...");
    }//GEN-LAST:event_txt_searchkyw_bynameMouseClicked

    private void txt_searchkyw_bynoregMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_searchkyw_bynoregMouseClicked
        // TODO add your handling code here:
        txt_searchkyw_bynoreg.setText("");
        txt_searchkyw_byname.setText("Please enter to search ...");
        txt_searchkyw_byId.setText("Please enter to search ...");
        txt_searchkyw_byDep.setText("Please enter to search ...");
    }//GEN-LAST:event_txt_searchkyw_bynoregMouseClicked

    private void txt_searchkyw_bynoregKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchkyw_bynoregKeyPressed
        // TODO add your handling code here:
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            initTableDaftarKaryawan();
            loadDaftarKaryawanbyNoreg();
        }
    }//GEN-LAST:event_txt_searchkyw_bynoregKeyPressed

    private void txt_searchkyw_byDepMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_searchkyw_byDepMouseClicked
        // TODO add your handling code here:
        txt_searchkyw_bynoreg.setText("Please enter to search ...");
        txt_searchkyw_byname.setText("Please enter to search ...");
        txt_searchkyw_byId.setText("Please enter to search ...");
        txt_searchkyw_byDep.setText("");
    }//GEN-LAST:event_txt_searchkyw_byDepMouseClicked

    private void txt_searchkyw_byDepKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchkyw_byDepKeyPressed
        // TODO add your handling code here:
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            initTableDaftarKaryawan();
            loadDaftarKaryawanbyDept();
        }
    }//GEN-LAST:event_txt_searchkyw_byDepKeyPressed

    private void txt_searchkyw_byIdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_searchkyw_byIdMouseClicked
        // TODO add your handling code here:
        txt_searchkyw_bynoreg.setText("Please enter to search ...");
        txt_searchkyw_byname.setText("Please enter to search ...");
        txt_searchkyw_byId.setText("");
        txt_searchkyw_byDep.setText("Please enter to search ...");
    }//GEN-LAST:event_txt_searchkyw_byIdMouseClicked

    private void txt_searchkyw_byIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchkyw_byIdKeyPressed
        // TODO add your handling code here:
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            initTableDaftarKaryawan();
            loadDaftarKaryawanbyId();
        }
    }//GEN-LAST:event_txt_searchkyw_byIdKeyPressed

    private void tabelDataDepartmentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelDataDepartmentMouseClicked
        // TODO add your handling code here:
        baris = tabelDataDepartment.getSelectedRow();
        kolom = tabelDataDepartment.getSelectedColumn();
        dataTerpilih = tabelDataDepartment.getValueAt(baris, 0).toString();
    }//GEN-LAST:event_tabelDataDepartmentMouseClicked

    private void btnSaveDepartmentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveDepartmentMouseClicked
        // TODO add your handling code here:
        Entity_Department ED = new Entity_Department();
        ED.setDep_id(txt_dep_id.getText());
        ED.setDep_cat((String) cb_dep_cat.getSelectedItem());
        ED.setDep_desc(txt_dep_desc.getText());
        ED.setDep_status((String) cb_dep_status.getSelectedItem());
        try {
            int hasil = departmentDao.saveDataDepartment(ED);
            if (hasil == 1) {
                JOptionPane.showMessageDialog(null, "Save Department Success", "Save Success", JOptionPane.INFORMATION_MESSAGE);
                init_Register_Number();
            } else {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Save Department Failed", "Save Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadDaftarDepartment();
        ClearFormAddDepartment();
    }//GEN-LAST:event_btnSaveDepartmentMouseClicked

    private void btnClearDepartmentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClearDepartmentMouseClicked
        // TODO add your handling code here:
        ClearFormAddDepartment();
    }//GEN-LAST:event_btnClearDepartmentMouseClicked

    private void btnLogoutMgmMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMgmMouseClicked
        // TODO add your handling code here:
        Object[] options = {"LogOut", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Are you sure to Logout ?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                Mode6();
                clearFormLogin();
                SetMgmStatusOffline();
                ses.shutdown();
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btnLogoutMgmMouseClicked

    private void btn_active_depMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_active_depMouseClicked
        // TODO add your handling code here:       
        Object[] options = {"Yes", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Are you sure to make this Department Active ?", "Info",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                if (tabelDataDepartment.getValueAt(baris, 3).toString().equals("Active")) {
                    JOptionPane.showMessageDialog(null, "Department Already Active", "Activation Info",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    Entity_Department ED = new Entity_Department();
                    ED.setDep_id(dataTerpilih);
                    ED.setDep_status(dataTerpilih);
                    try {
                        int hasil = departmentDao.updateStatusDepAc(ED);
                        int hasil2 = departmentDao.updateStatusLoginActive(ED);
                        if (hasil == 1 && hasil2 == 1) {
                            JOptionPane.showMessageDialog(null, "Activation Department Success", "Activation Success",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            evt.consume();
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(null, "Activation Department Failed", "Activation Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    loadDaftarDepartment();
                }
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btn_active_depMouseClicked

    private void iconMgmDaftarTugasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconMgmDaftarTugasMouseClicked
        // TODO add your handling code here:
        Mode11();
        init_Task_ID();
        last_session = 11;
    }//GEN-LAST:event_iconMgmDaftarTugasMouseClicked

    private void iconMgmDaftarTugasMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconMgmDaftarTugasMouseEntered
        // TODO add your handling code here:
        Color lightYellow = new Color(255, 200, 71);
        menuDaftarTugas.setBackground(lightYellow);
    }//GEN-LAST:event_iconMgmDaftarTugasMouseEntered

    private void iconMgmDaftarTugasMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconMgmDaftarTugasMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuDaftarTugas.setBackground(lightGray);
    }//GEN-LAST:event_iconMgmDaftarTugasMouseExited

    private void menuDaftarTugasMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuDaftarTugasMouseEntered
        // TODO add your handling code here:
        Color lightYellow = new Color(255, 200, 71);
        menuDaftarTugas.setBackground(lightYellow);
    }//GEN-LAST:event_menuDaftarTugasMouseEntered

    private void menuDaftarTugasMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuDaftarTugasMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuDaftarTugas.setBackground(lightGray);
    }//GEN-LAST:event_menuDaftarTugasMouseExited

    private void btnSaveTaskMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveTaskMouseClicked
        // TODO add your handling code here:
        Entity_Task ET = new Entity_Task();
        ET.setTask_id(txt_task_id.getText());
        ET.setTask_title(txt_task_title.getText());
        ET.setTask_desc(txt_task_desc.getText());
        ET.setTask_date(dt_task_date.getDate());
        ET.setTask_date_deadline(dt_task_deadline.getDate());
        ET.setTask_dep((String) cb_task_dep.getSelectedItem());
        ET.setTask_status((String) cb_task_status.getSelectedItem());
        try {
            int hasil = taskDao.saveTugas(ET);
            if (hasil == 1) {
                JOptionPane.showMessageDialog(null, "Save Tugas Success", "Save Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                evt.consume();
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "Save Tugas Failed", "Save Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        ClearFormTask();
        init_Task_ID();
        loadDaftarTugas();
    }//GEN-LAST:event_btnSaveTaskMouseClicked

    private void btnClearTaskMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClearTaskMouseClicked
        // TODO add your handling code here:
        ClearFormTask();
    }//GEN-LAST:event_btnClearTaskMouseClicked

    private void tabelDaftarTugasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelDaftarTugasMouseClicked
        // TODO add your handling code here:
        baris = tabelDaftarTugas.getSelectedRow();
        kolom = tabelDaftarTugas.getSelectedColumn();
        dataTerpilih = tabelDaftarTugas.getValueAt(baris, 0).toString();
    }//GEN-LAST:event_tabelDaftarTugasMouseClicked

    private void btn_upstate_taskMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_upstate_taskMouseClicked
        // TODO add your handling code here:
        Object[] options = {"Yes", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Are you sure to mark this Task as Done ?", "Info",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                if (tabelDaftarTugas.getValueAt(baris, 8).toString().equals("Done")) {
                    JOptionPane.showMessageDialog(null, "Task is Already Done", "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    Entity_Task ET = new Entity_Task();
                    ET.setTask_id(dataTerpilih);
                    try {
                        int hasil = taskDao.updateStatusTask_Done(ET);
                        if (hasil == 1) {
                            JOptionPane.showMessageDialog(null, "Update Task Success", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            evt.consume();
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(null, "Update Task Failed", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    loadDaftarTugas();
                }
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btn_upstate_taskMouseClicked

    private void btn_delete_taskMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_delete_taskMouseClicked
        // TODO add your handling code here:
        Object[] options = {"Yes", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Are you sure to delete this Task ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                Entity_Task ET = new Entity_Task();
                ET.setTask_id(dataTerpilih);
                try {
                    int hasil = taskDao.deleteTask(ET);
                    if (hasil == 1) {
                        JOptionPane.showMessageDialog(null, "Delete Task Success", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        evt.consume();
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Delete Task Failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                }
                loadDaftarTugas();
                init_Task_ID();
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btn_delete_taskMouseClicked

    private void iconMgmErrorReportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconMgmErrorReportMouseClicked
        // TODO add your handling code here:
        Mode12();
        init_Error_ID();
        last_session = 12;
    }//GEN-LAST:event_iconMgmErrorReportMouseClicked

    private void iconMgmErrorReportMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconMgmErrorReportMouseEntered
        // TODO add your handling code here:
        Color lightYellow = new Color(255, 200, 71);
        menuErrorManagement.setBackground(lightYellow);
    }//GEN-LAST:event_iconMgmErrorReportMouseEntered

    private void iconMgmErrorReportMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconMgmErrorReportMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuErrorManagement.setBackground(lightGray);
    }//GEN-LAST:event_iconMgmErrorReportMouseExited

    private void menuErrorManagementMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuErrorManagementMouseEntered
        // TODO add your handling code here:
        Color lightYellow = new Color(255, 200, 71);
        menuErrorManagement.setBackground(lightYellow);
    }//GEN-LAST:event_menuErrorManagementMouseEntered

    private void menuErrorManagementMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuErrorManagementMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuErrorManagement.setBackground(lightGray);
    }//GEN-LAST:event_menuErrorManagementMouseExited

    private void btn_upstate_task_undoneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_upstate_task_undoneMouseClicked
        // TODO add your handling code here:
        Object[] options = {"Yes", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Are you sure to mark this Task as Currently Added ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                if (tabelDaftarTugas.getValueAt(baris, 8).toString().equals("Currently Added")) {
                    JOptionPane.showMessageDialog(null, "Task is Already Currently  Added", "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    Entity_Task ET = new Entity_Task();
                    ET.setTask_id(dataTerpilih);
                    try {
                        int hasil = taskDao.updateStatusTask_UnDone(ET);
                        if (hasil == 1) {
                            JOptionPane.showMessageDialog(null, "Update Task Success", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            evt.consume();
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(null, "Update Task Failed", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    loadDaftarTugas();
                }
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btn_upstate_task_undoneMouseClicked

    private void btnSaveErrorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveErrorMouseClicked
        // TODO add your handling code here:
        String error_added_by = null;

        Entity_Signup ESU = new Entity_Signup();
        ESU.setName(txTMgmNama.getText());
        try {
            error_added_by = errorDao.getIDAdminError(ESU);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }

        Entity_ErrorReport EER = new Entity_ErrorReport();
        EER.setError_id(txt_error_id.getText());
        EER.setError_title(txt_error_title.getText());
        EER.setError_desc(txt_error_desc.getText());
        EER.setError_date(dtError_Date.getDate());
        EER.setError_status((String) cb_error_status.getSelectedItem());
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
    }//GEN-LAST:event_btnSaveErrorMouseClicked

    private void btnClearErrorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClearErrorMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnClearErrorMouseClicked

    private void tabelDaftarErrorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelDaftarErrorMouseClicked
        // TODO add your handling code here:
        baris = tabelDaftarError.getSelectedRow();
        kolom = tabelDaftarError.getSelectedColumn();
        dataTerpilih = tabelDaftarError.getValueAt(baris, 0).toString();
    }//GEN-LAST:event_tabelDaftarErrorMouseClicked

    private void btn_delete_errorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_delete_errorMouseClicked
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
    }//GEN-LAST:event_btn_delete_errorMouseClicked

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

    private void iconWarehouseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWarehouseMouseClicked
        // TODO add your handling code here:
        HomePageClientWarehouse HPC2 = new HomePageClientWarehouse();
        HPC2.setVisible(true);
        HPC2.Mode1();
        dispose();
    }//GEN-LAST:event_iconWarehouseMouseClicked

    private void iconBOBigMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconBOBigMouseClicked
        // TODO add your handling code here:
        Mode2();
    }//GEN-LAST:event_iconBOBigMouseClicked

    private void iconFOBigMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconFOBigMouseClicked
        // TODO add your handling code here:
        Mode2();
    }//GEN-LAST:event_iconFOBigMouseClicked

    private void iconSIMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconSIMMouseClicked
        // TODO add your handling code here:
        HomePageClientSIM HPC3 = new HomePageClientSIM();
        HPC3.setVisible(true);
        HPC3.Mode1();
        dispose();
    }//GEN-LAST:event_iconSIMMouseClicked

    private void iconSIMFOMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconSIMFOMouseClicked
        // TODO add your handling code here:
        HomePageClientSIM HPC3 = new HomePageClientSIM();
        HPC3.setVisible(true);
        HPC3.Mode1();
        dispose();
    }//GEN-LAST:event_iconSIMFOMouseClicked

    private void iconHome1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconHome1MouseClicked
        // TODO add your handling code here:
        Mode2();
    }//GEN-LAST:event_iconHome1MouseClicked

    private void iconHRDAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_iconHRDAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_iconHRDAncestorAdded

    private void iconHRDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconHRDMouseClicked
        // TODO add your handling code here:
        HomePageClientHRD HPCHRD = new HomePageClientHRD();
        HPCHRD.setVisible(true);
        HPCHRD.Mode1();
        dispose();
    }//GEN-LAST:event_iconHRDMouseClicked

    private void btn_printTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_printTransActionPerformed
        // TODO add your handling code here:
        if (dataTerpilihTabelKaryawan == null) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Choose Employee ID From Table", "Info", JOptionPane.WARNING_MESSAGE);
        } else {
            Object[] options = {"Yes", "Cancel"};
            int reply = JOptionPane.showOptionDialog(null, "Print CoCard ? " + dataTerpilihTabelKaryawan, "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            switch (reply) {
                case 0:
                    try {
                        JasperDesign jd = JRXmlLoader.load("src/nob/files/CoCard.jrxml");
                        JasperReport jr = JasperCompileManager.compileReport(jd);
                        HashMap parameter = new HashMap();
                        parameter.put("IdEmp", dataTerpilihTabelKaryawan);

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

    private void btn_printTransMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_printTransMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_printTransMouseClicked

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
            java.util.logging.Logger.getLogger(HomePageClientManagement.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomePageClientManagement.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomePageClientManagement.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomePageClientManagement.class
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
                new HomePageClientManagement().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bgBackOffice;
    private javax.swing.JLabel bgClientManagement;
    private javax.swing.JLabel bgClientManagementLogin;
    private javax.swing.JLabel bgDepartment;
    private javax.swing.JLabel bgFrontOffice;
    private javax.swing.JLabel bgLandingPage;
    private javax.swing.JLabel bgLogo;
    private javax.swing.JLabel bgLogoNOB;
    private javax.swing.JLabel bgManagement;
    private javax.swing.JLabel bgSubPanelCreateAcc;
    private javax.swing.JButton btnChangeState;
    private javax.swing.JLabel btnClear;
    private javax.swing.JLabel btnClearDepartment;
    private javax.swing.JLabel btnClearError;
    private javax.swing.JLabel btnClearTask;
    private javax.swing.JButton btnGetID;
    private javax.swing.JButton btnHideIP;
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel btnLogoutMgm;
    private javax.swing.JLabel btnMsgMgm;
    private javax.swing.JLabel btnNotifMgm;
    private javax.swing.JButton btnReset;
    private javax.swing.JLabel btnSave;
    private javax.swing.JLabel btnSaveDepartment;
    private javax.swing.JLabel btnSaveError;
    private javax.swing.JLabel btnSaveTask;
    private javax.swing.JButton btnSearchRegNum;
    private javax.swing.JButton btnShowIP;
    private javax.swing.JLabel btnUpdate;
    private javax.swing.JLabel btn_active_dep;
    private javax.swing.JLabel btn_deactive_dep;
    private javax.swing.JLabel btn_delete_error;
    private javax.swing.JLabel btn_delete_task;
    private javax.swing.JButton btn_printTrans;
    private javax.swing.JButton btn_trans_clearall1;
    private javax.swing.JButton btn_trans_process1;
    private javax.swing.JLabel btn_upstate_task;
    private javax.swing.JLabel btn_upstate_task_undone;
    private javax.swing.ButtonGroup buttongroup_Gender;
    private javax.swing.JComboBox<String> cbAction;
    private javax.swing.JComboBox<String> cb_dep_cat;
    private javax.swing.JComboBox<String> cb_dep_status;
    private javax.swing.JComboBox<String> cb_division;
    private javax.swing.JComboBox<String> cb_error_status;
    private javax.swing.JComboBox<String> cb_task_dep;
    private javax.swing.JComboBox<String> cb_task_status;
    private com.toedter.calendar.JDateChooser dtError_Date;
    private com.toedter.calendar.JDateChooser dt_task_date;
    private com.toedter.calendar.JDateChooser dt_task_deadline;
    private javax.swing.JLabel iconAdmin;
    private javax.swing.JLabel iconBO;
    private javax.swing.JLabel iconBOBig;
    private javax.swing.JLabel iconCashier;
    private javax.swing.JLabel iconFO;
    private javax.swing.JLabel iconFOBig;
    private javax.swing.JLabel iconHRD;
    private javax.swing.JLabel iconHome1;
    private javax.swing.JLabel iconKeuangan;
    private javax.swing.JLabel iconManagement;
    private javax.swing.JLabel iconMgmCreateAcc;
    private javax.swing.JLabel iconMgmDaftarTugas;
    private javax.swing.JLabel iconMgmDashboard;
    private javax.swing.JLabel iconMgmDataDepart;
    private javax.swing.JLabel iconMgmDataKyw;
    private javax.swing.JLabel iconMgmErrorReport;
    private javax.swing.JLabel iconSIM;
    private javax.swing.JLabel iconSIMFO;
    private javax.swing.JLabel iconWarehouse;
    private javax.swing.JInternalFrame inframeQRCode;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JLabel lblAction;
    private javax.swing.JLabel lblDashboardTitle;
    private javax.swing.JLabel lblDashboardTitle1;
    private javax.swing.JLabel lblDataKaryawanTitel;
    private javax.swing.JLabel lblDepartmentSubTitle;
    private javax.swing.JLabel lblDepartmentSubTitle1;
    private javax.swing.JLabel lblDepartmentSubTitle2;
    private javax.swing.JLabel lblDepartmentSubTitle3;
    private javax.swing.JLabel lblDepartmentSubTitle5;
    private javax.swing.JLabel lblDepartmentTitle;
    private javax.swing.JLabel lblDepartmentTitle1;
    private javax.swing.JLabel lblErrorReportSubTitle;
    private javax.swing.JLabel lblErrorReportTitle;
    private javax.swing.JLabel lblMode;
    private javax.swing.JLabel lblState;
    private javax.swing.JLabel lblStateBig;
    private javax.swing.JLabel lblTransInvoice_Confirm;
    private javax.swing.JLabel lbl_daftarkyw;
    private javax.swing.JLabel lbl_dep_cat;
    private javax.swing.JLabel lbl_dep_desc;
    private javax.swing.JLabel lbl_dep_status;
    private javax.swing.JLabel lbl_error_date;
    private javax.swing.JLabel lbl_error_desc;
    private javax.swing.JLabel lbl_error_id;
    private javax.swing.JLabel lbl_error_status;
    private javax.swing.JLabel lbl_error_title;
    private javax.swing.JLabel lbl_foto;
    private javax.swing.JLabel lbl_id_dep;
    private javax.swing.JLabel lbl_id_task;
    private javax.swing.JLabel lbl_list_dep;
    private javax.swing.JLabel lbl_list_error;
    private javax.swing.JLabel lbl_list_task;
    private javax.swing.JLabel lbl_management;
    private javax.swing.JLabel lbl_qrcode;
    private javax.swing.JLabel lbl_searchdep;
    private javax.swing.JLabel lbl_searchidkyw;
    private javax.swing.JLabel lbl_searchnama;
    private javax.swing.JLabel lbl_searchnoreg;
    private javax.swing.JLabel lbl_task_date;
    private javax.swing.JLabel lbl_task_deadline;
    private javax.swing.JLabel lbl_task_dep;
    private javax.swing.JLabel lbl_task_desc;
    private javax.swing.JLabel lbl_task_status;
    private javax.swing.JLabel lbl_task_title;
    private javax.swing.JPanel menuCreateAccount;
    private javax.swing.JPanel menuDaftarTugas;
    private javax.swing.JPanel menuDashboard;
    private javax.swing.JPanel menuDataDepartment;
    private javax.swing.JPanel menuDataKaryawan;
    private javax.swing.JPanel menuErrorManagement;
    private javax.swing.JPanel panelBackOffice;
    private javax.swing.JPanel panelDepartment;
    private javax.swing.JPanel panelFrontOffice;
    private javax.swing.JPanel panelKilledbyServer;
    private javax.swing.JPanel panelLandingPage;
    private javax.swing.JLabel panelLoginMgm;
    private javax.swing.JPanel panelMgmDashboard;
    private javax.swing.JPanel panelMgmLandingPage;
    private javax.swing.JPanel panelMgmLogin;
    private javax.swing.JPanel panelQRCode_trans;
    private javax.swing.JPanel panelfoto;
    private javax.swing.JPanel panelqrcode;
    private javax.swing.JButton qrsave;
    private javax.swing.JButton su_attachgbr;
    private javax.swing.JTextField su_fototext;
    private javax.swing.JRadioButton su_rbpria;
    private javax.swing.JRadioButton su_rbwanita;
    private javax.swing.JPanel subpanelCreateAccount;
    private javax.swing.JPanel subpanelDaftarTugas;
    private javax.swing.JPanel subpanelDashboard;
    private javax.swing.JPanel subpanelDataDepartment;
    private javax.swing.JPanel subpanelDataKaryawan;
    private javax.swing.JPanel subpanelErrorReport;
    private javax.swing.JTable tabelDaftarError;
    private javax.swing.JScrollPane tabelDaftarReportSP;
    private javax.swing.JTable tabelDaftarTugas;
    private javax.swing.JScrollPane tabelDaftarTugasSP;
    private javax.swing.JTable tabelDataDepartment;
    private javax.swing.JScrollPane tabelDataDepartmentSP;
    private javax.swing.JTable tabelDataKaryawan;
    private javax.swing.JScrollPane tabelDataKaryawanSP;
    private javax.swing.JLabel txTMgmNama;
    private javax.swing.JLabel txtActiveMode;
    private com.toedter.calendar.JDateChooser txtDob;
    private javax.swing.JTextField txtMail;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNoRegis;
    private javax.swing.JPasswordField txtPasswordMgm;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtPob;
    private javax.swing.JLabel txtServerTime;
    private javax.swing.JTextField txtUsernameMgm;
    private javax.swing.JTextArea txt_dep_desc;
    private javax.swing.JTextField txt_dep_id;
    private javax.swing.JTextArea txt_error_desc;
    private javax.swing.JTextField txt_error_id;
    private javax.swing.JTextField txt_error_title;
    private javax.swing.JLabel txt_jumlahdistributoraktif;
    private javax.swing.JLabel txt_jumlahstaff;
    private javax.swing.JLabel txt_jumlahtugas;
    private javax.swing.JPasswordField txt_password;
    private javax.swing.JTextField txt_searchkyw_byDep;
    private javax.swing.JTextField txt_searchkyw_byId;
    private javax.swing.JTextField txt_searchkyw_byname;
    private javax.swing.JTextField txt_searchkyw_bynoreg;
    private javax.swing.JLabel txt_state;
    private javax.swing.JLabel txt_stateBig;
    private javax.swing.JTextArea txt_task_desc;
    private javax.swing.JTextField txt_task_id;
    private javax.swing.JTextField txt_task_title;
    private javax.swing.JLabel txt_trans_invoiceno_confirm;
    private javax.swing.JTextField txt_username_div;
    // End of variables declaration//GEN-END:variables
}
