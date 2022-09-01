package shop.syeong.book.springboot.config.auth.dto;

import lombok.Getter;
import shop.syeong.book.springboot.domain.user.User;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    // 인증된 사용자 정보만 필요(그 외에 필요한 정보들은 없음)
    // User 클래스가 엔티티이기 때문에 언제 다른 엔티티와 관계가 형성될지 모름
    // 그렇기에 직렬화 기능을 가진 세션 Dto를 하나 추가로 만든것
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}


