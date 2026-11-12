import DAO.*;
import Model.*;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.*;
import org.junit.*;
import static org.junit.Assert.*;

public class Module2FlowTest {
    private static final String DB="jdbc:h2:mem:module2;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1";
    private static Tomcat tomcat;
    private static String base;
    private static final Map<String,String> originalProperties=new HashMap<>();
    private HttpClient client;

    @BeforeClass
    public static void start() throws Exception {
        for(String key:new String[]{"libman.db.url","libman.db.username","libman.db.password"}) originalProperties.put(key,System.getProperty(key));
        System.setProperty("libman.db.url",DB); System.setProperty("libman.db.username","sa"); System.setProperty("libman.db.password","");
        tomcat=new Tomcat(); tomcat.setPort(0); tomcat.getConnector();
        tomcat.setBaseDir(Files.createTempDirectory(Path.of("target"),"tomcat-module2-").toAbsolutePath().toString());
        Context context=tomcat.addWebapp("/BTL_PTTK",Path.of("src/main/webapp").toAbsolutePath().toString());
        context.setParentClassLoader(Module2FlowTest.class.getClassLoader());
        StandardRoot resources=new StandardRoot(context);
        resources.addPreResources(new DirResourceSet(resources,"/WEB-INF/classes",Path.of("target/classes").toAbsolutePath().toString(),"/"));
        context.setResources(resources); tomcat.start(); assertTrue(context.getState().isAvailable());
        base="http://localhost:"+tomcat.getConnector().getLocalPort()+"/BTL_PTTK";
    }

    @AfterClass
    public static void stop() throws Exception {
        try { if(tomcat!=null) { tomcat.stop(); tomcat.destroy(); } }
        finally { originalProperties.forEach((key,value)->{if(value==null) System.clearProperty(key);else System.setProperty(key,value);}); }
    }

