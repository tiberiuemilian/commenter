package com.example.commenter.repository;

import com.example.commenter.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Tag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {}
