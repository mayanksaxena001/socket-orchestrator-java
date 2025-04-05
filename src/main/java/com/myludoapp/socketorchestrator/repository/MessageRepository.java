package com.myludoapp.socketorchestrator.repository;

import com.myludoapp.socketorchestrator.dto.MessageDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageDto, String> {

    List<MessageDto> findAllByRoom(String room);
}