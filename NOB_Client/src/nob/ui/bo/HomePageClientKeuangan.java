/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nob.ui.bo;

import java.awt.Color;
import java.awt.Image;
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
import java.sql.DriverManager;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
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
import nob.dao.api.ISimDao;
import nob.dao.api.ISimulationDao;
import nob.dao.api.ITaskDao;
import nob.dao.api.ITransactionDao;
import nob.dao.api.IUserDao;
import nob.dao.api.IWarehouseDao;
import nob.model.Entity_ErrorReport;
import nob.model.Entity_SIM;
import nob.model.Entity_Signin;
import nob.model.Entity_Signup;
import nob.model.Entity_Task;
import sun.audio.*;

/**
 *
 * @author alimk
 */
public class HomePageClientKeuangan extends javax.swing.JFrame {

    String tglAwal = null;
    String tglAkhir = null;

    private IUserDao userDao = null;
    private ILoginDao loginDao = null;
    private IDistributorDao distributorDao = null;
    private IDepartmentDao departmentDao = null;
    private ITaskDao taskDao = null;
    private IReportDao errorDao = null;
    private ITransactionDao transactionDao = null;
    private ISimulationDao simulationDao = null;
    private IWarehouseDao warehouseDao = null;
    private ISimDao simDao = null;

    public String kodeSistem = "KEU";
    String hasilgetStatusServer = null;
    String hasilgetStatusKeu = null;
    final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

    int last_session = 0;
    String State_run_Keu = null;

    String ip = null;
    String modesu = null;
    String whoisonline = null;
    String dataTerpilih = null;
    String dataTerpilihTabelTugas = null;
    String dataTerpilihIzin = null;
    String dataTerpilihActivity = null;

    String idAdminSIM = null;
    String idIzin = null;
    String idActivity = null;

    int mode = 0;

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

    private List<Entity_SIM> recordCatIzin = new ArrayList<Entity_SIM>();
    private List<Entity_SIM> recordCatAct = new ArrayList<Entity_SIM>();

    private final Object[] DaftarListIzincolumNames = {"Request ID", "Request By", "Category", "Date Start", "Date End", "Description", "Created Date", "Status Approve"};
    private final DefaultTableModel tableModelIzin = new DefaultTableModel();
    private List<Entity_SIM> recordIzin = new ArrayList<Entity_SIM>();

    private final Object[] DaftarListActcolumNames = {"Request ID", "Request By", "Category", "Date Start", "Date End", "Description", "Created Date", "Status Approve"};
    private final DefaultTableModel tableModelAct = new DefaultTableModel();
    private List<Entity_SIM> recordActivity = new ArrayList<Entity_SIM>();

