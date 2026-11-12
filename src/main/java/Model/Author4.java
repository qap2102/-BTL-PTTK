package Model;

public class Author4 {
    private int id;
    private FullName4 authorName;
    private String email;

    public Author4() {
    }

    public Author4(int id, FullName4 authorName, String email) {
        this.id = id;
        this.authorName = authorName;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FullName4 getAuthorName() {
        return authorName;
    }

    public void setAuthorName(FullName4 authorName) {
        this.authorName = authorName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
