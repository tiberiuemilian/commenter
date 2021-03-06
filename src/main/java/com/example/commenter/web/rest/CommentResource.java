package com.example.commenter.web.rest;

import com.example.commenter.domain.Comment;
import com.example.commenter.repository.CommentRepository;
import com.example.commenter.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.example.commenter.web.util.HeaderUtil;
import com.example.commenter.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.example.commenter.domain.Comment}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CommentResource {

    private final Logger log = LoggerFactory.getLogger(CommentResource.class);

    private static final String ENTITY_NAME = "comment";

    @Value("${spring.application.name}")
    private String applicationName;

    private final CommentRepository commentRepository;

    public CommentResource(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * {@code POST  /comments} : Create a new comment.
     *
     * @param comment the comment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new comment, or with status {@code 400 (Bad Request)} if the comment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comments")
    public ResponseEntity<Comment> createComment(@Valid @RequestBody Comment comment) throws URISyntaxException {
        log.debug("REST request to save Comment : {}", comment);
        if (comment.getId() != null) {
            throw new BadRequestAlertException("A new comment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Comment result = commentRepository.save(comment);
        return ResponseEntity
            .created(new URI("/api/comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /comments/:id} : Updates an existing comment.
     *
     * @param id the id of the comment to save.
     * @param comment the comment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comment,
     * or with status {@code 400 (Bad Request)} if the comment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the comment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comments/{id}")
    public ResponseEntity<Comment> updateComment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Comment comment
    ) throws URISyntaxException {
        log.debug("REST request to update Comment : {}, {}", id, comment);
        if (comment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, comment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Comment result = commentRepository.save(comment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, comment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /comments/:id} : Partial updates given fields of an existing comment, field will ignore if it is null
     *
     * @param id the id of the comment to save.
     * @param comment the comment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comment,
     * or with status {@code 400 (Bad Request)} if the comment is not valid,
     * or with status {@code 404 (Not Found)} if the comment is not found,
     * or with status {@code 500 (Internal Server Error)} if the comment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/comments/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Comment> partialUpdateComment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Comment comment
    ) throws URISyntaxException {
        log.debug("REST request to partial update Comment partially : {}, {}", id, comment);
        if (comment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, comment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Comment> result = commentRepository
            .findById(comment.getId())
            .map(
                existingComment -> {
                    if (comment.getUrl() != null) {
                        existingComment.setUrl(comment.getUrl());
                    }
                    if (comment.getContent() != null) {
                        existingComment.setContent(comment.getContent());
                    }

                    return existingComment;
                }
            )
            .map(commentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, comment.getId().toString())
        );
    }

    /**
     * {@code GET  /comments} : get all the comments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of comments in body.
     */
    @GetMapping("/comments")
    public List<Comment> getAllComments() {
        log.debug("REST request to get all Comments");
        return commentRepository.findAll();
    }

    /**
     * {@code GET  /comments/:id} : get the "id" comment.
     *
     * @param id the id of the comment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the comment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/comments/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable Long id) {
        log.debug("REST request to get Comment : {}", id);
        Optional<Comment> comment = commentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(comment);
    }

    /**
     * {@code DELETE  /comments/:id} : delete the "id" comment.
     *
     * @param id the id of the comment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        log.debug("REST request to delete Comment : {}", id);
        commentRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /comments/directlyAssociated} : count directly associated comments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and directly associated comments nr in body.
     */
    @GetMapping("/comments/directlyAssociated")
    public Integer countDirectlyAssociatedComments(@RequestParam String author) {
        log.debug("REST request to count directly associated comments");
        return commentRepository.countDirectlyAssociatedComments(author);
    }

    /**
     * {@code GET  /comments/filterByAuthor} : get filtered comments by author and optional by searchFullText also.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and filtered comments in body.
     */
    @GetMapping("/comments/filterByAuthor")
    public Collection<Comment> filterCommentsByAuthor(
            @RequestParam String author,
            @RequestParam(required = false) String searchFullText
    ) {
        log.debug("REST request to get filtered comments");
        if (searchFullText == null)
            return commentRepository.findAllByAuthorName(author);
        else
            return commentRepository.findAllByAuthorAnFullText(author, searchFullText);
    }

    /**
     * {@code GET  /comments/filterByTag} : get filtered comments by tag and optional by searchFullText also.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and filtered comments in body.
     */
    @GetMapping("/comments/filterByTag")
    public Collection<Comment> filterCommentsByTag(
            @RequestParam String tag,
            @RequestParam(required = false) String searchFullText
    ) {
        log.debug("REST request to get filtered comments");
        if (searchFullText == null)
            return commentRepository.findAllByTagsName(tag);
        else
            return commentRepository.findAllByTagAnFullText(tag, searchFullText);
    }

    /**
     * {@code GET  /comments/:id/children : get all the comment's children.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of comments in body.
     */
    @GetMapping("/comments/{id}/children")
    public List<Comment> getAllChildren(@PathVariable Long id) {
        log.debug("REST request to get all Comment with specific parent id");
            return commentRepository.findAllByParentId(id);
    }


}
