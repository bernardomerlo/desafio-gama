export interface IMentor {
  id: number;
  nome?: string | null;
}

export type NewMentor = Omit<IMentor, 'id'> & { id: null };
