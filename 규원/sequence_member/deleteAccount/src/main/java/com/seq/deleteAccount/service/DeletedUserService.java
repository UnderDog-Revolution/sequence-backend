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
    public void saveDeletedUser(String username,Boolean is_deleted ,String reason) {
        DeletedUserEntity deletedUser = new DeletedUserEntity(username, is_deleted, reason);
        deletedUserRepository.save(deletedUser);
    }
}
