# Nore Lento Backend — ERD

> 모든 엔티티는 `BaseEntity`를 상속하며 `create_at / create_by / update_at / update_by` 컬럼을 공통으로 가집니다.

---

## 관계 Overview (한눈에 보기)

### 1. 상품 도메인

```mermaid
erDiagram
    MEMBER ||--o{ PRODUCT : "판매"
    PRODUCT_CATEGORY ||--o{ PRODUCT : "분류"
    PRODUCT ||--o| STOCK : "재고 (1:1)"
```

### 2. 장바구니 & 위시리스트

```mermaid
erDiagram
    MEMBER ||--o{ CART : "보유"
    CART ||--|{ CART_ITEM : "포함"
    PRODUCT ||--o{ CART_ITEM : "담김"

    MEMBER ||--o{ WISHLIST : "위시리스트"
    PRODUCT ||--o{ WISHLIST : "담김"
```

### 3. 주문 & 결제 & 배송

```mermaid
erDiagram
    MEMBER ||--o{ SHIPPING_ADDRESS : "배송지 관리"
    MEMBER ||--o{ ORDERS : "주문"
    SHIPPING_ADDRESS ||--o{ ORDERS : "배송지 참조"
    ORDERS ||--|{ ORDER_ITEM : "포함"
    PRODUCT ||--o{ ORDER_ITEM : "주문됨"

    ORDERS ||--o| PAYMENT : "결제 (1:1)"
    MEMBER ||--o{ PAYMENT : "결제자"

    ORDERS ||--o| DELIVERY : "배송 (1:1)"
```

### 4. 포인트 & 쿠폰

```mermaid
erDiagram
    MEMBER ||--o| USER_POINT : "잔액 (1:1)"
    MEMBER ||--o{ POINT_HISTORY : "변동 이력"

    MEMBER ||--o{ USER_COUPON : "보유"
    COUPON ||--o{ USER_COUPON : "발급"
```

### 5. 커뮤니티 & 알림

```mermaid
erDiagram
    MEMBER ||--o{ REVIEW : "작성"
    PRODUCT ||--o{ REVIEW : "리뷰"

    MEMBER ||--o{ NOTICE : "작성"
    NOTICE ||--o{ NOTICE_LIKE : "좋아요"
    MEMBER ||--o{ NOTICE_LIKE : "좋아요"

    MEMBER ||--o{ NOTIFICATION : "수신"
```

---

## 관계 이미지 (도메인별)

> 이미지 소스: [`docs/erd/`](docs/erd/)

### 1. 상품 도메인

![상품 도메인](docs/erd/01-product.png)

### 2. 장바구니 & 위시리스트

![장바구니 & 위시리스트](docs/erd/02-cart-wishlist.png)

### 3. 주문 & 결제 & 배송

![주문 & 결제 & 배송](docs/erd/03-order-payment-delivery.png)

### 4. 포인트 & 쿠폰

![포인트 & 쿠폰](docs/erd/04-point-coupon.png)

### 5. 커뮤니티 & 알림

![커뮤니티 & 알림](docs/erd/05-community.png)

---

## 상세 ERD (컬럼 포함)

