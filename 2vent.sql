-- phpMyAdmin SQL Dump
-- version 4.6.5.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- 생성 시간: 17-08-08 08:16
-- 서버 버전: 10.1.21-MariaDB
-- PHP 버전: 5.6.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 데이터베이스: `2vent`
--

-- --------------------------------------------------------

--
-- 테이블 구조 `company`
--

CREATE TABLE `company` (
  `com_number` varchar(255) NOT NULL,
  `com_name` varchar(255) NOT NULL,
  `com_addr` varchar(255) NOT NULL,
  `com_category` int(11) NOT NULL,
  `com_manager` varchar(30) NOT NULL,
  `com_URI` varchar(255) NOT NULL,
  `id` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 테이블의 덤프 데이터 `company`
--

INSERT INTO `company` (`com_number`, `com_name`, `com_addr`, `com_category`, `com_manager`, `com_URI`, `id`) VALUES
('11111111111', '테스트1', '대구 동구 신암동 294 동대구역', 0, '1', 'store_img/2vent162453_1009990063.jpg', '1'),
('22222222222', '테스트2', '동대구로', 1, '1', 'store_img/2vent164554_962694965.jpg', '1');

-- --------------------------------------------------------

--
-- 테이블 구조 `entry`
--

CREATE TABLE `entry` (
  `event_number` int(11) NOT NULL,
  `id` varchar(30) NOT NULL,
  `entry_name` varchar(255) NOT NULL,
  `entry_addr` varchar(255) NOT NULL,
  `entry_birthday` date NOT NULL,
  `entry_sex` int(11) NOT NULL,
  `entry_phone` varchar(255) NOT NULL,
  `entry_type` int(11) NOT NULL,
  `com_number` varchar(255) NOT NULL,
  `is_entry` int(11) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 테이블의 덤프 데이터 `entry`
--

INSERT INTO `entry` (`event_number`, `id`, `entry_name`, `entry_addr`, `entry_birthday`, `entry_sex`, `entry_phone`, `entry_type`, `com_number`, `is_entry`) VALUES
(2, '2', '신은재', '수성구 용학로', '1993-02-14', 1, '01035208543', 0, '22222222222', 0),
(32, '2', '신은재', '수성구 용학로', '1993-02-14', 1, '01035208543', 0, '11111111111', 0);

-- --------------------------------------------------------

--
-- 테이블 구조 `event`
--

CREATE TABLE `event` (
  `event_number` int(11) NOT NULL,
  `event_name` varchar(255) NOT NULL,
  `event_type` int(11) NOT NULL,
  `event_stats` int(11) NOT NULL,
  `event_content` text,
  `event_price` int(11) NOT NULL,
  `event_dis_price` int(11) NOT NULL,
  `event_people` int(11) DEFAULT NULL,
  `event_startday` date DEFAULT NULL,
  `event_endday` date DEFAULT NULL,
  `event_starttime` time DEFAULT NULL,
  `event_endtime` time DEFAULT NULL,
  `event_payment` int(11) DEFAULT NULL,
  `event_target` int(11) DEFAULT NULL,
  `event_minage` int(11) DEFAULT NULL,
  `event_maxage` int(11) DEFAULT NULL,
  `event_sex` int(11) DEFAULT NULL,
  `event_area` varchar(255) DEFAULT NULL,
  `com_number` varchar(255) NOT NULL,
  `id` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 테이블의 덤프 데이터 `event`
--

INSERT INTO `event` (`event_number`, `event_name`, `event_type`, `event_stats`, `event_content`, `event_price`, `event_dis_price`, `event_people`, `event_startday`, `event_endday`, `event_starttime`, `event_endtime`, `event_payment`, `event_target`, `event_minage`, `event_maxage`, `event_sex`, `event_area`, `com_number`, `id`) VALUES
(2, '테스트2', 0, 0, 'event_img/1/테스트2.jpg', 5000, 4000, 99, '2017-07-30', '2017-08-11', '20:00:00', '20:00:00', 0, 0, 0, 0, 0, '', '22222222222', '1'),
(4, '테스트4', 1, 2, 'event_img/1/테스트4.jpg', 20000, 1800, 10, '2017-08-01', '2017-08-02', '12:00:00', '12:00:00', 0, 0, 0, 0, 0, '', '11111111111', '1'),
(7, 'ㅇㅇㅇㅇ', 0, 2, 'event_img/1/ㅇㅇㅇㅇ.jpg', 1000, 900, 1, '2017-08-01', '2017-08-03', '12:00:00', '12:00:00', 0, 0, 0, 0, 0, '', '11111111111', '1'),
(8, '테스트7', 0, 2, 'event_img/1/테스트7.jpg', 100000, 99000, 5, '2017-08-01', '2017-08-01', '08:00:00', '09:00:00', 0, 0, 0, 0, 0, '', '11111111111', '1'),
(18, '임시저장5', 0, 2, 'event_img/1/임시저장5.jpg', 30000, 28000, 2, '2017-07-31', '2017-07-31', '12:00:00', '13:00:00', 0, 1, 20, 30, 2, '대구광역시', '11111111111', '1'),
(30, '이벤트 테스트', 0, 2, 'event_img/1/event-20jpg', 35000, 34000, 5, '2017-08-01', '2017-08-01', '09:00:00', '10:00:00', 0, 0, 0, 0, 0, '', '11111111111', '1'),
(31, '테스트11', 0, 1, 'event_img/1/event_31.jpg', 5000, 4000, 0, '0000-00-00', '0000-00-00', '00:00:00', '00:00:00', 0, 0, 0, 0, 0, '', '11111111111', '1'),
(32, '테스트11', 0, 0, 'event_img/1/event_32.jpg', 20000, 18000, 98, '2017-08-03', '2017-08-10', '20:00:00', '20:00:00', 0, 0, 0, 0, 0, '', '11111111111', '1');

-- --------------------------------------------------------

--
-- 테이블 구조 `event_img`
--

CREATE TABLE `event_img` (
  `event_number` int(11) NOT NULL,
  `event_URI` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 테이블의 덤프 데이터 `event_img`
--

INSERT INTO `event_img` (`event_number`, `event_URI`) VALUES
(32, 'event_img/1/event_32.jpg'),
(2, 'event_img/1/테스트8.jpg'),
(2, 'event_img/1/임시저장4.jpg');

-- --------------------------------------------------------

--
-- 테이블 구조 `user`
--

CREATE TABLE `user` (
  `id` varchar(30) NOT NULL,
  `pw` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `addr` varchar(255) NOT NULL,
  `birthday` date NOT NULL,
  `sex` int(11) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `user_type` int(11) NOT NULL,
  `account_number` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 테이블의 덤프 데이터 `user`
--

INSERT INTO `user` (`id`, `pw`, `name`, `addr`, `birthday`, `sex`, `phone`, `user_type`, `account_number`) VALUES
('1', '1', '1', '1', '1993-01-01', 0, '1', 2, '1'),
('2', '2', '신은재', '수성구 용학로', '1993-02-14', 1, '01035208543', 1, '1371029731'),
('3', '3', '테스트유저', '지엔케이', '1900-01-01', 0, '01000000000', 1, '1371029731');

--
-- 덤프된 테이블의 인덱스
--

--
-- 테이블의 인덱스 `company`
--
ALTER TABLE `company`
  ADD PRIMARY KEY (`com_number`),
  ADD KEY `FK_user_TO_company` (`com_manager`),
  ADD KEY `FK_user_TO_company2` (`id`);

--
-- 테이블의 인덱스 `entry`
--
ALTER TABLE `entry`
  ADD KEY `FK_event_TO_entry` (`event_number`),
  ADD KEY `FK_user_TO_entry` (`id`),
  ADD KEY `FK_company_TO_entry` (`com_number`);

--
-- 테이블의 인덱스 `event`
--
ALTER TABLE `event`
  ADD PRIMARY KEY (`event_number`),
  ADD KEY `FK_company_TO_event` (`com_number`),
  ADD KEY `FK_user_TO_event` (`id`);

--
-- 테이블의 인덱스 `event_img`
--
ALTER TABLE `event_img`
  ADD KEY `FK_event_TO_event_img` (`event_number`);

--
-- 테이블의 인덱스 `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- 덤프된 테이블의 AUTO_INCREMENT
--

--
-- 테이블의 AUTO_INCREMENT `event`
--
ALTER TABLE `event`
  MODIFY `event_number` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;
--
-- 덤프된 테이블의 제약사항
--

--
-- 테이블의 제약사항 `company`
--
ALTER TABLE `company`
  ADD CONSTRAINT `FK_user_TO_company` FOREIGN KEY (`com_manager`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_user_TO_company2` FOREIGN KEY (`id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 테이블의 제약사항 `entry`
--
ALTER TABLE `entry`
  ADD CONSTRAINT `FK_company_TO_entry` FOREIGN KEY (`com_number`) REFERENCES `company` (`com_number`),
  ADD CONSTRAINT `FK_event_TO_entry` FOREIGN KEY (`event_number`) REFERENCES `event` (`event_number`),
  ADD CONSTRAINT `FK_user_TO_entry` FOREIGN KEY (`id`) REFERENCES `user` (`id`);

--
-- 테이블의 제약사항 `event`
--
ALTER TABLE `event`
  ADD CONSTRAINT `FK_company_TO_event` FOREIGN KEY (`com_number`) REFERENCES `company` (`com_number`),
  ADD CONSTRAINT `FK_user_TO_event` FOREIGN KEY (`id`) REFERENCES `user` (`id`);

--
-- 테이블의 제약사항 `event_img`
--
ALTER TABLE `event_img`
  ADD CONSTRAINT `FK_event_TO_event_img` FOREIGN KEY (`event_number`) REFERENCES `event` (`event_number`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
