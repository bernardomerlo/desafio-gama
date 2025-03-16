import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { MentorService } from '../service/mentor.service';
import { IMentor } from '../mentor.model';
import { MentorFormService } from './mentor-form.service';

import { MentorUpdateComponent } from './mentor-update.component';

describe('Mentor Management Update Component', () => {
  let comp: MentorUpdateComponent;
  let fixture: ComponentFixture<MentorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let mentorFormService: MentorFormService;
  let mentorService: MentorService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MentorUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MentorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MentorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    mentorFormService = TestBed.inject(MentorFormService);
    mentorService = TestBed.inject(MentorService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const mentor: IMentor = { id: 17473 };
      const user: IUser = { id: 3944 };
      mentor.user = user;

      const userCollection: IUser[] = [{ id: 3944 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ mentor });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const mentor: IMentor = { id: 17473 };
      const user: IUser = { id: 3944 };
      mentor.user = user;

      activatedRoute.data = of({ mentor });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContainEqual(user);
      expect(comp.mentor).toEqual(mentor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMentor>>();
      const mentor = { id: 15712 };
      jest.spyOn(mentorFormService, 'getMentor').mockReturnValue(mentor);
      jest.spyOn(mentorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mentor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mentor }));
      saveSubject.complete();

      // THEN
      expect(mentorFormService.getMentor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(mentorService.update).toHaveBeenCalledWith(expect.objectContaining(mentor));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMentor>>();
      const mentor = { id: 15712 };
      jest.spyOn(mentorFormService, 'getMentor').mockReturnValue({ id: null });
      jest.spyOn(mentorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mentor: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mentor }));
      saveSubject.complete();

      // THEN
      expect(mentorFormService.getMentor).toHaveBeenCalled();
      expect(mentorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMentor>>();
      const mentor = { id: 15712 };
      jest.spyOn(mentorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mentor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(mentorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 3944 };
        const entity2 = { id: 6275 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
