package main.java.database.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import main.java.database.entities.ImagePoint;
import main.java.database.entities.PatientImage;


//TODO implement get by image method
public class ImagePointDao implements IDao<ImagePoint> {
    private final String url="jdbc:sqlite:test.db";
    private static final String SELECT_IMAGE_POINT_BY_ID = "select * from image_points where id =?";
    private static final String SELECT_IMAGE_POINT_BY_IMAGE = "select * from image_points where image_id =?";
    private static final String SELECT_ALL_IMAGE_POINTS = "select * from image_points";
    private static final String INSERT_IMAGE_POINT_SQL = "INSERT INTO image_points  (image_id, point_name, point_x, point_y) VALUES "
            + " (?, ?, ?, ?);";
    private static final String DELETE_IMAGE_POINT_SQL = "delete from image_points where id = ?;";
    private static final String UPDATE_IMAGE_POINT_SQL = "update image_points set image_id = ?, point_name= ?, point_x =?, point_y=? where id = ?;";

    @Override
    public ImagePoint get(Double id) {
        ImagePoint imagePoint = null;
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = conn.prepareStatement(SELECT_IMAGE_POINT_BY_ID)) {
            preparedStatement.setDouble(1,id);
            ResultSet rs = preparedStatement.executeQuery();
            imagePoint = getImagePointFromResultSet(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return imagePoint;
    }

    @Override
    public List<ImagePoint> getAll() {
        List<ImagePoint> imagePoints = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = conn.prepareStatement(SELECT_ALL_IMAGE_POINTS)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                imagePoints.add(getImagePointFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return imagePoints;
    }

    public ArrayList<ImagePoint> getByPatientImage(PatientImage patientImage) {
        ArrayList<ImagePoint> imagePoints = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = conn.prepareStatement(SELECT_IMAGE_POINT_BY_IMAGE)) {
            preparedStatement.setDouble(1, patientImage.getId());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                imagePoints.add(getImagePointFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return imagePoints;
    }

    // TODO add return
    // TODO update if already saved
    public ImagePoint save(Double imageId, String pointName, Double pointX, Double pointY) {
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = conn.prepareStatement(INSERT_IMAGE_POINT_SQL)) {
            preparedStatement.setDouble(1,imageId);
            preparedStatement.setString(2,pointName);
            preparedStatement.setDouble(3,pointX);
            preparedStatement.setDouble(4,pointY);
            preparedStatement.executeUpdate();
            long insertedId = preparedStatement.getGeneratedKeys().getLong(1);
            return this.get((double) insertedId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean update(ImagePoint imagePoint, String[] params) {
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_IMAGE_POINT_SQL)) {
            preparedStatement.setDouble(1,imagePoint.getImageId());
            preparedStatement.setString(2,imagePoint.getPointName());
            preparedStatement.setDouble(3,imagePoint.getPointX());
            preparedStatement.setDouble(4,imagePoint.getPointY());
            preparedStatement.setDouble(5,imagePoint.getId());
            return preparedStatement.executeUpdate()>0;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(ImagePoint imagePoint) {
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = conn.prepareStatement(DELETE_IMAGE_POINT_SQL)) {
            preparedStatement.setDouble(1,imagePoint.getId());
            return preparedStatement.executeUpdate()>0;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private ImagePoint getImagePointFromResultSet(ResultSet rs) throws SQLException {
        Double id = rs.getDouble("id");
        Double imageId = rs.getDouble("image_id");
        String pointName = rs.getString("point_name");
        Double pointX = rs.getDouble("point_x");
        Double pointY = rs.getDouble("point_y");
        return new ImagePoint(id,imageId,pointName,pointX,pointY);
    }
}
