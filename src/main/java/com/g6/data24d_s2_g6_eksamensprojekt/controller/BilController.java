package com.g6.data24d_s2_g6_eksamensprojekt.controller;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Bil;
import com.g6.data24d_s2_g6_eksamensprojekt.model.BilType;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.BilRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static com.g6.data24d_s2_g6_eksamensprojekt.controller.BrugerController.faaSession;

@Controller
public class BilController {

    @Autowired
    BilRepository bilRepository;


    @GetMapping("visbiler")
    public String getVisBiler(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model);
        if(session == null) return "redirect:/Logind";

        List<Bil> bilList = bilRepository.getBiler();
        model.addAttribute("bilList",bilList);

        return "visBiler";
    }
    @GetMapping("nyBil")
    public String getNyBil(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model);
        if(session == null) return "redirect:/Logind";


        return "nyBil";
    }
    @GetMapping("laverNyBil")
    public String postLaverNyBil (HttpServletRequest request, Model model){
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
