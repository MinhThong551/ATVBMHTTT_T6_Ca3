package nhom55.hcmuaf.controller.page.order;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@WebServlet("/page/order/huongdan")
public class HuongDanServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Đọc tệp HTML từ thư mục WEB-INF
        String filePath = "/WEB-INF/huongdan.html";
        ServletContext context = getServletContext();
        InputStream inputStream = context.getResourceAsStream(filePath);

        // Cấu hình loại nội dung trả về (HTML)
        response.setContentType("text/html");
        response.setContentLength(inputStream.available());

        // Gửi nội dung tệp HTML đến trình duyệt
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        inputStream.close();
    }
}
