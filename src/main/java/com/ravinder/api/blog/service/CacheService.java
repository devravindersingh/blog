package com.ravinder.api.blog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    private static final Logger logger = LoggerFactory.getLogger(CacheService.class);

    @CacheEvict(value = {"posts","postList"}, allEntries = true)
    public void clearCache() {
        logger.info("Clearing all caches");
    }
}
