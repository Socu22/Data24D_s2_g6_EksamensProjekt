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

    // tager imod navn og adgangskode og søger i databasen efter en medarbejder som har det og returnere det.
    public Bruger hentBruger(String navn, String adgangskode){
        Bruger bruger = null;
        String sql = "SELECT * FROM medArbejdere WHERE navn = '" + navn + "' AND adgangskode = '"+ adgangskode + "';";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(!list.isEmpty())
             bruger = new Bruger((int)list.get(0).get("medArbejder_id"),(String) list.get(0).get("navn"), (String) list.get(0).get("adgangskode"), (String)list.get(0).get("stilling"));
        return bruger;
    }
    // tager imod medarbejderId og søger i databasen efter en medarbejder som har det og returnere det.
    public Bruger hentBruger(int medarbejderId){
        Bruger bruger = null;
        String sql = "SELECT * FROM medArbejdere WHERE medArbejder_Id = " + medarbejderId +  ";";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(!list.isEmpty())
            bruger = new Bruger((int)list.get(0).get("medArbejder_id"),(String) list.get(0).get("navn"), (String) list.get(0).get("adgangskode"), (String)list.get(0).get("stilling"));
        return bruger;
    }
    // tager imod navn og søger i databasen efter en medarbejder som har det og returnere det.
    public Bruger hentBruger(String navn){
        Bruger bruger = null;
        String sql = "SELECT * FROM medArbejdere WHERE navn = '" + navn + "';";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(!list.isEmpty())
            bruger = new Bruger((int)list.get(0).get("medArbejder_id"),(String) list.get(0).get("navn"), (String) list.get(0).get("adgangskode"), (String)list.get(0).get("stilling"));
        return bruger;
    }
    // tager imod en string navn og tjekker i databasen om en bruger med dette navn allerede eksistere.
    // returnere true hvis der før og false hvis der ikke gør
    public boolean erBruger(String navn){
        String sql = "SELECT * FROM medArbejdere WHERE navn = '" + navn + "';";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(list.isEmpty())
            return false;
        else
            return true;
    }
    // tager imod navn, adgangskode og stilling og laver en ny medarbejder i databasen med givne variabler.
    public void gemBruger(String navn, String adgangskode, String stilling){
        String sql = "INSERT into medArbejdere(navn, adgangskode, stilling) VALUES ('"+ navn +"', '"+ adgangskode+"', '"+ stilling+"');";
        jdbcTemplate.update(sql);
    }
    // tager imod navn, adgangskode, stilling og medarbejderId og opdater medarbejderen med givent medarbejderId, med givne variabler.
    public void opdaterBruger(String navn, String adgangskode, String stilling, int medarbejderId){
        String sql = "UPDATE medArbejdere SET navn = '" + navn + "', adgangskode = '" + adgangskode + "', stilling = '" + stilling + "' WHERE medArbejder_Id = " + medarbejderId + ";";
        jdbcTemplate.update(sql);
    }
    // returnere en list med alle brugere i databasen
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
