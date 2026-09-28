import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Shared panel layout, inherited by several GUI sections. */
public abstract class TablePanel_Kilpatrick extends JPanel {
    protected final FarmController_Kilpatrick controller;
    protected final JTable table;
    protected final JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
    protected final JLabel summary = new JLabel(" ");
    public TablePanel_Kilpatrick(FarmController_Kilpatrick controller, String title, String subtitle, String... columns) {
        super(new BorderLayout(0,12)); this.controller = controller;
        setBorder(new EmptyBorder(20,20,20,20));
        table = Ui_Kilpatrick.table(columns);
        JPanel top = new JPanel(new BorderLayout(0,8));
        top.add(Ui_Kilpatrick.heading(title, subtitle), BorderLayout.NORTH);
        top.add(Ui_Kilpatrick.search(table), BorderLayout.CENTER);
        add(top, BorderLayout.NORTH); add(Ui_Kilpatrick.scroll(table), BorderLayout.CENTER);
        JPanel bottom = new JPanel(new BorderLayout(0,10));
        bottom.add(summary, BorderLayout.NORTH); bottom.add(actions, BorderLayout.SOUTH);
        add(bottom, BorderLayout.SOUTH);
        controller.onChange(this::refresh);
    }
    public JTable getTable() { return table; }
    public abstract void refresh();
}
