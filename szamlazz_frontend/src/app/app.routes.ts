import { Routes } from '@angular/router';
import { HomePage } from './pages/home-page/home-page';
import { DetailsPage } from './pages/details-page/details-page';
import { CreatePage } from './pages/create-page/create-page';

export const routes: Routes = [
    { path: '', redirectTo: 'home', pathMatch: 'full' },
    { path: 'home', component: HomePage },
    { path: 'details/:hivasAzonosito', component: DetailsPage },
    { path: 'create', component: CreatePage },
    { path: '**', redirectTo: 'home' }
];
