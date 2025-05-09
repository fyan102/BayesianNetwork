package org.fyan102.bayesiannetwork.ui.config;

import java.awt.*;

public final class UIConfig {
    // Colors
    public static final Color BACKGROUND_COLOR = new Color(250, 250, 250);
    public static final Color TOOLBAR_COLOR = new Color(240, 240, 240);
    public static final Color BORDER_COLOR = new Color(200, 200, 200);
    public static final Color BUTTON_COLOR = new Color(65, 105, 225); // Royal Blue
    public static final Color BUTTON_HOVER_COLOR = new Color(100, 149, 237); // Cornflower Blue
    public static final Color BUTTON_PRESSED_COLOR = new Color(0, 80, 150); // Windows 11 pressed blue
    public static final Color TEXT_COLOR = new Color(32, 32, 32);           // Windows 11 text
    public static final Color MENU_HOVER_COLOR = new Color(237, 237, 237);  // Windows 11 menu hover
    public static final Color GRID_COLOR = new Color(230, 230, 230);
    public static final Color NODE_BACKGROUND = new Color(255, 255, 255);
    public static final Color NODE_BORDER = new Color(224, 224, 224);
    public static final Color NODE_SELECTED_BORDER = new Color(0, 120, 212);
    public static final Color NODE_TEXT = new Color(32, 32, 32);
    public static final Color NODE_HOVER_BACKGROUND = new Color(243, 243, 243);
    public static final Color NODE_SHADOW = new Color(0, 0, 0, 20);

    // Fonts
    public static final Font MENU_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font NODE_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    // Dimensions
    public static final int CORNER_RADIUS = 8;  // Windows 11 corner radius
    public static final int TOOLBAR_HEIGHT = 40;
    public static final int GRID_SIZE = 20;
    public static final int NODE_WIDTH = 140;
    public static final int NODE_HEIGHT = 50;
    public static final int PADDING = 12;
    public static final int SHADOW_OFFSET = 2;

    // Window settings
    public static final int DEFAULT_WINDOW_WIDTH = 1024;
    public static final int DEFAULT_WINDOW_HEIGHT = 768;
    public static final String APPLICATION_TITLE = "Bayesian Network";

    private UIConfig() {
        // Prevent instantiation
    }
} 