package com.g6.data24d_s2_g6_eksamensprojekt.repository;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Bil;
import com.g6.data24d_s2_g6_eksamensprojekt.model.BilType;
import com.g6.data24d_s2_g6_eksamensprojekt.model.Lager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class BilRepository
{
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    BilTypeRepository bilTypeRepository;
    @Autowired
    LagerRepository lagerRepository;



    private final RowMapper<Bil> rowMapper = (rs, rowNum) -> {
        Bil bil = new Bil(
                rs.getString("vognNummer"),
                rs.getString("stelNummer"),
                new BilType(rs.getInt("bilType_Id")),
                rs.getInt("lager_Id"),
                rs.getString("status"),
                rs.getDouble("kørteKm"));
        return bil;
    };
    public void gemBil(Bil bil){
        String sql = "INSERT into bil(vognNummer, stelNummer, bilType_Id, lager_Id, status) values (?,?,?,?,?)";
        jdbcTemplate.update(sql, bil.getVognNummer(),bil.getStelNummer(),bil.getBilType_Id(),bil.getLager_Id(),bil.getStatus());
    }



    public List<Bil> hentBiler()
    {
        List<Bil> biler = jdbcTemplate.query("select * from bil",rowMapper);

        return bygBiler(biler);
    }
    public List<Bil> hentEksisteredeBiler()
    {
        List<Bil> biler = jdbcTemplate.query("select * from bil where status!='solgt'",rowMapper);

        return bygBiler(biler);
    }

    //tager fat i bil ud fra et vognNummer
    public Bil hentBil(String vognNummer){
        List<Bil> bilList= jdbcTemplate.query("select * from bil where vognNummer=?",rowMapper,vognNummer);
        if (bilList.isEmpty()) return null;
        return bygBiler(bilList).getFirst();
    }
    public boolean solgtBil(String vognNummer){ // todo: SOlgt Bil mangler mere logik
        List<Bil> bilList= jdbcTemplate.query("select * from bil where vognNummer=?",rowMapper,vognNummer);
        if (bilList.isEmpty()) return false; else {
            jdbcTemplate.update("update bil set status='SOLGT' where vognNummer=?",vognNummer);
            return true;
        }
    }


    public List<Bil> hentBilerUdFraVognNummer(String vognNummer){
        List<Bil> bilList = jdbcTemplate.query("select * from bil where status!='solgt'",rowMapper);
        bilList.removeIf(bil -> !bil.getVognNummer().contains(vognNummer));
        return bygBiler(bilList);
    }


    public List<Bil> hentBilerUdFraStelNummer(String stelNummer) {
        List<Bil> bilList = jdbcTemplate.query("select * from bil where status!='solgt'",rowMapper);
        bilList.removeIf(bil -> !bil.getStelNummer().contains(stelNummer));
        return bygBiler(bilList);


    }

//    public List<Bil> hentBilerUdFraBilMaerke(String bilMærke){
//        List<Bil> biler = jdbcTemplate.query(
//                "SELECT * FROM bil INNER JOIN bilType b ON bil.bilType_Id = b.bilType_Id WHERE b.mærke = ?",
//                     rowMapper,
//                     bilMærke);
//        return bygBiler(biler);
//    }
//    public List<Bil> hentBilerUdFraLager_Id(int lager_Id) {
//        List<Bil> biler = jdbcTemplate.query("select * from bil where lager_Id=?",rowMapper,lager_Id);
//        return bygBiler(biler);
//    }

    public List<Bil> hentEksisteredeBilerSoegFunktion(String lager_Id, String maerke, String status) {
        List<Bil> bilList = hentEksisteredeBiler();

        bilList = filtrerEfterLagerId(bilList, lager_Id);
        bilList = filtrerEfterMaerke(bilList, maerke);
        bilList = filtrerEfterStatus(bilList, status);

        return bygBiler(bilList);
    }

    private List<Bil> filtrerEfterLagerId(List<Bil> biler, String lager_Id) {
        if (lager_Id != null && !lager_Id.isEmpty()) {
            try {
                int lagerIdInt = Integer.parseInt(lager_Id);
                biler.removeIf(b -> b.getLager_Id() != lagerIdInt);
            } catch (NumberFormatException e) {
                System.out.println("fejl i filtrer lager_Id");
            }
        }
        return biler;
    }

    private List<Bil> filtrerEfterMaerke(List<Bil> biler, String maerke) {
        if (maerke != null && !maerke.isEmpty()) {
            biler.removeIf(b -> !b.getType().getMaerke().equalsIgnoreCase(maerke));
        }
        return biler;
    }

    private List<Bil> filtrerEfterStatus(List<Bil> biler, String status) {
        if (status != null && !status.isEmpty()) {
            biler.removeIf(b -> !b.getStatus().equalsIgnoreCase(status));
        }
        return biler;
    }

    private List<Bil> bygBiler(List<Bil> biler)
    {
        List<BilType> bilTyper = bilTypeRepository.hentBilTyper();
        HashMap<Integer, BilType> typerMapped = new HashMap<>();
        for (BilType biltype : bilTyper) {typerMapped.put(biltype.getBilType_Id(),biltype);}

        List<Lager> lagere = lagerRepository.hentLager();
        HashMap<Integer, Lager> lagereMapped = new HashMap<>();
        for (Lager lager : lagere) {lagereMapped.put(lager.getLager_Id(),lager);}

        for (Bil bil : biler)
        {
            bil.setType(typerMapped.get(bil.getBilType_Id()));
            bil.setLager(lagereMapped.get(bil.getLager_Id()));
        }

        return biler;
    }

    public void saetStatus(String vognNummer,String status) {
        jdbcTemplate.update("UPDATE bil set status=? where vognNummer=?", status, vognNummer);
    }

    public void saetStatus(Bil bil,String status) {
        bil.setStatus(status);
        saetStatus(bil.getVognNummer(), status);
    }

    public boolean sletBil(String vognNummer) {return false;}
}
