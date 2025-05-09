package com.g6.data24d_s2_g6_eksamensprojekt.controller;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Bil;
import com.g6.data24d_s2_g6_eksamensprojekt.model.Kunde;
import com.g6.data24d_s2_g6_eksamensprojekt.model.LejeAftale;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.AftaleRepository;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.BilRepository;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.KundeRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

import static com.g6.data24d_s2_g6_eksamensprojekt.controller.BrugerController.faaSession;

@Controller
public class LejeAftaleController {

    @Autowired
    AftaleRepository aftaleRepository;
    @Autowired
    BilRepository bilRepository;
    @Autowired
    KundeRepository kundeRepository;

    @GetMapping("LejeAftaler")
    public String visAftaler(HttpServletRequest request, Model model)
    {
        HttpSession session = BrugerController.faaSession(request, model);
        // if(session == null) return "redirect:/Logind";

        model.addAttribute("LejeAftaler", aftaleRepository.samleLejeAftalerIListeLogik());

        return "lejeAftaleListe";
    }

    @GetMapping("LejeAftale")
    public String visAftale(@RequestParam("aftaleId") int id, HttpServletRequest request, Model model)
    {
        HttpSession session = BrugerController.faaSession(request, model);
        // if(session == null) return "redirect:/Logind";
        // session.removeAttribute("Aftaler");

        LejeAftale aftale = aftaleRepository.tagFatILejeAftale(id);

        model.addAttribute("Bil", bilRepository.tagFatIBil(aftale.getVognNummer()));
        model.addAttribute("LejeAftale", aftale);

        return "visLejeAftale";
    }

    //logikken når man trykke på vælg bil.
    @GetMapping("nyLejeAftale")
    public String getNyLejeAftale(Model model, HttpServletRequest request){
        HttpSession session = faaSession(request, model);
        if(session == null) return "redirect:/Logind";

        List<Bil> bilList = bilRepository.getBiler();

        Bil bil = bilList.getFirst();
        if((Bil) session.getAttribute("bil") != null){
            bil = (Bil) session.getAttribute("bil");
        }

        List<Kunde> kundeList = kundeRepository.getKunder();
        Kunde kunde = kundeRepository.tagFatIKunde(1); //todo: smarter måde at få fart på en kunde

        model.addAttribute("bilList",bilList);
        model.addAttribute("bil", bil);

        model.addAttribute("kundeList",kundeList);
        model.addAttribute("kunde", kunde);

        return "nyLejeAftale";
    }
    @GetMapping("laverNyLejeAftale")
    public String postNyLejeAftale(@RequestParam("kunde_Id") int kunde_Id,
                                   @RequestParam("vognNummer") String vognNummer,
                                   @RequestParam("startDato") String startDato,
                                   @RequestParam("startDato") String slutDato,
                                   @RequestParam("detaljer") String detaljer,
                                   HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model);
        if(session == null) return "redirect:/Logind";

        LejeAftale lejeAftale = new LejeAftale(kunde_Id,vognNummer,startDato,slutDato,detaljer);

        aftaleRepository.nyAftaleLogik(lejeAftale);

        return "redirect:/";
    }


}
