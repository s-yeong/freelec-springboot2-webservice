package shop.syeong.book.springboot.service.posts;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.syeong.book.springboot.domain.posts.Posts;
import shop.syeong.book.springboot.domain.posts.PostsRepository;
import shop.syeong.book.springboot.web.dto.PostsListResponseDto;
import shop.syeong.book.springboot.web.dto.PostsResponseDto;
import shop.syeong.book.springboot.web.dto.PostsSaveRequestDto;
import shop.syeong.book.springboot.web.dto.PostsUpdateRequestDto;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    // 게시물 등록
    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).
                getId();
    }

    // 게시물 수정
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)  // Entity 반환, Optional로 감싸져있음
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    // 게시물 조회
    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }

    // 게시글 전체 조회
    @Transactional(readOnly = true)     // readonly = true => 트랜잭션 범위는 유지하되, 조회 기능만 남겨두어 조회 속도가 개선됨
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAlldesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }
    // .map(PostsListResponseDto::new) ==> .map(posts -> new PostsListResponseDto(posts))
    // postsRepository 결과로 넘어온 Posts의 Stream을 map을 통해 PostsListResponseDto 변환 -> List로 반환하는 메소드

}

/**
 * update 기능에서 데이터베이스에 쿼리를 날리는 부분이 없다 -> JPA의 영속성 컨텍스트 때문
 * 영속성 컨텍스트 : 엔티티를 영구 저장하는 환경(JPA의 핵심 내용은 엔티티가 영속성 컨텍스트에 포함되어 있냐 아니냐로 갈림)
 * 이 데이터는 영속성 컨텍스트가 유지된 상태
 * 이 상태에서 해당 데이터의 값을 변경하면 트랜잭션이 끝나는 시점에 해당 테이블에 변경분을 반영
 * => Entity 객체의 값만 변경하면 별도로 Update 쿼리를 날릴 필요가 없음(더티 체킹)
 */