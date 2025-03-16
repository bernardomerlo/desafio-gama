import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMentor } from '../mentor.model';
import { MentorService } from '../service/mentor.service';

const mentorResolve = (route: ActivatedRouteSnapshot): Observable<null | IMentor> => {
  const id = route.params.id;
  if (id) {
    return inject(MentorService)
      .find(id)
      .pipe(
        mergeMap((mentor: HttpResponse<IMentor>) => {
          if (mentor.body) {
            return of(mentor.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default mentorResolve;
