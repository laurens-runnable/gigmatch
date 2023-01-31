export function toDateInt(date?: Date) {
  return date ? Math.round(date.getTime() / (3600 * 24 * 1000)) : undefined
}
