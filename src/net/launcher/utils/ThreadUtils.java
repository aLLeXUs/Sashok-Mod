package net.launcher.utils;

import static net.launcher.utils.BaseUtils.buildUrl;
import static net.launcher.utils.BaseUtils.getPropertyString;
import static net.launcher.utils.BaseUtils.setProperty;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import net.y;
import net.launcher.components.Frame;
import net.launcher.components.PersonalContainer;
import net.launcher.run.Settings;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class ThreadUtils {

    public static UpdaterThread updaterThread;
    public static Thread serverPollThread;
    static String d = y.e() + "1234";

    public static void updateNewsPage(final String url) {
        BaseUtils.send("Updating news page...");
        if (!BaseUtils.getPropertyBoolean("loadnews", true)) {
            Frame.main.browser.setText("<center><font color=\"#F0F0F0\" style=\"font-family:Tahoma\">Загрузка новостей не включена</font></center>");
            return;
        }
        Frame.main.browser.setText("<center><font color=\"#F0F0F0\" style=\"font-family:Tahoma\">Обновление страницы...</font></center>");
        Thread t = new Thread() {
            public void run() {
                try {
                    Frame.main.browser.setPage(url);
                    BaseUtils.send("Updating news page sucessful!");
                } catch (Exception e) {
                    Frame.main.browser.setText("<center><font color=\"#FF0000\" style=\"font-family:Tahoma\"><b>Ошибка загрузки новостей:<br>" + e.toString() + "</b></font></center>");
                    BaseUtils.send("Updating news page fail! (" + e.toString() + ")");
                }
                interrupt();
            }
        };
        t.setName("Update news thread");
        t.start();
    }
    static String token = new String(Frame.password.getPassword());

    public static void auth() {
        BaseUtils.send("Logging in, login: " + Frame.login.getText());
        Thread t = new Thread() {
            public void run() {
                try {

                    boolean error = false;
                    token = new String(Frame.password.getPassword());
                    if (Frame.token.equals("token")) {
                        try {
                            token = decrypt(getPropertyString("password"), d);
                        } catch (Exception e) {
                            Frame.main.panel.tmpString = "Ошибка подключения";
                            error = true;
                        }
                    }
                    String answer2 = BaseUtils.execute(BaseUtils.buildUrl("launcher.php"), new Object[]{
                        "action", encrypt("auth:" + BaseUtils.getClientName() + ":" + Frame.login.getText() + ":" + token + ":" + GuardUtils.hash(ThreadUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI().toURL()) + ":" + Frame.token, Settings.key2),});
                    BaseUtils.send(answer2);
                    String answer = null;
                    try {
                        answer = decrypt(answer2, Settings.key1);
                    } catch (Exception e) {
                    }
                    if (answer == null) {
                        Frame.main.panel.tmpString = "Ошибка подключения";
                        error = true;
                    } else if (answer.contains("badlauncher<$>")) {
                        Frame.main.setUpdateComp(answer.replace("badlauncher<$>_", ""));
                        return;
                    } else if (answer.contains("errorLogin<$>")) {
                        Frame.main.panel.tmpString = "Ошибка авторизации (Логин, пароль)";
                        error = true;
                    } else if (answer.contains("errorsql<$>")) {
                        Frame.main.panel.tmpString = "Ошибка sql";
                        error = true;
                    } else if (answer.contains("client<$>")) {
                        Frame.main.panel.tmpString = "Ошибка: " + answer.replace("client<$>", "клиент") + " не найден";
                        error = true;
                    } else if (answer.contains("temp<$>")) {
                        Frame.main.panel.tmpString = "Подождите, перед следущей попыткой ввода (Логин Пароль)";
                        error = true;
                    } else if (answer.contains("badhash<$>")) {
                        Frame.main.panel.tmpString = "Ошибка: Неподдерживаемый способ хеширования";
                        error = true;
                    } else if (answer.split("<br>").length != 4) {
                        Frame.main.panel.tmpString = answer;
                        error = true;
                    }
                    if (error) {
                        Frame.main.panel.tmpColor = Color.red;
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                        }
                        Frame.main.setAuthComp();
                    } else {
                        String version = answer.split("<br>")[0].split("<:>")[0];

                        if (!version.equals(Settings.masterVersion)) {
                            Frame.main.setUpdateComp(version);
                            return;
                        }
                        BaseUtils.send("Logging in successful");

                        setProperty("password", encrypt(answer.split("<br>")[2].split("<br>")[0], d).replaceAll("\n|\r\n", ""));
                        if (Frame.token.equals("null")) {
                            Frame.password.setVisible(false);
                            Frame.toGame.setVisible(true);
                            Frame.toAuth.setVisible(false);
                            Frame.toLogout.setVisible(true);
                            Frame.token = "token";
                            Frame.login.setEditable(false);
                            Frame.main.setAuthComp();
                            return;
                        }
                        runUpdater(answer);
                    }
                    interrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.setName("Auth thread");
        t.start();
    }

    public static void runUpdater(String answer) {
        boolean zipupdate = false;
        boolean asupdate = false;
        List<String> files = GuardUtils.updateMods(answer);

        String folder = BaseUtils.getMcDir().getAbsolutePath() + File.separator;
        String asfolder = BaseUtils.getAssetsDir().getAbsolutePath() + File.separator;
        if (!answer.split("<br>")[0].split("<:>")[2].split("<>")[0].equals(BaseUtils.getPropertyString(BaseUtils.getClientName() + "_zipmd5"))
                || !new File(folder + "config").exists()
                || Frame.main.updatepr.isSelected()) {
            GuardUtils.filesize += Integer.parseInt(answer.split("<br>")[0].split("<:>")[2].split("<>")[1]);
            files.add("/" + BaseUtils.getClientName() + "/config.zip");
            zipupdate = true;
        }

        if (!Settings.assetsfolder) {
            if (!answer.split("<br>")[0].split("<:>")[3].split("<>")[0].equals(BaseUtils.getPropertyString("assets_aspmd5"))
                    || !new File(asfolder + "assets").exists()
                    || Frame.main.updatepr.isSelected()) {
                GuardUtils.filesize += Integer.parseInt(answer.split("<br>")[0].split("<:>")[3].split("<>")[1]);
                files.add("/assets.zip");
                asupdate = true;
            }
        }

        BaseUtils.send("---- Filelist start ----");
        for (Object s : files.toArray()) {
            BaseUtils.send("- " + (String) s);
        }
        BaseUtils.send("---- Filelist end ----");
        BaseUtils.send("Running updater...");
        updaterThread = new UpdaterThread(files, zipupdate, asupdate, answer);
        updaterThread.setName("Updater thread");
        Frame.main.setUpdateState();
        updaterThread.run();
    }

    public static void pollSelectedServer() {
        try {
            serverPollThread.interrupt();
            serverPollThread = null;
        } catch (Exception e) {
        }

        BaseUtils.send("Refreshing server state... (" + Frame.main.servers.getSelected() + ")");
        serverPollThread = new Thread() {
            public void run() {
                Frame.main.serverbar.updateBar("Обновление...", BaseUtils.genServerIcon(new String[]{null, "0", null}));
                int sindex = Frame.main.servers.getSelectedIndex();
                String ip = Settings.servers[sindex].split(", ")[1];
                int port = BaseUtils.parseInt(Settings.servers[sindex].split(", ")[2], 25565);
                String[] status = BaseUtils.pollServer(ip, port);
                String text = BaseUtils.genServerStatus(status);
                BufferedImage img = BaseUtils.genServerIcon(status);
                Frame.main.serverbar.updateBar(text, img);

                serverPollThread.interrupt();
                serverPollThread = null;
                BaseUtils.send("Refreshing server done!");
            }
        };
        serverPollThread.setName("Server poll thread");
        serverPollThread.start();
    }

    static String encrypt(String input, String key) {
        byte[] crypted = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes());
        } catch (Exception e) {
            BaseUtils.sendErr("Ключ должен быть 16 символов");
        }
        return new String(new sun.misc.BASE64Encoder().encode(crypted));
    }

    static String decrypt(String input, String key) {
        byte[] output = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            output = cipher.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(input));
        } catch (Exception e) {
            BaseUtils.sendErr("Ключ шифрование не совпадает или больше 16 символов, или полученна ошибка от launcher.php");
            BaseUtils.sendErr("Проверьте настройку  в Settings.java или connect.php");
        }
        return new String(output);
    }
}
