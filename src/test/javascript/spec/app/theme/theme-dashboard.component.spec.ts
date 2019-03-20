/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data } from '@angular/router';

import { PlannerTestModule } from '../../test.module';
import { ThemeDashboardComponent } from 'app/theme/theme-dashboard.component';
import { GoalService } from 'app/entities/goal/goal.service';
import { Goal } from 'app/shared/model/goal.model';
import { Theme } from 'app/shared/model/theme.model';

describe('Component Tests', () => {
  describe('Theme Dashboard Component', () => {
    let comp: ThemeDashboardComponent;
    let fixture: ComponentFixture<ThemeDashboardComponent>;
    const route = ({ data: of({ theme: new Theme(123) }) } as any) as ActivatedRoute;
    let service: GoalService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PlannerTestModule],
        declarations: [ThemeDashboardComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ThemeDashboardComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ThemeDashboardComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GoalService);
    });

    it('Should call load goals on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Goal(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalledWith({ 'themeId.equals': 123, sort: ['order,asc'] });
      expect(comp.goals[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
