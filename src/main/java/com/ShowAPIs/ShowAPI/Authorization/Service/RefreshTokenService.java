package com.ShowAPIs.ShowAPI.Authorization.Service;

import com.ShowAPIs.ShowAPI.Authorization.Repositories.RefreshtokenRepository;
import com.ShowAPIs.ShowAPI.Authorization.Repositories.UserRepository;
import com.ShowAPIs.ShowAPI.Authorization.entities.RefreshToken;
import com.ShowAPIs.ShowAPI.Authorization.entities.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final UserRepository userRepository;

    private final RefreshtokenRepository refreshtokenRepository;

    //Inject
    public RefreshTokenService(UserRepository userRepository, RefreshtokenRepository refreshtokenRepository) {
        this.userRepository = userRepository;
        this.refreshtokenRepository = refreshtokenRepository;
    }

    public RefreshToken createRefreshToken(String username){
        User user=userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found with email:"+username));
        RefreshToken refreshToken= user.getRefreshToken();
        //if user not hv a refresh token we will generate it
        if(refreshToken==null){
            long refreshTokenValidity=5*60*60*10000;
            refreshToken=RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(refreshTokenValidity))
                    .user(user)
                    .build();

            refreshtokenRepository.save(refreshToken);
        }
        //otherwise return existing token
        return  refreshToken;
    }
    public RefreshToken varifyRefreshToken(String refreshToken){
        RefreshToken refToken=refreshtokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()->new RuntimeException("Token not found"));

        //if refresh token is expire so it will no longer in use
        if(refToken.getExpirationTime().compareTo(Instant.now())<0){
            refreshtokenRepository.delete(refToken);
            throw  new RuntimeException("Refresh Token Expired");
        }
        //if all ok return ref token
        return refToken;
    }

}
