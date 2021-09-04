package main.java.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DatabaseCreator {
    private String url="jdbc:sqlite:test.db";
    private final String patientTableName="patients";
    private final String imageTableName="images";
    private final String pointTableName="image_points";
    private final String patientTableSql = "CREATE TABLE IF NOT EXISTS "+this.patientTableName+" (\n"
            + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
            + "	name text NOT NULL,\n"
            + "	birth_day text,\n"
            + "	register_date text,\n"
            + "	race text,\n"
            + "	gender text\n"
            + ");";
    private final String imageTableSql = "CREATE TABLE IF NOT EXISTS " +this.imageTableName+" (\n"
            + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
            + "	user_id integer REFERENCES "+this.patientTableName+" ,\n"
            + "	image blob NOT NULL,\n"
            + " image_length integer\n"
            + ");";
    private final String pointTableSql = "CREATE TABLE IF NOT EXISTS "+this.pointTableName+" (\n"
            + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
            + "	image_id integer REFERENCES "+this.imageTableName+" ,\n"
            + "	point_name text NOT NULL,\n"
            + "	point_x integer NOT NULL,\n"
            + "	point_y integer NOT NULL\n"
            + ");";
    public void createAllTables() throws ClassNotFoundException {
        List<String> createTableQueries = new ArrayList<>();
        createTableQueries.add(this.patientTableSql);
        createTableQueries.add(this.imageTableSql);
        createTableQueries.add(this.pointTableSql);

        for(String createTableQuery:createTableQueries){
            this.createTable(createTableQuery);
        }
    }

    private void createTable(String sql){
        try (Connection conn = DriverManager.getConnection(this.url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
