package model;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//used to save and write invoices to file
public class FileOperations {

    public String get_readInvoiceHeaderFilePath() {
        return _readInvoiceHeaderFilePath;
    }

    public void set_readInvoiceHeaderFilePath(String _readInvoiceHeaderFilePath) {
        this._readInvoiceHeaderFilePath = _readInvoiceHeaderFilePath;
    }

    public String get_readInvoiceLineFilePath() {
        return _readInvoiceLineFilePath;
    }

    public void set_readInvoiceLineFilePath(String _readInvoiceLineFilePath) {
        this._readInvoiceLineFilePath = _readInvoiceLineFilePath;
    }

    //invoiceNum, invoiceDate, CustomerName,
    private  String _readInvoiceHeaderFilePath;// InvoiceHeader.csv

    // invoiceNum, itemName, itemPrice, Count.
    private  String _readInvoiceLineFilePath;// InvoiceLines.csv

    public  FileOperations()
    {
    }
    public ArrayList<InvoiceHeader> readFile()
    {
        ArrayList<InvoiceHeader> invoice =new ArrayList<>();
        String line = "";
        String splitBy = ",";
        try
        {
            //parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader(_readInvoiceHeaderFilePath));
            while ((line = br.readLine()) != null)   //returns a Boolean value
            {
                String[] invoices = line.split(splitBy);
                if(invoices != null && invoices.length > 2)
                {
                    InvoiceHeader item = new InvoiceHeader();
                    item.setInvoiceNum( Integer.parseInt(invoices[0]));

                    SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
                    try {
                        item.setInvoiceDate(formatter.parse(invoices[1]));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    item.setCustomerName(invoices[2]);
                    invoice.add(item);
                }else
                {
                    throw new ParseException("Wrong file format",0);
                }

            }

            //read invoice line
            br = new BufferedReader(new FileReader(_readInvoiceLineFilePath));
            while ((line = br.readLine()) != null)   //returns a Boolean value
            {
                String[] invoices = line.split(splitBy);
                if(invoices != null && invoices.length > 3)
                {
                    InvoiceLine item = new InvoiceLine();
                    int invoiceNum = Integer.parseInt(invoices[0]);
                    item.setInvoiceNum(invoiceNum);
                    item.setItemName(invoices[1]);
                    item.setItemPrice(Double.parseDouble(invoices[2]));
                    item.setCount(Integer.parseInt(invoices[3]));
                    InvoiceHeader invoiceItem = findUsingEnhancedForLoop(invoiceNum , invoice);
                    if(invoiceItem != null)
                    {
                        if(invoiceItem.getInvoiceLines() == null)
                            invoiceItem.setInvoiceLines(new ArrayList<>());

                        invoiceItem.getInvoiceLines().add(item);

                    }
                }else
                {
                    throw new ParseException("Wrong file format",0);
                }

            }
        }
         catch (FileNotFoundException e) {
            throw new RuntimeException("File is not found ");
        } catch (IOException e) {
            throw new RuntimeException("File is corrupted ");
        }catch (ParseException e) {
            throw new RuntimeException("Wrong file format ");
        }catch (Exception e)
        {
            throw new RuntimeException("Application error");
        }
        return invoice;
    }

    public InvoiceHeader findUsingEnhancedForLoop(
            int invoiceNum, ArrayList<InvoiceHeader> invoices) {

        for (InvoiceHeader item : invoices) {
            if (item.getInvoiceNum() == (invoiceNum)) {
                return item;
            }
        }
        return null;
    }

    public void writeFile(ArrayList<InvoiceHeader> invoices) throws IOException {
        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();

        //invoiceNum, invoiceDate, CustomerName
        for (InvoiceHeader invoice : invoices) {

            list1.add(invoice.getInvoiceNum() + "," + invoice.getFormatedInvoiceDate() + "," + invoice.getCustomerName());

            if(invoice != null && invoice.getInvoiceLines() != null && invoice.getInvoiceLines().size() > 0)
            {
                // invoiceNum, itemName, itemPrice, Count
                for (InvoiceLine line : invoice.getInvoiceLines()) {

                    list2.add(line.getInvoiceNum() + "," + line.getItemName() + "," + line.getItemPrice() + "," + line.getCount());
                }
            }
        }



        File file1 = new File(_readInvoiceHeaderFilePath);
        FileWriter fw1 = new FileWriter(file1);
        BufferedWriter bw1 = new BufferedWriter(fw1);

        for(int i=0;i<list1.size();i++)
        {
            bw1.write(list1.get(i));
            bw1.newLine();
        }

        File file = new File(_readInvoiceLineFilePath);
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);

        for(int i=0;i<list2.size();i++)
        {
            bw.write(list2.get(i));
            bw.newLine();
        }
        bw.close();
        fw.close();
        bw1.close();
        fw1.close();

    }

    void Test()
    {
        ArrayList<InvoiceHeader> invoices = readFile();
        if(invoices != null && invoices.size() > 0)
        {
            for(int index=0; index< invoices.size() ; index++)
            {
                InvoiceHeader item = invoices.get(index);
                System.out.println(item.getInvoiceNum());

                DateFormat dateFormat = new SimpleDateFormat("DD/MM/YYYY");
                String strDate = dateFormat.format(item.getInvoiceDate());
                System.out.println(strDate + " , " + item.getCustomerName() );
                ArrayList<InvoiceLine> lines = item.getInvoiceLines();
                if(lines != null && lines.size() > 0)
                {
                    for(int indexLine=0; indexLine< lines.size() ; indexLine++)
                    {
                        InvoiceLine itemLine = lines.get(indexLine);
                        System.out.println(itemLine.getItemName() + " , " + itemLine.getItemPrice() + " , " + itemLine.getCount());

                    }
                }else
                {
                    System.out.println("No line date");
                }

            }
        }else
        {
            System.out.println("No invocie date");
        }
    }
}
