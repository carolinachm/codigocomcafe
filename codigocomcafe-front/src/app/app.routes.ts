import { Routes } from '@angular/router';

export const routes: Routes = [
    { path: 'dashboard', loadComponent: () => import('./component/dashboard/dashboard').then(c => c.Dashboard) },
    { path: 'categoria', loadComponent: () => import('./component/categoria/categoria').then(c => c.Categoria) },
    { path: 'post', loadComponent: () => import('./component/post/post').then(c => c.Post) },

];
