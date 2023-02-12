package me.qushe8r.studyspringsecurity5_7.repository;

import me.qushe8r.studyspringsecurity5_7.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Account, Long> {
}
