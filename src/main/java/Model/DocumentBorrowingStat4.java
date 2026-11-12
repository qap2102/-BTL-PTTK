package Model;

import java.util.Date;

public class DocumentBorrowingStat4 extends Document4 {
    private Date startDate;
    private Date endDate;
    private int totalDocuments;
    private int totalBorrowCount;

    public DocumentBorrowingStat4() {
    }

    public DocumentBorrowingStat4(Date startDate, Date endDate, int totalDocuments, int totalBorrowCount) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalDocuments = totalDocuments;
        this.totalBorrowCount = totalBorrowCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getTotalDocuments() {
        return totalDocuments;
    }

    public void setTotalDocuments(int totalDocuments) {
        this.totalDocuments = totalDocuments;
    }

    public int getTotalBorrowCount() {
        return totalBorrowCount;
    }

    public void setTotalBorrowCount(int totalBorrowCount) {
        this.totalBorrowCount = totalBorrowCount;
    }
}
