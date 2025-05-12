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

        List<Notation> liste;
        LejeAftale aftale = null;
        Bil bil           = null;

        if (session != null)
        {
            aftale = (LejeAftale) session.getAttribute("LejeAftale");
            bil    = (Bil) session.getAttribute("Bil");
        }

        if (bil == null || aftale == null) // ! udelukkende for testing !
        {
            aftale = aftaleRepository.tagFatILejeAftale(1); // ! udelukkende for testing !
            bil = bilRepository.tagFatIBil(aftale.getVognNummer()); // ! udelukkende for testing !
        }

        if (aftale != null)
        {
            model.addAttribute("LejeAftale", aftale);
            model.addAttribute("Bil", bil); // todo: hent bil gennem lejeaftalen!
        }
        else // hvis ikke der er en lejeaftale, må man være kommet her fra en bil.
        {
            model.addAttribute("Bil", bil);
        }


        return "notation";
    }

    @GetMapping("/AnnullerNotation")
    public String annullerNotation(HttpServletRequest request, Model model)
    {
        HttpSession session = BrugerController.faaSession(request, model);
        if (session.getAttribute("LejeAftale") != null) return "redirect:/"; // todo: LejeAftale-visning
        return "redirect:/"; // todo: BilKatalog-visning
    }

    @GetMapping("/GemNotation")
    public String gemNotation(HttpServletRequest request, Model model)
    {
        HttpSession session = BrugerController.faaSession(request, model);

        Bil bil           = (Bil)        session.getAttribute("Bil");
        LejeAftale aftale = (LejeAftale) session.getAttribute("LejeAftale");

        String notation   = request.getParameter("beskrivelse");
        double pris       = Double.parseDouble(request.getParameter("pris"));

        if (aftale != null)
        {
            notationRepository.addNotation(aftale.getVognNummer(), aftale.getAftale_Id(), notation, pris);

            return "redirect:/"; // todo: LejeAftale-visning
        }

        notationRepository.addNotation(bil.getVognNummer(), null, notation, pris);

        return "redirect:/"; // todo: BilKatalog-visning
    }
}
