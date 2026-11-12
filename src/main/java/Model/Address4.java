package Model;

public class Address4 {
    private int id;
    private String province;
    private String ward;
    private String streetAddress;
    private String description;

    public Address4() {
    }

    public Address4(int id, String province, String ward, String streetAddress, String description) {
        this.id = id;
        this.province = province;
        this.ward = ward;
        this.streetAddress = streetAddress;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
