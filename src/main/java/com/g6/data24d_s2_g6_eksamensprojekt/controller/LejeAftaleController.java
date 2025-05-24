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
        HttpSession session = BrugerController.faaSession(request, model,  new String[]{"data", "Skade", "forretnings"});
        if(session == null) return "redirect:/Logind";

        model.addAttribute("vognNummer", session.getAttribute("vognNummer"));
        model.addAttribute("periodeNummer", session.getAttribute("periodeNummer"));
        model.addAttribute("startDato", session.getAttribute("startDato"));
        model.addAttribute("slutDato", session.getAttribute("slutDato"));
        model.addAttribute("dato", session.getAttribute("dato"));
        model.addAttribute("kundeNavn", session.getAttribute("kundeNavn"));
        session.removeAttribute("vognNummer");
        session.removeAttribute("periodeNummer");
        session.removeAttribute("startDato");
        session.removeAttribute("slutDato");
        session.removeAttribute("dato");
        session.removeAttribute("kundeNavn");


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
        model.addAttribute("samletIndkomst", samletAfgift);

        model.addAttribute("lejeAftaler", aftaler);


        return "visLejeAftaler";
    }
    @GetMapping("/OmdirigerVisLejeAftaler")
    public String omdirigerVisLejeAftaler(HttpServletRequest request, Model model){
        HttpSession session = BrugerController.faaSession(request, model,  new String[]{"data", "Skade", "forretnings"});
        if(session == null) return "redirect:/Logind";

        String vognNummer = request.getParameter("vognNummer");
        Integer periodeNummer = Integer.parseInt(request.getParameter("periodeNummer"));
        String startDato =  request.getParameter("startDato");
        String slutDato = request.getParameter("slutDato");
        String dato = request.getParameter("dato");
        String kundeNavn = request.getParameter("kundeNavn");

        LocalDate startDato_ = null;
        LocalDate slutDato_ = null;
        LocalDate dato_ = null;

        if(startDato != null && !startDato.isEmpty()) {
            startDato_ = LocalDate.parse(startDato);
            System.out.println("startdato: " + startDato_.toString());
        }
        if(slutDato != null && !slutDato.isEmpty()) {
            slutDato_ = LocalDate.parse(slutDato);
            System.out.println("slutdato: " + slutDato_.toString());
        }
        if(dato != null && !dato.isEmpty()) {
            dato_ = LocalDate.parse(dato);
            System.out.println("dato: " + dato_.toString());
        }

        session.setAttribute("vognNummer", vognNummer);
        session.setAttribute("periodeNummer", periodeNummer);
        session.setAttribute("startDato", startDato_);
        session.setAttribute("slutDato", slutDato_);
        session.setAttribute("dato", dato_);
        session.setAttribute("kundeNavn", kundeNavn);


        return "redirect:/VisLejeAftaler";
    }


    @GetMapping("/VisLejeAftale")
    public String visLejeAftale(@RequestParam("aftaleId") int id, HttpServletRequest request, Model model)
    {
        HttpSession session = BrugerController.faaSession(request, model,  new String[]{"data", "Skade", "forretnings"});
        if(session == null) return "redirect:/Logind";

        LejeAftale aftale = aftaleRepository.hentLejeAftale(id);
        Bil bil =bilRepository.hentBil(aftale.getVognNummer());

        //aftale.setKunde(kundeRepository.hentKunde(aftale.getKunde_Id()));
        aftale.setBil(bil);

        aftale.setBil(bil);

        session.setAttribute("lejeAftale", aftale);
        session.setAttribute("lejeAftale_Id",id);

        model.addAttribute("lejeAftale", aftale);
        session.setAttribute("lejeAftale_Id",aftale);

        model.addAttribute("bil", bil);
        model.addAttribute("notationer", notationRepository.hentNotationer(aftale.getAftale_Id()));
        session.setAttribute("bil",bil);

        model.addAttribute("forlaeng_Maaneder", session.getAttribute("forlaeng_Maaneder"));


        //Finder ud af om en aftale ikke er startet i html vha. de her model.addAttribute
        LocalDate now = LocalDate.now();
        if (now.isBefore(aftale.getStartDato())) model.addAttribute("foerAftaleStart", true);
        else if (now.isBefore(aftale.getSlutDato().plusDays(1))) model.addAttribute("foerAftaleSlut", true);

        return "visLejeAftale";
    }
    @GetMapping("SletLejeAftale")
    public String sletLejeAftale(HttpServletRequest request, Model model){
        HttpSession session = BrugerController.faaSession(request, model,  new String[]{"data"});
        if(session == null) return "redirect:/Logind";

        aftaleRepository.sletLejeAftale((int)session.getAttribute("lejeAftale_Id"));
        return "redirect:VisLejeAftaler";
    }

    @GetMapping("/AfslutLejeAftale")
    public String afslutLejeAftale(HttpServletRequest request, Model model){
        HttpSession session = BrugerController.faaSession(request, model,  new String[]{"data","skade"});
        if(session == null) return "redirect:/Logind";


        // Få fat i lejeAftale
        LejeAftale lejeAftale = (LejeAftale) session.getAttribute("lejeAftale");
        model.addAttribute("lejeAftale",lejeAftale); // til html

        boolean lejeAftaleErOvreBool = lejeAftale.erAfsluttet(); // tjekker om lejeAftale er en limited eller unlimited type
        Bil bil = (Bil) session.getAttribute("bil");
        if(lejeAftaleErOvreBool){
            // Notation skader bliver automasik klaret når der oprettes en ny pris på notation tilhørende til afsluttet lejeaftale
            // Så er billen blevet tilbageLeveret til BilAbonnement. Den afventer Køber, så den kan ændres til at være solgt.
            bilRepository.saetStatus(bil,Bil.Status.TILBAGELEVERET.name()); // ændring af bil status.

        }
        return "redirect:/VisLejeAftale?aftaleId="+lejeAftale.getAftale_Id();
    }

    // todo: updater unlimted slutDato

