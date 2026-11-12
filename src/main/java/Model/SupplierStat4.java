package Model;

import java.util.Date;

public class SupplierStat4 extends Supplier4 {
    private Date startDate;
    private Date endDate;
    private int numberOfSupplier;

    public SupplierStat4() {
    }

    public SupplierStat4(Date startDate, Date endDate, int numberOfSupplier) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfSupplier = numberOfSupplier;
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

    public int getNumberOfSupplier() {
        return numberOfSupplier;
    }

    public void setNumberOfSupplier(int numberOfSupplier) {
        this.numberOfSupplier = numberOfSupplier;
    }
}
