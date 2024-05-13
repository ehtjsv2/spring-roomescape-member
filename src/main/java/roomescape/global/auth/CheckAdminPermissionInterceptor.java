package roomescape.global.auth;

import static roomescape.domain.member.domain.Role.ADMIN;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.domain.Member;
import roomescape.domain.member.service.MemberService;
import roomescape.global.exception.AuthorizationException;
import roomescape.global.exception.ClientIllegalArgumentException;

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
            throw new ClientIllegalArgumentException("쿠키가 존재하지 않습니다.");
        }
        String token = jwtTokenProvider.extractTokenFromCookie(cookies);
        Long memberId = jwtTokenProvider.validateAndGetLongSubject(token);
        Member member = memberService.findMemberById(memberId);
        if (member == null || member.getRole() != ADMIN) {
            throw new AuthorizationException("허가되지않은 접근입니다.");
        }
        return true;
    }
}
