package roomescape.service.admin;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationAddRequest;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;
import roomescape.repository.ThemeDao;

@Service
public class AdminReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public AdminReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public List<Reservation> findAllReservation() {
        return reservationDao.findAll();
    }

    public Reservation addReservation(ReservationAddRequest reservationAddRequest) {
        if (reservationDao.existByDateAndTimeIdAndThemeId(reservationAddRequest.getDate(),
                reservationAddRequest.getTimeId(), reservationAddRequest.getThemeId())) {
            throw new IllegalArgumentException("예약 날짜와 예약시간 그리고 테마가 겹치는 예약은 할 수 없습니다.");
        }

        ReservationTime reservationTime = getReservationTime(reservationAddRequest.getTimeId());
        Theme theme = getTheme(reservationAddRequest);

        Reservation reservationRequest = reservationAddRequest.toEntity(reservationTime, theme);
        return reservationDao.insert(reservationRequest);
    }

    private Theme getTheme(ReservationAddRequest reservationAddRequest) {
        return themeDao.findById(reservationAddRequest.getThemeId())
                .orElseThrow(() -> new IllegalArgumentException("존재 하지 않는 테마로 예약할 수 없습니다"));
    }

    private ReservationTime getReservationTime(Long reservationTimeId) {
        return reservationTimeDao.findById(reservationTimeId)
                .orElseThrow(() -> new IllegalArgumentException("존재 하지 않는 예약시각으로 예약할 수 없습니다."));
    }

    public void removeReservation(Long id) {
        if (reservationDao.findById(id).isEmpty()) {
            throw new IllegalArgumentException("해당 id를 가진 예약이 존재하지 않습니다.");
        }
        reservationDao.deleteById(id);
    }
}
