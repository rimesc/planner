import { Moment } from 'moment';
import { ITag } from 'app/shared/model/tag.model';
import { IGoal } from 'app/shared/model/goal.model';

export const enum Visibility {
    PUBLIC = 'PUBLIC',
    PRIVATE = 'PRIVATE'
}

export interface ITheme {
    id?: number;
    name?: string;
    description?: string;
    avatarContentType?: string;
    avatar?: any;
    createdAt?: Moment;
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
        public avatarContentType?: string,
        public avatar?: any,
        public createdAt?: Moment,
        public visibility?: Visibility,
        public tags?: ITag[],
        public goals?: IGoal[],
        public ownerId?: number
    ) {}
}
