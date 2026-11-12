package DAO;

import Model.Reader4;
import Model.ReaderCard4;
import java.sql.*;

public class ReaderCardDAO4 extends DAO4 {
    public ReaderCardDAO4() { }
    public ReaderCardDAO4(Connection connection) { super(connection); }

    public boolean hasReaderCard(Reader4 reader) {
        try (PreparedStatement ps = requireConnection().prepareStatement("SELECT id FROM tblReaderCard4 WHERE tblReader4Id=?")) {
            ps.setInt(1, reader.getId());
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException ex) { throw new IllegalStateException("Unable to check reader card.", ex); }
    }

    /** Locks the reader and atomically saves their profile and new card. */
    public ReaderCard4 createReaderCard(Reader4 reader) {
        Connection c = requireConnection();
        try {
            if (!c.getAutoCommit()) throw new IllegalStateException("Card registration requires its own transaction.");
            c.setAutoCommit(false);
            try {
                try (PreparedStatement ps = c.prepareStatement("SELECT tblMember4Id FROM tblReader4 WHERE tblMember4Id=? AND readerCode=? FOR UPDATE")) {
                    ps.setInt(1, reader.getId());
                    ps.setString(2, reader.getReaderCode());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new IllegalArgumentException("Reader profile does not match the signed-in account.");
                    }
                }
                if (hasReaderCard(reader)) throw new CardAlreadyExistsException();
                try (PreparedStatement ps = c.prepareStatement("UPDATE tblMember4 SET firstName=?, middleName=?, lastName=?, dateOfBirth=?, email=?, phoneNumber=? WHERE id=?")) {
                    ps.setString(1, reader.getFullName().getFirstName());
                    ps.setString(2, reader.getFullName().getMiddleName());
                    ps.setString(3, reader.getFullName().getLastName());
                    ps.setDate(4, new Date(reader.getDateOfBirth().getTime()));
                    ps.setString(5, reader.getEmail());
                    ps.setString(6, reader.getPhoneNumber());
                    ps.setInt(7, reader.getId());
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = c.prepareStatement("UPDATE tblReader4 SET tblReaderCategory4Id=? WHERE tblMember4Id=?")) {
                    ps.setInt(1, reader.getReaderCategory().getId());
                    ps.setInt(2, reader.getId());
                    ps.executeUpdate();
                }
                ReaderCard4 card;
                try (PreparedStatement ps = c.prepareStatement("INSERT INTO tblReaderCard4 (status,tblReader4Id) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, "ACTIVE");
                    ps.setInt(2, reader.getId());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) throw new SQLException("No generated reader card ID.");
                        card = new ReaderCard4(rs.getInt(1), "ACTIVE", reader);
                    }
                }
                c.commit();
                return card;
            } catch (SQLException | RuntimeException ex) {
                try { c.rollback(); } catch (SQLException rollback) { ex.addSuppressed(rollback); }
                throw ex;
            } finally { c.setAutoCommit(true); }
        } catch (SQLException ex) { throw new IllegalStateException("Unable to register reader card.", ex); }
    }

    public static class CardAlreadyExistsException extends IllegalStateException {
        public CardAlreadyExistsException() { super("Reader already has a card."); }
    }
}
