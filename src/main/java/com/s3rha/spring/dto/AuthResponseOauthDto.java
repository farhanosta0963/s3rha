package com.s3rha.spring.dto;



import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseOauthDto {




        private String accessToken;

        private int accessTokenExpiry;

        private TokenType tokenType;

        private String userName;

        private String existAlready ;

}
