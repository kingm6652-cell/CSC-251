import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

/** Main JFrame from Kilpatrick's project, reworked into one shared-data Swing app. */
public class FarmBusinessGUI_Kilpatrick extends JFrame {
    public FarmBusinessGUI_Kilpatrick(FarmController_Kilpatrick controller) {
        super("Barnyard Supply & Services | Johnson + Kilpatrick");
        JPanel content=createContent(controller);
        setContentPane(content);setSize(1200,800);setMinimumSize(new Dimension(1020,700));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter(){
            @Override public void windowClosing(WindowEvent e){
                SalesFrame_Kilpatrick checkout=(SalesFrame_Kilpatrick)content.getClientProperty("checkout");
                if(checkout.hasCart()&&!Ui_Kilpatrick.confirm(FarmBusinessGUI_Kilpatrick.this,"The unpurchased cart will be discarded. Close the app?"))return;
                try{controller.close();dispose();}
                catch(IOException ex){Ui_Kilpatrick.error(FarmBusinessGUI_Kilpatrick.this,"Could not close the data file: "+ex.getMessage());}
            }
        });
    }
    /** A separate content factory also permits off-screen layout and integration tests. */
    public static JPanel createContent(FarmController_Kilpatrick controller) {
        JPanel root=new JPanel(new BorderLayout());
        JPanel banner=new JPanel(new BorderLayout(10,0));banner.setBackground(Ui_Kilpatrick.GREEN);
        banner.setBorder(new EmptyBorder(17,22,17,22));
        JLabel brand=new JLabel("Barnyard Supply & Services");brand.setForeground(Color.WHITE);
        brand.setFont(new Font(Font.SANS_SERIF,Font.BOLD,22));banner.add(brand,BorderLayout.WEST);
        JButton help=Ui_Kilpatrick.button("Help",()->Ui_Kilpatrick.message(root,"Help for each section",
                "Barnyard Supply & Services\n\nOverview - Displays general information for store\n"+
                "Inventory - Displays current stock levels and allows for inventory control.\n"+
                "Store Sales - Register Screen, select item, add to cart, select payment time, and press checkout to finalize sale.\n\n"+
                "Services - Used for scheduling services. \n"+
                "Payments - Displays balances for services and all payments recieved.\n\n"+
                "Farm Animals - Tracks inventory of live farm animals \n"+
                "Local Breeders - Tracks inventory of local breeder animal stock\n"+
                "Data file: "+controller.getDataFile()+"\nPrevious saved version: same filename with .bak\n\n"+
                "Use fictional classroom data. No real payment processing, taxes, or clinical features."));
        banner.add(help,BorderLayout.EAST);root.add(banner,BorderLayout.NORTH);
        JTabbedPane tabs=new JTabbedPane();tabs.setName("Main sections");
        tabs.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,14));
        SalesFrame_Kilpatrick checkout=new SalesFrame_Kilpatrick(controller);
        tabs.addTab("Overview",new OverviewPanel_Kilpatrick(controller));
        tabs.addTab("Inventory",new InventoryFrame_Kilpatrick(controller));
        tabs.addTab("Store Sales",checkout);
        tabs.addTab("Services",new SchedulingFrame_Kilpatrick(controller));
        tabs.addTab("Payments",new PaymentsFrame_Kilpatrick(controller));
        tabs.addTab("Farm Animals",new AnimalSalesFrame_Kilpatrick(controller));
        tabs.addTab("Local Breeders",new BreederSalesFrame_Kilpatrick(controller));
        root.add(tabs,BorderLayout.CENTER);root.putClientProperty("checkout",checkout);root.putClientProperty("tabs",tabs);
        JLabel status=new JLabel("  Auto-save is on.  |  Data: "+controller.getDataFile());
        status.setBorder(new EmptyBorder(7,10,7,10));root.add(status,BorderLayout.SOUTH);
        return root;
    }
    private static Path defaultDataPath() {
        // Double-clicking the JAR should still save beside that JAR, not to an arbitrary folder.
        try {
            Path location=Paths.get(FarmBusinessGUI_Kilpatrick.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            if(location.toString().endsWith(".jar"))return location.getParent().resolve("data/barnyard.properties");
        }catch(Exception ignored){ }
        return Paths.get("data","barnyard.properties");
    }
    public static void main(String[] args) {
        Path file=args.length==2&&args[0].equals("--data")?Paths.get(args[1]):defaultDataPath();
        SwingUtilities.invokeLater(()->{
            FarmController_Kilpatrick controller=null;
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                controller=new FarmController_Kilpatrick(file);
                new FarmBusinessGUI_Kilpatrick(controller).setVisible(true);
            }catch(Exception ex){
                if(controller!=null)try{controller.close();}catch(IOException ignored){ }
                if(GraphicsEnvironment.isHeadless())System.err.println("Cannot open the desktop GUI: "+ex);
                else JOptionPane.showMessageDialog(null,"The app could not open. Existing data was not replaced.\n"+
                        ex.getMessage()+"\n\nIf the saved data is damaged, see the backup recovery steps in README_Kilpatrick.md.",
                        "Startup problem",JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
