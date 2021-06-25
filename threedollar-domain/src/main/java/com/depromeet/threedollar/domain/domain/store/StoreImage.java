package com.depromeet.threedollar.domain.domain.store;

import com.depromeet.threedollar.domain.domain.common.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
// TODO 테이블 이름 image -> store_image 마이그레이션 필요
public class StoreImage extends AuditingTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false) // TODO 실제 테이블에는 image_id여서 마이그레이션이 필요합니다.
	private Store store;

	private String url;
}
