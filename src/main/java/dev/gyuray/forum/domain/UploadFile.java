package dev.gyuray.forum.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@EqualsAndHashCode
public class UploadFile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFileName;
    private String storedFileName;
    private String size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public UploadFile() {
    }

    public UploadFile(String originalFileName, String storedFileName, long size) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.size = formattedSize(size);
    }

    public void addToPost(Post post) {
        this.post = post;
        post.getUploadFiles().add(this);
    }
    
    public String formattedSize(Long size) {
        double digits = Math.log10(size);

        if (digits < 3) {
            return size + "B";
        } else if (digits < 6) {
            return size / 1_000 + "KB";
        } else {
            return size / 1_000_000 + "MB";
        }
    }

}
