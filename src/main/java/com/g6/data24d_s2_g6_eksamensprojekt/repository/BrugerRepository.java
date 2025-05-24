package com.g6.data24d_s2_g6_eksamensprojekt.repository;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Bruger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class BrugerRepository {
@Autowired
private JdbcTemplate jdbcTemplate;

    public Bruger hentBruger(String navn, String adgangskode){
        Bruger bruger = null;
        String sql = "SELECT * FROM medArbejdere WHERE navn = '" + navn + "' AND adgangskode = '"+ adgangskode + "';";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(!list.isEmpty())
             bruger = new Bruger((int)list.get(0).get("medArbejder_id"),(String) list.get(0).get("navn"), (String) list.get(0).get("adgangskode"), (String)list.get(0).get("stilling"));
        return bruger;
    }
    public Bruger hentBruger(int medarbejderId){
        Bruger bruger = null;
        String sql = "SELECT * FROM medArbejdere WHERE medArbejder_Id = " + medarbejderId +  ";";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(!list.isEmpty())
            bruger = new Bruger((int)list.get(0).get("medArbejder_id"),(String) list.get(0).get("navn"), (String) list.get(0).get("adgangskode"), (String)list.get(0).get("stilling"));
        return bruger;
    }
    public Bruger hentBruger(String navn){
        Bruger bruger = null;
        String sql = "SELECT * FROM medArbejdere WHERE navn = '" + navn + "';";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(!list.isEmpty())
            bruger = new Bruger((int)list.get(0).get("medArbejder_id"),(String) list.get(0).get("navn"), (String) list.get(0).get("adgangskode"), (String)list.get(0).get("stilling"));
        return bruger;
    }
    public boolean erBruger(String navn){
        String sql = "SELECT * FROM medArbejdere WHERE navn = '" + navn + "';";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(list.isEmpty())
            return false;
        else
            return true;
    }
    public void gemBruger(String navn, String adgangskode, String stilling){
        String sql = "INSERT into medArbejdere(navn, adgangskode, stilling) VALUES ('"+ navn +"', '"+ adgangskode+"', '"+ stilling+"');";
        jdbcTemplate.update(sql);
    }
    public void opdaterBruger(String navn, String adgangskode, String stilling, int medarbejderId){
        String sql = "UPDATE medArbejdere SET navn = '" + navn + "', adgangskode = '" + adgangskode + "', stilling = '" + stilling + "' WHERE medArbejder_Id = " + medarbejderId + ";";
        jdbcTemplate.update(sql);
    }
    public List<Bruger> hentBrugere(){
        String sql = "SELECT * FROM medArbejdere;";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

        List<Bruger> brugere = new ArrayList<>();
        for(Map<String, Object> map : list){
           brugere.add(new Bruger((int)map.get("medArbejder_id"),(String) map.get("navn"), (String) map.get("adgangskode"), (String)map.get("stilling")));
        }
        return brugere;
    }
}
