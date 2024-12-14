package nhom55.hcmuaf.controller.page.bill;

import nhom55.hcmuaf.beans.BillDetails;
import nhom55.hcmuaf.dao.BillDao;
import nhom55.hcmuaf.dao.daoimpl.BillDaoImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.List;

@WebServlet(name = "billDetail", value = "/page/bill/detail")
public class billDetail extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy idBill từ request
        int idBill = Integer.parseInt(request.getParameter("idBills"));

        // Lấy danh sách chi tiết hóa đơn từ database
        BillDao orderDao = new BillDaoImpl();
        List<BillDetails> list = orderDao.getListProductInABill(idBill);

        // Lưu danh sách sản phẩm vào request để hiển thị trong JSP
        request.setAttribute("list", list);

        // Lưu idBill vào session để sử dụng trong doPost
        HttpSession session = request.getSession();
        session.setAttribute("idBill", idBill);

        // Chuyển tiếp đến trang chi tiết hóa đơn
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/user/chi-tiet-hoa-don.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy idBill từ session
        HttpSession session = request.getSession();
        Integer idBill = (Integer) session.getAttribute("idBill");

        // Kiểm tra xem idBill có tồn tại không
        if (idBill == null) {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Không có idBill!\"}");
            return;
        }

        // Lấy privateKey và hash từ request
        String privateKey = request.getParameter("privateKey");
        String hash = request.getParameter("hash");

        // Kiểm tra xem privateKey và hash có đầy đủ không
        if (privateKey == null || hash == null) {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Dữ liệu không đầy đủ!\"}");
            return;
        }

        // Lấy userId từ session
        Integer userId = (Integer) session.getAttribute("userId");

        // Kiểm tra xem userId có tồn tại không
        if (userId == null) {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"User chưa đăng nhập!\"}");
            return;
        }

        try {
            // Xử lý ký chữ ký điện tử
            String signature = signWithPrivateKey(privateKey, hash);

            // Lưu chữ ký vào cơ sở dữ liệu
            BillDao orderDao = new BillDaoImpl();
            boolean isSigned = orderDao.saveSignature(idBill, userId, signature);

            // Trả về kết quả
            response.setContentType("application/json");
            if (isSigned) {
                response.getWriter().write("{\"success\": true}");
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Lưu chữ ký thất bại!\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Lỗi khi ký hóa đơn!\"}");
        }
    }

    // Hàm ký hash với private key
    private String signWithPrivateKey(String privateKey, String hash) throws Exception {
        // Chuyển private key từ chuỗi sang đối tượng PrivateKey
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey key = keyFactory.generatePrivate(keySpec);

        // Ký dữ liệu hash
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(key);
        signature.update(hash.getBytes(StandardCharsets.UTF_8));
        byte[] signedData = signature.sign();

        // Trả về chữ ký dạng Base64
        return Base64.getEncoder().encodeToString(signedData);
    }
}
