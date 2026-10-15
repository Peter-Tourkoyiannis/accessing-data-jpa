package mainFolder.accessing_data_jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class BuddyInfo {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String phoneNumber;
    private String address;

    public BuddyInfo(String name, String phoneNumber, String address) {

        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;

    }

    public BuddyInfo(String name, String phoneNumber) {

        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = "";

    }

    public BuddyInfo() {

        this.name = null;
        this.phoneNumber = null;
        this.address = "";

    }

    public String getName() {

        return name;

    }

    public String getPhoneNumber() {

        return phoneNumber;

    }

    public Integer getId() {

        return id;

    }

    public String getAddress() {

        return address;

    }

    public void setName(String name) {

        this.name = name;

    }

    public void setPhoneNumber(String phoneNumber) {

        this.phoneNumber = phoneNumber;

    }

    public void setAddress(String address) {

        this.address = address;

    }

}
