-- 영화관 사이트 샘플 데이터 추가분 (sample_seed.sql 다음에 실행, 단독 실행도 가능)
-- 영향 범위: movie_title 3행, event_board_image 4행 (모두 INSERT, 기존 행 변경 없음). 같은 행이 있으면 건너뛴다.
-- 제목은 이미지에 적힌 글자 기준이다. 제외한 파일: 12.webp(너의 이름은 포스터 중복), 20190518_125745.jpg(어벤져스 이벤트 상세 이미지),
--   p1065570399398847_982_thum.png(한국기원 화면 캡처), movies.png(티켓 아이콘), Java_Logo.png 등 로고·아이콘.

USE movie;
SET @up = 'C:/courage3/JavaRealProject/potfolio_project/java/backend/movie/src/main/resources/static/css/uploadImage/files';

-- 1) 영화 (3행)
INSERT INTO movie_title (movie_title, movie_filename, uuid, reg_date, movie_resist_filePath, sizes, movie_textarea, uploadPaths, filePath)
SELECT '흑성탈출', '201930189_1280.jpg', UUID(), NOW(), CONCAT(@up, '/20241127/201930189_1280.jpg'), 158777, '', @up, '20241127'
WHERE NOT EXISTS (SELECT 1 FROM movie_title WHERE movie_title = '흑성탈출' AND movie_filename = '201930189_1280.jpg');
INSERT INTO movie_title (movie_title, movie_filename, uuid, reg_date, movie_resist_filePath, sizes, movie_textarea, uploadPaths, filePath)
SELECT '어벤져스: 엔드게임', '330px-a.jpg', UUID(), NOW(), CONCAT(@up, '/20241127/330px-a.jpg'), 382008, '', @up, '20241127'
WHERE NOT EXISTS (SELECT 1 FROM movie_title WHERE movie_title = '어벤져스: 엔드게임' AND movie_filename = '330px-a.jpg');
INSERT INTO movie_title (movie_title, movie_filename, uuid, reg_date, movie_resist_filePath, sizes, movie_textarea, uploadPaths, filePath)
SELECT '외톨이 THE ROCK!', 'c99315aa-f64f-421e-a7ba-353be98a32a9.webp', UUID(), NOW(), CONCAT(@up, '/20241127/c99315aa-f64f-421e-a7ba-353be98a32a9.webp'), 197408, '', @up, '20241127'
WHERE NOT EXISTS (SELECT 1 FROM movie_title WHERE movie_title = '외톨이 THE ROCK!' AND movie_filename = 'c99315aa-f64f-421e-a7ba-353be98a32a9.webp');

-- 2) 이벤트 (4행)
INSERT INTO event_board_image (filePath, event_name, uploadPaths, size, movie_filename, resist_textarea, movie_resist_filepath)
SELECT '20241207', '극장총집편 봇치 더 록! 후편 THAT''S THE TICKET', @up, 235634, '17267951991590.jpg', '', CONCAT(@up, '/20241207/17267951991590.jpg')
WHERE NOT EXISTS (SELECT 1 FROM event_board_image WHERE event_name = '극장총집편 봇치 더 록! 후편 THAT''S THE TICKET' AND movie_filename = '17267951991590.jpg');
INSERT INTO event_board_image (filePath, event_name, uploadPaths, size, movie_filename, resist_textarea, movie_resist_filepath)
SELECT '20241207', '어벤져스: 엔드게임 MX관 특별 포스터 증정 이벤트', @up, 314712, '20190518_125714.jpg', '', CONCAT(@up, '/20241207/20190518_125714.jpg')
WHERE NOT EXISTS (SELECT 1 FROM event_board_image WHERE event_name = '어벤져스: 엔드게임 MX관 특별 포스터 증정 이벤트' AND movie_filename = '20190518_125714.jpg');
INSERT INTO event_board_image (filePath, event_name, uploadPaths, size, movie_filename, resist_textarea, movie_resist_filepath)
SELECT '20241207', '스즈메의 문단속 카카오쇼핑 라이브', @up, 77906, 'clipboard1-horz.jpg', '', CONCAT(@up, '/20241207/clipboard1-horz.jpg')
WHERE NOT EXISTS (SELECT 1 FROM event_board_image WHERE event_name = '스즈메의 문단속 카카오쇼핑 라이브' AND movie_filename = 'clipboard1-horz.jpg');
INSERT INTO event_board_image (filePath, event_name, uploadPaths, size, movie_filename, resist_textarea, movie_resist_filepath)
SELECT '20241207', '너의 이름은. N차 관람 인증 경품 이벤트', @up, 507459, '15697566_1308908405851031_5576090048052701679_n.png', '', CONCAT(@up, '/20241207/15697566_1308908405851031_5576090048052701679_n.png')
WHERE NOT EXISTS (SELECT 1 FROM event_board_image WHERE event_name = '너의 이름은. N차 관람 인증 경품 이벤트' AND movie_filename = '15697566_1308908405851031_5576090048052701679_n.png');

-- 확인용(조회만): 이 파일까지 실행하면 movie_title 6, event_board_image 8
SELECT 'movie_title' AS t, COUNT(*) AS n FROM movie_title
UNION ALL SELECT 'event_board_image', COUNT(*) FROM event_board_image;
