package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

@Component
public class InitDataBase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public InitDataBase(UserRepository userRepository, RoleRepository roleRepository,
                        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        Role userRole = roleRepository.findByName("ROLE_USER");

        if (adminRole == null || userRole == null) {
            adminRole = new Role("ROLE_ADMIN");
            userRole = new Role("ROLE_USER");
            roleRepository.saveAll(List.of(adminRole, userRole));
        }

        userRepository.deleteAll();

        User admin = new User(passwordEncoder.encode("admin"), "admin@site.com",
                "Admin", "Adminov", 30);
        admin.setRoles(Set.of(adminRole));

        User user = new User(passwordEncoder.encode("user"), "user@site.com",
                "User", "Userov", 25);
        user.setRoles(Set.of(userRole));

        userRepository.saveAll(List.of(admin, user));
    }
}