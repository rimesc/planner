import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { PlannerSharedModule } from 'app/shared/shared.module';
import { PlannerCoreModule } from 'app/core/core.module';
import { PlannerAppRoutingModule } from './app-routing.module';
import { PlannerHomeModule } from './home/home.module';
import { PlannerEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    PlannerSharedModule,
    PlannerCoreModule,
    PlannerHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    PlannerEntityModule,
    PlannerAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective],
  bootstrap: [MainComponent]
})
export class PlannerAppModule {}
