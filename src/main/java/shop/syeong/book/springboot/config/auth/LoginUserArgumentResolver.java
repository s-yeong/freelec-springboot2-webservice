package shop.syeong.book.springboot.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import shop.syeong.book.springboot.config.auth.dto.SessionUser;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    /**
     * HandlerMethodArgumentReslover
     * 조건에 맞는 경우 메소드가 있다면 HandlerMethodArguemtResolver의 구현체가
     * 지정한 값으로 해당 메소드의 파라미터로 넘길 수 있음
     */

    private final HttpSession httpSession;

    // 조건에 맞는 경우
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        /**
         * supportsParameter()
         * 컨트롤러 메소드의 특정 파라미터를 지원하는지 판단
         * 파라미터에 @LoginUser 어노테이션이 붙어있고, 파라미터 클래스 타입이 SessionUser.class인 경우 true 반환
         */

        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());

        return isLoginUserAnnotation && isUserClass;

    }

    // 파라미터 넘김
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
        throws Exception {
        /**
         * resolveArgument()
         * 파라미터에 전달할 객체를 생성 - 여기서는 세션에서 객체를 가져옴
         */
        return httpSession.getAttribute("user");
    }

}
