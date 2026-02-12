package com.aronJourney.focus_forge.services.impl;

import com.aronJourney.focus_forge.dto.profilesAndReports.ProfileDto;
import com.aronJourney.focus_forge.dto.profilesAndReports.ProfileRequestDto;
import com.aronJourney.focus_forge.dto.studyRoom.StudySessionDto;
import com.aronJourney.focus_forge.dto.auth.UserDto;
import com.aronJourney.focus_forge.entities.Profile;
import com.aronJourney.focus_forge.entities.User;
import com.aronJourney.focus_forge.repositories.ProfileRepository;
import com.aronJourney.focus_forge.repositories.StudySessionRepository;
import com.aronJourney.focus_forge.repositories.UserRepository;
import com.aronJourney.focus_forge.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.aronJourney.focus_forge.utils.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ProfileRepository profileRepository;
    private final StudySessionRepository studySessionRepository;

    @Override
    public User getUserById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );
    }

    @Override
    public ProfileDto getMyProfile() {

        // 1. Get logged-in user from Security
        User user = getCurrentUser();

        // 2. Get profile
        Profile profile = user.getProfile();
        if (profile == null) {
            throw new RuntimeException("Profile not created yet!");
        }

        // 3. Fetch sessions of this user
        List<StudySessionDto> sessionDtos =
                studySessionRepository.findAllByUserId(user.getId())
                        .stream()
                        .map(s -> StudySessionDto.builder()
                                .date(s.getDate())
                                .subject(s.getSubject())
                                .platform(s.getPlatform())
                                .duration(s.getDuration())
                                .notes(s.getNotes())
                                .build())
                        .toList();



        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
        // 4. Build DTO
        return ProfileDto.builder()
                .user(userDto)
                .age(profile.getAge())
                .goal(profile.getGoal())
                .skills(profile.getSkills())
                .targetJob(profile.getTargetJob())
                .sessions(sessionDtos)
                .build();
    }


    @Override
    public ProfileRequestDto createProfile(Long userId,ProfileRequestDto profileRequestDto) {
        User user = getCurrentUser();
        // 1. Fetch user
        // 2. Check if profile already exists
        if (profileRepository.existsByUser(user)) {
            throw new RuntimeException("Profile already created for this user!");
        }

        // 4. Save
        Profile savedProfile = Profile.builder()
                .age(profileRequestDto.getAge())
                .goal(profileRequestDto.getGoal())
                .user(user)
                .skills(profileRequestDto.getSkills())
                .targetJob(profileRequestDto.getTargetJob())
                .build();

        user.setProfile(savedProfile);

        profileRepository.save(savedProfile);
        // 5. Return DTO
        return modelMapper.map(savedProfile, ProfileRequestDto.class);

    }

    @Override
    public ProfileDto updateProfile(ProfileRequestDto profileRequestDto) {
        User user = getCurrentUser();

        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found. Please create profile first."));

        // update only allowed fields
        profile.setAge(profileRequestDto.getAge());
        profile.setGoal(profileRequestDto.getGoal());
        profile.setSkills(profileRequestDto.getSkills());
        profile.setTargetJob(profileRequestDto.getTargetJob());
       profileRepository.save(profile);
       UserDto userDto= modelMapper.map(user,UserDto.class);

        return ProfileDto.builder()
                .age(profile.getAge())
                .goal(profile.getGoal())
                .skills(profile.getSkills())
                .targetJob(profile.getTargetJob())
                .user(userDto)
                .build();
    }

    @Override
    public void deleteProfile() {
        User user = getCurrentUser();

        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found with user "+user.getUsername()));

        profileRepository.delete(profile);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username) .orElseThrow(() ->
                new UsernameNotFoundException("User not found with email: " + username));
    }


    @Override
    public UserDto getMe() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }

        String username = authentication.getName(); // This is the username

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        return modelMapper.map(user, UserDto.class);
    }
}
