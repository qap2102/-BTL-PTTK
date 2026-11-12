package Model;

public class Supplier4 {
    private int id;
    private String supplierName;
    private String email;
    private String phoneNumber;
    private Address4 address;

    public Supplier4() {
    }

    public Supplier4(int id, String supplierName, String email, String phoneNumber, Address4 address) {
        this.id = id;
        this.supplierName = supplierName;
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

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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
