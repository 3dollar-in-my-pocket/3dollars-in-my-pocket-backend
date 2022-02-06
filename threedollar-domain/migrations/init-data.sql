INSERT INTO faq (category, question, answer, created_at, updated_at) VALUES ('STORE', '제 주변에는 가게가 많이 없어요.', '저희 가슴속 3천원은 오직 여러분의 소중한 제보로 완성되는 사용자 참여형 서비스입니다.함께 따뜻한 겨울을 완성해요! :)', NOW(), NOW());
INSERT INTO faq (category, question, answer, created_at, updated_at) VALUES ('STORE', '중복되는 가게가 등록되어 있어요!', '직접 사용자분들이 등록하다보니 중복되는 가게가 있을 수 있어요!''삭제요청''을 누르시고 ''중복되는 가게에요''라고 신청 부탁드립니다.동일 신고 내용이 3번 이상 등록되면 가게가 삭제될 것입니다:)', NOW(), NOW());
INSERT INTO faq (category, question, answer, created_at, updated_at) VALUES ('STORE', '막상 위치로 가보니 가게가 없어요!', '상세페이지 우측 상단의 ''삭제 요청''을 누르시고 ‘없어진 가게에요'' 사항으로 접수해주시거나, 저희에게 직접 1:1문의를 부탁드립니다.', NOW(), NOW());
INSERT INTO faq (category, question, answer, created_at, updated_at) VALUES ('REVIEW_MENU', '리뷰 수정이나 삭제를 하고 싶어요.', '리뷰 우측 상단의 메뉴 아이콘을 눌러 ''리뷰 수정'' 또는 ''리뷰 삭제''를 선택해주세요.자신이 작성한 리뷰가 아닐 경우 수정이나 삭제가 불가능합니다.', NOW(), NOW());
INSERT INTO faq (category, question, answer, created_at, updated_at) VALUES ('WITHDRAWAL', '회원 탈퇴를 하고싶어요.', '회원 탈퇴는 하단 ‘회원탈퇴하기’로 가능합니다.그동안 저희 서비스를 사용해주셔서 감사합니다!', NOW(), NOW());
INSERT INTO faq (category, question, answer, created_at, updated_at) VALUES ('BOARD', '게시글을 수정하고 싶어요', '가게 정보 수정은 상세 페이지의 ''정보 수정''을 통해 가능합니다.', '2021-07-30 02:10:56.000000', '2021-07-30 02:10:55.000000');
INSERT INTO faq (category, question, answer, created_at, updated_at) VALUES ('CATEGORY', '다른 카테고리도 등록하고싶어요.', '새로운 카테고리 추가를 원하신다면 앱스토어 리뷰나 메일로 의견을 전달해주세요!더욱 풍성한 길거리 음식 지도를 보여드릴 수 있도록 노력하겠습니다! :)', NOW(), NOW());
INSERT INTO faq (category, question, answer, created_at, updated_at) VALUES ('ETC', '가슴속 삼천원 서비스팀과 협업하고 싶어요.', '각종 광고 및 인터뷰, 협업 문의는 저희 가슴속 3천원 대표 메일을 통해 연락 주시면 빠르게 회신 가능합니다. 많은 연락과 관심 부탁드려요! 3dollarinmypocket@gmail.com', NOW(), NOW());
INSERT INTO faq (category, question, answer, created_at, updated_at) VALUES ('BOARD', '게시글을 삭제하고 싶어요.', '가게 정보 삭제는 상세페이지 우측 상단의 ''삭제요청''을 통해 가능합니다. 가게 삭제는 3명 이상의 사용자에게 요청이 들어오면 완료됩니다.잘못된 가게 정보를 빠르게 삭제하고 싶다면 ''문의하기''를 통해 메일을 보내주세요!', NOW(), NOW());


