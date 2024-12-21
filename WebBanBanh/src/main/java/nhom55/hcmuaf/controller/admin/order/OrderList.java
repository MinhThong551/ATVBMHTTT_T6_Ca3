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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

            String billn = billDao.getBillDetailsAsString(idBill);
            String currentBillFeatures = billDao.getBillFeature(idBill);

            if (!currentBillFeatures.equals(billn)) {
                try {
                    String billHashNow = generateSHA256Hash(billn);
                    String billHashStored = billDao.getBillHashById(idBill);

                    if (!billHashNow.equals(billHashStored)) {
                        String verifyStatus = "đã thay đổi";
                        billDao.updateBillVerifyStatus(idBill, verifyStatus);
                        billDao.updateStatusABill(idBill, "Đã hủy");

                        // Kiểm tra xem hóa đơn đã được gửi email chưa
                        if (!sentEmailBills.contains(idBill)) {
                            String userEmail = billDao.getEmailByBillId(idBill);
                            String subject = "Thông báo: Đơn hàng đã bị hủy";
                            String message = "Đơn hàng của bạn đã bị hủy và tiền sẽ được hoàn trả về tài khoản trong vòng 24 giờ. Xem chi tiết đơn hàng tại: \"\n" +
                                    "      \"http://localhost:8080/page/bill/list-bill ";

                            // Gửi email
                            EmailUtil.sendNotificationEmail(userEmail, subject, message);
                            System.out.println("Notification email sent to: " + userEmail);

                            // Thêm hóa đơn vào danh sách đã gửi email
                            sentEmailBills.add(idBill);
                        } else {
                            System.out.println("Email already sent for Bill ID: " + idBill);
                        }
                    } else {
                        System.out.println("Bill hash unchanged for Bill ID: " + idBill);
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Bill features unchanged for Bill ID: " + idBill);
            }
        }
    }



    private String generateSHA256Hash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes());
        return Hex.encodeHexString(hashBytes);
    }
}