package roomescape.domain.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.fixture.LocalDateFixture.AFTER_THREE_DAYS_DATE;
import static roomescape.fixture.LocalDateFixture.AFTER_TWO_DAYS_DATE;
import static roomescape.fixture.LocalDateFixture.BEFORE_ONE_DAYS_DATE;
import static roomescape.fixture.LocalDateFixture.BEFORE_THREE_DAYS_DATE;
import static roomescape.fixture.LocalDateFixture.BEFORE_TWO_DAYS_DATE;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.RepositoryTest;
import roomescape.domain.reservation.domain.Reservation;
import roomescape.domain.reservationTime.domain.ReservationTime;
import roomescape.domain.theme.domain.Theme;

class ReservationRepositoryImplTest extends RepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ReservationRepositoryImpl reservationRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new ReservationRepositoryImpl(jdbcTemplate);
        jdbcTemplate.update("insert into reservation_time values(1,'10:00')");
        jdbcTemplate.update("insert into theme(name, description, thumbnail) values(?,?,?)", "리비", "머리 쓰는 중",
                "url");
        jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values(?,?,?,?)", "브라운",
                AFTER_TWO_DAYS_DATE, 1L, 1L);
    }

    @AfterEach
    void setDown() {
        jdbcTemplate.update("delete from reservation");
    }

    @DisplayName("예약목록 모두를 불러옵니다.")
    @Test
    void should_findAll() {
        int expectedSize = 1;

        int actualSize = reservationRepository.findAll().size();

        assertThat(actualSize).isEqualTo(expectedSize);
    }


    @DisplayName("원하는 ID의 예약을 불러옵니다.")
    @Test
    void should_findById() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "리비", "머리 쓰는 중", "url");
        Reservation expectedReservation = new Reservation(1L, "브라운", AFTER_TWO_DAYS_DATE, reservationTime, theme);

        List<Reservation> all = reservationRepository.findAll();
        assertThat(reservationRepository.findById(1L)).isPresent();
    }

    @DisplayName("예약을 추가할 수 있습니다.")
    @Test
    void should_insert() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "리비", "머리 쓰는 중", "url");
        Reservation reservation = new Reservation(null, "도도", AFTER_THREE_DAYS_DATE, reservationTime, theme);

        Reservation savedReservation = reservationRepository.insert(reservation);

        assertThat(savedReservation.getId()).isNotNull();
    }

    @DisplayName("원하는 ID의 예약을 삭제할 수 있습니다.")
    @Test
    void should_deleteById() {
        int expectedCount = 0;

        reservationRepository.deleteById(1L);
        int actualCount = jdbcTemplate.queryForObject("select count(*) from reservation where id = 1", Integer.class);

        assertThat(actualCount).isEqualTo(expectedCount);
    }

    @DisplayName("예약날짜와 예약 시간 ID와 테마 ID가 동일한 경우를 알 수 있습니다.")
    @Test
    void should_return_true_when_reservation_date_and_time_id_and_theme_id_equal() {
        assertThat(reservationRepository.existByDateAndTimeIdAndThemeId(AFTER_TWO_DAYS_DATE, 1L, 1L)).isTrue();
    }

    @DisplayName("예약날짜와 예약 시간 ID와 테마 ID가 동일하지 않은 경우를 알 수 있습니다.")
    @Test
    void should_return_false_when_reservation_date_and_time_id_and_theme_id_not_equal() {
        assertThat(reservationRepository.existByDateAndTimeIdAndThemeId(AFTER_THREE_DAYS_DATE, 1L, 1L)).isFalse();
    }

    @DisplayName("인기 테마 목록을 불러올 수 있습니다.")
    @Test
    void should_read_theme_ranking() {
        jdbcTemplate.update("insert into theme(name, description, thumbnail) values(?,?,?)", "테마2", "테마2 설명 쓰는 중",
                "url");
        jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values(?,?,?,?)", "브라운",
                BEFORE_TWO_DAYS_DATE, 1L, 2L);
        jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values(?,?,?,?)", "도도",
                BEFORE_THREE_DAYS_DATE, 1L, 2L);
        jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values(?,?,?,?)", "리비",
                BEFORE_ONE_DAYS_DATE, 1L, 1L);

        List<Theme> themeRaking = reservationRepository.findThemeOrderByReservationCount();

        assertAll(
                () -> assertThat(themeRaking).hasSize(2),
                () -> assertThat(themeRaking.get(0).getName()).isEqualTo("테마2")
        );

    }
}