package dev.gyuray.forum.service;

import dev.gyuray.forum.repository.post.PostListDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class PostPagerDTO {
    private List<PostListDTO> postListDTOs;
    private int currentPage;
    private int startPage;
    private int endPage;
    private int lastPage;
    private boolean hasNextPages;
    private int pageSize;
}
