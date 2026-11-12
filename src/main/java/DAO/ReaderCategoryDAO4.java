package DAO;

import Model.ReaderCategory4;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReaderCategoryDAO4 extends DAO4 {
    public ReaderCategoryDAO4() { }
    public ReaderCategoryDAO4(Connection connection) { super(connection); }

    public List<ReaderCategory4> getAllReaderCategory() {
        List<ReaderCategory4> categories = new ArrayList<>();
        try (PreparedStatement ps = requireConnection().prepareStatement("SELECT * FROM tblReaderCategory4 ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) categories.add(map(rs));
            return categories;
        } catch (SQLException ex) { throw new IllegalStateException("Unable to load reader categories.", ex); }
    }

    public ReaderCategory4 getReaderCategory(int id) {
        try (PreparedStatement ps = requireConnection().prepareStatement("SELECT * FROM tblReaderCategory4 WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        } catch (SQLException ex) { throw new IllegalStateException("Unable to load reader category.", ex); }
    }

    private ReaderCategory4 map(ResultSet rs) throws SQLException {
        return new ReaderCategory4(rs.getInt("id"), rs.getString("readerType"), rs.getInt("maxBorrowQuantity"), rs.getInt("borrowPeriod"));
    }
}