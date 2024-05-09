package roomescape.domain.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.domain.Reservation;
import roomescape.domain.reservationTime.domain.ReservationTime;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.reservation.dto.ReservationAddRequest;
import roomescape.global.exception.ClientIllegalArgumentException;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservationTime.repository.ReservationTimeRepository;
import roomescape.domain.theme.repository.ThemeRepository;

@Service
public class AdminReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public AdminReservationService(ReservationRepository reservationRepository,
                                   ReservationTimeRepository reservationTimeRepository,
                                   ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<Reservation> findAllReservation() {
        return reservationRepository.findAll();
    }

    public Reservation addReservation(ReservationAddRequest reservationAddRequest) {
        if (reservationRepository.existByDateAndTimeIdAndThemeId(reservationAddRequest.date(),
                reservationAddRequest.timeId(), reservationAddRequest.themeId())) {
            throw new ClientIllegalArgumentException("예약 날짜와 예약시간 그리고 테마가 겹치는 예약은 할 수 없습니다.");
        }

        ReservationTime reservationTime = getReservationTime(reservationAddRequest.timeId());
        Theme theme = getTheme(reservationAddRequest);

        Reservation reservationRequest = reservationAddRequest.toEntity(reservationTime, theme);
        return reservationRepository.insert(reservationRequest);
    }

    private Theme getTheme(ReservationAddRequest reservationAddRequest) {
        return themeRepository.findById(reservationAddRequest.themeId())
                .orElseThrow(() -> new ClientIllegalArgumentException("존재 하지 않는 테마로 예약할 수 없습니다"));
    }

    private ReservationTime getReservationTime(Long reservationTimeId) {
        return reservationTimeRepository.findById(reservationTimeId)
                .orElseThrow(() -> new ClientIllegalArgumentException("존재 하지 않는 예약시각으로 예약할 수 없습니다."));
    }

    public void removeReservation(Long id) {
        if (reservationRepository.findById(id).isEmpty()) {
            throw new ClientIllegalArgumentException("해당 id를 가진 예약이 존재하지 않습니다.");
        }
        reservationRepository.deleteById(id);
    }
}
