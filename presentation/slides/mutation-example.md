# Mutation Example

```graphql
mutation AddMessage {
  addMessage(content: "Hello, world!", author: "Alice") {
    id
    content
    author
  }
}
```

- You specify what to change and what to get back
- No more guessing what the server returns! 