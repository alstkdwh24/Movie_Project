-- 영화관 사이트 샘플 데이터 (남아 있는 업로드 이미지 기준)
-- 사용법: 앱이 연결된 DB(movie)에서 실행. 같은 행이 이미 있으면 건너뛰므로 여러 번 실행해도 중복되지 않는다.
-- 영향 범위: movie_title 3행, event_board_image 4행, movie_gifticon 1행, movie_ticket 1행, delicious 13행 (모두 INSERT, 기존 행 변경 없음)
-- 제목은 파일 이름/기존 이벤트 페이지 문구에서 확인되는 것만 넣었다. 확인 안 되는 이미지(12.webp 등)는 뺐다.

USE movie;
SET @up = 'C:/courage3/JavaRealProject/potfolio_project/java/backend/movie/src/main/resources/static/css/uploadImage/files';

-- 1) 영화 (movie_title)
INSERT INTO movie_title (movie_title, movie_filename, uuid, reg_date, movie_resist_filePath, sizes, movie_textarea, uploadPaths, filePath)
SELECT '아이언맨', '아이언맨_포스터.jpg', UUID(), NOW(), CONCAT(@up, '/20241127/아이언맨_포스터.jpg'), 218585, '', @up, '20241127'
WHERE NOT EXISTS (SELECT 1 FROM movie_title WHERE movie_title = '아이언맨' AND movie_filename = '아이언맨_포스터.jpg');
INSERT INTO movie_title (movie_title, movie_filename, uuid, reg_date, movie_resist_filePath, sizes, movie_textarea, uploadPaths, filePath)
SELECT '스즈메의 문단속', 'door.jpeg', UUID(), NOW(), CONCAT(@up, '/20241127/door.jpeg'), 85748, '', @up, '20241127'
WHERE NOT EXISTS (SELECT 1 FROM movie_title WHERE movie_title = '스즈메의 문단속' AND movie_filename = 'door.jpeg');
INSERT INTO movie_title (movie_title, movie_filename, uuid, reg_date, movie_resist_filePath, sizes, movie_textarea, uploadPaths, filePath)
SELECT '너의 이름은.', 'your_name.jpg', UUID(), NOW(), CONCAT(@up, '/20241127/your_name.jpg'), 176872, '', @up, '20241127'
WHERE NOT EXISTS (SELECT 1 FROM movie_title WHERE movie_title = '너의 이름은.' AND movie_filename = 'your_name.jpg');

-- 2) 이벤트 (event_board_image)
INSERT INTO event_board_image (filePath, event_name, uploadPaths, size, movie_filename, resist_textarea, movie_resist_filepath)
SELECT '20241207', '슈퍼배드4 프리미어 상영회', @up, 112533, '17205914853020.jpg', '', CONCAT(@up, '/20241207/17205914853020.jpg')
WHERE NOT EXISTS (SELECT 1 FROM event_board_image WHERE event_name = '슈퍼배드4 프리미어 상영회' AND movie_filename = '17205914853020.jpg');
INSERT INTO event_board_image (filePath, event_name, uploadPaths, size, movie_filename, resist_textarea, movie_resist_filepath)
SELECT '20241207', '봇치 더 록!전편 보기', @up, 113991, '17258415781340.jpg', '', CONCAT(@up, '/20241207/17258415781340.jpg')
WHERE NOT EXISTS (SELECT 1 FROM event_board_image WHERE event_name = '봇치 더 록!전편 보기' AND movie_filename = '17258415781340.jpg');
INSERT INTO event_board_image (filePath, event_name, uploadPaths, size, movie_filename, resist_textarea, movie_resist_filepath)
SELECT '20241207', '흑성탈출 이벤트', @up, 815579, '390ef387c3db0826abb7bc123cade97d.jpg', '', CONCAT(@up, '/20241207/390ef387c3db0826abb7bc123cade97d.jpg')
WHERE NOT EXISTS (SELECT 1 FROM event_board_image WHERE event_name = '흑성탈출 이벤트' AND movie_filename = '390ef387c3db0826abb7bc123cade97d.jpg');
INSERT INTO event_board_image (filePath, event_name, uploadPaths, size, movie_filename, resist_textarea, movie_resist_filepath)
SELECT '20241207', '귀멸의 칼날 도공마을 편 현장 증정 이벤트', @up, 85886, '17083183544140.jpg', '', CONCAT(@up, '/20241207/17083183544140.jpg')
WHERE NOT EXISTS (SELECT 1 FROM event_board_image WHERE event_name = '귀멸의 칼날 도공마을 편 현장 증정 이벤트' AND movie_filename = '17083183544140.jpg');

