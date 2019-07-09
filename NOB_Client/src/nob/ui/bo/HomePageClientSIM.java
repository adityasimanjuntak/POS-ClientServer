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
import java.awt.image.BufferedImage;
import static java.awt.image.ImageObserver.WIDTH;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
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
public class HomePageClientSIM extends javax.swing.JFrame {

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

    String ip = null;
    String modesu = null;
    String whoisonline = null;
    String dataTerpilih = null;
    String dataTerpilihTabelTugas = null;

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
    public HomePageClientSIM() {
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

        loadComboBoxIzinCat();
        loadComboBoxActCat();
    }

    public void Mode1() {
        mode = 1;
        //Mode 1 = Menampilkan Panel Daftar Department Back Office - SIM - Landing Page
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelSIMLandingPage.setVisible(true);
        panelSIMLogin.setVisible(false);
        panelSIMDashboard.setVisible(false);
    }

    public void Mode2() {
        mode = 2;
        //Mode 2 = Menampilkan Panel Daftar Department Back Office - SIM - Login
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelSIMLandingPage.setVisible(false);
        panelSIMLogin.setVisible(true);
        panelSIMDashboard.setVisible(false);
    }

    public void Mode3() {
        mode = 3;
        //Mode 3 = Menampilkan Panel Daftar Department Back Office - SIM - Dashboard
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelSIMLandingPage.setVisible(false);
        panelSIMLogin.setVisible(false);
        panelSIMDashboard.setVisible(true);
        subpanelSIMDashboard.setVisible(true);
        subpanelSIMIzin.setVisible(false);
        subpanelSIMAddIzin.setVisible(false);
        subpanelSIMActivity.setVisible(false);
        subpanelSIMAddActivity.setVisible(false);
        subpanelSIMErrorReport.setVisible(false);
    }

    public void Mode4() {
        mode = 4;
        //Mode 4 = Menampilkan Panel Daftar Department Back Office - SIM - Izin
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelSIMLandingPage.setVisible(false);
        panelSIMLogin.setVisible(false);
        panelSIMDashboard.setVisible(true);
        subpanelSIMDashboard.setVisible(false);
        subpanelSIMIzin.setVisible(true);
        subpanelSIMAddIzin.setVisible(false);
        subpanelSIMActivity.setVisible(false);
        subpanelSIMAddActivity.setVisible(false);
        subpanelSIMErrorReport.setVisible(false);
    }

    public void Mode5() {
        mode = 5;
        //Mode 5 = Menampilkan Panel Daftar Department Back Office - SIM - Add Izin
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelSIMLandingPage.setVisible(false);
        panelSIMLogin.setVisible(false);
        panelSIMDashboard.setVisible(true);
        subpanelSIMDashboard.setVisible(false);
        subpanelSIMIzin.setVisible(false);
        subpanelSIMAddIzin.setVisible(true);
        subpanelSIMActivity.setVisible(false);
        subpanelSIMAddActivity.setVisible(false);
        subpanelSIMErrorReport.setVisible(false);
    }

    public void Mode6() {
        mode = 6;
        //Mode 6 = Menampilkan Panel Daftar Department Back Office - SIM - Activity
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelSIMLandingPage.setVisible(false);
        panelSIMLogin.setVisible(false);
        panelSIMDashboard.setVisible(true);
        subpanelSIMDashboard.setVisible(false);
        subpanelSIMIzin.setVisible(false);
        subpanelSIMAddIzin.setVisible(false);
        subpanelSIMActivity.setVisible(true);
        subpanelSIMAddActivity.setVisible(false);
        subpanelSIMErrorReport.setVisible(false);
    }

    public void Mode7() {
        mode = 7;
        //Mode 7 = Menampilkan Panel Daftar Department Back Office - SIM - Add Activity
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelSIMLandingPage.setVisible(false);
        panelSIMLogin.setVisible(false);
        panelSIMDashboard.setVisible(true);
        subpanelSIMDashboard.setVisible(false);
        subpanelSIMIzin.setVisible(false);
        subpanelSIMAddIzin.setVisible(false);
        subpanelSIMActivity.setVisible(false);
        subpanelSIMAddActivity.setVisible(true);
        subpanelSIMErrorReport.setVisible(false);
    }

    public void Mode8() {
        mode = 8;
        //Mode 8 = Menampilkan Panel Daftar Department Back Office - SIM - Error Report
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelSIMLandingPage.setVisible(false);
        panelSIMLogin.setVisible(false);
        panelSIMDashboard.setVisible(true);
        subpanelSIMDashboard.setVisible(false);
        subpanelSIMIzin.setVisible(false);
        subpanelSIMAddIzin.setVisible(false);
        subpanelSIMActivity.setVisible(false);
        subpanelSIMAddActivity.setVisible(false);
        subpanelSIMErrorReport.setVisible(true);
    }

    public void Mode9() {
        //sebagai mode awal
        mode = 9;
        panelLandingPage.setVisible(true);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelSIMLandingPage.setVisible(false);
        panelSIMLogin.setVisible(false);
        panelSIMDashboard.setVisible(false);
    }

    public void Mode10() {
        mode = 10;
        //Mode 10= Menampilkan Panel Daftar Department
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(true);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(false);
        panelSIMLandingPage.setVisible(false);
        panelSIMLogin.setVisible(false);
        panelSIMDashboard.setVisible(false);
    }

    public void Mode11() {
        mode = 11;
        //Mode 11 = Menampilkan Panel Daftar Department Back Office
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(true);
        panelFrontOffice.setVisible(false);
        panelSIMLandingPage.setVisible(false);
        panelSIMLogin.setVisible(false);
        panelSIMDashboard.setVisible(false);
    }

