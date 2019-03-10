import { Moment } from 'moment';
import { ITask } from 'app/shared/model/task.model';
import { INote } from 'app/shared/model/note.model';
import { ITag } from 'app/shared/model/tag.model';

export const enum Visibility {
    PUBLIC = 'PUBLIC',
    PRIVATE = 'PRIVATE'
}

export interface IGoal {
    id?: number;
    summary?: string;
    createdAt?: Moment;
    completedAt?: Moment;
    order?: number;
    visibility?: Visibility;
    tasks?: ITask[];
    notes?: INote[];
    ownerId?: number;
    tags?: ITag[];
    themeId?: number;
}

export class Goal implements IGoal {
    constructor(
        public id?: number,
        public summary?: string,
        public createdAt?: Moment,
        public completedAt?: Moment,
        public order?: number,
        public visibility?: Visibility,
        public tasks?: ITask[],
        public notes?: INote[],
        public ownerId?: number,
        public tags?: ITag[],
        public themeId?: number
    ) {}
}
