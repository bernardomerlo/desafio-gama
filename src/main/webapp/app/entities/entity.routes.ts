import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'gamaApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'aluno',
    data: { pageTitle: 'gamaApp.aluno.home.title' },
    loadChildren: () => import('./aluno/aluno.routes'),
  },
  {
    path: 'meta',
    data: { pageTitle: 'gamaApp.meta.home.title' },
    loadChildren: () => import('./meta/meta.routes'),
  },
  {
    path: 'mentor',
    data: { pageTitle: 'gamaApp.mentor.home.title' },
    loadChildren: () => import('./mentor/mentor.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
