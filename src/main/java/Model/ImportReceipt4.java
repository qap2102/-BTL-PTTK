package Model;

import java.math.BigDecimal;
import java.util.Date;

public class ImportReceipt4 {
    private int id;
    private Date importDate;
    private Librarian4 importUser;
    private Supplier4 supplierInfo;
    private ImportReceiptDetail4[] listImport = new ImportReceiptDetail4[0];
    private BigDecimal totalAmount;

    public ImportReceipt4() {
    }

    public ImportReceipt4(int id, Date importDate, Librarian4 importUser, Supplier4 supplierInfo, ImportReceiptDetail4[] listImport, BigDecimal totalAmount) {
        this.id = id;
        this.importDate = importDate;
        this.importUser = importUser;
        this.supplierInfo = supplierInfo;
        this.listImport = listImport;
        this.totalAmount = totalAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public Librarian4 getImportUser() {
        return importUser;
    }

    public void setImportUser(Librarian4 importUser) {
        this.importUser = importUser;
    }

    public Supplier4 getSupplierInfo() {
        return supplierInfo;
    }

    public void setSupplierInfo(Supplier4 supplierInfo) {
        this.supplierInfo = supplierInfo;
    }

    public ImportReceiptDetail4[] getListImport() {
        return listImport;
    }

    public void setListImport(ImportReceiptDetail4[] listImport) {
        this.listImport = listImport;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
