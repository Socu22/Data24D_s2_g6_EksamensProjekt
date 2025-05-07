package com.g6.data24d_s2_g6_eksamensprojekt.repository;

import com.g6.data24d_s2_g6_eksamensprojekt.model.Bil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BilRepository
{
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Bil> getBiler()
    {
        return new ArrayList<>();
    }
}
