package Model;

import java.math.BigDecimal;
import java.util.Date;

public class ReturnReceipt4 {
    private int id;
    private BigDecimal totalFine;
    private Date returnDate;
    private String paymentStatus;
    private Librarian4 librarian;
    private ReturnReceiptDetail4[] listReturn = new ReturnReceiptDetail4[0];

    public ReturnReceipt4() {
    }

    public ReturnReceipt4(int id, BigDecimal totalFine, Date returnDate, String paymentStatus, Librarian4 librarian, ReturnReceiptDetail4[] listReturn) {
        this.id = id;
        this.totalFine = totalFine;
        this.returnDate = returnDate;
        this.paymentStatus = paymentStatus;
        this.librarian = librarian;
        this.listReturn = listReturn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getTotalFine() {
        return totalFine;
    }

    public void setTotalFine(BigDecimal totalFine) {
        this.totalFine = totalFine;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Librarian4 getLibrarian() {
        return librarian;
    }

    public void setLibrarian(Librarian4 librarian) {
        this.librarian = librarian;
    }

    public ReturnReceiptDetail4[] getListReturn() {
        return listReturn;
    }

    public void setListReturn(ReturnReceiptDetail4[] listReturn) {
        this.listReturn = listReturn;
    }
}