```mermaid
erDiagram

    MEMBER {
        bigint      id           PK
        varchar(20) username     UK
        varchar     password
        varchar     name
        varchar     role              "USER | ADMIN | MASTER"
        varchar     grade             "NORMAL | VIP | ..."
        boolean     delete_check      "default false"
        datetime    create_at
        datetime    update_at
    }

    PRODUCT_CATEGORY {
        bigint   id           PK
        varchar  name
        boolean  delete_check
        datetime create_at
        datetime update_at
    }

    PRODUCT {
        bigint   id           PK
        bigint   user_id      FK
        bigint   category_id  FK
        varchar  name
        bigint   price
        text     description
        boolean  delete_check
        datetime create_at
        datetime update_at
    }

    STOCK {
        bigint  id         PK
        bigint  product_id FK  UK
        integer quantity
    }

    CART {
        bigint   id        PK
        bigint   user_id   FK
        datetime create_at
        datetime update_at
    }

    CART_ITEM {
        bigint   id         PK
        bigint   cart_id    FK
        bigint   product_id FK
        integer  quantity
        datetime create_at
        datetime update_at
    }

    SHIPPING_ADDRESS {
        bigint  id             PK
        bigint  user_id        FK
        varchar recipient_name
        varchar phone
        varchar address
        varchar detail_address
        varchar zip_code
        boolean is_default     "default false"
        datetime create_at
        datetime update_at
    }

    ORDERS {
        bigint   id                  PK
        bigint   user_id             FK
        bigint   shipping_address_id FK
        bigint   used_points
        varchar  status                   "PENDING | COMPLETED | CANCELLED"
        bigint   total_price
        boolean  delete_check
        datetime create_at
        datetime update_at
    }

    ORDER_ITEM {
        bigint   id         PK
        bigint   order_id   FK
        bigint   product_id FK
        integer  quantity
        bigint   price           "주문 시점 가격 스냅샷"
        datetime create_at
        datetime update_at
    }

    PAYMENT {
        bigint   id         PK
        bigint   order_id   FK
        bigint   user_id    FK
        varchar  method          "CARD | BANK_TRANSFER | KAKAO_PAY"
        varchar  status          "PENDING | COMPLETED | REFUNDED"
        bigint   amount
        datetime create_at
        datetime update_at
    }

    DELIVERY {
        bigint   id              PK
        bigint   order_id        FK  UK
        varchar  address
        varchar  tracking_number
        varchar  status               "PREPARING | SHIPPING | DELIVERED | RETURNED"
        datetime create_at
        datetime update_at
    }

    REVIEW {
        bigint   id         PK
        bigint   user_id    FK
        bigint   product_id FK
        text     content
        integer  rating
        boolean  delete_check
        datetime create_at
        datetime update_at
    }

    NOTIFICATION {
        bigint   id          PK
        bigint   user_id     FK
        varchar  title
        text     content
        boolean  is_read          "default false"
        boolean  delete_check     "default false"
        datetime create_at
        datetime update_at
    }

    NOTICE {
        bigint   id          PK
        bigint   user_id     FK
        varchar  title
        text     content
        varchar  category         "공지 유형"
        boolean  delete_check
        datetime create_at
        datetime update_at
    }

    NOTICE_LIKE {
        bigint   id        PK
        bigint   notice_id FK
        bigint   user_id   FK       "UK(notice_id, user_id)"
        datetime create_at
        datetime update_at
    }

    COUPON {
        bigint   id              PK
        varchar  name
        varchar  discount_type        "FIXED | PERCENTAGE"
        bigint   discount_value
        bigint   min_order_amount
        datetime expires_at
        datetime create_at
        datetime update_at
    }

    USER_COUPON {
        bigint   id        PK
        bigint   user_id   FK
        bigint   coupon_id FK
        boolean  is_used        "default false"
        datetime used_at
        datetime create_at
        datetime update_at
    }

    USER_POINT {
        bigint id         PK
        bigint user_id    FK  UK
        bigint balance         "default 0"
    }

    POINT_HISTORY {
        bigint   id       PK
        bigint   user_id  FK
        bigint   amount
        varchar  type          "EARN | USE | REFUND | REVOKE"
        datetime create_at
        datetime update_at
    }

    WISHLIST {
        bigint   id         PK
        bigint   user_id    FK
        bigint   product_id FK
        datetime create_at
        datetime update_at
    }

    %% ── Relationships ──────────────────────────────────────

    MEMBER           ||--o{ PRODUCT          : "판매 (user_id)"
    PRODUCT_CATEGORY ||--o{ PRODUCT          : "분류 (category_id)"
    PRODUCT          ||--o| STOCK            : "재고 1:1 (product_id UK)"

    MEMBER           ||--o{ CART             : "보유 (user_id)"
    CART             ||--|{ CART_ITEM        : "포함 (cart_id)"
    PRODUCT          ||--o{ CART_ITEM        : "담김 (product_id)"

    MEMBER           ||--o{ SHIPPING_ADDRESS : "배송지 (user_id)"
    MEMBER           ||--o{ ORDERS           : "주문 (user_id)"
    SHIPPING_ADDRESS ||--o{ ORDERS           : "주문배송지 (shipping_address_id)"
    ORDERS           ||--|{ ORDER_ITEM       : "포함 (order_id)"
    PRODUCT          ||--o{ ORDER_ITEM       : "주문됨 (product_id)"

    ORDERS           ||--o| PAYMENT          : "결제 1:1 (order_id)"
    MEMBER           ||--o{ PAYMENT          : "결제자 (user_id)"

    ORDERS           ||--o| DELIVERY         : "배송 1:1 (order_id unique)"

    MEMBER           ||--o{ REVIEW           : "작성 (user_id)"
    PRODUCT          ||--o{ REVIEW           : "리뷰 (product_id)"

    MEMBER           ||--o{ NOTIFICATION     : "수신 (user_id)"

    MEMBER           ||--o{ NOTICE           : "작성 (user_id)"
    NOTICE           ||--o{ NOTICE_LIKE      : "좋아요 (notice_id)"
    MEMBER           ||--o{ NOTICE_LIKE      : "좋아요 (user_id)"

    MEMBER           ||--o{ USER_COUPON      : "보유 (user_id)"
    COUPON           ||--o{ USER_COUPON      : "발급 (coupon_id)"

    MEMBER           ||--o| USER_POINT       : "포인트 1:1 (user_id UK)"
    MEMBER           ||--o{ POINT_HISTORY    : "이력 (user_id)"

    MEMBER           ||--o{ WISHLIST         : "위시리스트 (user_id)"
    PRODUCT          ||--o{ WISHLIST         : "위시리스트 (product_id)"
```

