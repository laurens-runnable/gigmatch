import { VacanciesReset } from "../avro/events";
import { Consumer } from "./index";
import { getLogger } from "@gigmatch/dashboard-shared/log";
export default {
  typeName: VacanciesReset.name!,
  handler: async function ({ payload }, { database}) {
    getLogger().info("Resetting vacancies");
    const coll = database.collections.vacancy();
    await coll.deleteMany({});
  },
} as Consumer;
