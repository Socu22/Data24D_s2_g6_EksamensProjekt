package com.g6.data24d_s2_g6_eksamensprojekt.controller;

import com.g6.data24d_s2_g6_eksamensprojekt.model.LejeAftale;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.AftaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LejeAftaleController {

    @Autowired
    AftaleRepository aftaleRepository;

    @GetMapping("nyLejeAftale")
    public String getNyLejeAftale(){

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
