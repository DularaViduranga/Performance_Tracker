package com.dulara.figure_controller.repository.oracle;

import com.dulara.figure_controller.dbConnection.JDBC;
import com.dulara.figure_controller.dto.myFigure.MyGWPResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class MyFiguresRepository {
    private final JdbcTemplate oracleJdbcTemplate;

    public MyFiguresRepository(
            @Qualifier("oracleJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.oracleJdbcTemplate = jdbcTemplate;
    }

    public List<MyGWPResponseDTO> getGWPData(String intermediaryCode, String start, String end) {

        String sql = """
            SELECT GLT_POLICY_NO,\s
                       SUM(PREMIUM) as PREMIUM,\s
                       SUM(SRCC) as SRCC,\s
                       SUM(TC) as TC
                FROM SICL.SDU_POLICIES_GWP
                WHERE INTERMEDIARY_CODE = ?
                  AND GLT_REF_DATE BETWEEN TO_DATE(?, 'DD-MON-YY') AND TO_DATE(?, 'DD-MON-YY')
                GROUP BY GLT_POLICY_NO
""";

        return oracleJdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    MyGWPResponseDTO dto = new MyGWPResponseDTO();
                    dto.setPolicyNumber(rs.getString("GLT_POLICY_NO"));
                    dto.setBasic(rs.getDouble("PREMIUM"));
                    dto.setSrcc(rs.getDouble("SRCC"));
                    dto.setTc(rs.getDouble("TC"));
                    return dto;
                },
                intermediaryCode,
                start,
                end
        );
    }

//    public List<Map<String, Object>> getGWPDetailedData(String intermidiaryCode, String start, String end, String productCode, String cla_Code) {
//        List<Map<String, Object>> list = new ArrayList<>();
//
//        try {
//            Connection co = JDBC.con();
//
//            StringBuilder query = new StringBuilder(
//                    "SELECT * FROM SICL.SDU_POLICIES_GWP WHERE INTERMEDIARY_CODE = '"
//                            + intermidiaryCode + "' "
//            );
//
//            // Only add CLA_CODE filter if it's not "all"
//            if (cla_Code != null && !cla_Code.equalsIgnoreCase("ALL")) {
//                query.append("AND CLA_CODE = '").append(cla_Code).append("' ");
//            }
//
//            // Only add GLT_PRODUCT filter if it's not "all"
//            if (productCode != null && !productCode.equalsIgnoreCase("ALL")) {
//                query.append("AND GLT_PRODUCT = '").append(productCode).append("' ");
//            }
//
//            query.append("AND GLT_REF_DATE BETWEEN TO_DATE('")
//                    .append(start).append("','DD-MON-YY') ")
//                    .append("AND TO_DATE('").append(end).append("','DD-MON-YY')");
//
//
//
//            System.out.println(query);
//            Statement stmt = co.createStatement();
//            ResultSet rs = stmt.executeQuery(query.toString());
//
//            while (rs.next()) {
//                Map<String, Object> row = new HashMap<>();
//                row.put("GLT_POLICY_NO", rs.getString("GLT_POLICY_NO"));
//                row.put("GLT_PRODUCT", rs.getString("GLT_PRODUCT"));
//                row.put("CUS_NAME", rs.getString("CUS_NAME"));
//                row.put("GLT_REF_2", rs.getString("GLT_REF_2"));
//                row.put("GLT_SEG_4", rs.getString("GLT_SEG_4"));
//                row.put("DEB_POL_PERIOD_FROM", rs.getString("DEB_POL_PERIOD_FROM"));
//                row.put("DEB_POL_PERIOD_TO", rs.getString("DEB_POL_PERIOD_TO"));
//                row.put("DEB_TRAN_TYPE", rs.getString("DEB_TRAN_TYPE"));
//                row.put("GLT_REF_DATE", rs.getString("GLT_REF_DATE"));
//                row.put("DEB_SUM_INSURED", rs.getDouble("DEB_SUM_INSURED"));
//                row.put("DEB_TRN_DATE", rs.getString("DEB_TRN_DATE"));
//                row.put("CLA_DESCRIPTION", rs.getString("CLA_DESCRIPTION"));
//                row.put("PREMIUM", rs.getDouble("PREMIUM"));
//                row.put("SRCC", rs.getDouble("SRCC"));
//                row.put("TC", rs.getDouble("TC"));
//                list.add(row);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
public List<Map<String, Object>> getGWPDetailedData(String intermidiaryCode, String start, String end, String productCode, String cla_Code) {
    StringBuilder sqlBuilder = new StringBuilder(
            "SELECT * FROM SICL.SDU_POLICIES_GWP WHERE INTERMEDIARY_CODE = ? "
    );

    List<Object> params = new ArrayList<>();
    params.add(intermidiaryCode);

    // Only add CLA_CODE filter if it's not "all"
    if (cla_Code != null && !cla_Code.equalsIgnoreCase("ALL")) {
        sqlBuilder.append("AND CLA_CODE = ? ");
        params.add(cla_Code);
    }

    // Only add GLT_PRODUCT filter if it's not "all"
    if (productCode != null && !productCode.equalsIgnoreCase("ALL")) {
        sqlBuilder.append("AND GLT_PRODUCT = ? ");
        params.add(productCode);
    }

    sqlBuilder.append("AND GLT_REF_DATE BETWEEN TO_DATE(?, 'DD-MON-YY') AND TO_DATE(?, 'DD-MON-YY')");
    params.add(start);
    params.add(end);

    String sql = sqlBuilder.toString();
    System.out.println("Generated SQL: " + sql);
    System.out.println("Parameters: " + params);

    return oracleJdbcTemplate.query(
            sql,
            (rs, rowNum) -> {
                Map<String, Object> row = new HashMap<>();
                row.put("GLT_POLICY_NO", rs.getString("GLT_POLICY_NO"));
                row.put("GLT_PRODUCT", rs.getString("GLT_PRODUCT"));
                row.put("CUS_NAME", rs.getString("CUS_NAME"));
                row.put("GLT_REF_2", rs.getString("GLT_REF_2"));
                row.put("GLT_SEG_4", rs.getString("GLT_SEG_4"));
                row.put("DEB_POL_PERIOD_FROM", rs.getString("DEB_POL_PERIOD_FROM"));
                row.put("DEB_POL_PERIOD_TO", rs.getString("DEB_POL_PERIOD_TO"));
                row.put("DEB_TRAN_TYPE", rs.getString("DEB_TRAN_TYPE"));
                row.put("GLT_REF_DATE", rs.getString("GLT_REF_DATE"));
                row.put("DEB_SUM_INSURED", rs.getDouble("DEB_SUM_INSURED"));
                row.put("DEB_TRN_DATE", rs.getString("DEB_TRN_DATE"));
                row.put("CLA_DESCRIPTION", rs.getString("CLA_DESCRIPTION"));
                row.put("PREMIUM", rs.getDouble("PREMIUM"));
                row.put("SRCC", rs.getDouble("SRCC"));
                row.put("TC", rs.getDouble("TC"));
                return row;
            },
            params.toArray()
    );
}
    /// ///////////////////////////////////////////////////////////////////////////////// ////////////////////////////////////////////////////////
    public List<Map<String, Object>> getClassPerformanceData(String intermediaryCode, String year, String month) {

        List<String> allClasses = Arrays.asList("MT", "MS", "MC", "FI", "EG", "MD");
        List<Map<String, Object>> resultList = new ArrayList<>();

        // Step 1: Generate start and end dates from year and month
        String[] dateRange = generateDateRange(year, month);
        String startDate = dateRange[0];
        String endDate = dateRange[1];

        // Step 2: Fetch all targets
        Map<String, Double> targetMap = getTargetsByClass(intermediaryCode, year, month);

        // Step 3: Fetch all achieved values
        Map<String, Double> achievedMap = getAchievedByClass(intermediaryCode, startDate, endDate);

        // Step 4: Build the result list with Map objects
        for (String classCode : allClasses) {
            Map<String, Object> classData = new HashMap<>();
            classData.put("classCode", classCode);
            classData.put("target", targetMap.getOrDefault(classCode, 0.0));
            classData.put("achieved", achievedMap.getOrDefault(classCode, 0.0));

            resultList.add(classData);
        }

        return resultList;
    }

    // Helper Method: Generate Start and End Dates
    private String[] generateDateRange(String year, String month) {
        String[] monthNames = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN",
                "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        // Convert month string to integer
        int monthInt = Integer.parseInt(month);

        // Check for leap year
        int yearInt = Integer.parseInt(year);
        if (monthInt == 2 && isLeapYear(yearInt)) {
            daysInMonth[1] = 29;
        }

        String monthName = monthNames[monthInt - 1];
        String yearShort = year.substring(2); // Get last 2 digits (e.g., "25" from "2025")

        String startDate = String.format("01-%s-%s", monthName, yearShort);
        String endDate = String.format("%02d-%s-%s", daysInMonth[monthInt - 1], monthName, yearShort);

        return new String[]{startDate, endDate};
    }

    // Helper Method: Check Leap Year
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    // Helper Method: Fetch Targets by Class
//    private Map<String, Double> getTargetsByClass(String personCode, String year, String month) {
//        Map<String, Double> targetMap = new HashMap<>();
//
//        String sql = "SELECT BBT_STY_CODE, BBT_TOT_VALUE " +
//                "FROM SICL.sm_T_BRA_Budget " +
//                "WHERE BBT_TAR_PERSON_CODE = ? " +
//                "  AND BBT_YEAR = ? " +
//                "  AND BBT_MONTH = ?";
//
//        try (Connection co = JDBC.con();
//             PreparedStatement stmt = co.prepareStatement(sql)) {
//
//            stmt.setString(1, personCode);
//            stmt.setString(2, year);
//            stmt.setString(3, month);
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    String classCode = rs.getString("BBT_STY_CODE");
//                    double target = rs.getDouble("BBT_TOT_VALUE");
//                    targetMap.put(classCode, target);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return targetMap;
//    }

    private Map<String, Double> getTargetsByClass(String personCode, String year, String month) {
        String sql = """
        SELECT BBT_STY_CODE, BBT_TOT_VALUE 
        FROM SICL.sm_T_BRA_Budget 
        WHERE BBT_TAR_PERSON_CODE = ? 
          AND BBT_YEAR = ? 
          AND BBT_MONTH = ?
        """;

        Map<String, Double> targetMap = oracleJdbcTemplate.query(
                        sql,
                        (rs, rowNum) -> {
                            String classCode = rs.getString("BBT_STY_CODE");
                            double target = rs.getDouble("BBT_TOT_VALUE");
                            return Map.entry(classCode, target);
                        },
                        personCode, year, month
                ).stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
        System.out.println(targetMap);
        return targetMap;
//        return oracleJdbcTemplate.query(
//                        sql,
//                        (rs, rowNum) -> {
//                            String classCode = rs.getString("BBT_STY_CODE");
//                            double target = rs.getDouble("BBT_TOT_VALUE");
//                            return Map.entry(classCode, target);
//                        },
//                        personCode, year, month
//                ).stream()
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey,
//                        Map.Entry::getValue
//                ));

    }

    // Helper Method: Fetch Achieved by Class
