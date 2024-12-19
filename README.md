## 1.프로젝트명 : Plus Week (플러스 주차 개인 과제)

---

## 2.프로젝트 설명
+ 관리자 및 고객 권한으로 로그인 및 회원가입이 가능하며 아이템을 만들어고 예약을 할 수 있는 프로그램
  + 필수 구현 기능 
    + Lv.1 createReservation 메서드에서 @Transactional을 이용하여 (All or Nothing) 수행.
    + Lv.2 WebConfug 메서드에 admin 인증/인가 코드 추가.
    + Lv.3 getReservations 메서드에서 JPQL 사용으로 N+1 문제 해결.
    + Lv.4 reportUsers 메서드를 수정하여 데이터 최소화
    + Lv.5 searchAndConvertReservations 메서드에서 QueryDSL를 사용하여 N+1 문제해결
    + Lv.6 item Entity에 @DynamicInsert를 사용하여 필요한 부분 갱신
    + Lv.7 updateReservationStatus 메서드 리팩토링
    + Lv.8 PasswordEncoder, Item Entity 단위 테스트 
    


---

## 3.프로젝트 설치 및 실행 방법
+ JAVA IDEA 프로그램 설치, JAVA 설치
+ Spring IDEA 프로그램 설치
+ mysql DB 설치
+ postman 사용
+ 코드 클론 후 사용

---

## 4.프로젝트 사용 방법
+ 개인 DB 설정
+ 프로그램 실행 후 서버가 정상적으로 작동하는 지 확인
+ postman을 통해 URL 및 JSON 작성

---

## 5.참고자료
+ https://cdchan.tistory.com/264
+ https://kafcamus.tistory.com/15

---

+ Version : 1.0
