import {
  type APIRequestContext,
  type FullConfig,
  chromium,
  expect,
} from '@playwright/test'
import { promises as fs } from 'fs'
import path from 'path'
import { fetchAdminAccessToken } from './admin'

async function populateReferenceData(
  request: APIRequestContext,
  baseURL: string,
  accessToken: string
): Promise<void> {
  const csv = await fs.readFile(path.join(__dirname, './skills.csv'))
  const data = csv.toString()
  const populateSkills = await request.post(
    `${baseURL}/matches/api/v1/reference-data/skills`,
    {
      headers: {
        'Content-Type': 'text/csv',
        Authorization: `Bearer ${accessToken}`,
      },
      data,
    }
  )
  expect(populateSkills.status()).toBe(204)
}

export default async function (config: FullConfig): Promise<void> {
  const browser = await chromium.launch()
  const page = await browser.newPage()
  const { request } = page

  const baseURL = config.projects[0].use.baseURL as string

  const accessToken = await fetchAdminAccessToken(baseURL)
  await populateReferenceData(request, baseURL, accessToken)

  await browser.close()
}
