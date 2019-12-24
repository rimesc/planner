/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data } from '@angular/router';

import { PlannerTestModule } from '../../test.module';
import { HomeComponent } from 'app/home/home.component';
import { ThemeService } from 'app/entities/theme/theme.service';
import { Theme } from 'app/shared/model/theme.model';

describe('Component Tests', () => {
  describe('Home Component', () => {
    let comp: HomeComponent;
    let fixture: ComponentFixture<HomeComponent>;
    let service: ThemeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PlannerTestModule],
        declarations: [HomeComponent]
      })
        .overrideTemplate(HomeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HomeComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ThemeService);
    });

    it('should load top themes', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Theme(123)],
            headers
          })
        )
      );

      // WHEN
      comp.loadThemes();

      // THEN
      expect(service.query).toHaveBeenCalledWith({ page: 0, size: 6, sort: ['id,asc'] });
      expect(comp.themes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
