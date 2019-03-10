import { Moment } from 'moment';

export interface ITask {
    id?: number;
    summary?: string;
    createdAt?: Moment;
    completedAt?: Moment;
    ownerId?: number;
    goalId?: number;
}

export class Task implements ITask {
    constructor(
        public id?: number,
        public summary?: string,
        public createdAt?: Moment,
        public completedAt?: Moment,
        public ownerId?: number,
        public goalId?: number
    ) {}
}
