package Model;

public class Staff4 extends Member4 {
    private String staffCode;
    private String position;

    public Staff4() {
    }

    public Staff4(String staffCode, String position) {
        this.staffCode = staffCode;
        this.position = position;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
