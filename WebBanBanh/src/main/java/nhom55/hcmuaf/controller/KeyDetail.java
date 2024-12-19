//package nhom55.hcmuaf.controller;
//
//
//import nhom55.hcmuaf.beans.Key;
//import nhom55.hcmuaf.beans.Users;
//
//import javax.servlet.*;
//import javax.servlet.http.*;
//import javax.servlet.annotation.*;
//import java.io.IOException;
//import java.util.List;
//
//@WebServlet(name = "KeyDetail", value = "/anime-main/KeyDetail")
//public class KeyDetail extends HttpServlet {
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        Users user = (Users) request.getSession().getAttribute("user");
//        List<Key> keyList = DAOKey.accountKeyList(user.getId());
//        request.setAttribute("keyList",keyList);
//        request.getRequestDispatcher("/keyDetail.jsp").forward(request,response);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//    }
//}
