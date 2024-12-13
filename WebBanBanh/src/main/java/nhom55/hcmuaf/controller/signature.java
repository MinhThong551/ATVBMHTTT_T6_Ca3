//package nhom55.hcmuaf.controller;
//
//import nhom55.hcmuaf.beans.Products;
//import nhom55.hcmuaf.dao.daoimpl.ProductDaoImpl;
//import nhom55.hcmuaf.log.LogDTO;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonObject;
//import nhom55.hcmuaf.beans.Bills;
//import nhom55.hcmuaf.beans.Key;
//import nhom55.hcmuaf.beans.Users;
//import nhom55.hcmuaf.beans.LocalDateTimeAdapter;
////import nhom55.hcmuaf.dao.daoimpl.DAOBills;
//import nhom55.hcmuaf.dao.daoimpl.DAOKey;
////import database.DAORecharge;
////import model.*;
//import nhom55.hcmuaf.security.DSA;
//import nhom55.hcmuaf.services_remaster.ProductService;
//
//import javax.servlet.*;
//import javax.servlet.http.*;
//import javax.servlet.annotation.*;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.List;
//
//@WebServlet(name = "signature", value = "/anime-main/signature")
//public class signature extends HttpServlet {
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//       try {
//           response.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
//           HttpSession session = request.getSession();
//           Users user = (Users) session.getAttribute("user");
//           String ipClient = request.getRemoteAddr();
//           String action = request.getParameter("action");
//           ProductService productService = (ProductService) session.getAttribute("order");
//           PrintWriter out = response.getWriter();
//
//           JsonObject object = new JsonObject();
//           Key publicKey = DAOKey.accountKeyNow(user.getId());
//           if(productService==null){
//               object.addProperty("isSuccess",false);
//               object.addProperty("message","Đơn hàng không còn hiệu lực");
//               out.print(object);
//               return;
//           }
//           DAORecharge daoRecharge = new DAORecharge();
//           LogDTO log = new LogDTO(LogDTO.INFO, -1, ipClient, "checkout", null, 1);
//           HashMap<String, WishListDetail> wishlist = (HashMap<String, WishListDetail>) session.getAttribute("wishlist");
//           ProductDaoImpl productDaoImpl = new ProductDaoImpl();
//           LocalDateTime now = LocalDateTime.now();
//           Gson gson = new GsonBuilder()
//                   .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
//                   .create();
//
//           String privateKey = request.getParameter("fileContent");
//           int billNum = (int) session.getAttribute("number");
//           List<Products> movieList = productService.getSelectedMovies();
//
//           if(publicKey==null){
//               object.addProperty("isSuccess",false);
//               object.addProperty("message","Người dùng hiện chưa có Key nào đang được áp dụng");
//               out.print(object);
//               return;
//           }
//           int id_bill = DAOBills.createBill(user.getId(), billNum, productService.getTotalPrice(), movieList,publicKey.getKey());
//           Bills bill =  DAOBills.getBillById(id_bill);
//           boolean isSign = false;
//           try {
//               byte[] signData = DSA.signBill(bill.toString()+DAOBills.getBillDetail(bill.getId()).toString(),DSA.verifyPrivateKey(privateKey),DSA.verifyPublicKey(publicKey.getKey()));
//               if(!(signData.length>0)){
//                   object.addProperty("isSuccess",false);
//                   object.addProperty("message","PrivateKey không hợp lệ với PublicKey hiện tại");
//                   out.print(object);
//                   return;
//
//               }
//               isSign = DAOBills.saveSignatureToBill(bill, DSA.toBase64(signData));
//           } catch (Exception e) {
//               object.addProperty("isSuccess",false);
//               object.addProperty("message","File không hợp lệ");
//               out.print(object);
//               return;
//           }
//
//           if (isSign) {
//                session.setAttribute("bill",bill);
//                object.addProperty("isSuccess",true);
//                out.print(object);
//           }
//
//       }catch (Exception e){
//           e.printStackTrace();
//       }
//
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        doGet(request, response);
//    }
//}
