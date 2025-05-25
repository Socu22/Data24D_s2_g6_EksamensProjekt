package com.g6.data24d_s2_g6_eksamensprojekt.controller;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Bil;
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

@Controller
public class NotationController
{
    //Alle Repositories autowired igennem springFramework
    @Autowired
    NotationRepository notationRepository;

    @Autowired
    BilRepository bilRepository;

    @Autowired
    AftaleRepository aftaleRepository; // ! kun for testing !

    @GetMapping("/NyNotation")
    public String nyNotation(HttpServletRequest request, Model model)
    {
        HttpSession session = BrugerController.faaSession(request, model, "data", "skade");// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";

        // bruger tidligere lavet session attribute
        LejeAftale aftale = (LejeAftale) session.getAttribute("lejeAftale");
        Bil bil           = (Bil) session.getAttribute("bil");

        /*
        if (bil == null || aftale == null) // ! udelukkende for testing !
        {
            aftale = aftaleRepository.tagFatILejeAftale(1); // ! udelukkende for testing !
            bil = bilRepository.tagFatIBil(aftale.getVognNummer()); // ! udelukkende for testing !
        }
         */
        // logik til så notation ved om man kommer fra en bil eller lejeAftale
        if (aftale != null)
        {
            bil = bilRepository.hentBil(aftale.getVognNummer());

            // sender til attributter fra html elementer med (name,value)
            model.addAttribute("lejeAftale", aftale);
            model.addAttribute("bil", bil);
            // sætter session attributter, som muligvis bruges til omdirigering
            session.setAttribute("bil", bil);
        }
        else // hvis ikke der er en lejeaftale, må man være kommet her fra en bil.
        {
            model.addAttribute("bil", bil);
        }

        return "notation";
    }

    @GetMapping("/AnnullerNotation")
    public String annullerNotation(HttpServletRequest request, Model model)
    {
        HttpSession session = BrugerController.faaSession(request, model, "data", "skade");// Hvem der har Rettighed til at bruge metoden.

        // bruger tidligere lavet session attribute
        LejeAftale aftale = (LejeAftale) session.getAttribute("lejeAftale");
        Bil bil           = (Bil)        session.getAttribute("bil");

        if (aftale != null) return "redirect:/VisLejeAftale?aftaleId=" + aftale.getAftale_Id();
        return "redirect:/VisBil?vognNummer=" + bil.getVognNummer();
    }

    @GetMapping("/GemNotation")
    public String gemNotation(HttpServletRequest request, Model model)
    {
        HttpSession session = BrugerController.faaSession(request, model, "data", "skade");// Hvem der har Rettighed til at bruge metoden.

        // bruger tidligere lavet session attribute
        LejeAftale aftale = (LejeAftale) session.getAttribute("lejeAftale");
        Bil bil           = (Bil)        session.getAttribute("bil");

        // Requester efter en parameter fra tidligere html form-> input ('name')
        String notation   = request.getParameter("beskrivelse");
        double pris       = Double.parseDouble(request.getParameter("pris"));

        // logik til så notation ved om man kommer fra en bil eller lejeAftale, og derfor kommer tilbage til enten lejeaftalen eller bilen.
        if (aftale != null)
        {
            notationRepository.gemNotation(aftale.getVognNummer(), aftale.getAftale_Id(), notation, pris);
            return "redirect:/VisLejeAftale?aftaleId=" + aftale.getAftale_Id();
        }

        notationRepository.gemNotation(bil.getVognNummer(), null, notation, pris);
        return "redirect:/VisBil?vognNummer=" + bil.getVognNummer();
    }
}
