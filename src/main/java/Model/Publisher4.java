package Model;

public class Publisher4 {
    private int id;
    private String publisherName;
    private String email;
    private String phoneNumber;
    private Address4 address;

    public Publisher4() {
    }

    public Publisher4(int id, String publisherName, String email, String phoneNumber, Address4 address) {
        this.id = id;
        this.publisherName = publisherName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address4 getAddress() {
        return address;
    }

    public void setAddress(Address4 address) {
        this.address = address;
    }
}
