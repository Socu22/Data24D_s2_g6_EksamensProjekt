package com.g6.data24d_s2_g6_eksamensprojekt.repository;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Bil;
import com.g6.data24d_s2_g6_eksamensprojekt.model.LejeAftale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Repository
public class AftaleRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    KundeRepository kundeRepository;

    @Autowired
    BilRepository bilRepository;

    private final RowMapper<LejeAftale> rowMapper = (rs, rowNum) -> {
        LejeAftale lejeAftale = new LejeAftale();
        lejeAftale.setAftale_Id(rs.getInt("aftale_Id"));
        lejeAftale.setKunde_Navn(rs.getString("kunde_Navn"));
        lejeAftale.setVognNummer(rs.getString("vognNummer"));
        lejeAftale.setStartDato(rs.getDate("startDato").toLocalDate());
        try {
            lejeAftale.setSlutDato(rs.getDate("slutDato").toLocalDate());

        }catch (NullPointerException e){
            lejeAftale.setSlutDato(null);

        }
        if (rs.getDate("betaltDato") != null) lejeAftale.setBetalingsDato(rs.getDate("betaltDato").toLocalDate());
        lejeAftale.setDetaljer(rs.getString("detaljer"));
        return lejeAftale;
    };
    //Laver en ny aftale og opdaterer databasen
    public void gemLejeAftale(LejeAftale lejeAftale){
        String sql = "INSERT into lejeAftaler (kunde_Navn, vognNummer, startDato, slutDato, detaljer ) values (?,?,?,?,?)";
        jdbcTemplate.update(sql, lejeAftale.getKunde_Navn(),lejeAftale.getVognNummer(),lejeAftale.getStartDato(),lejeAftale.getSlutDato(),lejeAftale.getDetaljer());
    }

    //Samler alle lejeAftale objekter sammen i en liste.
    public List<LejeAftale> hentLejeAftaler(){
        List<LejeAftale> lejeAftaleList = jdbcTemplate.query("select * from lejeAftaler",rowMapper);

        List<Bil> biler = bilRepository.hentBiler();
        HashMap<String, Bil> bilerMapped = new HashMap<>();
        for (Bil bil : biler)
        {
            bilerMapped.put(bil.getVognNummer(),bil);
        }

        /*List<Kunde> kunder = kundeRepository.hentKunder();
        HashMap<Integer, Kunde> kunderMapped = new HashMap<>();
        for (Kunde kunde: kunder)
        {
            kunderMapped.put(kunde.getKunde_Id(),kunde);
        }

         */

        for (LejeAftale aftale: lejeAftaleList)
        {
            //aftale.setKunde(kunderMapped.get(aftale.getKunde_Id()));
            aftale.setBil(bilerMapped.get(aftale.getVognNummer()));
        }

        return lejeAftaleList;
    }

    //tager fat i LejeAftale ud fra et id
    public LejeAftale hentLejeAftale(int aftale_Id){
        List<LejeAftale> lejeAftaleList= jdbcTemplate.query("select * from lejeAftaler where aftale_Id=?",rowMapper,aftale_Id);
        if (lejeAftaleList.size()==1){
            return lejeAftaleList.getFirst();
        }
        return null;
    }

    public LejeAftale hentLejeAftale(String vognNummer)
    {
        List<LejeAftale> lejeAftaleList= jdbcTemplate.query("select * from lejeAftaler where vognNummer=?",rowMapper,vognNummer);

        if (lejeAftaleList.isEmpty()) return null;
        return lejeAftaleList.getFirst();
    }

    public boolean aflysLejeAftale(int aftale_Id){
        List <LejeAftale> count= jdbcTemplate.query("select * from lejeAftaler where aftale_Id=?",rowMapper,aftale_Id);
        if (count.size()==1){
            jdbcTemplate.update(
                    "UPDATE bil SET status = 'TILGAENGELIG' WHERE bil.vognNummer IN (SELECT lejeAftaler.vognNummer FROM lejeAftaler WHERE aftale_Id = ?)",
                    aftale_Id
            );
            jdbcTemplate.update("delete from lejeAftaler where aftale_Id=?",aftale_Id);
            return true;
        }
        else{
            return false;
        }
    }

    public void forlaengLejeAftale(int lejeAftale_Id,int forlaengMaaneder) {
        jdbcTemplate.update("UPDATE lejeAftaler set slutDato=DATE_ADD(slutDato,interval ? month ) where aftale_Id = ?",forlaengMaaneder,lejeAftale_Id );

    }

    public void saetBetalt(int lejeAftale_Id, LocalDate dato)
    {
        jdbcTemplate.update("UPDATE lejeAftaler set betaltDato=? where aftale_Id=?", Date.valueOf(dato), lejeAftale_Id);
    }

    public void saetBetalt(int lejeAftale_Id)
    {
        saetBetalt(lejeAftale_Id, LocalDate.now());
    }

    public int getNextId() {
        String sql = "SELECT MAX(aftale_Id) FROM lejeAftaler";
        Integer maxWishId = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getInt(1));
        return (maxWishId != null ? maxWishId + 1 : 1);
    }
}
