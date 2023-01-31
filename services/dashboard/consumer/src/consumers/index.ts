import SkillCreatedOrUpdated from "./SkillCreatedOrUpdated";
import SkillDeleted from "./SkillDeleted";
import VacanciesReset from "./VacanciesReset";
import VacancyCreated from "./VacancyCreated";
import { Database } from "@gigmatch/dashboard-shared/mongodb";

export type Message = {
  typeName: string;
  payload: any;
};

export type Context = {
  database: Database;
};

type MessageHandler = (message: Message, context: Context) => Promise<void>;

export type Consumer = {
  readonly typeName: string;
  readonly handler: MessageHandler;
};

export type MessageHandlerRegistry = {
  [key: string]: MessageHandler;
};

export const matchEventHandlers: MessageHandlerRegistry = {
  [VacanciesReset.typeName]: VacanciesReset.handler,
  [VacancyCreated.typeName]: VacancyCreated.handler,
  [SkillDeleted.typeName]: SkillDeleted.handler,
  [SkillCreatedOrUpdated.typeName]: SkillCreatedOrUpdated.handler,
};
