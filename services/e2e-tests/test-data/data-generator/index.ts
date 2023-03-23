import 'reflect-metadata'

export interface DataGenerator {
  generateVacancies: (amount: number) => Promise<void>

  init: (baseUrl: string) => Promise<void>
}

export const DATA_GENERATOR_TYPE = Symbol.for('DataGenerator')
