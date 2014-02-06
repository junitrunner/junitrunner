package com.github.junitrunner.spring;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.github.junitrunner.JUnitRunner;
import com.github.junitrunner.WithPlugins;

@RunWith(JUnitRunner.class)
@WithPlugins(SpringPlugin.class)
@ContextConfiguration("/spring-context.xml")
@Transactional
@SuppressWarnings("boxing")
public class Spring_Plugin_Test {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Before
    public void prepareDB() {

        jdbcTemplate.update("insert into PERSON (NAME, AGE) values ('FIRST LAST', 31)");
    }

    @Test
    public void canPrepare() {

        jdbcTemplate.query("select * from PERSON", new ResultSetExtractor<Object>() {

            @Override
            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {

                assertThat(rs.next(), is(true));
                assertThat(rs.getString("NAME"), is("FIRST LAST"));
                assertThat(rs.getInt("AGE"), is(31));
                assertThat(rs.next(), is(false));
                return null;
            }

        });
    }
}
