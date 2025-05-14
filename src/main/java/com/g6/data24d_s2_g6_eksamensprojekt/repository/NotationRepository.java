package com.g6.data24d_s2_g6_eksamensprojekt.repository;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Bil;
import com.g6.data24d_s2_g6_eksamensprojekt.model.Notation;
import com.g6.data24d_s2_g6_eksamensprojekt.model.LejeAftale;

@Repository
public class NotationRepository
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addNotation(String vognNr, Integer aftaleId, String beskrivelse, double pris)
    {
        String sql = "INSERT INTO notationer (vognNummer, aftale_Id, beskrivelse, pris) VALUES (?, ?, ?, ?);";

        jdbcTemplate.update(sql, vognNr, aftaleId, beskrivelse, pris);
    }

    public void updateNotation(Notation notation)
    {
        String sql = "UPDATE notationer SET vognNummer = ?, aftale_Id = ?, beskrivelse = ?, pris = ? WHERE notationer_Id = ?;";

        jdbcTemplate.update(sql,
                            notation.getVognNummer(),
                            notation.getAftaleId(),
                            notation.getBeskrivelse(),
                            notation.getPris(),
                            notation.getId());
    }

    public List<Notation> getNotationer(int aftaleId)
    {
        return selectNotationer("aftale_Id", aftaleId);

        /*
        String sql = "SELECT * FROM notationer WHERE aftale_Id = ?;";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, aftale.getAftale_Id());

        return buildNotationer(list);
         */
    }

    public List<Notation> getNotationer(String vognNummer)
    {
        return selectNotationer("vognNummer", vognNummer);

        /*
        String sql = "SELECT * FROM notationer WHERE vognNummer = ?;";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, bil.getVognNummer());

        return buildNotationer(list);
         */
    }

    private List<Notation> selectNotationer(String column, Object identifier)
    {
        String sql = "SELECT * FROM notationer WHERE "+column+" = ?;";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, identifier);

        return buildNotationer(list);
    }

    private List<Notation> buildNotationer(List<Map<String, Object>> dataList)
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
