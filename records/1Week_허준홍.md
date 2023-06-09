## [1Week] 허준홍

### 미션 요구사항 분석 & 체크리스트

---

- [x] 호감상대 삭제
  - [x] 삭제 항목에 대한 소유권이 본인(로그인한 사람)에게 있는지 체크
  - [x] 삭제 후 다시 호감목록 페이지로 돌아와야 한다.


- [x] 구글 로그인
    - [x] 스프링 OAuth2 클라이언트로 구현
    - [x] 구글 로그인으로 가입한 회원의 providerTypeCode : GOOGLE




### 1주차 미션 요약

---
**[접근 방법]**

체크리스트를 중심으로 각각의 기능을 구현하기 위해 어떤 생각을 했는지 정리합니다. .

- 삭제 항목에 대한 소유권이 본인(로그인한 사람)에게 있는지 체크
  - 현재 로그인한 유저의 멤버 클래스에서 인스타 회원 정보를 불러온다.
  - 그 인스타 회원 정보의 id와 삭제를 누르면서 get으로 전달 받은 id를 가진 LikablePerson 테이블에서 FromInstaMember의 id와 같으면 삭제 권한을 허용한다.
- 삭제 후 다시 호감목록 페이지로 돌아와야 한다.
  - 삭제 후 다시 호감목록 페이지로 돌아가기 위해서 저번주에 강사님의 진도를 계속 따라오면 작성했던 Rq클래스의 redirectWithMsg를 이용해서 호감목록으로 돌아갈 수 있겠다고 생각했다.
  - RsData<LikeablePerson>을 통해서 권한이 없으면 삭제 권한이 없다는 msg를 담은 RsData를 return하고 삭제가 성공적으로 완료되면 삭제가 되었다는 msg를 담은 RsData를 return한다.
  - 그리고 return받은 RsData의 .getData를 통해서 msg와 함께 목록으로 돌아간다.
  
- 스프링 OAuth2 클라이언트로 구현
  - 구글 플랫폼에서 프로젝트 생성후 OAuth 클라이언트 ID 와 비밀번호를 발급 받았다.
  - 발급 받은 클라이언트 ID와 비밀번호를 yml에 추가했고 scope도 등록해주었다.

- 구글 로그인으로 가입한 회원의 providerTypeCode : GOOGLE
  - 카카오톡 로그인을 위해 구현되어 있던 CustomOAuth2UserService.java를 통해 GOOGLE로 로그인을 가능케 했다.
  - whenSocialLogin을 통해 최초 로그인 시 join이 발생
  - 소셜 로그인으로 로그인할 때는 비밀번호가 없다.


**[특이사항]**

구현 과정에서 아쉬웠던 점 / 궁금했던 점을 정리합니다.

- 구글 로그인을 위한 기능을 구현하면서 yml에 왜 client-id, client-secret, scope만 추가하는지 궁금했는데, 구글의 경우 Spring Security OAuth2가 provider에 대한 정보를 가지고 있기에 작성할 필요가 없다고 한다.
- 아쉬웠던 점은 CustomOAuth2UserService.java파일의 아직 명확히 되지 않았다.
- 호감 삭제기능에서 Transactional을 붙이는것에 시간을 많이 소요했다.

**[리팩토링]**
- LikeablePersonRepository 에 KEY 값의 자료형이 Integer로 되어있어서 findById를 Long자료형인 id로 찾을 수 있도록 새로 작성했었는데 그냥 KEY자료형을 Long으로 바꾸면 직접 작성할 필요가 없었다.
- findById에서 id를 기준으로 찾은 LikeablePerson의 객체가 null일 경우에 대한 처리가 부족했다. 그래서 DataNotFoundException 에러를 띄우도록 변경했다.