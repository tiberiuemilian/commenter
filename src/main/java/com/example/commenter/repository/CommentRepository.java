package com.example.commenter.repository;

import com.example.commenter.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Spring Data SQL repository for the Comment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(
            nativeQuery = true,
            value =
                    "with recursive cte (id, author_name, content, parent_id) as (\n" +
                    "    select com.id,\n" +
                    "           aut.name,\n" +
                    "           com.content,\n" +
                    "           com.parent_id\n" +
                    "    from comment com\n" +
                    "             inner join author aut\n" +
                    "                        on com.author_id = aut.id\n" +
                    "    where aut.name = :author\n" +
                    "    union all\n" +
                    "    select c.id,\n" +
                    "           author.name,\n" +
                    "           c.content,\n" +
                    "           c.parent_id\n" +
                    "    from comment c\n" +
                    "             inner join cte\n" +
                    "                        on c.parent_id = cte.id\n" +
                    "             inner join author\n" +
                    "                        on c.author_id = author.id\n" +
                    ")\n" +
                    "select count(*)\n" +
                    "from cte\n" +
                    "where author_name <> :author"
    )
    Integer countDirectlyAssociatedComments(@Param("author") String author);

    Collection<Comment> findAllByAuthorName(String author);

    @Query(
            nativeQuery = true,
            value =
                    "SELECT *\n" +
                    "FROM comment\n" +
                    "         INNER JOIN author on comment.author_id = author.id\n" +
                    "where author.name = :author\n" +
                    "  and MATCH(content) AGAINST(:searchFullText IN NATURAL LANGUAGE MODE)"
    )
    List<Comment> findAllByAuthorAnFullText(@Param("author") String author, @Param("searchFullText") String searchFullText);

    Collection<Comment> findAllByTagsName(String tag);

    @Query(
            nativeQuery = true,
            value =
                    "select *\n" +
                            "from comment\n" +
                            "         inner join tag on comment.id = tag.comment_id\n" +
                            "where tag.name = :tag\n" +
                            "  and MATCH(comment.content) AGAINST(:searchFullText IN NATURAL LANGUAGE MODE)"
    )
    List<Comment> findAllByTagAnFullText(@Param("tag") String tag, @Param("searchFullText") String searchFullText);
}
