/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chatvia.chatapp.Controllers;

/**
 * @author LeLan
 */

import com.chatvia.chatapp.Entities.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homepage(HttpSession session, HttpServletResponse response, @CookieValue(value = "USER", defaultValue = "") String userCookie) throws UnsupportedEncodingException {
        if (!userCookie.trim().equals("")) {
            return "index";
        } else if ((String) session.getAttribute("user") != null) {
            Cookie cookie = new Cookie("USER", URLEncoder.encode((String) session.getAttribute("user"), "UTF-8"));

            response.addCookie(cookie);

            return "index";
        } else  {
            return "redirect:login";
        }
    }

}
