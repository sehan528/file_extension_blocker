# File Extension Blocker

### Flow 서버 개발자 과제 - 장세창

<br>

## 프로젝트 소개
어떤 파일들은 첨부시 보안에 문제가 될 수 있습니다. 특히 exe, sh 등의 실행파일이 존재할 경우 서버에 올려서 실행이 될 수 있는 위험이 있어 파일 확장자 차단을 하게 되었습니다.<br>
File Extension Blocker는 고정 확장자와 커스텀 확장자를 지정하여 확장자 관리 및 차단 기능을 지원하는 웹 입니다.
 

<br>

## 주요 기능
- 고정 확장자 관리: 미리 정의된 확장자 목록을 토글하여 차단/허용
- 커스텀 확장자 관리: 사용자 정의 확장자 추가 및 삭제 (최대 200개)
- 파일 업로드: 차단되지 않은 확장자를 가진 파일만 업로드 하여 확장자 차단 테스트


<br>

## 기술 스택
- Backend: Spring Boot, Java
- Frontend: HTML, JavaScript, jQuery
- CSS: Tailwind CSS
- Database: MySQL (AWS RDS), H2
- Deployment: AWS EC2
- Build Tool: Gradle


<br>

## 추가 고려사항
1. 파일 업로드 안전성 강화
    - **이미 등록된 파일**의 확장자를 차단 시도할 경우 경고 메시지 표시
    - **이미 업로드된 파일**의 중복 업로드 방지
    - **파일 선택 없이 업로드 시도** 시 사용자에게 알림
      
2. 커스텀 확장자 유효성 검사 강화
    - **확장자 길이 제한**: 최소 1자에서 최대 20자
    - **입력 문자 제한**: 영문자와 숫자만 허용
      
3. DTO와 Builder annotation 활용
    - DTO를 통한 계층 간 데이터 전송으로 비즈니스 로직 안정성 향상
    - Builder 패턴을 사용하여 객체 생성 유연성 확보, 잘못된 값을 Set 방지 및 가독성 개선
      
4. 컨트롤러 분리
    - RestController와 Controller를 구분하여 API 엔드포인트와 뷰 렌더링 로직을 명확히 분리
    
5. 확장자 테이블 설계 최적화
    - 고정 확장자를 관리하는 ‘blocked_extensions’ table 에 'is_fixed_extension' column을 추가.
    - 고객 요구에 유연하게 고정 확장자 개수 및 타입 제어 가능.
    
6. 테스트 코드 구현
    - 도메인 구현 후 테스트 코드를 작성하여 기능의 정확성 검증
    - 단위 테스트를 통해 시스템의 안정성과 유지보수성 향상


<br>

## 화면 구성 
![image](https://github.com/sehan528/file_extension_blocker/assets/71550827/b9fc28c2-9b6b-4d0c-a34b-90b4fbb19301)

<br>

## ERD
![image](https://github.com/sehan528/file_extension_blocker/assets/71550827/10ba7240-7168-4d4a-9f7b-53e955210c94)


<br><br>

