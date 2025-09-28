import citd.nhom99.ck.config.InitData;
import citd.nhom99.ck.config.SchemaManager;
import citd.nhom99.ck.controller.AppController;

public class Main {
    public static void main(String[] args) {
        // 1. Initialize the database schema (creates tables if they don't exist)
        SchemaManager.initializeDatabase();

        // 2. Initialize sample data
        InitData.initializeSampleData();

        // 3. Run the application
        javax.swing.SwingUtilities.invokeLater(() -> {
            AppController controller = new AppController();
            controller.start();
        });
    }
}
