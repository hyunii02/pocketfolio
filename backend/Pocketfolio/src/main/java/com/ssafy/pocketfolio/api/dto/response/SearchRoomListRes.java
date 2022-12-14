package com.ssafy.pocketfolio.api.dto.response;

import com.ssafy.pocketfolio.db.view.SearchRoomListView;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Tag(name = "SearchRoomListRes", description = "방 검색 목록 Response")
public class SearchRoomListRes {
    @Schema(description = "마이룸 번호")
    private Long roomSeq;
    @Schema(description = "썸네일 url")
    private String thumbnail;
    @Schema(description = "방 이름")
    private String name;
    @Schema(description = "유저 번호")
    private Long userSeq;
    @Schema(description = "방 주인 이름")
    private String userName;
    @Schema(description = "방 주인 프로필 사진 url")
    private String userProfilePic;
    @Schema(description = "좋아요 수")
    private Long like;
    @Schema(description = "조회수")
    private Long hit;
    @Schema(description = "좋아요 여부")
    private Boolean isLiked;
    @Schema(description = "카테고리 이름")
    private String categoryName;

    public SearchRoomListRes(SearchRoomListView view) {
        roomSeq = view.getRoomSeq();
        thumbnail = view.getThumbnail();
        name = view.getName();
        userSeq = view.getUserSeq();
        userName = view.getUserName();
        userProfilePic = view.getUserProfilePic();
        like = view.getLike();
        hit = view.getHit();
        isLiked = view.getIsLiked() > 0;
        categoryName = view.getCategoryName();
    }

}
