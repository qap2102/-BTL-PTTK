package Model;

public class DocumentCopy4 {
    private int id;
    private String copyCode;
    private String physicalCondition;
    private StorageLocation4 storageLocation;
    private Document4 document;
    private DocumentPublisher4 documentPublisher;

    public DocumentCopy4() {
    }

    public DocumentCopy4(int id, String copyCode, String physicalCondition, StorageLocation4 storageLocation, Document4 document, DocumentPublisher4 documentPublisher) {
        this.id = id;
        this.copyCode = copyCode;
        this.physicalCondition = physicalCondition;
        this.storageLocation = storageLocation;
        this.document = document;
        this.documentPublisher = documentPublisher;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCopyCode() {
        return copyCode;
    }

    public void setCopyCode(String copyCode) {
        this.copyCode = copyCode;
    }

    public String getPhysicalCondition() {
        return physicalCondition;
    }

    public void setPhysicalCondition(String physicalCondition) {
        this.physicalCondition = physicalCondition;
    }

    public StorageLocation4 getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(StorageLocation4 storageLocation) {
        this.storageLocation = storageLocation;
    }

    public Document4 getDocument() {
        return document;
    }

    public void setDocument(Document4 document) {
        this.document = document;
    }

    public DocumentPublisher4 getDocumentPublisher() {
        return documentPublisher;
    }

    public void setDocumentPublisher(DocumentPublisher4 documentPublisher) {
        this.documentPublisher = documentPublisher;
    }
}
