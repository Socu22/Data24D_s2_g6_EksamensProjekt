package com.g6.data24d_s2_g6_eksamensprojekt.repository;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Bruger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class BrugerRepository {
@Autowired
private JdbcTemplate jdbcTemplate;

    public Bruger faaBruger(String navn, String adgangskode){
        Bruger bruger = null;
        String sql = "SELECT * FROM medArbejdere WHERE navn = '" + navn + "' AND adgangskode = '"+ adgangskode + "';";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(!list.isEmpty())
             bruger = new Bruger((int)list.get(0).get("medArbejder_id"),(String) list.get(0).get("navn"), (String) list.get(0).get("adgangskode"), (String)list.get(0).get("stilling"));
        return bruger;
    }
    public boolean brugerEksisterer(String navn){
        String sql = "SELECT * FROM medArbejdere WHERE navn = '" + navn + "';";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(list.isEmpty())
            return false;
        else
            return true;
    }
    public void lavBruger(String navn, String adgangskode, String stilling){
        String sql = "INSERT into medArbejdere(navn, adgangskode, stilling) VALUES ('"+ navn +"', '"+ adgangskode+"', '"+ stilling+"');";
        jdbcTemplate.update(sql);
    }
}
