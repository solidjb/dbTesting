package DatabaseTesting;

import com.mockrunner.jdbc.BasicJDBCTestCaseAdapter;
import com.mockrunner.jdbc.PreparedStatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockDataSource;
import com.mockrunner.mock.jdbc.MockResultSet;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Unit test for simple PhoneBookDAO2.
 */
public class PhoneBookDAOUnitTest extends BasicJDBCTestCaseAdapter {
    PhoneBookDAO dao;
    JdbcOperations jdbc;
    MockDataSource ds;
    String queryIdString = "select phone_num";
    String testId = "jones";

    public void setUp() throws Exception {
        super.setUp();
        ds = getJDBCMockObjectFactory().getMockDataSource();
        jdbc = new JdbcTemplate(ds);
        dao = new PhoneBookDAO();
        dao.setTemplate(jdbc);
    }

    private void prepareEmptyResultSet(MockDataSource ds) {
        PreparedStatementResultSetHandler srsh = createStatementHandler(ds);
        MockResultSet result = srsh.createResultSet();
        srsh.prepareGlobalResultSet(result);
    }

    public void test_no_hits() {
        prepareEmptyResultSet(ds);

        List result = dao.getPhoneNumbersForLastName(testId);
        assertThat(result.isEmpty(), is(equalTo(true)));

        performSqlVerification();
    }

    public void test_get_hits() {
        List<String> myHits = new ArrayList<String>();
        Collections.addAll(myHits, "1234567", "7654321", "1111111", "9999999");
        prepareFullResultSet(ds, myHits);

        List<String> result = dao.getPhoneNumbersForLastName(testId);
        assertThat(result, is(equalTo(myHits)));

        performSqlVerification();
    }

    private void prepareFullResultSet(MockDataSource dataSource, List hits) {
        PreparedStatementResultSetHandler srsh = createStatementHandler(dataSource);
        MockResultSet result = srsh.createResultSet();
        for (Object o : hits) {
            result.addRow(new Object[]{o});
        }

        srsh.prepareGlobalResultSet(result);
    }

    private PreparedStatementResultSetHandler createStatementHandler(MockDataSource ds) {
        MockConnection conn = ds.getMockConnection();
        return conn.getPreparedStatementResultSetHandler();
    }

    private void performSqlVerification() {
        verifySQLStatementExecuted(queryIdString);
        verifySQLStatementParameter(queryIdString, 0, 1, testId);
    }
}