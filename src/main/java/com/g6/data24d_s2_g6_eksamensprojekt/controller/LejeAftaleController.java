package com.g6.data24d_s2_g6_eksamensprojekt.controller;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Bil;
import com.g6.data24d_s2_g6_eksamensprojekt.model.Kunde;
import com.g6.data24d_s2_g6_eksamensprojekt.model.LejeAftale;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.AftaleRepository;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.BilRepository;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.KundeRepository;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.NotationRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
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
    @Autowired
    NotationRepository notationRepository;

    @GetMapping("/VisLejeAftaler")
    public String visLejeAftaler(HttpServletRequest request, Model model)
    {
        HttpSession session = BrugerController.faaSession(request, model);
        if(session == null) return "redirect:/Logind";

        List<LejeAftale> aftaler = aftaleRepository.hentLejeAftaler();
        List<Kunde> kunder = kundeRepository.hentKunder();
        HashMap<Integer, Kunde> kunderMapped = new HashMap<>();

        for (Kunde kunde: kunder)
        {
            kunderMapped.put(kunde.getKunde_Id(),kunde);
        }

        for (LejeAftale aftale: aftaler)
        {
            aftale.setKunde(kunderMapped.get(aftale.getKunde_Id()));
        }

        model.addAttribute("lejeAftaler", aftaler);
        // todo: udregn dagens indtægt

        return "visLejeAftaler";
    }

    @GetMapping("/VisLejeAftale")
    public String visLejeAftale(@RequestParam("aftaleId") int id, HttpServletRequest request, Model model)
    {
        HttpSession session = BrugerController.faaSession(request, model);
        if(session == null) return "redirect:/Logind";
        // session.removeAttribute("Aftaler");

        LejeAftale aftale = aftaleRepository.hentLejeAftale(id);

        aftale.setKunde(kundeRepository.hentKunde(aftale.getKunde_Id()));

        model.addAttribute("lejeAftale", aftale);
        model.addAttribute("bil", bilRepository.hentBil(aftale.getVognNummer()));
        model.addAttribute("notationer", notationRepository.hentNotationer(aftale.getAftale_Id()));

        return "visLejeAftale";
    }

    //logikken når man trykke på vælg bil.
    @GetMapping("/NyLejeAftale")
    public String nyLejeAftale(Model model, HttpServletRequest request){
        HttpSession session = faaSession(request, model);
        if(session == null) return "redirect:/Logind";

        List<Bil> bilList = bilRepository.hentBiler();
        //Bil bil = bilList.getFirst();
        Bil bil = null;
        if(session.getAttribute("vognNummer") != null){
            bil = bilRepository.hentBil((String) session.getAttribute("vognNummer"));
        }

        List<Kunde> kundeList = kundeRepository.hentKunder();
        //Kunde kunde =kundeList.getFirst();
        Kunde tempKunde = null;
        if(session.getAttribute("kunde_Id")!=null){
            tempKunde = kundeRepository.hentKunde((Integer) session.getAttribute("kunde_Id"));
        }
        model.addAttribute("startDato", session.getAttribute("startDato"));
        model.addAttribute("slutDato", session.getAttribute("slutDato"));
        model.addAttribute("detaljer", session.getAttribute("detaljer"));

        model.addAttribute("bilList",bilList);
        model.addAttribute("bil", bil);


        model.addAttribute("kundeList",kundeList);
        model.addAttribute("tempKunde", tempKunde);

        return "nyLejeAftale";
    }
    @GetMapping("/GemNyLejeAftale")
    public String gemNyLejeAftale(@RequestParam("kunde_Id") int kunde_Id,
                                  @RequestParam("vognNummer") String vognNummer,
                                  @RequestParam("startDato") String startDato,
                                  @RequestParam("slutDato") String slutDato,
                                  @RequestParam("detaljer") String detaljer,
                                  HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model);
        if(session == null) return "redirect:/Logind";

        LejeAftale lejeAftale = new LejeAftale(kunde_Id,vognNummer,startDato,slutDato,detaljer);

        aftaleRepository.gemLejeAftale(lejeAftale);

        return "redirect:/";
    }
    @GetMapping("/OmdirigerNyLejeAftale")
    public String omdirigerNyLejeAftale(Model model, HttpServletRequest request){
        HttpSession session = faaSession(request, model);
        if(session == null) return "redirect:/Logind";

        int aftale_Id=0;
        int kunde_Id=0;
        try {
            aftale_Id = Integer.parseInt(request.getParameter("aftale_Id"));
        }catch (NumberFormatException n){
            System.out.println("fangede ikke aftale Id");
        }
        try {
            kunde_Id = Integer.parseInt(request.getParameter("kunde_Id"));
        }catch (NumberFormatException n){
            System.out.println("fangede ikke kunde Id");
        }

        String vognNummer = request.getParameter("vognNummer");
        String startDato = request.getParameter("startDato");
        String slutDato = request.getParameter("slutDato");
        String detaljer = request.getParameter("detaljer");
        boolean bool = Boolean.parseBoolean(request.getParameter("submitKnap"));
        System.out.println(aftale_Id + " , " + kunde_Id + " , " + vognNummer + " , " + startDato + " , " + slutDato + " , " + detaljer + " , " + bool );
        if (bool){
            LejeAftale lejeAftale = new LejeAftale(kunde_Id,vognNummer,startDato,slutDato,detaljer);

            aftaleRepository.gemLejeAftale(lejeAftale);

            return "redirect:/";
        }
        session.setAttribute("aftale_Id",aftale_Id);
        session.setAttribute("kunde_Id",kunde_Id);
        session.setAttribute("vognNummer",vognNummer);
        session.setAttribute("startDato",startDato);
        session.setAttribute("slutDato",slutDato);
        session.setAttribute("detaljer",detaljer);
        return "redirect:/NyLejeAftale";
    }


}
