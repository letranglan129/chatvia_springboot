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
import com.chatvia.chatapp.Ultis.CompactName;
import com.google.gson.Gson;
import jakarta.servlet.GenericServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homepage(HttpSession session, HttpServletResponse response, @CookieValue(value = "USER", defaultValue = "") String userCookie, ModelMap model) throws UnsupportedEncodingException, SQLException {
        UserService userService = new UserService();
        if (!userCookie.trim().equals("") && userCookie != null) {
            String meStr = URLDecoder.decode(userCookie, "UTF-8");

            User me = new Gson().fromJson(meStr, User.class);
            me = userService.findUserById(me.getId());

            if(me == null) {
                return "redirect:/login";
            }

            Cookie cookie = new Cookie("USER", URLEncoder.encode(new Gson().toJson(me), "UTF-8"));
            response.addCookie(cookie);

            if (me.getAvatar() != null && !me.getAvatar().trim().equals(""))
                model.addAttribute("avatar", "<img src='" + me.getAvatar() + "' alt='' >");
            else
                model.addAttribute("avatar", "<span class='avatar-label bg-soft-success text-success fs-3 '>" + CompactName.get(me.getFullname() + "</span>"));
            model.addAttribute("user", userService.findUserById(me.getId()));

            System.out.println(meStr);

            return "index";
        } else if ((String) session.getAttribute("user") != null) {
            User me = new Gson().fromJson((String) session.getAttribute("user"), User.class);

            me = userService.findUserById(me.getId());

            Cookie cookie = new Cookie("USER", URLEncoder.encode(new Gson().toJson(me), "UTF-8"));
            response.addCookie(cookie);

            if (me.getAvatar() != null && !me.getAvatar().trim().equals(""))
                model.addAttribute("avatar", "<img src='" + me.getAvatar() + "' alt='' >");
            else
                model.addAttribute("avatar", "<span class='avatar-label bg-soft-success text-success fs-3 '>" + CompactName.get(me.getFullname() + "</span>"));

            model.addAttribute("user", me);

            return "index";
        } else {
            return "redirect:/login";
        }
    }

}