    /**
     * Creates new form HomePageClient_Administrator
     */
    public HomePageClientKeuangan() {
        initComponents();
        Mode9();
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
        init_SimDao();

        State_run_Keu = "Running";

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Object[] options = {"Yes", "Cancel"};
                int reply = JOptionPane.showOptionDialog(null, "Are you sure to close the system ?", "Information", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                switch (reply) {
                    case 0:
                        try {
                            if (State_run_Keu.equals("Running")) {
                                SetKeuStatusDisconnect();
                                e.getWindow().dispose();
                                System.exit(0);
                            } else if (hasilgetStatusServer.equals("Server Offline")) {
                                SetKeuStatusDisconnect();
                                e.getWindow().dispose();
                            } else {
                                int hasilSetKeuStatus = loginDao.UpdateStatusOffline("KEU");
                                if (hasilSetKeuStatus == 1) {
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

    public void SetKeuStatusOnline() {
        try {
            int hasilSetServerStatus = loginDao.UpdateStatusOnline(kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SetKeuStatusOffline() {
        try {
            int hasilSetServerStatus = loginDao.UpdateStatusOffline(kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SetKeuStatusDisconnect() {
        try {
            int hasilSetKeuStatus = loginDao.UpdateStatusDisconnect(kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getStatusKeu() {
        try {
            hasilgetStatusKeu = loginDao.CekClientStatus(kodeSistem);
            txt_stateKeu.setText(hasilgetStatusKeu);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SetKeuStatusKilledbyServer() {
        try {
            int hasilSetKeuStatus = loginDao.UpdateStatusKilledbyServer(kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getStatusServer() {
        try {
            hasilgetStatusServer = loginDao.CekClientStatus("SERVER");
            if (hasilgetStatusKeu.equals("Killed by Server")) {
                Mode13();
                SetKeuStatusKilledbyServer();
            } else if (hasilgetStatusServer.equals("Server Offline")) {
                Mode13();
                SetKeuStatusDisconnect();
            } else if (hasilgetStatusKeu.equals("Online")) {
                if (last_session == 3) {
                    Mode3();
                } else if (last_session == 4) {
                    Mode4();
                } else if (last_session == 5) {
                    Mode5();
                } else if (last_session == 6) {
                    Mode6();
                } else if (last_session == 8) {
                    Mode8();
                }
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setIPKEU() {
        Entity_Signin ES = new Entity_Signin();
        ES.setIp(ip);
        try {
            int hasilsetIPServer = loginDao.UpdateIP(ES, kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientKeuangan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void CekStatus5Sec() {
        ses.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                // TODO add your handling code here:
                getStatusKeu();
                getStatusServer();
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    public void Mode1() {
        mode = 1;
        //Mode 1 = Menampilkan Panel Daftar Department Back Office - SIM - Landing Page
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelKeuLandingPage.setVisible(true);
        panelKeuLogin.setVisible(false);
        panelKeuDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
        subpanelPrintOmset.setVisible(false);
        subpanelPrintPendapatan.setVisible(false);
        subpanelPrintPengeluaran.setVisible(false);
    }

    public void Mode2() {
        mode = 2;
        //Mode 2 = Menampilkan Panel Daftar Department Back Office - SIM - Login
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelKeuLandingPage.setVisible(false);
        panelKeuLogin.setVisible(true);
        panelKeuDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
        subpanelPrintOmset.setVisible(false);
        subpanelPrintPendapatan.setVisible(false);
        subpanelPrintPengeluaran.setVisible(false);
    }

    public void Mode3() {
        mode = 3;
        //Mode 3 = Menampilkan Panel Daftar Department Back Office - SIM - Dashboard
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelKeuLandingPage.setVisible(false);
        panelKeuLogin.setVisible(false);
        panelKeuDashboard.setVisible(true);
        subpanelKeuDashboard.setVisible(true);
        subpanelKeuErrorReport.setVisible(false);
        panelKilledbyServer.setVisible(false);
        subpanelPrintOmset.setVisible(false);
        subpanelPrintPendapatan.setVisible(false);
        subpanelPrintPengeluaran.setVisible(false);
    }

    public void Mode4() {
        mode = 4;
        //Mode 4 = Menampilkan Panel Pendapatan
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelKeuLandingPage.setVisible(false);
        panelKeuLogin.setVisible(false);
        panelKeuDashboard.setVisible(true);
        subpanelKeuDashboard.setVisible(false);
        subpanelKeuErrorReport.setVisible(false);
        panelKilledbyServer.setVisible(false);
        subpanelPrintOmset.setVisible(false);
        subpanelPrintPendapatan.setVisible(true);
        subpanelPrintPengeluaran.setVisible(false);
    }

    public void Mode5() {
        mode = 5;
        //Mode 5 = Menampilkan Panel Pengeluaran
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelKeuLandingPage.setVisible(false);
        panelKeuLogin.setVisible(false);
        panelKeuDashboard.setVisible(true);
        subpanelKeuDashboard.setVisible(false);
        subpanelKeuErrorReport.setVisible(false);
        panelKilledbyServer.setVisible(false);
        subpanelPrintOmset.setVisible(false);
        subpanelPrintPendapatan.setVisible(false);
        subpanelPrintPengeluaran.setVisible(true);
    }

    public void Mode6() {
        mode = 6;
        //Mode 6 = Menampilkan Panel Omset
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelKeuLandingPage.setVisible(false);
        panelKeuLogin.setVisible(false);
        panelKeuDashboard.setVisible(true);
        subpanelKeuDashboard.setVisible(false);
        subpanelKeuErrorReport.setVisible(false);
        panelKilledbyServer.setVisible(false);
        subpanelPrintOmset.setVisible(true);
        subpanelPrintPendapatan.setVisible(false);
        subpanelPrintPengeluaran.setVisible(false);
    }

    public void Mode8() {
        mode = 8;
        //Mode 8 = Menampilkan Panel Daftar Department Back Office - SIM - Error Report
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelKeuLandingPage.setVisible(false);
        panelKeuLogin.setVisible(false);
        panelKeuDashboard.setVisible(true);
        subpanelKeuDashboard.setVisible(false);
        subpanelKeuErrorReport.setVisible(true);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode9() {
        //sebagai mode awal
        mode = 9;
        panelLandingPage.setVisible(true);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelKeuLandingPage.setVisible(false);
        panelKeuLogin.setVisible(false);
        panelKeuDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode10() {
        mode = 10;
        //Mode 10= Menampilkan Panel Daftar Department
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(true);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelKeuLandingPage.setVisible(false);
        panelKeuLogin.setVisible(false);
        panelKeuDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode11() {
        mode = 11;
        //Mode 11 = Menampilkan Panel Daftar Department Back Office
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(true);
        panelFrontOffice.setVisible(false);
        panelKeuLandingPage.setVisible(false);
        panelKeuLogin.setVisible(false);
        panelKeuDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode12() {
        mode = 12;
        //Mode 12 = Menampilkan Panel Daftar Department Front Office
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(true);
        panelKeuLandingPage.setVisible(false);
        panelKeuLogin.setVisible(false);
        panelKeuDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode13() {
        mode = 13;
        //Mode 13 = Menampilkan Panel Killed by Server
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelKeuLandingPage.setVisible(false);
        panelKeuLogin.setVisible(false);
        panelKeuDashboard.setVisible(false);
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
                    txtServerTimeKeu.setText("Date  :  " + day + "/" + (month + 1) + "/" + year + "   Time : " + hour + ":" + (minute) + ":" + second);

                    try {
                        sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(HomePageClientKeuangan.class.getName()).log(Level.SEVERE, null, ex);
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
        btnShowIPKeu.setVisible(false);
        btnHideIPKeu.setVisible(true);
    }

    public void ShowIPModeWrh() {
        btnShowIPKeu.setVisible(true);
        btnHideIPKeu.setVisible(false);
    }

    private void init_User_Dao() {
        String url = "rmi://" + configuration.ip_server + ":1099/userDao";
        try {
            //ambil referensi dari remote objek yang ada di server
            userDao = (IUserDao) Naming.lookup(url);
        } catch (NotBoundException | MalformedURLException ex) {
            Logger.getLogger(HomePageClientKeuangan.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientKeuangan.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientKeuangan.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientKeuangan.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientKeuangan.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientKeuangan.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientKeuangan.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientKeuangan.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientKeuangan.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientKeuangan.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientKeuangan.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientKeuangan.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientKeuangan.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientKeuangan.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientKeuangan.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientKeuangan.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientKeuangan.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            //Logger.getLogger(FrmSupplier.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Koneksi ke Service" + url + "[GAGAL], cek kembali app server", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void init_SimDao() {
        String url = "rmi://" + configuration.ip_server + ":1099/simDao";
        try {
            //ambil referensi dari remote objek yang ada di server
            simDao = (ISimDao) Naming.lookup(url);
        } catch (NotBoundException ex) {
            Logger.getLogger(HomePageClientKeuangan.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientKeuangan.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            //Logger.getLogger(FrmSupplier.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Koneksi ke Service" + url + "[GAGAL], cek kembali app server", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void sfx_notifikasi() throws FileNotFoundException, IOException {

        // open the sound file as a Java input stream
//        String gongFile = "C:\\Users\\alimk\\Desktop\\Skripsi\\NOB Technology\\Sound FX\\sfx_transaction_pending.wav";
        String gongFile = "src\\nob\\soundfx\\sfx_new_notif.wav";
        InputStream in = new FileInputStream(gongFile);

        // create an audiostream from the inputstream
        AudioStream audioStream = new AudioStream(in);

        // play the audio clip with the audioplayer class
        AudioPlayer.player.start(audioStream);

    }

    private void init_Error_ID() {
        Entity_ErrorReport EER = new Entity_ErrorReport();
        try {
            String hasil = errorDao.GenerateErrorID(EER);
            if (hasil == null) {
                //do something
            } else {
                txt_error_id_keu.setText(hasil);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ClearFormError() {
        txt_error_title_keu.setText("");
        txt_error_desc_keu.setText("");
        dtError_Date_keu.setDate(null);
        txt_error_title_keu.requestFocus();
        cb_error_status_keu.setSelectedIndex(0);
    }

    public void initTabelError() {
        //set header table
        tableModelError.setColumnIdentifiers(DaftarErrorcolumNames);
        tabelDaftarError_Keu.setModel(tableModelError);
        tabelDaftarError_Keu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelDaftarError_Keu.setAutoResizeMode(tabelDaftarError_Keu.AUTO_RESIZE_OFF);
        customTabelListError();
    }

    public void customTabelListError() {
        tabelDaftarError_Keu.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabelDaftarError_Keu.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabelDaftarError_Keu.getColumnModel().getColumn(2).setPreferredWidth(300);
        tabelDaftarError_Keu.getColumnModel().getColumn(3).setPreferredWidth(150);
        tabelDaftarError_Keu.getColumnModel().getColumn(4).setPreferredWidth(150);
        tabelDaftarError_Keu.getColumnModel().getColumn(5).setPreferredWidth(150);
    }

    private void loadDaftarError() {
        tabelDaftarError_Keu.setAutoResizeMode(tabelDaftarError_Keu.AUTO_RESIZE_OFF);
        customTabelListError();
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
        txtUsernameKeu.setText("");
        txtPasswordKeu.setText("");
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
        panelKeuDashboard = new javax.swing.JPanel();
        btnNotifKeu = new javax.swing.JLabel();
        btnMsgKeu = new javax.swing.JLabel();
        btnLogoutKeu = new javax.swing.JLabel();
        subpanelPrintOmset = new javax.swing.JPanel();
        lblDashboardTitle6 = new javax.swing.JLabel();
        lblDashboardTitleKEU4 = new javax.swing.JLabel();
        jSeparator23 = new javax.swing.JSeparator();
        lbl_pilihanpencarian12 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        dtTanggalAkhirPendapatan2 = new com.toedter.calendar.JDateChooser();
        dtTanggalAwalPendapatan2 = new com.toedter.calendar.JDateChooser();
        btn_CetakPendapatanHarian2 = new javax.swing.JButton();
        btn_CetakPendapatanAll2 = new javax.swing.JButton();
        lbl_pilihanpencarian13 = new javax.swing.JLabel();
        lbl_pilihanpencarian14 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        cbPendapatanBulan2 = new javax.swing.JComboBox<>();
        cbPendapatanTahun2 = new javax.swing.JComboBox<>();
        btn_CetakPendapatanBulanan2 = new javax.swing.JButton();
        lbl_pilihanpencarian15 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        cbPendapatanTahunan2 = new javax.swing.JComboBox<>();
        btn_CetakPendapatanTahunan2 = new javax.swing.JButton();
        subpanelPrintPengeluaran = new javax.swing.JPanel();
        lblDashboardTitle5 = new javax.swing.JLabel();
        lblDashboardTitleKEU3 = new javax.swing.JLabel();
        jSeparator22 = new javax.swing.JSeparator();
        lbl_pilihanpencarian8 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        dtTanggalAkhirPendapatan1 = new com.toedter.calendar.JDateChooser();
        dtTanggalAwalPendapatan1 = new com.toedter.calendar.JDateChooser();
        btn_CetakPendapatanHarian1 = new javax.swing.JButton();
        btn_CetakPendapatanAll1 = new javax.swing.JButton();
        lbl_pilihanpencarian9 = new javax.swing.JLabel();
        lbl_pilihanpencarian10 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        cbPendapatanBulan1 = new javax.swing.JComboBox<>();
        cbPendapatanTahun1 = new javax.swing.JComboBox<>();
        btn_CetakPendapatanBulanan1 = new javax.swing.JButton();
        lbl_pilihanpencarian11 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        cbPendapatanTahunan1 = new javax.swing.JComboBox<>();
        btn_CetakPendapatanTahunan1 = new javax.swing.JButton();
        subpanelPrintPendapatan = new javax.swing.JPanel();
        lblDashboardTitle3 = new javax.swing.JLabel();
        lblDashboardTitleKEU1 = new javax.swing.JLabel();
        jSeparator20 = new javax.swing.JSeparator();
        lbl_pilihanpencarian4 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        dtTanggalAkhirPendapatan = new com.toedter.calendar.JDateChooser();
        dtTanggalAwalPendapatan = new com.toedter.calendar.JDateChooser();
        btn_CetakPendapatanHarian = new javax.swing.JButton();
        btn_CetakPendapatanAll = new javax.swing.JButton();
        lbl_pilihanpencarian5 = new javax.swing.JLabel();
        lbl_pilihanpencarian6 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        cbPendapatanBulan = new javax.swing.JComboBox<>();
        cbPendapatanTahun = new javax.swing.JComboBox<>();
        btn_CetakPendapatanBulanan = new javax.swing.JButton();
        lbl_pilihanpencarian7 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        cbPendapatanTahunan = new javax.swing.JComboBox<>();
        btn_CetakPendapatanTahunan = new javax.swing.JButton();
        subpanelKeuDashboard = new javax.swing.JPanel();
        lblDashboardTitle2 = new javax.swing.JLabel();
        lblDashboardTitleKEU = new javax.swing.JLabel();
        jSeparator19 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        subpanelKeuErrorReport = new javax.swing.JPanel();
        btnSaveError_keu = new javax.swing.JLabel();
        btnClearError_keu = new javax.swing.JLabel();
        lbl_list_error_keu = new javax.swing.JLabel();
        cb_error_status_keu = new javax.swing.JComboBox<>();
        lbl_error_date_keu = new javax.swing.JLabel();
        dtError_Date_keu = new com.toedter.calendar.JDateChooser();
        lbl_error_status_keu = new javax.swing.JLabel();
        txt_error_desc_keu = new javax.swing.JTextArea();
        lbl_error_desc_keu = new javax.swing.JLabel();
        txt_error_title_keu = new javax.swing.JTextField();
        lbl_error_title_keu = new javax.swing.JLabel();
        txt_error_id_keu = new javax.swing.JTextField();
        lbl_error_id_keu = new javax.swing.JLabel();
        lblErrorReportTitleKeu = new javax.swing.JLabel();
        lblErrorReportKeuSubTitle1 = new javax.swing.JLabel();
        jSeparator14 = new javax.swing.JSeparator();
        tabelDaftarReportSP = new javax.swing.JScrollPane();
        tabelDaftarError_Keu = new javax.swing.JTable();
        lblErrorReportKeuSubTitle = new javax.swing.JLabel();
        btn_delete_error_keu = new javax.swing.JLabel();
        jSeparator104 = new javax.swing.JSeparator();
        menuKeuErrorReport = new javax.swing.JPanel();
        iconKeuErrorReport = new javax.swing.JLabel();
        jSeparator101 = new javax.swing.JSeparator();
        menuKeuDashboard = new javax.swing.JPanel();
        iconKeuDashboard = new javax.swing.JLabel();
        jSeparator102 = new javax.swing.JSeparator();
        btnChangeStateKeu = new javax.swing.JButton();
        btnHideIPKeu = new javax.swing.JButton();
        btnShowIPKeu = new javax.swing.JButton();
        txtServerTimeKeu = new javax.swing.JLabel();
        txt_stateBigKeu = new javax.swing.JLabel();
        lblStateBigKeu = new javax.swing.JLabel();
        lblStateKeu = new javax.swing.JLabel();
        txt_stateKeu = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        txtKeuNama = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        iconAdminKeu = new javax.swing.JLabel();
        bgKeu = new javax.swing.JLabel();
        panelKilledbyServer = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        panelKeuLogin = new javax.swing.JPanel();
        txtUsernameKeu = new javax.swing.JTextField();
        txtPasswordKeu = new javax.swing.JPasswordField();
        btnLoginKeu = new javax.swing.JButton();
        btnResetKeu = new javax.swing.JButton();
        panelLoginHrd = new javax.swing.JLabel();
        bgClientHRD2 = new javax.swing.JLabel();
        panelKeuLandingPage = new javax.swing.JPanel();
        iconHomeHRD = new javax.swing.JLabel();
        bgHRDLogo = new javax.swing.JLabel();
        bgClientHRD = new javax.swing.JLabel();
        panelBackOffice = new javax.swing.JPanel();
        iconHRD = new javax.swing.JLabel();
        iconSIM = new javax.swing.JLabel();
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
        setTitle("NOB Tech - Client - HRD");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelKeuDashboard.setAutoscrolls(true);
        panelKeuDashboard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnNotifKeu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconNotif.png"))); // NOI18N
        btnNotifKeu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelKeuDashboard.add(btnNotifKeu, new org.netbeans.lib.awtextra.AbsoluteConstraints(1039, 97, -1, -1));

        btnMsgKeu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconMassage.png"))); // NOI18N
        btnMsgKeu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelKeuDashboard.add(btnMsgKeu, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 97, -1, -1));

        btnLogoutKeu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/iconLogoutSIM.png"))); // NOI18N
        btnLogoutKeu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogoutKeu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogoutKeuMouseClicked(evt);
            }
        });
        panelKeuDashboard.add(btnLogoutKeu, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 97, -1, -1));

        subpanelPrintOmset.setBackground(new java.awt.Color(255, 255, 255));
        subpanelPrintOmset.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblDashboardTitle6.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblDashboardTitle6.setForeground(java.awt.Color.gray);
        lblDashboardTitle6.setText("KEUANGAN");
        subpanelPrintOmset.add(lblDashboardTitle6, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 53, -1, -1));

        lblDashboardTitleKEU4.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblDashboardTitleKEU4.setForeground(java.awt.Color.gray);
        lblDashboardTitleKEU4.setText("PENDAPATAN");
        subpanelPrintOmset.add(lblDashboardTitleKEU4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        jSeparator23.setForeground(new java.awt.Color(241, 241, 241));
        subpanelPrintOmset.add(jSeparator23, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 930, 20));

        lbl_pilihanpencarian12.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_pilihanpencarian12.setForeground(java.awt.Color.gray);
        lbl_pilihanpencarian12.setText("Laporan Pendapatan Harian");
        subpanelPrintOmset.add(lbl_pilihanpencarian12, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, -1, 20));

        jPanel24.setBackground(new java.awt.Color(241, 241, 241));
        jPanel24.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel24.add(dtTanggalAkhirPendapatan2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 15, 160, -1));
        jPanel24.add(dtTanggalAwalPendapatan2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 15, 150, -1));

        subpanelPrintOmset.add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 370, 50));

        btn_CetakPendapatanHarian2.setBackground(new java.awt.Color(153, 0, 0));
        btn_CetakPendapatanHarian2.setForeground(new java.awt.Color(255, 255, 255));
        btn_CetakPendapatanHarian2.setText("CETAK");
        btn_CetakPendapatanHarian2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CetakPendapatanHarian2ActionPerformed(evt);
            }
        });
        subpanelPrintOmset.add(btn_CetakPendapatanHarian2, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 240, -1, 50));

        btn_CetakPendapatanAll2.setBackground(new java.awt.Color(153, 0, 0));
        btn_CetakPendapatanAll2.setForeground(new java.awt.Color(255, 255, 255));
        btn_CetakPendapatanAll2.setText("CETAK");
        btn_CetakPendapatanAll2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CetakPendapatanAll2ActionPerformed(evt);
            }
        });
        subpanelPrintOmset.add(btn_CetakPendapatanAll2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 180, 50));

        lbl_pilihanpencarian13.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_pilihanpencarian13.setForeground(java.awt.Color.gray);
        lbl_pilihanpencarian13.setText("Laporan Pendapatan");
        subpanelPrintOmset.add(lbl_pilihanpencarian13, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, 20));

        lbl_pilihanpencarian14.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_pilihanpencarian14.setForeground(java.awt.Color.gray);
        lbl_pilihanpencarian14.setText("Laporan Pendapatan Bulanan");
        subpanelPrintOmset.add(lbl_pilihanpencarian14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, -1, 20));

        jPanel25.setBackground(new java.awt.Color(241, 241, 241));
        jPanel25.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cbPendapatanBulan2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Pilih Bulan -", "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember" }));
        jPanel25.add(cbPendapatanBulan2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 15, 160, -1));

        cbPendapatanTahun2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Pilih Tahun -", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030" }));
        jPanel25.add(cbPendapatanTahun2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 15, 160, -1));

        subpanelPrintOmset.add(jPanel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, 370, 50));

        btn_CetakPendapatanBulanan2.setBackground(new java.awt.Color(153, 0, 0));
        btn_CetakPendapatanBulanan2.setForeground(new java.awt.Color(255, 255, 255));
        btn_CetakPendapatanBulanan2.setText("CETAK");
        btn_CetakPendapatanBulanan2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CetakPendapatanBulanan2ActionPerformed(evt);
            }
        });
        subpanelPrintOmset.add(btn_CetakPendapatanBulanan2, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 380, -1, 50));

        lbl_pilihanpencarian15.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_pilihanpencarian15.setForeground(java.awt.Color.gray);
        lbl_pilihanpencarian15.setText("Laporan Pendapatan Tahunan");
        subpanelPrintOmset.add(lbl_pilihanpencarian15, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 210, -1, 20));

        jPanel26.setBackground(new java.awt.Color(241, 241, 241));
        jPanel26.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cbPendapatanTahunan2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Pilih Tahun -", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030" }));
        jPanel26.add(cbPendapatanTahunan2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 15, 160, -1));

        subpanelPrintOmset.add(jPanel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 240, 200, 50));

        btn_CetakPendapatanTahunan2.setBackground(new java.awt.Color(153, 0, 0));
        btn_CetakPendapatanTahunan2.setForeground(new java.awt.Color(255, 255, 255));
        btn_CetakPendapatanTahunan2.setText("CETAK");
        btn_CetakPendapatanTahunan2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CetakPendapatanTahunan2ActionPerformed(evt);
            }
        });
        subpanelPrintOmset.add(btn_CetakPendapatanTahunan2, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 240, -1, 50));

