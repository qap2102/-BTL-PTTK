package DAO;

import java.sql.*;

public class ReturnReceiptDetailDAO4 extends DAO4 {
    public ReturnReceiptDetailDAO4() { }
    public ReturnReceiptDetailDAO4(Connection connection) { super(connection); }

    public boolean isReturned(int borrowingDetailId) {
        try (PreparedStatement ps = requireConnection().prepareStatement("SELECT id FROM tblReturnReceiptDetail4 WHERE tblBorrowingReceiptDetail4id=?")) {
            ps.setInt(1,borrowingDetailId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException ex) { throw new IllegalStateException("Unable to check return status.", ex); }
    }

}