INSERT INTO medal (id, name, activation_icon_url, created_at, updated_at, disable_icon_url, introduction) VALUES (1, '따끈따끈한 뉴비', 'https://storage.prod.threedollars.co.kr/medal/v1-newbie-default.png', NOW(), NOW(), 'https://storage.prod.threedollars.co.kr/medal/v2-newbie-disabled.png', '우리 동네 붕어빵과 알아가는 사이');
INSERT INTO medal (id, name, activation_icon_url, created_at, updated_at, disable_icon_url, introduction) VALUES (2, '붕어빵 전문가', 'https://storage.prod.threedollars.co.kr/medal/v1-challenger-default.png', NOW(), NOW(), 'https://storage.prod.threedollars.co.kr/medal/v2-challenger-disabled.png', '우리 동네 붕어빵에 대해서는 내가 바로 척척석사!');
INSERT INTO medal (id, name, activation_icon_url, created_at, updated_at, disable_icon_url, introduction) VALUES (3, '붕친맨', 'https://storage.prod.threedollars.co.kr/medal/v1-expert-default.png', NOW(), NOW(), 'https://storage.prod.threedollars.co.kr/medal/v2-expert-disabled.png', '앗, 이 정도면 붕어빵 척척박사는 넘어섰네요');
INSERT INTO medal (id, name, activation_icon_url, created_at, updated_at, disable_icon_url, introduction) VALUES (4, '각설이 마냥...', 'https://storage.prod.threedollars.co.kr/medal/v1-otl-default.png', NOW(), NOW(), 'https://storage.prod.threedollars.co.kr/medal/v2-otl-disabled.png', '다섯번이나 허탕쳤지만, 굴하지 않는 오뚜기 정신을 가진 당신');
INSERT INTO medal (id, name, activation_icon_url, created_at, updated_at, disable_icon_url, introduction) VALUES (5, '미슐랭 평가단', 'https://storage.prod.threedollars.co.kr/medal/v1-michelin-default.png', NOW(), NOW(), 'https://storage.prod.threedollars.co.kr/medal/v2-michelin-disabled.png', '길거리 음식쪽에서는 당신이 바로 미슐랭 평가단!');
INSERT INTO medal (id, name, activation_icon_url, created_at, updated_at, disable_icon_url, introduction) VALUES (6, '서비스 주세요', 'https://storage.prod.threedollars.co.kr/medal/v1-service-default.png', NOW(), NOW(), 'https://storage.prod.threedollars.co.kr/medal/v2-service-disabled.png', '사장님, 이 분 서비스 주세요!!!');
INSERT INTO medal (id, name, activation_icon_url, created_at, updated_at, disable_icon_url, introduction) VALUES (7, '우리동네 보안관', 'https://storage.prod.threedollars.co.kr/medal/v1-police-default.png', NOW(), NOW(), 'https://storage.prod.threedollars.co.kr/medal/v2-police-disabled.png', '허위 매물 신고에 적극적인 당신, 동네의 보안관이시네요');


INSERT INTO medal_acquisition_condition (medal_id, condition_type, count, created_at, updated_at, description) VALUES (1, 'NO_CONDITION', 0, NOW(), NOW(), '2021-12-02 02:08:45.000000', null);
INSERT INTO medal_acquisition_condition (medal_id, condition_type, count, created_at, updated_at, description) VALUES (2, 'VISIT_BUNGEOPPANG_STORE', 3, NOW(), NOW(), '붕어빵 가게 인증 3회 누적');
INSERT INTO medal_acquisition_condition (medal_id, condition_type, count, created_at, updated_at, description) VALUES (3, 'VISIT_BUNGEOPPANG_STORE', 10, NOW(), NOW(), '붕어빵 가게 인증 10회 누적');
INSERT INTO medal_acquisition_condition (medal_id, condition_type, count, created_at, updated_at, description) VALUES (4, 'VISIT_NOT_EXISTS_STORE', 5, NOW(), NOW(), '인증 실패 5회 누적 ');
INSERT INTO medal_acquisition_condition (medal_id, condition_type, count, created_at, updated_at, description) VALUES (5, 'ADD_REVIEW', 5, NOW(), NOW(), '별점 or 리뷰 5회 누적');
INSERT INTO medal_acquisition_condition (medal_id, condition_type, count, created_at, updated_at, description) VALUES (6, 'ADD_STORE', 3, NOW(), NOW(), '동네 가게 제보 3회 누적');
INSERT INTO medal_acquisition_condition (medal_id, condition_type, count, created_at, updated_at, description) VALUES (7, 'DELETE_STORE', 3, NOW(), NOW(), '삭제 요청 3회 누적');


INSERT INTO store_promotion (created_at, updated_at, introduction, icon_url, is_display_on_marker, is_display_on_the_detail) VALUES (NOW(), NOW(), '이 가게는 바삭하고 고소한<br>지니스에서 제조된</br> 반죽을 사용합니다', 'https://storage.prod.threedollars.co.kr/promotion/happy-promotion.png', 1, 1);


INSERT INTO advertisement (platform_type, image_url, link_url, start_date_time, end_date_time, created_at, updated_at, position_type, title, sub_title, bg_color, font_color) VALUES ('AOS', 'https://storage.prod.threedollars.co.kr/popup/aos_popup_visit_history_2021_11_25.jpg', 'https://www.instagram.com/p/CWiAfy_PBAe/', '2021-11-25 00:00:00.000000', '2021-12-05 11:57:08.000000', NOW(), NOW(), 'SPLASH', null, null, null, null);
INSERT INTO advertisement (platform_type, image_url, link_url, start_date_time, end_date_time, created_at, updated_at, position_type, title, sub_title, bg_color, font_color) VALUES ('IOS', 'https://storage.prod.threedollars.co.kr/popup/ios_popup_visit_history_2021_11_25.jpg', 'https://www.instagram.com/p/CWiAfy_PBAe/', '2021-11-25 00:00:00.000000', '2021-12-05 11:57:08.000000', NOW(), NOW(), 'SPLASH', null, null, null, null);
