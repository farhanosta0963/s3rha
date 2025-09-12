package com.s3rha.spring.controller;

import com.s3rha.spring.dto.RegisterStoreByUserWithPriceDto;
import com.s3rha.spring.dto.StoreAccountByUserWithAddressAndPriceDto;
import com.s3rha.spring.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class StoreByUserWithPriceController {
    private final AuthService authService;

    @PostMapping("/sign-up-storeByUser-and-add-price")
    public ResponseEntity<?> registerStoreByUser(@Valid @RequestBody StoreAccountByUserWithAddressAndPriceDto storeAccountByUserRegistrationDto,
                                                 BindingResult bindingResult, HttpServletResponse httpServletResponse){

        log.warn("[AuthController:registerUser]Signup Process Started for store account byyyyyyyyy User:{}",storeAccountByUserRegistrationDto.name());
//        if (bindingResult.hasErrors()) {
//            List<String> errorMessage = bindingResult.getAllErrors().stream()
//                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                    .toList();
//            log.error("[AuthController:registerUser]Errors in store byyyyy User:{}",errorMessage);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
//        }
        RegisterStoreByUserWithPriceDto registerStoreByUserWithPriceDto = authService.registerStoreByUserWithPrice(storeAccountByUserRegistrationDto,httpServletResponse) ;


        return ResponseEntity.ok(registerStoreByUserWithPriceDto);
    }
}