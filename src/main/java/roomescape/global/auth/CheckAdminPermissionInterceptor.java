package roomescape.global.auth;

import static roomescape.domain.member.domain.Role.ADMIN;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.domain.Member;
import roomescape.domain.member.service.MemberService;

@Component
public class CheckAdminPermissionInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public CheckAdminPermissionInterceptor(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            response.setStatus(401);
            return false;
        }
        String token = jwtTokenProvider.extractTokenFromCookie(cookies);
        Long memberId = jwtTokenProvider.validateAndGetLongSubject(token);
        Member member = memberService.findMemberById(memberId);
        if (member == null || member.getRole() != ADMIN) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}