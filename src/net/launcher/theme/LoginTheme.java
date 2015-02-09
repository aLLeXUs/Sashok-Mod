package net.launcher.theme;

import java.awt.Color;

import javax.swing.border.EmptyBorder;

import net.launcher.components.Align;
import net.launcher.components.ButtonStyle;
import net.launcher.components.ComboboxStyle;
import net.launcher.components.ComponentStyle;
import net.launcher.components.DragbuttonStyle;
import net.launcher.components.DraggerStyle;
import net.launcher.components.LinklabelStyle;
import net.launcher.components.PassfieldStyle;
import net.launcher.components.ServerbarStyle;
import net.launcher.components.TextfieldStyle;
import net.launcher.run.Settings;

public class LoginTheme {

    public static final int frameW = 800;
    public static final int frameH = 500;
    
    public static final float fontbasesize = 20F;
    public static final float fonttitlesize = 24F;

    public static ButtonStyle toAuth = new ButtonStyle(30, 110, 230, 40, "font", "button", fontbasesize, Color.ORANGE, true, Align.CENTER);
    public static ButtonStyle toLogout = new ButtonStyle(30, 70, 230, 40, "font", "button", fontbasesize, Color.ORANGE, true, Align.CENTER);
    public static ButtonStyle toGame = new ButtonStyle(30, 210, 230, 40, "font", "button", fontbasesize, Color.GREEN, true, Align.CENTER);
    public static ButtonStyle toOptions = new ButtonStyle(30, 400, 230, 40, "font", "button", fontbasesize, Color.YELLOW, true, Align.CENTER);

    public static TextfieldStyle login = new TextfieldStyle(30, 30, 230, 36, "textfield", "font", fontbasesize, Color.WHITE, Color.DARK_GRAY, Align.CENTER, new EmptyBorder(0, 10, 0, 10));
    public static PassfieldStyle password = new PassfieldStyle(30, 70, 230, 36, "textfield", "font", fontbasesize, Color.WHITE, Color.DARK_GRAY, "*", Align.CENTER, new EmptyBorder(0, 10, 0, 10));

    public static ComponentStyle newsBrowser = new ComponentStyle(320, 30, 450, 740, "font", fontbasesize, Color.WHITE, true);
    public static LinklabelStyle links = new LinklabelStyle(30, 450, 0, "font", fontbasesize, Color.WHITE, Color.LIGHT_GRAY);

    public static DragbuttonStyle dbuttons = new DragbuttonStyle(770, 2, 35, 24, 810, 2, 35, 24, "draggbutton", true);
    public static DraggerStyle dragger = new DraggerStyle(0, 0, frameW, Settings.customframe ? 30 : 0, "font", fontbasesize, Color.WHITE, Align.LEFT);

    public static ButtonStyle update_jar = new ButtonStyle(190, 400, 150, 40, "font", "button", fontbasesize, Color.GREEN, true, Align.CENTER);
    public static ButtonStyle update_no = new ButtonStyle(515, 400, 150, 40, "font", "button", fontbasesize, Color.RED, true, Align.CENTER);

    public static ComboboxStyle servers = new ComboboxStyle(30, 155, 230, 50, "font", "combobox", fontbasesize, Color.WHITE, true, Align.CENTER);
    public static ServerbarStyle serverbar = new ServerbarStyle(30, 180, 230, 16, "font", fontbasesize, Color.WHITE, true);
}
