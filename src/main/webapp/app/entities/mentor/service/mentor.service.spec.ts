import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMentor } from '../mentor.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../mentor.test-samples';

import { MentorService } from './mentor.service';

const requireRestSample: IMentor = {
  ...sampleWithRequiredData,
};

describe('Mentor Service', () => {
  let service: MentorService;
  let httpMock: HttpTestingController;
  let expectedResult: IMentor | IMentor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MentorService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Mentor', () => {
      const mentor = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(mentor).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Mentor', () => {
      const mentor = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(mentor).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Mentor', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Mentor', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Mentor', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMentorToCollectionIfMissing', () => {
      it('should add a Mentor to an empty array', () => {
        const mentor: IMentor = sampleWithRequiredData;
        expectedResult = service.addMentorToCollectionIfMissing([], mentor);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mentor);
      });

      it('should not add a Mentor to an array that contains it', () => {
        const mentor: IMentor = sampleWithRequiredData;
        const mentorCollection: IMentor[] = [
          {
            ...mentor,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMentorToCollectionIfMissing(mentorCollection, mentor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Mentor to an array that doesn't contain it", () => {
        const mentor: IMentor = sampleWithRequiredData;
        const mentorCollection: IMentor[] = [sampleWithPartialData];
        expectedResult = service.addMentorToCollectionIfMissing(mentorCollection, mentor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mentor);
      });

      it('should add only unique Mentor to an array', () => {
        const mentorArray: IMentor[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const mentorCollection: IMentor[] = [sampleWithRequiredData];
        expectedResult = service.addMentorToCollectionIfMissing(mentorCollection, ...mentorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const mentor: IMentor = sampleWithRequiredData;
        const mentor2: IMentor = sampleWithPartialData;
        expectedResult = service.addMentorToCollectionIfMissing([], mentor, mentor2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mentor);
        expect(expectedResult).toContain(mentor2);
      });

      it('should accept null and undefined values', () => {
        const mentor: IMentor = sampleWithRequiredData;
        expectedResult = service.addMentorToCollectionIfMissing([], null, mentor, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mentor);
      });

      it('should return initial array if no Mentor is added', () => {
        const mentorCollection: IMentor[] = [sampleWithRequiredData];
        expectedResult = service.addMentorToCollectionIfMissing(mentorCollection, undefined, null);
        expect(expectedResult).toEqual(mentorCollection);
      });
    });

    describe('compareMentor', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMentor(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 15712 };
        const entity2 = null;

        const compareResult1 = service.compareMentor(entity1, entity2);
        const compareResult2 = service.compareMentor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 15712 };
        const entity2 = { id: 17473 };

        const compareResult1 = service.compareMentor(entity1, entity2);
        const compareResult2 = service.compareMentor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 15712 };
        const entity2 = { id: 15712 };

        const compareResult1 = service.compareMentor(entity1, entity2);
        const compareResult2 = service.compareMentor(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
