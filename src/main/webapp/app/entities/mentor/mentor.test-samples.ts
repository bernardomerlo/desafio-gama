import { IMentor, NewMentor } from './mentor.model';

export const sampleWithRequiredData: IMentor = {
  id: 6346,
  nome: 'fence lovable',
};

export const sampleWithPartialData: IMentor = {
  id: 7720,
  nome: 'gah sedately flawed',
};

export const sampleWithFullData: IMentor = {
  id: 11719,
  nome: 'voluminous quicker voluminous',
};

export const sampleWithNewData: NewMentor = {
  nome: 'exotic',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
