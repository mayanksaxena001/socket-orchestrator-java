package com.myludoapp.socketorchestrator.repository;

import com.myludoapp.socketorchestrator.dto.GameInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GameInfoRepository extends JpaRepository<GameInfoDto, String> {
    List<GameInfoDto> findAllByGameId(String id);
}
