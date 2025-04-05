package com.myludoapp.socketorchestrator.repository;

import com.myludoapp.socketorchestrator.dto.LudoTokenDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LudoTokenRepository extends JpaRepository<LudoTokenDto, String> {
    List<LudoTokenDto> findAllByHouseId(String id);
}
