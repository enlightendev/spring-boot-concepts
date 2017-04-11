package io.enlightendev.controllers;

import io.enlightendev.service.JDBCService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/api")
public class JDBCController {

    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    JDBCService jdbcService;

    /**
     * Request params are appended to url
     * @return
     */
    @RequestMapping("/createTable")
    public String createTable() {

        jdbcService.createtable();

        return "created table";

    }

}