//    private Map<String, Double> getAchievedByClass(String personCode, String startDate, String endDate) {
//        Map<String, Double> achievedMap = new HashMap<>();
//
//        List<String> allClasses = Arrays.asList("MT", "MS", "MC", "FI", "EG", "MD");
//
//        String sql = "SELECT SUM(PREMIUM + SRCC + TC) as TOTAL_GWP " +
//                "FROM SICL.SDU_POLICIES_GWP " +
//                "WHERE INTERMEDIARY_CODE = ? " +
//                "  AND GLT_REF_DATE BETWEEN TO_DATE(?, 'DD-MON-YY') " +
//                "                       AND TO_DATE(?, 'DD-MON-YY') " +
//                "  AND CLA_CODE = ?";
//
//        for (String classCode : allClasses) {
//            try (Connection co = JDBC.con();
//                 PreparedStatement stmt = co.prepareStatement(sql)) {
//
//                stmt.setString(1, personCode);
//                stmt.setString(2, startDate);
//                stmt.setString(3, endDate);
//                stmt.setString(4, classCode);
//
//                try (ResultSet rs = stmt.executeQuery()) {
//                    if (rs.next()) {
//                        double achieved = rs.getDouble("TOTAL_GWP");
//                        achievedMap.put(classCode, achieved);
//                    }
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return achievedMap;
//    }

    private Map<String, Double> getAchievedByClass(String personCode, String startDate, String endDate) {
        List<String> allClasses = Arrays.asList("MT", "MS", "MC", "FI", "EG", "MD");

        String sql = """
        SELECT CLA_CODE, SUM(PREMIUM + SRCC + TC) as TOTAL_GWP 
        FROM SICL.SDU_POLICIES_GWP 
        WHERE INTERMEDIARY_CODE = ? 
          AND GLT_REF_DATE BETWEEN TO_DATE(?, 'DD-MON-YY') 
                               AND TO_DATE(?, 'DD-MON-YY') 
          AND CLA_CODE IN ('MT', 'MS', 'MC', 'FI', 'EG', 'MD')
        GROUP BY CLA_CODE
        """;

        Map<String, Double> achievedMap = oracleJdbcTemplate.query(
                sql,
                new ResultSetExtractor<Map<String, Double>>() {
                    @Override
                    public Map<String, Double> extractData(ResultSet rs) throws SQLException {
                        Map<String, Double> map = new HashMap<>();
                        // Initialize all classes with 0.0
                        for (String classCode : allClasses) {
                            map.put(classCode, 0.0);
                        }
                        // Update with actual values from database
                        while (rs.next()) {
                            String classCode = rs.getString("CLA_CODE");
                            double achieved = rs.getDouble("TOTAL_GWP");
                            map.put(classCode, achieved);
                        }
                        return map;
                    }
                },
                personCode, startDate, endDate
        );

        return achievedMap;
    }

//    private Map<String, Double> getAchievedByClassForBranch(String branchCode, String startDate, String endDate) {
//        Map<String, Double> achievedMap = new HashMap<>();
//
//        List<String> allClasses = Arrays.asList("MT", "MS", "MC", "FI", "EG", "MD");
//
//        String sql = "SELECT SUM(PREMIUM + SRCC + TC) as TOTAL_GWP " +
//                "FROM SICL.SDU_POLICIES_GWP " +
//                "WHERE GLT_SEG_4  = ? " +
//                "  AND GLT_REF_DATE BETWEEN TO_DATE(?, 'DD-MON-YY') " +
//                "                       AND TO_DATE(?, 'DD-MON-YY') " +
//                "  AND CLA_CODE = ?";
//
//        for (String classCode : allClasses) {
//            try (Connection co = JDBC.con();
//                 PreparedStatement stmt = co.prepareStatement(sql)) {
//
//                stmt.setString(1, branchCode);
//                stmt.setString(2, startDate);
//                stmt.setString(3, endDate);
//                stmt.setString(4, classCode);
//
//                try (ResultSet rs = stmt.executeQuery()) {
//                    if (rs.next()) {
//                        double achieved = rs.getDouble("TOTAL_GWP");
//                        achievedMap.put(classCode, achieved);
//                    }
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return achievedMap;
//    }
private Map<String, Double> getAchievedByClassForBranch(String branchCode, String startDate, String endDate) {
    List<String> allClasses = Arrays.asList("MT", "MS", "MC", "FI", "EG", "MD");
    Map<String, Double> achievedMap = new HashMap<>();

    // Single query with GROUP BY for better performance
    String sql = """
        SELECT CLA_CODE, SUM(PREMIUM + SRCC + TC) as TOTAL_GWP 
        FROM SICL.SDU_POLICIES_GWP 
        WHERE GLT_SEG_4 = ? 
          AND GLT_REF_DATE BETWEEN TO_DATE(?, 'DD-MON-YY') 
                               AND TO_DATE(?, 'DD-MON-YY') 
          AND CLA_CODE IN ('MT', 'MS', 'MC', 'FI', 'EG', 'MD')
        GROUP BY CLA_CODE
        """;

    // Execute single query and process results
    oracleJdbcTemplate.query(
            sql,
            (rs) -> {
                while (rs.next()) {
                    String classCode = rs.getString("CLA_CODE");
                    double achieved = rs.getDouble("TOTAL_GWP");
                    achievedMap.put(classCode, achieved);
                }
                return null;
            },
            branchCode, startDate, endDate
    );

    // Ensure all classes are present in the map (default to 0.0 if not found)
    for (String classCode : allClasses) {
        achievedMap.putIfAbsent(classCode, 0.0);
    }

    return achievedMap;
}
    /// //////////////////////////////////////////////////////////////
