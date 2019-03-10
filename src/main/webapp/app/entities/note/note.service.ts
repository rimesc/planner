import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { INote } from 'app/shared/model/note.model';

type EntityResponseType = HttpResponse<INote>;
type EntityArrayResponseType = HttpResponse<INote[]>;

@Injectable({ providedIn: 'root' })
export class NoteService {
    public resourceUrl = SERVER_API_URL + 'api/notes';

    constructor(protected http: HttpClient) {}

    create(note: INote): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(note);
        return this.http
            .post<INote>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(note: INote): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(note);
        return this.http
            .put<INote>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<INote>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<INote[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(note: INote): INote {
        const copy: INote = Object.assign({}, note, {
            createdAt: note.createdAt != null && note.createdAt.isValid() ? note.createdAt.toJSON() : null,
            editedAt: note.editedAt != null && note.editedAt.isValid() ? note.editedAt.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.createdAt = res.body.createdAt != null ? moment(res.body.createdAt) : null;
            res.body.editedAt = res.body.editedAt != null ? moment(res.body.editedAt) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((note: INote) => {
                note.createdAt = note.createdAt != null ? moment(note.createdAt) : null;
                note.editedAt = note.editedAt != null ? moment(note.editedAt) : null;
            });
        }
        return res;
    }
}
