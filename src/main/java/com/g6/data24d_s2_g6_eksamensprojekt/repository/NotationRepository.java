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

        /*
        String sql = "SELECT * FROM notationer WHERE aftale_Id = ?;";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, aftale.getAftale_Id());

        return buildNotationer(list);
         */
    }

    public List<Notation> hentNotationer(String vognNummer)
    {
        return hentNotationer("vognNummer", vognNummer);

        /*
        String sql = "SELECT * FROM notationer WHERE vognNummer = ?;";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, bil.getVognNummer());

        return buildNotationer(list);
         */
    }

    private List<Notation> hentNotationer(String column, Object identifier)
    {
        String sql = "SELECT * FROM notationer WHERE "+column+" = ?;";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, identifier);

        return bygNotationer(list);
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
}
