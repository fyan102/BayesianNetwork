package org.fyan102.bayesiannetwork.ui;

import javax.swing.*;
import java.util.ArrayList;

public class MenuView {
    private ArrayList<JMenu> menus;

    /**
     * Constructor of MenuView
     */
    public MenuView() {
        menus = new ArrayList<>();
        initMenu();
    }

    /**
     * Add one menu including the name and menu items
     *
     * @param name  the name of the menu
     * @param items a list of menu items
     */
    private void addMenu(String name, String[] items) {
        JMenu fileMenu = new JMenu(name);
        // add menu items to menu
        for (String menu : items) {
            JMenuItem item = new JMenuItem(menu);
            fileMenu.add(item);
        }
        menus.add(fileMenu);
    }

    /**
     * Get one item in menu by name
     *
     * @param name the name of the menu item
     * @return the menu item if exist, an empty menu item otherwise
     */
    public JMenuItem getMenuItem(String name) {
        for (JMenu menu : menus) {
            for (int i = 0; i < menu.getItemCount(); i++) {
                JMenuItem item = menu.getItem(i);
                if (item.getText().equals(name)) {
                    return item;
                }
            }
        }
        return new JMenuItem();
    }

    /**
     * Get the list of menus
     *
     * @return the list of menus
     */
    public ArrayList<JMenu> getMenus() {
        return menus;
    }

    private void initMenu() {
        addMenu("File", new String[]{"New", "Open", "Close"});
        addMenu("Node", new String[]{"Chance Node", "Decision Node", "Utility Node"});
        addMenu("Run", new String[]{"Run"});
    }

    /**
     * Mutator for menu list
     *
     * @param menus the menu list
     */
    public void setMenus(ArrayList<JMenu> menus) {
        this.menus = menus;
    }
}
