package shop.syeong.book.springboot.config.auth;


import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import shop.syeong.book.springboot.config.auth.dto.OAuthAttributes;
import shop.syeong.book.springboot.config.auth.dto.SessionUser;
import shop.syeong.book.springboot.domain.user.User;
import shop.syeong.book.springboot.domain.user.UserReository;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
// 구글 로그인 이후 가져온 사용자의 정보(email, name, picture 등)들을 기반으로 가입 및 정보수정, 세션 저장 등의 기능을 지원

    private final UserReository userReository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        /**
         * registrationId
         * 현재 로그인 진행 중인 서비스를 구분하는 코드
         * 지금은 구글만 사용하는 불필요한 값이지만, 이후 네이버 로그인 연동 시에 네이버 로그인인지, 구글 로그인인지 구분하기 위해 사용
         */
        /**
         * userNameAttributeName
         * OAuth2 로그인 진행 시 키가 되는 필드값 - Primary Key와 같은 의미
         * 구글의 경우 기본적으로 코드를 지원하지만, 네이버 카카오 등은 기본 지원하지 않는다. 구굴의 기본 코드는 "sub"이다
         * 이후 네이버 로그인과 구글 로그인을 동시 지원할 때 사용된다
         */

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        /**
         * OAuthAttributes
         * OAuth2UserService를 통해 가져온 Oauth2User의 attribute를 담은 클래스
         * 이후 네이버 등 다른 소셜 로그인도 이 클래스를 사용
         */
        User user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(user));
        /**
         * SessionUser
         * 세션에 사용자 정보를 저장하기 위한 Dto 클래스
         * User 클래스를 쓰지 않고 새로 만들어서 사용
         */
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributekey());
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userReository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());
        return userReository.save(user);
        }
    }
