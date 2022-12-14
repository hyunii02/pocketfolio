package com.ssafy.pocketfolio.db.repository;

import com.ssafy.pocketfolio.db.entity.PortfolioUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioUrlRepository extends JpaRepository<PortfolioUrl, Long> {
    // 포트폴리오 첨부파일 목록 조회
    List<PortfolioUrl> findAllByPortfolio_PortSeq(long portSeq);

    // 포트폴리오 첨부파일 삭제
    void deleteAllByPortfolio_PortSeq(long portSeq);
    List<PortfolioUrl> findAllByPortUrlSeqIn(List<Long> urlSeq);
    void deleteAllByPortUrlSeqIn(List<Long> urlSeqs);
}