//    private Map<String, Double> getTargetsByClassForPeriod(
//            String personCode, String fromYear, String fromMonth, String toYear, String toMonth) {
//        Map<String, Double> targetMap = new HashMap<>();
//
//        String sql =
//                "SELECT BBT_STY_CODE, SUM(BBT_TOT_VALUE) TARGET " +
//                        "FROM SICL.SM_T_BRA_BUDGET " +
//                        "WHERE BBT_TAR_PERSON_CODE = ? " +
//                        "AND ((BBT_YEAR > ? OR (BBT_YEAR = ? AND BBT_MONTH >= ?)) " +
//                        " AND (BBT_YEAR < ? OR (BBT_YEAR = ? AND BBT_MONTH <= ?))) " +
//                        "GROUP BY BBT_STY_CODE";
//
//        try (Connection co = JDBC.con();
//             PreparedStatement ps = co.prepareStatement(sql)) {
//
//            ps.setString(1, personCode);
//            ps.setString(2, fromYear);
//            ps.setString(3, fromYear);
//            ps.setString(4, fromMonth);
//            ps.setString(5, toYear);
//            ps.setString(6, toYear);
//            ps.setString(7, toMonth);
//
//            try (ResultSet rs = ps .executeQuery()) {
//                while (rs.next()) {
//                    String classCode = rs.getString("BBT_STY_CODE");
//                    double target = rs.getDouble("TARGET");
//                    targetMap.put(classCode, target);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return targetMap;
//    }
//
//    private Map<String, Double> getTargetsByClassForPeriodForBranch(
//            String branchCode, String fromYear, String fromMonth, String toYear, String toMonth) {
//        Map<String, Double> targetMap = new HashMap<>();
//
//        String sql =
//                "SELECT BBT_STY_CODE, SUM(BBT_TOT_VALUE) TARGET " +
//                        "FROM SICL.SM_T_BRA_BUDGET " +
//                        "WHERE BBT_BRN_CODE = ? " +
//                        "AND ((BBT_YEAR > ? OR (BBT_YEAR = ? AND BBT_MONTH >= ?)) " +
//                        " AND (BBT_YEAR < ? OR (BBT_YEAR = ? AND BBT_MONTH <= ?))) " +
//                        "GROUP BY BBT_STY_CODE";
//
//        try (Connection co = JDBC.con();
//             PreparedStatement ps = co.prepareStatement(sql)) {
//
//            ps.setString(1, branchCode);
//            ps.setString(2, fromYear);
//            ps.setString(3, fromYear);
//            ps.setString(4, fromMonth);
//            ps.setString(5, toYear);
//            ps.setString(6, toYear);
//            ps.setString(7, toMonth);
//
//            try (ResultSet rs = ps .executeQuery()) {
//                while (rs.next()) {
//                    String classCode = rs.getString("BBT_STY_CODE");
//                    double target = rs.getDouble("TARGET");
//                    targetMap.put(classCode, target);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return targetMap;
//    }

    private Map<String, Double> getTargetsByClassForPeriod(
            String personCode, String fromYear, String fromMonth, String toYear, String toMonth) {

        String sql = """
        SELECT BBT_STY_CODE, SUM(BBT_TOT_VALUE) TARGET 
        FROM SICL.SM_T_BRA_BUDGET 
        WHERE BBT_TAR_PERSON_CODE = ? 
        AND ((BBT_YEAR > ? OR (BBT_YEAR = ? AND BBT_MONTH >= ?)) 
         AND (BBT_YEAR < ? OR (BBT_YEAR = ? AND BBT_MONTH <= ?))) 
        GROUP BY BBT_STY_CODE
        """;

        Map<String, Double> targetMap = new HashMap<>();

        oracleJdbcTemplate.query(
                sql,
                (rs) -> {
                    while (rs.next()) {
                        targetMap.put(
                                rs.getString("BBT_STY_CODE"),
                                rs.getDouble("TARGET")
                        );
                    }
                    return null;
                },
                personCode, fromYear, fromYear, fromMonth, toYear, toYear, toMonth
        );

        return targetMap;
    }

    private Map<String, Double> getTargetsByClassForPeriodForBranch(
            String branchCode, String fromYear, String fromMonth, String toYear, String toMonth) {

        String sql = """
        SELECT BBT_STY_CODE, SUM(BBT_TOT_VALUE) TARGET 
        FROM SICL.SM_T_BRA_BUDGET 
        WHERE BBT_BRN_CODE = ? 
        AND ((BBT_YEAR > ? OR (BBT_YEAR = ? AND BBT_MONTH >= ?)) 
         AND (BBT_YEAR < ? OR (BBT_YEAR = ? AND BBT_MONTH <= ?))) 
        GROUP BY BBT_STY_CODE
        """;

        Map<String, Double> targetMap = new HashMap<>();

        oracleJdbcTemplate.query(
                sql,
                (rs) -> {
                    while (rs.next()) {
                        targetMap.put(
                                rs.getString("BBT_STY_CODE"),
                                rs.getDouble("TARGET")
                        );
                    }
                    return null;
                },
                branchCode, fromYear, fromYear, fromMonth, toYear, toYear, toMonth
        );

        return targetMap;
    }


    public List<Map<String, Object>> getClassPerformanceDataForPeriod(String intermediaryCode, String start, String end) {

        List<String> allClasses = Arrays.asList("MT", "MS", "MC", "FI", "EG", "MD");
        List<Map<String, Object>> resultList = new ArrayList<>();

        String fromYear = String.valueOf(DatePartsUtil.getYearNumber(start));
        String fromMonth = String.valueOf(DatePartsUtil.getMonthNumber(start));
        String toYear = String.valueOf(DatePartsUtil.getYearNumber(end));
        String toMonth = String.valueOf(DatePartsUtil.getMonthNumber(end));
        System.out.println(fromYear + "-" + fromMonth + " to " + toYear + "-" + toMonth);


        // Step 2: Fetch all targets
        Map<String, Double> targetMap = getTargetsByClassForPeriod(intermediaryCode, fromYear, fromMonth, toYear, toMonth);

        // Step 3: Fetch all achieved values
        Map<String, Double> achievedMap = getAchievedByClass(intermediaryCode, start, end);

        // Step 4: Build the result list with Map objects
        for (String classCode : allClasses) {
            Map<String, Object> classData = new HashMap<>();
            classData.put("classCode", classCode);
            classData.put("target", targetMap.getOrDefault(classCode, 0.0));
            classData.put("achieved", achievedMap.getOrDefault(classCode, 0.0));

            resultList.add(classData);
        }

        return resultList;
    }

    public List<Map<String, Object>> getClassPerformanceDataForPeriodForBranch(String branchCode, String start, String end) {

        List<String> allClasses = Arrays.asList("MT", "MS", "MC", "FI", "EG", "MD");
        List<Map<String, Object>> resultList = new ArrayList<>();

        String fromYear = String.valueOf(DatePartsUtil.getYearNumber(start));
        String fromMonth = String.valueOf(DatePartsUtil.getMonthNumber(start));
        String toYear = String.valueOf(DatePartsUtil.getYearNumber(end));
        String toMonth = String.valueOf(DatePartsUtil.getMonthNumber(end));
        System.out.println(fromYear + "-" + fromMonth + " to " + toYear + "-" + toMonth);


        // Step 2: Fetch all targets
        Map<String, Double> targetMap = getTargetsByClassForPeriodForBranch(branchCode, fromYear, fromMonth, toYear, toMonth);

        // Step 3: Fetch all achieved values
        Map<String, Double> achievedMap = getAchievedByClassForBranch(branchCode, start, end);

        // Step 4: Build the result list with Map objects
        for (String classCode : allClasses) {
            Map<String, Object> classData = new HashMap<>();
            classData.put("classCode", classCode);
            classData.put("target", targetMap.getOrDefault(classCode, 0.0));
            classData.put("achieved", achievedMap.getOrDefault(classCode, 0.0));

            resultList.add(classData);
        }

        return resultList;
    }

    public class DatePartsUtil {

        private static final DateTimeFormatter FORMATTER =
                new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern("dd-MMM-yy")
                        .toFormatter(Locale.ENGLISH);


        // 01-DEC-25 -> 12
        public static int getMonthNumber(String dateStr) {
            LocalDate date = LocalDate.parse(dateStr, FORMATTER);
            return date.getMonthValue();
        }

        // 01-DEC-25 -> 2025
        public static int getYearNumber(String dateStr) {
            LocalDate date = LocalDate.parse(dateStr, FORMATTER);
            return date.getYear();
        }
    }

    // ///////////////////////////////////////////////////////////////////////////////// ////////////////////////////////////////////////////////

    public List<Map<String, Object>> getCashCollection(String intermediaryCode, String start, String end) {

        String sql = """
            SELECT BRANCH,
                   CUS_NAME,
                   DEB_TRAN_TYPE,
                   SETTLED_AMOUNT,
                   DEB_POLICY_NO,
                   DEB_CLA_CODE,
                   PRO_DESC
            FROM SICL.SDU_RPT_V_Cash A
            WHERE INTER_CODE = ?
              AND trunc(A.DST_TRN_DATE) >= TO_DATE(?, 'YYYY/MM/DD')
              AND trunc(A.DST_TRN_DATE) <= TO_DATE(?, 'YYYY/MM/DD')
              AND (DST_STATUS = 'A'
                   OR (DST_STATUS = 'I'
                       AND trunc(DST_CAN_DATE) >= TO_DATE(?, 'YYYY/MM/DD')))
            """;



//        try (Connection co = JDBC.con();
//             PreparedStatement stmt = co.prepareStatement(sql)) {
//
//            stmt.setString(1, intermediaryCode);
//            stmt.setString(2, start);
//            stmt.setString(3, end);
//            stmt.setString(4, end);
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    Map<String, Object> row = new HashMap<>();
//                    row.put("BRANCH", rs.getString("BRANCH"));
//                    row.put("CUS_NAME", rs.getString("CUS_NAME"));
//                    row.put("DEB_TRAN_TYPE", rs.getString("DEB_TRAN_TYPE"));
//                    row.put("SETTLED_AMOUNT", rs.getDouble("SETTLED_AMOUNT"));
//                    row.put("DEB_POLICY_NO", rs.getString("DEB_POLICY_NO"));
//                    row.put("DEB_CLA_CODE", rs.getString("DEB_CLA_CODE"));
//                    row.put("PRO_DESC", rs.getString("PRO_DESC"));
//                    list.add(row);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(list);
//        return li
        return oracleJdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("BRANCH", rs.getString("BRANCH"));
                    row.put("CUS_NAME", rs.getString("CUS_NAME"));
                    row.put("DEB_TRAN_TYPE", rs.getString("DEB_TRAN_TYPE"));
                    row.put("SETTLED_AMOUNT", rs.getDouble("SETTLED_AMOUNT"));
                    row.put("DEB_POLICY_NO", rs.getString("DEB_POLICY_NO"));
                    row.put("DEB_CLA_CODE", rs.getString("DEB_CLA_CODE"));
                    row.put("PRO_DESC", rs.getString("PRO_DESC"));
                    return row;
                },
                intermediaryCode,
                start,
                end,
                end
        );

    }

    public List<Map<String, Object>> getNewBusiness(String start, String end, String intermediaryCode) {
//        List<Map<String, Object>> list = new ArrayList<>();
//
//        String sql = "SELECT POLICY_NO,CLASS_DESC,PRODUCT_DESC,CUSTOMER_DESC,pk_uw_m_customers.fn_get_cust_phone(CUSTOMER_CODE) Phone,\n" +
//                "       CREATED_DATE,PERIOD_FROM,PERIOD_TO,BRANCH_NAME,STATUS,TRANSACTION_DESC\n" +
//                "FROM UW_V_POL_MASTER_DTLS a\n" +
//                "WHERE TRUNC(CREATED_DATE) BETWEEN TO_DATE(?,'YYYY-MM-DD') \n" +
//                "                              AND TO_DATE(?,'YYYY-MM-DD')\n" +
//                "  AND a.TABLE_TYPE='policies'\n" +
//                "  AND INTMDRY_CODE = ?";
//
//
//        try (Connection co = JDBC.con();
//             PreparedStatement stmt = co.prepareStatement(sql)) {
//
//            stmt.setString(1, start);
//            stmt.setString(2, end);
//            stmt.setString(3, intermediaryCode);
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    Map<String, Object> row = new HashMap<>();
//                    row.put("POLICY_NO", rs.getString("POLICY_NO"));
//                    row.put("CLASS_DESC", rs.getString("CLASS_DESC"));
//                    row.put("PRODUCT_DESC", rs.getString("PRODUCT_DESC"));
//                    row.put("CUSTOMER_DESC", rs.getString("CUSTOMER_DESC"));
//                    row.put("Phone", rs.getString("Phone"));
//                    row.put("CREATED_DATE", rs.getString("CREATED_DATE"));
//                    row.put("PERIOD_FROM", rs.getString("PERIOD_FROM"));
//                    row.put("PERIOD_TO", rs.getString("PERIOD_TO"));
//                    row.put("BRANCH_NAME", rs.getString("BRANCH_NAME"));
//                    row.put("STATUS", rs.getString("STATUS"));
//                    row.put("TRANSACTION_DESC", rs.getString("TRANSACTION_DESC"));
//                    list.add(row);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(list);
//        return list;
        String sql = """
            SELECT POLICY_NO, CLASS_DESC, PRODUCT_DESC, CUSTOMER_DESC,
                   pk_uw_m_customers.fn_get_cust_phone(CUSTOMER_CODE) Phone,
                   CREATED_DATE, PERIOD_FROM, PERIOD_TO, BRANCH_NAME, STATUS, TRANSACTION_DESC
            FROM UW_V_POL_MASTER_DTLS a
            WHERE TRUNC(CREATED_DATE) BETWEEN TO_DATE(?, 'YYYY-MM-DD')
                                          AND TO_DATE(?, 'YYYY-MM-DD')
              AND a.TABLE_TYPE = 'policies'
              AND INTMDRY_CODE = ?
            """;

        return oracleJdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("POLICY_NO", rs.getString("POLICY_NO"));
                    row.put("CLASS_DESC", rs.getString("CLASS_DESC"));
                    row.put("PRODUCT_DESC", rs.getString("PRODUCT_DESC"));
                    row.put("CUSTOMER_DESC", rs.getString("CUSTOMER_DESC"));
                    row.put("Phone", rs.getString("Phone"));
                    row.put("CREATED_DATE", rs.getString("CREATED_DATE"));
                    row.put("PERIOD_FROM", rs.getString("PERIOD_FROM"));
                    row.put("PERIOD_TO", rs.getString("PERIOD_TO"));
                    row.put("BRANCH_NAME", rs.getString("BRANCH_NAME"));
                    row.put("STATUS", rs.getString("STATUS"));
                    row.put("TRANSACTION_DESC", rs.getString("TRANSACTION_DESC"));
                    return row;
                },
                start,
                end,
                intermediaryCode
        );
    }

