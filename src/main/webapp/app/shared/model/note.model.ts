import { IGoal } from './goal.model';

export interface INote {
  id?: number;
  markdown?: any;
  goal?: IGoal;
}

export class Note implements INote {
  constructor(public id?: number, public markdown?: any, public goal?: IGoal) {}
}
