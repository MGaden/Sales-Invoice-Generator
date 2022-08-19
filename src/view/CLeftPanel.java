package view;

import model.InvoiceHeader;
import model.InvoiceLine;
import model.JTableLoader;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

//model for left panel
public class CLeftPanel {

    ArrayList<InvoiceHeader> invoicesLst;
    JTableLoader<InvoiceHeader> loader;
    private SalesInvoice parent;
    public JTable tblInvoice ;
    public  CLeftPanel(SalesInvoice view) {

        parent = view;

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(2,1));
        leftPanel.setBorder(new LineBorder(new Color(0)));

        JScrollPane jScrollPane1 = new JScrollPane();

        String[] columns = {"Number", "Date", "Customer Name"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        tblInvoice = new JTable(tableModel);

        tblInvoice.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(jScrollPane1);

        jScrollPane1.setViewportView(tblInvoice);
        tblInvoice.getAccessibleContext().setAccessibleName("");

        JPanel btnPanel = new JPanel();

        JButton btnCreateInvoice = new JButton("Create new invoice");
        btnCreateInvoice.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCreateInvoice.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int input = JOptionPane.showConfirmDialog(null,
                        "Do you want to create?", "Select an Option...",JOptionPane.OK_CANCEL_OPTION);
                if(input == 0)
                {
                    createNewInvoice();
                }
            }
        } );
        btnPanel.add(btnCreateInvoice);

        JButton btnDeleteInvoice = new JButton("Delete invoice");
        btnDeleteInvoice.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDeleteInvoice.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int input = JOptionPane.showConfirmDialog(null,
                        "Do you want to delete selected?", "Select an Option...",JOptionPane.OK_CANCEL_OPTION);
                if(input == 0)
                {
                    deleteSelectedInvoice();
                }
            }
        } );
        btnPanel.add(btnDeleteInvoice);

        leftPanel.add(btnPanel);

        view.add(leftPanel);
    }

    public void deleteSelectedInvoice() {
        if(loader.table.getSelectedRow() != -1) {

            invoicesLst.remove(loader.table.getSelectedRow());
            bindInvoiceTable(invoicesLst);

            JOptionPane.showMessageDialog(null, "Selected row deleted successfully");
        }
    }

    private void createNewInvoice() {

        InvoiceHeader newItem = new InvoiceHeader();
        newItem.setNew(true);
        newItem.setInvoiceNum(invoicesLst.size() + 1);
        newItem.setCustomerName("New Customer " + newItem.getInvoiceNum());
        newItem.setInvoiceDate(new Date());
        newItem.setInvoiceLines(new ArrayList<>());
        InvoiceLine iItem = new InvoiceLine();
        iItem.setItemPrice(0);
        iItem.setCount(0);
        iItem.setInvoiceNum(newItem.getInvoiceNum());
        newItem.getInvoiceLines().add(iItem);

        invoicesLst.add(newItem);
        bindInvoiceTable(invoicesLst);
        loader.table.setRowSelectionInterval(invoicesLst.size()-1, invoicesLst.size()-1);
    }

    void bindInvoiceTable(ArrayList<InvoiceHeader> invoices)
    {
        loader = new JTableLoader<>(tblInvoice, "getInvoiceNum,getFormatedInvoiceDate,getCustomerName");

        loader.table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
try
{
    if(loader.table.getSelectedRow() != -1) {
        InvoiceHeader item = invoicesLst.get(loader.table.getSelectedRow());
        if(item != null)
        {
            parent.setSelectedInvoiceDetails(item);
        }
    }


}catch (Exception e)
{

}

            }
        });
        loader.load(invoices);

        if(invoices != null && invoices.size() > 0)
        {
            loader.table.setRowSelectionInterval(0, 0);
        }

    }
    public void loadInvoices(ArrayList<InvoiceHeader> invoices) {

        invoicesLst = invoices;
        bindInvoiceTable(invoicesLst);

    }

    public void saveCurrentItemUpdate(InvoiceHeader uitem) {

        for (int i = 0; i < invoicesLst.size(); i++) {
            if (invoicesLst.get(i).getInvoiceNum() == uitem.getInvoiceNum()) {
                invoicesLst.set(i , uitem);
            }
        }

        bindInvoiceTable(invoicesLst);
    }
}
