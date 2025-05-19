package com.g6.data24d_s2_g6_eksamensprojekt.controller;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Bruger;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.BrugerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BrugerController {

    @Autowired
    BrugerRepository brugerRepository;

    @GetMapping("/")
    public String hjemmeSide(HttpServletRequest request, Model model) {
        HttpSession session = faaSession(request, model,  new String[]{"data", "Skade", "forretnings"});
        if(session == null) return "redirect:/Logind";
        return "index";
    }
    @GetMapping("/Logind")
    public String logInd(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if(session != null)
            return "redirect:/VisLejeAftaler";

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
                //return "redirect:/";
                return "redirect:/VisLejeAftaler";
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
        HttpSession session = faaSession(request, model,  new String[]{"forretnings"});
        if(session == null) return "redirect:/Logind";

        model.addAttribute("registrerBesked", session.getAttribute("registrerBesked"));
        session.setAttribute("registrerBesked", "");
       // model.addAttribute("stilling", session.getAttribute("stilling"));

        String stilling = (String) session.getAttribute("stilling");
        if(stilling == null)
            stilling = "Dataregistrering";

        model.addAttribute("navn", session.getAttribute("navn"));
        model.addAttribute("adgangskode",session.getAttribute("adgangskode"));
        model.addAttribute("adgangskode2",session.getAttribute("adgangskode2"));
        model.addAttribute("stilling", stilling);
        model.addAttribute("manglendeInput", session.getAttribute("manglendeInput"));


        session.setAttribute("navn", "");
        session.setAttribute("adgangskode", "");
        session.setAttribute("adgangskode2", "");
        session.setAttribute("manglendeInput", null);


        return "registrer";
    }
    @GetMapping("/OmdirigerRegistrer")
    public String omdirigerRegistrer(HttpServletRequest request, Model model) {
        HttpSession session = faaSession(request, model, new String[]{"forretnings"});
        if(session == null) return "redirect:/Logind";


        String navn = request.getParameter("navn");
        String adgangskode = request.getParameter("adgangskode");
        String adgangskode2 = request.getParameter("adgangskode2");
        String stilling = request.getParameter("stilling");

        if(stilling.toCharArray()[stilling.length() - 1] == '_'){
            stilling = stilling.replace("_", "");
            if(!navn.isEmpty() && !adgangskode.isEmpty() && !adgangskode2.isEmpty() && !stilling.isEmpty()){

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
                if(navn.isEmpty()){
                    session.setAttribute("manglendeInput", 0);
                }
                else if(adgangskode.isEmpty()){
                    session.setAttribute("manglendeInput", 1);
                }
                else if(adgangskode2.isEmpty()){
                    session.setAttribute("manglendeInput", 2);
                }
            }
        }
        else {

        }
        session.setAttribute("stilling", stilling);
        session.setAttribute("navn", navn);
        session.setAttribute("adgangskode", adgangskode);
        session.setAttribute("adgangskode2", adgangskode2);
        return "redirect:/Registrer";
    }

    private void saetSession(HttpServletRequest request, Bruger bruger, Model model) {
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(1200);
        session.setAttribute("aktivBruger", bruger);
        model.addAttribute("aktivBruger", bruger);
    }
    static public HttpSession faaSession(HttpServletRequest request, Model model, String[] stillinger){
        HttpSession session = request.getSession(false);
        if(session != null){
            Bruger bruger = (Bruger) session.getAttribute("aktivBruger");
            boolean hasAccess = false;
            for (String stilling : stillinger) {
                String s = stilling.toLowerCase().toCharArray()[0] + "";
                //System.out.println(bruger.getStilling().toLowerCase().toCharArray()[0] + ", " + stilling.toLowerCase().toCharArray()[0] + " , " +(bruger.getStilling().toLowerCase().toCharArray()[0] == stilling.toLowerCase().toCharArray()[0]) );
                if(bruger.getStilling().toLowerCase().toCharArray()[0] == stilling.toLowerCase().toCharArray()[0])
                    hasAccess = true;
            }
            if(!hasAccess)
                return null;
            model.addAttribute("aktivBruger",bruger);
        }


        return session;
    }
}
