
package mainFolder.accessing_data_jpa;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import java.util.List;

@Controller
@RequestMapping("/addressbooklist")
public class AddressBookController {

    private final RestClient.Builder restClientBuilder;

    public AddressBookController(RestClient.Builder restClientBuilder) {
        this.restClientBuilder = restClientBuilder;
    }

    private RestClient api(HttpServletRequest request) {
        return restClientBuilder.clone().baseUrl("http://127.0.0.1:" + request.getLocalPort() + "/api/addressbooks").build();
    }

    @GetMapping
    public String showAddressBooks(Model model, HttpServletRequest request) {
        AddressBookRestController.BookResponse[] books = api(request).get().retrieve().body(AddressBookRestController.BookResponse[].class);
        model.addAttribute("addressBooks", books);
        return "addressbooklist";
    }

    @PostMapping
    public String createAddressBook(HttpServletRequest request) {
        api(request).post().body(new AddressBookRestController.CreateBookRequest(List.of())).retrieve().toBodilessEntity();
        return "redirect:/addressbooklist";
    }

    @GetMapping("/{id}")
    public String showAddressBook(@PathVariable Integer id, Model model, HttpServletRequest request) {
        AddressBookRestController.BookResponse book = api(request).get().uri("/{id}", id).retrieve().body(AddressBookRestController.BookResponse.class);
        model.addAttribute("addressBook", book);
        return "addressbookviewer";
    }

    @PostMapping("/{id}/buddies")
    public String addBuddy(@PathVariable Integer id, @RequestParam String name, @RequestParam String phoneNumber, @RequestParam(defaultValue = "") String address, HttpServletRequest request) {

        api(request).post().uri("/{id}/buddies", id).body(new AddressBookRestController.BuddyRequest(name, phoneNumber, address)).retrieve().toBodilessEntity();
        return "redirect:/addressbooklist/" + id;
    }

    @PostMapping("/{id}/buddies/{buddyId}/delete")
    public String deleteBuddy(@PathVariable Integer id, @PathVariable Integer buddyId, HttpServletRequest request) {
        api(request).delete().uri("/{id}/buddies/{buddyId}", id, buddyId).retrieve().toBodilessEntity();
        return "redirect:/addressbooklist/" + id;
    }
}
