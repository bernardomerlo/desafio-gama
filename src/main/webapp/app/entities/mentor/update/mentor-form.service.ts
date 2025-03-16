import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMentor, NewMentor } from '../mentor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMentor for edit and NewMentorFormGroupInput for create.
 */
type MentorFormGroupInput = IMentor | PartialWithRequiredKeyOf<NewMentor>;

type MentorFormDefaults = Pick<NewMentor, 'id'>;

type MentorFormGroupContent = {
  id: FormControl<IMentor['id'] | NewMentor['id']>;
  nome: FormControl<IMentor['nome']>;
  email: FormControl<IMentor['email']>;
  user: FormControl<IMentor['user']>;
};

export type MentorFormGroup = FormGroup<MentorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MentorFormService {
  createMentorFormGroup(mentor: MentorFormGroupInput = { id: null }): MentorFormGroup {
    const mentorRawValue = {
      ...this.getFormDefaults(),
      ...mentor,
    };
    return new FormGroup<MentorFormGroupContent>({
      id: new FormControl(
        { value: mentorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(mentorRawValue.nome, {
        validators: [Validators.required],
      }),
      email: new FormControl(mentorRawValue.email, {
        validators: [Validators.required],
      }),
      user: new FormControl(mentorRawValue.user),
    });
  }

  getMentor(form: MentorFormGroup): IMentor | NewMentor {
    return form.getRawValue() as IMentor | NewMentor;
  }

  resetForm(form: MentorFormGroup, mentor: MentorFormGroupInput): void {
    const mentorRawValue = { ...this.getFormDefaults(), ...mentor };
    form.reset(
      {
        ...mentorRawValue,
        id: { value: mentorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MentorFormDefaults {
    return {
      id: null,
    };
  }
}
