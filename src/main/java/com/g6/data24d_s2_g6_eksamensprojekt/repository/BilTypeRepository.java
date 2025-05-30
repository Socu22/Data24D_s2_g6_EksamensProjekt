package com.g6.data24d_s2_g6_eksamensprojekt.repository;

import com.g6.data24d_s2_g6_eksamensprojekt.model.BilType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class BilTypeRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public static final List<String> BIL_MAERKER = Arrays.asList(
            "Alfa Romeo", "Audi", "Aston Martin", "BMW",
            "Chrysler", "Citroen", "Daewoo", "Ferrari",
            "Fiat", "Ford", "Honda", "Hyundai",
            "Jaguar", "Jeep", "Kia", "Lancia",
            "Landrover", "Lexus", "Lotus", "Maserati",
            "Mazda", "Mercedes", "MG", "Mitsubishi",
            "Morgan", "Nissan", "Opel", "Peugeot",
            "Renault", "Rolls Royce", "Rover", "Saab",
            "Seat", "Skoda", "Subaru", "Suzuki",
            "Toyota", "Volvo", "VW"
    );

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

    public void gemBilType(BilType bilType){
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
    public List<BilType> hentBilTyper()
    {
        List<BilType> bilTypeList = jdbcTemplate.query("select * from bilType",rowMapper);
        return bilTypeList;
    }
    public BilType hentBilType(int bilTypeId)
    {
        List<BilType> bilTypeList = jdbcTemplate.query("select * from bilType where bilType_Id = ?",rowMapper, bilTypeId);
        if (!bilTypeList.isEmpty()) return bilTypeList.getFirst();
        return null;
    }

    public int getNextId() {
        String sql = "SELECT MAX(bilType_Id) FROM bilType";
        Integer maxWishId = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getInt(1));
        return (maxWishId != null ? maxWishId + 1 : 1);
    }
    public List<String> hentAlleMaerkerDK(){
        return BIL_MAERKER;
    }

}
