package com.s3rha.spring.controller;

import com.s3rha.spring.DAO.AccountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DashboardController {

//    @Autowired
//    AccountRepo accountRepo ;
//    @PostMapping("/ccc")
//    public ResponseEntity<?> cccx(){
//        return ResponseEntity.ok(accountRepo.findByUserName("farhan"));
//
//    }

//    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN','ROLE_USER')")
    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/welcome-message")
    public ResponseEntity<String> getFirstWelcomeMessage(Authentication authentication){
        System.out.println(authentication.getAuthorities());
        return ResponseEntity.ok("Welcome to the JWT Tutorial:"+authentication.getName()+"with scope:"+authentication.getAuthorities());
    }

   //@PreAuthorize("hasRole('ROLE_MANAGER')")
    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/manager-message")
    public ResponseEntity<String> getManagerData(Principal principal){
        return ResponseEntity.ok("Manager::"+principal.getName());

    }


    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PostMapping("/admin-message")
    public ResponseEntity<String> getAdminData(@RequestParam("message") String message, Principal principal){
        return ResponseEntity.ok("Admin::"+principal.getName()+" has this message:"+message);

    }

}