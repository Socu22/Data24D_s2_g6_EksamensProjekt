package com.g6.data24d_s2_g6_eksamensprojekt.controller;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Bruger;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.BrugerRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class BrugerController {
    //Alle Repositories autowired igennem springFramework
    @Autowired
    BrugerRepository brugerRepository;

    // getmappingen for "/", bruges til at omdirigere, hvis de ikke er logget ind omdirigerer den til logind siden,
    // ellers så til visbiler siden som bruges som forside på vores hjemmeside
    @GetMapping("/")
    public String hjemmeSide(HttpServletRequest request, Model model) {
        HttpSession session = faaSession(request, model, "data", "skade", "forretning");// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";
        return "redirect:/VisBiler";
    }
    // fra alle andre getmappings, hvis der ikke er nogen session bliver de omdirigeret til denne getmapping ("/Logind")
    // returnerer logind html siden
    @GetMapping("/Logind")
    public String logInd(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if(session != null)
            return "redirect:/VisBiler";
        // Requester efter en parameter fra tidligere html form-> input ('name')
        String brugerIkkeFundet = request.getParameter("d");
        // sender til attributter fra html elementer med (name,value)
        model.addAttribute("brugerIkkeFundet", brugerIkkeFundet);

        return "logind";
    }
    // bliver sendt her til fra logind html siden, den tager imod 2 parametre, navn og adgangskode.
    // den kalder metoden hentbruger som henter brugeren i databasen som har det navn og adgangskode.
    // hvis brugeren eksistere og derved er i databasen, så laver den en session for at indikere at man er logget ind.
    // ellers omdirigere den tilbage til logind siden så man kan prøve at logge ind igen.
    @GetMapping("/OmdirigerLogind")
    public String omdirigerLogInd(HttpServletRequest request, Model model) {
        // Requester efter en parameter fra tidligere html form-> input ('name')
        String navn = request.getParameter("navn");
        String adgangskode = request.getParameter("adgangskode");
        if(navn != null && adgangskode != null){
            // Samler alle tidligere parameter ind i en ny constructor ud fra relevant DataType
            Bruger bruger = brugerRepository.hentBruger(navn, adgangskode);
            if(bruger != null){
                saetSession(request, bruger, model);
                return "redirect:/VisBiler";
            }
            return "redirect:/Logind?d=true";
        }
        return "redirect:/Logind";
    }
    // man bliver sendt her til fra knappen i headeren, som hedder log Ud.
    // stopper sessionen, hvilket er måden vi har valgt at indikere om man er logget ind eller ej.
    // derefter omdirigere den en til logind siden.
    @GetMapping("/LogUd")
    public String logUd(HttpServletRequest request, Model model) {
        request.getSession().invalidate(); // slå fra den her session
        return "redirect:/Logind";
    }
    // man bliver sendt her til fra knappen i headeren, som hedder Register Ny Bruger.
    // den får bare en masse variabler fra sessionen som bliver givet af omdirigerRegistrer metoden før den omdirigere hertil
    // den sætter dem ind i modelen og fjerner dem fra session igen og sender en hen til registrer html siden.
    @GetMapping("/Registrer")
    public String registrer(HttpServletRequest request, Model model) {
        HttpSession session = faaSession(request, model, "forretning");// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";

        // Requester efter en parameter fra tidligere html form-> input ('name')
        String medarbejderId = request.getParameter("medarbejderId");
        if(medarbejderId != null){
            //henter bruger
            Bruger bruger = brugerRepository.hentBruger(Integer.parseInt(medarbejderId));
            // sender til attributter fra html elementer med (name,value)
            model.addAttribute("medarbejderId", medarbejderId);
            model.addAttribute("navn", bruger.getNavn());
            model.addAttribute("adgangskode", bruger.getAdgangskode());
            model.addAttribute("stilling", bruger.getStilling());
        }
        else {
            // sender til attributter fra html elementer med (name,value)
            model.addAttribute("navn", session.getAttribute("navn"));
            model.addAttribute("adgangskode",session.getAttribute("adgangskode"));
            model.addAttribute("adgangskode2",session.getAttribute("adgangskode2"));
            model.addAttribute("stilling", session.getAttribute("stilling"));
        }


        // sender til attributter fra html elementer med (name,value)
        model.addAttribute("registrerBesked", session.getAttribute("registrerBesked"));
        model.addAttribute("medarbejderNavn", session.getAttribute("medarbejderNavn"));
        // sletter ekstra Attributter så session ikke bliver fyldt op med disse attributter
        session.removeAttribute("navn");
        session.removeAttribute("adgangskode");
        session.removeAttribute("adgangskode2");
        session.removeAttribute("stilling");
        session.removeAttribute("registrerBesked");
        session.removeAttribute("medarbejderNavn");

        return "registrer";
    }
    // man bliver sendt hertil fra registrer html siden
    // den får en masse parametre som den sætter i sessionen som bruges i "/Registrer" getmappingen
    // hvis den ikke får et medarbejder id så skal den lave en ny bruger.
    // den tjekker om adgangskoden og verificer adgangskode er det samme og om der allerede eksistere en bruger med det samme navn. ellers opretter den en ny medarbejder
    // hvis den får et medarbejderId så skal den ikke registrere en ny medarbejder, men istedet opdater den medarbejder hvis medarbejderId det er.
    @GetMapping("/OmdirigerRegistrer")
    public String omdirigerRegistrer(HttpServletRequest request, Model model) {
        HttpSession session = faaSession(request, model, "forretning");// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";

        // Requester efter en parameter fra tidligere html form-> input ('name')
        String medarbejderId = request.getParameter("medarbejderId");
        String navn = request.getParameter("navn");
        String adgangskode = request.getParameter("adgangskode");
        String adgangskode2 = request.getParameter("adgangskode2");
        String stilling = request.getParameter("stilling");
        String medarbejderNavn =  request.getParameter("medarbejderNavn");
        // sætter session attributter, som muligvis bruges til omdirigering
        session.setAttribute("stilling", stilling);
        session.setAttribute("navn", navn);
        session.setAttribute("adgangskode", adgangskode);
        session.setAttribute("adgangskode2", adgangskode2);
        session.setAttribute("medarbejderNavn", medarbejderNavn);

        //login sted logik og beskeder
        if(medarbejderId == null){

            if(!adgangskode.equals(adgangskode2)){
                session.setAttribute("registrerBesked", "Verificer Adgangskode er ikke det samme som Adgangskode");
            }
            else if(brugerRepository.erBruger(navn)){
                session.setAttribute("registrerBesked", "Bruger med samme navn eksisterer allerede");
            }
            else{
                session.setAttribute("registrerBesked", "Ny bruger lavet");
                brugerRepository.gemBruger(navn, adgangskode, stilling);
            }
        }
       else {
            Bruger bruger = brugerRepository.hentBruger(navn);

            if(bruger != null && bruger.getMedArbejder_Id() != Integer.parseInt(medarbejderId)){
                session.setAttribute("registrerBesked", "Bruger med samme navn eksisterer allerede");

            }
            else{
                session.setAttribute("registrerBesked", "Bruger opdateret");
                brugerRepository.opdaterBruger(navn, adgangskode, stilling, Integer.parseInt(medarbejderId));
            }

            return "redirect:/Registrer?medarbejderId=" + medarbejderId;
       }

        return "redirect:/Registrer";
    }
    // man bliver sendt her til fra knappen på registrer html siden, som hedder Rediger Eksisterende Bruger.
    // den giver en liste med alle medarbejdere og sender en hen til soegBruger html siden.
    @GetMapping("/SoegBruger")
    public String soegBruger(HttpServletRequest request, Model model) {
        HttpSession session = faaSession(request, model, "forretning");// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";

        // sender til attributter fra html elementer med (name,value)
        model.addAttribute("medarbejdere", brugerRepository.hentBrugere());
        model.addAttribute("medarbejderNavn", session.getAttribute("medarbejderNavn"));
        // sletter ekstra Attributter så session ikke bliver fyldt op med disse attributter
        session.removeAttribute("medarbejderNavn");

        return "soegBruger";
    }
    // man bliver sendt her til fra soegBruger html siden.
    // den får parameteret medarbejderNavn som er den der skal bruges til at søges efter og omdirigere til "/SoegBruger"
    @GetMapping("/OmdirigerSoegBruger")
    public String omdirigerSoegBruger(HttpServletRequest request, Model model) {
        HttpSession session = faaSession(request, model, "forretning");// Hvem der har Rettighed til at bruge metoden.
        if(session == null) return "redirect:/Logind";
        // sætter session attributter, som muligvis bruges til omdirigering
        session.setAttribute("medarbejderNavn", request.getParameter("medarbejderNavn"));

        return "redirect:/SoegBruger";
    }

    @PostMapping("/saetValuta")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    public void saetValuta(HttpServletRequest request, Model model)
    {
        HttpSession session = faaSession(request, model);
        assert session != null;
        // sætter session attributter, som bruges til at se om det er dkk eller euro der skal vises
        session.setAttribute("valuta", request.getParameter("valutaCheck"));
    }
    // metoden kaldes efter man er logget ind, den laver en ny session og giver den en bruger,
    // som er den bruger der er logget ind
    private void saetSession(HttpServletRequest request, Bruger bruger, Model model) {
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(1200);
        // sætter session attributter, som bruges til at se om der er en bruger i session
        session.setAttribute("aktivBruger", bruger);
        // sender til attributter fra html elementer med (name,value)
        model.addAttribute("aktivBruger", bruger);
    }
    // metoden kaldes i starten af alle getmappings,
    // den tager imod sessionen og tilføjer valuta og bruger til modelen
    static public HttpSession faaSession(HttpServletRequest request, Model model, String... tilladteStillinger){
        HttpSession session = request.getSession(false);
        if(session != null){
            Bruger bruger = (Bruger) session.getAttribute("aktivBruger");
            if(!bruger.erStilling(tilladteStillinger)) return null;
            // sender til attributter fra html elementer med (name,value)
            model.addAttribute("aktivBruger",bruger);
            model.addAttribute("valuta", session.getAttribute("valuta"));
        }

        return session;
    }
}
