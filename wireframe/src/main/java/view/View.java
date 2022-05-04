package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;

public class View extends JFrame {
    private final static int DEFAULT_WIDTH = 640;
    private final static int DEFAULT_HEIGHT = 480;


    private final JMenuBar menubar;
    private final JToolBar toolbar;
    protected DialogPanel dialogPanel;
    protected WireframePanel wireframePanel;
    protected ButtonGroup buttonGroup;
    protected ButtonGroup menuButtonGroup;

    public View() {
        super("Wireframe");
        setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        setResizable(true);
        setLocationByPlatform(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        buttonGroup = new ButtonGroup();
        menuButtonGroup = new ButtonGroup();

        menubar = new JMenuBar();
        this.setJMenuBar(menubar);
        toolbar = new JToolBar("Tools");
        toolbar.setRollover(true);
        toolbar.setFloatable(false);
        this.add(toolbar, BorderLayout.PAGE_START);
        var bSplinePanel = new BSplinePanel(new JScrollPane());
        wireframePanel = new WireframePanel(bSplinePanel.getBSpline());
        dialogPanel = new DialogPanel(bSplinePanel, wireframePanel);

        this.add(wireframePanel);
    }

    public JMenuItem createMenuButton(String title, String info, int keyId, String iconName, String methodName) throws NoSuchMethodException {
        JMenuItem item;
        item = new JMenuItem(title);
        item.setActionCommand(title);
        menuButtonGroup.add(item);
        item.setToolTipText(info);
        item.setMnemonic(keyId);
        if (iconName != null) {
            item.setIcon(new ImageIcon(getClass().getResource("/" + iconName), title));
        }
        final Method method = getClass().getMethod(methodName, ActionEvent.class);

        item.addActionListener(e -> {
            try {
                method.invoke(View.this, e);
            } catch (InvocationTargetException | IllegalAccessException invocationTargetException) {
                invocationTargetException.printStackTrace();
            }
        });
        return item;
    }

    public void addMenuButton(String title, String info, int keyId, String iconName, String method) throws NoSuchMethodException {
        MenuElement element = getParentMenuElement(title);
        if (element == null)
            throw new InvalidParameterException("Menu path not found: " + title);
        JMenuItem item = createMenuButton(getMenuPathName(title), info, keyId, iconName, method);
        if (element instanceof JMenu)
            ((JMenu) element).add(item);
        else if (element instanceof JPopupMenu)
            ((JPopupMenu) element).add(item);
        else
            throw new InvalidParameterException("Invalid menu path: " + title);

    }

    private String getMenuPathName(String menuPath) {
        int pos = menuPath.lastIndexOf('/');
        if (pos > 0)
            return menuPath.substring(pos + 1);
        else
            return menuPath;
    }

    private MenuElement getParentMenuElement(String menuPath) {
        int pos = menuPath.lastIndexOf('/');
        if (pos > 0)
            return getMenuElement(menuPath.substring(0, pos));
        else
            return menubar;
    }

    private MenuElement getMenuElement(String menuPath) {
        MenuElement element = menubar;
        for (String pathElement : menuPath.split("/")) {
            MenuElement newElement = null;
            for (MenuElement subElement : element.getSubElements()) {
                if ((subElement instanceof JMenu && ((JMenu) subElement).getText().equals(pathElement))
                        || (subElement instanceof JMenuItem && ((JMenuItem) subElement).getText().equals(pathElement))) {
                    if (subElement.getSubElements().length == 1 && subElement.getSubElements()[0] instanceof JPopupMenu)
                        newElement = subElement.getSubElements()[0];
                    else
                        newElement = subElement;
                    break;
                }
            }
            if (newElement == null) return null;
            element = newElement;
        }
        return element;
    }

    public JToggleButton createToggleToolBarButton(JMenuItem item) {
        JToggleButton button = new JToggleButton(item.getIcon());
        for (ActionListener listener : item.getActionListeners())
            button.addActionListener(listener);

        button.setActionCommand(item.getText());
        button.setToolTipText(item.getToolTipText());
        button.setFocusPainted(false);
        return button;
    }

    public JToggleButton createToggleToolBarButton(String menuPath) {
        JMenuItem item = (JMenuItem) getMenuElement(menuPath);
        if (item == null)
            throw new InvalidParameterException("Menu path not found: " + menuPath);
        return createToggleToolBarButton(item);
    }


    public JButton createToolBarButton(JMenuItem item) {
        JButton button = new JButton(item.getIcon());
        for (ActionListener listener : item.getActionListeners())
            button.addActionListener(listener);
        button.setToolTipText(item.getToolTipText());
        button.setActionCommand(item.getText());
        button.setFocusPainted(false);
        return button;
    }

    public JButton createToolBarButton(String menuPath) {
        JMenuItem item = (JMenuItem) getMenuElement(menuPath);
        if (item == null)
            throw new InvalidParameterException("Menu path not found: " + menuPath);
        return createToolBarButton(item);
    }

    public void addToolBarButton(String menuPath) {
        var newButton = createToolBarButton(menuPath);
        toolbar.add(newButton);
        buttonGroup.add(newButton);

        if (menuPath.contains("Save")) {
            addToolBarSeparator();
        }
    }


    public void addToolBarSeparator() {
        toolbar.addSeparator();
    }

    public void addSubMenu(String title, int mnemonic) {
        MenuElement element = getParentMenuElement(title);
        if (element == null)
            throw new InvalidParameterException("Menu path not found: " + title);
        JMenu subMenu = createSubMenu(getMenuPathName(title), mnemonic);
        if (element instanceof JMenuBar)
            ((JMenuBar) element).add(subMenu);
        else if (element instanceof JMenu)
            ((JMenu) element).add(subMenu);
        else if (element instanceof JPopupMenu)
            ((JPopupMenu) element).add(subMenu);
        else
            throw new InvalidParameterException("Invalid menu path: " + title);
    }

    public JMenu createSubMenu(String title, int mnemonic) {
        JMenu menu = new JMenu(title);
        menu.setMnemonic(mnemonic);
        return menu;
    }


}
