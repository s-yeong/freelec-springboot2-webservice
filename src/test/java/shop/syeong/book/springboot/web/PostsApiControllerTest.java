package shop.syeong.book.springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import shop.syeong.book.springboot.domain.posts.Posts;
import shop.syeong.book.springboot.domain.posts.PostsRepository;
import shop.syeong.book.springboot.web.dto.PostsSaveRequestDto;
import shop.syeong.book.springboot.web.dto.PostsUpdateRequestDto;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before // 매번 테스트가 시작되기 전에 MockMvc 인스턴스를 생성
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

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
    @WithMockUser(roles="USER")
    /**
     * @WithMockUser(roles="USER")
     * 인증된 모의(가짜) 사용자를 만들어서 사용
     * roles에 권한을 추가할 수 있음
     * => 이 어노테이션으로 인해 ROLE_USER 권한을 가진 사용자가 API를 요청하는 것과 동일한 효과를 가짐
     * MockMvc에서만 작동함 -> MockMvc 인스턴스 생성
     */
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
//        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());
        /**
         * mvc.perform
         * 생성된 MockMvc를 통해 API를 테스트
         * 본문(Body) 영역은 문자열로 표현하기 위해 ObjectMapper를 통해 문자열 JSON으로 변환
         */

        //then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);    // -- 이부분 더 공부하기
//        assertThat(responseEntity.getBody()).isGreaterThan(0L); // -- 이부분 더 공부하기

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);

    }

    @Test
    @WithMockUser(roles="USER")
    public void Posts_수정된다() throws Exception {
        //given
        Posts savePosts = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());
        Long updatedId = savePosts.getId();
        String expectTitle = "title2";
        String expectContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectTitle)
                .content(expectContent)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updatedId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);
        // HttpEntity는 @RequestBody와 기능적으로 비슷하나 추가적으로 요청의 헤더 정보를 가져올 수 있음

        //when
//        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT,
//                requestEntity, Long.class);
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectContent);

    }
    
}


