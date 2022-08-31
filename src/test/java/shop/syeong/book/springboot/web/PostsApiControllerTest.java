package shop.syeong.book.springboot.web;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import shop.syeong.book.springboot.domain.posts.Posts;
import shop.syeong.book.springboot.domain.posts.PostsRepository;
import shop.syeong.book.springboot.web.dto.PostsSaveRequestDto;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
/**
 * 테스트를 진행할 때 JUnit에 내장된 실행자 외에 다른 실행자를 실행시킨다.
 * SpringRunner라는 스프링 실행자를 사용
 * -> 스프릥 부트 테스트와 JUnit 사이에 연결자 역할
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// WebEnvironment.RANDOM_PORT로 인한 랜덤 포트로 실행
/**
 * Api Controller 테스트를 하는데 HelloController와 달리 @WebMvcTest를 사용하지 않음
 * @WebMvcTest의 경우 JPA 기능이 작동하지 않기 때문임
 * => Controller와 ControllerAdvice 등 외부 연동과 관련된 부분만 활성화되니 지금 같이 JPA 기능까지 한번에 테스트 할 때는
 * @SpringBootTest와 @TestRestTemplate을 사용하면 된다.
 */
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }
    /**
     * @After
     * Junit에서 단위 테스트가 끝날 때 마다 수행되는 메소드를 지정
     * 보통은 배포 전 전체 테스트를 수행할 때 테스트간 데이터 침범을 막기 위해 사용함
     * 여러 테스트가 동시에 수행되면 테스트용 데이터베이스인 H2에 데이터가 그대로 남아 있어 다음 테스트 실행 시 테스트가 실패할 수 있음
     */

    @Test
    public void Posts_등록된다() throws Exception {
        //given
        String title = "title";
        String content = "content";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);    // -- 이부분 더 공부하기
        assertThat(responseEntity.getBody()).isGreaterThan(0L); // -- 이부분 더 공부하기

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);


    }
}


