package model;

public class InvoiceLine {

    private int invoiceNum;

    public int getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(int invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getCount() {
        return count;
    }

    public double getTotal() {
        return count * itemPrice;
    }
    public void setCount(int count) {
        this.count = count;
    }

    private String itemName;

    private double itemPrice;

    private  int count;

}
