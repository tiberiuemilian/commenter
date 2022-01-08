package com.example.commenter.repository;

import com.example.commenter.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Author entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {}
