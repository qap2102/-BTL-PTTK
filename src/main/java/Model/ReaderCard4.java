package Model;

public class ReaderCard4 {
    private int id;
    private String status;
    private Reader4 readerInfo;

    public ReaderCard4() {
    }

    public ReaderCard4(int id, String status, Reader4 readerInfo) {
        this.id = id;
        this.status = status;
        this.readerInfo = readerInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Reader4 getReaderInfo() {
        return readerInfo;
    }

    public void setReaderInfo(Reader4 readerInfo) {
        this.readerInfo = readerInfo;
    }
}
