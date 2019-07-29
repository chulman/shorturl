package com.chulm.shorturl.domain.repository;


import com.chulm.shorturl.domain.model.ShortUrl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import java.sql.Types;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

@Slf4j
@AllArgsConstructor
@Repository
public class ShortUrlRepository {
    private static final Scheduler scheduler = Schedulers.from(Executors.newFixedThreadPool(20));
    private final JdbcTemplate jdbcTemplate;

    private final String INSERT_URL = "INSERT INTO SHORT_URL(LINK, STATUS) VALUES(?, ?)";
    private final String SELECT_BY_ID = "SELECT * FROM SHORT_URL WHERE ID = ?";
    private final String SELECT_BY_URL = "SELECT * FROM SHORT_URL WHERE LINK = ?";

    @Transactional
    public Observable<Integer> save(ShortUrl shortUrl){
        return Observable.fromCallable(() ->   jdbcTemplate.update(INSERT_URL,
                                                                   new Object[]{shortUrl.getLink(), shortUrl.getStatus()},
                                                                   new int[]{Types.VARCHAR, Types.INTEGER }))
                         .doOnNext(integer -> log.info("shortUrlRepository insert query = {}, result = {} ", INSERT_URL, integer))
                         .doOnError(throwable -> {
                             //transactional annotation
                             log.error(throwable.getMessage());
                         })
                         .subscribeOn(scheduler);
    }

    public Observable<ShortUrl> findById(long id) {
        return queryExecute(SELECT_BY_ID, new Object[]{id});
    };

    public Observable<ShortUrl> findByUrl(String url) {
        return queryExecute(SELECT_BY_URL, new Object[]{url});
    };

    private Observable<ShortUrl> queryExecute(String query, Object[] condition) {
        return Observable.fromCallable(() -> jdbcTemplate.query(query, condition,
                                                (rs, rowNum) -> ShortUrl.builder()
                                                        .id(rs.getLong("ID"))
                                                        .link(rs.getString("LINK"))
                                                        .status(rs.getInt("STATUS"))
                                                        .build()))
                                                .map(this::toSingleResult)
                        .filter(Objects::nonNull)
                        .doOnNext(shortUrl -> log.info("find short url = {}", shortUrl.toString()))
                        .doOnError(throwable -> {
                            log.error(throwable.getMessage());
                        })
                        .subscribeOn(scheduler);
    };

    private ShortUrl toSingleResult(List<ShortUrl> shortUrls){
        if(shortUrls != null && shortUrls.size() !=0){
            if(shortUrls.size() > 1 ){
                log.warn("number of result expected:1, but was:{}", shortUrls.size());
            }
            return shortUrls.get(0);
        }
        return null;
    }
}
