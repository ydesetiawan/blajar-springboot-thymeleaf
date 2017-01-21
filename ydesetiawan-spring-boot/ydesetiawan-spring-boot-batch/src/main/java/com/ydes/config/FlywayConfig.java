package com.ydes.config;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author edys
 * @version 1.0, Jan 20, 2017
 * @since
 */
@Configuration
@ConfigurationProperties(prefix = "flyway")
public class FlywayConfig {

    private Logger log = LoggerFactory.getLogger(FlywayConfig.class);

    @Autowired
    private DataSource datasource;

    private String[] locations;

    private boolean dbsuffix;

    private boolean repair;

    private boolean outOfOrder;

    @Bean
    public Flyway flyway() {

        String[] expandedLocations = locations;
        if (dbsuffix) {
            try {
                DatabaseMetaData dbMeta = datasource.getConnection()
                        .getMetaData();
                String dbName;
                if ("MySQL".equalsIgnoreCase(dbMeta.getDatabaseProductName())) {
                    dbName = isDatabaseMajorVersion(dbMeta);
                } else if ("H2".equalsIgnoreCase(dbMeta
                        .getDatabaseProductName())) {
                    dbName = "h2";
                } else {
                    dbName = null;
                }
                if (dbName != null) {
                    expandedLocations = isExpandedLocations(dbName);
                }
            } catch (SQLException e) {
                log.warn("Could not retrieve database meta data.", e);
            }
        }
        log.info("Using locations: " + Arrays.asList(expandedLocations));

        Flyway fly = new Flyway();
        fly.setDataSource(datasource);
        fly.setLocations(expandedLocations);
        fly.setOutOfOrder(outOfOrder);
        if (repair) {
            fly.repair();
        }
        fly.migrate();
        return fly;
    }

    /**
     * @param dbName
     * @return
     */
    protected String[] isExpandedLocations(String dbName) {
        String[] expandedLocations;
        List<String> locs = new ArrayList<>();
        for (String location : locations) {
            locs.add(location + "_" + dbName);
            locs.add(location + "_default");
        }
        expandedLocations = locs.toArray(new String[locs.size()]);
        return expandedLocations;
    }

    /**
     * @param dbMeta
     * @return
     * @throws SQLException
     */
    protected String isDatabaseMajorVersion(DatabaseMetaData dbMeta)
            throws SQLException {
        String dbName;
        if (dbMeta.getDatabaseMajorVersion() >= 5
                && dbMeta.getDatabaseMinorVersion() >= 6) {
            dbName = "mysql56";
        } else {
            dbName = "mysql55";
        }
        return dbName;
    }

    public String[] getLocations() {
        return locations;
    }

    public boolean isDbsuffix() {
        return dbsuffix;
    }

    public boolean isOutOfOrder() {
        return outOfOrder;
    }

    public boolean isRepair() {
        return repair;
    }

    public void setDbsuffix(boolean dbsuffix) {
        this.dbsuffix = dbsuffix;
    }

    public void setLocations(String[] locations) {
        this.locations = locations;
    }

    public void setOutOfOrder(boolean outOfOrder) {
        this.outOfOrder = outOfOrder;
    }

    public void setRepair(boolean repair) {
        this.repair = repair;
    }
}
