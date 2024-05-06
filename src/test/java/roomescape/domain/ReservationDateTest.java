package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.fixture.LocalDateFixture.AFTER_ONE_DAYS_DATE;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.ClientIllegalArgumentException;

class ReservationDateTest {

    @DisplayName("날짜가 정상적인 값일 경우 예외가 발생하지 않습니다.")
    @Test
    void should_not_throw_exception_when_reservation_date_is_right() {
        assertThatCode(() -> new ReservationDate(AFTER_ONE_DAYS_DATE))
                .doesNotThrowAnyException();
    }

    @DisplayName("date가 null이면 ReservationDate생성 시 예외가 발생한다")
    @Test
    void should_throw_NPE_when_date_is_null() {
        assertThatThrownBy(() -> new ReservationDate(null))
                .isInstanceOf(ClientIllegalArgumentException.class)
                .hasMessage("날짜는 null일 수 없습니다");
    }

    @DisplayName("date가 현재 날짜 보다 이전 날짜이면 ReservationDate생성 시 예외가 발생한다")
    @Test
    void should_throw_IllegalArgument_when_date_is_past() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        assertThatThrownBy(() -> new ReservationDate(pastDate))
                .isInstanceOf(ClientIllegalArgumentException.class)
                .hasMessage(pastDate + ": 예약 날짜는 현재 보다 이전일 수 없습니다");
    }
}
