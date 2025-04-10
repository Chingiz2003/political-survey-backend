package com.politicalsurvey.backend.security;

import com.politicalsurvey.backend.repository.AdminRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class AdminDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    public AdminDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Попытка входа с username: " + username);
        var admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("Админ с username " + username + " не найден");
                    return new UsernameNotFoundException("Admin not found");
                });

        System.out.println("Админ найден: " + admin.getUsername());
        return new AdminDetails(admin);
    }
}
