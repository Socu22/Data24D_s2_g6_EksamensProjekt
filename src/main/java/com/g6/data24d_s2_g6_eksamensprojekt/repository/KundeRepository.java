package com.g6.data24d_s2_g6_eksamensprojekt.repository;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Kunde;
import com.g6.data24d_s2_g6_eksamensprojekt.model.Lager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class KundeRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    private final RowMapper<Kunde> rowMapper= (rs, rowNum) ->{
        Kunde kunde = new Kunde(rs.getInt("kunde_Id"), rs.getString("navn"));
        return kunde;
    };
    //todo: husk at brug getmapping
    public void gemNyKunde(String nyKunde){
        String sql = "INSERT into kunde (navn) values (?)";
        jdbcTemplate.update(sql, nyKunde);
    }
    //tager fat i LejeAftale ud fra et id
    public Kunde hentKunde(int kunde_Id){
        List<Kunde> lejeAftaleList= jdbcTemplate.query("select * from kunde where kunde_Id=?",rowMapper,kunde_Id);
        if (lejeAftaleList.size()==1){
            return lejeAftaleList.getFirst();
        }
        return null;
    }
    public List<Kunde> hentKunder()
    {
        List<Kunde> kundeList = jdbcTemplate.query("select * from kunde",rowMapper);
        return kundeList;
    }


}
