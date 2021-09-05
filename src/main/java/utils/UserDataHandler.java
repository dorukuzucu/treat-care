package main.java.utils;

import main.java.database.dao.ImagePointDao;
import main.java.database.dao.PatientDao;
import main.java.database.dao.PatientImageDao;
import main.java.database.entities.ImagePoint;
import main.java.database.entities.Patient;
import main.java.database.entities.PatientImage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UserDataHandler {
    private static UserDataHandler instance = null;

    private Patient activePatient = null;
    private PatientImage activePatientImage = null;
    private HashMap<PointEnum, ImagePoint> imagePointHashMap = new HashMap<>();

    public static UserDataHandler getInstance(){
        if (instance==null){
            instance = new UserDataHandler();
        }
        return instance;
    }

    public void setActivePatient(Patient patient){
        this.activePatient = patient;
    }

    public Patient getActivePatient() {
        return activePatient;
    }

    public void setActivePatientImage(PatientImage activePatientImage) {
        this.activePatientImage = activePatientImage;
    }

    public PatientImage getActivePatientImage() {
        return activePatientImage;
    }

    public void addImagePoint(PointEnum name, double x, double y) {
        ImagePoint imagePoint = new ImagePoint((double) 0, this.activePatientImage.getId(), name.toString().toUpperCase(Locale.ROOT), x, y);
        this.imagePointHashMap.put(name, imagePoint);
    }

    public HashMap<PointEnum, ImagePoint> getImagePointHashMap() {
        return imagePointHashMap;
    }

    public void savePatientData() throws SQLException {
        this.saveActivePatient();
        this.savePatientImage();
        this.saveImagePoints();
    }

    public void saveActivePatient() throws SQLException {
        PatientDao patientDao = new PatientDao();
        Patient saved = patientDao.save(this.activePatient);
        this.setActivePatient(saved);
    }

    public void savePatientImage() {
        PatientImageDao patientImageDao = new PatientImageDao();
        PatientImage savedPatientImage = patientImageDao.newImage(this.activePatient.getId(), this.activePatientImage.getImage(), this.activePatientImage.getLength());
        this.activePatientImage = savedPatientImage;
    }

    public void saveImagePoints() {
        ImagePointDao imagePointDao = new ImagePointDao();
        double imageId = this.getActivePatientImage().getId();

        for (Map.Entry<PointEnum, ImagePoint> point : this.imagePointHashMap.entrySet()){
            imagePointDao.newImagePoint(imageId, point.getKey().toString(), point.getValue().getPointX(), point.getValue().getPointY());
        }
    }

    public PatientImage getPatientImageFromDB(){
        PatientImageDao patientImageDao = new PatientImageDao();
        PatientImage patientImage = patientImageDao.getByPatient(this.activePatient);
        this.setActivePatientImage(patientImage);
        return patientImage;
    }

    public HashMap<PointEnum, ImagePoint> getPatientImagePointsFromDB(){
        ImagePointDao imagePointDao = new ImagePointDao();
        ArrayList<ImagePoint> points = imagePointDao.getByPatientImage(this.activePatientImage);
        HashMap<PointEnum, ImagePoint> imagePointHashMap = new HashMap<>();
        for(ImagePoint point: points){
            PointEnum pointName = PointEnum.fromString(point.getPointName());
            imagePointHashMap.put(pointName, point);
        }
        this.imagePointHashMap = imagePointHashMap;
        return imagePointHashMap;
    }

    public void getPatientData(){
        PatientImage image = this.getPatientImageFromDB();
        if(image==null){
            return;
        }
        this.getPatientImagePointsFromDB();
    }

    public void clearData(){
        this.activePatient = null;
        this.activePatientImage = null;
        this.imagePointHashMap.clear();
    }
}
