package com.pluralsight.dao;

import com.pluralsight.models.Shipper;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShipperDao {

    private BasicDataSource dataSource;

    public ShipperDao(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Shipper> displayShippers() {
        List<Shipper> shippers = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM shippers;");
             ResultSet resultSet = preparedStatement.executeQuery();
             )
        {   while (resultSet.next()) {
                int shipperId = resultSet.getInt("ShipperId");
                String companyName = resultSet.getString("CompanyName");
                String phone = resultSet.getString("Phone");
                Shipper shipper = new Shipper(shipperId, companyName, phone);
                shippers.add(shipper);
            }
            System.out.println("\nShipper ID        Company Name             Phone");
            System.out.println("---------- -------------------------- --------------");
            for (Shipper shipper : shippers)
                System.out.printf("%-11d %-28s %15s\n", shipper.getShipperId(), shipper.getCompanyName(), shipper.getPhone());
        } catch (SQLException e) { throw new RuntimeException("Database error: " + e.getMessage(), e); }
        return shippers;
    }

    public int insertShipper(String inputCompanyName, String inputPhone) {
        String sql = "INSERT INTO shippers (CompanyName, Phone) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)
            )
        {   preparedStatement.setString(1, inputCompanyName);
            preparedStatement.setString(2, inputPhone);
            preparedStatement.executeUpdate();
            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) { throw new RuntimeException("Error inserting shipper: " + e.getMessage(), e); }
        return -1;
    }

    public void updateShipperPhone(int inputId, String inputPhone) {
        String sql = "UPDATE shippers SET Phone = ? WHERE ShipperId = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)
            )
        {   preparedStatement.setString(1, inputPhone);
            preparedStatement.setInt(2, inputId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Error updating phone: " + e.getMessage(), e); }
    }

    public void deleteShipper(int inputId) {
        String sql = "DELETE FROM shippers WHERE ShipperId = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)
            )
        {   preparedStatement.setInt(1, inputId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Error deleting shipper: " + e.getMessage(), e); }
    }
}