    public void Mode12() {
        mode = 12;
        //Mode 12 = Menampilkan Panel Daftar Department Front Office
        panelLandingPage.setVisible(false);
        panelDepartment.setVisible(false);
        panelBackOffice.setVisible(false);
        panelFrontOffice.setVisible(true);
        panelSIMLandingPage.setVisible(false);
        panelSIMLogin.setVisible(false);
        panelSIMDashboard.setVisible(false);
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
                    txtServerTimeSIM.setText("Date  :  " + day + "/" + (month + 1) + "/" + year + "   Time : " + hour + ":" + (minute) + ":" + second);

                    try {
                        sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
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
        btnShowIPSIM.setVisible(false);
        btnHideIPSIM.setVisible(true);
    }

    public void ShowIPModeWrh() {
        btnShowIPSIM.setVisible(true);
        btnHideIPSIM.setVisible(false);
    }

    private void init_User_Dao() {
        String url = "rmi://" + configuration.ip_server + ":1099/userDao";
        try {
            //ambil referensi dari remote objek yang ada di server
            userDao = (IUserDao) Naming.lookup(url);
        } catch (NotBoundException | MalformedURLException ex) {
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            //Logger.getLogger(FrmSupplier.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Koneksi ke Service" + url + "[GAGAL], cek kembali app server", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void init_ReqIzin_ID() {
        Entity_SIM ES = new Entity_SIM();
        try {
            String hasil = simDao.GenerateReqIzinID(ES);
            if (hasil == null) {
                //do something
            } else {
                txt_izin_req_id.setText(hasil);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init_ReqAct_ID() {
        Entity_SIM ES = new Entity_SIM();
        try {
            String hasil = simDao.GenerateReqActID(ES);
            if (hasil == null) {
                //do something
            } else {
                txt_act_req_id.setText(hasil);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadComboBoxIzinCat() {
        try {
            // mengambil data department dari server
            // kemudian menyimpannya ke objek list
            recordCatIzin = simDao.getCbTipeIzin();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_SIM ES : recordCatIzin) {
                // ambil nomor urut terakhir
                cb_izin_cat.addItem(ES.getIzin_name());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadComboBoxActCat() {
        try {
            // mengambil data department dari server
            // kemudian menyimpannya ke objek list
            recordCatAct = simDao.getCbTipeActivity();
            // ekstrak data barang yg ada di dalam objek list
            // kemudian menampilkannya ke objek tabel
            for (Entity_SIM ES : recordCatAct) {
                // ambil nomor urut terakhir
                cb_act_cat.addItem(ES.getAct_name());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
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

    public int HitungJumlahIzinSuksesbyEmp() {
        int hasilizinsukses = 0;
        try {
            Entity_SIM ES = new Entity_SIM();
            ES.setReq_izin_id_emp(idAdminSIM);
            hasilizinsukses = simDao.CountIzinSuccess(ES);
            txt_count_notifIZIN.setText(String.valueOf(hasilizinsukses));
            txt_izin_success.setText(String.valueOf(hasilizinsukses));
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasilizinsukses;
    }

    public int HitungJumlahIzinPendingbyEmp() {
        int hasilizinpending = 0;
        try {
            Entity_SIM ES = new Entity_SIM();
            ES.setReq_izin_id_emp(idAdminSIM);
            hasilizinpending = simDao.CountIzinPending(ES);
            System.out.println(hasilizinpending);
            txt_izin_pending.setText(String.valueOf(hasilizinpending));
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasilizinpending;
    }

    public int HitungJumlahIzinRejectedbyEmp() {
        int hasilizinrejected = 0;
        try {
            Entity_SIM ES = new Entity_SIM();
            ES.setReq_izin_id_emp(idAdminSIM);
            hasilizinrejected = simDao.CountIzinRejected(ES);
            txt_count_notifIZIN.setText(String.valueOf(hasilizinrejected));
            txt_izin_rejected.setText(String.valueOf(hasilizinrejected));
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasilizinrejected;
    }

    public int HitungJumlahActSuksesbyEmp() {
        int hasilactsukses = 0;
        try {
            Entity_SIM ES = new Entity_SIM();
            ES.setReq_act_id_emp(idAdminSIM);
            hasilactsukses = simDao.CountActSuccess(ES);
            txt_count_notifACT.setText(String.valueOf(hasilactsukses));
            txt_act_success.setText(String.valueOf(hasilactsukses));
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasilactsukses;
    }

    public int HitungJumlahActPendingbyEmp() {
        int hasilactpending = 0;
        try {
            Entity_SIM ES = new Entity_SIM();
            ES.setReq_act_id_emp(idAdminSIM);
            hasilactpending = simDao.CountActPending(ES);
            System.out.println(hasilactpending);
            txt_act_pending.setText(String.valueOf(hasilactpending));
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasilactpending;
    }

    public int HitungJumlahActRejectedbyEmp() {
        int hasilactrejected = 0;
        try {
            Entity_SIM ES = new Entity_SIM();
            ES.setReq_act_id_emp(idAdminSIM);
            hasilactrejected = simDao.CountActRejected(ES);
            txt_count_notifACT.setText(String.valueOf(hasilactrejected));
            txt_act_rejected.setText(String.valueOf(hasilactrejected));
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasilactrejected;
    }

    public void CekNotifikasi10Sec() {
        final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                // TODO add your handling code here:

                HitungJumlahActPendingbyEmp();
                HitungJumlahActRejectedbyEmp();
                HitungJumlahActSuksesbyEmp();
                HitungJumlahIzinPendingbyEmp();
                HitungJumlahIzinRejectedbyEmp();
                HitungJumlahIzinSuksesbyEmp();

                if (HitungJumlahIzinSuksesbyEmp() != 0) {
                    txt_count_notifIZIN.setVisible(true);
                    try {
                        sfx_notifikasi();
                    } catch (IOException ex) {
                        Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (HitungJumlahIzinRejectedbyEmp() != 0) {
                    txt_count_notifIZIN.setVisible(true);
                    try {
                        sfx_notifikasi();
                    } catch (IOException ex) {
                        Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    txt_count_notifIZIN.setVisible(false);
                }

                if (HitungJumlahActSuksesbyEmp() != 0) {
                    txt_count_notifACT.setVisible(true);
                    try {
                        sfx_notifikasi();
                    } catch (IOException ex) {
                        Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (HitungJumlahActRejectedbyEmp() != 0) {
                    txt_count_notifACT.setVisible(true);
                    try {
                        sfx_notifikasi();
                    } catch (IOException ex) {
                        Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    txt_count_notifACT.setVisible(false);
                }

            }
        }, 0, 10, TimeUnit.SECONDS);
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
        DecimalFormat df = new DecimalFormat(",###.00");
        try {
            // reset data di tabel
            // mengambil data barang dari server
            tableModelIzin.setRowCount(0);
            // kemudian menyimpannya ke objek list
            recordIzin = simDao.getListIzinbyEmp(idAdminSIM);
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
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
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
            recordActivity = simDao.getListActbyEmp(idAdminSIM);
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
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
        }
        txt_act_total_data.setText(String.valueOf(tableModelAct.getRowCount()));
    }

    public void ClearFormIzin() {
        cb_izin_cat.setSelectedIndex(0);
        txt_izin_desc.setText("");
        dt_izin_dateStart.setDate(null);
        dt_izin_dateEnd.setDate(null);
        cb_izin_cat.requestFocus();
    }

    public void ClearFormActivity() {
        cb_act_cat.setSelectedIndex(0);
        txt_act_desc.setText("");
        dt_act_dateStart.setDate(null);
        dt_act_dateEnd.setDate(null);
        cb_act_cat.requestFocus();
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
        txtUsernameSIM.setText("");
        txtPasswordSIM.setText("");
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
        panelSIMDashboard = new javax.swing.JPanel();
        btnNotifSIM = new javax.swing.JLabel();
        btnMsgSIM = new javax.swing.JLabel();
        btnLogoutSIM = new javax.swing.JLabel();
        subpanelSIMDashboard = new javax.swing.JPanel();
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
        subpanelSIMIzin = new javax.swing.JPanel();
        lblSIMListIzinData = new javax.swing.JLabel();
        txt_izin_total_data = new javax.swing.JLabel();
        lblTransTotalDat = new javax.swing.JLabel();
        jSeparator16 = new javax.swing.JSeparator();
        tabelDaftarListIzinSP = new javax.swing.JScrollPane();
        tabelListIzin = new javax.swing.JTable();
        lblSIM2 = new javax.swing.JLabel();
        lblListTransactionPending = new javax.swing.JLabel();
        btn_tambah_izin = new javax.swing.JButton();
        subpanelSIMAddIzin = new javax.swing.JPanel();
        lblSIMAddIzin = new javax.swing.JLabel();
        lblSIM = new javax.swing.JLabel();
        jSeparator17 = new javax.swing.JSeparator();
        lblAddIzinSubTitle = new javax.swing.JLabel();
        lbl_izin_reqid = new javax.swing.JLabel();
        txt_izin_req_id = new javax.swing.JTextField();
        lbl_izin_dateend = new javax.swing.JLabel();
        lbl_izin_desc = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt_izin_desc = new javax.swing.JTextPane();
        cb_izin_cat = new javax.swing.JComboBox<>();
        btnSave_Izin = new javax.swing.JLabel();
        btnClear_Izin = new javax.swing.JLabel();
        lbl_izin_cat = new javax.swing.JLabel();
        lbl_izin_datestart = new javax.swing.JLabel();
        dt_izin_dateEnd = new com.toedter.calendar.JDateChooser();
        dt_izin_dateStart = new com.toedter.calendar.JDateChooser();
        subpanelSIMActivity = new javax.swing.JPanel();
        lblSIMListActivity = new javax.swing.JLabel();
        txt_act_total_data = new javax.swing.JLabel();
        lblAktivitasTotalData = new javax.swing.JLabel();
        jSeparator18 = new javax.swing.JSeparator();
        tabelDaftarListActivity = new javax.swing.JScrollPane();
        tabelListAktivitas = new javax.swing.JTable();
        lblSIM3 = new javax.swing.JLabel();
        lblListActivity = new javax.swing.JLabel();
        btn_tambah_act = new javax.swing.JButton();
        subpanelSIMAddActivity = new javax.swing.JPanel();
        lblSIMAddActivity = new javax.swing.JLabel();
        lblSIM1 = new javax.swing.JLabel();
        jSeparator20 = new javax.swing.JSeparator();
        lblAddActSubTitle = new javax.swing.JLabel();
        lbl_act_reqid = new javax.swing.JLabel();
        txt_act_req_id = new javax.swing.JTextField();
        lbl_izin_dateend1 = new javax.swing.JLabel();
        lbl_act_desc = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txt_act_desc = new javax.swing.JTextPane();
        cb_act_cat = new javax.swing.JComboBox<>();
        btnSave_Act = new javax.swing.JLabel();
        btnClear_Act = new javax.swing.JLabel();
        lbl_izin_cat1 = new javax.swing.JLabel();
        lbl_izin_datestart1 = new javax.swing.JLabel();
        dt_act_dateEnd = new com.toedter.calendar.JDateChooser();
        dt_act_dateStart = new com.toedter.calendar.JDateChooser();
        subpanelSIMErrorReport = new javax.swing.JPanel();
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
        menuWrhErrorReport = new javax.swing.JPanel();
        iconWrhErrorReport = new javax.swing.JLabel();
        jSeparator99 = new javax.swing.JSeparator();
        menuSIMAddActivity = new javax.swing.JPanel();
        iconSIMAddActivity = new javax.swing.JLabel();
        txt_count_notifACT = new javax.swing.JTextField();
        jSeparator100 = new javax.swing.JSeparator();
        menuSIMAddIzin = new javax.swing.JPanel();
        iconSIMAddIzin = new javax.swing.JLabel();
        txt_count_notifIZIN = new javax.swing.JTextField();
        jSeparator101 = new javax.swing.JSeparator();
        menuWrhDashboard = new javax.swing.JPanel();
        iconSIMDashboard = new javax.swing.JLabel();
        jSeparator102 = new javax.swing.JSeparator();
        btnChangeStateSIM = new javax.swing.JButton();
        btnHideIPSIM = new javax.swing.JButton();
        btnShowIPSIM = new javax.swing.JButton();
        txtServerTimeSIM = new javax.swing.JLabel();
        txt_stateBigSIM = new javax.swing.JLabel();
        lblStateBigSIM = new javax.swing.JLabel();
        lblStateSIM = new javax.swing.JLabel();
        txt_stateSIM = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        txtSIMNama = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        iconAdminSIM = new javax.swing.JLabel();
        bgSIM = new javax.swing.JLabel();
        panelSIMLogin = new javax.swing.JPanel();
        txtUsernameSIM = new javax.swing.JTextField();
        txtPasswordSIM = new javax.swing.JPasswordField();
        btnLoginSIM = new javax.swing.JButton();
        btnResetSIM = new javax.swing.JButton();
        panelLoginWrh = new javax.swing.JLabel();
        bgClientCashierSIM = new javax.swing.JLabel();
        panelSIMLandingPage = new javax.swing.JPanel();
        iconHome1 = new javax.swing.JLabel();
        bgsSIMLogo = new javax.swing.JLabel();
        bgClientSIM = new javax.swing.JLabel();
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
        setTitle("NOB Tech - Client - SIM");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelSIMDashboard.setAutoscrolls(true);
        panelSIMDashboard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnNotifSIM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconNotif.png"))); // NOI18N
        btnNotifSIM.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelSIMDashboard.add(btnNotifSIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(1039, 97, -1, -1));

        btnMsgSIM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconMassage.png"))); // NOI18N
        btnMsgSIM.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelSIMDashboard.add(btnMsgSIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 97, -1, -1));

        btnLogoutSIM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/iconLogoutSIM.png"))); // NOI18N
        btnLogoutSIM.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogoutSIM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogoutSIMMouseClicked(evt);
            }
        });
        panelSIMDashboard.add(btnLogoutSIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 97, -1, -1));

        subpanelSIMDashboard.setBackground(new java.awt.Color(255, 255, 255));
        subpanelSIMDashboard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_izin_success.setFont(new java.awt.Font("Gotham Ultra", 1, 48)); // NOI18N
        txt_izin_success.setForeground(new java.awt.Color(255, 255, 255));
        txt_izin_success.setText("0");
        subpanelSIMDashboard.add(txt_izin_success, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 150, -1, -1));

        txt_izin_pending.setFont(new java.awt.Font("Gotham Ultra", 1, 48)); // NOI18N
        txt_izin_pending.setForeground(new java.awt.Color(255, 255, 255));
        txt_izin_pending.setText("0");
        subpanelSIMDashboard.add(txt_izin_pending, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 150, -1, -1));

        txt_izin_rejected.setFont(new java.awt.Font("Gotham Ultra", 1, 48)); // NOI18N
        txt_izin_rejected.setForeground(new java.awt.Color(255, 255, 255));
        txt_izin_rejected.setText("0");
        subpanelSIMDashboard.add(txt_izin_rejected, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 150, -1, -1));

        txt_act_success.setFont(new java.awt.Font("Gotham Ultra", 1, 48)); // NOI18N
        txt_act_success.setForeground(new java.awt.Color(255, 255, 255));
        txt_act_success.setText("0");
        subpanelSIMDashboard.add(txt_act_success, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 370, -1, -1));

        txt_act_pending.setFont(new java.awt.Font("Gotham Ultra", 1, 48)); // NOI18N
        txt_act_pending.setForeground(new java.awt.Color(255, 255, 255));
        txt_act_pending.setText("0");
        subpanelSIMDashboard.add(txt_act_pending, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 370, -1, -1));

        txt_act_rejected.setFont(new java.awt.Font("Gotham Ultra", 1, 48)); // NOI18N
        txt_act_rejected.setForeground(new java.awt.Color(255, 255, 255));
        txt_act_rejected.setText("0");
        subpanelSIMDashboard.add(txt_act_rejected, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 370, -1, -1));

        lblDashboardTitle2.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblDashboardTitle2.setForeground(java.awt.Color.gray);
        lblDashboardTitle2.setText("Sistem Informasi Manajemen");
        subpanelSIMDashboard.add(lblDashboardTitle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 53, -1, -1));

        lblDashboardTitleSIM.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblDashboardTitleSIM.setForeground(java.awt.Color.gray);
        lblDashboardTitleSIM.setText("DASHBOARD");
        subpanelSIMDashboard.add(lblDashboardTitleSIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        jSeparator19.setForeground(new java.awt.Color(241, 241, 241));
        subpanelSIMDashboard.add(jSeparator19, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 930, 20));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/iconActRejected.png"))); // NOI18N
        subpanelSIMDashboard.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 330, -1, -1));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/iconActPending.png"))); // NOI18N
        subpanelSIMDashboard.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 330, -1, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/iconActSuccess.png"))); // NOI18N
        subpanelSIMDashboard.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 330, -1, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/iconIzinSuccess.png"))); // NOI18N
        subpanelSIMDashboard.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/iconIzinPending.png"))); // NOI18N
        subpanelSIMDashboard.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 110, -1, -1));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/iconIzinRejected.png"))); // NOI18N
        subpanelSIMDashboard.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 110, -1, -1));

        panelSIMDashboard.add(subpanelSIMDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        subpanelSIMIzin.setBackground(new java.awt.Color(255, 255, 255));
        subpanelSIMIzin.setPreferredSize(new java.awt.Dimension(1010, 538));
        subpanelSIMIzin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblSIMListIzinData.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblSIMListIzinData.setForeground(java.awt.Color.gray);
        lblSIMListIzinData.setText("LIST IZIN DATA");
        subpanelSIMIzin.add(lblSIMListIzinData, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        txt_izin_total_data.setFont(new java.awt.Font("Gotham Light", 1, 12)); // NOI18N
        txt_izin_total_data.setForeground(java.awt.Color.gray);
        txt_izin_total_data.setText("0");
        subpanelSIMIzin.add(txt_izin_total_data, new org.netbeans.lib.awtextra.AbsoluteConstraints(105, 490, -1, -1));

        lblTransTotalDat.setFont(new java.awt.Font("Gotham Light", 0, 12)); // NOI18N
        lblTransTotalDat.setForeground(java.awt.Color.gray);
        lblTransTotalDat.setText("Total Data :");
        subpanelSIMIzin.add(lblTransTotalDat, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 490, -1, -1));

        jSeparator16.setForeground(new java.awt.Color(241, 241, 241));
        subpanelSIMIzin.add(jSeparator16, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 950, 20));

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

        subpanelSIMIzin.add(tabelDaftarListIzinSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 950, 320));

        lblSIM2.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblSIM2.setForeground(java.awt.Color.gray);
        lblSIM2.setText("Sistem Informasi Manajemen");
        subpanelSIMIzin.add(lblSIM2, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 53, -1, -1));

        lblListTransactionPending.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblListTransactionPending.setForeground(java.awt.Color.gray);
        lblListTransactionPending.setText("List Izin ");
        subpanelSIMIzin.add(lblListTransactionPending, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, -1));

        btn_tambah_izin.setBackground(new java.awt.Color(153, 0, 0));
        btn_tambah_izin.setForeground(new java.awt.Color(255, 255, 255));
        btn_tambah_izin.setText("+ Tambah Izin");
        btn_tambah_izin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_tambah_izinMouseClicked(evt);
            }
        });
        subpanelSIMIzin.add(btn_tambah_izin, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 110, 120, -1));

        panelSIMDashboard.add(subpanelSIMIzin, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        subpanelSIMAddIzin.setBackground(new java.awt.Color(255, 255, 255));
        subpanelSIMAddIzin.setPreferredSize(new java.awt.Dimension(1010, 538));
        subpanelSIMAddIzin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblSIMAddIzin.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblSIMAddIzin.setForeground(java.awt.Color.gray);
        lblSIMAddIzin.setText("ADD IZIN");
        subpanelSIMAddIzin.add(lblSIMAddIzin, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        lblSIM.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblSIM.setForeground(java.awt.Color.gray);
        lblSIM.setText("Sistem Informasi Manajemen");
        subpanelSIMAddIzin.add(lblSIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 53, -1, -1));

        jSeparator17.setForeground(new java.awt.Color(241, 241, 241));
        subpanelSIMAddIzin.add(jSeparator17, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 950, 20));

        lblAddIzinSubTitle.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblAddIzinSubTitle.setForeground(java.awt.Color.gray);
        lblAddIzinSubTitle.setText("Add Izin");
        subpanelSIMAddIzin.add(lblAddIzinSubTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        lbl_izin_reqid.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_izin_reqid.setText("Req ID");
        subpanelSIMAddIzin.add(lbl_izin_reqid, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, -1, -1));

        txt_izin_req_id.setEnabled(false);
        subpanelSIMAddIzin.add(txt_izin_req_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 180, 30));

        lbl_izin_dateend.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_izin_dateend.setText("Date End");
        subpanelSIMAddIzin.add(lbl_izin_dateend, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 220, -1, -1));

        lbl_izin_desc.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_izin_desc.setText("Description");
        subpanelSIMAddIzin.add(lbl_izin_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, -1, -1));

        jScrollPane1.setViewportView(txt_izin_desc);

        subpanelSIMAddIzin.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 300, 280, 160));

        cb_izin_cat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Category -" }));
        cb_izin_cat.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_izin_catItemStateChanged(evt);
            }
        });
        subpanelSIMAddIzin.add(cb_izin_cat, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 210, 180, 30));

        btnSave_Izin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconSave.png"))); // NOI18N
        btnSave_Izin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSave_Izin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSave_IzinMouseClicked(evt);
            }
        });
        subpanelSIMAddIzin.add(btnSave_Izin, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 410, -1, -1));

        btnClear_Izin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconClear.png"))); // NOI18N
        btnClear_Izin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClear_Izin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClear_IzinMouseClicked(evt);
            }
        });
        subpanelSIMAddIzin.add(btnClear_Izin, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 410, -1, -1));

        lbl_izin_cat.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_izin_cat.setText("Category");
        subpanelSIMAddIzin.add(lbl_izin_cat, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, -1, -1));

        lbl_izin_datestart.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_izin_datestart.setText("Date Start");
        subpanelSIMAddIzin.add(lbl_izin_datestart, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 150, -1, -1));
        subpanelSIMAddIzin.add(dt_izin_dateEnd, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 210, 180, 30));
        subpanelSIMAddIzin.add(dt_izin_dateStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 140, 180, 30));

        panelSIMDashboard.add(subpanelSIMAddIzin, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        subpanelSIMActivity.setBackground(new java.awt.Color(255, 255, 255));
        subpanelSIMActivity.setPreferredSize(new java.awt.Dimension(1010, 538));
        subpanelSIMActivity.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblSIMListActivity.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblSIMListActivity.setForeground(java.awt.Color.gray);
        lblSIMListActivity.setText("LIST ACTIVITY");
        subpanelSIMActivity.add(lblSIMListActivity, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        txt_act_total_data.setFont(new java.awt.Font("Gotham Light", 1, 12)); // NOI18N
        txt_act_total_data.setForeground(java.awt.Color.gray);
        txt_act_total_data.setText("0");
        subpanelSIMActivity.add(txt_act_total_data, new org.netbeans.lib.awtextra.AbsoluteConstraints(105, 490, -1, -1));

        lblAktivitasTotalData.setFont(new java.awt.Font("Gotham Light", 0, 12)); // NOI18N
        lblAktivitasTotalData.setForeground(java.awt.Color.gray);
        lblAktivitasTotalData.setText("Total Data :");
        subpanelSIMActivity.add(lblAktivitasTotalData, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 490, -1, -1));

        jSeparator18.setForeground(new java.awt.Color(241, 241, 241));
        subpanelSIMActivity.add(jSeparator18, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 950, 20));

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

        subpanelSIMActivity.add(tabelDaftarListActivity, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 950, 320));

        lblSIM3.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblSIM3.setForeground(java.awt.Color.gray);
        lblSIM3.setText("Sistem Informasi Manajemen");
        subpanelSIMActivity.add(lblSIM3, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 53, -1, -1));

        lblListActivity.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblListActivity.setForeground(java.awt.Color.gray);
        lblListActivity.setText("List Izin ");
        subpanelSIMActivity.add(lblListActivity, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, -1));

        btn_tambah_act.setBackground(new java.awt.Color(153, 0, 0));
        btn_tambah_act.setForeground(new java.awt.Color(255, 255, 255));
        btn_tambah_act.setText("+ Tambah Aktivitas");
        btn_tambah_act.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_tambah_actMouseClicked(evt);
            }
        });
        subpanelSIMActivity.add(btn_tambah_act, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 110, 150, -1));

        panelSIMDashboard.add(subpanelSIMActivity, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        subpanelSIMAddActivity.setBackground(new java.awt.Color(255, 255, 255));
        subpanelSIMAddActivity.setPreferredSize(new java.awt.Dimension(1010, 538));
        subpanelSIMAddActivity.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblSIMAddActivity.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblSIMAddActivity.setForeground(java.awt.Color.gray);
        lblSIMAddActivity.setText("ADD ACTIVITY");
        subpanelSIMAddActivity.add(lblSIMAddActivity, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        lblSIM1.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblSIM1.setForeground(java.awt.Color.gray);
        lblSIM1.setText("Sistem Informasi Manajemen");
        subpanelSIMAddActivity.add(lblSIM1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 53, -1, -1));

        jSeparator20.setForeground(new java.awt.Color(241, 241, 241));
        subpanelSIMAddActivity.add(jSeparator20, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 950, 20));

        lblAddActSubTitle.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblAddActSubTitle.setForeground(java.awt.Color.gray);
        lblAddActSubTitle.setText("Add Activity");
        subpanelSIMAddActivity.add(lblAddActSubTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        lbl_act_reqid.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_act_reqid.setText("Req ID");
        subpanelSIMAddActivity.add(lbl_act_reqid, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, -1, -1));

        txt_act_req_id.setEnabled(false);
        subpanelSIMAddActivity.add(txt_act_req_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 180, 30));

        lbl_izin_dateend1.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_izin_dateend1.setText("Date End");
        subpanelSIMAddActivity.add(lbl_izin_dateend1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 220, -1, -1));

        lbl_act_desc.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_act_desc.setText("Description");
        subpanelSIMAddActivity.add(lbl_act_desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, -1, -1));

        jScrollPane2.setViewportView(txt_act_desc);

        subpanelSIMAddActivity.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 300, 280, 160));

        cb_act_cat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Category -" }));
        cb_act_cat.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_act_catItemStateChanged(evt);
            }
        });
        subpanelSIMAddActivity.add(cb_act_cat, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 210, 180, 30));

        btnSave_Act.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconSave.png"))); // NOI18N
        btnSave_Act.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSave_Act.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSave_ActMouseClicked(evt);
            }
        });
        subpanelSIMAddActivity.add(btnSave_Act, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 410, -1, -1));

        btnClear_Act.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconClear.png"))); // NOI18N
        btnClear_Act.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClear_Act.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClear_ActMouseClicked(evt);
            }
        });
        subpanelSIMAddActivity.add(btnClear_Act, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 410, -1, -1));

        lbl_izin_cat1.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_izin_cat1.setText("Category");
        subpanelSIMAddActivity.add(lbl_izin_cat1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, -1, -1));

        lbl_izin_datestart1.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_izin_datestart1.setText("Date Start");
        subpanelSIMAddActivity.add(lbl_izin_datestart1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 150, -1, -1));
        subpanelSIMAddActivity.add(dt_act_dateEnd, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 210, 180, 30));
        subpanelSIMAddActivity.add(dt_act_dateStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 140, 180, 30));

        panelSIMDashboard.add(subpanelSIMAddActivity, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        subpanelSIMErrorReport.setBackground(new java.awt.Color(255, 255, 255));
        subpanelSIMErrorReport.setPreferredSize(new java.awt.Dimension(1010, 538));
        subpanelSIMErrorReport.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSaveError_sim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconSave.png"))); // NOI18N
        btnSaveError_sim.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSaveError_sim.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveError_simMouseClicked(evt);
            }
        });
        subpanelSIMErrorReport.add(btnSaveError_sim, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 410, -1, -1));

        btnClearError_sim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/iconClear.png"))); // NOI18N
        btnClearError_sim.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClearError_sim.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClearError_simMouseClicked(evt);
            }
        });
        subpanelSIMErrorReport.add(btnClearError_sim, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 410, -1, -1));

        lbl_list_error_sim.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lbl_list_error_sim.setForeground(java.awt.Color.gray);
        lbl_list_error_sim.setText("Report List");
        subpanelSIMErrorReport.add(lbl_list_error_sim, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 100, -1, -1));

        cb_error_status_sim.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- Choose Status -", "Currently Added", "On Progress", "Done" }));
        subpanelSIMErrorReport.add(cb_error_status_sim, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 410, 180, -1));

        lbl_error_date_cas.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_date_cas.setText("Date");
        subpanelSIMErrorReport.add(lbl_error_date_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, -1, -1));
        subpanelSIMErrorReport.add(dtError_Date_sim, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 360, 180, -1));

        lbl_error_status_cas.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_status_cas.setText("Status");
        subpanelSIMErrorReport.add(lbl_error_status_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 410, -1, -1));

        txt_error_desc_sim.setBackground(new java.awt.Color(240, 240, 240));
        txt_error_desc_sim.setColumns(20);
        txt_error_desc_sim.setRows(5);
        subpanelSIMErrorReport.add(txt_error_desc_sim, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 240, 180, -1));

        lbl_error_desc_cas.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_desc_cas.setText("Description");
        subpanelSIMErrorReport.add(lbl_error_desc_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, -1, -1));
        subpanelSIMErrorReport.add(txt_error_title_sim, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 188, 180, -1));

        lbl_error_title_cas.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_title_cas.setText("Title");
        subpanelSIMErrorReport.add(lbl_error_title_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, -1));

        txt_error_id_sim.setEnabled(false);
        subpanelSIMErrorReport.add(txt_error_id_sim, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 180, -1));

        lbl_error_id_cas.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        lbl_error_id_cas.setText("Report ID");
        subpanelSIMErrorReport.add(lbl_error_id_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));

        lblErrorReportTitleCas.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblErrorReportTitleCas.setForeground(java.awt.Color.gray);
        lblErrorReportTitleCas.setText("ERROR REPORT");
        subpanelSIMErrorReport.add(lblErrorReportTitleCas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        lblErrorReportCasSubTitle1.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblErrorReportCasSubTitle1.setForeground(java.awt.Color.gray);
        lblErrorReportCasSubTitle1.setText("Add Report");
        subpanelSIMErrorReport.add(lblErrorReportCasSubTitle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        jSeparator14.setForeground(new java.awt.Color(241, 241, 241));
        subpanelSIMErrorReport.add(jSeparator14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 950, 20));

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

        subpanelSIMErrorReport.add(tabelDaftarReportSP1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 130, 580, 260));

        lblErrorReportCasSubTitle.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblErrorReportCasSubTitle.setForeground(java.awt.Color.gray);
        lblErrorReportCasSubTitle.setText("Sistem Informasi Manajemen");
        subpanelSIMErrorReport.add(lblErrorReportCasSubTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(335, 53, -1, -1));

        btn_delete_error_sim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/btnDelete.png"))); // NOI18N
        btn_delete_error_sim.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_delete_error_sim.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_delete_error_simMouseClicked(evt);
            }
        });
        subpanelSIMErrorReport.add(btn_delete_error_sim, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 87, -1, -1));

        panelSIMDashboard.add(subpanelSIMErrorReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));
        panelSIMDashboard.add(jSeparator104, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 170, 10));

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

        panelSIMDashboard.add(menuWrhErrorReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 395, 170, 30));
        panelSIMDashboard.add(jSeparator99, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 170, 10));

        menuSIMAddActivity.setBackground(new java.awt.Color(241, 241, 241));
        menuSIMAddActivity.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuSIMAddActivity.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuSIMAddActivityMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuSIMAddActivityMouseExited(evt);
            }
        });
        menuSIMAddActivity.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconSIMAddActivity.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/iconAddActivity.png"))); // NOI18N
        iconSIMAddActivity.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconSIMAddActivityMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconSIMAddActivityMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconSIMAddActivityMouseExited(evt);
            }
        });
        menuSIMAddActivity.add(iconSIMAddActivity, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        txt_count_notifACT.setEditable(false);
        txt_count_notifACT.setBackground(new java.awt.Color(32, 193, 237));
        txt_count_notifACT.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txt_count_notifACT.setForeground(new java.awt.Color(255, 255, 255));
        txt_count_notifACT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_count_notifACT.setText("0");
        txt_count_notifACT.setBorder(null);
        menuSIMAddActivity.add(txt_count_notifACT, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 7, 20, 20));

        panelSIMDashboard.add(menuSIMAddActivity, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 355, 170, 30));
        panelSIMDashboard.add(jSeparator100, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 170, 10));

        menuSIMAddIzin.setBackground(new java.awt.Color(241, 241, 241));
        menuSIMAddIzin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuSIMAddIzin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuSIMAddIzinMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuSIMAddIzinMouseExited(evt);
            }
        });
        menuSIMAddIzin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconSIMAddIzin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/iconAddIzin.png"))); // NOI18N
        iconSIMAddIzin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconSIMAddIzinMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconSIMAddIzinMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconSIMAddIzinMouseExited(evt);
            }
        });
        menuSIMAddIzin.add(iconSIMAddIzin, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 9, 70, -1));

        txt_count_notifIZIN.setEditable(false);
        txt_count_notifIZIN.setBackground(new java.awt.Color(32, 193, 237));
        txt_count_notifIZIN.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txt_count_notifIZIN.setForeground(new java.awt.Color(255, 255, 255));
        txt_count_notifIZIN.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_count_notifIZIN.setText("0");
        txt_count_notifIZIN.setBorder(null);
        menuSIMAddIzin.add(txt_count_notifIZIN, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 5, 20, 20));

        panelSIMDashboard.add(menuSIMAddIzin, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 315, 170, 30));
        panelSIMDashboard.add(jSeparator101, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 170, 10));

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

        iconSIMDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/IconDashboard.png"))); // NOI18N
        iconSIMDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconSIMDashboardMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconSIMDashboardMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconSIMDashboardMouseExited(evt);
            }
        });
        menuWrhDashboard.add(iconSIMDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 7, -1, -1));

        panelSIMDashboard.add(menuWrhDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 275, 170, 30));
        panelSIMDashboard.add(jSeparator102, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 170, 10));

        btnChangeStateSIM.setText("Change State");
        btnChangeStateSIM.setToolTipText("Click to Change State");
        btnChangeStateSIM.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnChangeStateSIM.setMaximumSize(new java.awt.Dimension(73, 23));
        btnChangeStateSIM.setMinimumSize(new java.awt.Dimension(74, 23));
        btnChangeStateSIM.setPreferredSize(new java.awt.Dimension(78, 23));
        btnChangeStateSIM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnChangeStateSIMMouseClicked(evt);
            }
        });
        panelSIMDashboard.add(btnChangeStateSIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(83, 230, 100, -1));

        btnHideIPSIM.setText("Hide IP");
        btnHideIPSIM.setToolTipText("Click to Hide IP");
        btnHideIPSIM.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHideIPSIM.setMaximumSize(new java.awt.Dimension(73, 23));
        btnHideIPSIM.setMinimumSize(new java.awt.Dimension(74, 23));
        btnHideIPSIM.setPreferredSize(new java.awt.Dimension(73, 23));
        btnHideIPSIM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHideIPSIMMouseClicked(evt);
            }
        });
        panelSIMDashboard.add(btnHideIPSIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 230, -1, -1));

        btnShowIPSIM.setText("Show IP");
        btnShowIPSIM.setToolTipText("Click to show IP");
        btnShowIPSIM.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnShowIPSIM.setMaximumSize(new java.awt.Dimension(73, 23));
        btnShowIPSIM.setMinimumSize(new java.awt.Dimension(73, 23));
        btnShowIPSIM.setPreferredSize(new java.awt.Dimension(73, 23));
        btnShowIPSIM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnShowIPSIMMouseClicked(evt);
            }
        });
        panelSIMDashboard.add(btnShowIPSIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 230, -1, -1));

        txtServerTimeSIM.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        txtServerTimeSIM.setText("SERVER TIME");
        panelSIMDashboard.add(txtServerTimeSIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 693, -1, -1));

        txt_stateBigSIM.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        txt_stateBigSIM.setText("SERVER STATE");
        panelSIMDashboard.add(txt_stateBigSIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 693, -1, -1));

        lblStateBigSIM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OnlineState.png"))); // NOI18N
        panelSIMDashboard.add(lblStateBigSIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 693, -1, -1));

        lblStateSIM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OnlineState.png"))); // NOI18N
        panelSIMDashboard.add(lblStateSIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(78, 208, -1, -1));

        txt_stateSIM.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        txt_stateSIM.setText("Online");
        panelSIMDashboard.add(txt_stateSIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 210, -1, -1));

        jLabel32.setFont(new java.awt.Font("Gotham Medium", 1, 11)); // NOI18N
        jLabel32.setText("Keyko");
        panelSIMDashboard.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(595, 695, -1, -1));

        jLabel33.setFont(new java.awt.Font("Gotham Light", 2, 11)); // NOI18N
        jLabel33.setText("powered by");
        panelSIMDashboard.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 695, -1, -1));

        jLabel34.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        jLabel34.setText("NOB Tech - ");
        panelSIMDashboard.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(465, 695, -1, -1));

        txtSIMNama.setFont(new java.awt.Font("Gotham Medium", 1, 12)); // NOI18N
        txtSIMNama.setText("Employee");
        panelSIMDashboard.add(txtSIMNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(79, 190, -1, -1));

        jLabel35.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        jLabel35.setText("Hello, ");
        panelSIMDashboard.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 170, -1, -1));

        iconAdminSIM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/management/AdminLogo.png"))); // NOI18N
        panelSIMDashboard.add(iconAdminSIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        bgSIM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/bgDashboardSIM.png"))); // NOI18N
        panelSIMDashboard.add(bgSIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelSIMDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 721));

        panelSIMLogin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelSIMLogin.add(txtUsernameSIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 380, 230, 40));
        panelSIMLogin.add(txtPasswordSIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 470, 230, 40));

        btnLoginSIM.setText("LOGIN");
        btnLoginSIM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLoginSIMMouseClicked(evt);
            }
        });
        panelSIMLogin.add(btnLoginSIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 550, -1, -1));

        btnResetSIM.setText("CANCEL");
        btnResetSIM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnResetSIMMouseClicked(evt);
            }
        });
        panelSIMLogin.add(btnResetSIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 550, -1, -1));

        panelLoginWrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/panelLoginSIM.png"))); // NOI18N
        panelSIMLogin.add(panelLoginWrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 120, -1, -1));

        bgClientCashierSIM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/bgLoginSIM.png"))); // NOI18N
        panelSIMLogin.add(bgClientCashierSIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelSIMLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 721));

        panelSIMLandingPage.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconHome1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/iconHome.png"))); // NOI18N
        iconHome1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconHome1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconHome1MouseClicked(evt);
            }
        });
        panelSIMLandingPage.add(iconHome1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        bgsSIMLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/logoSIM.png"))); // NOI18N
        bgsSIMLogo.setToolTipText("");
        bgsSIMLogo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bgsSIMLogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bgsSIMLogoMouseClicked(evt);
            }
        });
        panelSIMLandingPage.add(bgsSIMLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 200, -1, -1));

        bgClientSIM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/ui/design/sim/bgLandingPageSIM.png"))); // NOI18N
        panelSIMLandingPage.add(bgClientSIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelSIMLandingPage, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 720));

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

    private void bgsSIMLogoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bgsSIMLogoMouseClicked
        // TODO add your handling code here:
        Mode2();
    }//GEN-LAST:event_bgsSIMLogoMouseClicked

    private void btnLoginSIMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLoginSIMMouseClicked
        // TODO add your handling code here:
        String lettercode = txtUsernameSIM.getText().substring(0, 3);
        System.out.println(lettercode);
        if (txtUsernameSIM.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please Input Your Username", "Sign In Error", JOptionPane.ERROR_MESSAGE);
            txtUsernameSIM.requestFocus();
        } else if (txtPasswordSIM.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please Input Your Password", "Sign In Error", JOptionPane.ERROR_MESSAGE);
            txtPasswordSIM.requestFocus();
        } else {
            Entity_Signin ESI = new Entity_Signin();
            ESI.setUsername(txtUsernameSIM.getText());
            ESI.setPassword(txtPasswordSIM.getText());
            ESI.setStatus("Active");
            try {
                int hasil = loginDao.Login(ESI);
                if (hasil == 1) {
                    JOptionPane.showMessageDialog(null, "Sign In Success", "Sign-In Success", JOptionPane.INFORMATION_MESSAGE);
                    Mode3();
                    HitungJumlahIzinSuksesbyEmp();
                    HitungJumlahIzinPendingbyEmp();
                    HitungJumlahIzinRejectedbyEmp();
                    HitungJumlahActPendingbyEmp();
                    HitungJumlahActSuksesbyEmp();
                    HitungJumlahActRejectedbyEmp();
                    CekNotifikasi10Sec();
                    idAdminSIM = txtUsernameSIM.getText();
                } else {
                    JOptionPane.showMessageDialog(null, "Password or Username Does Not Match", "Sign-In Error", JOptionPane.ERROR_MESSAGE);
                    txtUsernameSIM.setText("");
                    txtPasswordSIM.setText("");
                }
            } catch (RemoteException ex) {
                Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        whoisonline = txtUsernameSIM.getText();
        Entity_Signin ESI = new Entity_Signin();
        ESI.setUsername(whoisonline);
        try {
            String hasil = loginDao.CekNamaOnline(ESI);
            txtSIMNama.setText(hasil);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnLoginSIMMouseClicked

    private void btnResetSIMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetSIMMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnResetSIMMouseClicked

    private void btnLogoutSIMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutSIMMouseClicked
        // TODO add your handling code here:
        Object[] options = {"LogOut", "Cancel"};
        int reply = JOptionPane.showOptionDialog(null, "Are you sure to Logout ?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                Mode2();
                clearFormLogin();
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btnLogoutSIMMouseClicked

    private void iconWrhErrorReportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconWrhErrorReportMouseClicked
        // TODO add your handling code here:
        Mode8();
        init_Error_ID();
        initTabelError();
        loadDaftarError();
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

    private void iconSIMAddActivityMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconSIMAddActivityMouseClicked
        // TODO add your handling code here:
        Mode6();
        initTabelListAct();
        loadDaftarTabelListAct();
    }//GEN-LAST:event_iconSIMAddActivityMouseClicked

    private void iconSIMAddActivityMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconSIMAddActivityMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuSIMAddActivity.setBackground(skyBlue);
    }//GEN-LAST:event_iconSIMAddActivityMouseEntered

    private void iconSIMAddActivityMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconSIMAddActivityMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuSIMAddActivity.setBackground(lightGray);
    }//GEN-LAST:event_iconSIMAddActivityMouseExited

    private void menuSIMAddActivityMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuSIMAddActivityMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuSIMAddActivity.setBackground(skyBlue);
    }//GEN-LAST:event_menuSIMAddActivityMouseEntered

    private void menuSIMAddActivityMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuSIMAddActivityMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuSIMAddActivity.setBackground(lightGray);
    }//GEN-LAST:event_menuSIMAddActivityMouseExited

    private void iconSIMAddIzinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconSIMAddIzinMouseClicked
        // TODO add your handling code here:
        Mode4();
        initTabelListIzin();
        loadDaftarTabelListIzin();
    }//GEN-LAST:event_iconSIMAddIzinMouseClicked

    private void iconSIMAddIzinMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconSIMAddIzinMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuSIMAddIzin.setBackground(skyBlue);
    }//GEN-LAST:event_iconSIMAddIzinMouseEntered

    private void iconSIMAddIzinMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconSIMAddIzinMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuSIMAddIzin.setBackground(lightGray);
    }//GEN-LAST:event_iconSIMAddIzinMouseExited

    private void menuSIMAddIzinMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuSIMAddIzinMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuSIMAddIzin.setBackground(skyBlue);
    }//GEN-LAST:event_menuSIMAddIzinMouseEntered

    private void menuSIMAddIzinMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuSIMAddIzinMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuSIMAddIzin.setBackground(lightGray);
    }//GEN-LAST:event_menuSIMAddIzinMouseExited

    private void iconSIMDashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconSIMDashboardMouseClicked
        // TODO add your handling code here:
        Mode3();
    }//GEN-LAST:event_iconSIMDashboardMouseClicked

    private void iconSIMDashboardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconSIMDashboardMouseEntered
        // TODO add your handling code here:
        Color skyBlue = new Color(29, 174, 214);
        menuWrhDashboard.setBackground(skyBlue);
    }//GEN-LAST:event_iconSIMDashboardMouseEntered

    private void iconSIMDashboardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconSIMDashboardMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        menuWrhDashboard.setBackground(lightGray);
    }//GEN-LAST:event_iconSIMDashboardMouseExited

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

    private void btnChangeStateSIMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChangeStateSIMMouseClicked
        // TODO add your handling code here:
        Object[] options = {"Online", "Idle", "Invisible", "Offline"};
        int reply = JOptionPane.showOptionDialog(null, "Choose your server state : ", "State Chooser", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                if (txt_stateSIM.getText().equals("Online")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Online", "Warning Message", JOptionPane.WARNING_MESSAGE);
                }
                txt_stateSIM.setText("Online");
                txt_stateBigSIM.setText("Server is Well-Connected");
                lblStateSIM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OnlineState.png")));
                lblStateBigSIM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OnlineState.png")));
                break;
            case 1:
                if (txt_stateSIM.getText().equals("Idle")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Idle", "Warning Message", JOptionPane.WARNING_MESSAGE);
                }
                txt_stateSIM.setText("Idle");
                txt_stateBigSIM.setText("Server is Well-Connected");
                lblStateSIM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/IdleState.png")));
                lblStateBigSIM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/IdleState.png")));
                break;
            case 2:
                if (txt_stateSIM.getText().equals("Invisible")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Invisible", "Warning Message", JOptionPane.WARNING_MESSAGE);
                }
                txt_stateSIM.setText("Invisible");
                txt_stateBigSIM.setText("Server is Well-Connected");
                lblStateSIM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/InvisState.png")));
                lblStateBigSIM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/InvisState.png")));
                break;
            case 3:
                if (txt_stateSIM.getText().equals("Offline")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Offline", "Warning Message", JOptionPane.WARNING_MESSAGE);
                }
                txt_stateSIM.setText("Offline");
                txt_stateBigSIM.setText("Server is Disconnected");
                lblStateSIM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OffState.png")));
                lblStateBigSIM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/icon/OffState.png")));
                break;
            //donothing
            default:
                break;
        }
    }//GEN-LAST:event_btnChangeStateSIMMouseClicked

    private void btnHideIPSIMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHideIPSIMMouseClicked
        // TODO add your handling code here:
        whoisonline = txtUsernameSIM.getText();
