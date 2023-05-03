## [3Week] 허준홍

### 미션 요구사항 분석 & 체크리스트

---

- [x] 호감취소/호감사유변경 쿨타임 지정
  - [x] 개별 호감표시건에 대해서, 호감표시를 한 후 개별호감표시건에 대해서, 3시간 동안은 호감취소와 호감사유변경을 할 수 없도록 작업
  - [x] 호감사유변경을 한 후 개별호감표시건에 대해서, 3시간 동안은 호감취소와 호감사유변경을 할 수 없도록 작업


- [x] 네이버클라우드플랫폼을 통한 배포
  - [x] 도메인 없이, IP로 접속


- [x] 알림기능 구현
  - [x] 호감표시를 받았거나, 본인에 대한 호감사유가 변경된 경우에 알림페이지에서 확인이 가능하도록 작업
  - [x] 각각의 알림은 생성시에 readDate 가 null 이고, 사용자가 알림을 읽으면 readDate 가 현재날짜로 세팅되어야 합니다.
  



### 3주차 미션 요약

---
**[접근 방법]**

#### 호감취소/호감사유변경 쿨타임 지정 
- 개별 호감표시건에 대해서, 호감표시를 한 후 개별호감표시건에 대해서, 3시간 동안은 호감취소와 호감사유변경을 할 수 없도록 작업
  - 이미 UI상에서 LikeablePerson 엔티티의 modifyUnlockDate(쿨타임이 끝나는 시간)을 이용해서 호감변경과 호감취소의 버튼을
쿨타임이 끝나기 전에는 비활성화 되도록 되어있다.
  -  처음에는 크로노유닛을 이용해서 수정시간과 현재시간의 차이 즉, 수정되서 지금까지 흐른 시간이 쿨타임 시간보다 적으면 변경과 삭제가
안되도록 했다.
  - canCancel, canModify 메서드에서 timcCheck메서드를 호출한다.
  - application.yml에 actionModify와 actionCancel을 추가했다.
  - timeCheck메서드는 개당 호감표시 정보인 likeablePerson과 application.yml에 추가한 action을 통해 "Modify" 또는 "Cancel"을
파라미터로 받도록했다.
  - UI상에서 남은 시간을 좀 더 정확하게 알기 위해 getModifyUnlockDateRemainStrHuman()의 메서드를 변경해 남은 시간을 시간, 분, 초로 보이도록 했다.
  - NotProd를 통해 처음 생성된 데이터들은 테스트를 생성 후 modifyUnlockDate의 값을 변경해서 쿨타임을 바로 없애버린 상태다.
  - 크로노 유닛을 통해 쿨타임을 체크하도록 했을 때 modifyUnlockDate는 사용하지 않았기 때문에 실행될 때 생성되는 호감표시에서
수정과 삭제가 안되는 오류가 발생했다.
  - 그래서 timcCheck메서드에서도 modifyUnlockDate를 사용하기로 했다.


#### 알림기능 구현
- 호감표시를 받았거나, 본인에 대한 호감사유가 변경된 경우에 알림페이지에서 확인이 가능하도록 작업
  - 호감표시를 받거나, 변경될 때 알림이 생성되어야 한다고 생각해서 NotificationEventListner을 생성했다.
  - NotificationEventListner 안에 EventAfterLike와 EventAfterModifyAttractiveType를 추가하였다.
  - notificationService에서 호감표시를 하나 받을 때 notification(알림)이 생성되도록 했다. TypeCode는 "Like"이다.
  - 생성되는 알림의 toInstamember 정보는 호감표시인 likeablePerson을 통해 getToinstaMember()의 메서드로 가져와 저장했다.
  - 생성되는 알림의 fromInstamember 정보는 호감표시인 likeablePerson을 통해 getFrominstaMember()의 메서드로 가져와 저장했다.
  - 생성되는 알림의 newAttractiveTypeCode는 알림이 생설될 때 새로 저장되는 호감사유라고 생각해서 likeablePerson.getAttractiveTypeCode()
를 넣었다.
  - 알림 목록 창에서 성별과 사유를 정확히 보기위해 Notification엔티티에 getGenderDisplayName() 과 getAttractiveTypeDisplayName()
을 생성했다.
  - 알림 생성시간옆의 몇분 전 의 정보를 위해 Notification엔티티에 passedTime이라는 메서드를 생성해서
