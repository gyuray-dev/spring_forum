package dev.gyuray.forum.repository.user;

import dev.gyuray.forum.domain.Address;
import dev.gyuray.forum.domain.Comment;
import dev.gyuray.forum.domain.Post;
import dev.gyuray.forum.domain.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserUpdateDTO {
    private Long id;
    private Address address;
    private Role role;
    private List<Post> posts;
    private List<Comment> comments;
}
