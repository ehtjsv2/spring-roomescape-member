# 기능 요구사항

## 예약 가능 시각

- [x] 예약 가능 시간은 null일 수 없다.
- [x] 예약 가능 시간은 이미 등록되어 있는 시간과 겹칠 수 없다.

## 예약

- [x] 예약 시각이 중복되는 예약이 존재하는 경우 예약을 할 수 없다.

## 예약자 이름

- [x] 예약자 이름은 빈 문자열일 수 없다.
- [x] 예약자 이름은 Null일 수 없다.

## 예약 날짜

- [x] 예약 날짜는 오늘보다 이전일 수 없다.
- [x] 예약 날짜는 null일 수 없다.

## 예약 시간

- [x] 예약시간은 지정된 예약시간 중 하나여야 한다.

### 2단계 요구사항

- [x] 테마를 조회할 수 있다
- [x] 테마를 추가할 수 있다
- [x] 테마를 삭제할 수 있다

### 3단계 요구사항

- [x] 날짜와 테마를 기반으로 예약 가능한 시각과 예약 불가능한 시각을 응답할 수 있다.
- [x] `/`요청 시 인기 테마 페이지를 응답한다.
- [ ] 지난 7일간의 기록으로 인기테마 목록 10개를 불러올 수 있다.

### 4단계 요구사항

- [x] 로그인 페이지요청에 응답이 가능하다 (GET "/login")
- [ ] 로그인 API 구현. (POST "login")
    - [x] email, password로 User를 불러올 수 있다.
    - [ ] 로그인 응답 cookie에 이름 기반의 token값을 포함 시킨다.
- [ ] 사용자 정보 조회 API 구현 (GET "/login/check")
