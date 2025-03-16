import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../mentor.test-samples';

import { MentorFormService } from './mentor-form.service';

describe('Mentor Form Service', () => {
  let service: MentorFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MentorFormService);
  });

  describe('Service methods', () => {
    describe('createMentorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMentorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            email: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing IMentor should create a new form with FormGroup', () => {
        const formGroup = service.createMentorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            email: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getMentor', () => {
      it('should return NewMentor for default Mentor initial value', () => {
        const formGroup = service.createMentorFormGroup(sampleWithNewData);

        const mentor = service.getMentor(formGroup) as any;

        expect(mentor).toMatchObject(sampleWithNewData);
      });

      it('should return NewMentor for empty Mentor initial value', () => {
        const formGroup = service.createMentorFormGroup();

        const mentor = service.getMentor(formGroup) as any;

        expect(mentor).toMatchObject({});
      });

      it('should return IMentor', () => {
        const formGroup = service.createMentorFormGroup(sampleWithRequiredData);

        const mentor = service.getMentor(formGroup) as any;

        expect(mentor).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMentor should not enable id FormControl', () => {
        const formGroup = service.createMentorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMentor should disable id FormControl', () => {
        const formGroup = service.createMentorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
