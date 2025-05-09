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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
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

    @GetMapping("/OpretNotation")
    public String opretNotation(HttpServletRequest request, Model model)
    {
        HttpSession session = BrugerController.faaSession(request, model);

        List<Notation> liste;
        LejeAftale aftale = null;
        Bil bil           = null;

        if (session != null)
        {
            aftale = (LejeAftale) session.getAttribute("LejeAftale");
            bil    = (Bil) session.getAttribute("Bil");
        }

        aftale = aftaleRepository.tagFatILejeAftale(1); // ! udelukkende for testing !
        bil = bilRepository.tagFatIBil(aftale.getVognNummer()); // ! udelukkende for testing !
        model.addAttribute("Biler", bilRepository.getBiler());

        if (aftale != null)
        {
            // ? liste = notationRepository.getNotationer(aftale);

            model.addAttribute("LejeAftale", aftale);
        }
        if (bil != null)
        {
            // ? liste = notationRepository.getNotationer(bil);

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

    @GetMapping("/BekraeftNotation")
    public String bekraeftNotation(@RequestParam("bekraeft") boolean bekraeft, HttpServletRequest request, Model model)
    {
        HttpSession session = BrugerController.faaSession(request, model);

        if (bekraeft)
        {
            if (session.getAttribute("LejeAftale") != null) return "redirect:/"; // todo: LejeAftale-visning
            return "redirect:/"; // todo: BilKatalog-visning
        }

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
