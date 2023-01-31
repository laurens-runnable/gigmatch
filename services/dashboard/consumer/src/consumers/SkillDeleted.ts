import { SkillDeleted } from "../avro/events";
import { Consumer } from "./index";
import { getLogger } from "@gigmatch/dashboard-shared/log";

export default {
  typeName: SkillDeleted.name!,
  handler: async function ({ payload }, { database }) {
    getLogger().info("Deleting skill %s", payload.id);
    const coll = database.collections.skill();
    await coll.deleteOne({ id: payload.id });
  },
} as Consumer;
