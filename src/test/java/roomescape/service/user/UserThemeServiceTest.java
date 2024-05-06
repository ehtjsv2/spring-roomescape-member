package roomescape.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;

@ExtendWith(MockitoExtension.class)
class UserThemeServiceTest {

    @InjectMocks
    UserThemeService userThemeService;

    @Mock
    ReservationRepository reservationRepository;

    @DisplayName("인기 테마를 알 수 있습니다.")
    @Test
    void should_get_theme_ranking() {
        Theme theme = new Theme(1L, "테마1", "테마1설명", "url");
        when(reservationRepository.findThemeOrderByReservationCount())
                .thenReturn(List.of(theme));

        List<Theme> themeRanking = userThemeService.getThemeRanking();

        assertAll(
                () -> assertThat(themeRanking).hasSize(1),
                () -> assertThat(themeRanking.get(0)).isEqualTo(theme)
        );

    }
}