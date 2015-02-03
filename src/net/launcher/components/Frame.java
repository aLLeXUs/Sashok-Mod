package net.launcher.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import net.launcher.run.Settings;
import net.launcher.utils.BaseUtils;
import net.launcher.utils.ImageUtils;
import net.launcher.utils.ThemeUtils;
import net.launcher.utils.ThreadUtils;
import static net.launcher.utils.BaseUtils.*;

import com.sun.awt.AWTUtilities;

public class Frame extends JFrame implements ActionListener, FocusListener {

    boolean b1 = false;
    boolean b2 = true;
    private static final long serialVersionUID = 1L;
    private static final Component Frame = null;
    public static String token = "null";
    public static boolean savetoken = false;

    public static Frame main;
    public Panel panel = new Panel(0);
    public Dragger dragger = new Dragger();
    public static Button toGame = new Button("Играть");
    public static Button toAuth = new Button("Авторизация");
    public static Button toLogout = new Button("Выход");
    public Button toOptions = new Button("Настройки");
    public JTextPane browser = new JTextPane();
    public JTextPane personalBrowser = new JTextPane();
    public JScrollPane bpane = new JScrollPane(browser);
    public JScrollPane personalBpane = new JScrollPane(personalBrowser);
    public static Textfield login = new Textfield();
    public static Passfield password = new Passfield();
    public Combobox servers = new Combobox(getServerNames(), 0);
    public Serverbar serverbar = new Serverbar();

    public LinkLabel[] links = new LinkLabel[Settings.links.length];

    public Dragbutton hide = new Dragbutton();
    public Dragbutton close = new Dragbutton();

    public Button update_exe = new Button("exe");
    public Button update_jar = new Button("jar");
    public Button update_no = new Button("Выход");

    public Checkbox loadnews = new Checkbox("Загружать новости");
    public Checkbox updatepr = new Checkbox("Принудительное обновление");
    public Checkbox cleanDir = new Checkbox("Очистить папку");
    public Checkbox fullscreen = new Checkbox("Запустить в полный экран");
    public Textfield memory = new Textfield();

    public Button options_close = new Button("Закрыть");

    public Frame() {
        try {
            ServerSocket socket = new ServerSocket(65534);
            Socket soc = new Socket(socket);
            soc.start();
        } catch (IOException var2) {
            JOptionPane.showMessageDialog(Frame, "Запуск второй копии лаунчера невозможен!", "Лаунчер уже запущен", javax.swing.JOptionPane.ERROR_MESSAGE);
            try {
                Class<?> af = Class.forName("java.lang.Shutdown");
                Method m = af.getDeclaredMethod("halt0", int.class);
                m.setAccessible(true);
                m.invoke(null, 1);
            } catch (Exception e) {
            }
        }

        //Подготовка окна
        setIconImage(BaseUtils.getLocalImage("favicon"));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.DARK_GRAY);
        setForeground(Color.DARK_GRAY);
        setLayout(new BorderLayout());
        setUndecorated(Settings.customframe && BaseUtils.getPlatform() != 0);
        if (isUndecorated()) {
            AWTUtilities.setWindowOpaque(this, false);
        }
        setResizable(false);

        for (int i = 0; i < links.length; i++) {
            String[] s = Settings.links[i].split("::");
            links[i] = new LinkLabel(s[0], s[1]);
            links[i].setEnabled(BaseUtils.checkLink(s[1]));
        }

