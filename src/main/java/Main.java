import citd.nhom99.ck.config.SchemaManager;
import citd.nhom99.ck.controller.AppController;
import citd.nhom99.ck.config.InitData;

public class Main {
    public static void main(String[] args) {
        // 1. Initialize the database schema (creates tables if they don't exist)
        SchemaManager.initializeDatabase();

        // 2. Seed the database with sample data if it's empty
        InitData.seedDatabaseIfEmpty();

        // 3. Run the application
        javax.swing.SwingUtilities.invokeLater(() -> {
            AppController controller = new AppController();
            controller.start();
        });
    }
}
