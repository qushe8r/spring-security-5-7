package me.qushe8r.studyspringsecurity5_7.service.impl;

import lombok.RequiredArgsConstructor;
import me.qushe8r.studyspringsecurity5_7.domain.Account;
import me.qushe8r.studyspringsecurity5_7.repository.UserRepository;
import me.qushe8r.studyspringsecurity5_7.service.UserService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public void createUser(Account account) {
        userRepository.save(account);
    }
}
