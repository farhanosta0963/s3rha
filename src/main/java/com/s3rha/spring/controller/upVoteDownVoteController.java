package com.s3rha.spring.controller;


import com.s3rha.spring.DAO.AccountRepo;
import com.s3rha.spring.DAO.UserPriceRepo;
import com.s3rha.spring.entity.Account;
import com.s3rha.spring.entity.UserPrice;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class upVoteDownVoteController {
    private final UserPriceRepo userPriceRepo ;
    private final AccountRepo accountRepo;
    @Transactional

    @PostMapping("/userPrices/{id}/like")
    public ResponseEntity<?> likePrice(@PathVariable Long id ){


        UserPrice userPrice = userPriceRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "UserPrice with id " + id + " not found"));

        String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
        Account account = accountRepo.findByUserName(userName)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User with UserName " + userName + " not found"));

        userPrice.addUpVotedAccount(account);
        return ResponseEntity.noContent().build();
    }
    @Transactional

    @PostMapping("/userPrices/{id}/dislike")
    public ResponseEntity<?> dislikePrice(@PathVariable Long id){
        UserPrice userPrice = userPriceRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "UserPrice with id " + id + " not found"));

        String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
        Account account = accountRepo.findByUserName(userName)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User with UserName " + userName + " not found"));

        userPrice.addDownVotingAccount(account);
        return ResponseEntity.noContent().build();
    }
    @Transactional
    @GetMapping("/userPrices/{id}/like/count")
    public ResponseEntity<Integer> getLikeCount(@PathVariable Long id) {
        UserPrice userPrice = userPriceRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UserPrice with id " + id + " not found"));

        int count = userPrice.getUpVotedAccountList() != null ? userPrice.getUpVotedAccountList().size() : 0;
        return ResponseEntity.ok(count);
    }
    @Transactional

    @GetMapping("/userPrices/{id}/dislike/count")
    public ResponseEntity<Integer> getDislikeCount(@PathVariable Long id) {
        UserPrice userPrice = userPriceRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UserPrice with id " + id + " not found"));

        int count = userPrice.getDownVotingAccountList() != null ? userPrice.getDownVotingAccountList().size() : 0;
        return ResponseEntity.ok(count);
    }

}
