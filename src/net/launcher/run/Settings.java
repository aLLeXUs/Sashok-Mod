package net.launcher.run;

public class Settings {

    /**
     * Настройка заголовка лаунчера
     */
    public static final String title = "Minecraft Launcher"; //Заголовок лаунчера
    public static final String titleInGame = "Minecraft"; //Заголовок лаунчера после авторизации
    public static final String basedir = "AppData"; //Родительская папка для Minecraft (только для Windows) [ProgramFiles, AppData]
    public static final String baseconf = ".allexus"; //Папка с файлом конфигурации
    public static final String pathconst = ".allexus/%SERVERNAME%"; //Конструктор пути к папке с MC
    public static final String skins = "MinecraftSkins/"; //Папка скинов
    public static final String cloaks = "MinecraftCloaks/"; //Папка плащей

    /**
     * Параметры подключения
     */
    public static final String domain = "localhost/mine";//Домен сайта
    public static final String siteDir = "site";//Папка с файлами лаунчера на сайте
    public static final String updateFile = "http://mc.allexus.tk/launcher/launcher/aLLeXUs-Launcher";//Ссылка на обновления лаунчера. Не писать на конце ".exe .jar"!
    public static final String[] p = {"wireshark", "cheat"};  //Список запрещенных процессов.
    public static final String http = "http://";   //Протокол подключения https:// если есть ssl сертификат

    public static int width = 800;      //Ширина окна клиента
    public static int height = 500;      //Высота окна клиента

    public static String[] servers
            = {
                "",};

    /**
     * Настройка панели ссылок *
     */
    public static final String[] links
            = {
                //Для отключения добавьте в адрес ссылки #
                " Регистрация ::http://allexus.tk/wp-login.php?action=register", " ЛК ::http://mc.allexus.tk"};

    /**
     * Настройки структуры лаунчера
     */
    public static boolean useAutoenter = true;  //Использовать функцию автозахода на выбранный сервер
    public static boolean useMulticlient = true;   //Использовать функцию "по клиенту на сервер"
    public static boolean useStandartWB = true;   //Использовать стандартный браузер для открытия ссылок
    public static boolean customframe = false;   //Использовать кастомный фрейм
    public static boolean useConsoleHider = false;  //Использовать скрытие консоли клиента
    public static boolean useModCheckerTimer = true;   //Перепроверка jar через 30 секунд
    public static int useModCheckerint = 2;      //Количество раз перепроверки jar во время игры
    public static boolean assetsfolder = false;  //Скачивать assets из папки, или из архива (true=из папки false=из архива) в connect.php должно быть так же.

    public static final String protectionKey = "1234567890"; //Ключ защиты сессии. Никому его не говорите.
    public static final String key1 = "1234567891234567"; //16 Character Key Ключ пост запросов
    public static final String key2 = "1234567891234567"; //16 Character Key Ключ пост запросов

    public static boolean debug = true; //Отображать все действия лаунчера (отладка)(true/false)
    public static boolean drawTracers = false; //Отрисовывать границы элементов лаунчера
    public static final String masterVersion = "Beta-1"; //Версия лаунчера

    public static boolean patchDir = true; //Использовать автоматическую замену директории игры (true/false)

    public static void onStart() {
    }

    public static void onStartMinecraft() {
    }

}
