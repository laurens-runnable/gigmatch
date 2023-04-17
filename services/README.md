# Services

| Component                                  | Description                  | Implementation        | Storage       |
|--------------------------------------------|------------------------------|-----------------------|---------------|
| [match-service](./match-service)           | Match Service                | Spring Boot, Axon     | PostgreSQL    |
| [dashboard](./dashboard/app)               | Dashboard                    | Nuxt, Vue.js, GraphQL | MongoDB       |
| [dashboard-consumer](./dashboard/consumer) | Event consumer for dashboard | Node.js               |               |
| [website](./website/server)                | Website                      | Quarkus Qute          | Elasticsearch |
| [website-consumer](./website/consumer)     | Event consumer for website   | Quarkus               |               |
| [e2e-tests](./e2e-tests)                   | End-to-end tests             | Playwright            |               |

## Avro schemas

Services exchange commands and events in [Apache Avro](https://avro.apache.org/) format.

Each service maintains its own version of a given schema.

* [match-service](./match-service/framework/src/main/avro) commands and events
* [dashboard-consumer](dashboard/consumer/src/application/events) events
* [dashboard](./dashboard/app/server/avro/commands) commands
* [website-consumer](website/consumer/src/main/avro/events) events
