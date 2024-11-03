package org.uvhnael.ecomapi.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.uvhnael.ecomapi.Dto.AddressResponse;
import org.uvhnael.ecomapi.Model.Address;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AddressRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Address> getByCustomerId(int customerId) {
        String query = "SELECT * FROM customers_addresses WHERE customer_id = ?";
        return jdbcTemplate.query(query, new Object[]{customerId}, (rs, rowNum) -> new Address(
                rs.getInt("id"),
                rs.getInt("customer_id"),
                rs.getBoolean("is_default"),
                rs.getString("customer_name"),
                rs.getString("address_line1"),
                rs.getString("address_line2"),
                rs.getString("phone_number"),
                rs.getString("city"),
                rs.getString("district"),
                rs.getString("ward")
        ));
    }

    public Address getByCustomerIdAndIsDefault(int customerId) {
        String query = "SELECT * FROM customers_addresses WHERE customer_id = ? AND is_default = 1";
        return jdbcTemplate.queryForObject(query, new Object[]{customerId}, (rs, rowNum) -> new Address(
                rs.getInt("id"),
                rs.getInt("customer_id"),
                rs.getBoolean("is_default"),
                rs.getString("customer_name"),
                rs.getString("address_line1"),
                rs.getString("address_line2"),
                rs.getString("phone_number"),
                rs.getString("city"),
                rs.getString("district"),
                rs.getString("ward")
        ));
    }

    public Address getById(int id) {
        String query = "SELECT * FROM customers_addresses WHERE id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, (rs, rowNum) -> new Address(
                rs.getInt("id"),
                rs.getInt("customer_id"),
                rs.getBoolean("is_default"),
                rs.getString("customer_name"),
                rs.getString("address_line1"),
                rs.getString("address_line2"),
                rs.getString("phone_number"),
                rs.getString("city"),
                rs.getString("district"),
                rs.getString("ward")
        ));
    }

    public boolean createAddress(int customerId, boolean isDefault, String customerName, String addressLine1, String addressLine2, String phoneNumber, String city, String district, String ward) {
        String query = "INSERT INTO customers_addresses (customer_id, is_default, customer_name, address_line1, address_line2, phone_number, city, district, ward) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(query, customerId, isDefault, customerName, addressLine1, addressLine2, phoneNumber, city, district, ward) > 0;
    }

    public boolean updateAddress(int id, boolean isDefault, String customerName, String addressLine1, String addressLine2, String phoneNumber, String city, String district, String ward) {
        String query = "UPDATE customers_addresses SET is_default = ?, customer_name = ?, address_line1 = ?, address_line2 = ?, phone_number = ?, city = ?, district = ?, ward = ? WHERE id = ?";
        return jdbcTemplate.update(query, isDefault, customerName, addressLine1, addressLine2, phoneNumber, city, district, ward, id) > 0;
    }

    public boolean deleteAddress(int id) {
        String query = "DELETE FROM customers_addresses WHERE id = ?";
        return jdbcTemplate.update(query, id) > 0;
    }

    public boolean setDefaultAddress(int id, int customerId) {
        boolean query1;
        boolean query2;
        String query = "";
        query = "UPDATE customers_addresses SET is_default = 0 WHERE id != ? AND customer_id = ?";
        query1 = jdbcTemplate.update(query, id, customerId) > 0;
        query = "UPDATE customers_addresses SET is_default = 1 WHERE id = ?";
        query2 = jdbcTemplate.update(query, id) > 0;
        return query1 && query2;
    }

    public List<AddressResponse> getCity() {
        String query = "SELECT code, full_name FROM provinces";
        return jdbcTemplate.query(query, (rs, rowNum) -> new AddressResponse(
                rs.getInt("code"),
                rs.getString("full_name")
        ));
    }

    public List<AddressResponse> getDistrict(int cityCode) {
        String query = "SELECT code, full_name FROM districts WHERE province_code = ?";
        return jdbcTemplate.query(query, new Object[]{cityCode}, (rs, rowNum) -> new AddressResponse(
                rs.getInt("code"),
                rs.getString("full_name")
        ));
    }

    public List<AddressResponse> getWard(int districtCode) {
        String query = "SELECT code, full_name FROM wards WHERE district_code = ?";
        return jdbcTemplate.query(query, new Object[]{districtCode}, (rs, rowNum) -> new AddressResponse(
                rs.getInt("code"),
                rs.getString("full_name")
        ));
    }


}
