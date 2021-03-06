import { Moment } from 'moment';
import { ITask } from 'app/shared/model/task.model';
import { INote } from 'app/shared/model/note.model';
import { ITag } from 'app/shared/model/tag.model';
import { ITheme } from 'app/shared/model/theme.model';

export interface IGoal {
  id?: number;
  summary?: string;
  order?: number;
  completedAt?: Moment;
  tasks?: ITask[];
  notes?: INote[];
  tags?: ITag[];
  theme?: ITheme;
}

export class Goal implements IGoal {
  constructor(
    public id?: number,
    public summary?: string,
    public order?: number,
    public completedAt?: Moment,
    public tasks?: ITask[],
    public notes?: INote[],
    public tags?: ITag[],
    public theme?: ITheme
  ) {}
}
