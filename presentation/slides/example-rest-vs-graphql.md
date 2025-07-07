# Example: REST vs GraphQL

**REST:**
```http
GET /users/42
GET /users/42/posts
```

**GraphQL:**
```graphql
query {
  user(id: 42) {
    name
    posts {
      title
    }
  }
}
``` 