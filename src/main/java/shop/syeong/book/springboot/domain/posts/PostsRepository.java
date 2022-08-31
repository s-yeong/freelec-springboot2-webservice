package shop.syeong.book.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

// Entity 클래스와 기본 Entity Repository는 함께 위치해야 함 - Entity 클래스는 기본 Repository 없이는 제대로 역하을 할 수 없음
// JpaRepository<Entity 클래스, PK 타입>
public interface PostsRepository extends JpaRepository<Posts, Long> {

}

/**
 * 보통 Dao라고 불리는 DB Layer 접근자에 해당
 * JPA에선 Repository라고 부르며 인터페이스로 생성
 * 단순히 인터페이스를 생성 후, JpaRepository<Entity 클래스, Pk 타입>을 상속하면 기본적인 CRUD 메소드가 자동으로 생성됨
 */
