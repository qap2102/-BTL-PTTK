package Model;

import java.math.BigDecimal;

public class ReturnReceiptDetail4 {
    private int id;
    private String returningCondition;
    private BigDecimal overdueFine;
    private BigDecimal damageFine;
    private String note;
    private BorrowingReceiptDetail4 borrowingDetail;

    public ReturnReceiptDetail4() {
    }

    public ReturnReceiptDetail4(int id, String returningCondition, BigDecimal overdueFine, BigDecimal damageFine, String note, BorrowingReceiptDetail4 borrowingDetail) {
        this.id = id;
        this.returningCondition = returningCondition;
        this.overdueFine = overdueFine;
        this.damageFine = damageFine;
        this.note = note;
        this.borrowingDetail = borrowingDetail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReturningCondition() {
        return returningCondition;
    }

    public void setReturningCondition(String returningCondition) {
        this.returningCondition = returningCondition;
    }

    public BigDecimal getOverdueFine() {
        return overdueFine;
    }

    public void setOverdueFine(BigDecimal overdueFine) {
        this.overdueFine = overdueFine;
    }

    public BigDecimal getDamageFine() {
        return damageFine;
    }

    public void setDamageFine(BigDecimal damageFine) {
        this.damageFine = damageFine;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BorrowingReceiptDetail4 getBorrowingDetail() {
        return borrowingDetail;
    }

    public void setBorrowingDetail(BorrowingReceiptDetail4 borrowingDetail) {
        this.borrowingDetail = borrowingDetail;
    }
}
