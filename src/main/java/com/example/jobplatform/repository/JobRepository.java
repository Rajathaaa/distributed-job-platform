package com.example.jobplatform.repository;
import com.example.jobplatform.entity.*; import org.springframework.data.jpa.repository.*; import org.springframework.data.repository.query.Param; import java.util.*;
public interface JobRepository extends JpaRepository<Job,UUID> {
 List<Job> findTop100ByOwnerIdOrderByCreatedAtDesc(Long ownerId);
 Optional<Job> findByIdAndOwnerId(UUID id,Long ownerId);
 @Modifying @Query(value="update jobs set status='RUNNING', started_at=now(), updated_at=now() where id=:id and status in ('QUEUED','RETRYING')",nativeQuery=true) int claim(@Param("id") UUID id);
}
