package com.g6.data24d_s2_g6_eksamensprojekt.controller;

import com.g6.data24d_s2_g6_eksamensprojekt.repository.BrugerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BrugerController {

    @Autowired
    BrugerRepository brugerRepository;

    @GetMapping("/")
    public String hjemmeSide(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session == null){
            return "redirect:/Logind";
        }

        return "index";
    }
    @GetMapping("/Logind")
    public String logindSide() {
        return "logind";
    }

    @GetMapping("/OmdirigerLogind")
    public String omdirigerLogindSide(HttpServletRequest request) {
        String brugernavn = request.getParameter("brugernavn");
        String adgangskode = request.getParameter("adgangskode");
        HttpSession session = request.getSession(false);
        if(brugernavn != null && adgangskode != null){
            //tjek om brugeren er i databasen
            sætSession(request);
            return "redirect:/";
        }

        return "redirect:/Logind";
    }
    private void sætSession(HttpServletRequest request){
       // lav en bruger
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(1200);
        //session.setAttribute("aktivBruger", bruger);
    }
}
