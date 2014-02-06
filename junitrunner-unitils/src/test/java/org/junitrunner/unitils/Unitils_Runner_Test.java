package org.junitrunner.unitils;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junitrunner.unitils.UnitilsRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByName;

@RunWith(UnitilsRunner.class)
@SpringApplicationContext({ "/unitils-context.xml" })
@DataSet("/UnitilsTest.xml")
@Transactional(TransactionMode.ROLLBACK)
@SuppressWarnings("boxing")
public class Unitils_Runner_Test {

    @SpringBeanByName
    private JdbcTemplate jdbcTemplate;

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

    @Test
    @ExpectedDataSet("/UnitilsTest-verify.xml")
    public void canVerify() throws Exception {

        jdbcTemplate.update("insert into PERSON (NAME, AGE) values (?,?)", new Object[] {"X", 1});
    }
}
