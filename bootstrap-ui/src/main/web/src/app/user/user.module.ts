import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserRoutingModule } from './user-routing.module';
import { UserIndexComponent } from './containers/user-index/user-index.component';
import { HomeComponent } from './components/home/home.component';
import { SampleDialogComponent } from './dialogs/sample-dialog/sample-dialog.component';
import {NzButtonModule} from "ng-zorro-antd/button";


@NgModule({
  declarations: [
    UserIndexComponent,
    HomeComponent,
    SampleDialogComponent
  ],
    imports: [
        CommonModule,
        UserRoutingModule,
        NzButtonModule
    ]
})
export class UserModule { }
