package com.dulara.figure_controller.repository.oracle;

import com.dulara.figure_controller.dbConnection.JDBC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductRepository {
    private final JdbcTemplate oracleJdbcTemplate;

    public ProductRepository(
            @Qualifier("oracleJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.oracleJdbcTemplate = jdbcTemplate;
    }
//    public List<Map<String, Object>> getProductsByClass(String classCode) {
//        List<Map<String, Object>> list = new ArrayList<>();
//        try{
//            Connection co = JDBC.con();
//            String query = "select PRD_CODE, PRD_DESCRIPTION from uw_m_products where PRD_ACTIVE='Y' and PRD_CLA_CODE=?";
//            PreparedStatement stmt = co.prepareStatement(query);
//            stmt.setString(1, classCode);
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                Map<String, Object> row = new HashMap<>();
//                row.put("PRD_CODE", rs.getString("PRD_CODE"));
//                row.put("PRD_DESCRIPTION", rs.getString("PRD_DESCRIPTION"));
//                list.add(row);
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
    public List<Map<String, Object>> getProductsByClass(String classCode) {
        String query = """
            SELECT PRD_CODE, PRD_DESCRIPTION 
            FROM uw_m_products 
            WHERE PRD_ACTIVE = 'Y' 
              AND PRD_CLA_CODE = ?
            """;

        return oracleJdbcTemplate.query(
                query,
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("PRD_CODE", rs.getString("PRD_CODE"));
                    row.put("PRD_DESCRIPTION", rs.getString("PRD_DESCRIPTION"));
                    return row;
                },
                classCode
        );
    }
}

