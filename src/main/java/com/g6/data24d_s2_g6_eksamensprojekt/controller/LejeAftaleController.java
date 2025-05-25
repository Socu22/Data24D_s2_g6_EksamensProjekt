package com.g6.data24d_s2_g6_eksamensprojekt.controller;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Bil;
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

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static com.g6.data24d_s2_g6_eksamensprojekt.controller.BrugerController.faaSession;

@Controller
public class LejeAftaleController {


    //Alle Repositories autowired igennem springFramework
    @Autowired
    AftaleRepository aftaleRepository;
    @Autowired
    BilRepository bilRepository;
    @Autowired
    KundeRepository kundeRepository;
    @Autowired
    NotationRepository notationRepository;

    // man bliver sendt her til fra knappen i headeren, som hedder Se Lejeaftaler.
    // den henter en liste med alle lejeaftaler og giver det til modelen og omdirigere hen til vislejeaftaler html siden.
    @GetMapping("/VisLejeAftaler")
    public String visLejeAftaler(HttpServletRequest request, Model model)
    {
        HttpSession session = BrugerController.faaSession(request, model,"data","skade,forretning");// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";

        // omdirigeret attributter fra html form->input (name,value)
        model.addAttribute("vognNummer", session.getAttribute("vognNummer"));
        model.addAttribute("periodeNummer", session.getAttribute("periodeNummer"));
        model.addAttribute("startDato", session.getAttribute("startDato"));
        model.addAttribute("slutDato", session.getAttribute("slutDato"));
        model.addAttribute("dato", session.getAttribute("dato"));
        model.addAttribute("kundeNavn", session.getAttribute("kundeNavn"));
        model.addAttribute("tjekkerStartDato", session.getAttribute("tjekkerStartDato"));
        model.addAttribute("tjekkerSlutDato", session.getAttribute("tjekkerSlutDato"));
        // sletter ekstra Attributter så session ikke bliver fyldt op med disse attributter
        session.removeAttribute("vognNummer");
        session.removeAttribute("periodeNummer");
        session.removeAttribute("startDato");
        session.removeAttribute("slutDato");
        session.removeAttribute("dato");
        session.removeAttribute("kundeNavn");
        session.removeAttribute("tjekkerStartDato");
        session.removeAttribute("tjekkerSlutDato");
        // den samlede afgift logik som samler notationPris i et lejeAftaleKort, som sker i visLejeAftaler.html
        double samletAfgift = 0;
        LocalDate now = LocalDate.now();

        List<LejeAftale> aftaler = aftaleRepository.hentLejeAftaler();

        for (LejeAftale aftale: aftaler)
        {
            if (aftale.getStartDato().isBefore(now))
            {
                aftale.setNotationPris(notationRepository.hentSumfor(aftale.getVognNummer()));
                if (aftale.getSlutDato() == null || aftale.getSlutDato().isAfter(now))
                {
                    samletAfgift += aftale.getNotationPris();
                }
            }
        }
        // sender til attributter fra html elementer med (name,value)
        model.addAttribute("samletIndkomst", samletAfgift); //
        model.addAttribute("lejeAftaler", aftaler);


        return "visLejeAftaler";
    }
    // man bliver sendt her til fra visLejeAftaler html siden.
    // den tager imod parametre og tilføjer dem til sessionen som skal bruges "/VisLejeAftaler" getmappingen
    // det gøres sådan så det ikke står i url'en i "/VisLejeAftaler"
    @GetMapping("/OmdirigerVisLejeAftaler")
    public String omdirigerVisLejeAftaler(HttpServletRequest request, Model model){
        HttpSession session = BrugerController.faaSession(request, model,"data","skade,forretning");// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";

        // Requester efter en parameter fra tidligere html form-> input ('name')
        String vognNummer = request.getParameter("vognNummer");
        Integer periodeNummer = Integer.parseInt(request.getParameter("periodeNummer"));
        String startDato =  request.getParameter("startDato");
        String slutDato = request.getParameter("slutDato");
        String dato = request.getParameter("dato");
        String kundeNavn = request.getParameter("kundeNavn");

        //Søg LejeAftale baseret på dato
        LocalDate startDato_ = null;
        LocalDate slutDato_ = null;
        LocalDate dato_ = null;

        if(startDato != null && !startDato.isEmpty())
            startDato_ = LocalDate.parse(startDato);
        if(slutDato != null && !slutDato.isEmpty())
            slutDato_ = LocalDate.parse(slutDato);
        if(dato != null && !dato.isEmpty())
            dato_ = LocalDate.parse(dato);

        // sætter session attributter, som muligvis bruges til omdirigering
        session.setAttribute("vognNummer", vognNummer);
        session.setAttribute("periodeNummer", periodeNummer);
        session.setAttribute("startDato", startDato_);
        session.setAttribute("slutDato", slutDato_);
        session.setAttribute("dato", dato_);
        session.setAttribute("kundeNavn", kundeNavn);
        session.setAttribute("tjekkerStartDato", request.getParameter("tjekkerStartDato"));
        session.setAttribute("tjekkerSlutDato", request.getParameter("tjekkerSlutDato"));

        return "redirect:/VisLejeAftaler";
    }