//    public List<Map<String, Object>> getNewBusinessByBranch(String start, String end, String branchCode) {
//        List<Map<String, Object>> list = new ArrayList<>();
//
//        String sql = "SELECT POLICY_NO,CLASS_DESC,PRODUCT_DESC,CUSTOMER_DESC,pk_uw_m_customers.fn_get_cust_phone(CUSTOMER_CODE) Phone,\n" +
//                "       CREATED_DATE,PERIOD_FROM,PERIOD_TO,INTMDRY_NAME,STATUS,TRANSACTION_DESC\n" +
//                "FROM UW_V_POL_MASTER_DTLS a\n" +
//                "WHERE TRUNC(CREATED_DATE) BETWEEN TO_DATE(?,'YYYY-MM-DD') \n" +
//                "                              AND TO_DATE(?,'YYYY-MM-DD')\n" +
//                "  AND a.TABLE_TYPE='policies'\n" +
//                "  AND BRANCH_CODE = ?";
//
//
//        try (Connection co = JDBC.con();
//             PreparedStatement stmt = co.prepareStatement(sql)) {
//
//            stmt.setString(1, start);
//            stmt.setString(2, end);
//            stmt.setString(3, branchCode);
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    Map<String, Object> row = new HashMap<>();
//                    row.put("POLICY_NO", rs.getString("POLICY_NO"));
//                    row.put("CLASS_DESC", rs.getString("CLASS_DESC"));
//                    row.put("PRODUCT_DESC", rs.getString("PRODUCT_DESC"));
//                    row.put("CUSTOMER_DESC", rs.getString("CUSTOMER_DESC"));
//                    row.put("Phone", rs.getString("Phone"));
//                    row.put("CREATED_DATE", rs.getString("CREATED_DATE"));
//                    row.put("PERIOD_FROM", rs.getString("PERIOD_FROM"));
//                    row.put("PERIOD_TO", rs.getString("PERIOD_TO"));
//                    row.put("INTMDRY_NAME", rs.getString("INTMDRY_NAME"));
//                    row.put("STATUS", rs.getString("STATUS"));
//                    row.put("TRANSACTION_DESC", rs.getString("TRANSACTION_DESC"));
//                    list.add(row);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(list);
//        return list;
//    }

    public List<Map<String, Object>> getNewBusinessByBranch(String start, String end, String branchCode) {
        String sql = """
        SELECT POLICY_NO, CLASS_DESC, PRODUCT_DESC, CUSTOMER_DESC, 
               pk_uw_m_customers.fn_get_cust_phone(CUSTOMER_CODE) Phone,
               CREATED_DATE, PERIOD_FROM, PERIOD_TO, INTMDRY_NAME, STATUS, TRANSACTION_DESC
        FROM UW_V_POL_MASTER_DTLS a
        WHERE TRUNC(CREATED_DATE) BETWEEN TO_DATE(?, 'YYYY-MM-DD') 
                                      AND TO_DATE(?, 'YYYY-MM-DD')
          AND a.TABLE_TYPE = 'policies'
          AND BRANCH_CODE = ?
        """;

        List<Map<String, Object>> result = oracleJdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("POLICY_NO", rs.getString("POLICY_NO"));
                    row.put("CLASS_DESC", rs.getString("CLASS_DESC"));
                    row.put("PRODUCT_DESC", rs.getString("PRODUCT_DESC"));
                    row.put("CUSTOMER_DESC", rs.getString("CUSTOMER_DESC"));
                    row.put("Phone", rs.getString("Phone"));
                    row.put("CREATED_DATE", rs.getString("CREATED_DATE"));
                    row.put("PERIOD_FROM", rs.getString("PERIOD_FROM"));
                    row.put("PERIOD_TO", rs.getString("PERIOD_TO"));
                    row.put("INTMDRY_NAME", rs.getString("INTMDRY_NAME"));
                    row.put("STATUS", rs.getString("STATUS"));
                    row.put("TRANSACTION_DESC", rs.getString("TRANSACTION_DESC"));
                    return row;
                },
                start, end, branchCode
        );

        System.out.println(result);
        return result;
    }

    public List<Map<String, Object>> getRenewal(String start, String end, String intermediaryCode) {
        String sql = """
            SELECT POLICY_NO, pk_uw_m_customers.fn_get_cust_phone(CUSTOMER_CODE) Phone,
                   CLASS_DESC, PRODUCT_DESC, CUSTOMER_DESC,
                   CREATED_DATE, PERIOD_FROM, PERIOD_TO, BRANCH_NAME
            FROM UW_V_POL_MASTER_DTLS a
            WHERE TRUNC(PERIOD_TO) BETWEEN TO_DATE(?, 'YYYY-MM-DD')
                                       AND TO_DATE(?, 'YYYY-MM-DD')
              AND a.TABLE_TYPE = 'policies'
              AND NVL(status, 111) <> 9
              AND AUTHORIZED_DATE IS NOT NULL
              AND INTMDRY_CODE = ?
            """;

        return oracleJdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("POLICY_NO", rs.getString("POLICY_NO"));
                    row.put("Phone", rs.getString("Phone"));
                    row.put("CLASS_DESC", rs.getString("CLASS_DESC"));
                    row.put("PRODUCT_DESC", rs.getString("PRODUCT_DESC"));
                    row.put("CUSTOMER_DESC", rs.getString("CUSTOMER_DESC"));
                    row.put("CREATED_DATE", rs.getString("CREATED_DATE"));
                    row.put("PERIOD_FROM", rs.getString("PERIOD_FROM"));
                    row.put("PERIOD_TO", rs.getString("PERIOD_TO"));
                    row.put("BRANCH_NAME", rs.getString("BRANCH_NAME"));
                    return row;
                },
                start,
                end,
                intermediaryCode
        );

    }

    public List<Map<String, Object>> getCancellation(String start, String end, String intermediaryCode) {
        String sql = """
            SELECT POLICY_NO, CLASS_DESC, PRODUCT_DESC, CUSTOMER_DESC,
                   pk_uw_m_customers.fn_get_cust_phone(CUSTOMER_CODE) Phone,
                   CREATED_DATE, PERIOD_FROM, PERIOD_TO, BRANCH_NAME
            FROM UW_V_POL_MASTER_DTLS a
            WHERE TRUNC(CANCELLED_DATE) BETWEEN TO_DATE(?, 'YYYY-MM-DD')
                                            AND TO_DATE(?, 'YYYY-MM-DD')
              AND a.TABLE_TYPE = 'policies'
              AND NVL(status, 111) = 9
              AND AUTHORIZED_DATE IS NOT NULL
              AND CANCELLED_REASON_DESC = 'AUTO CANCELLATION DUE TO DEBIT OUTSTANDING'
              AND INTMDRY_CODE = ?
            """;

        return oracleJdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("POLICY_NO", rs.getString("POLICY_NO"));
                    row.put("CLASS_DESC", rs.getString("CLASS_DESC"));
                    row.put("PRODUCT_DESC", rs.getString("PRODUCT_DESC"));
                    row.put("CUSTOMER_DESC", rs.getString("CUSTOMER_DESC"));
                    row.put("Phone", rs.getString("Phone"));
                    row.put("CREATED_DATE", rs.getString("CREATED_DATE"));
                    row.put("PERIOD_FROM", rs.getString("PERIOD_FROM"));
                    row.put("PERIOD_TO", rs.getString("PERIOD_TO"));
                    row.put("BRANCH_NAME", rs.getString("BRANCH_NAME"));
                    return row;
                },
                start,
                end,
                intermediaryCode
        );
    }

