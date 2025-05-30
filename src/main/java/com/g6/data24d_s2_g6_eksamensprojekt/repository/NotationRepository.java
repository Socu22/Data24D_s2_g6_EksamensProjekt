package com.g6.data24d_s2_g6_eksamensprojekt.repository;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Notation;

@Repository
public class NotationRepository
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void gemNotation(String vognNummer, Integer aftaleId, String beskrivelse, double pris)
    {
        String sql = "INSERT INTO notationer (vognNummer, aftale_Id, beskrivelse, pris) VALUES (?, ?, ?, ?);";

        jdbcTemplate.update(sql, vognNummer, aftaleId, beskrivelse, pris);
    }

    public void gemNotation(Notation notation)
    {
        String sql = "UPDATE notationer SET vognNummer = ?, aftale_Id = ?, beskrivelse = ?, pris = ? WHERE notationer_Id = ?;";

        jdbcTemplate.update(sql,
                            notation.getVognNummer(),
                            notation.getAftaleId(),
                            notation.getBeskrivelse(),
                            notation.getPris(),
                            notation.getId());
    }

    public List<Notation> hentNotationer(int aftaleId)
    {
        return hentNotationer("aftale_Id", aftaleId);
    }

    public List<Notation> hentNotationer(String vognNummer)
    {
        return hentNotationer("vognNummer", vognNummer);
    }

    public List<Notation> hentSkader(int aftaleId)
    {
        String sql = "SELECT * FROM notationer WHERE aftale_Id = ?;";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, aftaleId);

        return bygNotationer(list);
    }

    public Double hentSumfor(int aftaleId)
    {
        List<Double> liste = hent("pris", double.class, "aftale_Id", aftaleId);
        return sum(liste.toArray(new Double[0]));
    }

    public Double hentSumfor(String vognNummer)
    {
        List<Double> liste = hent("pris", double.class, "vognNummer", vognNummer);
        return sum(liste.toArray(new Double[0]));
    }

    private List<Notation> hentNotationer(String nøgle, Object kritirie)
    {
        String sql = "SELECT * FROM notationer WHERE "+nøgle+" = ? AND aftale_Id IS NULL;";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, kritirie);

        return bygNotationer(list);
    }

    private <T> List<T> hent(String kolonne, Class<T> type, String nøgle, Object kritirie)
    {
        String sql = "SELECT "+kolonne+" FROM notationer WHERE "+nøgle+" = ?;";
        return jdbcTemplate.queryForList(sql, type, kritirie);
    }

    private List<Notation> bygNotationer(List<Map<String, Object>> dataList)
    {
        List<Notation> list = new ArrayList<>();

        for (Map<String, Object> row:dataList)
        {
            list.add(new Notation((Integer) row.get("notationer_id"),
                                  (Integer) row.get("aftale_Id"),
                                  (String)  row.get("vognNummer"),
                                  (String)  row.get("beskrivelse"),
                                  (Double)  row.get("pris")));
        }

        return list;
    }

    private Double sum(Double... doubles)
    {
        Double sum = .0;
        for (Double d : doubles) {sum += d;}
        return sum;
    }
}
