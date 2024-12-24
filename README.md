## ✨ flow-api-gateway
MSA 구조에서 API-Gateway 역할을 담당하는 Repository입니다.

## Tool
- ![Java](https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
- ![Spring](https://img.shields.io/badge/Spring-6DB33F?style=flat-square&logo=spring&logoColor=white)
- ![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-6DB33F?style=flat-square&logo=spring&logoColor=white)
- ![Spring WebFlux](https://img.shields.io/badge/Spring%20WebFlux-6DB33F?style=flat-square&logo=spring&logoColor=white)
- ![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat-square&logo=spring&logoColor=white)
- ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=postgresql&logoColor=white)

## 주요 기능
```
1. Backend 프로젝트로의 진입점
2. 권한이 필요한 요청 제한
3. MSA 각각의 서비스로 요청 라우팅
4. JWT 세션을 확인하여 권한 확인
```

## CI/CD
```
.
├── .github
│   └── workflows
│       ├── ci-cd-dev.yml // dev 브랜치 전용 CI/CD
│       └── ci-cd-prd.yml // prd(main) 전용 CI/CD

1. Github Actions 실행
2. Docker 이미지 빌드
3. Private Docker Registry에 저장
4. flow-manifest 이미지 코드 수정
5. ArgoCD 최신화
```
