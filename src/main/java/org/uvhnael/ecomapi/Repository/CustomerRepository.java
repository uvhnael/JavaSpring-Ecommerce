package org.uvhnael.ecomapi.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.uvhnael.ecomapi.Model.Customer;

import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomerRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Customer> getAll() {
        String sql = "SELECT * FROM customers";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Customer(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("dob"),
                rs.getString("image_path"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("phone_number"),
                rs.getString("active"),
                rs.getString("registered_at")
        ));
    }

    public Customer getById(int id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> new Customer(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("dob"),
                rs.getString("image_path"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("phone_number"),
                rs.getString("active"),
                rs.getString("registered_at")
        ));
    }

    public Boolean getByUsername(String email) {
        String sql = "SELECT COUNT(*) FROM customers WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{email}, Integer.class) == 1;
    }

    public Customer getAllByUsername(String email) {
        String sql = "SELECT * FROM customers WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{email}, (rs, rowNum) -> new Customer(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("dob"),
                rs.getString("image_path"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("phone_number"),
                rs.getString("active"),
                rs.getString("registered_at")
        ));
    }

    public Customer getByUsernameAndPassword(String email, String password) {
        String sql = "SELECT * FROM customers WHERE email = ? AND password = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{email, password}, (rs, rowNum) -> new Customer(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("dob"),
                rs.getString("image_path"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("phone_number"),
                rs.getString("active"),
                rs.getString("registered_at")
        ));
    }

    public Integer create(Customer customer) {
        String sql = "INSERT INTO customers (name,dob, image_path, email, password, phone_number, active, registered_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, customer.getName(), customer.getDob(), customer.getImagePath(), customer.getEmail(), customer.getPassword(), customer.getPhoneNumber(), customer.getActive(), new Timestamp(System.currentTimeMillis()));
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
    }

    public boolean update(Customer customer) {
        String sql = "UPDATE customers SET name = ?, email = ?, password = ?, phone_number = ?, active = ? WHERE id = ?";
        return jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getPassword(), customer.getPhoneNumber(), customer.getActive(), customer.getId()) == 1;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM customers WHERE id = ?";
        return jdbcTemplate.update(sql, id) == 1;
    }

    public String getEmail(int id) {
        String sql = "SELECT email FROM customers WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, String.class);
    }

    public List<Customer> getCustomersData(int page, int size) {
        String sql = "SELECT * FROM customers LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{page, size}, (rs, rowNum) -> new Customer(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("dob"),
                rs.getString("image_path"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("phone_number"),
                rs.getString("active"),
                rs.getString("registered_at")
        ));
    }


}