package Model;

public class Reader4 extends Member4 {
    private String readerCode;
    private ReaderCategory4 readerCategory;

    public Reader4() {
    }

    public Reader4(String readerCode, ReaderCategory4 readerCategory) {
        this.readerCode = readerCode;
        this.readerCategory = readerCategory;
    }

    public String getReaderCode() {
        return readerCode;
    }

    public void setReaderCode(String readerCode) {
        this.readerCode = readerCode;
    }

    public ReaderCategory4 getReaderCategory() {
        return readerCategory;
    }

    public void setReaderCategory(ReaderCategory4 readerCategory) {
        this.readerCategory = readerCategory;
    }
}