//        System.out.println(whoisonline);
        Entity_Signin ESI = new Entity_Signin();
        ESI.setUsername(whoisonline);
        try {
            String hasil = loginDao.CekNamaOnline(ESI);
            txtSIMNama.setText(hasil);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePageClientManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        ShowIPModeWrh();
    }//GEN-LAST:event_btnHideIPSIMMouseClicked

    private void btnShowIPSIMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnShowIPSIMMouseClicked
        // TODO add your handling code here:
        txtSIMNama.setText(ip);
        HideIPModeWrh();
    }//GEN-LAST:event_btnShowIPSIMMouseClicked

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
        dataTerpilih = tabelListIzin.getValueAt(baris, 0).toString();
    }//GEN-LAST:event_tabelListIzinMouseClicked

    private void btnSave_IzinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSave_IzinMouseClicked
        // TODO add your handling code here:
        if (cb_izin_cat.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Choose Izin Category", "Warning Message", JOptionPane.WARNING_MESSAGE);
            cb_izin_cat.requestFocus();
        } else if (txt_izin_desc.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Choose Izin Description", "Warning Message", JOptionPane.WARNING_MESSAGE);
            txt_izin_desc.requestFocus();
        } else if (dt_izin_dateStart.getDate().equals(null)) {
            JOptionPane.showMessageDialog(null, "Choose Izin Description", "Warning Message", JOptionPane.WARNING_MESSAGE);
            dt_izin_dateStart.requestFocus();
        } else if (dt_izin_dateEnd.getDate().equals(null)) {
            JOptionPane.showMessageDialog(null, "Choose Izin Description", "Warning Message", JOptionPane.WARNING_MESSAGE);
            dt_izin_dateEnd.requestFocus();
        } else {
            Date date = new Date();
            System.out.println(date);

            Entity_SIM ES = new Entity_SIM();
            ES.setReq_izin_id(txt_izin_req_id.getText());
            ES.setReq_izin_id_emp(idAdminSIM);
            ES.setReq_izin_cat(idIzin);
            ES.setReq_izin_date_start(dt_izin_dateStart.getDate());
            ES.setReq_izin_date_end(dt_izin_dateEnd.getDate());
            ES.setReq_izin_desc(txt_izin_desc.getText());
            ES.setReq_izin_created_date(date);
            try {
                int hasilsaveIzin = simDao.saveIzin(ES);
                if (hasilsaveIzin == 1) {
                    JOptionPane.showMessageDialog(null, "Save Izin Success, Wait For Approval", "Success Message", JOptionPane.INFORMATION_MESSAGE);
                    init_ReqIzin_ID();
                    ClearFormIzin();
                } else {
                    JOptionPane.showMessageDialog(null, "Save Izin Failed", "Failed Message", JOptionPane.ERROR_MESSAGE);
                }
            } catch (RemoteException ex) {
                Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnSave_IzinMouseClicked

    private void btnClear_IzinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClear_IzinMouseClicked
        // TODO add your handling code here:
        ClearFormIzin();
    }//GEN-LAST:event_btnClear_IzinMouseClicked

    private void iconSIMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconSIMMouseClicked
        // TODO add your handling code here:
        Mode2();
    }//GEN-LAST:event_iconSIMMouseClicked

    private void btn_tambah_izinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_tambah_izinMouseClicked
        // TODO add your handling code here:
        Mode5();
        init_ReqIzin_ID();
    }//GEN-LAST:event_btn_tambah_izinMouseClicked

    private void cb_izin_catItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_izin_catItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == evt.SELECTED) {
            Entity_SIM ES = new Entity_SIM();
            ES.setIzin_name((String) cb_izin_cat.getSelectedItem());
            try {
                idIzin = simDao.getIDIzin(ES);
            } catch (RemoteException ex) {
                Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_cb_izin_catItemStateChanged

    private void tabelListAktivitasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelListAktivitasMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabelListAktivitasMouseClicked

    private void btn_tambah_actMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_tambah_actMouseClicked
        // TODO add your handling code here:
        Mode7();
        init_ReqAct_ID();
    }//GEN-LAST:event_btn_tambah_actMouseClicked

    private void cb_act_catItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_act_catItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == evt.SELECTED) {
            Entity_SIM ES = new Entity_SIM();
            ES.setAct_name((String) cb_act_cat.getSelectedItem());
            try {
                idActivity = simDao.getIDAct(ES);
            } catch (RemoteException ex) {
                Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_cb_act_catItemStateChanged

    private void btnSave_ActMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSave_ActMouseClicked
        // TODO add your handling code here:
        if (cb_act_cat.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Choose Izin Category", "Warning Message", JOptionPane.WARNING_MESSAGE);
            cb_act_cat.requestFocus();
        } else if (txt_act_desc.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Choose Izin Description", "Warning Message", JOptionPane.WARNING_MESSAGE);
            txt_act_desc.requestFocus();
        } else if (dt_act_dateStart.getDate().equals(null)) {
            JOptionPane.showMessageDialog(null, "Choose Izin Description", "Warning Message", JOptionPane.WARNING_MESSAGE);
            dt_act_dateStart.requestFocus();
        } else if (dt_act_dateEnd.getDate().equals(null)) {
            JOptionPane.showMessageDialog(null, "Choose Izin Description", "Warning Message", JOptionPane.WARNING_MESSAGE);
            dt_act_dateEnd.requestFocus();
        } else {
            Date date = new Date();
            System.out.println(date);

            Entity_SIM ES = new Entity_SIM();
            ES.setReq_act_id(txt_act_req_id.getText());
            ES.setReq_act_id_emp(idAdminSIM);
            ES.setReq_act_cat(idActivity);
            ES.setReq_act_date_start(dt_act_dateStart.getDate());
            ES.setReq_act_date_end(dt_act_dateEnd.getDate());
            ES.setReq_act_desc(txt_act_desc.getText());
            ES.setReq_act_created_date(date);
            try {
                int hasilsaveActivity = simDao.saveActivity(ES);
                if (hasilsaveActivity == 1) {
                    JOptionPane.showMessageDialog(null, "Save Activity Success, Wait For Approval", "Success Message", JOptionPane.INFORMATION_MESSAGE);
                    init_ReqAct_ID();
                    ClearFormActivity();
                } else {
                    JOptionPane.showMessageDialog(null, "Save Activity Failed", "Failed Message", JOptionPane.ERROR_MESSAGE);
                }
            } catch (RemoteException ex) {
                Logger.getLogger(HomePageClientSIM.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnSave_ActMouseClicked

    private void btnClear_ActMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClear_ActMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnClear_ActMouseClicked

    private void btnSaveError_simMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveError_simMouseClicked
        // TODO add your handling code here:
        String error_added_by = null;

        Entity_Signup ESU = new Entity_Signup();
        ESU.setName(txtSIMNama.getText());
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

    private void iconHome1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconHome1MouseClicked
        // TODO add your handling code here:
        Mode10();
    }//GEN-LAST:event_iconHome1MouseClicked

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
            java.util.logging.Logger.getLogger(HomePageClientSIM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomePageClientSIM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomePageClientSIM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomePageClientSIM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomePageClientSIM().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bgBackOffice;
    private javax.swing.JLabel bgClientCashierSIM;
    private javax.swing.JLabel bgClientSIM;
    private javax.swing.JLabel bgDepartment;
    private javax.swing.JLabel bgFrontOffice;
    private javax.swing.JLabel bgLandingPage;
    private javax.swing.JLabel bgLogoNOB;
    private javax.swing.JLabel bgSIM;
    private javax.swing.JLabel bgsSIMLogo;
    private javax.swing.JButton btnChangeStateSIM;
    private javax.swing.JLabel btnClearError_sim;
    private javax.swing.JLabel btnClear_Act;
    private javax.swing.JLabel btnClear_Izin;
    private javax.swing.JButton btnHideIPSIM;
    private javax.swing.JButton btnLoginSIM;
    private javax.swing.JLabel btnLogoutSIM;
    private javax.swing.JLabel btnMsgSIM;
    private javax.swing.JLabel btnNotifSIM;
    private javax.swing.JButton btnResetSIM;
    private javax.swing.JLabel btnSaveError_sim;
    private javax.swing.JLabel btnSave_Act;
    private javax.swing.JLabel btnSave_Izin;
    private javax.swing.JButton btnShowIPSIM;
    private javax.swing.JLabel btn_delete_error_sim;
    private javax.swing.JButton btn_tambah_act;
    private javax.swing.JButton btn_tambah_izin;
    private javax.swing.JButton btn_trans_clearall1;
    private javax.swing.JButton btn_trans_process1;
    private javax.swing.ButtonGroup buttongroup_Gender;
    private javax.swing.JComboBox<String> cb_act_cat;
    private javax.swing.JComboBox<String> cb_error_status_sim;
    private javax.swing.JComboBox<String> cb_izin_cat;
    private com.toedter.calendar.JDateChooser dtError_Date_sim;
    private com.toedter.calendar.JDateChooser dt_act_dateEnd;
    private com.toedter.calendar.JDateChooser dt_act_dateStart;
    private com.toedter.calendar.JDateChooser dt_izin_dateEnd;
    private com.toedter.calendar.JDateChooser dt_izin_dateStart;
    private javax.swing.JLabel iconAdminSIM;
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
    private javax.swing.JLabel iconSIMAddActivity;
    private javax.swing.JLabel iconSIMAddIzin;
    private javax.swing.JLabel iconSIMDashboard;
    private javax.swing.JLabel iconSIMFO;
    private javax.swing.JLabel iconWarehouse;
    private javax.swing.JLabel iconWrhErrorReport;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator100;
    private javax.swing.JSeparator jSeparator101;
    private javax.swing.JSeparator jSeparator102;
    private javax.swing.JSeparator jSeparator104;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JSeparator jSeparator99;
    private javax.swing.JLabel lblAddActSubTitle;
    private javax.swing.JLabel lblAddIzinSubTitle;
    private javax.swing.JLabel lblAktivitasTotalData;
    private javax.swing.JLabel lblDashboardTitle2;
    private javax.swing.JLabel lblDashboardTitleSIM;
    private javax.swing.JLabel lblErrorReportCasSubTitle;
    private javax.swing.JLabel lblErrorReportCasSubTitle1;
    private javax.swing.JLabel lblErrorReportTitleCas;
    private javax.swing.JLabel lblListActivity;
    private javax.swing.JLabel lblListTransactionPending;
    private javax.swing.JLabel lblSIM;
    private javax.swing.JLabel lblSIM1;
    private javax.swing.JLabel lblSIM2;
    private javax.swing.JLabel lblSIM3;
    private javax.swing.JLabel lblSIMAddActivity;
    private javax.swing.JLabel lblSIMAddIzin;
    private javax.swing.JLabel lblSIMListActivity;
    private javax.swing.JLabel lblSIMListIzinData;
    private javax.swing.JLabel lblStateBigSIM;
    private javax.swing.JLabel lblStateSIM;
    private javax.swing.JLabel lblTransInvoice_Confirm;
    private javax.swing.JLabel lblTransTotalDat;
    private javax.swing.JLabel lbl_act_desc;
    private javax.swing.JLabel lbl_act_reqid;
    private javax.swing.JLabel lbl_error_date_cas;
    private javax.swing.JLabel lbl_error_desc_cas;
    private javax.swing.JLabel lbl_error_id_cas;
    private javax.swing.JLabel lbl_error_status_cas;
    private javax.swing.JLabel lbl_error_title_cas;
    private javax.swing.JLabel lbl_izin_cat;
    private javax.swing.JLabel lbl_izin_cat1;
    private javax.swing.JLabel lbl_izin_dateend;
    private javax.swing.JLabel lbl_izin_dateend1;
    private javax.swing.JLabel lbl_izin_datestart;
    private javax.swing.JLabel lbl_izin_datestart1;
    private javax.swing.JLabel lbl_izin_desc;
    private javax.swing.JLabel lbl_izin_reqid;
    private javax.swing.JLabel lbl_list_error_sim;
    private javax.swing.JPanel menuSIMAddActivity;
    private javax.swing.JPanel menuSIMAddIzin;
    private javax.swing.JPanel menuWrhDashboard;
    private javax.swing.JPanel menuWrhErrorReport;
    private javax.swing.JPanel panelBackOffice;
    private javax.swing.JPanel panelDepartment;
    private javax.swing.JPanel panelFrontOffice;
    private javax.swing.JPanel panelLandingPage;
    private javax.swing.JLabel panelLoginWrh;
    private javax.swing.JPanel panelQRCode_trans;
    private javax.swing.JPanel panelSIMDashboard;
    private javax.swing.JPanel panelSIMLandingPage;
    private javax.swing.JPanel panelSIMLogin;
    private javax.swing.JPanel subpanelSIMActivity;
    private javax.swing.JPanel subpanelSIMAddActivity;
    private javax.swing.JPanel subpanelSIMAddIzin;
    private javax.swing.JPanel subpanelSIMDashboard;
    private javax.swing.JPanel subpanelSIMErrorReport;
    private javax.swing.JPanel subpanelSIMIzin;
    private javax.swing.JTable tabelDaftarError_Sim;
    private javax.swing.JScrollPane tabelDaftarListActivity;
    private javax.swing.JScrollPane tabelDaftarListIzinSP;
    private javax.swing.JScrollPane tabelDaftarReportSP1;
    private javax.swing.JTable tabelListAktivitas;
    private javax.swing.JTable tabelListIzin;
    private javax.swing.JPasswordField txtPasswordSIM;
    private javax.swing.JLabel txtSIMNama;
    private javax.swing.JLabel txtServerTimeSIM;
    private javax.swing.JTextField txtUsernameSIM;
    private javax.swing.JTextPane txt_act_desc;
    private javax.swing.JLabel txt_act_pending;
    private javax.swing.JLabel txt_act_rejected;
    private javax.swing.JTextField txt_act_req_id;
    private javax.swing.JLabel txt_act_success;
    private javax.swing.JLabel txt_act_total_data;
    private javax.swing.JTextField txt_count_notifACT;
    private javax.swing.JTextField txt_count_notifIZIN;
    private javax.swing.JTextArea txt_error_desc_sim;
    private javax.swing.JTextField txt_error_id_sim;
    private javax.swing.JTextField txt_error_title_sim;
    private javax.swing.JTextPane txt_izin_desc;
    private javax.swing.JLabel txt_izin_pending;
    private javax.swing.JLabel txt_izin_rejected;
    private javax.swing.JTextField txt_izin_req_id;
    private javax.swing.JLabel txt_izin_success;
    private javax.swing.JLabel txt_izin_total_data;
    private javax.swing.JLabel txt_stateBigSIM;
    private javax.swing.JLabel txt_stateSIM;
    private javax.swing.JLabel txt_trans_invoiceno_confirm;
    // End of variables declaration//GEN-END:variables
}
