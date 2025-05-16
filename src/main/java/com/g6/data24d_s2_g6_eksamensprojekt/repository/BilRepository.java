package com.g6.data24d_s2_g6_eksamensprojekt.repository;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Bil;
import com.g6.data24d_s2_g6_eksamensprojekt.model.BilType;
import com.g6.data24d_s2_g6_eksamensprojekt.model.Lager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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
                rs.getString("status"));
        return bil;
    };
    public void gemBil(Bil bil){
        String sql = "INSERT into bil(vognNummer, stelNummer, bilType_Id, lager_Id, status) values (?,?,?,?,?)";
        jdbcTemplate.update(sql, bil.getVognNummer(),bil.getStelNummer(),bil.getBilType_Id(),bil.getLager_Id(),bil.getStatus());
    }

    public List<Bil> hentBiler()
    {
        List<Bil> bilList = jdbcTemplate.query("select * from bil",rowMapper);

        List<BilType> bilTyper = bilTypeRepository.hentBilTyper();
        HashMap<Integer, BilType> typerMapped = new HashMap<>();
        for (BilType biltype : bilTyper) {typerMapped.put(biltype.getBilType_Id(),biltype);}

        List<Lager> lagere = lagerRepository.hentLager();
        HashMap<Integer, Lager> lagereMapped = new HashMap<>();
        for (Lager lager : lagere) {lagereMapped.put(lager.getLager_Id(),lager);}

        for (Bil bil : bilList)
        {
            bil.setType(typerMapped.get(bil.getBilType_Id()));
            bil.setLager(lagereMapped.get(bil.getLager_Id()));
        }

        return bilList;
    }
    //tager fat i bil ud fra et vognNummer
    public Bil hentBil(String vognNummer){
        List<Bil> bilList= jdbcTemplate.query("select * from bil where vognNummer=?",rowMapper,vognNummer);
        List<BilType> bilTyper = bilTypeRepository.hentBilTyper();
        HashMap<Integer, BilType> typerMapped = new HashMap<>();
        for (BilType biltype : bilTyper) {typerMapped.put(biltype.getBilType_Id(),biltype);}

        List<Lager> lagere = lagerRepository.hentLager();
        HashMap<Integer, Lager> lagereMapped = new HashMap<>();
        for (Lager lager : lagere) {lagereMapped.put(lager.getLager_Id(),lager);}

        for (Bil bil : bilList)
        {
            bil.setType(typerMapped.get(bil.getBilType_Id()));
            bil.setLager(lagereMapped.get(bil.getLager_Id()));
        }

        if (bilList.size()==1){
            Bil bil = bilList.getFirst();
            bil.setType(bilTypeRepository.hentBilType(bil.getBilType_Id()));
            return bil;
        }
        return null;
    }
    public boolean sletBil(String vognNummer){
        List <Bil> count= jdbcTemplate.query("select * from bil where vognNummer=?",rowMapper,vognNummer);
        if (count.size()==1){
            jdbcTemplate.update("delete from bil where vognNummer=?",vognNummer);
            return true;
        }
        else{
            return false;
        }
    }


    public List<Bil> hentBilerUdFraVognNummer(String vognNummer){
        List<Bil> bilList = jdbcTemplate.query("select * from bil where vognNummer=?",rowMapper,vognNummer);

        List<BilType> bilTyper = bilTypeRepository.hentBilTyper();
        HashMap<Integer, BilType> typerMapped = new HashMap<>();
        for (BilType biltype : bilTyper) {typerMapped.put(biltype.getBilType_Id(),biltype);}

        List<Lager> lagere = lagerRepository.hentLager();
        HashMap<Integer, Lager> lagereMapped = new HashMap<>();
        for (Lager lager : lagere) {lagereMapped.put(lager.getLager_Id(),lager);}

        for (Bil bil : bilList)
        {
            bil.setType(typerMapped.get(bil.getBilType_Id()));
            bil.setLager(lagereMapped.get(bil.getLager_Id()));
        }

        return bilList;
        }

    public List<Bil> hentBilerUdFraStelNummer(String stelNummer) {

        List<Bil> bilList = jdbcTemplate.query("select * from bil where stelNummer=?",rowMapper,stelNummer);

        List<BilType> bilTyper = bilTypeRepository.hentBilTyper();
        HashMap<Integer, BilType> typerMapped = new HashMap<>();
        for (BilType biltype : bilTyper) {typerMapped.put(biltype.getBilType_Id(),biltype);}

        List<Lager> lagere = lagerRepository.hentLager();
        HashMap<Integer, Lager> lagereMapped = new HashMap<>();
        for (Lager lager : lagere) {lagereMapped.put(lager.getLager_Id(),lager);}

        for (Bil bil : bilList)
        {
            bil.setType(typerMapped.get(bil.getBilType_Id()));
            bil.setLager(lagereMapped.get(bil.getLager_Id()));
        }
        return bilList;


    }

    public List<Bil> hentBilerUdFraBilMaerke(String bilMærke){
        List<Bil> bilList = jdbcTemplate.query(
                "SELECT * FROM bil INNER JOIN bilType b ON bil.bilType_Id = b.bilType_Id WHERE b.mærke = ?",
                rowMapper,
                bilMærke
        );
        List<BilType> bilTyper = bilTypeRepository.hentBilTyper();
        HashMap<Integer, BilType> typerMapped = new HashMap<>();
        for (BilType biltype : bilTyper) {typerMapped.put(biltype.getBilType_Id(),biltype);}

        List<Lager> lagere = lagerRepository.hentLager();
        HashMap<Integer, Lager> lagereMapped = new HashMap<>();
        for (Lager lager : lagere) {lagereMapped.put(lager.getLager_Id(),lager);}

        for (Bil bil : bilList)
        {
            bil.setType(typerMapped.get(bil.getBilType_Id()));
            bil.setLager(lagereMapped.get(bil.getLager_Id()));
        }
        return bilList;


    }
    public List<Bil> hentBilerUdFraLager_Id(int lager_Id) {
        List<Bil> bilList = jdbcTemplate.query("select * from bil where lager_Id=?",rowMapper,lager_Id);

        List<BilType> bilTyper = bilTypeRepository.hentBilTyper();
        HashMap<Integer, BilType> typerMapped = new HashMap<>();
        for (BilType biltype : bilTyper) {typerMapped.put(biltype.getBilType_Id(),biltype);}

        List<Lager> lagere = lagerRepository.hentLager();
        HashMap<Integer, Lager> lagereMapped = new HashMap<>();
        for (Lager lager : lagere) {lagereMapped.put(lager.getLager_Id(),lager);}

        for (Bil bil : bilList)
        {
            bil.setType(typerMapped.get(bil.getBilType_Id()));
            bil.setLager(lagereMapped.get(bil.getLager_Id()));
        }
        return bilList;

    }

    public List<Bil> hentBilerbilerUdFraLager_idEllerOgMaerke(String lager_Id, String maerke)
    {
        List<Bil> bilList = hentBiler();
        if (lager_Id != null && !lager_Id.isEmpty()) {
            int lager_Id_SomInt = Integer.parseInt(lager_Id);
            bilList.removeIf(b -> b.getLager_Id() != lager_Id_SomInt);
        }

        if (maerke != null && !maerke.isEmpty()) {
            bilList.removeIf(b -> !b.getType().getMaerke().equalsIgnoreCase(maerke));
        }
        return bilList;
    }
}
