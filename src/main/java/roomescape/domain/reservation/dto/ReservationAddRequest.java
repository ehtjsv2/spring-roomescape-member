package roomescape.domain.reservation.dto;

import java.time.LocalDate;
import roomescape.domain.reservation.domain.Reservation;
import roomescape.domain.reservationTime.domain.ReservationTime;
import roomescape.domain.theme.domain.Theme;
import roomescape.exception.ClientIllegalArgumentException;

public record ReservationAddRequest(LocalDate date, String name, Long timeId, Long themeId) {

    public ReservationAddRequest {
        if (date.isBefore(LocalDate.now())) {
            throw new ClientIllegalArgumentException(date + ": 예약 날짜는 현재 보다 이전일 수 없습니다");
        }
    }

    public Reservation toEntity(ReservationTime reservationTime, Theme theme) {
        return new Reservation(null, name, date, reservationTime, theme);
    }
}