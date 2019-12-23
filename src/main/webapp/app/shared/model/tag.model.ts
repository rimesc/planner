import { IGoal } from 'app/shared/model/goal.model';

export interface ITag {
  id?: number;
  name?: string;
  icon?: string;
  themeId?: number;
  goals?: IGoal[];
}

export class Tag implements ITag {
  constructor(public id?: number, public name?: string, public icon?: string, public themeId?: number, public goals?: IGoal[]) {}
}
