package com.kst.movie_ticket_reservation.db_seed;

import com.kst.movie_ticket_reservation.feat.auth.admin.entity.Admin;
import com.kst.movie_ticket_reservation.feat.auth.admin.repository.AdminRepository;
import com.kst.movie_ticket_reservation.util.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner
{
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception
    {
//        String email = "super@gmail.com";
//        String name = "Super Admin";
//        String password = "super000";
//        String hash = this.passwordEncoder.encode(password);
//        Set<Role> roleSet = new HashSet<>();
//        roleSet.add(Role.ROLE_SUPER_ADMIN);
//
//        Admin admin = new Admin();
//        admin.setName(name);
//        admin.setEmail(email);
//        admin.setPassword(hash);
//        admin.setRoles(roleSet);
//
//        this.adminRepository.save(admin);
//
//        System.out.println("saved super admin");
    }
}
