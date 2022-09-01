package shop.syeong.book.springboot.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import shop.syeong.book.springboot.config.auth.dto.SessionUser;
import shop.syeong.book.springboot.service.posts.PostsService;
import shop.syeong.book.springboot.web.dto.PostsResponseDto;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {
// 페이지 관련된 컨트롤러

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("posts",postsService.findAllDesc());
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        // 앞서 작성된 CustomOAuth2UserService에서 로그인 성공 시 세션에 SessionUser를 저장하도록 구성함
        // => 로그인 성공 시 httpSession.getAttribute("user")에서 값을 가져올 수 있음

        if(user != null) {
            model.addAttribute("userName", user.getName());
        }
        // 세션에 저장된 값이 있을 때만 model에 userName으로 등록
        // 세션에 저장된 값이 없으면 model에 아무런 값이 없는 상태이니 로그인 버튼이 보임
        return "index";
    }

    /**
     * Model
     * 서버 템플릿 엔진에서 사용할 수 있는 객체를 저장함
     * 여기서는 postsService.findAllDesc()로 가져온 결과를 posts로 index.mustache에 전달함
     */

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }

    /**
     * 머스테치 스타터 덕분에 컨트롤러에서 문자열을 반환할 떄 앞의 경로와 뒤의 파일 확장자는 자동으로 저장됨
     * 앞의 경로 : src/main/resuorces/templates
     * 뒤의 파일 확장자 : .mustache
     * => "index"를 반환하므로 src/main/resources/templates/index.mustache로 전환되어 View Resolver가 처리하게 됨
     */
    // View Resolver는 URL 요청의 결과를 전달할 타입과 값을 지정하는 관리자 격임
}
