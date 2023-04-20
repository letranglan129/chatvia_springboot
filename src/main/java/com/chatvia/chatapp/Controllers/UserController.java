/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chatvia.chatapp.Controllers;

/**
 * @author LeLan
 */

import com.chatvia.chatapp.Entities.User;
import com.chatvia.chatapp.Services.UserService;
import com.chatvia.chatapp.Ultis.Uploader;
import com.chatvia.chatapp.WS.Event.FileReceiver;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import netscape.javascript.JSObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @PostMapping("/user/updateAvatar")
    @ResponseBody
    public String updateAvatar(@RequestParam("update-avatar") MultipartFile file, String id) throws IOException, SQLException {
        UserService userService = new UserService();

        if (file.isEmpty()) {
            throw new IOException();
        }

        java.io.File resourceDirectory = new java.io.File("src/main/resources");
        String absolutePath = resourceDirectory.getAbsolutePath();
        String fileName = file.getOriginalFilename();
        File dest = new File(absolutePath + "/" + fileName);
        file.transferTo(dest);

        Uploader uploader = new Uploader();
        Map result = uploader.uploadPath(absolutePath + "/" + fileName);

        int rs = userService.setAvatar((String) result.get("secure_url"), id);

        if (dest.delete()) {
            System.out.println("File đã được xóa.");
        } else {
            System.out.println("Không thể xóa file.");
        }

        JsonObject jsonObject  = new JsonObject();
        if(result.get("secure_url") != null && rs == 1) {
            jsonObject.addProperty("result", true);
            return jsonObject.toString();
        } else {
            throw new IOException();
        }
    }

    @PostMapping("/user/changePassword")
    @ResponseBody
    public String changePassword(String email, String oldPassword, String newPassword, String reNewPassword) throws SQLException {
        UserService userService = new UserService();

        JsonObject resultObj = new JsonObject();

        if (email == null) {
            resultObj.addProperty("result", false);
            resultObj.addProperty("message", "Đã xảy ra lỗi!!!");
            return resultObj.toString();
        }

        User user = userService.findUserByEmail(email);

        if (user == null) {
            resultObj.addProperty("result", false);
            resultObj.addProperty("message", "Người dùng không tồn tại");
            return resultObj.toString();
        }

        if (newPassword != null && !newPassword.equals(reNewPassword)) {
            resultObj.addProperty("result", false);
            resultObj.addProperty("message", "Nhập lại mật khẩu không trùng khớp");
            return resultObj.toString();
        }

        if (oldPassword != null && !BCrypt.checkpw(oldPassword, user.getPassword().replaceFirst("^\\$2y\\$", "\\$2a\\$"))) {
            resultObj.addProperty("result", false);
            resultObj.addProperty("message", "Mật khẩu cũ không chính xác");
            return resultObj.toString();
        }

        int result = userService.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt(10)).replaceFirst("^\\$2a\\$", "\\$2y\\$"), Integer.toString(user.getId()));

        resultObj.addProperty("result", result == 1);
        resultObj.addProperty("message", result == 1 ? "Đổi mật khẩu thành công" : "Đổi mật khẩu không thành công");
        return resultObj.toString();
    }

    @PostMapping("/user/update")
    @ResponseBody
    public String update(String email,
                         String fullname,
                         String phone,
                         String describe) throws SQLException {
        UserService userService = new UserService();
        if (email != null && !email.trim().equals("") && fullname != null && !fullname.trim().equals("")) {
            int rs = userService.update(fullname, email, describe, phone);

            User user = userService.findUserByEmail(email);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("email", user.getEmail());
            jsonObject.addProperty("fullname", user.getFullname());
            jsonObject.addProperty("phone", user.getPhone());
            jsonObject.addProperty("describe", user.getDescribe());
            jsonObject.addProperty("result", rs);
            return jsonObject.toString();
        } else {
            return null;
        }
    }


    @GetMapping("/user")
    @ResponseBody
    public String search(String q, String id, @CookieValue(value = "USER", defaultValue = "") String userCookie) throws SQLException {
        UserService userService = new UserService();
        User user = new Gson().fromJson(userCookie, User.class);

        if (user != null && q != null && !q.trim().equals("")) {
            List<User> results = userService.searchUsers(Integer.toString(user.getId()), q);
            return new Gson().toJson(results);
        }

        if (user != null && id != null && !id.trim().equals("")) {
            User me = userService.findUserById(Integer.parseInt(id));
            return new Gson().toJson(me);
        }

        return null;
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerHandle(ModelMap model, @RequestParam String email, @RequestParam String fullname, @RequestParam String password, @RequestParam String repassword, RedirectAttributes redirectAttributes) throws SQLException {
        UserService userService = new UserService();
        model.addAttribute("EMAIL", email);
        model.addAttribute("FULLNAME", fullname);
        model.addAttribute("PASSWORD", password);
        model.addAttribute("REPASSWORD", repassword);
        if (password.length() < 6) {
            model.addAttribute("ERROR_PASSWORD", "Mật khẩu tối thiểu 6 kí tự");
            return "register";
        }

        if (!email.matches("^\\S+@\\S+\\.\\S+$")) {
            model.addAttribute("ERROR_EMAIL", "Trường này phải là Email");
            return "register";
        }

        if (!password.equals(repassword)) {
            model.addAttribute("ERROR_PASSWORD", "Mật khẩu nhập lại không chính xác");
            return "register";
        }

        User user = userService.findUserByEmail(email);

        if (user != null) {
            model.addAttribute("ERROR_EMAIL", "Email đã tồn tại");
            return "register";
        }

        userService.saveUser(fullname, email, BCrypt.hashpw(password, BCrypt.gensalt(10)).replaceFirst("^\\$2a\\$", "\\$2y\\$"));
        redirectAttributes.addAttribute("isSuccess", true);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(Boolean isSuccess, ModelMap model) {
        model.addAttribute("isSuccess", isSuccess);
        return "login";
    }

    @PostMapping("/login")
    public String loginHandle(HttpSession session, ModelMap model, @RequestParam String email, @RequestParam String password) throws SQLException {
        Gson gson = new GsonBuilder().serializeNulls().create();

        UserService userService = new UserService();
        User user = userService.findUserByEmail(email);
        session.setAttribute("user", user);
        if (user != null && BCrypt.checkpw(password, user.getPassword().replaceFirst("^\\$2y\\$", "\\$2a\\$"))) {
            user.setPassword(null);
            session.setAttribute("user", gson.toJson(user));
            return "redirect:/";
        } else {
            model.addAttribute("EMAIL", email);
            model.addAttribute("isError", true);
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("USER", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/login";
    }
}
