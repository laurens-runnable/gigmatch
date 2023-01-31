import { VacancyCreated } from "../avro/events";
import { Consumer } from "./index";
import { getLogger } from "@gigmatch/dashboard-shared/log";
import { VacancyDocument } from "@gigmatch/dashboard-shared/mongodb";

export default {
  typeName: VacancyCreated.name!,
  handler: async function ({ payload }, { database }) {
    getLogger().info("Inserting vacancy %s", payload.id);
    const document: VacancyDocument = {
      id: payload.id,
      name: payload.name,
      start: new Date(payload.start),
    };
    const coll = database.collections.vacancy<VacancyDocument>();
    await coll.insertOne(document);
  },
} as Consumer;
