package view;

import model.FileOperations;
import model.InvoiceHeader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

//main frame with main method
public class SalesInvoice extends JFrame implements ActionListener {

  //file handler
    private FileOperations fileOp;
    private  CRightPanel rightPanel;
    private CLeftPanel leftPanel;
    
    public SalesInvoice() {
        super("Sales Invoice Generator");

        setMainMenu(this);
        setMainLayout();

        //comments to load empty table
//        try
//        {
//            ArrayList<InvoiceHeader> invoices = loadDataFromFiles("asset");
//            if(invoices != null && invoices.size() > 0)
//            {
//                leftPanel.loadInvoices(invoices);
//            }else
//            {
//                JOptionPane.showMessageDialog(null,"No data to display","Info",1);
//            }
//        }
//        catch (RuntimeException e) {
//            JOptionPane.showMessageDialog(null,e.getMessage(),"Error",1);
//        }catch (Exception e)
//        {
//            JOptionPane.showMessageDialog(null,e.getMessage(),"Error",1);
//        }


        setSize(300,400);
        setLocation(200,200);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private ArrayList<InvoiceHeader> loadDataFromFiles(String firstFilePath, String secondFilePath) {

        fileOp = new FileOperations();
        fileOp.set_readInvoiceHeaderFilePath(firstFilePath);
        fileOp.set_readInvoiceLineFilePath(secondFilePath);
        return fileOp.readFile();

    }

    private void setMainLayout() {

        setLayout(new GridLayout(1,2));

        leftPanel = new CLeftPanel(this);

        rightPanel = new CRightPanel(this);

    }

    private void setMainMenu(SalesInvoice salesInvoice) {
        CActionMenu menu = new CActionMenu(salesInvoice);
    }

    public static void main(String[] args) {
        new SalesInvoice().setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       switch (e.getActionCommand())
       {
           case "Save":
                SaveFile();
               break;

           case "Load":
               ArrayList<InvoiceHeader> invoices = LoadFile();
               if(invoices != null && invoices.size() > 0)
               {
                    leftPanel.loadInvoices(invoices);
               }else
               {
                   JOptionPane.showMessageDialog(null,"No data to display","Info",1);
               }
               break;
       }
    }

    private ArrayList<InvoiceHeader> LoadFile() {
        try
        {
            JFileChooser fc = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV FILES", "csv");
            fc.setFileFilter(filter);
            int result = fc.showOpenDialog(this);
            if(result == JFileChooser.APPROVE_OPTION)
            {
                String firstFilePath = fc.getSelectedFile().getPath();
                String secondFilePath = "";
                result = fc.showOpenDialog(this);
                if(result == JFileChooser.APPROVE_OPTION)
                {
                    secondFilePath = fc.getSelectedFile().getPath();

                }else
                {
                    JOptionPane.showMessageDialog(null,"You don't select file","Error",1);
                }
                return loadDataFromFiles(firstFilePath,secondFilePath);

            }else
            {
                JOptionPane.showMessageDialog(null,"You don't select file","Error",1);
            }
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(null,e.getMessage(),"Error",1);
        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,e.getMessage(),"Error",1);
        }


        return null;
    }

    private void SaveFile() {

        if(leftPanel != null && leftPanel.invoicesLst != null && leftPanel.invoicesLst.size() > 0)
        {

            try
            {
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("Specify a file to save");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV FILES", "csv");
                fc.setFileFilter(filter);
                int result = fc.showSaveDialog(this);
                if(result == JFileChooser.APPROVE_OPTION)
                {
                    String firstFilePath = fc.getSelectedFile().getPath();
                    String secondFilePath ="";
                    result = fc.showSaveDialog(this);
                    if(result == JFileChooser.APPROVE_OPTION)
                    {
                        secondFilePath = fc.getSelectedFile().getPath();
                        saveDataToFiles(firstFilePath,secondFilePath,leftPanel.invoicesLst);
                    }else
                    {
                        JOptionPane.showMessageDialog(null,"You don't select file","Error",1);
                    }

                }else
                {
                    JOptionPane.showMessageDialog(null,"You don't select file","Error",1);
                }
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(null,e.getMessage(),"Error",1);
            }catch (Exception e)
            {
                JOptionPane.showMessageDialog(null,e.getMessage(),"Error",1);
            }


        }else
        {
            JOptionPane.showMessageDialog(null,"No data to save","Info",1);
        }
    }

    private void saveDataToFiles(String firstFilePath, String secondFilePath, ArrayList<InvoiceHeader> invoicesLst) {

        FileOperations fileOp = new FileOperations();
        fileOp.set_readInvoiceHeaderFilePath(firstFilePath);
        fileOp.set_readInvoiceLineFilePath(secondFilePath);
        try {
            fileOp.writeFile(invoicesLst);
            JOptionPane.showMessageDialog(null,"files were saved successfully","Info",1);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Error when write file ","Error",1);
        }
    }

    public void setSelectedInvoiceDetails(InvoiceHeader item) {
        rightPanel.setSelectedInvoiceDetails(item);
    }

    public void undoNewInvoice() {
        leftPanel.deleteSelectedInvoice();
    }

    public void saveCurrentItemUpdate(InvoiceHeader item) {
        leftPanel.saveCurrentItemUpdate(item);
    }

    public void clearRightData() {
        rightPanel.clearRightData();
    }
}
