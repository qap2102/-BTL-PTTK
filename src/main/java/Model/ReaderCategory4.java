package Model;

public class ReaderCategory4 {
    private int id;
    private String readerType;
    private int maxBorrowQuantity;
    private int borrowPeriod;

    public ReaderCategory4() {
    }

    public ReaderCategory4(int id, String readerType, int maxBorrowQuantity, int borrowPeriod) {
        this.id = id;
        this.readerType = readerType;
        this.maxBorrowQuantity = maxBorrowQuantity;
        this.borrowPeriod = borrowPeriod;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReaderType() {
        return readerType;
    }

    public void setReaderType(String readerType) {
        this.readerType = readerType;
    }

    public int getMaxBorrowQuantity() {
        return maxBorrowQuantity;
    }

    public void setMaxBorrowQuantity(int maxBorrowQuantity) {
        this.maxBorrowQuantity = maxBorrowQuantity;
    }

    public int getBorrowPeriod() {
        return borrowPeriod;
    }

    public void setBorrowPeriod(int borrowPeriod) {
        this.borrowPeriod = borrowPeriod;
    }
}
