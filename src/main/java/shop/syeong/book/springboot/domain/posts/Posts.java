package shop.syeong.book.springboot.domain.posts;

import com.sun.javafx.beans.IDProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor  // 기본 생성자 자동 추가 - public Posts() {}와 같은 효과
@Entity // JPA의 어노테이션
/**
 * @Entity
 * 테이블과 링크될 클래스임을 나타냄
 * 기본적으로 클래스의 카멜케이스 이름을 언더스코어 네이밍(_)으로 테이블 이름을 매칭함 ex)SalesManager.java -> sales_manager table
 */
public class Posts {    // 주요 어노테이션을 클래스에 가깝게
// Posts 클래스는 실제 DB의 테이블과 매칭될 클래스(보통 Entity 클래스라고 함)
// JPA를 사용하면 DB 데이터에 작업할 경우 실제 쿼리를 날리기 보다는, 이 Entity 클래스의 수정을 통해 작업함

    @Id // 해당 테이블의 PK 필드
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * @GeneratedValue
     * Pk의 생성 규칙을 나타냄
     * GenerationType.IDENTITY -> auto_increment
     */

    @Column(length = 500, nullable = false)
    private String title;


    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /**
     * @Column
     * 테이블의 칼럼을 나타냄, 굳이 선언하지 않더라도 해당 클래스의 필드는 모두 칼럼이 됨
     * 기본값 외에 추가로 변경이 필요한 옵션이 있을 때 사용
     * 문자열의 경우 VARCHAR(255)가 기본값인데, 사이즈를 늘리거나, 타입을 변경하고 싶거나 등에 사용
     */

    private String author;

    @Builder    // 해당 클래스의 빌더 패턴 클래스 생성, 생성자 상단에 선언시 생성자에 포함된 필드만 빌더에 포함
    // 생성자나 빌더나 생성 시점에 값을 채워주는 역할은 똑같음
    public Posts(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

}


/**
 * "Setter 메소드가 없음"
 * -> getter/setter를 무작정 생성하는 경우 해당 클래스의 인스턴스 값들이 언제 어디서 변해야 하는지 코드상으로 명확하게 구분할 수가 없어
 * 차후 기능 변경 시 정말 복잡해짐
 * -> 해당 필드의 값 변경이 필요하면 명확히 그 목적과 의도를 나타낼 수 있는 메소드를 추가해야함
 */

/**
 * public class Order {
 *     public void cancleOrder() {
 *         this.status = false;
 *     }
 * }
 * public void 주문서비스의_취소이벤트 () {
 *     order.cancelOrder();
 * }
 */

/**
 * "Setter가 없는 상황에서 DB에 삽입 방법"
 * 기본적인 구조 - 생성자를 통해 최종값을 채운 후 DB에 삽입하는 것
 * 값 변경이 필요한 경우 해당 이벤트에 맞는 public 메소들르 호출하여 변경하는 것을 전제로 함
 * >> 생성자 대신 @Builder를 통해 제공되는 클래스를 사용 <<
 * 생성자의 경우 지금 채워야 할 필드가 무엇인지 명확히 지정할 수 없음
 * <-> 빌더를 사용하면 어느 필드에 어떤 값을 채워야 할지 명확하게 인지할 수 있음!!
 */