    @Before
    public void reset() throws Exception {
        client=HttpClient.newBuilder().cookieHandler(new CookieManager(null,CookiePolicy.ACCEPT_ALL)).followRedirects(HttpClient.Redirect.NEVER).build();
        try(Connection c=connection();Statement s=c.createStatement()) {
            s.execute("DROP ALL OBJECTS");
            s.execute("CREATE TABLE tblMember4(id INT PRIMARY KEY,userName VARCHAR(255),password VARCHAR(255),firstName VARCHAR(255),middleName VARCHAR(255),lastName VARCHAR(255),dateOfBirth DATE,email VARCHAR(255),phoneNumber VARCHAR(255))");
            s.execute("INSERT INTO tblMember4 VALUES(1,'reader','pass','An','Văn','Nguyễn','2000-01-01','reader@example.com','0123456789'),(2,'manager','pass','Bình','Văn','Trần','1980-01-01','manager@example.com','0987654321'),(3,'librarian','pass','Chi','Thị','Lê','1990-01-01','staff@example.com','0987654322')");
            s.execute("CREATE TABLE tblReaderCategory4(id INT PRIMARY KEY,readerType VARCHAR(255),maxBorrowQuantity INT,borrowPeriod INT)");
            s.execute("INSERT INTO tblReaderCategory4 VALUES(1,'Sinh viên',8,150)");
            s.execute("CREATE TABLE tblReader4(tblMember4id INT PRIMARY KEY,readerCode VARCHAR(255),tblReaderCategory4id INT)");
            s.execute("INSERT INTO tblReader4 VALUES(1,'R001',1)");
            s.execute("CREATE TABLE tblStaff4(tblMember4id INT PRIMARY KEY,staffCode VARCHAR(255),position VARCHAR(255))");
            s.execute("INSERT INTO tblStaff4 VALUES(2,'M002','Manager'),(3,'L003','Librarian')");
            s.execute("CREATE TABLE tblDocumentType4(id INT PRIMARY KEY,typeCode VARCHAR(255),typeName VARCHAR(255),description VARCHAR(255))");
            s.execute("CREATE TABLE tblDocumentCategory4(id INT PRIMARY KEY,categoryName VARCHAR(255),description VARCHAR(255))");
            s.execute("INSERT INTO tblDocumentType4 VALUES(1,'BOOK','Sách','')");
            s.execute("INSERT INTO tblDocumentCategory4 VALUES(1,'CNTT','')");
            s.execute("CREATE TABLE tblDocument4(id INT PRIMARY KEY,documentCode VARCHAR(255),documentTitle VARCHAR(255),referencePrice DECIMAL(12,2),tblDocumentType4id INT,tblDocumentCategory4id INT)");
            s.execute("INSERT INTO tblDocument4 VALUES(1,'D001','Lập trình Java',100000,1,1),(2,'D002','Cơ sở dữ liệu',90000,1,1),(3,'D003','<script>alert(1)</script>',80000,1,1),(4,'D004','Chưa được mượn',70000,1,1)");
            s.execute("CREATE TABLE tblAuthor4(id INT PRIMARY KEY,authorName VARCHAR(255),email VARCHAR(255))");
            s.execute("INSERT INTO tblAuthor4 VALUES(1,'Tác giả A','a@example.com'),(2,'Tác giả B','b@example.com')");
            s.execute("CREATE TABLE tblDocumentAuthor4(id INT PRIMARY KEY,authorRole VARCHAR(255),tblDocument4id INT,tblAuthor4id INT)");
            s.execute("INSERT INTO tblDocumentAuthor4 VALUES(1,'Chủ biên',1,1),(2,'Đồng tác giả',1,2),(3,'Tác giả',2,1)");
            s.execute("CREATE TABLE tblDocumentCopy4(id INT PRIMARY KEY,copyCode VARCHAR(255),physicalCondition VARCHAR(255),tblDocument4id INT)");
            s.execute("INSERT INTO tblDocumentCopy4 VALUES(1,'CP001','Tốt',1),(2,'CP002','Tốt',1),(3,'CP003','Tốt',2),(4,'CP004','Tốt',3)");
            s.execute("CREATE TABLE tblBorrowingReceipt4(id INT PRIMARY KEY,receiptCode VARCHAR(255),borrowDate DATE,dueDate DATE,status VARCHAR(255),tblReader4id INT,tblStaff4id INT)");
            s.execute("INSERT INTO tblBorrowingReceipt4 VALUES(1,'PM001','2026-10-11','2026-11-11','PARTIAL_RETURN',1,3),(2,'PM002','2026-10-12','2026-11-12','BORROWING',1,3),(3,'PM003','2026-10-10','2026-11-10','BORROWING',1,3),(4,'PM004','2026-10-13','2026-11-13','BORROWING',1,3),(5,'PM005','2026-10-12','2026-11-12','BORROWING',1,3)");
            s.execute("CREATE TABLE tblBorrowingReceiptDetail4(id INT PRIMARY KEY,borrowingCondition VARCHAR(255),tblBorrowingReceipt4id INT,tblDocumentCopy4id INT)");
            s.execute("INSERT INTO tblBorrowingReceiptDetail4 VALUES(1,'Nguyên vẹn',1,1),(2,'Cũ',1,2),(3,'Nguyên vẹn',1,3),(4,'Nguyên vẹn',2,1),(5,'Nguyên vẹn',2,4),(6,'Nguyên vẹn',3,3),(7,'Nguyên vẹn',4,2),(8,'Nguyên vẹn',5,3)");
            s.execute("CREATE TABLE tblReturnReceipt4(id INT PRIMARY KEY,returnDate DATE)");
            s.execute("CREATE TABLE tblReturnReceiptDetail4(id INT PRIMARY KEY,tblReturnReceipt4id INT,tblBorrowingReceiptDetail4id INT)");
            s.execute("INSERT INTO tblReturnReceipt4 VALUES(1,'2026-10-11'),(2,'2026-10-20')");
            s.execute("INSERT INTO tblReturnReceiptDetail4 VALUES(1,1,1),(2,2,3)");
        }
    }

