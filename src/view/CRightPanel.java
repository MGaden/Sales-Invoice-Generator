package view;

import model.InvoiceHeader;
import model.InvoiceLine;
import controller.JTableLoader;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CRightPanel {

    SalesInvoice parent;
    InvoiceHeader currentInvoiceHeader;
    JTableLoader<InvoiceLine> loader ;
    JTable tblInvoice ;
    JLabel InvoiceNumValueLabel=new JLabel("0");
    JTextField invoiceDateTextField=new JTextField();
    JTextField customerNameTextField=new JTextField();
    JLabel InvoiceTotalValueLabel=new JLabel("0");
    public  CRightPanel(SalesInvoice view) {

        parent = view;

        JPanel rightPanel = new JPanel();
        rightPanel.setBorder(new LineBorder(new Color(0)));

        rightPanel.setLayout(new GridLayout(3,1));

        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();

        JLabel InvoiceNumLabel=new JLabel("Invoice Number");

        JLabel invoiceDateLabel=new JLabel("Invoice Date");


        JLabel customerNameLabel=new JLabel("Customer Name");


        JLabel InvoiceTotalLabel=new JLabel("Invoice Total");


        JButton saveButton=new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int input = JOptionPane.showConfirmDialog(null,
                        "Do you want to save?", "Select an Option...",JOptionPane.OK_CANCEL_OPTION);
                if(input == 0)
                {
                    saveInvoiceDetails(currentInvoiceHeader);
                }
            }
        } );

        JButton cancelButton=new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int input = JOptionPane.showConfirmDialog(null,
                        "Do you want to cancel?", "Select an Option...",JOptionPane.OK_CANCEL_OPTION);
                if(input == 0)
                {
                    if(currentInvoiceHeader.getNew())
                    {
                        parent.undoNewInvoice();
                        return;
                    }
                    resetInvoiceDetails(currentInvoiceHeader);
                }
            }


        } );

        JButton addItem = new JButton("Add item");
        addItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(loader != null && loader.model != null)
                {
                    //invoiceNum, itemName, itemPrice, Count
                    loader.model.addRow(
                            new Object[]{
                                    currentInvoiceHeader.getInvoiceNum(),
                                    "",
                                    "","",""
                            }
                    );
                }

            }
        });

        JButton removeItem = new JButton("Remove item");
        removeItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(loader != null && loader.model != null&& loader.table != null) {
                    if (loader.table.getSelectedRow() != -1) {
                        loader.model.removeRow(loader.table.getSelectedRow());
                    }
                }

            }
        });

        Border blackline = BorderFactory.createTitledBorder("Invoice items");
        JPanel panel = new JPanel();
        LayoutManager layout = new FlowLayout();
        panel.setLayout(layout);

        JPanel panel1 = new JPanel();

        JScrollPane jScrollPane1 = new JScrollPane();

        String[] columns = {"No.", "item Name", "item Price", "Count", "total"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

         tblInvoice = new JTable(tableModel);

        tblInvoice.setAlignmentX(Component.CENTER_ALIGNMENT);

        jScrollPane1.setViewportView(tblInvoice);
        tblInvoice.getAccessibleContext().setAccessibleName("");

        panel1.add(jScrollPane1);
        panel1.setBorder(blackline);

        panel.add(panel1);
        //panel.setBounds(100,250,500,250);


        InvoiceNumLabel.setBounds(50,50,100,30);
        InvoiceNumValueLabel.setBounds(200,50,100,30);
        invoiceDateLabel.setBounds(50,100,150,30);
        invoiceDateTextField.setBounds(200,100,150,30);

        customerNameLabel.setBounds(50,150,100,30);
        customerNameTextField.setBounds(200,150,100,30);
        InvoiceTotalLabel.setBounds(50,200,150,30);
        InvoiceTotalValueLabel.setBounds(200,200,150,30);

        saveButton.setBounds(50,800,100,30);
        cancelButton.setBounds(200,800,100,30);

        addItem.setBounds(50,240,100,30);
        removeItem.setBounds(200,240,120,30);

        p1.setLayout(null);
        p1.add(InvoiceNumLabel);
        p1.add(InvoiceNumValueLabel);
        p1.add(invoiceDateLabel);
        p1.add(invoiceDateTextField);
        p1.add(customerNameLabel);
        p1.add(customerNameTextField);
        p1.add(InvoiceTotalLabel);
        p1.add(InvoiceTotalValueLabel);

        p1.add(addItem);
        p1.add(removeItem);

        p2.add(panel, BorderLayout.CENTER);

        p3.add(saveButton);
        p3.add(cancelButton);

        rightPanel.add(p1);
        rightPanel.add(p2);
        rightPanel.add(p3);

        view.add(rightPanel);

    }

    private void saveInvoiceDetails(InvoiceHeader item) {
        item.setNew(false);
        item.setCustomerName(customerNameTextField.getText());

        SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
        try {
            item.setInvoiceDate(formatter.parse(invoiceDateTextField.getText()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        item.setInvoiceLines(new ArrayList<>());
        for (int count = 0; count < loader.model.getRowCount(); count++){
            InvoiceLine line = new InvoiceLine();

            line.setInvoiceNum(Integer.parseInt(loader.model.getValueAt(count, 0).toString()));
            line.setItemName(loader.model.getValueAt(count, 1).toString());
            line.setItemPrice(Double.parseDouble(loader.model.getValueAt(count, 2).toString()));
            line.setCount(Integer.parseInt(loader.model.getValueAt(count, 3).toString()));

            item.getInvoiceLines().add(line);
        }

        parent.saveCurrentItemUpdate(item);
    }

    private void resetInvoiceDetails(InvoiceHeader item) {

        InvoiceNumValueLabel.setText(item.getInvoiceNum() + "");
        invoiceDateTextField.setText(item.getFormatedInvoiceDate());
        customerNameTextField.setText(item.getCustomerName());

        if(item.getInvoiceLines() != null)
        {
            InvoiceTotalValueLabel.setText(item.getInvoiceLines().size() + "");
            //"No.", "item Name", "item Price", "Count", "total"
            loader = new JTableLoader<>(tblInvoice, "getInvoiceNum,getItemName,getItemPrice,getCount,getTotal");
            loader.load(item.getInvoiceLines());
        }
    }

    public void setSelectedInvoiceDetails(InvoiceHeader item) {

        currentInvoiceHeader = item;
        resetInvoiceDetails(item);
    }

    public void clearRightData() {
        InvoiceNumValueLabel.setText("");
        invoiceDateTextField.setText("");
        customerNameTextField.setText("");

        loader.model.setRowCount(0);
    }
}
