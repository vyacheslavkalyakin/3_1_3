package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

@Component
public class InitDataBase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public InitDataBase(UserRepository userRepository,
                        RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void init() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            Role adminRole = new Role("ROLE_ADMIN");
            Role userRole = new Role("ROLE_USER");

            roleRepository.saveAll(List.of(adminRole, userRole));

            User admin = new User("admin", "admin", "admin@site.com");
            admin.setRoles(Set.of(adminRole));
            userRepository.save(admin);

            User user = new User("user", "user", "user@site.com");
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
        }
    }
}