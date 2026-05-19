# Nore Lento Backend — ERD

> 모든 엔티티는 `BaseEntity`를 상속하며 `create_at / create_by / update_at / update_by` 컬럼을 공통으로 가집니다.

```mermaid
erDiagram

    MEMBER {
        bigint      id           PK
        varchar(20) username     UK
        varchar     password
        varchar     name
        varchar     role              "USER | ADMIN | MASTER"
        varchar     grade             "NORMAL | ..."
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

    ORDERS {
        bigint   id          PK
        bigint   user_id     FK
        varchar  status           "PENDING | COMPLETED | CANCELLED"
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

    %% ── Relationships ──────────────────────────────────────

    MEMBER           ||--o{ PRODUCT      : "판매 (user_id)"
    PRODUCT_CATEGORY ||--o{ PRODUCT      : "분류 (category_id)"

    MEMBER           ||--o{ CART         : "보유 (user_id)"
    CART             ||--|{ CART_ITEM    : "포함 (cart_id)"
    PRODUCT          ||--o{ CART_ITEM    : "담김 (product_id)"

    MEMBER           ||--o{ ORDERS       : "주문 (user_id)"
    ORDERS           ||--|{ ORDER_ITEM   : "포함 (order_id)"
    PRODUCT          ||--o{ ORDER_ITEM   : "주문됨 (product_id)"

    ORDERS           ||--o| PAYMENT      : "결제 1:1 (order_id)"
    MEMBER           ||--o{ PAYMENT      : "결제자 (user_id)"

    ORDERS           ||--o| DELIVERY     : "배송 1:1 (order_id unique)"

    MEMBER           ||--o{ REVIEW       : "작성 (user_id)"
    PRODUCT          ||--o{ REVIEW       : "리뷰 (product_id)"

    MEMBER           ||--o{ NOTIFICATION : "수신 (user_id)"

    MEMBER           ||--o{ NOTICE       : "작성 (user_id)"
    NOTICE           ||--o{ NOTICE_LIKE  : "좋아요 (notice_id)"
    MEMBER           ||--o{ NOTICE_LIKE  : "좋아요 (user_id)"
```

---

## 엔티티 요약

| 엔티티 | 테이블 | 설명 |
|---|---|---|
| `User` | `member` | 회원 정보. `role`(USER·ADMIN·MASTER), `grade`(등급) 보유 |
| `ProductCategory` | `product_category` | 상품 카테고리 |
| `Product` | `product` | 상품. 판매자(`user_id`)와 카테고리(`category_id`) 참조 |
| `Cart` | `cart` | 장바구니. 회원 1명당 1개 |
| `CartItem` | `cart_item` | 장바구니 상품 라인. `quantity` 보유 |
| `Order` | `orders` | 주문. `status`(PENDING→COMPLETED·CANCELLED), `total_price` |
| `OrderItem` | `order_item` | 주문 상품 라인. 주문 시점의 `price` 스냅샷 저장 |
| `Payment` | `payment` | 결제. `method`(CARD·BANK_TRANSFER·KAKAO_PAY), `status`(PENDING→COMPLETED→REFUNDED) |
| `Delivery` | `delivery` | 배송. 주문 1건당 1개(`order_id` unique). `status`(PREPARING→SHIPPING→DELIVERED→RETURNED) |
| `Review` | `review` | 상품 리뷰. `rating`(평점) |
| `Notification` | `notification` | 사용자 알림. `is_read` 읽음 여부 |
| `Notice` | `notice` | 공지사항. `category`(공지 유형) |
| `NoticeLike` | `notice_like` | 공지 좋아요. `(notice_id, user_id)` 복합 유니크 |

---

## 주요 관계 정리

```
MEMBER ──< PRODUCT         (1:N) 판매자
PRODUCT_CATEGORY ──< PRODUCT (1:N) 카테고리 분류

MEMBER ──< CART            (1:N) 장바구니 보유
CART ──< CART_ITEM         (1:N) 장바구니 항목
CART_ITEM >── PRODUCT      (N:1) 상품 참조

MEMBER ──< ORDERS          (1:N) 주문
ORDERS ──< ORDER_ITEM      (1:N) 주문 항목
ORDER_ITEM >── PRODUCT     (N:1) 주문 시 상품 참조

ORDERS ──| PAYMENT         (1:1) 주문당 결제 1건
ORDERS ──| DELIVERY        (1:1) 주문당 배송 1건

MEMBER ──< REVIEW          (1:N) 리뷰 작성
PRODUCT ──< REVIEW         (1:N) 상품 리뷰

MEMBER ──< NOTIFICATION    (1:N) 알림 수신
MEMBER ──< NOTICE          (1:N) 공지 작성
NOTICE ──< NOTICE_LIKE     (1:N) 공지 좋아요
MEMBER ──< NOTICE_LIKE     (1:N) 좋아요한 공지
```

---

## 상태 전이 요약

| 엔티티 | 상태 전이 |
|---|---|
| `Order` | `PENDING` → `COMPLETED` (결제 완료) · `CANCELLED` (취소/환불) |
| `Payment` | `PENDING` → `COMPLETED` → `REFUNDED` |
| `Delivery` | `PREPARING` → `SHIPPING` → `DELIVERED` → `RETURNED` |
