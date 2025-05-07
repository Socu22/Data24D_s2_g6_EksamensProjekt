package com.g6.data24d_s2_g6_eksamensprojekt.controller;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Lager;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.LagerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.g6.data24d_s2_g6_eksamensprojekt.controller.BrugerController.faaSession;

@Controller
public class LagerController {


    @Autowired
    LagerRepository lagerRepository;

    @GetMapping("/nytLager")
    public String getNytLager(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model);
        if(session == null) return "redirect:/Logind";

        return "nytLager";
    }
    @GetMapping("laverNytLager")
    public String postNytLager(@RequestParam("navn") String navn,
                               @RequestParam("adresse") String adresse,
                               HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model);
        if(session == null) return "redirect:/Logind";

        Lager nytLager = new Lager(navn,adresse);
        lagerRepository.nytLagerLogik(nytLager);
        return "redirect:/"; //skal ændres
    }


}
