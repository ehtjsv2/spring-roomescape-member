package roomescape.domain.login.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.global.auth.JwtTokenProvider;
import roomescape.domain.login.domain.Member;
import roomescape.domain.login.dto.LoginRequest;
import roomescape.domain.login.dto.MemberResponse;
import roomescape.domain.login.service.LoginService;

@RestController
public class LoginController {

    private final JwtTokenProvider jwtTokenProvider;
    private final LoginService loginService;

    public LoginController(JwtTokenProvider jwtTokenProvider, LoginService loginService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Member member = loginService.findMemberByEmailAndPassword(loginRequest.email(),
                loginRequest.password());
        Cookie cookie = new Cookie("token", jwtTokenProvider.generateToken(String.valueOf(member.getId())));
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        String token = jwtTokenProvider.extractTokenFromCookie(request.getCookies());
        Long memberId = jwtTokenProvider.validateAndGetMemberId(token);
        Member member = loginService.findMemberById(memberId);
        return ResponseEntity.ok(new MemberResponse(member.getName()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logOut(HttpServletResponse response) {
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
