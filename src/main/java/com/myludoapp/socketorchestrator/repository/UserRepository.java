package com.myludoapp.socketorchestrator.repository;

import com.myludoapp.socketorchestrator.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDto, String> {
}