    @GetMapping("/VisLejeAftale")
    public String visLejeAftale(@RequestParam("aftaleId") int id, HttpServletRequest request, Model model) //@RequestParam("aftaleId") = Integer.parse(request.getParameter("aftaleId")),
    {
        HttpSession session = BrugerController.faaSession(request, model,"data","skade,forretning");// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";

        // at få fat på en lejeAftale basseret på aftaleId
        LejeAftale aftale = aftaleRepository.hentLejeAftale(id);
        Bil bil =bilRepository.hentBil(aftale.getVognNummer());
        aftale.setBil(bil);

        // sætter session attributter, som muligvis bruges til omdirigering
        session.setAttribute("lejeAftale", aftale);
        session.setAttribute("lejeAftale_Id",id);
        session.setAttribute("lejeAftale_Id",aftale);
        session.setAttribute("bil",bil);

        // sender til attributter fra html elementer med (name,value)
        model.addAttribute("lejeAftale", aftale);
        model.addAttribute("bil", bil);
        model.addAttribute("notationer", notationRepository.hentNotationer(aftale.getAftale_Id()));
        model.addAttribute("forlaeng_Maaneder", session.getAttribute("forlaeng_Maaneder"));


        //Finder ud af om en aftale ikke er startet i html vha. de her model.addAttribute
        LocalDate now = LocalDate.now();
        if (now.isBefore(aftale.getStartDato())) model.addAttribute("foerAftaleStart", true);
        else if (aftale.getSlutDato()!=null && now.isBefore(aftale.getSlutDato().plusDays(1))) model.addAttribute("foerAftaleSlut", true);

        return "visLejeAftale";
    }

    @GetMapping("SletLejeAftale")
    public String sletLejeAftale(HttpServletRequest request, Model model){
        HttpSession session = BrugerController.faaSession(request, model, "data");// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";

        //Sletter lejeAftale hvis den ikke er begyndt. (Logik i html til at få knappen frem)
        aftaleRepository.sletLejeAftale(((LejeAftale) session.getAttribute("lejeAftale")).getAftale_Id());
        return "redirect:VisLejeAftaler";
    }

