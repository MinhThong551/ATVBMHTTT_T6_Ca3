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
import java.util.*;

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

        List<Bills> changedBills = null; // Danh sách các hóa đơn đã thay đổi

        // Kiểm tra nếu người dùng nhấn nút cập nhật tất cả
        if (request.getParameter("updateAll") != null) {
            changedBills = updateAllBills(billDao); // Cập nhật tất cả và trả về danh sách các hóa đơn đã thay đổi
            request.setAttribute("changedBills", changedBills); // Truyền danh sách này sang JSP

            // Làm mới lại danh sách hóa đơn để phản ánh các thay đổi trong bảng chính
            listBills = billDao.get10BillsForEachPage(pageNumber, quantityDefault);
        }

        request.setAttribute("listBills", listBills); // Truyền lại danh sách hóa đơn mới
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
    private List<Bills> updateAllBills(BillDao billDao) {
        List<Bills> listBills = billDao.getAllBills();
        List<Bills> changedBills = new ArrayList<>(); // Danh sách hóa đơn đã thay đổi

        for (Bills bill : listBills) {
            int idBill = bill.getId();

            String billn = billDao.getBillDetailsAsString(idBill);
            try {
                int userId = bill.getUserId();
                String base64PublicKey = billDao.getPublicKeyByUserId(userId);
                String base64Signature = billDao.getSignatureById(idBill);

                PublicKey publicKey = decodePublicKey(base64PublicKey);
                boolean isSignatureValid = verifySignature(billn, base64Signature, publicKey);

                if (isSignatureValid) {
                    continue; // Bỏ qua nếu chữ ký hợp lệ
                }

                String verifyStatus = billDao.getBillVerifyStatus(idBill);
                if (verifyStatus.equals("chưa xác thực") || verifyStatus.equals("đã thay đổi")) {
                    continue; // Không làm gì nếu trạng thái là 'chưa xác thực' hoặc 'đã thay đổi'
                }

                billDao.updateBillVerifyStatus(idBill, "đã thay đổi");
                billDao.updateStatusABill(idBill, "Đã hủy");
                changedBills.add(bill); // Thêm hóa đơn vào danh sách đã thay đổi

                if (!sentEmailBills.contains(idBill)) {
                    String userEmail = billDao.getEmailByBillId(idBill);
                    String subject = "Thông báo: Đơn hàng đã bị hủy";
                    String message = "Đơn hàng của bạn đã bị hủy và tiền sẽ được hoàn trả về tài khoản trong vòng 24 giờ. Xem chi tiết đơn hàng tại: \n" +
                            "http://localhost:8080/page/bill/list-bill";

                    EmailUtil.sendNotificationEmail(userEmail, subject, message);
                    sentEmailBills.add(idBill);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return changedBills; // Trả về danh sách các hóa đơn đã thay đổi
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