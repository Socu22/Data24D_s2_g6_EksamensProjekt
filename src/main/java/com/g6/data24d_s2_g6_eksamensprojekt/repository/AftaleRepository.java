package com.g6.data24d_s2_g6_eksamensprojekt.repository;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Lager;
import com.g6.data24d_s2_g6_eksamensprojekt.model.LejeAftale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AftaleRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;


    private final RowMapper<LejeAftale> rowMapper = (rs, rowNum) -> {
        LejeAftale lejeAftale = new LejeAftale();
        lejeAftale.setAftale_Id(rs.getInt("aftale_Id"));
        lejeAftale.setKunde_Id(rs.getInt("kunde_Id"));
        lejeAftale.setVognNummer(rs.getString("vognNummer"));
        lejeAftale.setStartDato(rs.getString("startDato"));
        lejeAftale.setSlutDato(rs.getString("slutDato"));
        lejeAftale.setDetaljer(rs.getString("detaljer"));
        return lejeAftale;
    };
    //Laver en ny aftale og opdaterer databasen
    public void nyAftaleLogik(LejeAftale lejeAftale){
        String sql = "INSERT into lejeAftaler (kunde_Id, vognNummer, startDato, slutDato, detaljer ) values (?,?,?,?,?)";
        jdbcTemplate.update(sql, lejeAftale.getKunde_Id(),lejeAftale.getVognNummer(),lejeAftale.getStartDato(),lejeAftale.getSlutDato(),lejeAftale.getDetaljer());
    }

    //Samler alle lejeAftale objekter sammen i en liste.
    public List<LejeAftale> samleLejeAftalerIListeLogik(){
        List<LejeAftale> lejeAftaleList = jdbcTemplate.query("select * from lejeAftaler",rowMapper);
        System.out.println(lejeAftaleList);
        return lejeAftaleList;
    }

    //tager fat i LejeAftale ud fra et id
    public LejeAftale tagFatILageret(int aftale_Id){
        List<LejeAftale> lejeAftaleList= jdbcTemplate.query("select * from lejeAftaler where aftale_Id=?",rowMapper,aftale_Id);
        if (lejeAftaleList.size()==1){
            return lejeAftaleList.getFirst();
        }
        return null;
    }

    //sletter lejeAftale med specifikt id.
    public boolean sletLejeAftale(int aftale_Id){
        List <LejeAftale> count= jdbcTemplate.query("select * from lejeAftaler where aftale_Id=?",rowMapper,aftale_Id);
        if (count.size()==1){
            jdbcTemplate.update("delete from lejeAftaler where aftale_Id=?",aftale_Id);
            return true;
        }
        else{
            return false;
        }
    }


}
