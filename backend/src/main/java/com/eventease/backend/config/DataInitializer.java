package com.eventease.backend.config;

import com.eventease.backend.entity.Category;
import com.eventease.backend.entity.User;
import com.eventease.backend.repository.CategoryRepository;
import com.eventease.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public DataInitializer(UserRepository userRepository,
                           CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        // Seed a default admin user if none exists
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@eventease.local");
            admin.setPassword("$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi"); // "password"
            admin.setRole("ADMIN");
            admin.setCreatedAt(LocalDateTime.now());
            userRepository.save(admin);
        }

        // Seed default categories if none exist
        if (categoryRepository.count() == 0) {
            String[] categories = {"Music", "Sports", "Technology", "Arts", "Science"};
            for (String name : categories) {
                if (!categoryRepository.existsByName(name)) {
                    Category category = new Category();
                    category.setName(name);
                    categoryRepository.save(category);
                }
            }
        }
    }
}
