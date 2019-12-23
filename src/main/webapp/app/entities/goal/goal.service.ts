import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IGoal } from 'app/shared/model/goal.model';

type EntityResponseType = HttpResponse<IGoal>;
type EntityArrayResponseType = HttpResponse<IGoal[]>;

@Injectable({ providedIn: 'root' })
export class GoalService {
  public resourceUrl = SERVER_API_URL + 'api/goals';

  constructor(protected http: HttpClient) {}

  create(goal: IGoal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(goal);
    return this.http
      .post<IGoal>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(goal: IGoal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(goal);
    return this.http
      .put<IGoal>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IGoal>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IGoal[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(goal: IGoal): IGoal {
    const copy: IGoal = Object.assign({}, goal, {
      created: goal.created && goal.created.isValid() ? goal.created.toJSON() : undefined,
      completed: goal.completed && goal.completed.isValid() ? goal.completed.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.created = res.body.created ? moment(res.body.created) : undefined;
      res.body.completed = res.body.completed ? moment(res.body.completed) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((goal: IGoal) => {
        goal.created = goal.created ? moment(goal.created) : undefined;
        goal.completed = goal.completed ? moment(goal.completed) : undefined;
      });
    }
    return res;
  }
}
