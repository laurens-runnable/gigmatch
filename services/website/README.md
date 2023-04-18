# Website

```bash
# Make sure 'website-shared' artifact can be found 
mvn clean install

# Start consumer
cd consumer
mvn quarkus:dev

# Start server
cd server
mvn quarkus:dev
```
