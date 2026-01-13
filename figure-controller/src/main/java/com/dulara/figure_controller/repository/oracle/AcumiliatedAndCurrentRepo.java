package com.dulara.figure_controller.repository.oracle;

import com.dulara.figure_controller.dto.branch.DailyBranchGWPDTO;
import com.dulara.figure_controller.dto.region.RegionsWithGWPDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public class AcumiliatedAndCurrentRepo {
    private final JdbcTemplate oracleJdbcTemplate;


    public AcumiliatedAndCurrentRepo(@Qualifier("oracleJdbcTemplate")JdbcTemplate oracleJdbcTemplate) {
        this.oracleJdbcTemplate = oracleJdbcTemplate;
    }

    public List<DailyBranchGWPDTO> getDailyBranchGWP() {
        String sql = "WITH monthly_data AS (\n" +
                "    SELECT \n" +
                "        GLT_SEG_4,\n" +
                "        SUM(PREMIUM + SRCC + TC) as CURRENT_MONTH_GWP\n" +
                "    FROM SICL.SDU_POLICIES_GWP\n" +
                "    WHERE GLT_REF_DATE >= TRUNC(SYSDATE, 'MM')  \n" +
                "      AND GLT_REF_DATE <= SYSDATE                \n" +
                "    GROUP BY GLT_SEG_4\n" +
                "),\n" +
                "ytd_data AS (\n" +
                "    SELECT \n" +
                "        GLT_SEG_4,\n" +
                "        SUM(PREMIUM + SRCC + TC) as YTD_GWP\n" +
                "    FROM SICL.SDU_POLICIES_GWP\n" +
                "    WHERE GLT_REF_DATE >= TRUNC(SYSDATE, 'YEAR')  \n" +
                "      AND GLT_REF_DATE <= SYSDATE                  \n" +
                "    GROUP BY GLT_SEG_4\n" +
                ")\n" +
                "SELECT \n" +
                "    COALESCE(m.GLT_SEG_4, y.GLT_SEG_4) AS GLT_SEG_4,\n" +
                "    PK_SM_M_SALESLOC.fn_get_desc(COALESCE(m.GLT_SEG_4, y.GLT_SEG_4)) AS BRN_NAME,\n" +
                "    COALESCE(m.CURRENT_MONTH_GWP, 0) AS CURRENT_MONTH_GWP,\n" +
                "    COALESCE(y.YTD_GWP, 0) AS YTD_GWP\n" +
                "FROM monthly_data m\n" +
                "FULL OUTER JOIN ytd_data y \n" +
                "    ON m.GLT_SEG_4 = y.GLT_SEG_4\n" +
                "ORDER BY GLT_SEG_4";

        return oracleJdbcTemplate.query(
                sql,
                new Object[]{},
                (rs, rowNum) -> new DailyBranchGWPDTO(
                        rs.getString("GLT_SEG_4"),
                        rs.getString("BRN_NAME"),
                        rs.getBigDecimal("CURRENT_MONTH_GWP"),
                        rs.getBigDecimal("YTD_GWP")
                )
        );
    }


    public List<RegionsWithGWPDTO> getDailyRegionGWP() {
        String sql = "SELECT\n" +
                "    aa.REG,\n" +
                "    aa.CODE,\n" +
                "\n" +
                "    /* Current Month GWP */\n" +
                "    SUM(\n" +
                "        CASE\n" +
                "            WHEN TRUNC(GLT_REF_DATE, 'MM') = TRUNC(SYSDATE, 'MM')\n" +
                "            THEN PREMIUM\n" +
                "            ELSE 0\n" +
                "        END\n" +
                "    ) AS CURRENT_MONTH_GWP,\n" +
                "\n" +
                "    /* Current Year GWP */\n" +
                "    SUM(\n" +
                "        CASE\n" +
                "            WHEN EXTRACT(YEAR FROM GLT_REF_DATE) = EXTRACT(YEAR FROM SYSDATE)\n" +
                "            THEN PREMIUM\n" +
                "            ELSE 0\n" +
                "        END\n" +
                "    ) AS CURRENT_YEAR_GWP\n" +
                "\n" +
                "FROM (\n" +
                "    SELECT\n" +
                "        (SELECT PK_SM_M_SALESLOC.fn_get_desc(slc_report_code)\n" +
                "           FROM sm_m_salesloc\n" +
                "          WHERE slc_brn_code = GLT_SEG_4\n" +
                "        ) AS REG,\n" +
                "\n" +
                "        (SELECT slc_report_code\n" +
                "           FROM sm_m_salesloc\n" +
                "          WHERE slc_brn_code = GLT_SEG_4\n" +
                "        ) AS CODE,\n" +
                "\n" +
                "        (PREMIUM + SRCC + TC) AS PREMIUM,\n" +
                "        GLT_REF_DATE\n" +
                "\n" +
                "    FROM SICL.SDU_POLICIES_GWP\n" +
                ") aa\n" +
                "GROUP BY aa.REG, aa.CODE";

        return oracleJdbcTemplate.query(
                sql,
                new Object[]{},
                (rs, rowNum) -> new RegionsWithGWPDTO(
                        rs.getString("CODE"),
                        rs.getString("REG"),
                        rs.getBigDecimal("CURRENT_MONTH_GWP"),
                        rs.getBigDecimal("CURRENT_YEAR_GWP")
                )
        );
    }


    public BigDecimal getAccumulatedGwp() {
        int currentYear = LocalDate.now().getYear();
        LocalDate startDate = LocalDate.of(currentYear, 1, 1);
        LocalDate endDate = LocalDate.now();

        String sql = "SELECT SUM(PREMIUM + SRCC + TC) AS PREMIUM " +
                "FROM SICL.SDU_POLICIES_GWP " +
                "WHERE GLT_REF_DATE BETWEEN ? AND ?";


        BigDecimal accumulatedGwp = oracleJdbcTemplate.queryForObject(
                sql,
                new Object[]{Date.valueOf(startDate), Date.valueOf(endDate)},
                BigDecimal.class
        );

        return accumulatedGwp != null ? accumulatedGwp : BigDecimal.ZERO;

    }
}
