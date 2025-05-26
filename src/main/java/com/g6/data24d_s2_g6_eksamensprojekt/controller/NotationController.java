package com.g6.data24d_s2_g6_eksamensprojekt.controller;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Bil;
import com.g6.data24d_s2_g6_eksamensprojekt.model.Bruger;
import com.g6.data24d_s2_g6_eksamensprojekt.model.LejeAftale;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.AftaleRepository;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.BilRepository;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.NotationRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NotationController
{
    //Alle Repositories autowired igennem springFramework
    @Autowired
    NotationRepository notationRepository;

    @Autowired
    BilRepository bilRepository;

    @GetMapping("/NyNotation")
    public String nyNotation(HttpServletRequest request, Model model)
    {
        HttpSession session = BrugerController.faaSession(request, model, Bruger.Stilling.DATA, Bruger.Stilling.SKADE);// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";

        // bruger tidligere lavet session attribute
        int aftaleId  = Integer.parseInt(request.getParameter("aftaleId"));
        session.setAttribute("aftaleId", aftaleId);

        LejeAftale aftale = (LejeAftale) session.getAttribute("lejeAftale");
        Bil bil           = (Bil) session.getAttribute("bil");

        // logik til så notation ved om man kommer fra en bil eller lejeAftale
        if (aftaleId != 0)
        {


            bil = bilRepository.hentBil(aftale.getVognNummer());
            // sætter session attributter, som muligvis bruges til omdirigering
            session.setAttribute("bil", bil);

            // sender til attributter fra html elementer med (name,value)
            model.addAttribute("lejeAftale", aftale);
        }
        // hvis ikke der er en lejeaftale, må man være kommet her fra en bil.
        model.addAttribute("bil", bil);

        return "notation";
    }

    @GetMapping("/AnnullerNotation")
    public String annullerNotation(HttpServletRequest request, Model model)
    {
        HttpSession session = BrugerController.faaSession(request, model, Bruger.Stilling.DATA, Bruger.Stilling.SKADE);// Hvem der har Rettighed til at bruge metoden.

        Bil bil           = (Bil)        session.getAttribute("bil");

        if (bil == null) return "redirect:/VisBiler";
        return "redirect:/VisBil?vognNummer=" + bil.getVognNummer();
    }

    @GetMapping("/GemNotation")
    public String gemNotation(HttpServletRequest request, Model model)
    {
        HttpSession session = BrugerController.faaSession(request, model, Bruger.Stilling.DATA, Bruger.Stilling.SKADE);// Hvem der har Rettighed til at bruge metoden.

        // bruger tidligere lavet session attribute
        Integer aftaleId  = (Integer)    session.getAttribute("aftaleId");
        LejeAftale aftale = (LejeAftale) session.getAttribute("lejeAftale");
        Bil bil           = (Bil)        session.getAttribute("bil");

        // Requester efter en parameter fra tidligere html form-> input ('name')
        String notation   = request.getParameter("beskrivelse");
        double pris       = Double.parseDouble(request.getParameter("pris"));

        // logik til så notation ved om man kommer fra en bil eller lejeAftale, og derfor kommer tilbage til enten lejeaftalen eller bilen.
        if (aftaleId != 0)
        {
            notationRepository.gemNotation(aftale.getVognNummer(), aftaleId, notation, pris);
        }
        else
        {
            notationRepository.gemNotation(bil.getVognNummer(), null, notation, pris);
        }
        return "redirect:/VisBil?vognNummer=" + bil.getVognNummer();
    }
}
