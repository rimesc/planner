import { Moment } from 'moment';
import { Visibility } from 'app/shared/model/enumerations/visibility.model';

export interface INote {
  id?: number;
  markdown?: any;
  html?: any;
  createdAt?: Moment;
  editedAt?: Moment;
  visibility?: Visibility;
  ownerId?: number;
  goalId?: number;
}

export class Note implements INote {
  constructor(
    public id?: number,
    public markdown?: any,
    public html?: any,
    public createdAt?: Moment,
    public editedAt?: Moment,
    public visibility?: Visibility,
    public ownerId?: number,
    public goalId?: number
  ) {}
}
