package mainFolder.accessing_data_jpa;

import jakarta.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class AddressBook {

    @Id
    @GeneratedValue
    private Integer id;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<BuddyInfo> peopleList;

    public AddressBook() {

        peopleList = new LinkedList<>();

    }

    public String[] getStringArray() {

        String[] output = new String[4];
        output[0] = "===== List of Addresses =====";

        int i = 0;
        while (!peopleList.isEmpty()) {

            BuddyInfo currentPerson = peopleList.removeFirst();
            output[i + 1] = "Name: " + currentPerson.getName() + " Phone: " + currentPerson.getPhoneNumber();
            i++;

        }

        return output;

    }

    public void addBuddyInfo(BuddyInfo buddyInfo) {

        peopleList.add(buddyInfo);

    }

    public List<BuddyInfo> getPeopleList() {

        return peopleList;

    }

    public Integer getId() {

        return id;

    }

}
