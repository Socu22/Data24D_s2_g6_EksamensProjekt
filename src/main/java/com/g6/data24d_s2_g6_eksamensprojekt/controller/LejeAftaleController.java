package com.g6.data24d_s2_g6_eksamensprojekt.controller;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Bil;
import com.g6.data24d_s2_g6_eksamensprojekt.model.LejeAftale;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.AftaleRepository;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.BilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LejeAftaleController {

    @Autowired
    AftaleRepository aftaleRepository;
    @Autowired
    BilRepository bilRepository;

    @GetMapping("nyLejeAftale")
    public String getNyLejeAftale(Model model){
        List<Bil> bilList = bilRepository.getBiler();
        Bil bil = bilList.getFirst();
        model.addAttribute("bilList",bilList);
        model.addAttribute("kunde", bil);
        model.addAttribute("bil", bil);

        return "nyLejeAftale";
    }
    @GetMapping("laverNyLejeAftale")
    public String postNyLejeAftale(@RequestParam("kunde_Id") int kunde_Id,
                                   @RequestParam("vognNummer") String vognNummer,
                                   @RequestParam("startDato") String startDato,
                                   @RequestParam("startDato") String slutDato,
                                   @RequestParam("detaljer") String detaljer){

        LejeAftale lejeAftale = new LejeAftale(kunde_Id,vognNummer,startDato,slutDato,detaljer);

        aftaleRepository.nyAftaleLogik(lejeAftale);

        return "redirect:/";
    }

}
