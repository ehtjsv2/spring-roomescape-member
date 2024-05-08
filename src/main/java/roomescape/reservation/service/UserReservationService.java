package roomescape.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservation.dto.BookableTimeResponse;
import roomescape.reservation.dto.BookableTimesRequest;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationTime.repository.ReservationTimeRepository;

@Service
public class UserReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public UserReservationService(ReservationRepository reservationRepository,
                                  ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<BookableTimeResponse> findBookableTimes(BookableTimesRequest bookableTimesRequest) {
        List<ReservationTime> bookedTimes = reservationRepository.findTimesByDateAndTheme(bookableTimesRequest.date(),
                bookableTimesRequest.themeId());
        List<ReservationTime> allTimes = reservationTimeRepository.findAll();
        return allTimes.stream()
                .map(time -> {
                    return new BookableTimeResponse(time.getStartAt(), time.getId(), isBookedTime(bookedTimes, time));
                })
                .toList();
    }

    private boolean isBookedTime(List<ReservationTime> bookedTimes, ReservationTime time) {
        return bookedTimes.contains(time);
    }
}