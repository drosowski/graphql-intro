# Anatomy of a GraphQL Query 🩻

```graphql
query GetUser {
  user(id: 42) {
    name
    email
    posts {
      title
      publishedAt
    }
  }
}
```

- You choose the fields!
- Nested queries? No problem! 