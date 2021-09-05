package main.java.database.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import main.java.database.entities.Patient;
import main.java.utils.GetCurrentTime;


public class PatientDao implements IDao<Patient> {
    private static final String url="jdbc:sqlite:test.db";
    private static final String SELECT_PATIENT_BY_ID = "select * from patients where id =?";
    private static final String SELECT_ALL_PATIENTS = "SELECT * FROM patients;";
    private static final String INSERT_PATIENT_SQL = "INSERT INTO patients  (name, birth_day, register_date, race ,gender) VALUES "
            + " (?, ?, ?, ?, ?);";
    private static final String DELETE_PATIENT_SQL = "delete from patients where id = ?;";
    private static final String UPDATE_PATIENT_SQL = "update patients set name = ?,birth_day= ?, register_date =?, race=?,gender=? where id = ?;";


    @Override
    public Patient get(Double id) {
        Patient patient = null;
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = conn.prepareStatement(SELECT_PATIENT_BY_ID)) {
            preparedStatement.setDouble(1,id);
            ResultSet rs = preparedStatement.executeQuery();
            patient = getPatientFromResultSet(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return patient;
    }

    @Override
    public List<Patient> getAll() {
        List<Patient> patients = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = conn.prepareStatement(SELECT_ALL_PATIENTS)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                patients.add(getPatientFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return patients;
    }

    public Patient save(String name, String birthDay, String race, String gender) throws SQLException {
        Connection conn = DriverManager.getConnection(url);
        PreparedStatement preparedStatement = conn.prepareStatement(INSERT_PATIENT_SQL);
        preparedStatement.setString(1,name);
        preparedStatement.setString(2,birthDay);
        preparedStatement.setString(3, GetCurrentTime.now());
        preparedStatement.setString(4,race);
        preparedStatement.setString(5,gender);
        preparedStatement.executeUpdate();
        long insertedId = preparedStatement.getGeneratedKeys().getLong(1);
        return this.get((double) insertedId);
    }

    public Patient save(Patient patient) throws SQLException {
        // TODO add try catch block, re-raise exception(for all dao)
        Connection conn = DriverManager.getConnection(url);
        PreparedStatement preparedStatement = conn.prepareStatement(INSERT_PATIENT_SQL);
        preparedStatement.setString(1, patient.getName());
        preparedStatement.setString(2, patient.getBirthDay());
        preparedStatement.setString(3, patient.getRegisterDate());
        preparedStatement.setString(4, patient.getRace());
        preparedStatement.setString(5, patient.getGender());
        preparedStatement.executeUpdate();
        long insertedId = preparedStatement.getGeneratedKeys().getLong(1);
        return this.get((double) insertedId);
    }

    public boolean update(Patient patient, String[] params) {
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_PATIENT_SQL)) {
            preparedStatement.setString(1,patient.getName());
            preparedStatement.setString(2,patient.getBirthDay());
            preparedStatement.setString(3,patient.getRegisterDate());
            preparedStatement.setString(4,patient.getRace());
            preparedStatement.setString(5,patient.getGender());
            preparedStatement.setDouble(6,patient.getId());
            return preparedStatement.executeUpdate()>0;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Patient patient) {
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = conn.prepareStatement(DELETE_PATIENT_SQL)) {
            preparedStatement.setDouble(1,patient.getId());
            return preparedStatement.executeUpdate()>0;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private Patient getPatientFromResultSet(ResultSet rs) throws SQLException {
        double user_id = rs.getDouble("id");
        String name = rs.getString("name");
        String birthDay = rs.getString("birth_day");
        String registerDate = rs.getString("register_date");
        String race = rs.getString("race");
        String gender = rs.getString("gender");
        return new Patient(user_id,name,birthDay,registerDate,race,gender);
    }
}

