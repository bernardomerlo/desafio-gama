import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MentorDetailComponent } from './mentor-detail.component';

describe('Mentor Management Detail Component', () => {
  let comp: MentorDetailComponent;
  let fixture: ComponentFixture<MentorDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MentorDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./mentor-detail.component').then(m => m.MentorDetailComponent),
              resolve: { mentor: () => of({ id: 15712 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MentorDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MentorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load mentor on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MentorDetailComponent);

      // THEN
      expect(instance.mentor()).toEqual(expect.objectContaining({ id: 15712 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
