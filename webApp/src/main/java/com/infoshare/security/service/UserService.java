package com.infoshare.security.service;

import com.infoshare.domain.Provider;
import com.infoshare.domain.SocialProviderUserData;
import com.infoshare.domain.User;
import com.infoshare.formobjects.RegisterUserForm;
import com.infoshare.security.CustomOAuth2User;
import com.infoshare.security.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UsersRepository userRepository;

    public void processOAuthPostLogin(CustomOAuth2User oAuth2User) {
        userRepository.findByUserName(oAuth2User.getName())
                .ifPresentOrElse(user1 -> addSocialProviderDataToUser(oAuth2User, user1),
                        () -> addNewUser(oAuth2User));
    }

    private void addSocialProviderDataToUser(CustomOAuth2User oAuth2User, User currentUser) {
        if (currentUser.getSocialProviderUserData().stream()
                .noneMatch(socialProviderUserData -> checkExistenceOfSocialData(oAuth2User, socialProviderUserData))) {
            currentUser.getSocialProviderUserData().add(
                    new SocialProviderUserData((String) oAuth2User.getAttributes().get("id"),
                            Provider.valueOf(oAuth2User.getAuthorizedClientRegistrationId().toUpperCase()), currentUser));
            userRepository.save(currentUser);
        }
    }

    private void addNewUser(CustomOAuth2User oAuth2User) {
        User newUser = new User();
        newUser.setUserName(oAuth2User.getName());
        newUser.setUserEmail(oAuth2User.getEmail());
        SocialProviderUserData socialProviderUserData =
                new SocialProviderUserData((String) oAuth2User.getAttributes().get("id"),
                        Provider.valueOf(oAuth2User.getAuthorizedClientRegistrationId().toUpperCase()), newUser);
        newUser.setSocialProviderUserData(Set.of(socialProviderUserData));
        userRepository.save(newUser);
    }

    public void registerNewUser(RegisterUserForm form) {
        // TODO: write method for user register.
    }

    private boolean checkExistenceOfSocialData(CustomOAuth2User oAuth2User, SocialProviderUserData socialProviderUserData) {
        return socialProviderUserData.getProvider().name()
                .equalsIgnoreCase(oAuth2User.getAuthorizedClientRegistrationId())
                && socialProviderUserData.getProviderUserId().equals(oAuth2User.getAttributes().get("id"));
    }
}