---

## 엔티티 요약

| 엔티티 | 테이블 | 설명 |
|---|---|---|
| `User` | `member` | 회원 정보. `role`(USER·ADMIN·MASTER), `grade`(등급) 보유 |
| `ProductCategory` | `product_category` | 상품 카테고리 |
| `Product` | `product` | 상품. 판매자(`user_id`)와 카테고리(`category_id`) 참조 |
| `Stock` | `stock` | 상품 재고. `product_id` 유니크 (1:1) |
| `Cart` | `cart` | 장바구니. 회원 1명당 1개 |
| `CartItem` | `cart_item` | 장바구니 상품 라인. `quantity` 보유 |
| `ShippingAddress` | `shipping_address` | 배송지. `is_default`로 기본 배송지 지정 |
| `Order` | `orders` | 주문. `status`(PENDING→COMPLETED·CANCELLED), `used_points` 사용 포인트 저장 |
| `OrderItem` | `order_item` | 주문 상품 라인. 주문 시점의 `price` 스냅샷 저장 |
| `Payment` | `payment` | 결제. `method`(CARD·BANK_TRANSFER·KAKAO_PAY), `status`(PENDING→COMPLETED→REFUNDED) |
| `Delivery` | `delivery` | 배송. 주문 1건당 1개(`order_id` unique). `status`(PREPARING→SHIPPING→DELIVERED→RETURNED) |
| `Review` | `review` | 상품 리뷰. `rating`(평점) |
| `Notification` | `notification` | 사용자 알림. `is_read` 읽음 여부 |
| `Notice` | `notice` | 공지사항. `category`(공지 유형) |
| `NoticeLike` | `notice_like` | 공지 좋아요. `(notice_id, user_id)` 복합 유니크 |
| `Coupon` | `coupon` | 쿠폰. `discount_type`(FIXED·PERCENTAGE), `expires_at` 만료일 |
| `UserCoupon` | `user_coupon` | 회원 보유 쿠폰. `is_used` 사용 여부, `used_at` 사용 시각 |
| `UserPoint` | `user_point` | 회원 포인트 잔액. `user_id` 유니크 (1:1) |
| `PointHistory` | `point_history` | 포인트 변동 이력. `type`(EARN·USE·REFUND·REVOKE) |
| `Wishlist` | `wishlist` | 위시리스트. 회원-상품 연결 |

---

## 주요 관계 정리

```
MEMBER ──< PRODUCT               (1:N) 판매자
PRODUCT_CATEGORY ──< PRODUCT     (1:N) 카테고리 분류
PRODUCT ──| STOCK                (1:1) 상품 재고

MEMBER ──< CART                  (1:N) 장바구니 보유
CART ──< CART_ITEM               (1:N) 장바구니 항목
CART_ITEM >── PRODUCT            (N:1) 상품 참조

MEMBER ──< SHIPPING_ADDRESS      (1:N) 배송지 관리
MEMBER ──< ORDERS                (1:N) 주문
SHIPPING_ADDRESS ──< ORDERS      (1:N) 주문 배송지 참조
ORDERS ──< ORDER_ITEM            (1:N) 주문 항목
ORDER_ITEM >── PRODUCT           (N:1) 주문 시 상품 참조

ORDERS ──| PAYMENT               (1:1) 주문당 결제 1건
ORDERS ──| DELIVERY              (1:1) 주문당 배송 1건

MEMBER ──< REVIEW                (1:N) 리뷰 작성
PRODUCT ──< REVIEW               (1:N) 상품 리뷰

MEMBER ──< NOTIFICATION          (1:N) 알림 수신
MEMBER ──< NOTICE                (1:N) 공지 작성
NOTICE ──< NOTICE_LIKE           (1:N) 공지 좋아요
MEMBER ──< NOTICE_LIKE           (1:N) 좋아요한 공지

MEMBER ──< USER_COUPON           (1:N) 쿠폰 보유
COUPON ──< USER_COUPON           (1:N) 쿠폰 발급

MEMBER ──| USER_POINT            (1:1) 포인트 잔액
MEMBER ──< POINT_HISTORY         (1:N) 포인트 변동 이력

MEMBER ──< WISHLIST              (1:N) 위시리스트
PRODUCT ──< WISHLIST             (1:N) 위시리스트에 담김
```

---

## 상태 전이 요약

| 엔티티 | 상태 전이 |
|---|---|
| `Order` | `PENDING` → `COMPLETED` (결제 완료) · `CANCELLED` (취소/환불) |
| `Payment` | `PENDING` → `COMPLETED` → `REFUNDED` |
| `Delivery` | `PREPARING` → `SHIPPING` → `DELIVERED` → `RETURNED` |
| `PointHistory.type` | `EARN` (주문 완료 적립) · `USE` (주문 시 차감) · `REFUND` (주문 취소 환불) · `REVOKE` (결제 환불 시 적립분 회수) |
