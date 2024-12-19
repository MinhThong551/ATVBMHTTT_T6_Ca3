//package nhom55.hcmuaf.controller;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonObject;
//import nhom55.hcmuaf.beans.Users;
//import nhom55.hcmuaf.beans.Key;
//import nhom55.hcmuaf.beans.LocalDateTimeAdapter;
//
//import javax.servlet.*;
//import javax.servlet.http.*;
//import javax.servlet.annotation.*;
//import java.io.IOException;
//import java.time.LocalDateTime;
//
//@WebServlet(name = "LostKey", value = "/anime-main/LostKey")
//public class LostKey extends HttpServlet {
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        Users user = (Users) request.getSession().getAttribute("user");
//        boolean isSuccess = DAOKey.disableAllOldKey(user.getId(),null);
//        Key disabledKey = DAOKey.latestKey(user.getId());
//        JsonObject object = new JsonObject();
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
//                .create();
//        object.addProperty("isSuccess",isSuccess);
//        object.addProperty("key",gson.toJson(disabledKey));
//        response.getWriter().println(object);
//
//    }
//}
