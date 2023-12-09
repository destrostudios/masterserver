package com.destrostudios.masterserver;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.destrostudios.masterserver.database.schema.App;
import com.destrostudios.masterserver.database.schema.News;
import com.destrostudios.masterserver.model.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("Only for local development")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("local")
class TestIntegration {

	@LocalServerPort
	private int port;
	@Autowired
	private TestRestTemplate restTemplate;

	@Nested
	class TestNotLoggedIn {

		@Test
		public void getUserByIdSuccess() {
			assertUserDto(get("/users/1", UserDto.class, HttpStatus.OK));
		}

		@Test
		public void getUserByIdNotFound() {
			get("/users/999999", UserDto.class, HttpStatus.NOT_FOUND);
		}

		@Test
		public void getUserByLoginSuccess() {
			assertUserDto(get("/users/destroflyer", UserDto.class, HttpStatus.OK));;
		}

		@Test
		public void getUserByLoginNotFound() {
			get("/users/thisLoginDoesNotExist", UserDto.class, HttpStatus.NOT_FOUND);
		}

		@Test
		public void registerNoLogin() {
			post("/users/register", new RegistrationDto("", "myEmail", "mySaltClient", "myClientHashedPassword"), Void.class, HttpStatus.BAD_REQUEST);
		}

		@Test
		public void registerNoEmail() {
			post("/users/register", new RegistrationDto("myLogin", "", "mySaltClient", "myClientHashedPassword"), Void.class, HttpStatus.BAD_REQUEST);
		}

		@Test
		public void registerNoSaltClient() {
			post("/users/register", new RegistrationDto("myLogin", "myEmail", "", "myClientHashedPassword"), Void.class, HttpStatus.BAD_REQUEST);
		}

		@Test
		public void registerNoClientHashedPassword() {
			post("/users/register", new RegistrationDto("myLogin", "myEmail", "mySaltClient", ""), Void.class, HttpStatus.BAD_REQUEST);
		}

		@Test
		public void registerLoginAlreadyExists() {
			post("/users/register", new RegistrationDto("destroflyer", "myEmail", "mySaltClient", "myClientHashedPassword"), Void.class, HttpStatus.FORBIDDEN);
		}

		@Test
		public void registerEmailAlreadyExists() {
			post("/users/register", new RegistrationDto("myLogin", "destro-flyer@web.de", "mySaltClient", "myClientHashedPassword"), Void.class, HttpStatus.FORBIDDEN);
		}

		@Test
		public void sendEmailConfirmationEmailUserNotFound() {
			post("/users/999999/sendEmailConfirmationEmail", null, Void.class, HttpStatus.NOT_FOUND);
		}

		@Test
		public void confirmEmailUserNotFound() {
			post("/users/999999/confirmEmail?secret=mySecret", null, Void.class, HttpStatus.NOT_FOUND);
		}

		@Test
		public void confirmEmailWrongSecret() {
			post("/users/1/confirmEmail?secret=mySecret", null, Void.class, HttpStatus.FORBIDDEN);
		}

		@Test
		public void sendPasswordResetEmailUserNotFound() {
			post("/users/999999/sendPasswordResetEmail", null, Void.class, HttpStatus.NOT_FOUND);
		}

		@Test
		public void resetPasswordUserNotFound() {
			post("/users/999999/resetPassword", new ResetPasswordDto("myEmailSecret", "myClientHashedPassword"), Void.class, HttpStatus.NOT_FOUND);
		}

		@Test
		public void resetPasswordWrongSecret() {
			post("/users/1/resetPassword", new ResetPasswordDto("myEmailSecret", "myClientHashedPassword"), Void.class, HttpStatus.FORBIDDEN);
		}

		@Test
		public void getSaltClientUserNotFound() {
			get("/users/thisLoginDoesNotExist/saltClient", String.class, HttpStatus.NOT_FOUND);
		}

		@Test
		public void loginUserNotFound() {
			post("/users/login", new LoginDto("thisLoginDoesNotExist", "myClientHashedPassword"), String.class, HttpStatus.NOT_FOUND);
		}

