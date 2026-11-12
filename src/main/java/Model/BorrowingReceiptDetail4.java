package Model;

public class BorrowingReceiptDetail4 {
    private int id;
    private String borrowingCondition;
    private DocumentCopy4 documentCopyInfo;
    private BorrowingReceipt4 borrowingReceipt;

    public BorrowingReceipt4 getBorrowingReceipt() { return borrowingReceipt; }

    public void setBorrowingReceipt(BorrowingReceipt4 borrowingReceipt) { this.borrowingReceipt = borrowingReceipt; }

    public BorrowingReceiptDetail4() {
    }

    public BorrowingReceiptDetail4(int id, String borrowingCondition, DocumentCopy4 documentCopyInfo) {
        this.id = id;
        this.borrowingCondition = borrowingCondition;
        this.documentCopyInfo = documentCopyInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBorrowingCondition() {
        return borrowingCondition;
    }

    public void setBorrowingCondition(String borrowingCondition) {
        this.borrowingCondition = borrowingCondition;
    }

    public DocumentCopy4 getDocumentCopyInfo() {
        return documentCopyInfo;
    }

    public void setDocumentCopyInfo(DocumentCopy4 documentCopyInfo) {
        this.documentCopyInfo = documentCopyInfo;
    }
}
