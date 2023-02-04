export type Event = {
  type: string
  payload: any
}

export interface EventHandler {
  handleEvent(event: Event): Promise<void>
}
