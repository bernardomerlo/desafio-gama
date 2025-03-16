import { IMentor } from 'app/entities/mentor/mentor.model';

export interface IAluno {
  id: number;
  nome?: string | null;
  mentor?: IMentor | null;
}

export type NewAluno = Omit<IAluno, 'id'> & { id: null };
