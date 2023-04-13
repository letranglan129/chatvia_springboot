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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

@Controller
public class UserController {
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
}
