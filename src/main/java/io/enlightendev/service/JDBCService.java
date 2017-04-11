package io.enlightendev.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class JDBCService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final Logger log = LogManager.getLogger(this.getClass());

    public void createtable(){

        log.info("Creating tables");

        jdbcTemplate.execute("DROP TABLE IF EXISTS pet");

        jdbcTemplate.execute("CREATE TABLE pet " +
                "(name VARCHAR(20), owner VARCHAR(20),species VARCHAR(20), " +
                "sex CHAR(1), birth DATE, death DATE)");
    }

}
