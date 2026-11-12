import DAO.*;
import Model.*;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;
import java.util.stream.Collectors;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.*;
import org.junit.*;
import static org.junit.Assert.*;

/** Runs the actual Servlet/JSP flow against an isolated, in-memory SQL fixture. */
public class Module1FlowTest {
    private static final String DB = "jdbc:h2:mem:module1;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1";
    private static Tomcat tomcat;
    private static String base;
    private static final Map<String, String> originalProperties = new HashMap<>();
    private HttpClient client;

    @BeforeClass
    public static void start() throws Exception {
        for (String key : new String[]{"libman.db.url", "libman.db.username", "libman.db.password"})
            originalProperties.put(key, System.getProperty(key));
        System.setProperty("libman.db.url", DB);
        System.setProperty("libman.db.username", "sa");
        System.setProperty("libman.db.password", "");
        tomcat = new Tomcat();
        tomcat.setBaseDir(Files.createTempDirectory(Path.of("target"), "tomcat-module1-").toAbsolutePath().toString());
        tomcat.setPort(0);
        tomcat.getConnector();
        Context context = tomcat.addWebapp("/BTL_PTTK", Path.of("src/main/webapp").toAbsolutePath().toString());
        context.setParentClassLoader(Module1FlowTest.class.getClassLoader());
        StandardRoot resources = new StandardRoot(context);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", Path.of("target/classes").toAbsolutePath().toString(), "/"));
        context.setResources(resources);
        tomcat.start();
        assertTrue("Web application must start", context.getState().isAvailable());
        base = "http://localhost:" + tomcat.getConnector().getLocalPort() + "/BTL_PTTK";
    }

    @AfterClass
    public static void stop() throws Exception {
        try { if (tomcat != null) { tomcat.stop(); tomcat.destroy(); } }
        finally {
            originalProperties.forEach((key, value) -> {
                if (value == null) System.clearProperty(key); else System.setProperty(key, value);
            });
        }
    }

