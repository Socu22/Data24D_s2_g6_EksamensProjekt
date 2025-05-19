package com.g6.data24d_s2_g6_eksamensprojekt.controller;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Bil;
import com.g6.data24d_s2_g6_eksamensprojekt.model.BilType;
import com.g6.data24d_s2_g6_eksamensprojekt.model.Lager;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.BilRepository;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.BilTypeRepository;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.LagerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

import static com.g6.data24d_s2_g6_eksamensprojekt.controller.BrugerController.faaSession;

@Controller
public class BilController {

    @Autowired
    BilRepository bilRepository;
    @Autowired
    BilTypeRepository bilTypeRepository;

    @Autowired
    LagerRepository lagerRepository;


    @GetMapping("/VisBiler")
    public String visBiler(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model,  new String[]{"data", "Skade", "forretnings"});
        if(session == null) return "redirect:/Logind";

        List<Bil> biler = bilRepository.hentEksisteredeBiler();
        List<Lager> lagerList = lagerRepository.hentLager();
        List<BilType> bilTypeList = bilTypeRepository.hentBilTyper();
        List<String> statusList = bilRepository.hentStatusser().subList(0,3);

        if(session.getAttribute("biler")!=null){
            biler = (List<Bil>) session.getAttribute("biler");

        }
        model.addAttribute("biler",biler);
        model.addAttribute("lagerList",lagerList);
        model.addAttribute("bilTypeList",bilTypeList);
        model.addAttribute("statusList",statusList);

        return "visBiler";
    }
    @GetMapping("OmdirigerVisBiler")
    public String Omdirigerbil(HttpServletRequest request, Model model) {
        HttpSession session = faaSession(request, model,  new String[]{"data", "Skade", "forretnings"});
        if (session == null) return "redirect:/Logind";

        String vognNummer = request.getParameter("vognNummer");
        String lager_Id = request.getParameter("lager_Id");
        String maerke = request.getParameter("maerke");
        String status = request.getParameter("status");

        List<Bil> bilList = new ArrayList<>();

        // Hvis vognNummer eller stelNummer er givet
        if (vognNummer != null && !vognNummer.isEmpty()) {
            if (vognNummer.length() == 7) { // her tjekkes der om det er et vognNummer.
                bilList = bilRepository.hentBilerUdFraVognNummer(vognNummer);
            } else if (vognNummer.length() == 17) { // her tjekkes der om det er et stelNummer.
                String stelNummer = vognNummer; //Sikker på at værdien i vognNummer rent faktisk er et stelNummer
                bilList = bilRepository.hentBilerUdFraStelNummer(stelNummer);
            }
        } else if(maerke != null || lager_Id != null||status!=null) {
            // hvis du vælger lager, mærke eller begge virke den her metode
            bilList = bilRepository.hentBilerbilerUdFraLager_idEllerOgMaerkeEllerOgStatus(lager_Id,maerke,status);
        }

        session.setAttribute("biler", bilList);
        return "redirect:/VisBiler";
    }
    @GetMapping("VisBilerReset")
    public String omdirigerVisBilerReset(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model,  new String[]{"data", "Skade", "forretnings"});
        if (session == null) return "redirect:/Logind";

        session.removeAttribute("biler");

        return "redirect:/VisBiler";
    }


    @GetMapping("/VisBil")
    public String visBil(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model,  new String[]{"data", "Skade", "forretnings"});
        if(session == null) return "redirect:/Logind";

        String vognNummer = request.getParameter("vognNummer");
        session.setAttribute("vognNummer",vognNummer);


        Bil bil = bilRepository.hentBil(vognNummer);
        session.setAttribute("bil",bil);
        model.addAttribute("bil", bil);

        return "visBil";
    }

    @GetMapping("/NyBil")
    public String nyBil(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model, new String[]{"forretnings"});
        if(session == null) return "redirect:/Logind";


        List<BilType> bilTypeList = bilTypeRepository.hentBilTyper();
        List<Lager> lagerList =lagerRepository.hentLager();
        model.addAttribute("lagerList",lagerList);
        model.addAttribute("bilTypeList",bilTypeList);


        return "nyBil";
    }
    @GetMapping("/GemNyBil")
    public String gemNyBil(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model,  new String[]{"forretnings"});
        if(session == null) return "redirect:/Logind";

        String vognNummer= (String) request.getParameter("vognNummer");
        String stelNummer= (String) request.getParameter("stelNummer");
        int bilType_Id= Integer.parseInt(request.getParameter("bilType_Id"));
        int lager_Id= Integer.parseInt(request.getParameter("lager_Id"));
        String status= (String) request.getParameter("status");

        Bil bil = new Bil(vognNummer,stelNummer,new BilType(bilType_Id),lager_Id,status);

        bilRepository.gemBil(bil);
        return "redirect:/";
    }
    @GetMapping("/SletBil")
    public String sletBil(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model,  new String[]{"forretnings"});
        if(session == null) return "redirect:/Logind";

        String vognNummer= (String) session.getAttribute("vognNummer");
        System.out.println(vognNummer+"_2");



        boolean bool = bilRepository.sletBil(vognNummer);
       if (!bool){
           System.out.println("Fangede ikke en bil(sletBil)");
       }
        return "redirect:/";
    }

}
