/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.devluk.inventory.controller;

import com.devluk.inventory.dao.GenericDao;
import com.devluk.inventory.entity.Users;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {

    @Autowired
    GenericDao genericDao;

    @RequestMapping(value = {"/", "Login"}, method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/Login", method = RequestMethod.POST)
    public String Login(@ModelAttribute Users user, RedirectAttributes model) {
        Map<String, String[]> l = new HashMap<>();
        l.put("filter", new String[]{"username,eq," + user.getUsername(), "password,eq," + user.getPassword()});
        try {
            List allData = genericDao.getAllData(Users.class, l);
            if (allData.size() > 0) {
                user = (Users) allData.get(0);
                model.addFlashAttribute("msg", "alertfy.success('Welcome " + user.getUsername() + "')");
                return "home";
            } else {
                model.addFlashAttribute("msg", "alertfy.success('Username or Password incorrent')");
                return "redirect:/Login";
            }
        } catch (Exception ex) {
            model.addFlashAttribute("msg", "alertfy.success('" + ex.getMessage() + "')");
        }
        return "redirect:/Login";

    }

}