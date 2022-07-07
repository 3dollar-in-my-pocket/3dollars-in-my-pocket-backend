package com.depromeet.threedollar.api.userservice.service.store.dto.request;

import com.depromeet.threedollar.domain.rds.domain.userservice.store.DeleteReasonType;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreDeleteRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeleteStoreRequest {

    @NotNull(message = "{store.delete.reason.notNull}")
    private DeleteReasonType deleteReasonType;

    @Builder(builderMethodName = "testBuilder")
    private DeleteStoreRequest(DeleteReasonType deleteReasonType) {
        this.deleteReasonType = deleteReasonType;
    }

    public StoreDeleteRequest toEntity(Store store, Long userId) {
        return StoreDeleteRequest.of(store, userId, deleteReasonType);
    }

}
