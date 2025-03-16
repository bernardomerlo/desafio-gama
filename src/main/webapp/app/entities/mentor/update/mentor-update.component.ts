import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IMentor } from '../mentor.model';
import { MentorService } from '../service/mentor.service';
import { MentorFormGroup, MentorFormService } from './mentor-form.service';

@Component({
  selector: 'jhi-mentor-update',
  templateUrl: './mentor-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MentorUpdateComponent implements OnInit {
  isSaving = false;
  mentor: IMentor | null = null;

  usersSharedCollection: IUser[] = [];

  protected mentorService = inject(MentorService);
  protected mentorFormService = inject(MentorFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MentorFormGroup = this.mentorFormService.createMentorFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mentor }) => {
      this.mentor = mentor;
      if (mentor) {
        this.updateForm(mentor);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mentor = this.mentorFormService.getMentor(this.editForm);
    if (mentor.id !== null) {
      this.subscribeToSaveResponse(this.mentorService.update(mentor));
    } else {
      this.subscribeToSaveResponse(this.mentorService.create(mentor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMentor>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(mentor: IMentor): void {
    this.mentor = mentor;
    this.mentorFormService.resetForm(this.editForm, mentor);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, mentor.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.mentor?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
