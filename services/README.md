# Services

| Component                                  | Description                  | Implementation        | Storage       |
|--------------------------------------------|------------------------------|-----------------------|---------------|
| [match-service](./match-service)           | Match Service                | Spring Boot, Axon     | PostgreSQL    |
| [dashboard](./dashboard/app)               | Dashboard                    | Nuxt, Vue.js, GraphQL | MongoDB       |
| [dashboard-consumer](./dashboard/consumer) | Event consumer for dashboard | Node.js               |               |
| [website](./website)                       | Website                      | ASP.NET Core MVC      | Elasticsearch |
| website-consumer                           | Event consumer for website   | .NET                  |               |
| [e2e-tests](./e2e-tests)                   | End-to-end tests             | Playwright            |               |

## Avro schemas

Services exchange commands and events using [Apache Avro](https://avro.apache.org/).

Each service maintains its own version of a given schema. Schemas can evolve independently, but must remain compatible.

* [match-service](./match-service/framework/src/main/avro) commands and events
* [dashboard-consumer](dashboard/consumer/src/application/avro) events
* [dashboard](./dashboard/app/server/avro/commands) commands
