## [4Week] 허준홍

### 미션 요구사항 분석 & 체크리스트

---

- [x] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 성별 필터링기능 구현
  - [x] 내가 받은 호감리스트에서 특정 성별을 가진 사람에게서 받은 호감만 필터링해서 볼 수 있다.
  

- [x] 네이버클라우드플랫폼을 통한 배포, 도메인, HTTPS 까지 적용
  - [x] https://도메인/ 형태로 접속이 가능
  - [x] 운영서버에서 각종 소셜로그인, 인스타 아이디 연결이 잘 되어야 합니다.


- [x] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 호감사유 필터링기능 구현
  - [x] 내가 받은 호감리스트에서 특정 호감사유의 호감만 필터링해서 볼 수 있다.
  
- [x] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 정렬기능
  - [x] 아래 케이스 대로 작동해야 한다.
    - [x] 최신순(기본)
      - 가장 최근에 받은 호감표시를 우선적으로 표시
    - [x] 날짜순
      - 가장 오래전에 받은 호감표시를 우선적으로 표시
    - [x] 인기 많은 순
      - 가장 인기가 많은 사람들의 호감표시를 우선적으로 표시
    - [x] 인기 적은 순
      - 가장 인기가 적은 사람들의 호감표시를 우선적으로 표시
    - [x] 성별순
      - 여성에게 받은 호감표시를 먼저 표시하고, 그 다음 남자에게 받은 호감표시를 후에 표시
      - 2순위 정렬조건으로는 최신순
    - [x] 호감사유순
      - 외모 때문에 받은 호감표시를 먼저 표시하고, 그 다음 성격 때문에 받은 호감표시를 후에 표시, 마지막으로 능력 때문에 받은 호감표시를 후에 표시
      - 2순위 정렬조건으로는 최신순

- [ ] 젠킨스를 통해서 리포지터리의 main 브랜치에 커밋 이벤트가 발생하면 자동으로 배포가 진행되도록
  - [ ] 네이버클라우드플랫폼을 이용합니다.
  - [ ] 젠킨스를 이용합니다.


### 4주차 미션 요약

---
**[접근 방법]**

#### 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 성별 필터링기능 구현
- 내가 받은 호감리스트에서 특정 성별을 가진 사람에게서 받은 호감만 필터링해서 볼 수 있다.
  - 스트림을 이용해서 필터링했다.
  - 개별 호감표시에서 호감을 표시한 사람의 성별을 이용해서 필터링

#### 네이버클라우드플랫폼을 통한 배포, 도메인, HTTPS 까지 적용
- https://도메인/ 형태로 접속이 가능
  - 먼저 "wholikes.site"라는 도메인을 구매했다.
![내가 구매한 도메인.PNG](..%2F..%2F..%2F..%2F%EB%A9%94%EC%B6%94%EB%9D%BC%EA%B8%B0%2F%EB%82%B4%EA%B0%80%20%EA%B5%AC%EB%A7%A4%ED%95%9C%20%EB%8F%84%EB%A9%94%EC%9D%B8.PNG)
  - 네이버 클라우드 플랫폼을 통해서 배포중인 서버에 프록시Hosts에 등록
![wholikes.site.PNG](..%2F..%2F..%2F..%2F%EB%A9%94%EC%B6%94%EB%9D%BC%EA%B8%B0%2Fwholikes.site.PNG)
  - wholikes.site의 url로 접속하게되면 배포중인 그램그램에 접속된다.
![4주차 미션 배포.PNG](..%2F..%2F..%2F..%2F%EB%A9%94%EC%B6%94%EB%9D%BC%EA%B8%B0%2F4%EC%A3%BC%EC%B0%A8%20%EB%AF%B8%EC%85%98%20%EB%B0%B0%ED%8F%AC.PNG)

- 운영서버에서 각종 소셜로그인, 인스타 아이디 연결이 잘 되어야 합니다.
  - 각 소셜 로그인을 위한 리다이렉트 URL을 수정하여 로그인이 되도록 했다.

#### 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 호감사유 필터링기능 구현
- 내가 받은 호감리스트에서 특정 호감사유의 호감만 필터링해서 볼 수 있다.
  - 스트림을 이용해서 필터링했다.
  - 개별 호감표시에서 호감사유를 이용해서 필터링 했다.

  
#### 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 정렬기능
-  최신순(기본)
   - 스트림의 sorted()를 이용 
   - CreatedDate를 기준으로 reversed()를 적용하여 반대로 정렬


-  날짜순
    - 스트림의 sorted()를 이용 
    - CreatedDate를 기준으로 정렬


- 인기 많은 순
  - 스트림의 sorted()를 이용
  - 개별 호감표시의 호감을 표시한 사람의 인스타 멤버인 FromInstaMember()
  - FromInstaMember인 InstaMember를 좋아하는 사람들의 호감표시 List인 ToLikeablePeople
  - 즉, getFromInstaMember().getToLikeablePeople()의 size()를 이용해서 인기 많음의 척도를 나타냄 
  - reversed()를 이용해서 많은것 부터 내림차순


- 인기 적은 순
  - 스트림의 sorted()를 이용
  - 개별 호감표시의 호감을 표시한 사람의 인스타 멤버인 FromInstaMember()
  - FromInstaMember인 InstaMember를 좋아하는 사람들의 호감표시 List인 ToLikeablePeople
  - 즉, getFromInstaMember().getToLikeablePeople()의 size()를 이용해서 인기 많음의 척도를 나타냄


- 성별순
   - 호감을 표시해준 사람들의 성별을 기준으로 먼저 정렬
   - 그 후에 thenComparing를 이용하여 최신순으로 정렬


- 호감사유순
  - 호감사유를 기준으로 정렬
  - 그 후에 thenComparing를 이용하여 최신순으로 정렬

**[특이사항]**
- '젠킨스를 통해서 리포지터리의 main 브랜치에 커밋 이벤트가 발생하면 자동으로 배포가 진행되도록'의 선택미션을 수행하지 못한것이 아쉽다.


- 쿼리DSL이 아니라 스트림을 이용하여 구현한 것이 조금 아쉽다. 


- 정렬에서 최신순의 정렬이 아닐 때도 다른 항목에서도 해당 정렬을 만족하면 다음 기준으로는 따로 정렬을 하지 않았음에도 최신순으로 정렬이 되는것 같은데 이 부분이 궁금하다.



- 성별을 기준으로 정렬할 때는 ```likeablePeopleStream.sorted(Comparator.comparing((LikeablePerson p) -> p.getFromInstaMember().getGender()).reversed());```로 여성이 먼저 나오게 정렬이 되었는데 
이 다음 정렬기준인 최신순을 위해 ```.thenComparing((LikeablePerson p) -> p.getCreateDate()).reversed()```를 추가 하니까 남성먼저 정렬이 되었다.


- 결국 ```likeablePeopleStream.sorted(Comparator.comparing((LikeablePerson p) -> p.getFromInstaMember().getGender())
  .thenComparing((LikeablePerson p) -> p.getCreateDate()).reversed());``` 처럼 처음 성별을 기준으로 정렬할 때 ```reversed()```를 빼니까 정렬이 잘 되는데 왜 저렇게 되는지 잘 모르겠다.