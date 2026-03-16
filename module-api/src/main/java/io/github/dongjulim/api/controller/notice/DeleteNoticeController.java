package io.github.dongjulim.api.controller.notice;

import io.github.dongjulim.domain.notice.usecase.DeleteNoticeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeleteNoticeController {

    private final DeleteNoticeUseCase deleteNoticeUseCase;

    @DeleteMapping("/api/v2/notice/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        deleteNoticeUseCase.deleteNotice(id);
        return ResponseEntity.noContent().build();
    }
}
