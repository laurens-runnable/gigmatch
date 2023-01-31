import avroEvents from "./avro/events";
import { Context, MessageHandlerRegistry } from "./consumers";
import { Database } from "@gigmatch/dashboard-shared/mongodb";
import { getLogger } from "@gigmatch/dashboard-shared/log";
import { Channel } from "amqplib";
import { ConsumeMessage } from "amqplib/properties";

type Services = {
  amqp: {
    channel: Channel;
    queue: string;
  };
  mongo: {
    database: Database;
  };
};

export async function consumeEvents(
  services: Services,
  handlerRegistry: MessageHandlerRegistry
) {
  const {
    amqp: { channel, queue },
    mongo: { database },
  } = services;

  const context: Context = { database };

  await channel.consume(
    queue,
    (message: ConsumeMessage | null) => {
      const buffer = message?.content;
      if (!buffer) {
        return;
      }
      const typeName = message?.properties.headers["gm.type"] as string;
      const eventType = avroEvents[typeName];
      const handler = handlerRegistry[typeName];
      if (eventType && handler) {
        const payload = eventType.fromBuffer(buffer);
        handler({ typeName, payload }, context);
      } else {
        getLogger().warn(`Unhandled event type: ${typeName}`);
      }
    },
    { noAck: true }
  );
}
