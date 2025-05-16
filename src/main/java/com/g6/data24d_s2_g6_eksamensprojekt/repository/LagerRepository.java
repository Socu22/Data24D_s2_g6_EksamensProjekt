package com.g6.data24d_s2_g6_eksamensprojekt.repository;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Lager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LagerRepository {


    @Autowired
    JdbcTemplate jdbcTemplate;

    //Dette er lavet til at gøre processen med at konvertere MySQL til reelle objekter af Lager klassen
    private final RowMapper<Lager> rowMapper = (rs, rowNum) -> {
        Lager lager = new Lager();
        lager.setLager_Id(rs.getInt("lager_Id"));
        lager.setNavn(rs.getString("navn"));
        lager.setAdresse(rs.getString("adresse"));
        return lager;
    };
        //Laver et nyt lager og opdaterer databasen
    public void gemLager(Lager nytLager){
        String sql = "INSERT into lager (navn, adresse) values (?,?)";
        jdbcTemplate.update(sql, nytLager.getNavn(),nytLager.getAdresse());
    }

    //Samler alle lager objekter sammen i en liste. ?brugbart?
   public List<Lager> hentLager(){
        List<Lager> lagerList = jdbcTemplate.query("select * from lager",rowMapper);
        System.out.println(lagerList);
        return lagerList;
   }

   //tager fat i lager ud fra et navn
   public Lager hentLager(String navn){
       List<Lager> lagerList= jdbcTemplate.query("select * from lager where navn=?",rowMapper,navn);
        if (lagerList.size()==1){
            return lagerList.getFirst();
        }
        return null;
   }

   //sletter hvis der kun er en lager med samme navn
    public boolean sletLager(String navn){
        List <Lager> count= jdbcTemplate.query("select * from lager where navn=?",rowMapper,navn);
        int value;
        if (count.size()==1){
            value= jdbcTemplate.update("delete from lager where navn=?",navn);
            return true;
        }
        else{
            return false;
        }
    }




}