		@Test
		public void getSaltClientAndLoginWrongPassword() {
			String saltClient = get("/users/destroflyer/saltClient", String.class, HttpStatus.OK);
			String clientHashedPassword = BCrypt.hashpw("wrongPassword", saltClient);
			post("/users/login", new LoginDto("destroflyer", clientHashedPassword), String.class, HttpStatus.FORBIDDEN);
		}

		@Test
		public void getSaltClientAndLoginSuccess() {
			String authToken = login();
			DecodedJWT decodedJwt = JWT.decode(authToken);
			Map<String, Object> userClaims = decodedJwt.getClaim("user").asMap();
			assertUserClaims(userClaims);
		}

		@Test
		public void getApps() {
			List<App> apps = get("/apps", new ParameterizedTypeReference<>() {}, HttpStatus.OK);
			assertTrue(apps.size() > 0);
			App appAmara = apps.stream().filter(app -> app.getName().equals("Amara")).findAny().orElseThrow();
			assertEquals(1, appAmara.getId());
		}

		@Test
		public void updateAppFilesAppNotFound() {
			post("/apps/999999/updateFiles", null, Void.class, HttpStatus.NOT_FOUND);
		}

		@Test
		public void getAppFilesAppNotFound() {
			get("/apps/999999/files", AppFilesDto.class, HttpStatus.NOT_FOUND);
		}

		@Test
		public void getAppFileContentAppFileNotFound() {
			get("/apps/file/999999", String.class, HttpStatus.NOT_FOUND);
		}

		@Test
		public void updateAndGetAppFilesSuccess() {
			post("/apps/2/updateFiles", null, Void.class, HttpStatus.OK);

			AppFilesDto appFilesDto = get("/apps/2/files", AppFilesDto.class, HttpStatus.OK);
			assertTrue(appFilesDto.getFiles().size() > 0);
			assertTrue(appFilesDto.getProtections().size() > 0);

			AppFileDto appFileDto = appFilesDto.getFiles().get(0);
			assertTrue(appFileDto.getPath().length() > 0);
			assertTrue(appFileDto.getSizeBytes() > 0);
			assertTrue(appFileDto.getChecksumSha256().length() > 0);

			String fileContent = get("/apps/file/" + appFileDto.getId(), String.class, HttpStatus.OK);
			assertTrue(fileContent.length() > 0);
		}

		@Test
		public void getAllNews() {
			List<News> news = get("/news", new ParameterizedTypeReference<>() {}, HttpStatus.OK);
			assertTrue(news.size() > 0);
		}

		@Test
		public void getLatestNews() {
			int limit = 2;
			List<News> news = get("/news/latest?limit=" + limit, new ParameterizedTypeReference<>() {}, HttpStatus.OK);
			assertEquals(limit, news.size());
		}

		@Test
		public void getClientConfig() {
			Map<String, String> configs = get("/config/client", new ParameterizedTypeReference<>() {}, HttpStatus.OK);
			assertEquals("1", configs.get("featured_app_id"));
		}

		@Test
		public void verifyAuthTokenValid() {
			String authToken = login();
			Map<String, Object> claims = get("/authToken/verify", getSingleHeaderRequestEntity(null, "authToken", authToken), new ParameterizedTypeReference<>() {}, HttpStatus.OK);
			Map<String, Object> userClaims = (Map<String, Object>) claims.get("user");
			assertUserClaims(userClaims);
		}

		@Test
		public void verifyAuthTokenInvalid() {
			get("/authToken/verify", getSingleHeaderRequestEntity(null, "authToken", "myInvalidAuthToken"), String.class, HttpStatus.FORBIDDEN);
		}

		private void assertUserDto(UserDto userDto) {
			assertEquals(1, userDto.getId());
			assertEquals("destroflyer", userDto.getLogin());
			assertTrue(userDto.getOwnedAppIds().size() > 0);
		}

