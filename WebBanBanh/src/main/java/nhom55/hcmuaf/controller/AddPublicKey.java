package nhom55.hcmuaf.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import database.DAOBonus;
import nhom55.hcmuaf.beans.Users;
import nhom55.hcmuaf.dao.daoimpl.DAOKey;
import model.Bonus;
import nhom55.hcmuaf.beans.Key;
import nhom55.hcmuaf.beans.LocalDateTimeAdapter;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;

@WebServlet(name = "AddPublicKey", value = "/anime-main/AddPublicKey")
public class AddPublicKey extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String addPublicKey = request.getParameter("addPublicKey");
        String addDayExpired = request.getParameter("addDayExpired");
        Users user = (Users) request.getSession().getAttribute("user");
        try {
            Key insertedKey = DAOKey.importKey(user.getId(), user.getUsername(), addPublicKey,addDayExpired);
               if(insertedKey!=null){
                   Gson gson = new GsonBuilder()
                           .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                           .create();
                   JsonObject object = new JsonObject();
                   object.addProperty("newKey",gson.toJson(insertedKey));
                   response.getWriter().println(object);
               }else{
                   response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                   response.getWriter().println("Public key đã tồn tại trong hệ thống");
               }
        } catch (IllegalArgumentException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Public key có độ dài không hợp lệ");
        }


    }
}
