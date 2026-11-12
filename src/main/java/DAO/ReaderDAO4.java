package DAO;

import Model.Reader4;
import java.sql.*;

public class ReaderDAO4 extends DAO4 {
    public ReaderDAO4() { }
    public ReaderDAO4(Connection connection) { super(connection); }

    public Reader4 getReader(String readerCode) { return find("r.readerCode", readerCode); }
    public Reader4 getReaderById(int memberId) { return find("m.id", memberId); }

    private Reader4 find(String column, Object value) {
        String sql = "SELECT m.*, r.readerCode, r.tblReaderCategory4Id FROM tblMember4 m "
                + "JOIN tblReader4 r ON r.tblMember4Id=m.id WHERE " + column + "=?";
        try (PreparedStatement ps = requireConnection().prepareStatement(sql)) {
            ps.setObject(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Reader4 reader = new Reader4();
                MemberDAO4.populateMember(rs, reader);
                reader.setReaderCode(rs.getString("readerCode"));
                reader.setReaderCategory(new ReaderCategoryDAO4(connection).getReaderCategory(rs.getInt("tblReaderCategory4Id")));
                return reader;
            }
        } catch (SQLException ex) { throw new IllegalStateException("Unable to load reader.", ex); }
    }
}