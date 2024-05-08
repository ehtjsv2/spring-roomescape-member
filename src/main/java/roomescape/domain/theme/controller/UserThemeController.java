package roomescape.domain.theme.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.theme.service.UserThemeService;

@RestController
public class UserThemeController {

    private final UserThemeService userThemeService;

    public UserThemeController(UserThemeService userThemeService) {
        this.userThemeService = userThemeService;
    }

    @GetMapping("/theme-ranking")
    public ResponseEntity<List<Theme>> getThemeRank() {
        return ResponseEntity.ok(userThemeService.getThemeRanking());
    }
}