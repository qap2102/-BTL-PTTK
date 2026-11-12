package Model;

public class DocumentAuthor4 {
    private int id;
    private String authorRole;
    private Author4 author;
    private Document4 document;

    public DocumentAuthor4() {
    }

    public DocumentAuthor4(int id, String authorRole, Author4 author, Document4 document) {
        this.id = id;
        this.authorRole = authorRole;
        this.author = author;
        this.document = document;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthorRole() {
        return authorRole;
    }

    public void setAuthorRole(String authorRole) {
        this.authorRole = authorRole;
    }

    public Author4 getAuthor() {
        return author;
    }

    public void setAuthor(Author4 author) {
        this.author = author;
    }

    public Document4 getDocument() {
        return document;
    }

    public void setDocument(Document4 document) {
        this.document = document;
    }
}
