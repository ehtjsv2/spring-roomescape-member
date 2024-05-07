package roomescape.controller.admin;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeAddRequest;
import roomescape.service.admin.AdminReservationTimeService;

@RestController
public class AdminReservationTimeController {

    private final AdminReservationTimeService adminReservationTimeService;

    public AdminReservationTimeController(AdminReservationTimeService adminReservationTimeService) {
        this.adminReservationTimeService = adminReservationTimeService;
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTime>> getReservationTimeList() {
        return ResponseEntity.ok(adminReservationTimeService.findAllReservationTime());
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTime> addReservationTime(
            @RequestBody ReservationTimeAddRequest reservationTimeAddRequest) {
        ReservationTime reservationTime = adminReservationTimeService.addReservationTime(reservationTimeAddRequest);
        return ResponseEntity.created(URI.create("/times/" + reservationTime.getId())).body(reservationTime);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable("id") Long id) {
        adminReservationTimeService.removeReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
