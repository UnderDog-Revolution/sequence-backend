package com.seq.deleteAccount.service;

import com.seq.deleteAccount.entity.DeletedUserEntity;
import com.seq.deleteAccount.repository.DeletedUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeletedUserService {

    private final DeletedUserRepository deletedUserRepository;

    public DeletedUserService(DeletedUserRepository deletedUserRepository) {
        this.deletedUserRepository = deletedUserRepository;
    }

    @Transactional
    public void saveDeletedUser(Long userId, String username,Boolean is_deleted ,String reason) {
        DeletedUserEntity deletedUser = new DeletedUserEntity(userId, username, is_deleted, reason);
        deletedUserRepository.save(deletedUser);
    }

    // 사용자 탈퇴 여부 확인(id)
    public boolean isDeletedUser(Long userId) {
        return deletedUserRepository.existsByUserId(userId);
    }

    // 사용자 탈퇴 여부 확인(username)
    public boolean isDeletedUser(String username) {
        return deletedUserRepository.existsByUsername(username);
    }
}
