package Model;

import java.math.BigDecimal;

public class Document4 {
    private int id;
    private String documentCode;
    private DocumentType4 documentType;
    private String documentTitle;
    private DocumentAuthor4[] listDocumentAuthors = new DocumentAuthor4[0];
    private DocumentPublisher4[] listDocumentPublishers = new DocumentPublisher4[0];
    private DocumentCategory4 documentCategory;
    private BigDecimal referencePrice;

    public Document4() {
    }

    public Document4(int id, String documentCode, DocumentType4 documentType, String documentTitle, DocumentAuthor4[] listDocumentAuthors, DocumentPublisher4[] listDocumentPublishers, DocumentCategory4 documentCategory, BigDecimal referencePrice) {
        this.id = id;
        this.documentCode = documentCode;
        this.documentType = documentType;
        this.documentTitle = documentTitle;
        this.listDocumentAuthors = listDocumentAuthors;
        this.listDocumentPublishers = listDocumentPublishers;
        this.documentCategory = documentCategory;
        this.referencePrice = referencePrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocumentCode() {
        return documentCode;
    }

    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }

    public DocumentType4 getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType4 documentType) {
        this.documentType = documentType;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public DocumentAuthor4[] getListDocumentAuthors() {
        return listDocumentAuthors;
    }

    public void setListDocumentAuthors(DocumentAuthor4[] listDocumentAuthors) {
        this.listDocumentAuthors = listDocumentAuthors;
    }

    public DocumentPublisher4[] getListDocumentPublishers() {
        return listDocumentPublishers;
    }

    public void setListDocumentPublishers(DocumentPublisher4[] listDocumentPublishers) {
        this.listDocumentPublishers = listDocumentPublishers;
    }

    public DocumentCategory4 getDocumentCategory() {
        return documentCategory;
    }

    public void setDocumentCategory(DocumentCategory4 documentCategory) {
        this.documentCategory = documentCategory;
    }

    public BigDecimal getReferencePrice() {
        return referencePrice;
    }

    public void setReferencePrice(BigDecimal referencePrice) {
        this.referencePrice = referencePrice;
    }
}
