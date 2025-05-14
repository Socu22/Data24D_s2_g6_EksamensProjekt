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
        HttpSession session = faaSession(request, model);
        if(session == null) return "redirect:/Logind";

        List<Bil> biler = bilRepository.getBiler();
        if(session.getAttribute("biler")!=null){
            biler = (List<Bil>) session.getAttribute("biler");

        }
        model.addAttribute("biler",biler);

        return "visBiler";
    }
    @GetMapping("OmdirigerVisBiler")
    public String Omdirigerbil(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model);
        if(session == null) return "redirect:/Logind";


        List<Bil> bilList = new ArrayList<>(); // attribute som overskriver default biler vist;
        String vognNummer = request.getParameter("vognNummer");
        if (vognNummer!=null){
            if((vognNummer.length()==7)){
                bilList = bilRepository.findBilUdFraVognNummer(vognNummer);
            }else {
                if(vognNummer.length()==17){
                    bilList = bilRepository.findBilUdFraStelNummer(vognNummer); //  behandles som et stelNummer
                }
            }
        } else if (request.getParameter("lager_Id")!=null) {
            bilList= bilRepository.findBilUdFraLager_Id(Integer.parseInt(request.getParameter("lager_Id")));

        } else if (request.getParameter("maerke")!=null){
            bilList = bilRepository.findBilUdFraBilMaerke(request.getParameter("maerke"));
        }

        //todo: sum element i et set også ind til session bilList. (krav: lager_Id og maerke skal kunne aktiveres og samles. evt bruge true or false på alle og samle dem som er true sat noget i. vente med submit indtil knap trykkes. )
        session.setAttribute("biler",bilList); //gemmes is session.

        return "redirect:/VisBiler";
    }

    @GetMapping("/VisBil")
    public String visBil(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model);
        if(session == null) return "redirect:/Logind";

        String vognNummer = request.getParameter("vognNummer");
        Bil bil = bilRepository.tagFatIBil(vognNummer);
        session.setAttribute("bil",bil);
        model.addAttribute("bil", bil);

        return "visBil";
    }
    @GetMapping("/SletBil")
    public String sletBil(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model);
        if(session == null) return "redirect:/Logind";

        Bil bil = (Bil) session.getAttribute("bil");

        bilRepository.sletBil(bil.getVognNummer()); // todo: fix så en bil kan slettes



        return "redirect:visBiler";
    }
    @GetMapping("/NyBil")
    public String nyBil(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model);
        if(session == null) return "redirect:/Logind";


        List<BilType> bilTypeList = bilTypeRepository.getBilTyper();
        List<Lager> lagerList =lagerRepository.samleLagerIListeLogik();
        model.addAttribute("lagerList",lagerList);
        model.addAttribute("bilTypeList",bilTypeList);


        return "nyBil";
    }
    @GetMapping("/GemNyBil")
    public String gemNyBil(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model);
        if(session == null) return "redirect:/Logind";

        String vognNummer= (String) request.getParameter("vognNummer");
        String stelNummer= (String) request.getParameter("stelNummer");
        int bilType_Id= Integer.parseInt(request.getParameter("bilType_Id"));
        int lager_Id= Integer.parseInt(request.getParameter("lager_Id"));
        String status= (String) request.getParameter("status");

        Bil bil = new Bil(vognNummer,stelNummer,new BilType(bilType_Id),lager_Id,status);

        bilRepository.nyBilLogik(bil);
        return "redirect:/";
    }
}
