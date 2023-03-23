import { type Config, createApplication } from './application'
import yargs, { type Argv } from 'yargs'

const application = createApplication()

function createConfig(args: any): Config {
  return {
    baseUrl: args.baseUrl,
    admin: {
      username: 'admin1',
      password: 'admin1',
    },
    recruiter: {
      username: 'recruiter:1',
      password: 'recruiter:1',
    },
  }
}

void yargs
  .command(
    'generate-vacancies',
    'Generate Vacancies',
    (yargs: Argv) =>
      yargs
        .option('base-url', { default: 'http://localhost:8080' })
        .option('amount', { default: 10 }),
    async (args: any) => {
      const config = createConfig(args)
      await application.init(config)
      await application.generateVacancies(args.amount)
    }
  )
  .demandCommand(1, 'Command is required')
  .help().argv
