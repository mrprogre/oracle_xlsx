package QueriesCreater;

import java.awt.*;
import java.util.Locale;

public class Fonts {
    static void fontsList(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] allFonts = ge.getAllFonts();
        for (Font font : allFonts) {
            if (!font.getFontName(Locale.US).contains("."))
            Gui.fontsCombobox.addItem(font.getFontName(Locale.US));
        }

    }
}
