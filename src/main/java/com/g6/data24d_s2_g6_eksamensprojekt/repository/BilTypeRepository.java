package com.g6.data24d_s2_g6_eksamensprojekt.repository;

import com.g6.data24d_s2_g6_eksamensprojekt.model.BilType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BilTypeRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final RowMapper<BilType> rowMapper = (rs, rowNum) -> {
        BilType bilType = new BilType(rs.getInt("bilType_Id"),
                rs.getString("mærke"),
                rs.getString("model"),
                rs.getString("udstyrsniveau"),
                rs.getDouble("stålPris"),
                rs.getDouble("afgift"),
                rs.getDouble("udledning_Co2"));
        return bilType;
    };

    public void nyBilTypeLogik(BilType bilType){
        String sql = "INSERT into bilType(" +
                "bilType_Id," +
                "mærke," +
                " model, " +
                "udstyrsniveau, " +
                "stålPris, " +
                "afgift, " +
                "udledning_Co2) values (?,?,?,?,?,?,?);";
        jdbcTemplate.update(sql,
                bilType.getBilType_Id(),
                bilType.getMaerke(),
                bilType.getModel(),
                bilType.getUdstyr(),
                bilType.getStaalPris(),
                bilType.getAfgift(),
                bilType.getUdledningCo2());
    }
    public List<BilType> getBilTyper()
    {
        List<BilType> bilTypeList = jdbcTemplate.query("select * from bilType",rowMapper);
        return bilTypeList;
    }

    public int getNextId() {
        String sql = "SELECT MAX(bilType_Id) FROM bilType";
        Integer maxWishId = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getInt(1));
        return (maxWishId != null ? maxWishId + 1 : 1);
    }

}
