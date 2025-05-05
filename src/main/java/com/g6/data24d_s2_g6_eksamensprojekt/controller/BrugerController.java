package com.g6.data24d_s2_g6_eksamensprojekt.controller;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Bruger;
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
    public String logindSide(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if(session != null)
            return "redirect:/";

        String brugerIkkeFundet = request.getParameter("d");
        model.addAttribute("brugerIkkeFundet", brugerIkkeFundet);

        return "logind";
    }

    @GetMapping("/OmdirigerLogind")
    public String omdirigerLogindSide(HttpServletRequest request, Model model) {
        String brugernavn = request.getParameter("brugernavn");
        String adgangskode = request.getParameter("adgangskode");
        HttpSession session = request.getSession(false);
        if(brugernavn != null && adgangskode != null){
            Bruger bruger = brugerRepository.faaBruger(brugernavn, adgangskode);
            if(bruger != null){
                saetSession(request, bruger);
                return "redirect:/";
            }
           // model.addAttribute("brugerIkkeFundet", true);
            return "redirect:/Logind?d=true";
        }

        return "redirect:/Logind";
    }
    private void saetSession(HttpServletRequest request, Bruger bruger){

        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(1200);
        session.setAttribute("aktivBruger", bruger);
    }
}
