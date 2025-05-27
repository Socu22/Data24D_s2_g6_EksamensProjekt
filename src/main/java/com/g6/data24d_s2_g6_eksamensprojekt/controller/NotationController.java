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

    // requestmapping, som sætter modellen op til indtastning af en notation
    // herved menes hhv en skade eller en fejl&mangler,
    // alt efter om der gives en relateret aftaleId eller ej
    @GetMapping("/NyNotation")
    public String nyNotation(HttpServletRequest request, Model model)
    {
        HttpSession session = BrugerController.faaSession(request, model, Bruger.Stilling.DATA, Bruger.Stilling.SKADE);// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";

        // hent aftaleId fra url - givet at 'Interger's parser ikke tillad null,
        // og at sql-lister starter ved 1, tolkes værdien '0' som ikke værende relateret til en aftale
        int aftaleId  = Integer.parseInt(request.getParameter("aftaleId"));
        session.setAttribute("aftaleId", aftaleId);

        // bruger tidligere lavet session attribute
        LejeAftale aftale = (LejeAftale) session.getAttribute("lejeAftale");
        Bil bil           = (Bil) session.getAttribute("bil");

        // logik til så notation ved om man kommer fra en bil eller lejeAftale
        if (aftaleId != 0)
        {
            // der er ikke nødvændigvis den korrekte bil hvis man kommer fra en lejeaftale
            // derfor hentes bilen relateret til aftalen fra databasen
            bil = bilRepository.hentBil(aftale.getVognNummer());
            session.setAttribute("bil", bil);

            // sender til attributter fra html elementer med (name,value)
            model.addAttribute("lejeAftale", aftale);
        }

        // hvis ikke der er en lejeaftale, må man være kommet her fra en bil.
        model.addAttribute("bil", bil);

        return "notation";
    }

    // requestmapping til at annullere indtastning af en notation, og derved ikke gemme den i databasen
    @GetMapping("/AnnullerNotation")
    public String annullerNotation(HttpServletRequest request, Model model)
    {
        HttpSession session = BrugerController.faaSession(request, model, Bruger.Stilling.DATA, Bruger.Stilling.SKADE);// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";

        session.removeAttribute("aftaleId");
        Bil bil = (Bil) session.getAttribute("bil"); // hent "aktiv" bil fra aktiv session

        if (bil == null) return "redirect:/VisBiler";
        return "redirect:/VisBil?vognNummer=" + bil.getVognNummer(); // diriger til visning af aktiv bil
    }

    // requestmapping til at bekræfte og gemme indtastet notation i databasen.
    @GetMapping("/GemNotation")
    public String gemNotation(HttpServletRequest request, Model model)
    {
        HttpSession session = BrugerController.faaSession(request, model, Bruger.Stilling.DATA, Bruger.Stilling.SKADE);// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";

        // hent attributter fra aktiv sessionen som bruges til at identificere pågældende bil/aftale
        Bil bil           = (Bil)        session.getAttribute("bil");
        LejeAftale aftale = (LejeAftale) session.getAttribute("lejeAftale");
        Integer aftaleId  = (Integer)    session.getAttribute("aftaleId");
        session.removeAttribute("aftaleId");

        // Hent parametre fra request
        String notation   = request.getParameter("beskrivelse");
        double pris       = Double.parseDouble(request.getParameter("pris"));

        // hvis der er sat en gyldig aftaleId i session, skrives denne med i databasen, og notationen betragtes som en skade.
        if (aftaleId != 0)
        {
            notationRepository.gemNotation(aftale.getVognNummer(), aftaleId, notation, pris);
            bilRepository.saetStatus(bil.getVognNummer(), "SKADET");
        }
        else // ellers skrives tilskrives notationen null, og betragtes som en fejl&mangler
        {
            notationRepository.gemNotation(bil.getVognNummer(), null, notation, pris);
        }
        return "redirect:/VisBil?vognNummer=" + bil.getVognNummer(); // diriger til visning af "aktiv" bil
    }
}
