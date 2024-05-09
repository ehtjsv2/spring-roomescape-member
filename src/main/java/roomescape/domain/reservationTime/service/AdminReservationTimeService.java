package roomescape.domain.reservationTime.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.global.exception.ClientIllegalArgumentException;
import roomescape.domain.reservationTime.domain.ReservationTime;
import roomescape.domain.reservationTime.dto.ReservationTimeAddRequest;
import roomescape.domain.reservationTime.repository.ReservationTimeRepository;

@Service
public class AdminReservationTimeService {

    private ReservationTimeRepository reservationTimeRepository;

    public AdminReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTime> findAllReservationTime() {
        return reservationTimeRepository.findAll();
    }

    public ReservationTime addReservationTime(ReservationTimeAddRequest reservationTimeAddRequest) {
        if (reservationTimeRepository.existByStartAt(reservationTimeAddRequest.startAt())) {
            throw new ClientIllegalArgumentException("이미 존재하는 예약시간은 추가할 수 없습니다.");
        }
        return reservationTimeRepository.insert(reservationTimeAddRequest.toEntity());
    }

    public void removeReservationTime(Long id) {
        if (reservationTimeRepository.findById(id).isEmpty()) {
            throw new ClientIllegalArgumentException("해당 id를 가진 예약시간이 존재하지 않습니다.");
        }
        reservationTimeRepository.deleteById(id);
    }
}
