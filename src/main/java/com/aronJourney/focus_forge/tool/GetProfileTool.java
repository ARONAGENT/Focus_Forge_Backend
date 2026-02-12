package com.aronJourney.focus_forge.tool;


import com.aronJourney.focus_forge.dto.profilesAndReports.ProfileDto;
import com.aronJourney.focus_forge.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProfileTool {

    private final UserService userService;


    @Tool(description = "Get the Detail Profile Info of a User")
    public ProfileDto getProfile(){
        return userService.getMyProfile();
    }
}
