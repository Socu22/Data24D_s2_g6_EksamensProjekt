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

    public void nytLagerLogik(Lager nytLager){
        String sql = "INSERT into lager (navn, adresse) values (?,?)";
        jdbcTemplate.update(sql, nytLager.getNavn(),nytLager.getAdresse());
    }
   public List<Lager> samleLagerIListeLogik(){
        List<Lager> lagerList = jdbcTemplate.query("select * from lager",rowMapper);
        System.out.println(lagerList);
        return lagerList;
   }
   public Lager tagFatILageret(String navn){
       List<Lager> lagerList= jdbcTemplate.query("select * from lager where navn=?",rowMapper,navn);
        if (lagerList.size()==1){
            return lagerList.getFirst();
        }
        return null;
   }
    public boolean sletLageret(String navn){
        int value= jdbcTemplate.update("delete from lager where navn=?",navn);
        if (value==1){
            return true;
        }
        else{
            return false;
        }
    }



}
