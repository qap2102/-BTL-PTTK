package DAO;

import Model.*;
import java.sql.*;
import java.text.Normalizer;
import java.util.Locale;

/** Loads a member and their role from the existing library accounts. */
public class MemberDAO4 extends DAO4 {
    public MemberDAO4() { }
    public MemberDAO4(Connection connection) { super(connection); }

    public Member4 login(String userName, String password) {
        String sql = "SELECT m.*, r.readerCode, r.tblReaderCategory4Id, s.staffCode, s.position "
                + "FROM tblMember4 m LEFT JOIN tblReader4 r ON r.tblMember4Id=m.id "
                + "LEFT JOIN tblStaff4 s ON s.tblMember4Id=m.id WHERE m.userName=?";
        try (PreparedStatement ps = requireConnection().prepareStatement(sql)) {
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next() || !password.equals(rs.getString("password"))) return null;
                Member4 member;
                if (rs.getString("staffCode") != null) {
                    String position = rs.getString("position");
                    if (position == null) throw new IllegalStateException("Staff position is missing.");
                    String role = Normalizer.normalize(position, Normalizer.Form.NFD)
                            .replaceAll("\\p{M}", "").replace('đ', 'd').replace('Đ', 'D').toLowerCase(Locale.ROOT);
                    Staff4 staff;
                    if (role.contains("manager") || role.contains("quan ly")) staff = new Manager4();
                    else if (role.contains("librarian") || role.contains("thu vien") || role.contains("thu thu")) staff = new Librarian4();
                    else throw new IllegalStateException("Unknown staff position in the database.");
                    staff.setStaffCode(rs.getString("staffCode"));
                    staff.setPosition(rs.getString("position"));
                    member = staff;
                } else if (rs.getString("readerCode") != null) {
                    Reader4 reader = new Reader4();
                    reader.setReaderCode(rs.getString("readerCode"));
                    reader.setReaderCategory(new ReaderCategoryDAO4(connection).getReaderCategory(rs.getInt("tblReaderCategory4Id")));
                    member = reader;
                } else throw new IllegalStateException("Account has no reader or staff profile.");
                populateMember(rs, member);
                return member;
            }
        } catch (SQLException ex) { throw new IllegalStateException("Unable to authenticate the member.", ex); }
    }

    static void populateMember(ResultSet rs, Member4 member) throws SQLException {
        member.setId(rs.getInt("id"));
        member.setUserName(rs.getString("userName"));
        member.setFullName(new FullName4(0, rs.getString("firstName"), rs.getString("middleName"), rs.getString("lastName")));
        String birth = rs.getString("dateOfBirth");
        if (birth != null && !birth.isEmpty()) {
            try { member.setDateOfBirth(Date.valueOf(birth.substring(0, Math.min(10, birth.length())))); }
            catch (IllegalArgumentException ex) { throw new SQLException("Invalid dateOfBirth in member profile.", ex); }
        }
        member.setEmail(rs.getString("email"));
        member.setPhoneNumber(rs.getString("phoneNumber"));
        // Never copy the database password into the session model.
    }
}
