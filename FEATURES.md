# Nore Lento Backend — API 기능 정리

> Base URL: `/api/v2`
> 인증: JWT Bearer Token (로그인 제외, `GET /api/v2/notice`, `GET /api/v2/notice/*`, `POST /api/v2/user` 제외)
> 권한 표기: 🔒 인증 필요 / 🛡️ ADMIN·MASTER 전용

---

## Auth (인증)

| Method | Path | 설명 |
|---|---|---|
| POST | `/api/v2/login` | 로그인 — JWT 토큰 발급 |

---

## User (회원)

| Method | Path | 권한 | 설명 |
|---|---|---|---|
| POST | `/api/v2/user` | 공개 | 회원 가입 |
| GET | `/api/v2/user` | 🔒 | 사용자 목록 조회 |
| GET | `/api/v2/user/{id}` | 🔒 | 사용자 상세 조회 |
| PUT | `/api/v2/user/{id}` | 🔒 | 사용자 정보 수정 |
| PATCH | `/api/v2/user/{id}/password` | 🔒 | 비밀번호 변경 |
| PATCH | `/api/v2/user/{id}/role` | 🛡️ | 권한 변경 (USER·ADMIN·MASTER) |
| PATCH | `/api/v2/user/{id}/grade` | 🛡️ | 등급 변경 (NORMAL·VIP·...) |
| DELETE | `/api/v2/user/{id}` | 🔒 | 회원 탈퇴 (soft delete) |

---

## Product (상품)

| Method | Path | 권한 | 설명 |
|---|---|---|---|
| GET | `/api/v2/product` | 🔒 | 상품 목록 조회 (카테고리 필터, 페이징) |
| GET | `/api/v2/product/{id}` | 🔒 | 상품 상세 조회 |
| POST | `/api/v2/product` | 🔒 | 상품 등록 |
| PUT | `/api/v2/product/{id}` | 🔒 | 상품 수정 |
| DELETE | `/api/v2/product/{id}` | 🔒 | 상품 삭제 (soft delete) |

### Stock (재고)

| Method | Path | 권한 | 설명 |
|---|---|---|---|
| GET | `/api/v2/product/{id}/stock` | 🔒 | 재고 조회 |
| PATCH | `/api/v2/product/{id}/stock` | 🛡️ | 재고 수정 |

### Product Category (카테고리)

| Method | Path | 권한 | 설명 |
|---|---|---|---|
| GET | `/api/v2/product/categories` | 🔒 | 카테고리 목록 조회 |
| POST | `/api/v2/product/categories` | 🛡️ | 카테고리 등록 |
| PUT | `/api/v2/product/categories/{id}` | 🛡️ | 카테고리 수정 |
| DELETE | `/api/v2/product/categories/{id}` | 🛡️ | 카테고리 삭제 (soft delete) |

---

## Cart (장바구니)

| Method | Path | 권한 | 설명 |
|---|---|---|---|
| GET | `/api/v2/cart` | 🔒 | 장바구니 조회 |
| POST | `/api/v2/cart/items` | 🔒 | 상품 추가 |
| PUT | `/api/v2/cart/items/{cartItemId}` | 🔒 | 수량 수정 |
| DELETE | `/api/v2/cart/items` | 🔒 | 선택 상품 제거 |
| DELETE | `/api/v2/cart` | 🔒 | 장바구니 비우기 |

---

## Shipping Address (배송지)

| Method | Path | 권한 | 설명 |
|---|---|---|---|
| GET | `/api/v2/shipping-addresses` | 🔒 | 배송지 목록 조회 |
| POST | `/api/v2/shipping-addresses` | 🔒 | 배송지 등록 |
| PUT | `/api/v2/shipping-addresses/{id}` | 🔒 | 배송지 수정 |
| PATCH | `/api/v2/shipping-addresses/{id}/default` | 🔒 | 기본 배송지 설정 |
| DELETE | `/api/v2/shipping-addresses/{id}` | 🔒 | 배송지 삭제 |

---

## Order (주문)

| Method | Path | 권한 | 설명 |
|---|---|---|---|
| GET | `/api/v2/orders` | 🔒 | 주문 목록 조회 |
| GET | `/api/v2/orders/{id}` | 🔒 | 주문 상세 조회 |
| POST | `/api/v2/orders` | 🔒 | 직접 주문 (포인트 사용 가능, 재고 자동 차감) |
| POST | `/api/v2/orders/from-cart` | 🔒 | 장바구니 주문 (포인트 사용 가능, 재고 자동 차감) |
| DELETE | `/api/v2/orders/{id}` | 🔒 | 주문 취소 (재고 복원, 포인트 환불) |

---

## Payment (결제)

| Method | Path | 권한 | 설명 |
|---|---|---|---|
| GET | `/api/v2/payments` | 🔒 | 결제 목록 조회 |
| GET | `/api/v2/payments/{id}` | 🔒 | 결제 상세 조회 |
| POST | `/api/v2/payments` | 🔒 | 결제 처리 (포인트 적립) |
| DELETE | `/api/v2/payments/{id}` | 🔒 | 환불 (재고 복원, 적립 포인트 회수, 사용 포인트 환불) |

---

## Delivery (배송)

