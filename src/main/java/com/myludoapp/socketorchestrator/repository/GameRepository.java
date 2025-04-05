package com.myludoapp.socketorchestrator.repository;

import com.myludoapp.socketorchestrator.dto.GameDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<GameDto, String> {
    @Override
    List<GameDto> findAll();
}
