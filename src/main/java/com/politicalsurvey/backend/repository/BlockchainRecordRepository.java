package com.politicalsurvey.backend.repository;

import com.politicalsurvey.backend.entity.BlockchainRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BlockchainRecordRepository extends JpaRepository<BlockchainRecord, Long> {
    List<BlockchainRecord> findByPollId(UUID pollId);
}
