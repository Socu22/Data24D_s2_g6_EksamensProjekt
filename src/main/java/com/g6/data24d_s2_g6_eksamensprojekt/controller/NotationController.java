package com.g6.data24d_s2_g6_eksamensprojekt.controller;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Bil;
import com.g6.data24d_s2_g6_eksamensprojekt.model.LejeAftale;
import com.g6.data24d_s2_g6_eksamensprojekt.model.Notation;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.AftaleRepository;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.BilRepository;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.NotationRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class NotationController
{
    @Autowired
    NotationRepository notationRepository;

    @Autowired
    BilRepository bilRepository;

    @Autowired
    AftaleRepository aftaleRepository; // ! kun for testing !

    @GetMapping("/NyNotation")
    public String nyNotation(HttpServletRequest request, Model model)
    {
        HttpSession session = BrugerController.faaSession(request, model);
        if(session == null) return "redirect:/Logind";

        LejeAftale aftale = (LejeAftale) session.getAttribute("lejeAftale");
        Bil bil           = (Bil) session.getAttribute("bil");

        /*
        if (bil == null || aftale == null) // ! udelukkende for testing !
        {
            aftale = aftaleRepository.tagFatILejeAftale(1); // ! udelukkende for testing !
            bil = bilRepository.tagFatIBil(aftale.getVognNummer()); // ! udelukkende for testing !
        }
         */

        if (aftale != null)
        {
            bil = bilRepository.tagFatIBil(aftale.getVognNummer());

            model.addAttribute("lejeAftale", aftale);
            model.addAttribute("bil", bil);
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
        HttpSession session = BrugerController.faaSession(request, model);

        LejeAftale aftale = (LejeAftale) session.getAttribute("lejeAftale");
        Bil bil           = (Bil)        session.getAttribute("bil");

        if (aftale != null) return "redirect:/VisLejeAftale?aftaleId=" + aftale.getAftale_Id();
        return "redirect:/VisBil?vognNummer=" + bil.getVognNummer();
    }

    @GetMapping("/GemNotation")
    public String gemNotation(HttpServletRequest request, Model model)
    {
        HttpSession session = BrugerController.faaSession(request, model);

        LejeAftale aftale = (LejeAftale) session.getAttribute("lejeAftale");
        Bil bil           = (Bil)        session.getAttribute("bil");

        String notation   = request.getParameter("beskrivelse");
        double pris       = Double.parseDouble(request.getParameter("pris"));

        if (aftale != null)
        {
            notationRepository.addNotation(aftale.getVognNummer(), aftale.getAftale_Id(), notation, pris);
            return "redirect:/VisLejeAftale?aftaleId=" + aftale.getAftale_Id();
        }

        notationRepository.addNotation(bil.getVognNummer(), null, notation, pris);
        return "redirect:/VisBil?vognNummer=" + bil.getVognNummer();
    }
}
