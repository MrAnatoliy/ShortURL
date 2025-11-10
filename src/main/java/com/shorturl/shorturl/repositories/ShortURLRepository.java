    package com.shorturl.shorturl.repositories;

    import java.time.Instant;
import java.util.List;
import java.util.Optional;
    import java.util.UUID;

    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Modifying;
    import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shorturl.shorturl.entities.ShortURLEntity;

    public interface ShortURLRepository extends JpaRepository<ShortURLEntity, Long>{
        Optional<ShortURLEntity> findByShortCode(String shortCode);
        Optional<ShortURLEntity> findByuserUUID(UUID userUUID);
        List<ShortURLEntity> findAllByUserUUID(UUID userUUID);

        @Modifying
        @Query("select s.shortCode from ShortURLEntity s where s.expiresAt <= :now")
        List<String> findExpiredCodes(@Param("now") Instant now);

        @Modifying
        @Query("delete from ShortURLEntity s where s.expiresAt <= :now")
        int deleteExpired(@Param("now") Instant now);
    }