지금시간과 알림이 생성된 시간의 차이를 통해 표시했다.
  - 호감사유가 변경되면 EventAfterModifyAttractiveType를 통해 새 알림을 생성하도록 하였고 TypeCode에는 "Modify"를 넣었고 파라미터로
받은 oldAttractiveTypeCode를 새로 생성하는 notification의 oldAttractiveTypeCode에 넣었다. newAttractiveTypeCode에는 likeablePerson.getAttractiveTypeCode()의 정보를 넣었다.
  - 알림 목록창에서 호감 표시와 호감 사유 변경의 내용이 다르기 때문에 Notification엔티티에 
isLike와 isModify를 생성하여 notification/list.html에서 th:if="${notification.isLike()}"면 호감표시 알림 내용을 표시하게 해서 구분하였다.


- 각각의 알림은 생성시에 readDate 가 null 이고, 사용자가 알림을 읽으면 readDate 가 현재날짜로 세팅되어야 합니다.
  - 읽으면 readDate에 현재날짜를 생성하도록 하여야한다.
  - 읽는다는걸 처음에 어떻게 할 지 고민하다. 알림 항목에서 알림을 누르면 알림의 세부 페이지로 이동하도록 해야겠다고 생각했다.
  - 세부 페이지로 이동할 때 이벤트를 통해 readDate의 값에 현재 시간을 저장하면 될것 같다고 생각했다.
  - notification_detail.html을 생성하고 개별 알림을 클릭 시 notification_detail.html로 이동하도록 하였다.
  - EventReadNotifications라는 이벤트를 하나 생성하여 NotificationService에서 읽음 이벤트 발생시 setReadDate를 동작하게 해서 
readDate를 현재시간으로 설정하도록 하였다.
  - notification_detail.html 내부에는 읽은 시간을 비록하여 간단하게 호감을 표시해준 사람의 이름과 계정으로 갈 수 있는 링크를 표시하도록 했다.


#### 네이버클라우드플랫폼을 통한 배포
- 도메인 없이, IP로 접속
  - 강사님의 강의를 따라 진행하였습니다.
  - 네이버 클라우드 플랫폼을 통해 생성한 서버의 공인 IP는 27.96.134.88 이었다.
![캡처.PNG](..%2F..%2F..%2F..%2F%EC%B0%8C%EB%A5%B4%EB%A0%88%EA%B8%B0%2F%EC%BA%A1%EC%B2%98.PNG)
  - 위의 두 가지 미션을 구현한 3Week 브랜치를 이미지로 생성하여 실행시켜 http://27.96.134.88:80을 입력하여 성공적으로 접속이되는것을 확인할 수 있었다.
![진짜 배포.PNG](..%2F..%2F..%2F..%2F%EC%B0%8C%EB%A5%B4%EB%A0%88%EA%B8%B0%2F%EC%A7%84%EC%A7%9C%20%EB%B0%B0%ED%8F%AC.PNG)



**[특이사항]**

구현 과정에서 아쉬웠던 점 / 궁금했던 점을 정리합니다.
- 시간을 정확하기 표현하기 위해서 passedTime 혹은 getModifyUnlockDateRemainStrHuman의 메서드를 만들었는데 초의 시간의 시간과 분으로
표현하기 위해서 3600과 60등으로 나누었는데 이것또한 하드코딩보다 yml을 통해 입력하는게 더 좋았을지 궁금하다.
- 호감 표시와 삭제의 쿨타임을 구별하기 위한 timeCheck메서드에서도 RsData에 기입하기 위한 글을 직접 입력했는데 이 부분도 하드코딩이라
별로일지 잘 모르겠다.
- 알림을 읽을시에 readDate가 설정된다는 말이 헷갈려서 클릭시 세부 정보로 들어가야 읽은걸로 판단한 부분이 좋은지 모르겠다.
- notification생성할 때 기입하는 정보들을 제대로 기입한건지, 특히 생성할 때 newAttractiveTypeCode에는 likeablePerson.getAttractiveTypeCode()를 통해 넣었는데
notification의 newgender에는 따로 값을 넣지 않아서 likeablePerson.getFromInstaMember().getGender()를 통해 넣어주는것으로 통일하는것이 좋은가 고민된다.
