package Model;

import java.util.Date;

public class ReaderStat4 extends Reader4 {
    private Date startDate;
    private Date endDate;
    private int numberOfReaders;

    public ReaderStat4() {
    }

    public ReaderStat4(Date startDate, Date endDate, int numberOfReaders) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfReaders = numberOfReaders;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getNumberOfReaders() {
        return numberOfReaders;
    }

    public void setNumberOfReaders(int numberOfReaders) {
        this.numberOfReaders = numberOfReaders;
    }
}
