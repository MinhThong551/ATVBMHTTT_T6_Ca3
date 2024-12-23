package nhom55.hcmuaf.controller.admin.order;

import nhom55.hcmuaf.beans.Bills;
import nhom55.hcmuaf.beans.Users;
import nhom55.hcmuaf.dao.BillDao;
import nhom55.hcmuaf.dao.daoimpl.BillDaoImpl;
import nhom55.hcmuaf.util.EmailUtil;
import nhom55.hcmuaf.util.MyUtils;
import org.apache.commons.codec.binary.Hex;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet(name = "OrderList", value = "/admin/order/order-list")
public class OrderList extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users admin = MyUtils.getLoginedUser(session);
        BillDao billDao = new BillDaoImpl();

        String pageStr = request.getParameter("pageId");
        int pageNumber = (pageStr == null) ? 1 : Integer.valueOf(pageStr);

        int quantityDefault = 10;
        int totalRow = billDao.countTotalRowProductInDatabase();
        int haveMaxPage = (totalRow / quantityDefault) + 1;
        List<Bills> listBills = billDao.get10BillsForEachPage(pageNumber, quantityDefault);

        // Kiểm tra nếu người dùng nhấn nút cập nhật tất cả
        if (request.getParameter("updateAll") != null) {
            updateAllBills(billDao);
            // Sau khi cập nhật, chuyển hướng lại trang này
            response.sendRedirect(request.getRequestURI());
            return; // Đảm bảo không tiếp tục xử lý phần còn lại của doGet
        }

        request.setAttribute("listBills", listBills);
        request.setAttribute("admin", admin);
        request.setAttribute("haveMaxPage", haveMaxPage);
        request.setAttribute("pageId", pageNumber);

        RequestDispatcher dispatcher = this.getServletContext()
                .getRequestDispatcher("/WEB-INF/admin/order-list.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private final Set<Integer> sentEmailBills = new HashSet<>(); // Lưu các hóa đơn đã gửi email
    private void updateAllBills(BillDao billDao) {
        List<Bills> listBills = billDao.getAllBills();
        for (Bills bill : listBills) {
            int idBill = bill.getId();

            // Lấy chi tiết hóa đơn dưới dạng chuỗi
            String billn = billDao.getBillDetailsAsString(idBill);
            System.out.println("Bill Details (Bill ID " + idBill + "): " + billn); // Log chi tiết hóa đơn

            try {
                // Lấy userId từ bảng bill để lấy public key từ bảng users
                int userId = bill.getUserId(); // Lấy userId từ hóa đơn
                System.out.println("User ID for Bill ID " + idBill + ": " + userId); // Log userId

                // Lấy public key từ bảng users theo userId
                String base64PublicKey = billDao.getPublicKeyByUserId(userId); // Lấy public key từ bảng users
                System.out.println("Base64 Public Key (User ID " + userId + "): " + base64PublicKey); // Log public key

                // Lấy chữ ký từ cơ sở dữ liệu
                String base64Signature = billDao.getSignatureById(idBill);
                System.out.println("Base64 Signature (Bill ID " + idBill + "): " + base64Signature); // Log chữ ký

                // Giải mã khóa công khai
                PublicKey publicKey = decodePublicKey(base64PublicKey);

                // Xác thực chữ ký
                boolean isSignatureValid = verifySignature(billn, base64Signature, publicKey);
                System.out.println("Signature Verification Result (Bill ID " + idBill + "): " + isSignatureValid); // Log kết quả xác thực chữ ký

                if (isSignatureValid) {
                    // Nếu chữ ký hợp lệ, không cần thay đổi trạng thái
                    System.out.println("Chữ ký hợp lệ cho Bill ID: " + idBill);
                } else {
                    // Nếu chữ ký không hợp lệ, cập nhật trạng thái của hóa đơn và gửi email
                    System.out.println("Xác thực chữ ký thất bại cho Bill ID: " + idBill);

                    // Cập nhật trạng thái hóa đơn
                    String verifyStatus = "đã thay đổi";
                    billDao.updateBillVerifyStatus(idBill, verifyStatus); // Cập nhật trạng thái verifyStatus thành "đã thay đổi"
                    billDao.updateStatusABill(idBill, "Đã hủy"); // Cập nhật trạng thái hóa đơn thành "Đã hủy"

                    // Kiểm tra xem email đã được gửi chưa
                    if (!sentEmailBills.contains(idBill)) {
                        String userEmail = billDao.getEmailByBillId(idBill);
                        String subject = "Thông báo: Đơn hàng đã bị hủy";
                        String message = "Đơn hàng của bạn đã bị hủy và tiền sẽ được hoàn trả về tài khoản trong vòng 24 giờ. Xem chi tiết đơn hàng tại: \"\n" +
                                "      \"http://localhost:8080/page/bill/list-bill ";

                        // Gửi email
                        EmailUtil.sendNotificationEmail(userEmail, subject, message);
                        System.out.println("Đã gửi email thông báo đến: " + userEmail); // Log email đã gửi

                        // Thêm hóa đơn vào danh sách đã gửi email
                        sentEmailBills.add(idBill);
                    } else {
                        System.out.println("Email đã được gửi cho Bill ID: " + idBill); // Log trường hợp email đã được gửi
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Lỗi trong quá trình xử lý Bill ID: " + idBill); // Log lỗi
            }
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