## [2Week] 허준홍

### 미션 요구사항 분석 & 체크리스트

---

- [x] 케이스 4 : 한명의 인스타회원이 다른 인스타회원에게 중복으로 호감표시를 할 수 없습니다.
  - [x] 같은 대상에게 호감을 표시하면 중복으로 이기 때문에 처리를 하지 않는다. (rq.historyBack)


- [x] 케이스 5 : 한명의 인스타회원이 11명 이상의 호감상대를 등록 할 수 없습니다.
  - [x] 10명까지 호감표시를 하고 11명 째 호감표시에서 처리되면 안된다. (rq.historyBack)

  
- [x] 케이스 6 : 케이스 4 가 발생했을 때 기존의 사유와 다른 사유로 호감을 표시하는 경우에는 성공으로 처리
  - [x] 같은 대상에게 호감을 표시할 때 사유가 다르다면 수정으로 처리하고 사유만 수정한다.
  - [x] resultCode = S-2
  - [x] msg=bbbb 에 대한 호감사유를 외모에서 성격으로 변경합니다.


- [x] 네이버 로그인
    - [x] 스프링 OAuth2 클라이언트로 구현
    - [x] 네이버 로그인으로 가입한 회원의 providerTypeCode : NAVER




### 2주차 미션 요약

---
**[접근 방법]**

체크리스트를 중심으로 각각의 기능을 구현하기 위해 어떤 생각을 했는지 정리합니다. .

- 같은 대상에게 호감을 표시하면 중복으로 이기 때문에 처리를 하지 않는다. (rq.historyBack)
  - 먼저 같은대상을 찾는게 중요하다고 생각했다. LikeablePerson에서 fromInstamember가 현재 로그인한 member의 instamember와 동일한 접속자이다.
  - 이 상태에서 addForm에서 전달받은 username이 호감을 표시하고 등록하려는 대상이다. 그렇다면 LikeablePerson에서 toInstamember의 username 전달받은 username과
똑같으면 이미 호감을 표시하여 LikeablePerson에 저장되어 있는 대상이기 때문에 중복으로 처리한다. exist라는 메서드를 만들어 existLikeablePeople에 저장했다.
  - 처음에는 로그인한 member의 instamember의 getFromLikealbePeople()을 이용해서 List를 다 불러 온 후 반복문으로 찾아냈지만, 이 후에
    JPA 함수에서 언더바(_)를 사용하여 관련 엔티티의 필드까지 검색조건으로 활용하여 필요한 LikeablePerson을 한 번에 불러오도록 했다.
  - historyBack은 rsData.of를 F-x (Fail)로 return하여 historyBack이 되도록 했다.


- 10명까지 호감표시를 하고 11명 째 호감표시에서 처리되면 안된다. (rq.historyBack)
  - 로그인한 member의 instamember의 getFromLikealbePeople()을 이용해서 List를 다 불러와 list의 size()를 10명 이상이 되면 rsData.of로 Fail을 return하도록 했다.
  - rsData.of를 F-x (Fail)로 return하여 historyBack이 되도록 했다.


- 같은 대상에게 호감을 표시할 때 사유가 다르다면 수정으로 처리하고 사유만 수정한다.
  - 이미 존재하는 대상의 LikealbePerson의 정보가 담긴 객체 existLikeablePeople의 호감 사유인 getAttractiveTypeCode()가 
새로 입력받아 들어온 attractiveTypeCode와 다르면 수정으로 처리하도록 했다.
  - modify(existLikeablePeople,attractiveTypeCode) 메서드를 이용해서 기존의 호감사유를 String beforeAttractive 에 저장해두고
호감 사유를 attractiveTypeCode로 수정하였다.
  - 수정 성공의 msg에는 "%s에 대한 호감사유를 %s에서 %s으로 변경합니다.".formatted(modifyLikeablePeople.getToInstaMember().getUsername()
    ,beforeAttractive,modifyLikeablePeople.getAttractiveTypeDisplayName()),modifyLikeablePeople 을 이용했다.
  - 현재 수정하는 [호감대상의 이름]에 대한 [이전의 호감사유]에서 [새로 받아온 attractiveTypeCode(호감사유)]으로 변경합니다.


- 스프링 OAuth2 클라이언트로 구현
  - 네이버 API에서 API 생성후 OAuth 클라이언트 ID 와 비밀번호를 발급 받았다.
  - 발급 받은 클라이언트 ID와 비밀번호를 yml에 추가했고 provider도 등록해주었다.



- 네이버 로그인으로 가입한 회원의 providerTypeCode : NAVER
  - 카카오톡 로그인을 위해 구현되어 있던 CustomOAuth2UserService.java를 통해 NAVER 로그인을 가능케 했다.
  - whenSocialLogin을 통해 최초 로그인 시 join이 발생
  - 소셜 로그인으로 로그인할 때는 비밀번호가 없다.


**[특이사항]**

구현 과정에서 아쉬웠던 점 / 궁금했던 점을 정리합니다.

- 네이버 로그인시 provider에서 user-name-attribute: response 로 받아오다 보니 id뿐만 아니라 다른 정보들도 한 번에 oauthId에 저장이 되어서
정보를 따로 불러오기 위해 OAuth2UserInfo를 상속받은 NaverUserInfo를 만들었는데 아직 어떻게 동작하는지 완벽하게 이해가 되지 않는다.

