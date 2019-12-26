export interface INote {
  id?: number;
  markdown?: any;
  goalId?: number;
}

export class Note implements INote {
  constructor(public id?: number, public markdown?: any, public goalId?: number) {}
}
