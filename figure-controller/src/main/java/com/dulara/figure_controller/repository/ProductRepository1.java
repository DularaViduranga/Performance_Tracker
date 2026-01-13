package com.dulara.figure_controller.repository;

import com.dulara.figure_controller.dbConnection.JDBC;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductRepository1 {
    public List<Map<String, Object>> getProductsByClass(String classCode) {
        List<Map<String, Object>> list = new ArrayList<>();
        try{
            Connection co = JDBC.con();
            String query = "select PRD_CODE, PRD_DESCRIPTION from uw_m_products where PRD_ACTIVE='Y' and PRD_CLA_CODE=?";
            PreparedStatement stmt = co.prepareStatement(query);
            stmt.setString(1, classCode);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("PRD_CODE", rs.getString("PRD_CODE"));
                row.put("PRD_DESCRIPTION", rs.getString("PRD_DESCRIPTION"));
                list.add(row);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
