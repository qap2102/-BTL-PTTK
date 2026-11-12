package DAO;

import Model.*;
import java.sql.*;
import java.util.*;

public class DocumentDAO4 extends DAO4 {
    public DocumentDAO4() { }
    public DocumentDAO4(Connection connection) { super(connection); }

    public Document4 getDocument(String documentCode) {
        String sql = "SELECT d.*, t.typeCode,t.typeName,t.description AS typeDescription, "
                + "c.categoryName,c.description AS categoryDescription FROM tblDocument4 d "
                + "JOIN tblDocumentType4 t ON t.id=d.tblDocumentType4id "
                + "JOIN tblDocumentCategory4 c ON c.id=d.tblDocumentCategory4id WHERE d.documentCode=?";
        try (PreparedStatement ps = requireConnection().prepareStatement(sql)) {
            ps.setString(1, documentCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Document4 d = new Document4();
                mapDocument(rs, d);
                d.setDocumentType(new DocumentType4(rs.getInt("tblDocumentType4id"),rs.getString("typeCode"),rs.getString("typeName"),rs.getString("typeDescription")));
                d.setDocumentCategory(new DocumentCategory4(rs.getInt("tblDocumentCategory4id"),rs.getString("categoryName"),rs.getString("categoryDescription")));
                loadAuthors(Collections.singletonList(d));
                return d;
            }
        } catch (SQLException ex) { throw new IllegalStateException("Unable to load document.", ex); }
    }

    static void mapDocument(ResultSet rs, Document4 document) throws SQLException {
        document.setId(rs.getInt("id"));
        document.setDocumentCode(rs.getString("documentCode"));
        document.setDocumentTitle(rs.getString("documentTitle"));
        document.setReferencePrice(rs.getBigDecimal("referencePrice"));
    }

    /** Batch authors separately so coauthors cannot multiply borrowing counts. */
    void loadAuthors(Collection<? extends Document4> documents) {
        List<? extends Document4> list = new ArrayList<>(documents);
        for (int offset=0; offset<list.size(); offset+=500) {
            List<? extends Document4> batch = list.subList(offset, Math.min(offset+500,list.size()));
            Map<Integer,Document4> byId = new HashMap<>();
            Map<Integer,List<DocumentAuthor4>> authors = new HashMap<>();
            for (Document4 d : batch) byId.put(d.getId(),d);
            String placeholders = String.join(",",Collections.nCopies(batch.size(),"?"));
            String sql = "SELECT da.id,da.authorRole,da.tblDocument4id,a.id AS authorId,a.authorName,a.email "
                    + "FROM tblDocumentAuthor4 da JOIN tblAuthor4 a ON a.id=da.tblAuthor4id "
                    + "WHERE da.tblDocument4id IN (" + placeholders + ") ORDER BY da.id";
            try (PreparedStatement ps = requireConnection().prepareStatement(sql)) {
                for (int i=0;i<batch.size();i++) ps.setInt(i+1,batch.get(i).getId());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("tblDocument4id");
                        // The SQL design stores authorName as one string; the entity uses FullName4.
                        Author4 author = new Author4(rs.getInt("authorId"),new FullName4(0,rs.getString("authorName"),"",""),rs.getString("email"));
                        authors.computeIfAbsent(id,key -> new ArrayList<>()).add(new DocumentAuthor4(rs.getInt("id"),rs.getString("authorRole"),author,byId.get(id)));
                    }
                }
                for (Document4 d : batch) d.setListDocumentAuthors(authors.getOrDefault(d.getId(),Collections.emptyList()).toArray(new DocumentAuthor4[0]));
            } catch (SQLException ex) { throw new IllegalStateException("Unable to load document authors.", ex); }
        }
    }
}