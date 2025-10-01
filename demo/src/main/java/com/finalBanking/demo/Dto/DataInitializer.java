package com.finalBanking.demo.Dto;

import com.finalBanking.demo.Entity.Permission;
import com.finalBanking.demo.Entity.Role;
import com.finalBanking.demo.Entity.User;
import com.finalBanking.demo.Repository.permissionRepository;
import com.finalBanking.demo.Repository.roleRepository;
import com.finalBanking.demo.Repository.userRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    @Order(1)
    public CommandLineRunner initializeData(
            roleRepository roleRepository,
            permissionRepository permissionRepository,
            userRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            System.out.println("=== Starting DataInitializer ===");

            try {
                // 1. Initialize Permissions
                long permCount = permissionRepository.count();
                System.out.println("Current permission count: " + permCount);

                if (permCount == 0) {
                    List<Permission> permissions = createDefaultPermissions();
                    permissionRepository.saveAll(permissions);
                    permissionRepository.flush();
                    System.out.println("✅ Default permissions initialized: " + permissions.size() + " permissions created");
                } else {
                    System.out.println("⚠️ Permissions already exist, skipping initialization");
                }

                // 2. Initialize Roles
                long roleCount = roleRepository.count();
                System.out.println("Current role count: " + roleCount);

                if (roleCount == 0) {
                    List<Role> roles = createDefaultRoles(permissionRepository);
                    roleRepository.saveAll(roles);
                    roleRepository.flush();
                    System.out.println("✅ Default roles initialized: " + roles.size() + " roles created");
                } else {
                    System.out.println("⚠️ Roles already exist, skipping initialization");
                }

                // 3. Initialize Default Users
                long userCount = userRepository.count();
                System.out.println("Current user count: " + userCount);

                if (userCount == 0) {
                    List<User> users = createDefaultUsers(roleRepository, passwordEncoder);
                    userRepository.saveAll(users);
                    userRepository.flush();
                    System.out.println("✅ Default users initialized: " + users.size() + " users created");
                    System.out.println("📧 Admin: admin@bank.com / Password: admin123");
                    System.out.println("📧 Manager: manager@bank.com / Password: manager123");
                    System.out.println("📧 User: user@bank.com / Password: user123");
                } else {
                    System.out.println("⚠️ Users already exist, skipping initialization");
                }

                System.out.println("=== DataInitializer Completed Successfully ===");
            } catch (Exception e) {
                System.err.println("❌ Error during data initialization: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }

    private List<Permission> createDefaultPermissions() {
        System.out.println("Creating default permissions...");

        Permission createUser = createPermission(
                "CREATE_USER",
                "Permission to create new users",
                "USER_MANAGEMENT"
        );

        Permission createAccount = createPermission(
                "CREATE_ACCOUNT",
                "Permission to create new accounts",
                "ACCOUNT_MANAGEMENT"
        );

        Permission transfer = createPermission(
                "TRANSFER",
                "Permission to perform money transfers",
                "TRANSACTION_MANAGEMENT"
        );

        Permission viewTransactions = createPermission(
                "VIEW_TRANSACTIONS",
                "Permission to view transaction history",
                "TRANSACTION_MANAGEMENT"
        );

        Permission deleteAccount = createPermission(
                "DELETE_ACCOUNT",
                "Permission to delete accounts",
                "ACCOUNT_MANAGEMENT"
        );

        Permission viewAccounts = createPermission(
                "VIEW_ACCOUNTS",
                "Permission to view account details",
                "ACCOUNT_MANAGEMENT"
        );

        Permission updateUser = createPermission(
                "UPDATE_USER",
                "Permission to update user information",
                "USER_MANAGEMENT"
        );

        Permission deleteUser = createPermission(
                "DELETE_USER",
                "Permission to delete users",
                "USER_MANAGEMENT"
        );

        return Arrays.asList(
                createUser, createAccount, transfer, viewTransactions,
                deleteAccount, viewAccounts, updateUser, deleteUser
        );
    }

    private Permission createPermission(String name, String description, String category) {
        Permission permission = new Permission();
        permission.setName(name);
        permission.setDescription(description);
        permission.setCategory(category);
        return permission;
    }

    private List<Role> createDefaultRoles(permissionRepository permissionRepository) {
        System.out.println("Creating default roles...");

        // Fetch all permissions
        Permission createUser = permissionRepository.findByName("CREATE_USER")
                .orElseThrow(() -> new RuntimeException("CREATE_USER permission not found"));
        Permission createAccount = permissionRepository.findByName("CREATE_ACCOUNT")
                .orElseThrow(() -> new RuntimeException("CREATE_ACCOUNT permission not found"));
        Permission transfer = permissionRepository.findByName("TRANSFER")
                .orElseThrow(() -> new RuntimeException("TRANSFER permission not found"));
        Permission viewTransactions = permissionRepository.findByName("VIEW_TRANSACTIONS")
                .orElseThrow(() -> new RuntimeException("VIEW_TRANSACTIONS permission not found"));
        Permission deleteAccount = permissionRepository.findByName("DELETE_ACCOUNT")
                .orElseThrow(() -> new RuntimeException("DELETE_ACCOUNT permission not found"));
        Permission viewAccounts = permissionRepository.findByName("VIEW_ACCOUNTS")
                .orElseThrow(() -> new RuntimeException("VIEW_ACCOUNTS permission not found"));
        Permission updateUser = permissionRepository.findByName("UPDATE_USER")
                .orElseThrow(() -> new RuntimeException("UPDATE_USER permission not found"));
        Permission deleteUser = permissionRepository.findByName("DELETE_USER")
                .orElseThrow(() -> new RuntimeException("DELETE_USER permission not found"));

        // Create ADMIN role with all permissions
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        adminRole.setDescription("Administrator with full system access");
        adminRole.setCreatedAt(LocalDateTime.now());
        adminRole.setPermissions(new HashSet<>(Arrays.asList(
                createUser, createAccount, transfer, viewTransactions,
                deleteAccount, viewAccounts, updateUser, deleteUser
        )));

        // Create MANAGER role with management permissions
        Role managerRole = new Role();
        managerRole.setName("MANAGER");
        managerRole.setDescription("Manager with account and transaction management access");
        managerRole.setCreatedAt(LocalDateTime.now());
        managerRole.setPermissions(new HashSet<>(Arrays.asList(
                createAccount, transfer, viewTransactions, viewAccounts
        )));

        // Create USER role with basic permissions
        Role userRole = new Role();
        userRole.setName("USER");
        userRole.setDescription("Regular user with basic access");
        userRole.setCreatedAt(LocalDateTime.now());
        userRole.setPermissions(new HashSet<>(Arrays.asList(
                transfer, viewTransactions, viewAccounts
        )));

        return Arrays.asList(adminRole, managerRole, userRole);
    }

    private List<User> createDefaultUsers(roleRepository roleRepository, PasswordEncoder passwordEncoder) {
        System.out.println("Creating default users...");

        // Fetch roles
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("ADMIN role not found"));
        Role managerRole = roleRepository.findByName("MANAGER")
                .orElseThrow(() -> new RuntimeException("MANAGER role not found"));
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("USER role not found"));

        // Create Admin User
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@bank.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFirstName("System");
        admin.setLastName("Administrator");
        admin.setPhoneNumber("1234567890");
        admin.setCreateDate(LocalDateTime.now());
        admin.setRoles(new HashSet<>(Arrays.asList(adminRole)));

        // Create Manager User
        User manager = new User();
        manager.setUsername("manager");
        manager.setEmail("manager@bank.com");
        manager.setPassword(passwordEncoder.encode("manager123"));
        manager.setFirstName("Account");
        manager.setLastName("Manager");
        manager.setPhoneNumber("1234567891");
        manager.setCreateDate(LocalDateTime.now());
        manager.setRoles(new HashSet<>(Arrays.asList(managerRole)));

        // Create Regular User
        User regularUser = new User();
        regularUser.setUsername("user");
        regularUser.setEmail("user@bank.com");
        regularUser.setPassword(passwordEncoder.encode("user123"));
        regularUser.setFirstName("Regular");
        regularUser.setLastName("User");
        regularUser.setPhoneNumber("1234567892");
        regularUser.setCreateDate(LocalDateTime.now());
        regularUser.setRoles(new HashSet<>(Arrays.asList(userRole)));

        return Arrays.asList(admin, manager, regularUser);
    }
}
