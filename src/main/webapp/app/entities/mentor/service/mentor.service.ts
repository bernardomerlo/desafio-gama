import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMentor, NewMentor } from '../mentor.model';

export type PartialUpdateMentor = Partial<IMentor> & Pick<IMentor, 'id'>;

export type EntityResponseType = HttpResponse<IMentor>;
export type EntityArrayResponseType = HttpResponse<IMentor[]>;

@Injectable({ providedIn: 'root' })
export class MentorService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mentors');

  create(mentor: NewMentor): Observable<EntityResponseType> {
    return this.http.post<IMentor>(this.resourceUrl, mentor, { observe: 'response' });
  }

  update(mentor: IMentor): Observable<EntityResponseType> {
    return this.http.put<IMentor>(`${this.resourceUrl}/${this.getMentorIdentifier(mentor)}`, mentor, { observe: 'response' });
  }

  partialUpdate(mentor: PartialUpdateMentor): Observable<EntityResponseType> {
    return this.http.patch<IMentor>(`${this.resourceUrl}/${this.getMentorIdentifier(mentor)}`, mentor, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMentor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMentor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMentorIdentifier(mentor: Pick<IMentor, 'id'>): number {
    return mentor.id;
  }

  compareMentor(o1: Pick<IMentor, 'id'> | null, o2: Pick<IMentor, 'id'> | null): boolean {
    return o1 && o2 ? this.getMentorIdentifier(o1) === this.getMentorIdentifier(o2) : o1 === o2;
  }

  addMentorToCollectionIfMissing<Type extends Pick<IMentor, 'id'>>(
    mentorCollection: Type[],
    ...mentorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const mentors: Type[] = mentorsToCheck.filter(isPresent);
    if (mentors.length > 0) {
      const mentorCollectionIdentifiers = mentorCollection.map(mentorItem => this.getMentorIdentifier(mentorItem));
      const mentorsToAdd = mentors.filter(mentorItem => {
        const mentorIdentifier = this.getMentorIdentifier(mentorItem);
        if (mentorCollectionIdentifiers.includes(mentorIdentifier)) {
          return false;
        }
        mentorCollectionIdentifiers.push(mentorIdentifier);
        return true;
      });
      return [...mentorsToAdd, ...mentorCollection];
    }
    return mentorCollection;
  }
}
