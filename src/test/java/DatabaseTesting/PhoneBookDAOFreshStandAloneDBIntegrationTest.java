package DatabaseTesting;


import liquibase.Liquibase;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.jdbc.SimpleJdbcTestUtils.*;

/**
 * PhoneBookDAOExistingStandAloneDBIntegrationTest
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional
public class PhoneBookDAOFreshStandAloneDBIntegrationTest {
    @Resource
    private Liquibase liquibase;

    @Resource
    private PhoneBookDAO testDao;

    @Resource
    private SimpleJdbcTemplate jdbcTemplate;

    @Resource
    org.springframework.core.io.Resource testDataResource;

    private void create_tables() throws Throwable {
        liquibase.update("");
    }

    @After
    public void drop_tables() throws Throwable {
        liquibase.rollback(Integer.MAX_VALUE, "");
    }

    @Before
    public void setup_database() throws Throwable {
        create_tables();
        load_test_data();
    }

    private void load_test_data() {
        executeSqlScript(jdbcTemplate, testDataResource, false);
    }

    @Test
    public void test_no_hits() {
        List result = testDao.getPhoneNumbersForLastName("abcdefg");
        assertThat(result.isEmpty(), is(equalTo(true)));
    }

    @Test
    public void test_get_hits() {
        List<String> result = testDao.getPhoneNumbersForLastName("jones");
        assertThat(result.size(), is(equalTo(3)));
        assertThat(result, hasItems("1234567", "3333333", "2222222"));
    }
}