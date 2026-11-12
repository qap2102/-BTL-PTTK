package Model;

public class DocumentPublisher4 {
    private int id;
    private Document4 document;
    private Publisher4 publisher;
    private String publicationYear;
    private int editionNumber;
    private String editionDescription;

    public DocumentPublisher4() {
    }

    public DocumentPublisher4(int id, Document4 document, Publisher4 publisher, String publicationYear, int editionNumber, String editionDescription) {
        this.id = id;
        this.document = document;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.editionNumber = editionNumber;
        this.editionDescription = editionDescription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Document4 getDocument() {
        return document;
    }

    public void setDocument(Document4 document) {
        this.document = document;
    }

    public Publisher4 getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher4 publisher) {
        this.publisher = publisher;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(String publicationYear) {
        this.publicationYear = publicationYear;
    }

    public int getEditionNumber() {
        return editionNumber;
    }

    public void setEditionNumber(int editionNumber) {
        this.editionNumber = editionNumber;
    }

    public String getEditionDescription() {
        return editionDescription;
    }

    public void setEditionDescription(String editionDescription) {
        this.editionDescription = editionDescription;
    }
}
