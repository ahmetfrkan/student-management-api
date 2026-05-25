package com.tpe.student_management.contact_us.repository;

import com.tpe.student_management.contact_us.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findAllByEmail(String email, Pageable pageable);

    Page<Message> findAllBySubject(String subject, Pageable pageable);

    List<Message> findAllByCreatedAtBetween(LocalDateTime beginDate, LocalDateTime endDate);
}
