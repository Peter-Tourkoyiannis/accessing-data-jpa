package mainFolder.accessing_data_jpa;

import org.springframework.data.repository.CrudRepository;

public interface AddressBookRepository extends CrudRepository<AddressBook, Integer> {

    AddressBook findById(long id);

}
