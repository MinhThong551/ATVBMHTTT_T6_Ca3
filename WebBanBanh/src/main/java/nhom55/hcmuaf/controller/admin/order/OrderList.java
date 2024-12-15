package nhom55.hcmuaf.controller.admin.order;

import nhom55.hcmuaf.beans.Bills;
import nhom55.hcmuaf.beans.Users;
import nhom55.hcmuaf.dao.BillDao;
import nhom55.hcmuaf.dao.daoimpl.BillDaoImpl;
import nhom55.hcmuaf.util.MyUtils;
import org.apache.commons.codec.binary.Hex;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

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

    private void updateAllBills(BillDao billDao) {
        // Lấy tất cả hóa đơn
        List<Bills> listBills = billDao.getAllBills();
        for (Bills bill : listBills) {
            int idBill = bill.getId();

            // Lấy chi tiết hóa đơn
            String billFeatures = billDao.getBillDetailsAsString(idBill);

            // Kiểm tra xem billFeatures có null hay không
            if (billFeatures != null && !billFeatures.isEmpty()) {
                try {
                    String billHashNow = generateSHA256Hash(billFeatures);

                    // Lấy hash từ DB
                    String billHashStored = billDao.getBillHashById(idBill);

                    // Xác định trạng thái xác thực
                    String verifyStatus = billHashNow.equals(billHashStored) ? "đã xác thực" : "đã thay đổi";

                    // Cập nhật trạng thái vào DB
                    billDao.updateBillVerifyStatus(idBill, verifyStatus);
                } catch (NoSuchAlgorithmException e) {
                    // Xử lý lỗi nếu có (ví dụ: thông báo lỗi hoặc ghi log)
                    e.printStackTrace();
                }
            } else {
                // Nếu billFeatures là null hoặc rỗng, có thể ghi log hoặc xử lý theo yêu cầu
                System.out.println("Bill details are empty for Bill ID: " + idBill);
            }
        }
    }


    private String generateSHA256Hash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes());
        return Hex.encodeHexString(hashBytes);
    }
}
