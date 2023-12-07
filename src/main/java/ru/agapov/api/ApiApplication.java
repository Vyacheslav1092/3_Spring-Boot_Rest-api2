package ru.agapov.api;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.agapov.api.model.User;


@SpringBootApplication
public class ApiApplication {

	private static final String API_URL = "http://94.198.50.185:7081/api/users";
	private static String sessionId;

	public static void main(String[] args) {
		ResponseEntity<String> response = makeRequest(API_URL, HttpMethod.GET, String.class);
		sessionId = response.getHeaders().get("set-cookie").get(0);

		User userToSave = new User(3L, "James", "Brown", (byte) 31);
		ResponseEntity<String> saveResponse = makeRequest(API_URL, HttpMethod.POST, userToSave, String.class);
		String part1 = saveResponse.getBody();

		User userToUpdate = new User(3L, "Thomas", "Shelby", (byte) 35);
		ResponseEntity<String> updateResponse = makeRequest(API_URL, HttpMethod.PUT, userToUpdate, String.class);
		String part2 = updateResponse.getBody();

		ResponseEntity<String> deleteRespnse = makeRequest(API_URL + "/3", HttpMethod.DELETE, String.class);
		String part3 = deleteRespnse.getBody();

		String finalCode = part1 + part2 + part3;
		System.out.println("Итоговый код: " + finalCode);
	}


	private static <T> ResponseEntity<T> makeRequest(String url, HttpMethod method, Class<T> responseType) {
		HttpHeaders headers = new HttpHeaders();
		if (sessionId != null) {
			headers.add("Cookie", sessionId);
		}

		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.exchange(url, method, requestEntity, responseType);
	}

	private static <T> ResponseEntity<T> makeRequest(String url, HttpMethod method, Object body, Class<T> responseType) {
		HttpHeaders headers = new HttpHeaders();
		if (sessionId != null) {
			headers.add("Cookie", sessionId);
		}

		HttpEntity<Object> requestEntity = new HttpEntity<>(body, headers);
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.exchange(url, method, requestEntity, responseType);
	}

}
