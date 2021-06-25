import javax.swing.*;

public class LookAndFeelFactory {

    /**
     * Class name of Windows L&F provided in Sun JDK.
     */
    public static final String WINDOWS_LNF = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

    public static ClassLoader getUIManagerClassLoader() {
        Object cl = UIManager.get("ClassLoader");
        if (cl instanceof ClassLoader) {
            return (ClassLoader) cl;
        }
        ClassLoader classLoader = LookAndFeelFactory.class.getClassLoader();
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        return classLoader;
    }

    public static void installJideExtension() {
        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        defaults.put("StyledLabelUI", BasicStyledLabelUI.class.getName());
    }

    /**
     * As of Java 10, com.sun.java.swing.plaf.windows.WindowsLookAndFeel is no longer available on macOS thus
     * "instanceof WindowsLookAndFeel" directives will result in a NoClassDefFoundError during runtime. This method
     * was introduced to avoid this exception.
     *
     * @param lnf
     * @return true if it is a WindowsLookAndFeel.
     */
    public static boolean isWindowsLookAndFeel(LookAndFeel lnf) {
        if (lnf == null) {
            return false;
        } else {
            try {
                Class c = Class.forName(WINDOWS_LNF);
                return c.isInstance(lnf);
            } catch (ClassNotFoundException ignore) {
                // if it is not possible to load the Windows LnF class, the
                // given lnf instance cannot be an instance of the Windows
                // LnF class
                return false;
            } catch (NoClassDefFoundError ignore) {
                // if it is not possible to load the Windows LnF class, the
                // given lnf instance cannot be an instance of the Windows
                // LnF class
                return false;
            }
        }
    }

    public static boolean isMnemonicHidden() {
        return !UIManager.getBoolean("Button.showMnemonics");
    }
}
