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

    @Autowired
    BrugerRepository brugerRepository;

    @GetMapping("/")
    public String hjemmeSide(HttpServletRequest request, Model model) {
        HttpSession session = faaSession(request, model, "data", "skade", "forretning");
        if(session == null) return "redirect:/Logind";
        return "index";
    }
    @GetMapping("/Logind")
    public String logInd(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if(session != null)
            return "redirect:/VisBiler";

        String brugerIkkeFundet = request.getParameter("d");
        model.addAttribute("brugerIkkeFundet", brugerIkkeFundet);

        return "logind";
    }

    @GetMapping("/OmdirigerLogind")
    public String omdirigerLogInd(HttpServletRequest request, Model model) {
        String navn = request.getParameter("navn");
        String adgangskode = request.getParameter("adgangskode");
        if(navn != null && adgangskode != null){
            Bruger bruger = brugerRepository.hentBruger(navn, adgangskode);
            if(bruger != null){
                saetSession(request, bruger, model);
                return "redirect:/VisBiler";
            }
            return "redirect:/Logind?d=true";
        }
        return "redirect:/Logind";
    }

    @GetMapping("/LogUd")
    public String logUd(HttpServletRequest request, Model model) {
        request.getSession().invalidate();
        return "redirect:/Logind";
    }

    @GetMapping("/Registrer")
    public String registrer(HttpServletRequest request, Model model) {
        HttpSession session = faaSession(request, model, "forretning");
        if(session == null) return "redirect:/Logind";


        String medarbejderId = request.getParameter("medarbejderId");
        if(medarbejderId != null){
            model.addAttribute("medarbejderId", medarbejderId);
            Bruger bruger = brugerRepository.hentBruger(Integer.parseInt(medarbejderId));

            model.addAttribute("navn", bruger.getNavn());
            model.addAttribute("adgangskode", bruger.getAdgangskode());
            model.addAttribute("stilling", bruger.getStilling());
        }
        else {
            model.addAttribute("navn", session.getAttribute("navn"));
            model.addAttribute("adgangskode",session.getAttribute("adgangskode"));
            model.addAttribute("adgangskode2",session.getAttribute("adgangskode2"));
            model.addAttribute("stilling", session.getAttribute("stilling"));
        }
        // model.addAttribute("stilling", session.getAttribute("stilling"));

       /* String stilling = (String) session.getAttribute("stilling");
        if(stilling == null)
            stilling = "Dataregistrering";*/



        model.addAttribute("registrerBesked", session.getAttribute("registrerBesked"));
        model.addAttribute("medarbejderNavn", session.getAttribute("medarbejderNavn"));

        session.removeAttribute("navn");
        session.removeAttribute("adgangskode");
        session.removeAttribute("adgangskode2");
        session.removeAttribute("stilling");
        session.removeAttribute("registrerBesked");
        session.removeAttribute("medarbejderNavn");

        return "registrer";
    }

    @GetMapping("/OmdirigerRegistrer")
    public String omdirigerRegistrer(HttpServletRequest request, Model model) {
        HttpSession session = faaSession(request, model, "forretning");
        if(session == null) return "redirect:/Logind";

        String medarbejderId = request.getParameter("medarbejderId");

        String navn = request.getParameter("navn");
        String adgangskode = request.getParameter("adgangskode");
        String adgangskode2 = request.getParameter("adgangskode2");
        String stilling = request.getParameter("stilling");
        String medarbejderNavn =  request.getParameter("medarbejderNavn");
        session.setAttribute("stilling", stilling);
        session.setAttribute("navn", navn);
        session.setAttribute("adgangskode", adgangskode);
        session.setAttribute("adgangskode2", adgangskode2);
        session.setAttribute("medarbejderNavn", medarbejderNavn);

        //if(stilling.toCharArray()[stilling.length() - 1] == '_'){
        //stilling = stilling.replace("_", "");
        if(navn!= null && adgangskode!= null && adgangskode2 != null && stilling!= null){

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
        if(medarbejderId != null){
            Bruger bruger = brugerRepository.hentBruger(navn);

            if(bruger != null && bruger.getMedArbejder_Id() != Integer.parseInt(medarbejderId)){
                session.setAttribute("registrerBesked", "Bruger med samme navn eksisterer allerede");
                return "redirect:/Registrer?medarbejderId=" + medarbejderId;
            }
            else{
                session.setAttribute("registrerBesked", "Bruger opdateret");
                brugerRepository.opdaterBruger(navn, adgangskode, stilling, Integer.parseInt(medarbejderId));
            }


        }
           /* else {
                if(navn.isEmpty()){
                    session.setAttribute("manglendeInput", 0);
                }
                else if(adgangskode.isEmpty()){
                    session.setAttribute("manglendeInput", 1);
                }
                else if(adgangskode2.isEmpty()){
                    session.setAttribute("manglendeInput", 2);
                }
            }*/
       /* }
        else {

        }*/


        return "redirect:/Registrer";
    }

    @GetMapping("/SoegBruger")
    public String soegBruger(HttpServletRequest request, Model model) {
        HttpSession session = faaSession(request, model, "forretning");
        if(session == null) return "redirect:/Logind";

        model.addAttribute("medarbejdere", brugerRepository.hentBrugere());

        model.addAttribute("medarbejderNavn", session.getAttribute("medarbejderNavn"));
        session.removeAttribute("medarbejderNavn");

        return "soegBruger";
    }

    @GetMapping("/OmdirigerSoegBruger")
    public String omdirigerSoegBruger(HttpServletRequest request, Model model) {
        HttpSession session = faaSession(request, model, "forretning");
        if(session == null) return "redirect:/Logind";

        session.setAttribute("medarbejderNavn", request.getParameter("medarbejderNavn"));

        return "redirect:/SoegBruger";
    }

    /*@GetMapping("/RedigerBruger")
    public String redigerBruger(HttpServletRequest request, Model model) {
        HttpSession session = faaSession(request, model, new String[]{"forretnings"});
        if(session == null) return "redirect:/Logind";

        return "index";
    }*/

    @GetMapping("/OmdirigerRedigerBruger")
    public String omdirigerRedigerBruger(HttpServletRequest request, Model model) {
        HttpSession session = faaSession(request, model, "forretning");
        if(session == null) return "redirect:/Logind";

        return "redirect:/Registrer";
    }

    @PostMapping("/saetValuta")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    public void saetValuta(HttpServletRequest request, Model model)
    {
        HttpSession session = faaSession(request, model);
        assert session != null;
        session.setAttribute("valuta", request.getParameter("valutaCheck"));
    }

    private void saetSession(HttpServletRequest request, Bruger bruger, Model model) {
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(1200);
        session.setAttribute("aktivBruger", bruger);
        model.addAttribute("aktivBruger", bruger);
    }

    static public HttpSession faaSession(HttpServletRequest request, Model model, String... tilladteStillinger){
        HttpSession session = request.getSession(false);
        if(session != null){
            Bruger bruger = (Bruger) session.getAttribute("aktivBruger");
            if(!bruger.erStilling(tilladteStillinger)) return null;

            model.addAttribute("aktivBruger",bruger);
            model.addAttribute("valuta", session.getAttribute("valuta"));
        }

        return session;
    }
}
