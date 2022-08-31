package shop.syeong.book.springboot.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * 머스테치 스타터 덕분에 컨트롤러에서 문자열을 반환할 떄 앞의 경로와 뒤의 파일 확장자는 자동으로 저장됨
     * 앞의 경로 : src/main/resuorces/templates
     * 뒤의 파일 확장자 : .mustache
     * => "index"를 반환하므로 src/main/resources/templates/index.mustache로 전환되어 View Resolver가 처리하게 됨
     */
    // View Resolver는 URL 요청의 결과를 전달할 타입과 값을 지정하는 관리자 격임
}
