
package mainFolder.accessing_data_jpa;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/addressbooks", produces = MediaType.APPLICATION_JSON_VALUE)
public class AddressBookRestController {

    private final AddressBookRepository repository;

    public AddressBookRestController(AddressBookRepository repository) {
        this.repository = repository;
    }

    public record BuddyRequest(String name, String phoneNumber, String address) {}
    public record CreateBookRequest(List<BuddyRequest> buddies) {}
    public record BuddyResponse(Integer id, String name, String phoneNumber, String address) {}
    public record BookResponse(Integer id, List<BuddyResponse> buddies) {}

    private BookResponse toResponse(AddressBook book) {
        List<BuddyResponse> buddies = new ArrayList<>();

        for (BuddyInfo buddy : book.getPeopleList()) {
            buddies.add(new BuddyResponse(buddy.getId(), buddy.getName(), buddy.getPhoneNumber(), buddy.getAddress()));
        }

        return new BookResponse(book.getId(), buddies);
    }

    private AddressBook findBook(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address book not found"));
    }

    @GetMapping
    public List<BookResponse> getAllBooks() {
        List<BookResponse> result = new ArrayList<>();
        repository.findAll().forEach(book -> result.add(toResponse(book)));
        return result;
    }

    @GetMapping("/{id}")
    public BookResponse getBook(@PathVariable Integer id) {
        return toResponse(findBook(id));
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookResponse> createBook(@RequestBody CreateBookRequest request) {

        AddressBook book = new AddressBook();

        if (request.buddies() != null) {
            for (BuddyRequest input : request.buddies()) {
                book.addBuddyInfo(makeBuddy(input));
            }
        }

        book = repository.save(book);

        return ResponseEntity.created(URI.create("/api/addressbooks/" + book.getId())).body(toResponse(book));
    }

    private BuddyInfo makeBuddy(BuddyRequest input) {
        if (input == null || input.name() == null || input.name().isBlank() || input.phoneNumber() == null || input.phoneNumber().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name and phone number required");
        }

        BuddyInfo buddy = new BuddyInfo(input.name(), input.phoneNumber());
        buddy.setAddress(input.address() == null ? "" : input.address());
        return buddy;
    }

    @Transactional
    @PostMapping(value = "/{id}/buddies",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookResponse> addBuddy(@PathVariable Integer id, @RequestBody BuddyRequest input) {

        AddressBook book = findBook(id);
        book.addBuddyInfo(makeBuddy(input));
        book = repository.save(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(book));
    }

    @Transactional
    @DeleteMapping("/{id}/buddies/{buddyId}")
    public ResponseEntity<Void> deleteBuddy(@PathVariable Integer id, @PathVariable Integer buddyId) {

        AddressBook book = findBook(id);
        boolean removed = book.getPeopleList().removeIf(buddy -> buddyId.equals(buddy.getId()));

        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Buddy not found");
        }

        repository.save(book);
        return ResponseEntity.noContent().build();
    }
}