		private void assertUserClaims(Map<String, Object> userClaims) {
			assertEquals(1, userClaims.get("id"));
			assertEquals("destroflyer", userClaims.get("login"));
		}
	}

	@Nested
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	class TestLoggedIn {

		private String authToken;
		private HttpEntity<Void> emptyBody;

		@BeforeAll
		public void loginAndStoreAuthToken() {
			authToken = login();
			emptyBody = getRequestEntity(null);
		}

		@Test
		public void addAppToAccountAppNotFound() {
			post("/apps/999999/addToAccount", emptyBody, Void.class, HttpStatus.NOT_FOUND);
		}

		@Test
		public void removeAppFromAccountAppNotFound() {
			post("/apps/999999/removeFromAccount", emptyBody, Void.class, HttpStatus.NOT_FOUND);
		}

		@Test
		public void removeAppFromAccountAndAddAgain() {
			post("/apps/1/removeFromAccount", emptyBody, Void.class, HttpStatus.OK);
			post("/apps/1/removeFromAccount", emptyBody, Void.class, HttpStatus.FORBIDDEN);
			post("/apps/1/addToAccount", emptyBody, Void.class, HttpStatus.OK);
			post("/apps/1/addToAccount", emptyBody, Void.class, HttpStatus.FORBIDDEN);
		}

		private <T> HttpEntity<T> getRequestEntity(T body) {
			return getSingleHeaderRequestEntity(body, HttpHeaders.AUTHORIZATION, "Bearer " + authToken);
		}
	}

	private String login() {
		String saltClient = get("/users/destroflyer/saltClient", String.class, HttpStatus.OK);
		String clientHashedPassword = BCrypt.hashpw("test", saltClient);
		return post("/users/login", new LoginDto("destroflyer", clientHashedPassword), String.class, HttpStatus.OK);
	}

	private <T> HttpEntity<T> getSingleHeaderRequestEntity(T body, String headerName, String headerValue) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(headerName, headerValue);
		return new HttpEntity<>(body, headers);
	}

	private <T> T get(String path, Class<T> responseClass, HttpStatus expectedHttpStatus) {
		return get(path, null, responseClass, expectedHttpStatus);
	}

	private <T> T get(String path, HttpEntity<?> requestEntity, Class<T> responseClass, HttpStatus expectedHttpStatus) {
		return request(HttpMethod.GET, path, requestEntity, responseClass, expectedHttpStatus);
	}

	private <T> T get(String path, ParameterizedTypeReference<T> responseType, HttpStatus expectedHttpStatus) {
		return get(path, null, responseType, expectedHttpStatus);
	}

	private <T> T get(String path, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, HttpStatus expectedHttpStatus) {
		return request(HttpMethod.GET, path, requestEntity, responseType, expectedHttpStatus);
	}

	private <T> T post(String path, Object body, Class<T> responseClass, HttpStatus expectedHttpStatus) {
		return post(path, new HttpEntity<>(body), responseClass, expectedHttpStatus);
	}

	private <T> T post(String path, HttpEntity<?> requestEntity, Class<T> responseClass, HttpStatus expectedHttpStatus) {
		return request(HttpMethod.POST, path, requestEntity, responseClass, expectedHttpStatus);
	}

	private <T> T request(HttpMethod httpMethod, String path, HttpEntity<?> requestEntity, Class<T> responseClass, HttpStatus expectedHttpStatus) {
		return checkResponse(this.restTemplate.exchange(getUrl(path), httpMethod, requestEntity, responseClass), expectedHttpStatus);
	}

	private <T> T request(HttpMethod httpMethod, String path, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, HttpStatus expectedHttpStatus) {
		return checkResponse(this.restTemplate.exchange(getUrl(path), httpMethod, requestEntity, responseType), expectedHttpStatus);
	}

	private String getUrl(String path) {
		return "http://localhost:" + port + path;
	}

	private <T> T checkResponse(ResponseEntity<T> response, HttpStatus expectedHttpStatus) {
		assertEquals(expectedHttpStatus, response.getStatusCode());
		return response.getBody();
	}
}
