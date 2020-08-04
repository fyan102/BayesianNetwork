package org.fyan102.bayesiannetwork;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.util.ArrayList;

public class MenuView {
    private ArrayList<Menu> menus;

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
        Menu fileMenu = new Menu(name);
        // add menu items to menu
        for (String menu : items) {
            MenuItem item = new MenuItem(menu);
            fileMenu.getItems().add(item);
        }
        menus.add(fileMenu);
    }

    /**
     * Get one item in menu by name
     *
     * @param name the name of the menu item
     * @return the menu item if exist, an empty menu item otherwise
     */
    public MenuItem getMenuItem(String name) {
        for (Menu menu : menus) {
            for (MenuItem item : menu.getItems()) {
                if (item.getText().equals(name)) {
                    return item;
                }
            }
        }
        return new MenuItem();
    }

    /**
     * Get the list of menus
     *
     * @return the list of menus
     */
    public ArrayList<Menu> getMenus() {
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
    public void setMenus(ArrayList<Menu> menus) {
        this.menus = menus;
    }
}
