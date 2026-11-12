package DAO;

import Model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class BorrowingReceiptDetailDAO4 extends DAO4 {
    public BorrowingReceiptDetailDAO4() { }
    public BorrowingReceiptDetailDAO4(Connection connection) { super(connection); }

    private static final String DETAIL_SQL = "SELECT bd.id,bd.borrowingCondition,cp.id AS copyId,cp.copyCode,cp.physicalCondition, "
            + "d.id AS documentId,d.documentCode,d.documentTitle,b.id AS receiptId,b.receiptCode,b.borrowDate,b.dueDate,b.status, "
            + "r.tblMember4id,r.readerCode FROM tblBorrowingReceiptDetail4 bd "
            + "JOIN tblDocumentCopy4 cp ON cp.id=bd.tblDocumentCopy4id JOIN tblDocument4 d ON d.id=cp.tblDocument4id "
            + "JOIN tblBorrowingReceipt4 b ON b.id=bd.tblBorrowingReceipt4id JOIN tblReader4 r ON r.tblMember4id=b.tblReader4id ";

    public List<BorrowingReceiptDetail4> getListBorrowing(int id) { return getListBorrowing(id,null,null); }

    public List<BorrowingReceiptDetail4> getListBorrowing(int id, Date startDate, Date endDate) {
        return query("d.id",id,startDate,endDate);
    }

    public List<BorrowingReceiptDetail4> getDetailsByReceiptId(int id) { return query("b.id",id,null,null); }

    private List<BorrowingReceiptDetail4> query(String column, int id, Date startDate, Date endDate) {
        boolean period = startDate != null && endDate != null;
        String sql = DETAIL_SQL + "WHERE " + column + "=? " + (period ? "AND b.borrowDate BETWEEN ? AND ? " : "")
                + "ORDER BY b.borrowDate DESC,b.id DESC,bd.id";
        List<BorrowingReceiptDetail4> list = new ArrayList<>();
        try (PreparedStatement ps = requireConnection().prepareStatement(sql)) {
            ps.setInt(1,id);
            if (period) { ps.setDate(2,new java.sql.Date(startDate.getTime())); ps.setDate(3,new java.sql.Date(endDate.getTime())); }
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    Document4 document = new Document4();
                    document.setId(rs.getInt("documentId")); document.setDocumentCode(rs.getString("documentCode")); document.setDocumentTitle(rs.getString("documentTitle"));
                    DocumentCopy4 copy = new DocumentCopy4();
                    copy.setId(rs.getInt("copyId")); copy.setCopyCode(rs.getString("copyCode")); copy.setPhysicalCondition(rs.getString("physicalCondition")); copy.setDocument(document);
                    Reader4 reader = new Reader4(); reader.setId(rs.getInt("tblMember4id")); reader.setReaderCode(rs.getString("readerCode"));
                    BorrowingReceipt4 receipt = new BorrowingReceipt4();
                    receipt.setId(rs.getInt("receiptId")); receipt.setReceiptCode(rs.getString("receiptCode")); receipt.setBorrowDate(rs.getDate("borrowDate"));
                    receipt.setDueDate(rs.getDate("dueDate")); receipt.setStatus(rs.getString("status")); receipt.setReader(reader);
                    BorrowingReceiptDetail4 detail = new BorrowingReceiptDetail4(rs.getInt("id"),rs.getString("borrowingCondition"),copy);
                    detail.setBorrowingReceipt(receipt); list.add(detail);
                }
            }
            return list;
        } catch (SQLException ex) { throw new IllegalStateException("Unable to load borrowing details.", ex); }
    }
}