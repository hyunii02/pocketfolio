package com.ssafy.pocketfolio.db.repository;

import com.ssafy.pocketfolio.db.entity.Guestbook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuestbookRepository extends JpaRepository<Guestbook, Long> {
    List<Guestbook> findAllByRoom_RoomSeq(long roomSeq);
}
