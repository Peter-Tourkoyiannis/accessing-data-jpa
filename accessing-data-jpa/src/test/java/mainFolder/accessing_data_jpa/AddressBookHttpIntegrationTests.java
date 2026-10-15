package mainFolder.accessing_data_jpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class AddressBookHttpIntegrationTests {

	@Value("${local.server.port}")
	private int port;
	private RestTestClient client;

	@BeforeEach
	void setup() {
		client = RestTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
	}

	@Test
	void createAndFetchJson() {
		client.post().uri("/api/addressbooks").contentType(MediaType.APPLICATION_JSON).body("{\"buddies\":[]}").exchange().expectStatus().isCreated().expectBody().jsonPath("$.id").exists().jsonPath("$.buddies").isArray();
	}

	@Test
	void createWithNewAddressAndViewWithThymeleaf() {
		client.post().uri("/api/addressbooks").contentType(MediaType.APPLICATION_JSON).body("""
                    {
                      "buddies": [
                        {
                          "name": "Peter Tourkoyiannis",
                          "phoneNumber": "1234567890",
                          "address": "Nepean"
                        }
                      ]
                    }
                    """).exchange().expectStatus().isCreated().expectBody().jsonPath("$.buddies[0].name").isEqualTo("Peter Tourkoyiannis").jsonPath("$.buddies[0].address").isEqualTo("Nepean");
		client.get().uri("/addressbooklist").exchange().expectStatus().isOk().expectBody(String.class);
	}

	@Test
	void rejectMissingAddressBook() {
		client.get().uri("/api/addressbooks/-1").exchange().expectStatus().isNotFound();
	}

	@Test
	void rejectInvalidBuddy() {
		client.post().uri("/api/addressbooks").contentType(MediaType.APPLICATION_JSON).body("""
                    {
                      "buddies": [
                        {"name": "", "phoneNumber": "1234567890"}
                      ]
                    }
                    """).exchange().expectStatus().isBadRequest();
	}
}
