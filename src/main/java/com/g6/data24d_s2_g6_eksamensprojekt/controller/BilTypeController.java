package com.g6.data24d_s2_g6_eksamensprojekt.controller;

import com.g6.data24d_s2_g6_eksamensprojekt.model.BilType;
import com.g6.data24d_s2_g6_eksamensprojekt.model.Bruger;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.BilTypeRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static com.g6.data24d_s2_g6_eksamensprojekt.controller.BrugerController.faaSession;

@Controller
public class BilTypeController {

    //Alle Repositories autowired igennem springFramework
    @Autowired
    BilTypeRepository bilTypeRepository;

    @GetMapping("/NyBilType")
    public String getNyBilType(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model, Bruger.Stilling.FORRETNING);// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";

        List<String> maerkeList= bilTypeRepository.hentAlleMaerkerDK(); //Alle Mærker af biler der bliver Solgt i Danmark
        model.addAttribute("maerkeList",maerkeList); // sendt til dropdownMenu


        return "nyBilType";
    }

    @GetMapping("/GemNyBilType")
    public String gemNyBilType(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model, Bruger.Stilling.FORRETNING);// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";

        int bilType_Id = bilTypeRepository.getNextId(); // finder næste id.

        // Requester efter en parameter fra tidligere html form-> input ('name')
        String mærke = request.getParameter("maerke");
        String modelP = request.getParameter("model");
        String udstyrsniveau = request.getParameter("udstyrsniveau");
        double stålPris = Double.parseDouble(request.getParameter("stålPris"));
        double afgift = Double.parseDouble(request.getParameter("afgift"));
        double udledning_Co2 = Double.parseDouble(request.getParameter("udledning_Co2"));

        // Samler alle tidligere parameter ind i en ny constructor ud fra relevant DataType
        BilType bilType = new BilType(bilType_Id,mærke,modelP,udstyrsniveau,stålPris,afgift,udledning_Co2);

        // Gemmer tidligere objekt vha. en metode i den her Repository
        bilTypeRepository.gemBilType(bilType);
        return "redirect:/VisBiler";
    }
}
