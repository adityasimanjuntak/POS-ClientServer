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
public class HomePageClientHRD extends javax.swing.JFrame {

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

    public String kodeSistem = "HRD";
    String hasilgetStatusServer = null;
    String hasilgetStatusHrd = null;
    final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

    int last_session = 0;
    String State_run_Hrd = null;

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
    public HomePageClientHRD() {
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

        txt_count_notifIZIN.setVisible(false);
        txt_count_notifACT.setVisible(false);

        State_run_Hrd = "Running";

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Object[] options = {"Yes", "Cancel"};
                int reply = JOptionPane.showOptionDialog(null, "Are you sure to close the system ?", "Information", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                switch (reply) {
                    case 0:
                        try {
                            if (State_run_Hrd.equals("Running")) {
                                SetHrdStatusDisconnect();
                                e.getWindow().dispose();
                                System.exit(0);
                            } else if (hasilgetStatusServer.equals("Server Offline")) {
                                SetHrdStatusDisconnect();
                                e.getWindow().dispose();
                            } else {
                                int hasilSetHrdStatus = loginDao.UpdateStatusOffline("HRD");
                                if (hasilSetHrdStatus == 1) {
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

    public void SetHrdStatusOnline() {
        try {
            int hasilSetServerStatus = loginDao.UpdateStatusOnline(kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SetHrdStatusOffline() {
        try {
            int hasilSetServerStatus = loginDao.UpdateStatusOffline(kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SetHrdStatusDisconnect() {
        try {
            int hasilSetHrdStatus = loginDao.UpdateStatusDisconnect(kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getStatusHrd() {
        try {
            hasilgetStatusHrd = loginDao.CekClientStatus(kodeSistem);
            txt_stateHrd.setText(hasilgetStatusHrd);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SetHrdStatusKilledbyServer() {
        try {
            int hasilSetHrdStatus = loginDao.UpdateStatusKilledbyServer(kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getStatusServer() {
        try {
            hasilgetStatusServer = loginDao.CekClientStatus("SERVER");
            if (hasilgetStatusHrd.equals("Killed by Server")) {
                Mode13();
                SetHrdStatusKilledbyServer();
            } else if (hasilgetStatusServer.equals("Server Offline")) {
                Mode13();
                SetHrdStatusDisconnect();
            } else if (hasilgetStatusHrd.equals("Online")) {
                if (last_session == 3) {
                    Mode3();
                } else if (last_session == 4) {
                    Mode4();
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

    public void setIPHRD() {
        Entity_Signin ES = new Entity_Signin();
        ES.setIp(ip);
        try {
            int hasilsetIPServer = loginDao.UpdateIP(ES, kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void CekStatus5Sec() {
        ses.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                // TODO add your handling code here:
                getStatusHrd();
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
        panelHRDLandingPage.setVisible(true);
        panelHRDLogin.setVisible(false);
        panelHRDDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode2() {
        mode = 2;
        //Mode 2 = Menampilkan Panel Daftar Department Back Office - SIM - Login
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelHRDLandingPage.setVisible(false);
        panelHRDLogin.setVisible(true);
        panelHRDDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode3() {
        mode = 3;
        //Mode 3 = Menampilkan Panel Daftar Department Back Office - SIM - Dashboard
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelHRDLandingPage.setVisible(false);
        panelHRDLogin.setVisible(false);
        panelHRDDashboard.setVisible(true);
        subpanelHrdDashboard.setVisible(true);
        subpanelHrdListIzin.setVisible(false);
        subpanelHrdListActivity.setVisible(false);
        subpanelHrdListActivity.setVisible(false);
        subpanelHrdErrorReport.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode4() {
        mode = 4;
        //Mode 4 = Menampilkan Panel Daftar Department Back Office - SIM - Izin
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelHRDLandingPage.setVisible(false);
        panelHRDLogin.setVisible(false);
        panelHRDDashboard.setVisible(true);
        subpanelHrdDashboard.setVisible(false);
        subpanelHrdListIzin.setVisible(true);
        subpanelHrdListActivity.setVisible(false);
        subpanelHrdListActivity.setVisible(false);
        subpanelHrdErrorReport.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode6() {
        mode = 6;
        //Mode 6 = Menampilkan Panel Daftar Department Back Office - SIM - Activity
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelHRDLandingPage.setVisible(false);
        panelHRDLogin.setVisible(false);
        panelHRDDashboard.setVisible(true);
        subpanelHrdDashboard.setVisible(false);
        subpanelHrdListIzin.setVisible(false);
        subpanelHrdListActivity.setVisible(true);
        subpanelHrdErrorReport.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode8() {
        mode = 8;
        //Mode 8 = Menampilkan Panel Daftar Department Back Office - SIM - Error Report
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelHRDLandingPage.setVisible(false);
        panelHRDLogin.setVisible(false);
        panelHRDDashboard.setVisible(true);
        subpanelHrdDashboard.setVisible(false);
        subpanelHrdListIzin.setVisible(false);
        subpanelHrdListActivity.setVisible(false);
        subpanelHrdListActivity.setVisible(false);
        subpanelHrdErrorReport.setVisible(true);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode9() {
        //sebagai mode awal
        mode = 9;
        panelLandingPage.setVisible(true);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelHRDLandingPage.setVisible(false);
        panelHRDLogin.setVisible(false);
        panelHRDDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode10() {
        mode = 10;
        //Mode 10= Menampilkan Panel Daftar Department
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(true);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelHRDLandingPage.setVisible(false);
        panelHRDLogin.setVisible(false);
        panelHRDDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode11() {
        mode = 11;
        //Mode 11 = Menampilkan Panel Daftar Department Back Office
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(true);
        panelFrontOffice.setVisible(false);
        panelHRDLandingPage.setVisible(false);
        panelHRDLogin.setVisible(false);
        panelHRDDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode12() {
        mode = 12;
        //Mode 12 = Menampilkan Panel Daftar Department Front Office
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(true);
        panelHRDLandingPage.setVisible(false);
        panelHRDLogin.setVisible(false);
        panelHRDDashboard.setVisible(false);
        panelKilledbyServer.setVisible(false);
    }

    public void Mode13() {
        mode = 13;
        //Mode 13 = Menampilkan Panel Killed by Server
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelHRDLandingPage.setVisible(false);
        panelHRDLogin.setVisible(false);
        panelHRDDashboard.setVisible(false);
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
                    txtServerTimeHrd.setText("Date  :  " + day + "/" + (month + 1) + "/" + year + "   Time : " + hour + ":" + (minute) + ":" + second);

                    try {
                        sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
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
        btnShowIPHrd.setVisible(false);
        btnHideIPHrd.setVisible(true);
    }

    public void ShowIPModeWrh() {
        btnShowIPHrd.setVisible(true);
        btnHideIPHrd.setVisible(false);
    }

    private void init_User_Dao() {
        String url = "rmi://" + configuration.ip_server + ":1099/userDao";
        try {
            //ambil referensi dari remote objek yang ada di server
            userDao = (IUserDao) Naming.lookup(url);
        } catch (NotBoundException | MalformedURLException ex) {
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
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

    public int HitungJumlahIzinSuksesAll() {
        int hasilizinsukses = 0;
        try {
            hasilizinsukses = simDao.CountIzinSuccessAll();
            txt_izin_success.setText(String.valueOf(hasilizinsukses));
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasilizinsukses;
    }

    public int HitungJumlahIzinPendingAll() {
        int hasilizinpending = 0;
        try {
            hasilizinpending = simDao.CountIzinPendingAll();
            txt_izin_pending.setText(String.valueOf(hasilizinpending));
            txt_count_notifIZIN.setText(String.valueOf(hasilizinpending));
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasilizinpending;
    }

    public int HitungJumlahIzinRejectedAll() {
        int hasilizinrejected = 0;
        try {
            hasilizinrejected = simDao.CountIzinRejectedAll();
            txt_izin_rejected.setText(String.valueOf(hasilizinrejected));
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasilizinrejected;
    }

    public int HitungJumlahActSuksesAll() {
        int hasilactsukses = 0;
        try {
            hasilactsukses = simDao.CountActSuccessAll();
            txt_act_success.setText(String.valueOf(hasilactsukses));
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasilactsukses;
    }

    public int HitungJumlahActPendingAll() {
        int hasilactpending = 0;
        try {
            hasilactpending = simDao.CountActPendingAll();
            txt_count_notifACT.setText(String.valueOf(hasilactpending));
            txt_act_pending.setText(String.valueOf(hasilactpending));
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasilactpending;
    }

    public int HitungJumlahActRejectedAll() {
        int hasilactrejected = 0;
        try {
            hasilactrejected = simDao.CountActRejectedAll();
            txt_act_rejected.setText(String.valueOf(hasilactrejected));
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasilactrejected;
    }

    public void CekNotifikasi10Sec() {
        final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                // TODO add your handling code here:

                HitungJumlahActPendingAll();
                HitungJumlahActRejectedAll();
                HitungJumlahActSuksesAll();
                HitungJumlahIzinPendingAll();
                HitungJumlahIzinRejectedAll();
                HitungJumlahIzinSuksesAll();

                if (HitungJumlahIzinPendingAll() == 0) {
                    txt_count_notifIZIN.setVisible(false);
                } else {
                    txt_count_notifIZIN.setVisible(true);
                    try {
                        sfx_notifikasi();
                    } catch (IOException ex) {
                        Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (HitungJumlahActPendingAll() == 0) {
                    txt_count_notifACT.setVisible(false);
                } else {
                    txt_count_notifACT.setVisible(true);
                    try {
                        sfx_notifikasi();
                    } catch (IOException ex) {
                        Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        },
                0, 10, TimeUnit.SECONDS
        );
    }

    public void initTabelListIzin() {
        //set header table
        tableModelIzin.setColumnIdentifiers(DaftarListIzincolumNames);
        tabelListIzin.setModel(tableModelIzin);
        tabelListIzin.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelListIzin.setAutoResizeMode(tabelListIzin.AUTO_RESIZE_OFF);
        customTabelListIzin();
    }

    public void customTabelListIzin() {
        tabelListIzin.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabelListIzin.getColumnModel().getColumn(1).setPreferredWidth(125);
        tabelListIzin.getColumnModel().getColumn(2).setPreferredWidth(225);
        tabelListIzin.getColumnModel().getColumn(3).setPreferredWidth(150);
        tabelListIzin.getColumnModel().getColumn(4).setPreferredWidth(150);
        tabelListIzin.getColumnModel().getColumn(5).setPreferredWidth(250);
        tabelListIzin.getColumnModel().getColumn(6).setPreferredWidth(150);
        tabelListIzin.getColumnModel().getColumn(7).setPreferredWidth(150);
    }

    public void loadDaftarTabelListIzin() {
        tabelListIzin.setAutoResizeMode(tabelListIzin.AUTO_RESIZE_OFF);
        customTabelListIzin();
        try {
            // reset data di tabel
            // mengambil data barang dari server
            tableModelIzin.setRowCount(0);
            // kemudian menyimpannya ke objek list
            recordIzin = simDao.getListIzinAll();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_SIM ES : recordIzin) {
                // ambil nomor urut terakhir
                Object[] objects = new Object[DaftarListIzincolumNames.length];
                objects[0] = ES.getReq_izin_id();
                objects[1] = ES.getReq_izin_id_emp();
                objects[2] = ES.getReq_izin_cat();
                objects[3] = ES.getReq_izin_date_start();
                objects[4] = ES.getReq_izin_date_end();
                objects[5] = ES.getReq_izin_desc();
                objects[6] = ES.getReq_izin_created_date();
                objects[7] = ES.getReq_izin_status_approve();
                // tambahkan data barang ke dalam tabel
                tableModelIzin.addRow(objects);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
        }
        txt_izin_total_data.setText(String.valueOf(tableModelIzin.getRowCount()));
    }

    public void initTabelListAct() {
        //set header table
        tableModelAct.setColumnIdentifiers(DaftarListActcolumNames);
        tabelListAktivitas.setModel(tableModelAct);
        tabelListAktivitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelListAktivitas.setAutoResizeMode(tabelListAktivitas.AUTO_RESIZE_OFF);
        customTabelListAct();
    }

    public void customTabelListAct() {
        tabelListAktivitas.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabelListAktivitas.getColumnModel().getColumn(1).setPreferredWidth(125);
        tabelListAktivitas.getColumnModel().getColumn(2).setPreferredWidth(225);
        tabelListAktivitas.getColumnModel().getColumn(3).setPreferredWidth(150);
        tabelListAktivitas.getColumnModel().getColumn(4).setPreferredWidth(150);
        tabelListAktivitas.getColumnModel().getColumn(5).setPreferredWidth(250);
        tabelListAktivitas.getColumnModel().getColumn(6).setPreferredWidth(150);
        tabelListAktivitas.getColumnModel().getColumn(7).setPreferredWidth(150);
    }

    public void loadDaftarTabelListAct() {
        tabelListAktivitas.setAutoResizeMode(tabelListAktivitas.AUTO_RESIZE_OFF);
        customTabelListAct();
        try {
            // reset data di tabel
            // mengambil data barang dari server
            tableModelAct.setRowCount(0);
            // kemudian menyimpannya ke objek list
            recordActivity = simDao.getListActAll();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_SIM ES : recordActivity) {
                // ambil nomor urut terakhir
                Object[] objects = new Object[DaftarListActcolumNames.length];
                objects[0] = ES.getReq_act_id();
                objects[1] = ES.getReq_act_id_emp();
                objects[2] = ES.getReq_act_cat();
                objects[3] = ES.getReq_act_date_start();
                objects[4] = ES.getReq_act_date_end();
                objects[5] = ES.getReq_act_desc();
                objects[6] = ES.getReq_act_created_date();
                objects[7] = ES.getReq_act_status_approve();
                // tambahkan data barang ke dalam tabel
                tableModelAct.addRow(objects);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
        }
        txt_act_total_data.setText(String.valueOf(tableModelAct.getRowCount()));
    }

    private void init_Error_ID() {
        Entity_ErrorReport EER = new Entity_ErrorReport();
        try {
            String hasil = errorDao.GenerateErrorID(EER);
            if (hasil == null) {
                //do something
            } else {
                txt_error_id_sim.setText(hasil);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ClearFormError() {
        txt_error_title_sim.setText("");
        txt_error_desc_sim.setText("");
        dtError_Date_sim.setDate(null);
        txt_error_title_sim.requestFocus();
        cb_error_status_sim.setSelectedIndex(0);
    }

    public void initTabelError() {
        //set header table
        tableModelError.setColumnIdentifiers(DaftarErrorcolumNames);
        tabelDaftarError_Sim.setModel(tableModelError);
        tabelDaftarError_Sim.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelDaftarError_Sim.setAutoResizeMode(tabelDaftarError_Sim.AUTO_RESIZE_OFF);
        customTabelListError();
    }

    public void customTabelListError() {
        tabelDaftarError_Sim.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabelDaftarError_Sim.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabelDaftarError_Sim.getColumnModel().getColumn(2).setPreferredWidth(300);
        tabelDaftarError_Sim.getColumnModel().getColumn(3).setPreferredWidth(150);
        tabelDaftarError_Sim.getColumnModel().getColumn(4).setPreferredWidth(150);
        tabelDaftarError_Sim.getColumnModel().getColumn(5).setPreferredWidth(150);
    }

    private void loadDaftarError() {
        tabelDaftarError_Sim.setAutoResizeMode(tabelListAktivitas.AUTO_RESIZE_OFF);
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
        txtUsernameHrd.setText("");
        txtPasswordHrd.setText("");
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
        panelHRDDashboard = new javax.swing.JPanel();
        btnNotifHrd = new javax.swing.JLabel();
        btnMsgHrd = new javax.swing.JLabel();
        btnLogoutHrd = new javax.swing.JLabel();
        subpanelHrdDashboard = new javax.swing.JPanel();
        txt_izin_success = new javax.swing.JLabel();
        txt_izin_pending = new javax.swing.JLabel();
        txt_izin_rejected = new javax.swing.JLabel();
        txt_act_success = new javax.swing.JLabel();
        txt_act_pending = new javax.swing.JLabel();
        txt_act_rejected = new javax.swing.JLabel();
        lblDashboardTitle2 = new javax.swing.JLabel();
        lblDashboardTitleSIM = new javax.swing.JLabel();
        jSeparator19 = new javax.swing.JSeparator();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        subpanelHrdListIzin = new javax.swing.JPanel();
        lblSIMListIzinData = new javax.swing.JLabel();
        txt_izin_total_data = new javax.swing.JLabel();
        lblTransTotalDat = new javax.swing.JLabel();
        jSeparator16 = new javax.swing.JSeparator();
        tabelDaftarListIzinSP = new javax.swing.JScrollPane();
        tabelListIzin = new javax.swing.JTable();
        lblSIM2 = new javax.swing.JLabel();
        lblListTransactionPending = new javax.swing.JLabel();
        btn_approve_izin = new javax.swing.JButton();
        btn_reject_izin = new javax.swing.JButton();
        subpanelHrdListActivity = new javax.swing.JPanel();
        lblSIMListActivity = new javax.swing.JLabel();
        txt_act_total_data = new javax.swing.JLabel();
        lblAktivitasTotalData = new javax.swing.JLabel();
        jSeparator18 = new javax.swing.JSeparator();
        tabelDaftarListActivity = new javax.swing.JScrollPane();
        tabelListAktivitas = new javax.swing.JTable();
        lblSIM3 = new javax.swing.JLabel();
        lblListActivity = new javax.swing.JLabel();
        btn_approve_act = new javax.swing.JButton();
        btn_reject_act = new javax.swing.JButton();
        subpanelHrdErrorReport = new javax.swing.JPanel();
        btnSaveError_sim = new javax.swing.JLabel();
        btnClearError_sim = new javax.swing.JLabel();
        lbl_list_error_sim = new javax.swing.JLabel();
        cb_error_status_sim = new javax.swing.JComboBox<>();
        lbl_error_date_cas = new javax.swing.JLabel();
        dtError_Date_sim = new com.toedter.calendar.JDateChooser();
        lbl_error_status_cas = new javax.swing.JLabel();
        txt_error_desc_sim = new javax.swing.JTextArea();
        lbl_error_desc_cas = new javax.swing.JLabel();
        txt_error_title_sim = new javax.swing.JTextField();
        lbl_error_title_cas = new javax.swing.JLabel();
        txt_error_id_sim = new javax.swing.JTextField();
        lbl_error_id_cas = new javax.swing.JLabel();
        lblErrorReportTitleCas = new javax.swing.JLabel();
        lblErrorReportCasSubTitle1 = new javax.swing.JLabel();
        jSeparator14 = new javax.swing.JSeparator();
        tabelDaftarReportSP1 = new javax.swing.JScrollPane();
        tabelDaftarError_Sim = new javax.swing.JTable();
        lblErrorReportCasSubTitle = new javax.swing.JLabel();
        btn_delete_error_sim = new javax.swing.JLabel();
        jSeparator104 = new javax.swing.JSeparator();
        menuHrdErrorReport = new javax.swing.JPanel();
        iconHrdErrorReport = new javax.swing.JLabel();
        jSeparator99 = new javax.swing.JSeparator();
        menuHrdListActivity = new javax.swing.JPanel();
        iconHrdListActivity = new javax.swing.JLabel();
        txt_count_notifACT = new javax.swing.JTextField();
        jSeparator100 = new javax.swing.JSeparator();
        menuHrdListIzin = new javax.swing.JPanel();
        iconHrdListIzin = new javax.swing.JLabel();
        txt_count_notifIZIN = new javax.swing.JTextField();
        jSeparator101 = new javax.swing.JSeparator();
        menuHrdDashboard = new javax.swing.JPanel();
        iconHrdDashboard = new javax.swing.JLabel();
        jSeparator102 = new javax.swing.JSeparator();
        btnChangeStateHrd = new javax.swing.JButton();
        btnHideIPHrd = new javax.swing.JButton();
        btnShowIPHrd = new javax.swing.JButton();
        txtServerTimeHrd = new javax.swing.JLabel();
        txt_stateBigHrd = new javax.swing.JLabel();
        lblStateBigHrd = new javax.swing.JLabel();
        lblStateHrd = new javax.swing.JLabel();
        txt_stateHrd = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        txtHrdNama = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        iconAdminHrd = new javax.swing.JLabel();
        bgHrd = new javax.swing.JLabel();
        panelKilledbyServer = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        panelHRDLogin = new javax.swing.JPanel();
        txtUsernameHrd = new javax.swing.JTextField();
        txtPasswordHrd = new javax.swing.JPasswordField();
        btnLoginHrd = new javax.swing.JButton();
        btnResetHrd = new javax.swing.JButton();
        panelLoginHrd = new javax.swing.JLabel();
        bgClientHRD2 = new javax.swing.JLabel();
        panelHRDLandingPage = new javax.swing.JPanel();
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

        panelHRDDashboard.setAutoscrolls(true);
        panelHRDDashboard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnNotifHrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconNotif.png"))); // NOI18N
        btnNotifHrd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelHRDDashboard.add(btnNotifHrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(1039, 97, -1, -1));

        btnMsgHrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconMassage.png"))); // NOI18N
        btnMsgHrd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelHRDDashboard.add(btnMsgHrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 97, -1, -1));

        btnLogoutHrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/iconLogoutSIM.png"))); // NOI18N
        btnLogoutHrd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogoutHrd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogoutHrdMouseClicked(evt);
            }
        });
        panelHRDDashboard.add(btnLogoutHrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 97, -1, -1));

        subpanelHrdDashboard.setBackground(new java.awt.Color(255, 255, 255));
        subpanelHrdDashboard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_izin_success.setFont(new java.awt.Font("Gotham Ultra", 1, 48)); // NOI18N
        txt_izin_success.setForeground(new java.awt.Color(255, 255, 255));
        txt_izin_success.setText("0");
        subpanelHrdDashboard.add(txt_izin_success, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 150, -1, -1));

        txt_izin_pending.setFont(new java.awt.Font("Gotham Ultra", 1, 48)); // NOI18N
        txt_izin_pending.setForeground(new java.awt.Color(255, 255, 255));
        txt_izin_pending.setText("0");
        subpanelHrdDashboard.add(txt_izin_pending, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 150, -1, -1));

        txt_izin_rejected.setFont(new java.awt.Font("Gotham Ultra", 1, 48)); // NOI18N
        txt_izin_rejected.setForeground(new java.awt.Color(255, 255, 255));
        txt_izin_rejected.setText("0");
        subpanelHrdDashboard.add(txt_izin_rejected, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 150, -1, -1));

        txt_act_success.setFont(new java.awt.Font("Gotham Ultra", 1, 48)); // NOI18N
        txt_act_success.setForeground(new java.awt.Color(255, 255, 255));
        txt_act_success.setText("0");
        subpanelHrdDashboard.add(txt_act_success, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 370, -1, -1));

        txt_act_pending.setFont(new java.awt.Font("Gotham Ultra", 1, 48)); // NOI18N
        txt_act_pending.setForeground(new java.awt.Color(255, 255, 255));
        txt_act_pending.setText("0");
        subpanelHrdDashboard.add(txt_act_pending, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 370, -1, -1));

        txt_act_rejected.setFont(new java.awt.Font("Gotham Ultra", 1, 48)); // NOI18N
        txt_act_rejected.setForeground(new java.awt.Color(255, 255, 255));
        txt_act_rejected.setText("0");
        subpanelHrdDashboard.add(txt_act_rejected, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 370, -1, -1));

        lblDashboardTitle2.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblDashboardTitle2.setForeground(java.awt.Color.gray);
        lblDashboardTitle2.setText("Human Resource and Development");
        subpanelHrdDashboard.add(lblDashboardTitle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 53, -1, -1));

        lblDashboardTitleSIM.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblDashboardTitleSIM.setForeground(java.awt.Color.gray);
        lblDashboardTitleSIM.setText("DASHBOARD");
        subpanelHrdDashboard.add(lblDashboardTitleSIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        jSeparator19.setForeground(new java.awt.Color(241, 241, 241));
        subpanelHrdDashboard.add(jSeparator19, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 930, 20));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/iconActRejected.png"))); // NOI18N
        subpanelHrdDashboard.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 330, -1, -1));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/iconActPending.png"))); // NOI18N
        subpanelHrdDashboard.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 330, -1, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/iconActSuccess.png"))); // NOI18N
        subpanelHrdDashboard.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 330, -1, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/iconIzinSuccess.png"))); // NOI18N
        subpanelHrdDashboard.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/iconIzinPending.png"))); // NOI18N
        subpanelHrdDashboard.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 110, -1, -1));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/iconIzinRejected.png"))); // NOI18N
        subpanelHrdDashboard.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 110, -1, -1));

        panelHRDDashboard.add(subpanelHrdDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        subpanelHrdListIzin.setBackground(new java.awt.Color(255, 255, 255));
        subpanelHrdListIzin.setPreferredSize(new java.awt.Dimension(1010, 538));
        subpanelHrdListIzin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblSIMListIzinData.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblSIMListIzinData.setForeground(java.awt.Color.gray);
        lblSIMListIzinData.setText("LIST IZIN");
        subpanelHrdListIzin.add(lblSIMListIzinData, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        txt_izin_total_data.setFont(new java.awt.Font("Gotham Light", 1, 12)); // NOI18N
        txt_izin_total_data.setForeground(java.awt.Color.gray);
        txt_izin_total_data.setText("0");
        subpanelHrdListIzin.add(txt_izin_total_data, new org.netbeans.lib.awtextra.AbsoluteConstraints(105, 490, -1, -1));

        lblTransTotalDat.setFont(new java.awt.Font("Gotham Light", 0, 12)); // NOI18N
        lblTransTotalDat.setForeground(java.awt.Color.gray);
        lblTransTotalDat.setText("Total Data :");
        subpanelHrdListIzin.add(lblTransTotalDat, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 490, -1, -1));

        jSeparator16.setForeground(new java.awt.Color(241, 241, 241));
        subpanelHrdListIzin.add(jSeparator16, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 950, 20));

        tabelDaftarListIzinSP.setBackground(new java.awt.Color(241, 241, 241));
        tabelDaftarListIzinSP.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        tabelDaftarListIzinSP.setAutoscrolls(true);
        tabelDaftarListIzinSP.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tabelListIzin.setBackground(new java.awt.Color(240, 240, 240));
        tabelListIzin.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Request ID", "Request By", "Category", "Date Start", "Date End", "Description", "Created Date", "Status Approve"
            }
        ));
        tabelListIzin.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabelListIzin.setAutoscrolls(false);
        tabelListIzin.setGridColor(new java.awt.Color(241, 241, 241));
        tabelListIzin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelListIzinMouseClicked(evt);
            }
        });
        tabelDaftarListIzinSP.setViewportView(tabelListIzin);
        if (tabelListIzin.getColumnModel().getColumnCount() > 0) {
            tabelListIzin.getColumnModel().getColumn(0).setPreferredWidth(150);
            tabelListIzin.getColumnModel().getColumn(1).setPreferredWidth(125);
            tabelListIzin.getColumnModel().getColumn(2).setPreferredWidth(225);
            tabelListIzin.getColumnModel().getColumn(3).setPreferredWidth(150);
            tabelListIzin.getColumnModel().getColumn(4).setPreferredWidth(150);
            tabelListIzin.getColumnModel().getColumn(5).setPreferredWidth(250);
            tabelListIzin.getColumnModel().getColumn(6).setPreferredWidth(150);
            tabelListIzin.getColumnModel().getColumn(7).setPreferredWidth(150);
        }

        subpanelHrdListIzin.add(tabelDaftarListIzinSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 950, 320));

        lblSIM2.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblSIM2.setForeground(java.awt.Color.gray);
        lblSIM2.setText("Human Resource and Development");
        subpanelHrdListIzin.add(lblSIM2, new org.netbeans.lib.awtextra.AbsoluteConstraints(205, 53, -1, -1));

        lblListTransactionPending.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblListTransactionPending.setForeground(java.awt.Color.gray);
        lblListTransactionPending.setText("List Izin ");
        subpanelHrdListIzin.add(lblListTransactionPending, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, -1));

        btn_approve_izin.setBackground(new java.awt.Color(0, 102, 51));
        btn_approve_izin.setForeground(new java.awt.Color(255, 255, 255));
        btn_approve_izin.setText("APPROVE");
        btn_approve_izin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_approve_izinMouseClicked(evt);
            }
        });
        subpanelHrdListIzin.add(btn_approve_izin, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 110, 100, -1));

        btn_reject_izin.setBackground(new java.awt.Color(153, 0, 0));
        btn_reject_izin.setForeground(new java.awt.Color(255, 255, 255));
        btn_reject_izin.setText("REJECT");
        btn_reject_izin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_reject_izinMouseClicked(evt);
            }
        });
        subpanelHrdListIzin.add(btn_reject_izin, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 110, 90, -1));

        panelHRDDashboard.add(subpanelHrdListIzin, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        subpanelHrdListActivity.setBackground(new java.awt.Color(255, 255, 255));
        subpanelHrdListActivity.setPreferredSize(new java.awt.Dimension(1010, 538));
        subpanelHrdListActivity.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblSIMListActivity.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblSIMListActivity.setForeground(java.awt.Color.gray);
        lblSIMListActivity.setText("LIST ACTIVITY");
        subpanelHrdListActivity.add(lblSIMListActivity, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        txt_act_total_data.setFont(new java.awt.Font("Gotham Light", 1, 12)); // NOI18N
        txt_act_total_data.setForeground(java.awt.Color.gray);
        txt_act_total_data.setText("0");
        subpanelHrdListActivity.add(txt_act_total_data, new org.netbeans.lib.awtextra.AbsoluteConstraints(105, 490, -1, -1));

        lblAktivitasTotalData.setFont(new java.awt.Font("Gotham Light", 0, 12)); // NOI18N
        lblAktivitasTotalData.setForeground(java.awt.Color.gray);
        lblAktivitasTotalData.setText("Total Data :");
        subpanelHrdListActivity.add(lblAktivitasTotalData, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 490, -1, -1));

        jSeparator18.setForeground(new java.awt.Color(241, 241, 241));
        subpanelHrdListActivity.add(jSeparator18, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 950, 20));

        tabelDaftarListActivity.setBackground(new java.awt.Color(241, 241, 241));
        tabelDaftarListActivity.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        tabelDaftarListActivity.setAutoscrolls(true);
        tabelDaftarListActivity.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tabelListAktivitas.setBackground(new java.awt.Color(240, 240, 240));
        tabelListAktivitas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Request ID", "Request By", "Category", "Date Start", "Date End", "Description", "Created Date", "Status Approve"
            }
        ));
        tabelListAktivitas.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabelListAktivitas.setAutoscrolls(false);
        tabelListAktivitas.setGridColor(new java.awt.Color(241, 241, 241));
        tabelListAktivitas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelListAktivitasMouseClicked(evt);
            }
        });
        tabelDaftarListActivity.setViewportView(tabelListAktivitas);
        if (tabelListAktivitas.getColumnModel().getColumnCount() > 0) {
            tabelListAktivitas.getColumnModel().getColumn(0).setPreferredWidth(150);
            tabelListAktivitas.getColumnModel().getColumn(1).setPreferredWidth(125);
            tabelListAktivitas.getColumnModel().getColumn(2).setPreferredWidth(225);
            tabelListAktivitas.getColumnModel().getColumn(3).setPreferredWidth(150);
            tabelListAktivitas.getColumnModel().getColumn(4).setPreferredWidth(150);
            tabelListAktivitas.getColumnModel().getColumn(5).setPreferredWidth(250);
            tabelListAktivitas.getColumnModel().getColumn(6).setPreferredWidth(150);
            tabelListAktivitas.getColumnModel().getColumn(7).setPreferredWidth(150);
        }

        subpanelHrdListActivity.add(tabelDaftarListActivity, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 950, 320));

        lblSIM3.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblSIM3.setForeground(java.awt.Color.gray);
        lblSIM3.setText("Human Resource and Development");
        subpanelHrdListActivity.add(lblSIM3, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 53, -1, -1));

        lblListActivity.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblListActivity.setForeground(java.awt.Color.gray);
        lblListActivity.setText("List Izin ");
        subpanelHrdListActivity.add(lblListActivity, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, -1));

        btn_approve_act.setBackground(new java.awt.Color(0, 102, 51));
        btn_approve_act.setForeground(new java.awt.Color(255, 255, 255));
        btn_approve_act.setText("APPROVE");
        btn_approve_act.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_approve_actMouseClicked(evt);
            }
        });
        subpanelHrdListActivity.add(btn_approve_act, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 110, 100, -1));

        btn_reject_act.setBackground(new java.awt.Color(153, 0, 0));
        btn_reject_act.setForeground(new java.awt.Color(255, 255, 255));
        btn_reject_act.setText("REJECT");
        btn_reject_act.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_reject_actMouseClicked(evt);
            }
        });
        subpanelHrdListActivity.add(btn_reject_act, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 110, 90, -1));

