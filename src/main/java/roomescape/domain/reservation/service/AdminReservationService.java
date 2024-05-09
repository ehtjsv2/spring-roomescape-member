package roomescape.domain.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.login.domain.Member;
import roomescape.domain.login.repository.MemberRepository;
import roomescape.domain.reservation.domain.Reservation;
import roomescape.domain.reservation.dto.ReservationAddRequest;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservationTime.domain.ReservationTime;
import roomescape.domain.reservationTime.repository.ReservationTimeRepository;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.global.exception.ClientIllegalArgumentException;

@Service
public class AdminReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public AdminReservationService(ReservationRepository reservationRepository,
                                   ReservationTimeRepository reservationTimeRepository,
                                   ThemeRepository themeRepository, MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
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
        Theme theme = getTheme(reservationAddRequest.themeId());
        Member member = getMember(reservationAddRequest.memberId());

        Reservation reservationRequest = reservationAddRequest.toEntity(reservationTime, theme, member);
        return reservationRepository.insert(reservationRequest);
    }

    private Theme getTheme(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new ClientIllegalArgumentException("존재 하지 않는 테마로 예약할 수 없습니다"));
    }

    private ReservationTime getReservationTime(Long reservationTimeId) {
        return reservationTimeRepository.findById(reservationTimeId)
                .orElseThrow(() -> new ClientIllegalArgumentException("존재 하지 않는 예약시각으로 예약할 수 없습니다."));
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ClientIllegalArgumentException("존재 하지 않는 멤버로 예약할 수 없습니다."));
    }

    public void removeReservation(Long id) {
        if (reservationRepository.findById(id).isEmpty()) {
            throw new ClientIllegalArgumentException("해당 id를 가진 예약이 존재하지 않습니다.");
        }
        reservationRepository.deleteById(id);
    }
}
