import { Moment } from 'moment';

export interface ITask {
  id?: number;
  summary?: string;
  completedAt?: Moment;
  goalId?: number;
}

export class Task implements ITask {
  constructor(public id?: number, public summary?: string, public completedAt?: Moment, public goalId?: number) {}
}
