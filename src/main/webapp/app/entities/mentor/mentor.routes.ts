import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MentorResolve from './route/mentor-routing-resolve.service';

const mentorRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/mentor.component').then(m => m.MentorComponent),
    data: {
      defaultSort: `id,${ASC}`,
      authorities: ['ROLE_ADMIN'],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/mentor-detail.component').then(m => m.MentorDetailComponent),
    resolve: {
      mentor: MentorResolve,
    },
    data: {
      authorities: ['ROLE_ADMIN'],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/mentor-update.component').then(m => m.MentorUpdateComponent),
    resolve: {
      mentor: MentorResolve,
    },
    data: {
      authorities: ['ROLE_ADMIN'],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/mentor-update.component').then(m => m.MentorUpdateComponent),
    resolve: {
      mentor: MentorResolve,
    },
    data: {
      authorities: ['ROLE_ADMIN'],
    },
    canActivate: [UserRouteAccessService],
  },
];

export default mentorRoute;
