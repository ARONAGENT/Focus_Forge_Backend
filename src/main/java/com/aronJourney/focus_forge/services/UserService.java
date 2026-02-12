package com.aronJourney.focus_forge.services;

import com.aronJourney.focus_forge.dto.profilesAndReports.ProfileDto;
import com.aronJourney.focus_forge.dto.profilesAndReports.ProfileRequestDto;
import com.aronJourney.focus_forge.dto.auth.UserDto;
import com.aronJourney.focus_forge.entities.User;

public interface UserService {
    User getUserById(Long id);

//    void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

    ProfileDto getMyProfile();

    ProfileRequestDto createProfile(Long userId,ProfileRequestDto profileRequestDto);

     ProfileDto updateProfile(ProfileRequestDto profileRequestDto);

    void deleteProfile();
    UserDto getMe();
}
