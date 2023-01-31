import { SkillCreatedOrUpdated } from "../avro/events";
import { Consumer } from "./index";
import { getLogger } from "@gigmatch/dashboard-shared/log";
import { SkillDocument } from "@gigmatch/dashboard-shared/mongodb";

export default {
  typeName: SkillCreatedOrUpdated.name!,
  handler: async function ({ payload }, { database }) {
    getLogger().info("Updating skill %s", payload.id);

    const document: SkillDocument = {
      id: payload.id,
      name: payload.name,
      slug: payload.slug,
    };
    const coll = database.collections.skill<SkillDocument>();
    await coll.findOneAndUpdate(
      { id: document.id },
      { $set: document },
      { upsert: true }
    );
  },
} as Consumer;