    @Test
    public void statisticsCountCopiesOnceAndIncludeBothBoundaries() throws Exception {
        try(Connection c=connection()) {
            DocumentBorrowingStatDAO4 dao=new DocumentBorrowingStatDAO4(c);
            List<DocumentBorrowingStat4> stats=dao.getDocumentStatistics(day("2026-10-11"),day("2026-10-12"));
            assertEquals(3,stats.size());
            assertEquals("D001",stats.get(0).getDocumentCode()); assertEquals(3,stats.get(0).getTotalBorrowCount());
            assertEquals(2,stats.get(0).getListDocumentAuthors().length);
            assertEquals(2,stats.get(1).getTotalBorrowCount()); assertEquals(1,stats.get(2).getTotalBorrowCount());
            List<DocumentBorrowingStat4> sameDay=dao.getDocumentStatistics(day("2026-10-12"),day("2026-10-12"));
            assertEquals(Arrays.asList("D001","D002","D003"),sameDay.stream().map(Document4::getDocumentCode).collect(Collectors.toList()));
            assertTrue(sameDay.stream().allMatch(row->row.getTotalBorrowCount()==1));
            assertTrue(dao.getDocumentStatistics(day("2030-01-01"),day("2030-01-02")).isEmpty());
        }
    }

    @Test
    public void fullManagerNavigationKeepsPeriodAndShowsPartialReturns() throws Exception {
        login("manager");
        assertTrue(get("/Manager4/MainMenuManagerUI4.jsp").body().contains("StatisticsMenuUI4.jsp"));
        assertEquals(200,get("/Manager4/StatisticsMenuUI4.jsp").statusCode());
        HttpResponse<String> stat=get("/DocumentBorrowingStatServlet4?startDate=2026-10-11&endDate=2026-10-12");
        assertEquals(stat.body(),200,stat.statusCode());
        assertTrue(stat.body().contains("<strong>3</strong>")); assertTrue(stat.body().contains("<strong>6</strong>"));
        assertTrue(stat.body().contains("Tác giả A, Tác giả B"));
        assertTrue(stat.body().contains("&lt;script&gt;alert(1)&lt;/script&gt;")); assertFalse(stat.body().contains("<script>alert(1)</script>"));
        String records=get("/DocumentBorrowingReceiptServlet4?documentCode=D001&startDate=2026-10-11&endDate=2026-10-12").body();
        assertTrue(records.contains("PM001")); assertTrue(records.contains("PM002")); assertFalse(records.contains("PM004"));
        assertTrue(records.contains("id=1&amp;documentCode=D001&amp;startDate=2026-10-11&amp;endDate=2026-10-12"));
        HttpResponse<String> detail=get("/BorrowingReceiptDetailServlet4?id=1&documentCode=D001&startDate=2026-10-11&endDate=2026-10-12");
        assertEquals(detail.body(),200,detail.statusCode());
        assertTrue(detail.body().contains("CP001")); assertTrue(detail.body().contains("CP002")); assertTrue(detail.body().contains("CP003"));
        assertTrue(detail.body().contains("Đã trả")); assertTrue(detail.body().contains("Chưa trả"));
        assertFalse(records.contains("Ngày trả thực tế"));
        assertFalse(detail.body().contains("Ngày trả thực tế"));
        assertFalse(detail.body().contains("20/10/2026"));
        assertTrue(detail.body().contains("11/10/2026"));
        assertTrue(detail.body().contains("11/11/2026"));
        assertTrue(detail.body().contains("Nguyễn Văn An")); assertTrue(detail.body().contains("Lê Thị Chi"));
        assertTrue(detail.body().contains("DocumentBorrowingReceiptServlet4?documentCode=D001&amp;startDate=2026-10-11&amp;endDate=2026-10-12"));
        try(Connection c=connection()) {
            BorrowingReceiptDetailDAO4 dao=new BorrowingReceiptDetailDAO4(c);
            assertEquals(3,dao.getListBorrowing(1,day("2026-10-11"),day("2026-10-12")).size());
            assertEquals(3,dao.getDetailsByReceiptId(1).size());
            ReturnReceiptDetailDAO4 returns=new ReturnReceiptDetailDAO4(c);
            assertTrue(returns.isReturned(1)); assertFalse(returns.isReturned(2));
            assertTrue(returns.isReturned(3));
        }
    }

