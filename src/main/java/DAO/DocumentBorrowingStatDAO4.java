package DAO;

import Model.DocumentBorrowingStat4;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DocumentBorrowingStatDAO4 extends DAO4 {
    public DocumentBorrowingStatDAO4() { }
    public DocumentBorrowingStatDAO4(Connection connection) { super(connection); }

    /** Each borrowed physical copy counts as one turn, including both boundary dates. */
    public List<DocumentBorrowingStat4> getDocumentStatistics(Date startDate, Date endDate) {
        if (startDate == null || endDate == null || startDate.after(endDate)) throw new IllegalArgumentException("Invalid statistics period.");
        String sql = "SELECT d.id,d.documentCode,d.documentTitle,d.referencePrice,COUNT(*) AS borrowCount "
                + "FROM tblBorrowingReceipt4 b JOIN tblBorrowingReceiptDetail4 bd ON bd.tblBorrowingReceipt4id=b.id "
                + "JOIN tblDocumentCopy4 cp ON cp.id=bd.tblDocumentCopy4id JOIN tblDocument4 d ON d.id=cp.tblDocument4id "
                + "WHERE b.borrowDate BETWEEN ? AND ? GROUP BY d.id,d.documentCode,d.documentTitle,d.referencePrice "
                + "ORDER BY borrowCount DESC,d.documentCode ASC";
        List<DocumentBorrowingStat4> result = new ArrayList<>();
        try (PreparedStatement ps = requireConnection().prepareStatement(sql)) {
            ps.setDate(1,new java.sql.Date(startDate.getTime()));
            ps.setDate(2,new java.sql.Date(endDate.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DocumentBorrowingStat4 stat = new DocumentBorrowingStat4();
                    DocumentDAO4.mapDocument(rs,stat);
                    stat.setStartDate(startDate); stat.setEndDate(endDate);
                    stat.setTotalBorrowCount(rs.getInt("borrowCount"));
                    result.add(stat);
                }
            }
            for (DocumentBorrowingStat4 stat : result) stat.setTotalDocuments(result.size());
            new DocumentDAO4(connection).loadAuthors(result);
            return result;
        } catch (SQLException ex) { throw new IllegalStateException("Unable to load document borrowing statistics.", ex); }
    }
}