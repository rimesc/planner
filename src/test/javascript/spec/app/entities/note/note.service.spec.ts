import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { NoteService } from 'app/entities/note/note.service';
import { INote, Note } from 'app/shared/model/note.model';
import { Visibility } from 'app/shared/model/enumerations/visibility.model';

describe('Service Tests', () => {
  describe('Note Service', () => {
    let injector: TestBed;
    let service: NoteService;
    let httpMock: HttpTestingController;
    let elemDefault: INote;
    let expectedResult: INote | INote[] | boolean | null;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(NoteService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Note(0, 'AAAAAAA', 'AAAAAAA', currentDate, currentDate, Visibility.PUBLIC);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            editedAt: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Note', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            editedAt: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            createdAt: currentDate,
            editedAt: currentDate
          },
          returnedFromService
        );
        service
          .create(new Note())
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Note', () => {
        const returnedFromService = Object.assign(
          {
            markdown: 'BBBBBB',
            html: 'BBBBBB',
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            editedAt: currentDate.format(DATE_TIME_FORMAT),
            visibility: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdAt: currentDate,
            editedAt: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Note', () => {
        const returnedFromService = Object.assign(
          {
            markdown: 'BBBBBB',
            html: 'BBBBBB',
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            editedAt: currentDate.format(DATE_TIME_FORMAT),
            visibility: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            createdAt: currentDate,
            editedAt: currentDate
          },
          returnedFromService
        );
        service
          .query()
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Note', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