    @Before
    public void reset() throws Exception {
        client = HttpClient.newBuilder().cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL))
                .followRedirects(HttpClient.Redirect.NEVER).build();
        try (Connection c = connection(); Statement s = c.createStatement()) {
            s.execute("DROP ALL OBJECTS");
            s.execute("CREATE TABLE tblMember4(id INT PRIMARY KEY, userName VARCHAR(255) UNIQUE, password VARCHAR(255), firstName VARCHAR(255), middleName VARCHAR(255), lastName VARCHAR(255), dateOfBirth VARCHAR(255), email VARCHAR(255), phoneNumber VARCHAR(255))");
            s.execute("CREATE TABLE tblReaderCategory4(id INT PRIMARY KEY, readerType VARCHAR(255), maxBorrowQuantity INT, borrowPeriod INT)");
            s.execute("CREATE TABLE tblReader4(tblMember4Id INT PRIMARY KEY REFERENCES tblMember4(id), readerCode VARCHAR(255) UNIQUE, tblReaderCategory4Id INT REFERENCES tblReaderCategory4(id))");
            s.execute("CREATE TABLE tblStaff4(tblMember4Id INT PRIMARY KEY REFERENCES tblMember4(id), staffCode VARCHAR(255), position VARCHAR(255))");
            s.execute("CREATE TABLE tblReaderCard4(id INT AUTO_INCREMENT PRIMARY KEY, status VARCHAR(255), tblReader4Id INT REFERENCES tblReader4(tblMember4Id))");
            s.execute("INSERT INTO tblReaderCategory4 VALUES(1,'Sinh viên',8,150),(2,'Giảng viên',7,60)");
            for (int i = 1; i <= 5; i++) s.execute("INSERT INTO tblMember4 VALUES(" + i + ",'user" + i + "','Secret123','Anh','Quang','Phạm','2005-10-03','reader@example.com','0123456789')");
            s.execute("INSERT INTO tblReader4 VALUES(1,'R001',1),(2,'R002',1),(5,'R005',1)");
            s.execute("INSERT INTO tblStaff4 VALUES(3,'S003','Nhân viên quản lý'),(4,'S004','Nhân viên thư viện')");
            s.execute("INSERT INTO tblReaderCard4(status,tblReader4Id) VALUES('Active',2)");
        }
    }

    @Test
    public void entryPointsAndSessionAccess() throws Exception {
        assertRedirect(get("/"), "/login");
        assertEquals(200, get("/login").statusCode());
        assertEquals(200, get("/Member4/LoginUI4.jsp").statusCode());
        assertRedirect(get("/Reader4/MainMenuReaderUI4.jsp"), "/login");
        assertRedirect(get("/RegistrationServlet4"), "/login");
        assertRedirect(get("/Reader4/RegistrationNoticeUI4.jsp"), "/login");
    }

    @Test
    public void authenticationValidatesCredentialsAndRoles() throws Exception {
        String token = csrf(get("/login").body());
        HttpResponse<String> wrong = post("/login", Map.of("userName", "user1", "password", "secret123", "csrfToken", token));
        assertTrue(wrong.body().contains("Tên đăng nhập hoặc mật khẩu không đúng"));
        HttpResponse<String> injection = post("/login", Map.of("userName", "' OR 1=1 --", "password", "Secret123", "csrfToken", token));
        assertTrue(injection.body().contains("Tên đăng nhập hoặc mật khẩu không đúng"));
        assertRedirect(login("user1"), "/Reader4/MainMenuReaderUI4.jsp");
        assertEquals(403, get("/Manager4/MainMenuManagerUI4.jsp").statusCode());
        assertTrue(get("/Reader4/MainMenuReaderUI4.jsp").body().contains("Phạm Quang Anh"));
        assertRedirect(post("/logout", Map.of("csrfToken", csrf(get("/Reader4/MainMenuReaderUI4.jsp").body()))), "/login");
        assertRedirect(get("/RegistrationServlet4"), "/login");
        assertRedirect(login("user3"), "/Manager4/MainMenuManagerUI4.jsp");
        assertEquals(200, get("/Manager4/MainMenuManagerUI4.jsp").statusCode());
        assertEquals(403, get("/RegistrationServlet4").statusCode());
        post("/logout", Map.of("csrfToken", csrf(get("/Manager4/MainMenuManagerUI4.jsp").body())));
        assertRedirect(login("user4"), "/Librian4/MainMenuLibrarianUI4.jsp");
        assertEquals(200, get("/Librian4/MainMenuLibrarianUI4.jsp").statusCode());
    }

    @Test
    public void registrationSavesProfileAndCardAndPreventsDuplicate() throws Exception {
        login("user1");
        HttpResponse<String> page = get("/RegistrationServlet4");
        assertEquals(200, page.statusCode());
        assertTrue(page.body().contains("Sinh viên"));
        assertTrue(page.body().contains("Giảng viên"));
        Map<String, String> form = registration(csrf(page.body()));
        assertRedirect(post("/RegistrationServlet4", form), "/RegistrationServlet4?view=success");
        String notice = get("/RegistrationServlet4?view=success").body();
        assertTrue(notice.contains("Đăng ký thẻ bạn đọc thành công"));
        assertTrue(notice.contains("Nguyễn Văn Bình"));
        assertRedirect(get("/RegistrationServlet4?view=success"), "/Reader4/MainMenuReaderUI4.jsp");
        assertTrue(get("/RegistrationServlet4").body().contains("Bạn đã có thẻ bạn đọc"));
        assertTrue(post("/RegistrationServlet4", form).body().contains("Đã tồn tại thẻ"));
        try (Connection c = connection(); Statement s = c.createStatement(); ResultSet r = s.executeQuery("SELECT m.firstName,m.middleName,m.lastName,r.tblReaderCategory4Id,(SELECT COUNT(*) FROM tblReaderCard4 WHERE tblReader4Id=1) AS cards FROM tblMember4 m JOIN tblReader4 r ON r.tblMember4Id=m.id WHERE m.id=1")) {
            assertTrue(r.next());
            assertEquals("Bình", r.getString(1));
            assertEquals("Văn", r.getString(2));
            assertEquals("Nguyễn", r.getString(3));
            assertEquals(2, r.getInt(4));
            assertEquals(1, r.getInt(5));
        }
    }

    @Test
    public void registrationRejectsInvalidInputAndOtherAccounts() throws Exception {
        login("user1");
        Map<String, String> form = registration(csrf(get("/RegistrationServlet4").body()));
        form.put("email", "bad-email");
        form.put("dateOfBirth", "2999-01-01");
        form.put("phoneNumber", "abc");
        String body = post("/RegistrationServlet4", form).body();
        assertTrue(body.contains("Email không hợp lệ"));
        assertTrue(body.contains("Ngày sinh không được ở tương lai"));
        assertTrue(body.contains("Số điện thoại cần"));
        form = registration(form.get("csrfToken"));
        form.put("readerCode", "R005");
        assertTrue(post("/RegistrationServlet4", form).body().contains("Mã bạn đọc phải thuộc tài khoản"));
        form.put("readerCode", "R001");
        form.put("readerCategoryId", "999");
        assertTrue(post("/RegistrationServlet4", form).body().contains("Vui lòng chọn loại bạn đọc hợp lệ"));
        form.put("csrfToken", "invalid");
        assertEquals(403, post("/RegistrationServlet4", form).statusCode());
        try (Connection c = connection(); Statement s = c.createStatement(); ResultSet r = s.executeQuery("SELECT COUNT(*) FROM tblReaderCard4 WHERE tblReader4Id IN(1,5)")) {
            r.next(); assertEquals(0, r.getInt(1));
        }
    }

    @Test
    public void failedCardInsertRollsBackProfileAndCategory() throws Exception {
        try (Connection c = connection()) {
            Reader4 reader = new ReaderDAO4(c).getReader("R001");
            reader.setEmail("changed@example.com");
            reader.setReaderCategory(new ReaderCategoryDAO4(c).getReaderCategory(2));
            try (Statement s = c.createStatement()) {
                s.execute("ALTER TABLE tblReaderCard4 ADD CONSTRAINT reject_test CHECK(tblReader4Id<>1)");
            }
            try {
                new ReaderCardDAO4(c).createReaderCard(reader);
                fail("Insert should fail");
            } catch (IllegalStateException expected) { }
            assertTrue(c.getAutoCommit());
            Reader4 unchanged = new ReaderDAO4(c).getReader("R001");
            assertEquals("reader@example.com", unchanged.getEmail());
            assertEquals(1, unchanged.getReaderCategory().getId());
            assertFalse(new ReaderCardDAO4(c).hasReaderCard(unchanged));
            assertFalse("Borrowed connections remain usable", c.isClosed());
        }
    }

    private static Connection connection() throws SQLException { return DriverManager.getConnection(DB, "sa", ""); }

    @Test
    public void simultaneousRegistrationCreatesOnlyOneCardWithoutUniqueConstraint() throws Exception {
        ExecutorService workers = Executors.newFixedThreadPool(2);
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch submit = new CountDownLatch(1);
        Callable<Boolean> register = () -> {
            try (Connection c = connection()) {
                Reader4 reader = new ReaderDAO4(c).getReader("R001");
                ready.countDown();
                if (!submit.await(5, TimeUnit.SECONDS)) throw new AssertionError("Timed out waiting for submit");
                try { new ReaderCardDAO4(c).createReaderCard(reader); return true; }
                catch (ReaderCardDAO4.CardAlreadyExistsException expected) { return false; }
            }
        };
        try {
            Future<Boolean> first = workers.submit(register);
            Future<Boolean> second = workers.submit(register);
            assertTrue(ready.await(5, TimeUnit.SECONDS));
            submit.countDown();
            assertNotEquals(first.get(5, TimeUnit.SECONDS), second.get(5, TimeUnit.SECONDS));
            try (Connection c = connection(); Statement s = c.createStatement(); ResultSet r = s.executeQuery("SELECT COUNT(*) FROM tblReaderCard4 WHERE tblReader4Id=1")) {
                r.next(); assertEquals(1, r.getInt(1));
            }
        } finally { workers.shutdownNow(); }
    }
    private HttpResponse<String> login(String user) throws Exception {
        return post("/login", Map.of("userName", user, "password", "Secret123", "csrfToken", csrf(get("/login").body())));
    }
    private Map<String, String> registration(String token) {
        Map<String, String> form = new HashMap<>();
        form.put("csrfToken", token); form.put("fullName", "Nguyễn Văn Bình");
        form.put("dateOfBirth", "2004-01-15"); form.put("email", "binh@example.com");
        form.put("phoneNumber", "0987654321"); form.put("readerCode", "R001"); form.put("readerCategoryId", "2");
        return form;
    }
    private HttpResponse<String> get(String path) throws Exception {
        return client.send(HttpRequest.newBuilder(URI.create(base + path)).GET().build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }
    private HttpResponse<String> post(String path, Map<String, String> values) throws Exception {
        String body = values.entrySet().stream().map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8)).collect(Collectors.joining("&"));
        return client.send(HttpRequest.newBuilder(URI.create(base + path)).header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(body)).build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }
    private String csrf(String html) {
        Matcher m = Pattern.compile("name=\"csrfToken\" value=\"([^\"]+)\"").matcher(html);
        assertTrue("Expected a CSRF token in rendered JSP: " + html, m.find());
        return m.group(1);
    }
    private void assertRedirect(HttpResponse<String> response, String path) {
        assertEquals(response.body(), 302, response.statusCode());
        assertTrue(response.headers().firstValue("Location").orElse("").endsWith("/BTL_PTTK" + path));
    }
}
