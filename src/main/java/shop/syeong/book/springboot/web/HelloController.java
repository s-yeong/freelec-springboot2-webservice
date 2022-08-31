package shop.syeong.book.springboot.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.syeong.book.springboot.web.dto.HelloResponseDto;

@RestController // 컨트롤러를 JSON으로 반환하는 컨트롤러로 만들어 줌
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/hello/dto")
    public HelloResponseDto helloDto(@RequestParam("name") String name, @RequestParam("amount") int amount) {
        // @RequestParam : 외부에서 API로 넘긴 파라미터를 가져옴 -> name과 amount는 API를 호출하는 곳에서 넘겨준 값들

        return new HelloResponseDto(name, amount);
    }

}
