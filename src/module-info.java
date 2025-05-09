module org.fyan102.bayesiannetwork {
    requires java.desktop;
    // requires javafx.controls;
    // requires javafx.fxml;
    requires com.google.gson;
    
    // Open packages for reflection (needed for Gson)
    opens org.fyan102.bayesiannetwork.ui;
    opens org.fyan102.bayesiannetwork.model;
    opens org.fyan102.bayesiannetwork.util;
    
    exports org.fyan102.bayesiannetwork.ui;
    exports org.fyan102.bayesiannetwork.model;
    exports org.fyan102.bayesiannetwork.util;
} 