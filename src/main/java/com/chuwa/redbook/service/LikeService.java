package com.chuwa.redbook.service;

import com.chuwa.redbook.payload.Like.LikeDto;
import com.chuwa.redbook.payload.Like.LikeResponseDto;
import com.chuwa.redbook.payload.Like.LikeResponseDto_ByUser;
import com.chuwa.redbook.payload.Like.UnLikeDto;

import java.security.Principal;
import java.util.List;

public interface LikeService {

    LikeDto createLike(LikeDto likeDto, Principal principal);

    LikeResponseDto getLikesByPostId(long postId);

    void unlikePost(UnLikeDto unLikeDto, Principal principal);


    LikeResponseDto_ByUser getLikesByUserId(long userId);
}
