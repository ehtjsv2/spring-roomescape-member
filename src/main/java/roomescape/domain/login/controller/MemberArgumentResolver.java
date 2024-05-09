package roomescape.domain.login.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.login.service.MemberService;
import roomescape.global.auth.JwtTokenProvider;
import roomescape.global.exception.ClientIllegalArgumentException;

@Component
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie[] cookie = request.getCookies();
        if (cookie == null) {
            throw new ClientIllegalArgumentException("쿠키가 존재하지 않습니다.");
        }
        String token = jwtTokenProvider.extractTokenFromCookie(request.getCookies());
        Long memberId = jwtTokenProvider.validateAndGetMemberId(token);
        return memberService.findMemberById(memberId);
    }
}