    @Test
    public void invalidDatesMissingRecordsAndEmptyResultsAreHandled() throws Exception {
        login("manager");
        assertEquals(200,get("/DocumentBorrowingStatServlet4").statusCode());
        assertEquals(400,get("/DocumentBorrowingStatServlet4?startDate=2026-10-12&endDate=2026-10-11").statusCode());
        assertEquals(400,get("/DocumentBorrowingStatServlet4?startDate=2026-02-30&endDate=2026-10-11").statusCode());
        assertEquals(400,get("/DocumentBorrowingStatServlet4?startDate=&endDate=").statusCode());
        String empty=get("/DocumentBorrowingStatServlet4?startDate=2030-01-01&endDate=2030-01-02").body();
        assertTrue(empty.contains("Không có dữ liệu thống kê")); assertTrue(empty.contains("<strong>0</strong>"));
        assertEquals(400,get("/DocumentBorrowingReceiptServlet4?documentCode=D001").statusCode());
        assertEquals(404,get("/DocumentBorrowingReceiptServlet4?documentCode=missing&startDate=2026-10-11&endDate=2026-10-12").statusCode());
        assertEquals(400,get("/BorrowingReceiptDetailServlet4?id=abc").statusCode());
        assertEquals(404,get("/BorrowingReceiptDetailServlet4?id=999999").statusCode());
        assertEquals(200,get("/BorrowingReceiptDetailServlet4?id=1").statusCode());
        assertEquals(302,get("/Manager4/BorrowingReceiptDetailUI4.jsp").statusCode());
    }

    @Test
    public void onlyManagersCanReadReportsAndReceiptDetails() throws Exception {
        String[] paths={"/Manager4/StatisticsMenuUI4.jsp","/DocumentBorrowingStatServlet4","/DocumentBorrowingReceiptServlet4?documentCode=D001&startDate=2026-10-11&endDate=2026-10-12","/BorrowingReceiptDetailServlet4?id=1","/Manager4/BorrowingReceiptDetailUI4.jsp"};
        for(String path:paths) assertEquals(302,get(path).statusCode());
        login("reader"); for(String path:paths) assertEquals(403,get(path).statusCode());
        client=HttpClient.newBuilder().cookieHandler(new CookieManager(null,CookiePolicy.ACCEPT_ALL)).followRedirects(HttpClient.Redirect.NEVER).build();
        login("librarian"); for(String path:paths) assertEquals(403,get(path).statusCode());
    }

    private static java.sql.Date day(String date) { return java.sql.Date.valueOf(date); }
    private static Connection connection() throws SQLException { return DriverManager.getConnection(DB,"sa",""); }
    private HttpResponse<String> get(String path) throws Exception { return client.send(HttpRequest.newBuilder(URI.create(base+path)).GET().build(),HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)); }
    private void login(String user) throws Exception {
        Matcher token=Pattern.compile("name=\"csrfToken\" value=\"([^\"]+)\"").matcher(get("/login").body()); assertTrue(token.find());
        String form="userName="+user+"&password=pass&csrfToken="+token.group(1);
        HttpResponse<String> response=client.send(HttpRequest.newBuilder(URI.create(base+"/login")).header("Content-Type","application/x-www-form-urlencoded").POST(HttpRequest.BodyPublishers.ofString(form)).build(),HttpResponse.BodyHandlers.ofString());
        assertEquals(302,response.statusCode());
    }
}
