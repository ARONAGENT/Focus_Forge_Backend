package com.aronJourney.focus_forge.controllers;

import com.aronJourney.focus_forge.dto.profilesAndReports.ProfileDto;
import com.aronJourney.focus_forge.dto.profilesAndReports.ProfileRequestDto;
import com.aronJourney.focus_forge.dto.auth.UserDto;
import com.aronJourney.focus_forge.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "User Profile APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create profile for a user by userId")
    @PostMapping("/create/profile/{userId}")
    public ResponseEntity<ProfileRequestDto> createProfile(
            @Parameter(description = "User ID for whom profile is created")
            @PathVariable Long userId, @RequestBody ProfileRequestDto profileRequestDto) {
        ProfileRequestDto profileRequestDto1 = userService.createProfile(userId, profileRequestDto);
        return ResponseEntity.ok(profileRequestDto1);
    }


    @GetMapping("/profile/get")
    @Operation(summary = "Get logged-in user's profile")
    public ResponseEntity<ProfileDto> getMyProfile() {
        return ResponseEntity.ok(userService.getMyProfile());
    }

    @PutMapping("/profile/update")
    @Operation(summary = "Update logged-in user's profile")
    public ResponseEntity<ProfileDto> updateProfile(@RequestBody ProfileRequestDto profileRequestDto) {
        return ResponseEntity.ok(userService.updateProfile(profileRequestDto));
    }

    @DeleteMapping("/profile/remove")
    @Operation(summary = "Delete logged-in user's profile")
    public ResponseEntity<Void> deleteProfile(){
        userService.deleteProfile();
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMe(){
        return ResponseEntity.ok(userService.getMe());
    }

}
