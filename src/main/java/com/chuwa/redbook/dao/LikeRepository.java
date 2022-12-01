package com.chuwa.redbook.dao;

import com.chuwa.redbook.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    List<Like> findByPost(long postId);

    @Query("select l.user from Like l where l.post = :id")
    List<Object> getUserByPost(@Param("id") long postId);

    long countByPost(long postId);

    @Transactional
    void deleteByPostAndUser(long postId,long userId);

    boolean existsByPostAndUser(long postId, long userId);

    long countByUser(long userId);
    List<Like> findByUser(long userId);
    @Query("select l.post from Like l where l.user = :id")
    List<Object> getPostByUser(@Param("id") long userId);




}
