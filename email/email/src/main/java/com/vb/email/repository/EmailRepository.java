package com.vb.email.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vb.email.domain.EmailModel;

public interface EmailRepository extends JpaRepository<EmailModel, UUID> {
}
