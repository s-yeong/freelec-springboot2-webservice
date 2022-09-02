package shop.syeong.book.springboot.web;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import shop.syeong.book.springboot.config.auth.SecurityConfig;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
/**
 * 테스트를 진행할 때 JUnit에 내장된 실행자 외에 다른 실행자를 실행시킨다.
 * SpringRunner라는 스프링 실행자를 사용
 * -> 스프릥 부트 테스트와 JUnit 사이에 연결자 역할
 */
@WebMvcTest(controllers = HelloController.class,
        excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
                // 스캔 대상에서 SecurityConfig 제거
})
/**
 * 여러 스프링 테스트 어노테이션 중, Web(Srping MVC)에 집중할 수 있는 어노테이션
 * 선언할 경우 @Controller, @ControllerAdvice 등을 사용할 수 있음
 * 단, @Service, @Component, @Repository 등은 사용할 수 없음
 */
public class HelloControllerTest {

    @Autowired
    private MockMvc mvc;
    /**
     * 웹 API를 테스트할 때 사용
     * 스프링 MVC 테스트의 시작점
     * 이 클래스를 통해 HTTP GET, POST 등에 대한 API 테스트를 할 수 있음
     */

    @WithMockUser(roles="USER")
    @Test
    public void hello가_리턴된다() throws Exception {
        String hello = "hello";

        mvc.perform(get("/hello"))  // MockMvc를 통해 /hello 주소로 HTTP GET 요청을 함,
                // 체이닝이 지원되어 아래와 같이 여러 검정 기능을 이어서 선언할 수 있음.
                .andExpect(status().isOk()) // mvc.perform의 결과 검증
                //HTTP Header의 Status 검증, 우리가 흔히 알고 있는 200, 404, 500 등의 상태 검증
                // 여기선 -> 200인지 아닌지 검증
                .andExpect(content().string(hello)); // mvc.perform의 결과 검증
                // 응답 본문의 내용 검증
                // Controller에서 "hello"를 리턴하기 때문에 이값이 맞는지 검증

    }

    // JSON이 리턴되는 API
    @WithMockUser(roles="USER")
    @Test
    public void helloDto가_리턴된다() throws Exception {
        String name = "hello";
        int amount = 1000;

        mvc.perform(
                get("/hello/dto")
                        .param("name", name)  // param : API 테스트할 때 사용될 요청 파라미터를 설정
                        // 단 값은 String만 허용 -> 다른 데이터들 문자열로 변경해야 가능!!
                        .param("amount", String.valueOf(amount)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(name)))    // JSON 응답값을 필드별로 검증할 수 있는 메소드
                        // $를 기준으로 필드명을 명시
        .andExpect(jsonPath("$.amount",is(amount)));

    }



}
