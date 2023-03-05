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

Each service maintains its own version of a given schema.

* [match-service](./match-service/framework/src/main/avro) commands and events
* [dashboard-consumer](dashboard/consumer/src/application/events) events
* [dashboard](./dashboard/app/server/avro/commands) commands
* [website-consumer](website/Gigmatch.Website.Consumer/avro/events) events

For the sake of simplicity command and events are kept as 'flat' as possible and don't use any nested types. In general,
Avro types are intended to be self-contained and used mainly for serialization. I.e. data transfer or storage.

### Type differences

There may be slight differences between schemas used by consumers, depending on the level of support in the respective
Avro library.

For example, the [VacancyCreated event](./match-service/framework/src/main/avro/events/VacancyCreated.avsc) defines
a `skills` property with a logical type of `uuid`:

```json
{
  "name": "skills",
  "type": {
    "type": "array",
    "items": {
      "type": "string",
      "logicalType": "uuid"
    }
  }
}
```

The property of the corresponding generated Java code is of type `java.util.List<java.util.UUID>`.

`AvroConvert` (.NET) does not seem to support logical types for arrays, at least not for code generation. To solve this,
the [VacancyCreated event](./website/Gigmatch.Website.Consumer/avro/events/VacancyCreated.avsc) used by website-consumer
defines this property as a `string` instead:

```json
{
  "name": "skills",
  "type": {
    "type": "array",
    "items": "string"
  }
}
```