        panelKeuDashboard.add(subpanelPrintOmset, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        subpanelPrintPengeluaran.setBackground(new java.awt.Color(255, 255, 255));
        subpanelPrintPengeluaran.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblDashboardTitle5.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblDashboardTitle5.setForeground(java.awt.Color.gray);
        lblDashboardTitle5.setText("KEUANGAN");
        subpanelPrintPengeluaran.add(lblDashboardTitle5, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 53, -1, -1));

        lblDashboardTitleKEU3.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblDashboardTitleKEU3.setForeground(java.awt.Color.gray);
        lblDashboardTitleKEU3.setText("PENDAPATAN");
        subpanelPrintPengeluaran.add(lblDashboardTitleKEU3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        jSeparator22.setForeground(new java.awt.Color(241, 241, 241));
        subpanelPrintPengeluaran.add(jSeparator22, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 930, 20));

        lbl_pilihanpencarian8.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_pilihanpencarian8.setForeground(java.awt.Color.gray);
        lbl_pilihanpencarian8.setText("Laporan Pendapatan Harian");
        subpanelPrintPengeluaran.add(lbl_pilihanpencarian8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, -1, 20));

        jPanel21.setBackground(new java.awt.Color(241, 241, 241));
        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel21.add(dtTanggalAkhirPendapatan1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 15, 160, -1));
        jPanel21.add(dtTanggalAwalPendapatan1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 15, 150, -1));

        subpanelPrintPengeluaran.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 370, 50));

        btn_CetakPendapatanHarian1.setBackground(new java.awt.Color(153, 0, 0));
        btn_CetakPendapatanHarian1.setForeground(new java.awt.Color(255, 255, 255));
        btn_CetakPendapatanHarian1.setText("CETAK");
        btn_CetakPendapatanHarian1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CetakPendapatanHarian1ActionPerformed(evt);
            }
        });
        subpanelPrintPengeluaran.add(btn_CetakPendapatanHarian1, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 240, -1, 50));

        btn_CetakPendapatanAll1.setBackground(new java.awt.Color(153, 0, 0));
        btn_CetakPendapatanAll1.setForeground(new java.awt.Color(255, 255, 255));
        btn_CetakPendapatanAll1.setText("CETAK");
        btn_CetakPendapatanAll1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CetakPendapatanAll1ActionPerformed(evt);
            }
        });
        subpanelPrintPengeluaran.add(btn_CetakPendapatanAll1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 180, 50));

        lbl_pilihanpencarian9.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_pilihanpencarian9.setForeground(java.awt.Color.gray);
        lbl_pilihanpencarian9.setText("Laporan Pendapatan");
        subpanelPrintPengeluaran.add(lbl_pilihanpencarian9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, 20));

        lbl_pilihanpencarian10.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_pilihanpencarian10.setForeground(java.awt.Color.gray);
        lbl_pilihanpencarian10.setText("Laporan Pendapatan Bulanan");
        subpanelPrintPengeluaran.add(lbl_pilihanpencarian10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, -1, 20));

        jPanel22.setBackground(new java.awt.Color(241, 241, 241));
        jPanel22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cbPendapatanBulan1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Pilih Bulan -", "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember" }));
        jPanel22.add(cbPendapatanBulan1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 15, 160, -1));

        cbPendapatanTahun1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Pilih Tahun -", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030" }));
        jPanel22.add(cbPendapatanTahun1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 15, 160, -1));

        subpanelPrintPengeluaran.add(jPanel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, 370, 50));

        btn_CetakPendapatanBulanan1.setBackground(new java.awt.Color(153, 0, 0));
        btn_CetakPendapatanBulanan1.setForeground(new java.awt.Color(255, 255, 255));
        btn_CetakPendapatanBulanan1.setText("CETAK");
        btn_CetakPendapatanBulanan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CetakPendapatanBulanan1ActionPerformed(evt);
            }
        });
        subpanelPrintPengeluaran.add(btn_CetakPendapatanBulanan1, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 380, -1, 50));

        lbl_pilihanpencarian11.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_pilihanpencarian11.setForeground(java.awt.Color.gray);
        lbl_pilihanpencarian11.setText("Laporan Pendapatan Tahunan");
        subpanelPrintPengeluaran.add(lbl_pilihanpencarian11, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 210, -1, 20));

        jPanel23.setBackground(new java.awt.Color(241, 241, 241));
        jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cbPendapatanTahunan1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Pilih Tahun -", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030" }));
        jPanel23.add(cbPendapatanTahunan1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 15, 160, -1));

        subpanelPrintPengeluaran.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 240, 200, 50));

        btn_CetakPendapatanTahunan1.setBackground(new java.awt.Color(153, 0, 0));
        btn_CetakPendapatanTahunan1.setForeground(new java.awt.Color(255, 255, 255));
        btn_CetakPendapatanTahunan1.setText("CETAK");
        btn_CetakPendapatanTahunan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CetakPendapatanTahunan1ActionPerformed(evt);
            }
        });
        subpanelPrintPengeluaran.add(btn_CetakPendapatanTahunan1, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 240, -1, 50));

        panelKeuDashboard.add(subpanelPrintPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        subpanelPrintPendapatan.setBackground(new java.awt.Color(255, 255, 255));
        subpanelPrintPendapatan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblDashboardTitle3.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblDashboardTitle3.setForeground(java.awt.Color.gray);
        lblDashboardTitle3.setText("KEUANGAN");
        subpanelPrintPendapatan.add(lblDashboardTitle3, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 53, -1, -1));

        lblDashboardTitleKEU1.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblDashboardTitleKEU1.setForeground(java.awt.Color.gray);
        lblDashboardTitleKEU1.setText("PENDAPATAN");
        subpanelPrintPendapatan.add(lblDashboardTitleKEU1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        jSeparator20.setForeground(new java.awt.Color(241, 241, 241));
        subpanelPrintPendapatan.add(jSeparator20, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 930, 20));

        lbl_pilihanpencarian4.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_pilihanpencarian4.setForeground(java.awt.Color.gray);
        lbl_pilihanpencarian4.setText("Laporan Pendapatan Harian");
        subpanelPrintPendapatan.add(lbl_pilihanpencarian4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, -1, 20));

        jPanel18.setBackground(new java.awt.Color(241, 241, 241));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel18.add(dtTanggalAkhirPendapatan, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 15, 160, -1));
        jPanel18.add(dtTanggalAwalPendapatan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 15, 150, -1));

        subpanelPrintPendapatan.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 370, 50));

        btn_CetakPendapatanHarian.setBackground(new java.awt.Color(153, 0, 0));
        btn_CetakPendapatanHarian.setForeground(new java.awt.Color(255, 255, 255));
        btn_CetakPendapatanHarian.setText("CETAK");
        btn_CetakPendapatanHarian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CetakPendapatanHarianActionPerformed(evt);
            }
        });
        subpanelPrintPendapatan.add(btn_CetakPendapatanHarian, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 240, -1, 50));

        btn_CetakPendapatanAll.setBackground(new java.awt.Color(153, 0, 0));
        btn_CetakPendapatanAll.setForeground(new java.awt.Color(255, 255, 255));
        btn_CetakPendapatanAll.setText("CETAK");
        btn_CetakPendapatanAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CetakPendapatanAllActionPerformed(evt);
            }
        });
        subpanelPrintPendapatan.add(btn_CetakPendapatanAll, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 180, 50));

        lbl_pilihanpencarian5.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_pilihanpencarian5.setForeground(java.awt.Color.gray);
        lbl_pilihanpencarian5.setText("Laporan Pendapatan");
        subpanelPrintPendapatan.add(lbl_pilihanpencarian5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, 20));

        lbl_pilihanpencarian6.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_pilihanpencarian6.setForeground(java.awt.Color.gray);
        lbl_pilihanpencarian6.setText("Laporan Pendapatan Bulanan");
        subpanelPrintPendapatan.add(lbl_pilihanpencarian6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, -1, 20));

        jPanel19.setBackground(new java.awt.Color(241, 241, 241));
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cbPendapatanBulan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Pilih Bulan -", "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember" }));
        jPanel19.add(cbPendapatanBulan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 15, 160, -1));

        cbPendapatanTahun.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Pilih Tahun -", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030" }));
        jPanel19.add(cbPendapatanTahun, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 15, 160, -1));

        subpanelPrintPendapatan.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, 370, 50));

        btn_CetakPendapatanBulanan.setBackground(new java.awt.Color(153, 0, 0));
        btn_CetakPendapatanBulanan.setForeground(new java.awt.Color(255, 255, 255));
        btn_CetakPendapatanBulanan.setText("CETAK");
        btn_CetakPendapatanBulanan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CetakPendapatanBulananActionPerformed(evt);
            }
        });
        subpanelPrintPendapatan.add(btn_CetakPendapatanBulanan, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 380, -1, 50));

        lbl_pilihanpencarian7.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_pilihanpencarian7.setForeground(java.awt.Color.gray);
        lbl_pilihanpencarian7.setText("Laporan Pendapatan Tahunan");
        subpanelPrintPendapatan.add(lbl_pilihanpencarian7, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 210, -1, 20));

        jPanel20.setBackground(new java.awt.Color(241, 241, 241));
        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cbPendapatanTahunan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Pilih Tahun -", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030" }));
        jPanel20.add(cbPendapatanTahunan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 15, 160, -1));

        subpanelPrintPendapatan.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 240, 200, 50));

        btn_CetakPendapatanTahunan.setBackground(new java.awt.Color(153, 0, 0));
        btn_CetakPendapatanTahunan.setForeground(new java.awt.Color(255, 255, 255));
        btn_CetakPendapatanTahunan.setText("CETAK");
        btn_CetakPendapatanTahunan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CetakPendapatanTahunanActionPerformed(evt);
            }
        });
        subpanelPrintPendapatan.add(btn_CetakPendapatanTahunan, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 240, -1, 50));

        panelKeuDashboard.add(subpanelPrintPendapatan, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        subpanelKeuDashboard.setBackground(new java.awt.Color(255, 255, 255));
        subpanelKeuDashboard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblDashboardTitle2.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblDashboardTitle2.setForeground(java.awt.Color.gray);
        lblDashboardTitle2.setText("KEUANGAN");
        subpanelKeuDashboard.add(lblDashboardTitle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 53, -1, -1));

        lblDashboardTitleKEU.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblDashboardTitleKEU.setForeground(java.awt.Color.gray);
        lblDashboardTitleKEU.setText("DASHBOARD");
        subpanelKeuDashboard.add(lblDashboardTitleKEU, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        jSeparator19.setForeground(new java.awt.Color(241, 241, 241));
        subpanelKeuDashboard.add(jSeparator19, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 930, 20));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/keuangan/btnOmset.png"))); // NOI18N
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });
        subpanelKeuDashboard.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, -1, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/keuangan/btnPengeluaran.png"))); // NOI18N
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });
        subpanelKeuDashboard.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 200, -1, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/keuangan/btnPendapatan.png"))); // NOI18N
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });
        subpanelKeuDashboard.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 200, -1, -1));

        panelKeuDashboard.add(subpanelKeuDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        subpanelKeuErrorReport.setBackground(new java.awt.Color(255, 255, 255));
        subpanelKeuErrorReport.setPreferredSize(new java.awt.Dimension(1010, 538));
        subpanelKeuErrorReport.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSaveError_keu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconSave.png"))); // NOI18N
        btnSaveError_keu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSaveError_keu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveError_keuMouseClicked(evt);
            }
        });
        subpanelKeuErrorReport.add(btnSaveError_keu, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 410, -1, -1));

        btnClearError_keu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconClear.png"))); // NOI18N
        btnClearError_keu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClearError_keu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClearError_keuMouseClicked(evt);
            }
        });
        subpanelKeuErrorReport.add(btnClearError_keu, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 410, -1, -1));

        lbl_list_error_keu.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_list_error_keu.setForeground(java.awt.Color.gray);
        lbl_list_error_keu.setText("Report List");
        subpanelKeuErrorReport.add(lbl_list_error_keu, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 100, -1, -1));

        cb_error_status_keu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Status -", "Currently Added", "On Progress", "Done" }));
        subpanelKeuErrorReport.add(cb_error_status_keu, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 410, 180, -1));

        lbl_error_date_keu.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_date_keu.setText("Date");
        subpanelKeuErrorReport.add(lbl_error_date_keu, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, -1, -1));
        subpanelKeuErrorReport.add(dtError_Date_keu, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 360, 180, -1));

        lbl_error_status_keu.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_status_keu.setText("Status");
        subpanelKeuErrorReport.add(lbl_error_status_keu, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 410, -1, -1));

        txt_error_desc_keu.setBackground(new java.awt.Color(240, 240, 240));
        txt_error_desc_keu.setColumns(20);
        txt_error_desc_keu.setRows(5);
        subpanelKeuErrorReport.add(txt_error_desc_keu, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 240, 180, -1));

        lbl_error_desc_keu.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_desc_keu.setText("Description");
        subpanelKeuErrorReport.add(lbl_error_desc_keu, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, -1, -1));
        subpanelKeuErrorReport.add(txt_error_title_keu, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 188, 180, -1));

        lbl_error_title_keu.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_title_keu.setText("Title");
        subpanelKeuErrorReport.add(lbl_error_title_keu, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, -1));

        txt_error_id_keu.setEnabled(false);
        subpanelKeuErrorReport.add(txt_error_id_keu, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 180, -1));

        lbl_error_id_keu.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_id_keu.setText("Report ID");
        subpanelKeuErrorReport.add(lbl_error_id_keu, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));

        lblErrorReportTitleKeu.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblErrorReportTitleKeu.setForeground(java.awt.Color.gray);
        lblErrorReportTitleKeu.setText("ERROR REPORT");
        subpanelKeuErrorReport.add(lblErrorReportTitleKeu, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        lblErrorReportKeuSubTitle1.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblErrorReportKeuSubTitle1.setForeground(java.awt.Color.gray);
        lblErrorReportKeuSubTitle1.setText("Add Report");
        subpanelKeuErrorReport.add(lblErrorReportKeuSubTitle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        jSeparator14.setForeground(new java.awt.Color(241, 241, 241));
        subpanelKeuErrorReport.add(jSeparator14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 950, 20));

        tabelDaftarReportSP.setBackground(new java.awt.Color(241, 241, 241));
        tabelDaftarReportSP.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        tabelDaftarReportSP.setAutoscrolls(true);
        tabelDaftarReportSP.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tabelDaftarError_Keu.setBackground(new java.awt.Color(240, 240, 240));
        tabelDaftarError_Keu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Error ID", "Title", "Description", "Date", "PIC", "Status"
            }
        ));
        tabelDaftarError_Keu.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabelDaftarError_Keu.setAutoscrolls(false);
        tabelDaftarError_Keu.setGridColor(new java.awt.Color(241, 241, 241));
        tabelDaftarError_Keu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelDaftarError_KeuMouseClicked(evt);
            }
        });
        tabelDaftarReportSP.setViewportView(tabelDaftarError_Keu);
        if (tabelDaftarError_Keu.getColumnModel().getColumnCount() > 0) {
            tabelDaftarError_Keu.getColumnModel().getColumn(0).setPreferredWidth(150);
            tabelDaftarError_Keu.getColumnModel().getColumn(1).setPreferredWidth(150);
            tabelDaftarError_Keu.getColumnModel().getColumn(2).setPreferredWidth(300);
            tabelDaftarError_Keu.getColumnModel().getColumn(3).setPreferredWidth(150);
            tabelDaftarError_Keu.getColumnModel().getColumn(4).setPreferredWidth(150);
            tabelDaftarError_Keu.getColumnModel().getColumn(5).setPreferredWidth(150);
        }

        subpanelKeuErrorReport.add(tabelDaftarReportSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 130, 580, 260));

        lblErrorReportKeuSubTitle.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblErrorReportKeuSubTitle.setForeground(java.awt.Color.gray);
        lblErrorReportKeuSubTitle.setText("Sistem Informasi Manajemen");
        subpanelKeuErrorReport.add(lblErrorReportKeuSubTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(335, 53, -1, -1));

        btn_delete_error_keu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/btnDelete.png"))); // NOI18N
        btn_delete_error_keu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_delete_error_keu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_delete_error_keuMouseClicked(evt);
            }
        });
        subpanelKeuErrorReport.add(btn_delete_error_keu, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 87, -1, -1));

        panelKeuDashboard.add(subpanelKeuErrorReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));
        panelKeuDashboard.add(jSeparator104, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 170, 10));

        menuKeuErrorReport.setBackground(new java.awt.Color(241, 241, 241));
        menuKeuErrorReport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuKeuErrorReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuKeuErrorReportMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuKeuErrorReportMouseExited(evt);
            }
        });
        menuKeuErrorReport.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconKeuErrorReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/IconErrorReport.png"))); // NOI18N
        iconKeuErrorReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconKeuErrorReportMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconKeuErrorReportMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconKeuErrorReportMouseExited(evt);
            }
        });
        menuKeuErrorReport.add(iconKeuErrorReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        panelKeuDashboard.add(menuKeuErrorReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 315, 170, 30));
        panelKeuDashboard.add(jSeparator101, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 170, 10));

        menuKeuDashboard.setBackground(new java.awt.Color(241, 241, 241));
        menuKeuDashboard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuKeuDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuKeuDashboardMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuKeuDashboardMouseExited(evt);
            }
        });
        menuKeuDashboard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconKeuDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/IconDashboard.png"))); // NOI18N
        iconKeuDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconKeuDashboardMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconKeuDashboardMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconKeuDashboardMouseExited(evt);
            }
        });
        menuKeuDashboard.add(iconKeuDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 7, -1, -1));

        panelKeuDashboard.add(menuKeuDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 275, 170, 30));
        panelKeuDashboard.add(jSeparator102, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 170, 10));

        btnChangeStateKeu.setText("Change State");
        btnChangeStateKeu.setToolTipText("Click to Change State");
        btnChangeStateKeu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnChangeStateKeu.setMaximumSize(new java.awt.Dimension(73, 23));
        btnChangeStateKeu.setMinimumSize(new java.awt.Dimension(74, 23));
        btnChangeStateKeu.setPreferredSize(new java.awt.Dimension(78, 23));
        btnChangeStateKeu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnChangeStateKeuMouseClicked(evt);
            }
        });
        panelKeuDashboard.add(btnChangeStateKeu, new org.netbeans.lib.awtextra.AbsoluteConstraints(83, 230, 100, -1));

        btnHideIPKeu.setText("Hide IP");
        btnHideIPKeu.setToolTipText("Click to Hide IP");
        btnHideIPKeu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHideIPKeu.setMaximumSize(new java.awt.Dimension(73, 23));
        btnHideIPKeu.setMinimumSize(new java.awt.Dimension(74, 23));
        btnHideIPKeu.setPreferredSize(new java.awt.Dimension(73, 23));
        btnHideIPKeu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHideIPKeuMouseClicked(evt);
            }
        });
        panelKeuDashboard.add(btnHideIPKeu, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 230, -1, -1));

        btnShowIPKeu.setText("Show IP");
        btnShowIPKeu.setToolTipText("Click to show IP");
        btnShowIPKeu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnShowIPKeu.setMaximumSize(new java.awt.Dimension(73, 23));
        btnShowIPKeu.setMinimumSize(new java.awt.Dimension(73, 23));
        btnShowIPKeu.setPreferredSize(new java.awt.Dimension(73, 23));
        btnShowIPKeu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnShowIPKeuMouseClicked(evt);
            }
        });
        panelKeuDashboard.add(btnShowIPKeu, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 230, -1, -1));

        txtServerTimeKeu.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        txtServerTimeKeu.setText("SERVER TIME");
        panelKeuDashboard.add(txtServerTimeKeu, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 693, -1, -1));

        txt_stateBigKeu.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        txt_stateBigKeu.setText("SERVER STATE");
        panelKeuDashboard.add(txt_stateBigKeu, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 693, -1, -1));

        lblStateBigKeu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OnlineState.png"))); // NOI18N
        panelKeuDashboard.add(lblStateBigKeu, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 693, -1, -1));

        lblStateKeu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OnlineState.png"))); // NOI18N
        panelKeuDashboard.add(lblStateKeu, new org.netbeans.lib.awtextra.AbsoluteConstraints(78, 208, -1, -1));

        txt_stateKeu.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        txt_stateKeu.setText("Online");
        panelKeuDashboard.add(txt_stateKeu, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 210, -1, -1));

        jLabel32.setFont(new java.awt.Font("Gotham Medium", 1, 11)); // NOI18N
        jLabel32.setText("Keyko");
        panelKeuDashboard.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(595, 695, -1, -1));

        jLabel33.setFont(new java.awt.Font("Gotham Light", 2, 11)); // NOI18N
        jLabel33.setText("powered by");
        panelKeuDashboard.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 695, -1, -1));

        jLabel34.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        jLabel34.setText("NOB Tech - ");
        panelKeuDashboard.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(465, 695, -1, -1));

        txtKeuNama.setFont(new java.awt.Font("Gotham Medium", 1, 12)); // NOI18N
        txtKeuNama.setText("KEUANGAN");
        panelKeuDashboard.add(txtKeuNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(79, 190, -1, -1));

        jLabel35.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        jLabel35.setText("Hello, ");
        panelKeuDashboard.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 170, -1, -1));

        iconAdminKeu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/AdminLogo.png"))); // NOI18N
        panelKeuDashboard.add(iconAdminKeu, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        bgKeu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/keuangan/dashboardKeuangan.png"))); // NOI18N
        panelKeuDashboard.add(bgKeu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelKeuDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 721));

        panelKilledbyServer.setBackground(new java.awt.Color(255, 255, 255));
        panelKilledbyServer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/keuangan/keuKB.png"))); // NOI18N
        panelKilledbyServer.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelKilledbyServer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 720));

        panelKeuLogin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelKeuLogin.add(txtUsernameKeu, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 380, 230, 40));
        panelKeuLogin.add(txtPasswordKeu, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 470, 230, 40));

        btnLoginKeu.setText("LOGIN");
        btnLoginKeu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLoginKeuMouseClicked(evt);
            }
        });
        panelKeuLogin.add(btnLoginKeu, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 550, -1, -1));

        btnResetKeu.setText("CANCEL");
        btnResetKeu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnResetKeuMouseClicked(evt);
            }
        });
        panelKeuLogin.add(btnResetKeu, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 550, -1, -1));

        panelLoginHrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/keuangan/panelLogin.png"))); // NOI18N
        panelKeuLogin.add(panelLoginHrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 120, -1, -1));

        bgClientHRD2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/keuangan/Keuangan.png"))); // NOI18N
        panelKeuLogin.add(bgClientHRD2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelKeuLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 721));

        panelKeuLandingPage.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconHomeHRD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconHomeGreen.png"))); // NOI18N
        iconHomeHRD.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconHomeHRD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconHomeHRDMouseClicked(evt);
            }
        });
        panelKeuLandingPage.add(iconHomeHRD, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        bgHRDLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/keuangan/iconKeuangan.png"))); // NOI18N
        bgHRDLogo.setToolTipText("");
        bgHRDLogo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bgHRDLogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bgHRDLogoMouseClicked(evt);
            }
        });
        panelKeuLandingPage.add(bgHRDLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 200, -1, -1));

        bgClientHRD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/keuangan/Keuangan.png"))); // NOI18N
        panelKeuLandingPage.add(bgClientHRD, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelKeuLandingPage, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 720));

        panelBackOffice.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconHRD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/landingpage/iconBackOfficeHRD.png"))); // NOI18N
        iconHRD.setToolTipText("");
        iconHRD.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconHRD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconHRDMouseClicked(evt);
            }
        });
        panelBackOffice.add(iconHRD, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 430, -1, -1));

        iconSIM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/btnSIM.png"))); // NOI18N
        iconSIM.setToolTipText("");
        iconSIM.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconSIM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconSIMMouseClicked(evt);
            }
        });
        panelBackOffice.add(iconSIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 120, -1, -1));

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
        Mode10();
    }//GEN-LAST:event_bgLogoNOBMouseClicked

    private void iconBOMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconBOMouseClicked
        // TODO add your handling code here:
        Mode11();
    }//GEN-LAST:event_iconBOMouseClicked

    private void iconFOMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconFOMouseClicked
        // TODO add your handling code here:
        Mode12();
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

    private void bgHRDLogoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bgHRDLogoMouseClicked
        // TODO add your handling code here:
        Mode2();
    }//GEN-LAST:event_bgHRDLogoMouseClicked

    private void btnLoginKeuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLoginKeuMouseClicked
        // TODO add your handling code here:
        String lettercode = txtUsernameKeu.getText().substring(0, 3);
        System.out.println(lettercode);
        if (txtUsernameKeu.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please Input Your Username", "Sign In Error", JOptionPane.ERROR_MESSAGE);
            txtUsernameKeu.requestFocus();
        } else if (txtPasswordKeu.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please Input Your Password", "Sign In Error", JOptionPane.ERROR_MESSAGE);
            txtPasswordKeu.requestFocus();
        } else if (txtUsernameKeu.getText().substring(0, 3).equals("KEU")) {
            Entity_Signin ESI = new Entity_Signin();
            ESI.setUsername(txtUsernameKeu.getText());
            ESI.setPassword(txtPasswordKeu.getText());
            ESI.setStatus("Active");
            ESI.setDlc("KEU");
            try {
                int hasil = loginDao.Login(ESI);
                if (hasil == 1) {
                    JOptionPane.showMessageDialog(null, "Sign In Success", "Sign-In Success", JOptionPane.INFORMATION_MESSAGE);
                    Mode3();
                    SetKeuStatusOnline();
                    CekStatus5Sec();
                    last_session = 3;
                    setIPKEU();
                } else {
                    JOptionPane.showMessageDialog(null, "Password or Username Does Not Match", "Sign-In Error", JOptionPane.ERROR_MESSAGE);
                    txtUsernameKeu.setText("");
                    txtPasswordKeu.setText("");
                }
            } catch (RemoteException ex) {
                Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Sorry, You Have No Access to HRD Area", "Sign In Error", JOptionPane.ERROR_MESSAGE);
            txtPasswordKeu.requestFocus();
            txtUsernameKeu.setText("");
            txtPasswordKeu.setText("");
        }

        whoisonline = txtUsernameKeu.getText();
        Entity_Signin ESI = new Entity_Signin();
        ESI.setUsername(whoisonline);
        try {
            String hasil = loginDao.CekNamaOnline(ESI);
            txtKeuNama.setText(hasil);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnLoginKeuMouseClicked

    private void btnResetKeuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetKeuMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnResetKeuMouseClicked

    private void btnLogoutKeuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutKeuMouseClicked
        // TODO add your handling code here:
        Object[] options = {"LogOut", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Are you sure to Logout ?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                Mode2();
                clearFormLogin();
                SetKeuStatusOffline();
                ses.shutdown();
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btnLogoutKeuMouseClicked

    private void iconKeuDashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconKeuDashboardMouseClicked
        // TODO add your handling code here:
        Mode3();
        last_session = 3;
    }//GEN-LAST:event_iconKeuDashboardMouseClicked

    private void iconKeuDashboardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconKeuDashboardMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuKeuDashboard.setBackground(skyBlue);
    }//GEN-LAST:event_iconKeuDashboardMouseEntered

    private void iconKeuDashboardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconKeuDashboardMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuKeuDashboard.setBackground(lightGray);
    }//GEN-LAST:event_iconKeuDashboardMouseExited

    private void menuKeuDashboardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuKeuDashboardMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuKeuDashboard.setBackground(skyBlue);
    }//GEN-LAST:event_menuKeuDashboardMouseEntered

    private void menuKeuDashboardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuKeuDashboardMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuKeuDashboard.setBackground(lightGray);
    }//GEN-LAST:event_menuKeuDashboardMouseExited

    private void btnChangeStateKeuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChangeStateKeuMouseClicked
        // TODO add your handling code here:
        int hasilUpdateStatusKeu = 0;
        Object[] options = {"Online", "Idle", "Invisible", "Offline"};
        int reply = JOptionPane.showOptionDialog(null, "Choose your Client [Keuangan] state : ", "State Chooser", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                if (txt_stateKeu.getText().equals("Online")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Online", "Warning Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int hasilSetKeuStatus = loginDao.UpdateStatusOnline(kodeSistem);
                        if (hasilSetKeuStatus == 1) {
                            txt_stateBigKeu.setText("Server is Well-Connected");
                            lblStateKeu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OnlineState.png")));
                            lblStateBigKeu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OnlineState.png")));
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case 1:
                if (txt_stateKeu.getText().equals("Idle")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Idle", "Warning Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int hasilSetKeuStatus = loginDao.UpdateStatusIdle(kodeSistem);
                        if (hasilSetKeuStatus == 1) {
                            txt_stateBigKeu.setText("Server is Well-Connected");
                            lblStateKeu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/IdleState.png")));
                            lblStateBigKeu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/IdleState.png")));
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case 2:
                if (txt_stateKeu.getText().equals("Invisible")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Invisible", "Warning Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int hasilSetKeuStatus = loginDao.UpdateStatusInvisible(kodeSistem);
                        if (hasilSetKeuStatus == 1) {
                            txt_stateBigKeu.setText("Server is Well-Connected");
                            lblStateKeu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/InvisState.png")));
                            lblStateBigKeu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/InvisState.png")));
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case 3:
                if (txt_stateKeu.getText().equals("Offline")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Offline", "Warning Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int hasilSetKeuStatus = loginDao.UpdateStatusDisconnect(kodeSistem);
                        if (hasilSetKeuStatus == 1) {

                            txt_stateBigKeu.setText("Server is Disconnected");
                            lblStateKeu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OffState.png")));
                            lblStateBigKeu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OffState.png")));
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
    }//GEN-LAST:event_btnChangeStateKeuMouseClicked

    private void btnHideIPKeuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHideIPKeuMouseClicked
        // TODO add your handling code here:
        whoisonline = txtUsernameKeu.getText();
//        System.out.println(whoisonline);
        Entity_Signin ESI = new Entity_Signin();
        ESI.setUsername(whoisonline);
        try {
            String hasil = loginDao.CekNamaOnline(ESI);
            txtKeuNama.setText(hasil);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        ShowIPModeWrh();
    }//GEN-LAST:event_btnHideIPKeuMouseClicked

    private void btnShowIPKeuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnShowIPKeuMouseClicked
        // TODO add your handling code here:
        txtKeuNama.setText(ip);
        HideIPModeWrh();
    }//GEN-LAST:event_btnShowIPKeuMouseClicked

    private void iconWarehouseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWarehouseMouseClicked
        // TODO add your handling code here:
        HomePageClientWarehouse HPC2 = new HomePageClientWarehouse();
        HPC2.setVisible(true);
        HPC2.Mode2();
        dispose();
    }//GEN-LAST:event_iconWarehouseMouseClicked

    private void iconSIMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconSIMMouseClicked
        // TODO add your handling code here:
        Mode2();
    }//GEN-LAST:event_iconSIMMouseClicked

    private void btnSaveError_keuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveError_keuMouseClicked
        // TODO add your handling code here:
        String error_added_by = null;

        Entity_Signup ESU = new Entity_Signup();
        ESU.setName(txtKeuNama.getText());
        try {
            error_added_by = errorDao.getIDAdminError(ESU);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }

        Entity_ErrorReport EER = new Entity_ErrorReport();
        EER.setError_id(txt_error_id_keu.getText());
        EER.setError_title(txt_error_title_keu.getText());
        EER.setError_desc(txt_error_desc_keu.getText());
        EER.setError_date(dtError_Date_keu.getDate());
        EER.setError_status((String) cb_error_status_keu.getSelectedItem());
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
    }//GEN-LAST:event_btnSaveError_keuMouseClicked

    private void btnClearError_keuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClearError_keuMouseClicked
        // TODO add your handling code here:
        ClearFormError();
    }//GEN-LAST:event_btnClearError_keuMouseClicked

    private void tabelDaftarError_KeuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelDaftarError_KeuMouseClicked
        // TODO add your handling code here:
        baris = tabelDaftarError_Keu.getSelectedRow();
        kolom = tabelDaftarError_Keu.getSelectedColumn();
        dataTerpilih = tabelDaftarError_Keu.getValueAt(baris, 0).toString();
    }//GEN-LAST:event_tabelDaftarError_KeuMouseClicked

    private void btn_delete_error_keuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_delete_error_keuMouseClicked
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
                initTabelError();
                loadDaftarError();
                init_Error_ID();
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btn_delete_error_keuMouseClicked

    private void iconSIMFOMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconSIMFOMouseClicked
        // TODO add your handling code here:
        Mode2();
    }//GEN-LAST:event_iconSIMFOMouseClicked

    private void iconHomeHRDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconHomeHRDMouseClicked
        // TODO add your handling code here:
        Mode10();
    }//GEN-LAST:event_iconHomeHRDMouseClicked

    private void iconBOBigMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconBOBigMouseClicked
        // TODO add your handling code here:
        Mode10();
    }//GEN-LAST:event_iconBOBigMouseClicked

    private void iconFOBigMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconFOBigMouseClicked
        // TODO add your handling code here:
        Mode10();
    }//GEN-LAST:event_iconFOBigMouseClicked

    private void iconHRDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconHRDMouseClicked
        // TODO add your handling code here:
        HomePageClientHRD HPCHRD = new HomePageClientHRD();
        HPCHRD.Mode3();
        dispose();
    }//GEN-LAST:event_iconHRDMouseClicked

    private void iconKeuanganMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconKeuanganMouseClicked
        // TODO add your handling code here:
        Mode1();
    }//GEN-LAST:event_iconKeuanganMouseClicked

    private void menuKeuErrorReportMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuKeuErrorReportMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuKeuErrorReport.setBackground(lightGray);
    }//GEN-LAST:event_menuKeuErrorReportMouseExited

    private void menuKeuErrorReportMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuKeuErrorReportMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuKeuErrorReport.setBackground(skyBlue);
    }//GEN-LAST:event_menuKeuErrorReportMouseEntered

    private void iconKeuErrorReportMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconKeuErrorReportMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuKeuErrorReport.setBackground(lightGray);
    }//GEN-LAST:event_iconKeuErrorReportMouseExited

    private void iconKeuErrorReportMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconKeuErrorReportMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuKeuErrorReport.setBackground(skyBlue);
    }//GEN-LAST:event_iconKeuErrorReportMouseEntered

    private void iconKeuErrorReportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconKeuErrorReportMouseClicked
        // TODO add your handling code here:
        Mode8();
        init_Error_ID();
        initTabelError();
        loadDaftarError();
        last_session = 8;
    }//GEN-LAST:event_iconKeuErrorReportMouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        // TODO add your handling code here:
        Mode4();
        last_session = 4;
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
        Mode5();
        last_session = 5;
    }//GEN-LAST:event_jLabel2MouseClicked

    private void btn_CetakPendapatanHarianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CetakPendapatanHarianActionPerformed
        // TODO add your handling code here:
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        if (dtTanggalAwalPendapatan.getDate() == null) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Pilih Tanggal Awal dahulu", "Info", JOptionPane.WARNING_MESSAGE);
            dtTanggalAwalPendapatan.requestFocus();
        } else if (dtTanggalAkhirPendapatan.getDate() == null) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Pilih Tanggal Akhir dahulu", "Info", JOptionPane.WARNING_MESSAGE);
            dtTanggalAkhirPendapatan.requestFocus();
        } else {
            Object[] options = {"Yes", "Cancel"};
            int reply = JOptionPane.showOptionDialog(null, "Cetak Laporan Pendapatan Keseluruhan ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            switch (reply) {
                case 0:

                    tglAwal = format.format(dtTanggalAwalPendapatan.getDate());
                    tglAkhir = format.format(dtTanggalAkhirPendapatan.getDate());

                    try {
                        JasperDesign jd = JRXmlLoader.load("src/nob/files/PendapatanHarian.jrxml");
                        JasperReport jr = JasperCompileManager.compileReport(jd);
                        HashMap parameter = new HashMap();
                        parameter.put("TanggalAwal", dtTanggalAwalPendapatan.getDate());
                        parameter.put("TanggalAkhir", dtTanggalAkhirPendapatan.getDate());

                        JasperPrint jp = JasperFillManager.fillReport(jr, parameter, DriverManager.getConnection("jdbc:sqlserver://"+configuration.ip_server+":1433;DatabaseName=NOBTech", "sa", "12345"));
                        JasperViewer.viewReport(jp, false);

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                    }

                    break;
                case 1:
                    break;
                default:
                    break;
            }
        }
    }//GEN-LAST:event_btn_CetakPendapatanHarianActionPerformed

    private void btn_CetakPendapatanAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CetakPendapatanAllActionPerformed
        // TODO add your handling code here:
        Object[] options = {"Yes", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Cetak Laporan Pendapatan Keseluruhan ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                try {
                    JasperDesign jd = JRXmlLoader.load("src/nob/files/Pendapatan.jrxml");
                    JasperReport jr = JasperCompileManager.compileReport(jd);

                    JasperPrint jp = JasperFillManager.fillReport(jr, null, DriverManager.getConnection("jdbc:sqlserver://"+configuration.ip_server+":1433;DatabaseName=NOBTech", "sa", "12345"));
                    JasperViewer.viewReport(jp, false);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                break;
            case 1:
                break;
            default:
                break;

        }
    }//GEN-LAST:event_btn_CetakPendapatanAllActionPerformed

    private void btn_CetakPendapatanBulananActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CetakPendapatanBulananActionPerformed
        // TODO add your handling code here:
        if (cbPendapatanBulan.getSelectedIndex() == 0) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Pilih Bulan dahulu", "Info", JOptionPane.WARNING_MESSAGE);
            cbPendapatanBulan.requestFocus();
        } else if (cbPendapatanTahun.getSelectedIndex() == 0) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Pilih Tahun dahulu", "Info", JOptionPane.WARNING_MESSAGE);
            cbPendapatanTahun.requestFocus();
        } else {
            Object[] options = {"Yes", "Cancel"};
            int reply = JOptionPane.showOptionDialog(null, "Cetak Laporan Pendapatan Bulan " + cbPendapatanBulan.getSelectedItem() + " ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            switch (reply) {
                case 0:

                    String bulanPendapatan = null;
                    String tahunPendapatan = null;

                    if (cbPendapatanBulan.getSelectedItem().equals("Januari")) {
                        bulanPendapatan = "01";
                    } else if (cbPendapatanBulan.getSelectedItem().equals("Februari")) {
                        bulanPendapatan = "02";
                    } else if (cbPendapatanBulan.getSelectedItem().equals("Maret")) {
                        bulanPendapatan = "03";
                    } else if (cbPendapatanBulan.getSelectedItem().equals("April")) {
                        bulanPendapatan = "04";
                    } else if (cbPendapatanBulan.getSelectedItem().equals("Mei")) {
                        bulanPendapatan = "05";
                    } else if (cbPendapatanBulan.getSelectedItem().equals("Juni")) {
                        bulanPendapatan = "06";
                    } else if (cbPendapatanBulan.getSelectedItem().equals("Juli")) {
                        bulanPendapatan = "07";
                    } else if (cbPendapatanBulan.getSelectedItem().equals("Agustus")) {
                        bulanPendapatan = "08";
                    } else if (cbPendapatanBulan.getSelectedItem().equals("September")) {
                        bulanPendapatan = "09";
                    } else if (cbPendapatanBulan.getSelectedItem().equals("Oktober")) {
                        bulanPendapatan = "10";
                    } else if (cbPendapatanBulan.getSelectedItem().equals("November")) {
                        bulanPendapatan = "11";
                    } else if (cbPendapatanBulan.getSelectedItem().equals("Desember")) {
                        bulanPendapatan = "12";
                    }

                    tahunPendapatan = String.valueOf(cbPendapatanTahun.getSelectedItem());
                    try {
                        JasperDesign jd = JRXmlLoader.load("src/nob/files/PendapatanBulanan.jrxml");
                        JasperReport jr = JasperCompileManager.compileReport(jd);
                        HashMap parameter = new HashMap();
                        parameter.put("Bulan", bulanPendapatan);
                        parameter.put("Tahun", tahunPendapatan);

                        JasperPrint jp = JasperFillManager.fillReport(jr, parameter, DriverManager.getConnection("jdbc:sqlserver://"+configuration.ip_server+":1433;DatabaseName=NOBTech", "sa", "12345"));
                        JasperViewer.viewReport(jp, false);

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                    }

                    break;
                case 1:
                    break;
                default:
                    break;
            }
        }
    }//GEN-LAST:event_btn_CetakPendapatanBulananActionPerformed

    private void btn_CetakPendapatanTahunanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CetakPendapatanTahunanActionPerformed
        // TODO add your handling code here:       
        if (cbPendapatanTahunan.getSelectedIndex() == 0) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Pilih Tahun dahulu", "Info", JOptionPane.WARNING_MESSAGE);
            cbPendapatanTahunan.requestFocus();
        } else {
            Object[] options = {"Yes", "Cancel"};
            int reply = JOptionPane.showOptionDialog(null, "Cetak Laporan Pendapatan Tahun " + cbPendapatanTahunan.getSelectedItem() + " ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            switch (reply) {
                case 0:

                    String tahunPendapatan = String.valueOf(cbPendapatanTahunan.getSelectedItem());
                    try {
                        JasperDesign jd = JRXmlLoader.load("src/nob/files/PendapatanTahunan.jrxml");
                        JasperReport jr = JasperCompileManager.compileReport(jd);
                        HashMap parameter = new HashMap();
                        parameter.put("Tahun", tahunPendapatan);

                        JasperPrint jp = JasperFillManager.fillReport(jr, parameter, DriverManager.getConnection("jdbc:sqlserver://"+configuration.ip_server+":1433;DatabaseName=NOBTech", "sa", "12345"));
                        JasperViewer.viewReport(jp, false);

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                    }

                    break;
                case 1:
                    break;
                default:
                    break;
            }
        }
    }//GEN-LAST:event_btn_CetakPendapatanTahunanActionPerformed

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel5MouseClicked

    private void btn_CetakPendapatanHarian1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CetakPendapatanHarian1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_CetakPendapatanHarian1ActionPerformed

    private void btn_CetakPendapatanAll1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CetakPendapatanAll1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_CetakPendapatanAll1ActionPerformed

    private void btn_CetakPendapatanBulanan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CetakPendapatanBulanan1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_CetakPendapatanBulanan1ActionPerformed

    private void btn_CetakPendapatanTahunan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CetakPendapatanTahunan1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_CetakPendapatanTahunan1ActionPerformed

    private void btn_CetakPendapatanHarian2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CetakPendapatanHarian2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_CetakPendapatanHarian2ActionPerformed

    private void btn_CetakPendapatanAll2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CetakPendapatanAll2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_CetakPendapatanAll2ActionPerformed

    private void btn_CetakPendapatanBulanan2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CetakPendapatanBulanan2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_CetakPendapatanBulanan2ActionPerformed

    private void btn_CetakPendapatanTahunan2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CetakPendapatanTahunan2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_CetakPendapatanTahunan2ActionPerformed

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
            java.util.logging.Logger.getLogger(HomePageClientKeuangan.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomePageClientKeuangan.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomePageClientKeuangan.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomePageClientKeuangan.class
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
                new HomePageClientKeuangan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bgBackOffice;
    private javax.swing.JLabel bgClientHRD;
    private javax.swing.JLabel bgClientHRD2;
    private javax.swing.JLabel bgDepartment;
    private javax.swing.JLabel bgFrontOffice;
    private javax.swing.JLabel bgHRDLogo;
    private javax.swing.JLabel bgKeu;
    private javax.swing.JLabel bgLandingPage;
    private javax.swing.JLabel bgLogoNOB;
    private javax.swing.JButton btnChangeStateKeu;
    private javax.swing.JLabel btnClearError_keu;
    private javax.swing.JButton btnHideIPKeu;
    private javax.swing.JButton btnLoginKeu;
    private javax.swing.JLabel btnLogoutKeu;
    private javax.swing.JLabel btnMsgKeu;
    private javax.swing.JLabel btnNotifKeu;
    private javax.swing.JButton btnResetKeu;
    private javax.swing.JLabel btnSaveError_keu;
    private javax.swing.JButton btnShowIPKeu;
    private javax.swing.JButton btn_CetakPendapatanAll;
    private javax.swing.JButton btn_CetakPendapatanAll1;
    private javax.swing.JButton btn_CetakPendapatanAll2;
    private javax.swing.JButton btn_CetakPendapatanBulanan;
    private javax.swing.JButton btn_CetakPendapatanBulanan1;
    private javax.swing.JButton btn_CetakPendapatanBulanan2;
    private javax.swing.JButton btn_CetakPendapatanHarian;
    private javax.swing.JButton btn_CetakPendapatanHarian1;
    private javax.swing.JButton btn_CetakPendapatanHarian2;
    private javax.swing.JButton btn_CetakPendapatanTahunan;
    private javax.swing.JButton btn_CetakPendapatanTahunan1;
    private javax.swing.JButton btn_CetakPendapatanTahunan2;
    private javax.swing.JLabel btn_delete_error_keu;
    private javax.swing.JButton btn_trans_clearall1;
    private javax.swing.JButton btn_trans_process1;
    private javax.swing.ButtonGroup buttongroup_Gender;
    private javax.swing.JComboBox<String> cbPendapatanBulan;
    private javax.swing.JComboBox<String> cbPendapatanBulan1;
    private javax.swing.JComboBox<String> cbPendapatanBulan2;
    private javax.swing.JComboBox<String> cbPendapatanTahun;
    private javax.swing.JComboBox<String> cbPendapatanTahun1;
    private javax.swing.JComboBox<String> cbPendapatanTahun2;
    private javax.swing.JComboBox<String> cbPendapatanTahunan;
    private javax.swing.JComboBox<String> cbPendapatanTahunan1;
    private javax.swing.JComboBox<String> cbPendapatanTahunan2;
    private javax.swing.JComboBox<String> cb_error_status_keu;
    private com.toedter.calendar.JDateChooser dtError_Date_keu;
    private com.toedter.calendar.JDateChooser dtTanggalAkhirPendapatan;
    private com.toedter.calendar.JDateChooser dtTanggalAkhirPendapatan1;
    private com.toedter.calendar.JDateChooser dtTanggalAkhirPendapatan2;
    private com.toedter.calendar.JDateChooser dtTanggalAwalPendapatan;
    private com.toedter.calendar.JDateChooser dtTanggalAwalPendapatan1;
    private com.toedter.calendar.JDateChooser dtTanggalAwalPendapatan2;
    private javax.swing.JLabel iconAdminKeu;
    private javax.swing.JLabel iconBO;
    private javax.swing.JLabel iconBOBig;
    private javax.swing.JLabel iconCashier;
    private javax.swing.JLabel iconFO;
    private javax.swing.JLabel iconFOBig;
    private javax.swing.JLabel iconHRD;
    private javax.swing.JLabel iconHomeHRD;
    private javax.swing.JLabel iconKeuDashboard;
    private javax.swing.JLabel iconKeuErrorReport;
    private javax.swing.JLabel iconKeuangan;
    private javax.swing.JLabel iconManagement;
    private javax.swing.JLabel iconSIM;
    private javax.swing.JLabel iconSIMFO;
    private javax.swing.JLabel iconWarehouse;
    private javax.swing.JInternalFrame inframeQRCode;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JSeparator jSeparator101;
    private javax.swing.JSeparator jSeparator102;
    private javax.swing.JSeparator jSeparator104;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JSeparator jSeparator22;
    private javax.swing.JSeparator jSeparator23;
    private javax.swing.JLabel lblDashboardTitle2;
    private javax.swing.JLabel lblDashboardTitle3;
    private javax.swing.JLabel lblDashboardTitle5;
    private javax.swing.JLabel lblDashboardTitle6;
    private javax.swing.JLabel lblDashboardTitleKEU;
    private javax.swing.JLabel lblDashboardTitleKEU1;
    private javax.swing.JLabel lblDashboardTitleKEU3;
    private javax.swing.JLabel lblDashboardTitleKEU4;
    private javax.swing.JLabel lblErrorReportKeuSubTitle;
    private javax.swing.JLabel lblErrorReportKeuSubTitle1;
    private javax.swing.JLabel lblErrorReportTitleKeu;
    private javax.swing.JLabel lblStateBigKeu;
    private javax.swing.JLabel lblStateKeu;
    private javax.swing.JLabel lblTransInvoice_Confirm;
    private javax.swing.JLabel lbl_error_date_keu;
    private javax.swing.JLabel lbl_error_desc_keu;
    private javax.swing.JLabel lbl_error_id_keu;
    private javax.swing.JLabel lbl_error_status_keu;
    private javax.swing.JLabel lbl_error_title_keu;
    private javax.swing.JLabel lbl_list_error_keu;
    private javax.swing.JLabel lbl_pilihanpencarian10;
    private javax.swing.JLabel lbl_pilihanpencarian11;
    private javax.swing.JLabel lbl_pilihanpencarian12;
    private javax.swing.JLabel lbl_pilihanpencarian13;
    private javax.swing.JLabel lbl_pilihanpencarian14;
    private javax.swing.JLabel lbl_pilihanpencarian15;
    private javax.swing.JLabel lbl_pilihanpencarian4;
    private javax.swing.JLabel lbl_pilihanpencarian5;
    private javax.swing.JLabel lbl_pilihanpencarian6;
    private javax.swing.JLabel lbl_pilihanpencarian7;
    private javax.swing.JLabel lbl_pilihanpencarian8;
    private javax.swing.JLabel lbl_pilihanpencarian9;
    private javax.swing.JPanel menuKeuDashboard;
    private javax.swing.JPanel menuKeuErrorReport;
    private javax.swing.JPanel panelBackOffice;
    private javax.swing.JPanel panelDepartment;
    private javax.swing.JPanel panelFrontOffice;
    private javax.swing.JPanel panelKeuDashboard;
    private javax.swing.JPanel panelKeuLandingPage;
    private javax.swing.JPanel panelKeuLogin;
    private javax.swing.JPanel panelKilledbyServer;
    private javax.swing.JPanel panelLandingPage;
    private javax.swing.JLabel panelLoginHrd;
    private javax.swing.JPanel panelQRCode_trans;
    private javax.swing.JPanel subpanelKeuDashboard;
    private javax.swing.JPanel subpanelKeuErrorReport;
    private javax.swing.JPanel subpanelPrintOmset;
    private javax.swing.JPanel subpanelPrintPendapatan;
    private javax.swing.JPanel subpanelPrintPengeluaran;
    private javax.swing.JTable tabelDaftarError_Keu;
    private javax.swing.JScrollPane tabelDaftarReportSP;
    private javax.swing.JLabel txtKeuNama;
    private javax.swing.JPasswordField txtPasswordKeu;
    private javax.swing.JLabel txtServerTimeKeu;
    private javax.swing.JTextField txtUsernameKeu;
    private javax.swing.JTextArea txt_error_desc_keu;
    private javax.swing.JTextField txt_error_id_keu;
    private javax.swing.JTextField txt_error_title_keu;
    private javax.swing.JLabel txt_stateBigKeu;
    private javax.swing.JLabel txt_stateKeu;
    private javax.swing.JLabel txt_trans_invoiceno_confirm;
    // End of variables declaration//GEN-END:variables
}
