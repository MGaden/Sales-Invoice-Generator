package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InvoiceHeader {

    private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    public int getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(int invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public String getFormatedInvoiceDate() {
        return df.format(getInvoiceDate());
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public ArrayList<InvoiceLine> getInvoiceLines() {
        return invoiceLines;
    }

    public void setInvoiceLines(ArrayList<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    public  double getInvoiceTotal()
    {
        double invoiceTotal = 0;
        if(getInvoiceLines() != null && getInvoiceLines().size() > 0 )
        {
            for (int i = 0; i < getInvoiceLines().size(); i++) {
                invoiceTotal = invoiceTotal + getInvoiceLines().get(i).getTotal();
            }
        }
        return invoiceTotal;
    }
    private int invoiceNum;

    private Date invoiceDate;

    private String customerName;

    private ArrayList<InvoiceLine> invoiceLines ;

    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    private Boolean isNew;

}
