package com.depromeet.threedollar.api.service.review;

import com.depromeet.threedollar.api.service.review.dto.request.RetrieveMyReviewsRequest;
import com.depromeet.threedollar.api.service.review.dto.response.ReviewScrollResponse;
import com.depromeet.threedollar.common.collection.ScrollPaginationCollection;
import com.depromeet.threedollar.domain.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.domain.review.projection.ReviewWithWriterProjection;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReviewRetrieveService {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    @Transactional(readOnly = true)
    public ReviewScrollResponse retrieveMyReviews(RetrieveMyReviewsRequest request, Long userId) {
        ScrollPaginationCollection<ReviewWithWriterProjection> scrollCollection = ScrollPaginationCollection.of(
            reviewRepository.findAllByUserIdWithScroll(userId, request.getCursor(), request.getSize() + 1), request.getSize());
        return ReviewScrollResponse.of(scrollCollection, findStoresByReviews(scrollCollection.getItemsInCurrentScroll()),
            Objects.requireNonNullElseGet(request.getCachingTotalElements(), () -> reviewRepository.findCountsByUserId(userId)));
    }

    private Map<Long, Store> findStoresByReviews(List<ReviewWithWriterProjection> reviews) {
        List<Long> storeIds = reviews.stream()
            .map(ReviewWithWriterProjection::getStoreId)
            .collect(Collectors.toList());
        return storeRepository.findAllByIds(storeIds).stream()
            .collect(Collectors.toMap(Store::getId, store -> store));
    }

}
