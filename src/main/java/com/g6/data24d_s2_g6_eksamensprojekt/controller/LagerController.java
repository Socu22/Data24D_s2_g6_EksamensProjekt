package com.g6.data24d_s2_g6_eksamensprojekt.controller;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Bruger;
import com.g6.data24d_s2_g6_eksamensprojekt.model.Lager;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.LagerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.g6.data24d_s2_g6_eksamensprojekt.controller.BrugerController.faaSession;

@Controller
public class LagerController {
    //Alle Repositories autowired igennem springFramework
    @Autowired
    LagerRepository lagerRepository;

    @GetMapping("/NytLager")
    public String nytLager(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model, Bruger.Stilling.FORRETNING);// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";


        return "nytLager";
    }

    @GetMapping("/GemNytLager")
    public String gemNytLager(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model, Bruger.Stilling.FORRETNING);// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";

        // Requester efter en parameter fra tidligere html form-> input ('name')
        String navn = request.getParameter("navn");
        String adresse = request.getParameter("adresse");
        // Samler alle tidligere parameter ind i en ny constructor ud fra relevant DataType
        Lager nytLager = new Lager(navn,adresse);
        // Gemmer tidligere objekt vha. en metode i den her Repository
        lagerRepository.gemLager(nytLager);
        return "redirect:/VisBiler";
    }
}
