package main.java.database.dao;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import main.java.database.entities.Patient;
import main.java.database.entities.PatientImage;


public class PatientImageDao implements IDao<PatientImage> {
    private final String url="jdbc:sqlite:test.db";
    private static final String SELECT_IMAGE_BY_ID_SQL = "select * from images where id =?";
    private static final String SELECT_IMAGE_BY_USER_ID_SQL = "select * from images where USER_id =?";
    private static final String SELECT_ALL_IMAGES_SQL = "select * from images;";
    private static final String INSERT_IMAGE_SQL = "INSERT INTO images  (user_id, image, image_length) VALUES "
            + " (?, ?, ?);";
    private static final String DELETE_IMAGE_SQL = "delete from images where id = ?;";
    private static final String UPDATE_IMAGE_SQL = "update images set image= ?, image_length= ? where id = ?;";

    @Override
    public PatientImage get(Double id) {
        PatientImage patientImage = null;
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = conn.prepareStatement(SELECT_IMAGE_BY_ID_SQL)) {
            preparedStatement.setDouble(1,id);
            ResultSet rs = preparedStatement.executeQuery();
            patientImage = getPatientImageFromResultSet(rs);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return patientImage;
    }

    @Override
    public List<PatientImage> getAll() {
        List<PatientImage> patientImages = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = conn.prepareStatement(SELECT_ALL_IMAGES_SQL)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                patientImages.add(getPatientImageFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return patientImages;
    }

    public PatientImage getByPatient(Patient patient){
        PatientImage patientImage = null;
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = conn.prepareStatement(SELECT_IMAGE_BY_USER_ID_SQL)) {
            preparedStatement.setDouble(1,patient.getId());
            ResultSet rs = preparedStatement.executeQuery();
            patientImage = getPatientImageFromResultSet(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return patientImage;
    }

    public PatientImage getByPatientId(Double patientId){
        PatientImage patientImage = null;
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = conn.prepareStatement(SELECT_IMAGE_BY_USER_ID_SQL)) {
            preparedStatement.setDouble(1,patientId);
            ResultSet rs = preparedStatement.executeQuery();
            patientImage = getPatientImageFromResultSet(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return patientImage;
    }

    public PatientImage save(Double patientId, File imageFile) {
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = conn.prepareStatement(INSERT_IMAGE_SQL)) {
            preparedStatement.setDouble(1,patientId);
            InputStream imageInputStream = new FileInputStream(imageFile);
            preparedStatement.setBinaryStream(2, imageInputStream, (int) imageFile.length());
            preparedStatement.setInt(3, (int) imageFile.length());
            preparedStatement.executeUpdate();
            imageInputStream.close();
            long insertedId = preparedStatement.getGeneratedKeys().getLong(1);
            return this.get((double) insertedId);
        } catch (SQLException | FileNotFoundException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PatientImage newImage(Double patientId, File imageFile){
        PatientImage patientImage = this.getByPatientId(patientId);
        if(patientImage==null){
            patientImage = this.save(patientId, imageFile);
        } else {
            patientImage = this.update(patientImage, imageFile);
        }
        return patientImage;
    }

    public PatientImage newImage(Double patientId, InputStream imageInputStream, int imageFileLength){
        PatientImage patientImage = this.getByPatientId(patientId);
        if(patientImage==null){
            patientImage = this.save(patientId, imageInputStream, imageFileLength);
        } else {
            patientImage = this.update(patientImage, imageInputStream, imageFileLength);
        }
        return patientImage;
    }

    public PatientImage save(Double patientId, InputStream imageInputStream, int imageFileLength) {
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = conn.prepareStatement(INSERT_IMAGE_SQL)) {
            preparedStatement.setDouble(1,patientId);
            preparedStatement.setBinaryStream(2, imageInputStream, imageFileLength);
            preparedStatement.executeUpdate();
            imageInputStream.close();
            long insertedId = preparedStatement.getGeneratedKeys().getLong(1);
            return this.get((double) insertedId);
        } catch (SQLException | FileNotFoundException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PatientImage update(PatientImage patientImage, InputStream imageInputStream, int imageFileLength) {
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_IMAGE_SQL)) {
            preparedStatement.setBinaryStream(1,imageInputStream);
            preparedStatement.setDouble(2,imageFileLength);
            preparedStatement.setDouble(3, patientImage.getId());
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        patientImage.setImage(imageInputStream);
        patientImage.setLength(imageFileLength);
        return patientImage;
    }

    public PatientImage update(PatientImage patientImage, File imageFile) {
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_IMAGE_SQL)) {
            InputStream imageInputStream = new FileInputStream(imageFile);
            preparedStatement.setBinaryStream(1, imageInputStream, (int) imageFile.length());
            preparedStatement.setDouble(2, (int) imageFile.length());
            preparedStatement.setDouble(3, patientImage.getId());
            preparedStatement.executeUpdate();
            patientImage.setImage(imageInputStream);
            patientImage.setLength((int) imageFile.length());
        }catch (SQLException | FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return patientImage;
    }

    @Override
    public boolean delete(PatientImage patientImage) {
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = conn.prepareStatement(DELETE_IMAGE_SQL)) {
            preparedStatement.setDouble(1,patientImage.getId());
            return preparedStatement.executeUpdate()>0;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private PatientImage getPatientImageFromResultSet(ResultSet rs) throws SQLException {
        if (!rs.next()){
            return null;
        }
        double image_id = rs.getDouble("id");
        double user_id = rs.getDouble("user_id");
        InputStream image = rs.getBinaryStream("image");
        int length = rs.getInt("image_length");
        return new PatientImage(image_id,user_id, image, length);
    }
}
