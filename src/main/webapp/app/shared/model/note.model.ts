import { Moment } from 'moment';

export const enum Visibility {
    PUBLIC = 'PUBLIC',
    PRIVATE = 'PRIVATE'
}

export interface INote {
    id?: number;
    markdown?: any;
    html?: any;
    created?: Moment;
    edited?: Moment;
    visibility?: Visibility;
    ownerId?: number;
    goalId?: number;
}

export class Note implements INote {
    constructor(
        public id?: number,
        public markdown?: any,
        public html?: any,
        public created?: Moment,
        public edited?: Moment,
        public visibility?: Visibility,
        public ownerId?: number,
        public goalId?: number
    ) {}
}
