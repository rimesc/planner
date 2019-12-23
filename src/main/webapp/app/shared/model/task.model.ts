import { Moment } from 'moment';

export interface ITask {
  id?: number;
  summary?: string;
  created?: Moment;
  completed?: Moment;
  ownerId?: number;
  goalId?: number;
}

export class Task implements ITask {
  constructor(
    public id?: number,
    public summary?: string,
    public created?: Moment,
    public completed?: Moment,
    public ownerId?: number,
    public goalId?: number
  ) {}
}
