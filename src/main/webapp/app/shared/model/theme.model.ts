import { ITag } from 'app/shared/model/tag.model';
import { IGoal } from 'app/shared/model/goal.model';

export interface ITheme {
  id?: number;
  name?: string;
  description?: string;
  avatarContentType?: string;
  avatar?: any;
  tags?: ITag[];
  goals?: IGoal[];
}

export class Theme implements ITheme {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public avatarContentType?: string,
    public avatar?: any,
    public tags?: ITag[],
    public goals?: IGoal[]
  ) {}
}
