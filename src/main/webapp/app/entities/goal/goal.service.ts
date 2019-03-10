import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
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

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(goal: IGoal): IGoal {
        const copy: IGoal = Object.assign({}, goal, {
            createdAt: goal.createdAt != null && goal.createdAt.isValid() ? goal.createdAt.toJSON() : null,
            completedAt: goal.completedAt != null && goal.completedAt.isValid() ? goal.completedAt.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.createdAt = res.body.createdAt != null ? moment(res.body.createdAt) : null;
            res.body.completedAt = res.body.completedAt != null ? moment(res.body.completedAt) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((goal: IGoal) => {
                goal.createdAt = goal.createdAt != null ? moment(goal.createdAt) : null;
                goal.completedAt = goal.completedAt != null ? moment(goal.completedAt) : null;
            });
        }
        return res;
    }
}
