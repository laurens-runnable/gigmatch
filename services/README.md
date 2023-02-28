# Services

| Component                                               | Description                  | Implementation        | Storage       |
|---------------------------------------------------------|------------------------------|-----------------------|---------------|
| [match-service](./match-service)                        | Match Service                | Spring Boot, Axon     | PostgreSQL    |
| [dashboard](./dashboard/app)                            | Dashboard                    | Nuxt, Vue.js, GraphQL | MongoDB       |
| [dashboard-consumer](./dashboard/consumer)              | Event consumer for dashboard | Node.js               |               |
| [website](./website/Gigmatch.Website.Mvc)               | Website                      | ASP.NET Core MVC      | Elasticsearch |
| [website-consumer](./website/Gigmatch.Website.Consumer) | Event consumer for website   | .NET                  |               |
| [e2e-tests](./e2e-tests)                                | End-to-end tests             | Playwright            |               |

## Avro schemas

Services exchange commands and events in [Apache Avro](https://avro.apache.org/) format.

Each service maintains its own version of a given schema. Schemas can evolve independently, but must remain compatible.

* [match-service](./match-service/framework/src/main/avro) commands and events
* [dashboard-consumer](dashboard/consumer/src/application/events) events
* [dashboard](./dashboard/app/server/avro/commands) commands
* [website-consumer](website/Gigmatch.Website.Consumer/avro/events) events

For the sake of simplicity command and events are kept as 'flat' as possible and don't use any nested types. In general,
Avro types are intended to be self-contained and used mainly for serialization. I.e. data transfer or storage. 
