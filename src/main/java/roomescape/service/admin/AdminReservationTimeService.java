package roomescape.service.admin;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeAddRequest;
import roomescape.repository.ReservationTimeRepository;

@Service
public class AdminReservationTimeService {

    private ReservationTimeRepository reservationTimeRepository;

    AdminReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTime> findAllReservationTime() {
        return reservationTimeRepository.findAll();
    }

    public ReservationTime addReservationTime(ReservationTimeAddRequest reservationTimeAddRequest) {
        if (reservationTimeRepository.existByStartAt(reservationTimeAddRequest.startAt())) {
            throw new IllegalArgumentException("이미 존재하는 예약시간은 추가할 수 없습니다.");
        }
        return reservationTimeRepository.insert(reservationTimeAddRequest.toEntity());
    }

    public void removeReservationTime(Long id) {
        if (reservationTimeRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("해당 id를 가진 예약시간이 존재하지 않습니다.");
        }
        reservationTimeRepository.deleteById(id);
    }
}