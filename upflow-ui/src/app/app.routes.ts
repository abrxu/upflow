import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { UserCreateComponent } from './pages/user-create/user-create.component';

export const routes: Routes = [

  {
    path: 'login',
    component: LoginComponent
  },

  {
    path: 'users/create',
    component: UserCreateComponent
  },

  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  }

];