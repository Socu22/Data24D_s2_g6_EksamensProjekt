package com.g6.data24d_s2_g6_eksamensprojekt.controller;

import com.g6.data24d_s2_g6_eksamensprojekt.model.BilType;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.BilTypeRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static com.g6.data24d_s2_g6_eksamensprojekt.controller.BrugerController.faaSession;

@Controller
public class BilTypeController {
    @Autowired
    BilTypeRepository bilTypeRepository;

    @GetMapping("nyBilType")
    public String getNyBilType(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model);
        if(session == null) return "redirect:/Logind";


        return "nyBilType";
    }
    @GetMapping("laverNyBilType")
    public String postLaverNyBilType(HttpServletRequest request, Model model){
        HttpSession session = faaSession(request, model);
        if(session == null) return "redirect:/Logind";

        int bilType_Id = bilTypeRepository.getNextId();
        String mærke = request.getParameter("mærke");
        String modelP = request.getParameter("model");
        String udstyrsniveau = request.getParameter("udstyrsniveau");
        double stålPris = Double.parseDouble(request.getParameter("stålPris"));
        double afgift = Double.parseDouble(request.getParameter("afgift"));
        double udledning_Co2 = Double.parseDouble(request.getParameter("udledning_Co2"));

        BilType bilType = new BilType(bilType_Id,mærke,modelP,udstyrsniveau,stålPris,afgift,udledning_Co2);

        bilTypeRepository.nyBilTypeLogik(bilType);
        return "redirect:/"; //todo: show all biltyper
    }
}
