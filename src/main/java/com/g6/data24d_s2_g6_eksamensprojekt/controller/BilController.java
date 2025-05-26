package com.g6.data24d_s2_g6_eksamensprojekt.controller;

import com.g6.data24d_s2_g6_eksamensprojekt.model.*;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.g6.data24d_s2_g6_eksamensprojekt.controller.BrugerController.faaSession;

@Controller
public class BilController {
    //Alle Repositories autowired igennem springFramework
    @Autowired
    BilRepository bilRepository;
    @Autowired
    BilTypeRepository bilTypeRepository;
    @Autowired
    LagerRepository lagerRepository;
    @Autowired
    AftaleRepository aftaleRepository;
    @Autowired
    NotationRepository notationRepository;

    @GetMapping("/VisBiler")
    public String visBiler(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model);// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";
        // Bruges til søgeFunktion i VisBiler til at lave dropdown menuer
        List<Bil> biler = bilRepository.hentEksisteredeBiler(); //selve bilerne der bliver hvist
        List<Lager> lagerList = lagerRepository.hentLager(); // dropdown
        List<BilType> bilTypeList = bilTypeRepository.hentBilTyper();// dropdown
        List<String> statusList = Bil.Status.getNames();// dropdown
        statusList.removeIf(s -> s.equalsIgnoreCase("SOLGT")); // sletter SOLGT da solgte biler ikke kan komme i lejeAftale igen. men stadig skal kunne ses i VisLejeAftaler

        //til bilkort
        if(session.getAttribute("biler")!=null){
            biler = (List<Bil>) session.getAttribute("biler");
        }

        // sender til attributter fra html elementer med (name,value)
        //dropdown, og list
        model.addAttribute("biler",biler);
        model.addAttribute("lagerList",lagerList);
        model.addAttribute("bilTypeList",bilTypeList);
        model.addAttribute("statusList",statusList);


        // omdirigeret attributter fra html form->input (name,value)
        model.addAttribute("vognNummer",session.getAttribute("vognNummer"));
        model.addAttribute("lager_Id",session.getAttribute("lager_Id"));
        model.addAttribute("maerke",session.getAttribute("maerke"));
        model.addAttribute("status",session.getAttribute("status"));

        //slet efter gået væk fra VisBiler
        session.removeAttribute("vognNummer");
        session.removeAttribute("lager_Id");
        session.removeAttribute("maerke");
        session.removeAttribute("status");
        session.removeAttribute("lejeAftale");
        session.removeAttribute("lejeAftale_id");
        session.removeAttribute("aftale_id");
        session.removeAttribute("bil");
        session.removeAttribute("vognNummer");

