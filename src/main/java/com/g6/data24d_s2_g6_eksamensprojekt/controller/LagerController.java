package com.g6.data24d_s2_g6_eksamensprojekt.controller;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Lager;
import com.g6.data24d_s2_g6_eksamensprojekt.repository.LagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LagerController {


    @Autowired
    LagerRepository lagerRepository;

    @GetMapping("/nytLager")
    public String getNytLager(){
        //bruger logik, hvem kan lave et nyt lager?

        return "nytLager";
    }
    @GetMapping("laverNytLager")
    public String postNytLager(@RequestParam("navn") String navn,
                               @RequestParam("adresse") String adresse){
        Lager nytLager = new Lager(navn,adresse);
        lagerRepository.nytLagerLogik(nytLager);
        return "redirect:/"; //skal ændres
    }


}
