package com.g6.data24d_s2_g6_eksamensprojekt.repository;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Bil;
import com.g6.data24d_s2_g6_eksamensprojekt.model.BilType;
import com.g6.data24d_s2_g6_eksamensprojekt.model.LejeAftale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Repository
public class BilRepository
{
    @Autowired
    JdbcTemplate jdbcTemplate;

    private final RowMapper<Bil> rowMapper = (rs, rowNum) -> {
        Bil bil = new Bil(
                rs.getString("vognNummer"),
                rs.getString("stelNummer"),
                new BilType(rs.getInt("bilType_Id")),
                rs.getInt("lager_Id"),
                rs.getString("status"));
        return bil;
    };
    public void nyBilLogik(Bil bil){
        String sql = "INSERT into bil(vognNummer, stelNummer, bilType_Id, lager_Id, status) values (?,?,?,?,?)";
        jdbcTemplate.update(sql, bil.getVognNummer(),bil.getStelNummer(),bil.getBilType_Id(),bil.getLager_Id(),bil.getStatus());
    }

    public List<Bil> getBiler()
    {
        List<Bil> bilList = jdbcTemplate.query("select * from bil",rowMapper);
        return bilList;
    }
    //tager fat i bil ud fra et vognNummer
    public Bil tagFatIBil(String vognNummer){
        List<Bil> bilList= jdbcTemplate.query("select * from bil where vognNummer=?",rowMapper,vognNummer);
        if (bilList.size()==1){
            return bilList.getFirst();
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

    public List<Bil> findUdFraKrav(String vognNummer, String bilMærke){
        List<Bil> bilList = new ArrayList<>();
        bilList.addAll(findBilUdFraVognNummer(vognNummer));
        bilList.addAll(findBilUdFraModel(bilMærke));

        LinkedHashSet<Bil> bilLinkedHashSet = new LinkedHashSet<>(bilList);
        return bilLinkedHashSet.stream().toList();

    }
    public List<Bil> findBilUdFraVognNummer(String vognNummer){

        return jdbcTemplate.query("select * from bil where vognNummer=?",rowMapper,vognNummer);

    }
    public List<Bil> findBilUdFraModel(String bilMærke){

        return jdbcTemplate.query("select * from bil inner join bilType b on bilType_Id = b.bilType_Id where mærke='?'",rowMapper,bilMærke);

    }

    public List<Bil> findBilUdFraStelNummer(String stelNummer) {
        return jdbcTemplate.query("select * from bil where stelNummer=?",rowMapper,stelNummer);
    }
}
