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
    @Autowired
    NotationRepository notationRepository;

    @Autowired
    BilRepository bilRepository; // ?: is this necessary?

    @Autowired
    AftaleRepository aftaleRepo; // ?: is this necessary?

    @GetMapping("/OpretNotation")
    public String opretNotation(HttpServletRequest request, Model model)
    {
        HttpSession session = BrugerController.faaSession(request, model);

        // todo: faktisk få gennem session, lige nu oprettes session ikke for testing
        LejeAftale aftale = null;//   = (LejeAftale) session.getAttribute("LejeAftale");
        Bil bil           = null;//   = (Bil) session.getAttribute("Bil");

        // aftale = aftaleRepo.tagFatILejeAftale(1);

        if (aftale != null)
        {
            model.addAttribute("LejeAftale", aftale);

            if (bil == null || !bil.getVognNummer().equals(aftale.getVognNummer()))
            {
                bil = bilRepository.getBiler().getFirst(); // todo: hent bil ud fra vognNummer
            }
        }
        else if (bil == null)
        {
            bil = bilRepository.getBiler().getFirst(); // todo: hent bil ud fra vognNummer
        }

        model.addAttribute("Bil", bil);

        return "notation";
    }

    @GetMapping("/AnnullerNotation")
    public String annullerNotation()
    {
        return "redirect:/"; // todo: alt efter bil/aftale
    }

    @GetMapping("/BekraeftNotation")
    public String bekraeftNotation(HttpServletRequest request, Model model)
    {
        return "redirect:/"; // todo: alt efter bil/aftale
    }
}
