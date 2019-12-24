import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ThemeService } from 'app/entities/theme/theme.service';
import { ITheme, Theme } from 'app/shared/model/theme.model';
import { Visibility } from 'app/shared/model/enumerations/visibility.model';

describe('Service Tests', () => {
  describe('Theme Service', () => {
    let injector: TestBed;
    let service: ThemeService;
    let httpMock: HttpTestingController;
    let elemDefault: ITheme;
    let expectedResult: ITheme | ITheme[] | boolean | null;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ThemeService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Theme(0, 'AAAAAAA', 'AAAAAAA', 'image/png', 'AAAAAAA', currentDate, Visibility.PUBLIC);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createdAt: currentDate.format(DATE_TIME_FORMAT)
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

      it('should create a Theme', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createdAt: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            createdAt: currentDate
          },
          returnedFromService
        );
        service
          .create(new Theme())
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Theme', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            description: 'BBBBBB',
            avatar: 'BBBBBB',
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            visibility: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdAt: currentDate
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

      it('should return a list of Theme', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            description: 'BBBBBB',
            avatar: 'BBBBBB',
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            visibility: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            createdAt: currentDate
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

      it('should delete a Theme', () => {
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
