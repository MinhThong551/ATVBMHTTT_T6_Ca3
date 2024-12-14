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
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
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
        } else if ("delete-key".equals(action)) {
            handleDeleteKey(request, response, user);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ.");
        }
    }

    private void handleGenerateKey(HttpServletRequest request, HttpServletResponse response, Users user)
            throws ServletException, IOException {
        try {
            String publicKey = request.getParameter("public-key");

            if (publicKey != null && !publicKey.isEmpty()) {
                // Nếu textarea có dữ liệu, kiểm tra xem đó có phải là public key hợp lệ không
                try {
                    // Giải mã public key từ Base64
                    byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);

                    // Sử dụng KeyFactory để tạo PublicKey từ dữ liệu Base64
                    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
                    PublicKey pubKey = keyFactory.generatePublic(keySpec);

                    // Tạo private key tương ứng với public key
                    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
                    keyGen.initialize(2048);
                    KeyPair keyPair = keyGen.generateKeyPair();

                    // Lưu public key vào request để hiển thị lại trên form
                    request.setAttribute("publicKey", publicKey);

                    // Lưu public key và private key vào session (có thể sử dụng sau này nếu cần)
                    request.getSession().setAttribute("generatedPublicKey", publicKey);
                    String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
                    request.getSession().setAttribute("generatedPrivateKey", privateKey);

                    request.setAttribute("result", "Khóa đã được sinh thành công.");
                } catch (Exception e) {
                    request.setAttribute("result", "Dữ liệu không phải là public key hợp lệ.");
                }
            } else {
                // Nếu textarea rỗng, tạo một cặp khóa mới
                KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
                keyGen.initialize(2048);
                KeyPair keyPair = keyGen.generateKeyPair();

                // Lấy public key
                publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

                // Lưu public key vào request để hiển thị lại trên form
                request.setAttribute("publicKey", publicKey);

                // Lưu public key và private key vào session (có thể sử dụng sau này nếu cần)
                request.getSession().setAttribute("generatedPublicKey", publicKey);
                String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
                request.getSession().setAttribute("generatedPrivateKey", privateKey);

                // Hiển thị thông báo kết quả
                request.setAttribute("result", "Khóa đã được sinh thành công.");
            }

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
        // Lấy giá trị khóa công khai từ textarea (public-key)
        String publicKey = request.getParameter("public-key");
        String privateKey = (String) request.getSession().getAttribute("generatedPrivateKey");

        if (publicKey == null || publicKey.isEmpty() || privateKey == null) {
            request.setAttribute("result", "Vui lòng nhập khóa công khai và sinh khóa trước khi lưu.");
            doGet(request, response);
            return;
        }

        try {
            // Kiểm tra xem publicKey có phải là một Base64 hợp lệ không
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);

            // Nếu publicKey hợp lệ, tiến hành lưu publicKey vào cơ sở dữ liệu
            UsersDao userDao = new UsersDaoImpl();
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
        } catch (IllegalArgumentException e) {
            request.setAttribute("result", "Khóa công khai không hợp lệ. Vui lòng kiểm tra lại.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("result", "Đã xảy ra lỗi: " + e.getMessage());
        }

        doGet(request, response);
    }
    private void handleDeleteKey(HttpServletRequest request, HttpServletResponse response, Users user)
            throws ServletException, IOException {
        // Xóa khóa công khai trong cơ sở dữ liệu
        try {
            UsersDao userDao = new UsersDaoImpl();
            boolean isDeleted = userDao.deletePublicKey(user.getId());

            if (isDeleted) {
                request.setAttribute("result", "Khóa công khai đã được xóa.");
            } else {
                request.setAttribute("result", "Không thể xóa khóa công khai. Vui lòng thử lại.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("result", "Đã xảy ra lỗi khi xóa khóa: " + e.getMessage());
        }

        doGet(request, response);
    }
}
