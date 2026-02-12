package com.aronJourney.focus_forge.services;

import com.aronJourney.focus_forge.dto.studyRoom.CreateStudyRoomRequest;
import com.aronJourney.focus_forge.dto.studyRoom.StudyRoomResponse;
import com.aronJourney.focus_forge.entities.StudyRoom;

import java.util.List;

public interface StudyRoomService {

     StudyRoomResponse createStudyRoom(CreateStudyRoomRequest request);
     void startStudyRoom(Long roomId);
     void endStudyRoom(Long roomId);

     List<StudyRoomResponse> getMyCreatedRooms();
      List<StudyRoomResponse> getMyParticipatingRooms();
      StudyRoomResponse getRoomDetails(Long roomId);

     void joinStudyRoom(Long roomId);
     void leaveStudyRoom(Long roomId);
}