-- 3) 기프트카드 (movie_gifticon)
INSERT INTO movie_gifticon (Gifticon_name, Gifticon_filename, uuid, reg_date, movie_filepath, resist_textarea, size, uploadPaths)
SELECT '기프트카드', 'coupon.png', UUID(), NOW(), '20241208', '', 31211, @up
WHERE NOT EXISTS (SELECT 1 FROM movie_gifticon WHERE Gifticon_name = '기프트카드' AND Gifticon_filename = 'coupon.png');

-- 4) 영화관람권 (movie_ticket)
INSERT INTO movie_ticket (movie_ticket_name, movie_ticket_filename, uuid, reg_date, movie_filepath, uploadPaths, size, resist_textarea, filePath)
SELECT '영화관람권', 'ticket.png', UUID(), NOW(), '20241211', @up, 31071, '', '20241211'
WHERE NOT EXISTS (SELECT 1 FROM movie_ticket WHERE movie_ticket_name = '영화관람권' AND movie_ticket_filename = 'ticket.png');

-- 5) 팝콘·콤보 (delicious)
INSERT INTO delicious (delicious_name, delicious_filename, uuid, reg_date, movie_filepath, size, resist_textarea, filePath, uploadPaths, movie_resist_filePath)
SELECT '꼬북칩 팝콘', '꼬북칩 팝콘.png', UUID(), NOW(), '20241212', 654190, '', '20241212', @up, CONCAT(@up, '/20241212/꼬북칩 팝콘.png')
WHERE NOT EXISTS (SELECT 1 FROM delicious WHERE delicious_name = '꼬북칩 팝콘' AND delicious_filename = '꼬북칩 팝콘.png');
INSERT INTO delicious (delicious_name, delicious_filename, uuid, reg_date, movie_filepath, size, resist_textarea, filePath, uploadPaths, movie_resist_filePath)
SELECT '달콤팝콘(대)', '달콤팝콘(대).png', UUID(), NOW(), '20241212', 181134, '', '20241212', @up, CONCAT(@up, '/20241212/달콤팝콘(대).png')
WHERE NOT EXISTS (SELECT 1 FROM delicious WHERE delicious_name = '달콤팝콘(대)' AND delicious_filename = '달콤팝콘(대).png');
INSERT INTO delicious (delicious_name, delicious_filename, uuid, reg_date, movie_filepath, size, resist_textarea, filePath, uploadPaths, movie_resist_filePath)
SELECT '달콤팝콘(중)', '달콤팝콘(중).png', UUID(), NOW(), '20241212', 142053, '', '20241212', @up, CONCAT(@up, '/20241212/달콤팝콘(중).png')
WHERE NOT EXISTS (SELECT 1 FROM delicious WHERE delicious_name = '달콤팝콘(중)' AND delicious_filename = '달콤팝콘(중).png');
INSERT INTO delicious (delicious_name, delicious_filename, uuid, reg_date, movie_filepath, size, resist_textarea, filePath, uploadPaths, movie_resist_filePath)
SELECT '미니언즈 미니콤보', '미니언즈 미니콤보.png', UUID(), NOW(), '20241212', 286341, '', '20241212', @up, CONCAT(@up, '/20241212/미니언즈 미니콤보.png')
WHERE NOT EXISTS (SELECT 1 FROM delicious WHERE delicious_name = '미니언즈 미니콤보' AND delicious_filename = '미니언즈 미니콤보.png');
INSERT INTO delicious (delicious_name, delicious_filename, uuid, reg_date, movie_filepath, size, resist_textarea, filePath, uploadPaths, movie_resist_filePath)
SELECT '미니언즈_타파컵_콤보', '미니언즈_타파컵_콤보 .png', UUID(), NOW(), '20241212', 344920, '', '20241212', @up, CONCAT(@up, '/20241212/미니언즈_타파컵_콤보 .png')
WHERE NOT EXISTS (SELECT 1 FROM delicious WHERE delicious_name = '미니언즈_타파컵_콤보' AND delicious_filename = '미니언즈_타파컵_콤보 .png');
INSERT INTO delicious (delicious_name, delicious_filename, uuid, reg_date, movie_filepath, size, resist_textarea, filePath, uploadPaths, movie_resist_filePath)
SELECT '순후추 팝콘', '순후추 팝콘.png', UUID(), NOW(), '20241212', 323641, '', '20241212', @up, CONCAT(@up, '/20241212/순후추 팝콘.png')
WHERE NOT EXISTS (SELECT 1 FROM delicious WHERE delicious_name = '순후추 팝콘' AND delicious_filename = '순후추 팝콘.png');
INSERT INTO delicious (delicious_name, delicious_filename, uuid, reg_date, movie_filepath, size, resist_textarea, filePath, uploadPaths, movie_resist_filePath)
SELECT '스몰세트', '스몰세트.png', UUID(), NOW(), '20241212', 103948, '', '20241212', @up, CONCAT(@up, '/20241212/스몰세트.png')
WHERE NOT EXISTS (SELECT 1 FROM delicious WHERE delicious_name = '스몰세트' AND delicious_filename = '스몰세트.png');
INSERT INTO delicious (delicious_name, delicious_filename, uuid, reg_date, movie_filepath, size, resist_textarea, filePath, uploadPaths, movie_resist_filePath)
SELECT '양파팝콘(대)', '양파팝콘(대).png', UUID(), NOW(), '20241212', 172357, '', '20241212', @up, CONCAT(@up, '/20241212/양파팝콘(대).png')
WHERE NOT EXISTS (SELECT 1 FROM delicious WHERE delicious_name = '양파팝콘(대)' AND delicious_filename = '양파팝콘(대).png');
INSERT INTO delicious (delicious_name, delicious_filename, uuid, reg_date, movie_filepath, size, resist_textarea, filePath, uploadPaths, movie_resist_filePath)
SELECT '양파팝콘(중)', '양파팝콘(중).png', UUID(), NOW(), '20241212', 133765, '', '20241212', @up, CONCAT(@up, '/20241212/양파팝콘(중).png')
WHERE NOT EXISTS (SELECT 1 FROM delicious WHERE delicious_name = '양파팝콘(중)' AND delicious_filename = '양파팝콘(중).png');
INSERT INTO delicious (delicious_name, delicious_filename, uuid, reg_date, movie_filepath, size, resist_textarea, filePath, uploadPaths, movie_resist_filePath)
SELECT '어벤져스 탑퍼콤보', '어벤져스 탑퍼콤보.png', UUID(), NOW(), '20241212', 699323, '', '20241212', @up, CONCAT(@up, '/20241212/어벤져스 탑퍼콤보.png')
WHERE NOT EXISTS (SELECT 1 FROM delicious WHERE delicious_name = '어벤져스 탑퍼콤보' AND delicious_filename = '어벤져스 탑퍼콤보.png');
INSERT INTO delicious (delicious_name, delicious_filename, uuid, reg_date, movie_filepath, size, resist_textarea, filePath, uploadPaths, movie_resist_filePath)
SELECT '치즈팝콘(대)', '치즈팝콘(대).png', UUID(), NOW(), '20241212', 180251, '', '20241212', @up, CONCAT(@up, '/20241212/치즈팝콘(대).png')
WHERE NOT EXISTS (SELECT 1 FROM delicious WHERE delicious_name = '치즈팝콘(대)' AND delicious_filename = '치즈팝콘(대).png');
INSERT INTO delicious (delicious_name, delicious_filename, uuid, reg_date, movie_filepath, size, resist_textarea, filePath, uploadPaths, movie_resist_filePath)
SELECT '큰세트', '큰세트.png', UUID(), NOW(), '20241212', 156179, '', '20241212', @up, CONCAT(@up, '/20241212/큰세트.png')
WHERE NOT EXISTS (SELECT 1 FROM delicious WHERE delicious_name = '큰세트' AND delicious_filename = '큰세트.png');
INSERT INTO delicious (delicious_name, delicious_filename, uuid, reg_date, movie_filepath, size, resist_textarea, filePath, uploadPaths, movie_resist_filePath)
SELECT '팝콘(대)', '팝콘(대).png', UUID(), NOW(), '20241212', 176198, '', '20241212', @up, CONCAT(@up, '/20241212/팝콘(대).png')
WHERE NOT EXISTS (SELECT 1 FROM delicious WHERE delicious_name = '팝콘(대)' AND delicious_filename = '팝콘(대).png');

-- 확인용(조회만)
SELECT 'movie_title' AS t, COUNT(*) AS n FROM movie_title
UNION ALL SELECT 'event_board_image', COUNT(*) FROM event_board_image
UNION ALL SELECT 'movie_gifticon', COUNT(*) FROM movie_gifticon
UNION ALL SELECT 'movie_ticket', COUNT(*) FROM movie_ticket
UNION ALL SELECT 'delicious', COUNT(*) FROM delicious;
