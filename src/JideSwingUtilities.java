import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.Set;

public class JideSwingUtilities {

    static Map renderingHints = null;

    static {
        Toolkit tk = Toolkit.getDefaultToolkit();
        renderingHints = (Map) (tk.getDesktopProperty("awt.font.desktophints"));
        tk.addPropertyChangeListener("awt.font.desktophints", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getNewValue() instanceof RenderingHints) {
                    renderingHints = (RenderingHints) evt.getNewValue();
                }
            }
        });
    }

    /**
     * Get rendering hints from a Graphics instance. "hintsToSave" is a Map of RenderingHint key-values. For each hint
     * key present in that map, the value of that hint is obtained from the Graphics and stored as the value for the key
     * in savedHints.
     */
    private static Map getRenderingHints(Graphics2D g2d,
                                         Map hintsToSave,
                                         Map savedHints) {
        if (savedHints == null) {
            savedHints = new RenderingHints(null);
        } else {
            savedHints.clear();
        }
        if (hintsToSave == null || hintsToSave.size() == 0) {
            return savedHints;
        }
        /* RenderingHints.keySet() returns Set*/
        Set objects = hintsToSave.keySet();
        for (Object o : objects) {
            RenderingHints.Key key = (RenderingHints.Key) o;
            Object value = g2d.getRenderingHint(key);
            if (value != null) {
                savedHints.put(key, value);
            }
        }

        return savedHints;
    }

    public static void drawStringUnderlineCharAt(JComponent c, Graphics g, String text,
                                                 int underlinedIndex, int x, int y) {
        drawString(c, g, text, x, y);

        if (underlinedIndex >= 0 && underlinedIndex < text.length()) {
            FontMetrics fm = g.getFontMetrics();
            int underlineRectX = x + fm.stringWidth(text.substring(0, underlinedIndex));
            int underlineRectY = y;
            int underlineRectWidth = fm.charWidth(text.charAt(underlinedIndex));
            int underlineRectHeight = 1;
            g.fillRect(underlineRectX, underlineRectY + fm.getDescent() - 1,
                    underlineRectWidth, underlineRectHeight);
        }
    }

    public static void drawString(JComponent c, Graphics g, String text, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;
        Map oldHints = null;
        if (renderingHints != null) {
            oldHints = getRenderingHints(g2d, renderingHints, null);
            g2d.addRenderingHints(renderingHints);
        }
        g2d.drawString(text, x, y);
        if (oldHints != null) {
            g2d.addRenderingHints(oldHints);
        }
    }
}
