package com.infoshare.repository;

import com.infoshare.domain.NeedRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NeedRequestRepository extends JpaRepository<NeedRequest, UUID> {
}
