package Controller;

import Model.*;
import jakarta.servlet.http.*;
import java.util.UUID;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.Arrays;

public final class WebSupport4 {
    private WebSupport4() { }

    public static String html(Object value) {
        if (value == null) return "";
        return value.toString().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }

    public static String fullName(Member4 member) {
        if (member == null || member.getFullName() == null) return "";
        FullName4 n = member.getFullName();
        return (text(n.getLastName()) + " " + text(n.getMiddleName()) + " " + text(n.getFirstName())).trim().replaceAll("\\s+", " ");
    }

    private static String text(String value) { return value == null ? "" : value; }

    public static String authors(Document4 document) {
        return Arrays.stream(document.getListDocumentAuthors()).map(link -> {
            FullName4 n = link.getAuthor().getAuthorName();
            return (text(n.getLastName()) + " " + text(n.getMiddleName()) + " " + text(n.getFirstName())).trim().replaceAll("\\s+", " ");
        }).distinct().collect(Collectors.joining(", "));
    }

    public static String date(Date date) { return date == null ? "—" : new SimpleDateFormat("dd/MM/yyyy").format(date); }

    public static String url(HttpServletRequest request, String path, String... parameters) {
        StringBuilder url = new StringBuilder(request.getContextPath()).append(path);
        for (int i=0;i<parameters.length;i+=2) {
            url.append(i == 0 ? '?' : '&').append(URLEncoder.encode(parameters[i],StandardCharsets.UTF_8))
                    .append('=').append(URLEncoder.encode(parameters[i+1],StandardCharsets.UTF_8));
        }
        return url.toString();
    }

    public static String csrf(HttpSession session) {
        if (session.getAttribute("csrfToken") == null) session.setAttribute("csrfToken", UUID.randomUUID().toString());
        return (String) session.getAttribute("csrfToken");
    }

    public static boolean validCsrf(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("csrfToken") != null
                && session.getAttribute("csrfToken").equals(request.getParameter("csrfToken"));
    }

    public static String home(Member4 member) {
        if (member instanceof Reader4) return "/Reader4/MainMenuReaderUI4.jsp";
        if (member instanceof Manager4) return "/Manager4/MainMenuManagerUI4.jsp";
        if (member instanceof Librarian4) return "/Librarian4/MainMenuLibrarianUI4.jsp";
        throw new IllegalArgumentException("Unsupported member role.");
    }
}
