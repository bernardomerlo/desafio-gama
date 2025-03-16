import { IUser } from 'app/entities/user/user.model';

export interface IMentor {
  id: number;
  nome?: string | null;
  email?: string | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewMentor = Omit<IMentor, 'id'> & { id: null };
