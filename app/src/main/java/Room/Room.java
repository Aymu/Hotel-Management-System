package Room;

import java.io.Serializable;
import android.view.View;
import android.widget.TextView;
public class Room implements Serializable{
    private long id;
    private String name;
    private int age;
    private String sex;
    private int room;
    private String phoneNumber;
    private String trainDate;
    public Room() {
        super();
    }
    public Room(String name, int age, String sex, String phoneNumber,
                String trainDate,int room) {
        super();
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.room = room;
        this.phoneNumber = phoneNumber;
        this.trainDate = trainDate;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getSex() { return sex;     }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getTrainDate() {
        return trainDate;
    }
    public void setTrainDate(String trainDate) { this.trainDate = trainDate; }
    public int getRoom() {
        return room;
    }
    public void setRoom(int room) {
        this.room = room;
    }
}

