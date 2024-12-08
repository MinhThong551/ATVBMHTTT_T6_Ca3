package nhom55.hcmuaf.controller.user;

import nhom55.hcmuaf.beans.Users;
import nhom55.hcmuaf.dao.UsersDao;
import nhom55.hcmuaf.dao.daoimpl.UsersDaoImpl;
import nhom55.hcmuaf.util.EmailUtil;
import nhom55.hcmuaf.util.MyUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

@WebServlet(name = "updateKeyPairUser", value = "/page/user/update-key-pair")
public class UpdateKeyPairUser extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward đến trang JSP hoặc xử lý logic khi truy cập bằng GET
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/user/update-key-pair.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = MyUtils.getLoginedUser(session);

        // Kiểm tra nếu người dùng đã đăng nhập
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        if ("generate-key".equals(action)) {
            handleGenerateKey(request, response, user);
        } else if ("save-key".equals(action)) {
            handleSaveKey(request, response, user);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ.");
        }
    }

    private void handleGenerateKey(HttpServletRequest request, HttpServletResponse response, Users user)
            throws ServletException, IOException {
        try {
            // Generate key pair
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair keyPair = keyGen.generateKeyPair();

            // Lấy public key
            String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

            // Lưu public key vào request để hiển thị lại trên form
            request.setAttribute("publicKey", publicKey);

            // Lưu public key và private key vào session (có thể sử dụng sau này nếu cần)
            request.getSession().setAttribute("generatedPublicKey", publicKey);
            String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
            request.getSession().setAttribute("generatedPrivateKey", privateKey);

            // Hiển thị thông báo kết quả
            request.setAttribute("result", "Khóa đã được sinh thành công.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("result", "Lỗi khi sinh khóa: " + e.getMessage());
        }

        // Forward lại đến trang JSP để hiển thị
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/user/update-key-pair.jsp");
        dispatcher.forward(request, response);
    }

    private void handleSaveKey(HttpServletRequest request, HttpServletResponse response, Users user)
            throws ServletException, IOException {
        String publicKey = (String) request.getSession().getAttribute("generatedPublicKey");
        String privateKey = (String) request.getSession().getAttribute("generatedPrivateKey");

        if (publicKey == null || privateKey == null) {
            request.setAttribute("result", "Vui lòng sinh khóa trước khi lưu.");
            doGet(request, response);
            return;
        }

        try {
            UsersDao userDao = new UsersDaoImpl();
            // Lưu public key vào database
            boolean isUpdated = userDao.updatePublicKey(user.getId(), publicKey);

            if (isUpdated) {
                // Gửi email chứa private key dưới dạng file đính kèm
                String subject = "Khóa riêng tư của bạn";
                String messageBody = "Đính kèm là tệp chứa khóa riêng tư của bạn.";
                EmailUtil.sendEmailWithAttachment(user.getEmail(), subject, messageBody, privateKey);

                request.setAttribute("result", "Lưu khóa công khai thành công. Khóa riêng tư đã được gửi đến email của bạn.");
            } else {
                request.setAttribute("result", "Lưu khóa thất bại. Vui lòng thử lại.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("result", "Đã xảy ra lỗi: " + e.getMessage());
        }

        doGet(request, response);
    }
}