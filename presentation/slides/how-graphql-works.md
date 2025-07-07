# How does GraphQL work? 🤔

1. **Client**: Sends a precise query describing the needed data.
2. **Server**: Validates the query against its schema and uses resolvers to gather the data from various sources.
3. **Execution**: The server assembles the data according to the query structure.
4. **Response**: Returns exactly the requested data in a single JSON response—no more, no less.