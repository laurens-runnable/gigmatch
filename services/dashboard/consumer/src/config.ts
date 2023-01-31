const process = require("process");

export type EurekaConfig = {
  readonly enabled: boolean;
  readonly instance: {
    readonly app: string;
    readonly port: number;
  };
  readonly eureka: {
    readonly host: string;
    readonly port: number;
    readonly servicePath: "/eureka/apps/";
  };
};

export type Config = {
  readonly eureka: EurekaConfig;
  readonly actuator: {
    readonly port: number;
  };
};

const port = process.env["GIGMATCH_ACTUATOR_PORT"] ?? 3001;
const config: Config = {
  eureka: {
    enabled: (process.env["GIGMATCH_EUREKA_ENABLED"] ?? "false") == "true",
    instance: {
      app: "admin-consumer",
      port,
    },
    eureka: {
      host: process.env["GIGMATCH_EUREKA_HOST"] ?? "localhost",
      port: process.env["GIGMATCH_EUREKA_PORT"] ?? 8070,
      servicePath:
        process.env["GIGMATCH_EUREKA_SERVICE_PATH"] ?? "/eureka/apps/",
    },
  },
  actuator: {
    port,
  },
};

export default config;
