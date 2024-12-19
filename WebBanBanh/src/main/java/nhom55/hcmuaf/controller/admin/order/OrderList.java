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
            String billn = billDao.getBillDetailsAsString(idBill);
//            boolean update = billDao.updateBillFeatures(idBill, billn); // Cập nhật billFeatures vào DB

            // In giá trị bill_features ra console để kiểm tra
            String currentBillFeatures = billDao.getBillFeature(idBill); // Lấy billFeatures mới nhất từ DB
            System.out.println("billn: " + billn);  // In ra billn
            System.out.println("currentBillFeatures: " + currentBillFeatures);  // In ra currentBillFeatures

            // Kiểm tra xem billFeatures có thay đổi không
            if (!currentBillFeatures.equals(billn)) {
                System.out.println("Bill features updated for Bill ID: " + idBill);

                try {
                    // Tính toán hash mới
                    String billHashNow = generateSHA256Hash(billn);
                    System.out.println("Hash now: " + billHashNow);  // In hash mới

                    // Lấy hash từ DB
                    String billHashStored = billDao.getBillHashById(idBill);
                    System.out.println("Hash stored: " + billHashStored);  // In hash đã lưu

                    // Kiểm tra nếu hash mới khác với hash đã lưu, cập nhật trạng thái
                    if (!billHashNow.equals(billHashStored)) {
                        String verifyStatus = "đã thay đổi"; // Trạng thái mới
                        billDao.updateBillVerifyStatus(idBill, verifyStatus); // Cập nhật trạng thái
                        System.out.println("Updated verification status for Bill ID: " + idBill);  // In trạng thái cập nhật
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