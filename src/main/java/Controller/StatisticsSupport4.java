package Controller;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

final class StatisticsSupport4 {
    private StatisticsSupport4() { }

    static Date[] period(HttpServletRequest request) {
        String start = request.getParameter("startDate"), end = request.getParameter("endDate");
        if (start == null || end == null || start.isEmpty() || end.isEmpty())
            throw new IllegalArgumentException("Vui lòng chọn đầy đủ ngày bắt đầu và ngày kết thúc.");
        try {
            if (!start.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}") || !end.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) throw new DateTimeParseException("Invalid date", start, 0);
            LocalDate first = LocalDate.parse(start), last = LocalDate.parse(end);
            if (first.getYear()<1000 || last.getYear()>9999) throw new IllegalArgumentException("Ngày thống kê phải nằm trong khoảng năm 1000 đến 9999.");
            if (first.isAfter(last)) throw new IllegalArgumentException("Ngày bắt đầu không được muộn hơn ngày kết thúc.");
            return new Date[]{Date.valueOf(first),Date.valueOf(last)};
        } catch (DateTimeParseException ex) { throw new IllegalArgumentException("Ngày thống kê không hợp lệ."); }
    }

    static String code(HttpServletRequest request) {
        String code = request.getParameter("documentCode");
        if (code == null || code.trim().isEmpty() || code.length()>255) throw new IllegalArgumentException("Mã tài liệu không hợp lệ.");
        return code.trim();
    }
}
