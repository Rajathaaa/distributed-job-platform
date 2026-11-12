package com.example.jobplatform.repository;
import com.example.jobplatform.entity.OutboxEvent; import org.springframework.data.jpa.repository.*; import org.springframework.data.repository.query.Param; import java.util.*;
public interface OutboxRepository extends JpaRepository<OutboxEvent,Long> {
 @Query(value="select * from outbox_events where published_at is null order by id limit 100",nativeQuery=true) List<OutboxEvent> findPending();
 @Modifying @Query("update OutboxEvent e set e.publishedAt=current_timestamp where e.id=:id and e.publishedAt is null") int markPublished(@Param("id") Long id);
}
