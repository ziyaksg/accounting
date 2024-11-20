package com.uydev.entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BaseEntityListener extends AuditingEntityListener {

    @PrePersist
    private void onPrePersist(BaseEntity baseEntity) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        baseEntity.setInsertDateTime(LocalDateTime.now());
        baseEntity.setLastUpdateDateTime(LocalDateTime.now());

        baseEntity.setInsertUserId(1L);
        baseEntity.setLastUpdateUserId(1L);

//        if (authentication!=null && !authentication.getName().equals("anonymousUser")) {
//            baseEntity.setInsertUserId(((UserPrincipal) authentication.getPrincipal()).getId());
//            baseEntity.setLastUpdateUserId(((UserPrincipal) authentication.getPrincipal()).getId());
//        } else {
//            baseEntity.setInsertUserId(-1L);
//            baseEntity.setLastUpdateUserId(-1L);
//        }
    }

    @PreUpdate
    private void onPreUpdate(BaseEntity baseEntity) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        baseEntity.setLastUpdateDateTime(LocalDateTime.now());
        baseEntity.setLastUpdateUserId(1L);
//        if (authentication!=null && !authentication.getName().equals("anonymousUser")) {
//            baseEntity.setLastUpdateUserId(((UserPrincipal) authentication.getPrincipal()).getId());
//        } else {
//            baseEntity.setInsertUserId(-1L);
//            baseEntity.setLastUpdateUserId(-1L);
//        }
    }
}