    @GetMapping("/AfslutLejeAftale")
    public String afslutLejeAftale(HttpServletRequest request, Model model){
        HttpSession session = BrugerController.faaSession(request, model, "data","skade");// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";


        // Få fat i lejeAftale
        LejeAftale lejeAftale = (LejeAftale) session.getAttribute("lejeAftale");
        // sender til attributter fra html elementer med (name,value)
        model.addAttribute("lejeAftale",lejeAftale);

        boolean lejeAftaleErOvreBool = lejeAftale.erAfsluttet(); // tjekker om lejeAftale er en limited eller unlimited type
        Bil bil = (Bil) session.getAttribute("bil");
        if(lejeAftaleErOvreBool){
            // Notation skader bliver automasik klaret når der oprettes en ny pris på notation tilhørende til afsluttet lejeaftale
            // Så er billen blevet tilbageLeveret til BilAbonnement. Den afventer Køber, så den kan ændres til at være solgt.
            bilRepository.saetStatus(bil,Bil.Status.TILBAGELEVERET.name()); // ændring af bil status.

        }
        return "redirect:/VisLejeAftale?aftaleId="+lejeAftale.getAftale_Id();
    }


    @GetMapping("/UpdaterUnlimitedLejeAftale")
    public String updaterUnlimitedLejeAftale(HttpServletRequest request, Model model){
        HttpSession session = BrugerController.faaSession(request, model,  "data");// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";



        // Få fat i lejeAftale
        LejeAftale lejeAftale = (LejeAftale) session.getAttribute("lejeAftale");
        // sender til attributter fra html elementer med (name,value)
        model.addAttribute("lejeAftale",lejeAftale);
        //nummer af måneder der forlængedes
        int forlaeng_Maaneder = Integer.parseInt(request.getParameter("forlaeng_Maaneder"));
        aftaleRepository.forlaengLejeAftale(lejeAftale.getAftale_Id(),forlaeng_Maaneder);
        // sætter session attributter, som bruges til omdirigering
        session.setAttribute("forlaeng_Maaneder",forlaeng_Maaneder);

        return "redirect:/VisLejeAftale?aftaleId="+lejeAftale.getAftale_Id();
    }



    @GetMapping("/NyLejeAftale")
    public String nyLejeAftale(Model model, HttpServletRequest request){
        HttpSession session = faaSession(request, model, "data");// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";


        // bilkort dukker frem efter valg af bil.
        List<Bil> bilList = bilRepository.hentEksisteredeBiler();

        Bil bil = null;
        if(session.getAttribute("vognNummer") != null){
            bil = bilRepository.hentBil((String) session.getAttribute("vognNummer"));
        }

        // bilkort dukker frem efter valg af bil.
        String kunde_Navn = null;
        if ((String) session.getAttribute("kunde_Navn")!=null){
            kunde_Navn=(String) session.getAttribute("kunde_Navn");
        }
        // omdirigeret attributter fra html form->input (name,value)
        model.addAttribute("kunde_Navn",kunde_Navn);
        model.addAttribute("startDato", session.getAttribute("startDato"));
        model.addAttribute("slutDato", session.getAttribute("slutDato"));
        model.addAttribute("detaljer", session.getAttribute("detaljer"));
        model.addAttribute("bilList",bilList); // undtagen den her.
        model.addAttribute("bil", bil);

        // sætter session attributter, som muligvis bruges til omdirigering
        session.setAttribute("bil",bil);

        // sletter ekstra Attributter så session ikke bliver fyldt op med disse attributter
        session.removeAttribute("kunde_Navn");
        session.removeAttribute("vognNummer");
        session.removeAttribute("startDato");
        session.removeAttribute("slutDato");
        session.removeAttribute("detaljer");

        return "nyLejeAftale";
    }

