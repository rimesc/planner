import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
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

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(note: INote): INote {
    const copy: INote = Object.assign({}, note, {
      createdAt: note.createdAt && note.createdAt.isValid() ? note.createdAt.toJSON() : undefined,
      editedAt: note.editedAt && note.editedAt.isValid() ? note.editedAt.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? moment(res.body.createdAt) : undefined;
      res.body.editedAt = res.body.editedAt ? moment(res.body.editedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((note: INote) => {
        note.createdAt = note.createdAt ? moment(note.createdAt) : undefined;
        note.editedAt = note.editedAt ? moment(note.editedAt) : undefined;
      });
    }
    return res;
  }
}