        panelHRDDashboard.add(subpanelHrdListActivity, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        subpanelHrdErrorReport.setBackground(new java.awt.Color(255, 255, 255));
        subpanelHrdErrorReport.setPreferredSize(new java.awt.Dimension(1010, 538));
        subpanelHrdErrorReport.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSaveError_sim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconSave.png"))); // NOI18N
        btnSaveError_sim.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSaveError_sim.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveError_simMouseClicked(evt);
            }
        });
        subpanelHrdErrorReport.add(btnSaveError_sim, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 410, -1, -1));

        btnClearError_sim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconClear.png"))); // NOI18N
        btnClearError_sim.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClearError_sim.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClearError_simMouseClicked(evt);
            }
        });
        subpanelHrdErrorReport.add(btnClearError_sim, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 410, -1, -1));

        lbl_list_error_sim.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_list_error_sim.setForeground(java.awt.Color.gray);
        lbl_list_error_sim.setText("Report List");
        subpanelHrdErrorReport.add(lbl_list_error_sim, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 100, -1, -1));

        cb_error_status_sim.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Status -", "Currently Added", "On Progress", "Done" }));
        subpanelHrdErrorReport.add(cb_error_status_sim, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 410, 180, -1));

        lbl_error_date_cas.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_date_cas.setText("Date");
        subpanelHrdErrorReport.add(lbl_error_date_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, -1, -1));
        subpanelHrdErrorReport.add(dtError_Date_sim, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 360, 180, -1));

        lbl_error_status_cas.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_status_cas.setText("Status");
        subpanelHrdErrorReport.add(lbl_error_status_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 410, -1, -1));

        txt_error_desc_sim.setBackground(new java.awt.Color(240, 240, 240));
        txt_error_desc_sim.setColumns(20);
        txt_error_desc_sim.setRows(5);
        subpanelHrdErrorReport.add(txt_error_desc_sim, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 240, 180, -1));

        lbl_error_desc_cas.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_desc_cas.setText("Description");
        subpanelHrdErrorReport.add(lbl_error_desc_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, -1, -1));
        subpanelHrdErrorReport.add(txt_error_title_sim, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 188, 180, -1));

        lbl_error_title_cas.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_title_cas.setText("Title");
        subpanelHrdErrorReport.add(lbl_error_title_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, -1));

        txt_error_id_sim.setEnabled(false);
        subpanelHrdErrorReport.add(txt_error_id_sim, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 180, -1));

        lbl_error_id_cas.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_id_cas.setText("Report ID");
        subpanelHrdErrorReport.add(lbl_error_id_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));

        lblErrorReportTitleCas.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblErrorReportTitleCas.setForeground(java.awt.Color.gray);
        lblErrorReportTitleCas.setText("ERROR REPORT");
        subpanelHrdErrorReport.add(lblErrorReportTitleCas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        lblErrorReportCasSubTitle1.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblErrorReportCasSubTitle1.setForeground(java.awt.Color.gray);
        lblErrorReportCasSubTitle1.setText("Add Report");
        subpanelHrdErrorReport.add(lblErrorReportCasSubTitle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        jSeparator14.setForeground(new java.awt.Color(241, 241, 241));
        subpanelHrdErrorReport.add(jSeparator14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 950, 20));

        tabelDaftarReportSP1.setBackground(new java.awt.Color(241, 241, 241));
        tabelDaftarReportSP1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        tabelDaftarReportSP1.setAutoscrolls(true);
        tabelDaftarReportSP1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tabelDaftarError_Sim.setBackground(new java.awt.Color(240, 240, 240));
        tabelDaftarError_Sim.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelDaftarError_Sim.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tabelDaftarError_Sim.setAutoscrolls(false);
        tabelDaftarError_Sim.setGridColor(new java.awt.Color(241, 241, 241));
        tabelDaftarError_Sim.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelDaftarError_SimMouseClicked(evt);
            }
        });
        tabelDaftarReportSP1.setViewportView(tabelDaftarError_Sim);
        if (tabelDaftarError_Sim.getColumnModel().getColumnCount() > 0) {
            tabelDaftarError_Sim.getColumnModel().getColumn(0).setPreferredWidth(150);
            tabelDaftarError_Sim.getColumnModel().getColumn(1).setPreferredWidth(150);
            tabelDaftarError_Sim.getColumnModel().getColumn(2).setPreferredWidth(300);
            tabelDaftarError_Sim.getColumnModel().getColumn(3).setPreferredWidth(150);
            tabelDaftarError_Sim.getColumnModel().getColumn(4).setPreferredWidth(150);
            tabelDaftarError_Sim.getColumnModel().getColumn(5).setPreferredWidth(150);
        }

        subpanelHrdErrorReport.add(tabelDaftarReportSP1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 130, 580, 260));

        lblErrorReportCasSubTitle.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblErrorReportCasSubTitle.setForeground(java.awt.Color.gray);
        lblErrorReportCasSubTitle.setText("Sistem Informasi Manajemen");
        subpanelHrdErrorReport.add(lblErrorReportCasSubTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(335, 53, -1, -1));

        btn_delete_error_sim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/btnDelete.png"))); // NOI18N
        btn_delete_error_sim.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_delete_error_sim.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_delete_error_simMouseClicked(evt);
            }
        });
        subpanelHrdErrorReport.add(btn_delete_error_sim, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 87, -1, -1));

        panelHRDDashboard.add(subpanelHrdErrorReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));
        panelHRDDashboard.add(jSeparator104, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 170, 10));

        menuHrdErrorReport.setBackground(new java.awt.Color(241, 241, 241));
        menuHrdErrorReport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuHrdErrorReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuHrdErrorReportMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuHrdErrorReportMouseExited(evt);
            }
        });
        menuHrdErrorReport.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconHrdErrorReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/IconErrorReport.png"))); // NOI18N
        iconHrdErrorReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconHrdErrorReportMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconHrdErrorReportMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconHrdErrorReportMouseExited(evt);
            }
        });
        menuHrdErrorReport.add(iconHrdErrorReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        panelHRDDashboard.add(menuHrdErrorReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 395, 170, 30));
        panelHRDDashboard.add(jSeparator99, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 170, 10));

        menuHrdListActivity.setBackground(new java.awt.Color(241, 241, 241));
        menuHrdListActivity.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuHrdListActivity.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuHrdListActivityMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuHrdListActivityMouseExited(evt);
            }
        });
        menuHrdListActivity.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconHrdListActivity.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/hrd/iconListActivity.png"))); // NOI18N
        iconHrdListActivity.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconHrdListActivityMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconHrdListActivityMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconHrdListActivityMouseExited(evt);
            }
        });
        menuHrdListActivity.add(iconHrdListActivity, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        txt_count_notifACT.setEditable(false);
        txt_count_notifACT.setBackground(new java.awt.Color(32, 193, 237));
        txt_count_notifACT.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txt_count_notifACT.setForeground(new java.awt.Color(255, 255, 255));
        txt_count_notifACT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_count_notifACT.setText("0");
        txt_count_notifACT.setBorder(null);
        menuHrdListActivity.add(txt_count_notifACT, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 7, 20, 20));

        panelHRDDashboard.add(menuHrdListActivity, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 355, 170, 30));
        panelHRDDashboard.add(jSeparator100, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 170, 10));

        menuHrdListIzin.setBackground(new java.awt.Color(241, 241, 241));
        menuHrdListIzin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuHrdListIzin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuHrdListIzinMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuHrdListIzinMouseExited(evt);
            }
        });
        menuHrdListIzin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconHrdListIzin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/hrd/iconListIzin.png"))); // NOI18N
        iconHrdListIzin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconHrdListIzinMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconHrdListIzinMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconHrdListIzinMouseExited(evt);
            }
        });
        menuHrdListIzin.add(iconHrdListIzin, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 9, 70, -1));

        txt_count_notifIZIN.setEditable(false);
        txt_count_notifIZIN.setBackground(new java.awt.Color(32, 193, 237));
        txt_count_notifIZIN.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txt_count_notifIZIN.setForeground(new java.awt.Color(255, 255, 255));
        txt_count_notifIZIN.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_count_notifIZIN.setText("0");
        txt_count_notifIZIN.setBorder(null);
        menuHrdListIzin.add(txt_count_notifIZIN, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 5, 20, 20));

        panelHRDDashboard.add(menuHrdListIzin, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 315, 170, 30));
        panelHRDDashboard.add(jSeparator101, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 170, 10));

        menuHrdDashboard.setBackground(new java.awt.Color(241, 241, 241));
        menuHrdDashboard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuHrdDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuHrdDashboardMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuHrdDashboardMouseExited(evt);
            }
        });
        menuHrdDashboard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconHrdDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/IconDashboard.png"))); // NOI18N
        iconHrdDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconHrdDashboardMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconHrdDashboardMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconHrdDashboardMouseExited(evt);
            }
        });
        menuHrdDashboard.add(iconHrdDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 7, -1, -1));

        panelHRDDashboard.add(menuHrdDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 275, 170, 30));
        panelHRDDashboard.add(jSeparator102, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 170, 10));

        btnChangeStateHrd.setText("Change State");
        btnChangeStateHrd.setToolTipText("Click to Change State");
        btnChangeStateHrd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnChangeStateHrd.setMaximumSize(new java.awt.Dimension(73, 23));
        btnChangeStateHrd.setMinimumSize(new java.awt.Dimension(74, 23));
        btnChangeStateHrd.setPreferredSize(new java.awt.Dimension(78, 23));
        btnChangeStateHrd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnChangeStateHrdMouseClicked(evt);
            }
        });
        panelHRDDashboard.add(btnChangeStateHrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(83, 230, 100, -1));

        btnHideIPHrd.setText("Hide IP");
        btnHideIPHrd.setToolTipText("Click to Hide IP");
        btnHideIPHrd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHideIPHrd.setMaximumSize(new java.awt.Dimension(73, 23));
        btnHideIPHrd.setMinimumSize(new java.awt.Dimension(74, 23));
        btnHideIPHrd.setPreferredSize(new java.awt.Dimension(73, 23));
        btnHideIPHrd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHideIPHrdMouseClicked(evt);
            }
        });
        panelHRDDashboard.add(btnHideIPHrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 230, -1, -1));

        btnShowIPHrd.setText("Show IP");
        btnShowIPHrd.setToolTipText("Click to show IP");
        btnShowIPHrd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnShowIPHrd.setMaximumSize(new java.awt.Dimension(73, 23));
        btnShowIPHrd.setMinimumSize(new java.awt.Dimension(73, 23));
        btnShowIPHrd.setPreferredSize(new java.awt.Dimension(73, 23));
        btnShowIPHrd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnShowIPHrdMouseClicked(evt);
            }
        });
        panelHRDDashboard.add(btnShowIPHrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 230, -1, -1));

        txtServerTimeHrd.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        txtServerTimeHrd.setText("SERVER TIME");
        panelHRDDashboard.add(txtServerTimeHrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 693, -1, -1));

        txt_stateBigHrd.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        txt_stateBigHrd.setText("SERVER STATE");
        panelHRDDashboard.add(txt_stateBigHrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 693, -1, -1));

        lblStateBigHrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OnlineState.png"))); // NOI18N
        panelHRDDashboard.add(lblStateBigHrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 693, -1, -1));

        lblStateHrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OnlineState.png"))); // NOI18N
        panelHRDDashboard.add(lblStateHrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(78, 208, -1, -1));

        txt_stateHrd.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        txt_stateHrd.setText("Online");
        panelHRDDashboard.add(txt_stateHrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 210, -1, -1));

        jLabel32.setFont(new java.awt.Font("Gotham Medium", 1, 11)); // NOI18N
        jLabel32.setText("Keyko");
        panelHRDDashboard.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(595, 695, -1, -1));

        jLabel33.setFont(new java.awt.Font("Gotham Light", 2, 11)); // NOI18N
        jLabel33.setText("powered by");
        panelHRDDashboard.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 695, -1, -1));

        jLabel34.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        jLabel34.setText("NOB Tech - ");
        panelHRDDashboard.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(465, 695, -1, -1));

        txtHrdNama.setFont(new java.awt.Font("Gotham Medium", 1, 12)); // NOI18N
        txtHrdNama.setText("HRD");
        panelHRDDashboard.add(txtHrdNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(79, 190, -1, -1));

        jLabel35.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        jLabel35.setText("Hello, ");
        panelHRDDashboard.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 170, -1, -1));

        iconAdminHrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/AdminLogo.png"))); // NOI18N
        panelHRDDashboard.add(iconAdminHrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        bgHrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/hrd/panelDashboardHRD.png"))); // NOI18N
        panelHRDDashboard.add(bgHrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelHRDDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 721));

        panelKilledbyServer.setBackground(new java.awt.Color(255, 255, 255));
        panelKilledbyServer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/hrd/HrdServerDisconnect.png"))); // NOI18N
        panelKilledbyServer.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelKilledbyServer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 720));

        panelHRDLogin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelHRDLogin.add(txtUsernameHrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 380, 230, 40));
        panelHRDLogin.add(txtPasswordHrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 470, 230, 40));

        btnLoginHrd.setText("LOGIN");
        btnLoginHrd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLoginHrdMouseClicked(evt);
            }
        });
        panelHRDLogin.add(btnLoginHrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 550, -1, -1));

        btnResetHrd.setText("CANCEL");
        btnResetHrd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnResetHrdMouseClicked(evt);
            }
        });
        panelHRDLogin.add(btnResetHrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 550, -1, -1));

        panelLoginHrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/hrd/panelLoginHRD.png"))); // NOI18N
        panelHRDLogin.add(panelLoginHrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 120, -1, -1));

        bgClientHRD2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/hrd/bgLoginHRD.png"))); // NOI18N
        panelHRDLogin.add(bgClientHRD2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelHRDLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 721));

        panelHRDLandingPage.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconHomeHRD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconHome.png"))); // NOI18N
        iconHomeHRD.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconHomeHRD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconHomeHRDMouseClicked(evt);
            }
        });
        panelHRDLandingPage.add(iconHomeHRD, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        bgHRDLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/hrd/logoHRD.png"))); // NOI18N
        bgHRDLogo.setToolTipText("");
        bgHRDLogo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bgHRDLogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bgHRDLogoMouseClicked(evt);
            }
        });
        panelHRDLandingPage.add(bgHRDLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 200, -1, -1));

        bgClientHRD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/hrd/bgLandingPageHRD.png"))); // NOI18N
        panelHRDLandingPage.add(bgClientHRD, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelHRDLandingPage, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 720));

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

    private void btnLoginHrdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLoginHrdMouseClicked
        // TODO add your handling code here:
        String lettercode = txtUsernameHrd.getText().substring(0, 3);
        System.out.println(lettercode);
        if (txtUsernameHrd.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please Input Your Username", "Sign In Error", JOptionPane.ERROR_MESSAGE);
            txtUsernameHrd.requestFocus();
        } else if (txtPasswordHrd.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please Input Your Password", "Sign In Error", JOptionPane.ERROR_MESSAGE);
            txtPasswordHrd.requestFocus();
        } else if (txtUsernameHrd.getText().substring(0, 3).equals("HRD")) {
            Entity_Signin ESI = new Entity_Signin();
            ESI.setUsername(txtUsernameHrd.getText());
            ESI.setPassword(txtPasswordHrd.getText());
            ESI.setStatus("Active");
            ESI.setDlc("HRD");
            try {
                int hasil = loginDao.Login(ESI);
                if (hasil == 1) {
                    JOptionPane.showMessageDialog(null, "Sign In Success", "Sign-In Success", JOptionPane.INFORMATION_MESSAGE);
                    Mode3();
                    HitungJumlahActPendingAll();
                    HitungJumlahActRejectedAll();
                    HitungJumlahActSuksesAll();
                    HitungJumlahIzinPendingAll();
                    HitungJumlahIzinRejectedAll();
                    HitungJumlahIzinSuksesAll();
                    CekNotifikasi10Sec();
                    SetHrdStatusOnline();
                    CekStatus5Sec();
                    last_session = 3;
                    setIPHRD();
                } else {
                    JOptionPane.showMessageDialog(null, "Password or Username Does Not Match", "Sign-In Error", JOptionPane.ERROR_MESSAGE);
                    txtUsernameHrd.setText("");
                    txtPasswordHrd.setText("");
                }
            } catch (RemoteException ex) {
                Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Sorry, You Have No Access to HRD Area", "Sign In Error", JOptionPane.ERROR_MESSAGE);
            txtPasswordHrd.requestFocus();
            txtUsernameHrd.setText("");
            txtPasswordHrd.setText("");
        }

        whoisonline = txtUsernameHrd.getText();
        Entity_Signin ESI = new Entity_Signin();
        ESI.setUsername(whoisonline);
        try {
            String hasil = loginDao.CekNamaOnline(ESI);
            txtHrdNama.setText(hasil);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientCashier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnLoginHrdMouseClicked

    private void btnResetHrdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetHrdMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnResetHrdMouseClicked

    private void btnLogoutHrdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutHrdMouseClicked
        // TODO add your handling code here:
        Object[] options = {"LogOut", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Are you sure to Logout ?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                Mode2();
                clearFormLogin();
                SetHrdStatusOffline();
                ses.shutdown();
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btnLogoutHrdMouseClicked

    private void iconHrdErrorReportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconHrdErrorReportMouseClicked
        // TODO add your handling code here:
        Mode8();
        init_Error_ID();
        initTabelError();
        loadDaftarError();
        last_session = 8;
    }//GEN-LAST:event_iconHrdErrorReportMouseClicked

    private void iconHrdErrorReportMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconHrdErrorReportMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuHrdErrorReport.setBackground(skyBlue);
    }//GEN-LAST:event_iconHrdErrorReportMouseEntered

    private void iconHrdErrorReportMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconHrdErrorReportMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuHrdErrorReport.setBackground(lightGray);
    }//GEN-LAST:event_iconHrdErrorReportMouseExited

    private void menuHrdErrorReportMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuHrdErrorReportMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuHrdErrorReport.setBackground(skyBlue);
    }//GEN-LAST:event_menuHrdErrorReportMouseEntered

    private void menuHrdErrorReportMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuHrdErrorReportMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuHrdErrorReport.setBackground(lightGray);
    }//GEN-LAST:event_menuHrdErrorReportMouseExited

    private void iconHrdListActivityMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconHrdListActivityMouseClicked
        // TODO add your handling code here:
        Mode6();
        initTabelListAct();
        loadDaftarTabelListAct();
        last_session = 6;
    }//GEN-LAST:event_iconHrdListActivityMouseClicked

    private void iconHrdListActivityMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconHrdListActivityMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuHrdListActivity.setBackground(skyBlue);
    }//GEN-LAST:event_iconHrdListActivityMouseEntered

    private void iconHrdListActivityMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconHrdListActivityMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuHrdListActivity.setBackground(lightGray);
    }//GEN-LAST:event_iconHrdListActivityMouseExited

    private void menuHrdListActivityMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuHrdListActivityMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuHrdListActivity.setBackground(skyBlue);
    }//GEN-LAST:event_menuHrdListActivityMouseEntered

    private void menuHrdListActivityMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuHrdListActivityMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuHrdListActivity.setBackground(lightGray);
    }//GEN-LAST:event_menuHrdListActivityMouseExited

    private void iconHrdListIzinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconHrdListIzinMouseClicked
        // TODO add your handling code here:
        Mode4();
        initTabelListIzin();
        loadDaftarTabelListIzin();
        last_session = 4;
    }//GEN-LAST:event_iconHrdListIzinMouseClicked

    private void iconHrdListIzinMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconHrdListIzinMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuHrdListIzin.setBackground(skyBlue);
    }//GEN-LAST:event_iconHrdListIzinMouseEntered

    private void iconHrdListIzinMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconHrdListIzinMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuHrdListIzin.setBackground(lightGray);
    }//GEN-LAST:event_iconHrdListIzinMouseExited

    private void menuHrdListIzinMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuHrdListIzinMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuHrdListIzin.setBackground(skyBlue);
    }//GEN-LAST:event_menuHrdListIzinMouseEntered

    private void menuHrdListIzinMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuHrdListIzinMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuHrdListIzin.setBackground(lightGray);
    }//GEN-LAST:event_menuHrdListIzinMouseExited

    private void iconHrdDashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconHrdDashboardMouseClicked
        // TODO add your handling code here:
        Mode3();
        last_session = 3;
    }//GEN-LAST:event_iconHrdDashboardMouseClicked

    private void iconHrdDashboardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconHrdDashboardMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuHrdDashboard.setBackground(skyBlue);
    }//GEN-LAST:event_iconHrdDashboardMouseEntered

    private void iconHrdDashboardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconHrdDashboardMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuHrdDashboard.setBackground(lightGray);
    }//GEN-LAST:event_iconHrdDashboardMouseExited

    private void menuHrdDashboardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuHrdDashboardMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuHrdDashboard.setBackground(skyBlue);
    }//GEN-LAST:event_menuHrdDashboardMouseEntered

    private void menuHrdDashboardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuHrdDashboardMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuHrdDashboard.setBackground(lightGray);
    }//GEN-LAST:event_menuHrdDashboardMouseExited

    private void btnChangeStateHrdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChangeStateHrdMouseClicked
        // TODO add your handling code here:
        int hasilUpdateStatusHrd = 0;
        Object[] options = {"Online", "Idle", "Invisible", "Offline"};
        int reply = JOptionPane.showOptionDialog(null, "Choose your Client [Cashier] state : ", "State Chooser", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                if (txt_stateHrd.getText().equals("Online")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Online", "Warning Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int hasilSetWrhStatus = loginDao.UpdateStatusOnline(kodeSistem);
                        if (hasilSetWrhStatus == 1) {
                            txt_stateBigHrd.setText("Server is Well-Connected");
                            lblStateHrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OnlineState.png")));
                            lblStateBigHrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OnlineState.png")));
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case 1:
                if (txt_stateHrd.getText().equals("Idle")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Idle", "Warning Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int hasilSetHrdStatus = loginDao.UpdateStatusIdle(kodeSistem);
                        if (hasilSetHrdStatus == 1) {
                            txt_stateBigHrd.setText("Server is Well-Connected");
                            lblStateHrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/IdleState.png")));
                            lblStateBigHrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/IdleState.png")));
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case 2:
                if (txt_stateHrd.getText().equals("Invisible")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Invisible", "Warning Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int hasilSetHrdStatus = loginDao.UpdateStatusInvisible(kodeSistem);
                        if (hasilSetHrdStatus == 1) {
                            txt_stateBigHrd.setText("Server is Well-Connected");
                            lblStateHrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/InvisState.png")));
                            lblStateBigHrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/InvisState.png")));
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case 3:
                if (txt_stateHrd.getText().equals("Offline")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Offline", "Warning Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int hasilSetWrhStatus = loginDao.UpdateStatusDisconnect(kodeSistem);
                        if (hasilSetWrhStatus == 1) {

                            txt_stateBigHrd.setText("Server is Disconnected");
                            lblStateHrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OffState.png")));
                            lblStateBigHrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OffState.png")));
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
    }//GEN-LAST:event_btnChangeStateHrdMouseClicked

    private void btnHideIPHrdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHideIPHrdMouseClicked
        // TODO add your handling code here:
        whoisonline = txtUsernameHrd.getText();
//        System.out.println(whoisonline);
        Entity_Signin ESI = new Entity_Signin();
        ESI.setUsername(whoisonline);
        try {
            String hasil = loginDao.CekNamaOnline(ESI);
            txtHrdNama.setText(hasil);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        ShowIPModeWrh();
    }//GEN-LAST:event_btnHideIPHrdMouseClicked

    private void btnShowIPHrdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnShowIPHrdMouseClicked
        // TODO add your handling code here:
        txtHrdNama.setText(ip);
        HideIPModeWrh();
    }//GEN-LAST:event_btnShowIPHrdMouseClicked

    private void iconWarehouseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWarehouseMouseClicked
        // TODO add your handling code here:
        HomePageClientWarehouse HPC2 = new HomePageClientWarehouse();
        HPC2.setVisible(true);
        HPC2.Mode2();
        dispose();
    }//GEN-LAST:event_iconWarehouseMouseClicked

    private void tabelListIzinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelListIzinMouseClicked
        // TODO add your handling code here:
        baris = tabelListIzin.getSelectedRow();
        kolom = tabelListIzin.getSelectedColumn();
        dataTerpilihIzin = tabelListIzin.getValueAt(baris, 0).toString();
    }//GEN-LAST:event_tabelListIzinMouseClicked

    private void iconSIMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconSIMMouseClicked
        // TODO add your handling code here:
        Mode2();
    }//GEN-LAST:event_iconSIMMouseClicked

    private void btn_reject_izinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_reject_izinMouseClicked
        Object[] options = {"Yes", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Are you sure to Reject this Izin ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                if (dataTerpilihIzin == null) {
                    JOptionPane.showMessageDialog(null, "Choose Data Izin From Table by Clicking Table Row", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    Entity_SIM ES = new Entity_SIM();
                    ES.setReq_izin_id(dataTerpilihIzin);
                    try {
                        int hasilReject = simDao.updateRejectIzin(ES);
                        if (hasilReject == 1) {
                            JOptionPane.showMessageDialog(null, "Izin Rejected", "Reject Success", JOptionPane.INFORMATION_MESSAGE);
                            loadDaftarTabelListIzin();
                        } else {
                            evt.consume();
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(null, "Izin Can't be Rejected", "Reject Failed", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btn_reject_izinMouseClicked

    private void tabelListAktivitasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelListAktivitasMouseClicked
        // TODO add your handling code here:
        baris = tabelListAktivitas.getSelectedRow();
        kolom = tabelListAktivitas.getSelectedColumn();
        dataTerpilihActivity = tabelListAktivitas.getValueAt(baris, 0).toString();
    }//GEN-LAST:event_tabelListAktivitasMouseClicked

    private void btnSaveError_simMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveError_simMouseClicked
        // TODO add your handling code here:
        String error_added_by = null;

        Entity_Signup ESU = new Entity_Signup();
        ESU.setName(txtHrdNama.getText());
        try {
            error_added_by = errorDao.getIDAdminError(ESU);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }

        Entity_ErrorReport EER = new Entity_ErrorReport();
        EER.setError_id(txt_error_id_sim.getText());
        EER.setError_title(txt_error_title_sim.getText());
        EER.setError_desc(txt_error_desc_sim.getText());
        EER.setError_date(dtError_Date_sim.getDate());
        EER.setError_status((String) cb_error_status_sim.getSelectedItem());
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
    }//GEN-LAST:event_btnSaveError_simMouseClicked

    private void btnClearError_simMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClearError_simMouseClicked
        // TODO add your handling code here:
        ClearFormError();
    }//GEN-LAST:event_btnClearError_simMouseClicked

    private void tabelDaftarError_SimMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelDaftarError_SimMouseClicked
        // TODO add your handling code here:
        baris = tabelDaftarError_Sim.getSelectedRow();
        kolom = tabelDaftarError_Sim.getSelectedColumn();
        dataTerpilih = tabelDaftarError_Sim.getValueAt(baris, 0).toString();
    }//GEN-LAST:event_tabelDaftarError_SimMouseClicked

    private void btn_delete_error_simMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_delete_error_simMouseClicked
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
    }//GEN-LAST:event_btn_delete_error_simMouseClicked

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
        Mode1();
    }//GEN-LAST:event_iconHRDMouseClicked

    private void btn_approve_izinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_approve_izinMouseClicked
        // TODO add your handling code here:
        Object[] options = {"Yes", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Are you sure to Approve this Izin ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                if (dataTerpilihIzin == null) {
                    JOptionPane.showMessageDialog(null, "Choose Data Izin From Table by Clicking Table Row", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    Entity_SIM ES = new Entity_SIM();
                    ES.setReq_izin_id(dataTerpilihIzin);
                    try {
                        int hasilApprove = simDao.updateApproveIzin(ES);
                        if (hasilApprove == 1) {
                            JOptionPane.showMessageDialog(null, "Izin Approved", "Approve Success", JOptionPane.INFORMATION_MESSAGE);
                            loadDaftarTabelListIzin();
                        } else {
                            evt.consume();
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(null, "Izin Can't be Approved", "Approve Failed", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btn_approve_izinMouseClicked

    private void btn_approve_actMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_approve_actMouseClicked
        // TODO add your handling code here:
        Object[] options = {"Yes", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Are you sure to Approve this Activity ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                if (dataTerpilihActivity == null) {
                    JOptionPane.showMessageDialog(null, "Choose Data Activity From Table by Clicking Table Row", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    Entity_SIM ES = new Entity_SIM();
                    ES.setReq_act_id(dataTerpilihActivity);
                    try {
                        int hasilApprove = simDao.updateApproveAct(ES);
                        if (hasilApprove == 1) {
                            JOptionPane.showMessageDialog(null, "Activity Approved", "Approve Success", JOptionPane.INFORMATION_MESSAGE);
                            loadDaftarTabelListAct();
                        } else {
                            evt.consume();
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(null, "Activity Can't be Approved", "Approve Failed", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btn_approve_actMouseClicked

    private void btn_reject_actMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_reject_actMouseClicked
        // TODO add your handling code here:
        Object[] options = {"Yes", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Are you sure to Reject this Activity ?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                if (dataTerpilihActivity == null) {
                    JOptionPane.showMessageDialog(null, "Choose Data Activity From Table by Clicking Table Row", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    Entity_SIM ES = new Entity_SIM();
                    ES.setReq_act_id(dataTerpilihActivity);
                    try {
                        int hasilApprove = simDao.updateRejectAct(ES);
                        if (hasilApprove == 1) {
                            JOptionPane.showMessageDialog(null, "Activity Rejected", "Reject Success", JOptionPane.INFORMATION_MESSAGE);
                            loadDaftarTabelListAct();
                        } else {
                            evt.consume();
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(null, "Activity Can't be Rejected", "Reject Failed", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePageClientHRD.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btn_reject_actMouseClicked

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
            java.util.logging.Logger.getLogger(HomePageClientHRD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomePageClientHRD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomePageClientHRD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomePageClientHRD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomePageClientHRD().setVisible(true);
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
    private javax.swing.JLabel bgHrd;
    private javax.swing.JLabel bgLandingPage;
    private javax.swing.JLabel bgLogoNOB;
    private javax.swing.JButton btnChangeStateHrd;
    private javax.swing.JLabel btnClearError_sim;
    private javax.swing.JButton btnHideIPHrd;
    private javax.swing.JButton btnLoginHrd;
    private javax.swing.JLabel btnLogoutHrd;
    private javax.swing.JLabel btnMsgHrd;
    private javax.swing.JLabel btnNotifHrd;
    private javax.swing.JButton btnResetHrd;
    private javax.swing.JLabel btnSaveError_sim;
    private javax.swing.JButton btnShowIPHrd;
    private javax.swing.JButton btn_approve_act;
    private javax.swing.JButton btn_approve_izin;
    private javax.swing.JLabel btn_delete_error_sim;
    private javax.swing.JButton btn_reject_act;
    private javax.swing.JButton btn_reject_izin;
    private javax.swing.JButton btn_trans_clearall1;
    private javax.swing.JButton btn_trans_process1;
    private javax.swing.ButtonGroup buttongroup_Gender;
    private javax.swing.JComboBox<String> cb_error_status_sim;
    private com.toedter.calendar.JDateChooser dtError_Date_sim;
    private javax.swing.JLabel iconAdminHrd;
    private javax.swing.JLabel iconBO;
    private javax.swing.JLabel iconBOBig;
    private javax.swing.JLabel iconCashier;
    private javax.swing.JLabel iconFO;
    private javax.swing.JLabel iconFOBig;
    private javax.swing.JLabel iconHRD;
    private javax.swing.JLabel iconHomeHRD;
    private javax.swing.JLabel iconHrdDashboard;
    private javax.swing.JLabel iconHrdErrorReport;
    private javax.swing.JLabel iconHrdListActivity;
    private javax.swing.JLabel iconHrdListIzin;
    private javax.swing.JLabel iconKeuangan;
    private javax.swing.JLabel iconManagement;
    private javax.swing.JLabel iconSIM;
    private javax.swing.JLabel iconSIMFO;
    private javax.swing.JLabel iconWarehouse;
    private javax.swing.JInternalFrame inframeQRCode;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JSeparator jSeparator100;
    private javax.swing.JSeparator jSeparator101;
    private javax.swing.JSeparator jSeparator102;
    private javax.swing.JSeparator jSeparator104;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator99;
    private javax.swing.JLabel lblAktivitasTotalData;
    private javax.swing.JLabel lblDashboardTitle2;
    private javax.swing.JLabel lblDashboardTitleSIM;
    private javax.swing.JLabel lblErrorReportCasSubTitle;
    private javax.swing.JLabel lblErrorReportCasSubTitle1;
    private javax.swing.JLabel lblErrorReportTitleCas;
    private javax.swing.JLabel lblListActivity;
    private javax.swing.JLabel lblListTransactionPending;
    private javax.swing.JLabel lblSIM2;
    private javax.swing.JLabel lblSIM3;
    private javax.swing.JLabel lblSIMListActivity;
    private javax.swing.JLabel lblSIMListIzinData;
    private javax.swing.JLabel lblStateBigHrd;
    private javax.swing.JLabel lblStateHrd;
    private javax.swing.JLabel lblTransInvoice_Confirm;
    private javax.swing.JLabel lblTransTotalDat;
    private javax.swing.JLabel lbl_error_date_cas;
    private javax.swing.JLabel lbl_error_desc_cas;
    private javax.swing.JLabel lbl_error_id_cas;
    private javax.swing.JLabel lbl_error_status_cas;
    private javax.swing.JLabel lbl_error_title_cas;
    private javax.swing.JLabel lbl_list_error_sim;
    private javax.swing.JPanel menuHrdDashboard;
    private javax.swing.JPanel menuHrdErrorReport;
    private javax.swing.JPanel menuHrdListActivity;
    private javax.swing.JPanel menuHrdListIzin;
    private javax.swing.JPanel panelBackOffice;
    private javax.swing.JPanel panelDepartment;
    private javax.swing.JPanel panelFrontOffice;
    private javax.swing.JPanel panelHRDDashboard;
    private javax.swing.JPanel panelHRDLandingPage;
    private javax.swing.JPanel panelHRDLogin;
    private javax.swing.JPanel panelKilledbyServer;
    private javax.swing.JPanel panelLandingPage;
    private javax.swing.JLabel panelLoginHrd;
    private javax.swing.JPanel panelQRCode_trans;
    private javax.swing.JPanel subpanelHrdDashboard;
    private javax.swing.JPanel subpanelHrdErrorReport;
    private javax.swing.JPanel subpanelHrdListActivity;
    private javax.swing.JPanel subpanelHrdListIzin;
    private javax.swing.JTable tabelDaftarError_Sim;
    private javax.swing.JScrollPane tabelDaftarListActivity;
    private javax.swing.JScrollPane tabelDaftarListIzinSP;
    private javax.swing.JScrollPane tabelDaftarReportSP1;
    private javax.swing.JTable tabelListAktivitas;
    private javax.swing.JTable tabelListIzin;
    private javax.swing.JLabel txtHrdNama;
    private javax.swing.JPasswordField txtPasswordHrd;
    private javax.swing.JLabel txtServerTimeHrd;
    private javax.swing.JTextField txtUsernameHrd;
    private javax.swing.JLabel txt_act_pending;
    private javax.swing.JLabel txt_act_rejected;
    private javax.swing.JLabel txt_act_success;
    private javax.swing.JLabel txt_act_total_data;
    private javax.swing.JTextField txt_count_notifACT;
    private javax.swing.JTextField txt_count_notifIZIN;
    private javax.swing.JTextArea txt_error_desc_sim;
    private javax.swing.JTextField txt_error_id_sim;
    private javax.swing.JTextField txt_error_title_sim;
    private javax.swing.JLabel txt_izin_pending;
    private javax.swing.JLabel txt_izin_rejected;
    private javax.swing.JLabel txt_izin_success;
    private javax.swing.JLabel txt_izin_total_data;
    private javax.swing.JLabel txt_stateBigHrd;
    private javax.swing.JLabel txt_stateHrd;
    private javax.swing.JLabel txt_trans_invoiceno_confirm;
    // End of variables declaration//GEN-END:variables
}