| Method | Path | 권한 | 설명 |
|---|---|---|---|
| GET | `/api/v2/deliveries/{orderId}` | 🔒 | 배송 조회 |
| POST | `/api/v2/deliveries` | 🛡️ | 배송 등록 |
| PATCH | `/api/v2/deliveries/{id}/status` | 🛡️ | 배송 상태 변경 |
| PATCH | `/api/v2/deliveries/{id}/address` | 🔒 | 배송지 변경 |
| DELETE | `/api/v2/deliveries/{id}/return` | 🛡️ | 반품 처리 |

---

## Review (리뷰)

| Method | Path | 권한 | 설명 |
|---|---|---|---|
| GET | `/api/v2/reviews` | 🔒 | 리뷰 목록 조회 |
| GET | `/api/v2/reviews/eligible` | 🔒 | 리뷰 작성 가능 여부 확인 |
| POST | `/api/v2/reviews` | 🔒 | 리뷰 작성 |
| PUT | `/api/v2/reviews/{id}` | 🔒 | 리뷰 수정 |
| DELETE | `/api/v2/reviews/{id}` | 🔒 | 리뷰 삭제 (soft delete) |

---

## Notification (알림)

| Method | Path | 권한 | 설명 |
|---|---|---|---|
| GET | `/api/v2/notifications` | 🔒 | 알림 목록 조회 |
| GET | `/api/v2/notifications/unread` | 🔒 | 미읽음 알림 조회 |
| POST | `/api/v2/notifications` | 🛡️ | 알림 발송 |
| PUT | `/api/v2/notifications/{id}/read` | 🔒 | 알림 읽음 처리 |
| PATCH | `/api/v2/notifications/read-all` | 🔒 | 전체 읽음 처리 |
| DELETE | `/api/v2/notifications/{id}` | 🔒 | 알림 삭제 |

---

## Notice (공지사항)

| Method | Path | 권한 | 설명 |
|---|---|---|---|
| GET | `/api/v2/notice` | 공개 | 공지 목록 조회 |
| GET | `/api/v2/notice/{id}` | 공개 | 공지 상세 조회 |
| POST | `/api/v2/notice` | 🛡️ | 공지 등록 |
| PUT | `/api/v2/notice/{id}` | 🛡️ | 공지 수정 |
| DELETE | `/api/v2/notice/{id}` | 🛡️ | 공지 삭제 (soft delete) |
| POST | `/api/v2/notice/{id}/like` | 🔒 | 좋아요 |
| DELETE | `/api/v2/notice/{id}/like` | 🔒 | 좋아요 취소 |

---

## Point (포인트)

| Method | Path | 권한 | 설명 |
|---|---|---|---|
| GET | `/api/v2/points` | 🔒 | 포인트 잔액 조회 |
| GET | `/api/v2/points/history` | 🔒 | 포인트 변동 이력 조회 |

### 포인트 적립/차감 규칙

| 시점 | 타입 | 처리 |
|---|---|---|
| 결제 완료 | `EARN` | `total_price / 100` 적립 |
| 주문 생성 시 포인트 사용 | `USE` | 지정한 포인트 차감 (잔액 부족 시 예외) |
| 주문 취소 | `REFUND` | 사용한 포인트(`used_points`) 환불 |
| 결제 환불 | `REVOKE` | 적립된 포인트 회수 (잔액 초과 시 잔액 전액 차감) |
| 결제 환불 + 포인트 사용 이력 | `REFUND` | 사용한 포인트(`used_points`) 추가 환불 |

---

## Coupon (쿠폰)

| Method | Path | 권한 | 설명 |
|---|---|---|---|
| GET | `/api/v2/coupons` | 🔒 | 전체 쿠폰 목록 조회 |
| GET | `/api/v2/coupons/mine` | 🔒 | 내 쿠폰 목록 조회 |
| POST | `/api/v2/coupons` | 🛡️ | 쿠폰 발행 |
| PATCH | `/api/v2/coupons/{id}` | 🔒 | 쿠폰 수정 |
| DELETE | `/api/v2/coupons/{id}` | 🔒 | 쿠폰 삭제 |
| POST | `/api/v2/coupons/{couponId}/users/{userId}` | 🛡️ | 특정 회원에게 쿠폰 지급 |

---

## Wishlist (위시리스트)

| Method | Path | 권한 | 설명 |
|---|---|---|---|
| GET | `/api/v2/wishlists` | 🔒 | 위시리스트 조회 |
| POST | `/api/v2/wishlists` | 🔒 | 위시리스트 추가 |
| DELETE | `/api/v2/wishlists/{id}` | 🔒 | 위시리스트 제거 |

---

## 도메인별 엔드포인트 수 요약

| 도메인 | 엔드포인트 수 |
|---|---|
| Auth | 1 |
| User | 8 |
| Product | 5 |
| Stock | 2 |
| Product Category | 4 |
| Cart | 5 |
| Shipping Address | 5 |
| Order | 5 |
| Payment | 4 |
| Delivery | 5 |
| Review | 5 |
| Notification | 6 |
| Notice | 7 |
| Point | 2 |
| Coupon | 6 |
| Wishlist | 3 |
| **합계** | **73** |
