package nhom55.hcmuaf.controller.page.bill;

import nhom55.hcmuaf.beans.BillDetails;
import nhom55.hcmuaf.beans.Bills;
import nhom55.hcmuaf.beans.Users;
import nhom55.hcmuaf.dao.BillDao;
import nhom55.hcmuaf.dao.UsersDao;
import nhom55.hcmuaf.dao.daoimpl.BillDaoImpl;
import nhom55.hcmuaf.dao.daoimpl.UsersDaoImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

import nhom55.hcmuaf.database.JDBIConnector;
import org.apache.commons.codec.binary.Hex;

@WebServlet(name = "billDetail", value = "/page/bill/detail")
public class billDetail extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy idBill từ request
        int idBill = Integer.parseInt(request.getParameter("idBills"));

        // Lấy danh sách chi tiết hóa đơn từ database
        BillDao orderDao = new BillDaoImpl();
        List<BillDetails> list = orderDao.getListProductInABill(idBill);

        // Lấy chuỗi đặc điểm đơn hàng
        BillDaoImpl billDaoImpl = new BillDaoImpl();
        String billFeatures = billDaoImpl.getBillDetailsAsString(idBill);
        Bills bill = billDaoImpl.getABill(idBill);
        request.setAttribute("bill", bill);

        // Tạo mã hash từ chuỗi đặc điểm đơn hàng
        String billHash = null;
        try {
            billHash = generateSHA256Hash(billFeatures);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        // Lưu danh sách sản phẩm, chuỗi đặc điểm và hash vào request để hiển thị trong JSP
        request.setAttribute("list", list);
        request.setAttribute("billFeatures", billFeatures);
        request.setAttribute("billHash", billHash);

        // Lưu idBill vào session để sử dụng trong doPost
        HttpSession session = request.getSession();
        session.setAttribute("idBill", idBill);

        // Chuyển tiếp đến trang chi tiết hóa đơn
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/user/chi-tiet-hoa-don.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer idBill = (Integer) session.getAttribute("idBill");
        Users user = (Users) session.getAttribute("user");
        Integer userId = (Integer) session.getAttribute("userId");

        // In ra các giá trị từ session
        System.out.println("Session idBill: " + idBill);
        System.out.println("Session userId: " + userId);

        if (idBill == null || userId == null) {
            System.out.println("idBill hoặc userId không tồn tại trong session.");
            response.setContentType("text/html");
            response.getWriter().write("<script>alert('Không có idBill hoặc userId!'); window.location.href='" + request.getContextPath() + "/page/bill/list-bill';</script>");
            return;
        }

        String userSignature = request.getParameter("signature");
        String billFeatures = request.getParameter("billFeatures");

        // In các giá trị nhận từ request
        System.out.println("Received user signature: " + userSignature);
        System.out.println("Received bill features: " + billFeatures);

        String billHash;

        // Lấy public key từ database dựa vào userId
        UsersDao usersDao = new UsersDaoImpl();
        String publicKeyString = usersDao.getPublicKeyByUserId(userId);
        System.out.println("Fetched public key: " + publicKeyString);

        if (publicKeyString == null || publicKeyString.isEmpty()) {
            System.out.println("Người dùng không có public key.");
            response.setContentType("text/html");
            response.getWriter().write("<script>alert('Người dùng chưa có public key!'); window.location.href='" + request.getContextPath() + "/page/bill/list-bill';</script>");
            return;
        }

        try {
            billHash = generateSHA256Hash(billFeatures);
            System.out.println("Generated bill hash: " + billHash);

            // Lưu billHash vào bảng bills
            try {
                JDBIConnector.get().withHandle(h ->
                        h.createUpdate("UPDATE bills SET billHash = :billHash , bill_features = :billFeatures WHERE id = :idBill")
                                .bind("billHash", billHash)
                                .bind("billFeatures", billFeatures)
                                .bind("idBill", idBill)
                                .execute()
                );
                System.out.println("Bill hash and features saved to database.");
            } catch (Exception e) {
                e.printStackTrace();
                response.setContentType("text/html");
                response.getWriter().write("<script>alert('Lỗi lưu billHash vào database!'); window.location.href='" + request.getContextPath() + "/page/bill/list-bill';</script>");
                return;
            }

            PublicKey publicKey = decodePublicKey(publicKeyString);
            boolean isVerified = verifySignature(billFeatures, userSignature, publicKey);
            System.out.println("Signature verification result: " + isVerified);

            if (isVerified) {
                // Cập nhật cột verify trong bảng bill
                try {
                    JDBIConnector.get().withHandle(h ->
                            h.createUpdate("UPDATE bills SET verify = :verify, signature = :signature WHERE id = :idBill")
                                    .bind("verify", "đã xác thực")
                                    .bind("signature", userSignature)
                                    .bind("idBill", idBill)
                                    .execute()
                    );
                    System.out.println("Bill verification updated to 'đã xác thực'.");
                    response.setContentType("text/html");
                    response.getWriter().write("<script>alert('Chữ ký hợp lệ và hóa đơn đã được xác thực!'); window.location.href='" + request.getContextPath() + "/page/bill/list-bill';</script>");
                } catch (Exception e) {
                    e.printStackTrace();
                    response.setContentType("text/html");
                    response.getWriter().write("<script>alert('Lỗi cập nhật trạng thái hóa đơn!'); window.location.href='" + request.getContextPath() + "/page/bill/list-bill';</script>");
                    return;
                }
            } else {
                System.out.println("Chữ ký không hợp lệ.");
                response.setContentType("text/html");
                response.getWriter().write("<script>alert('Chữ ký không hợp lệ!'); window.location.href='" + request.getContextPath() + "/page/bill/list-bill';</script>");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            response.setContentType("text/html");
            response.getWriter().write("<script>alert('Lỗi tạo mã hash!'); window.location.href='" + request.getContextPath() + "/page/bill/list-bill';</script>");
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/html");
            response.getWriter().write("<script>alert('Lỗi xác thực chữ ký!'); window.location.href='" + request.getContextPath() + "/page/bill/list-bill';</script>");
        }
    }
    private String generateSHA256Hash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes());
        return Hex.encodeHexString(hashBytes);
    }

    private PublicKey decodePublicKey(String base64PublicKey) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(base64PublicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(keySpec);
    }

    private boolean verifySignature(String data, String base64Signature, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);

        // Tạo hash của dữ liệu
        byte[] dataHash = generateSHA256Hash(data).getBytes();

        signature.update(dataHash);
        byte[] signedData = Base64.getDecoder().decode(base64Signature);

        // Xác minh chữ ký
        return signature.verify(signedData);
    }
}