package DatabaseTesting;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.List;

/**
 * PhoneBookDAO
 * A DAO for getting phone book data.
 */
public class PhoneBookDAO {
    public JdbcOperations getTemplate() {
            return template;
        }

        public void setTemplate(JdbcOperations template) {
            this.template = template;
        }

        private JdbcOperations template;

        public List<String> getPhoneNumbersForLastName(String lastName) throws DataAccessException {
            String query = "select phone_number from phone_book where last_name = ?";
            return getTemplate().queryForList(query, new String[]{lastName}, String.class);
        }
}