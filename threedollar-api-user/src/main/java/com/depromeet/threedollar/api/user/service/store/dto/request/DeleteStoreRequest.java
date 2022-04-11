package com.depromeet.threedollar.api.user.service.store.dto.request;

import javax.validation.constraints.NotNull;

import com.depromeet.threedollar.domain.rds.user.domain.store.DeleteReasonType;
import com.depromeet.threedollar.domain.rds.user.domain.store.Store;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreDeleteRequest;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeleteStoreRequest {

    @NotNull(message = "{store.delete.reason.notNull}")
    private DeleteReasonType deleteReasonType;

    public static DeleteStoreRequest testInstance(DeleteReasonType reasonType) {
        return new DeleteStoreRequest(reasonType);
    }

    public StoreDeleteRequest toEntity(Store store, Long userId) {
        return StoreDeleteRequest.of(store, userId, deleteReasonType);
    }

}
