package me.qushe8r.studyspringsecurity5_7.controller.user;

import lombok.RequiredArgsConstructor;
import me.qushe8r.studyspringsecurity5_7.domain.Account;
import me.qushe8r.studyspringsecurity5_7.domain.AccountDto;
import me.qushe8r.studyspringsecurity5_7.mapper.UserMapper;
import me.qushe8r.studyspringsecurity5_7.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserMapper userMapper;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @GetMapping("/mypage")
    public String myPage() {
        return "/user/mypage";
    }

    @GetMapping("/users")
    public String createUser() {
        return "user/login/register";
    }

    @PostMapping("/users")
    public String createUser(AccountDto accountDto) {
        Account account = userMapper.accountDtoToAccount(accountDto);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        userService.createUser(account);
        return "redirect:/";
    }
}
