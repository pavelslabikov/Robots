package gui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();

    private static final int PIXEL_INSET = 50;
    private static final Dimension GAME_WINDOW_SIZE = new Dimension(400, 400);
    private static final Dimension LOG_WINDOW_SIZE = new Dimension(300, 800);


    public MainApplicationFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(PIXEL_INSET, PIXEL_INSET,
            screenSize.width  - PIXEL_INSET * 2,
            screenSize.height - PIXEL_INSET * 2);

        setContentPane(desktopPane);
        addWindows(createLogWindow(), createGameWindow());
        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(LOG_WINDOW_SIZE);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected GameWindow createGameWindow() {
        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(GAME_WINDOW_SIZE);
        return gameWindow;
    }

    protected void addWindows(JInternalFrame... windows) {
        for (var window : windows) {
            desktopPane.add(window);
            window.setVisible(true);
        }
    }
    
//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
// 
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
// 
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        return menuBar;
//    }
    
    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();

        JMenu displayModeMenu = createMenu(
                "Режим отображения",
                KeyEvent.VK_V,
                "Управление режимом отображения приложения");

        JMenuItem systemMenuItem = createMenuItem(
                "Системная схема",
                (event) -> {
                    updateLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    this.invalidate();
                });

        JMenuItem crossPlatformMenuItem = createMenuItem(
                "Универсальная схема",
                (event) -> {
                    updateLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    this.invalidate();
                });

        displayModeMenu.add(systemMenuItem);
        displayModeMenu.add(crossPlatformMenuItem);

        JMenu testMenu = createMenu("Тесты", KeyEvent.VK_T, "Тестовые команды");

        JMenuItem logMessageItem = createMenuItem(
                "Сообщение в лог",
                (event) -> Logger.debug("Новая строка"));

        testMenu.add(logMessageItem);

        menuBar.add(displayModeMenu);
        menuBar.add(testMenu);
        return menuBar;
    }

    private JMenuItem createMenuItem(String text, ActionListener listener) {
        JMenuItem item = new JMenuItem(text, KeyEvent.VK_S);
        item.addActionListener(listener);
        return item;
    }

    private JMenu createMenu(String title, int mnemonic, String description) {
        JMenu menu = new JMenu(title);
        menu.setMnemonic(mnemonic);
        menu.getAccessibleContext().setAccessibleDescription(description);
        return menu;
    }

    private void updateLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }
}
