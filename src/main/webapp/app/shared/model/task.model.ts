import { Moment } from 'moment';
import { IGoal } from './goal.model';

export interface ITask {
  id?: number;
  summary?: string;
  completedAt?: Moment;
  goal?: IGoal;
}

export class Task implements ITask {
  constructor(public id?: number, public summary?: string, public completedAt?: Moment, public goal?: IGoal) {}
}
