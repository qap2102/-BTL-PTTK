package Model;

import java.util.Date;

public class Member4 {
    private int id;
    private String userName;
    private String password;
    private FullName4 fullName;
    private Date dateOfBirth;
    private String email;
    private String phoneNumber;
    private Address4 address;

    public Member4() {
    }

    public Member4(int id, String userName, String password, FullName4 fullName, Date dateOfBirth, String email, String phoneNumber, Address4 address) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FullName4 getFullName() {
        return fullName;
    }

    public void setFullName(FullName4 fullName) {
        this.fullName = fullName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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
