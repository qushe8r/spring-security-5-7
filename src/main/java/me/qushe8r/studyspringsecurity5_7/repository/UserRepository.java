package me.qushe8r.studyspringsecurity5_7.repository;

import me.qushe8r.studyspringsecurity5_7.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);
}