//    public List<Map<String, Object>> getBranchCancellation(String formattedStart, String formattedEnd, String branchCode) {
//        List<Map<String, Object>> list = new ArrayList<>();
//
//        String sql = "SELECT POLICY_NO,CLASS_DESC,PRODUCT_DESC,CUSTOMER_DESC,pk_uw_m_customers.fn_get_cust_phone(CUSTOMER_CODE) Phone,\n" +
//                "       CREATED_DATE,PERIOD_FROM,PERIOD_TO,INTMDRY_NAME\n" +
//                "FROM UW_V_POL_MASTER_DTLS a\n" +
//                "WHERE TRUNC(CANCELLED_DATE) BETWEEN TO_DATE(?,'YYYY-MM-DD')\n" +
//                "                                AND TO_DATE(?,'YYYY-MM-DD')\n" +
//                "  AND a.TABLE_TYPE = 'policies'\n" +
//                "  AND NVL(status,111) = 9\n" +
//                "  AND AUTHORIZED_DATE IS NOT NULL\n" +
//                "  AND CANCELLED_REASON_DESC = 'AUTO CANCELLATION DUE TO DEBIT OUTSTANDING'\n" +
//                "  AND BRANCH_CODE = ?";
//
//
//        try (Connection co = JDBC.con();
//             PreparedStatement stmt = co.prepareStatement(sql)) {
//
//            stmt.setString(1, formattedStart);
//            stmt.setString(2, formattedEnd);
//            stmt.setString(3, branchCode);
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    Map<String, Object> row = new HashMap<>();
//                    row.put("POLICY_NO", rs.getString("POLICY_NO"));
//                    row.put("CLASS_DESC", rs.getString("CLASS_DESC"));
//                    row.put("PRODUCT_DESC", rs.getString("PRODUCT_DESC"));
//                    row.put("CUSTOMER_DESC", rs.getString("CUSTOMER_DESC"));
//                    row.put("Phone", rs.getString("Phone"));
//                    row.put("CREATED_DATE", rs.getString("CREATED_DATE"));
//                    row.put("PERIOD_FROM", rs.getString("PERIOD_FROM"));
//                    row.put("PERIOD_TO", rs.getString("PERIOD_TO"));
//                    row.put("INTMDRY_NAME", rs.getString("INTMDRY_NAME"));
//                    list.add(row);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(list);
//        return list;
//    }
//
//    public List<Map<String, Object>> getNewBusinessCountByTranType(String start, String end, String intermediaryCode) {
//        List<Map<String, Object>> list = new ArrayList<>();
//
//        String sql = "select TRANSACTION_DESC,count(1) CNT\n" +
//                "  from UW_V_POL_MASTER_DTLS a where trunc(CREATED_DATE) between TO_DATE(?,'YYYY-MM-DD')\n" +
//                "                                            and TO_DATE(?,'YYYY-MM-DD')  \n" +
//                "and a.TABLE_TYPE='policies'  and  INTMDRY_CODE=?\n" +
//                "group by TRANSACTION_DESC";
//
//        try (Connection co = JDBC.con();
//             PreparedStatement stmt = co.prepareStatement(sql)) {
//
//            stmt.setString(1, start);
//            stmt.setString(2, end);
//            stmt.setString(3, intermediaryCode);
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    Map<String, Object> row = new HashMap<>();
//                    row.put("TRANSACTION_DESC", rs.getString("TRANSACTION_DESC"));
//                    row.put("CNT", rs.getString("CNT"));
//                    list.add(row);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(list);
//        return list;
//    }
//
//    public List<Map<String, Object>> getOutstanding(String intermediaryCode) {
//        List<Map<String, Object>> list = new ArrayList<>();
//
//        String sql = "select POLICY_NO,CLASS_DESC,PRODUCT_DESC,CUSTOMER_DESC,CUSTOMER_ADDRESS,\n" +
//                "v.CREATED_DATE,v.PERIOD_FROM,v.PERIOD_TO,BRANCH_NAME,status,DEB_TOTAL_AMOUNT," +
//                "pk_uw_m_customers.fn_get_cust_phone(CUSTOMER_CODE) Phone,DEB_TOTAL_SETTLED," +
//                "DEB_AUTO_CAN_DAY_PARAM,TRANSACTION_DESC\n" +
//                "  from UW_V_POL_MASTER_DTLS v,rc_t_debit_note d where nvl(status,111) <>9   \n" +
//                "and SEQUENCE_NO=DEB_POL_SEQ_NO and DEB_SETTLED='N'\n" +
//                "and trunc(v.PERIOD_TO)>=trunc(sysdate) and INTMDRY_CODE=? \n" +
//                "order by POLICY_NO,CREATED_DATE";
//
//        try (
//                Connection con = JDBC.con();
//                PreparedStatement stmt = con.prepareStatement(sql)
//        ) {
//            stmt.setString(1, intermediaryCode);
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    Map<String, Object> row = new HashMap<>();
//                    row.put("POLICY_NO", rs.getString("POLICY_NO"));
//                    row.put("CLASS_DESC", rs.getString("CLASS_DESC"));
//                    row.put("PRODUCT_DESC", rs.getString("PRODUCT_DESC"));
//                    row.put("CUSTOMER_DESC", rs.getString("CUSTOMER_DESC"));
//                    row.put("Phone", rs.getString("Phone"));
//                    row.put("CREATED_DATE", rs.getString("CREATED_DATE"));
//                    row.put("PERIOD_FROM", rs.getString("PERIOD_FROM"));
//                    row.put("PERIOD_TO", rs.getString("PERIOD_TO"));
//                    row.put("BRANCH_NAME", rs.getString("BRANCH_NAME"));
//                    row.put("status", rs.getString("status"));
//                    row.put("DEB_TOTAL_AMOUNT", rs.getDouble("DEB_TOTAL_AMOUNT"));
//                    row.put("DEB_TOTAL_SETTLED", rs.getDouble("DEB_TOTAL_SETTLED"));
//                    row.put("DEB_AUTO_CAN_DAY_PARAM", rs.getString("DEB_AUTO_CAN_DAY_PARAM"));
//                    row.put("TRANSACTION_DESC", rs.getString("TRANSACTION_DESC"));
//
//                    list.add(row);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(list);
//        return list;
//
//    }

    public List<Map<String, Object>> getBranchCancellation(String formattedStart, String formattedEnd, String branchCode) {
        String sql = """
        SELECT POLICY_NO, CLASS_DESC, PRODUCT_DESC, CUSTOMER_DESC,
               pk_uw_m_customers.fn_get_cust_phone(CUSTOMER_CODE) Phone,
               CREATED_DATE, PERIOD_FROM, PERIOD_TO, INTMDRY_NAME
        FROM UW_V_POL_MASTER_DTLS a
        WHERE TRUNC(CANCELLED_DATE) BETWEEN TO_DATE(?, 'YYYY-MM-DD')
                                        AND TO_DATE(?, 'YYYY-MM-DD')
          AND a.TABLE_TYPE = 'policies'
          AND NVL(status, 111) = 9
          AND AUTHORIZED_DATE IS NOT NULL
          AND CANCELLED_REASON_DESC = 'AUTO CANCELLATION DUE TO DEBIT OUTSTANDING'
          AND BRANCH_CODE = ?
        """;

        List<Map<String, Object>> result = oracleJdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("POLICY_NO", rs.getString("POLICY_NO"));
                    row.put("CLASS_DESC", rs.getString("CLASS_DESC"));
                    row.put("PRODUCT_DESC", rs.getString("PRODUCT_DESC"));
                    row.put("CUSTOMER_DESC", rs.getString("CUSTOMER_DESC"));
                    row.put("Phone", rs.getString("Phone"));
                    row.put("CREATED_DATE", rs.getString("CREATED_DATE"));
                    row.put("PERIOD_FROM", rs.getString("PERIOD_FROM"));
                    row.put("PERIOD_TO", rs.getString("PERIOD_TO"));
                    row.put("INTMDRY_NAME", rs.getString("INTMDRY_NAME"));
                    return row;
                },
                formattedStart, formattedEnd, branchCode
        );

        System.out.println(result);
        return result;
    }

    public List<Map<String, Object>> getNewBusinessCountByTranType(String start, String end, String intermediaryCode) {
        String sql = """
        SELECT TRANSACTION_DESC, COUNT(1) CNT
        FROM UW_V_POL_MASTER_DTLS a
        WHERE TRUNC(CREATED_DATE) BETWEEN TO_DATE(?, 'YYYY-MM-DD')
                                      AND TO_DATE(?, 'YYYY-MM-DD')
          AND a.TABLE_TYPE = 'policies'
          AND INTMDRY_CODE = ?
        GROUP BY TRANSACTION_DESC
        """;

        List<Map<String, Object>> result = oracleJdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("TRANSACTION_DESC", rs.getString("TRANSACTION_DESC"));
                    // Convert to integer if needed, or keep as string
                    row.put("CNT", rs.getString("CNT"));
                    return row;
                },
                start, end, intermediaryCode
        );

        System.out.println(result);
        return result;
    }

    public List<Map<String, Object>> getOutstanding(String intermediaryCode) {
        String sql = """
        SELECT POLICY_NO, CLASS_DESC, PRODUCT_DESC, CUSTOMER_DESC, CUSTOMER_ADDRESS,
               v.CREATED_DATE, v.PERIOD_FROM, v.PERIOD_TO, BRANCH_NAME, status, DEB_TOTAL_AMOUNT,
               pk_uw_m_customers.fn_get_cust_phone(CUSTOMER_CODE) Phone, DEB_TOTAL_SETTLED,
               DEB_AUTO_CAN_DAY_PARAM, TRANSACTION_DESC
        FROM UW_V_POL_MASTER_DTLS v, rc_t_debit_note d
        WHERE NVL(status, 111) <> 9
          AND SEQUENCE_NO = DEB_POL_SEQ_NO
          AND DEB_SETTLED = 'N'
          AND TRUNC(v.PERIOD_TO) >= TRUNC(SYSDATE)
          AND INTMDRY_CODE = ?
        ORDER BY POLICY_NO, CREATED_DATE
        """;

        List<Map<String, Object>> result = oracleJdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("POLICY_NO", rs.getString("POLICY_NO"));
                    row.put("CLASS_DESC", rs.getString("CLASS_DESC"));
                    row.put("PRODUCT_DESC", rs.getString("PRODUCT_DESC"));
                    row.put("CUSTOMER_DESC", rs.getString("CUSTOMER_DESC"));
                    row.put("Phone", rs.getString("Phone"));
                    row.put("CREATED_DATE", rs.getString("CREATED_DATE"));
                    row.put("PERIOD_FROM", rs.getString("PERIOD_FROM"));
                    row.put("PERIOD_TO", rs.getString("PERIOD_TO"));
                    row.put("BRANCH_NAME", rs.getString("BRANCH_NAME"));
                    row.put("status", rs.getString("status"));
                    row.put("DEB_TOTAL_AMOUNT", rs.getDouble("DEB_TOTAL_AMOUNT"));
                    row.put("DEB_TOTAL_SETTLED", rs.getDouble("DEB_TOTAL_SETTLED"));
                    row.put("DEB_AUTO_CAN_DAY_PARAM", rs.getString("DEB_AUTO_CAN_DAY_PARAM"));
                    row.put("TRANSACTION_DESC", rs.getString("TRANSACTION_DESC"));
                    return row;
                },
                intermediaryCode
        );

        System.out.println(result);
        return result;
    }

