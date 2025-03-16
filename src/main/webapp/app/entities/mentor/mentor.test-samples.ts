import { IMentor, NewMentor } from './mentor.model';

export const sampleWithRequiredData: IMentor = {
  id: 6346,
  nome: 'fence lovable',
  email: 'Sophia_Braga@hotmail.com',
};

export const sampleWithPartialData: IMentor = {
  id: 7720,
  nome: 'gah sedately flawed',
  email: 'Nataniel.Moraes14@gmail.com',
};

export const sampleWithFullData: IMentor = {
  id: 11719,
  nome: 'voluminous quicker voluminous',
  email: 'Beatriz98@gmail.com',
};

export const sampleWithNewData: NewMentor = {
  nome: 'exotic',
  email: 'Washington.Batista@gmail.com',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
