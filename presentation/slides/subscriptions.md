# Subscriptions 📣

- Subscriptions = real-time updates via WebSockets (not just queries & mutations!)
- Great for live data: chats, notifications, dashboards, etc.
- Client subscribes to a data stream, server pushes updates as they happen

```graphql
subscription OnMessage {
  messageAdded {
    id
    content
    author
  }
}
```