
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import nob.server.dao.impl.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import nob.dao.api.ILoginDao;
import nob.model.Entity_Signin;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author alimk
 */
public class HomePage_Server extends javax.swing.JFrame {

    String kodeSistem = "SERVER";
    String ip = null;
    String ipserver = null;
    String url = "jdbc:jtds:sqlserver://localhost:1433/NOBTech";
    String user = "sa";
    String password = "aditya12345";
    private ILoginDao login2Dao = null;
    String State_run_Server = null;

    /**
     * Creates new form HomePage_Server
     */
    public HomePage_Server() {
        initComponents();
        Mode1();
        ShowIPMode();
        set_TimeServer();
        State_run_Server = "Running";
        txt_stateBig.setText("Server is Well-Connected");

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Object[] options = {"Yes", "Cancel"};
                int reply = JOptionPane.showOptionDialog(null, "Are you sure to close the Server?", "Information", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                switch (reply) {
                    case 0:
                        try {
                            if (State_run_Server.equals("Running")) {
                                e.getWindow().dispose();
                                System.exit(0);
                            } else {
                                int hasilSetServerStatus = login2Dao.UpdateStatusOffline("SERVER");
                                int hasilSetMgmStatus = login2Dao.UpdateStatusOffline("MGM");
                                int hasilSetCasStatus = login2Dao.UpdateStatusOffline("CAS");
                                int hasilSetWrhStatus = login2Dao.UpdateStatusOffline("WRH");
                                int hasilSetKeuStatus = login2Dao.UpdateStatusOffline("KEU");
                                int hasilSetHRDStatus = login2Dao.UpdateStatusOffline("HRD");
                                if (hasilSetServerStatus == 1 && hasilSetMgmStatus == 1 && hasilSetCasStatus == 1 && hasilSetWrhStatus == 1 && hasilSetKeuStatus == 1 && hasilSetHRDStatus == 1) {
                                    e.getWindow().dispose();
                                    System.exit(0);
                                }
                            }
                        } catch (RemoteException ex) {
                            Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    default:
                        break;
                }
            }
        }
        );
    }

    public void Run_Server() {
        try {
            // mendaftarkan JDBC Driver
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            try {
                // Akses ke Komputer IP : 192.168.1.24 Port 1433
                url = "jdbc:jtds:sqlserver://localhost:1433/NOBTech";
                user = "sa";
                password = "aditya12345";
                // buat objek Connection
                Connection conn = DriverManager.getConnection(url, user, password);
                try {
                    // buat objek BarangDao dan SupplierDao
                    DistributorDao distributorDao = new DistributorDao(conn);
                    LoginDao loginDao = new LoginDao(conn);
                    ErrorReportDao errorDao = new ErrorReportDao(conn);
                    UserDao userDao = new UserDao(conn);
                    DepartmentDao departmentDao = new DepartmentDao(conn);
                    TaskDao taskDao = new TaskDao(conn);
                    TransactionDao transactionDao = new TransactionDao(conn);
                    SimulationDao simulationDao = new SimulationDao(conn);
                    WarehouseDao warehouseDao = new WarehouseDao(conn);
                    SIMDao simDao = new SIMDao(conn);
//                    KategoriDao kategoriDao = new KategoriDao(conn);
//                    PeralatanDao peralatanDao = new PeralatanDao(conn);
//                    RuanganDao ruanganDao = new RuanganDao(conn);
//                    EventDao eventDao = new EventDao(conn);
                    // buat objek RMI registry menggunakan port default RMI (1099)
                    Registry registry = LocateRegistry.createRegistry(1099);
                    // mendaftarkan objek barangDao dan supplierDao ke RMI Registry
                    registry.rebind("distributorDao", distributorDao);
                    registry.rebind("loginDao", loginDao);
                    registry.rebind("errorDao", errorDao);
                    registry.rebind("userDao", userDao);
                    registry.rebind("departmentDao", departmentDao);
                    registry.rebind("taskDao", taskDao);
                    registry.rebind("transactionDao", transactionDao);
                    registry.rebind("simulationDao", simulationDao);
                    registry.rebind("warehouseDao", warehouseDao);
                    registry.rebind("simDao", simDao);
//                    registry.rebind("kategoriDao", kategoriDao);
//                    registry.rebind("peralatanDao", peralatanDao);
//                    registry.rebind("ruanganDao", ruanganDao);
//                    registry.rebind("eventDao",eventDao);
                    txt_stateBig.setText("Server is Well-Connected");
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(null, "Technical Issue Happened, Call Admin", "Server Connection", JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Technical Issue Happened, Call Admin", "Server Connection", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init_LoginDao() {
        String url = "rmi://" + ip + ":1099/loginDao";
        try {
            //ambil referensi dari remote objek yang ada di server
            login2Dao = (ILoginDao) Naming.lookup(url);
        } catch (NotBoundException ex) {
            Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            //Logger.getLogger(FrmSupplier.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Koneksi ke Service" + url + "[GAGAL], cek kembali app server", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void SetServerStatus() {
        try {
            int hasilSetServerStatus = login2Dao.UpdateStatusOnline(kodeSistem);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getStatusServer() {
        try {
            String hasilgetStatusServer = login2Dao.CekClientStatus(kodeSistem);
            txt_state.setText(hasilgetStatusServer);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setIPServer() {
        Entity_Signin ES = new Entity_Signin();
        ES.setIp(ip);
        try {
            int hasilsetIPServer = login2Dao.UpdateIP(ES, kodeSistem);
            System.out.println(ip);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void CekStatusClient5Sec() {
        final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                // TODO add your handling code here:
                getClientStatusManagement();
                getClientStatusCashier();
                getClientStatusWarehouse();
                getClientStatusKeuangan();
                getClientStatusHRD();
                getColorIndicator();
                getStatusServer();
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    public void getColorIndicator() {
        if (txt_status_mgm.getText().equals("Online")) {
            lbl_status_client_Mgm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OnlineState.png")));
        } else if (txt_status_mgm.getText().equals("Idle")) {
            lbl_status_client_Mgm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/IdleState.png")));
        } else if (txt_status_mgm.getText().equals("Invisible")) {
            lbl_status_client_Mgm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/InvisState.png")));
        } else {
            lbl_status_client_Mgm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OffState.png")));
        }

        if (txt_status_cas.getText().equals("Online")) {
            lbl_status_client_Cas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OnlineState.png")));
        } else if (txt_status_cas.getText().equals("Idle")) {
            lbl_status_client_Cas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/IdleState.png")));
        } else if (txt_status_cas.getText().equals("Invisible")) {
            lbl_status_client_Cas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/InvisState.png")));
        } else {
            lbl_status_client_Cas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OffState.png")));
        }

        if (txt_status_wrh.getText().equals("Online")) {
            lbl_status_client_Wrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OnlineState.png")));
        } else if (txt_status_wrh.getText().equals("Idle")) {
            lbl_status_client_Wrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/IdleState.png")));
        } else if (txt_status_wrh.getText().equals("Invisible")) {
            lbl_status_client_Wrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/InvisState.png")));
        } else {
            lbl_status_client_Wrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OffState.png")));
        }

        if (txt_status_keu.getText().equals("Online")) {
            lbl_status_client_Keu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OnlineState.png")));
        } else if (txt_status_keu.getText().equals("Idle")) {
            lbl_status_client_Keu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/IdleState.png")));
        } else if (txt_status_keu.getText().equals("Invisible")) {
            lbl_status_client_Keu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/InvisState.png")));
        } else {
            lbl_status_client_Keu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OffState.png")));
        }

        if (txt_status_hrd.getText().equals("Online")) {
            lbl_status_client_Hrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OnlineState.png")));
        } else if (txt_status_hrd.getText().equals("Idle")) {
            lbl_status_client_Hrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/IdleState.png")));
        } else if (txt_status_hrd.getText().equals("Invisible")) {
            lbl_status_client_Hrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/InvisState.png")));
        } else {
            lbl_status_client_Hrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OffState.png")));
        }
    }

    public void getClientStatusManagement() {
        try {
            String hasilSetMgmStatus = login2Dao.CekClientStatus("MGM");
            txt_status_mgm.setText(hasilSetMgmStatus);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getClientStatusCashier() {
        try {
            String hasilSetCasStatus = login2Dao.CekClientStatus("CAS");
            txt_status_cas.setText(hasilSetCasStatus);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getClientStatusWarehouse() {
        try {
            String hasilSetWrhStatus = login2Dao.CekClientStatus("WRH");
            txt_status_wrh.setText(hasilSetWrhStatus);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getClientStatusKeuangan() {
        try {
            String hasilSetKeuStatus = login2Dao.CekClientStatus("KEU");
            txt_status_keu.setText(hasilSetKeuStatus);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getClientStatusHRD() {
        try {
            String hasilSetHrdStatus = login2Dao.CekClientStatus("HRD");
            txt_status_hrd.setText(hasilSetHrdStatus);
        } catch (RemoteException ex) {
            Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void killManagement() {
        try {
            int hasilKillMgmStatus = login2Dao.UpdateStatusKilledbyServer("MGM");
        } catch (RemoteException ex) {
            Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void killCashier() {
        try {
            int hasilKillCasStatus = login2Dao.UpdateStatusKilledbyServer("CAS");
        } catch (RemoteException ex) {
            Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void killWarehouse() {
        try {
            int hasilKillWrhStatus = login2Dao.UpdateStatusKilledbyServer("WRH");
        } catch (RemoteException ex) {
            Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void killKeuangan() {
        try {
            int hasilKillKeuStatus = login2Dao.UpdateStatusKilledbyServer("KEU");
        } catch (RemoteException ex) {
            Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void killHRD() {
        try {
            int hasilKillHrdStatus = login2Dao.UpdateStatusKilledbyServer("HRD");
        } catch (RemoteException ex) {
            Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Mode1() {
        //Mode Untuk Menampilkan Panel Home
        panelHome.setVisible(true);
        panelLogin.setVisible(false);
        panelDashboard.setVisible(false);
        lbl_status_client_Mgm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OffState.png")));
        lbl_status_client_Cas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OffState.png")));
        lbl_status_client_Keu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OffState.png")));
        lbl_status_client_Wrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OffState.png")));
        lbl_status_client_Hrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OffState.png")));
    }

    public void Mode2() {
        //Mode Untuk Menampilkan Panel Login Server
        panelHome.setVisible(false);
        panelLogin.setVisible(true);
        panelDashboard.setVisible(false);
    }

    public void Mode3() {
        //Mode Untuk Menampilkan Panel Dashboard
        panelHome.setVisible(false);
        panelLogin.setVisible(false);
        panelDashboard.setVisible(true);
    }

    public void get_ip_address() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            ip = address.getHostAddress();
            Object[] options = {"Use IP for Login", "Cancel"};
            int reply = JOptionPane.showOptionDialog(null, "Your Server IP Address : " + ip, "IP Address",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (reply == 0) {
                txt_ipaddress.setText(ip);
            } else {
                //donothing
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
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
                        Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        };
        clock.start();
    }

    public void clear() {
        txt_ipaddress.setText("");
        txt_passwordserver.setText("");
    }

    public void HideIPMode() {
        btnShowIP.setVisible(false);
        btnHideIP.setVisible(true);
    }

    public void ShowIPMode() {
        btnShowIP.setVisible(true);
        btnHideIP.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenu1 = new javax.swing.JMenu();
        panelDashboard = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        subpanelServerDashboard = new javax.swing.JPanel();
        lblDashboardTitle2 = new javax.swing.JLabel();
        lblDashboardTitleServer = new javax.swing.JLabel();
        jSeparator19 = new javax.swing.JSeparator();
        txt_status_mgm = new javax.swing.JLabel();
        txt_status_cas = new javax.swing.JLabel();
        txt_status_wrh = new javax.swing.JLabel();
        txt_status_keu = new javax.swing.JLabel();
        txt_status_hrd = new javax.swing.JLabel();
        lbl_status_client_Mgm = new javax.swing.JLabel();
        lbl_status_client_Cas = new javax.swing.JLabel();
        lbl_status_client_Wrh = new javax.swing.JLabel();
        lbl_status_client_Keu = new javax.swing.JLabel();
        lbl_status_client_Hrd = new javax.swing.JLabel();
        lbl_client_management = new javax.swing.JLabel();
        lbl_client_cashier = new javax.swing.JLabel();
        lbl_client_warehouse = new javax.swing.JLabel();
        lbl_cleint_keuangan = new javax.swing.JLabel();
        lbl_client_hrd = new javax.swing.JLabel();
        subpanelDashboard = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
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
        txtServerIP = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        panelHome = new javax.swing.JPanel();
        bgLogo = new javax.swing.JLabel();
        bgServer = new javax.swing.JLabel();
        panelLogin = new javax.swing.JPanel();
        txt_ipaddress = new javax.swing.JTextField();
        txt_passwordserver = new javax.swing.JPasswordField();
        btnCheckIP = new javax.swing.JButton();
        btnLoginServer = new javax.swing.JButton();
        bgLoginServer = new javax.swing.JLabel();

        jMenu1.setText("jMenu1");
        jPopupMenu1.add(jMenu1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("NOB Server Home Page");
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelDashboard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelDashboard.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 170, 10));

        subpanelServerDashboard.setBackground(new java.awt.Color(255, 255, 255));
        subpanelServerDashboard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblDashboardTitle2.setFont(new java.awt.Font("Gotham Light", 0, 18)); // NOI18N
        lblDashboardTitle2.setForeground(java.awt.Color.gray);
        lblDashboardTitle2.setText("Server System");
        subpanelServerDashboard.add(lblDashboardTitle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 53, -1, -1));

        lblDashboardTitleServer.setFont(new java.awt.Font("Gotham Bold", 0, 36)); // NOI18N
        lblDashboardTitleServer.setForeground(java.awt.Color.gray);
        lblDashboardTitleServer.setText("DASHBOARD");
        subpanelServerDashboard.add(lblDashboardTitleServer, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        jSeparator19.setForeground(new java.awt.Color(241, 241, 241));
        subpanelServerDashboard.add(jSeparator19, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 930, 20));

        txt_status_mgm.setFont(new java.awt.Font("Gotham Light", 1, 14)); // NOI18N
        txt_status_mgm.setForeground(new java.awt.Color(255, 255, 255));
        txt_status_mgm.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_status_mgm.setText("OFFLINE");
        subpanelServerDashboard.add(txt_status_mgm, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 220, -1, -1));

        txt_status_cas.setFont(new java.awt.Font("Gotham Light", 1, 14)); // NOI18N
        txt_status_cas.setForeground(new java.awt.Color(255, 255, 255));
        txt_status_cas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_status_cas.setText("OFFLINE");
        subpanelServerDashboard.add(txt_status_cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 220, -1, -1));

        txt_status_wrh.setFont(new java.awt.Font("Gotham Light", 1, 14)); // NOI18N
        txt_status_wrh.setForeground(new java.awt.Color(255, 255, 255));
        txt_status_wrh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_status_wrh.setText("OFFLINE");
        subpanelServerDashboard.add(txt_status_wrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 220, -1, -1));

        txt_status_keu.setFont(new java.awt.Font("Gotham Light", 1, 14)); // NOI18N
        txt_status_keu.setForeground(new java.awt.Color(255, 255, 255));
        txt_status_keu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_status_keu.setText("OFFLINE");
        subpanelServerDashboard.add(txt_status_keu, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 440, -1, -1));

        txt_status_hrd.setFont(new java.awt.Font("Gotham Light", 1, 14)); // NOI18N
        txt_status_hrd.setForeground(new java.awt.Color(255, 255, 255));
        txt_status_hrd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_status_hrd.setText("OFFLINE");
        subpanelServerDashboard.add(txt_status_hrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 440, -1, -1));

        lbl_status_client_Mgm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OffState.png"))); // NOI18N
        subpanelServerDashboard.add(lbl_status_client_Mgm, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 220, -1, -1));

        lbl_status_client_Cas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OffState.png"))); // NOI18N
        subpanelServerDashboard.add(lbl_status_client_Cas, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 220, -1, -1));

        lbl_status_client_Wrh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OffState.png"))); // NOI18N
        subpanelServerDashboard.add(lbl_status_client_Wrh, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 220, -1, -1));

        lbl_status_client_Keu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OffState.png"))); // NOI18N
        subpanelServerDashboard.add(lbl_status_client_Keu, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 440, -1, -1));

        lbl_status_client_Hrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OffState.png"))); // NOI18N
        subpanelServerDashboard.add(lbl_status_client_Hrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 440, -1, -1));

        lbl_client_management.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/iconBackOfficeManagement.png"))); // NOI18N
        lbl_client_management.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_client_managementMouseClicked(evt);
            }
        });
        subpanelServerDashboard.add(lbl_client_management, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        lbl_client_cashier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/iconFrontOfficeCashier.png"))); // NOI18N
        lbl_client_cashier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_client_cashierMouseClicked(evt);
            }
        });
        subpanelServerDashboard.add(lbl_client_cashier, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 110, -1, -1));

        lbl_client_warehouse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/iconBackOfficeWarehouse.png"))); // NOI18N
        lbl_client_warehouse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_client_warehouseMouseClicked(evt);
            }
        });
        subpanelServerDashboard.add(lbl_client_warehouse, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 110, -1, -1));

        lbl_cleint_keuangan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/iconBackOfficeKeuangan.png"))); // NOI18N
        subpanelServerDashboard.add(lbl_cleint_keuangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 330, -1, -1));

        lbl_client_hrd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/iconBackOfficeHRD.png"))); // NOI18N
        lbl_client_hrd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_client_hrdMouseClicked(evt);
            }
        });
        subpanelServerDashboard.add(lbl_client_hrd, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 330, -1, -1));

        panelDashboard.add(subpanelServerDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 143, 1010, 536));

        subpanelDashboard.setBackground(new java.awt.Color(241, 241, 241));
        subpanelDashboard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        subpanelDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                subpanelDashboardMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                subpanelDashboardMouseExited(evt);
            }
        });
        subpanelDashboard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/IconDashboard.png"))); // NOI18N
        subpanelDashboard.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 7, -1, -1));

        panelDashboard.add(subpanelDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 275, 170, 30));
        panelDashboard.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 170, 10));

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
        panelDashboard.add(btnChangeState, new org.netbeans.lib.awtextra.AbsoluteConstraints(83, 230, 100, -1));

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
        panelDashboard.add(btnHideIP, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 230, 70, -1));

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
        panelDashboard.add(btnShowIP, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 230, -1, -1));

        txtServerTime.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        txtServerTime.setText("SERVER TIME");
        panelDashboard.add(txtServerTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 693, -1, -1));

        txt_stateBig.setFont(new java.awt.Font("Gotham Light", 0, 14)); // NOI18N
        txt_stateBig.setText("SERVER STATE");
        panelDashboard.add(txt_stateBig, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 693, -1, -1));

        lblStateBig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OnlineState.png"))); // NOI18N
        panelDashboard.add(lblStateBig, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 693, -1, -1));

        lblState.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OnlineState.png"))); // NOI18N
        panelDashboard.add(lblState, new org.netbeans.lib.awtextra.AbsoluteConstraints(78, 208, -1, -1));

        txt_state.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        txt_state.setText("Online");
        panelDashboard.add(txt_state, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 210, -1, -1));

        jLabel7.setFont(new java.awt.Font("Gotham Medium", 1, 11)); // NOI18N
        jLabel7.setText("Keyko");
        panelDashboard.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(595, 695, -1, -1));

        jLabel6.setFont(new java.awt.Font("Gotham Light", 2, 11)); // NOI18N
        jLabel6.setText("powered by");
        panelDashboard.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 695, -1, -1));

        jLabel5.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        jLabel5.setText("NOB Tech - ");
        panelDashboard.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(465, 695, -1, -1));

        txtServerIP.setFont(new java.awt.Font("Gotham Medium", 1, 12)); // NOI18N
        txtServerIP.setText("Server");
        panelDashboard.add(txtServerIP, new org.netbeans.lib.awtextra.AbsoluteConstraints(79, 190, -1, -1));

        jLabel3.setFont(new java.awt.Font("Gotham Light", 0, 11)); // NOI18N
        jLabel3.setText("Hello, ");
        panelDashboard.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 170, -1, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/AdminLogo.png"))); // NOI18N
        panelDashboard.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/ServerAdminPage2.png"))); // NOI18N
        panelDashboard.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 721));

        panelHome.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bgLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/LogoNOBV1.png"))); // NOI18N
        bgLogo.setToolTipText("");
        bgLogo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bgLogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bgLogoMouseClicked(evt);
            }
        });
        panelHome.add(bgLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 200, -1, -1));

        bgServer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/HomePageServer.png"))); // NOI18N
        panelHome.add(bgServer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 720));

        panelLogin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_ipaddress.setEnabled(false);
        panelLogin.add(txt_ipaddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 380, 230, 40));
        panelLogin.add(txt_passwordserver, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 470, 230, 40));

        btnCheckIP.setText("CHECK IP");
        btnCheckIP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCheckIPMouseClicked(evt);
            }
        });
        panelLogin.add(btnCheckIP, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 550, -1, -1));

        btnLoginServer.setText("LOGIN");
        btnLoginServer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLoginServerMouseClicked(evt);
            }
        });
        btnLoginServer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnLoginServerKeyPressed(evt);
            }
        });
        panelLogin.add(btnLoginServer, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 550, -1, -1));

        bgLoginServer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/LoginServerBg.png"))); // NOI18N
        panelLogin.add(bgLoginServer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(panelLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 721));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void bgLogoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bgLogoMouseClicked
        // TODO add your handling code here:
        Mode2();
    }//GEN-LAST:event_bgLogoMouseClicked

    private void btnCheckIPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCheckIPMouseClicked
        // TODO add your handling code here:
        get_ip_address();
    }//GEN-LAST:event_btnCheckIPMouseClicked

    private void btnLoginServerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLoginServerMouseClicked
        // TODO add your handling code here:
        if (txt_ipaddress.getText().equals(ip) && txt_passwordserver.getText().equals("nobnumberone")) {
            JOptionPane.showMessageDialog(null, "Login Success", "Login Success", JOptionPane.INFORMATION_MESSAGE);
            Mode3();
            Run_Server();
            init_LoginDao();
            SetServerStatus();
            CekStatusClient5Sec();
            setIPServer();
        } else {
            JOptionPane.showMessageDialog(null, "IP and Password Does Not Exist", "Login Failed", JOptionPane.ERROR_MESSAGE);
            clear();
            txt_ipaddress.requestFocus();
        }
    }//GEN-LAST:event_btnLoginServerMouseClicked

    private void btnHideIPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHideIPMouseClicked
        // TODO add your handling code here:
        txtServerIP.setText("Server");
        ShowIPMode();
    }//GEN-LAST:event_btnHideIPMouseClicked

    private void btnShowIPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnShowIPMouseClicked
        // TODO add your handling code here:
        txtServerIP.setText(ip);
        HideIPMode();
    }//GEN-LAST:event_btnShowIPMouseClicked

    private void btnChangeStateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChangeStateMouseClicked
        // TODO add your handling code here:
        int hasilUpdateStatusServer = 0;
        Object[] options = {"Online", "Idle", "Invisible", "Offline"};
        int reply = JOptionPane.showOptionDialog(null, "Choose your server state : ", "State Chooser", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (reply) {
            case 0:
                if (txt_state.getText().equals("Online")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Online", "Warning Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int hasilSetServerStatus = login2Dao.UpdateStatusOnline(kodeSistem);
                        if (hasilSetServerStatus == 1) {
                            txt_stateBig.setText("Server is Well-Connected");
                            lblState.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OnlineState.png")));
                            lblStateBig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OnlineState.png")));
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case 1:
                if (txt_state.getText().equals("Idle")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Idle", "Warning Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int hasilSetServerStatus = login2Dao.UpdateStatusIdle(kodeSistem);
                        if (hasilSetServerStatus == 1) {
                            txt_stateBig.setText("Server is Well-Connected");
                            lblState.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/IdleState.png")));
                            lblStateBig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/IdleState.png")));
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case 2:
                if (txt_state.getText().equals("Invisible")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Invisible", "Warning Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int hasilSetServerStatus = login2Dao.UpdateStatusInvisible(kodeSistem);
                        if (hasilSetServerStatus == 1) {
                            txt_stateBig.setText("Server is Well-Connected");
                            lblState.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/InvisState.png")));
                            lblStateBig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/InvisState.png")));
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case 3:
                if (txt_state.getText().equals("Offline")) {
                    JOptionPane.showMessageDialog(null, "You Are Already Offline", "Warning Message", JOptionPane.WARNING_MESSAGE);
                    System.exit(0);
                } else {
                    try {
                        int hasilSetServerStatus = login2Dao.UpdateStatusServerOffline(kodeSistem);
                        int hasilSetMgmStatus = login2Dao.UpdateStatusDisconnect("MGM");
                        int hasilSetCasStatus = login2Dao.UpdateStatusDisconnect("CAS");
                        int hasilSetWrhStatus = login2Dao.UpdateStatusDisconnect("WRH");
                        int hasilSetKeuStatus = login2Dao.UpdateStatusDisconnect("KEU");
                        int hasilSetHrdStatus = login2Dao.UpdateStatusDisconnect("HRD");
                        if (hasilSetServerStatus == 1) {
                            txt_stateBig.setText("Server is Disconnected");
                            lblState.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OffState.png")));
                            lblStateBig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nob/server/design/OffState.png")));
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            //donothing
            default:
                break;
        }
    }//GEN-LAST:event_btnChangeStateMouseClicked

    private void subpanelDashboardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subpanelDashboardMouseEntered
        // TODO add your handling code here:
        Color lightGreen = new Color(0, 207, 152);
        subpanelDashboard.setBackground(lightGreen);
    }//GEN-LAST:event_subpanelDashboardMouseEntered

    private void subpanelDashboardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subpanelDashboardMouseExited
        // TODO add your handling code here:
        Color lightGray = new Color(241, 241, 241);
        subpanelDashboard.setBackground(lightGray);
    }//GEN-LAST:event_subpanelDashboardMouseExited

    private void lbl_client_managementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_client_managementMouseClicked
        // TODO add your handling code here:
        String hasilgetMgmStatus = null;
        String hasilgetServerStatus = null;

        int hasilsetMgmOnline = 0;

        try {
            hasilgetMgmStatus = login2Dao.CekClientStatus("MGM");
            hasilgetServerStatus = login2Dao.CekClientStatus("SERVER");
        } catch (RemoteException ex) {
            Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (hasilgetServerStatus.equals("Server Offline")) {
            JOptionPane.showMessageDialog(null, "Open Connection [Server] Fist ! Set Server State to be [Online]", "Failed to Reconnect", JOptionPane.INFORMATION_MESSAGE);
        } else if (hasilgetMgmStatus.equals("Killed by Server") || hasilgetMgmStatus.equals("Disconnect")) {
            Object[] options = {"Yes", "Cancel"};
            int reply = JOptionPane.showOptionDialog(null, "Are You Sure To Open Connection [Management] ? ", "Information", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (reply == 0) {
                try {
                    hasilsetMgmOnline = login2Dao.UpdateStatusOnline("MGM");
                    if (hasilsetMgmOnline == 1) {
                        JOptionPane.showMessageDialog(null, "Management Reconnected...", "Reconnecting", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Can't Reconnect ..", "Failed to Reconnect", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                //donothing
            }
        } else {
            Object[] options = {"Yes", "Cancel"};
            int reply = JOptionPane.showOptionDialog(null, "Are You Sure To Kill [MANAGEMENT] Session ? ", "Information", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (reply == 0) {
                killManagement();
            } else {
                //donothing
            }
        }
    }//GEN-LAST:event_lbl_client_managementMouseClicked

    private void lbl_client_cashierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_client_cashierMouseClicked
        // TODO add your handling code here:
        String hasilgetCasStatus = null;
        String hasilgetServerStatus = null;

        int hasilsetCasOnline = 0;

        try {
            hasilgetCasStatus = login2Dao.CekClientStatus("CAS");
            hasilgetServerStatus = login2Dao.CekClientStatus("SERVER");
        } catch (RemoteException ex) {
            Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (hasilgetServerStatus.equals("Server Offline")) {
            JOptionPane.showMessageDialog(null, "Open Connection [Server] Fist ! Set Server State to be [Online]", "Failed to Reconnect", JOptionPane.INFORMATION_MESSAGE);
        } else if (hasilgetCasStatus.equals("Killed by Server") || hasilgetCasStatus.equals("Disconnect")) {
            Object[] options = {"Yes", "Cancel"};
            int reply = JOptionPane.showOptionDialog(null, "Are You Sure To Open Connection [Cashier] ? ", "Information", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (reply == 0) {
                try {
                    hasilsetCasOnline = login2Dao.UpdateStatusOnline("CAS");
                    if (hasilsetCasOnline == 1) {
                        JOptionPane.showMessageDialog(null, "Cashier Reconnected...", "Reconnecting", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Can't Reconnect ..", "Failed to Reconnect", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                //donothing
            }
        } else {
            Object[] options = {"Yes", "Cancel"};
            int reply = JOptionPane.showOptionDialog(null, "Are You Sure To Kill [CASHIER] Session ? ", "Information", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (reply == 0) {
                killCashier();
            } else {
                //donothing
            }
        }
    }//GEN-LAST:event_lbl_client_cashierMouseClicked

    private void lbl_client_warehouseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_client_warehouseMouseClicked
        // TODO add your handling code here:
        String hasilgetWrhStatus = null;
        String hasilgetServerStatus = null;

        int hasilsetWrhOnline = 0;

        try {
            hasilgetWrhStatus = login2Dao.CekClientStatus("WRH");
            hasilgetServerStatus = login2Dao.CekClientStatus("SERVER");
        } catch (RemoteException ex) {
            Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (hasilgetServerStatus.equals("Server Offline")) {
            JOptionPane.showMessageDialog(null, "Open Connection [Server] Fist ! Set Server State to be [Online]", "Failed to Reconnect", JOptionPane.INFORMATION_MESSAGE);
        } else if (hasilgetWrhStatus.equals("Killed by Server")) {
            Object[] options = {"Yes", "Cancel"};
            int reply = JOptionPane.showOptionDialog(null, "Are You Sure To Open Connection [Warehouse] ? ", "Information", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (reply == 0) {
                try {
                    hasilsetWrhOnline = login2Dao.UpdateStatusOnline("WRH");
                    if (hasilsetWrhOnline == 1) {
                        JOptionPane.showMessageDialog(null, "Management Reconected...", "Reconnecting", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Can't Reconnect ..", "Failed to Reconnect", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                //donothing
            }
        } else {
            Object[] options = {"Yes", "Cancel"};
            int reply = JOptionPane.showOptionDialog(null, "Are You Sure To Kill [WAREHOUSE] Session ? ", "Information", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (reply == 0) {
                killWarehouse();
            } else {
                //donothing
            }
        }
    }//GEN-LAST:event_lbl_client_warehouseMouseClicked

    private void lbl_client_hrdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_client_hrdMouseClicked
        // TODO add your handling code here:
        String hasilgetHrdStatus = null;
        String hasilgetServerStatus = null;
        int hasilsetHrdOnline = 0;

        try {
            hasilgetHrdStatus = login2Dao.CekClientStatus("HRD");
            hasilgetServerStatus = login2Dao.CekClientStatus("SERVER");
        } catch (RemoteException ex) {
            Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (hasilgetServerStatus.equals("Server Offline")) {
            JOptionPane.showMessageDialog(null, "Open Connection [Server] Fist ! Set Server State to be [Online]", "Failed to Reconnect", JOptionPane.INFORMATION_MESSAGE);
        } else if (hasilgetHrdStatus.equals("Killed by Server")) {
            Object[] options = {"Yes", "Cancel"};
            int reply = JOptionPane.showOptionDialog(null, "Are You Sure To Open Connection [HRD] ? ", "Information", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (reply == 0) {
                try {
                    hasilsetHrdOnline = login2Dao.UpdateStatusOnline("HRD");
                    if (hasilsetHrdOnline == 1) {
                        JOptionPane.showMessageDialog(null, "Management Reconected...", "Reconnecting", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Can't Reconnect ..", "Failed to Reconnect", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(HomePage_Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                //donothing
            }
        } else {
            Object[] options = {"Yes", "Cancel"};
            int reply = JOptionPane.showOptionDialog(null, "Are You Sure To Kill [HRD] Session ? ", "Information", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (reply == 0) {
                killHRD();
            } else {
                //donothing
            }
        }
    }//GEN-LAST:event_lbl_client_hrdMouseClicked

    private void btnLoginServerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnLoginServerKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLoginServerKeyPressed

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
            java.util.logging.Logger.getLogger(HomePage_Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomePage_Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomePage_Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomePage_Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomePage_Server().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bgLoginServer;
    private javax.swing.JLabel bgLogo;
    private javax.swing.JLabel bgServer;
    private javax.swing.JButton btnChangeState;
    private javax.swing.JButton btnCheckIP;
    private javax.swing.JButton btnHideIP;
    private javax.swing.JButton btnLoginServer;
    private javax.swing.JButton btnShowIP;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblDashboardTitle2;
    private javax.swing.JLabel lblDashboardTitleServer;
    private javax.swing.JLabel lblState;
    private javax.swing.JLabel lblStateBig;
    private javax.swing.JLabel lbl_cleint_keuangan;
    private javax.swing.JLabel lbl_client_cashier;
    private javax.swing.JLabel lbl_client_hrd;
    private javax.swing.JLabel lbl_client_management;
    private javax.swing.JLabel lbl_client_warehouse;
    private javax.swing.JLabel lbl_status_client_Cas;
    private javax.swing.JLabel lbl_status_client_Hrd;
    private javax.swing.JLabel lbl_status_client_Keu;
    private javax.swing.JLabel lbl_status_client_Mgm;
    private javax.swing.JLabel lbl_status_client_Wrh;
    private javax.swing.JPanel panelDashboard;
    private javax.swing.JPanel panelHome;
    private javax.swing.JPanel panelLogin;
    private javax.swing.JPanel subpanelDashboard;
    private javax.swing.JPanel subpanelServerDashboard;
    private javax.swing.JLabel txtServerIP;
    private javax.swing.JLabel txtServerTime;
    private javax.swing.JTextField txt_ipaddress;
    private javax.swing.JPasswordField txt_passwordserver;
    private javax.swing.JLabel txt_state;
    private javax.swing.JLabel txt_stateBig;
    private javax.swing.JLabel txt_status_cas;
    private javax.swing.JLabel txt_status_hrd;
    private javax.swing.JLabel txt_status_keu;
    private javax.swing.JLabel txt_status_mgm;
    private javax.swing.JLabel txt_status_wrh;
    // End of variables declaration//GEN-END:variables
}
