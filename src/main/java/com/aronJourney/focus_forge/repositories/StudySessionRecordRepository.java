package com.aronJourney.focus_forge.repositories;

import com.aronJourney.focus_forge.entities.StudySessionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudySessionRecordRepository extends JpaRepository<StudySessionRecord,Long> {
    List<StudySessionRecord> findByTopicNameContaining(String topicName);

}
