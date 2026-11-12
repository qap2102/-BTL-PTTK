package Model;

import java.util.Date;

public class BorrowingReceipt4 {
    private int id;
    private String receiptCode;
    private Date borrowDate;
    private Date dueDate;
    private String status;
    private Reader4 reader;
    private Librarian4 librarian;
    private BorrowingReceiptDetail4[] borrowingList = new BorrowingReceiptDetail4[0];

    public BorrowingReceipt4() {
    }

    public BorrowingReceipt4(int id, String receiptCode, Date borrowDate, Date dueDate, String status, Reader4 reader, Librarian4 librarian, BorrowingReceiptDetail4[] borrowingList) {
        this.id = id;
        this.receiptCode = receiptCode;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = status;
        this.reader = reader;
        this.librarian = librarian;
        this.borrowingList = borrowingList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReceiptCode() {
        return receiptCode;
    }

    public void setReceiptCode(String receiptCode) {
        this.receiptCode = receiptCode;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Reader4 getReader() {
        return reader;
    }

    public void setReader(Reader4 reader) {
        this.reader = reader;
    }

    public Librarian4 getLibrarian() {
        return librarian;
    }

    public void setLibrarian(Librarian4 librarian) {
        this.librarian = librarian;
    }

    public BorrowingReceiptDetail4[] getBorrowingList() {
        return borrowingList;
    }

    public void setBorrowingList(BorrowingReceiptDetail4[] borrowingList) {
        this.borrowingList = borrowingList;
    }
}