    @GetMapping("/OmdirigerNyLejeAftale")
    public String omdirigerNyLejeAftale(Model model, HttpServletRequest request){
        HttpSession session = faaSession(request, model,  "data"); // sender til attributter fra html elementer med (name,value)
        if(session == null) return "redirect:/Logind";

        // Requester efter en parameter fra tidligere html form-> input ('name')
        int aftale_Id=0;
        String kunde_Navn;
        try {
            aftale_Id = Integer.parseInt(request.getParameter("aftale_Id"));
        }catch (NumberFormatException n){
            System.out.println("fangede ikke aftale Id");
        }
        boolean checkBoxBool = Boolean.parseBoolean(request.getParameter("checkBox"));
        System.out.println(checkBoxBool+"_test");
        kunde_Navn =request.getParameter("kunde_Navn");
        String vognNummer = request.getParameter("vognNummer"); //Tag fat i et navngivet input element fra tidligere html side
        String startDato = request.getParameter("startDato");
        String slutDato = request.getParameter("slutDato");
        Bil bil = (Bil) session.getAttribute("bil");

        // checkbox som har at gøre om hvordan slutDato bliver sat basseret om lejeAftalen er limited(5 måneder) Unlimited(3-36 måneder)
        if(checkBoxBool){//limited
            //omdiriger beskyttet
            Date _startDate_ = null;
            Date _slutDate_;
            try{
                _startDate_=Date.valueOf(startDato); // hvis der ingen startDato er.

            }catch (IllegalArgumentException e){
                _startDate_= Date.valueOf(LocalDate.of(1,1,1).toString()); // Bliver det til det her.
            }
            //LocalDate Regner frem i tiden baseret på type af lejeAftale
            LocalDate startDateAsLocalDate = _startDate_.toLocalDate();
            LocalDate slutDateAsLocalDate = startDateAsLocalDate.plusMonths(5);
            _slutDate_ = Date.valueOf(slutDateAsLocalDate);
            slutDato= _slutDate_.toString();
            // sætter session attributter, som muligvis bruges til omdirigering
            session.setAttribute("slutDato", _slutDate_.toString());
        }else { //Unlimited
            //omdiriger beskyttet

            Date startDate = null;
            Date slutDate;
            try{
                startDate=Date.valueOf(startDato);// hvis der ingen startDato er.

            }catch (IllegalArgumentException e){
                startDate= Date.valueOf(LocalDate.of(1,1,1).toString()); // Bliver det til det her.
            }
            //LocalDate Regner frem i tiden baseret på type af lejeAftale
            LocalDate startDateAsLocalDate = startDate.toLocalDate();
            LocalDate slutDateAsLocalDate = startDateAsLocalDate.plusMonths(3);
            slutDate = Date.valueOf(slutDateAsLocalDate);
            slutDato=slutDate.toString();
            // sætter session attributter, som muligvis bruges til omdirigering
            session.setAttribute("slutDato", slutDate.toString());

        }
        // sender til attributter fra html elementer med (name,value)
        String detaljer = request.getParameter("detaljer");
        boolean gemLejeAftaleBool = Boolean.parseBoolean(request.getParameter("submitKnap"));

        if (gemLejeAftaleBool){ // hvis bekræft lejeAftale er trykket
            // Samler alle tidligere parameter ind i en ny constructor ud fra relevant DataType
            LejeAftale lejeAftale = new LejeAftale(kunde_Navn,vognNummer,startDato,slutDato,detaljer);


            // Gemmer tidligere objekt vha. en metode i den her Repository
            aftaleRepository.gemLejeAftale(lejeAftale);

            // sætter bilernes status baseret på tidligere checkBoxBool
            if(checkBoxBool){

                bilRepository.saetStatus(bil,Bil.Status.LIMITED.name());
            }else {
                bilRepository.saetStatus(bil,Bil.Status.UNLIMITED.name());
            }
            // sletter ekstra Attributter så session ikke bliver fyldt op med disse attributter
            session.removeAttribute("bil");

            return "redirect:/";
        }

        // omdirigeret attributter fra html form->input (name,value)
        session.setAttribute("aftale_Id",aftale_Id);
        session.setAttribute("kunde_Navn",kunde_Navn);
        session.setAttribute("vognNummer",vognNummer);
        session.setAttribute("startDato",startDato);
        session.setAttribute("slutDato",slutDato);
        session.setAttribute("detaljer",detaljer);
        return "redirect:/NyLejeAftale";
    }
}
