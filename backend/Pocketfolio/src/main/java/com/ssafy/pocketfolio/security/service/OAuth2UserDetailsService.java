package com.ssafy.pocketfolio.security.service;

import com.ssafy.pocketfolio.db.entity.*;
import com.ssafy.pocketfolio.db.repository.*;
import com.ssafy.pocketfolio.security.dto.UserAuthDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class OAuth2UserDetailsService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final OauthRepository oauthRepository;
    private final RoomRepository roomRepository;
    private final CategoryRepository categoryRepository;
    private final RoomCategoryRepository roomCategoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("--------------------------------------");
        log.info("userRequest:" + userRequest);

        String clientName = userRequest.getClientRegistration().getClientName(); // getRegistrationId()

        log.info("clientName: " + clientName);
        log.info(userRequest.getAdditionalParameters());

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName();
        log.info("userNameAttributeName: " + userNameAttributeName);

        OAuth2User oAuth2User =  super.loadUser(userRequest);

        log.info("==============================");
        oAuth2User.getAttributes().forEach((k,v) -> {
            log.info(k +":" + v);
        });

        String email = null;
        String name = null;
        String key = null;

        if (clientName.equals("Google")) {
            email = oAuth2User.getAttribute("email");
            name = oAuth2User.getAttribute("name");
            key = (String) oAuth2User.getAttributes().get(userNameAttributeName);
        } else if (clientName.equals("Kakao")) {
            log.info("this is kakao");
            Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
            email = (String) kakaoAccount.get("email");
            key = Long.toString((Long) oAuth2User.getAttributes().get("id"));
        }

        log.info("EMAIL: " + email);
        log.info("NAME: " + name);
        log.info("key: " + key); // ????????? ????????? ??? 1234

        User user;
        Optional<User> result = userRepository.findByEmail(email);
        boolean isSignUp = false;

        if (!result.isPresent()) {
            // ?????? ??????
            user = saveSocialUser(email, name);
            log.info("DB User ?????? ??????");
            saveSocialOAuth(user, clientName, key);
            log.info("DB OAuth ?????? ??????");
            createFirstRoom(user);
            log.info("DB Room ?????? ??????");
            isSignUp = true;
        } else {
            // ?????? ?????? ?????????
            user = result.get();
            if (!oauthRepository.findByUser_UserSeqAndFrom(user.getUserSeq(), clientName).isPresent()) {
                // ?????? ????????? ?????? ?????? ???????????? ??????
                // key??? ????????? ????????? ???????????? findByKey??? ???????????? ???
                saveSocialOAuth(user, clientName, key);
                log.info("DB OAuth ?????? ??????");
            }
        }

        Set<GrantedAuthority> roleSet = new HashSet<>();
        roleSet.add(new SimpleGrantedAuthority("ROLE_USER"));

        UserAuthDto userAuthDto = new UserAuthDto(
                Long.toString(user.getUserSeq()),
                passwordEncoder.encode("1111"),
                roleSet,
                oAuth2User.getAttributes(),
                clientName.toLowerCase(),
                isSignUp
        );

        userAuthDto.setEmail(user.getEmail());
        userAuthDto.setName(user.getName());

        return userAuthDto;
    }

    private User saveSocialUser(String email, String name) {
        if (name == null || name.isEmpty()) {
            name = email.substring(0, email.indexOf("@"));
        }

        User user = User.builder()
                .email(email)
                .name(name)
                .describe("???????????????. " + name + "?????????.")
                .build();

        userRepository.save(user);
        return user;
    }

    private void saveSocialOAuth(User user, String from, String key) {
        Oauth oauth = Oauth.builder()
                .key(key) // ?????? ????????? key ??? ?????? ??? ???????????? ??????
                .from(from)
                .user(user)
                .build();
        oauthRepository.save(oauth);
    }

    private void createFirstRoom(User user) {
        Room room = Room.builder()
                .name(user.getName() + "?????? ??????")
                .user(user)
                .theme("room_01")
                .isMain("T")
                .privacy("O")
                .build();
        room = roomRepository.save(room);
        roomCategoryRepository.save(RoomCategory.builder().room(room).category(categoryRepository.getReferenceById(1L)).build());
        log.debug("?????? ?????? -> ?????? ?????? -> ?????? ???????????? ?????? ??????");
    }

}
