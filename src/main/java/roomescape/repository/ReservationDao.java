package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;

public interface ReservationDao {

    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    Reservation insert(Reservation reservation);

    void deleteById(Long id);
}
