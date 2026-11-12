package DAO;

import Model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class BorrowingReceiptDAO4 extends DAO4 {
    public BorrowingReceiptDAO4() { }
    public BorrowingReceiptDAO4(Connection connection) { super(connection); }

    private static final String RECEIPT_SQL = "SELECT b.*,r.readerCode,rm.firstName AS readerFirstName,rm.middleName AS readerMiddleName,rm.lastName AS readerLastName, "
            + "rm.email AS readerEmail,rm.phoneNumber AS readerPhone,s.staffCode,s.position, "
            + "sm.firstName AS staffFirstName,sm.middleName AS staffMiddleName,sm.lastName AS staffLastName "
            + "FROM tblBorrowingReceipt4 b JOIN tblReader4 r ON r.tblMember4id=b.tblReader4id "
            + "JOIN tblMember4 rm ON rm.id=r.tblMember4id JOIN tblStaff4 s ON s.tblMember4id=b.tblStaff4id "
            + "JOIN tblMember4 sm ON sm.id=s.tblMember4id ";

    public List<BorrowingReceipt4> getListBorrowingReceipts(String documentCode) {
        return getListBorrowingReceipts(documentCode,null,null);
    }

    public List<BorrowingReceipt4> getListBorrowingReceipts(String documentCode, Date startDate, Date endDate) {
        boolean period = startDate != null && endDate != null;
        String sql = RECEIPT_SQL + "WHERE EXISTS (SELECT 1 FROM tblBorrowingReceiptDetail4 bd "
                + "JOIN tblDocumentCopy4 cp ON cp.id=bd.tblDocumentCopy4id JOIN tblDocument4 d ON d.id=cp.tblDocument4id "
                + "WHERE bd.tblBorrowingReceipt4id=b.id AND d.documentCode=?) "
                + (period ? "AND b.borrowDate BETWEEN ? AND ? " : "") + "ORDER BY b.borrowDate DESC,b.id DESC";
        List<BorrowingReceipt4> list = new ArrayList<>();
        try (PreparedStatement ps = requireConnection().prepareStatement(sql)) {
            ps.setString(1,documentCode);
            if (period) { ps.setDate(2,new java.sql.Date(startDate.getTime())); ps.setDate(3,new java.sql.Date(endDate.getTime())); }
            try (ResultSet rs = ps.executeQuery()) { while(rs.next()) list.add(mapReceipt(rs)); }
            return list;
        } catch (SQLException ex) { throw new IllegalStateException("Unable to load borrowing receipts.", ex); }
    }

    public BorrowingReceipt4 getBorrowingReceipt(int id) {
        try (PreparedStatement ps = requireConnection().prepareStatement(RECEIPT_SQL + "WHERE b.id=?")) {
            ps.setInt(1,id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? mapReceipt(rs) : null; }
        } catch (SQLException ex) { throw new IllegalStateException("Unable to load borrowing receipt.", ex); }
    }

    private BorrowingReceipt4 mapReceipt(ResultSet rs) throws SQLException {
        Reader4 reader = new Reader4();
        reader.setId(rs.getInt("tblReader4id")); reader.setReaderCode(rs.getString("readerCode"));
        reader.setFullName(new FullName4(0,rs.getString("readerFirstName"),rs.getString("readerMiddleName"),rs.getString("readerLastName")));
        reader.setEmail(rs.getString("readerEmail")); reader.setPhoneNumber(rs.getString("readerPhone"));
        Librarian4 librarian = new Librarian4();
        librarian.setId(rs.getInt("tblStaff4id")); librarian.setStaffCode(rs.getString("staffCode")); librarian.setPosition(rs.getString("position"));
        librarian.setFullName(new FullName4(0,rs.getString("staffFirstName"),rs.getString("staffMiddleName"),rs.getString("staffLastName")));
        BorrowingReceipt4 receipt = new BorrowingReceipt4();
        receipt.setId(rs.getInt("id")); receipt.setReceiptCode(rs.getString("receiptCode"));
        receipt.setBorrowDate(rs.getDate("borrowDate")); receipt.setDueDate(rs.getDate("dueDate")); receipt.setStatus(rs.getString("status"));
        receipt.setReader(reader); receipt.setLibrarian(librarian);
        return receipt;
    }
}