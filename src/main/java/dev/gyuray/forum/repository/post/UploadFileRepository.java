package dev.gyuray.forum.repository.post;

import dev.gyuray.forum.domain.UploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class UploadFileRepository {

    private final EntityManager em;

    public Optional<UploadFile> findOne(Long uploadFileId) {
        return Optional.ofNullable(em.find(UploadFile.class, uploadFileId));
    }

    public void delete(Long uploadFileId) {
        UploadFile uploadFile = em.find(UploadFile.class, uploadFileId);
        em.remove(uploadFile);
    }
}
