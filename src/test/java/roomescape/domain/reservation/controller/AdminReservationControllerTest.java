package roomescape.domain.reservation.controller;

import static org.hamcrest.Matchers.is;
import static roomescape.fixture.LocalDateFixture.AFTER_THREE_DAYS_DATE;
import static roomescape.fixture.LocalDateFixture.AFTER_TWO_DAYS_DATE;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.ControllerTest;
import roomescape.domain.login.dto.LoginRequest;
import roomescape.domain.reservation.dto.ReservationAddRequest;

public class AdminReservationControllerTest extends ControllerTest {

    private static final String EMAIL = "admin@gmail.com";
    private static final String PASSWORD = "123456";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update(
                "insert into member (name, email, password, role) values ('어드민', 'admin@gmail.com', '123456', 'ADMIN')");
        jdbcTemplate.update("insert into reservation_time (start_at) values(?)"
                , "10:00");
        jdbcTemplate.update("insert into theme (name, description, thumbnail )values(?,?,?)"
                , "테마1", "테마1설명", "url");
        jdbcTemplate.update("insert into reservation (date, time_id, theme_id, member_id) values(?,?,?,?)"
                , AFTER_TWO_DAYS_DATE, 1, 1, 1);
    }

    @AfterEach
    void setDown() {
        jdbcTemplate.update("delete from reservation");
    }

    @DisplayName("예약 목록을 불러올 수 있다.")
    @Test
    void should_response_reservation_list_when_request_reservations() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("어드민의 예약을 추가를 성공할 시, 201 ok를 응답한다,")
    @Test
    void should_add_reservation_when_post_request_admin_reservations() {
        LoginRequest loginRequest = new LoginRequest(EMAIL, PASSWORD);
        String cookie = RestAssured.given().log().all()
                .body(loginRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().header("Set-Cookie").split(";")[0];

        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(
                AFTER_THREE_DAYS_DATE, 1L, 1L, null);
        RestAssured.given().log().all()
                .header("Cookie", cookie)
                .contentType(ContentType.JSON)
                .body(reservationAddRequest)
                .when().post("admin/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("존재하는 리소스에 대한 삭제 요청시, 204 no content를 응답한다.")
    @Test
    void should_remove_reservation_when_delete_request_reservations_id() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("존재하지 않는 리소스에 대한 삭제 요청시, 500 Internel Server Error를 응답한다.")
    @Test
    void should_response_bad_request_when_nonExist_id() {
        RestAssured.given().log().all()
                .when().delete("/reservations/2")
                .then().log().all()
                .statusCode(400);
    }
}