//    @GetMapping("/UpdaterUnlimitedLejeAftale")
//    public String updaterUnlimitedLejeAftale(HttpServletRequest request, Model model){
//        HttpSession session = BrugerController.faaSession(request, model,  new String[]{"data"});
//        if(session == null) return "redirect:/Logind";
//
//
//
//        // Få fat i lejeAftale
//        LejeAftale lejeAftale = (LejeAftale) session.getAttribute("lejeAftale");
//        model.addAttribute("lejeAftale",lejeAftale); // til html
//        //nummer af måneder der forlængedes
//        int forlaeng_Maaneder = Integer.parseInt(request.getParameter("forlaeng_Maaneder"));
//
//        aftaleRepository.forlaengLejeAftale(lejeAftale.getAftale_Id(),forlaeng_Maaneder);
//
//
//
//
//
//
//        session.setAttribute("forlaeng_Maaneder",forlaeng_Maaneder);
//
//
//    }


    //logikken når man trykke på vælg bil.
    @GetMapping("/NyLejeAftale")
    public String nyLejeAftale(Model model, HttpServletRequest request){
        HttpSession session = faaSession(request, model,  new String[]{"data"});
        if(session == null) return "redirect:/Logind";

        List<Bil> bilList = bilRepository.hentEksisteredeBiler();
        //Bil bil = bilList.getFirst();
        Bil bil = null;
        if(session.getAttribute("vognNummer") != null){
            bil = bilRepository.hentBil((String) session.getAttribute("vognNummer"));
        }

//        List<Kunde> kundeList = kundeRepository.hentKunder();
//        //Kunde kunde =kundeList.getFirst();
//        Kunde tempKunde = null;
//        if(session.getAttribute("kunde_Id")!=null){
//            tempKunde = kundeRepository.hentKunde((Integer) session.getAttribute("kunde_Id"));
//        }
        String kunde_Navn = null;
        if ((String) session.getAttribute("kunde_Navn")!=null){
            kunde_Navn=(String) session.getAttribute("kunde_Navn");
        }
        model.addAttribute("kunde_Navn",kunde_Navn);
        model.addAttribute("startDato", session.getAttribute("startDato"));
        model.addAttribute("slutDato", session.getAttribute("slutDato"));
        model.addAttribute("detaljer", session.getAttribute("detaljer"));

        model.addAttribute("bilList",bilList);
        model.addAttribute("bil", bil);
        session.setAttribute("bil",bil);


       // model.addAttribute("kundeList",kundeList);
       // model.addAttribute("tempKunde", tempKunde);
       // session.removeAttribute("kunde_Id");
        session.removeAttribute("kunde_Navn");
        session.removeAttribute("vognNummer");
        session.removeAttribute("startDato");
        session.removeAttribute("slutDato");
        session.removeAttribute("detaljer");

        return "nyLejeAftale";
    }

    @GetMapping("/OmdirigerNyLejeAftale")
    public String omdirigerNyLejeAftale(Model model, HttpServletRequest request){
        HttpSession session = faaSession(request, model,  new String[]{"data"});
        if(session == null) return "redirect:/Logind";

        int aftale_Id=0;
        String kunde_Navn;
        try {
            aftale_Id = Integer.parseInt(request.getParameter("aftale_Id"));
        }catch (NumberFormatException n){
            System.out.println("fangede ikke aftale Id");
        }
        boolean checkBoxBool = Boolean.parseBoolean(request.getParameter("checkBox"));
        System.out.println(checkBoxBool+"_test");

        Bil bil = (Bil) session.getAttribute("bil");
        System.out.println(bil.getStatus()+"_test");
        //----
        kunde_Navn =request.getParameter("kunde_Navn");
        String vognNummer = request.getParameter("vognNummer"); //Tag fat i et navngivet input element fra tidligere html side
        String startDato = request.getParameter("startDato");

        String slutDato = request.getParameter("slutDato");
        if(checkBoxBool){
            Date startDate = Date.valueOf(startDato);
            Date slutDate;

            LocalDate startDateAsLocalDate = startDate.toLocalDate();
            LocalDate slutDateAsLocalDate = startDateAsLocalDate.plusMonths(5);
            slutDate = Date.valueOf(slutDateAsLocalDate);
            slutDato= slutDate.toString();
            session.setAttribute("slutDato", slutDate.toString());
            System.out.println(slutDate+"_Test");



        }else {
            Date startDate = Date.valueOf(startDato);
            Date slutDate;

            LocalDate startDateAsLocalDate = startDate.toLocalDate();
            LocalDate slutDateAsLocalDate = startDateAsLocalDate.plusMonths(3);
            slutDate = Date.valueOf(slutDateAsLocalDate);
            session.setAttribute("slutDato", slutDate.toString());
            System.out.println(slutDate+"_Test");
        }

        String detaljer = request.getParameter("detaljer");
        boolean gemLejeAftaleBool = Boolean.parseBoolean(request.getParameter("submitKnap"));

        if (gemLejeAftaleBool){
            System.out.println(slutDato+"INportant");

            LejeAftale lejeAftale = new LejeAftale(kunde_Navn,vognNummer,startDato,slutDato,detaljer);

            aftaleRepository.gemLejeAftale(lejeAftale);

            if(checkBoxBool){

                bilRepository.saetStatus(bil,Bil.Status.LIMITED.name());
            }else {
                bilRepository.saetStatus(bil,Bil.Status.UNLIMITED.name());
            }
            session.removeAttribute("bil");

            return "redirect:/";
        }
        session.setAttribute("aftale_Id",aftale_Id);
        session.setAttribute("kunde_Navn",kunde_Navn);
        session.setAttribute("vognNummer",vognNummer);
        session.setAttribute("startDato",startDato);
        //todo: set slutDato til at blive 5 eller 3 måneder frem i tiden

        session.setAttribute("slutDato",slutDato);


        session.setAttribute("detaljer",detaljer);
        return "redirect:/NyLejeAftale";
    }


    


}
