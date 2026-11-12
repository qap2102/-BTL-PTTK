package Model;

public class StorageLocation4 {
    private int id;
    private String room;
    private String shelfCode;
    private String description;

    public StorageLocation4() {
    }

    public StorageLocation4(int id, String room, String shelfCode, String description) {
        this.id = id;
        this.room = room;
        this.shelfCode = shelfCode;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getShelfCode() {
        return shelfCode;
    }

    public void setShelfCode(String shelfCode) {
        this.shelfCode = shelfCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