//    public List<Map<String, Object>> findSFCcodesBySFC(String sfcCode, String fromDate, String toDate) {
//        List<Map<String, Object>> list = new ArrayList<>();
//
//        String query =
//                "SELECT DISTINCT S2.SOF_SFC_CODE, " +
//                        "       sicl.pk_sm_m_sales_force.fn_get_name(S2.SOF_SFC_CODE) AS SFC_NAME, " +
//                        "       S2.SOF_BRN_CODE " +
//                        "FROM SM_M_OVERRIDERS O2 " +
//                        "INNER JOIN SM_M_SALES_OFFICER S2 ON O2.OVR_SUBORDINATES_CODE = S2.SOF_CODE " +
//                        "WHERE O2.OVR_SFC_CODE = ? " +
//                        "  AND S2.SOF_SFC_CODE <> ? " +
//                        "  AND O2.OVR_VALID_STOP BETWEEN ? AND ? " +
//                        "  AND O2.OVR_VALID_START <= ? " +
//
//                        "UNION ALL " +
//
//                        "SELECT DISTINCT S2.SOF_SFC_CODE, " +
//                        "       sicl.pk_sm_m_sales_force.fn_get_name(S2.SOF_SFC_CODE) AS SFC_NAME, " +
//                        "       S2.SOF_BRN_CODE " +
//                        "FROM SM_M_OVERRIDERS O2 " +
//                        "INNER JOIN SM_M_SALES_OFFICER S2 ON O2.OVR_SUBORDINATES_CODE = S2.SOF_CODE " +
//                        "WHERE O2.OVR_SFC_CODE = ? " +
//                        "  AND S2.SOF_SFC_CODE <> ? " +
//                        "  AND O2.OVR_VALID_START <= ? " +
//                        "  AND O2.OVR_VALID_STOP IS NULL " +
//
//                        "UNION ALL " +
//
//                        "SELECT DISTINCT S2.SOF_SFC_CODE, " +
//                        "       sicl.pk_sm_m_sales_force.fn_get_name(S2.SOF_SFC_CODE) AS SFC_NAME, " +
//                        "       S2.SOF_BRN_CODE " +
//                        "FROM SM_M_OVERRIDERS O2 " +
//                        "INNER JOIN SM_M_SALES_OFFICER S2 ON O2.OVR_SUBORDINATES_CODE = S2.SOF_CODE " +
//                        "WHERE O2.OVR_SFC_CODE = ? " +
//                        "  AND S2.SOF_SFC_CODE <> ? " +
//                        "  AND O2.OVR_VALID_STOP >= ? " +
//
//                        "UNION ALL " +
//
//                        "SELECT DISTINCT O.OVR_SFC_CODE, " +
//                        "       sicl.pk_sm_m_sales_force.fn_get_name(O.OVR_SFC_CODE) AS SFC_NAME, " +
//                        "       S.SOF_BRN_CODE " +        // FIXED: S2 → S
//                        "FROM SM_M_SALES_OFFICER S, SM_M_OVERRIDERS O " +
//                        "WHERE S.SOF_CODE = O.OVR_OVRRIDER_CODE " +
//                        "  AND S.SOF_SFC_CODE = ? " +
//                        "  AND O.OVR_SFC_CODE <> ? " +
//                        "  AND O.OVR_VALID_STOP BETWEEN ? AND ? " +
//                        "  AND O.OVR_VALID_START <= ? " +
//
//                        "UNION ALL " +
//
//                        "SELECT DISTINCT O.OVR_SFC_CODE, " +
//                        "       sicl.pk_sm_m_sales_force.fn_get_name(O.OVR_SFC_CODE) AS SFC_NAME, " +
//                        "       S.SOF_BRN_CODE " +        // FIXED
//                        "FROM SM_M_SALES_OFFICER S, SM_M_OVERRIDERS O " +
//                        "WHERE S.SOF_CODE = O.OVR_OVRRIDER_CODE " +
//                        "  AND S.SOF_SFC_CODE = ? " +
//                        "  AND O.OVR_SFC_CODE <> ? " +
//                        "  AND O.OVR_VALID_START <= ? " +
//                        "  AND O.OVR_VALID_STOP IS NULL " +
//
//                        "UNION ALL " +
//
//                        "SELECT DISTINCT O.OVR_SFC_CODE, " +
//                        "       sicl.pk_sm_m_sales_force.fn_get_name(O.OVR_SFC_CODE) AS SFC_NAME, " +
//                        "       S.SOF_BRN_CODE " +        // FIXED
//                        "FROM SM_M_SALES_OFFICER S, SM_M_OVERRIDERS O " +
//                        "WHERE S.SOF_CODE = O.OVR_OVRRIDER_CODE " +
//                        "  AND S.SOF_SFC_CODE = ? " +
//                        "  AND O.OVR_SFC_CODE <> ? " +
//                        "  AND O.OVR_VALID_STOP > ?";
//
//
//        try (Connection co = JDBC.con();
//             PreparedStatement stmt = co.prepareStatement(query)) {
//
//            // Set all parameters (same as before)
//            stmt.setString(1, sfcCode);
//            stmt.setString(2, sfcCode);
//            stmt.setDate(3, java.sql.Date.valueOf(fromDate));
//            stmt.setDate(4, java.sql.Date.valueOf(toDate));
//            stmt.setDate(5, java.sql.Date.valueOf(toDate));
//
//            stmt.setString(6, sfcCode);
//            stmt.setString(7, sfcCode);
//            stmt.setDate(8, java.sql.Date.valueOf(toDate));
//
//            stmt.setString(9, sfcCode);
//            stmt.setString(10, sfcCode);
//            stmt.setDate(11, java.sql.Date.valueOf(toDate));
//
//            stmt.setString(12, sfcCode);
//            stmt.setString(13, sfcCode);
//            stmt.setDate(14, java.sql.Date.valueOf(fromDate));
//            stmt.setDate(15, java.sql.Date.valueOf(toDate));
//            stmt.setDate(16, java.sql.Date.valueOf(toDate));
//
//            stmt.setString(17, sfcCode);
//            stmt.setString(18, sfcCode);
//            stmt.setDate(19, java.sql.Date.valueOf(toDate));
//
//            stmt.setString(20, sfcCode);
//            stmt.setString(21, sfcCode);
//            stmt.setDate(22, java.sql.Date.valueOf(toDate));
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    Map<String, Object> row = new HashMap<>();
//                    row.put("SFC_CODE", rs.getString(1));
//                    row.put("SFC_NAME", rs.getString(2)); // get the name
//                    row.put("BRN_CODE", rs.getString(3));
//                    list.add(row);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//    public List<Map<String, Object>> getSFCCodesByBranch(String brnCode, String formattedStart, String formattedEnd) {
//        List<Map<String, Object>> list = new ArrayList<>();
//
//        String sql = "SELECT DISTINCT S.SOF_SFC_CODE, " +
//                "       sicl.pk_sm_m_sales_force.fn_get_name(S.SOF_SFC_CODE) AS SFC_NAME, " +
//                "       S.SOF_BRN_CODE " +
//                "FROM SM_M_SALES_OFFICER S " +
//                "WHERE S.SOF_BRN_CODE = ? " +
//                "  AND S.SOF_START_DATE <= ? " +
//                "  AND (S.SOF_STOP_DATE IS NULL OR S.SOF_STOP_DATE >= ?) " +
//                "ORDER BY S.SOF_SFC_CODE";
//
//        try (
//                Connection con = JDBC.con();
//                PreparedStatement stmt = con.prepareStatement(sql)
//        ) {
//            stmt.setString(1, brnCode);
//            stmt.setString(2, formattedStart);
//            stmt.setString(3, formattedEnd);
//
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    Map<String, Object> row = new HashMap<>();
//                    row.put("SFC_CODE", rs.getString(1));
//                    row.put("SFC_NAME", rs.getString(2));
//                    list.add(row);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(list);
//        return list;
//    }
//
//    public List<Map<String, Object>> findBranchesByRegionCode(String regionCode) {
//        List<Map<String, Object>> list = new ArrayList<>();
//
//        String query = "select SLC_BRN_CODE,PK_SM_M_SALESLOC.fn_get_desc (SLC_BRN_CODE)," +
//                "PK_SM_M_SALESLOC.fn_get_desc (SLC_REPORT_CODE) from Sm_m_salesloc " +
//                "where  SLC_ACTIVE ='Y' and SLC_REPORT_CODE= ?";
//
//        try (Connection co = JDBC.con();
//             PreparedStatement stmt = co.prepareStatement(query)) {
//
//            stmt.setString(1, regionCode);
//
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                Map<String, Object> row = new HashMap<>();
//                row.put("BRN_CODE", rs.getString(1));
//                row.put("BRN_NAME", rs.getString(2));
//                row.put("REGION_NAME", rs.getString(3));
//                list.add(row);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(list);
//        return list;
//    }
//
//
//    public List<Map<String, Object>> getBranchGWPData(String branchCode, String formattedStart, String formattedEnd) {
//        List<Map<String, Object>> list = new ArrayList<>();
//
//        String query = """
//            SELECT GLT_POLICY_NO,
//                   SUM(PREMIUM) as PREMIUM,
//                   SUM(SRCC) as SRCC,
//                   SUM(TC) as TC
//            FROM SICL.SDU_POLICIES_GWP
//            WHERE GLT_SEG_4 = ?
//              AND GLT_REF_DATE BETWEEN TO_DATE(?, 'DD-MON-YY')\s
//                                   AND TO_DATE(?, 'DD-MON-YY')
//            GROUP BY GLT_POLICY_NO
//""";
//
//        try (Connection co = JDBC.con();
//             PreparedStatement stmt = co.prepareStatement(query)) {
//
//            stmt.setString(1, branchCode);
//            stmt.setString(2, formattedStart);
//            stmt.setString(3, formattedEnd);
//
//            ResultSet rs = stmt.executeQuery();
//
//            System.out.println(stmt);
//            while (rs.next()) {
//                Map<String, Object> row = new HashMap<>();
//                row.put("GLT_POLICY_NO", rs.getString("GLT_POLICY_NO"));
//                row.put("PREMIUM", rs.getDouble("PREMIUM"));
//                row.put("SRCC", rs.getDouble("SRCC"));
//                row.put("TC", rs.getDouble("TC"));
//                list.add(row);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//
//    public List<Map<String, Object>> getBranchCashCollection(String branchCode, String formattedStart, String formattedEnd) {
//        List<Map<String, Object>> list = new ArrayList<>();
//
//        String sql = "SELECT CUS_NAME,DEB_TRAN_TYPE, SETTLED_AMOUNT, DEB_POLICY_NO,INTER_DESC,DEB_CLA_CODE,PRO_DESC\n" +
//                "FROM SICL.SDU_RPT_V_Cash A\n" +
//                "WHERE A.SDT_BRN_CODE = ? \n" +
//                "  AND TRUNC(A.DST_TRN_DATE) >= TO_DATE(?, 'DD-MON-YY')\n" +
//                "  AND TRUNC(A.DST_TRN_DATE) <= TO_DATE(?, 'DD-MON-YY') \n" +
//                "  AND (A.DST_STATUS = 'A' \n" +
//                "       OR (A.DST_STATUS = 'I' \n" +
//                "           AND TRUNC(A.DST_CAN_DATE) >= TO_DATE(?, 'DD-MON-YY')))";
//
//
//
//        try (Connection co = JDBC.con();
//             PreparedStatement stmt = co.prepareStatement(sql)) {
//
//            stmt.setString(1, branchCode);
//            stmt.setString(2, formattedStart);
//            stmt.setString(3, formattedEnd);
//            stmt.setString(4, formattedEnd);
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    Map<String, Object> row = new HashMap<>();
//                    row.put("CUS_NAME", rs.getString("CUS_NAME"));
//                    row.put("DEB_TRAN_TYPE", rs.getString("DEB_TRAN_TYPE"));
//                    row.put("SETTLED_AMOUNT", rs.getDouble("SETTLED_AMOUNT"));
//                    row.put("DEB_POLICY_NO", rs.getString("DEB_POLICY_NO"));
//                    row.put("INTER_DESC", rs.getString("INTER_DESC"));
//                    row.put("DEB_CLA_CODE", rs.getString("DEB_CLA_CODE"));
//                    row.put("PRO_DESC", rs.getString("PRO_DESC"));
//                    list.add(row);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(list);
//        return list;
//    }
//
//    public List<Map<String, Object>> getBranchRenewal(String formattedStart, String formattedEnd, String branchCode) {
//        List<Map<String, Object>> list = new ArrayList<>();
//
//        String sql = "SELECT POLICY_NO, pk_uw_m_customers.fn_get_cust_phone(CUSTOMER_CODE) AS Phone, CLASS_DESC, PRODUCT_DESC, CUSTOMER_DESC,\n" +
//                "       CREATED_DATE, PERIOD_FROM, PERIOD_TO, INTMDRY_NAME\n" +
//                "FROM UW_V_POL_MASTER_DTLS a\n" +
//                "WHERE TRUNC(PERIOD_TO) BETWEEN TO_DATE(?, 'DD-MON-YY')\n" +
//                "                           AND TO_DATE(?, 'DD-MON-YY')\n" +
//                "  AND a.TABLE_TYPE = 'policies'\n" +
//                "  AND NVL(a.STATUS, 111) <> 9\n" +
//                "  AND a.AUTHORIZED_DATE IS NOT NULL\n" +
//                "  AND a.BRANCH_CODE = ?";
//
//        try (Connection co = JDBC.con();
//             PreparedStatement stmt = co.prepareStatement(sql)) {
//
//            stmt.setString(1, formattedStart);
//            stmt.setString(2, formattedEnd);
//            stmt.setString(3, branchCode);
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    Map<String, Object> row = new HashMap<>();
//                    row.put("POLICY_NO", rs.getString("POLICY_NO"));
//                    row.put("Phone", rs.getString("Phone"));
//                    row.put("CLASS_DESC", rs.getString("CLASS_DESC"));
//                    row.put("PRODUCT_DESC", rs.getString("PRODUCT_DESC"));
//                    row.put("CUSTOMER_DESC", rs.getString("CUSTOMER_DESC"));
//                    row.put("CREATED_DATE", rs.getString("CREATED_DATE"));
//                    row.put("PERIOD_FROM", rs.getString("PERIOD_FROM"));
//                    row.put("PERIOD_TO", rs.getString("PERIOD_TO"));
//                    row.put("INTMDRY_NAME", rs.getString("INTMDRY_NAME"));
//                    list.add(row);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(list);
//        return list;
//
//    }

    public List<Map<String, Object>> findSFCcodesBySFC(String sfcCode, String fromDate, String toDate) {
        String query = """
        SELECT DISTINCT S2.SOF_SFC_CODE, 
               sicl.pk_sm_m_sales_force.fn_get_name(S2.SOF_SFC_CODE) AS SFC_NAME, 
               S2.SOF_BRN_CODE 
        FROM SM_M_OVERRIDERS O2 
        INNER JOIN SM_M_SALES_OFFICER S2 ON O2.OVR_SUBORDINATES_CODE = S2.SOF_CODE 
        WHERE O2.OVR_SFC_CODE = ? 
          AND S2.SOF_SFC_CODE <> ? 
          AND O2.OVR_VALID_STOP BETWEEN ? AND ? 
          AND O2.OVR_VALID_START <= ? 
        
        UNION ALL 
        
        SELECT DISTINCT S2.SOF_SFC_CODE, 
               sicl.pk_sm_m_sales_force.fn_get_name(S2.SOF_SFC_CODE) AS SFC_NAME, 
               S2.SOF_BRN_CODE 
        FROM SM_M_OVERRIDERS O2 
        INNER JOIN SM_M_SALES_OFFICER S2 ON O2.OVR_SUBORDINATES_CODE = S2.SOF_CODE 
        WHERE O2.OVR_SFC_CODE = ? 
          AND S2.SOF_SFC_CODE <> ? 
          AND O2.OVR_VALID_START <= ? 
          AND O2.OVR_VALID_STOP IS NULL 
        
        UNION ALL 
        
        SELECT DISTINCT S2.SOF_SFC_CODE, 
               sicl.pk_sm_m_sales_force.fn_get_name(S2.SOF_SFC_CODE) AS SFC_NAME, 
               S2.SOF_BRN_CODE 
        FROM SM_M_OVERRIDERS O2 
        INNER JOIN SM_M_SALES_OFFICER S2 ON O2.OVR_SUBORDINATES_CODE = S2.SOF_CODE 
        WHERE O2.OVR_SFC_CODE = ? 
          AND S2.SOF_SFC_CODE <> ? 
          AND O2.OVR_VALID_STOP >= ? 
        
        UNION ALL 
        
        SELECT DISTINCT O.OVR_SFC_CODE, 
               sicl.pk_sm_m_sales_force.fn_get_name(O.OVR_SFC_CODE) AS SFC_NAME, 
               S.SOF_BRN_CODE 
        FROM SM_M_SALES_OFFICER S, SM_M_OVERRIDERS O 
        WHERE S.SOF_CODE = O.OVR_OVRRIDER_CODE 
          AND S.SOF_SFC_CODE = ? 
          AND O.OVR_SFC_CODE <> ? 
          AND O.OVR_VALID_STOP BETWEEN ? AND ? 
          AND O.OVR_VALID_START <= ? 
        
        UNION ALL 
        
        SELECT DISTINCT O.OVR_SFC_CODE, 
               sicl.pk_sm_m_sales_force.fn_get_name(O.OVR_SFC_CODE) AS SFC_NAME, 
               S.SOF_BRN_CODE 
        FROM SM_M_SALES_OFFICER S, SM_M_OVERRIDERS O 
        WHERE S.SOF_CODE = O.OVR_OVRRIDER_CODE 
          AND S.SOF_SFC_CODE = ? 
          AND O.OVR_SFC_CODE <> ? 
          AND O.OVR_VALID_START <= ? 
          AND O.OVR_VALID_STOP IS NULL 
        
        UNION ALL 
        
        SELECT DISTINCT O.OVR_SFC_CODE, 
               sicl.pk_sm_m_sales_force.fn_get_name(O.OVR_SFC_CODE) AS SFC_NAME, 
               S.SOF_BRN_CODE 
        FROM SM_M_SALES_OFFICER S, SM_M_OVERRIDERS O 
        WHERE S.SOF_CODE = O.OVR_OVRRIDER_CODE 
          AND S.SOF_SFC_CODE = ? 
          AND O.OVR_SFC_CODE <> ? 
          AND O.OVR_VALID_STOP > ?
        """;

        // Convert String dates to java.sql.Date
        java.sql.Date sqlFromDate = java.sql.Date.valueOf(fromDate);
        java.sql.Date sqlToDate = java.sql.Date.valueOf(toDate);

        return oracleJdbcTemplate.query(
                query,
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("SFC_CODE", rs.getString("SOF_SFC_CODE"));
                    row.put("SFC_NAME", rs.getString("SFC_NAME"));
                    row.put("BRN_CODE", rs.getString("SOF_BRN_CODE"));
                    return row;
                },
                // All parameters in order
                sfcCode, sfcCode, sqlFromDate, sqlToDate, sqlToDate,
                sfcCode, sfcCode, sqlToDate,
                sfcCode, sfcCode, sqlToDate,
                sfcCode, sfcCode, sqlFromDate, sqlToDate, sqlToDate,
                sfcCode, sfcCode, sqlToDate,
                sfcCode, sfcCode, sqlToDate
        );
    }

    public List<Map<String, Object>> getSFCCodesByBranch(String brnCode, String formattedStart, String formattedEnd) {
        String sql = """
        SELECT DISTINCT S.SOF_SFC_CODE, 
               sicl.pk_sm_m_sales_force.fn_get_name(S.SOF_SFC_CODE) AS SFC_NAME, 
               S.SOF_BRN_CODE 
        FROM SM_M_SALES_OFFICER S 
        WHERE S.SOF_BRN_CODE = ? 
          AND S.SOF_START_DATE <= ? 
          AND (S.SOF_STOP_DATE IS NULL OR S.SOF_STOP_DATE >= ?) 
        ORDER BY S.SOF_SFC_CODE
        """;

        List<Map<String, Object>> result = oracleJdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("SFC_CODE", rs.getString("SOF_SFC_CODE"));
                    row.put("SFC_NAME", rs.getString("SFC_NAME"));
                    row.put("BRN_CODE", rs.getString("SOF_BRN_CODE"));
                    return row;
                },
                brnCode, formattedStart, formattedEnd
        );

        System.out.println(result);
        return result;
    }

    public List<Map<String, Object>> findBranchesByRegionCode(String regionCode) {
        String query = """
        SELECT SLC_BRN_CODE,
               PK_SM_M_SALESLOC.fn_get_desc(SLC_BRN_CODE),
               PK_SM_M_SALESLOC.fn_get_desc(SLC_REPORT_CODE) 
        FROM Sm_m_salesloc 
        WHERE SLC_ACTIVE = 'Y' 
          AND SLC_REPORT_CODE = ?
        """;

        List<Map<String, Object>> result = oracleJdbcTemplate.query(
                query,
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("BRN_CODE", rs.getString("SLC_BRN_CODE"));
                    row.put("BRN_NAME", rs.getString(2));
                    row.put("REGION_NAME", rs.getString(3));
                    return row;
                },
                regionCode
        );

        System.out.println(result);
        return result;
    }

    public List<Map<String, Object>> getBranchGWPData(String branchCode, String formattedStart, String formattedEnd) {
        String query = """
        SELECT GLT_POLICY_NO,
               SUM(PREMIUM) as PREMIUM,
               SUM(SRCC) as SRCC,
               SUM(TC) as TC
        FROM SICL.SDU_POLICIES_GWP
        WHERE GLT_SEG_4 = ?
          AND GLT_REF_DATE BETWEEN TO_DATE(?, 'DD-MON-YY')
                               AND TO_DATE(?, 'DD-MON-YY')
        GROUP BY GLT_POLICY_NO
        """;

        return oracleJdbcTemplate.query(
                query,
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("GLT_POLICY_NO", rs.getString("GLT_POLICY_NO"));
                    row.put("PREMIUM", rs.getDouble("PREMIUM"));
                    row.put("SRCC", rs.getDouble("SRCC"));
                    row.put("TC", rs.getDouble("TC"));
                    return row;
                },
                branchCode, formattedStart, formattedEnd
        );
    }

    public List<Map<String, Object>> getBranchCashCollection(String branchCode, String formattedStart, String formattedEnd) {
        String sql = """
        SELECT CUS_NAME, DEB_TRAN_TYPE, SETTLED_AMOUNT, DEB_POLICY_NO, INTER_DESC, DEB_CLA_CODE, PRO_DESC
        FROM SICL.SDU_RPT_V_Cash A
        WHERE A.SDT_BRN_CODE = ? 
          AND TRUNC(A.DST_TRN_DATE) >= TO_DATE(?, 'DD-MON-YY')
          AND TRUNC(A.DST_TRN_DATE) <= TO_DATE(?, 'DD-MON-YY') 
          AND (A.DST_STATUS = 'A' 
               OR (A.DST_STATUS = 'I' 
                   AND TRUNC(A.DST_CAN_DATE) >= TO_DATE(?, 'DD-MON-YY')))
        """;

        List<Map<String, Object>> result = oracleJdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("CUS_NAME", rs.getString("CUS_NAME"));
                    row.put("DEB_TRAN_TYPE", rs.getString("DEB_TRAN_TYPE"));
                    row.put("SETTLED_AMOUNT", rs.getDouble("SETTLED_AMOUNT"));
                    row.put("DEB_POLICY_NO", rs.getString("DEB_POLICY_NO"));
                    row.put("INTER_DESC", rs.getString("INTER_DESC"));
                    row.put("DEB_CLA_CODE", rs.getString("DEB_CLA_CODE"));
                    row.put("PRO_DESC", rs.getString("PRO_DESC"));
                    return row;
                },
                branchCode, formattedStart, formattedEnd, formattedEnd
        );

        System.out.println(result);
        return result;
    }

    public List<Map<String, Object>> getBranchRenewal(String formattedStart, String formattedEnd, String branchCode) {
        String sql = """
        SELECT POLICY_NO, pk_uw_m_customers.fn_get_cust_phone(CUSTOMER_CODE) AS Phone, 
               CLASS_DESC, PRODUCT_DESC, CUSTOMER_DESC,
               CREATED_DATE, PERIOD_FROM, PERIOD_TO, INTMDRY_NAME
        FROM UW_V_POL_MASTER_DTLS a
        WHERE TRUNC(PERIOD_TO) BETWEEN TO_DATE(?, 'DD-MON-YY')
                                   AND TO_DATE(?, 'DD-MON-YY')
          AND a.TABLE_TYPE = 'policies'
          AND NVL(a.STATUS, 111) <> 9
          AND a.AUTHORIZED_DATE IS NOT NULL
          AND a.BRANCH_CODE = ?
        """;

        List<Map<String, Object>> result = oracleJdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("POLICY_NO", rs.getString("POLICY_NO"));
                    row.put("Phone", rs.getString("Phone"));
                    row.put("CLASS_DESC", rs.getString("CLASS_DESC"));
                    row.put("PRODUCT_DESC", rs.getString("PRODUCT_DESC"));
                    row.put("CUSTOMER_DESC", rs.getString("CUSTOMER_DESC"));
                    row.put("CREATED_DATE", rs.getString("CREATED_DATE"));
                    row.put("PERIOD_FROM", rs.getString("PERIOD_FROM"));
                    row.put("PERIOD_TO", rs.getString("PERIOD_TO"));
                    row.put("INTMDRY_NAME", rs.getString("INTMDRY_NAME"));
                    return row;
                },
                formattedStart, formattedEnd, branchCode
        );

        System.out.println(result);
        return result;
    }

}