        try {
            ThemeUtils.updateStyle(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Добавление слушателей
        toGame.addActionListener(this);
        toAuth.addActionListener(this);
        toLogout.addActionListener(this);
        toOptions.addActionListener(this);
        login.setText("Логин...");
        login.addActionListener(this);
        login.addFocusListener(this);
        password.setEchoChar('*');
        password.addActionListener(this);
        password.addFocusListener(this);
        String pass = getPropertyString("password");
        if (pass == null || pass.equals("-")) {
            b1 = true;
            b2 = false;
        }
        login.setVisible(true);
        password.setVisible(b1);
        toGame.setVisible(b2);
        toAuth.setVisible(b1);
        toLogout.setVisible(b2);
        if (toGame.isVisible()) {
            token = "token";
        }

        login.setEditable(b1);
        bpane.setOpaque(false);
        bpane.getViewport().setOpaque(false);
        bpane.setBorder(null);

        personalBpane.setOpaque(false);
        personalBpane.getViewport().setOpaque(false);
        personalBpane.setBorder(null);

        personalBrowser.setOpaque(false);
        personalBrowser.setBorder(null);
        personalBrowser.setContentType("text/html");
        personalBrowser.setEditable(false);
        personalBrowser.setFocusable(false);
        personalBrowser.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    openURL(e.getURL().toString());
                }
            }
        });

        browser.setOpaque(false);
        browser.setBorder(null);
        browser.setContentType("text/html");
        browser.setEditable(false);
        browser.setFocusable(false);
        browser.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if (Settings.useStandartWB) {
                        openURL(e.getURL().toString());
                    } else {
                        ThreadUtils.updateNewsPage(e.getURL().toString());
                    }
                }
            }
        });
        hide.addActionListener(this);
        close.addActionListener(this);

        update_exe.addActionListener(this);
        update_jar.addActionListener(this);
        update_no.addActionListener(this);
        servers.addMouseListener(new MouseListener() {
            public void mouseReleased(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseClicked(MouseEvent e) {
                if (servers.getPressed() || e.getButton() != MouseEvent.BUTTON1) {
                    return;
                }

                ThreadUtils.pollSelectedServer();
                setProperty("server", servers.getSelectedIndex());
            }
        });

        options_close.addActionListener(this);
        loadnews.addActionListener(this);
        fullscreen.addActionListener(this);

        login.setText(getPropertyString("login"));
        servers.setSelectedIndex(getPropertyInt("server"));

        addAuthComp();
        addFrameComp();
        add(panel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        validate();
        repaint();
        setVisible(true);
    }

    public void addFrameComp() {
        if (Settings.customframe) {
            panel.add(hide);
            panel.add(close);
            panel.add(dragger);
        }
    }

    public void setAuthComp() {
        panel.type = 0;
        panel.timer.stop();
        panel.removeAll();
        addFrameComp();
        addAuthComp();
        repaint();
    }

    /**
     * Добавление элементов авторизации
     */
    public void addAuthComp() {
        panel.add(servers);
        panel.add(serverbar);
        for (LinkLabel link : links) {
            panel.add(link);
        }
        panel.add(toGame);
        panel.add(toAuth);
        panel.add(toLogout);
        panel.add(toOptions);
        panel.add(login);
        panel.add(bpane);
        panel.add(password);
    }

    //Старт программы
    public static void start() {
        try {
            send("****launcher****");
            try {
                send("Setting new LaF...");
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                send("Fail setting LaF");
            }
            send("Running debug methods...");

            new Runnable() {
                public void run() {
                    Settings.onStart();
                }
            }.run();

            main = new Frame();

            ThreadUtils.updateNewsPage(buildUrl("news.php"));
            ThreadUtils.pollSelectedServer();
            try {
                main.memory.setText(String.valueOf(getPropertyInt("memory", 512)));
                main.fullscreen.setSelected(getPropertyBoolean("fullscreen"));
                main.loadnews.setSelected(getPropertyBoolean("loadnews", true));
            } catch (Exception e) {
            }
        } catch (Exception e) {
            throwException(e, main);
        }
    }

    public static String jar;

    @SuppressWarnings("deprecation")
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == hide) {
            setExtendedState(ICONIFIED);
        }
        if (e.getSource() == close || e.getSource() == update_no) {
            System.exit(0);
        }

        if (e.getSource() == update_exe) {
            jar = ".exe";
            new Thread() {
                public void run() {
                    try {
                        panel.type = 8;
                        update_exe.setEnabled(false);
                        update_no.setText("Отмена");
                        panel.repaint();
                        BaseUtils.updateLauncher();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        send("Error updating launcher!");
                        update_no.setText("Выйти");
                        update_exe.setEnabled(true);
                        panel.type = 9;
                        panel.repaint();
                    }
                }
            }.start();
        }

        if (e.getSource() == update_jar) {
            jar = ".jar";
            new Thread() {
                public void run() {
                    try {
                        panel.type = 8;
                        update_jar.setEnabled(false);
                        update_no.setText("Отмена");
                        panel.repaint();
                        BaseUtils.updateLauncher();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        send("Error updating launcher!");
                        update_no.setText("Выйти");
                        update_jar.setEnabled(true);
                        panel.type = 9;
                        panel.repaint();
                    }
                }
            }.start();
        }

        if (e.getSource() == toLogout) {
            setProperty("password", "-");
            password.setVisible(true);
            toGame.setVisible(false);
            toAuth.setVisible(true);
            toLogout.setVisible(false);
            token = "null";
            login.setEditable(true);
        }

        if (e.getSource() == login || e.getSource() == password || e.getSource() == toGame || e.getSource() == toAuth) {
            setProperty("login", login.getText());
            setProperty("server", servers.getSelectedIndex());
            panel.remove(hide);
            panel.remove(close);
            BufferedImage screen = ImageUtils.sceenComponent(panel);
            panel.removeAll();
            addFrameComp();
            panel.setAuthState(screen);
            ThreadUtils.auth();
        }

        if (e.getSource() == toOptions) {
            setOptions();
        }

        if (e.getSource() == options_close) {
            if (!memory.getText().equals(getPropertyString("memory"))) {
                try {
                    int i = Integer.parseInt(memory.getText());
                    setProperty("memory", i);
                } catch (Exception e1) {
                }
                restart();
            }
            setAuthComp();
        }

        if (e.getSource() == fullscreen || e.getSource() == loadnews) {
            setProperty("fullscreen", fullscreen.isSelected());
            setProperty("loadnews", loadnews.isSelected());
        }
    }

    public void focusGained(FocusEvent e) {
        if (e.getSource() == login && login.getText().equals("Логин...")) {
            login.setText(empty);
        }
    }

    public void focusLost(FocusEvent e) {
        if (e.getSource() == login && login.getText().equals(empty)) {
            login.setText("Логин...");
        }
    }

    public void setUpdateComp(String version) {
        panel.removeAll();
        addFrameComp();
        panel.setUpdateState(version);
        panel.add(update_exe);
        panel.add(update_jar);
        panel.add(update_no);
        repaint();
    }

    public void setUpdateState() {
        panel.removeAll();
        addFrameComp();
        panel.setUpdateStateMC();
        repaint();
    }

    public void setOptions() {
        panel.remove(hide);
        panel.remove(close);
        BufferedImage screen = ImageUtils.sceenComponent(panel);
        panel.removeAll();
        addFrameComp();
        panel.setOptions(screen);
        panel.add(loadnews);
        panel.add(updatepr);
        panel.add(cleanDir);
        panel.add(fullscreen);
        panel.add(memory);
        panel.add(options_close);
        repaint();
    }

    public void setLoading() {
        panel.remove(hide);
        panel.remove(close);
        BufferedImage screen = ImageUtils.sceenComponent(panel);
        panel.removeAll();
        addFrameComp();
        panel.setLoadingState(screen, "Выполнение...");
    }

    public void setError(String s) {
        panel.removeAll();
        addFrameComp();
        panel.setErrorState(s);
    }
}
