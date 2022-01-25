package com.depromeet.threedollar.redis.boss.domain.store;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BossStoreOpenInfoRepository extends CrudRepository<BossStoreOpenInfo, String> {

    @NotNull
    @Override
    List<BossStoreOpenInfo> findAll();

}
