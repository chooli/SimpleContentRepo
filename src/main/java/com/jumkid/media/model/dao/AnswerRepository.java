package com.jumkid.media.model.dao;

import com.jumkid.media.model.Answer;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AnswerRepository implements IAnswerRepository{


    @Override
    public <S extends Answer> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Answer> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Answer> findById(String primaryKey) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public Iterable<Answer> findAll() {
        return null;
    }

    @Override
    public Iterable<Answer> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(Answer entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends Answer> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Answer> S index(S entity) {
        return null;
    }

    @Override
    public Iterable<Answer> search(QueryBuilder query) {
        return null;
    }

    @Override
    public Page<Answer> search(QueryBuilder query, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Answer> search(SearchQuery searchQuery) {
        return null;
    }

    @Override
    public Page<Answer> searchSimilar(Answer entity, String[] fields, Pageable pageable) {
        return null;
    }

    @Override
    public void refresh() {

    }

    @Override
    public Class<Answer> getEntityClass() {
        return null;
    }

    @Override
    public Iterable<Answer> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Answer> findAll(Pageable pageable) {
        return null;
    }
}
