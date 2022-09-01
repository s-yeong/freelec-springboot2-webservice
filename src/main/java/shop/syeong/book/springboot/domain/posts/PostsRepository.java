package shop.syeong.book.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// Entity 클래스와 기본 Entity Repository는 함께 위치해야 함 - Entity 클래스는 기본 Repository 없이는 제대로 역하을 할 수 없음
// JpaRepository<Entity 클래스, PK 타입>
public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("select p from Posts p order by p.id desc")
    List<Posts> findAlldesc();

    // SpringDataJpa에서 제공하지 않는 메소드는 위처럼 쿼리로 작성해도 되는 것을 보여주려고 @Query 사용
    // => 이 쿼리는 SpringDataJpa에서 제공하는 기본 메소드로 해결 가능!! (다만 @Qurey가 훨씬 가독성 좋음)

}

/**
 * 보통 Dao라고 불리는 DB Layer 접근자에 해당
 * JPA에선 Repository라고 부르며 인터페이스로 생성
 * 단순히 인터페이스를 생성 후, JpaRepository<Entity 클래스, Pk 타입>을 상속하면 기본적인 CRUD 메소드가 자동으로 생성됨
 */

/**
 * 규모가 있는 프로젝트에서의 데이터 조회는 FK의 조인, 복잡한 조건 등으로 인해 이런 Entity 클래스만으로 처리하기 어려워 조회용 프레임워크를 추가로 사용함
 * ex) Querydsl, Mybatis, Jooq
 * 조회 - 3가지 프레임 워크, 등록/수정/삭제, SpringDataJpa
 */