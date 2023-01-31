import config from "./config";
import { matchEventHandlers } from "./consumers";
import { consumeEvents } from "./events";
import { register as registerEureka } from "./infrastructure/eureka";
import { openMessageQueue } from "@gigmatch/dashboard-shared/amqp";
import { getLogger, setLogger } from "@gigmatch/dashboard-shared/log";
import { openMongoDatabase } from "@gigmatch/dashboard-shared/mongodb";
import express from "express";
import actuator from "express-actuator";
import winston, { format } from "winston";

const app = express();
app.use(actuator());

setLogger(
  winston.createLogger({
    transports: [new winston.transports.Console()],
    format: format.combine(format.splat(), format.simple()),
  })
);

type ExitHandler = () => Promise<void>;
const exitHandlers: ExitHandler[] = [];

async function initEureka() {
  if (!config.eureka.enabled) {
    return;
  }
  const { unregister } = await registerEureka(config.eureka);
  exitHandlers.push(unregister);
}

const server = app.listen(config.actuator.port, async () => {
  getLogger().info(`Actuator listening on port ${config.actuator.port}`);

  await initEureka();
  const messageQueue = await openMessageQueue();

  const database = await openMongoDatabase();
  await database.collections.initialize();
  exitHandlers.push(() => database.close());

  await consumeEvents(
    {
      amqp: { channel: messageQueue.channel, queue: messageQueue.queue },
      mongo: { database },
    },
    matchEventHandlers
  );
});

process.on("SIGTERM", async () => {
  await Promise.all(exitHandlers.map((exitHandler) => exitHandler()));

  getLogger().info("Closing HTTP server");
  server.close(() => {
    process.exit();
  });
});
