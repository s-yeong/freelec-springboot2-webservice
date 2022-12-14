package shop.syeong.book.springboot.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    // 각 사용자의 권한을 관리할 Enum 클래스 Role

    GUEST("ROLE_GUEST", "손님"),
    USER("ROLE_USER", "일반 사용자");

    private final String key;
    private final String title;

    //스프링 시큐리티에서는 권한 코드에 항상 ROLE_이 앞에 있어야 한다.

}
