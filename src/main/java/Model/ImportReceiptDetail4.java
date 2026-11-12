package Model;

import java.math.BigDecimal;

public class ImportReceiptDetail4 {
    private int id;
    private int quantity;
    private Document4 document;
    private BigDecimal unitPrice;

    public ImportReceiptDetail4() {
    }

    public ImportReceiptDetail4(int id, int quantity, Document4 document, BigDecimal unitPrice) {
        this.id = id;
        this.quantity = quantity;
        this.document = document;
        this.unitPrice = unitPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Document4 getDocument() {
        return document;
    }

    public void setDocument(Document4 document) {
        this.document = document;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