        return "visBiler";
    }

    @GetMapping("OmdirigerVisBiler")
    public String Omdirigerbil(HttpServletRequest request, Model model) {
        HttpSession session = faaSession(request, model);// Hvem der har Rettighed til at bruge metoden.);
        if (session == null) return "redirect:/Logind";

        // Requester efter en parameter fra tidligere html form-> input ('name')
        String vognNummer = request.getParameter("vognNummer");
        String lager_Id = request.getParameter("lager_Id");
        String maerke = request.getParameter("maerke");
        String status = request.getParameter("status");

        List<Bil> bilList = new ArrayList<>();

        // Hvis vognNummer eller stelNummer er givet
        if (vognNummer != null && !vognNummer.isEmpty()) {
            if (vognNummer.length() <=7) { // her tjekkes der om det er et vognNummer.
                bilList = bilRepository.hentBilerUdFraVognNummer(vognNummer);
            } else if (vognNummer.length() <= 17) { // her tjekkes der om det er et stelNummer.
                String stelNummer = vognNummer; //Sikker på at værdien i vognNummer rent faktisk er et stelNummer
                bilList = bilRepository.hentBilerUdFraStelNummer(stelNummer);
            }
        } else if(maerke != null || lager_Id != null||status!=null) {
            // hvis du vælger lager, mærke eller begge virke den her metode
            bilList = bilRepository.hentEksisteredeBilerSoegFunktion(lager_Id,maerke,status);
        }
        // sætter session attributter, som muligvis bruges til omdirigering
        session.setAttribute("vognNummer",vognNummer);
        if(lager_Id!=null){
            session.setAttribute("lager_Id",Integer.parseInt(lager_Id));
        }
        session.setAttribute("maerke",maerke);
        session.setAttribute("status",status);

        session.setAttribute("biler", bilList);
        return "redirect:/VisBiler";
    }

    @GetMapping("VisBilerReset")
    public String omdirigerVisBilerReset(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model);// Hvem der har Rettighed til at bruge metoden.
        if (session == null) return "redirect:/Logind";
        // sletter ekstra Attributter så alle biler kan ses
        session.removeAttribute("biler");

        return "redirect:/VisBiler";
    }

    @GetMapping("/VisBil")
    public String visBil(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model);// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";

        // Requester efter en parameter fra tidligere html form-> input ('name')
        double notationsPris = 0;
        String vognNummer = request.getParameter("vognNummer");
        Bil bil           = bilRepository.hentBil(vognNummer);
        List<Notation> notationer = notationRepository.hentNotationer(vognNummer);
        LejeAftale aftale = aftaleRepository.hentLejeAftale(vognNummer);
        if(aftale != null)
        {
            aftale.setBil(bil);
            session.setAttribute("aftaleId",aftale.getAftale_Id());
        }
        for (Notation notation : notationer)
        {
            notationsPris += notation.getPris();
        }
        // sender til attributter fra html elementer med (name,value)
        // sætter session attributter, som muligvis bruges til omdirigering

        session.setAttribute("bil", bil);
        session.setAttribute("vognNummer", bil.getVognNummer());

        model.addAttribute("bil"          , bil);
        model.addAttribute("aftale"       , aftale);
        model.addAttribute("notationer"   , notationer);
        model.addAttribute("notationsPris", notationsPris);

        return "visBil";
    }

    @GetMapping("/OpdaterBil")
    public String opdaterBil(HttpServletRequest request, Model model)
    {
        HttpSession session = faaSession(request, model, Bruger.Stilling.FORRETNING);
        if(session == null) return "redirect:/Logind";

        String status     = request.getParameter("status");
        String vognNummer = (String) session.getAttribute("vognNummer");
        int aftaleId      = (int) session.getAttribute("aftaleId");

        if (status.equals("BETALT")) {aftaleRepository.saetBetalt(aftaleId);}
        else                         {bilRepository.saetStatus(vognNummer, status);}

        return "redirect:/VisBil?vognNummer=" + vognNummer;
    }

    @GetMapping("/NyBil")
    public String nyBil(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model, Bruger.Stilling.FORRETNING);// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";

        //lister til dropdown menuer
        List<BilType> bilTypeList = bilTypeRepository.hentBilTyper();
        List<Lager> lagerList =lagerRepository.hentLager();
        List<String> statusList = Bil.Status.getNames();
        statusList.removeIf(s -> !s.equalsIgnoreCase("TILGAENGELIG")); // sletter alt anden end TILGAENGELIG fra status.
        // sender til attributter fra html elementer med (name,value)
        model.addAttribute("lagerList",lagerList);
        model.addAttribute("bilTypeList",bilTypeList);
        model.addAttribute("statusList",statusList);

        return "nyBil";
    }

    @GetMapping("/GemNyBil")
    public String gemNyBil(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model, Bruger.Stilling.FORRETNING);// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";
        // Requester efter en parameter fra tidligere html form-> input ('name')
        String vognNummer= (String) request.getParameter("vognNummer");
        String stelNummer= (String) request.getParameter("stelNummer");
        int bilType_Id= Integer.parseInt(request.getParameter("bilType_Id"));
        int lager_Id= Integer.parseInt(request.getParameter("lager_Id"));
        String status= (String) request.getParameter("status");

        // Samler alle tidligere parameter ind i en ny constructor ud fra relevant DataType
        Bil bil = new Bil(vognNummer,stelNummer,new BilType(bilType_Id),lager_Id,status);
        // Gemmer tidligere objekt vha. en metode i den her Repository
        bilRepository.gemBil(bil);
        return "redirect:/VisBil?vognNummer="+vognNummer;
    }
    @GetMapping("/SaelgBil") //Todo: ændre navnet her så det passer med metoden
    public String saelgBil(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model, Bruger.Stilling.FORRETNING);// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";


        // bruger tidligere lavet session attribute fra VisBil
        String vognNummer= (String) session.getAttribute("vognNummer");


        //Tjekker om bil rent faktisk bliver slettet
        boolean bool = bilRepository.solgtBil(vognNummer);
        if (!bool){
            System.out.println("Fangede ikke en bil(sletBil)");
        }
        return "redirect:/";
    }

}
