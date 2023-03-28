package com.mycompany.onlineexam.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mycompany.onlineexam.domain.Role;
import com.mycompany.onlineexam.domain.User;
import com.mycompany.onlineexam.repository.RoleRepository;
import com.mycompany.onlineexam.repository.UserRepository;
import com.mycompany.onlineexam.service.UserService;
import com.mycompany.onlineexam.web.errors.NotFoundException;
import com.mycompany.onlineexam.web.model.RefreshTokenModel;
import com.mycompany.onlineexam.web.model.TokenModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.mycompany.onlineexam.domain.constants.Constants.PASSWORD;
import static com.mycompany.onlineexam.domain.constants.Constants.USERNAME;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${application.security.login-address}")
    private String loginAddress;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Request to load user by username :{}", username);
        User user = findUserByUsername(username);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new NotFoundException("User with  username : " + username));
    }

    @Override
    public User save(User user) {
        logger.info("request to save user : {}", user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        logger.info("Request to add role :{} to  user with  username :{}", roleName, username);
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new NotFoundException(username));
        Role role = roleRepository.findRoleByName(roleName).get();
        user.getRoles().add(role);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public TokenModel getToken(String username, String password) {
        MultiValueMap<String, String> user = new LinkedMultiValueMap<>();
        user.add(USERNAME, username);
        user.add(PASSWORD, password);
        TokenModel token = WebClient.create().post()
                .uri(loginAddress)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(user))
                .retrieve()
                .bodyToMono(TokenModel.class)
                .block();
        return token;
    }

    @Override
    public TokenModel createNewTokenByRefreshToken(RefreshTokenModel refreshTokenModel) {
        String refreshToken = refreshTokenModel.getRefreshToken();
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refreshToken);

        String username = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Arrays.stream(roles).forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });

        String accessToken = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() * 10 * 60 * 1000))
                .withClaim("roles", Arrays.asList(roles))
                .sign(algorithm);

        String newRefreshToken = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() * 30 * 60 * 1000))
                .withIssuer("/api/refresh-token")
                .sign(algorithm);

//            return ResponseEntity.ok(new AccessTokenResponse(accessToken));
        TokenModel newTokenModel = new TokenModel();
        newTokenModel.setAccessToken(accessToken);
        newTokenModel.setRefreshToken(newRefreshToken);
        return newTokenModel;
    }
}
