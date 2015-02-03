package net.launcher.utils;

import java.awt.Dimension;
import java.awt.FontMetrics;

import net.launcher.components.Frame;
import net.launcher.components.LinkLabel;
import net.launcher.run.Settings;
import static net.launcher.theme.LoginTheme.*;
import static net.launcher.theme.OptionsTheme.*;

public class ThemeUtils extends BaseUtils {

    public static void updateStyle(Frame main) throws Exception {
        int i = 0;
        for (LinkLabel link : main.links) {
            links.apply(link);
            FontMetrics fm = link.getFontMetrics(link.getFont());
            link.setBounds(i + links.x, links.y, fm.stringWidth(link.getText()), fm.getHeight());
            i += fm.stringWidth(link.getText()) + links.margin;
        }

        dragger.apply(main.dragger);
        dbuttons.apply(main.hide, main.close);
        toGame.apply(Frame.toGame);
        toAuth.apply(Frame.toAuth);
        toLogout.apply(Frame.toLogout);
        toOptions.apply(main.toOptions);
        login.apply(Frame.login);
        password.apply(Frame.password);
        newsBrowser.apply(main.bpane);
        servers.apply(main.servers);
        serverbar.apply(main.serverbar);

        loadnews.apply(main.loadnews);
        updatepr.apply(main.updatepr);
        cleandir.apply(main.cleanDir);
        fullscrn.apply(main.fullscreen);
        memory.apply(main.memory);
        close.apply(main.options_close);

        update_no.apply(main.update_no);
        update_exe.apply(main.update_exe);
        update_jar.apply(main.update_jar);

        main.panel.setPreferredSize(new Dimension(frameW, frameH));

        main.setIconImage(BaseUtils.getLocalImage("favicon"));
        main.setTitle(Settings.title);
        main.setLocationRelativeTo(null);
        main.pack();
        main.repaint();
    }
}
