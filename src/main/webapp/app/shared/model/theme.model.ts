import { Moment } from 'moment';
import { ITag } from 'app/shared/model/tag.model';
import { IGoal } from 'app/shared/model/goal.model';
import { Visibility } from 'app/shared/model/enumerations/visibility.model';

export interface ITheme {
  id?: number;
  name?: string;
  description?: string;
  shortName?: string;
  avatarContentType?: string;
  avatar?: any;
  created?: Moment;
  visibility?: Visibility;
  tags?: ITag[];
  goals?: IGoal[];
  ownerId?: number;
}

export class Theme implements ITheme {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public shortName?: string,
    public avatarContentType?: string,
    public avatar?: any,
    public created?: Moment,
    public visibility?: Visibility,
    public tags?: ITag[],
    public goals?: IGoal[],
    public ownerId?: number
  ) {}
}
