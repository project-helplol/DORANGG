






```tree
src/
└── main/
└── java/
└── com/example/project/
└── domain/
└── riot/
├── controller/
│   ├── MatchController.java              // /api/match/**
│   ├── DashboardController.java          // /api/riot/dashboard
│   └── AiCommentController.java          // /api/ai/daily-briefing
│
├── service/
│   ├── MatchService.java
│   ├── DashboardService.java
│   └── AiCommentService.java
│
├── repository/
│   ├── MatchRepository.java
│   ├── MatchStatSummaryRepository.java
│   └── AiCommentRepository.java
│
├── dto/
│   ├── request/
│   │   ├── DailyBriefingRequest.java
│   │   └── MatchSummaryRequest.java
│   │
│   └── response/
│       ├── DailyBriefingResponse.java
│       ├── MatchResponse.java
│       └── MatchSummaryResponse.java
│
├── entity/
│   ├── Match.java
│   ├── MatchStatSummary.java
│   └── AiComment.java
│
└── exception/
└── RiotDataNotFoundException.java
