package main.java.database.entities;

public class Patient {
    Double id;
    String name;
    String birthDay;
    String registerDate;
    String race;
    String gender;

    public Patient(Double id, String name, String birthDay, String registerDate, String race, String gender){
        this.id = id;
        this.name = name;
        this.birthDay = birthDay;
        this.registerDate = registerDate;
        this.race = race;
        this.gender = gender;
    }

    public void setId(Double id) {
        this.id = id;
    }

    public Double getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String toString(){
        return String.format(
                "id: %.0f, name: %s, Birthday: %s, Registered on: %s, Race: %s, Gender: %s",
                id,name,birthDay,registerDate,race,gender);
    }
}
