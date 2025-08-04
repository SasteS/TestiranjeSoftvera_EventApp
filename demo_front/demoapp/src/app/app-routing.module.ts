import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegistrationComponent } from './registration/registration.component';
import { EventManagementComponent } from './event-management/event-management.component';

const routes: Routes = [
  { path: 'register', component: RegistrationComponent },
  { path: 'event-management', component: EventManagementComponent },
  { path: '', redirectTo: '/register', pathMatch: 'full' }
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
